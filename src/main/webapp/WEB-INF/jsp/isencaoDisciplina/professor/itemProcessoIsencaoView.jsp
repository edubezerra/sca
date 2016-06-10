<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Isenção de Disciplina</title>
</head>
<body>
	<!-- <h4 align="left">
		<b> Professor: ${professor.nome }</b> <br></br> <b> Matrícula:
			${professor.matricula }</b>
	</h4>  
	<br></br> -->
	<h3 align="left">
	Aluno: ${aluno.nome }
	
	<br>
	Matrícula: ${aluno.matricula }
	
	</h3>
	<br>
	<h3 align="center">Pedidos de Isenção de Disciplina</h3>
	<br></br>

	<div>
		<form action="${pageContext.request.contextPath}/isencaoDisciplina/downloadFile" method="POST" target="_blank">
		<input type="hidden" name="comprovanteProcIsencao" value="${aluno.processoIsencao.id}">
			<button type="submit" class="btn btn-link">
				<i class="fa fa-download fa-2x"></i>
				<h4 class="comprovante">Comprovante</h4>
			</button>
		</form>
	</div>

	<form action="${pageContext.request.contextPath}/isencaoDisciplina/professorSucesso"
		  method="POST">
		  <input name="aluno" value="${aluno.matricula}" type="hidden" />
		<div class="table-responsive" align="center">
			<table class="table">
				<thead>
					<tr>
						<th>ID</th>
						<th>Código</th>
						<th>Disciplina</th>
						<th>Situacao</th>

					</tr>
				</thead>
				<tbody>
					<c:forEach varStatus="i" items="${alunosItemIsencao}" var="alunosItemIsencao" >
					<input name="alunosItemIsencao" value="${alunosItemIsencao.disciplina.codigo}-${i.index}"
								type="hidden" />
						<tr>
							<td>${alunosItemIsencao.id}</td>
							<td>${alunosItemIsencao.disciplina.codigo}
							</td>
							<td>${alunosItemIsencao.disciplina.nome}</td>
							<td>
							
							<c:if test="${alunosItemIsencao.situacao != null}">
									<c:if test="${alunosItemIsencao.situacao == 'deferir'}">
										<h5>DEFERIDO</h5>
									</c:if> 
									<c:if test="${alunosItemIsencao.situacao == 'indeferir'}">
										<h5>INDEFERIDO</h5>
									</c:if> 		
								</c:if> 

								<c:if test="${alunosItemIsencao.situacao == null}">
									<button class="btn btn-success custom-width" type="submit"
										name="btAvaliador" value="deferir-${i.index}" >Deferir</button>
									<button class="btn btn-danger custom-width" type="submit"
										name="btAvaliador" value="indeferir-${i.index}">Indeferir</button>
								</c:if>
							</td>
							<c:if test="${alunosItemIsencao.situacao == 'indeferir'}">
								<td>${alunosItemIsencao.motivo}</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>		
		</div>
	</form>
	<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
</body>
</html>