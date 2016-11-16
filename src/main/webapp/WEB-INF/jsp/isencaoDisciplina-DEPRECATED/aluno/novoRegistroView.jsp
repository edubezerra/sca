<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html data-ng-app="inclusaoDisciplina" data-ng-controller="inclusaoController">

<head>
    <meta charset="UTF-8">
	<meta name="viewport"
		content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		
    <title>SCA - Requerimento de registro de atividade complementar</title>
    
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/vendor/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
	<script src="${pageContext.request.contextPath}/resources/js/vendor/jquery-1.11.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/vendor/angular.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/app.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
	
	<link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
		media="screen" rel="stylesheet" type="text/css" />
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/app.js"></script>
    <script>
		function textCounter(field,field2,maxlimit){
		 	var countfield = document.getElementById(field2);
		 	if ( field.value.length > maxlimit ) {
		  		field.value = field.value.substring( 0, maxlimit );
		  		return false;
		 	} else {
		 		countfield.innerHTML = maxlimit - field.value.length;
		 	}
		}
	</script>
</head>

<body class="inclusao-disciplina">
    <div class="container">
    	<div class="row text-center">
			<h3>Requerimento de registro de atividade complementar</h3>
		</div>
		<hr/>
        <div class="row">
			<h5><b>Aluno:</b> <c:out value="${requestScope.dadosAluno.nomeAluno}"></c:out> (Matrícula: <c:out value="${requestScope.matricula}"></c:out>)</h5>
			<h5><b>Curso:</b> <c:out value="${requestScope.dadosAluno.curso.sigla}"></c:out> - <c:out value="${requestScope.dadosAluno.curso.nome}"></c:out> (Grade: <c:out value="${requestScope.dadosAluno.versaoCurso}"></c:out>)</h5>
		</div>
        <br/>
        <div class="row">
            <h5><i class="fa fa-warning"></i><b> Atenção:</b>
            O registro de atividade complementar somente será considerado para cálculo de carga horária cumprida 
            	após a autorização do professor/chefe de departamento.</h5>
        </div>
        <c:if test="${requestScope.error != null}">
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<hr/>
        <div class="row">
            <form action="${pageContext.request.contextPath}/registroAtividades/registraAtividade" 
            	  method="post" enctype="multipart/form-data">
            	  
            	<div class="row">
            		<h4><b>Atividade Complementar:</b> ${requestScope.dadosAtiv.categoriaAtiv} - ${requestScope.dadosAtiv.descrAtiv}</h4>
              	</div>
              	<br/>
                <div class="row">
					<label for="descricao">Descrição do registro:</label>
					<textarea name="descricao" class="form-control" rows="2" id="descricao"
							  maxlength="144" 
							  placeholder="(opcional, max 144 caracteres)"
							  onkeyup="textCounter(this,'counter',144);"></textarea>
					<h5>Faltam <label id="counter">144</label> caracteres.</h5>
				</div>
				               
                <div class="row">
	                <label for="inputFile">Anexar comprovante de cumprimento da atividade complementar
	                (Formatos aceitos: PDF, JPEG ou PNG. Tamanho Máximo: 10mb)</label>
	                <input class="file" type="file" name="file" id="inputFile" required>
	                
	                <span class="label label-danger" id="tamanhoGrande" style="display:none;"></span>
                </div>
                <br/>
                
                <div class="row">
	                <label for="cargaHoraria">Carga horária do registro (em horas):</label>
	                <input style="width:100px;" class="form-control number" 
	                 id="cargaHoraria" name="cargaHoraria" type="number"
	                 maxlength="3" max="${requestScope.dadosAtiv.cargaHorariaRestante}" min="1" value="1" required/>
                </div>
               <br/>
                <hr/>
                <div class="row checkbox">
                    <label>
                        <input type="checkbox" value="" required> Estou ciente de que o presente requerimento 
                        <b>não garante o registro da atividade complementar solicitada</b> 
                        e de que posso verificar se o registro foi efetivado, ou não, 
                        através de consulta a esta mesma plataforma.
                    </label>
                </div>
                <br/>
				<div class=text-center>
					<input type="hidden" id="idAtiv" name="idAtiv" value="${requestScope.idAtiv}"> 			
					<button type="submit" class="btn btn-primary btn-lg text-center">Confirmar</button>
				</div>
                
            </form>
        </div>
        <br/>
        <a class="btn btn-default" href="${pageContext.request.contextPath}/registroAtividades/solicitaNovamenteRegistroAtividades">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
    </div>
    
 
</body>

</html>
	