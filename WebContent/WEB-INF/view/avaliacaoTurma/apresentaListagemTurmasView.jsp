<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA - Avaliação</title>
<link href="${pageContext.request.contextPath}/css/base.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/form.css"
	rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/table.css"
	rel="stylesheet" type="text/css" />
</head>
<body class="basic-grey">

	<h1>Avaliação de turmas cursadas</h1>

	<p>A avaliação de Turmas por Discentes ocorre em dois momentos: no
		primeiro e no segundo semestre letivo de cada ano por meio de
		formulário eletrônico.</p>

	<p>As avaliações permanecem no banco de dados, sendo processadas
		pela Coordenação do Curso. Os resultados são repassados ao professor
		de cada turma avaliada, na forma consolidada, após o término do
		semestre letivo, para que os alunos não se sintam inibidos ao avaliar
		seus atuais professores.</p>


	<p>
		Para acesso ao formulário de avaliação, utilize os campos abaixo.
		Entre com as mesmas credenciais que você utiliza para acesso ao <b>Portal
			do Aluno</b>.
	</p>

	<br>
	<br>

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

	<c:if test="${requestScope.turmasCursadas == null}">
		<h3>Você não está inscrito em nenhuma turma.</h3>
	</c:if>

	<c:if test="${requestScope.turmasCursadas != null}">
		<div class="table">
			<div class="row">
				<div class="field">
					<div>
						<b>Período Letivo</b>
					</div>
				</div>
				<div class="field">
					<div>
						<b>Código da Turma</b>
					</div>
				</div>
				<div class="field">
					<div>
						<b>Nome da Disciplina</b>
					</div>
				</div>
				<div class="field"></div>
			</div>
			<c:forEach items="${requestScope.turmasCursadas}" var="turma">
				<div class="row">
					<div class="field">
						<div>${turma.periodoLetivo}</div>
					</div>
					<div class="field">
						<div>${turma.codigoTurma}</div>
					</div>
					<div class="field">
						<div>${turma.nomeDisciplina}</div>
					</div>
					<div class="field">
						<c:if test="${turma.isAvaliada}">
							<input class="lastfield" type="submit" value="Avaliada"
								disabled="disabled" />
						</c:if>
						<c:if test="${! turma.isAvaliada}">
							<form
								action="${pageContext.request.contextPath}/avaliacaoTurma/solicitaAvaliacaoTurma"
								method="post">
								<input type="hidden" name="idTurma"
									value="${turma.idTurma}" /> <input class="lastfield"
									type="submit" value="Avaliar" />
							</form>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:if>

	<br />

	<a class="button_embedded"
		href="${pageContext.request.contextPath}/avaliacaoTurma/menuPrincipal">
		<input type="button" value="Voltar" />
	</a>

</body>
</html>
