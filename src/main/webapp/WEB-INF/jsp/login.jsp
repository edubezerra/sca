<!DOCTYPE html>
<%@include file="taglib.jsp"%>
<html>
<head>
<title>SCA - Login</title>
<link href="${rootURL}resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<link href="${rootURL}resources/login/login.css" media="screen"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${rootURL}resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${rootURL}resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${rootURL}resources/js/app.js"></script>
</head>
<body>
	<div class="container login-container">
		

		<div class="login-box">
			
			<div class="login-header">
				<h2>Sistema de Controle Acadêmico</h2>
			</div>
			
			<div class="login-body">
				<form:form id="loginForm" method="post" action="${rootURL}login"
				modelAttribute="user" class="form-horizontal" role="form">
					
					<c:if test="${param.error != null}">
						<div class="alert alert-danger">Login ou senha inválidos.</div>
					</c:if>
					<c:if test="${param.logout != null}">
						<div class="alert alert-success">Logout realizado com sucesso.
						</div>
					</c:if>
					
					<div class="form-group">
						<label for="username">Usuário</label>
						<input type="text" id="username" name="username"
								class="form-control" placeholder="UserName" />
					</div>
					<div class="form-group">
						<label for="password">Senha</label>
						<input type="password" id="password" name="password"
							class="form-control" placeholder="Password" />
					</div>
					<div class="btn-center">
						<input type="submit" class="btn btn-primary btn-lg" value="Login">
					</div>
	
				</form:form>
			</div>
			
		</div>
	</div>


</body>
</html>