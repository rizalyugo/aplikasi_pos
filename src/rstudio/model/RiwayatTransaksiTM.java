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
public class RiwayatTransaksiTM extends AbstractTableModel{

    private List<RiwayatTransaksi> list;
    
    public RiwayatTransaksiTM(){
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
                return list.get(rowIndex).getNo();
            case 1:
                return list.get(rowIndex).getNoFaktur();
            case 2:
                return list.get(rowIndex).getTgl();
            case 3:
                return list.get(rowIndex).getPetugas(); 
            case 4:
                return list.get(rowIndex).getTotal();     
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
                return "No Faktur";
            case 2:
                return "Tanggal";
            case 3:
                return "Petugas";
            case 4:
                return "Total";    
            default:
                return null;
        }
    }  
    
    public void add(RiwayatTransaksi b) {
        list.add(b);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
}
