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
public class SupplierTM extends AbstractTableModel {

    private List<Supplier> list;
    
    public SupplierTM(){
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
                return list.get(rowIndex).getTelepon();
            case 3:
                return list.get(rowIndex).getAlamat();    
   
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Kode Supplier";
            case 1:
                return "Nama Supplier";
            case 2:
                return "Telepon";
            case 3:
                return "Alamat";
            default:
                return null;
        }
    }  
    
    public void add(Supplier sup) {
        list.add(sup);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
}
