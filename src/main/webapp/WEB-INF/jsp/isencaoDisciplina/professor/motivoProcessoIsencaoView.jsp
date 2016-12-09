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
	Aluno: ${aluno.nome }
	
	<br>
	Matrícula: ${aluno.matricula }
	
	</h3>
	<br>
	<h3 align="center">Motivo Indeferimento da Disciplina</h3>
	<br></br>

	<form action="${pageContext.request.contextPath}/isencaoDisciplina/motivoProcessoIsencao"
		  method="POST" >
		  <input name="aluno" value="${aluno.matricula}" type="hidden" />
		  <input name="codigoDisciplina" value="${codigoDisciplina}" type="hidden" />
		  <input name="situacao" value="${situacao}" type="hidden" />
		
		<div class="dropdown" align="center">
			<select name="motivos" class="">
					<option value="" class="form-control" label="Selecionar" selected disabled>Selecionar</option>
					<option value="Carga horária insuficiente">Carga horária insuficiente</option>
					<option value="Ausência de programa">Ausência de programa</option>
					<option value="Programa insuficiente">Programa insuficiente</option>
					<option value="Disciplina não cursada">Disciplina não cursada</option>
					<option value="Disciplina sem registro no histórico escolar">Disciplina sem registro no histórico escolar</option>
					<option value="Disciplina sem aprovação">Disciplina sem aprovação</option>
					<option value="Conteúdo programático já utilizado em outra disciplina">Conteúdo programático já utilizado em outra disciplina</option>
					<option value="Outros">Outros</option>
			</select>
			<br>
		</div>
		<div class="form-group">
			<label for="observacao">Justificativa:</label>
			<textarea class="form-control" rows="5" name="observacao" placeholder="Apenas para Outros"></textarea>
		</div>

		<div>
			<button class="btn btn-success custom-width" type="submit"
				name="confirma">Confirmar</button>
		</div>
	</form>
	<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
</body>
</html>