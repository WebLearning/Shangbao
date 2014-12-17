<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head> 
    <title>Log In</title>
	<link href="/Shangbao01/WEB-SRC/src/css/bootstrap.css" rel="stylesheet">
    <link href="/Shangbao01/WEB-SRC/src/css/signin.css" rel="stylesheet">
           
    <script src="/Shangbao01/WEB-SRC/src/js/jquery-1.11.0.js"></script>
    <script src="/Shangbao01/WEB-SRC/src/js/bootstrap.js"></script>
  </head>
  
  <body>
    <!-- <form action="/Shangbao01/j_spring_security_check" method="post">
    	用户：<input type="text" name="j_username"/><br/>
    	密码：<input type="password" name="j_password"/><br/>
    	<input type="submit" value="登录"/>
    </form>-->
    
    
    <div class="container">

      <form class="form-signin" action="/Shangbao01/j_spring_security_check" method="post" role="form">
        <h2 class="form-signin-heading">Please sign in</h2>
        <input name="j_username" type="text" class="form-control" placeholder="User name" required autofocus>
        <input name="j_password" type="password" class="form-control" placeholder="Password" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>                
      </form>
      <div  style="margin-left:37%;width:300px" >      	
      	<button class="btn btn-lg btn-success btn-block" data-toggle="modal" data-target="#myModalRegisterUser" disabled="disabled">Register</button>
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
