/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.model;

/**
 *
 * @author PALUPI
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DAFI
 */
public class StrukModel {
    private String noFaktur,namaToko,alamatToko,total,bayar,kembali,nama;
    private List<ItemStruk> listItemFaktur = new ArrayList<ItemStruk>();

    public StrukModel(String noFaktur, String namaToko, String alamatToko, String total, String bayar, String kembali, String nama) {
        this.noFaktur = noFaktur;
        this.namaToko = namaToko;
        this.alamatToko = alamatToko;
        this.total = total;
        this.bayar = bayar;
        this.kembali = kembali;
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }
 
    public String getNoFaktur() {
        return noFaktur;
    }

    public String getTotal() {
        return total;
    }

    public String getBayar() {
        return bayar;
    }

    public String getKembali() {
        return kembali;
    }
 
    
 

    public String getNamaToko() {
        return namaToko;
    }

    public String getAlamatToko() {
        return alamatToko;
    }
 
    
 
    public List<ItemStruk> getListItemFaktur() {
        return listItemFaktur;
    }
 
    public void tambahItemFaktur(ItemStruk itemFaktur) {
        this.listItemFaktur.add(itemFaktur);
    }
}

