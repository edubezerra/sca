<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<!DOCTYPE>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>Inscrições Realizadas</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    
    <style>
td, th {
    border: 2px solid white;
    padding:10px;
    text-align: center;
}

thead, tbody { display: block; }

tbody {
    height: 400px;       
    overflow-y: auto;    
    overflow-x: hidden; 
}

tr:nth-child(even) {
    background-color: rgb(212,223,232);
    }

th {
    background-color: rgb(51,122,183);
    color: white;
}

div.errors{
     color: rgb(255,0,0);
}
    </style>
</head>
<body class="home-inclusao text-center">
    <div class="container">
	  	<div class="row">
      		<h2>Inscrições em Turmas Oferecidas para ${semestreLetivo}</h2>
		    <div class="aluno"><br>	    
		    <span class="label label-primary">Matrícula:</span>&nbsp;<c:out value="${requestScope.aluno.matricula}"></c:out>
		    &nbsp;
		    <span class="label label-primary">Aluno:</span>&nbsp;<c:out value="${requestScope.aluno.nome}"></c:out>
		    &nbsp;
		    <span class="label label-primary">Curso:</span>&nbsp;<c:out value="${requestScope.aluno.versaoCurso.curso.nome}"></c:out>
		    </div>
		    <br>
       <table>
		     <tr>
		        <th>Nome Disciplina</th>
		        <th>Código Disciplina</th>
		        <th>Código Turma</th>
		        <th>Professor</th>
		        <th>Horário</th>
		        <th>Local</th>
		        <th>Situação da Inscrição</th>
		     </tr>
            <c:forEach var="turma" items="${inscritas}">
            <tr>
	             <td><b>${turma.key.disciplina.nome}</b></td>
	             <td>${turma.key.disciplina.codigo}</td>
	             <td>${turma.key.codigo}</td>
	             <td><b>${turma.key.professor.nome}</b></td>
	             <td>
	        	     <c:forEach var="aula" items="${turma.key.aulas}">
	     	                ${aula}
	            	 </c:forEach> 
	             </td> 
	             <td> 
	             	 <c:forEach var="aula" items="${turma.key.aulas}">
	                	     ${aula.local.descricao}
	             	 </c:forEach> 
	             </td> 
	               	<c:if test="${turma.value == 'Inscrição realizada com sucesso!'}">
						<td bgcolor=#00C800 style="color:white;">
						<b> ${turma.value}</b>
			      		</td>
			        </c:if>
	             	<c:if test="${turma.value == 'Inscrição já realizada!'}">
						<td bgcolor=C80000 style="color:white;">
						<b> ${turma.value}</b>
			      		</td>
			        </c:if>
	             	<c:if test="${turma.value == 'Limite de vagas já alcançado!'}">
						<td bgcolor=#C80000 style="color:white;">
						<b> ${turma.value}</b>
			      		</td>
			        </c:if>
	         </tr>
             </c:forEach>
    	 </table>	  
       </div>
       <a class="btn btn-default" href="${pageContext.request.contextPath}/realizarInscricao/realizarInscricao">
		 <i class="fa fa-arrow-left"> </i> Voltar</a>
		  <a class="btn btn-default" href="${pageContext.request.contextPath}/realizarInscricao/menuPrincipal">
		 <i class="fa fa-arrow-right"> </i> Menu Principal</a>
     </div>
</body>
</html>