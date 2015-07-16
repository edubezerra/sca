<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Home</title>
<link href="${pageContext.request.contextPath}/css/base.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/form.css"
	rel="stylesheet" type="text/css" />
</head>
<body class="basic-grey">

	<h1>Menu Principal</h1>

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

	<ul>
		<li><a
			href="${pageContext.request.contextPath}/avaliacaoTurma/avaliacaoTurmas">
				Avaliar turmas (${requestScope.periodoLetivo})</a></li>
		<li><a
			href="${pageContext.request.contextPath}/logout">
				Sair do sistema</a></li>
	</ul>
</body>
</html>
