package com.example.demo.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleHelloServlet extends HttpServlet {
    
    private String message = "Hello from servlet";

    @Override
    public void init(ServletConfig config) throws ServletException {
        if (config != null) {
            super.init(config);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write(message);
    }
}