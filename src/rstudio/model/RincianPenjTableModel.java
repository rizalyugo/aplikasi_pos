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
public class RincianPenjTableModel extends AbstractTableModel {

    private List<RincianPenj> list;
    
    public RincianPenjTableModel(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 11;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getId();
            case 1:
                return list.get(rowIndex).getNo();
            case 2:
                return list.get(rowIndex).getKodeBrg();
            case 3:
                return list.get(rowIndex).getNamaBrg();
            case 4:
                return list.get(rowIndex).getHargaawal();
            case 5:
                return list.get(rowIndex).getDiskon();
            case 6:
                return list.get(rowIndex).getPotongan();
            case 7:
                return list.get(rowIndex).getHargajual();
            case 8:
                return list.get(rowIndex).getJml();
            case 9:
                return list.get(rowIndex).getSatuan();
            case 10:
                return list.get(rowIndex).getTotal();
             
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
                return "No";
            case 2:
                return "Kode";
            case 3:
                return "Nama Brg";
            case 4:
                return "Harga Awal";
            case 5:
                return "Diskon";
            case 6:
                return "Potongan";
            case 7:
                return "Harga Akhir";
            case 8:
                return "Jml";
            case 9:
                return "Satuan";
            case 10:
                return "Total";
            default:
                return null;
        }
    }  
    
    public void add(RincianPenj di) {
        list.add(di);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
}
