/**
 * Created by QK on 2014/10/27.
 */

var RegisterTargetUserName=null;
var RegisterNewPassword=null;
var ReRegisterNewPassword=null;
var RegisterNewPassword_ifMatch=false;


function submitRegister(){
	
//	console.log(RegisterTargetUserName);
//	console.log(RegisterNewPassword);
	
    var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    var url="/Shangbao01/register?userName="+RegisterTargetUserName+"&password="+RegisterNewPassword;
    xmlhttp.open("POST",url,true);
    xmlhttp.send();

    xmlhttp.onreadystatechange=function()
    {  
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            var res = xmlhttp.responseText;
            if(res=="true"){
            	alert("Register Success");
            }
            else{
            	alert("User already existed!");
            }
            closeAndRefreshUserRegisterModal();
        }
    };
}

function setUserRegisterTargetUserName(str){
    RegisterTargetUserName=str;
    judgeIfToShowSubmitBtn();
}

function setUserRegisterTargetUserPassword(str){
    RegisterNewPassword=str;
    judgeIfUserRegisterPasswordMatch(RegisterNewPassword,ReRegisterNewPassword);
    judgeIfToShowSubmitBtn();
}

function setReUserRegisterTargetUserPassword(str){
    ReRegisterNewPassword=str;
    judgeIfUserRegisterPasswordMatch(RegisterNewPassword,ReRegisterNewPassword);
    judgeIfToShowSubmitBtn();
}

function judgeIfUserRegisterPasswordMatch(iRegisterNewPassword,iReRegisterNewPassword){
    if(iRegisterNewPassword!=iReRegisterNewPassword){
        document.getElementById("ifUserRegisterPasswordMatch").innerHTML="<span class='label label-danger'>The new passwords you typed do not match! </span> <br><span class='label label-danger'>Please type the same password in both text boxes.</span>";
        RegisterNewPassword_ifMatch=false;
    }
    else{
        document.getElementById("ifUserRegisterPasswordMatch").innerHTML="<span class='label label-success'>The new passwords you typed match. </span><br><br>";
        RegisterNewPassword_ifMatch=true;
    }
}


function judgeIfToShowSubmitBtn(){
    if(RegisterTargetUserName!=null&&RegisterTargetUserName!=""&&RegisterNewPassword_ifMatch!=false){
        document.getElementById("submitBtnOfUserRegister").innerHTML="<button type='submit' class='btn btn-primary' onclick='submitRegister()'>Submit</button>";
    }
    else{
        document.getElementById("submitBtnOfUserRegister").innerHTML="<button type='submit' class='btn btn-default' onclick='submitRegister()' disabled='disabled'>Submit</button>";
    }
}

function closeAndRefreshUserRegisterModal(){
    $('#myModalRegisterUser').modal('toggle');
    var temp1,temp2,temp3;
    temp1="<div class='input-group-addon'>New User Name</div><input class='form-control' type='text' placeholder='New User Name' onkeyup='setUserRegisterTargetUserName(this.value)'>";
    temp2="<div class='input-group-addon'>New Password</div><input class='form-control' type='password' placeholder='New Password' onkeyup='setUserRegisterTargetUserPassword(this.value)'>";
    temp3="<div class='input-group-addon'>New Password Again</div><input class='form-control' type='password' placeholder='New Password Again' onkeyup='setReUserRegisterTargetUserPassword(this.value)'>";
    document.getElementById("newUserRegisterNameInput").innerHTML=temp1;
    document.getElementById("newUserRegisterPasswordInput").innerHTML=temp2;
    document.getElementById("newUserRegisterPasswordAgainInput").innerHTML=temp3;
    document.getElementById("ifUserRegisterPasswordMatch").innerHTML="";
    document.getElementById("submitBtnOfUserRegister").innerHTML="<button type='submit' class='btn btn-default' onclick='submitRegister()' disabled='disabled'>Submit</button>";
}







