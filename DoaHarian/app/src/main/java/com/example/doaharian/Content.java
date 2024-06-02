package com.example.doaharian;

public class Content {

    private int id;
    private String doa;
    private String ayat;
    private String latin;
    private String artinya;

    public Content(String doa, String ayat, String latin, String artinya) {
        this.doa = doa;
        this.ayat = ayat;
        this.latin = latin;
        this.artinya = artinya;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoa() {
        return doa;
    }

    public void setDoa(String doa) {
        this.doa = doa;
    }

    public String getAyat() {
        return ayat;
    }

    public void setAyat(String ayat) {
        this.ayat = ayat;
    }

    public String getLatin() {
        return latin;
    }

    public void setLatin(String latin) {
        this.latin = latin;
    }

    public String getArtinya() {
        return artinya;
    }

    public void setArti(String atri) {
        this.artinya = artinya;
    }

}
