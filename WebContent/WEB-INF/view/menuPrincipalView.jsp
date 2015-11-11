<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>SCA - Home</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<body class="basic-grey">

	<h1>Menu Principal</h1>

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

	<h1>Aluno</h1>
	<ul>
		<li><a
			href="${pageContext.request.contextPath}/avaliacaoTurma/avaliacaoTurmas">
				Avaliar turmas (${requestScope.periodoLetivo})</a></li>
		<li><a
			href="${pageContext.request.contextPath}/gradedisponibilidades/apresentarFormulario">
				Fornecer Grade de Disponibilidades (${requestScope.periodoLetivo})</a></li>
		<li><a
			href="${pageContext.request.contextPath}/inclusaoDisciplina/homeInclusao">
				Solicitar Matrícula Fora do Prazo</a></li>
	</ul>

	<h1>Professor</h1>
	<ul>
		<li><a href="${pageContext.request.contextPath}/professor/homeInclusao/"> Avaliar
				Solicitações de Matrícula Fora do Prazo</a></li>
	</ul>

	<h1>Comum</h1>
	<ul>
		<li><a href="${pageContext.request.contextPath}/logout"> Sair
				do sistema</a></li>
	</ul>

</body>
</html>
