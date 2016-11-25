<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Isenção de Disciplinas</title>

<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
	rel="stylesheet">
<link href="<c:url value='/resources/bootstrap/css/bootstrap.css' />"
	rel="stylesheet" />
<link href="<c:url value='/resources/css/usuarios.css' />"
	rel="stylesheet" />
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<script>
	jQuery(document).ready(function($) {
		$("#formAnaliseItemPedidoIsencao").submit(function(event) {
			// Disable the search button
			enableSearchButton(false);
			// Prevent the form from submitting via the browser.
			event.preventDefault();
		});
	});

	jQuery(document).ready(function($) {
		$("#formIndeferimentoItem").submit(function(event) {
			// Disable the search button
			enableSearchButton(false);
			// Prevent the form from submitting via the browser.
			event.preventDefault();
		});
	});

	function registraDeferimentoItem(idItem) {
// 			alert("ok30");
			var search = {}
			search["matriculaProfessor"] = String("${professor.matricula}");
			search["matriculaAluno"] = String("${aluno.matricula}");
			search["status"] = String($("#status").val());
			search["startDate"] = $('#startDate').val();
			search["novaSituacao"] =  'DEFERIDO';
			search["idItemPedidoIsencao"] = String(idItem);
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${pageContext.request.contextPath}/isencaoDisciplina/analisaItemPedidoIsencao",
				data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);
					$("#statusError").html("");
					display(data, idItem);},
				error : function(e) {
					console.log("ERROR: ", e);
					$("#statusError").html("Registro de atividade complementar não pode ser deferido! Carga horária máxima atingida!");}
			});	
	}

	function registraIndeferimentoItem() {
// 		alert("ok50");
		var idItem = idItemPedidoIsencao;
// 		alert(idItem);
		
// 		alert($(".modal-body #motivo").val());
		
		var search = {}
		search["matriculaProfessor"] = String("${professor.matricula}");
		search["matriculaAluno"] = String("${aluno.matricula}");
		search["status"] = String($("#status").val());
		search["startDate"] = $('#startDate').val();
		search["novaSituacao"] =  'INDEFERIDO';
		search["idItemPedidoIsencao"] = String(idItem);
 		search["motivoIndeferimento"] = String($(".modal-body #motivo").val());
 		search["motivoIndeferimento"] = search["motivoIndeferimento"] + ' - ' + String($(".modal-body #justificativaIndeferimento").val());
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "${pageContext.request.contextPath}/isencaoDisciplina/analisaItemPedidoIsencao",
			data : JSON.stringify(search),
			dataType : 'json',
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				$("#statusError").html("");
				display(data, idItem, search["motivoIndeferimento"]);
				$("#modalIndeferirItem").modal('hide');},
			error : function(e) {
				console.log("ERROR: ", e);
				$("#statusError").html("Registro de atividade complementar não pode ser deferido! Carga horária máxima atingida!");}
		});	
	}
</script>

<script>
	function enableSearchButton(flag) {
		$("#btn-regLotacao").prop("disabled", flag);
	}

	function display(data, idItem, motivoIndeferimento) {
// 		alert('display');
// 		alert(data);
// 		alert(idItem);
		if(data.length == 0){
			$("#" + idItem).html("Não há itens de pedido de isenção!");
		}
		else{
			var div_data = "<div id='${" + idItem + "}' class='status text-center'>";
			if(data == 'DEFERIDO') {
				div_data = div_data + "<p class='text-success'>";}
			if(data == 'INDEFERIDO') {
				div_data = div_data + "<p class='text-danger'>";
			}
			div_data = div_data + "<b>" + data + "</b>";
			div_data = div_data + "</p>";
			if(data == 'INDEFERIDO') {
				div_data = div_data + "<p><b>Motivo do indeferimento: </b>";
				div_data = div_data + motivoIndeferimento;
				div_data = div_data + "</p>";
			}
			div_data = div_data + "</div>";      		
			$("#" + idItem).html(div_data);				
		}
	}
</script>

<script>
	var idItemPedidoIsencao;
	 
	$(document).on("click", ".open-formIndeferimentoItem", function () {
// 		alert('ok80');
	    idItemPedidoIsencao = $(this).data('id');
// 		alert(idItemPedidoIsencao);
	});

</script>

</head>
<body class="lista-solicitacoes">
	<div class="container">

		<div class="row text-center">
			<h3>Isenção de Disciplinas</h3>
		</div>
		<hr />
		<div class="row">
			<h5>
				<b>Aluno:</b>
				<c:out value="${requestScope.aluno.nome}"></c:out>
				(Matrícula:
				<c:out value="${requestScope.aluno.matricula}"></c:out>
				)
			</h5>
			<h5>
				<b>Curso:</b>
				<c:out value="${requestScope.aluno.versaoCurso.curso.sigla}"></c:out>
				-
				<c:out value="${requestScope.aluno.versaoCurso.curso.nome}"></c:out>
				(Grade:
				<c:out value="${requestScope.aluno.versaoCurso}"></c:out>
				)
			</h5>
		</div>
		<br />
		
		<c:if test="${requestScope.sucesso != null}">
			<div class="row text-center">
				<span class="label label-success">${requestScope.sucesso}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.error != null}">
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<div class="row">
			<form id="formAnaliseItemPedidoIsencao">

				<c:forEach items="${itensPedidoIsencao}" var="item">
					<div class="well">
						<div class="dados-aluno">
							<p>
								<b>Disciplina solicitada:</b> ${item.disciplina.codigo} -
								${item.disciplina.nome}
							</p>
							<p>
								<b>Disciplina externa:</b> ${item.descritorDisciplinaExterna}
							</p>
							<p>
								<b>Observações do solicitante:</b> ${item.observacao}
							</p>
						</div>

						<div id="${item.id}" class="status text-center">
							<c:choose>
								<c:when test="${item.situacao eq 'INDEFINIDO'}">

									<button class="btn btn-success"
										onclick='registraDeferimentoItem(${item.id});'>
										<i class="fa fa-check"></i> Deferir
									</button>

									<button type="button"
										class="open-formIndeferimentoItem btn btn-danger"
										data-toggle="modal" data-id="${item.id}"
										data-target="#modalIndeferirItem">
										<i class="fa fa-times"></i> Indeferir
									</button>
								</c:when>
								<c:otherwise>
									<c:set var="classeStatus" scope="page">
										<c:choose>
											<c:when test="${item.situacao eq 'INDEFERIDO'}">
												text-danger
											</c:when>
											<c:when test="${item.situacao eq 'DEFERIDO'}">
												text-success
											</c:when>
										</c:choose>
									</c:set>
									<p class="${classeStatus}">
										<b>${item.situacao}</b>
									</p>
									<c:if test="${item.situacao == 'INDEFERIDO'}">
										<p>
											<b>Motivo do indeferimento: </b> ${item.motivoIndeferimento}
										</p>
									</c:if>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>
			</form>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/isencaoDisciplina/visualizaAlunosSolicitantes">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>


	<!-- Modal Indeferir Item de Isenção -->
	<form:form id="formIndeferimentoItem">
		<div class="modal fade" id="modalIndeferirItem" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Justificativa do
							Indeferimento</h4>
					</div>

					<div class="modal-body">

						<div class="dropdown" align="center">
							<select id="motivo" name="motivo" class="form-control" required>
								<option value="" class="form-control" label="Selecionar"
									selected disabled>Selecionar</option>
								<option value="Carga horária insuficiente">Carga
									horária insuficiente</option>
								<option value="Ausência de programa">Ausência de
									programa</option>
								<option value="Programa insuficiente">Programa
									insuficiente</option>
								<option value="Disciplina não cursada">Disciplina não
									cursada</option>
								<option value="Disciplina sem registro no histórico escolar">Disciplina
									sem registro no histórico escolar</option>
								<option value="Disciplina sem aprovação">Disciplina sem
									aprovação</option>
								<option
									value="Conteúdo programático já utilizado em outra disciplina">Conteúdo
									programático já utilizado em outra disciplina</option>
								<option value="Outros">Outros</option>
							</select> <br>
						</div>

						<hr />
						<div class="form-group">
							<label for="justificativaIndeferimento">Opcionalmente,
								forneça um texto de justificativa para o indeferimento:</label>

							<textarea name="justificativaIndeferimento" class="form-control"
								rows="6" id="justificativaIndeferimento" maxlength="500"
								placeholder="(opcional, max 500 caracteres)"></textarea>

							<h5>
								Faltam <span id="max"></span> caracteres.
							</h5>
						</div>
					</div>

					<div class="modal-footer">
						<input type="hidden" name="bookId" id="bookId" value="" />
						<!-- 						<button class="btn btn-default" id="submit">OK</button> -->
						<button class="btn btn-default"
							onclick='registraIndeferimentoItem();'>OK</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>