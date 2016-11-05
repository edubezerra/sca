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
<title>Importação de Dados</title>
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

	$(document).ready(function() {
		$("#formAC").validate({
			rules : {
				tabAtividadeComplementarFile : {
					required : true,
					extension : "xls"
				}
			},
			messages : {
				tabAtividadeComplementarFile : {
					required : "Escolha um arquivo.",
					extension : "É necessário que o formato seja xls."
				}
			},
			errorPlacement : function(error, element) {
				error.insertAfter(element.parent());
			}
		});

		$("#tabAtividadeComplementarFile").change(function() {
			$("#uploadFileAC").val($("#tabAtividadeComplementarFile").prop('files')[0]['name']);
		});

	});

	$(document).ready(function() {
		$("#formProfessor").validate({
			rules : {
				professorFile : {
					required : true,
					extension : "xls"
				}
			},
			messages : {
				professorFile : {
					required : "Escolha um arquivo.",
					extension : "É necessário que o formato seja xls."
				}
			},
			errorPlacement : function(error, element) {
				error.insertAfter(element.parent());
			}
		});

		$("#professorFile").change(function() {
			$("#uploadFileProfessor").val($("#professorFile").prop('files')[0]['name']);
		});

	});
</script>

<body>

	<div class="content">
		<h1 class="text-center">Importação de Dados</h1>
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
			action="${pageContext.request.contextPath}/importacaoDados/importacaoDados"
			method="post" enctype="multipart/form-data">

			<br /> <br />
			<div class="col-md-6 col-md-offset-2">
				<label for="tipoImportacao">Tipo importação:</label> <select
					data-title="testando..." class="form-control" name="tipoImportacao"
					id="tipoImportacao" required>
					<option value="" label="Escolha o tipo de importação desejado"
						selected disabled></option>
					<c:forEach items="${descritor.descritorImportadorMap}"
						var="tipoImportacao">
						<option value="${tipoImportacao.key}">
							${tipoImportacao.value}</option>
					</c:forEach>
				</select>
			</div>

			<input id="uploadFile" value="Nenhum arquivo selecionado..."
				disabled="disabled" class="uploadedFile" />
			<div class="fileUpload btn btn-primary">
				<span>Procurar...</span> <input name="historicoFile"
					id="historicoFile" type="file" class="upload" />
			</div>

			<br /> <br />
			<button class="btn btn-lg btn-primary btn-block" type="submit">Importar
				Histórico</button>
		</form>
	</div>


	<hr />

	<div class="content">
		<h2 class="text-center">Importação de Tabela de Atividades
			Complementares</h2>

		<form id="formAC"
			action="${pageContext.request.contextPath}/importacaoDados/importacaoTabelaAtividadesComplementares"
			method="post" enctype="multipart/form-data">

			<input type="hidden" name="tipoImportacao" value="6"> <br />
			<br /> <input id="uploadFileAC"
				value="Nenhum arquivo selecionado..." disabled="disabled"
				class="uploadedFile" />
			<div class="fileUpload btn btn-primary">
				<span>Procurar...</span> <input name="tabAtividadeComplementarFile"
					id="tabAtividadeComplementarFile" type="file" class="upload" />
			</div>

			<br /> <br />
			<button class="btn btn-lg btn-primary btn-block" type="submit">Importar</button>
		</form>
	</div>

	<hr />

	<div class="content">
		<h2 class="text-center">Importação de Professores</h2>

		<form id="formProfessor"
			action="${pageContext.request.contextPath}/importacaoDados/importacaoProfessores"
			method="post" enctype="multipart/form-data">

			<input type="hidden" name="tipoImportacao" value="7"> <br />
			<br /> <input id="uploadFileProfessor"
				value="Nenhum arquivo selecionado..." disabled="disabled"
				class="uploadedFile" />
			<div class="fileUpload btn btn-primary">
				<span>Procurar...</span> <input name="professorFile"
					id="professorFile" type="file" class="upload" />
			</div>

			<br /> <br />
			<button class="btn btn-lg btn-primary btn-block" type="submit">Importar</button>
		</form>
	</div>
</body>
</html>