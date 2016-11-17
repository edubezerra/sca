<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Lotação de Professores</title>

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
	var lotacao = {};

	function editarLotacao(matriculaProfessor, select) {
		var optionSelected = $(select).find("option:selected");
		var siglaDepto = optionSelected.val();

		lotacao[matriculaProfessor] = siglaDepto;
	}
</script>

<script>
	jQuery(document).ready(function($) {
		$("#formLotacaoProfessor").submit(function(event) {
			// Disable the search button
			enableSearchButton(false);
			// Prevent the form from submitting via the browser.
			event.preventDefault();
			registrarLotacaoProfessor();
		});
	});

	function registrarLotacaoProfessor() {
		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					url : "${pageContext.request.contextPath}/alocacaoProfessorDepartamento/alocaProfessores",
					data : JSON.stringify(lotacao),
					dataType : 'text',
					timeout : 100000,
					success : function(data) {
						console.log("SUCCESS");
						$("#feedback").html("<b>" + data + "</b>");
						setTimeout(function() {
							window.location.reload(true);
						}, 2000);

					},
					error : function(e) {
						console.log("ERROR: ", e);
						$("#feedback").html(JSON.stringify(e));
					},
					done : function(e) {
						console.log("DONE");
						enableSearchButton(false);
					}
				});
	}
</script>

<script>
	function enableSearchButton(flag) {
		$("#btn-regLotacao").prop("disabled", flag);
	}
</script>

</head>

<body class="lista-solicitacoes">

	<div class="container">
		<div class="row text-center">
			<h3>Lotação de Professores em Departamentos</h3>
		</div>
		<hr />
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
			<form id="formLotacaoProfessor">
				<!-- Default panel contents -->
				<div class="panel-heading">
					<h3>Lista de Professores</h3>
				</div>
				<table class="table table-hover">
					<thead>
						<tr>
							<th>Nome</th>
							<th>Login</th>
							<th>Email</th>
							<th>Departamento</th>
						</tr>
					</thead>

					<tbody>

						<c:forEach varStatus="i" items="${professores}" var="professor">
							<input name="matricula" value="${professor.matricula}-${i.index}"
								type="hidden" />
							<tr>
								<td>${professor.pessoa.nome}</td>
								<td>${professor.matricula}</td>
								<td>${professor.pessoa.email}</td>

								<td><select name="departamento" class="form-control input"
									onchange="editarLotacao('${professor.matricula}',this)">

										<c:forEach items="${lotacoes}" var="lotacao">
											<c:if test="${lotacao.key == professor.matricula}">
												<c:if test="${lotacao.value != null}">
													<option value="${lotacao.value}" label="" selected disabled>${requestScope.departamentos[lotacao.value]}</option>
												</c:if>
												<c:if test="${lotacao.value == null}">
													<option value="" class="form-control" label="Selecionar..."
														selected disabled>Selecionar</option>
												</c:if>
											</c:if>
										</c:forEach>
										<c:forEach items="${departamentos}" var="departamento">
											<option value="${departamento.key}">${departamento.value}</option>
										</c:forEach>
								</select></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<div class="text-center">
					<button id="btn-regLotacao" type="submit" class="btn btn-primary"
						title="Buscar registros">Registrar Lotações</button>
				</div>
			</form>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>
</body>
</html>