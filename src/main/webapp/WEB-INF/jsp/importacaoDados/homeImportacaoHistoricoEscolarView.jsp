<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta content="charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/sca/resources/bootstrap/css/bootstrap.css" media="screen"
	rel="stylesheet" type="text/css" />
<link href="/sca/resources/importadorHistorico/importadorHistorico.css"
	media="screen" rel="stylesheet" type="text/css" />
<script src="/sca/resources/jquery/jquery-1.10.2.js"></script>
<script src="/sca/resources/jquery/jquery.validate.min.js"></script>
<script src="/sca/resources/jquery/jquery.additional-methods.js"></script>
<title>Importação de Alunos e Históricos Escolares</title>
</head>

<style>
@import
	url('//netdna.bootstrapcdn.com/bootstrap/2.3.2/css/bootstrap.min.css');

.container {
	margin-top: 30px;
	width: 400px;
}
</style>

<script type="text/javascript">
	var progress = setInterval(function() {
		var $bar = $('.bar');

		if ($bar.width() >= 400) {
			clearInterval(progress);
			$('.progress').removeClass('active');
		} else {
			$bar.width($bar.width() + 40);
		}
		$bar.text($bar.width() / 4 + "%");
	}, 800);

	$(document).ready(
			function() {
				$("#formHistorico").validate({
					rules : {
						historicoFile : {
							required : true,
							extension : "xls"
						}
					},
					messages : {
						historicoFile : {
							required : "Escolha um arquivo.",
							extension : "É necessário que o formato seja xls."
						}
					},
					errorPlacement : function(error, element) {
						error.insertAfter(element.parent());
					}
				});

				$("#tabHistoricoEscolar").change(
						function() {
							$("#uploadFile")
									.val(
											$("#tabHistoricoEscolar").prop(
													'files')[0]['name']);
						});

			});
</script>

<body>

	<div class="content">
		<h1 class="text-center">Importação de Alunos e Históricos
			Escolares</h1>
		<hr />
		<br />

		<div class="col-md-6 col-md-offset-2">
			<c:if test="${not empty response}">
				<c:set var="messages" value="${fn:split(response, ';')}" />
				<c:choose>
					<c:when test="${fn:contains(response, 'Erro')}">
						<div class="alert alert-danger">
							<table>
								<c:forEach items="${messages}" var="message">
									<tr>
										<td align="left"><c:out value="${message}" /></td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-success">
							<table>
								<c:forEach items="${messages}" var="message">
									<tr>
										<td align="left"><c:out value="${message}" /></td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</c:otherwise>
				</c:choose>
			</c:if>
		</div>
	</div>

	<div class="content">
		<form id="formHistorico"
			action="${pageContext.request.contextPath}/importacaoDados/importacaoHistoricoEscolar"
			method="post" enctype="multipart/form-data"
			onsubmit="this.submit.disabled = true;">

			<br /> <br /> <input id="uploadFile"
				value="Nenhum arquivo selecionado..." disabled="disabled"
				class="uploadedFile" />
			<div class="fileUpload btn btn-primary">
				<span>Procurar (11.02.05.99.60)...</span> <input
					name="tabHistoricoEscolar" id="tabHistoricoEscolar" type="file"
					class="upload" />
			</div>

			<br /> <br />
			<button id="submit" class="btn btn-lg btn-primary btn-block"
				type="submit">Importar</button>

			<br /> <br />
			<div class="container">
				<div class="progress progress-striped active">
					<div class="bar" style="width: 0%;"></div>
				</div>
			</div>
		</form>
	</div>

</body>
</html>