<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="br.cefetrj.sca.dominio.Disciplina"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Adicionar Solicitação de Isenção</title>
<link rel="stylesheet" type="text/css" href="site.css" />
<script type="text/javascript" src="resources/js/jquery-1.7.2.min.js"></script>
</head>
<body>
	<%
		List<Disciplina> disciplinas = (ArrayList<Disciplina>) request.getAttribute("disciplinas");
	%>
	<div class="popup">
		<div>
			<form action="AdicionarSol.do?method=adicionar" method="POST">
				<h1>Adicionar Solicita&ccedil;&atilde;o</h1>
				<h2>Disciplina Interna</h2>
				<select name="disciplinaInt">
					<%
						if (disciplinas != null && !disciplinas.isEmpty()) {
							for (int i = 0; i < disciplinas.size(); i++) {
								out.write("<option value='" + disciplinas.get(i).getId() + "'/>" + disciplinas.get(i).getNome());
							}
						}
					%>
				</select>
				<h2>Disciplina Externa</h2>
				<h3>Código:</h3>
				<input type="text" name="codExterna" id="codExterna" /><br />
				<h3>Nome:</h3>
				<input type="text" name="nomeExterna" id="nomeExterna" /><br /> <input
					type="submit" value="Adicionar" />
			</form>
		</div>
	</div>
</body>