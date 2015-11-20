<%@page import="br.cefetrj.sca.dominio.isencoes.ItemSolicitacaoIsencaoDisciplinas"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Solicitacoes de Isencao</title>
<link rel="stylesheet" type="text/css" href="site.css" />
<script type="text/javascript" src="resources/js/jquery-1.7.2.min.js"></script>
</head>
<body>
	<%
		List<ItemSolicitacaoIsencaoDisciplinas> itensSolicitacao = (ArrayList) session.getAttribute("itensSolicitacao");
	%>
	<div class="caixa">
		<div>
			<form action="AdicionarSol.do?method=registrar" method="POST">
				<h1>Solicitacoes de Isen&ccedil;&atilde;o</h1>
				<table border="1" align="center" cellpadding="0" cellspacing="0"
					id="tabelaSol">
					<tr>
						<th></th>
						<th colspan="2">Disciplinas Internas</th>
						<th colspan="2">Disciplinas Externas</th>
						<th></th>
					</tr>
					<tr>
						<th>Numero da Solicitacao</th>
						<th>Nome</th>
						<th>Codigo</th>
						<th>Nome</th>
						<th>Codigo</th>
						<th></th>
					</tr>
					<%
						if (itensSolicitacao != null && !itensSolicitacao.isEmpty()) {
							for (int i = 0; i < itensSolicitacao.size(); i++) {
								out.write("<tr>");
								out.write("<td>" + (i + 1) + "</td>");
								out.write("<td>" + itensSolicitacao.get(i).getDisciplina().getNome() + "</td>");
								out.write("<td>" + itensSolicitacao.get(i).getDisciplina().getCodigo() + "</td>");
								out.write("<td>" + itensSolicitacao.get(i).getNomeDisExterna() + "</td>");
								out.write("<td>" + itensSolicitacao.get(i).getCodDisExterna() + "</td>");
								out.write("<td><a href='AdicionarSol.do?method=remover&id=" + i + "'>remover</a></td>");
								out.write("</tr>");
							}
						}
					%>
				</table>
				<input type="button" value="Adicionar" class="button_right"
					onclick="window.open('AdicionarSol.do?method=exibir','','width=800,height=500')" />
				<input type="submit" value="Registrar" class="button_left"
					id="Registrar" />
			</form>
		</div>
	</div>

	<script type="text/javascript">
		$("#Registrar").click(function() {
			if ($("#tabelaSol tr:nth-child(3)").length == 0) {
				alert('Adicione ao menos um item.');
				return false;
			} else {
				return true;
			}
		});
	<%if (request.getAttribute("idSolicitacao") != null) {
				out.write("alert(' Solicitacao gerada. Número: " + request.getAttribute("idSolicitacao") + ".');");
			}%>
		
	</script>
</body>