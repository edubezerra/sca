<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>SCA - Avaliação de Turmas</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
	
</head>
<body class="login-view">
	<div class="container">
		<div class="row">
			<h1 class="text-center">SCA - Sistema de Controle Acadêmico</h1>
			<hr/>
			<c:if test="${requestScope.error != null}">
				<span class="label label-danger">${requestScope.error}</span>
			</c:if>
		
			<c:if test="${requestScope.info != null}">
				<span class="label label-default">${requestScope.info}</span>
			</c:if>
			
			<p>Para acesso ao formulário de avaliação, utilize os campos abaixo. Entre com as mesmas credenciais que você utiliza para acesso ao
			<a href="http://eic.cefet-rj.br/moodle/">Moodle da EIC</a></p>
		</div>
		<div class="row">
			<form class="form-signin" action="${pageContext.request.contextPath}/autenticacao/menuPrincipal" method="post">
		        <label for="inputCpf">CPF:</label>
		        <input type="text" id="inputCpf" class="form-control" name="cpf" placeholder="CPF" required autofocus/>
		        <label for="inputPassword">Senha:</label>
		        <input type="password" id="inputPassword" class="form-control" name="senha" placeholder="Senha" required maxlength="16" />
		        <button class="btn btn-lg btn-primary btn-block" type="submit">Entrar</button>
		    </form>
		</div>
	</div>
	<div id="footer"></div>
</body>
	