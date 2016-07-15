<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Avaliação de Turmas</title>

<link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>

</head>

<body class="text-center">

	<div class="container">

		<h1>Avaliação de Turmas Cursadas</h1>

		<p>As avaliações permanecem no banco de dados, sendo processadas
			pela Coordenação do Curso. Os resultados são repassados ao professor
			de cada turma avaliada, na forma consolidada e anônima (i.e., sem
			identificação do aluno avaliador).</p>

		<br> <br>

		<c:if test="${requestScope.error != null}">
			<div>
				<p style="color:red;font-weight: bold;" class="error">${requestScope.error}</p>
			</div>
		</c:if>

		<c:if test="${requestScope.info != null}">
			<div>
				<p style="color:blue;font-weight: bold;" class="info">${requestScope.info}</p>
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
										<form
											action="${pageContext.request.contextPath}/avaliacaoTurma/solicitaAvaliacaoTurma"
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

		<br /> <a class="button_embedded"
			href="${pageContext.request.contextPath}/avaliacaoTurma/menuPrincipal">
			<input type="button" value="Voltar" />
		</a>

	</div>
</body>
</html>
