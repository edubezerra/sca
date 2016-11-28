<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Acompanhamento do Egresso</title>

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

		<h1>Acompanhamento do Egresso</h1>

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

		<h3>Este programa visa a acompanhar o o desenvolvimento acadêmico
			e profissional dos egressos do Bacharelado em Ciência da Computação
			durante os dois primeiros anos de sua atuação profissional.</h3>

		<hr>

		<ul>
			<li><a
				href="${pageContext.request.contextPath}/avaliacaoEgresso/questionarioMedio">
					Egresso de Curso Médio/Técnico </a></li>
			<li><a
				href="${pageContext.request.contextPath}/avaliacaoEgresso/questionarioGraduacao">
					Egresso de Curso de Graduação</a></li>
		</ul>
	</div>
</body>
</html>
