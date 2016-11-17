<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ include file="../taglib.jsp" %>


<template:mainLayout rootURL="${rootURL}" username="${username}"
                     contextPath="${pageContext.request.contextPath}" title="Monografias"
                     anonymous="${UsuarioController.getCurrentUser()==null}">

	<jsp:attribute name="content">
        <div>
            <div class="form-group">
                <label>Título: </label>
                <span>${monografia.titulo}</span>
            </div>
            <div class="form-group">
                <label>Autores: </label>
                <span>
                    <c:forEach var="autor" items="${monografia.autores}">
                        ${autor}<br>
                    </c:forEach>
                </span>
            </div>
            <div class="form-group">
                <label>Orientador: </label>
                <span>${monografia.orientador}</span>
            </div>
            <div class="form-group">
                <label>Presidente da banca: </label>
                <span>${monografia.presidenteBanca}</span>
            </div>
            <div class="form-group">
                <label>Membros da banca: </label>
                <span>${monografia.nomesMembrosBanca}</span>
            </div>
            <div class="form-group">
                <label>Resumo em português: </label>
                <div>${monografia.resumoPortugues}</div>
            </div>
            <div class="form-group">
                <label>Resumo em língua estrangeira: </label>
                <div>${monografia.resumoLinguaEstrangeira}</div>
            </div>
            <div id="formArquivos" class="form-group">
                <label>Arquivos: </label>
                <div id="arquivosExistentes">
                    <c:forEach var="arquivo" items="${monografia.arquivos}">
                         <c:if test="${arquivo.tamanho != 0}">
                             <div>
                                 <a href="<c:url value='/monografias/download/'>
                                <c:param name="id" value="${monografia.id}"></c:param>
                                <c:param name="arquivo" value="${arquivo.nome}"></c:param>
                            </c:url>">
                                         ${arquivo.nome} (${arquivo.tamanhoLegivel})
                                 </a>
                             </div>
                         </c:if>
                    </c:forEach>
                </div>
            </div>
        </div>
	</jsp:attribute>
</template:mainLayout>