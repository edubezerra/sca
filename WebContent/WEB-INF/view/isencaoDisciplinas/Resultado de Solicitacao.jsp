<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="br.cefetrj.sca.dominio.Disciplina"%>
<%@page import="br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas"%>
<%@page import="br.cefetrj.sca.dominio.isencoes.ItemSolicitacaoIsencaoDisciplinas"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Resultado de Solicitacao</title>
<link rel="stylesheet" type="text/css" href="site.css" />
<script type="text/javascript" src="resources/js/jquery-1.7.2.min.js"></script>
</head>
<body>
	<%
	SolicitacaoIsencaoDisciplinas solicitacao = (SolicitacaoIsencaoDisciplinas) request.getAttribute("solicitacao");
	%>
	<div class="caixa">
		<div>
			<form action="ResultadoDeSolicitacao.do" method="POST">
				<h1>Resultado de Solicita&ccedil;&atilde;o</h1>
				<h2>Solicita&ccedil;&otilde;es</h2>
				Código <input type="text" name="codigo" id="codigo" /> <input
					type="submit" value="Buscar" class="button_left" id="Buscar"
					name="Buscar" />
				<table border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<th colspan="2">Disciplinas Internas</th>
						<th colspan="2">Disciplinas Externas</th>
					</tr>
					<tr>
						<th>Nome</th>
						<th>Codigo</th>
						<th>Nome</th>
						<th>Codigo</th>
						<th>Situação</th>

						<%
							if (solicitacao != null && !solicitacao.getItensSolicitacao().isEmpty()) {
								for (int i = 0; i < solicitacao.getItensSolicitacao().size(); i++) {
									ItemSolicitacaoIsencaoDisciplinas itemAtual = solicitacao.getItensSolicitacao().get(i);
									out.write("<tr>");
									out.write("<td>" + itemAtual.getDisciplina().getNome() + "</td>");
									out.write("<td>" + itemAtual.getDisciplina().getCodigo() + "</td>");
									out.write("<td>" + itemAtual.getNomeDisExterna() + "</td>");
									out.write("<td>" + itemAtual.getCodDisExterna() + "</td>");
									out.write("<td>" + itemAtual.getSituacaoItem().getValue() + "</td>");
									out.write("</tr>");
								}
							}
						%>
					
				</table>
			</form>
		</div>
	</div>
	<script>
		$("#Buscar").click(function() {
			if ($("#codigo").val() == "") {
				alert('Insira um valor.');
				return false;
			}
		});
	</script>
</body>