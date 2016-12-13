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

<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/jquery.popconfirm.js"></script>
<title>Isenção de Disciplinas</title>
</head>
<body>

	<div class="container">

		<div class="row text-center">
			<h2>Pedidos de Isenção com Análise Pendente</h2>
			<h4>Professor avaliador: ${requestScope.professor.nome}
				(Matrícula ${professor.matricula})</h4>
		</div>

		<div>
			<form
				action="${pageContext.request.contextPath}/isencaoDisciplina/analisaItensPedidoIsencao"
				method="POST">

				<input type="hidden" name="idDepartamento" value="${idDepartamento}">

				<div class="table-responsive" align="center">
					<table class="table">

						<caption>Pedidos de Isenção com Análise Pendente</caption>

						<thead>
							<tr>
								<th>Matrícula</th>
								<th>Nome</th>
								<th></th>

							</tr>
						</thead>

						<tbody>

							<c:forEach items="${alunosSolicitantes}" var="aluno">

								<tr>
									<td style="vertical-align:middle">${aluno.matricula}</td>
									<td style="vertical-align:middle">${aluno.nome}</td>
									<td style="vertical-align:middle">
										<button class="btn btn-primary text-center" type="submit"
											value="${aluno.matricula}" name="matriculaAluno">Visualizar
											Pedido</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>

					</table>
				</div>

			</form>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>
</body>
</html>