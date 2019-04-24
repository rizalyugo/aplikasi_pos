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
public class SalesTableModel extends AbstractTableModel {

    private List<Sales> list;
    
    public SalesTableModel(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getId();
            case 1:
                return list.get(rowIndex).getNama();
            case 2:
                return list.get(rowIndex).getAlamat();
            case 3:
                return list.get(rowIndex).getNohp();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID Sales";
            case 1:
                return "Nama Sales";
            case 2:
                return "Alamat";
            case 3:
                return "No. HP/Telp";

            default:
                return null;
        }
    }  
    
    public void add(Sales di) {
        list.add(di);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
}
