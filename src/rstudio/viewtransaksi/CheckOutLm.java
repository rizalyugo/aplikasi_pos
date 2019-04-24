/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewtransaksi;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import rstudio.config.Fungsi;
import rstudio.config.Koneksi;
import rstudio.report.Report;

/**
 *
 * @author PALUPI
 */
public class CheckOutLm extends javax.swing.JDialog {

    NumberFormat gFormat;
    Fungsi f;
    private double diskon = 0;
    Connection con;
    /**
     * Creates new form CheckOut
     */
    public CheckOutLm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        con = Koneksi.getKoneksi();
        
        gFormat = NumberFormat.getNumberInstance();
        f.addEscapeListener(this);
        tampilCmbBank();
        cmbBank.setEnabled(false);
        
        lblDiskon.setVisible(false);
        cmbDiskon.setVisible(false);
        txtDiskon.setVisible(false);
        lblF2.setVisible(false);
    }

    public JComboBox<String> getCmbBank() {
        return cmbBank;
    }

    public void setCmbBank(JComboBox<String> cmbBank) {
        this.cmbBank = cmbBank;
    }
    
    
    private void tampilCmbBank(){
        try{
            String tampil="SELECT nama_bank FROM tb_bank";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            while (set.next()){
                cmbBank.addItem(set.getString("nama_bank").trim());  
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Suplier tidak bisa ditampilkan: "+e.getMessage());
        }
    }

    public JComboBox<String> getCmbJenisPemb() {
        return cmbJenisPemb;
    }

    
    public JFormattedTextField getTxtTotal() {
        return txtTotal;
    }

    public JFormattedTextField getTxtTotAkhir() {
        return txtTotAkhir;
    }

    public JFormattedTextField getTxtBayar() {
        return txtBayar;
    }

    public JComboBox<String> getCmbDiskon() {
        return cmbDiskon;
    }

    public double getDiskon() {
        return diskon;
    }

    public void setDiskon(double diskon) {
        this.diskon = diskon;
    }
    
    

    public JFormattedTextField getTxtKembali() {
        return txtKembali;
    }
    
    private String namaApp(String id){
        String nilai="";
        PreparedStatement statement = null;
        try{
            String tampil="SELECT nilai FROM tb_usaha WHERE id=?";
            statement=con.prepareStatement(tampil);
            statement.setString(1, id);
            ResultSet set=statement.executeQuery();
            if (set.next()){
                nilai=set.getString("nilai");
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        return nilai;
    }
    
    public void cetakStruk(){
        //f.changeWindowsDefaultPrinter(setPrinter("2"));
        try {
            InputStream input=Report.class.getResourceAsStream("Struk.jasper");
            
            Map param = new HashMap();
            
            String kembalian="",byrn="";
            int kmbl=0,byr=0;
            if(txtKembali.getText().equals("0")){
                kembalian="0";
            }else{
                
                //NumberFormat gFormat = NumberFormat.getNumberInstance();
                try {
                    kmbl=gFormat.parse(txtKembali.getText()).intValue();
                    
                } catch (ParseException ex) {
                    Logger.getLogger(CheckOutLm.class.getName()).log(Level.SEVERE, null, ex);
                }
                kembalian=String.valueOf(kmbl);
            }
            
            try {
                byr=gFormat.parse(txtBayar.getText()).intValue();
                byrn=String.valueOf(byr);
            } catch (ParseException ex) {
                Logger.getLogger(CheckOutLm.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            param.put("idTransaksi", lbNoFaktur.getText());
            param.put("bayar", Double.parseDouble(byrn));
            param.put("kembali", Double.parseDouble(kembalian));
            param.put("instansi", namaApp("1"));
            param.put("alamat", "Alamat : "+namaApp("2"));
            param.put("noTelp", "Telp & Whatsapp : "+namaApp("3"));
            
            JasperPrint jasperPrint=JasperFillManager.fillReport(input, param, con);
            //JasperViewer jv=new JasperViewer(jasperPrint, false);
            JasperPrintManager.printReport(jasperPrint,false);
        } catch (JRException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
//     private void cetakStrukEsc(){
//        f.changeWindowsDefaultPrinter(setPrinter("2"));
//        //String byr=gFormat.format(Integer.parseInt(txtBayar.getText()));
////        int pilihan = JOptionPane.showConfirmDialog(null, "Langsung dicetak?", "Pilihan", JOptionPane.YES_NO_OPTION);
////            if(pilihan == 0){
//               StrukModel struk = new StrukModel(lblIdTrans.getText(),namaApp("1"), namaApp("2"),txtTotal.getText(), byr, txtKembali.getText(),lblPasien.getText());
//                
//                PreparedStatement stat = null;
//                try{
//                    String tampil="SELECT dp.id,dp.kode_barang,dp.jumlah,dp.total_all,b.stock,b.nama_barang FROM tb_fdetailpos dp JOIN " +
//        "                   tb_fbarang b ON dp.kode_barang=b.kode_barang WHERE id_transaksi=?";
//                    stat=con.prepareStatement(tampil);
//                    stat.setString(1, lblIdTrans.getText());
//                    ResultSet set=stat.executeQuery();
//                    while (set.next()){
//                        String harga=gFormat.format(Integer.parseInt(set.getString("total_all")));
//                        
//                        struk.tambahItemFaktur(new ItemStruk(set.getString("nama_barang"), set.getString("jumlah"),harga));
//                    }
//                }
//                catch(Exception e){
//                    JOptionPane.showMessageDialog(null, "Tidak ada barang: "+e.getMessage());
//                }
//                
//                SimpleEscp simpleEscp = new SimpleEscp(setPrinter("2")); 
//                Template template  = null;
//                try {
//                     template = new JsonTemplate(Report.class.getResourceAsStream("Struk.json"));
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }                      
//                simpleEscp.print(template, DataSources.from(struk));
////            }else{
////                ds = new DialogStruk(null, true);
////                ds.setLocationRelativeTo(null);
////               //pp.tampilPasien();
////               tampilStruk();
////               ds.setVisible(true);
////            } 
//    }
    
    public JLabel getLbNoFaktur(){
        return lbNoFaktur;
    }

    public void clearField() {
        cmbDiskon.setSelectedIndex(0);
        txtDiskon.setValue(null);
        txtBayar.setValue(null);
        txtKembali.setText("");
    }
    
    public static void oneKey(final AbstractButton button, String actionName, int key) {
        button.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0), actionName);

        button.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        });
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblDiskon = new javax.swing.JLabel();
        txtKembali = new javax.swing.JFormattedTextField();
        txtTotal = new javax.swing.JFormattedTextField();
        cmbDiskon = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblF2 = new javax.swing.JLabel();
        txtTotAkhir = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtBayar = new javax.swing.JFormattedTextField();
        txtDiskon = new javax.swing.JFormattedTextField();
        lbNoFaktur = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbJenisPemb = new javax.swing.JComboBox<>();
        cmbBank = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("TOTAL");

        lblDiskon.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        lblDiskon.setText("DISKON");

        txtKembali.setEditable(false);
        txtKembali.setBackground(new java.awt.Color(255, 255, 204));
        txtKembali.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtKembali.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 204));
        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N

        cmbDiskon.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        cmbDiskon.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "%", "Rp" }));
        cmbDiskon.setNextFocusableComponent(txtDiskon);
        cmbDiskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDiskonActionPerformed(evt);
            }
        });
        cmbDiskon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDiskonKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel3.setText("BAYAR");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel4.setText("KEMBALI");

        lblF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblF2.setText("F2 : Diskon");

        txtTotAkhir.setEditable(false);
        txtTotAkhir.setBackground(new java.awt.Color(255, 255, 204));
        txtTotAkhir.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtTotAkhir.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel6.setText("TOTAL AKHIR");

        jSeparator1.setForeground(new java.awt.Color(51, 51, 51));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Enter : Simpan");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Esc : Keluar");

        txtBayar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtBayar.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBayarActionPerformed(evt);
            }
        });
        txtBayar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBayarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBayarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBayarKeyTyped(evt);
            }
        });

        txtDiskon.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtDiskon.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        txtDiskon.setNextFocusableComponent(txtBayar);
        txtDiskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiskonActionPerformed(evt);
            }
        });
        txtDiskon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDiskonKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDiskonKeyReleased(evt);
            }
        });

        lbNoFaktur.setText("jLabel9");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel9.setText("JENIS PEMB");

        cmbJenisPemb.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        cmbJenisPemb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CASH", "TRANSFER" }));
        cmbJenisPemb.setNextFocusableComponent(txtDiskon);
        cmbJenisPemb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbJenisPembActionPerformed(evt);
            }
        });
        cmbJenisPemb.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJenisPembKeyPressed(evt);
            }
        });

        cmbBank.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        cmbBank.setNextFocusableComponent(txtDiskon);
        cmbBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBankActionPerformed(evt);
            }
        });
        cmbBank.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbBankKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel9))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(cmbJenisPemb, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbBank, 0, 249, Short.MAX_VALUE))
                            .addComponent(txtKembali, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotAkhir, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBayar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDiskon)
                                .addGap(90, 90, 90)
                                .addComponent(cmbDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblF2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7)
                                .addGap(44, 44, 44)
                                .addComponent(lbNoFaktur)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbJenisPemb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(cmbBank, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtBayar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lbNoFaktur)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblF2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDiskon)
                    .addComponent(cmbDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBayarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyReleased
        if(!txtBayar.getText().isEmpty()){
            try {                                     
            // TODO add your handling code here:
            int total=0, kembali=0, bayar=0;
            bayar = gFormat.parse(txtBayar.getText()).intValue();
            total=gFormat.parse(txtTotAkhir.getText()).intValue();
            kembali = bayar - total;
            String sKembali="0";
            sKembali=gFormat.format(kembali);
            txtKembali.setText(sKembali);
        } catch (ParseException ex) {
            Logger.getLogger(CheckOutLm.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        
    }//GEN-LAST:event_txtBayarKeyReleased

    private void txtBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBayarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBayarActionPerformed

    private void txtBayarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyPressed
        // TODO add your handling code here:  
    }//GEN-LAST:event_txtBayarKeyPressed

    private void cmbDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDiskonActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cmbDiskonActionPerformed

    private void txtDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiskonActionPerformed
        // TODO add your handling code here:
        txtBayar.setText("");
        txtBayar.requestFocus();
    }//GEN-LAST:event_txtDiskonActionPerformed

    private void txtDiskonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiskonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiskonKeyPressed

    private void txtDiskonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDiskonKeyReleased
        // TODO add your handling code here:
        if(!txtDiskon.getText().isEmpty()){
            try {                                     
            // TODO add your handling code here:
            int jenisDiskon = cmbDiskon.getSelectedIndex();
            double presentase=0;
            int total=0, totAkhir=0;
            total=gFormat.parse(txtTotal.getText()).intValue();
            if(jenisDiskon == 0){
                presentase = gFormat.parse(txtDiskon.getText()).intValue();
                diskon =  ( ((double) presentase /  100.0)* (double) total);
            }else if (jenisDiskon == 1){
                diskon = gFormat.parse(txtDiskon.getText()).intValue();
            }
            
            totAkhir =  (int) (total - diskon);
            String stotAkhir="0";
            stotAkhir=gFormat.format(totAkhir);
            txtTotAkhir.setText(stotAkhir);
        } catch (ParseException ex) {
            Logger.getLogger(CheckOutLm.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_txtDiskonKeyReleased

    private void cmbDiskonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDiskonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            txtDiskon.requestFocus();
        }
    }//GEN-LAST:event_cmbDiskonKeyPressed

    private void txtBayarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKeyTyped
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtBayarKeyTyped

    private void cmbJenisPembActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbJenisPembActionPerformed
        // TODO add your handling code here:
        if(cmbJenisPemb.getSelectedIndex() == 1){
            cmbBank.setEnabled(true);
        }else{
            cmbBank.setEnabled(false);
        }
    }//GEN-LAST:event_cmbJenisPembActionPerformed

    private void cmbJenisPembKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJenisPembKeyPressed
        // TODO add your handling code here:
        if(cmbJenisPemb.getSelectedIndex() == 1 && evt.getKeyCode() == KeyEvent.VK_ENTER){
            cmbBank.requestFocus();
        }else if(cmbJenisPemb.getSelectedIndex() == 0 && evt.getKeyCode() == KeyEvent.VK_ENTER){
            txtBayar.requestFocus();
        }
    }//GEN-LAST:event_cmbJenisPembKeyPressed

    private void cmbBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBankActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_cmbBankActionPerformed

    private void cmbBankKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbBankKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            txtBayar.requestFocus();
        }
    }//GEN-LAST:event_cmbBankKeyPressed

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
            java.util.logging.Logger.getLogger(CheckOutLm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CheckOutLm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CheckOutLm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CheckOutLm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CheckOutLm dialog = new CheckOutLm(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cmbBank;
    private javax.swing.JComboBox<String> cmbDiskon;
    private javax.swing.JComboBox<String> cmbJenisPemb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbNoFaktur;
    private javax.swing.JLabel lblDiskon;
    private javax.swing.JLabel lblF2;
    private javax.swing.JFormattedTextField txtBayar;
    private javax.swing.JFormattedTextField txtDiskon;
    private javax.swing.JFormattedTextField txtKembali;
    private javax.swing.JFormattedTextField txtTotAkhir;
    private javax.swing.JFormattedTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
