<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ attribute name="rootURL" required="true" rtexprvalue="true" %>
<%@ attribute name="username" required="true" rtexprvalue="true" %>
<%@ attribute name="contextPath" required="true" rtexprvalue="true" %>
<%@ attribute name="content" fragment="true" %>

<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>

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

		<link href="${rootURL}resources/navbar/navbar.css"
			media="screen" rel="stylesheet" type="text/css" />

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
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">Pasta Virtual</a>
                </div>
                <div id="navbar" class="navbar-collapse collapse">
                    <ul class="nav navbar-nav">
                    	<li>
                    		<a href="${contextPath}">Principal</a>
                    	</li>
                        <li>
                            <a href="${contextPath}/pastaProfessor/dashboard">Dashboard</a>
                        </li>
                        <sec:authorize access="hasRole('ROLE_COORDENADOR_CURSO')">
	                        <li>
	                            <a href="${contextPath}/pastaProfessor/professores">Professores</a>
	                        </li>
                        </sec:authorize>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a>${username}</a>
                        </li>
                        <li>
                            <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        </li>
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
