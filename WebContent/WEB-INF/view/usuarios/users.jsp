<%-- 
    Document   : users
    Created on : May 28, 2015, 10:25:11 AM
    Author     : ariestania.winda
--%>

<%@page import="com.aries.blog.controller.BaseController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            String baseURL = (String) request.getAttribute(BaseController.PARAM_BASE_URL);
        %>
        <title>List Of User</title>
        <!-- BOOTSTRAP STYLES-->
        <link href="<%=baseURL%>/css/bootstrap.min.css" rel="stylesheet" />
        <!-- ANGULAR JS -->
        <script src="<%=baseURL%>/js/angular.js"></script>
        <!--USERS JS -->
        <script src="<%=baseURL%>/js/users.js"></script>
    </head>
    <body>
        <div class="row" ng-controller="UsersController" ng-app="mainApp" style="margin: 10px;">
            <div class="col-md-7">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        Users
                    </div>
                    <div class="panel-body">
                        <div ng-show="isEmpty(listUser)">
                            {{resMsg}}
                        </div>
                        <div id="dynamic" ng-hide="isEmpty(listUser)">
                            <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="exampleone">
                                <thead>
                                    <tr>
                                        <th>No.</th>
                                        <th>User Name</th>
                                        <th>Name</th>
                                        <th>Is Active</th>
                                    </tr>                                    
                                </thead>
                                <tbody>
                                    <tr ng-repeat="user in listUser">
                                        <td>{{$index + 1}}</td>
                                        <td>{{user.username}}</td>
                                        <td>{{user.name}}</td>
                                        <td>{{user.enable}}</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>            
        </div>
    </body>
</html>
