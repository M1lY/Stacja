package com.mily.stacja;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class Samochod extends Thread{
    int id;
    Semaphore stanowiska;
    Semaphore kasy;
    Semaphore stacja;
    int idPaliwa;
    String paliwo;
    int litry;

    ArrayList<Integer> kolejka;

    Semaphore chron;

    boolean[] wolneStanowisko;
    boolean[] wolnaKasa;
    int stanowisko;
    int kasa;

    Pane pane;

    Circle circle;
    Text text;

    int promienKierowcy;

    public Samochod(int id, Semaphore stanowiska, Semaphore kasy, Semaphore stacja, Semaphore chron,
                    ArrayList<Integer> kolejka, boolean[] wolneStanowisko, Pane pane, boolean[] wolnaKasa, int idPaliwa,
                    int litry) {
        this.id = id;
        this.stanowiska = stanowiska;
        this.kasy = kasy;
        this.stacja = stacja;
        this.idPaliwa=idPaliwa;
        this.paliwo = SettingsController.settings.getPaliwo(idPaliwa);
        this.litry=litry;
        this.chron=chron;
        this.kolejka=kolejka;
        this.wolneStanowisko=wolneStanowisko;
        this.wolnaKasa=wolnaKasa;
        this.pane=pane;
        promienKierowcy=15;
    }

    private void animZajeciaStanowiska(int sa, int st){
        try {
            chron.acquire();
        } catch (InterruptedException e) {
            return;
        }
        int h=-1;
        for(int i=0; i<MainAlg.samochodyKolejkaList.size(); i++){
            if(Objects.equals(MainAlg.samochodyKolejkaList.get(i).getAccessibleText(), String.valueOf(id))){
                h=i;
                break;
            }
        }
        Rectangle samochodKolejka=MainAlg.samochodyKolejkaList.get(h);
        Text textSamochodKolejka=MainAlg.textSamochodyKolejkaList.get(h);
        Text textPaliwoSamochodKolejka=MainAlg.textPaliwoSamochodyKolejkaList.get(h);

        MainAlg.samochodyKolejkaList.remove(h);
        MainAlg.textSamochodyKolejkaList.remove(h);
        MainAlg.textPaliwoSamochodyKolejkaList.remove(h);
        kolejka.remove(h);

        int finalH = h;
        Platform.runLater(()->{
            TranslateTransition translate = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/3));
            translate.setToX(0);
            translate.setToY(samochodKolejka.getY()+80);

            TranslateTransition translateText = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/3));
            translateText.setToX(0);
            translateText.setToY(samochodKolejka.getY()+80);

            TranslateTransition translateTextPaliwo = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/3));
            translateTextPaliwo.setToX(0);
            translateTextPaliwo.setToY(samochodKolejka.getY()+80);

            ParallelTransition transition = new ParallelTransition(samochodKolejka,translate);
            ParallelTransition transitionText = new ParallelTransition(textSamochodKolejka,translateText);
            ParallelTransition transitionTextPaliwo = new ParallelTransition(textPaliwoSamochodKolejka,translateTextPaliwo);

            transition.play();
            transitionText.play();
            transitionTextPaliwo.play();

            transitionTextPaliwo.setOnFinished(actionEvent -> {
                moveKolejka(finalH);

                translateTextPaliwo.setToX(MainController.stanowiskaList.get(st).getX()+5-samochodKolejka.getX());
                translateTextPaliwo.setToY(samochodKolejka.getY()+80);
                ParallelTransition transitionTextPaliwo12 =new ParallelTransition(textPaliwoSamochodKolejka,translateTextPaliwo);
                transitionTextPaliwo12.play();

                transitionTextPaliwo12.setOnFinished(actionEvent1 -> {
                    translateTextPaliwo.setToX(MainController.stanowiskaList.get(st).getX()+5-samochodKolejka.getX());
                    translateTextPaliwo.setToY(MainController.stanowiskaList.get(st).getY()+5-samochodKolejka.getY());
                    ParallelTransition transitionTextPaliwo1 =new ParallelTransition(textPaliwoSamochodKolejka,translateTextPaliwo);

                    transitionTextPaliwo1.play();
                });
            });

            transitionText.setOnFinished(actionEvent -> {
                translateText.setToX(MainController.stanowiskaList.get(st).getX()+5-samochodKolejka.getX());
                translateText.setToY(samochodKolejka.getY()+80);
                ParallelTransition transitionText12 =new ParallelTransition(textSamochodKolejka,translateText);

                transitionText12.play();

                transitionText12.setOnFinished(actionEvent12 -> {
                    translateText.setToX(MainController.stanowiskaList.get(st).getX()+5-samochodKolejka.getX());
                    translateText.setToY(MainController.stanowiskaList.get(st).getY()+5-samochodKolejka.getY());
                    ParallelTransition transitionText1 =new ParallelTransition(textSamochodKolejka,translateText);

                    transitionText1.play();
                });
            });

            transition.setOnFinished(actionEvent -> {
                translate.setToX(MainController.stanowiskaList.get(st).getX()+5-samochodKolejka.getX());
                translate.setToY(samochodKolejka.getY()+80);
                ParallelTransition transition12 =new ParallelTransition(samochodKolejka,translate);
                transition12.play();

                transition12.setOnFinished(actionEvent1 -> {
                    translate.setToX(MainController.stanowiskaList.get(st).getX() + 5 - samochodKolejka.getX());
                    translate.setToY(MainController.stanowiskaList.get(st).getY() + 5 - samochodKolejka.getY());
                    ParallelTransition transition1 = new ParallelTransition(samochodKolejka, translate);
                    transition1.play();
                    transition1.setOnFinished(actionEvent2 -> {
                        renderKierowca(id, stanowisko);
                    });
                });
            });

        });
    }

    private void moveKolejka(int id){
        Platform.runLater(()->{
            for(int i=0; i<MainAlg.samochodyKolejkaList.size(); i++){
                MainAlg.samochodyKolejkaList.get(i).setX(MainController.samochodyContenerList.get(i).getX()+5);
                MainAlg.textSamochodyKolejkaList.get(i).setX(MainController.samochodyContenerList.get(i).getX()+5+3);
                MainAlg.textPaliwoSamochodyKolejkaList.get(i).setX(MainController.samochodyContenerList.get(i).getX()+5+3);
            }
            chron.release();
        });
    }

    private void renderKierowca(int id, int stanowisko){
        Platform.runLater(()->{
            circle=new Circle(MainController.stanowiskaList.get(stanowisko).getX()+5+MainController.bokKwadratu/2,MainController.stanowiskaList.get(stanowisko).getY()+5+MainController.bokKwadratu/2,promienKierowcy);
            circle.setFill(Color.BLACK);
            circle.setAccessibleText(String.valueOf(id));
            MainAlg.kierowcaList.add(circle);

            text=new Text(String.valueOf(id));
            text.setX(circle.getCenterX()-text.getBoundsInLocal().getWidth()/2-2);
            text.setY(circle.getCenterY()+text.getBoundsInLocal().getHeight()/2-3);
            text.setFont(new Font(15));
            text.setFill(Color.WHITE);
            MainAlg.textKierowcaList.add(text);

            pane.getChildren().add(circle);
            pane.getChildren().add(text);
        });
    }

    private void animZajeciaKasy(int id, int kasa){
        try {
            chron.acquire();
        } catch (InterruptedException e) {
            return;
        }
        int h=-1;
        for (int i=0; i<MainAlg.kierowcaList.size(); i++){
            if(Objects.equals(MainAlg.kierowcaList.get(i).getAccessibleText(), String.valueOf(id))){
                h=i;
                break;
            }
        }
        Circle kierowca=MainAlg.kierowcaList.get(h);
        Text textKieowca=MainAlg.textKierowcaList.get(h);

        chron.release();

        Platform.runLater(()->{
            TranslateTransition translate = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/2));
            translate.setToX(0);
            if(kasa<wolnaKasa.length/2) translate.setToY(-80);
            else translate.setToY(80);

            TranslateTransition translateText = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/2));
            translateText.setToX(0);
            if(kasa<wolnaKasa.length/2) translateText.setToY(-80);
            else translateText.setToY(80);

            ParallelTransition transition = new ParallelTransition(kierowca,translate);
            ParallelTransition transitionText = new ParallelTransition(textKieowca,translateText);

            transition.play();
            transitionText.play();

            transitionText.setOnFinished(actionEvent -> {
                translateText.setToX(MainController.kasyList.get(kasa).getX()-textKieowca.getX()+MainController.szerKasy/2-textKieowca.getBoundsInLocal().getWidth()/2);
                translateText.setToY(MainController.kasyList.get(kasa).getY()-textKieowca.getY()+MainController.wysKasy/2+textKieowca.getBoundsInLocal().getHeight()/4);
                ParallelTransition transitionText1=new ParallelTransition(textKieowca,translateText);
                transitionText1.play();
            });

            transition.setOnFinished(actionEvent -> {
                translate.setToX(MainController.kasyList.get(kasa).getX()-kierowca.getCenterX()+MainController.szerKasy/2);
                translate.setToY(MainController.kasyList.get(kasa).getY()-kierowca.getCenterY()+MainController.wysKasy/2);
                ParallelTransition transition1=new ParallelTransition(kierowca,translate);
                transition1.play();
            });
        });
    }

    private void animWyjechania(int id, int kasa, int stanowisko){
        try {
            chron.acquire();
        } catch (InterruptedException e) {
            return;
        }
        int h=-1;
        for(int i=0; i<MainAlg.kierowcaList.size(); i++){
            if(Objects.equals(MainAlg.kierowcaList.get(i).getAccessibleText(), String.valueOf(id))){
                h=i;
                break;
            }
        }
        int hSamochod=-1;
        for (int i=0; i<MainAlg.samochodyList.size(); i++){
            if(Objects.equals(MainAlg.samochodyList.get(i).getAccessibleText(), String.valueOf(id))){
                hSamochod=i;
                break;
            }
        }
        Circle kierowca = MainAlg.kierowcaList.get(h);
        Text textKierowca = MainAlg.textKierowcaList.get(h);

        Rectangle samochod = MainAlg.samochodyList.get(hSamochod);
        Text textSamochodu = MainAlg.textSamochodyList.get(hSamochod);

        Text textPaliwoSamochodu = MainAlg.textPaliwoSamochodyList.get(hSamochod);

        MainAlg.kierowcaList.remove(h);
        MainAlg.textKierowcaList.remove(h);

        MainAlg.samochodyList.remove(hSamochod);
        MainAlg.textSamochodyList.remove(hSamochod);

        MainAlg.textPaliwoSamochodyList.remove(hSamochod);

        chron.release();

        Platform.runLater(()->{
            TranslateTransition translate = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/3));
            translate.setToX(kierowca.getCenterX()-MainController.stanowiskaList.get(stanowisko).getX()-MainController.bokKwadratu/2-5);
            if(kasa<wolnaKasa.length/2) translate.setToY(MainController.stanowiskaList.get(stanowisko).getY()-kierowca.getCenterY()+MainController.bokKwadratu/2+5-80);
            else translate.setToY(MainController.stanowiskaList.get(stanowisko).getY()-kierowca.getCenterY()+MainController.bokKwadratu/2+5+80);

            TranslateTransition translateText = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/3));
            translateText.setToX(kierowca.getCenterX()-MainController.stanowiskaList.get(stanowisko).getX()-MainController.bokKwadratu/2-5);
            if(kasa<wolnaKasa.length/2) translateText.setToY(MainController.stanowiskaList.get(stanowisko).getY()-kierowca.getCenterY()+MainController.bokKwadratu/2+5-80);
            else translateText.setToY(MainController.stanowiskaList.get(stanowisko).getY()-kierowca.getCenterY()+MainController.bokKwadratu/2+5+80);

            ParallelTransition transition = new ParallelTransition(kierowca,translate);
            ParallelTransition transitionText = new ParallelTransition(textKierowca,translateText);

            transition.play();
            transitionText.play();

            transitionText.setOnFinished(actionEvent -> {
                wolnaKasa[kasa]=true;
                kasy.release();

                translateText.setToX(0);
                translateText.setToY(0);
                ParallelTransition transitionText1 = new ParallelTransition(textKierowca,translateText);
                transitionText1.play();

                transitionText1.setOnFinished(actionEvent1 -> {
                    translateText.setToX(0);
                    translateText.setToY(pane.getHeight()/2+promienKierowcy);
                    ParallelTransition transitionText2 = new ParallelTransition(textKierowca,translateText);

                    TranslateTransition translateTextSamochod = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/3));
                    translateTextSamochod.setToX(MainController.stanowiskaList.get(stanowisko).getX()-samochod.getX()+5);
                    translateTextSamochod.setToY(pane.getHeight()-samochod.getY()+MainController.bokKwadratu/2);
                    ParallelTransition transitionTextSamochod = new ParallelTransition(textSamochodu,translateTextSamochod);

                    TranslateTransition translateTextPaliwo=new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/3));
                    translateTextPaliwo.setToX(MainController.stanowiskaList.get(stanowisko).getX()-samochod.getX()+5);
                    translateTextPaliwo.setToY(pane.getHeight()-samochod.getY()+MainController.bokKwadratu/2);
                    ParallelTransition transitionTextPaliwo = new ParallelTransition(textPaliwoSamochodu,translateTextPaliwo);

                    transitionTextPaliwo.play();
                    transitionTextSamochod.play();
                    transitionText2.play();
                });
            });

            transition.setOnFinished(actionEvent -> {
                translate.setToX(0);
                translate.setToY(0);
                ParallelTransition transition1 = new ParallelTransition(kierowca,translate);
                transition1.play();

                transition1.setOnFinished(actionEvent1 -> {
                    translate.setToX(0);
                    translate.setToY(pane.getHeight()/2+promienKierowcy);
                    ParallelTransition transition2 = new ParallelTransition(kierowca, translate);

                    TranslateTransition translateSamochod = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()/3));
                    translateSamochod.setToX(MainController.stanowiskaList.get(stanowisko).getX()-samochod.getX()+5);
                    translateSamochod.setToY(pane.getHeight()-samochod.getY()+MainController.bokKwadratu/2);
                    ParallelTransition transitionSamochod = new ParallelTransition(samochod,translateSamochod);

                    transitionSamochod.play();
                    transition2.play();
                    transition2.setOnFinished(actionEvent2 -> {
                        pane.getChildren().remove(samochod);
                        pane.getChildren().remove(textSamochodu);

                        pane.getChildren().remove(kierowca);
                        pane.getChildren().remove(textKierowca);

                        pane.getChildren().remove(textPaliwoSamochodu);

                        wolneStanowisko[stanowisko]=true;
                        stanowiska.release();
                        stacja.release();
                    });
                });
            });
        });
    }

    private void animWyjechaniaZKolejki(int id, int stanowisko){
        try {
            chron.acquire();
        } catch (InterruptedException e) {
            return;
        }
        int h=-1;
        for(int i=0; i<MainAlg.samochodyKolejkaList.size(); i++){
            if(Objects.equals(MainAlg.samochodyKolejkaList.get(i).getAccessibleText(), String.valueOf(id))){
                h=i;
                break;
            }
        }
        Rectangle samochodKolejka = MainAlg.samochodyKolejkaList.get(h);
        Text textSamochodKolejka = MainAlg.textSamochodyKolejkaList.get(h);
        Text textPaliwoSamochodKolejka = MainAlg.textPaliwoSamochodyKolejkaList.get(h);

        Rectangle samochod = MainAlg.samochodyList.get(MainAlg.samochodyList.indexOf(samochodKolejka));
        Text textSamochod = MainAlg.textSamochodyList.get(MainAlg.textSamochodyList.indexOf(textSamochodKolejka));
        Text textPaliwoSamochod = MainAlg.textPaliwoSamochodyList.get(MainAlg.textPaliwoSamochodyList.indexOf(textPaliwoSamochodKolejka));

        MainAlg.samochodyKolejkaList.remove(samochodKolejka);
        MainAlg.textSamochodyKolejkaList.remove(textSamochodKolejka);
        MainAlg.textPaliwoSamochodyKolejkaList.remove(textPaliwoSamochodKolejka);

        MainAlg.samochodyList.remove(samochod);
        MainAlg.textSamochodyList.remove(textSamochod);
        MainAlg.textPaliwoSamochodyList.remove(textPaliwoSamochod);

        kolejka.remove(h);

        chron.release();

        Platform.runLater(()->{
            TranslateTransition translate = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()));
            translate.setToX(0);
            translate.setToY(pane.getHeight());

            TranslateTransition translateText = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()));
            translateText.setToX(0);
            translateText.setToY(pane.getHeight());

            TranslateTransition translateTextPaliwo = new TranslateTransition(Duration.millis((double)SettingsController.settings.getTimeAnim()));
            translateTextPaliwo.setToX(0);
            translateTextPaliwo.setToY(pane.getHeight());

            ParallelTransition transition = new ParallelTransition(samochod, translate);
            ParallelTransition transitionText = new ParallelTransition(textSamochod, translateText);
            ParallelTransition transitionTextPaliwo = new ParallelTransition(textPaliwoSamochod, translateTextPaliwo);
            transition.play();
            transitionText.play();
            transitionTextPaliwo.play();

            transition.setOnFinished(actionEvent -> {
                wolneStanowisko[stanowisko]=true;
                stanowiska.release();
                stacja.release();
            });
        });
    }

    @Override
    public void run() {
        try {
            stanowiska.acquire();
            for(int i=0; i<wolneStanowisko.length; i++){
                if(wolneStanowisko[i]){
                    wolneStanowisko[i]=false;
                    stanowisko=i;
                    break;
                }
            }
            if(SettingsController.settings.getPozostalePaliwo(idPaliwa)-litry>=0){
                while (kolejka.indexOf(id)!=0);
                SettingsController.settings.setPozostalePaliwo(idPaliwa, SettingsController.settings.getPozostalePaliwo(idPaliwa)-litry);
                animZajeciaStanowiska(id,stanowisko);
            }else {
                if(SettingsController.settings.koniecPaliwa()){
                    animWyjechaniaZKolejki(id, stanowisko);
                    return;
                }
            }

            Thread.sleep((long) SettingsController.settings.getTimeFueling()*litry+SettingsController.settings.getTimeAnim());

            SettingsController.settings.setPozostalePaliwoForDisplay(idPaliwa, SettingsController.settings.getPozostalePaliwoForDisplay(idPaliwa)-litry);
            MainController.textPozostalePaliwoList[idPaliwa].setText(String.valueOf(SettingsController.settings.getPozostalePaliwoForDisplay(idPaliwa)));

            kasy.acquire();
            for (int i=0; i<wolnaKasa.length; i++){
                if(wolnaKasa[i]){
                    wolnaKasa[i]=false;
                    kasa=i;
                    break;
                }
            }
            animZajeciaKasy(id,kasa);

            Thread.sleep(SettingsController.settings.getTimePaying()+SettingsController.settings.getTimeAnim());

            animWyjechania(id,kasa,stanowisko);

            MainAlg.samochods.remove(Samochod.this);
        } catch (InterruptedException e) {
            return;
        }
    }
}
