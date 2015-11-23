/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var myApp = angular.module('mainApp', []);

myApp.controller('UsersController', ['$scope', '$http', function($scope, $http) {

    $scope.listUser = null;
    $scope.resMsg = null;

        $scope.showUserList = function() {
            var urlGetUsers = 'getalluser';
            
            var responEC = $http.get(urlGetUsers, {cache: true, transformResponse: function(data, headersGetter) {
                    try {
                        var jsonObject = JSON.parse(data);
                        keepGoing = true;
                        return jsonObject;
                    }
                    catch (e) {
                        console.log(e);
                        $scope.resMsg = "Error. Cannot Retrieve Data";
                    }
                    return {};
                }});//end ajax 

            responEC.success(function(listUser, status, headers, config) {
                $scope.listUser = listUser;
                if ($scope.listUser == null || $scope.listUser.length == 0) {
                    $scope.resMsg = "No Data";
                }
            });
        };
        
        $scope.showUserList();

    }]);