package com.mily.stacja;

import java.util.Arrays;

public class Settings {
    private int Cars;
    private int M;
    private int K;
    private int N;
    private int timeRefill;
    private int timeFueling;
    private int timePaying;
    private int maxPaliwa;
    private String[] rodzajePaliw;
    private int timeAnim;

    private int[] pozostalePaliwo;
    private int[] pozostalePaliwoForDisplay;

    public Settings() {
        loadDefault();
    }

    public void loadDefault(){
        setCars(0);
        setM(2);
        setK(2);
        setN(6);
        setTimeRefill(10000);
        setTimeFueling(100);
        setTimePaying(2000);
        setMaxPaliwa(300);
        setRodzajePaliw(new String[]{"Pb", "ON", "LPG"});
        setTimeAnim(300);
    }

    public int getCars() {
        return Cars;
    }

    public void setCars(int cars) {
        Cars = cars;
    }

    public boolean koniecPaliwa(){
        for(int i=0; i<rodzajePaliw.length; i++){
            if(getPozostalePaliwo(i)<60) return true;
        }
        return false;
    }

    public int getPozostalePaliwo(int i){
        return pozostalePaliwo[i];
    }

    public void setPozostalePaliwo(int i, int pozostalePaliwo){
        this.pozostalePaliwo[i]=pozostalePaliwo;
    }

    public int getPozostalePaliwoForDisplay(int i){
        return pozostalePaliwoForDisplay[i];
    }

    public void setPozostalePaliwoForDisplay(int i, int pozostalePaliwo){
        this.pozostalePaliwoForDisplay[i]=pozostalePaliwo;
    }

    public String getPaliwo(int i){
        return rodzajePaliw[i];
    }

    public int[] getPozostalePaliwo() {
        return pozostalePaliwo;
    }

    public void setPozostalePaliwo(int[] pozostalePaliwo) {
        this.pozostalePaliwo = pozostalePaliwo;
    }

    public int getTimeAnim() {
        return timeAnim;
    }

    public void setTimeAnim(int timeAnim) {
        this.timeAnim = timeAnim;
    }

    public String[] getRodzajePaliw() {
        return rodzajePaliw;
    }

    public void setRodzajePaliw(String[] rodzajePaliw) {
        this.rodzajePaliw = rodzajePaliw;

        this.pozostalePaliwo=new int[rodzajePaliw.length];
        Arrays.fill(this.pozostalePaliwo, this.maxPaliwa);

        this.pozostalePaliwoForDisplay=new int[rodzajePaliw.length];
        Arrays.fill(this.pozostalePaliwoForDisplay, this.maxPaliwa);
    }

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public int getTimeRefill() {
        return timeRefill;
    }

    public void setTimeRefill(int timeRefill) {
        this.timeRefill = timeRefill;
    }

    public int getTimeFueling() {
        return timeFueling;
    }

    public void setTimeFueling(int timeFueling) {
        this.timeFueling = timeFueling;
    }

    public int getTimePaying() {
        return timePaying;
    }

    public void setTimePaying(int timePaying) {
        this.timePaying = timePaying;
    }

    public int getMaxPaliwa() {
        return maxPaliwa;
    }

    public void setMaxPaliwa(int maxPaliwa) {
        this.maxPaliwa = maxPaliwa;
    }
}
