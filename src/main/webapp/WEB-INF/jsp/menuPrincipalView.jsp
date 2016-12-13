<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="taglib.jsp" %>

<template:mainLayout rootURL="${rootURL}" username="<%=UsuarioController.getCurrentUser().getNome()%>"
	contextPath="${pageContext.request.contextPath}" title="Sistema de Controle Acadêmico">
	
	<jsp:attribute name="content">
		
		<div class="row">
			
			<div class="col-md-12">
				<div class="jumbotron">
					<h3>
						Olá,
						${UsuarioController.getCurrentUser().getNome()}!
					</h3>
					
					<p>
						Matrícula:
						${UsuarioController.getCurrentUser().getMatricula()}
						<%-- 		<sec:authentication property="name" /> --%>
					</p>
				</div>
				
			</div>
		
		</div>
	
	</jsp:attribute>
</template:mainLayout>