/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewpilih;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import rstudio.config.Fungsi;
import rstudio.config.Koneksi;
import rstudio.config.TableColumnAdjuster;
import net.coderazzi.filters.gui.AutoChoices;
import net.coderazzi.filters.gui.TableFilterHeader;
import rstudio.model.MasterBrg;
import rstudio.model.MasterBrgTM;
import rstudio.model.UserSession;

/**
 *
 * @author DAFI
 */
public class StockOpname extends javax.swing.JDialog implements KeyListener {

    Connection con;
    MasterBrgTM tableBarang;
    Fungsi f;
    
    public char kar;
    public String str;
    private String sortBarang;
    TableFilterHeader filterHeader;
    NumberFormat gFormat;
    private int stockLama;
    private String tglSkrg;
    private String stokSkrg,kodeBrg;
    

    /**
     * Creates new form FormSupplier
     */
    public StockOpname(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        addEscapeListener(this);
        con = Koneksi.getKoneksi();
        gFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        tglSkrg = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        btnOpname.setEnabled(false);
        tampilBarang();

        filterHeader = new TableFilterHeader(jTableBarang, AutoChoices.DISABLED);

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
 
    

    
   
    
    
    private void tampilBarang(){
        tableBarang = new MasterBrgTM();
        try{
            String tampil="SELECT * FROM tb_barang b JOIN tb_kategori k ON b.id_kat=k.id_kat "
                    + "JOIN tb_suplier s ON b.id_supplier=s.id_supplier ORDER BY nama_brg ASC";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            while (set.next()){
                MasterBrg b = new MasterBrg();
                
                SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dExpire = sourceDateFormat.parse(set.getString("expire"));
                String expire = sdf.format(dExpire);
                
                b.setKodeBrg(set.getString("kode_brg"));
                b.setKategori(set.getString("kategori"));
                b.setNamaBrg(set.getString("nama_brg"));
                b.setSatuan(set.getString("satuan"));
                b.setHbeli(set.getString("harga_satuan"));
                b.setHumum(set.getString("harga_jual"));
                b.setHgrosir(set.getString("harga_grosir"));
                b.setHmember(set.getString("harga_member"));
                b.setSuplier(set.getString("nama_supplier"));
                b.setStok(set.getString("stok"));
                b.setExpire(expire);
                
                tableBarang.add(b);
            }
            jTableBarang.setModel(tableBarang);
            
            jTableBarang.getColumnModel().getColumn(1).setMinWidth(350);

            
            jTableBarang.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTableBarang);
            tca.adjustColumns();
            
            
            
            DefaultTableCellRenderer right = new DefaultTableCellRenderer();
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            jTableBarang.getColumnModel().getColumn(4).setCellRenderer(right);
            jTableBarang.getColumnModel().getColumn(5).setCellRenderer(right);
            jTableBarang.getColumnModel().getColumn(6).setCellRenderer(right);
            jTableBarang.getColumnModel().getColumn(7).setCellRenderer(right);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    
    private void updateBarang(String kode, String stok){
        String update="UPDATE tb_barang set stok=? WHERE kode_brg=?";
        PreparedStatement statement = null;
        String berat;
       
             try{
                statement = con.prepareStatement(update);
                statement.setString(1, stok);
                statement.setString(2, kode);
                
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Sukses");
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }finally{
                tampilBarang();
            }
    }
    
    
   private void editKartuStok(String kode, String stok, String ket){
        String insert = "INSERT INTO tb_kartustok(id,kode_brg,tanggal,asal,jumlah,akhir,ket,user) " +
                        " VALUES(null,?,CURRENT_TIMESTAMP(),?,?,?,?,?);";
        PreparedStatement statement = null;
           int jumlah = Integer.parseInt(stok)-stockLama;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, kode);
                statement.setString(2, String.valueOf(stockLama));
                statement.setString(3, String.valueOf(jumlah));
                statement.setString(4, stok);
                statement.setString(5, "STOCK OPNAME : "+ket);
                statement.setString(6, UserSession.getUser());
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnOpname = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableBarang = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Input Barang");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Stock Opname");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        btnOpname.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnOpname.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonIcon/blog_accept.png"))); // NOI18N
        btnOpname.setText("Stock Opname");
        btnOpname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpnameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOpname)
                .addContainerGap(969, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(btnOpname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Barang"));

        jTableBarang.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTableBarang.setModel(new javax.swing.table.DefaultTableModel(
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
        jTableBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableBarangMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jTableBarangMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(jTableBarang);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1102, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpnameActionPerformed
        String jumlah = (String)JOptionPane.showInputDialog(null, "Masukan Stok Baru","Input", JOptionPane.QUESTION_MESSAGE,null,null,String.valueOf(stockLama));
      if(stockLama!=Integer.parseInt(jumlah)){
        String keterangan = (String)JOptionPane.showInputDialog(null, "Masukan Keterangan","Input", JOptionPane.QUESTION_MESSAGE,null,null,"-");
        editKartuStok(kodeBrg,jumlah,keterangan);
        updateBarang(kodeBrg, jumlah);
      }else if(stockLama==Integer.parseInt(jumlah)){
            JOptionPane.showMessageDialog(null, "Stok yang anda masukan sama dengan sebelumnya", "Informasi",JOptionPane.INFORMATION_MESSAGE);
       }
      btnOpname.setEnabled(false);
        
    }//GEN-LAST:event_btnOpnameActionPerformed

    private void jTableBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBarangMouseClicked
        // TODO add your handling code here:
       
        int row = jTableBarang.getSelectedRow();
        stockLama=Integer.parseInt(jTableBarang.getValueAt(row, 9).toString());
        kodeBrg=jTableBarang.getValueAt(row, 0).toString();
        
        if(!kodeBrg.equals("")){
            btnOpname.setEnabled(true);
        }
        
        
        
    }//GEN-LAST:event_jTableBarangMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:\
        //str = evt.getKeyChar(evt.getKeyCode());
        //jLabel15.setText(str);
    }//GEN-LAST:event_formKeyPressed

    private void jTableBarangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBarangMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableBarangMouseEntered

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
            java.util.logging.Logger.getLogger(StockOpname.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockOpname.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockOpname.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockOpname.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                StockOpname dialog = new StockOpname(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnOpname;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableBarang;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        kar = e.getKeyChar();
       // str = kar;
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
