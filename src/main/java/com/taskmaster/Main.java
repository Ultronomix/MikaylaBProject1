
package com.taskmaster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmaster.revature.auth.AuthService;
import com.taskmaster.revature.auth.AuthServlet;
import com.taskmaster.revature.users.UserDAO;
import com.taskmaster.revature.users.UserService;
import com.taskmaster.revature.users.UserServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import com.taskmaster.revature.reimbursment.ReimbServlet;
import com.taskmaster.revature.reimbursment.ReimbService;
import com.taskmaster.revature.reimbursment.ReimbDAO;
public class Main {
    public static void main(String[] args) throws LifecycleException {
        String docBase = System.getProperty("java.io.tmpdir");
        // Web server base configurations

        Tomcat webServer = new Tomcat();
        webServer.setBaseDir(docBase);
        webServer.setPort(5000);
        webServer.getConnector();


        // App component instantiation
        UserDAO userDAO = new UserDAO();
        ReimbDAO reimbDAO = new ReimbDAO();
        ReimbService reimbService = new ReimbService(reimbDAO);
        ReimbServlet reimbServlet = new ReimbServlet(reimbService);
        AuthService authService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        ObjectMapper jsonMapper = null;
        UserServlet userServlet = new UserServlet(userService, jsonMapper);
        AuthServlet authServlet = new AuthServlet(authService, jsonMapper);
        jsonMapper = new ObjectMapper();


        // Web server context and servlet configurations
        final String rootContext = "/taskmaster";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");
        webServer.addServlet(rootContext, "ReimbServlet", reimbServlet).addMapping("/reimb");
        webServer.start();
        webServer.getServer().await();
    }
}
//        UserDAO userdao = new UserDAO();
//        //serdao.findUserByUsername("aanderson");
//        System.out.println(userdao.findUserByUsername("aanderson"));
//    }







