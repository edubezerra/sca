<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SCA - Pedido de Isenção</title>

<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
	rel="stylesheet">
<link href="<c:url value='/resources/bootstrap/css/bootstrap.css' />"
	rel="stylesheet" />
<link href="<c:url value='/resources/css/usuarios.css' />"
	rel="stylesheet" />
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>

<script>
	jQuery(document).ready(function($) {
		$("#formAnaliseItemPedidoIsencao").submit(function(event) {
			// Disable the search button
			enableSearchButton(false);
			// Prevent the form from submitting via the browser.
			event.preventDefault();
		});
	});

	function promptJustificativa(idItem,novoStatus){
		alert("ok1");
		$.bs.popup.prompt({
				  title: 'Justificativa',
				  info: 'Informe a justificativa para o indeferimento.'
				}, function(dialogE, message) {
					registraAnaliseItem(idItem,novoStatus,message);
					dialogE.modal('hide');
				});			
	}

	function registraAnaliseItem(idItem,novoStatus,justificativa) {
			alert("ok2");
			var search = {}
			search["matriculaProfessor"] = String("${professor.matricula}");
			search["matriculaAluno"] = String("${aluno.matricula}");
			search["status"] = String($("#status").val());
			search["startDate"] = $('#startDate').val();
			search["novaSituacao"] =  String(novoStatus);
			search["idItemPedidoIsencao"] = String(idItem);
			search["justificativa"] = String(justificativa);
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${pageContext.request.contextPath}/isencaoDisciplina/analisaItemPedidoIsencao",
				data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					alert(data);
					console.log("SUCCESS: ", data);
					$("#statusError").html("");
					display(data, idItem);},
				error : function(e) {
					alert('error!');
					console.log("ERROR: ", e);
					$("#statusError").html("Registro de atividade complementar não pode ser deferido! Carga horária máxima atingida!");}
			});	
	}
</script>

<script>
	function enableSearchButton(flag) {
		$("#btn-regLotacao").prop("disabled", flag);
	}

	function display(data, idItem) {
		alert('display');
		alert(data);
		alert(idItem);
		if(data.length == 0){
			$("#" + idItem).html("Não há registros de atividades complementares com os critérios pesquisados!");
		}
		else{
			var div_data = "<div id='${" + idItem + "}' class='status text-center'>";
// 			div_data = div_data + "<c:set var='classeStatus' scope='page'>";
// 			if(data == 'DEFERIDO') {
// 				div_data = div_data + " text-success ";
// 			} else if(data == 'INDEFERIDO') {
// 				div_data = div_data + " text-danger ";
// 			}
// 			div_data = div_data + "</c:set>";
			div_data = div_data + "<p class='text-success'>";
			div_data = div_data + "<b>" + data + "</b>";
			div_data = div_data + "</p>";
			div_data = div_data + "</div>";      		
			$("#" + idItem).html(div_data);				
		}
	}
</script>

</head>
<body class="lista-solicitacoes">
	<div class="container">
		<div class="row text-center">
			<h2>Itens do Pedido de Isenção de Disciplinas</h2>
			<h4>Professor: ${requestScope.professor.nome} (Matrícula
				${professor.matricula})</h4>
			<h4>Departamento: ${requestScope.siglaDepartamento}</h4>
			<h4>Aluno solicitante: ${aluno.nome} (Matrícula
				${aluno.matricula})</h4>
		</div>

		<c:if test="${requestScope.sucesso != null}">
			<div class="row text-center">
				<span class="label label-success">${requestScope.sucesso}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.error != null}">
			<div class="row text-center">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<div class="row">
			<form id="formAnaliseItemPedidoIsencao">

				<c:forEach items="${itensPedidoIsencao}" var="item">
					<div class="well">
						<div class="dados-aluno">
							<p>
								<b>Disciplina solicitada:</b> ${item.disciplina.codigo} -
								${item.disciplina.nome}
							</p>
							<p>
								<b>Disciplina externa:</b> ${item.descritorDisciplinaExterna}
							</p>
							<p>
								<b>Observações:</b> ${item.observacao}
							</p>
						</div>

						<div id="${item.id}" class="status text-center">
							<c:choose>
								<c:when test="${item.situacao eq 'INDEFINIDO'}">

									<button class="btn btn-success"
										onclick='registraAnaliseItem(${item.id}, "DEFERIDO", "");'>
										<i class="fa fa-check"></i> Deferir
									</button>

									<button class="btn btn-danger"
										onClick='promptJustificativa("${item.id}", "INDEFERIDO");'>
										<i class="fa fa-times"></i> Indeferir
									</button>
								</c:when>
								<c:otherwise>
									<c:set var="classeStatus" scope="page">
										<c:choose>
											<c:when test="${item.situacao eq 'INDEFERIDO'}">
												text-danger
											</c:when>
											<c:when test="${item.situacao eq 'DEFERIDO'}">
												text-success
											</c:when>
										</c:choose>
									</c:set>
									<p class="${classeStatus}">
										<b>${item.situacao}</b>
									</p>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</c:forEach>
			</form>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/isencaoDisciplina/professor/visualizaAlunosSolicitantes">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>
</body>
</html>