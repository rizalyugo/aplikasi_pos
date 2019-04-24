/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewtransaksi;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import rstudio.viewpilih.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import rstudio.config.Fungsi;
import rstudio.config.Koneksi;
import rstudio.config.TableColumnAdjuster;
import rstudio.model.Barang;
import rstudio.model.RincianPenj;
import rstudio.model.RincianPenj2TableModel;
import rstudio.model.RiwayatTransaksi;
import rstudio.model.RiwayatTransaksiTM;
import rstudio.model.UserSession;
import rstudio.report.Report;

/**
 *
 * @author DAFI
 */
public class HistoriTransaksi extends javax.swing.JDialog {

    Connection con;
    RiwayatTransaksiTM tableRiwayat;
    RincianPenj2TableModel tableRincian;
    private Barang barang;
    private String sortBarang;
    Fungsi f;
    private String noFak="",totAkhir="";
    private String tglSkrg;
    //TableFilterHeader filterHeader;
    /**
     * Creates new form DataBarang
     */
    public HistoriTransaksi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        con = Koneksi.getKoneksi();
        
        f.addEscapeListener(this);
        
        tglSkrg = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        initDate();
        
        tampilRiwayat();
        
        //filterHeader = new TableFilterHeader(jTableBarang, AutoChoices.DISABLED);
        
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

    
    public Barang getRiwayat(){
         setVisible(true);
         return barang;
    }
    
    public JTable getjTableRiwayat() {
        return jTableRiwayat;
    }
    
    public RiwayatTransaksiTM getMode(){
        return tableRiwayat;
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
            Logger.getLogger(HistoriTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void tampilRiwayat(){
        tableRiwayat = new RiwayatTransaksiTM();
        int no =1;
        PreparedStatement stat = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String dTglAwal = simpleDateFormat.format(tglAwal.getDate());
            String dTglAkhir = simpleDateFormat.format(tglAkhir.getDate());
        try{
            String tampil="SELECT * FROM tb_penjualan WHERE DATE(tanggal) BETWEEN ? AND ?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, dTglAwal);
            stat.setString(2, dTglAkhir);
            ResultSet set=stat.executeQuery();
            while (set.next()){
                RiwayatTransaksi b = new RiwayatTransaksi();
                
                SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dTgl = sourceDateFormat.parse(set.getString("tanggal"));
                String tgl = sdf.format(dTgl);
                
                b.setNo(String.valueOf(no));
                b.setNoFaktur(set.getString("no_faktur"));
                b.setTgl(tgl);
                b.setPetugas(set.getString("petugas"));
                b.setTotal(set.getString("total_akhir"));
                
                tableRiwayat.add(b);
                no++;
            }
            jTableRiwayat.setModel(tableRiwayat);
            
            jTableRiwayat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTableRiwayat);
            tca.adjustColumns();
            
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(HistoriTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(no>1){
                int firstRow = jTableRiwayat.convertRowIndexToView(0);
                jTableRiwayat.setRowSelectionInterval(firstRow, firstRow);
                jTableRiwayat.requestFocus();
                tampilDetailBeli(noFak);
            }
        }
    }
    

    

    
   
    private void tampilDetailBeli(String noFaktur){
        tableRincian = new RincianPenj2TableModel();
        PreparedStatement stat = null;
        int total = 0;
        try{
            int no = 1, jum=0, hpp=0;
            String tampil="SELECT * FROM tb_penjualan_detail WHERE no_faktur=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, noFaktur);
            ResultSet set=stat.executeQuery();
            while (set.next()){
                RincianPenj db = new RincianPenj();
                
                db.setKodeBrg(set.getString("kode_brg"));
                db.setNamaBrg(set.getString("nama_brg"));
                db.setJml(set.getString("jml"));
                db.setHargajual(set.getString("harga_jual"));
                db.setTotal(set.getString("harga_total"));
                db.setId(set.getString("noitem"));

                no++;
                tableRincian.add(db);
            }
            jTableDetailRiwayat.setModel(tableRincian);
            
            jTableDetailRiwayat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTableDetailRiwayat);
            tca.adjustColumns();

            jTableDetailRiwayat.getColumnModel().getColumn(0).setMinWidth(0);
            jTableDetailRiwayat.getColumnModel().getColumn(0).setMaxWidth(0);
            
            DefaultTableCellRenderer right = new DefaultTableCellRenderer();
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            jTableDetailRiwayat.getColumnModel().getColumn(3).setCellRenderer(right);
            jTableDetailRiwayat.getColumnModel().getColumn(4).setCellRenderer(right);
            jTableDetailRiwayat.getColumnModel().getColumn(5).setCellRenderer(right);;
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
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
//            if(txtKembali.getText().equals("0")){
//                kembalian="0";
//            }else{
//                
//                //NumberFormat gFormat = NumberFormat.getNumberInstance();
//                try {
//                    kmbl=gFormat.parse(txtKembali.getText()).intValue();
//                    
//                } catch (ParseException ex) {
//                    Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                kembalian=String.valueOf(kmbl);
//            }
            
//            try {
//                byr=gFormat.parse(txtBayar.getText()).intValue();
//                byrn=String.valueOf(byr);
//            } catch (ParseException ex) {
//                Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
//            }
            
            param.put("idTransaksi", noFak);
            param.put("bayar", Double.parseDouble(totAkhir));
            param.put("kembali", Double.parseDouble("0"));
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
    
    public void cetakInvoice(){
        //f.changeWindowsDefaultPrinter(setPrinter("2"));
        try {
            InputStream input=Report.class.getResourceAsStream("Invoice.jasper");
            
            Map param = new HashMap();
            
            String kembalian="",byrn="";
            int kmbl=0,byr=0;
//            if(txtKembali.getText().equals("0")){
//                kembalian="0";
//            }else{
//                
//                //NumberFormat gFormat = NumberFormat.getNumberInstance();
//                try {
//                    kmbl=gFormat.parse(txtKembali.getText()).intValue();
//                    
//                } catch (ParseException ex) {
//                    Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                kembalian=String.valueOf(kmbl);
//            }
            
//            try {
//                byr=gFormat.parse(txtBayar.getText()).intValue();
//                byrn=String.valueOf(byr);
//            } catch (ParseException ex) {
//                Logger.getLogger(CheckOut.class.getName()).log(Level.SEVERE, null, ex);
//            }
            
            param.put("idTransaksi", noFak);
           // param.put("bayar", Double.parseDouble(totAkhir));
          //  param.put("kembali", Double.parseDouble("0"));
            param.put("instansi", namaApp("1"));
            param.put("alamat", "Alamat : "+namaApp("2"));
            param.put("noTelp", "Telp & Whatsapp : "+namaApp("3"));
            
            JasperPrint jasperPrint=JasperFillManager.fillReport(input, param, con);
            //JasperViewer jv=new JasperViewer(jasperPrint, false);
            JasperViewer jv=new JasperViewer(jasperPrint, false);
            
                JDialog dialog = new JDialog(this);//the owner
                dialog.setContentPane(jv.getContentPane());
                dialog.setSize(jv.getSize());
                dialog.setTitle("Cetak Invoice");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRiwayat = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDetailRiwayat = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tglAwal = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        tglAkhir = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Riwayat Transaksi");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Riwayat Transaksi (F3)"));

        jTableRiwayat.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTableRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableRiwayat.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jTableRiwayatAncestorMoved(evt);
            }
        });
        jTableRiwayat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTableRiwayatFocusGained(evt);
            }
        });
        jTableRiwayat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableRiwayatMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableRiwayatMousePressed(evt);
            }
        });
        jTableRiwayat.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTableRiwayatPropertyChange(evt);
            }
        });
        jTableRiwayat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableRiwayatKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableRiwayat);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Detail Transaksi (F5)"));

        jTableDetailRiwayat.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTableDetailRiwayat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableDetailRiwayat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDetailRiwayatMouseClicked(evt);
            }
        });
        jTableDetailRiwayat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableDetailRiwayatKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDetailRiwayat);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/printer.png"))); // NOI18N
        jButton1.setText("Cetak Struk");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Dari Tanggal");

        tglAwal.setDateFormatString("dd/MM/yyyy");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Sampai Tanggal");

        tglAkhir.setDateFormatString("dd/MM/yyyy");

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/browse16.png"))); // NOI18N
        jButton2.setText("Tampilkan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/printer.png"))); // NOI18N
        jButton3.setText("Cetak Invoice");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(tglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)))
                .addGap(141, 141, 141))
            .addGroup(layout.createSequentialGroup()
                .addGap(193, 193, 193)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tglAwal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(tglAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableRiwayatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableRiwayatKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTableRiwayatKeyPressed

    private void jTableRiwayatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRiwayatMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTableRiwayatMouseClicked

    private void jTableDetailRiwayatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDetailRiwayatMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDetailRiwayatMouseClicked

    private void jTableDetailRiwayatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableDetailRiwayatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDetailRiwayatKeyPressed

    private void jTableRiwayatFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTableRiwayatFocusGained
        // TODO add your handling code here:
        int baris = jTableRiwayat.getSelectedRow();
        
        noFak=jTableRiwayat.getValueAt(baris, 1).toString();
        tampilDetailBeli(noFak);
    }//GEN-LAST:event_jTableRiwayatFocusGained

    private void jTableRiwayatAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTableRiwayatAncestorMoved
        // TODO add your handling code here:
     
    }//GEN-LAST:event_jTableRiwayatAncestorMoved

    private void jTableRiwayatPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTableRiwayatPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_jTableRiwayatPropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cetakStruk();
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        tampilRiwayat();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTableRiwayatMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRiwayatMousePressed
        // TODO add your handling code here:
        int baris = jTableRiwayat.getSelectedRow();
        
        noFak=jTableRiwayat.getValueAt(baris, 1).toString();
        totAkhir=jTableRiwayat.getValueAt(baris, 4).toString();
        tampilDetailBeli(noFak);
    }//GEN-LAST:event_jTableRiwayatMousePressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        cetakInvoice();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(HistoriTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HistoriTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HistoriTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HistoriTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HistoriTransaksi dialog = new HistoriTransaksi(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableDetailRiwayat;
    private javax.swing.JTable jTableRiwayat;
    private com.toedter.calendar.JDateChooser tglAkhir;
    private com.toedter.calendar.JDateChooser tglAwal;
    // End of variables declaration//GEN-END:variables
}
