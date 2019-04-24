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
public class BankTM extends AbstractTableModel {

    private List<Bank> list;
    
    public BankTM(){
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
                return list.get(rowIndex).getRekening();
            case 1:
                return list.get(rowIndex).getNama();
            case 2:
                return list.get(rowIndex).getSaldo();
            case 3:
                return list.get(rowIndex).getId(); 
   
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "No Rekening";
            case 1:
                return "Nama Bank";
            case 2:
                return "Saldo";
            case 3:
                return "Id";    
            default:
                return null;
        }
    }  
    
    public void add(Bank sup) {
        list.add(sup);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
}
