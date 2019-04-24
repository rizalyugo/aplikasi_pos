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
public class UserTableModel extends AbstractTableModel {

    private List<User> list;
    
    public UserTableModel(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getId();
            case 1:
                return list.get(rowIndex).getNama();
            case 2:
                return list.get(rowIndex).getUsername();
            case 3:
                return list.get(rowIndex).getPassword();
            case 4:
                return list.get(rowIndex).getJabatan();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "ID User";
            case 1:
                return "Nama User";
            case 2:
                return "Username";
            case 3:
                return "Password";
            case 4:
                return "Jabatan";
            default:
                return null;
        }
    }  
    
    public void add(User di) {
        list.add(di);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
}
