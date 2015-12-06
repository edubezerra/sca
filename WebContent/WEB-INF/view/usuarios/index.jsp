<%@page import="com.aries.blog.controller.BaseController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome to Bohay Blog</title>
        <%
            String baseURL = (String) request.getAttribute(BaseController.PARAM_BASE_URL);
        %>
        <!-- BOOTSTRAP STYLES-->
        <link href="<%=baseURL%>/css/bootstrap.min.css" rel="stylesheet" />
    </head>

    <body>
        <div class="row">
            <div class="col-md-10" style="margin: 10px;">
                <h2>Welcome .. !!</h2>
                <h5>
                    To view sample <a href="<%=baseURL%>/userlist">clik here</a>
                </h5>
            </div>
        </div>
    </body>
</html>
