package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class PersonnelApp {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        handler.addServlet(new ServletHolder(new com.example.servlet.PersonelEkleServlet()), "/personelEkle");
        handler.addServlet(new ServletHolder(new com.example.servlet.PersonelListeleServlet()), "/personelListele");

        server.start();
        server.join();
    }
}
