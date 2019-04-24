/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.config;



import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ABUL FIDA
 */
public class Koneksi {
    static HashMap conf = new HashMap ();
    
    private static String host;
    private static String user;
    private String pass;
    private String name;
    private String port;
    static Connection con;
    
    private static File configFile = new File("config/config.properties");
    private static Properties configProps;
    
    public Koneksi(){

        //readConf();
        //adkonek();
        
        try {
                loadProperties();
        } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "The config.properties file does not exist, default properties loaded.");
        }
        
    }
    
    private static void loadProperties() throws IOException {
		Properties defaultProps = new Properties();
		 
		configProps = new Properties(defaultProps);
		
		// loads properties from file
		InputStream inputStream = new FileInputStream(configFile);
		configProps.load(inputStream);
		inputStream.close();
	}
	
	private void saveProperties(String key, String namaPrinter) throws IOException {
		configProps.setProperty(key, namaPrinter);
		OutputStream outputStream = new FileOutputStream(configFile);
		configProps.store(outputStream, "host setttings");
		outputStream.close();
	}

    public static void readConf(){
        try{
            FileInputStream fstream = new FileInputStream("config/database.conf");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            String line[];
            while ((strLine = br.readLine()) != null) {
            if(!strLine.startsWith("#") && strLine.length()!=0){ //tidak membaca pada baris yang di awali # dan baris kosong
                line = strLine.split(":"); // memisah karakter : menjadi array
                line[0].trim();
                line[1].trim();
                conf.put(line[0],line[1]); // memasukkan nilai pada Hash
            }
        }
            in.close();
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static String getUser(){
        String u ="";
         if(conf!=null){
            u=conf.get("dbUser").toString().trim();
        }
         return u;
    }
    
    public static String getHost(){
        String u ="";
         if(conf!=null){
            u=conf.get("dbHost").toString().trim();
        }
         return u;
    }
    
    public static String getPass(){
        String u ="";
         if(conf!=null){
            u=conf.get("dbPass").toString().trim();
        }
         return u;
    }
    
    public static String getName(){
        String u ="";
         if(conf!=null){
            u=conf.get("dbName").toString().trim();
        }
         return u;
    }
    
    public static String getPort(){
        String u ="";
         if(conf!=null){
            u=conf.get("dbPort").toString().trim();
        }
         return u;
    }
    
    
    public void konek(){
        if(con == null){
            try{ 
            Class.forName("com.mysql.jdbc.Driver"); 
            con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+name,user,pass);
            
        }catch(Exception e){
            System.out.println("Koneksi gagal");
        }
        }
    }
    
    public static Connection getKoneksi(){
        if(con == null){
         
         try{
             try {
                 //   String a;
                 //a = host;
                 loadProperties();
             } catch (IOException ex) {
                 Logger.getLogger(Koneksi.class.getName()).log(Level.SEVERE, null, ex);
             }
             //readConf();
             //System.out.println("d"+getUser());
            String url ="jdbc:mysql://"+configProps.getProperty("dbHost")+":"+configProps.getProperty("dbPort")+"/"+configProps.getProperty("dbName");
           //String url ="jdbc:mysql://localhost/ummuhani1";

            String usr = configProps.getProperty("dbUser");
            //System.out.println(user);
            String password = configProps.getProperty("dbPass");
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection(url, usr, password);
            }catch(SQLException ex){
             //System.out.println("Koneksi gagal");
             JOptionPane.showMessageDialog(null, "Periksa Koneksi Database");
         }
        }
        return con;
    }
}

