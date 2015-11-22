<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>SCA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>

<body class="home-inclusao text-center">
	<div class="container">
		<div class="row">
			<h2>Solicitação de Isenção de Disciplinas</h2>
			<h4>Aluno: <c:out value="${requestScope.aluno.nome}"></c:out> (Matrícula: <c:out value="${requestScope.aluno.matricula}">)</c:out></h4>
		</div>
		<c:if test="${requestScope.sucesso != null}">
			<div class="row">
				<span class="label label-success">${requestScope.sucesso}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.error != null}">
			<div class="row">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<div class="row">
			<c:choose>
				<c:when test="${requestScope.solicitacao == null}">
					<div class="vcenter well ">
						<p>Não há solicitação iniciada.</p>
					</div>
				</c:when>
				<c:otherwise>
					<div class="table-responsive">
						<h4>Solicitação:</h4>
						<table class="table table-bordered text-center">
							<thead>
								<tr>
									<th class="text-center">Semestre Letivo</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<tr>
						          <td>${semestreLetivo}</td>
						          <td>
						          	<form action="${pageContext.request.contextPath}/isencaoDisciplinas/listarSolicitacoes" method="POST">
										<button type="submit" class="btn btn-link">Detalhes</button>
									</form>
								</td>
						        </tr>
							</tbody>
						</table>
					</div>
				</c:otherwise>
			</c:choose>
			<div class="row">
						<form action="${pageContext.request.contextPath}/isencaoDisciplinas/iniciaSolicitaIsencaoDisciplinas" method="POST">
							<button type="submit" class="btn btn-primary">Fazer Solicitação</button>
						</form>
			</div>
		</div>
		<a class="btn btn-default" href="${pageContext.request.contextPath}/avaliacaoTurma/menuPrincipal">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>
	
</body>

</html>
