package com.example;

import com.example.model.Personel;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonnelAppTests {

    @Test
    public void testPersonelToString() {
        Personel personel = new Personel("Ahmet", "Yılmaz", "12345", new String[]{"IT", "HR"}, "555-1234", new Date(1640995200000L), 50000.0, true);
        String expected = "Personel{ad='Ahmet', soyad='Yılmaz', sicilNo='12345', departman=[IT, HR], telefon='555-1234', iseGirisTarihi=Sat Jan 01 00:00:00 UTC 2022, maas=50000.0, aktif=true}";
        assertEquals(expected, personel.toString());
    }
}
