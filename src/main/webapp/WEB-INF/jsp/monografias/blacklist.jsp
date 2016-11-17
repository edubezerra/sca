<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"%>
<%@ include file="../taglib.jsp" %>


<template:mainLayout rootURL="${rootURL}" username="${username}"
					 contextPath="${pageContext.request.contextPath}" title="Blacklist de monografias"
					 anonymous="${UsuarioController.getCurrentUser()==null}">

	<jsp:attribute name="content">

		<div style="padding: 0 20px 0 20px; max-width: 800px;">
			<form id="form" enctype="multipart/form-data" method="POST" action="/monografias/blacklist/">
				<div class="form-group">
					<label for="blacklist">Blacklist de tags (separadas por linha)</label>
					<textarea name="blacklist" id="blacklist" style="min-height:800px;">${blacklist}</textarea>
				</div>

				<button type="submit" class="btn btn-default">Salvar</button>
			</form>
		</div>
	</jsp:attribute>
</template:mainLayout>