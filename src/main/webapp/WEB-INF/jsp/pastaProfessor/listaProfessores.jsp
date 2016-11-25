<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>

<template:pastaProfessor rootURL="${rootURL}" username="<%=UsuarioController.getCurrentUser().getNome()%>"
	contextPath="${pageContext.request.contextPath}" title="Professores de ${departamento.getNome()}">
	
	<jsp:attribute name="content">
		
		<sec:authorize access="hasRole('ROLE_COORDENADOR_CURSO')">
			<div class="row">
					<div class="col-xs-12">
	                	
	                    <div class="panel panel-default">
	                        <div class="panel-heading">
	                            <h3 class="panel-title">Professores</h3>
	                        </div>
	                        <div class="panel-body">
								
								<c:if test="${!empty professores}">
									<table class="table table-striped">
		                                <thead>
		                                    <tr>
		                                        <th>Nome</th>
		                                        <th>Opções</th>
		                                    </tr>
		                                </thead>
		                                <tbody>
		                                	<c:forEach items="${professores}" var="prof">
			                                    <tr>
			                                        <td>${prof.getPessoa().getNome()}</td>
			                                        <td>
			                                            <a class="btn btn-md btn-info"
															href="${pageContext.request.contextPath}/pastaProfessor/detalhesProfessor/${prof.getId()}">Detalhes</a>
			                                        </td>
			                                    </tr>
		                                    </c:forEach>
		                                </tbody>
		                            </table>
	                           </c:if>
	                            
	                        </div>
	                    </div>
	                </div>
	                
	             </div>
			
			</sec:authorize>
	</jsp:attribute>
	
</template:pastaProfessor>