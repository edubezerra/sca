<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>SCA - Sistema de Controle Acadêmico</title>

<link href="${pageContext.request.contextPath}/css/base.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/form.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/table.css"
	rel="stylesheet" type="text/css" />
</head>

<t:wrapper>
	<jsp:attribute name="header">
		<h1>SCA</h1>
    </jsp:attribute>

	<jsp:body>

	<c:if test="${requestScope.error != null}">
		<div>
			<p class="error">${requestScope.error}</p>
		</div>
	</c:if>

	<c:if test="${requestScope.info != null}">
		<div>
			<p class="info">${requestScope.info}</p>
		</div>
	</c:if>

	<div class="table">
		<form class="row"
				action="${pageContext.request.contextPath}/menuPrincipal"
				method="post">


			<div class="field">
				<div>CPF:</div>
			</div>
			<div class="field">
				<input type="text" name="cpf" value="" size="30" maxlength="16" />
			</div>
			<br />
			<div class="field">
				<div>Senha:</div>
			</div>
			<div class="field">
				<input type="password" name="senha" value="" size="30"
						maxlength="16" />
			</div>
			<div class="field">
				<input class="lastfield big" type="submit" value="Entrar" />
			</div>
		</form>
	</div>

	<div id="footer"></div>

    </jsp:body>
</t:wrapper>