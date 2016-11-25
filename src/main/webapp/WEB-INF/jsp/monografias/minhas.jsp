<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
                                           pageEncoding="ISO-8859-1"%>
<%@ include file="../taglib.jsp" %>


<template:mainLayout rootURL="${rootURL}" username="${username}"
                     contextPath="${pageContext.request.contextPath}" title="Minhas monografias"
                     anonymous="${UsuarioController.getCurrentUser()==null}">

	<jsp:attribute name="content">
        <div>
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
	</jsp:attribute>
</template:mainLayout>