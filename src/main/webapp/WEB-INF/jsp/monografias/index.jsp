<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Monografias</title>

	<link href="<c:url value='/resources/bootstrap/css/bootstrap.css' />" rel="stylesheet" />
	<link href="<c:url value='/resources/css/usuarios.css' />" rel="stylesheet" />

</head>

<body>
	<div class="generic-container">
		<div class="panel panel-default">
			<!-- Default panel contents -->
			<div class="panel-heading">
				<span class="lead">Monografias</span>
			</div>
		</div>
		<div style="padding: 0 20px 0 20px; max-width: 800px;">
			<form id="form" method="GET" action="/monografias/">
				<div class="form-group">
					<input type="text" name="q" id="q" value="${q}">
					<input type="submit" value="Buscar">
				</div>
			</form>
		</div>
        <c:if test="${queryResults != null}">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <td>Título</td>
                        <td>Autor</td>
                        <td>Orientador</td>
                        <td>Resumo em língua estrangeira</td>
                        <td>Resumo em Português</td>
                    </tr>
                </thead>
                <c:forEach var="monografia" items="${queryResults}">
                    <tr>
                        <td><a href="/monografias/visualizar/?id=${monografia.id}">${monografia.titulo}</a></td>
                        <td>${monografia.autor}</td>
                        <td>${monografia.orientador}</td>
                        <td>${monografia.resumoLinguaEstrangeira}</td>
                        <td>${monografia.resumoPortugues}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
		<c:if test="${tags != null}">

		</c:if>
		<div class="well">
			<a href="<c:url value='/monografias/minhas/' />">Minhas monografias</a>
		</div>
	</div>
</body>
</html>