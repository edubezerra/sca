<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>

<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>SCA - Matrícula Fora do Prazo</title>

<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />

<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-select.css"
	media="screen" rel="stylesheet" type="text/css" />

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap-select.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$(".triggerRemove").click(function(e) {
			e.preventDefault();
			$("#modalRemove .removeBtn").attr("href", $(this).attr("href"));
			$("#modalRemove").modal();
		});
	});
</script>

<script type="text/javascript" charset="utf-8">
	var url = "${pageContext.request.contextPath}/rest/turmas";

	$(function() {
		$("select#siglaDepartamento").change(
				function() {

					$.getJSON(url + '/' + $(this).val(), function(json) {

						$("select#idTurma").empty();

						$('select#idTurma').append(
								$("<option></option>").attr("value", "").text(
										""));
						$.each(json, function(key, value) {
							$('select#idTurma').append(
									$("<option></option>").attr("value", key)
											.text(value));
						});
					})
				})
	})
</script>

</head>

<body>
	<div class="container">
		<h1 class="text-center">Requerimento de Matrícula Fora do Prazo
			(${sessionScope.ficha.periodoLetivoCorrente})</h1>

		<h3 class="aluno">Aluno: ${sessionScope.ficha.aluno.nome}
			(${sessionScope.ficha.aluno.matricula})</h3>

		<c:if test="${requestScope.error != null}">
			<hr />
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
			<hr />
		</c:if>

		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">Turmas solicitadas pelo portal em ${sessionScope.periodoLetivo}</h3>
			</div>
			<div class="panel-body">
				<c:if test="${not empty sessionScope.turmasCursadas}">

					<div class="row">
						<table class="table table-bordered table-hover table-striped">
							<thead>
								<tr>
									<th>Turma/Disciplina</th>
									<th>Horário</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${sessionScope.turmasCursadas}" var="item">
									<tr>
										<td>${item.codigo} (${item.disciplina.codigo} -
											${item.nomeDisciplina})</td>
										<td>${item.horarioFormatado}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>
			</div>
		</div>

		<form role="form"
			action="${pageContext.request.contextPath}/matriculaForaPrazo/requerimento/confirmarRegistroRequerimento"
			method="post" id="formularioSolicitacao">

			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">Itens do Requerimento</h3>
				</div>
				<div class="panel-body">
					<c:if test="${not empty sessionScope.ficha.itensRequerimento}">

						<div class="row">
							<table class="table table-bordered table-hover table-striped">
								<thead>
									<tr>
										<th>Turma/Disciplina</th>
										<th>Sigla do Departamento</th>
										<th>Modalidade</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${sessionScope.ficha.itensRequerimento}"
										var="item">
										<tr>
											<td>${item.codigoTurma}(${item.codigoDisciplina} -
												${item.nomeDisciplina})</td>
											<td>${item.siglaDepartamento}</td>
											<td class="text-center">${item.opcao}</td>

											<td class="text-center"><a
												href="<spring:url value="/matriculaForaPrazo/requerimento/item/remove/${item.idTurma}" />"
												class="btn btn-danger triggerRemove"> Remover </a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</c:if>

					<div class="row">
						<div>
							<!-- Button trigger modal -->
							<button type="button" class="btn btn-primary" data-toggle="modal"
								data-target="#modalIncluir">Adicionar Item</button>
						</div>
					</div>
				</div>
			</div>

			<hr />
			<div class="row checkbox">
				<label> <input type="checkbox" value="" required>
					Estou ciente de que o presente requerimento <b>não garante a
						matrícula na disciplina solicitada</b> e de que posso verificar se a
					inscrição foi efetivada, ou não, por meio de consulta ao meu
					histórico escolar, disponível no portal do aluno.
				</label>
			</div>


			<div class="row checkbox">
				<label> <input type="checkbox" value="" required>
					Estou ciente de que, após eventual autorização do requerimento, a
					matrícula somente será efetivada caso não haja choque de horários
					nem quebras de pré-requisito (exceto no caso da modalidade "Aluno
					Formando".)
				</label>
			</div>


			<div class="row"></div>
			<hr />
			<div class=text-center>
				<button type="submit" name="submit"
					class="btn btn-primary btn-lg text-center">Enviar
					Requerimento</button>
			</div>
		</form>

		<hr />

		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/matriculaForaPrazo/requerimento/visualizarRequerimentos">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>

	</div>

	<!-- Modal Remover Item -->
	<div class="modal fade" id="modalRemove" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Remover Item</h4>
				</div>
				<div class="modal-body">Confirma remoção?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
					<a href="" class="btn btn-danger removeBtn">Remover</a>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal Incluir Item -->
	<form:form method="post"
		action="${pageContext.request.contextPath}/matriculaForaPrazo/requerimento/adicionarItem">
		<div class="modal fade" id="modalIncluir" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">Item do
							Requerimento</h4>
					</div>

					<div class="modal-body">

						<hr />
						<div class="form-group">
							<label for="siglaDepartamento">Departamento Acadêmico:</label> <select
								data-title="testando..." class="form-control"
								name="siglaDepartamento" id="siglaDepartamento" required>
								<option value="" label="Escolha o departamento desejado"
									selected disabled></option>
								<c:forEach items="${sessionScope.ficha.departamentos}"
									var="departamento">
									<option value="${departamento.siglaDepartamento}">
										${departamento.siglaDepartamento}</option>
								</c:forEach>
							</select>
						</div>

						<hr />
						<div class="form-group">
							<label for="idTurma">Turma:</label> <br />

							<!-- 							<select -->
							<!-- 								class="selectpicker" data-width="auto" data-live-search="true" -->
							<!-- 								name="idTurma" id="idTurma" required> -->

							<select class="form-control" name="idTurma" id="idTurma" required>
								<option value="" label="Escolha a turma desejada" selected
									disabled>Selecione</option>
								<c:forEach items="${sessionScope.turmasDisponiveis}" var="turma">
									<option value="${turma.id}">${turma.codigo}-
										${turma.nomeDisciplina}</option>
								</c:forEach>
							</select>
						</div>

						<hr />
						<p>
							<b>Modalidade da matrícula solicitada:</b>
						</p>

						<div class="btn-group">
							<label class="btn btn-primary"> <input type="radio"
								name="opcao" value="1" role="button" required /> Falta de vagas
								<!-- 								Matrícula em turma do próprio curso. Opção restrita a alunos que na matrícula pela internet, -->
								<!-- 								solicitaram a inscrição na disciplina e turma ora requerida, mas -->
								<!-- 								sua solicitação foi rejeitada por falta de vagas. -->
							</label> <label class="btn btn-primary"> <input type="radio"
								name="opcao" value="2" role="button" required /> Erro em
								lançamento <!-- Matrícula em turma na qual houve impossibilidade de inscrição durante o -->
								<!-- 								prazo regulamentar em <b>decorrência de erro</b> no lançamento -->
								<!-- 								de nota do período letivo anterior. -->
							</label> <label class="btn btn-primary"> <input type="radio"
								name="opcao" value="3" role="button" required /> Aluno formando
								<!-- 								3) Matrícula --> <!-- 								em turma de disciplina com <b>quebra de pré-requisito</b>, -->
								<!-- 								envolvendo prováveis formandos que poderão alcançar a -->
								<!-- 								integralização do respectivo curso (vide manual do aluno). -->
							</label>
						</div>

					</div>

					<hr />
					<div class="form-group">
						<label for="observacao">Opcionalmente, forneça informações
							relevantes para a análise:</label>

						<textarea name="observacao" class="form-control" rows="6"
							id="observacao" maxlength="500"
							placeholder="(opcional, max 500 caracteres)"></textarea>

						<h5>
							Faltam <span id="max"></span> caracteres.
						</h5>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
						<input type="submit" class="btn btn-primary" value="OK" />
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>

</html>
