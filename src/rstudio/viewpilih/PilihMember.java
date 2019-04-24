/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewpilih;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import rstudio.config.Fungsi;
import rstudio.config.Koneksi;
import rstudio.config.TableColumnAdjuster;
import rstudio.model.Member;
import rstudio.model.MemberTM;
import rstudio.report.Report;

/**
 *
 * @author DAFI
 */
public class PilihMember extends javax.swing.JDialog {

    Connection con;
    MemberTM tableMember;
    private Member member;
    Fungsi f;
    private String kodeMember="";
    /**
     * Creates new form FormMember
     */
    public PilihMember(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        con = Koneksi.getKoneksi();
        
        txtNama.requestFocus();
        tampilMember();
        f.addEscapeListener(this);
    }
    

    
    
    
    public Member getMember(){
         setVisible(true);
         return member;
    }
    
    public JTable getjTableMember() {
        return jTableMember;
    }
    
    public MemberTM getMode(){
        return tableMember;
    }
    
    private void tampilMember(){
        tableMember = new MemberTM();
        try{
            String tampil="SELECT * FROM tb_member";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            while (set.next()){
                Member gb = new Member();
                
                gb.setKode(set.getString("id_member"));
                gb.setNama(set.getString("nama_member"));
                gb.setTelepon(set.getString("telp"));
                gb.setAlamat(set.getString("alamat"));
                
                tableMember.add(gb);
            }
            jTableMember.setModel(tableMember);
            
            jTableMember.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTableMember);
            tca.adjustColumns();

            jTableMember.getColumnModel().getColumn(1).setMinWidth(250);
            jTableMember.getColumnModel().getColumn(2).setMinWidth(250);
            jTableMember.getColumnModel().getColumn(3).setMinWidth(150);
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    
    
     private void cetakLaporan(){
        try {
            InputStream input=Report.class.getResourceAsStream("RiwayatMember.jasper");
                   
            HashMap param = new HashMap();
            
   
            param.put("idMember", kodeMember);
            
            JasperPrint jasperPrint=JasperFillManager.fillReport(input, param, con);
            
            JasperViewer jv=new JasperViewer(jasperPrint, false);

            JDialog dialog = new JDialog(this);//the owner
            dialog.setContentPane(jv.getContentPane());
            dialog.setSize(jv.getSize());
            dialog.setTitle("Cetak Laporan");
            final Toolkit toolkit = Toolkit.getDefaultToolkit();
            final Dimension screenSize = toolkit.getScreenSize();
            final int x = (screenSize.width - dialog.getWidth()) / 2;
            final int y = (screenSize.height - dialog.getHeight()) / 2;
            dialog.setLocation(x, y);
//dialog.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()); 
                dialog.setVisible(true);
            
            
        } catch (JRException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cariMember(){
        tableMember = new MemberTM();
        PreparedStatement stat = null;
        try{
            String tampil="SELECT * FROM tb_member WHERE nama_member LIKE ?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, "%"+txtNama.getText()+"%");
            ResultSet set=stat.executeQuery();
            while (set.next()){
                Member gb = new Member();
                
                gb.setKode(set.getString("id_member"));
                gb.setNama(set.getString("nama_member"));
                gb.setTelepon(set.getString("telp"));
                gb.setAlamat(set.getString("alamat"));
                
                tableMember.add(gb);
            }
            jTableMember.setModel(tableMember);
            
            jTableMember.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTableMember);
            tca.adjustColumns();

            jTableMember.getColumnModel().getColumn(1).setMinWidth(250);
            jTableMember.getColumnModel().getColumn(2).setMinWidth(250);
            jTableMember.getColumnModel().getColumn(3).setMinWidth(150);
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }finally{
            int firstRow = jTableMember.convertRowIndexToView(0);
            jTableMember.setRowSelectionInterval(firstRow, firstRow);
            jTableMember.requestFocus();
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

        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableMember = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        bntRiwayat = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Member");

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Member"));

        jTableMember.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableMember.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMemberMouseClicked(evt);
            }
        });
        jTableMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableMemberKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTableMember);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Cari Member");

        txtNama.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });
        txtNama.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNamaKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("(Enter)");

        bntRiwayat.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        bntRiwayat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/browse16.png"))); // NOI18N
        bntRiwayat.setText("Lihat Riwayat Pembelian");
        bntRiwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bntRiwayatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1019, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2))
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bntRiwayat, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(bntRiwayat)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableMemberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMemberMouseClicked
        // TODO add your handling code here:
        int row = jTableMember.getSelectedRow();
        kodeMember=jTableMember.getValueAt(row, 0).toString();

    }//GEN-LAST:event_jTableMemberMouseClicked

    private void txtNamaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtNamaKeyReleased

    private void jTableMemberKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableMemberKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_F1){
            txtNama.requestFocus();
        }
    }//GEN-LAST:event_jTableMemberKeyPressed

    private void txtNamaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN){
            int firstRow = jTableMember.convertRowIndexToView(0);
            jTableMember.setRowSelectionInterval(firstRow, firstRow);
            jTableMember.requestFocus();
        }
    }//GEN-LAST:event_txtNamaKeyPressed

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
        cariMember();
    }//GEN-LAST:event_txtNamaActionPerformed

    private void bntRiwayatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bntRiwayatActionPerformed
        // TODO add your handling code here:
        cetakLaporan();
    }//GEN-LAST:event_bntRiwayatActionPerformed

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
            java.util.logging.Logger.getLogger(PilihMember.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PilihMember.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PilihMember.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PilihMember.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                PilihMember dialog = new PilihMember(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton bntRiwayat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableMember;
    private javax.swing.JTextField txtNama;
    // End of variables declaration//GEN-END:variables
}
