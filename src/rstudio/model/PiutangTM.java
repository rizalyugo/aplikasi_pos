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
 * @author PALUPI
 */
public class PiutangTM extends AbstractTableModel{

    private List<Piutang> list;
    
    public PiutangTM(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getNo();
            case 1:
                return list.get(rowIndex).getMember();    
            case 2:
                return list.get(rowIndex).getNoFaktur();
            case 3:
                return list.get(rowIndex).getTgl();
            case 4:
                return list.get(rowIndex).getPetugas(); 
            case 5:
                return list.get(rowIndex).getTempo();   
            case 6:
                return list.get(rowIndex).getTotal(); 
            case 7:
                return list.get(rowIndex).getTotalBayar();    
            case 8:
                return list.get(rowIndex).getSisaHutang();  
                
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "No";
            case 1:
                return "Nama Member";
            case 2:
                return "No Faktur";
            case 3:
                return "Tanggal";
            case 4:
                return "Petugas";
            case 5:
                return "Jatuh Tempo";
            case 6:
                return "Total";  
            case 7:
                return "Total Bayar";
            case 8:
                return "Sisa Hutang";        
            default:
                return null;
        }
    }  
    
    public void add(Piutang b) {
        list.add(b);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
}
