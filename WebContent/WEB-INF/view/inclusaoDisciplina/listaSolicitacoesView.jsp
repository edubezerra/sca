<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title>SCA - Matrícula Fora do Prazo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="lista-solicitacoes">
	<div class="container">
		<div class="row text-center">
			<h2>Solicitações de matrícula fora do parazo - <c:out value="${solicitacaoAtual.semestreLetivo}"></c:out></h2>
			<h4>Aluno: <c:out value="${aluno.nome}"></c:out></h4>
			<h4>Matricula: <c:out value="${aluno.matricula}"></c:out></h4>
		</div>
		<div class="row">
			<c:forEach items="${solicitacaoAtual.itensSolicitacao}" var="itemSolicitacao">
				<div class="well">
					<div class="dados-aluno">
						<p><b>Departamento:</b> ${itemSolicitacao.departamento.nome}</p>
						<p><b>Discipilna solicitada:</b>
						${itemSolicitacao.turma.disciplina.codigo} - ${itemSolicitacao.turma.disciplina.nome}</p>
						<p><b>Turma:</b> ${itemSolicitacao.turma.codigo}</p>
						<p><b>Data da solicitação:</b> ${itemSolicitacao.dataSolicitacao}</p>
						<p><b>Observações:</b> ${itemSolicitacao.observacao}</p>
					</div>
					<div class="status text-center">
						<h4>Status</h4>
						<c:set var="classeStatus" scope="page">
							<c:choose>
								<c:when test="${itemSolicitacao.status eq 'INDEFERIDO'}">
									text-danger
								</c:when>
								<c:when test="${itemSolicitacao.status eq 'DEFERIDO'}">
									text-success
								</c:when>
								<c:when test="${itemSolicitacao.status eq 'AGUARDANDO'}">
									text-primary
								</c:when>
							</c:choose>
						</c:set>
						<p class="${classeStatus}"><b>${itemSolicitacao.status}</b></p>
						
						<c:if test="${itemSolicitacao.comprovante != null}">
							<form action="${pageContext.request.contextPath}/inclusaoDisciplina/downloadFile" method="POST" target="_blank">
				          		<input type="hidden" name="solicitacaoId" value="${itemSolicitacao.id}">
								<button type="submit" class="btn btn-link">
									<i class="fa fa-download fa-2x"></i><h4 class="comprovante">Comprovante</h4>
								</button>
							</form>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
		<a class="btn btn-default" href="${pageContext.request.contextPath}/inclusaoDisciplina/homeInclusao">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>
	
</body>
</html>

