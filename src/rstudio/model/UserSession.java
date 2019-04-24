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
public class UserSession {
   private static String user, nama, level,idHistori;

    public static String getIdHistori() {
        return idHistori;
    }

    public static void setIdHistori(String idHistori) {
        UserSession.idHistori = idHistori;
    }
    
    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        UserSession.user = user;
    }

    public static String getNama() {
        return nama;
    }

    public static void setNama(String nama) {
        UserSession.nama = nama;
    }

    public static String getLevel() {
        return level;
    }

    public static void setLevel(String level) {
        UserSession.level = level;
    }
    
    
}
