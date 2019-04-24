/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.model;

/**
 *
 * @author DAFI
 */
public class UserSession1 {
   private static String user, nama, kodeDokter;

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        UserSession1.user = user;
    }

    public static String getNama() {
        return nama;
    }

    public static void setNama(String nama) {
        UserSession1.nama = nama;
    }

    public static String getKodeDokter() {
        return kodeDokter;
    }

    public static void setKodeDokter(String kodeDokter) {
        UserSession1.kodeDokter = kodeDokter;
    }
    
    
}
