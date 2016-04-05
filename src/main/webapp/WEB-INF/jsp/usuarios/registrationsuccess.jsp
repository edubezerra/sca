<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registration Confirmation Page</title>

	<link href="<c:url value='/resources/bootstrap/css/bootstrap.css' />" rel="stylesheet" />
	<link href="<c:url value='/resources/css/usuarios.css' />" rel="stylesheet" />

</head>
<body>
	<div class="generic-container">
		<div class="alert alert-success lead">${success}</div>

		<span class="well floatRight"> Go to <a
			href="<c:url value='/usuarios/list' />">Lista de Usuários</a>
		</span>
	</div>
</body>

</html>