<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head> 
    <title>Log In</title>

  </head>
  
  <body>
    <form action="/Shangbao01/j_spring_security_check" method="post">
    	用户：<input type="text" name="j_username"/><br/>
    	密码：<input type="password" name="j_password"/><br/>
    	<input type="submit" value="登录"/>
    </form>
  </body>
</html>
