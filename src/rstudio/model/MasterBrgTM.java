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
public class MasterBrgTM extends AbstractTableModel{

    private List<MasterBrg> list;
    
    public MasterBrgTM(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 11;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getKodeBrg();
            case 1:
                return list.get(rowIndex).getNamaBrg();
            case 2:
                return list.get(rowIndex).getKategori();
            case 3:
                return list.get(rowIndex).getSatuan();
            case 4:
                return list.get(rowIndex).getHbeli();
            case 5:
                return list.get(rowIndex).getHumum();
            case 6:
                return list.get(rowIndex).getHgrosir();
            case 7:
                return list.get(rowIndex).getHmember();
            case 8:
                return list.get(rowIndex).getSuplier();
            case 9:
                return list.get(rowIndex).getStok();
            case 10:
                return list.get(rowIndex).getExpire();    
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Kode";
            case 1:
                return "Nama Barang";
            case 2:
                return "Kategori";
            case 3:
                return "Satuan Jual";
            case 4:
                return "Hrg Beli";
            case 5:
                return "Hrg Umum";
            case 6:
                return "Hrg Grosir";
            case 7:
                return "Hrg Member";
            case 8:
                return "Suplier";
            case 9:
                return "Stok";
            case 10:
                return "Expire";
            default:
                return null;
        }
    }  
    
    public void add(MasterBrg b) {
        list.add(b);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    
}
