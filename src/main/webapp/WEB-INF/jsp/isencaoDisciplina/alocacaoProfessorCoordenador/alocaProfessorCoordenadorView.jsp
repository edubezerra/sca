<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Lista de Usuários</title>

<link href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
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
		var coordenacao = {};
		
		function editarCoordenador(siglaCurso,select) {
			var optionSelected = $(select).find("option:selected");
            var matriculaCoord  = optionSelected.val();
            
            coordenacao[siglaCurso] = matriculaCoord;
		}
	</script>
	
	<script>
		jQuery(document).ready(function($) {	
			$("#formCoordenacaoAtividades").submit(function(event) {	
				// Disable the search button
				enableSearchButton(false);
				// Prevent the form from submitting via the browser.
				event.preventDefault();	
				registrarCoordenadoresAtividade();	
			});
		});
	
		function registrarCoordenadoresAtividade() {	
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${pageContext.request.contextPath}/coordenacaoAtividades/alocaCoordenadoresAtividades",
				data : JSON.stringify(coordenacao),
				dataType : 'text',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS");
					$("#feedback").html("<b>"+data+"</b>");
					setTimeout(function(){
						window.location.reload(true);
				    }, 2000);
					
				},
				error : function(e) {
					console.log("ERROR: ", e);
					$("#feedback").html(JSON.stringify(e));},
				done : function(e) {
					console.log("DONE");
					enableSearchButton(false);}
			});			
		}
	</script>
	
	<script>
		function enableSearchButton(flag) {
			$("#btn-regCoord").prop("disabled", flag);
		}
	</script>

</head>

<body class="lista-solicitacoes">

	<div class="container">
		<div class="row text-center">
			<h3>Alocação de Coordenadores de Atividades Complementares</h3>
		</div>
		<hr/>
		<c:if test="${requestScope.sucesso != null}">
			<div class="row">
				<span id="sucesso" class="label label-success">${requestScope.sucesso}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.error != null}">
			<div class="row">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.info != null}">
			<div class="row">
				<span class="label label-info">${requestScope.info}</span>
			</div>
		</c:if>
		
		<div id="feedback" class="row label-success text-center"></div>
		
		<div class="row">
			<form id="formCoordenacaoAtividades">
          		<table class="table table-hover">
          			<thead>
						<tr>
							<th>Sigla</th>
							<th>Curso</th>
							<th>Coordenador de Atividade</th>
						</tr>
					</thead>
					<tbody>

						<c:forEach varStatus="i" items="${requestScope.dadosCoordenacaoAtividades.cursos}" var="curso">
							<input name="siglaCurso" value="${curso.key}" type="hidden" />
							<tr>
								<td>${curso.key}</td>
								<td>${curso.value}</td>
								
								<td><select name="coordenadores" class="form-control input" onchange="editarCoordenador('${curso.key}',this)">						 		
								 		
								 		<c:forEach items="${requestScope.dadosCoordenacaoAtividades.coordenacaoCursoProf}" var="coordenacao">
											<c:if test="${coordenacao.key == curso.key}">
												<c:if test="${coordenacao.value != null}">
													<option value="${coordenacao.value}" label="" selected disabled>${requestScope.dadosCoordenacaoAtividades.professores[coordenacao.value]} (Matrícula:${coordenacao.value})</option>
												</c:if>
												<c:if test="${coordenacao.value == null}">
													<option value="" class="form-control" label="Selecionar..."
														selected disabled>Selecionar</option>
												</c:if>
											</c:if>
										</c:forEach>
										
										<c:forEach items="${requestScope.dadosCoordenacaoAtividades.professores}" var="professor">
											<option value="${professor.key}">${professor.value} (Matrícula:${professor.key})</option>
										</c:forEach>
									</select>
								</td>
							</tr>
						</c:forEach>
					</tbody>
          		</table>
          		<div class="text-center">
          			<button id="btn-regCoord" type="submit" class="btn btn-primary" title="Buscar registros">Registrar Coordenadores</button>
          		</div>
			</form>
		</div>		
		
		<a class="btn btn-default" href="${pageContext.request.contextPath}/coordenacaoAtividades/menuPrincipal">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
		
	</div>
	
</body>
</html>