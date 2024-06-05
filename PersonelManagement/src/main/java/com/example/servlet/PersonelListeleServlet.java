package com.example.servlet;

import com.example.model.Personel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/personelListele")
public class PersonelListeleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        @SuppressWarnings("unchecked")
        List<Personel> personelListesi = (List<Personel>) getServletContext().getAttribute("personelListesi");
        req.setAttribute("personelListesi", personelListesi);
        req.getRequestDispatcher("personel_listesi.html").forward(req, resp);
    }
}
