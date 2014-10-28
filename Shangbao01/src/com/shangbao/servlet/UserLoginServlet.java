package com.shangbao.servlet;

import com.shangbao.control.LogInControl;
import com.shangbao.control.Imp.LogInControlImp;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonArray;
//import com.uestc1010.user.mongo.User;
//import com.uestc1010.user.mongo.UserMongoDBOperator;
//import com.uestc1010.user.mongo.UserMongoDBQueryAssembler;
//import com.uestc1010.user.session.SessionManagement;
//import com.uestc1010.user.operate.UserDBManager;
//import com.uestc1010.user.main.UserService;




import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Vector;

/**
 * Created by QK on 2014/10/27.
 */
public class UserLoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        String responseString="";
        
        String UserName=request.getParameter("userName");
        String Password=request.getParameter("password");
//        System.out.println(UserName+"___"+Password);
        
        ConfigurableApplicationContext context = null;  
        context = new ClassPathXmlApplicationContext("applicationContext.xml");        
        LogInControl logIn = (LogInControl)context.getBean("logInControl");        
        responseString=logIn.logIn(UserName, Password).toString();
//        System.out.println(responseString);  
        
        if(responseString=="true"){
            request.getRequestDispatcher("/WEB-SRC/index.html").forward(request, response);
        }else{
            request.getRequestDispatcher("/WEB-SRC/fail.jsp").forward(request, response);
        }           

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
