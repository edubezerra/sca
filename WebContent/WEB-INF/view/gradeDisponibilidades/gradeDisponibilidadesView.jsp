<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<style type="text/css">
body {
	background-image:
		url('http://cdn3.crunchify.com/wp-content/uploads/2013/03/Crunchify.bg_.300.png');
}
</style>
<script type="text/javascript"
	src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
<script type="text/javascript">
	function crunchifyAjax() {
		$.ajax({
			url : 'ajaxtest.html',
			success : function(data) {
				$('#result').html(data);
			}
		});
	}
</script>

<script type="text/javascript">
	$(document).ready(
			function() {
				$('#sampleForm').submit(
						function(event) {
							var matriculaProfessor = $('#matriculaProfessor')
									.val();
							var data = 'matriculaProfessor='
									+ encodeURIComponent(matriculaProfessor);
							$.ajax({
								url : $("#sampleForm").attr("action"),
								data : data,
								type : "GET",

								success : function(response) {
									alert(response);
								},
								error : function(xhr, status, error) {
									alert(xhr.responseText);
								}
							});
							return false;
						});
			});
</script>

</head>

<title>SCA - Fornecer Grade de Disponibilidades</title>

<link href="${pageContext.request.contextPath}/css/base.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/form.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/table.css"
	rel="stylesheet" type="text/css" />
</head>

<t:wrapper>
	<jsp:attribute name="header">
		<h1>SCA - Fornecer Grade de Disponibilidades</h1>
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
				action="${pageContext.request.contextPath}/gradedisponibilidades/validarProfessor"
				method="post">


			<div class="field">
				<div>Matrícula do professor:</div>
			</div>
			<div class="field">
				<input type="text" name="matriculaProfessor" value="" size="30"
						maxlength="16" />
			</div>
			<br />
			<div class="field">
				<input class="lastfield big" type="submit" value="Validar" />
			</div>
		</form>
	</div>

	<div id="footer"></div>

    </jsp:body>
</t:wrapper>