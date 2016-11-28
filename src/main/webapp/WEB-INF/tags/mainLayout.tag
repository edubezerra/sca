<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ attribute name="title" required="true" rtexprvalue="true"%>
<%@ attribute name="rootURL" required="true" rtexprvalue="true"%>
<%@ attribute name="username" required="true" rtexprvalue="true"%>
<%@ attribute name="contextPath" required="true" rtexprvalue="true"%>
<%@ attribute name="content" fragment="true"%>
<%@ attribute name="anonymous" required="false" rtexprvalue="true"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!doctype html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7" lang=""> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8" lang=""> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9" lang=""> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="ptBR">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>SCA</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="apple-touch-icon" href="apple-touch-icon.png">

<link href="${rootURL}resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${rootURL}resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${rootURL}resources/bootstrap/js/bootstrap.js"></script>

<script type="text/javascript" src="${rootURL}resources/util/util.js"></script>

<link href="${rootURL}resources/navbar/navbar.css" media="screen"
	rel="stylesheet" type="text/css" />

<style>
body {
	padding-top: 60px;
	padding-bottom: 20px;
}
</style>

</head>
<body>
	<!--[if lt IE 8]> <p class="browserupgrade">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p> <![endif]-->
	<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">SCA</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<sec:authorize access="hasRole('ROLE_EGRESSO')">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Aluno Egresso</a>
							<ul class="dropdown-menu">
								<li><a
									href="${pageContext.request.contextPath}/avaliacaoEgresso/questionarioGraduacao">
										Acompanhamento</a></li>
							</ul></li>

					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_ALUNO')">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Aluno</a>
							<ul class="dropdown-menu">
								<li><a
									href="${pageContext.request.contextPath}/avaliacaoTurma/avaliacaoTurmas">
										Avaliação de Docentes</a></li>
								<li><a
									href="${pageContext.request.contextPath}/registroAtividades/registroAtividades">
										Atividades Complementares </a></li>
								<!-- 				<li><a -->
								<%-- 					href="${pageContext.request.contextPath}/matriculaForaPrazo/requerimento/visualizarRequerimentos"> --%>
								<!-- 						Matrícula Fora de Prazo</a></li> -->
								<!-- 				<li><a -->
								<%-- 					href="${pageContext.request.contextPath}/realizarInscricao/realizarInscricao"> --%>
								<!-- 						Realização de Inscriçôes</a></li> -->
								<li><a
									href="${pageContext.request.contextPath}/registroIsencoes/registroIsencoes">
										Isenção de Disciplinas </a></li>
								<li><a
									href="${pageContext.request.contextPath}/monografias/">
										Monografias </a></li>
							</ul></li>

					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_PROFESSOR')">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Professor</a>
							<ul class="dropdown-menu">
								<li><a
									href="${pageContext.request.contextPath}/gradedisponibilidades/apresentarFormulario">
										Fornecimento de Grade de Disponibilidades</a></li>
								<li><a
									href="${pageContext.request.contextPath}/visualizacaoAvaliacaoDiscente/turma">
										Visualização das Respostas dos Questionarios</a></li>
								<li><a
									href="${pageContext.request.contextPath}/pastaProfessor/dashboard">
										Pasta Virtual</a></li>
								<li><a
									href="${pageContext.request.contextPath}/monografias/">
										Monografias </a></li>
							</ul></li>

					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_COORDENADOR_CURSO')">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Coordenador</a>
							<ul class="dropdown-menu">
								<li><a
									href="${pageContext.request.contextPath}/matriculaForaPrazo/analise/homeInclusao/">
										Matrícula Fora do Prazo</a></li>
								<li><a
									href="${pageContext.request.contextPath}/isencaoDisciplina/visualizaAlunosSolicitantes">
										Isenção de Disciplinas </a></li>

								<li><a
									href="${pageContext.request.contextPath}/relatorioEvasao/homeEvasao">
										Relatório de Evasão</a></li>

								<li><a
									href="${pageContext.request.contextPath}/relatorioRetencao/homeRetencao">
										Relatório de Retenção</a></li>

								<li><a
									href="${pageContext.request.contextPath}/relatorioReprovacaoDisciplina/homeReprovacaoDisciplina">
										Relatório de Reprovação por Disciplina</a></li>
								<li><a
									href="${pageContext.request.contextPath}/pastaProfessor/dashboard">
										Pasta Virtual</a></li>
								<li><a
									href="${pageContext.request.contextPath}/monografias/">
										Monografias </a></li>
							</ul></li>
					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_COORDENADOR_ATIVIDADES')">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Atividades</a>
							<ul class="dropdown-menu">
								<li><a
									href="${pageContext.request.contextPath}/analiseAtividades/homeAnalise">
										Análise de Registros de Atividade Complementar </a></li>
							</ul></li>
					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_ADMIN')">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown">Administrador</a>
							<ul class="dropdown-menu">
								<li><a
									href="${pageContext.request.contextPath}/usuarios/list">
										Listar usuários </a></li>
								<li><a
									href="${pageContext.request.contextPath}/alocacaoDisciplinaDepartamento/homeAlocacaoDisciplinas">
										Alocar Disciplinas a Departamentos</a></li>
								<li><a
									href="${pageContext.request.contextPath}/alocacaoProfessorDepartamento/homeAlocacaoProfessores">
										Alocar Professores a Departamentos</a></li>
								<li><a
									href="${pageContext.request.contextPath}/coordenacaoAtividades/homeAlocacaoCoordenadorAtividades">
										Alocar Coordenadores de Atividades Complementares </a></li>
								<li><a
									href="${pageContext.request.contextPath}/importacaoDados/homeImportacaoDados">
										Importar Dados</a></li>
								<li><a
									href="${pageContext.request.contextPath}/monografias/">
										Monografias </a></li>
								<li><a
									href="${pageContext.request.contextPath}/monografias/blacklist/">
										Blacklist de tags em monografias </a></li>
							</ul></li>
					</sec:authorize>

				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a>${username}</a></li>
					<li><c:if test='${anonymous=="true"}'>
							<a href="/">Login</a>
						</c:if> <c:if test='${anonymous!="true"}'>
							<a href="${pageContext.request.contextPath}/logout">Logout</a>
						</c:if></li>
				</ul>
			</div>
			<!--/.navbar-collapse -->
		</div>
	</nav>

	<div class="container">

		<h2>${title}</h2>
		<hr>

		<c:if test="${requestScope.error != null}">
			<div class="row">
				<div class="col-sm-12">
					<p class="alert alert-danger">${requestScope.error}</p>
				</div>
			</div>
		</c:if>

		<c:if test="${requestScope.info != null}">
			<div class="row">
				<div class="col-sm-12">
					<p class="alert alert-success">${requestScope.info}</p>
				</div>
			</div>
		</c:if>

		<jsp:invoke fragment="content"></jsp:invoke>

		<footer>
			<hr>
			<p>&copy; Cefet-RJ 2016</p>
		</footer>
	</div>

</body>
</html>
