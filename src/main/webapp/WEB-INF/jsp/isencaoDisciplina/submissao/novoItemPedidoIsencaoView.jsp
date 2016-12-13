<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>

<html>

<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>SCA - Adição de Item de Pedido de Isenção de Disciplina</title>

<script>
	function textCounter(field, field2, maxlimit) {
		var countfield = document.getElementById(field2);
		if (field.value.length > maxlimit) {
			field.value = field.value.substring(0, maxlimit);
			return false;
		} else {
			countfield.innerHTML = maxlimit - field.value.length;
		}
	}
</script>

<script
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/jquery/vendor/jquery.ui.widget.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/jquery/jquery.iframe-transport.js"></script>

<script
	src="${pageContext.request.contextPath}/resources/jquery/jquery.fileupload.js"></script>

<!-- bootstrap just to have good looking page -->
<script
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>

<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	type="text/css" rel="stylesheet" />

<!-- we code these -->
<link
	href="${pageContext.request.contextPath}/resources/css/dropzone.css"
	type="text/css" rel="stylesheet" />

<script
	src="${pageContext.request.contextPath}/resources/jquery/myuploadfunction.js"></script>

</head>

<body class="inclusao-disciplina">
	<div class="container">
		<div class="row text-center">
			<h3>Adição de Item de Pedido de Isenção de Disciplina</h3>
		</div>
		<hr />
		<div class="row">
			<h5>
				<b>Aluno:</b>
				<c:out value="${requestScope.fichaIsencaoDisciplinas.nomeAluno}"></c:out>
				(Matrícula:
				<c:out value="${requestScope.matricula}"></c:out>
				)
			</h5>
			<h5>
				<b>Curso:</b>
				<c:out value="${requestScope.fichaIsencaoDisciplinas.siglaCurso}"></c:out>
				-
				<c:out value="${requestScope.fichaIsencaoDisciplinas.nomeCurso}"></c:out>
				(Grade:
				<c:out value="${requestScope.fichaIsencaoDisciplinas.descritorVersaoCurso}"></c:out>
				)
			</h5>
		</div>
		<br />
		<c:if test="${requestScope.error != null}">
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<hr />
		<div class="row">
			<form
				action="${pageContext.request.contextPath}/submissaoIsencoes/adicionarItemNoPedido"
				method="post" enctype="multipart/form-data">

				<hr />
				<div class="row">
					<label for="idDisciplina">Disciplina a isentar:</label> <select
						data-title="testando..." class="form-control" name="idDisciplina"
						id="idDisciplina" required>
						<option value="" label="Escolha a disciplina a isentar" selected
							disabled></option>
						<c:forEach items="${requestScope.disciplinas}" var="disciplina">
							<option value="${disciplina.id}">${disciplina.nome}</option>
						</c:forEach>
					</select>
				</div>

				<br />
				<div class="row">
					<label for="nomeDisciplinaExterna">Nome da disciplina
						externa:</label> <input name="nomeDisciplinaExterna" class="form-control"
						onkeyup="this.value=this.value.toUpperCase()"
						id="nomeDisciplinaExterna" maxlength="144" required></input>
				</div>

				<br />
				<div class="row">
					<label for="cargaHoraria">Carga horária da disciplina
						externa (em horas):</label> <input style="width: 100px;"
						class="form-control number" id="cargaHoraria" name="cargaHoraria"
						type="number" maxlength="3"
						max="${requestScope.dadosAtiv.cargaHorariaRestante}" min="1"
						value="" required />
				</div>

				<br />
				<div class="row">
					<label for="notaFinalDisciplinaExterna">Nota final obtida
						na disciplina externa:</label> <input name="notaFinalDisciplinaExterna"
						class="form-control" id="notaFinalDisciplinaExterna" required></input>
				</div>

				<br />
				<div class="row">
					<label for="inputFile">Anexar comprovante de aprovação e
						programa da disciplina externa usada nesta isenção. Nos documentos
						aqui apensados, devem obrigatoriamente constar o nome da disciplina
						externa e a nota final obtida, exatamente conforme informados
						acima (Formatos aceitos: PDF, JPEG ou PNG. Tamanho Máximo: 10mb)</label> <input
						class="file" type="file" name="file" id="inputFile" required>
					<span class="label label-danger" id="tamanhoGrande"
						style="display: none;"></span>
				</div>

				<br />
				<div class="row">
					<label for="observacao">(Opcional) Forneça quaisquer
						observações que achar relevantes para a análise de seu pedido:</label>
					<textarea name="observacao" class="form-control" rows="2"
						id="observacao" maxlength="144"
						placeholder="(opcional, max 144 caracteres)"
						onkeyup="textCounter(this,'counter',144);"></textarea>
					<h5>
						Faltam <label id="counter">144</label> caracteres.
					</h5>
				</div>

				<br />
				<hr />
				<div class="row checkbox">
					<label> <input type="checkbox" value="" required>
						Estou ciente de que o presente requerimento <b>não garante a
							isenção solicitada</b> e de que posso verificar se o pedido foi
						deferido, ou não, através de consulta a esta mesma plataforma.
					</label>
				</div>
				<br />
				<div class=text-center>
					<button type="submit" class="btn btn-primary btn-lg text-center">Confirmar</button>
				</div>

			</form>
		</div>
		<br /> <a class="btn btn-default"
			href="${pageContext.request.contextPath}/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>

</html>
