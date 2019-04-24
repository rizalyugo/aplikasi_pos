/*
 * To change this template, choose Tools | Templates
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
import java.util.Date;
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
import rstudio.model.DetailBeli;
import rstudio.model.DetailBeliTM;
import rstudio.model.PengadaanTM;
import rstudio.model.UserSession;
import rstudio.viewpilih.PilihProduk;
import rstudio.viewpilih.RiwayatPengadaan;


    
/**
 *
 * @author DAFI
 */
public class FormPengadaan extends javax.swing.JDialog {

    Connection con;
    private PilihProduk pp = new PilihProduk(null, true);
    private RiwayatPengadaan rp = new RiwayatPengadaan(null, true);
    DetailBeliTM detailBeliTable;
    PengadaanTM tablePengadaan;
    private String tglSkrg, tglBln, blnSkrg;
    Fungsi f;
    SimpleDateFormat sdf;
    //private String jumbeli="0",idbeli="";
    //private int bijian=0,hb=0,subtotal=0;
    NumberFormat gFormat;
    /**
     * Creates new form FormPengadaan
     */
    public FormPengadaan(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        con = Koneksi.getKoneksi();
        initListener();
        addEscapeListener(this);
        //tampilSatuan();
        enableFormA(false);
        enableFormB(false);
        
        gFormat = NumberFormat.getNumberInstance();
        
        tglSkrg = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        tglBln = new SimpleDateFormat("YYMM").format(Calendar.getInstance().getTime());
        blnSkrg = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        initDate();

        lblSubTotal.setVisible(false);
        
//        NumberFormat nf = NumberFormat.getNumberInstance(Locale.GERMAN);// Specify specific format here.
//        NumberFormatter nff = new NumberFormatter(nf);
//        DefaultFormatterFactory factory = new DefaultFormatterFactory(nff);
//        
//        txtHBesar.setFormatterFactory(factory);
//        txtBeliBiji.setFormatterFactory(factory);
//        txtJualBiji.setFormatterFactory(factory);
        
        lblFaktur.setVisible(false);
        lblIdBeli.setVisible(false);
        
        tampilCmbSuplier();
        
        oneKey(btnCariProduk, "cariProduk", KeyEvent.VK_F3);
        oneKey(btnBaru, "baru", KeyEvent.VK_F1);
        twoKey(btnSimpan2, "pgdaan", KeyEvent.VK_END, KeyEvent.CTRL_MASK);
        tampilDetailBeli("");
        
        
    }
    
    private void tampilCmbSuplier(){
        try{
            String tampil="SELECT id_supplier,nama_supplier FROM tb_suplier";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            while (set.next()){
                cmbSuplier.addItem(set.getString("id_supplier").trim()+" - "+set.getString("nama_supplier").trim());  
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Suplier tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    
//    private void cekPrevPengadaan(){
//        PreparedStatement stat = null;
//        try{
//            String tampil="SELECT no_faktur FROM tb_pembelian WHERE status=?";
//            stat=con.prepareStatement(tampil);
//            stat.setString(1, "0");
//            ResultSet set=stat.executeQuery();
//            if (set.next()){
//                int pilih=JOptionPane.showConfirmDialog(null, "Transaksi sebelumnya belum selesai, apakah anda akan melanjutkan?","Informasi",JOptionPane.YES_NO_OPTION);
//                if(pilih==0){
//                    tampilDetailBeli();
//                    txtNoFaktur.setText(set.getString("no_faktur"));
//                    enableFormA(true);
//                    enableFormB(true);
//                    initDate();
//                }else{
//                    hapusTrans(set.getString("no_faktur"));
//                }
//            }
//  
//
//        }
//        catch(Exception e){
//            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
//        }
//    }
     
    
    public static void oneKey(final AbstractButton button, String actionName, int key) {
        button.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, 0), actionName);

        button.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        });
    }
    
    public static void twoKey(final AbstractButton button, String actionName, int key, int CombKey) {
        button.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key, CombKey), actionName);

        button.getActionMap().put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        });
    }
    
    private void initDate(){
        try {
            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            Date dTglSkrg = sourceDateFormat.parse(tglSkrg);
            tglNota.setDate(dTglSkrg);
            jatuhTempo.setDate(dTglSkrg);
        } catch (ParseException ex) {
            Logger.getLogger(FormPengadaan.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void clearField(){
        txtFakturTerusan.setText("-");
        tglNota.setDate(null);
        cmbSuplier.setSelectedIndex(0);
        jatuhTempo.setDate(null);
    }
    
    private void clearField2(){
        txtKdBrg.setText("");
        txtNama.setText("");
        txtSatuan.setText("");
        txtJumlah.setText("");
        txtBiji.setText("");
        txtHBesar.setText("");
        txtBeliBiji.setText("");
        txtJualBiji.setText("");
        
    }
    
    private void enableFormA(boolean status){
        txtNoFaktur.setEnabled(status);
        cmbSuplier.setEnabled(status);
        tglNota.setEnabled(status);
        jatuhTempo.setEnabled(status);
    }
    
    private void enableFormB(boolean status){
        txtKdBrg.setEnabled(status);
        btnCariProduk.setEnabled(status);
        txtNama.setEnabled(status);
        txtSatuan.setEnabled(status);
        txtBiji.setEnabled(status);
        txtJumlah.setEnabled(status);
        txtHBesar.setEnabled(status);
        txtBeliBiji.setEnabled(status);
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
    
   private void tampilDetailBeli(String noFak){
        detailBeliTable = new DetailBeliTM();
        PreparedStatement stat = null;
        int total = 0;
        try{
            String tampil="SELECT dp.id_beli, dp.kode_brg,b.nama_brg,dp.satuan, "
                    + "dp.jumlah, dp.harga_beli, dp.harga_total"
                    + " FROM tb_pembelian_detail dp JOIN tb_barang b ON "
                    + "dp.kode_brg=b.kode_brg WHERE dp.no_faktur=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, noFak);
            ResultSet set=stat.executeQuery();
            while (set.next()){
                DetailBeli db = new DetailBeli();
                
                db.setKodeBrg(set.getString("kode_brg"));
                db.setNamaBrg(set.getString("nama_brg"));
                db.setSatuan(set.getString("satuan"));
                db.setJumlah(set.getString("jumlah"));
                db.setHargabeli(set.getString("harga_beli"));
                db.setHargatotal(set.getString("harga_total"));
                db.setIdBeli(set.getString("id_beli"));
                
                total = total + Integer.parseInt(set.getString("harga_total"));
                
                detailBeliTable.add(db);
            }
            jTableBarang.setModel(detailBeliTable);
            
            jTableBarang.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTableBarang);
            tca.adjustColumns();

            jTableBarang.getColumnModel().getColumn(6).setMinWidth(0);
            jTableBarang.getColumnModel().getColumn(6).setMaxWidth(0);
            jTableBarang.getColumnModel().getColumn(1).setMinWidth(250);
            
            DefaultTableCellRenderer right = new DefaultTableCellRenderer();
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            jTableBarang.getColumnModel().getColumn(3).setCellRenderer(right);
            jTableBarang.getColumnModel().getColumn(4).setCellRenderer(right);
            jTableBarang.getColumnModel().getColumn(5).setCellRenderer(right);
            
            
            txtTotal.setText(String.valueOf(total));

        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
    }
     
    
    private void initListener(){
        
        
        btnCariProduk.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                pp.setLocationRelativeTo(null);
                pp.getTxtNama().setText("");
                pp.getTxtKode().setText("");
                pp.tampilBarang();
                pp.setVisible(true);
            }
        });
        
       
        
        pp.getjTableBarang().addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    
                    txtKdBrg.setText(pp.getjTableBarang().getValueAt(pp.getjTableBarang().getSelectedRow(), 0).toString());
                    tampilDataBarang(pp.getjTableBarang().getValueAt(pp.getjTableBarang().getSelectedRow(), 0).toString());
                    txtJumlah.requestFocus();
                    pp.dispose();
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
        
        pp.getjTableBarang().addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                    if(pp.getjTableBarang().getRowCount()!=-1){
                        txtKdBrg.setText(pp.getjTableBarang().getValueAt(pp.getjTableBarang().getSelectedRow(), 0).toString());
                        tampilDataBarang(pp.getjTableBarang().getValueAt(pp.getjTableBarang().getSelectedRow(), 0).toString());
                        
                        pp.dispose();
                    }
                }
                
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        
        rp.getjTableRiwayat().addMouseListener(new MouseListener(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    String noFaktur=rp.getjTableRiwayat().getValueAt(rp.getjTableRiwayat().getSelectedRow(), 1).toString();
                    txtNoFaktur.setText(noFaktur);
                    tampilDetailBeli(noFaktur);
                    rp.setVisible(false);
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
        
        rp.getjTableRiwayat().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()== KeyEvent.VK_ENTER){
                   if(rp.getjTableRiwayat().getRowCount()!=-1){
                        String noFaktur=rp.getjTableRiwayat().getValueAt(rp.getjTableRiwayat().getSelectedRow(), 1).toString();
                        txtNoFaktur.setText(noFaktur);
                        tampilDetailBeli(noFaktur);
                        rp.setVisible(false);
                   }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }
        });
    }
    
    private void tampilDataBarang(String kodeBrg){
        PreparedStatement stat = null;
        try{
            String tampil="SELECT * FROM tb_barang WHERE kode_brg=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, kodeBrg);
            ResultSet set=stat.executeQuery();
            if (set.next()){
                txtNama.setText(set.getString("nama_brg"));
                txtSatuan.setText(set.getString("satuan_beli")); 
                txtBiji.setText(set.getString("isi_kemasan")); 
                txtHBesar.setText(gFormat.format(Integer.parseInt(set.getString("harga_beli")))); 
                txtBeliBiji.setText(gFormat.format(Integer.parseInt(set.getString("harga_satuan")))); 
                txtJualBiji.setText(gFormat.format(Integer.parseInt(set.getString("harga_jual")))); 
                //hargaPerBiji();
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    


     private void updateDetailPembelian(){
        String harga="";
        PreparedStatement stat = null;
        try{
            String tampil="SELECT id_beli,no_faktur,kode_brg,jml_konversi FROM tb_pembelian_detail WHERE no_faktur=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, txtNoFaktur.getText().trim());
            ResultSet set=stat.executeQuery();
            while (set.next()){
                int stokSkrg = Integer.parseInt(cekStockAwal(set.getString("kode_brg")))+Integer.parseInt(set.getString("jml_konversi"));
                simpanKartuStok(set.getString("kode_brg"), cekStockAwal(set.getString("kode_brg")), set.getString("jml_konversi"), String.valueOf(stokSkrg), set.getString("no_faktur"));
                
                updateStok(set.getString("jml_konversi"), set.getString("kode_brg"));
           }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }finally{
            tampilDetailBeli(txtNoFaktur.getText().trim());
            JOptionPane.showMessageDialog(null, "Pengadaan berhasil disimpan","Informasi",JOptionPane.INFORMATION_MESSAGE);
        }
    }
     
     private void simpanKartuStok(String kodeBrg, String asal, String jml, String akhir,String faktur){
        String insert = "INSERT INTO tb_kartustok(id,kode_brg,tanggal,asal,jumlah,akhir,ket,user) " +
                        " VALUES(null,?,CURRENT_TIMESTAMP(),?,?,?,?,?);";
        PreparedStatement statement = null;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, kodeBrg);
                statement.setString(2, asal);
                statement.setString(3, jml);
                statement.setString(4, akhir);
                statement.setString(5, "PENGADAAN NO FAKTUR "+faktur);
                statement.setString(6, UserSession.getUser());
                
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }
    }
    
     
     private void updateBeli(String noFak){
        String update="UPDATE tb_pembelian set total=?, faktur_terusan=?, status='1' WHERE no_faktur=?";
        PreparedStatement statement = null;
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, txtTotal.getText());
                statement.setString(2, txtFakturTerusan.getText());
                statement.setString(3, noFak);
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }
    }
    
     
    private void simpanPengadaan(){
        String insert = "INSERT INTO tb_pembelian(no_faktur,tgl_nota,id_supplier,jatuh_tempo,user,total, faktur_terusan)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;  
        String nota="",tempo="";
   
        String [] pecahSup;
        pecahSup = cmbSuplier.getSelectedItem().toString().trim().split("-");
        
        if (tglNota.getDate()!=null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            nota = simpleDateFormat.format(tglNota.getDate());
            tempo = simpleDateFormat.format(jatuhTempo.getDate());
        }
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, txtNoFaktur.getText().trim());
                statement.setString(2, nota);
                statement.setString(3, pecahSup[0]);
                statement.setString(4, tempo);
                statement.setString(5, UserSession.getUser());
                statement.setString(6, txtTotal.getText());
                statement.setString(7, txtFakturTerusan.getText());
                statement.executeUpdate();
                
                
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }finally{
                //clearField();
                //enableFormA(false);
             }
    }
    
    private void transaksiBaru(){
        txtNoFaktur.setText("PN-"+tglBln+getNoFaktur());
        tampilDetailBeli(txtNoFaktur.getText().trim());
    }
    
    private String getNoFaktur(){
        PreparedStatement stat = null;
        String total="0",noFak="0",fakterakhir="0";
        int t=0;
        try{
            String tampil="SELECT SUBSTRING(no_faktur,8) as faktrkhr FROM tb_pembelian WHERE MONTH(tgl_nota)=? ORDER BY no_faktur DESC LIMIT 1";
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


        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        return noFak;
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
    
    private void lanjutTrans(){
        rp.setLocationRelativeTo(null);
        rp.tampilRiwayat();
        rp.setVisible(true);
    }
    

    private void cekPrevPengadaan(){
        int jum=0;
        String noFak="0";
        //PreparedStatement stat = null;
        try{
            String tampil="SELECT COUNT(no_faktur) AS tot,no_faktur,status FROM tb_pembelian WHERE status='0'";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            
            while (set.next()){    
                jum=Integer.parseInt(set.getString("tot"));
                noFak=set.getString("no_faktur");
            }
            
            if(jum == 1){
                
                int pilihan = JOptionPane.showConfirmDialog(null, "Transaksi sebelumnya belum selesai! \n Apakah akan melanjutkan transaksi sebelumnya?", "Pilihan", JOptionPane.YES_NO_OPTION);
                if(pilihan == 0){
                    txtNoFaktur.setText(noFak);
                    tampilDetailBeli(noFak);
                    
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
                        tampilDetailBeli(txtNoFaktur.getText().trim());
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

    private void hapusTransaksi(){
       String delete = "DELETE FROM tb_pembelian WHERE status=?";
        
        PreparedStatement statement = null;
        try{
           statement = con.prepareStatement(delete);
           statement.setString(1, "0");
           statement.executeUpdate();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: "+ex.getMessage());
        }finally{
            tampilDetailBeli(txtNoFaktur.getText().trim());
        }
   }
    
    private void simpanBarang(){
        if(txtJumlah.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"Jumlah Beli tidak boleh kosong","Peringatan",JOptionPane.INFORMATION_MESSAGE);
        }else if(txtNoFaktur.getText().isEmpty()){
            JOptionPane.showMessageDialog(null,"No Faktur tidak boleh kosong","Peringatan",JOptionPane.INFORMATION_MESSAGE);
            txtNoFaktur.requestFocus();
        }else{
            try{
            String insert = "INSERT INTO tb_pembelian_detail(id_beli,no_faktur,kode_brg,satuan,jumlah,"
                    + "harga_beli,harga_total,jml_konversi)"
                    + " VALUES(null, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = null;
            int hb=0,biji=0,hbbiji=0,jbiji=0,total=0;
            
            if(txtBiji.getText().isEmpty()){
                biji=1;
            }else{
                biji=Integer.parseInt(txtBiji.getText());
            } 

                try {
                    hb=gFormat.parse(txtHBesar.getText()).intValue();
                    hbbiji=gFormat.parse(txtBeliBiji.getText()).intValue();
                    jbiji=gFormat.parse(txtJualBiji.getText()).intValue();
                } catch (ParseException ex) {
                    Logger.getLogger(FormPengadaan.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            

            int sub_total=Integer.parseInt(txtJumlah.getText())*hb;
            int total_biji = Integer.parseInt(txtJumlah.getText())*biji;

            int belibiji=gFormat.parse(txtBeliBiji.getText()).intValue();
            int jualbiji=gFormat.parse(txtJualBiji.getText()).intValue();
            
            try{
                statement = con.prepareStatement(insert);
                statement.setString(1, txtNoFaktur.getText().trim());
                statement.setString(2, txtKdBrg.getText());
                statement.setString(3, txtSatuan.getText());
                statement.setString(4, txtJumlah.getText());
                statement.setString(5, String.valueOf(hb));
                statement.setString(6, String.valueOf(sub_total));
                statement.setString(7, String.valueOf(total_biji));
                
                statement.executeUpdate();
                
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
            }finally{
                clearField2();
                tampilDetailBeli(txtNoFaktur.getText().trim());
                
               // updateSubTotal(String.valueOf(sub_total));
            }
            }catch(ParseException ex){
                        Logger.getLogger(FormPengadaan.class.getName()).log(Level.SEVERE,null, ex);
            }
        }
    }
    
    private void updateStok(String tambahanStok, String kodeBrg){
        String update="UPDATE tb_barang set stok=stok+? WHERE kode_brg=?";
        PreparedStatement statement = null;
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, tambahanStok);
                statement.setString(2, kodeBrg);
                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }
    }
    

    
    private void updateJumBeli(String jumlah,String bijian, String hb,String id){
        String update="UPDATE tb_pembelian_detail set jumlah=?, jml_konversi=?, harga_total=? WHERE id_beli=?";
        PreparedStatement statement = null;
        int totalSkrg=0;    
        try{
                //totalSkrg=Integer.parseInt(txtTotal.getText())-subtotal;
                int total_biji = Integer.parseInt(jumlah)*Integer.parseInt(bijian);
                int sub_total=Integer.parseInt(jumlah)*Integer.parseInt(hb);
            
                statement = con.prepareStatement(update);
                statement.setString(1, jumlah);
                statement.setString(2, String.valueOf(total_biji));
                statement.setString(3, String.valueOf(sub_total));
                statement.setString(4, id);
                
                //totalSkrg=totalSkrg+sub_total;
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }finally{
                tampilDetailBeli(txtNoFaktur.getText().trim());
                //txtTotal.setText(String.valueOf(totalSkrg));
            }
    }
    

    private void hapusTrans(String faktur){
        String delete = "DELETE FROM tb_pembelian_detail WHERE no_faktur=?";
        int totalSkrg=0;
        PreparedStatement statement = null;
        try{
            statement = con.prepareStatement(delete);
            statement.setString(1, faktur);
            statement.executeUpdate();
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: "+ex.getMessage());
            
        }finally{
            txtTotal.setText(String.valueOf(totalSkrg));
            tampilDetailBeli(txtNoFaktur.getText().trim());
        }
    }

    
    private void hapusBarang(String id_beli){
        String delete = "DELETE FROM tb_pembelian_detail WHERE id_beli=?";
        int totalSkrg=0;
        PreparedStatement statement = null;
        try{
            statement = con.prepareStatement(delete);
            statement.setString(1, id_beli);
            statement.executeUpdate();
            //totalSkrg=Integer.parseInt(txtTotal.getText())-subtotal;
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: "+ex.getMessage());
            
        }finally{
            //txtTotal.setText(String.valueOf(totalSkrg));
            tampilDetailBeli(txtNoFaktur.getText().trim());
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
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtNoFaktur = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        tglNota = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jatuhTempo = new com.toedter.calendar.JDateChooser();
        cmbSuplier = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        txtFakturTerusan = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        btnCariProduk = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtBiji = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtHBesar = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        txtKdBrg = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblFaktur = new javax.swing.JLabel();
        lblIdBeli = new javax.swing.JLabel();
        txtSatuan = new javax.swing.JTextField();
        lblSubTotal = new javax.swing.JLabel();
        stockLama = new javax.swing.JLabel();
        txtBeliBiji = new javax.swing.JFormattedTextField();
        txtJualBiji = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableBarang = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnSimpan2 = new javax.swing.JButton();
        btnBaru = new javax.swing.JButton();
        txtTotal = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Form Pengadaan Barang");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pengadaan Barang");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Pengadaan"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Supplier");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel13.setText("No Faktur");

        txtNoFaktur.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtNoFaktur.setNextFocusableComponent(tglNota);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel14.setText("Tgl Nota");

        tglNota.setDateFormatString("dd/MM/yyyy");
        tglNota.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel18.setText("Jatuh Tempo");

        jatuhTempo.setDateFormatString("dd/MM/yyyy");
        jatuhTempo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jatuhTempo.setNextFocusableComponent(txtKdBrg);

        cmbSuplier.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel16.setText("No Faktur Terusan");

        txtFakturTerusan.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtFakturTerusan.setText("-");
        txtFakturTerusan.setNextFocusableComponent(tglNota);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNoFaktur)
                    .addComponent(tglNota, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addGap(96, 96, 96)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cmbSuplier, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16)
                        .addGap(18, 18, 18)
                        .addComponent(txtFakturTerusan, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(txtFakturTerusan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txtNoFaktur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(cmbSuplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(tglNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18)))
                .addGap(139, 139, 139))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Pilih Barang"));
        jPanel6.setToolTipText("");

        btnCariProduk.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnCariProduk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/browse16.png"))); // NOI18N
        btnCariProduk.setText("F3");
        btnCariProduk.setNextFocusableComponent(txtKdBrg);
        btnCariProduk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariProdukActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Kode Barang");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel4.setText("Nama Barang");

        txtNama.setEditable(false);
        txtNama.setBackground(new java.awt.Color(255, 255, 255));
        txtNama.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtNama.setNextFocusableComponent(txtSatuan);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel7.setText("Satuan");

        txtJumlah.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJumlahActionPerformed(evt);
            }
        });
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtJumlahKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel5.setText("Jumlah Beli (Kemasan)");

        txtBiji.setEditable(false);
        txtBiji.setBackground(new java.awt.Color(255, 255, 255));
        txtBiji.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBiji.setNextFocusableComponent(txtBeliBiji);
        txtBiji.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBijiKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBijiKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel11.setText("Isi Kemasan");

        txtHBesar.setEditable(false);
        txtHBesar.setBackground(new java.awt.Color(255, 255, 255));
        txtHBesar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtHBesar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHBesar.setNextFocusableComponent(txtBiji);
        txtHBesar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHBesarKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel6.setText("Harga Kemasan");

        txtKdBrg.setEditable(false);
        txtKdBrg.setBackground(new java.awt.Color(255, 255, 255));
        txtKdBrg.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtKdBrg.setNextFocusableComponent(txtNama);
        txtKdBrg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtKdBrgKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel9.setText("Harga Satuan");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel15.setText("Harga Jual");

        lblFaktur.setText("jLabel16");

        lblIdBeli.setText("jLabel16");

        txtSatuan.setEditable(false);
        txtSatuan.setBackground(new java.awt.Color(255, 255, 255));
        txtSatuan.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtSatuan.setNextFocusableComponent(txtHBesar);

        txtBeliBiji.setEditable(false);
        txtBeliBiji.setBackground(new java.awt.Color(255, 255, 255));
        txtBeliBiji.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtBeliBiji.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBeliBiji.setNextFocusableComponent(txtJualBiji);
        txtBeliBiji.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBeliBijiKeyReleased(evt);
            }
        });

        txtJualBiji.setEditable(false);
        txtJualBiji.setBackground(new java.awt.Color(255, 255, 255));
        txtJualBiji.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtJualBiji.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtJualBiji.setNextFocusableComponent(txtJumlah);
        txtJualBiji.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJualBijiKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 51, 255));
        jLabel8.setText("(Enter untuk Simpan)");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(txtKdBrg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariProduk))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(txtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(164, 164, 164))
                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIdBeli)
                    .addComponent(lblFaktur))
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel11)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtBiji, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBeliBiji, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtHBesar, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtJualBiji)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(stockLama)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubTotal)
                .addGap(225, 225, 225))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtHBesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBiji, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBeliBiji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(btnCariProduk)
                            .addComponent(txtKdBrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblFaktur)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)
                                .addComponent(lblIdBeli))
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(txtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(txtJualBiji, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblSubTotal, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(stockLama, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        jTableBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableBarangKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableBarang);

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));

        btnSimpan2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnSimpan2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/save16.png"))); // NOI18N
        btnSimpan2.setText("Ctrl + End : Selesai");
        btnSimpan2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSimpan2.setOpaque(false);
        btnSimpan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpan2ActionPerformed(evt);
            }
        });

        btnBaru.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnBaru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/new16.png"))); // NOI18N
        btnBaru.setText("F1: Transaksi Baru");
        btnBaru.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBaru.setOpaque(false);
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });

        txtTotal.setEditable(false);
        txtTotal.setBackground(new java.awt.Color(255, 255, 255));
        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtTotal.setText("0");
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("TOTAL");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Del : Hapus Item");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Alt : Edit Jumlah");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBaru)
                    .addComponent(btnSimpan2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnBaru)
                            .addComponent(jLabel17))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSimpan2)
                            .addComponent(jLabel12))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 1072, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtJumlahKeyTyped

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        // TODO add your handling code here:
        enableFormA(true);
        txtNoFaktur.requestFocus();
        enableFormB(true);
        initDate();
        cekPrevPengadaan();
        
        
    }//GEN-LAST:event_btnBaruActionPerformed

    private void txtBijiKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBijiKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtBijiKeyTyped

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtJumlahKeyReleased

    private void txtHBesarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHBesarKeyReleased
        // TODO add your handling code here:
  
    }//GEN-LAST:event_txtHBesarKeyReleased

    private void txtKdBrgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKdBrgKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar()) || Character.isDigit(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtKdBrgKeyTyped

    private void btnSimpan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpan2ActionPerformed
        // TODO add your handling code here:
        //simpanPengadaan();
        updateBeli(txtNoFaktur.getText().trim());
        updateDetailPembelian();
        transaksiBaru();
        
    }//GEN-LAST:event_btnSimpan2ActionPerformed

    private void jTableBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBarangMouseClicked
        // TODO add your handling code here:
        
        
        
    }//GEN-LAST:event_jTableBarangMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formKeyReleased

    private void btnCariProdukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariProdukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCariProdukActionPerformed

    private void txtBijiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBijiKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBijiKeyReleased

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void txtBeliBijiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBeliBijiKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBeliBijiKeyReleased

    private void txtJualBijiKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJualBijiKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJualBijiKeyReleased

    private void jTableBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableBarangKeyPressed
        // TODO add your handling code here:
//        btnEdit2.setEnabled(true);
//        btnHapus2.setEnabled(true);
//        enableFormB(true);
        int baris = jTableBarang.getSelectedRow();
        String kdBrg=jTableBarang.getValueAt(baris, 0).toString();
        String idBeli=jTableBarang.getValueAt(baris, 6).toString();
        String bijian="0",hb="0";
        PreparedStatement stat = null;
        try{
            String tampil="SELECT isi_kemasan,harga_beli FROM tb_barang WHERE kode_brg=?";
            stat=con.prepareStatement(tampil);
            stat.setString(1, kdBrg);
            ResultSet set=stat.executeQuery();
            if (set.next()){
                bijian=set.getString("isi_kemasan"); 
                hb=set.getString("harga_beli"); 
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Data tidak bisa ditampilkan: "+e.getMessage());
        }
        

        String jumbeli=jTableBarang.getValueAt(baris, 3).toString();
        
        
        if(evt.getKeyCode() == KeyEvent.VK_ALT){
            String jum;
            jum=JOptionPane.showInputDialog(null, "Edit Jumlah Beli","Edit Jumlah",JOptionPane.OK_OPTION,null,null,jumbeli).toString();
            int editjum=0;
            editjum=Integer.parseInt(jum);
            if(editjum!=0){
                updateJumBeli(jum, bijian, hb, idBeli);
            }
         }else if(evt.getKeyCode() == KeyEvent.VK_DELETE){
            int pilihan = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan menghapus barang ini?", "Pilihan", JOptionPane.YES_NO_OPTION);
            if(pilihan == 0){
                hapusBarang(idBeli);
            }else{

            }
         }
    }//GEN-LAST:event_jTableBarangKeyPressed

    private void jTableBarangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBarangMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableBarangMouseEntered

    private void txtJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJumlahActionPerformed
        // TODO add your handling code here:
        if(txtTotal.getText().equals("0")){
            simpanPengadaan();
            simpanBarang();
        }else{
            simpanBarang();
        }
        //txtNoFaktur.setEnabled(false);
    }//GEN-LAST:event_txtJumlahActionPerformed

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
            java.util.logging.Logger.getLogger(FormPengadaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormPengadaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormPengadaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormPengadaan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FormPengadaan dialog = new FormPengadaan(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBaru;
    private javax.swing.JButton btnCariProduk;
    private javax.swing.JButton btnSimpan2;
    private javax.swing.JComboBox<String> cmbSuplier;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableBarang;
    private com.toedter.calendar.JDateChooser jatuhTempo;
    private javax.swing.JLabel lblFaktur;
    private javax.swing.JLabel lblIdBeli;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel stockLama;
    private com.toedter.calendar.JDateChooser tglNota;
    private javax.swing.JFormattedTextField txtBeliBiji;
    private javax.swing.JTextField txtBiji;
    private javax.swing.JTextField txtFakturTerusan;
    private javax.swing.JFormattedTextField txtHBesar;
    private javax.swing.JFormattedTextField txtJualBiji;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKdBrg;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoFaktur;
    private javax.swing.JTextField txtSatuan;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
