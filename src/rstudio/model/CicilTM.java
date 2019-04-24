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
public class CicilTM extends AbstractTableModel{

    private List<Cicil> list;
    
    public CicilTM(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getNoFak();
            case 1:
                return list.get(rowIndex).getTgl();
            case 2:
                return list.get(rowIndex).getTotal();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "No Fak";
            case 1:
                return "Tanggal";
            case 2:
                return "Jml Cicil";
            default:
                return null;
        }
    }  
    
    public void add(Cicil db) {
        list.add(db);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    
}
