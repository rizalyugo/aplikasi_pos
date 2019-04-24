/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.main;

import com.alee.laf.WebLookAndFeel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import rstudio.config.Koneksi;
import rstudio.model.UserSession;
import rstudio.viewmaster.FormBarang;
import rstudio.view.FormUsaha;
import rstudio.view.FormUser;
import rstudio.view.SettingPrinter;
import rstudio.viewlaporan.LaporanKartuStok;
import rstudio.viewlaporan.LapDataBarang;
import rstudio.viewlaporan.LapHistoriLogin;
import rstudio.viewlaporan.LapLabaRugi;
import rstudio.viewlaporan.LapPendapatan;
import rstudio.viewlaporan.LapPengeluaran;
import rstudio.viewlaporan.LaporanPengadaan;
import rstudio.viewlaporan.LaporanPenjualan;
import rstudio.viewmaster.FormBank;
import rstudio.viewmaster.FormKategori;
import rstudio.viewmaster.FormMember;
import rstudio.viewmaster.FormSatuan;
import rstudio.viewmaster.FormSupplier;
import rstudio.viewpilih.PilihMember;
import rstudio.viewpilih.StockOpname;
import rstudio.viewtransaksi.FormModal;
import rstudio.viewtransaksi.FormPengadaan;
import rstudio.viewtransaksi.FormPengeluaran;
import rstudio.viewtransaksi.FormPenjualan;
import rstudio.viewtransaksi.HapusTransaksi;
import rstudio.viewtransaksi.HistoriTransaksi;
import rstudio.viewtransaksi.FormPiutang;

/**
 *
 * @author DAFI
 */
public class MenuUtama extends javax.swing.JFrame {

    Connection con;
    /**
     * Creates new form MenuUtama
     */
    public MenuUtama() {
        initComponents();
        
        con = Koneksi.getKoneksi();
        
        this.setTitle(namaApp("1"));
        
        lblUsername.setText(UserSession.getUser());
        lblNama.setText(UserSession.getNama());
        lblJabatan.setText(UserSession.getLevel());
        
        if(UserSession.getLevel().equals("PETUGAS")){
            mnDataBrg.setEnabled(false);
            mnKategori.setEnabled(false);
            mnSatuan.setEnabled(false);
            mnMember.setEnabled(true);
            mnSuplier.setEnabled(false);
            mnBank.setEnabled(false);
            
            mnPengadaan.setEnabled(true);
            mnPenjualan.setEnabled(true);
            mnPengeluaran.setEnabled(true);
           // mnKas.setEnabled(true);
            mnStockOpname.setEnabled(false);
            mnHapus.setEnabled(false);
            
            mnLapBarang.setEnabled(false);
            mnLapLabaRugi.setEnabled(false);
            mnLapPengadaan.setEnabled(false);
            mnLapPenjualan.setEnabled(true);
            mnKartuStok.setEnabled(false);
            mnRekapPendapatan.setEnabled(false);

            mnUser.setEnabled(false);
            mnUsaha.setEnabled(false);
            mnHistoriTrans.setEnabled(false);
            mnHistoriLogin.setEnabled(false);
            mnHistoriPengeluaran.setEnabled(false);
        }else if(UserSession.getLevel().equals("ADMIN")){
            mnDataBrg.setEnabled(true);
            mnKategori.setEnabled(true);
            mnSatuan.setEnabled(true);
            mnMember.setEnabled(true);
            mnSuplier.setEnabled(true);
            mnBank.setEnabled(true);
            
            
            mnPengadaan.setEnabled(true);
            mnPenjualan.setEnabled(true);
            mnPengeluaran.setEnabled(true);
          //  mnKas.setEnabled(true);
            mnStockOpname.setEnabled(true);
            mnHapus.setEnabled(true);
            
            mnLapBarang.setEnabled(true);
            mnLapLabaRugi.setEnabled(true);
            mnLapPengadaan.setEnabled(true);
            mnLapPenjualan.setEnabled(true);
            mnKartuStok.setEnabled(true);
            mnRekapPendapatan.setEnabled(true);

            mnUser.setEnabled(true);
            mnUsaha.setEnabled(true);
            mnHistoriTrans.setEnabled(true);
            mnHistoriLogin.setEnabled(true);
            mnHistoriPengeluaran.setEnabled(true);
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

    private void dataBarang(){
        FormBarang fp = new FormBarang(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void kategori(){
        FormKategori fp = new FormKategori(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void satuan(){
        FormSatuan fp = new FormSatuan(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void suplier(){
        FormSupplier fp = new FormSupplier(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    
    private void formPenjualan(){
        FormPenjualan fp = new FormPenjualan(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
   
 
    
    private void formUsaha(){
        FormUsaha fp = new FormUsaha(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    
    private void kartuStok(){
        LaporanKartuStok fp = new LaporanKartuStok(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    

    
    private void settingUser(){
        FormUser fp = new FormUser(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }

    
    private void FormPengadaan(){
        FormPengadaan fp = new FormPengadaan(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void formMember(){
        FormMember fp = new FormMember(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void riwayatMember(){
        PilihMember fp = new PilihMember(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void piutang(){
        FormPiutang fp = new FormPiutang(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void historiTransaksi(){
        HistoriTransaksi fp = new HistoriTransaksi(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void lapBarang(){
        LapDataBarang fp = new LapDataBarang(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void lapPengeluaran(){
        LapPengeluaran fp = new LapPengeluaran(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void stockOpname(){
        StockOpname fp = new StockOpname(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void lapPenjualan(){
        LaporanPenjualan fp = new LaporanPenjualan(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void formPengeluaran(){
        FormPengeluaran fp = new FormPengeluaran(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void formModal(){
        FormModal fp = new FormModal(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void lapPendapatan(){
        LapPendapatan fp = new LapPendapatan(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void lapLabaRugi(){
        LapLabaRugi fp = new LapLabaRugi(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void dataBank(){
        FormBank fp = new FormBank(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void hapusTransaksi(){
        HapusTransaksi fp = new HapusTransaksi(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void historiLogin(){
        LapHistoriLogin fp = new LapHistoriLogin(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void settingPrinter(){
        SettingPrinter fp = new SettingPrinter(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    private void lapPengadaan(){
        LaporanPengadaan fp = new LaporanPengadaan(this, true);
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void login(){
        Login fp = new Login();
        
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - fp.getWidth()) / 2;
        final int y = (screenSize.height - fp.getHeight()) / 2;
        fp.setLocation(x, y);
        fp.setVisible(true);
    }
    
    private void updateHistori(){
        String update="UPDATE tb_historilogin set logout=CURRENT_TIMESTAMP WHERE noindex=?";
        PreparedStatement statement = null;
        
            try{
                statement = con.prepareStatement(update);
                statement.setString(1, UserSession.getIdHistori());

                
                statement.executeUpdate();
            }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Data gagal diubah: "+e.getMessage());
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

        panelBackgroundImage11 = new rstudio.modul.PanelBackgroundImage1();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblJabatan = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnDataBrg = new javax.swing.JMenuItem();
        mnKategori = new javax.swing.JMenuItem();
        mnSatuan = new javax.swing.JMenuItem();
        mnSuplier = new javax.swing.JMenuItem();
        mnMember = new javax.swing.JMenuItem();
        mnBank = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnPengadaan = new javax.swing.JMenuItem();
        mnHistoriTrans = new javax.swing.JMenuItem();
        mnPenjualan = new javax.swing.JMenuItem();
        mnStockOpname = new javax.swing.JMenuItem();
        mnPengeluaran = new javax.swing.JMenuItem();
        mnHapus = new javax.swing.JMenuItem();
        mnPenjualan1 = new javax.swing.JMenuItem();
        mnPenjualan2 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        mnLapBarang = new javax.swing.JMenuItem();
        mnLapPengadaan = new javax.swing.JMenuItem();
        mnKartuStok = new javax.swing.JMenuItem();
        mnLapPenjualan = new javax.swing.JMenuItem();
        mnRekapPendapatan = new javax.swing.JMenuItem();
        mnLapLabaRugi = new javax.swing.JMenuItem();
        mnHistoriLogin = new javax.swing.JMenuItem();
        mnHistoriPengeluaran = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        mnUsaha = new javax.swing.JMenuItem();
        mnUser = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Username : ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Nama :");

        lblNama.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblNama.setText("................");

        lblUsername.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblUsername.setText("................");

        lblJabatan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblJabatan.setText("................");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Jabatan :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("aplikasii toko versi 1.3.0 (28/05/2017)");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(5, 5, 5)
                .addComponent(lblUsername)
                .addGap(52, 52, 52)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNama)
                .addGap(85, 85, 85)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblJabatan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblJabatan)
                .addComponent(jLabel3)
                .addComponent(jLabel4))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lblUsername)
                .addComponent(lblNama)
                .addComponent(jLabel2))
        );

        jToolBar1.add(jPanel1);

        javax.swing.GroupLayout panelBackgroundImage11Layout = new javax.swing.GroupLayout(panelBackgroundImage11);
        panelBackgroundImage11.setLayout(panelBackgroundImage11Layout);
        panelBackgroundImage11Layout.setHorizontalGroup(
            panelBackgroundImage11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelBackgroundImage11Layout.setVerticalGroup(
            panelBackgroundImage11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBackgroundImage11Layout.createSequentialGroup()
                .addContainerGap(289, Short.MAX_VALUE)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(panelBackgroundImage11, java.awt.BorderLayout.CENTER);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/master.png"))); // NOI18N
        jMenu1.setText("Master");

        mnDataBrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/box.png"))); // NOI18N
        mnDataBrg.setText("Data Barang");
        mnDataBrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnDataBrgActionPerformed(evt);
            }
        });
        jMenu1.add(mnDataBrg);

        mnKategori.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/box.png"))); // NOI18N
        mnKategori.setText("Kategori Barang");
        mnKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnKategoriActionPerformed(evt);
            }
        });
        jMenu1.add(mnKategori);

        mnSatuan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/box.png"))); // NOI18N
        mnSatuan.setText("Satuan Barang");
        mnSatuan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSatuanActionPerformed(evt);
            }
        });
        jMenu1.add(mnSatuan);

        mnSuplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/box.png"))); // NOI18N
        mnSuplier.setText("Data Suplier");
        mnSuplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnSuplierActionPerformed(evt);
            }
        });
        jMenu1.add(mnSuplier);

        mnMember.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/box.png"))); // NOI18N
        mnMember.setText("Data Member");
        mnMember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnMemberActionPerformed(evt);
            }
        });
        jMenu1.add(mnMember);

        mnBank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/box.png"))); // NOI18N
        mnBank.setText("Data Bank");
        mnBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBankActionPerformed(evt);
            }
        });
        jMenu1.add(mnBank);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/transaksi.png"))); // NOI18N
        jMenu2.setText("Transaksi");

        mnPengadaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/folder_download.png"))); // NOI18N
        mnPengadaan.setText("Pengadaan Barang");
        mnPengadaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPengadaanActionPerformed(evt);
            }
        });
        jMenu2.add(mnPengadaan);

        mnHistoriTrans.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/folder_download.png"))); // NOI18N
        mnHistoriTrans.setText("Histori Transaksi");
        mnHistoriTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnHistoriTransActionPerformed(evt);
            }
        });
        jMenu2.add(mnHistoriTrans);

        mnPenjualan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/shopping_cart.png"))); // NOI18N
        mnPenjualan.setText("Penjualan");
        mnPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPenjualanActionPerformed(evt);
            }
        });
        jMenu2.add(mnPenjualan);

        mnStockOpname.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/shopping_cart.png"))); // NOI18N
        mnStockOpname.setText("Stock Opname");
        mnStockOpname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnStockOpnameActionPerformed(evt);
            }
        });
        jMenu2.add(mnStockOpname);

        mnPengeluaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/shopping_cart.png"))); // NOI18N
        mnPengeluaran.setText("Pengeluaran Operasional");
        mnPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPengeluaranActionPerformed(evt);
            }
        });
        jMenu2.add(mnPengeluaran);

        mnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/shopping_cart.png"))); // NOI18N
        mnHapus.setText("Hapus Transaksi");
        mnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnHapusActionPerformed(evt);
            }
        });
        jMenu2.add(mnHapus);

        mnPenjualan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/shopping_cart.png"))); // NOI18N
        mnPenjualan1.setText("Riwayat Pembelian Member");
        mnPenjualan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPenjualan1ActionPerformed(evt);
            }
        });
        jMenu2.add(mnPenjualan1);

        mnPenjualan2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/shopping_cart.png"))); // NOI18N
        mnPenjualan2.setText("Piutang");
        mnPenjualan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnPenjualan2ActionPerformed(evt);
            }
        });
        jMenu2.add(mnPenjualan2);

        jMenuBar1.add(jMenu2);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/calendar.png"))); // NOI18N
        jMenu5.setText("Laporan");

        mnLapBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/blog.png"))); // NOI18N
        mnLapBarang.setText("Laporan Data Barang");
        mnLapBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLapBarangActionPerformed(evt);
            }
        });
        jMenu5.add(mnLapBarang);

        mnLapPengadaan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/blog.png"))); // NOI18N
        mnLapPengadaan.setText("Laporan Pengadaan");
        mnLapPengadaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLapPengadaanActionPerformed(evt);
            }
        });
        jMenu5.add(mnLapPengadaan);

        mnKartuStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/credit_cards.png"))); // NOI18N
        mnKartuStok.setText("Laporan Kartu Stok");
        mnKartuStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnKartuStokActionPerformed(evt);
            }
        });
        jMenu5.add(mnKartuStok);

        mnLapPenjualan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/blog.png"))); // NOI18N
        mnLapPenjualan.setText("Laporan Penjualan");
        mnLapPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLapPenjualanActionPerformed(evt);
            }
        });
        jMenu5.add(mnLapPenjualan);

        mnRekapPendapatan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/blog.png"))); // NOI18N
        mnRekapPendapatan.setText("Rekap Pendapatan");
        mnRekapPendapatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRekapPendapatanActionPerformed(evt);
            }
        });
        jMenu5.add(mnRekapPendapatan);

        mnLapLabaRugi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/blog.png"))); // NOI18N
        mnLapLabaRugi.setText("Laporan Laba Rugi");
        mnLapLabaRugi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnLapLabaRugiActionPerformed(evt);
            }
        });
        jMenu5.add(mnLapLabaRugi);

        mnHistoriLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/blog.png"))); // NOI18N
        mnHistoriLogin.setText("Histori Login");
        mnHistoriLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnHistoriLoginActionPerformed(evt);
            }
        });
        jMenu5.add(mnHistoriLogin);

        mnHistoriPengeluaran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/blog.png"))); // NOI18N
        mnHistoriPengeluaran.setText("Laporan Pengeluaran");
        mnHistoriPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnHistoriPengeluaranActionPerformed(evt);
            }
        });
        jMenu5.add(mnHistoriPengeluaran);

        jMenuBar1.add(jMenu5);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/tools.png"))); // NOI18N
        jMenu4.setText("Setting");

        mnUsaha.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/info_blue.png"))); // NOI18N
        mnUsaha.setText("Setting Nama Usaha");
        mnUsaha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnUsahaActionPerformed(evt);
            }
        });
        jMenu4.add(mnUsaha);

        mnUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonMenu/user_group.png"))); // NOI18N
        mnUser.setText("Setting User");
        mnUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnUserActionPerformed(evt);
            }
        });
        jMenu4.add(mnUser);

        jMenuBar1.add(jMenu4);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonIcon/exit.png"))); // NOI18N
        jMenu3.setText("Keluar");
        jMenu3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu3MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnDataBrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnDataBrgActionPerformed
        // TODO add your handling code here:
        dataBarang();
    }//GEN-LAST:event_mnDataBrgActionPerformed

    private void mnPengadaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPengadaanActionPerformed
        // TODO add your handling code here:
        FormPengadaan();
    }//GEN-LAST:event_mnPengadaanActionPerformed

    private void jMenu3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu3MouseClicked
        // TODO add your handling code here:
        try {
            getToolkit().beep();
            String[] options = new String[]{"Ya, Keluar Aplikasi", "Log off","Batal"};
            int response = JOptionPane.showOptionDialog(null, "Apakah anda yakin ingin keluar dari aplikasi ?", "Pesan Konfirmasi", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            System.out.println("Response: " + response);
            switch (response) {
                case 0:
                    updateHistori();
                    System.exit(0);
                    JOptionPane.showMessageDialog(null, "Terimakasih sudah memakai sistem kami", "Pesan", JOptionPane.PLAIN_MESSAGE);
                    
                case 1:
                    updateHistori();
                    dispose();
                    login();
                default:
                    break;
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jMenu3MouseClicked

    private void mnUsahaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnUsahaActionPerformed
        // TODO add your handling code here:
        formUsaha();
    }//GEN-LAST:event_mnUsahaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
         try {
            getToolkit().beep();
            String[] options = new String[]{"Ya, Keluar Aplikasi", "Log off","Batal"};
            int response = JOptionPane.showOptionDialog(null, "Apakah anda yakin ingin keluar dari aplikasi ?", "Pesan Konfirmasi", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            System.out.println("Response: " + response);
            switch (response) {
                case 0:
                    updateHistori();
                    System.exit(0);
                    JOptionPane.showMessageDialog(null, "Terimakasih sudah memakai sistem kami", "Pesan", JOptionPane.PLAIN_MESSAGE);
                    
                case 1:
                    updateHistori();
                    dispose();
                    login();
                default:
                    break;
            }
        } catch (Exception e) {
        }
        
    }//GEN-LAST:event_formWindowClosing

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseClicked

    private void mnKartuStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnKartuStokActionPerformed
        // TODO add your handling code here:
        kartuStok();
    }//GEN-LAST:event_mnKartuStokActionPerformed

    private void mnLapPengadaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnLapPengadaanActionPerformed
        // TODO add your handling code here:
        lapPengadaan();
    }//GEN-LAST:event_mnLapPengadaanActionPerformed

    private void mnUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnUserActionPerformed
        // TODO add your handling code here:'
        settingUser();
    }//GEN-LAST:event_mnUserActionPerformed

    private void mnKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnKategoriActionPerformed
        // TODO add your handling code here:
        kategori();
    }//GEN-LAST:event_mnKategoriActionPerformed

    private void mnSatuanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSatuanActionPerformed
        // TODO add your handling code here:
        satuan();
    }//GEN-LAST:event_mnSatuanActionPerformed

    private void mnSuplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnSuplierActionPerformed
        // TODO add your handling code here:
        suplier();
    }//GEN-LAST:event_mnSuplierActionPerformed

    private void mnPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPenjualanActionPerformed
        // TODO add your handling code here:
        formPenjualan();
    }//GEN-LAST:event_mnPenjualanActionPerformed

    private void mnMemberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnMemberActionPerformed
        // TODO add your handling code here:
        formMember();
    }//GEN-LAST:event_mnMemberActionPerformed

    private void mnLapBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnLapBarangActionPerformed
        // TODO add your handling code here:
        lapBarang();
    }//GEN-LAST:event_mnLapBarangActionPerformed

    private void mnStockOpnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnStockOpnameActionPerformed
        // TODO add your handling code here:
        stockOpname();
    }//GEN-LAST:event_mnStockOpnameActionPerformed

    private void mnLapPenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnLapPenjualanActionPerformed
        // TODO add your handling code here:
        lapPenjualan();
    }//GEN-LAST:event_mnLapPenjualanActionPerformed

    private void mnPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPengeluaranActionPerformed
        // TODO add your handling code here:
        formPengeluaran();
    }//GEN-LAST:event_mnPengeluaranActionPerformed

    private void mnRekapPendapatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRekapPendapatanActionPerformed
        // TODO add your handling code here:
        lapPendapatan();
    }//GEN-LAST:event_mnRekapPendapatanActionPerformed

    private void mnLapLabaRugiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnLapLabaRugiActionPerformed
        // TODO add your handling code here:
        lapLabaRugi();
    }//GEN-LAST:event_mnLapLabaRugiActionPerformed

    private void mnBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnBankActionPerformed
        // TODO add your handling code here:
        dataBank();
    }//GEN-LAST:event_mnBankActionPerformed

    private void mnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnHapusActionPerformed
        // TODO add your handling code here:
        hapusTransaksi();
    }//GEN-LAST:event_mnHapusActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_formWindowClosed

    private void mnHistoriLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnHistoriLoginActionPerformed
        // TODO add your handling code here:
        historiLogin();
    }//GEN-LAST:event_mnHistoriLoginActionPerformed

    private void mnPenjualan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPenjualan1ActionPerformed
        // TODO add your handling code here:
        riwayatMember();
    }//GEN-LAST:event_mnPenjualan1ActionPerformed

    private void mnHistoriTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnHistoriTransActionPerformed
        // TODO add your handling code here:
        historiTransaksi();
    }//GEN-LAST:event_mnHistoriTransActionPerformed

    private void mnHistoriPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnHistoriPengeluaranActionPerformed
        // TODO add your handling code here:
        lapPengeluaran();
    }//GEN-LAST:event_mnHistoriPengeluaranActionPerformed

    private void mnPenjualan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnPenjualan2ActionPerformed
        // TODO add your handling code here:
        piutang();
    }//GEN-LAST:event_mnPenjualan2ActionPerformed

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
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuUtama().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblJabatan;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JMenuItem mnBank;
    private javax.swing.JMenuItem mnDataBrg;
    private javax.swing.JMenuItem mnHapus;
    private javax.swing.JMenuItem mnHistoriLogin;
    private javax.swing.JMenuItem mnHistoriPengeluaran;
    private javax.swing.JMenuItem mnHistoriTrans;
    private javax.swing.JMenuItem mnKartuStok;
    private javax.swing.JMenuItem mnKategori;
    private javax.swing.JMenuItem mnLapBarang;
    private javax.swing.JMenuItem mnLapLabaRugi;
    private javax.swing.JMenuItem mnLapPengadaan;
    private javax.swing.JMenuItem mnLapPenjualan;
    private javax.swing.JMenuItem mnMember;
    private javax.swing.JMenuItem mnPengadaan;
    private javax.swing.JMenuItem mnPengeluaran;
    private javax.swing.JMenuItem mnPenjualan;
    private javax.swing.JMenuItem mnPenjualan1;
    private javax.swing.JMenuItem mnPenjualan2;
    private javax.swing.JMenuItem mnRekapPendapatan;
    private javax.swing.JMenuItem mnSatuan;
    private javax.swing.JMenuItem mnStockOpname;
    private javax.swing.JMenuItem mnSuplier;
    private javax.swing.JMenuItem mnUsaha;
    private javax.swing.JMenuItem mnUser;
    private rstudio.modul.PanelBackgroundImage1 panelBackgroundImage11;
    // End of variables declaration//GEN-END:variables
}
