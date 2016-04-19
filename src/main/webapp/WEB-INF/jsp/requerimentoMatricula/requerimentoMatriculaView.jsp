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
<title>SCA</title>

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

</head>

<body>
	<div class="container">
		<h1 class="text-center">Requerimento de Matrícula Fora do Prazo</h1>

		<h3 class="aluno">Aluno: ${requestScope.aluno.nome}
			(${requestScope.aluno.matricula})</h3>
		<hr />
		<c:if test="${requestScope.error != null}">
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>

		<form role="form"
			action="${pageContext.request.contextPath}/requerimentoMatricula/incluiSolicitacao"
			method="post" enctype="multipart/form-data"
			id="formularioSolicitacao">

			<div class="row">
				<table class="table table-bordered table-hover table-striped">
					<thead>
						<tr>
							<th>Código da Turma</th>
							<th>Nome da Disciplina</th>
							<th>Sigla do Departamento</th>
							<th>Modalidade</th>
							<th>---</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${itensRequerimento}" var="item">
							<tr>
								<td>${item.codigoTurma}</td>
								<td>${item.nomeDisciplina}</td>
								<td>${item.nomeDepartamento}</td>
								<td>${item.opcao}</td>
								<td><a
									href="<spring:url value="/users/remove/${user.id}.html" />"
									class="btn btn-danger triggerRemove"> Remover </a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<div class="row">
				<div>
					<!-- Button trigger modal -->
					<button type="button" class="btn btn-primary btn-lg"
						data-toggle="modal" data-target="#modalIncluir">Inserir
						Item no Requerimento</button>
				</div>
			</div>

			<div class="row"></div>

			<hr />
			<div class="form-group">
				<label for="observacoes">Observações adicionais:</label>

				<textarea name="observacoes" class="form-control" rows="6"
					id="observacoes" maxlength="500"
					placeholder="(opcional, max 500 caracteres)"></textarea>

				<h5>
					Faltam <span id="max"></span> caracteres.
				</h5>
			</div>

			<hr />

			<label for="inputFile">Anexe o comprovante de solicitação de
				matrícula do período atual (Formatos aceitos: PDF, JPEG ou PNG.
				Tamanho Máximo: 10mb)</label> <input type="file" name="file" id="inputFile"
				required> <span class="label label-danger"
				id="tamanhoGrande" style="display: none;"> </span>

			<hr />
			<div class="row checkbox">
				<label> <input type="checkbox" value="" required>
					Estou ciente de que o presente requerimento <b>não garante a
						matrícula na disciplina solicitada</b> e de que posso verificar se a
					inscrição foi efetivada, ou não, por meio de consulta ao meu
					histórico escolar, disponível no portal do aluno.
				</label>
			</div>

			<hr />
			<div class=text-center>
				<button type="submit" class="btn btn-primary btn-lg text-center">Confirmar</button>
			</div>
		</form>

		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/requerimetoMatricula/homeInclusao">
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
					<h4 class="modal-title" id="myModalLabel">Remove blog</h4>
				</div>
				<div class="modal-body">Really remove?</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
					<a href="" class="btn btn-danger removeBtn">Remove</a>
				</div>
			</div>
		</div>
	</div>

	<!-- Modal Incluir Item -->
	<form:form method="post"
		action="${pageContext.request.contextPath}/requerimentoMatricula/adicionarItemMatriculaForaPrazo">
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

						<div>
							<b>Atenção</b>: Após a autorização do chefe de departamento, a
							matrícula somente será efetivada caso não haja choque de horários
							nem quebras de pré-requisito (exceto no caso de escolha da
							modalidade 3)
						</div>

						<hr />
						<div class="form-group">
							<label for="departamento">Departamento Acadêmico:</label> <select
								class="form-control" name="nomeDepartamento"
								id="nomeDepartamento" required>
								<option value="" label="Escolha o departamento desejado"
									selected disabled></option>
								<c:forEach items="${requestScope.departamentos}"
									var="departamento">
									<option value="${departamento.nomeDepartamento}">
										${departamento.nomeDepartamento}</option>
								</c:forEach>
							</select>
						</div>

						<hr />
						<div class="form-group">
							<label for="turmasDisponiveis">Turma:</label> <br /> <select
								class="selectpicker" data-width="auto" data-live-search="true"
								name="idTurma" id="idTurma" required>
								<option value="" label="Escolha o departamento desejado"
									selected disabled>Selecione</option>
								<c:forEach items="${requestScope.turmasDisponiveis}" var="turma">
									<option value="${turma.id}">${turma.codigo}-
										${turma.nomeDisciplina}</option>
								</c:forEach>
							</select>
						</div>

						<hr />
						<p>
							<b>Modalidade da matrícula solicitada:</b>
						</p>

						<div class="radio">
							<label> <input type="radio" name="opcao" value="1" /> 1)
								<b>Matrícula em turma do próprio curso.</b> Opção restrita a
								alunos que na matrícula pela internet, solicitaram a inscrição
								na disciplina e turma ora requerida, mas sua solicitação foi
								rejeitada por falta de vagas.
							</label>
						</div>
						<div class="radio">
							<label> <input type="radio" name="opcao" value="2" /> 2)
								Matrícula em turma na qual houve impossibilidade de inscrição
								durante o prazo regulamentra em <b>decorrência de erro</b> no
								lançamento de nota do período letivo anterior.
							</label>
						</div>
						<div class="radio">
							<label> <input type="radio" name="opcao" value="3" /> 3)
								Matrícula em turma de disciplina com <b>quebra de
									pré-requisito</b>, envolvendo prováveis formandos que poderão
								alcançar a integralização do respectivo curso (vide manual do
								aluno).
							</label>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
						<input type="submit" class="btn btn-primary" value="Incluir" />
					</div>
				</div>
			</div>
		</div>
	</form:form>

</body>

</html>
