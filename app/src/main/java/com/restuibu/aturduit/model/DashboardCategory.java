package com.restuibu.aturduit.model;

public class DashboardCategory {
    private String kategori;
    private String avg_harga;
    private String min_harga;
    private String max_harga;
    private String sum_harga;

    public DashboardCategory(String kategori, String avg_harga, String min_harga, String max_harga, String sum_harga) {
        this.kategori = kategori;
        this.avg_harga = avg_harga;
        this.min_harga = min_harga;
        this.max_harga = max_harga;
        this.sum_harga = sum_harga;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setAvg_harga(String avg_harga) {
        this.avg_harga = avg_harga;
    }

    public void setMin_harga(String min_harga) {
        this.min_harga = min_harga;
    }

    public void setMax_harga(String max_harga) {
        this.max_harga = max_harga;
    }

    public void setSum_harga(String sum_harga) {
        this.sum_harga = sum_harga;
    }

    public String getKategori() {
        return kategori;
    }

    public String getAvg_harga() {
        return avg_harga;
    }

    public String getMin_harga() {
        return min_harga;
    }

    public String getMax_harga() {
        return max_harga;
    }

    public String getSum_harga() {
        return sum_harga;
    }
}
