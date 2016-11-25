<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<title>SCA - Pedido de Isenção de Disciplinas</title>

<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-select.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap-select.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/app.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/vendor/font-awesome/css/font-awesome.min.css">

<!-- Required for tablesorter and jquery.popconfirm-->
<script src="http://code.jquery.com/jquery-1.12.1.min.js"></script>

<!-- Bootstrap stylesheet -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/tablesorter/css/bootstrap.min.css">
<!-- bootstrap widget theme -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/tablesorter/css/theme.bootstrap_2.css">
<!-- tablesorter plugin -->
<script
	src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.js"></script>
<!-- tablesorter widget file - loaded after the plugin -->
<script
	src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.widgets.js"></script>
<!-- pager plugin -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/tablesorter/css/jquery.tablesorter.pager.css">
<script
	src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.pager.js"></script>

<script
	src="http://netdna.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/jquery.popconfirm.js"></script>

<script>  
	    function escondeMostra(x){  
	        if(document.getElementById(x).style.display == "none" || document.getElementById(x).style.display == ""){  
	            document.getElementById(x).style.display = "table-row";  
	        }  
	        else{  
	            document.getElementById(x).style.display = "none";  
	        }  
	    }  
	</script>

<script>
	  	$(function() {
	  		
	  	  // call the tablesorter plugin and apply the uitheme widget
	  	  var $table1 = $("#tabela_registros").tablesorter({
	  		textExtraction: "complex",
	  	    // this will apply the bootstrap theme if "uitheme" widget is included
	  	    // the widgetOptions.uitheme is no longer required to be set
	  	    theme : "bootstrap",
	
	  	    widthFixed: true,
	  		// this is the default setting
	        cssChildRow: "tablesorter-childRow",
	
	  	    headerTemplate : '{content} {icon}', // new in v2.7. Needed to add the bootstrap icon!
	  	    
	  		// sort on the fifth column and first column, order desc and asc 
	        sortList: [[4,1],[0,0]],
	
	  	    // widget code contained in the jquery.tablesorter.widgets.js file
	  	    // use the zebra stripe widget if you plan on hiding any rows (filter widget)
	  	    widgets : [ "uitheme", "filter", "zebra", "pager" ],
	
	  	    widgetOptions : {
	  	      // If there are child rows in the table (rows with class name from "cssChildRow" option)
              // and this option is true and a match is found anywhere in the child row, then it will make that row
              // visible; default is false
              filter_childRows: true,
	  	      // using the default zebra striping class name, so it actually isn't included in the theme variable above
	  	      // this is ONLY needed for bootstrap theming if you are using the filter widget, because rows are hidden
	  	      zebra : ["even", "odd"],
	
	  	      // hide the filter row when not active
	  	      filter_hideFilters : true,
	  	      
	          // output default: '{page}/{totalPages}'
	          // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
	          pager_output: '{startRow} - {endRow} / {filteredRows} ({totalRows})',

	          pager_removeRows: false,

	          // class name applied to filter row and each input
	          filter_cssFilter  : 'tablesorter-filter',
	          // search from beginning
	          filter_startsWith : false,
	          // Set this option to false to make the searches case sensitive
	          filter_ignoreCase : true
	        }
	  	  })
	  	  .tablesorterPager({
	
	  	    // target the pager markup - see the HTML block below
	  	    container: $(".ts-pager"),
	
	  	    // target the pager page select dropdown - choose a page
	  	    cssGoto  : ".pagenum",
	
	  	    // remove rows from the table to speed up the sort of large tables.
	  	    // setting this to false, only hides the non-visible rows; needed if you plan to add/remove rows with the pager enabled.
	  	    removeRows: false,
	
	  	    // output string - default is '{page}/{totalPages}';
	  	    // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
	  	    output: '{startRow} - {endRow} / {filteredRows} ({totalRows})'
	
	  	  });
	  	  
	  	  // hide child rows - get in the habit of not using .hide()
	  	  // See http://jsfiddle.net/Mottie/u507846y/ & https://github.com/jquery/jquery/issues/1767
	  	  // and https://github.com/jquery/jquery/issues/2308
	  	  // This won't be a problem in jQuery v3.0+
	  	  $table1.find( '.tablesorter-childRow td' ).addClass( 'hidden' );

	  	  // Toggle child row content (td), not hiding the row since we are using rowspan
	  	  // Using delegate because the pager plugin rebuilds the table after each page change
	  	  // "delegate" works in jQuery 1.4.2+; use "live" back to v1.3; for older jQuery - SOL
	  	  $table1.delegate( '.toggle', 'click' ,function() {
	  	    // use "nextUntil" to toggle multiple child rows
	  	    // toggle table cells instead of the row
	  	    $( this )
	  	      .closest( 'tr' )
	  	      .nextUntil( 'tr.tablesorter-hasChildRow' )
	  	      .find( 'td' )
	  	      .toggleClass( 'hidden' );
	  	    return false;
	  	  });

	  	  // Toggle filter_childRows option
	  	  $( 'button.toggle-combined' ).click( function() {
	  	    var wo = $table1[0].config.widgetOptions,
	  	    o = !wo.filter_childRows;
	  	    wo.filter_childRows = o;
	  	    $( '.state1' ).html( o.toString() );
	  	    // update filter; include false parameter to force a new search
	  	    $table1.trigger( 'search', false );
	  	    return false;
	  	  });
	  	});
	  	$(function() {
		  	  $("[data-toggle='confirmation']").popConfirm();
		});
  	</script>

<script>		
		function downloadComprovante(IdReg) {			
			var form = $("<form id='downloadForm' style='height:10px;' action='${pageContext.request.contextPath}/registroIsencoes/downloadFile' method='POST' target='_blank'>");
            form.append($("<input type='hidden' name='IdReg' value='"+IdReg+"'>"));
            $('body').append(form);
            form.submit();
            $('body').remove('#downloadForm');
		}
	</script>

</head>
<body>

	<div class="container">
		<div class="row text-center">
			<h3>Pedido de Isenção de Disciplinas</h3>
		</div>
		<hr />
		<div class="row">
			<h5>
				<b>Aluno:</b>
				<c:out value="${requestScope.dadosAluno.nomeAluno}"></c:out>
				(Matrícula:
				<c:out value="${requestScope.matricula}"></c:out>
				)
			</h5>
			<h5>
				<b>Curso:</b>
				<c:out value="${requestScope.dadosAluno.curso.sigla}"></c:out>
				-
				<c:out value="${requestScope.dadosAluno.curso.nome}"></c:out>
				(Grade:
				<c:out value="${requestScope.dadosAluno.versaoCurso}"></c:out>
				)
			</h5>
		</div>
		<br />
		<c:if test="${requestScope.sucesso != null}">
			<div class="row">
				<span class="label label-success">${requestScope.sucesso}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.error != null}">
			<div class="row">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.info != null}">
			<div class="row">
				<span class="label label-info">${requestScope.info}</span>
			</div>
		</c:if>

		<div class="row">
			<c:if test="${requestScope.dadosAluno.temAtividadesSuficientes}">
				<div class="row text-center">
					<span class="label label-success">Parabéns! Você cumpriu
						atividades complementares suficientes para se formar.</span>
				</div>
			</c:if>
			<c:if test="${!requestScope.dadosAluno.temAtividadesSuficientes}">
				<div class="row text-center">
					<span class="label label-warning"> <i class="fa fa-warning"></i>
						Você ainda não cumpriu atividades complementares suficientes para
						se formar.
					</span>
				</div>
			</c:if>
		</div>

		<div class="row">
			<h4>
				<b>Itens do Pedido de Isenção:</b>
			</h4>
			<c:choose>
				<c:when test="${fn:length(requestScope.itensPedidoIsencao) eq 0}">
					<div class="vcenter well">
						<p>Não há itens de pedido de isenção</p>
					</div>
				</c:when>
				<c:otherwise>
					<div class="vcenter well">

						<table id="tabela_registros" class="table text-center tablesorter">
							<thead>
								<tr>
									<th><b>Nome</b></th>
									<th><b>Código</b></th>
									<th><b>Créditos</b></th>
									<th class="sorter-false filter-false"><b>Comprovante</b></th>
									<th class="filter-select filter-exact"
										data-placeholder="Escolha uma data"><b>Data de
											Solicitação</b></th>
									<th class="filter-select filter-exact"
										data-placeholder="Escolha um status"><b>Status</b></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${requestScope.itensPedidoIsencao}" var="registro">
									<c:choose>
										<c:when test="${registro.estado ne 'SUBMETIDO'}">
											<tr class="tablesorter-hasChildRow toggle"
												style="cursor: pointer; cursor: hand;">
										</c:when>
										<c:otherwise>
											<tr>
										</c:otherwise>
									</c:choose>
									<td class='text-left'>${registro.disciplina.nome}</td>
									<td class='text-left'>
										<div style='overflow: auto; width: 100%; height: 50px;'>
											${registro.disciplina.codigo}</div>
									</td>
									<td>${registro.disciplina.quantidadeCreditos}</td>
									<td><c:if test="${registro.temDocumento}">
											<button class="btn btn-default" title="Download"
												onClick="downloadComprovante(${registro.idItem})">
												<i class="fa fa-download"></i>
											</button>
										</c:if></td>
									<td>${registro.dataSolicitacao}</td>
									<td><c:set var="classeStatus" scope="page">
											<c:choose>
												<c:when test="${registro.estado eq 'INDEFERIDO'}">
													text-danger
												</c:when>
												<c:when test="${registro.estado eq 'DEFERIDO'}">
													text-success
												</c:when>
												<c:when test="${registro.estado eq 'EM_ANÁLISE'}">
													text-warning
												</c:when>
												<c:when test="${registro.estado eq 'SUBMETIDO'}">
													text-primary
												</c:when>
											</c:choose>
										</c:set> <span class="${classeStatus}"><b>${registro.estado}</b>
											<c:if test="${registro.podeSerCancelado}">
												<form style="height: 10px;"
													action="${pageContext.request.contextPath}/registroIsencoes/removeRegistroAtividade"
													method="POST">
													<input type="hidden" name="idReg"
														value="${registro.idItem}">
													<button type="submit" class="btn btn-default"
														data-toggle='confirmation'
														data-confirm-title='Confirmação'
														data-confirm-content='Cancelar esta submissão?'
														data-confirm-placement='top' data-confirm-yesBtn='Sim'
														data-confirm-noBtn='Não'>
														<i class="fa fa-trash-o"></i>
													</button>
												</form>
											</c:if></span></td>
									</tr>
									<tr class="tablesorter-childRow">
										<td colspan="6">
											<table class="table text-center">
												<tr>
													<td class="text-left"><b>Professor Avaliador:</b>
														${registro.nomeAvaliador}</td>
													<td class="text-left"><b>Data de Análise:</b>
														${registro.dataAnalise}</td>
												</tr>
												<c:if test="${registro.estado eq 'INDEFERIDO'}">
													<tr>
														<td colspan="2" class="text-left"><b>Justificativa:</b>
															${registro.justificativa}</td>
													</tr>
												</c:if>
											</table>
										</td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<th><b>Nome</b></th>
									<th><b>Código</b></th>
									<th><b>Créditos</b></th>
									<th><b>Comprovante</b></th>
									<th><b>Data de Solicitação</b></th>
									<th><b>Status</b></th>
								</tr>

								<tr>
									<th colspan='7' class='ts-pager form-horizontal'>
										<button type='button' class='btn first'>
											<i
												class='icon-step-backward glyphicon glyphicon-step-backward'></i>
										</button>
										<button type='button' class='btn prev'>
											<i class='icon-arrow-left glyphicon glyphicon-backward'></i>
										</button> <span class='pagedisplay'></span>
										<button type='button' class='btn next'>
											<i class='icon-arrow-right glyphicon glyphicon-forward'></i>
										</button>
										<button type='button' class='btn last'>
											<i class='icon-step-forward glyphicon glyphicon-step-forward'></i>
										</button> <select class='pagesize input-mini'
										title='Selecione o tamanho da página'>
											<option selected='selected' value='10'>10</option>
											<option value='20'>20</option>
											<option value='30'>30</option>
											<option value='40'>40</option>
									</select> <select class='pagenum input-mini' title='Selecione a página'></select>
									</th>
								</tr>
							</tfoot>
						</table>
					</div>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="row">
			<form style="height: 10px;"
				action="${pageContext.request.contextPath}/registroIsencoes/solicitaRegistroItem"
				method="POST">
				<button type="submit" class="btn btn-default"
					title="Registrar novo item">
					<i class="fa fa-plus"></i> Novo Item
				</button>
			</form>
		</div>

		<br />

		<div class="row">
			<a class="btn btn-default"
				href="${pageContext.request.contextPath}/registroIsencoes/menuPrincipal">
				<i class="fa fa-arrow-left"> </i> Voltar
			</a>
		</div>
	</div>

</body>
</html>