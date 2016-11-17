<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="../taglib.jsp" %>


<template:mainLayout rootURL="${rootURL}" username="${username}"
					 contextPath="${pageContext.request.contextPath}" title="Monografias"
					 anonymous="${UsuarioController.getCurrentUser()==null}">

	<jsp:attribute name="content">
		<div class="generic-container">
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
							<td>Autores</td>
							<td>Orientador</td>
							<td>Resumo em língua estrangeira</td>
							<td>Resumo em Português</td>
							<sec:authorize access="hasRole('ROLE_COORDENADOR_ATIVIDADES') or hasRole('ROLE_ADMIN')">
								<td></td>
							</sec:authorize>
						</tr>
					</thead>
					<c:forEach var="monografia" items="${queryResults}">
						<tr>
							<td><a href="/monografias/visualizar/?id=${monografia.id}">${monografia.titulo}</a></td>
							<td>${monografia.autoresString}</td>
							<td>${monografia.orientador}</td>
							<td>${monografia.resumoLinguaEstrangeira}</td>
							<td>${monografia.resumoPortugues}</td>
							<sec:authorize access="hasRole('ROLE_COORDENADOR_CURSO') or hasRole('ROLE_ADMIN')">
								<td><a href="/monografias/editar/?id=${monografia.id}">Editar</a></td>
							</sec:authorize>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<c:if test="${tagsJSON != null}">
				<div id="tags"></div>
				<script>
					var tags = ${tagsJSON};
				</script>
				<script src="<c:url value='/resources/relatorio/d3.v3.min.js' />"></script>
				<script src="<c:url value='/resources/monografias/d3.cloud.js' />"></script>
				<script src="<c:url value='/resources/monografias/tagcloud.js' />"></script>
			</c:if>
			<sec:authorize access="hasRole('ROLE_ALUNO')">
				<a href="<c:url value='/monografias/minhas/' />">Minhas monografias</a>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_COORDENADOR_CURSO') or hasRole('ROLE_ADMIN')">
				<a href="<c:url value='/monografias/nova/' />">Enviar monografia</a>
			</sec:authorize>
		</div>
	</jsp:attribute>
</template:mainLayout>