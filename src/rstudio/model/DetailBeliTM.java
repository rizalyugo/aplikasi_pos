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
public class DetailBeliTM extends AbstractTableModel{

    private List<DetailBeli> list;
    
    public DetailBeliTM(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getKodeBrg();
            case 1:
                return list.get(rowIndex).getNamaBrg();
            case 2:
                return list.get(rowIndex).getSatuan();
            case 3:
                return list.get(rowIndex).getJumlah();
            case 4:
                return list.get(rowIndex).getHargabeli();
            case 5:
                return list.get(rowIndex).getHargatotal();
            case 6:
                return list.get(rowIndex).getIdBeli();
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
                return "Nama Barang";
            case 2:
                return "Satuan";
            case 3:
                return "Jumlah Beli";
            case 4:
                return "Harga Beli";
            case 5:
                return "Sub Total";
            case 6:
                return "Id Beli";    
            default:
                return null;
        }
    }  
    
    public void add(DetailBeli db) {
        list.add(db);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    
}
