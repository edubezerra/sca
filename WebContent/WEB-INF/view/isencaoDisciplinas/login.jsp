<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>
	Login
</title>
<link rel="stylesheet" type="text/css" href="site.css" />
</head>
<body>
	<div id="header">
		<div id="barraSuperior">
		</div> 
		 <div id="header_cefet">
			<div class="logo_cefet"><img src="imagens/logoCefet.gif" alt="logo do CEFET/RJ" align="left" width="50px" height="50px"></div>
			<div class="nome_cefet">
			   <h1 class="titulo_site">
				  <span class="titulo1">CEFET/RJ -</span> 
				  <span class="titulo2">Centro Federal de Educa&ccedil;&atilde;o Tecnol&oacute;gica Celso Suckow da Fonseca</span>
			   </h1>
			</div>        
		 </div>
		</div>
		<div id="corpo-login">
		<div id="login">
			<form action="Login.do" method="POST">
					<table align="center" cellpadding="0" cellspacing="0">
						<tbody>
							<tr>
								<td id="col1" colspan="2">
									&Aacute;rea Restrita</td>
							</tr>
							<tr>
								<td id="col2" colspan="2"></td>
							</tr>
							<tr>
								<td id="col3" colspan="2">
									Acesso ao Portal</td>
							</tr>
							<tr>
								<td class="col46" colspan="2">
									Matr&iacute;cula:</td>
							</tr>
							<tr>
								<td id="col5" colspan="2">
									<input type="text" id="matricula" name="matricula" size="30" style="width:200px;">
								</td>
							</tr>
							<tr>
								<td class="col46" colspan="2">
									Senha:</td>
							</tr>
							<tr>
								<td id="col7" colspan="2" style="padding-bottom:10px;">
									<input type="password" id="password" name="password" size="30" style="width:200px;">

									<a href="#" style="font-size:9px;">
										Esqueci minha senha</a>
									<p style="color:red;"><%out.print(request.getAttribute("message")==null?"":request.getAttribute("message"));%></p>
								</td>
							</tr>
							<tr>
								<td id="col8">
									<input class="button" type="submit" value="Entrar">
								</td>
							</tr>
							</tbody>
					</table>
			</form>
		</div>
	</div>
</body>