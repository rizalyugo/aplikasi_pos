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
public class SupplierTM2 extends AbstractTableModel {

    private List<Supplier> list;
    
    public SupplierTM2(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getId();
            case 1:
                return list.get(rowIndex).getNama();  
   
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
            default:
                return null;
        }
    }  
    
    public void add(Supplier sup) {
        list.add(sup);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
}
