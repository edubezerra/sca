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
<title>Importação de Grade Curricular</title>
</head>

<script type="text/javascript">
	$(document).ready(function() {
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

		$("#historicoFile").change(function() {
			$("#uploadFile").val($("#historicoFile").prop('files')[0]['name']);
		});

	});

	$(document).ready(
			function() {
				$("#formAC").validate({
					rules : {
						tabGradeCurricularFile : {
							required : true,
							extension : "xls"
						}
					},
					messages : {
						tabGradeCurricularFile : {
							required : "Escolha um arquivo.",
							extension : "É necessário que o formato seja xls."
						}
					},
					errorPlacement : function(error, element) {
						error.insertAfter(element.parent());
					}
				});

				$("#tabGradeCurricularFile").change(
						function() {
							$("#uploadFile")
									.val(
											$("#tabGradeCurricularFile").prop(
													'files')[0]['name']);
						});

			});
</script>

<body>

	<div class="content">
		<h1 class="text-center">Importação de Grade Curricular</h1>
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
			action="${pageContext.request.contextPath}/importacaoDados/importacaoGradeCurricular"
			method="post" enctype="multipart/form-data">

			<br /> <br /> <input id="uploadFile"
				value="Nenhum arquivo selecionado..." disabled="disabled"
				class="uploadedFile" />
			<div class="fileUpload btn btn-primary">
				<span>Procurar...</span> <input name="tabGradeCurricularFile"
					id="tabGradeCurricularFile" type="file" class="upload" />
			</div>

			<br /> <br />
			<button class="btn btn-lg btn-primary btn-block" type="submit">Importar</button>
		</form>
	</div>

</body>
</html>