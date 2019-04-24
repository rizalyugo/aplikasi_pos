/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewlaporan;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import rstudio.config.Koneksi;
import rstudio.report.Report;

/**
 *
 * @author DAFI
 */
public class LaporanPenjualan extends javax.swing.JDialog {

    Connection con;
    private String dTglAwal,dTglAkhir,tglSkrg;
    
    /**
     * Creates new form Sales
     */
    public LaporanPenjualan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
                
        con = Koneksi.getKoneksi();
        addEscapeListener(this);
        tglSkrg = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        initDate();
        tampilCmbKategori();
    }
    
  
    private void tampilCmbKategori(){
        try{
            String tampil="SELECT * FROM tb_kategori";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            cmbKategori.addItem("Semua");
            while (set.next()){
                cmbKategori.addItem(set.getString("id_kat")+" - "+set.getString("kategori"));  
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
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
    
    private void initDate(){
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.MONTH, 0);
//        cal.set(Calendar.DATE, 1);
//        Date firstDateOfPreviousMonth = cal.getTime();
//
//        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal
//
//        Date lastDateOfPreviousMonth = cal.getTime();
//        
       SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            
//        dTglAwal = new SimpleDateFormat("yyyy-MM-dd").format(firstDateOfPreviousMonth);
//        dTglAkhir = new SimpleDateFormat("yyyy-MM-dd").format(lastDateOfPreviousMonth);
//
        try {
            Date dTgl = sourceDateFormat.parse(tglSkrg);
//            Date dcTglAkhir = sourceDateFormat.parse(dTglAkhir);
            tglAwal.setDate(dTgl);
            tglAkhir.setDate(dTgl);
       } catch (ParseException ex) {
            Logger.getLogger(LaporanPenjualan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String tampilSetting(String id){
        String nilai;
        PreparedStatement statement = null;
        try{
            String tampil="SELECT nilai FROM tb_usaha WHERE id=?";
            statement=con.prepareStatement(tampil);
            statement.setString(1, id);
            ResultSet set=statement.executeQuery();
            if (set.next()){
                nilai=set.getString("nilai");
                //biaya=set.getString("biaya");
                return nilai;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        return null;
    }
    
    private String totalPenjualan(){
        String nilai="";
        PreparedStatement statement = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String dTglAwal = simpleDateFormat.format(tglAwal.getDate());
            String dTglAkhir = simpleDateFormat.format(tglAkhir.getDate());
            
        String idkat="";
            
            if(cmbKategori.getSelectedIndex() == 0){
                idkat="";
            }else{
                String [] pecahIdkat = cmbKategori.getSelectedItem().toString().split("-");
                idkat = pecahIdkat[0].trim();
            }
            
        try{
            String tampil="SELECT SUM(pd.harga_total) as totAkhir FROM tb_penjualan p JOIN tb_penjualan_detail pd " +
                        "ON p.no_faktur=pd.no_faktur WHERE p.status='1'  AND " +
                        " pd.kategori LIKE ?  AND DATE(p.tanggal) BETWEEN ? AND ?";
            statement=con.prepareStatement(tampil);
            statement.setString(1, "%"+idkat+"%");
            statement.setString(2, dTglAwal);
            statement.setString(3, dTglAkhir);
            ResultSet set=statement.executeQuery();
            if (set.next()){
                nilai=set.getString("totAkhir");
                
                //biaya=set.getString("biaya");
                System.out.println(nilai);
                //return nilai;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        return nilai;
    }
    
    private String totCash(String jenis){
        String nilai="";
        PreparedStatement statement = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String dTglAwal = simpleDateFormat.format(tglAwal.getDate());
            String dTglAkhir = simpleDateFormat.format(tglAkhir.getDate());
            
        String idkat="";
            
            if(cmbKategori.getSelectedIndex() == 0){
                idkat="";
            }else{
                String [] pecahIdkat = cmbKategori.getSelectedItem().toString().split("-");
                idkat = pecahIdkat[0].trim();
            }
            
        try{
            String tampil="SELECT SUM(IF(p.jenis_pemb=?,(pd.harga_total),0)) AS total " +
                          " FROM tb_penjualan p JOIN tb_penjualan_detail pd " +
                          " ON p.no_faktur=pd.no_faktur WHERE p.status='1'  AND " +
                          " pd.kategori LIKE ?  AND DATE(p.tanggal) BETWEEN ? AND ?";
            statement=con.prepareStatement(tampil);
            statement.setString(1, jenis);
            statement.setString(2, "%"+idkat+"%");
            statement.setString(3, dTglAwal);
            statement.setString(4, dTglAkhir);
            ResultSet set=statement.executeQuery();
            if (set.next()){
                nilai=set.getString("total");
                
                //biaya=set.getString("biaya");
                System.out.println(nilai);
                //return nilai;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        return nilai;
    }
    
    private String totalHpp(){
        String nilai="";
        PreparedStatement statement = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String dTglAwal = simpleDateFormat.format(tglAwal.getDate());
            String dTglAkhir = simpleDateFormat.format(tglAkhir.getDate());
            
        String idkat="";
            
            if(cmbKategori.getSelectedIndex() == 0){
                idkat="";
            }else{
                String [] pecahIdkat = cmbKategori.getSelectedItem().toString().split("-");
                idkat = pecahIdkat[0].trim();
            }
            
        try{
            String tampil="SELECT SUM(pd.harga_beli*pd.jml) as hpp FROM tb_penjualan p JOIN tb_penjualan_detail pd " +
                        "ON p.no_faktur=pd.no_faktur WHERE p.status='1'  AND " +
                        " pd.kategori LIKE ?  AND DATE(p.tanggal) BETWEEN ? AND ?";
            statement=con.prepareStatement(tampil);
            statement.setString(1, "%"+idkat+"%");
            statement.setString(2, dTglAwal);
            statement.setString(3, dTglAkhir);
            ResultSet set=statement.executeQuery();
            if (set.next()){
                nilai=set.getString("hpp");
                
                //biaya=set.getString("biaya");
                System.out.println(nilai);
                //return nilai;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        return nilai;
    }
    
    private void cetakLaporan(){
        try {
            InputStream input=Report.class.getResourceAsStream("LapPenjualan.jasper");
                   
            HashMap param = new HashMap();
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dmy = new SimpleDateFormat("dd/MMM/yyyy");
            String dTglAwal = simpleDateFormat.format(tglAwal.getDate());
            String dTglAkhir = simpleDateFormat.format(tglAkhir.getDate());
            String dAwal = dmy.format(tglAwal.getDate());
            String dAkhir = dmy.format(tglAkhir.getDate());
            
            
            String idkat="";
            
            if(cmbKategori.getSelectedIndex() == 0){
                idkat="";
            }else{
                String [] pecahIdkat = cmbKategori.getSelectedItem().toString().split("-");
                idkat = pecahIdkat[0].trim();
            }
            
            param.put("dtglAwal", dTglAwal);
            param.put("dtglAkhir", dTglAkhir);
            param.put("subjudul", "Dari: "+dAwal + " s/d " + dAkhir);
            param.put("instansi", tampilSetting("1"));
            param.put("alamat", "Alamat: "+tampilSetting("2"));
            param.put("telp", "Telepon: "+tampilSetting("3"));
            param.put("kat", "%"+idkat+"%");
            param.put("totAkhir", Double.parseDouble(totalPenjualan()));
            param.put("totCash", Double.parseDouble(totCash("CASH")));
            param.put("totTransfer", Double.parseDouble(totCash("TRANSFER")));
            param.put("totHpp", Double.parseDouble(totalHpp()));
            
            JasperPrint jasperPrint=JasperFillManager.fillReport(input, param, con);
            
            JasperViewer jv=new JasperViewer(jasperPrint, false);

            JDialog dialog = new JDialog(this);//the owner
            dialog.setContentPane(jv.getContentPane());
            dialog.setSize(jv.getSize());
            dialog.setTitle("Cetak Laporan Penjualan");
            dialog.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()); 
                dialog.setVisible(true);
            
            
        } catch (JRException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cetakSummary(){
        try {
            InputStream input=Report.class.getResourceAsStream("LapPenjualanSummary.jasper");
                   
            HashMap param = new HashMap();
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dmy = new SimpleDateFormat("dd/MMM/yyyy");
            String dTglAwal = simpleDateFormat.format(tglAwal.getDate());
            String dTglAkhir = simpleDateFormat.format(tglAkhir.getDate());
            String dAwal = dmy.format(tglAwal.getDate());
            String dAkhir = dmy.format(tglAkhir.getDate());
            
            String idkat="";
            
            if(cmbKategori.getSelectedIndex() == 0){
                idkat="";
            }else{
                String [] pecahIdkat = cmbKategori.getSelectedItem().toString().split("-");
                idkat = pecahIdkat[0].trim();
            }
            
            param.put("dtglAwal", dTglAwal);
            param.put("dtglAkhir", dTglAkhir);
            param.put("subjudul", "Dari: "+dAwal + " s/d " + dAkhir);
            param.put("instansi", tampilSetting("1"));
            param.put("alamat", "Alamat: "+tampilSetting("2"));
            param.put("telp", "Telepon: "+tampilSetting("3"));
            param.put("kat", "%"+idkat+"%");
    
            param.put("totAkhir", Double.parseDouble(totalPenjualan()));
            param.put("totCash", Double.parseDouble(totCash("CASH")));
            param.put("totTransfer", Double.parseDouble(totCash("TRANSFER")));
            param.put("totHpp", Double.parseDouble(totalHpp()));
            
            JasperPrint jasperPrint=JasperFillManager.fillReport(input, param, con);
            
            JasperViewer jv=new JasperViewer(jasperPrint, false);

            JDialog dialog = new JDialog(this);//the owner
            dialog.setContentPane(jv.getContentPane());
            dialog.setSize(jv.getSize());
            dialog.setTitle("Cetak Laporan Penjualan");
            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolkit.getScreenSize();
            final int x = (screenSize.width - dialog.getWidth()) / 2;
            final int y = (screenSize.height - dialog.getHeight()) / 2;
            dialog.setLocation(x, y);
            dialog.setVisible(true);
            
            
        } catch (JRException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
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

        panelBackgroundImage12 = new rstudio.modul.PanelBackgroundImage1();
        panelTransparant1 = new rstudio.modul.PanelTransparant();
        jLabel1 = new javax.swing.JLabel();
        tglAwal = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        tglAkhir = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        cmbKategori = new javax.swing.JComboBox<>();
        panelTransparant4 = new rstudio.modul.PanelTransparant();
        btnCetak = new javax.swing.JButton();
        btnCetak1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Laporan Penjualan");

        panelTransparant1.setBackground(new java.awt.Color(0, 0, 0));
        panelTransparant1.setToolTipText("");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Dari Tanggal");

        tglAwal.setDateFormatString("dd/MM/yyyy");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Sampai Tanggal");

        tglAkhir.setDateFormatString("dd/MM/yyyy");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Pilih Kategori");

        javax.swing.GroupLayout panelTransparant1Layout = new javax.swing.GroupLayout(panelTransparant1);
        panelTransparant1.setLayout(panelTransparant1Layout);
        panelTransparant1Layout.setHorizontalGroup(
            panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparant1Layout.createSequentialGroup()
                .addGroup(panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTransparant1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 11, Short.MAX_VALUE))
                    .addGroup(panelTransparant1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(32, 32, 32)))
                .addGroup(panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTransparant1Layout.createSequentialGroup()
                        .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(panelTransparant1Layout.createSequentialGroup()
                        .addGroup(panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tglAkhir, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                        .addGap(136, 136, 136))))
        );
        panelTransparant1Layout.setVerticalGroup(
            panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparant1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        panelTransparant4.setBackground(new java.awt.Color(0, 0, 0));

        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonIcon/printer32.png"))); // NOI18N
        btnCetak.setText("Cetak Rinci");
        btnCetak.setOpaque(false);
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        btnCetak1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonIcon/printer32.png"))); // NOI18N
        btnCetak1.setText("Cetak Summary");
        btnCetak1.setOpaque(false);
        btnCetak1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetak1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTransparant4Layout = new javax.swing.GroupLayout(panelTransparant4);
        panelTransparant4.setLayout(panelTransparant4Layout);
        panelTransparant4Layout.setHorizontalGroup(
            panelTransparant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTransparant4Layout.createSequentialGroup()
                .addContainerGap(163, Short.MAX_VALUE)
                .addComponent(btnCetak)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCetak1))
        );
        panelTransparant4Layout.setVerticalGroup(
            panelTransparant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparant4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransparant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetak)
                    .addComponent(btnCetak1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelBackgroundImage12Layout = new javax.swing.GroupLayout(panelBackgroundImage12);
        panelBackgroundImage12.setLayout(panelBackgroundImage12Layout);
        panelBackgroundImage12Layout.setHorizontalGroup(
            panelBackgroundImage12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBackgroundImage12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTransparant4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelBackgroundImage12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBackgroundImage12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTransparant1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(16, Short.MAX_VALUE)))
        );
        panelBackgroundImage12Layout.setVerticalGroup(
            panelBackgroundImage12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBackgroundImage12Layout.createSequentialGroup()
                .addContainerGap(133, Short.MAX_VALUE)
                .addComponent(panelTransparant4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelBackgroundImage12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBackgroundImage12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTransparant1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(86, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBackgroundImage12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelBackgroundImage12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        // TODO add your handling code here:
        cetakLaporan();
    }//GEN-LAST:event_btnCetakActionPerformed

    private void btnCetak1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetak1ActionPerformed
        // TODO add your handling code here:
        cetakSummary();
    }//GEN-LAST:event_btnCetak1ActionPerformed

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
            java.util.logging.Logger.getLogger(LaporanPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LaporanPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LaporanPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LaporanPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LaporanPenjualan dialog = new LaporanPenjualan(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnCetak1;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private rstudio.modul.PanelBackgroundImage1 panelBackgroundImage12;
    private rstudio.modul.PanelTransparant panelTransparant1;
    private rstudio.modul.PanelTransparant panelTransparant4;
    private com.toedter.calendar.JDateChooser tglAkhir;
    private com.toedter.calendar.JDateChooser tglAwal;
    // End of variables declaration//GEN-END:variables
}
