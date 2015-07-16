<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Avaliação</title>
<link href="${pageContext.request.contextPath}/css/base.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/form.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/table.css"
	rel="stylesheet" type="text/css" />
</head>
<body class="basic-grey">

	<h1>Avaliação de turmas cursadas em ${requestScope.periodoLetivo}</h1>

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

	<c:if test="${requestScope.turmas == null}">
		<h3>Você não está inscrito em nenhuma turma.</h3>
	</c:if>

	<c:if test="${requestScope.turmas != null}">
		<div class="table">
			<div class="row">
				<div class="field">
					<div>
						<b>Código da Turma</b>
					</div>
				</div>
				<div class="field">
					<div>
						<b>Nome da Disciplina</b>
					</div>
				</div>
				<div class="field"></div>
			</div>
			<c:forEach items="${requestScope.turmas}" var="turma">
				<div class="row">
					<div class="field">
						<div>${turma.codigoTurma}</div>
					</div>
					<div class="field">
						<div>${turma.nomeDisciplina}</div>
					</div>
					<div class="field">
						<c:if test="${turma.isAvaliada}">
							<input class="lastfield" type="submit" value="Avaliada"
								disabled="disabled" />
						</c:if>
						<c:if test="${! turma.isAvaliada}">
							<form
								action="${pageContext.request.contextPath}/avaliacaoTurma/solicitaAvaliacaoTurma"
								method="post">
								<input type="hidden" name="codigoTurma"
									value="${turma.codigoTurma}" /> <input class="lastfield"
									type="submit" value="Avaliar" />
							</form>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:if>

	<br />

	<a class="button_embedded"
		href="${pageContext.request.contextPath}/avaliacaoTurma/menuPrincipal">
		<input type="button" value="Voltar" />
	</a>

</body>
</html>
