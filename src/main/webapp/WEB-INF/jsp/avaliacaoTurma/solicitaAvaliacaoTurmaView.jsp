<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Avaliação de Turma</title>

<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>

</head>
<body class="basic-grey">

	<div class="container">

		<c:if test="${requestScope.error != null}">
			<div>
				<p style="color: red; font-weight: bold;" class="error">${requestScope.error}</p>
			</div>
		</c:if>

		<c:if test="${requestScope.info != null}">
			<div>
				<p style="color: blue; font-weight: bold;" class="info">${requestScope.info}</p>
			</div>
		</c:if>

		<c:if test="${requestScope.questoes == null}">
			<h3>Não existem questões a serem respondidas.</h3>
		</c:if>

		<c:if test="${requestScope.questoes != null}">

			<h1>Turma: ${requestScope.codigoTurma}</h1>

			<h1>Disciplina: ${requestScope.nomeDisciplina}</h1>
			
			<hr>

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
							<label> 
							<input type="radio" name="quesito${i.index}"
								value="${j.index}"
								<c:set var="oldQuesitoVarName" value="oldQuesito${i.index}"/>
								<c:if test="${requestScope[oldQuesitoVarName] == j.index}"> checked="checked"</c:if> />
								${alternativa}
							</label>
						</div>
					</c:forEach>
					<br />
				</c:forEach>

				<h3>Descreva aspectos que você considerou positivos, se
					existirem:</h3>

				<textarea class="form-control" name="aspectosPositivos"
					maxlength="8192" rows="10" cols="70"
					placeholder="(opcional, max 8192 caracteres)">${requestScope.aspectosPositivos}</textarea>

				<br />

				<h3>Descreva aspectos que você considerou negativos, se
					existirem:</h3>
				<textarea class="form-control" name="aspectosNegativos"
					maxlength="8192" rows="10" cols="70"
					placeholder="(opcional, max 8192 caracteres)">${requestScope.aspectosNegativos}</textarea>

				<br />

				<button type="submit" class="btn btn-primary">Submeter</button>
				<a class="btn btn-default"
					href="${pageContext.request.contextPath}/avaliacaoTurma/solicitaNovamenteAvaliacaoTurma">
					<input type="button" value="Voltar" />
				</a>
			</form>
		</c:if>

	</div>

</body>
</html>
