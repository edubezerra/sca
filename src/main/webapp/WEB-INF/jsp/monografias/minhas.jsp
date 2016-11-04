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
            <span class="lead">Minhas monografias</span>
        </div>
    </div>
    <div style="padding: 0 20px 0 20px; max-width: 800px;">
        <form method="GET" action="/monografias/nova/">
            <input type="submit" value="Nova monografia">
        </form>
    </div>
    <c:if test="${monografias != null}">
        <table class="table table-striped">
            <thead>
            <tr>
                <td>Título</td>
                <td>Orientador</td>
                <td>Resumo em língua estrangeira</td>
                <td>Resumo em Português</td>
            </tr>
            </thead>
            <c:forEach var="monografia" items="${monografias}">
                <tr>
                    <td><a href="<c:url value="/monografias/editar/"><c:param name="id" value="${monografia.id}"></c:param></c:url>">${monografia.titulo}</a></td>
                    <td>${monografia.orientador}</td>
                    <td>${monografia.resumoLinguaEstrangeira}</td>
                    <td>${monografia.resumoPortugues}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <div class="well">
        <a href="<c:url value='/monografias/' />" onclick="history.back(); return false">Voltar</a>
    </div>
</div>
</body>
</html>