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
public class ItemStruk {
    private String namaBrg, jml, harga;

    public ItemStruk(String namaBrg, String jml, String harga) {
        this.namaBrg = namaBrg;
        this.jml = jml;
        this.harga = harga;
    }

    public String getNamaBrg() {
        return namaBrg;
    }

    public void setNamaBrg(String namaBrg) {
        this.namaBrg = namaBrg;
    }

    public String getJml() {
        return jml;
    }

    public void setJml(String jml) {
        this.jml = jml;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }


    
}
