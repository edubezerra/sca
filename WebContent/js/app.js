'use strict';
var app = angular.module('inclusaoDisciplina', [])


app.controller('inclusaoController',['$scope', '$http', '$timeout',
    function ($scope, $http, $timeout) {
		var array = {}
		
		$scope.disciplina = array;
		$scope.changeTurma = function ($event, i) {
			$timeout(function(){
				$http.get(url + '/' + $event.turma)
				.success(function(data, status, headers, config) {
					var turma = Object.keys(data);
					$scope.disciplina[i] = data[turma];
				})
				.error(function(data, status, headers, config) {
					$scope.disciplina[i].push('');
				});

			},1000);
		}
		
		$scope.inputs = [{input:true}];

		/*adiciona um novo input para solicitar uma turma/disciplina*/
		$scope.addInput = function () {
			if($scope.inputs.length < 3 || solicitacoesRestantes > 1){
				$scope.inputs.push({input:true});
				solicitacoesRestantes--;
			}
			
			if($scope.inputs.length >= 3 || solicitacoesRestantes == 1){
				$(".add").hide();
			} else {
				$(".add").show()
			}
			
			$timeout(function (){
				$(".remove").show();
			});
			
		};

		/*remove um input de turma/disciplina*/
		$scope.removeInput = function (index) {
			$scope.inputs.splice(index,1);
			if($scope.inputs.length == 1) {
				$(".remove").hide();
			}
			solicitacoesRestantes++;
			if($scope.inputs.length < 3 || solicitacoesRestantes > 1){
				$(".add").show();
			}
			
		};
	}
]);
