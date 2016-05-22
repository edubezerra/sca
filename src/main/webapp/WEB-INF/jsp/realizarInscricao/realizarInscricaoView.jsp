<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>Realizar Inscrição</title>
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

div.aluno {
    text-align: center;
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
  <form action="${pageContext.request.contextPath}/realizarInscricao/registrarTurmas" method="get">
	<div class="container">
	   <div class="row">
	      <h2>Inscrições em Turmas Oferecidas para ${semestreLetivo}</h2></pre>
		    <div class="aluno"><br>	    
		    <span class="label label-primary">Matrícula:</span>&nbsp;<c:out value="${requestScope.aluno.matricula}"></c:out>
		    &nbsp;
		    <span class="label label-primary">Aluno:</span>&nbsp;<c:out value="${requestScope.aluno.nome}"></c:out>
		    &nbsp;
		    <span class="label label-primary">Curso:</span>&nbsp;<c:out value="${requestScope.aluno.versaoCurso.curso.nome}"></c:out></p>
		    </div>
		    <div class="errors">
		      <c:if test="${error != null}">
				<span class="label label-danger">${error}</span>
			  </c:if>
              <c:if test="${msg != null}">
				<span class="label label-danger">${msg}</span>
			  </c:if>
            </div>
		    </br>
		    <table align="center">
		       <tr>
		          <th>Seleção</th>
		          <th>Nome Disciplina</th>
		          <th>Código Disciplina</th>
		          <th>Código Turma</th>
		          <th>Professor</th>
		          <th>Horário</th>
		       </tr>
		       <c:forEach var="turma" items="${turmasPossiveis}">
		        <tr>
				  <td><input type="checkbox" name="checkbox" value="${turma.codigo}"/></td>
			      <td><b>${turma.disciplina.nome}</b></td>
			      <td>${turma.disciplina.codigo}</td>
			      <td>${turma.codigo}</td>
			      <td><b>${turma.professor.nome}</b></td>
			      <td>
			        <c:forEach var="aula" items="${turma.aulas}">
		            ${aula}
		            </c:forEach>    
		          </td>
		        </tr>
		       </c:forEach>
		    </table>
		<br>
		</div>
			   <a class="btn btn-primary" href="${pageContext.request.contextPath}/realizarInscricao/menuPrincipal">
			   <i class="fa fa-arrow-left"> </i> Voltar</a>
			   <button type="submit" class="btn btn-success">Registrar Inscrições</button>
		</div>
	</form>
 </body>
</html>