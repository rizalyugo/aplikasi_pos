/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import rstudio.config.Koneksi;


/**
 *
 * @author DAFI
 */
public class SettingPrinter extends javax.swing.JDialog {

    Connection con;
    static HashMap conf = new HashMap ();
    
    private static String printer1,printer2,printer3;
    private File configFile = new File("config/config.properties");
    private Properties configProps;
    
    /**
     * Creates new form SettingPrinter
     */
    public SettingPrinter(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        showPrinter();
        
        con = Koneksi.getKoneksi();
        
        addEscapeListener(this);

        try {
                loadProperties();
        } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "The config.properties file does not exist, default properties loaded.");
        }
        tampilSetting();
    }
    
//    public static void readConf(){
//        try{
//            FileInputStream fstream = new FileInputStream("config/config.properties");
//            DataInputStream in = new DataInputStream(fstream);
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String strLine;
//            String line[];
//            while ((strLine = br.readLine()) != null) {
//            if(!strLine.startsWith("#") && strLine.length()!=0){ //tidak membaca pada baris yang di awali # dan baris kosong
//                line = strLine.split(":"); // memisah karakter : menjadi array
//                line[0].trim();
//                line[1].trim();
//                conf.put(line[0],line[1]); // memasukkan nilai pada Hash
//            }
//        }
//            in.close();
//        }catch (Exception e){
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//    
//    public static String getPrinterA(){
//        String u ="";
//         if(conf!=null){
//            u=conf.get("printerA").toString().trim();
//        }
//         return u;
//    }
//    
//    public static String getPrinterB(){
//        String u ="";
//         if(conf!=null){
//            u=conf.get("printerB").toString().trim();
//        }
//         return u;
//    }
//    
//    public static String getPrinterC(){
//        String u ="";
//         if(conf!=null){
//            u=conf.get("printerC").toString().trim();
//        }
//         return u;
//    }
    
//    public static void changeProperty(String key, String value) throws IOException {
//        Properties prop =new Properties();
//        prop.load(new FileInputStream("config/database.conf"));
//        prop.setProperty(key, value);
//        prop.store(new FileOutputStream("config/database.conf"),null);
//     }
    
    private void loadProperties() throws IOException {
		Properties defaultProps = new Properties();
		// sets default properties
//		defaultProps.setProperty("dbHost", "localhost");
//		defaultProps.setProperty("dbPort", "3306");
//		defaultProps.setProperty("dbUser", "root");
//		defaultProps.setProperty("dbPass", "");
//		defaultProps.setProperty("printerA", "Cek");
                
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
    
        
//    public static void changeProperty(String key, String value) throws IOException {
//        final File tmpFile = new File("config/config.properties" + ".tmp");
//        final File file = new File("config/config.properties");
//        PrintWriter pw = new PrintWriter(tmpFile);
//        BufferedReader br = new BufferedReader(new FileReader(file));
//        boolean found = false;
//        final String toAdd = key + ':' + value;
//        for (String line; (line = br.readLine()) != null; ) {
//            if (line.startsWith(key + ':')) {
//                line = toAdd;
//                found = true;
//            }
//            pw.println(line);
//        }
//        if (!found)
//            pw.println(toAdd);
//        br.close();
//        pw.close();
//        tmpFile.renameTo(file);
//    }
//    
    
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
    
    private void tampilSetting(){
        //lblPrinterKartu.setText(configProps.getProperty("printerA"));
        lblPrinterKecil.setText(configProps.getProperty("printerB"));
        lblPrinterBesar.setText(configProps.getProperty("printerC"));
    }
    
    private void updateSetting(String namaprinter, String id){
        String update="UPDATE tb_settingprinter set namaprinter=? WHERE id=?";
        PreparedStatement statement = null;
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, namaprinter);
                statement.setString(2, id);
                
                statement.executeUpdate();
                
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }finally{
                tampilSetting();
            }
    }
    
    private void updateAllSetting(){
        
        
        //updateSetting(lblPrinterKartu.getText(), "1");
        updateSetting(lblPrinterKecil.getText(), "2");
        updateSetting(lblPrinterBesar.getText(), "3");
  


        JOptionPane.showMessageDialog(null, "Setting berhasil diubah");
        tampilSetting();
    }
    
    private void showPrinter(){
        DefaultListModel model = new DefaultListModel();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        //System.out.println("Number of print services: " + printServices.length);
        
        for (PrintService printer : printServices){
            String ItemList = printer.getName();
            model.addElement(ItemList);
        }
        listPrinter.setModel(model);

                
    }
    
    static void changeWindowsDefaultPrinter(String printerName) {
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
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        listPrinter = new javax.swing.JList<>();
        btnPrinterStruk = new javax.swing.JButton();
        btnPrinterSep = new javax.swing.JButton();
        lblPrinterKecil = new javax.swing.JLabel();
        lblPrinterBesar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Setting Printer");

        listPrinter.setBorder(javax.swing.BorderFactory.createTitledBorder("List Printer"));
        listPrinter.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jScrollPane1.setViewportView(listPrinter);

        btnPrinterStruk.setText("PRINTER KERTAS KECIL");
        btnPrinterStruk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrinterStrukActionPerformed(evt);
            }
        });

        btnPrinterSep.setText("PRINTER KERTAS BESAR");
        btnPrinterSep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrinterSepActionPerformed(evt);
            }
        });

        lblPrinterKecil.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPrinterKecil.setText("....");

        lblPrinterBesar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblPrinterBesar.setText("....");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnPrinterSep, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrinterStruk, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblPrinterKecil)
                    .addComponent(lblPrinterBesar))
                .addContainerGap(232, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnPrinterStruk, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPrinterKecil)
                        .addGap(40, 40, 40)
                        .addComponent(btnPrinterSep, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPrinterBesar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrinterStrukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrinterStrukActionPerformed
        // TODO add your handling code here:
        try {
                saveProperties("printerB",listPrinter.getSelectedValue().toString());
                JOptionPane.showMessageDialog(null, 
                                "Printer berhasil diubah!");		
                tampilSetting();
        } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, 
                                "Error saving properties file: " + ex.getMessage());		
        }
        
    }//GEN-LAST:event_btnPrinterStrukActionPerformed

    private void btnPrinterSepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrinterSepActionPerformed
        // TODO add your handling code here:
        try {
                saveProperties("printerC",listPrinter.getSelectedValue().toString());
                JOptionPane.showMessageDialog(null, 
                                "Printer berhasil diubah!");		
                tampilSetting();
        } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, 
                                "Error saving properties file: " + ex.getMessage());		
        }
    }//GEN-LAST:event_btnPrinterSepActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SettingPrinter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SettingPrinter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SettingPrinter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SettingPrinter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SettingPrinter dialog = new SettingPrinter(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrinterSep;
    private javax.swing.JButton btnPrinterStruk;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPrinterBesar;
    private javax.swing.JLabel lblPrinterKecil;
    private javax.swing.JList<String> listPrinter;
    // End of variables declaration//GEN-END:variables
}
