/**
 * Created by QK on 2014/10/27.
 */

function submitLogin(){
	var userName=document.getElementById("userName").value;
	var password=document.getElementById("password").value;

    var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    var url="/Shangbao01/login?userName="+userName+"&password="+password;
    xmlhttp.open("POST",url,true);
    xmlhttp.send();

    xmlhttp.onreadystatechange=function()
    {  
        if (xmlhttp.readyState==4 && xmlhttp.status==200)
        {
            var res = xmlhttp.responseText;
            console.log(res);
            if(res=="true"){
//            	window.location="xj.jsp";
//            	window.navigate("xj.jsp");
//            	window.location.replace("xj.jsp");
//            	self.location='xj.jsp';
//            	top.location='xj.jsp';
            }
            else{
            	alert("Wrong password OR No such user!");
            }
        }
    };
}





