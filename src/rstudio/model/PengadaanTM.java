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
public class PengadaanTM extends AbstractTableModel{

    private List<Pengadaan> list;
    
    public PengadaanTM(){
        list = new ArrayList<>();
    }
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 12;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).getNoFaktur();
            case 1:
                return list.get(rowIndex).getTglNota();
            case 2:
                return list.get(rowIndex).getIdSup();
            case 3:
                return list.get(rowIndex).getSup();
            case 4:
                return list.get(rowIndex).getTotal();
            case 5:
                return list.get(rowIndex).getPpn();
            case 6:
                return list.get(rowIndex).getTotalAkhir();
            case 7:
                return list.get(rowIndex).getJatuhTempo();
            case 8:
                return list.get(rowIndex).getDibayar();
            case 9:
                return list.get(rowIndex).getLunas();
            case 10:
                return list.get(rowIndex).getUser();
            case 11:
                return list.get(rowIndex).getId();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "No Faktur";
            case 1:
                return "Tgl Nota";
            case 2:
                return "Kode Sup.";
            case 3:
                return "Supplier";
            case 4:
                return "Total Harga";
            case 5:
                return "PPN";
            case 6:
                return "Total Harga+PPN";
            case 7:
                return "Jatuh Tempo";
            case 8:
                return "Dibayar";
            case 9:
                return "Lunas";
            case 10:
                return "User";
            case 11:
                return "Id";
            default:
                return null;
        }
    }  
    
    public void add(Pengadaan p) {
        list.add(p);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }
    
}
