<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Seleção das Turmas</title>
</head>
<body>

	<form action="<%=request.getContextPath()%>/visualizacaoAvaliacaoDiscente/Escolhaturma" method="get">
		<c:forEach items="${turmas}" var="t">
		<input type="radio" name="cod" value="${t.id}">${t.disciplina} ${t.codigo}
		
<br />
		</c:forEach>
		<input type="submit" value="Enviar escolha da Turma">

	</form>


</body>
</html>