<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>SCA - Monografias</title>

    <link href="<c:url value='/resources/bootstrap/css/bootstrap.css' />" rel="stylesheet" />

</head>

<body>
<div class="generic-container">
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading">
            <span class="lead">${monografia.titulo}</span>
        </div>
    </div>

    <div style="padding: 0 20px 0 20px; max-width: 800px;">
        <div class="form-group">
            <label>Título: </label>
            <span>${monografia.titulo}</span>
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

    <div class="well">
        <a href="<c:url value='/monografias/' />" onclick="history.back(); return false">Voltar</a>
    </div>
</div>
</body>
</html>