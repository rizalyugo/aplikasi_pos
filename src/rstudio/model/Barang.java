/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rstudio.model;

/**
 *
 * @author DAFI
 */
public class Barang {
    private String kodeBarang,namaBarang,kategori,satuan,harga,hargaMember,hargaGrosir,stok,satuanBeli;

    public String getSatuanBeli() {
        return satuanBeli;
    }

    public void setSatuanBeli(String satuanBeli) {
        this.satuanBeli = satuanBeli;
    }

    
    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getHargaMember() {
        return hargaMember;
    }

    public void setHargaMember(String hargaMember) {
        this.hargaMember = hargaMember;
    }

    public String getHargaGrosir() {
        return hargaGrosir;
    }

    public void setHargaGrosir(String hargaGrosir) {
        this.hargaGrosir = hargaGrosir;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
    
}
