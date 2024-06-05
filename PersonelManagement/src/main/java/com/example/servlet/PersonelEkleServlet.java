package com.example.servlet;

import com.example.model.Personel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/personelEkle")
public class PersonelEkleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ad = req.getParameter("ad");
        String soyad = req.getParameter("soyad");
        String sicilNo = req.getParameter("sicilNo");
        String[] departman = req.getParameterValues("departman");
        String telefon = req.getParameter("telefon");
        String iseGirisTarihiStr = req.getParameter("iseGirisTarihi");
        double maas = Double.parseDouble(req.getParameter("maas"));
        boolean aktif = Boolean.parseBoolean(req.getParameter("aktif"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date iseGirisTarihi = null;
        try {
            iseGirisTarihi = sdf.parse(iseGirisTarihiStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Personel personel = new Personel(ad, soyad, sicilNo, departman, telefon, iseGirisTarihi, maas, aktif);

        @SuppressWarnings("unchecked")
        List<Personel> personelListesi = (List<Personel>) getServletContext().getAttribute("personelListesi");
        if (personelListesi == null) {
            personelListesi = new ArrayList<>();
        }
        personelListesi.add(personel);
        getServletContext().setAttribute("personelListesi", personelListesi);

        resp.sendRedirect("personel_listesi.html");
    }
}
