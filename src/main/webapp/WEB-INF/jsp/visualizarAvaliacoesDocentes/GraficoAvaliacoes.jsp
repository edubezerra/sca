<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<meta charset="utf-8">
<style>
body {
	font: 10px sans-serif;
}

svg {
	padding: 10px 0 0 10px;
}

.arc {
	stroke: #fff;
}

table {
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 50%;
}

td, th {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 5px;
}

tr:nth-child(even) {
	background-color: #dddddd;
}
</style>
<body>
	<script src="https://d3js.org/d3.v4.min.js"></script>
	<script>
		var radius = 74, padding = 10;

		var color = d3.scaleOrdinal().range(
				[ "#ff0000", "#26ff00", "#002aff", "#000000" ]);

		var arc = d3.arc().outerRadius(radius).innerRadius(radius - 30);

		var pie = d3.pie().sort(null).value(function(d) {
			return d.population;
		});
		
		
			
		var data = [
		         {"${Respostas[0]}" : "${Respostas[5]}","${Respostas[1]}" : "${Respostas[6]}","${Respostas[2]}" : "${Respostas[7]}","${Respostas[3]}" : "${Respostas[8]}","${Respostas[4]}" : "${Respostas[9]}"},
		         {"${Respostas[0]}" : "${Respostas[10]}","${Respostas[1]}" : "${Respostas[11]}","${Respostas[2]}" : "${Respostas[12]}","${Respostas[3]}" : "${Respostas[13]}","${Respostas[4]}" : "${Respostas[14]}"},
		         {"${Respostas[0]}" : "${Respostas[15]}","${Respostas[1]}" : "${Respostas[16]}","${Respostas[2]}" : "${Respostas[17]}","${Respostas[3]}" : "${Respostas[18]}","${Respostas[4]}" : "${Respostas[19]}"},
		         {"${Respostas[0]}" : "${Respostas[20]}","${Respostas[1]}" : "${Respostas[21]}","${Respostas[2]}" : "${Respostas[22]}","${Respostas[3]}" : "${Respostas[23]}","${Respostas[4]}" : "${Respostas[24]}"},
		         {"${Respostas[0]}" : "${Respostas[25]}","${Respostas[1]}" : "${Respostas[26]}","${Respostas[2]}" : "${Respostas[27]}","${Respostas[3]}" : "${Respostas[28]}","${Respostas[4]}" : "${Respostas[29]}"},
		         {"${Respostas[0]}" : "${Respostas[30]}","${Respostas[1]}" : "${Respostas[31]}","${Respostas[2]}" : "${Respostas[32]}","${Respostas[3]}" : "${Respostas[33]}","${Respostas[4]}" : "${Respostas[34]}"},
		         {"${Respostas[0]}" : "${Respostas[35]}","${Respostas[1]}" : "${Respostas[36]}","${Respostas[2]}" : "${Respostas[37]}","${Respostas[3]}" : "${Respostas[38]}","${Respostas[4]}" : "${Respostas[39]}"},
		         {"${Respostas[0]}" : "${Respostas[40]}","${Respostas[1]}" : "${Respostas[41]}","${Respostas[2]}" : "${Respostas[42]}","${Respostas[3]}" : "${Respostas[43]}","${Respostas[4]}" : "${Respostas[44]}"}
		         
			      ]		
		
		function grafico(){
			color.domain(d3.keys(data[0]).filter(function(key) {
				return key !== "Pergunta";
			}));

			var total = 0;

			data.forEach(function(d) {
				d.ages = color.domain().map(function(name) {
					total = parseInt(total) + parseInt(d[name]);
				})
			});

			total = total / 8;

			data.forEach(function(d) {
				d.ages = color.domain().map(function(name) {
					return {
						name : name,
						population : +d[name],
						percentage : (parseInt(d[name]) / total).toFixed(2)
					};
				})
			});

			var legend = d3.select("body").append("svg")
					.attr("class", "legend").attr("width", radius * 2.5).attr(
							"height", radius * 2).selectAll("g").data(
							color.domain().slice().reverse()).enter().append(
							"g").attr("transform", function(d, i) {
						return "translate(0," + i * 20 + ")";
					});

			legend.append("rect").attr("width", 18).attr("height", 18).style(
					"fill", color);

			legend.append("text").attr("x", 24).attr("y", 9)
					.attr("dy", ".35em").text(function(d) {
						return d;
					});


			var svg = d3.select("body").selectAll(".pie").data(data).enter()
					.append("svg").attr("class", "pie").attr("width",
							radius * 2).attr("height", radius * 2).append("g")
					.attr("transform",
							"translate(" + radius + "," + radius + ")");

			svg.append("text").attr("dy", ".35em").style("text-anchor",
					"middle").text(function(d) {
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
								return "translate(" + arc.centroid(d)
										+ ")rotate(" + angle(d) + ")";
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
	<h1 style="font-size: 15">${turma.disciplina}</h1>

	<table>
		<tr>
			<th>Numero da Pergunta</th>
			<th>Pergunta</th>
		</tr>
		<tr>
			<td>Primeira</td>
			<td>De forma geral, a apresentação do programa e dos objetivos dessa disciplina ocorreu de maneira...</td>
		</tr>
		<tr>
			<td>Segunda</td>
			<td>De forma geral, a atualização da bibliografia utilizada e/ou adequação aos tópicos do programa dessa disciplina ocorreu de maneira...</td>
		</tr>
		<tr>
			<td>Terceira</td>
			<td>De forma geral, o esclarecimento prévio sobre os critérios utilizados para a avaliação ocorreu de maneira...</td>
		</tr>
		<tr>
			<td>Quarta</td>
			<td>De forma geral, o cumprimento do conteúdo programático ocorreu de maneira...</td>
		</tr>
		<tr>
			<td>Quinta</td>
			<td>As práticas pedagógicas promovem a contextualização. De forma geral, a relação da teoria com a prática nessa disciplina ocorreu de maneira...</td>
		</tr>
		<tr>
			<td>Sexta</td>
			<td>De forma geral, o planejamento/organização das aulas pelo professor ocorreu de maneira...</td>
		</tr>
		<tr>
			<td>Setima</td>
			<td>De forma geral, a assiduidade do professor ocorreu de forma...</td>
		</tr>
		<tr>
			<td>Oitava</td>
			<td>De forma geral, a pontualidade do professor pode ser avaliada como...</td>
		</tr>
	</table>
	
	<script>grafico() </script>

	
	
	
</body>
