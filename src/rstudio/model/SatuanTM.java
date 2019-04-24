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
public class SatuanTM extends AbstractTableModel {

    private List<Satuan> list;
    
    public SatuanTM(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getNama();

            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Satuan";
            default:
                return null;
        }
    }  
    
    public void add(Satuan sat) {
        list.add(sat);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
}
