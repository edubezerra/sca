<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<script src="https://d3js.org/d3.v4.min.js"></script>

<script type="text/javascript">
	var dataSet1 = [ {
		"${Respostas[0]}" : "${Respostas[5]}",
		"${Respostas[1]}" : "${Respostas[6]}",
		"${Respostas[2]}" : "${Respostas[7]}",
		"${Respostas[3]}" : "${Respostas[8]}",
		"${Respostas[4]}" : "${Respostas[9]}"
	} ];
	var dataSet2 = [ {
		"${Respostas[0]}" : "${Respostas[10]}",
		"${Respostas[1]}" : "${Respostas[11]}",
		"${Respostas[2]}" : "${Respostas[12]}",
		"${Respostas[3]}" : "${Respostas[13]}",
		"${Respostas[4]}" : "${Respostas[14]}"
	} ];
	var dataSet3 = [ {
		"${Respostas[0]}" : "${Respostas[15]}",
		"${Respostas[1]}" : "${Respostas[16]}",
		"${Respostas[2]}" : "${Respostas[17]}",
		"${Respostas[3]}" : "${Respostas[18]}",
		"${Respostas[4]}" : "${Respostas[19]}"
	} ];
	var dataSet4 = [ {
		"${Respostas[0]}" : "${Respostas[20]}",
		"${Respostas[1]}" : "${Respostas[21]}",
		"${Respostas[2]}" : "${Respostas[22]}",
		"${Respostas[3]}" : "${Respostas[23]}",
		"${Respostas[4]}" : "${Respostas[24]}"
	} ];
	var dataSet5 = [ {
		"${Respostas[0]}" : "${Respostas[25]}",
		"${Respostas[1]}" : "${Respostas[26]}",
		"${Respostas[2]}" : "${Respostas[27]}",
		"${Respostas[3]}" : "${Respostas[28]}",
		"${Respostas[4]}" : "${Respostas[29]}"
	} ];
	var dataSet6 = [ {
		"${Respostas[0]}" : "${Respostas[30]}",
		"${Respostas[1]}" : "${Respostas[31]}",
		"${Respostas[2]}" : "${Respostas[32]}",
		"${Respostas[3]}" : "${Respostas[33]}",
		"${Respostas[4]}" : "${Respostas[34]}"
	} ];
	var dataSet7 = [ {
		"${Respostas[0]}" : "${Respostas[35]}",
		"${Respostas[1]}" : "${Respostas[36]}",
		"${Respostas[2]}" : "${Respostas[37]}",
		"${Respostas[3]}" : "${Respostas[38]}",
		"${Respostas[4]}" : "${Respostas[39]}"
	} ];
	var dataSet8 = [ {
		"${Respostas[0]}" : "${Respostas[40]}",
		"${Respostas[1]}" : "${Respostas[41]}",
		"${Respostas[2]}" : "${Respostas[42]}",
		"${Respostas[3]}" : "${Respostas[43]}",
		"${Respostas[4]}" : "${Respostas[44]}"
	} ];

	function drawPie(data, selectString, margin, outerRadius, innerRadius,
			sortArcs) {

		var canvasWidth = 300;
		var pieWidthTotal = outerRadius * 2;

		var pieCenterX = outerRadius + margin / 2;
		var pieCenterY = outerRadius + margin / 2;

		var canvasHeight = 190;

		var radius = 74, padding = 10;
		var color = d3.scaleOrdinal().range(
				[ "#6b486b", "#a05d56", "#d0743c", "#ff8c00" ]);

		var arc = d3.arc().outerRadius(radius).innerRadius(radius - 30);

		var pie = d3.pie().sort(null).value(function(d) {
			return d.population;
		});
		color.domain(d3.keys(data[0]).filter(function(key) {
			return key !== "Pergunta";
		}));

		var legend = d3.select(selectString).append("svg").attr("class",
				"legend").attr("width", radius * 2)
				.attr("height", radius * 1.5).selectAll("g").data(
						color.domain().slice().reverse()).enter().append("g")
				.attr("transform", function(d, i) {
					return "translate(0," + i * 20 + ")";
				});

		legend.append("rect").attr("width", 18).attr("height", 18).style(
				"fill", color);

		legend.append("text").attr("x", 24).attr("y", 9).attr("dy", ".35em")
				.text(function(d) {
					return d;
				});

		var total = 0;

		data.forEach(function(d) {
			d.ages = color.domain().map(function(name) {
				total = parseInt(total) + parseInt(d[name]);
			})
		});

		data.forEach(function(d) {
			d.ages = color.domain().map(function(name) {
				return {
					name : name,
					population : +d[name],
					percentage : (parseInt(d[name]) / total).toFixed(2)
				};
			})
		});

		var svg = d3.select(selectString).append("svg:svg").data(data).attr(
				"width", canvasWidth).attr("height", canvasHeight).append(
				"svg:g").attr("transform",
				"translate(" + pieCenterX + "," + pieCenterY + ")");

		svg.append("text").attr("dy", ".35em").style("text-anchor", "middle")
				.text(function(d) {
					return (d.Pergunta);
				});

		var g = svg.selectAll("g").data(function(d) {
			return pie(d.ages);
		}).enter().append("g");

		g.append("path").attr("d", arc).style("fill", function(d) {
			return color(d.data.name);
		}).append("title").text(function(d) {
			return d.data.percentage;
		});

		g.filter(function(d) {
			return d.data.percentage > .0
		}).append("text").attr("dy", ".35em").attr("text-anchor", "middle")
				.attr(
						"transform",
						function(d) {
							return "translate(" + arc.centroid(d) + ")rotate("
									+ angle(d) + ")";
						}).style("stroke", "white").text(function(d) {
					return d.data.percentage + "%";
				});

		function angle(d) {
			var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
			if (a = 180) {
				return a - 180;
			}
			if (a > 180) {
				return a - 210;
			}
			if (a > 90) {
				return a - 150;
			}
			if (a < 90) {
				return a + 30;
			}
			if (a = 90) {
				return a - 90;
			}
		}

	}
</script>

<style type="text/css">
svg {
	font: 10px sans-serif;
	/* 	display: block; */
}
</style>
<STYLE type="text/css">
div.div_RootBody {
	position: relative;
	border: 2px solid White;
	border-radius: 7px;
	background: WhiteSmoke;
	font: normal 14px Arial;
	font-family: Arial, Helvetica, sans-serif;
	color: Black;
	padding: 0px 1em;
	text-align: left;
}
div.div_RootBodyAspect {
	position: relative;
	border: 2px solid White;
	border-radius: 7px;
	background: WhiteSmoke;
	font: normal 14px Arial;
	font-family: Arial, Helvetica, sans-serif;
	color: Black;
	padding: 0px 1em;
	text-align: left;
	
}
</STYLE>

</head>

<body>

	<div class="div_RootBody">
		<h1 style="text-align: center; font: bold 1.5em Arial;">${titulo}</h1>
	</div>

	<div class="div_RootBody" id="pie_chart_1">
		<h3 class="h3_Body">Primeira - De forma geral, a apresentação do
			programa e dos objetivos dessa disciplina ocorreu de maneira...</h3>
		<div class="chart"></div>
	</div>
	<div class="div_RootBody" id="pie_chart_2">
		<h3 class="h3_Body">Segunda - De forma geral, a atualização da
			bibliografia utilizada e/ou adequação aos tópicos do programa dessa
			disciplina ocorreu de maneira...</h3>
		<div class="chart"></div>
	</div>
	<div class="div_RootBody" id="pie_chart_3">
		<h3 class="h3_Body">Terceira - De forma geral, o esclarecimento
			prévio sobre os critérios utilizados para a avaliação ocorreu de
			maneira...</h3>
		<div class="chart"></div>
	</div>
	<div class="div_RootBody" id="pie_chart_4">
		<h3 class="h3_Body">Quarta - De forma geral, o cumprimento do
			conteúdo programático ocorreu de maneira...</h3>
		<div class="chart"></div>
	</div>
	<div class="div_RootBody" id="pie_chart_5">
		<h3 class="h3_Body">Quinta - As práticas pedagógicas promovem a
			contextualização. De forma geral, a relação da teoria com a prática
			nessa disciplina ocorreu de maneira...</h3>
		<div class="chart"></div>
	</div>
	<div class="div_RootBody" id="pie_chart_6">
		<h3 class="h3_Body">Sexta - De forma geral, o
			planejamento/organização das aulas pelo professor ocorreu de
			maneira...</h3>
		<div class="chart"></div>
	</div>
	<div class="div_RootBody" id="pie_chart_7">
		<h3 class="h3_Body">Setima - De forma geral, a assiduidade do
			professor ocorreu de forma...</h3>
		<div class="chart"></div>
	</div>
	<div class="div_RootBody" id="pie_chart_8">
		<h3 class="h3_Body">Oitava - De forma geral, a pontualidade do
			professor pode ser avaliada como...</h3>
		<div class="chart"></div>
	</div>
	<div class="div_RootBodyAspect">
		<h3 class="h3_Body">Aspectos Positivos</h3>
		<textarea readonly disabled rows="10" cols="100">
		<c:forEach items="${respPos}" var="i">
		 ${i}
		</c:forEach>
	</textarea>
	</div>
	<div class="div_RootBodyAspect">
		<h3 class="h3_Body">Aspectos Negativo</h3>
		<textarea readonly disabled rows="10" cols="100">
		<c:forEach items="${respNeg}" var="i">
		 ${i}
		</c:forEach>
	</textarea>
	</div>
	<a class="btn btn-default"
		href="${pageContext.request.contextPath}/visualizacaoAvaliacaoDiscente/turma">
		<input type="button" value="Voltar" />
	</a>


	<script type="text/javascript">
		drawPie(dataSet1, "#pie_chart_1 .chart", 10, 100, 5, 0);
		drawPie(dataSet2, "#pie_chart_2 .chart", 10, 100, 5, 0);
		drawPie(dataSet3, "#pie_chart_3 .chart", 10, 100, 5, 0);
		drawPie(dataSet4, "#pie_chart_4 .chart", 10, 100, 5, 0);
		drawPie(dataSet5, "#pie_chart_5 .chart", 10, 100, 5, 0);
		drawPie(dataSet6, "#pie_chart_6 .chart", 10, 100, 5, 0);
		drawPie(dataSet7, "#pie_chart_7 .chart", 10, 100, 5, 0);
		drawPie(dataSet8, "#pie_chart_8 .chart", 10, 100, 5, 0);
	</script>

</body>

</html>


