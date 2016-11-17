<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"%>
<%@ include file="../taglib.jsp" %>


<template:mainLayout rootURL="${rootURL}" username="${username}"
					 contextPath="${pageContext.request.contextPath}" title="Nova monografia"
					 anonymous="${UsuarioController.getCurrentUser()==null}">

	<jsp:attribute name="content">
		<div>
			<form id="form" enctype="multipart/form-data" method="POST" action="/monografias/salvar/">
                <input type="hidden" name="id" value="${monografia.id}">
				<div class="form-group" id="autoresDiv">
					<label for="autores">Autores</label>
					<input type="hidden" id="autores" name="autores">
					<c:forEach var="autor" items="${monografia.autores}">
                        <input type="text" class="form-control" data-provide="typeahead" value="${autor}">
                    </c:forEach>
					<input type="text" class="form-control">
				</div>
				<div class="form-group">
					<label for="titulo">Título</label>
					<input type="text" class="form-control" id="titulo" name="titulo" value="${monografia.titulo}">
				</div>
				<div class="form-group">
					<label for="orientador">Orientador</label>
					<input type="text" class="form-control" name="orientador" id="orientador" value="${monografia.orientador}">
				</div>
				<div class="form-group">
					<label for="presidenteBanca">Presidente da banca</label>
					<input type="text" class="form-control" name="presidenteBanca" id="presidenteBanca" value="${monografia.presidenteBanca}">
				</div>
				<div class="form-group">
					<label for="membrosBanca">Membros da banca</label>
					<input type="hidden" id="membrosBanca" name="membrosBanca">
                    <c:forEach var="membro" items="${monografia.membrosBanca}">
                        <input type="text" class="form-control" value="${membro}">
                    </c:forEach>
					<input type="text" class="form-control">
				</div>
				<div class="form-group">
					<label for="resumoPortugues">Resumo em português</label>
					<textarea class="form-control" name="resumoPortugues" id="resumoPortugues" value="${monografia.resumoPortugues}"></textarea>
				</div>
				<div class="form-group">
					<label for="resumoLinguaEstrangeira">Resumo em língua estrangeira</label>
					<textarea class="form-control" name="resumoLinguaEstrangeira" id="resumoLinguaEstrangeira" value="${monografia.resumoLinguaEstrangeira}"></textarea>
				</div>
				<div id="formArquivos" class="form-group">
					<label>Arquivos</label>
					<input type="hidden" id="arquivosRemovidos" name="arquivosRemovidos" value="" />
					<div id="arquivosExistentes">
                        <c:forEach var="arquivo" items="${monografia.arquivos}">
                            <div>
                                <a href="#" class="removerArquivo">
                                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                </a>
                                <a href="<c:url value='/monografias/download/'>
                                    <c:param name="id" value="${monografia.id}"></c:param>
                                    <c:param name="arquivo" value="${arquivo.nome}"></c:param>
                                </c:url>">
                                        ${arquivo.nome} (${arquivo.tamanhoLegivel})
                                </a>
                            </div>
                        </c:forEach>
					</div>
					<div class="uploadArquivo">
						<a style="display:none" href="#"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
						<input type="file" style="display: inline;">
					</div>
				</div>
				<button type="submit" class="btn btn-default">Salvar</button>
			</form>
		</div>

		<script src="<c:url value='/resources/monografias/typeahead.js' />"></script>
		<script>
			$(function(){
				var membroBancaDiv = $("#membrosBanca").parent();
				var autoresDiv = $("#autoresDiv");
				var membroInput = function(e){
					var $this = $(this);
					var parent = $this.parent();
					if(this == parent.find("input:last")[0]){
						if($this.val() != ""){
							var newInput = $('<input type="text" class="form-control">');
							newInput.on("input", membroInput);
							if($this.parent().attr("id") == "autoresDiv"){
								newInput.typeahead({
									source: function(callback){
										$.getJSON("/rest/alunos/", {q: $this.val().toUpperCase()}, callback);
									},
									autoSelect: true
								});
							}
							parent.append(newInput);
						}
					} else {
						if($this.val() == ""){
							$this.remove();
						}
					}
				};
				membroBancaDiv.find("input[type='text']").on("input", membroInput);
				autoresDiv.find("input[type='text']").on("input", membroInput);
				autoresDiv.find("input[type='text']").each(function(){
					var $this = $(this);
					$this.typeahead({
						source: function(query, callback){
							$.getJSON("/rest/alunos/", {q: query.toUpperCase()}, callback);
						},
						autoSelect: true
					});
				});
				$("#form").submit(function(){
					$("#membrosBanca").val(
						membroBancaDiv.find("input[type='text']:not(:last)").map(function(){
							return $(this).val();
						}).toArray().join("\n")
					);
					$("#autores").val(
							autoresDiv.find("input[type='text']:not(:last)").map(function(){
								return $(this).val();
							}).toArray().join("\n")
					);
					$(".uploadArquivo input").each(function(i, input){
						$(input).attr("name", "arquivos[" + i + "]");
					});
				});

				var arquivosRemovidosInput = $("#arquivosRemovidos");
				$(".removerArquivo").on("click", function(e){
					e.preventDefault();
					var parent = $(this).parent();
					var name = parent.find("a:last").text().trim();
					var arquivosRemovidos = arquivosRemovidosInput.val();

					if(arquivosRemovidos != ""){
						arquivosRemovidosInput.val(name);
					} else {
						arquivosRemovidosInput.val(arquivosRemovidos + "\n" + name);
					}

					parent.remove();
					return false;
				});

				var newFileHandler = function(){
					if(this == $(".uploadArquivo:last input")[0]){
						$(".uploadArquivo:last a").css("display", "inline").on("click", function(e){
							e.preventDefault();
							$(this).parent().remove();
							return false;
						});
						$("#formArquivos").append(
								'<div class="uploadArquivo">'
								+'<a style="display:none" href="#"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>'+
								'<input type="file" style="display: inline;">' +
								'</div>');
						$(".uploadArquivo:last input").on("input", newFileHandler);
					}
				};
				$(".uploadArquivo:last input").on("input", newFileHandler);

			});
		</script>
	</jsp:attribute>
</template:mainLayout>