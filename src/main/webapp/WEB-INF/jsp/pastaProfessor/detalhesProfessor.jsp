<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../taglib.jsp" %>

<template:pastaProfessor rootURL="${rootURL}" username="<%=UsuarioController.getCurrentUser().getNome()%>"
	contextPath="${pageContext.request.contextPath}" title="Detalhes do Professor">
	
	<jsp:attribute name="content">
		
		<sec:authorize access="hasRole('ROLE_COORDENADOR_CURSO')">
			<div class="row">
	
					<div class="col-xs-12">
						<a class="btn btn-sm btn-default"
							href="${pageContext.request.contextPath}/pastaProfessor/professores">Voltar</a>
							
						<hr>
					</div>
					
	                <div class="col-xs-12">
	                	
	                    <div class="panel panel-default">
	                        <div class="panel-heading">
	                            <h3 class="panel-title">Detalhes de ${professor.getPessoa().getNome() }</h3>
	                        </div>
	                        <div class="panel-body">
	                            <div class="form-group">
	                                <a href="#" data-toggle="modal" data-target="#addDocumentModal"
	                                	class="btn btn-md btn-success">Novo Documento</a>
	                            </div>
								
								<c:if test="${!empty documentos}">
		                            <table class="table table-striped">
		                                <thead>
		                                    <tr>
		                                        <th>Categoria</th>
		                                        <th>Nome</th>
		                                        <th>Arquivo</th>
		                                        <th>Opções</th>
		                                    </tr>
		                                </thead>
		                                <tbody>
		                                	<c:forEach items="${documentos}" var="doc">
			                                    <tr>
			                                        <td>${doc.getCategoria()}</td>
			                                        <td>${doc.getNome()}</td>
			                                        <td>
			                                            <a href="${pageContext.request.contextPath}/pastaProfessor/visualizarDocumento/${doc.getId()}">${doc.getDocumento().getNome()}</a>
			                                        </td>
			                                        <td>
			                                        	<a href="#" class="btn btn-md btn-danger"
			                                        		onclick="Utility.confirm('${pageContext.request.contextPath}/pastaProfessor/detalhesProfessor/${professor.getId()}/excluirDocumento/${doc.getId()}')">
			                                        		Excluir
		                                        		</a>
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
			
				<!-- Modal -->
				<div id="addDocumentModal" class="modal fade" role="dialog">
				  <div class="modal-dialog">
				
				    <!-- Modal content-->
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal">&times;</button>
				        <h4 class="modal-title">Novo Documento</h4>
				      </div>
				      <form action="${pageContext.request.contextPath}/pastaProfessor/detalhesProfessor/${professor.getId()}/registrarDocumento" method="POST"
				      	enctype="multipart/form-data">
				      	
						<div class="modal-body">
						
							<div class="row">
								<div class="form-group col-xs-12 col-md-6">
								  	<label>Nome do Documento</label>
								  	<input type="text" name="nome" class="form-control" />
								</div>
								<div class="form-group col-xs-12 col-md-6">
								  	<label>Categoria</label>
								  	<select name="categoria" class="form-control">
								  		<option value="Diploma">Diploma</option>
								  		<option value="Curso de Extensão">Curso de Extensão</option>
								  		<option value="Certificado">Certificado</option>
								  		<option value="Outros">Outros</option>
								  	</select>
								</div>
							</div>
							
							<div class="row">
								<div class="form-group col-sm-12 col-md-12">
									<label>Arquivo</label>
									<input type="file" name="file" />
								</div>
							</div>
						  
						</div>
						<div class="modal-footer">
						  <button type="submit" class="btn btn-success">Salvar</button>
						  <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
						</div>
				      </form>
				      
				    </div>
				
				  </div>
				</div>
			</sec:authorize>
	</jsp:attribute>
	
</template:pastaProfessor>