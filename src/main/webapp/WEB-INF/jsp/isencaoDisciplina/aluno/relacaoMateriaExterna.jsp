<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>Isenção de Disciplina</title>
</head>
<body>
	<h3 align="left">
		Aluno: ${aluno.nome } <br> Matrícula: ${aluno.matricula }

	</h3>
	<br>
	<h3 align="center">Relação de Matérias Externas</h3>
	<br></br>


	<form
		action="${pageContext.request.contextPath}/isencaoDisciplina/relacaoMateriaExterna"
		method="POST">
		<input name="aluno" value="${aluno.matricula}" type="hidden" />
		<div class="table-responsive" align="center">
			<table class="table">
				<thead>
					<tr>
						<th>Código</th>
						<th>Disciplina</th>
						<th>Matéria Externa</th>

					</tr>
				</thead>
				<tbody>
					<c:forEach items="${itemIsencaoByProcessoIsencao}"
						var="itemIsencaoByProcessoIsencao">
						<input name="itemIsencaoByProcessoIsencao"
							value="${itemIsencaoByProcessoIsencao.disciplina.codigo}"
							type="hidden" />
						<tr>
							<td>${itemIsencaoByProcessoIsencao.disciplina.codigo}</td>
							<td>${itemIsencaoByProcessoIsencao.disciplina.nome}</td>
							<td>
								<div class="form-group">
									<label >Digite o nome da disciplina que deseja associar:</label>
									<textarea class="form-control" rows="3"
										name="disciplinaAssociada"
										placeholder="Digite o nome da disciplina que deseja associar e divida com (; ou /) cada uma.(Ex: Banco de dados ; Estrutura de Dados)"></textarea>
								</div>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		
	     <button class="btn btn-success custom-width" type="submit"
				name="matricula">Confirmar</button>
	</form>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/menuPrincipalView"> <i
		class="fa fa-arrow-left"> </i> Voltar
	</a>
</body>
</html>