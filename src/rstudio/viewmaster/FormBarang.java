/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewmaster;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
public class FormBarang extends javax.swing.JDialog implements KeyListener {

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
    

    /**
     * Creates new form FormSupplier
     */
    public FormBarang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        enableForm(false);
        enableButtonA(false);
        enableButtonB(false);
        
        con = Koneksi.getKoneksi();
        gFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        tglSkrg = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        initDate();
        tampilBarang();

        f.addEscapeListener(this);
        
        filterHeader = new TableFilterHeader(jTableBarang, AutoChoices.DISABLED);
        //AutoCompletion.enable(cmbKategori);
        tampilCmbKategori();
        //AutoCompletion.enable(cmbSatuan);
        tampilCmbSatuanJual();
        tampilCmbSatuanBeli();
        
        //AutoCompletion.enable(cmbSuplier);
        tampilCmbSuplier();
        lblKode.setVisible(false);
    }
    
    private void initDate(){
        try {
            SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            Date dTglSkrg = sourceDateFormat.parse(tglSkrg);
            expire.setDate(dTglSkrg);
        } catch (ParseException ex) {
            Logger.getLogger(FormBarang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void tampilCmbKategori(){
        try{
            String tampil="SELECT id_kat,kategori FROM tb_kategori";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            while (set.next()){
                cmbKategori.addItem(set.getString("id_kat")+" - "+set.getString("kategori"));  
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Kategori tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    
    private void tampilCmbSatuanJual(){
        try{
            String tampil="SELECT satuan FROM tb_satuan";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            while (set.next()){
                cmbSatuanJual.addItem(set.getString("satuan"));  
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Satuan tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    
    private void tampilCmbSatuanBeli(){
        try{
            String tampil="SELECT satuan FROM tb_satuan";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            while (set.next()){
                cmbSatuanBeli.addItem(set.getString("satuan"));  
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Satuan tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    
    private void tampilCmbSuplier(){
        try{
            String tampil="SELECT id_supplier,nama_supplier FROM tb_suplier";
            Statement stat=con.createStatement();
            ResultSet set=stat.executeQuery(tampil);
            while (set.next()){
                cmbSuplier.addItem(set.getString("id_supplier")+" - "+set.getString("nama_supplier"));  
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Suplier tidak bisa ditampilkan: "+e.getMessage());
        }
    }
    
    private void aktif(boolean baru, boolean simpan, boolean ubah, boolean hapus, boolean batal) {
        btnBaru.setEnabled(baru);
        btnSimpan.setEnabled(simpan);
        btnEdit.setEnabled(ubah);
        btnHapus.setEnabled(hapus);
        btnBatal.setEnabled(batal);
    }
    
    private void tampilDataBarangKode(String kode) {
        
        PreparedStatement statement = null;
        try {
            String sql = "select b.*, k.kategori, s.nama_supplier from tb_barang b  "
                    + "JOIN tb_kategori k ON b.id_kat=k.id_kat JOIN tb_suplier s ON b.id_supplier=s.id_supplier "
                    + "where b.kode_brg=?";
            statement = con.prepareStatement(sql);
            statement.setString(1, kode);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                
                stockLama=Integer.parseInt(set.getString("stok"));
                
                lblKode.setText(set.getString("kode_brg"));
                txtKode.setText(set.getString("kode_brg"));
                txtNamaBrg.setText(set.getString("nama_brg"));
                cmbKategori.setSelectedItem(set.getString("id_kat").trim()+" - "+set.getString("kategori").trim());
                cmbSatuanJual.setSelectedItem(set.getString("satuan"));
                cmbSatuanBeli.setSelectedItem(set.getString("satuan_beli"));
                txtHargaKemasan.setText(set.getString("harga_beli"));
                txtIsiKemasan.setText(set.getString("isi_kemasan"));
                txtHargaSatuan.setText(set.getString("harga_satuan"));
                txtUntung.setText(set.getString("margin"));
                txtHJual.setText(set.getString("harga_jual"));
                txtUntungGrosir.setText(set.getString("margin_grosir"));
                txtGrosir.setText(set.getString("harga_grosir"));
                txtUntungMember.setText(set.getString("margin_member"));
                txtMember.setText(set.getString("harga_member"));
                cmbSuplier.setSelectedItem(set.getString("id_supplier").trim()+" - "+set.getString("nama_supplier").trim());
                SimpleDateFormat sourceDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dExpire = sourceDateFormat.parse(set.getString("expire"));
                expire.setDate(dExpire);
                txtStok.setText(set.getString("stok"));
                
                enableForm(true);
                aktif(false, false, true, true, true);
                //txtKode.requestFocus();
            } else {
                aktif(true, true, false, false, true);
                //txtKode.requestFocus();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    private void enableForm(boolean status){
        txtKode.setEnabled(status);
        txtNamaBrg.setEnabled(status);
        cmbSatuanJual.setEnabled(status);
        cmbSatuanBeli.setEnabled(status);
        txtStok.setEnabled(status);
        txtHargaKemasan.setEnabled(status);
        txtHJual.setEnabled(status);
        txtUntung.setEnabled(status);
        txtIsiKemasan.setEnabled(status);
        txtHargaSatuan.setEnabled(status);
        cmbKategori.setEnabled(status);
        cmbSuplier.setEnabled(status);
        txtUntungGrosir.setEnabled(status);
        txtGrosir.setEnabled(status);
        txtUntungMember.setEnabled(status);
        txtMember.setEnabled(status);
        expire.setEnabled(status);        
    }
    
    private void enableButtonA(boolean status){
        btnEdit.setEnabled(status);
        btnHapus.setEnabled(status);
    }
    
    private void enableButtonB(boolean status){
        btnSimpan.setEnabled(status);
    }
     
    private void clearField(){       
        txtKode.setText("");
        txtNamaBrg.setText("");
        cmbSatuanJual.setSelectedIndex(0);
        cmbSatuanBeli.setSelectedIndex(0);
        txtStok.setText("");
        txtHargaKemasan.setText("");
        txtHJual.setText("");
        txtIsiKemasan.setText("1");
        txtHargaSatuan.setText("");
        cmbKategori.setSelectedIndex(0);
        cmbSuplier.setSelectedIndex(0);
        txtGrosir.setText("0");
        txtMember.setText("");
    }
    
    private void simpanBarang(){
        String insert = "INSERT INTO tb_barang(kode_brg,nama_brg,id_kat,harga_beli,"
                + "isi_kemasan,harga_satuan,margin,harga_jual,margin_grosir,harga_grosir,margin_member,"
                + "harga_member,id_supplier,satuan,stok,expire,satuan_beli)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;
 
        String [] pecahKat,pecahSup;
        pecahKat = cmbKategori.getSelectedItem().toString().trim().split("-");
        pecahSup = cmbSuplier.getSelectedItem().toString().trim().split("-");
        String texpire="";
   
        
        if (expire.getDate()!=null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            texpire = simpleDateFormat.format(expire.getDate());
        }
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, txtKode.getText());
                statement.setString(2, txtNamaBrg.getText());
                statement.setString(3, pecahKat[0]);
                statement.setString(4, txtHargaKemasan.getText());
                statement.setString(5, txtIsiKemasan.getText());
                statement.setString(6, txtHargaSatuan.getText());
                statement.setString(7, txtUntung.getText());
                statement.setString(8, txtHJual.getText());
                statement.setString(9, txtUntungGrosir.getText());
                statement.setString(10, txtGrosir.getText());
                statement.setString(11, txtUntungMember.getText());
                statement.setString(12, txtMember.getText());
                statement.setString(13, pecahSup[0]);
                statement.setString(14, cmbSatuanJual.getSelectedItem().toString().trim());
                statement.setString(15, txtStok.getText());
                statement.setString(16, texpire);
                statement.setString(17, cmbSatuanBeli.getSelectedItem().toString().trim());
                
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }finally{
                clearField();
                enableForm(false);
                tampilBarang();
             }
    }
    
    private void editKartuStok(){
        String insert = "INSERT INTO tb_kartustok(id,kode_brg,tanggal,asal,jumlah,akhir,ket,user) " +
                        " VALUES(null,?,CURRENT_TIMESTAMP(),?,?,?,'EDIT MASTER BARANG',?);";
        PreparedStatement statement = null;
           int jumlah = Integer.parseInt(txtStok.getText())-stockLama;
             try{
                statement = con.prepareStatement(insert);
                statement.setString(1, txtKode.getText());
                statement.setString(2, String.valueOf(stockLama));
                statement.setString(3, String.valueOf(jumlah));
                statement.setString(4, txtStok.getText());
                statement.setString(5, UserSession.getUser());
                
                statement.executeUpdate();
                
             }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal disimpan: "+e.getMessage());
             }
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
            
            jTableBarang.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            TableColumnAdjuster tca = new TableColumnAdjuster(jTableBarang);
            tca.adjustColumns();
            
            jTableBarang.getColumnModel().getColumn(1).setMinWidth(300);
            
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
    
    
    private void updateBarang(){
        String update="UPDATE tb_barang set nama_brg=?, id_kat=?, harga_beli=?, isi_kemasan=?,"
                + "harga_satuan=?, margin=?, harga_jual=?, margin_grosir=?, harga_grosir=?, margin_member=?, harga_member=?,"
                + "id_supplier=?, satuan=?, stok=?, expire=?, satuan_beli=?, kode_brg=? WHERE kode_brg=?";
        PreparedStatement statement = null;
        String [] pecahKat,pecahSup;
        pecahKat = cmbKategori.getSelectedItem().toString().trim().split("-");
        pecahSup = cmbSuplier.getSelectedItem().toString().trim().split("-");
        String texpire="";
        
        if (expire.getDate()!=null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            texpire = simpleDateFormat.format(expire.getDate());
        }
             try{
                statement = con.prepareStatement(update);
                
                statement.setString(1, txtNamaBrg.getText());
                statement.setString(2, pecahKat[0]);
                statement.setString(3, txtHargaKemasan.getText());
                statement.setString(4, txtIsiKemasan.getText());
                statement.setString(5, txtHargaSatuan.getText());
                statement.setString(6, txtUntung.getText());
                statement.setString(7, txtHJual.getText());
                statement.setString(8, txtUntungGrosir.getText());
                statement.setString(9, txtGrosir.getText());
                statement.setString(10, txtUntungMember.getText());
                statement.setString(11, txtMember.getText());
                statement.setString(12, pecahSup[0]);
                statement.setString(13, cmbSatuanJual.getSelectedItem().toString().trim());
                statement.setString(14, txtStok.getText());
                statement.setString(15, texpire);
                statement.setString(16, cmbSatuanBeli.getSelectedItem().toString().trim());
                statement.setString(17, txtKode.getText());
                 statement.setString(18, lblKode.getText());
                 
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil diubah");
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
            }finally{
                enableForm(false);
                clearField();
                tampilBarang();
            }
    }
    
    private void hapusBarang(){
        String delete = "DELETE FROM tb_barang WHERE kode_brg=?";
        
        PreparedStatement statement = null;
        try{
            statement = con.prepareStatement(delete);
            statement.setString(1, txtKode.getText());
            statement.executeUpdate();
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Data gagal dihapus: "+ex.getMessage());
        }finally{
            tampilBarang();
        }
    }
    
    private void hargaPerBiji(){
        int hb=0,hbb=0;
        int jumlahObat=0,biji=0;
        
          
            if(txtHargaKemasan.getText().isEmpty()){
                hb=0;
            }else{
        
                hb=Integer.parseInt(txtHargaKemasan.getText());
          
            } 
            
            biji = Integer.parseInt(txtIsiKemasan.getText());
            
            jumlahObat = biji;
            hbb=hb/jumlahObat;
            txtHargaSatuan.setText(String.valueOf(hbb));
    }
    
    private void getPresentaseUntung(){
        double untung=0;
            int jual=0,bb=0,jumuntung=0,iuntung=0;
            bb=Integer.parseInt(txtHargaSatuan.getText());
            if(txtHJual.getText().isEmpty()){
                untung=0;
            }else{
                jual = Integer.parseInt(txtHJual.getText());
                jumuntung = jual - bb;
                untung =  ( ((double) jumuntung / (double) bb)*100.0);
                iuntung = (int) untung;
            }
            
            txtUntung.setText(String.valueOf(iuntung));
    }
    
    private void getPresentaseGrosir(){
        double untung=0;
            int jual=0,bb=0,jumuntung=0,iuntung=0;
            bb=Integer.parseInt(txtHargaSatuan.getText());
            if(txtGrosir.getText().isEmpty()){
                untung=0;
            }else{
                jual = Integer.parseInt(txtGrosir.getText());
                jumuntung = jual - bb;
                untung =  ( ((double) jumuntung / (double) bb)*100.0);
                iuntung = (int) untung;
            }
            
            txtUntungGrosir.setText(String.valueOf(iuntung));
    }
    
    private void getPresentaseMember(){
        double untung=0;
            int jual=0,bb=0,jumuntung=0,iuntung=0;
            bb=Integer.parseInt(txtHargaSatuan.getText());
            if(txtMember.getText().isEmpty()){
                untung=0;
            }else{
                jual = Integer.parseInt(txtMember.getText());
                jumuntung = jual - bb;
                untung =  ( ((double) jumuntung / (double) bb)*100.0);
                iuntung = (int) untung;
            }
            
            txtUntungMember.setText(String.valueOf(iuntung));
    }
    
    private void getHargaJual(){
        float untung=0;
            int jual=0;
            int bb=0;
            bb=Integer.parseInt(txtHargaSatuan.getText());
            if(txtUntung.getText().isEmpty()){
                untung=0;
                jual=0;
            }else{
                untung = (float) ((Integer.parseInt(txtUntung.getText())/100.0)*bb); 
                jual = (int) (bb+untung);
            }
            
            txtHJual.setText(String.valueOf(jual));
    }
    
    private void getHargaGrosir(){
        float untung=0;
            int jual=0;
            int bb=0;
            bb=Integer.parseInt(txtHJual.getText());
            if(txtUntungGrosir.getText().isEmpty()){
                untung=0;
                jual=0;
            }else{
                untung = (float) ((Integer.parseInt(txtUntungGrosir.getText())/100.0)*bb); 
                jual = (int) (bb-untung);
            }
            
            txtGrosir.setText(String.valueOf(jual));
    }
    
    private void getHargaMember(){
        float untung=0;
            int jual=0;
            int bb=0;
            bb=Integer.parseInt(txtHJual.getText());
            if(txtUntungMember.getText().isEmpty()){
                untung=0;
                jual=0;
            }else{
                untung = (float) ((Integer.parseInt(txtUntungMember.getText())/100.0)*bb); 
                jual = (int) (bb-untung);
            }
            
            txtMember.setText(String.valueOf(jual));
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cmbSatuanJual = new javax.swing.JComboBox();
        txtNamaBrg = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtStok = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtUntung = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtIsiKemasan = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtUntungGrosir = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtUntungMember = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        cmbSuplier = new javax.swing.JComboBox<>();
        expire = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cmbKategori = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtGrosir = new javax.swing.JTextField();
        txtHargaKemasan = new javax.swing.JTextField();
        txtHargaSatuan = new javax.swing.JTextField();
        txtHJual = new javax.swing.JTextField();
        txtMember = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cmbSatuanBeli = new javax.swing.JComboBox();
        lblKode = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnBaru = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
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
        jLabel1.setText("Master Barang");

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Form Input Barang"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Kategori");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel4.setText("Nama Barang");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel7.setText("Kode Barang");

        txtKode.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtKode.setNextFocusableComponent(cmbKategori);
        txtKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel8.setText("Satuan Beli");

        cmbSatuanJual.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbSatuanJual.setNextFocusableComponent(txtHargaKemasan);

        txtNamaBrg.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtNamaBrg.setNextFocusableComponent(cmbSatuanBeli);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel10.setText("Suplier");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel12.setText("Stok");

        txtStok.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtStok.setNextFocusableComponent(expire);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel14.setText("Umum");

        txtUntung.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtUntung.setText("30");
        txtUntung.setNextFocusableComponent(txtHJual);
        txtUntung.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUntungKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUntungKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel6.setText("Harga Kemasan");

        txtIsiKemasan.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtIsiKemasan.setText("1");
        txtIsiKemasan.setNextFocusableComponent(txtHargaSatuan);
        txtIsiKemasan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIsiKemasanKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIsiKemasanKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel15.setText("Isi Kemasan");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel16.setText("Harga Satuan");

        jLabel13.setText("%");

        txtUntungGrosir.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtUntungGrosir.setText("20");
        txtUntungGrosir.setNextFocusableComponent(txtGrosir);
        txtUntungGrosir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUntungGrosirKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUntungGrosirKeyTyped(evt);
            }
        });

        jLabel17.setText("%");

        txtUntungMember.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtUntungMember.setText("5");
        txtUntungMember.setNextFocusableComponent(txtMember);
        txtUntungMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUntungMemberKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUntungMemberKeyTyped(evt);
            }
        });

        jLabel18.setText("%");

        cmbSuplier.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbSuplier.setNextFocusableComponent(txtStok);

        expire.setDateFormatString("dd/MM/yyyy");
        expire.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Expire");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel19.setText("Grosir");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel20.setText("Member");

        cmbKategori.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbKategori.setNextFocusableComponent(txtNamaBrg);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 204));
        jLabel5.setText("Harga Beli");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 204));
        jLabel9.setText("Harga Jual");

        txtGrosir.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtGrosir.setNextFocusableComponent(txtUntungMember);
        txtGrosir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGrosirKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGrosirKeyTyped(evt);
            }
        });

        txtHargaKemasan.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHargaKemasan.setNextFocusableComponent(txtIsiKemasan);
        txtHargaKemasan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHargaKemasanKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaKemasanKeyTyped(evt);
            }
        });

        txtHargaSatuan.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHargaSatuan.setNextFocusableComponent(txtUntung);
        txtHargaSatuan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHargaSatuanKeyTyped(evt);
            }
        });

        txtHJual.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHJual.setNextFocusableComponent(txtUntungGrosir);
        txtHJual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHJualKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtHJualKeyTyped(evt);
            }
        });

        txtMember.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtMember.setNextFocusableComponent(cmbSuplier);
        txtMember.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMemberKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtMemberKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel11.setText("Satuan Jual");

        cmbSatuanBeli.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbSatuanBeli.setNextFocusableComponent(cmbSatuanJual);

        lblKode.setText("jLabel21");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel11)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(cmbSatuanJual, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblKode))))
                    .addComponent(cmbSatuanBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(88, 88, 88)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIsiKemasan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                            .addComponent(txtHargaKemasan)
                            .addComponent(txtHargaSatuan)))
                    .addComponent(jLabel5))
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtUntungMember, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMember, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtUntungGrosir, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGrosir))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtUntung, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHJual)))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbSuplier, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(expire, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtStok, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))))
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtUntung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(txtHJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtIsiKemasan, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(txtHargaSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(txtHargaKemasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(cmbSuplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUntungGrosir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel19)
                                    .addComponent(txtGrosir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(expire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtUntungMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel18)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel20)
                                        .addComponent(txtMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(lblKode))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNamaBrg, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cmbSatuanBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbSatuanJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))))
                .addContainerGap())
        );

        btnBaru.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnBaru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/new16.png"))); // NOI18N
        btnBaru.setText("Baru");
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });

        btnSimpan.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/save16.png"))); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/flat_seo216.png"))); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/Delete.png"))); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnBatal.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/cancel16.png"))); // NOI18N
        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBaru)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBatal)
                .addContainerGap(672, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBaru)
                    .addComponent(btnSimpan)
                    .addComponent(btnEdit)
                    .addComponent(btnHapus)
                    .addComponent(btnBatal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        enableForm(true);
        enableButtonB(true);
        btnBaru.setEnabled(false);
        txtKode.requestFocus();
    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpanBarang();
        btnBaru.setEnabled(true);
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if(stockLama!=Integer.parseInt(txtStok.getText())){
            editKartuStok();
        }
        updateBarang();
        clearField();
        enableForm(false);
        enableButtonA(false);
        btnBaru.setEnabled(true); 
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        int pilihan = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan menghapus data?", "Pilihan", JOptionPane.YES_NO_OPTION);
                if(pilihan == 0){
                    hapusBarang();
                    clearField();
                    tampilBarang();
                   enableForm(false);
                    enableButtonA(false);
                    enableButtonB(false);
                    btnBaru.setEnabled(true);  
                }else{
                    System.out.println("no option");
                }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
       enableForm(false);
       enableButtonA(false);
       enableButtonB(false);
       btnBaru.setEnabled(true);
       clearField();
       tampilBarang();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void jTableBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBarangMouseClicked
        // TODO add your handling code here:
       
        int row = jTableBarang.getSelectedRow();
        String kode=jTableBarang.getValueAt(row, 0).toString();
        tampilDataBarangKode(kode);
        //txtKode.setEnabled(false);
        
        
        
    }//GEN-LAST:event_jTableBarangMouseClicked

    private void txtKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:\
        //str = evt.getKeyChar(evt.getKeyCode());
        //jLabel15.setText(str);
    }//GEN-LAST:event_formKeyPressed

    private void txtUntungKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUntungKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtUntungKeyTyped

    private void txtUntungKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUntungKeyReleased

        getHargaJual();
        getHargaGrosir();
        getHargaGrosir();
    }//GEN-LAST:event_txtUntungKeyReleased

    private void jTableBarangMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBarangMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jTableBarangMouseEntered

    private void txtIsiKemasanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIsiKemasanKeyReleased
        // TODO add your handling code here:
        hargaPerBiji();
        getHargaJual();
        getHargaGrosir();
        getHargaMember();
    }//GEN-LAST:event_txtIsiKemasanKeyReleased

    private void txtIsiKemasanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIsiKemasanKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtIsiKemasanKeyTyped

    private void txtUntungGrosirKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUntungGrosirKeyReleased
        // TODO add your handling code here:
        getHargaGrosir();
    }//GEN-LAST:event_txtUntungGrosirKeyReleased

    private void txtUntungGrosirKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUntungGrosirKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtUntungGrosirKeyTyped

    private void txtUntungMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUntungMemberKeyReleased
        // TODO add your handling code here:
        getHargaMember();
    }//GEN-LAST:event_txtUntungMemberKeyReleased

    private void txtUntungMemberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUntungMemberKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtUntungMemberKeyTyped

    private void txtHargaKemasanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaKemasanKeyReleased
        // TODO add your handling code here:
        hargaPerBiji();
        getHargaJual();
        getHargaGrosir();
        getHargaMember();
        
    }//GEN-LAST:event_txtHargaKemasanKeyReleased

    private void txtHJualKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHJualKeyReleased
        // TODO add your handling code here:
        getPresentaseUntung();
        getHargaGrosir();
        getHargaMember();
    }//GEN-LAST:event_txtHJualKeyReleased

    private void txtGrosirKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrosirKeyReleased
        // TODO add your handling code here:
        getPresentaseGrosir();
    }//GEN-LAST:event_txtGrosirKeyReleased

    private void txtMemberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMemberKeyReleased
        // TODO add your handling code here:
        getPresentaseMember();
    }//GEN-LAST:event_txtMemberKeyReleased

    private void txtHargaKemasanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaKemasanKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtHargaKemasanKeyTyped

    private void txtHargaSatuanKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHargaSatuanKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtHargaSatuanKeyTyped

    private void txtHJualKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHJualKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtHJualKeyTyped

    private void txtGrosirKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGrosirKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtGrosirKeyTyped

    private void txtMemberKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMemberKeyTyped
        // TODO add your handling code here:
        if(Character.isAlphabetic(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtMemberKeyTyped

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
            java.util.logging.Logger.getLogger(FormBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormBarang dialog = new FormBarang(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSimpan;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JComboBox cmbSatuanBeli;
    private javax.swing.JComboBox cmbSatuanJual;
    private javax.swing.JComboBox<String> cmbSuplier;
    private com.toedter.calendar.JDateChooser expire;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableBarang;
    private javax.swing.JLabel lblKode;
    private javax.swing.JTextField txtGrosir;
    private javax.swing.JTextField txtHJual;
    private javax.swing.JTextField txtHargaKemasan;
    private javax.swing.JTextField txtHargaSatuan;
    private javax.swing.JTextField txtIsiKemasan;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtMember;
    private javax.swing.JTextField txtNamaBrg;
    private javax.swing.JTextField txtStok;
    private javax.swing.JTextField txtUntung;
    private javax.swing.JTextField txtUntungGrosir;
    private javax.swing.JTextField txtUntungMember;
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
