<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="br.cefetrj.sca.dominio.Aluno"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Área Restrita</title>

<link rel="stylesheet" type="text/css" href="site.css" />
</head>
<body>
	<%
		Aluno aluno = (Aluno) session.getAttribute("aluno");
	%>
	<div id="header">
		<div id="barraSuperior"></div>
		<div id="header_cefet" style="">
			<div class="logo_cefet">
				<img src="imagens/logoCefet.gif" alt="logo do CEFET/RJ" align="left"
					width="50px" height="50px">
			</div>
			<div class="nome_cefet">
				<h1 class="titulo_site">
					<span class="titulo1">CEFET/RJ -</span> <span class="titulo2">Centro
						Federal de Educa&ccedil;&atilde;o Tecnol&oacute;gica Celso Suckow
						da Fonseca</span>
				</h1>
			</div>
		</div>
	</div>
	<div id="container">
		<div id="corpo-menu">
			<div id="menu">
				<table align="center" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<td id="sessionVars" class="opcao" colspan="2">Matricula: <%
								out.write(aluno.getMatricula().toString());
							%><br />
								Nome: <%
								out.write(aluno.getNome().toString());
							%><br /> Curso: <%
								out.write(aluno.getVersaoCurso().getCurso().getNome().toString());
							%><br />
								<a href="Login.do?logout=logout">Logout</a>
							</td>
						</tr>

						<tr>
							<td id="col1" colspan="2">Menu</td>
						</tr>
						<tr>
							<td id="col2" colspan="2"></td>
						</tr>
						<tr>
							<td class="opcao" colspan="2"><a
								href="Resultado de Solicitacao.jsp" target="frame_corpo">Resultado
									de Solicita&ccedil;&atilde;o</a></td>
						</tr>
						<tr>
							<td class="opcao" colspan="2"><a
								href="Solicitacoes de Isencao.jsp" target="frame_corpo">Solicita&ccedil;&otilde;es
									de Isen&ccedil;&atilde;o</a></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div id="frame">
			<iframe src="" name="frame_corpo" seamless="yes" width="1000"
				height="960"></iframe>
		</div>
	</div>
</body>