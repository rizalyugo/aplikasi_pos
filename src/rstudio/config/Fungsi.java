/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 *
 * @author DAFI
 */
public class Fungsi {
    
    static double msPerGregorianYear = 365.25 * 86400 * 1000;
    
    public static String selisihDateTime(Date waktuSatu, Date waktuDua) {
        long selisihMS = Math.abs(waktuSatu.getTime() - waktuDua.getTime());
        long selisihDetik = selisihMS / 1000 % 60;
        long selisihMenit = selisihMS / (60 * 1000) % 60;
        long selisihJam = selisihMS / (60 * 60 * 1000) % 24;
        long selisihHari = selisihMS / (24 * 60 * 60 * 1000);
        String selisih =  selisihJam + " jam "
                + selisihMenit + " menit " + selisihDetik + " detik";
        return selisih;
    }
     
     
     public static Date konversiStringkeDate(String tanggalDanWaktuStr,
        String pola, Locale lokal) {
        Date tanggalDate = null;
        SimpleDateFormat formatter;
        if (lokal == null) {
            formatter = new SimpleDateFormat(pola);
        } else {
            formatter = new SimpleDateFormat(pola, lokal);
        }
        try {
            tanggalDate = formatter.parse(tanggalDanWaktuStr);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return tanggalDate;
    }
    
    public String tampilSelisihDateTime(String waktuSatuStr, String waktuDuaStr){
        Locale lokal = null;
        String pola = "yyyy-MM-DD HH:mm:ss";
        //String waktuSatuStr = "2015-11-07 22:06:56";
        //String waktuDuaStr = "2015-11-08 06:43:23";
        Date waktuSatu = konversiStringkeDate(waktuSatuStr,
                pola, lokal);
        Date WaktuDua = konversiStringkeDate(waktuDuaStr, pola,
                lokal);
        String hasilSelisih = selisihDateTime(waktuSatu,
                WaktuDua);
        //System.out.println("Selisih tanggal \""+waktuSatuStr+"\" dengan tanggal \""+waktuDuaStr+"\" adalah: ");
        //System.out.println(hasilSelisih);
        return hasilSelisih;
    }
    
    public static int getAge(Date dateOfBirth) throws Exception {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            throw new Exception("Tanggal lahir tidak boleh hari mendatang");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of
        // leap year) then decrement age one year
        if ((birthDate.get(Calendar.DAY_OF_YEAR)
                - today.get(Calendar.DAY_OF_YEAR) > 3)
                || (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
            age--;

            // If birth date and todays date are of same month and birth day of
            // month is greater than todays day of month then decrement age
        } else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
                && (birthDate.get(Calendar.DAY_OF_MONTH) > today
                        .get(Calendar.DAY_OF_MONTH))) {
            age--;
        }

        return age;
    }
    
    public static int getUmur(String tanggal) {
        int umur = 0;
        Date tglLahir = null;
        long time;
        try {
            time = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal).getTime();
            tglLahir = new Date(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            umur = Fungsi.getAge(tglLahir);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return umur;

    }
    
    public static String konversiTgl(String inputTgl){
        String tanggal="";
        try {
            
            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dTanggal = sourceDateFormat.parse(inputTgl);
            tanggal = sdf.format(dTanggal);
        } catch (ParseException ex) {
            Logger.getLogger(Fungsi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tanggal;
    }
    
    public static String konversiCur(String var){
        NumberFormat gFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        String angka="";
        angka=gFormat.format(Integer.parseInt(var));
        return angka;
    }
    
    public static String konversiCurB(String var){
        NumberFormat gFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        String angka="";
        angka=gFormat.format(Integer.parseInt(var));
        return angka;
    }
    
    public static String bulanIndo(String bulan){
        String month=bulan;
        
        switch (month) {
                case "01":
                    month="Januari";
                    break;
                case "02":
                    month="Februari";
                    break;
                case "03":
                    month="Maret";
                    break;
                case "04":
                    month="April";
                    break;
                case "05":
                    month="Mei";
                    break;
                case "06":
                    month="Juni";
                    break;
                case "07":
                    month="Juli";
                    break;
                case "08":
                    month="Agustus";
                    break;
                case "09":
                    month="September";
                    break;
                case "10":
                    month="Oktober";
                    break;
                case "11":
                    month="November";
                    break;
                case "12":
                    month="Desember";
                    break;
            }
        return month;
    }
    
    public static void addEscapeListener(final JDialog dialog) {
        ActionListener escListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        };

        dialog.getRootPane().registerKeyboardAction(escListener,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

    }
    
    public static double getAgeAsOf(Date date, Date birthDay) {
        return (date.getTime() - birthDay.getTime()) / msPerGregorianYear;
    }
    
    public static void changeWindowsDefaultPrinter(String printerName) {
        String cmdLine  = String.format("RUNDLL32 PRINTUI.DLL,PrintUIEntry /y /n \"%s\"", printerName);
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmdLine );
        builder.redirectErrorStream(true);
        Process p = null;
        try { p = builder.start(); }
        catch (IOException e) { e.printStackTrace(); }

        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = new String();
        while (true) {
            try {
                line = r.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) { break; }
            System.out.println( "result  "  + line);
        }
    }
}
