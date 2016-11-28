<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Perfil do Egresso</title>

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
			<h3>Não existem questões a responder.</h3>
		</c:if>

		<c:if test="${requestScope.questoes != null}">

			<h1>Perfil do Egresso - Graduação</h1>

			<%-- 			<h1>Egresso: ${requestScope.nomeEgresso}</h1> --%>

			<%-- 			<h1>CPF: ${requestScope.cpfEgresso}</h1> --%>

			<hr>

			<form method="POST"
				action="${pageContext.request.contextPath}/avaliacaoEgresso/avaliaEgresso">
				<c:forEach items="${requestScope.questoes}" var="questao"
					varStatus="i">
					<%-- 					<c:if test="${i.index+1 == 2}"> --%>
					<!-- 						<div id="divHideSlice"> -->
					<%-- 					</c:if> --%>
					<c:choose>
						<c:when test="${i.index+1 == 7}">
							<h3>${i.index + 1})&nbsp;${questao.quesito}</h3>
							<div style="margin-left: 30px;">
								<input type="text" name="resp-especialidade"
									class="form-control" style="width: 400px;" />
							</div>
						</c:when>

						<c:when test="${i.index+1 != 7}">

							<h3>${i.index + 1})&nbsp;${questao.quesito}</h3>

							<c:forEach items="${questao.alternativas}" var="alternativaDto"
								varStatus="j">

								<div class="radio">
									<label> <input type="radio"
										name="question-${i.index+1}" value="${alternativaDto.id}" />
										${alternativaDto.alternativa} <c:if
											test="${alternativaDto.id == 37}">
											<input type="text" name="resp-10" />
										</c:if> <c:if test="${alternativaDto.id == 55}">
											<input type="text" name="resp-area" />
										</c:if>
									</label>
								</div>
							</c:forEach>
						</c:when>
					</c:choose>
					<br />
					<br />
					<%-- 					<c:if test="${i.index+1 == 12}"> --%>
				</c:forEach>

				<br /> <br />

				<div class="checkbox" style="margin-left: 200px">
					<label> <input type="checkbox" id="chkAllow">
						Autorizo a divulgação dos meus contatos pessoais junto às
						empresas.
					</label>
				</div>

				<br /> <br />
				<div style="margin-left: 250px;">
					<div style="float: left; margin-right: 30px;">
						<button type="submit" class="btn btn-primary" id="btnSend">
							<span class="glyphicon glyphicon-ok"></span> Confirmar envio
						</button>
					</div>
					<div style="float: left;">
						<button type="button" class="btn btn-default">
							<span class="glyphicon glyphicon-remove"></span> Limpar respostas
						</button>
					</div>
				</div>
			</form>
		</c:if>
		<br /> <br /> <br /> <br />
	</div>
</body>
</html>