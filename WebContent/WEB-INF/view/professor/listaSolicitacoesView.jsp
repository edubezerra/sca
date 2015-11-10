<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
	<meta charset="UTF-8">
	<title>SCA - Inclusão de disciplina</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="lista-solicitacoes">
	<div class="container">
		<div class="row text-center">
			<h2>Solicitações de inclusão de disciplina</h2>
			<h4>Professor: <c:out value="${professor.nome}"></c:out></h4>
			<h4>Matricula: <c:out value="${professor.matricula}"></c:out></h4>
		</div>
		<c:if test="${requestScope.sucesso != null}">
			<div class="row text-center">
				<span class="label label-success">${requestScope.sucesso}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.error != null}">
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<div class="row">
			<c:forEach items="${solicitacoes}" var="solicitacao">
				<c:forEach items="${solicitacao.itemSolicitacao}" var="itemSolicitacao">
					<div class="well">
						<div class="dados-aluno">
							<p><b>Aluno:</b> ${solicitacao.aluno.nome}</p>
							<p><b>Departamento:</b> ${itemSolicitacao.departamento.nome}</p>
							<p><b>Discipilna solicitada:</b>
							${itemSolicitacao.turma.disciplina.codigo} - ${itemSolicitacao.turma.disciplina.nome}</p>
							<p><b>Turma:</b> ${itemSolicitacao.turma.codigo}</p>
							<p><b>Data da solicitação:</b> ${itemSolicitacao.dataSolicitacao}</p>
							<p><b>Opção:</b> ${itemSolicitacao.opcao}</p>
							<p><b>Observações:</b> ${itemSolicitacao.observacao}</p>
						</div>
						<div class="status text-center">
							<h4>Status</h4>
							<c:choose>
								<c:when test="${itemSolicitacao.status eq 'AGUARDANDO'}">
									<form action="${pageContext.request.contextPath}/professor/defineStatusAluno" method="POST">
										<button type="submit" name="status" value="Deferido" class="btn btn-success">
											<i class="fa fa-check"></i> Deferido
										</button>
										<button type="submit" name="status" value="Indeferido" class="btn btn-danger">
											<i class="fa fa-times"></i> Indeferido
										</button>
										<input type="hidden" name="idItemSolicitacao" value="${itemSolicitacao.id}"></input>
										<input type="hidden" name="ano" value="${solicitacao.semestreLetivo.ano}">
						          		<input type="hidden" name="periodo" value="${solicitacao.semestreLetivo.periodo}">
									</form>
								</c:when>
								<c:otherwise>
									<c:set var="classeStatus" scope="page">
										<c:choose>
											<c:when test="${itemSolicitacao.status eq 'INDEFERIDO'}">
												text-danger
											</c:when>
											<c:when test="${itemSolicitacao.status eq 'DEFERIDO'}">
												text-success
											</c:when>
										</c:choose>
									</c:set>
									<p class="${classeStatus}"><b>${itemSolicitacao.status}</b></p>
								</c:otherwise>
							</c:choose>
							
							<c:if test="${itemSolicitacao.comprovante != null}">
								<form action="${pageContext.request.contextPath}/professor/downloadFile" method="POST" target="_blank">
					          		<input type="hidden" name="solicitacaoId" value="${itemSolicitacao.id}">
									<button type="submit" class="btn btn-link">
										<i class="fa fa-download fa-2x"></i><h4 class="comprovante">Comprovante</h4>
									</button>
								</form>
							</c:if>
						</div>
					</div>
				</c:forEach>
			</c:forEach>
			
		</div>
		<a class="btn btn-default" href="${pageContext.request.contextPath}/professor/homeInclusao">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>
	
</body>
</html>