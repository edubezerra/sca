<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Avaliação</title>

<link href="${rootURL}resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${rootURL}resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${rootURL}resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${rootURL}resources/js/app.js"></script>

</head>
<body class="basic-grey">

	<h1>Avaliação de turmas cursadas</h1>

	<p>A avaliação de Turmas por Discentes ocorre em dois momentos: no
		primeiro e no segundo semestre letivo de cada ano por meio de
		formulário eletrônico.</p>

	<p>As avaliações permanecem no banco de dados, sendo processadas
		pela Coordenação do Curso. Os resultados são repassados ao professor
		de cada turma avaliada, na forma consolidada e anônima (i.e., sem
		identificação do aluno avaliador), após o término do semestre letivo,
		para que os alunos não se sintam inibidos ao avaliar seus atuais
		professores.</p>

	<br>
	<br>

	<c:if test="${requestScope.error != null}">
		<div>
			<p class="error">${requestScope.error}</p>
		</div>
	</c:if>

	<c:if test="${requestScope.info != null}">
		<div>
			<p class="info">${requestScope.info}</p>
		</div>
	</c:if>

	<c:if test="${requestScope.turmasCursadas == null}">
		<h3>Você não está inscrito em nenhuma turma.</h3>
	</c:if>

	<c:if test="${requestScope.turmasCursadas != null}">
		<div class="table-responsive">
			<table class="table">
				<thead>
					<tr>
						<th>Período Letivo</th>
						<th>Código da Turma</th>
						<th>Nome da Disciplina</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${requestScope.turmasCursadas}" var="turma">
						<tr>
							<td>${turma.periodoLetivo}</td>
							<td>${turma.codigoTurma}</td>
							<td>${turma.nomeDisciplina}</td>
							<td><c:if test="${turma.isAvaliada}">
									<input class="lastfield" type="submit" value="Avaliada"
										disabled="disabled" />
								</c:if> <c:if test="${! turma.isAvaliada}">
									<form action="${pageContext.request.contextPath}/avaliacaoTurma/solicitaAvaliacaoTurma"
										method="post">
										<input type="hidden" name="idTurma" value="${turma.idTurma}" />
										<input class="lastfield" type="submit" value="Avaliar" />
									</form>
								</c:if></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</c:if>

	<br />

	<a class="button_embedded"
		href="${pageContext.request.contextPath}/avaliacaoTurma/menuPrincipal"> <input
		type="button" value="Voltar" />
	</a>

</body>
</html>
