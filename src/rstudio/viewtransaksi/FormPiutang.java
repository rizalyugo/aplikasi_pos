/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewtransaksi;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import rstudio.model.Cicil;
import rstudio.model.CicilTM;
import rstudio.model.RincianPenj;
import rstudio.model.RincianPenj2TableModel;
import rstudio.model.Piutang;
import rstudio.model.PiutangTM;

/**
 *
 * @author DAFI
 */
public class FormPiutang extends javax.swing.JDialog {

    Connection con;
    PiutangTM tablePiutang;
    CicilTM tableCicil;
    private Barang barang;
    private String sortBarang;
    Fungsi f;
    private String noFak="",totAkhir="";
    private String tglSkrg;
    //TableFilterHeader filterHeader;
    /**
     * Creates new form DataBarang
     */
    public FormPiutang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        con = Koneksi.getKoneksi();
        
        f.addEscapeListener(this);
        
        tglSkrg = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
  
        tampilPiutang();
        
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

    
    public Barang getPiutang(){
         setVisible(true);
         return barang;
    }
    
    public JTable getjTablePiutang() {
        return jTablePiutang;
    }
    
    public PiutangTM getMode(){
        return tablePiutang;
    }
    
    private void insertCicil(String noFak, String total){
        String insert = "INSERT INTO tb_cicil(no_faktur, tanggal, total) " +
                        " VALUES(?, CURRENT_TIMESTAMP(),?);";
        PreparedStatement statement = null;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, noFak);
                statement.setString(2, total);

                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }finally{
                 
                 tampilDetailBeli(noFak);
             }
    }
    
  
    public void tampilPiutang(){
        tablePiutang = new PiutangTM();
        int no =1;
        PreparedStatement stat = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try{
            String tampil="SELECT * FROM tb_penjualan WHERE jenis_pemb='TEMPO' and bayar < total_akhir";
            stat=con.prepareStatement(tampil);
//            stat.setString(1, dTglAwal);
//            stat.setString(2, dTglAkhir);
            ResultSet set=stat.executeQuery();
            while (set.next()){
                Piutang b = new Piutang();
                
                SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dTgl = sourceDateFormat.parse(set.getString("tanggal"));
                Date dTglTempo = sourceDateFormat.parse(set.getString("tempo"));
                String tgl = sdf.format(dTgl);
                String tempo = sdf.format(dTglTempo);
                b.setNo(String.valueOf(no));
                b.setNoFaktur(set.getString("no_faktur"));
                b.setTgl(tgl);
                b.setPetugas(set.getString("petugas"));
                b.setTotal(set.getString("total_akhir"));
                b.setMember(set.getString("nama_member"));
                b.setTempo(set.getString("tempo"));
                b.setTotalBayar(set.getString("bayar"));
                int sisa= Integer.parseInt(set.getString("total_akhir"))-Integer.parseInt(set.getString("bayar"));
                b.setSisaHutang(String.valueOf(sisa));
                
                tablePiutang.add(b);
                no++;
            }
            jTablePiutang.setModel(tablePiutang);
            
            jTablePiutang.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTablePiutang);
            tca.adjustColumns();
            
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(FormPiutang.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            if(no>1){
                int firstRow = jTablePiutang.convertRowIndexToView(0);
                jTablePiutang.setRowSelectionInterval(firstRow, firstRow);
                jTablePiutang.requestFocus();
                tampilDetailBeli(noFak);
            }
        }
    }
    

    private void updateCicil(String bayar, String noFaktur){
        String update="UPDATE tb_penjualan set bayar=bayar+? WHERE no_faktur=?";
        PreparedStatement statement = null;
            try{  
                statement = con.prepareStatement(update);
                statement.setString(1, bayar);
                statement.setString(2, noFaktur);
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            } 
            
    }
    
     private void updateLunas(String noFaktur){
        String update="UPDATE tb_penjualan set bayar=total_akhir WHERE no_faktur=?";
        PreparedStatement statement = null;
            try{  
                statement = con.prepareStatement(update);
                statement.setString(1, noFaktur);
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            } finally{
                 tampilPiutang();
            }
            
    }
    
    private void tampilDetailBeli(String noFaktur){
        tableCicil = new CicilTM();
        PreparedStatement stat = null;
        int total = 0;
        try{
            int no = 1, jum=0, hpp=0;
            String tampil="SELECT * FROM tb_cicil WHERE no_faktur=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, noFaktur);
            ResultSet set=stat.executeQuery();
            while (set.next()){
                Cicil db = new Cicil();
                
                db.setNoFak(set.getString("no_faktur"));
                db.setTgl(set.getString("tanggal"));
                db.setTotal(set.getString("total"));

                no++;
                tableCicil.add(db);
            }
            jTableDetailPiutang.setModel(tableCicil);
            
            jTableDetailPiutang.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTableDetailPiutang);
            tca.adjustColumns();

           // jTableDetailPiutang.getColumnModel().getColumn(0).setMinWidth(0);
            //jTableDetailPiutang.getColumnModel().getColumn(0).setMaxWidth(0);
            
//            DefaultTableCellRenderer right = new DefaultTableCellRenderer();
//            right.setHorizontalAlignment(SwingConstants.RIGHT);
//            jTableDetailPiutang.getColumnModel().getColumn(3).setCellRenderer(right);
//            jTableDetailPiutang.getColumnModel().getColumn(4).setCellRenderer(right);
//            jTableDetailPiutang.getColumnModel().getColumn(5).setCellRenderer(right);;
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
        jTablePiutang = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableDetailPiutang = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Riwayat Transaksi");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Riwayat Transaksi (F3)"));

        jTablePiutang.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTablePiutang.setModel(new javax.swing.table.DefaultTableModel(
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
        jTablePiutang.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
                jTablePiutangAncestorMoved(evt);
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jTablePiutang.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTablePiutangFocusGained(evt);
            }
        });
        jTablePiutang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablePiutangMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTablePiutangMousePressed(evt);
            }
        });
        jTablePiutang.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTablePiutangPropertyChange(evt);
            }
        });
        jTablePiutang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTablePiutangKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTablePiutang);

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

        jTableDetailPiutang.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTableDetailPiutang.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableDetailPiutang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableDetailPiutangMouseClicked(evt);
            }
        });
        jTableDetailPiutang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableDetailPiutangKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableDetailPiutang);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 826, Short.MAX_VALUE)
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
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/flat_seo216.png"))); // NOI18N
        jButton1.setText("Cicil");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/save16.png"))); // NOI18N
        jButton3.setText("Lunas");
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
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTablePiutangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTablePiutangKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTablePiutangKeyPressed

    private void jTablePiutangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePiutangMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jTablePiutangMouseClicked

    private void jTableDetailPiutangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableDetailPiutangMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDetailPiutangMouseClicked

    private void jTableDetailPiutangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableDetailPiutangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableDetailPiutangKeyPressed

    private void jTablePiutangFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTablePiutangFocusGained
        // TODO add your handling code here:
        int baris = jTablePiutang.getSelectedRow();
        
        noFak=jTablePiutang.getValueAt(baris, 2).toString();
        tampilDetailBeli(noFak);
    }//GEN-LAST:event_jTablePiutangFocusGained

    private void jTablePiutangAncestorMoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTablePiutangAncestorMoved
        // TODO add your handling code here:
     
    }//GEN-LAST:event_jTablePiutangAncestorMoved

    private void jTablePiutangPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTablePiutangPropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_jTablePiutangPropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String jumlah = (String)JOptionPane.showInputDialog(null, "Masukan Jumalh Cicil","Input", JOptionPane.QUESTION_MESSAGE,null,null,"0");
     // if(stockLama!=Integer.parseInt(jumlah)){
       // String keterangan = (String)JOptionPane.showInputDialog(null, "Masukan Keterangan","Input", JOptionPane.QUESTION_MESSAGE,null,null,"-");
        insertCicil(noFak, jumlah);
       updateCicil(jumlah,noFak);
       tampilPiutang();
//      }else if(stockLama==Integer.parseInt(jumlah)){
//            JOptionPane.showMessageDialog(null, "Stok yang anda masukan sama dengan sebelumnya", "Informasi",JOptionPane.INFORMATION_MESSAGE);
//       }

        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTablePiutangMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePiutangMousePressed
        // TODO add your handling code here:
        int baris = jTablePiutang.getSelectedRow();
        
        noFak=jTablePiutang.getValueAt(baris, 2).toString();
        totAkhir=jTablePiutang.getValueAt(baris, 4).toString();
        tampilDetailBeli(noFak);
    }//GEN-LAST:event_jTablePiutangMousePressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        //String jumlah = (String)JOptionPane.showInputDialog(null, "Masukan Jumalh Cicil","Input", JOptionPane.QUESTION_MESSAGE,null,null,"0");
        //insertCicil(noFak, jumlah);
        int pilihan = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan melunasi hutang?", "Pilihan", JOptionPane.YES_NO_OPTION);
                if(pilihan == 0){
                    updateLunas(noFak);
                }else{
                    System.out.println("no option");
                }
        
       
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
            java.util.logging.Logger.getLogger(FormPiutang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPiutang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPiutang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPiutang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormPiutang dialog = new FormPiutang(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableDetailPiutang;
    private javax.swing.JTable jTablePiutang;
    // End of variables declaration//GEN-END:variables
}
