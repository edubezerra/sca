<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Grade de Disponibilidades</title>
</head>
<body>

	<p>
		<%=request.getAttribute("nomeProfessor")%>
	</p>

	<form action="sca" method=post name="sca">
	<center>
		<select name='nomeDisciplina'>
			<c:forEach items="${requestScope.habilitacoes}" var="disciplina">
				<option value='${disciplina.nome}'>${disciplina.nome}</option>
			</c:forEach>
		</select> 
		<input type="hidden" name="comando" value="adicionarDisciplinaEmGrade">
		<input type="submit" id="btnAdicionarMatriculaEmGrade"
			value="Adicionar Disciplina na Grade">
	</center>
	</form>

	<br>

	<center>
		<table cellpadding=4 cellspacing=2 border=0>
			<th bgcolor="#CCDDEE" colspan=6><font size=5>
					Disciplinas da grade</font></th>
			<tr bgcolor="#F7F7F7">
				<td valign=top><b>Código</b></td>
				<td valign=top><b>Nome</b></td>
				<td valign=top></td>
				<td valign=top></td>
			</tr>

			<c:forEach items="${requestScope.disciplinas}" var="d">
				<tr bgcolor="#F7F7F7">
					<td valign=top><c:out value="${d.codigo}" /></td>
					<td valign=top><c:out value="${d.nome}" /></td>
					<td valign=top><a
						href="sca?comando=excluirDisciplina&id=<c:out value='${d.codigo}'/>">Excluir</a></td>
				</tr>
			</c:forEach>

		</table>
	</center>

</body>
</html>