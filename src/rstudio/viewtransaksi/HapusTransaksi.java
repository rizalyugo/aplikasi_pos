/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewtransaksi;

import rstudio.viewpilih.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import rstudio.config.Fungsi;
import rstudio.config.Koneksi;
import rstudio.config.TableColumnAdjuster;
import rstudio.model.Barang;
import rstudio.model.RincianPenj;
import rstudio.model.RincianPenj2TableModel;
import rstudio.model.RiwayatTransaksi;
import rstudio.model.RiwayatTransaksiTM;
import rstudio.model.UserSession;

/**
 *
 * @author DAFI
 */
public class HapusTransaksi extends javax.swing.JDialog {

    Connection con;
    RiwayatTransaksiTM tableRiwayat;
    RincianPenj2TableModel tableRincian;
    private Barang barang;
    private String sortBarang;
    Fungsi f;
    private String noFak="";
    //TableFilterHeader filterHeader;
    /**
     * Creates new form DataBarang
     */
    public HapusTransaksi(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        con = Koneksi.getKoneksi();
        tampilRiwayat();
        f.addEscapeListener(this);
        
        
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
    
    public void tampilRiwayat(){
        tableRiwayat = new RiwayatTransaksiTM();
        int no =1;
        try{
            String tampil="SELECT * FROM tb_penjualan WHERE DATE(tanggal) BETWEEN DATE_ADD(CURDATE(), INTERVAL - 1 MONTH) AND CURDATE()";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
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
            Logger.getLogger(HapusTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(no>1){
                int firstRow = jTableRiwayat.convertRowIndexToView(0);
                jTableRiwayat.setRowSelectionInterval(firstRow, firstRow);
                jTableRiwayat.requestFocus();
                tampilDetailBeli(noFak);
            }
        }
    }
    
    private String stokTerakhir(String kodeBrg){
        PreparedStatement stat = null;
        String stokBrg="";
        try{
            String tampil="SELECT stok from tb_barang where kode_brg=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, kodeBrg);
            ResultSet set=stat.executeQuery();
    
            if (set.next()){
                
                stokBrg=set.getString("stok");
            }
            
            
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        return stokBrg;
    }
    
    private void updateStok(String kodeBrg, String jml){
        String update="UPDATE tb_barang set stok=stok+? WHERE kode_brg=?";
        PreparedStatement statement = null;
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, jml);
                statement.setString(2, kodeBrg);
             
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }
    }
    
    private void updateSaldo(String bank, String saldoKurang){
        String update="UPDATE tb_bank set saldo=saldo-? WHERE nama_bank=?";
        PreparedStatement statement = null;
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, saldoKurang);
                statement.setString(2, bank);
             
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }
    }
    
    private void updateAllKartuStok(String noFaktur){
        PreparedStatement stat = null;
        int total = 0;
        String stokAkhir="";
        int stokLama=0;
        int akhir=0;
        try{
            int no = 1, jum=0, hpp=0;
            String tampil="SELECT p.jenis_pemb, p.bank, dp.* FROM tb_penjualan p JOIN tb_penjualan_detail dp "
                    + "ON p.no_faktur=dp.no_faktur WHERE p.no_faktur=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, noFaktur);
            ResultSet set=stat.executeQuery();
            while (set.next()){
                stokAkhir=stokTerakhir(set.getString("kode_brg"));
                stokLama=Integer.parseInt(stokAkhir);
                akhir=stokLama+Integer.parseInt(set.getString("jml"));
                
                editKartuStok(set.getString("kode_brg"), stokAkhir, set.getString("jml"), String.valueOf(akhir));
                updateStok(set.getString("kode_brg"), set.getString("jml"));
                if(set.getString("jenis_pemb").equals("TRANSFER")){
                    updateSaldo(set.getString("bank"), set.getString("harga_total"));
                }

            }
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }finally{
            hapusTransaksi(noFak);
            hapusDetailTransaksi(noFak);
            JOptionPane.showMessageDialog(null, "Transaksi berhasil dihapus","Informasi",JOptionPane.INFORMATION_MESSAGE);
            tampilRiwayat();
        }
    }
    
    private void hapusTransaksi(String noFak){
       String delete = "DELETE FROM tb_penjualan WHERE no_faktur=?";
        
        PreparedStatement statement = null;
        try{
           statement = con.prepareStatement(delete);
           statement.setString(1, noFak);
           statement.executeUpdate();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: "+ex.getMessage());
        }
   }
    
    private void hapusDetailTransaksi(String noFak){
       String delete = "DELETE FROM tb_penjualan_detail WHERE no_faktur=?";
        
        PreparedStatement statement = null;
        try{
           statement = con.prepareStatement(delete);
           statement.setString(1, noFak);
           statement.executeUpdate();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: "+ex.getMessage());
        }
   }
    
     private void editKartuStok(String kode, String asal, String jumlah, String akhir){
        String insert = "INSERT INTO tb_kartustok(id,kode_brg,tanggal,asal,jumlah,akhir,ket,user) " +
                        " VALUES(null,?,CURRENT_TIMESTAMP(),?,?,?,?,?);";
        PreparedStatement statement = null;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, kode);
                statement.setString(2, asal);
                statement.setString(3, jumlah);
                statement.setString(4, akhir);
                statement.setString(5, "BATAL TRANSAKSI");
                statement.setString(6, UserSession.getUser());
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
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
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jTableRiwayatAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton1.setText("BATAL TRANSAKSI");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableRiwayatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableRiwayatKeyPressed
        // TODO add your handling code here:
        int baris = jTableRiwayat.getSelectedRow();
        
        noFak=jTableRiwayat.getValueAt(baris, 1).toString();
        tampilDetailBeli(noFak);
    }//GEN-LAST:event_jTableRiwayatKeyPressed

    private void jTableRiwayatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableRiwayatMouseClicked
        // TODO add your handling code here:
        int baris = jTableRiwayat.getSelectedRow();
        
        noFak=jTableRiwayat.getValueAt(baris, 1).toString();
        tampilDetailBeli(noFak);
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
        int pilihan = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan membatalkan transaksi?", "Pilihan", JOptionPane.YES_NO_OPTION);
        if(pilihan == 0){
            updateAllKartuStok(noFak);
        }else{

        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(HapusTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HapusTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HapusTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HapusTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HapusTransaksi dialog = new HapusTransaksi(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableDetailRiwayat;
    private javax.swing.JTable jTableRiwayat;
    // End of variables declaration//GEN-END:variables
}
