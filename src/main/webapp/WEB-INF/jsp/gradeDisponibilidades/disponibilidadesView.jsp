<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Grade de Disponibilidades</title>

<link href="${rootURL}resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${rootURL}resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${rootURL}resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${rootURL}resources/js/app.js"></script>

</head>
<body class="basic-grey">

	<h1>Grade de Disponibilidades (${requestScope.periodoLetivo})</h1>

	<c:if test="${requestScope.error != null}">
		<div>
			<p class="error">${requestScope.error}</p>
		</div>
	</c:if>

	<c:if test="${requestScope.info != null}">
		<div>
			<p class="info">${requestScope.info}</p>
		</div>
	</c:if>

	<c:if test="${requestScope.habilitacoes != null}">
		<div class="table">
			<div class="row">
				<div class="field">
					<div>
						<b>Código da Disciplina</b>
					</div>
				</div>
				<div class="field">
					<div>
						<b>Nome da Disciplina</b>
					</div>
				</div>
				<div class="field"></div>
			</div>
			<c:forEach items="${requestScope.habilitacoes}" var="habilitacao">
				<div class="row">
					<div class="field">
						<div>${habilitacao.codigo}</div>
					</div>
					<div class="field">
						<div>${habilitacao.nome}</div>
					</div>
					<div class="field">
						<form
							action="${rootUrl}gradedisponibilidades/solicitaAdicaoDisciplina"
							method="post">
							<input type="hidden" name="codigoDisciplina"
								value="${habilitacao.codigo}" /> <input class="lastfield"
								type="submit" value="Adicionar" />
						</form>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:if>

	<br />

	<a class="button_embedded"
		href="${rootUrl}avaliacaoTurma/menuPrincipal"> <input
		type="button" value="Voltar" />
	</a>

</body>
</html>
