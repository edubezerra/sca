<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Avaliação de Turma</title>

<link href="${rootURL}resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${rootURL}resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${rootURL}resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${rootURL}resources/js/app.js"></script>

</head>
<body class="basic-grey">

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

		<h1>Avaliação da turma ${requestScope.codigoTurma}
			(${requestScope.nomeDisciplina})</h1>

		<form
			action="${pageContext.request.contextPath}/avaliacaoTurma/avaliaTurma"
			method="post">
			<input type="hidden" name="idTurma" value="${requestScope.idTurma}" />

			<c:forEach items="${requestScope.questoes}" var="quesito"
				varStatus="i">

				<h3>${i.index + 1})&nbsp;${quesito.quesito}</h3>

				<c:forEach items="${quesito.alternativas}" var="alternativa"
					varStatus="j">

					<div class="radio">
						<label> <input type="radio" name="quesito${i.index}"
							value="${j.index}"
							<c:set var="oldQuesitoVarName" value="oldQuesito${i.index}"/>
							<c:if test="${requestScope[oldQuesitoVarName] == j.index}"> checked="checked"</c:if> />
							${alternativa}
						</label>
					</div>
					<br />
				</c:forEach>
				<br />
			</c:forEach>

			<h3>Descreva aspectos que você considerou positivos, se
				existirem:</h3>

			<textarea class="form-control" name="aspectosPositivos"
				maxlength="8192" rows="10" cols="70"
				placeholder="(opcional, max 8192 caracteres)"></textarea>

			<br />

			<h3>Descreva aspectos que você considerou negativos, se
				existirem:</h3>
			<textarea class="form-control" name="aspectosNegativos"
				maxlength="8192" rows="10" cols="70"
				placeholder="(opcional, max 8192 caracteres)"></textarea>

			<br />

			<button type="submit" class="btn btn-default">Submeter</button>
			<a class="btn btn-default"
				href="${pageContext.request.contextPath}/avaliacaoTurma/solicitaNovamenteAvaliacaoMatricula">
				<input type="button" value="Voltar" />
			</a>
		</form>
	</c:if>

</body>
</html>
