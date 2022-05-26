package com.mily.stacja;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class MainAlg extends Thread {
    static int bokKwadratu = 60;
    Rectangle rec;
    Text text;
    Text text1;
    static ArrayList<Rectangle> samochodyList;
    static ArrayList<Rectangle> samochodyKolejkaList;
    static ArrayList<Text> textSamochodyKolejkaList;
    static ArrayList<Text> textSamochodyList;
    static ArrayList<Circle> kierowcaList;
    static ArrayList<Text> textKierowcaList;
    static Pane pane;

    static ArrayList<Text> textPaliwoSamochodyList;
    static ArrayList<Text> textPaliwoSamochodyKolejkaList;

    ArrayList<Integer> kolejka;
    Semaphore chron;

    Semaphore stacja;

    int idPaliwa;
    int litry;

    static ArrayList<Samochod> samochods;

    public MainAlg(Pane pane) {
        samochodyKolejkaList = new ArrayList<>();
        samochodyList = new ArrayList<>();
        textSamochodyKolejkaList = new ArrayList<>();
        textSamochodyList = new ArrayList<>();
        kierowcaList=new ArrayList<>();
        textKierowcaList=new ArrayList<>();
        MainAlg.pane =pane;

        kolejka = new ArrayList<>();

        textPaliwoSamochodyList=new ArrayList<>();
        textPaliwoSamochodyKolejkaList=new ArrayList<>();

        chron = new Semaphore(1);

        samochods=new ArrayList<>();
    }

    synchronized private void renderCzek(int id, int idPaliwa, int litry){
        //renderowanie samochodow
        try {
            chron.acquire();
        } catch (InterruptedException e) {
            return;
        }
        Platform.runLater(()->{
            rec=new Rectangle(MainController.samochodyContenerList.get(samochodyKolejkaList.size()).getX()+5, MainController.samochodyContenerList.get(samochodyList.size()).getY()+5, bokKwadratu, bokKwadratu);
            rec.setFill(Paint.valueOf("#C5C5C5"));
            rec.setStrokeType(StrokeType.INSIDE);
            rec.setStrokeWidth(1);
            rec.setStroke(Color.BLACK);
            rec.setAccessibleText(String.valueOf(id));
            samochodyKolejkaList.add(rec);
            samochodyList.add(rec);

            text=new Text(String.valueOf(id));
            text.setX(rec.getX()+2);
            text.setY(rec.getY()+text.getBoundsInLocal().getHeight()-3);
            text.setFill(Color.BLACK);

            text1=new Text(SettingsController.settings.getRodzajePaliw()[idPaliwa]+"x"+litry+"L");
            text1.setX(rec.getX()+3);
            text1.setY(rec.getY()+bokKwadratu-3);
            text1.setFill(Color.BLACK);

            textSamochodyKolejkaList.add(text);
            textSamochodyList.add(text);
            textPaliwoSamochodyKolejkaList.add(text1);
            textPaliwoSamochodyList.add(text1);

            pane.getChildren().add(rec);
            pane.getChildren().add(text);
            pane.getChildren().add(text1);
            chron.release();
        });
    }

    public void endAlg(){
        while (stacja.availablePermits()!=SettingsController.settings.getN() && !MainController.mainAlg.isInterrupted());
        Platform.runLater(()->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Koniec Symulacji");
            alert.setHeaderText(null);
            alert.setContentText("Symulacja została zakończona");

            alert.show();
            final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
            runnable.run();

            MainController.btnStartS.setDisable(false);
            MainController.btnStopS.setDisable(true);
            MainController.btnSettingS.setDisable(false);
            MainController.flagConfig=false;
        });
    }

    @Override
    public void run(){
        startAlg();
        endAlg();
    }

    private void startAlg() {
        Semaphore stanowiska = new Semaphore(SettingsController.settings.getM());
        Semaphore kasy = new Semaphore(SettingsController.settings.getK());
        stacja = new Semaphore(SettingsController.settings.getN());

        boolean[] wolneStanowisko=new boolean[SettingsController.settings.getM()];
        boolean[] wolnaKasa=new boolean[SettingsController.settings.getK()];
        for (int j=0; j<SettingsController.settings.getM(); j++){
            wolneStanowisko[j]=true;
        }
        for (int j=0; j<SettingsController.settings.getK(); j++){
            wolnaKasa[j]=true;
        }

        Samochod samochod;
        int i=0;
        boolean loop=true;
        while (loop){
            try {
                if(SettingsController.settings.koniecPaliwa()){
                    while (stacja.availablePermits()!=SettingsController.settings.getN());
                    Thread.sleep(SettingsController.settings.getTimeRefill());
                    for(int j=0; j<SettingsController.settings.getRodzajePaliw().length; j++){
                        SettingsController.settings.setPozostalePaliwo(j, SettingsController.settings.getMaxPaliwa());
                        SettingsController.settings.setPozostalePaliwoForDisplay(j, SettingsController.settings.getMaxPaliwa());
                        MainController.textPozostalePaliwoList[j].setText(String.valueOf(SettingsController.settings.getPozostalePaliwoForDisplay(j)));
                    }
                }
                stacja.acquire();

                Random random = new Random();
                idPaliwa=random.nextInt(SettingsController.settings.getRodzajePaliw().length);
                litry = random.nextInt(60-10+1)+10;

                if(SettingsController.settings.koniecPaliwa()){
                    stacja.release();
                    i++;
                    continue;
                }
                chron.acquire();
                kolejka.add(i);
                chron.release();

                renderCzek(i, idPaliwa, litry);
                samochod = new Samochod(i, stanowiska, kasy, stacja, chron, kolejka, wolneStanowisko, pane, wolnaKasa,
                        idPaliwa, litry);
                samochods.add(samochod);
                samochod.start();


                if(SettingsController.settings.getCars()>0 && i==SettingsController.settings.getCars()-1) loop=false;
                i++;
            } catch (InterruptedException e) {
                for(Iterator<Samochod> samochodIterator = samochods.iterator(); samochodIterator.hasNext();){
                    Samochod next = samochodIterator.next();
                    next.interrupt();
                }
                /*for (Samochod x : samochods){
                    x.interrupt();
                }*/
                this.interrupt();
                return;
            }
        }
    }
}
