<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <title>Signin</title>
    
    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
          <link href="WEB-SRC/css/bootstrap.css" rel="stylesheet">
          <link href="WEB-SRC/css/signin.css" rel="stylesheet">   
           
          <script src="WEB-SRC/js/jquery-1.11.0.js"></script>                 
          <script src="WEB-SRC/js/bootstrap.min.js"></script>
          <script src="WEB-SRC/js/userLogin.js"></script>
          <script src="WEB-SRC/js/userRegister.js"></script>   
          
          
   
  </head>
  
  <body>
   
    <div class="container">

      <form class="form-signin" action="/static/j_spring_security_check" method="post" role="form">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input name="j_username" type="text" class="form-control" placeholder="User name" required autofocus>
        <input name="j_password" type="password" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>                
      </form>
      <div  style="margin-left:37%;width:300px" >      	
      	<button class="btn btn-lg btn-success btn-block" data-toggle="modal" data-target="#myModalRegisterUser">Register</button>
      </div>

    </div> <!-- /container -->
    
    <!-- register modal -->
    <div class="modal fade" id="myModalRegisterUser">
    
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" onclick="closeAndRefreshUserRegisterModal()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                        <h4 class="modal-title">User Register</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <div class="input-group" id="newUserRegisterNameInput">
                                <div class="input-group-addon">New User Name</div>
                                <input class="form-control" type="text" placeholder="New User Name" onkeyup="setUserRegisterTargetUserName(this.value)">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group" id="newUserRegisterPasswordInput">
                                <div class="input-group-addon">New Password</div>
                                <input class="form-control" type="password" placeholder="New Password" onkeyup="setUserRegisterTargetUserPassword(this.value)">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group" id="newUserRegisterPasswordAgainInput">
                                <div class="input-group-addon">New Password Again</div>
                                <input class="form-control" type="password" placeholder="New Password Again" onkeyup="setReUserRegisterTargetUserPassword(this.value)">
                            </div>
                        </div>
                        <p><span id="ifUserRegisterPasswordMatch"><br><br></span></p>                        
                    </div>
                    <div class="modal-footer" id="submitBtnOfUserRegister">
                        <button type="submit" class="btn btn-default" onclick="submitRegister()" disabled="disabled">Submit</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal-dialog -->
            
        </div><!-- /.modal -->

  </body>
</html>
