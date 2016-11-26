<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Seleção das Turmas</title>
<style>
div.div_RootBody {
	position: relative;
	border: 2px solid White;
	border-radius: 7px;
	background: WhiteSmoke;
	font: normal 14px Arial;
	font-family: Arial, Helvetica, sans-serif;
	color: Black;
	padding: 0px 1em;
	text-align: left;
}
</style>
</head>
<body>
<dir>
	<form
		action="<%=request.getContextPath()%>/visualizacaoAvaliacaoDiscente/Escolhaturma"
		method="get">
		<c:forEach items="${turmas}" var="t">
			<input type="radio" name="cod" value="${t.id}">${t.disciplina.nome} ${t.codigo}
		
<br />
		</c:forEach>
		<input type="submit" value="Enviar escolha da Turma"> <a
			class="button_embedded"
			href="${pageContext.request.contextPath}/avaliacaoTurma/menuPrincipal">
			<input type="button" value="Voltar" />
		</a>

	</form>
</dir>

</body>
</html>