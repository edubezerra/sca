<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html data-ng-app="inclusaoDisciplina"
	data-ng-controller="inclusaoController">

<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<title>SCA - Requerimento de registro de atividade complementar</title>

<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/vendor/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/vendor/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/style.css">
<script
	src="${pageContext.request.contextPath}/resources/js/vendor/jquery-1.11.3.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/vendor/angular.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/app.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>

<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/app.js"></script>
<script>
	function textCounter(field, field2, maxlimit) {
		var countfield = document.getElementById(field2);
		if (field.value.length > maxlimit) {
			field.value = field.value.substring(0, maxlimit);
			return false;
		} else {
			countfield.innerHTML = maxlimit - field.value.length;
		}
	}
</script>
</head>

<body>
	<div class="container">
		<div class="row text-center">
			<h3 align="center">Relação de Disciplinas Externas</h3>
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


		<form
			action="${pageContext.request.contextPath}/isencaoDisciplina/relacaoMateriaExterna"
			method="POST">
			<input name="aluno" value="${aluno.matricula}" type="hidden" />
			<div class="table-responsive" align="center">
				<table class="table">
					<thead>
						<tr>
							<th>Código</th>
							<th>Disciplina</th>
							<th>Disciplina Externa</th>

						</tr>
					</thead>
					<tbody>
						<c:forEach items="${itemIsencaoByProcessoIsencao}"
							var="itemIsencaoByProcessoIsencao">
							<input name="itemIsencaoByProcessoIsencao"
								value="${itemIsencaoByProcessoIsencao.disciplina.codigo}"
								type="hidden" />
							<tr>
								<td>${itemIsencaoByProcessoIsencao.disciplina.codigo}</td>
								<td>${itemIsencaoByProcessoIsencao.disciplina.nome}</td>
								<td>
									<div class="form-group">
										<label>Digite o nome da disciplina que deseja
											associar:</label>
										<textarea class="form-control" rows="3"
											name="disciplinaAssociada"
											placeholder="Digite o nome da disciplina que deseja associar e divida com (; ou /) cada uma.(Ex: Banco de dados ; Estrutura de Dados)"></textarea>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<button class="btn btn-success custom-width" type="submit"
				name="matricula">Confirmar</button>
		</form>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>
</body>
</html>