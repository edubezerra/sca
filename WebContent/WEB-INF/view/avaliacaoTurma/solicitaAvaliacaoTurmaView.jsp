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

	<h1>Responder às questões de avaliação da turma:</h1>

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

	<c:if test="${requestScope.questoes == null}">
		<h3>Não existem questões a serem respondidas.</h3>
	</c:if>

	<c:if test="${requestScope.questoes != null}">

		<div>
			<p class="discipline">${requestScope.questoes.getNomeDisciplina()}
				(${requestScope.questoes.getCodigoTurma()})</p>
		</div>

		<form
			action="${pageContext.request.contextPath}/avaliacaoTurma/avaliaTurma"
			method="post">
			<input type="hidden" name="codigoTurma"
				value="${requestScope.questoes.getCodigoTurma()}" />
			<c:forEach items="${requestScope.questoes}" var="quesito"
				varStatus="i">
				<h3>${i.index + 1})&nbsp;${quesito.quesito}</h3>
				<c:forEach items="${quesito.alternativas}" var="alternativas"
					varStatus="j">
					<input type="radio" name="quesito${i.index}" value="${j.index}"
						<c:set var="oldQuesitoVarName" value="oldQuesito${i.index}"/>
						<c:if test="${requestScope[oldQuesitoVarName] == j.index}"> checked="checked"</c:if> />
					<div class="question">${alternativas}</div>
					<br />
				</c:forEach>
				<br />
			</c:forEach>

			<div>
				<h4>Forneça sugestões ou críticas para a avaliação da
					disciplina:</h4>
				<textarea name="sugestoes" maxlength="8192"
					placeholder="(opcional, max 8192 caracteres)"></textarea>
			</div>

			<br />

			<div>
				<input type="submit" value="Submeter" /> 
				<a class="button_embedded"
					href="${pageContext.request.contextPath}/avaliacaoTurma/solicitaNovamenteAvaliacaoMatricula">
					<input type="button" value="Voltar" />
				</a>
			</div>
		</form>
	</c:if>

</body>
</html>
