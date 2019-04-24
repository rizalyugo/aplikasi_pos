/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
public class RincianPenj2TableModel extends AbstractTableModel {

    private List<RincianPenj> list;
    
    public RincianPenj2TableModel(){
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
                return list.get(rowIndex).getId();
            case 1:
                return list.get(rowIndex).getKodeBrg();
            case 2:
                return list.get(rowIndex).getNamaBrg();
            case 3:
                return list.get(rowIndex).getHargajual();
            case 4:
                return list.get(rowIndex).getJml();
            case 5:
                return list.get(rowIndex).getTotal();
            case 6:
                return list.get(rowIndex).getFakturRetur();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID";
            case 1:
                return "Kode";
            case 2:
                return "Nama Brg";
            case 3:
                return "Harga Satuan";
            case 4:
                return "Jml";
            case 5:
                return "Total";
            case 6:
                return "Faktur";
            default:
                return null;
        }
    }  
    
    public void add(RincianPenj di) {
        list.add(di);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
}
