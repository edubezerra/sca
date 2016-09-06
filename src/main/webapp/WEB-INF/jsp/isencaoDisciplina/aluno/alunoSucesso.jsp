<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<title>SCA - Isenção de Disciplinas</title>

<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-select.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap-select.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/app.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/vendor/font-awesome/css/font-awesome.min.css">

<!-- Required for tablesorter and jquery.popconfirm-->
<script src="http://code.jquery.com/jquery-1.12.1.min.js"></script>

<!-- Bootstrap stylesheet -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/tablesorter/css/bootstrap.min.css">
<!-- bootstrap widget theme -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/tablesorter/css/theme.bootstrap_2.css">
<!-- tablesorter plugin -->
<script
	src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.js"></script>
<!-- tablesorter widget file - loaded after the plugin -->
<script
	src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.widgets.js"></script>
<!-- pager plugin -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/tablesorter/css/jquery.tablesorter.pager.css">
<script
	src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.pager.js"></script>

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/jquery.popconfirm.js"></script>
</head>
<body>

	<div class="container">
		<div class="row text-center">
			<h3>Isenção de Disciplinas</h3>
		</div>
		<hr />
		<div class="row">
			<h5>
				<b>Aluno:</b>
				<c:out value="${requestScope.dadosAluno.nomeAluno}"></c:out>
				(Matrícula:
				<c:out value="${requestScope.matricula}"></c:out>
				)
			</h5>
			<h5>
				<b>Curso:</b>
				<c:out value="${requestScope.dadosAluno.curso.sigla}"></c:out>
				-
				<c:out value="${requestScope.dadosAluno.curso.nome}"></c:out>
				(Grade:
				<c:out value="${requestScope.dadosAluno.versaoCurso}"></c:out>
				)
			</h5>
		</div>
		<br />
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
		<c:if test="${requestScope.info != null}">
			<div class="row">
				<span class="label label-info">${requestScope.info}</span>
			</div>
		</c:if>

		<!-- <h4> -->
		<%-- <b> Aluno: ${aluno.nome }</b> --%>
		<!-- 	<br></br> -->
		<%-- 	<b> Matrícula: ${aluno.matricula }</b> --%>
		<!-- 	<br></br> -->
		<%-- 	<c:if test="${aluno.processoIsencao.situacaoProcessoIsencao != null}"> --%>
		<%-- 	Situação: ${aluno.processoIsencao.situacaoProcessoIsencao} --%>
		<%-- 	</c:if> --%>
		<!-- 	<br></br> -->

		<!-- </h4> -->

		<c:if test="${aluno.processoIsencao != null}">
			<div class="table-responsive">
				<table class="table">
					<thead>
						<tr>
							<th>ID</th>
							<th>Disciplina</th>
							<th>Situação</th>
							<th>Data Analise</th>
							<th>Motivo</th>
						</tr>
					</thead>

					<tbody>
						<c:forEach items="${itemIsencaoByProcessoIsencao}"
							var="itemIsencaoByProcessoIsencao">
							<tr>

								<td>${itemIsencaoByProcessoIsencao.id}</td>
								<td>${itemIsencaoByProcessoIsencao.disciplina.nome}<c:if
										test="${itemIsencaoByProcessoIsencao.disciplinaAssociada != null}">
										<div>
											Disciplinas Associadas: <br>
											${itemIsencaoByProcessoIsencao.disciplinaAssociada}
										</div>
									</c:if>
								</td>
								<td><c:if
										test="${itemIsencaoByProcessoIsencao.situacao == 'deferir'}">
										DEFERIDO
									</c:if> <c:if
										test="${itemIsencaoByProcessoIsencao.situacao == 'indeferir'}">
										INDEFERIDO
									</c:if></td>
								<td><fmt:formatDate pattern="dd/MM/yyyy"
										value="${itemIsencaoByProcessoIsencao.dataAnalise}" /></td>
								<td>${itemIsencaoByProcessoIsencao.motivo}</td>
								<td>${itemIsencaoByProcessoIsencao.observacao}</td>
							</tr>
						</c:forEach>
					</tbody>

				</table>
			</div>
			<div>
				<c:if
					test="${aluno.processoIsencao.situacaoProcessoIsencao == null }">
					<a class="btn btn-success custom-width" type="submit"
						href="${pageContext.request.contextPath}/isencaoDisciplina/alunoView">
						<i class="fa fa-arrow-left"> </i> Criar Nova Isenção
					</a>
				</c:if>

			</div>
		</c:if>
		<c:if test="${aluno.processoIsencao == null}">
			<h5 align="center">Não existe Processo de Isenção em aberto</h5>
			<br>
			<div>
				<a class="btn btn-success custom-width" type="submit"
					href="${pageContext.request.contextPath}/isencaoDisciplina/alunoView">
					<i class="fa fa-arrow-left"> </i> Criar Isenção
				</a>
			</div>
		</c:if>


		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>
</html>