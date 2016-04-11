<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Lista de Usuários</title>

	<link href="<c:url value='/resources/bootstrap/css/bootstrap.css' />" rel="stylesheet" />
	<link href="<c:url value='/resources/css/usuarios.css' />" rel="stylesheet" />

</head>

<body>
	<div class="generic-container">
		<div class="panel panel-default">
			<!-- Default panel contents -->
			<div class="panel-heading">
				<span class="lead">Lista de Usuários</span>
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Nome</th>
						<th>Login</th>
						<th>Email</th>
						<th>Data Criação</th>
						<th width="100"></th>
						<th width="100"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${users}" var="user">
						<tr>
							<td>${user.nome}</td>
							<td>${user.login}</td>
							<td>${user.email}</td>
							<td>${user.dob}</td>
							<td><a href="<c:url value='/usuarios/edit-user-${user.login}' />"
								class="btn btn-success custom-width">editar</a></td>
							<td><a href="<c:url value='/usuarios/delete-user-${user.login}' />"
								class="btn btn-danger custom-width">excluir</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="well">
			<a href="<c:url value='/usuarios/newuser' />">Adicionar Novo Usuário</a>
		</div>
	</div>
</body>
</html>