<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>

<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>SCA - Requerimento de Matrícula Fora do Prazo</title>

<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>

</head>

<body class="lista-solicitacoes">
	<div class="container">
		<div class="row text-center">
			<h2>Requerimento de matrícula fora do prazo -
				${requerimento.semestreLetivo}</h2>

			<h3>Aluno: ${requerimento.aluno.nome} (matrícula ${requerimento.aluno.matricula})</h3>
		</div>
		<div class="row">
			<c:forEach items="${requerimento.itens}"
				var="itemSolicitacao">
				<div class="well">
					<div class="dados-aluno">
						<p>
							<b>Departamento:</b> ${itemSolicitacao.departamento.nome}
						</p>
						<p>
							<b>Item solicitado:</b>
							${itemSolicitacao.turma.disciplina.codigo} -
							${itemSolicitacao.turma.disciplina.nome}
						</p>
						<p>
							<b>Turma:</b> ${itemSolicitacao.turma.codigo}
						</p>
						<p>
							<b>Data e hora do requerimento:</b>
							<fmt:formatDate pattern="dd/MM/yyyy, HH:mm"
								value="${itemSolicitacao.dataSolicitacao}" />
						</p>
						<p>
							<b>Observações:</b>
							<c:if test="${itemSolicitacao.observacao eq ''}">
								sem observações.
							</c:if>
							<c:if test="${itemSolicitacao.observacao != null}">
								${itemSolicitacao.observacao}
							</c:if>
						</p>
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
						<p class="${classeStatus}">
							<b>${itemSolicitacao.status}</b>
						</p>
					</div>
				</div>
			</c:forEach>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/matriculaForaPrazo/requerimento/visualizarRequerimentos">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>
</html>

