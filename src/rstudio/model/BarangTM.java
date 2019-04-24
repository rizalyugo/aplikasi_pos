/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DAFI
 */
public class BarangTM extends AbstractTableModel{

    private List<Barang> list;
    
    public BarangTM(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getKodeBarang();
            case 1:
                return list.get(rowIndex).getNamaBarang();
            case 2:
                return list.get(rowIndex).getKategori();
            case 3:
                return list.get(rowIndex).getSatuanBeli();
            case 4:
                return list.get(rowIndex).getSatuan();    
            case 5:
                return list.get(rowIndex).getHarga();
            case 6:
                return list.get(rowIndex).getHargaGrosir();
            case 7:
                return list.get(rowIndex).getHargaMember();
            case 8:
                return list.get(rowIndex).getStok();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Kode Brg";
            case 1:
                return "Nama Brg";
            case 2:
                return "Kategori";
            case 3:
                return "Satuan Beli";
            case 4:
                return "Satuan Jual";    
            case 5:
                return "Harga Umum";
            case 6:
                return "Harga Grosir";
            case 7:
                return "Harga Member";
            case 8:
                return "Stok";
            default:
                return null;
        }
    }  
    
    public void add(Barang b) {
        list.add(b);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    
    
}
