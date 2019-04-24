/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewtransaksi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
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
import rstudio.model.RincianPenj;
import rstudio.model.RincianPenjTableModel;
import rstudio.model.UserSession;
import rstudio.viewpilih.PilihMember;
import rstudio.viewpilih.PilihProduk;
import rstudio.viewpilih.ReturBarang;
import rstudio.viewpilih.SkipTransaksi;

/**
 *
 * @author PALUPI
 */
public class FormPenjualan extends javax.swing.JDialog {

    Connection con;
    Fungsi f;
    private String tglBln,tglSkrg,blnSkrg;
    private PilihProduk pp = new PilihProduk(null, true);
    private PilihMember pm = new PilihMember(null, true);
    private CheckOut co = new CheckOut(null, true);
    private SkipTransaksi st = new SkipTransaksi(null, true);
    private ReturBarang rb = new ReturBarang(null, true);
    private String jnsTrans="NORMAL";
    RincianPenjTableModel tableRincian;
    
    NumberFormat gFormat;
    private String statusBrg="";
    /**
     * Creates new form FormPenjualan
     */
    public FormPenjualan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        con = Koneksi.getKoneksi();
        tglBln = new SimpleDateFormat("YYMM").format(Calendar.getInstance().getTime());
        tglSkrg = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        blnSkrg = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        addKeyListener(this);
        initListener();
        gFormat = NumberFormat.getNumberInstance();
        tampilDetailBeli("");
        
        txtKodeBrg.requestFocus();
        
        lblNamaBrg.setVisible(false);
        lblHargaJual.setVisible(false);
        lblHargaBeli.setVisible(false);
        lblSatuan.setVisible(false);
        lblJmlBrg.setVisible(false);
        lblHpp.setVisible(false);
        lblKategori.setVisible(false);
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
    public static void addKeyListener(final JDialog dialog) {
   

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
    
    private void clearField(){
        txtKodeBrg.setText("");
        txtTotal.setText("0");
        txtKodeMember.setText("");
        txtMember.setText("");
        lblNamaBrg.setText("");
        lblHargaBeli.setText("0");
        lblHargaJual.setText("0");
        lblSatuan.setText("");
        lblKategori.setText("");
        lblJmlBrg.setText("0");
        lblHpp.setText("0");
        lblFakturRetur.setText("-");
        lblRetur.setText("");
        lblReturKet.setText("");
        statusBrg="NORMAL";
    }
    
    private void simpanPenjualan(String diskon, String totalAkhir){
        String insert = "INSERT INTO tb_penjualan(no_faktur,tanggal,jml,total,petugas,id_member,nama_member,diskon,total_akhir,hpp,status)"
                + " VALUES(?, CURRENT_TIMESTAMP(), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;  
        try{
                String kodeMember="-",member="-";
                if(!(txtKodeMember.getText().isEmpty() && txtMember.getText().isEmpty()) ){
                    kodeMember=txtKodeMember.getText();
                    member=txtMember.getText();
                }
                int total = gFormat.parse(txtTotal.getText()).intValue();
                
                statement = con.prepareStatement(insert);
                statement.setString(1, txtNoFaktur.getText());
                statement.setString(2, lblJmlBrg.getText());
                statement.setString(3, String.valueOf(total));
                statement.setString(4, UserSession.getUser());
                statement.setString(5, kodeMember);
                statement.setString(6, member);
                statement.setString(7, diskon);
                statement.setString(8, totalAkhir);
                statement.setString(9, lblHpp.getText());
                statement.setString(10, "0");
                statement.executeUpdate();
                
                //JOptionPane.showMessageDialog(null, "Pengadaan berhasil disimpan","Informasi",JOptionPane.INFORMATION_MESSAGE);
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             } catch (ParseException ex) {
            Logger.getLogger(FormPenjualan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void updatePenjualan(String diskon, String totalAkhir, String jenisPemb, String bank, String jatuhtempo, String byr){
        String update="UPDATE tb_penjualan set tanggal=CURRENT_TIMESTAMP(),jml=?,total=?,"
                + "id_member=?,nama_member=?,diskon=?,total_akhir=?,hpp=?, jenis_pemb=?, bank=?, tempo=?, bayar=? WHERE no_faktur=?";
        PreparedStatement statement = null;
            try{
                String kodeMember="-",member="-";
                if(!(txtKodeMember.getText().isEmpty() && txtMember.getText().isEmpty()) ){
                    kodeMember=txtKodeMember.getText();
                    member=txtMember.getText();
                }
                int total = gFormat.parse(txtTotal.getText()).intValue();
                
                
                statement = con.prepareStatement(update);
                statement.setString(1, lblJmlBrg.getText());
                statement.setString(2, String.valueOf(total));
                statement.setString(3, kodeMember);
                statement.setString(4, member);
                statement.setString(5, diskon);
                statement.setString(6, totalAkhir);
                statement.setString(7, lblHpp.getText());
                statement.setString(8, jenisPemb);
                statement.setString(9, bank);
                statement.setString(10, jatuhtempo);
                statement.setString(11, byr);
                statement.setString(12, txtNoFaktur.getText());
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            } catch (ParseException ex) {
                Logger.getLogger(FormPenjualan.class.getName()).log(Level.SEVERE, null, ex);
            }
            
    }
    
    private void cekTransaksiSblmnya(){
        int jum=0;
        String noFak="0";
        //PreparedStatement stat = null;
        try{
            String tampil="SELECT COUNT(no_faktur) AS tot,no_faktur,status FROM tb_penjualan WHERE status='0'";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            
            while (set.next()){    
                jum=Integer.parseInt(set.getString("tot"));
                noFak=set.getString("no_faktur");
            }
            
            if(jum == 1){
                
                int pilihan = JOptionPane.showConfirmDialog(null, "Transaksi sebelumnya belum selesai! \n Apakah akan melanjutkan transaksi sebelumnya?", "Pilihan", JOptionPane.YES_NO_OPTION);
                if(pilihan == 0){
                    tampilDetailBeli(noFak);
                    txtNoFaktur.setText(noFak);
                }else{
                    int hapus = JOptionPane.showConfirmDialog(null, "Apakah transaksi sebelumnya akan dihapus?", "Pilihan", JOptionPane.YES_NO_OPTION);
                    if(hapus == 0){
                        hapusTransaksi();
                        transaksiBaru();
                    }else{
                        transaksiBaru();
                    }
                }
            }else if(jum > 1){
                int pilihan = JOptionPane.showConfirmDialog(null, "Transaksi sebelumnya belum selesai! \n Apakah akan melanjutkan transaksi sebelumnya?", "Pilihan", JOptionPane.YES_NO_OPTION);
                if(pilihan == 0){
                    lanjutTrans();
                }else{
                    int hapus = JOptionPane.showConfirmDialog(null, "Apakah transaksi sebelumnya akan dihapus?", "Pilihan", JOptionPane.YES_NO_OPTION);
                    if(hapus == 0){
                        hapusTransaksi();
                        transaksiBaru();
                    }else{
                        tampilDetailBeli(noFak);
                        txtNoFaktur.setText(noFak);
                    }
                }
            }else{
                transaksiBaru();
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        } 
    }
    
    private String cekStockAwal(String kodeBrg){
        String stock="";
        PreparedStatement stat = null;
        try{
            String tampil="SELECT stok FROM tb_barang WHERE kode_brg=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, kodeBrg);
            ResultSet set=stat.executeQuery();
            if (set.next()){
                stock=set.getString("stok");
                return stock;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        
        return stock;
    }
    
    private void updateDetailPenjualan(String noFaktur){
        String harga="";
        PreparedStatement stat = null;
        try{
            String tampil="SELECT noitem,no_faktur,kode_brg,jml,jenis_transaksi,faktur_retur"
                    + " FROM tb_penjualan_detail WHERE no_faktur=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, noFaktur);
            ResultSet set=stat.executeQuery();
            String jnsTransaksi="";
            while (set.next()){
                int stokSkrg = Integer.parseInt(cekStockAwal(set.getString("kode_brg")))-Integer.parseInt(set.getString("jml"));
                String jmlBrg="";
                int absJum=0;
                if(set.getString("jenis_transaksi").equals("RETUR")){
                    jnsTransaksi="RETUR PENJUALAN NO FAKTUR "+set.getString("faktur_retur");
                    absJum=Math.abs(Integer.parseInt(set.getString("jml")));
                    jmlBrg=String.valueOf(absJum);
                }else{
                    jmlBrg="-"+set.getString("jml");
                    jnsTransaksi="PENJUALAN NO FAKTUR "+set.getString("no_faktur");
                }
                simpanKartuStok(set.getString("kode_brg"), cekStockAwal(set.getString("kode_brg")), jmlBrg, String.valueOf(stokSkrg),jnsTransaksi);
                
                updateStok(set.getString("jml"), set.getString("kode_brg"));
           }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    
    private void updateStok(String krgStok, String kodeBrg){
        String update="UPDATE tb_barang set stok=stok-? WHERE kode_brg=?";
        PreparedStatement statement = null;
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, krgStok);
                statement.setString(2, kodeBrg);
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }
    }
    
    private void updateStatusJual(String no){
        String update="UPDATE tb_penjualan set status='1' WHERE no_faktur=?";
        PreparedStatement statement = null;
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, no);
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }
    }
    
    private void updateSaldoBank(String saldo,String bank){
        String update="UPDATE tb_bank set saldo=saldo+? WHERE nama_bank=?";
        PreparedStatement statement = null;
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, saldo);
                statement.setString(2, bank);
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }
    }
    
    private void simpanKartuStok(String kodeBrg, String asal, String jml, String akhir, String ket){
        String insert = "INSERT INTO tb_kartustok(id,kode_brg,tanggal,asal,jumlah,akhir,ket,user) " +
                        " VALUES(null,?,CURRENT_TIMESTAMP(),?,?,?,?,?);";
        PreparedStatement statement = null;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, kodeBrg);
                statement.setString(2, asal);
                statement.setString(3, jml);
                statement.setString(4, akhir);
                statement.setString(5, ket);
                statement.setString(6, UserSession.getUser());
                
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }
    }
    
    private void checkOut(){
        try {
                        int bayar = gFormat.parse(co.getTxtBayar().getText()).intValue();
                        int tot = gFormat.parse(co.getTxtTotAkhir().getText()).intValue();
                        if(bayar < tot && !co.getCmbJenisPemb().getSelectedItem().toString().equals("TEMPO") ){
                            JOptionPane.showMessageDialog(null, "Maaf, input nominal harus benar/tidak boleh kurang", "Informasi", JOptionPane.WARNING_MESSAGE);
                        
                        }else{
                            co.setVisible(false);
                            int pilihan = JOptionPane.showConfirmDialog(null, "Cetak struk?", "Pilihan", JOptionPane.YES_NO_OPTION);
                            int totalAkhir = gFormat.parse(co.getTxtTotAkhir().getText()).intValue();
                            String jatuhTempo="0000-00-00";
                                if (co.getTxtTempo().getDate()!=null && co.getCmbJenisPemb().getSelectedItem().toString().equals("TEMPO")){
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    jatuhTempo = simpleDateFormat.format(co.getTxtTempo().getDate());
                                }else{
                                    jatuhTempo="0000-00-00";
                                }
                                
                            if(pilihan == 0){
                                if(co.getCmbJenisPemb().getSelectedItem().toString().equals("TRANSFER")){
                                    updateSaldoBank(String.valueOf(totalAkhir), co.getCmbBank().getSelectedItem().toString());
                                }
                                
                                
                            
                                updatePenjualan(String.valueOf(co.getDiskon()), String.valueOf(totalAkhir), co.getCmbJenisPemb().getSelectedItem().toString(), 
                                        co.getCmbBank().getSelectedItem().toString(), jatuhTempo, String.valueOf(bayar));
                                
                                
                                updateDetailPenjualan(txtNoFaktur.getText());
                                updateStatusJual(txtNoFaktur.getText());
                                co.cetakStruk();
                            }else{
                                if(co.getCmbJenisPemb().getSelectedItem().toString().equals("TRANSFER")){
                                    updateSaldoBank(String.valueOf(totalAkhir), co.getCmbBank().getSelectedItem().toString());
                                }
                                updatePenjualan(String.valueOf(co.getDiskon()), String.valueOf(totalAkhir),
                                        co.getCmbJenisPemb().getSelectedItem().toString(), co.getCmbBank().getSelectedItem().toString(), 
                                        jatuhTempo, String.valueOf(bayar));
                                updateDetailPenjualan(txtNoFaktur.getText());
                                updateStatusJual(txtNoFaktur.getText());
                            }

                            
                            JOptionPane.showMessageDialog(null, "Transaksi berhasil disimpan \nKembalian : "+co.getTxtKembali().getText(), "Informasi", JOptionPane.INFORMATION_MESSAGE);
                            transaksiBaru();
                            jnsTrans="NORMAL";
                            clearField();
                            tampilDetailBeli(txtNoFaktur.getText());
                            
                        }
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(FormPenjualan.class.getName()).log(Level.SEVERE, null, ex);
                    }
    }
    
    private void initListener(){
        co.getTxtBayar().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(Character.isAlphabetic(e.getKeyChar())){
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
//                if(e.getKeyCode() == KeyEvent.VK_F2){
//                    co.getCmbDiskon().requestFocus();
//                }else 
                    if(e.getKeyCode() == KeyEvent.VK_ENTER && !co.getTxtBayar().getText().isEmpty()){
                        checkOut();
                        System.out.println("bayar");
                    }
//                    }else if (e.getKeyCode() == KeyEvent.VK_ENTER && !co.getTxtBayar().getText().isEmpty() && co.getCmbJenisPemb().getSelectedIndex()==2){
//                        co.getTxtTempo().requestFocus();
//                    }
            }

            @Override
            public void keyReleased(KeyEvent e) {
           }
        });
        
        
        co.getTxtTempo().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
//                if(e.getKeyCode() == KeyEvent.VK_F2){
//                    co.getCmbDiskon().requestFocus();
//                }else 
                    if(e.getKeyCode() == KeyEvent.VK_ENTER && !co.getTxtBayar().getText().isEmpty()){
                        checkOut();
                    }   
            }

            @Override
            public void keyReleased(KeyEvent e) {
           }
        });
        
        pp.getjTableBarang().addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    String kodeBrg=pp.getjTableBarang().getValueAt(pp.getjTableBarang().getSelectedRow(), 0).toString();
                    txtKodeBrg.setText(kodeBrg);
                    tampilDataBarangKode(kodeBrg);
                    txtKodeBrg.requestFocus();
                    pp.setVisible(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        pp.getjTableBarang().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                   if(pp.getjTableBarang().getRowCount()!=-1){
                       String kodeBrg=pp.getjTableBarang().getValueAt(pp.getjTableBarang().getSelectedRow(), 0).toString();
                        txtKodeBrg.setText(kodeBrg);
                        tampilDataBarangKode(kodeBrg);
                        txtKodeBrg.requestFocus();
                        pp.setVisible(false);
                   }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        
        st.getjTableRiwayat().addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    String noFaktur=st.getjTableRiwayat().getValueAt(st.getjTableRiwayat().getSelectedRow(), 1).toString();
                    txtNoFaktur.setText(noFaktur);
                    tampilDetailBeli(noFaktur);
                    txtKodeBrg.requestFocus();
                    st.setVisible(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        st.getjTableRiwayat().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                   if(st.getjTableRiwayat().getRowCount()!=-1){
                        String noFaktur=st.getjTableRiwayat().getValueAt(st.getjTableRiwayat().getSelectedRow(), 1).toString();
                        txtNoFaktur.setText(noFaktur);
                        tampilDetailBeli(noFaktur);
                        txtKodeBrg.requestFocus();
                        st.setVisible(false);
                   }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        
        rb.getjTableRincian().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                   if(rb.getjTableRincian().getRowCount()!=-1){
                        statusBrg="retur";
                        String kode=rb.getjTableRincian().getValueAt(rb.getjTableRincian().getSelectedRow(), 1).toString();
                        String fretur=rb.getjTableRincian().getValueAt(rb.getjTableRincian().getSelectedRow(), 6).toString();
                        
                        lblFakturRetur.setText(fretur);
                        txtKodeBrg.setText(kode);
                        tampilDataBarangKode(kode);
                        txtKodeBrg.requestFocus();
                        rb.setVisible(false);
                   }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        
        pm.getjTableMember().addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    String idMember=pm.getjTableMember().getValueAt(pm.getjTableMember().getSelectedRow(), 0).toString();
                    String namaMember=pm.getjTableMember().getValueAt(pm.getjTableMember().getSelectedRow(), 1).toString();
                    txtKodeMember.setText(idMember);
                    txtMember.setText(namaMember);
                    pm.setVisible(false);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        pm.getjTableMember().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                   if(pm.getjTableMember().getRowCount()!=-1){
                       String idMember=pm.getjTableMember().getValueAt(pm.getjTableMember().getSelectedRow(), 0).toString();
                        String namaMember=pm.getjTableMember().getValueAt(pm.getjTableMember().getSelectedRow(), 1).toString();
                        txtKodeMember.setText(idMember);
                        txtMember.setText(namaMember);
                        pm.setVisible(false);
                   }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
        
    }
    
    private void bayar(){
        lblStatusAksi.setText("");
        co.setLocationRelativeTo(null);
        co.clearField();
        co.setDiskon(0);
        co.getLbNoFaktur().setText(txtNoFaktur.getText());
        co.getTxtTotal().setText(txtTotal.getText());
        co.getTxtTotAkhir().setText(txtTotal.getText());
        co.getCmbJenisPemb().requestFocus();
        co.setVisible(true);
    }
    
    private void member(){
        pm.setLocationRelativeTo(null);
        pm.setVisible(true);
    }
    
    private void retur(){
        rb.setLocationRelativeTo(null);
        rb.tampilRiwayat();
        rb.setVisible(true);
    }
    
    private void lanjutTrans(){
        st.setLocationRelativeTo(null);
        st.tampilRiwayat();
        st.setVisible(true);
    }
    
    private void tampilDetailBeli(String noFaktur){
        tableRincian = new RincianPenjTableModel();
        PreparedStatement stat = null;
        int total = 0, no =1;
        try{
            int jum=0, hpp=0;
            String tampil="SELECT * FROM tb_penjualan_detail WHERE no_faktur=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, noFaktur);
            ResultSet set=stat.executeQuery();
            while (set.next()){
                RincianPenj db = new RincianPenj();
                
                int jumSkrg;
                if(set.getString("jenis_transaksi").equals("RETUR")){
                    jumSkrg=0;
                }else{
                    jumSkrg=Integer.parseInt(set.getString("jml"));
                }
                
                db.setNo(String.valueOf(no));
                db.setKodeBrg(set.getString("kode_brg"));
                db.setNamaBrg(set.getString("nama_brg"));
                db.setSatuan(set.getString("satuan"));
                db.setJml(set.getString("jml"));
                db.setHargaawal(set.getString("harga_jual_kotor"));
                db.setHargajual(set.getString("harga_jual"));
                db.setDiskon(set.getString("diskon"));
                db.setPotongan(set.getString("potongan"));
                db.setTotal(set.getString("harga_total"));
                db.setId(set.getString("noitem"));
                
                total = total + Integer.parseInt(set.getString("harga_total"));
                jum = jum + jumSkrg ;
                hpp = hpp +  Integer.parseInt(set.getString("harga_beli"))*Integer.parseInt(set.getString("jml"));
                
                lblJmlBrg.setText(String.valueOf(jum));
                lblHpp.setText(String.valueOf(hpp));
                no++;
                tableRincian.add(db);
                
                

            }
            jTablePenjualan.setModel(tableRincian);
            
            jTablePenjualan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTablePenjualan);
            tca.adjustColumns();

            jTablePenjualan.getColumnModel().getColumn(0).setMinWidth(0);
            jTablePenjualan.getColumnModel().getColumn(0).setMaxWidth(0);
            jTablePenjualan.getColumnModel().getColumn(3).setMinWidth(350);
            
            DefaultTableCellRenderer right = new DefaultTableCellRenderer();
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            jTablePenjualan.getColumnModel().getColumn(4).setCellRenderer(right);
            jTablePenjualan.getColumnModel().getColumn(5).setCellRenderer(right);
            jTablePenjualan.getColumnModel().getColumn(6).setCellRenderer(right);
            jTablePenjualan.getColumnModel().getColumn(7).setCellRenderer(right);
            jTablePenjualan.getColumnModel().getColumn(8).setCellRenderer(right);
            jTablePenjualan.getColumnModel().getColumn(10).setCellRenderer(right);
            
            txtTotal.setText(gFormat.format(total));
            
            
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        
    }
     
    
    private void tampilDataBarangKode(String kode) {
        
        PreparedStatement statement = null;
        try {
            String sql = "select * from tb_barang WHERE kode_brg=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, kode);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                lblNamaBrg.setText(set.getString("nama_brg"));
                
                if(!txtKodeMember.getText().isEmpty()){
                   lblHargaJual.setText(set.getString("harga_member"));
                   
                }
                
                if(jnsTrans.equals("NORMAL") && txtKodeMember.getText().isEmpty()){
                    lblHargaJual.setText(set.getString("harga_jual"));
                }else if(jnsTrans.equals("GROSIR") && txtKodeMember.getText().isEmpty()){
                    lblHargaJual.setText(set.getString("harga_grosir"));
                }
                
                lblHargaBeli.setText(set.getString("harga_satuan")); 
                
                lblSatuan.setText(set.getString("satuan"));
                lblKategori.setText(set.getString("id_kat"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Barang tidak ada "+e.getMessage(),"Peringatan",JOptionPane.WARNING_MESSAGE);
        } 
    }
    
    private boolean cekKodeBrg(String kode) {
        boolean status=false;
        PreparedStatement statement = null;
        try {
            String sql = "select kode_brg from tb_barang WHERE kode_brg=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, kode);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                status=true;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Kode barang tidak ditemukan "+e.getMessage(),"Peringatan",JOptionPane.WARNING_MESSAGE);
        } 
        return status;
    }
    
    private int cekStok(String kode) {
        int stok=0;
        PreparedStatement statement = null;
        try {
            String sql = "select stok from tb_barang WHERE kode_brg=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, kode);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                stok=Integer.parseInt(set.getString("stok"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Kode barang tidak ditemukan"+e.getMessage(),"Peringatan",JOptionPane.WARNING_MESSAGE);
        } 
        return stok;
    }
    
    private void cekBarangJual(String noFaktur) {
        boolean status=false;
        
        //return status;
    }
    
    private void transaksiBaru(){
        txtNoFaktur.setText("PJ-"+tglBln+getNoFaktur());
        txtKodeBrg.requestFocus();
        tampilDetailBeli(txtNoFaktur.getText());
    }
    
    private String getNoFaktur(){
        PreparedStatement stat = null;
        String total="0",noFak="0",fakterakhir="0";
        int t=0;
        try{
            String tampil="SELECT SUBSTRING(no_faktur,8) as faktrkhr FROM tb_penjualan WHERE MONTH(tanggal)=? ORDER BY no_faktur DESC LIMIT 1";
            stat=con.prepareStatement(tampil);
            stat.setString(1, blnSkrg);
            ResultSet set=stat.executeQuery();
            if (set.next()){
                 fakterakhir=set.getString("faktrkhr");
                 //System.out.println(fakterakhir);
            }
  
            t = Integer.parseInt(fakterakhir)+1;
//                t = Integer.parseInt(set.getString("tot"))+1;
                total = String.valueOf(t);
                if(total.length()==1){
                    noFak="000"+total;
                }else if(total.length()==2){
                    noFak="00"+total;
                }else if(total.length()==3){
                    noFak="0"+total;
                }else if(total.length()==4){
                    noFak=total;
                }

               
                //noFak = String.valueOf(t);

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        return noFak;
    }
    
    private void simpanBarang(String jumlah, String jenis_trans){
        
            String insert = "INSERT INTO tb_penjualan_detail(no_faktur,kode_brg,nama_brg,jml,harga_jual_kotor,"
                    + "harga_jual,harga_total,harga_beli,satuan,jenis_transaksi,faktur_retur, kategori)"
                    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = null;
            try{
                String fakturRetur;
                if(statusBrg.equals("retur")){
                    fakturRetur=lblFakturRetur.getText();
                }else{
                    fakturRetur="-";
                }
                int hargaTot=Integer.parseInt(jumlah)*Integer.parseInt(lblHargaJual.getText());
                statement = con.prepareStatement(insert);
                statement.setString(1, txtNoFaktur.getText());
                statement.setString(2, txtKodeBrg.getText());
                statement.setString(3, lblNamaBrg.getText());
                statement.setString(4, jumlah);
                statement.setString(5, lblHargaJual.getText());
                statement.setString(6, lblHargaJual.getText());
                statement.setString(7, String.valueOf(hargaTot));
                statement.setString(8, lblHargaBeli.getText());
                statement.setString(9, lblSatuan.getText());
                statement.setString(10, jenis_trans);
                statement.setString(11, fakturRetur);
                statement.setString(12, lblKategori.getText().trim());
                
                statement.executeUpdate();
                
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
            }finally{
                lblFakturRetur.setText("-");
            }
    }
        
    private void updateJumlahBrg(String jmlh,String noItem){
        String insert = "UPDATE tb_penjualan_detail SET jml=jml+?, harga_total=jml*harga_jual WHERE noitem=?";
        PreparedStatement statement = null;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, jmlh);

                statement.setString(2, noItem);
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }
    }
    
    private void updateJumlahBrg2(String jml,String noItem){
        String insert = "UPDATE tb_penjualan_detail SET jml=?, harga_total=?*harga_jual WHERE noitem=?";
        PreparedStatement statement = null;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, jml);
                statement.setString(2, jml);
                statement.setString(3, noItem);
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }
    }
    
    private void updateHargaJual(String harga,String jml,String noItem, String diskon){
        String insert = "UPDATE tb_penjualan_detail SET harga_jual=?, harga_jual_kotor=?, diskon=?, harga_total=?*harga_jual WHERE noitem=?";
        PreparedStatement statement = null;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, harga);
                statement.setString(2, harga);
                statement.setString(3, diskon);
                //statement.setString(4, potongan);
                statement.setString(4, jml);
                statement.setString(5, noItem);
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }
    }
    
    private void updateHargaJualDisk(String harga,String jml,String noItem, String diskon, String potongan){
        String insert = "UPDATE tb_penjualan_detail SET harga_jual=?, diskon=?, potongan=?, harga_total=?*harga_jual WHERE noitem=?";
        PreparedStatement statement = null;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, harga);
                statement.setString(2, diskon);
                statement.setString(3, potongan);
                statement.setString(4, jml);
                statement.setString(5, noItem);
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }
    }
    
    private void hapusItem(String id){
       String delete = "DELETE FROM tb_penjualan_detail WHERE noitem=?";
        
        PreparedStatement statement = null;
        try{
           statement = con.prepareStatement(delete);
           statement.setString(1, id);
           statement.executeUpdate();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: "+ex.getMessage());
        }finally{
            tampilDetailBeli(txtNoFaktur.getText());
        }
   }
    
    private void hapusTransaksi(){
       String delete = "DELETE FROM tb_penjualan WHERE status=?";
        
        PreparedStatement statement = null;
        try{
           statement = con.prepareStatement(delete);
           statement.setString(1, "0");
           statement.executeUpdate();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: "+ex.getMessage());
        }finally{
            tampilDetailBeli(txtNoFaktur.getText());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNoFaktur = new javax.swing.JTextField();
        txtKodeBrg = new javax.swing.JTextField();
        lblNamaBrg = new javax.swing.JLabel();
        lblHargaJual = new javax.swing.JLabel();
        lblHargaBeli = new javax.swing.JLabel();
        lblSatuan = new javax.swing.JLabel();
        txtKodeMember = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMember = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblJenisTrans = new javax.swing.JLabel();
        lblStatusAksi = new javax.swing.JLabel();
        lblRetur = new javax.swing.JLabel();
        lblReturKet = new javax.swing.JLabel();
        lblFakturRetur = new javax.swing.JLabel();
        lblKategori = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTablePenjualan = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtTotal = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        lblJmlBrg = new javax.swing.JLabel();
        lblHpp = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Penjualan");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("NO FAKTUR");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("KODE ITEM");

        txtNoFaktur.setEditable(false);
        txtNoFaktur.setBackground(new java.awt.Color(255, 255, 204));
        txtNoFaktur.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtNoFaktur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoFakturKeyPressed(evt);
            }
        });

        txtKodeBrg.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        txtKodeBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeBrgActionPerformed(evt);
            }
        });
        txtKodeBrg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKodeBrgKeyPressed(evt);
            }
        });

        lblNamaBrg.setText("0");

        lblHargaJual.setText("0");

        lblHargaBeli.setText("0");

        lblSatuan.setText("0");

        txtKodeMember.setEditable(false);
        txtKodeMember.setBackground(new java.awt.Color(255, 255, 204));
        txtKodeMember.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("MEMBER");

        txtMember.setEditable(false);
        txtMember.setBackground(new java.awt.Color(255, 255, 204));
        txtMember.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("TRANSAKSI");

        lblJenisTrans.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblJenisTrans.setForeground(new java.awt.Color(0, 51, 153));
        lblJenisTrans.setText("NORMAL");

        lblStatusAksi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblStatusAksi.setForeground(new java.awt.Color(0, 153, 51));

        lblRetur.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblRetur.setForeground(new java.awt.Color(0, 51, 153));

        lblReturKet.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblReturKet.setForeground(new java.awt.Color(0, 153, 51));

        lblFakturRetur.setText("-");

        lblKategori.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(lblReturKet, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKodeBrg)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(lblNamaBrg)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHargaJual)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHargaBeli)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSatuan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblKategori)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lblStatusAksi, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblFakturRetur))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtKodeMember, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtMember, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(1, 1, 1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(19, 19, 19)
                        .addComponent(lblJenisTrans)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblRetur, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStatusAksi, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFakturRetur)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lblJenisTrans))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtKodeMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNamaBrg)
                        .addComponent(lblHargaJual)
                        .addComponent(lblHargaBeli)
                        .addComponent(lblSatuan)
                        .addComponent(lblKategori)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblReturKet, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(23, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtKodeBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTablePenjualan.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTablePenjualan.setModel(new javax.swing.table.DefaultTableModel(
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
        jTablePenjualan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTablePenjualanKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTablePenjualan);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 204));
        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("End : Bayar");

        lblJmlBrg.setText("0");

        lblHpp.setText("0");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Del : Hapus Item");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Alt : Edit Jumlah");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Shift : Input Diskon");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Ctrl : Edit Harga");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("F7 : Pilih Member");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setText("F9: Transaksi Retur");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setText("F1: Transaksi Baru");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setText("F2: Transaksi Grosir");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel14.setText("F3: Transaksi Normal");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setText("F12: Skip Transaksi");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setText("Enter di Kode Item : Untuk Mencari Barang");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setText("Page Up : Kembali ke Kode Item");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setText("Page Down : Menuju ke Tabel");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4))
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(30, 30, 30)
                        .addComponent(jLabel15))
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblJmlBrg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblHpp)))
                .addGap(72, 72, 72))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(459, 459, 459)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(186, 186, 186)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17))))
                .addGap(345, 345, 345))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel11))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblJmlBrg)
                                    .addComponent(lblHpp)))
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel9)))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel15)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel14))
                                    .addComponent(jLabel16))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel17)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel18)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtKodeBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeBrgActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtKodeBrgActionPerformed

    private void jTablePenjualanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTablePenjualanKeyPressed
        // TODO add your handling code here:
            int baris = jTablePenjualan.getSelectedRow();
        
            String noItem=jTablePenjualan.getValueAt(baris, 0).toString();
            String jmlBrg=jTablePenjualan.getValueAt(baris, 8).toString();
            String hrgBrg=jTablePenjualan.getValueAt(baris, 7).toString();
            String hrgKotor=jTablePenjualan.getValueAt(baris, 4).toString();
            String kdBrg=jTablePenjualan.getValueAt(baris, 3).toString();
            String dDisk=jTablePenjualan.getValueAt(baris, 5).toString();
            
            if(evt.getKeyCode() == KeyEvent.VK_ALT){
            
            int totalInt=0;
            
            int jum=0;
            String jumlah = (String)JOptionPane.showInputDialog(null, "Edit Jumlah","Input", JOptionPane.QUESTION_MESSAGE,null,null,jmlBrg);
            try {
                    jum = Integer.parseInt(jumlah); 
                    
                        PreparedStatement statement = null;
                        try {
                            String sql = "select noitem, kode_brg from tb_penjualan_detail WHERE noitem=?";
                            statement = con.prepareStatement(sql);
                            statement.setString(1, noItem);
                            ResultSet set = statement.executeQuery();
                            if (set.next()) {
                                //if(txtKodeBrg.getText().equals(set.getString("kode_brg"))){
                                    updateJumlahBrg2(String.valueOf(jum), set.getString("noitem"));
                                   // s=1;
                               // }
                            }
                            

                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(),"Peringatan",JOptionPane.WARNING_MESSAGE);
                        }finally{
                            tampilDetailBeli(txtNoFaktur.getText());
                            txtKodeBrg.setText("");
                            txtKodeBrg.requestFocus();
                            
                        }

                
              } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Input jumlah terlebih dahulu", "Peringatan",JOptionPane.ERROR_MESSAGE);
              }
        }else if(evt.getKeyCode() == KeyEvent.VK_DELETE){
            int pilihan = JOptionPane.showConfirmDialog(null, "Yakin akan dihapus?", "Pilihan", JOptionPane.YES_NO_OPTION);
             if(pilihan == 0){
                 hapusItem(noItem);
                 int firstRow = jTablePenjualan.convertRowIndexToView(0);
                 jTablePenjualan.setRowSelectionInterval(firstRow, firstRow);
                 jTablePenjualan.requestFocus();
             }else{
                 
             }
        }if(evt.getKeyCode() == KeyEvent.VK_CONTROL){
            
            int hargaInt=0;
            
            int hrg=0;
            String harga = (String)JOptionPane.showInputDialog(null, "Edit Harga","Konfirmasi", JOptionPane.QUESTION_MESSAGE,null,null,hrgBrg);
            try {
                    hrg = Integer.parseInt(harga); 
                    System.out.println(hrg);
                        PreparedStatement statement = null;
                        try {
                            String sql = "select noitem, kode_brg, harga_jual_kotor from tb_penjualan_detail WHERE noitem=?";
                            statement = con.prepareStatement(sql);
                            statement.setString(1, noItem);
                            ResultSet sets = statement.executeQuery();
                            
                            
                            if (sets.next()) {
                                int ptgan = Integer.parseInt(sets.getString("harga_jual_kotor"))-hrg;
                                //if(txtKodeBrg.getText().equals(set.getString("kode_brg"))){
                                updateHargaJual(harga, jmlBrg, sets.getString("noitem"), dDisk);
                            }else{
                                System.out.println("err");
                            }
                            

                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(),"Peringatan",JOptionPane.WARNING_MESSAGE);
                        }finally{
                            tampilDetailBeli(txtNoFaktur.getText());
                            txtKodeBrg.setText("");
                            txtKodeBrg.requestFocus();
                            
                        }

                
              } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Input jumlah terlebih dahulu", "Peringatan",JOptionPane.ERROR_MESSAGE);
              }
        }if(evt.getKeyCode() == KeyEvent.VK_SHIFT){
            int diskon=0;
            double presentase=0,hrgDiskon;
            String potong="";
            int total=0, totAkhir=0;
            String dsk = (String)JOptionPane.showInputDialog(null, "Input Diskon (%)","Konfirmasi", JOptionPane.QUESTION_MESSAGE,null,null,dDisk);
            try {
                    diskon = Integer.parseInt(dsk); 
                    
                        PreparedStatement statement = null;
                        try {
                            String sql = "select noitem, kode_brg from tb_penjualan_detail WHERE noitem=?";
                            statement = con.prepareStatement(sql);
                            statement.setString(1, noItem);
                            ResultSet set = statement.executeQuery();
                            if (set.next()) {
                                presentase = diskon;
                                total = Integer.parseInt(hrgKotor);
                                hrgDiskon =  ( ((double) presentase /  100.0)* (double) total );
                                totAkhir =  (int) (total - hrgDiskon);
                                potong=String.valueOf(hrgDiskon);
                                updateHargaJualDisk(String.valueOf(totAkhir), jmlBrg, set.getString("noitem"), dsk,potong);
                            }
                            

                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, e.getMessage(),"Peringatan",JOptionPane.WARNING_MESSAGE);
                        }finally{
                            tampilDetailBeli(txtNoFaktur.getText());
                            txtKodeBrg.setText("");
                            txtKodeBrg.requestFocus();
                            
                        }

                
              } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Input jumlah terlebih dahulu", "Peringatan",JOptionPane.ERROR_MESSAGE);
              }
        }else if(evt.getKeyCode() == KeyEvent.VK_END){
            bayar();
        }else if(evt.getKeyCode() == KeyEvent.VK_PAGE_UP){
            txtKodeBrg.requestFocus();
        }else if(evt.getKeyCode() == KeyEvent.VK_F7){
            member();
        }else if(evt.getKeyCode() == KeyEvent.VK_F1){
            cekTransaksiSblmnya();
            jnsTrans="NORMAL";
        }else if(evt.getKeyCode() == KeyEvent.VK_F2){
            jnsTrans="GROSIR";
            lblJenisTrans.setText(jnsTrans);
        }else if(evt.getKeyCode() == KeyEvent.VK_F3){
            jnsTrans="NORMAL";
            lblJenisTrans.setText(jnsTrans);
        }else if(evt.getKeyCode() == KeyEvent.VK_F12){
            transaksiBaru();
            jnsTrans="NORMAL";
            lblJenisTrans.setText(jnsTrans);
            lblStatusAksi.setText("SKIP TRANSAKSI");
        }
        
    }//GEN-LAST:event_jTablePenjualanKeyPressed

    private void txtKodeBrgKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKodeBrgKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN){
            int firstRow = jTablePenjualan.convertRowIndexToView(0);
            jTablePenjualan.setRowSelectionInterval(firstRow, firstRow);
            jTablePenjualan.requestFocus();
        }else if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(txtKodeBrg.getText().isEmpty() && !txtNoFaktur.getText().isEmpty() ){
                pp.setLocationRelativeTo(null);
                pp.tampilBarang();
                pp.getTxtNama().requestFocus();
                pp.setVisible(true);
            }else if(!txtKodeBrg.getText().isEmpty() && !txtNoFaktur.getText().isEmpty()){
                int jum=0;
                tampilDataBarangKode(txtKodeBrg.getText());
                String jumlah = (String)JOptionPane.showInputDialog(null, "Input Jumlah","Input", JOptionPane.QUESTION_MESSAGE,null,null,"1");
                try {
                    if(cekKodeBrg(txtKodeBrg.getText()) == true){
                        jum = Integer.parseInt(jumlah); 
                        if(jum > cekStok(txtKodeBrg.getText())){
                            JOptionPane.showMessageDialog(null, "stok tidak cukup","Peringatan",JOptionPane.WARNING_MESSAGE);
                        }else{
                            PreparedStatement statement = null;
                            int s=0;
                            try {
                                String sql = "select noitem, kode_brg from tb_penjualan_detail WHERE no_faktur=?";
                                statement = con.prepareStatement(sql);
                                statement.setString(1, txtNoFaktur.getText());
                                ResultSet set = statement.executeQuery();
                                while (set.next()) {
                                    if(txtKodeBrg.getText().equals(set.getString("kode_brg"))){
                                        updateJumlahBrg(String.valueOf(jum), set.getString("noitem"));
                                        s=1;
                                    }
                                }

                                if(s == 0){
                                        if(txtTotal.getText().equals("0")){
                                            simpanPenjualan("0", "0");
                                            if(statusBrg.equals("retur")){
                                                jum = -1;
                                                simpanBarang(String.valueOf(jum), "RETUR");
                                                statusBrg="REPLACEMENT";
                                            }else if(statusBrg.equals("REPLACEMENT")){
                                                simpanBarang(String.valueOf(jum), "REPLACEMENT");                                            
                                            }else{
                                                simpanBarang(String.valueOf(jum), jnsTrans);
                                            }
                                            
                                        }else{
                                            if(statusBrg.equals("retur")){
                                                jum = -1;
                                                simpanBarang(String.valueOf(jum), "RETUR");
                                                statusBrg="REPLACEMENT";
                                            }else if(statusBrg.equals("REPLACEMENT")){
                                                simpanBarang(String.valueOf(jum), "REPLACEMENT");                                            
                                            }else{
                                                simpanBarang(String.valueOf(jum), jnsTrans);
                                            }
                                            
                                        }
                                    }

                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, e.getMessage(),"Peringatan",JOptionPane.WARNING_MESSAGE);
                            }finally{
                                tampilDetailBeli(txtNoFaktur.getText());
                                txtKodeBrg.setText("");
                                txtKodeBrg.requestFocus();
                            }


                        }

                    }else{
                        JOptionPane.showMessageDialog(null, "Kode barang tidak ditemukan","Peringatan",JOptionPane.WARNING_MESSAGE);
                    }

                  } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Input jumlah terlebih dahulu", "Peringatan",JOptionPane.ERROR_MESSAGE);
                  }
            }else{
               JOptionPane.showMessageDialog(null, "No Faktur Tidak Boleh Kosong", "Peringatan",JOptionPane.ERROR_MESSAGE); 
            }
        }else if(evt.getKeyCode() == KeyEvent.VK_END){
            bayar();
        }else if(evt.getKeyCode() == KeyEvent.VK_F7){
            member();
        }else if(evt.getKeyCode() == KeyEvent.VK_F1){
            cekTransaksiSblmnya();
            jnsTrans="NORMAL";
        }else if(evt.getKeyCode() == KeyEvent.VK_F2){
            jnsTrans="GROSIR";
            lblJenisTrans.setText(jnsTrans);
        }else if(evt.getKeyCode() == KeyEvent.VK_F3){
            jnsTrans="NORMAL";
            lblJenisTrans.setText(jnsTrans);
        }else if(evt.getKeyCode() == KeyEvent.VK_F12){
            transaksiBaru();
            jnsTrans="NORMAL";
            lblJenisTrans.setText(jnsTrans);
            lblStatusAksi.setText("SKIP TRANSAKSI");
        }else if(evt.getKeyCode() == KeyEvent.VK_F9){
            if(lblRetur.getText().isEmpty()){
                lblRetur.setText("- RETUR");
                lblReturKet.setText("F10 - Pilih Retur");
            }else{
                lblRetur.setText("");
                lblReturKet.setText("");
            } 
        }else if(evt.getKeyCode() == KeyEvent.VK_F10 && txtKodeBrg.getText().isEmpty() && lblRetur.getText().equals("- RETUR")){
            retur();
        }
                    
    }//GEN-LAST:event_txtKodeBrgKeyPressed

    private void txtNoFakturKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoFakturKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtNoFakturKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formKeyPressed

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
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPenjualan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormPenjualan dialog = new FormPenjualan(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTablePenjualan;
    private javax.swing.JLabel lblFakturRetur;
    private javax.swing.JLabel lblHargaBeli;
    private javax.swing.JLabel lblHargaJual;
    private javax.swing.JLabel lblHpp;
    private javax.swing.JLabel lblJenisTrans;
    private javax.swing.JLabel lblJmlBrg;
    private javax.swing.JLabel lblKategori;
    private javax.swing.JLabel lblNamaBrg;
    private javax.swing.JLabel lblRetur;
    private javax.swing.JLabel lblReturKet;
    private javax.swing.JLabel lblSatuan;
    private javax.swing.JLabel lblStatusAksi;
    private javax.swing.JTextField txtKodeBrg;
    private javax.swing.JTextField txtKodeMember;
    private javax.swing.JTextField txtMember;
    private javax.swing.JTextField txtNoFaktur;
    private javax.swing.JFormattedTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
