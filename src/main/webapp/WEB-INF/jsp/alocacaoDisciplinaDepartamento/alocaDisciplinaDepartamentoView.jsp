<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Alocação de Disciplina</title>

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

	function editarLotacao(idDisciplina, select) {
		var optionSelected = $(select).find("option:selected");
		var siglaDepto = optionSelected.val();

		lotacao[idDisciplina] = siglaDepto;

		var associativeArray = {};
		associativeArray["idDisciplina"] = idDisciplina;
		associativeArray["siglaDepartamento"] = siglaDepto;

		var mystring = JSON.stringify(associativeArray);

		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					url : "${pageContext.request.contextPath}/alocacaoDisciplinaDepartamento/alocaDisciplinaEmDepartamento",
					data : JSON.stringify(associativeArray),
					dataType : 'text',
					timeout : 100000,
					success : function(data) {
						console.log("SUCCESS");
						
						var codigo = $( "#tabelaAlocacoes" ).find("#codigo" + idDisciplina).text();
						$( "#tabelaAlocacoes" ).find("#codigo" + idDisciplina).html(codigo);
						
						var nome = $( "#tabelaAlocacoes" ).find("#nome" + idDisciplina).text();
						$( "#tabelaAlocacoes" ).find("#nome" + idDisciplina).html(nome);
						
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

	function listarDisciplinas(select) {
		alert('listarDisciplinas1');

		var optionSelected = $(select).find("option:selected");

		var idVersaoCurso = optionSelected.val();

		alert(idVersaoCurso);

		alert('listarDisciplinas4');

		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					url : "${pageContext.request.contextPath}/alocacaoDisciplinaDepartamento/filtraDisciplinas",
					data : JSON.stringify(idVersaoCurso),
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
	jQuery(document).ready(function($) {
		$("#formLotacaoDisciplina").submit(function(event) {
			// Disable the search button
			enableSearchButton(false);
			// Prevent the form from submitting via the browser.
			event.preventDefault();
			registrarAlocacaoDisciplina();
		});
	});

	function registrarAlocacaoDisciplina() {
		$
				.ajax({
					type : "POST",
					contentType : "application/json",
					url : "${pageContext.request.contextPath}/alocacaoDisciplinaDepartamento/alocaDisciplinas",
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

<body class="lista-solicitacoes" onload="teste()">

	<div class="container">
		<div class="row text-center">
			<h3>Alocação de Disciplinas em Departamentos</h3>
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
			<form id="formSelecaoVersaoCurso">
				<select id="versoesCurso" class="form-control input">
					<option value="" class="form-control"
						label="Selecionar versão de curso..." selected disabled>Selecionar
						versão de curso</option>
					<c:forEach items="${versoesCurso}" var="versaoCurso">
						<option value="${versaoCurso.id}">${versaoCurso.curso.nome},
							versão ${versaoCurso.numero}</option>
					</c:forEach>
				</select>
			</form>
		</div>

		<div class="row">
			<form id="formLotacaoDisciplina">
				<!-- Default panel contents -->
				<div class="panel-heading">
					<h3>Lista de Disciplinas</h3>
				</div>
				<table id='tabelaAlocacoes' class="table table-hover">
					<thead>
						<tr>
							<th>Código</th>
							<th>Nome</th>
							<th>Departamento</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach varStatus="i" items="${disciplinas}" var="disciplina">
							<tr>
								<td style="display: none;">${disciplina.versaoCurso.id}</td>

								<c:if test="${lotacoes[disciplina.id] != null}">
									<td>${disciplina.codigo}</td>
									<td>${disciplina.nome}</td>
								</c:if>
								<c:if test="${lotacoes[disciplina.id] == null}">
									<td id=codigo${disciplina.id}><font color="red">${disciplina.codigo}</font></td>
									<td id=nome${disciplina.id}><font color="red">${disciplina.nome}</font></td>
								</c:if>

								<td><select name="departamento" class="form-control input"
									onchange="editarLotacao('${disciplina.id}',this)">

										<c:if test="${lotacoes[disciplina.id] != null}">
											<option value="${lotacoes[disciplina.id]}" label="" selected
												disabled>${requestScope.departamentos[lotacoes[disciplina.id]]}</option>
										</c:if>
										<c:if test="${lotacoes[disciplina.id] == null}">
											<option value="" class="form-control" label="Selecionar..."
												selected disabled>Selecionar</option>
										</c:if>

										<c:forEach items="${departamentos}" var="departamento">
											<option value="${departamento.key}">${departamento.value}</option>
										</c:forEach>
								</select></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
<!-- 				<div class="text-center"> -->
<!-- 					<button id="btn-regLotacao" type="submit" class="btn btn-primary" -->
<!-- 						title="Buscar registros">Registrar Alocações</button> -->
<!-- 				</div> -->
			</form>
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

		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>
</body>

<script>
	function teste() {
		var selection = $("#formSelecaoVersaoCurso #versoesCurso").val();

		//alert(selection);

		if (selection == "all") {
			$("#tabelaAlocacoes tr").hide();
		} else {
			$("#tabelaAlocacoes tr")
					.each(
							function() {
								if ($(this).find("th").length == 0) {
									if ($(this).find("td").eq(0).text()
											.toLowerCase() == selection)
										$(this).show();
									else
										$(this).hide();
								}
							});
		}
	}
</script>
<script>
	$("#formSelecaoVersaoCurso #versoesCurso").change(teste);
</script>

</html>