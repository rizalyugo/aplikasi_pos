/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.viewlaporan;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author DAFI
 */
public class LapDataBarang extends javax.swing.JDialog {

    Connection con;
    private String dTglAwal,dTglAkhir;
    /**
     * Creates new form Sales
     */
    public LapDataBarang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
                
        con = Koneksi.getKoneksi();
        addEscapeListener(this);
        
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
    
    private void cetakLaporan(){
        try {
            InputStream input=Report.class.getResourceAsStream("DataBarang.jasper");
                   
            HashMap param = new HashMap();
            String idkat="";
            String [] pecahIdkat = cmbKategori.getSelectedItem().toString().split("-");
            if(cmbKategori.getSelectedIndex() == 0){
                idkat="";
            }else{
                idkat = pecahIdkat[0].trim();
            }
            
            
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat dmy = new SimpleDateFormat("dd/MMM/yyyy");
//            String dTglAwal = simpleDateFormat.format(tglAwal.getDate());
//            String dTglAkhir = simpleDateFormat.format(tglAkhir.getDate());
//            String dAwal = dmy.format(tglAwal.getDate());
//            String dAkhir = dmy.format(tglAkhir.getDate());
            
//            param.put("tglAwal", dTglAwal);
//            param.put("tglAkhir", dTglAkhir);
//            param.put("subjudul", "Dari: "+dAwal + " s/d " + dAkhir);
            param.put("instansi", tampilSetting("1"));
            param.put("alamat", "Alamat: "+tampilSetting("2"));
            param.put("telp", "Telepon: "+tampilSetting("3"));
            param.put("idkategori", "%"+idkat+"%");
            
            JasperPrint jasperPrint=JasperFillManager.fillReport(input, param, con);
            
            JasperViewer jv=new JasperViewer(jasperPrint, false);

            JDialog dialog = new JDialog(this);//the owner
            dialog.setContentPane(jv.getContentPane());
            dialog.setSize(jv.getSize());
            dialog.setTitle("Cetak Laporan Data Barang");
//            final Toolkit toolkit = Toolkit.getDefaultToolkit();
//            final Dimension screenSize = toolkit.getScreenSize();
//            final int x = (screenSize.width - dialog.getWidth()) / 2;
//            final int y = (screenSize.height - dialog.getHeight()) / 2;
//            dialog.setLocation(x, y);
//            dialog.setVisible(true);
                dialog.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()); 
                dialog.setVisible(true);
            
            
        } catch (JRException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void cetakInventaris(){
        try {
            InputStream input=Report.class.getResourceAsStream("Inventaris.jasper");
                   
            HashMap param = new HashMap();
            String idkat="";
            String [] pecahIdkat = cmbKategori.getSelectedItem().toString().split("-");
            if(cmbKategori.getSelectedIndex() == 0){
                idkat="";
            }else{
                idkat = pecahIdkat[0].trim();
            }
            
            
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat dmy = new SimpleDateFormat("dd/MMM/yyyy");
//            String dTglAwal = simpleDateFormat.format(tglAwal.getDate());
//            String dTglAkhir = simpleDateFormat.format(tglAkhir.getDate());
//            String dAwal = dmy.format(tglAwal.getDate());
//            String dAkhir = dmy.format(tglAkhir.getDate());
            
//            param.put("tglAwal", dTglAwal);
//            param.put("tglAkhir", dTglAkhir);
//            param.put("subjudul", "Dari: "+dAwal + " s/d " + dAkhir);
            param.put("instansi", tampilSetting("1"));
            param.put("alamat", "Alamat: "+tampilSetting("2"));
            param.put("telp", "Telepon: "+tampilSetting("3"));
            param.put("idkategori", "%"+idkat+"%");
            
            JasperPrint jasperPrint=JasperFillManager.fillReport(input, param, con);
            
            JasperViewer jv=new JasperViewer(jasperPrint, false);

            JDialog dialog = new JDialog(this);//the owner
            dialog.setContentPane(jv.getContentPane());
            dialog.setSize(jv.getSize());
            dialog.setTitle("Cetak Laporan Data Barang");
//            final Toolkit toolkit = Toolkit.getDefaultToolkit();
//            final Dimension screenSize = toolkit.getScreenSize();
//            final int x = (screenSize.width - dialog.getWidth()) / 2;
//            final int y = (screenSize.height - dialog.getHeight()) / 2;
//            dialog.setLocation(x, y);
//            dialog.setVisible(true);
                dialog.setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds()); 
                dialog.setVisible(true);
            
            
        } catch (JRException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
   private void exportToExcel(){
        PreparedStatement statement = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dmy = new SimpleDateFormat("dd/MMM/yyyy");
            SimpleDateFormat dmmy = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat My = new SimpleDateFormat("MMyy");


            
            Workbook wb = new HSSFWorkbook();
            Sheet personSheet = wb.createSheet("Data Barang");
            HSSFCellStyle style=(HSSFCellStyle) wb.createCellStyle();
            HSSFCellStyle headerStyle=(HSSFCellStyle) wb.createCellStyle();
            
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            
            headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
                    
            Row headerRow = personSheet.createRow(0);
            Cell nameHeaderCell = headerRow.createCell(0);
            Cell addressHeaderCell = headerRow.createCell(1);
            
            String idkat="";
            String [] pecahIdkat = cmbKategori.getSelectedItem().toString().split("-");
            if(cmbKategori.getSelectedIndex() == 0){
                idkat="";
            }else{
                idkat = pecahIdkat[0].trim();
            }
            
            String sql = "SELECT b.kode_brg,b.nama_brg,b.id_kat, b.harga_satuan, b.harga_jual, b.satuan, "
                    + " b.stok, k.kategori, b.harga_satuan*b.stok as aset, b.harga_jual*b.stok as omset FROM tb_barang b "
                    + " JOIN tb_kategori k ON b.id_kat=k.id_kat WHERE b.id_kat LIKE ?";

            statement =  con.prepareStatement(sql);
            statement.setString(1, "%"+idkat+"%");
//            statement.setString(2, dTglAkhir);
            ResultSet resultSet = statement.executeQuery();
            
            int rowJudul=0;
            Row dataRowJudul = personSheet.createRow(rowJudul);
            Cell JudulCell = dataRowJudul.createCell(0);
            JudulCell.setCellValue("LAPORAN DATA BARANG");
            personSheet.addMergedRegion(new CellRangeAddress(rowJudul,rowJudul,0,9));
            JudulCell.setCellStyle(headerStyle);
            
            int rowJudulB=1;
            Row dataRowJudulB = personSheet.createRow(rowJudulB);
            Cell JudulCellB = dataRowJudulB.createCell(0);
            personSheet.addMergedRegion(new CellRangeAddress(rowJudulB,rowJudulB,0,9));
            JudulCellB.setCellStyle(headerStyle);

            JudulCellB.setCellValue(tampilSetting("1"));
            int rowJudulC=2;
            Row dataRowJudulC = personSheet.createRow(rowJudulC);
            Cell JudulCellC = dataRowJudulC.createCell(0);
            JudulCellC.setCellValue("Alamat: "+tampilSetting("2"));
            personSheet.addMergedRegion(new CellRangeAddress(rowJudulC,rowJudulC,0,9));
            JudulCellC.setCellStyle(headerStyle);
            
            int rowJudulE=3;
            Row dataRowJudulE= personSheet.createRow(rowJudulE);
            Cell JudulCellE = dataRowJudulE.createCell(0);
            JudulCellE.setCellValue("Telepon: "+tampilSetting("3"));
            personSheet.addMergedRegion(new CellRangeAddress(rowJudulE,rowJudulE,0,9));
            JudulCellE.setCellStyle(headerStyle);
            
            int rowJudulD=5;
            Row dataRowJudulD = personSheet.createRow(rowJudulD);
            Cell JudulCellD = dataRowJudulD.createCell(0);
            Cell JudulCellD2 = dataRowJudulD.createCell(1);
            Cell JudulCellD3 = dataRowJudulD.createCell(2);
            Cell JudulCellD4 = dataRowJudulD.createCell(3);
            Cell JudulCellD5 = dataRowJudulD.createCell(4);
            Cell JudulCellD6 = dataRowJudulD.createCell(5);
            Cell JudulCellD7 = dataRowJudulD.createCell(6);
            Cell JudulCellD8 = dataRowJudulD.createCell(7);
            Cell JudulCellD9 = dataRowJudulD.createCell(8);
            Cell JudulCellD10 = dataRowJudulD.createCell(9);
            
            JudulCellD.setCellValue("No");
            JudulCellD2.setCellValue("Kode Barang");
            JudulCellD3.setCellValue("Nama Barang");
            JudulCellD4.setCellValue("Kategori");
            JudulCellD5.setCellValue("Harga Beli");
            JudulCellD6.setCellValue("Harga Jual");
            JudulCellD7.setCellValue("Satuan");
            JudulCellD8.setCellValue("Stok");
            JudulCellD9.setCellValue("Aset");
            JudulCellD10.setCellValue("Omset");
            
            JudulCellD.setCellStyle(style);
            JudulCellD2.setCellStyle(style);
            JudulCellD3.setCellStyle(style);
            JudulCellD4.setCellStyle(style);
            JudulCellD5.setCellStyle(style);
            JudulCellD6.setCellStyle(style);
            JudulCellD7.setCellStyle(style);
            JudulCellD8.setCellStyle(style);
            JudulCellD9.setCellStyle(style);
            JudulCellD10.setCellStyle(style);
            
            int row = 6;
            
            int no=1;
            
            
            while(resultSet.next()) {
                Row dataRow = personSheet.createRow(row);
                
                 FormulaEvaluator fev = wb.getCreationHelper().createFormulaEvaluator();
                
                
                Cell dataNameCell = dataRow.createCell(0);
                dataNameCell.setCellValue(String.valueOf(no));
                
                Cell dataAddressCell = dataRow.createCell(1);
                dataAddressCell.setCellValue(resultSet.getString("kode_brg"));
                
                Cell lkCell = dataRow.createCell(2);
                lkCell.setCellValue(resultSet.getString("nama_brg"));
                
                Cell prCell = dataRow.createCell(3);
                prCell.setCellValue(resultSet.getString("kategori"));
                
                Cell jumCell = dataRow.createCell(4);
                //jumCell.setCellValue(resultSet.getString("harga_satuan"));
                
                String hSat = resultSet.getString("harga_satuan");            
                jumCell.setCellFormula("VALUE(" + hSat + ")");
                fev.evaluateInCell(jumCell);
                
                Cell d = dataRow.createCell(5);
//                d.setCellValue(resultSet.getString("harga_jual"));
                
                String hJual = resultSet.getString("harga_jual");            
                d.setCellFormula("VALUE(" + hJual + ")");
                fev.evaluateInCell(d);
                
                Cell e = dataRow.createCell(6);
                e.setCellValue(resultSet.getString("satuan"));
                
                Cell f = dataRow.createCell(7);
               // f.setCellValue(resultSet.getString("stok"));
                
                String st = resultSet.getString("stok");            
                f.setCellFormula("VALUE(" + st + ")");
                fev.evaluateInCell(f);
                
                
                Cell g = dataRow.createCell(8);
                //g.setCellValue(resultSet.getString("aset"));
                
                String aset = resultSet.getString("aset");            
                g.setCellFormula("VALUE(" + aset + ")");
                fev.evaluateInCell(g);
                
                Cell h = dataRow.createCell(9);
               // h.setCellValue(resultSet.getString("omset"));
                
                String omset = resultSet.getString("omset");            
                h.setCellFormula("VALUE(" + omset + ")");
                fev.evaluateInCell(h);
                
                dataAddressCell.setCellStyle(style);
                dataNameCell.setCellStyle(style);       
                lkCell.setCellStyle(style);
                prCell.setCellStyle(style);
                jumCell.setCellStyle(style);
                d.setCellStyle(style);
                e.setCellStyle(style);
                f.setCellStyle(style);
                g.setCellStyle(style);
                h.setCellStyle(style);
                
                
        

                        
                row = row + 1;
                no++;
               // jumAkhir=jumAkhir+Integer.parseInt(jumlah);
                
            }
            
            personSheet.autoSizeColumn(0);
            personSheet.autoSizeColumn(1);
            personSheet.autoSizeColumn(2);
            personSheet.autoSizeColumn(3);
            personSheet.autoSizeColumn(4);
            personSheet.autoSizeColumn(5);
            personSheet.autoSizeColumn(6);
            personSheet.autoSizeColumn(7);
            personSheet.autoSizeColumn(8);
            personSheet.autoSizeColumn(9);
            
            Row dataRowJudulf = personSheet.createRow(row);
            Cell JudulCellf = dataRowJudulf.createCell(1);
            Cell JudulCellg = dataRowJudulf.createCell(2);
            //JudulCellE.setCellValue(jumAkhir);
            
            String outputDirPath = "E:/DataBarang.xls";
            FileOutputStream fileOut;
            
            fileOut = new FileOutputStream(outputDirPath);
            wb.write(fileOut);
            fileOut.close();
            
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan ke Excel","Informasi", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException ex) {
            Logger.getLogger(LapDataBarang.class.getName()).log(Level.SEVERE, null, ex);
        }catch (FileNotFoundException ex) {
            Logger.getLogger(LapDataBarang.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LapDataBarang.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel7 = new javax.swing.JLabel();
        cmbKategori = new javax.swing.JComboBox<>();
        panelTransparant4 = new rstudio.modul.PanelTransparant();
        btnCetak = new javax.swing.JButton();
        btnCetak1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Laporan Data Barang");

        panelTransparant1.setBackground(new java.awt.Color(0, 0, 0));
        panelTransparant1.setToolTipText("");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Pilih Kategori");

        javax.swing.GroupLayout panelTransparant1Layout = new javax.swing.GroupLayout(panelTransparant1);
        panelTransparant1.setLayout(panelTransparant1Layout);
        panelTransparant1Layout.setHorizontalGroup(
            panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparant1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(41, 41, 41)
                .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        panelTransparant1Layout.setVerticalGroup(
            panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparant1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(panelTransparant1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        panelTransparant4.setBackground(new java.awt.Color(0, 0, 0));

        btnCetak.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonIcon/printer32.png"))); // NOI18N
        btnCetak.setText("Data Barang");
        btnCetak.setOpaque(false);
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        btnCetak1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rstudio/image/buttonIcon/printer32.png"))); // NOI18N
        btnCetak1.setText("Inventaris Toko");
        btnCetak1.setOpaque(false);
        btnCetak1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetak1ActionPerformed(evt);
            }
        });

        jButton1.setText("Export ke Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTransparant4Layout = new javax.swing.GroupLayout(panelTransparant4);
        panelTransparant4.setLayout(panelTransparant4Layout);
        panelTransparant4Layout.setHorizontalGroup(
            panelTransparant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelTransparant4Layout.createSequentialGroup()
                .addContainerGap(151, Short.MAX_VALUE)
                .addGroup(panelTransparant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addGroup(panelTransparant4Layout.createSequentialGroup()
                        .addComponent(btnCetak)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCetak1)))
                .addGap(18, 18, 18))
        );
        panelTransparant4Layout.setVerticalGroup(
            panelTransparant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransparant4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTransparant4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCetak1)
                    .addComponent(btnCetak))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addContainerGap(85, Short.MAX_VALUE)
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
        cetakInventaris();
    }//GEN-LAST:event_btnCetak1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        exportToExcel();
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
            java.util.logging.Logger.getLogger(LapDataBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LapDataBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LapDataBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LapDataBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                LapDataBarang dialog = new LapDataBarang(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel7;
    private rstudio.modul.PanelBackgroundImage1 panelBackgroundImage12;
    private rstudio.modul.PanelTransparant panelTransparant1;
    private rstudio.modul.PanelTransparant panelTransparant4;
    // End of variables declaration//GEN-END:variables
}
