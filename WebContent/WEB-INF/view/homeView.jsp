<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>SCA - Avaliação de Turmas</title>

<link href="${pageContext.request.contextPath}/css/base.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/form.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/table.css"
	rel="stylesheet" type="text/css" />
</head>

<t:wrapper>
	<jsp:attribute name="header">
		<h1>Avaliação de Turmas por Discentes</h1>
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

<p>A avaliação de Turmas por Discentes ocorre em dois momentos: no primeiro e no segundo 
semestre letivo de cada ano por meio de formulário eletrônico.</p>

<p>As avaliações permanecem no banco de dados, sendo processadas pela Coordenação do Curso. 
Os resultados são repassados ao professor de cada turma avaliada, na forma consolidada, 
após o término do semestre letivo, para que os alunos não se sintam inibidos 
ao avaliar seus atuais professores.</p>


	Para acesso ao formulário de avaliação, utilize os campos abaixo. Entre com as mesmas credenciais que você utiliza para acesso ao
	<a href="http://eic.cefet-rj.br/moodle/">Moodle da EIC</a>
	<br>
	<br>

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