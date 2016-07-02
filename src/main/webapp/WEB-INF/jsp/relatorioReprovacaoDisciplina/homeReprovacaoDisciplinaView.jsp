<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta content="charset=UTF-8">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/sca/resources/bootstrap/css/bootstrap.css" media="screen" rel="stylesheet" type="text/css" />
    <link href="/sca/resources/relatorio/relatorio.css" media="screen" rel="stylesheet" type="text/css" />
    <script src="/sca/resources/jquery/jquery-1.10.2.js"></script>
	<script src='/sca/resources/relatorio/d3.v3.min.js'></script>
	<script src="/sca/resources/relatorio/dimple.v2.1.6.min.js"></script>
	<script src="/sca/resources/jquery/jquery.validate.min.js"></script>			
   <title>Relatório de Reprovação por Disciplina</title>
</head>

<script type="text/javascript">

$(document).ready(function(){
	$("#frmReprovacaoDisciplina").validate({
		rules: {
			disciplina: {
				required: true
			}	
		},
		messages: {
			disciplina: {required: "Preencha o código da disciplina."}
		}
	});
});

function drawReport(data) {
	var disciplina = "${disciplina}";
	
	 var svg = dimple.newSvg("#RecallsChart", 590, 420);
	
	 var myChart = new dimple.chart(svg, data);
	 myChart.setBounds(65, 30, 505, 330)
	 
	 var x =  myChart.addCategoryAxis("x", "Periodo Letivo");
	 myChart.addMeasureAxis("y", "Alunos");
	 
	 myChart.addSeries(null, dimple.plot.line);
	 var mySeries = myChart.addSeries(["Periodo Letivo", "Alunos", "Lista de Alunos"] , dimple.plot.line); 
	 
	 mySeries.getTooltipText = function(e) {
		 var html = '';
		 html += '<span class="marker"> Período Letivo: ' + e.aggField[0]+'</span>';
		 html += '<br/> <br/>';
		
		 var stringJson = e.aggField[2].toString();
		 var array = stringJson.split(",");			 
		 
		 html += '<strong>Lista de Alunos</strong>';
		 html += '<ul align="left">';
		 for(var i = 0; i < array.length; i++){
			 html += "<li>" + array[i] + "</li>";
		 }
		 html += '</ul>';
		 
		 $("#RecallDetails").html(html);
		 
		 return [
		         	"Disciplina: " + disciplina,
		        	"Periodo Letivo: " + e.aggField[0],
		        	"Quantidade de Alunos: " + e.aggField[1]
		        ];
	 }
	 
	 myChart.draw();
}

</script>

<body>

 <div class="content">
 
 <h1 class="text-center">Relatório de Reprovação por Disciplina</h1>
 <hr />
 <br>
 
  <div>	
	<c:if test="${error != null}">
            <div class="alert alert-danger">
                <c:out value="${error}"/>
            </div>
        </c:if>
 </div>
 <form id="frmReprovacaoDisciplina" action="${pageContext.request.contextPath}/relatorioReprovacaoDisciplina/relatorioReprovacaoDisciplina" method="post">
        
        <label for="turma">Código da Disciplina:</label> 
        <input type="text" id="disciplina" name="disciplina" class="form-inputs" value="${disciplina}"/>
        <br><br><br>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Gerar Relatório</button>
 </form>
      <br>
      <hr />
      <br>
   
   	 <c:set var="lengthResponse" value="${fn:length(response)}"/>
   
     <div id="RecallsChart" class="chart"></div><div id="RecallDetails"></div>
	 
	 <!--  it's working, besides warning -->
	 <c:if test="${not empty response}">
	 	<c:choose>
	 		<c:when test="${lengthResponse gt 2}">
	 			<script>
		 		drawReport(${response});
		 		</script>
	 		</c:when>
	 		<c:otherwise>
	 			<p>Não existem alunos reprovados para esta disciplina.</p>
	 		</c:otherwise>
	 	</c:choose>
	 </c:if>
	 
 </div>

</body>
</html>
