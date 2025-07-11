/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application.models;

/**
 *
 * @author mhdja
 */
public class RangkingModel {

   
    private String namaAlternatif;
    private double totalNilai;
    private int peringkat;
    private String batch;
    private String kursus;
    
     /**
     * @return the batch
     */
    public String getBatch() {
        return batch;
    }

    /**
     * @param batch the batch to set
     */
    public void setBatch(String batch) {
        this.batch = batch;
    }

    /**
     * @return the kursus
     */
    public String getKursus() {
        return kursus;
    }

    /**
     * @param kursus the kursus to set
     */
    public void setKursus(String kursus) {
        this.kursus = kursus;
    }

    public String getNamaAlternatif() {
        return namaAlternatif;
    }

    public void setNamaAlternatif(String namaAlternatif) {
        this.namaAlternatif = namaAlternatif;
    }

    public double getTotalNilai() {
        return totalNilai;
    }

    public void setTotalNilai(double totalNilai) {
        this.totalNilai = totalNilai;
    }

    public int getPeringkat() {
        return peringkat;
    }

    public void setPeringkat(int peringkat) {
        this.peringkat = peringkat;
    }
}