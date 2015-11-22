<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html data-ng-app="isencaoDisciplina"
	data-ng-controller="isencaoController">

<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>SCA - Requerimento de Isenção de Disciplinas</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/vendor/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<script
	src="${pageContext.request.contextPath}/js/vendor/jquery-1.11.3.min.js"></script>
<script
	src="${pageContext.request.contextPath}/js/vendor/angular.min.js"></script>
<script>
	var urlIsencao = "${pageContext.request.contextPath}/rest/disciplina";
	var numeroSolicitacoes = 0;
	var solicitacoesRestantes = 3 - numeroSolicitacoes;
</script>

<script src="${pageContext.request.contextPath}/js/app.js"></script>
<script src="${pageContext.request.contextPath}/js/mainIsencao.js"></script>
</head>

<body class="inclusao-disciplina">
	<div class="container">
		<div class="row">
			<h1 class="text-center">Requerimento de Isenção de Disciplinas</h1>
		</div>
		<div class="text-inline">
			<h4 class="aluno">Aluno: ${requestScope.aluno.nome}</h4>
			<h4>Matrícula: ${requestScope.aluno.matricula}</h4>
		</div>
		<hr />
		<c:if test="${requestScope.error != null}">
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<div class="row">
			<form
				action="${pageContext.request.contextPath}/isencaoDisciplinas/incluiSolicitacao"
				method="post" enctype="multipart/form-data">
				<div class="departamento-select">
					<p>
						<b>Departamento:</b>
					</p>
					<select class="form-control" name="departamento" required>
						<option value="" label="Escolha o departamento desejado" selected
							disabled></option>
						<c:forEach items="${requestScope.departamentos}"
							var="departamento">
							<option value="${departamento.codigoDepartamento}">
								${departamento.nomeDepartamento}</option>
						</c:forEach>
					</select>
				</div>

				<hr />
				<div class="row" id="isencao-disciplina">
					<ul>
						<li id="turmas" data-ng-repeat="input in inputs">

							<div id="inputs" class="col-md-4">
								<p>
									<b>Código da Disciplina Interna:</b>
								</p>
								<input class="form-control input-lg" data-ng-model="turma"
									data-ng-change="changeDisciplina(this, $index)"
									name="codigoDisciplina{{$index}}" type="text"
									placeholder="Digite o código da disciplina..." required />
							</div>
							<div id="inputs" class="col-md-7">
								<p>
									<b>Nome da Disciplina Interna:</b>
								</p>
								<input type="text" class="form-control input-lg"
									value="{{disciplina[$index]}}" readonly required>
							</div>

							<div id="inputs" class="col-md-4">
								<p>
									<b>Código da Disciplina Externa:</b>
								</p>
								<input class="form-control input-lg"
									name="codigoDisciplinaExterna{{$index}}" type="text" required />
							</div>
							<div id="inputs" class="col-md-7">
								<p>
									<b>Nome da Disciplina Externa:</b>
								</p>
								<input class="form-control input-lg"
									name="nomeDisciplinaExterna{{$index}}" type="text" required />
							</div>

							<div class="col-md-1">
								<a class="remove" data-ng-click="removeInput($index)"><i
									class="fa fa-minus-circle fa-3x"></i></a>
							</div>
						</li>
					</ul>
				</div>
				<div class="row">
					<a data-ng-click="addInput()" class="add"><i
						class="fa fa-plus-circle fa-3x"></i>
						<h4>Adicionar Item</h4> </a>
				</div>

				<hr />
				<div class="form-group">
					<label for="observacao">Observações adicionais:</label>
					<textarea name="observacao" class="form-control" rows="6"
						id="observacao" maxlength="500"
						placeholder="(opcional, max 500 caracteres)"></textarea>
					<h5>
						Faltam <span id="max"></span> caracteres.
					</h5>
				</div>

				<hr />

				<label for="inputFile">Anexar documentos comprobatórios de
					que cursou cada uma das disciplinas externas acima listadas
					(Formatos aceitos: PDF, JPEG ou PNG. Tamanho Máximo: 10mb)</label> <input
					type="file" name="file" id="inputFile" required>

				<hr />
				<div class="row checkbox">
					<label> <input type="checkbox" value="" required>
						Estou ciente de que o presente requerimento <b>não garante as
							isenções solicitadas</b> e de que posso verificar se o resultado da
						análise na SECAD.
					</label>
				</div>

				<hr />
				<div class=text-center>
					<input type="hidden" name="numeroSolicitacoes"
						value="${requestScope.numeroSolicitacoes}">
					<button type="submit" class="btn btn-primary btn-lg text-center">Confirmar</button>
				</div>

			</form>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/isencaoDisciplinas/homeInclusao">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>


</body>

</html>
