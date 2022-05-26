package com.mily.stacja;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    public Button btnStart;
    public Button btnStop;
    public Button btnSettings;

    public Pane pane;
    Settings settings;
    static ArrayList<Rectangle> samochodyContenerList;
    static ArrayList<Rectangle> stanowiskaList;
    static ArrayList<Rectangle> kasyList;
    ArrayList<Text> textSamochodyContenerList;

    static Text[] textPozostalePaliwoList;

    static MainAlg mainAlg;

    static int bokKwadratu;
    static int wysKasy;
    static int szerKasy;

    static boolean flagConfig=false;
    boolean flagFormStart=false;

    static Button btnStartS;
    static Button btnStopS;
    static Button btnSettingS;

    public void initialize(){
        btnStartS=btnStart;
        btnStopS=btnStop;
        btnSettingS=btnSettings;
    }

    public void startSymulation(Event actionEvent) throws IOException {
        if(!flagConfig){
            flagFormStart=true;
            settings(actionEvent);
        }else{
            mainAlg = new MainAlg(pane);
            mainAlg.start();
        }
        btnStart.setDisable(true);
        btnStop.setDisable(false);
        btnSettings.setDisable(true);
    }

    public void stopSymulation(Event actionEvent) {
        btnStart.setDisable(false);
        btnStop.setDisable(true);
        btnSettings.setDisable(false);
        flagConfig=false;

        mainAlg.interrupt();
    }

    public void settings(Event actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("SettingsGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 575);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("Ustawienia");
        stage.setScene(scene);

        stage.setOnHidden(we -> {
            pane.getChildren().clear();
            settings=SettingsController.settings;

            bokKwadratu = 60;
            int odstep = 30;
            Rectangle rec;

            //renderowanie samochodow
            samochodyContenerList = new ArrayList<>();
            textSamochodyContenerList = new ArrayList<>();
            double startingPointX = pane.getWidth() / 2 - settings.getN() * (bokKwadratu+10) / 2 - (settings.getN() - 1) * odstep / 2;
            double startingPointY = 20;
            for (int i = 0; i < settings.getN(); i++) {
                rec = new Rectangle(startingPointX + i * odstep + i * (bokKwadratu+10), startingPointY, (bokKwadratu+10), (bokKwadratu+10));
                samochodyContenerList.add(rec);
            }
            Text text;
            int h = 0;
            for (Rectangle x : samochodyContenerList) {
                text = new Text(String.valueOf(h));
                h++;
                text.setX(x.getX() + 2);
                text.setY(x.getY() - 2);
                text.setFill(Color.BLACK);
                textSamochodyContenerList.add(text);

                x.setFill(Color.TRANSPARENT);
                x.setStrokeType(StrokeType.INSIDE);
                x.setStrokeWidth(1);
                x.setStroke(Color.BLACK);

                pane.getChildren().add(x);
                pane.getChildren().add(text);
            }

            //renderowanie stanowisk
            stanowiskaList = new ArrayList<>();
            startingPointX=pane.getWidth()/2-settings.getM()*(bokKwadratu+10)/2-(settings.getM()-1)*odstep/2;
            startingPointY=pane.getHeight()/2-(bokKwadratu+10)/2;
            for(int i=0; i<settings.getM(); i++){
                rec=new Rectangle(startingPointX+i*odstep+i*(bokKwadratu+10), startingPointY, (bokKwadratu+10), (bokKwadratu+10));
                stanowiskaList.add(rec);
            }
            h=0;
            for(Rectangle x : stanowiskaList){
                text=new Text(String.valueOf(h));
                h++;
                text.setX(x.getX()+2);
                text.setY(x.getY()-2);
                text.setFill(Color.BLACK);

                x.setFill(Color.TRANSPARENT);
                x.setStrokeType(StrokeType.INSIDE);
                x.setStrokeWidth(1);
                x.setStroke(Color.BLACK);

                pane.getChildren().add(x);
                pane.getChildren().add(text);
            }

            //renderowanie kas
            wysKasy=60;
            szerKasy=40;
            kasyList = new ArrayList<>();
            startingPointX=pane.getWidth()-szerKasy;
            startingPointY=pane.getHeight()/2-settings.getK()*wysKasy/2-(settings.getK()-1)*odstep/2;
            for(int i=0; i<settings.getK(); i++){
                rec=new Rectangle(startingPointX, startingPointY+i*odstep+i*wysKasy, szerKasy, wysKasy);
                kasyList.add(rec);
            }
            h=0;
            for(Rectangle x : kasyList){
                text=new Text(String.valueOf(h));
                h++;
                text.setX(x.getX()+2);
                text.setY(x.getY()-2);
                text.setFill(Color.BLACK);

                x.setFill(Paint.valueOf("#FEDB9B"));
                x.setStrokeType(StrokeType.INSIDE);
                x.setStrokeWidth(1);
                x.setStroke(Color.BLACK);

                pane.getChildren().add(x);
                pane.getChildren().add(text);
            }

            //renderowanie pozostalego paliwa
            Text text1;
            int maxl=0;
            for(String x : settings.getRodzajePaliw()){
                if(maxl<x.length()) maxl=x.length();
            }

            textPozostalePaliwoList=new Text[settings.getRodzajePaliw().length];
            for(int i=0; i<settings.getRodzajePaliw().length; i++){
                text=new Text(settings.getRodzajePaliw()[settings.getRodzajePaliw().length-1-i]+":");
                text.setFont(Font.font(16));
                text.setX(3);
                text.setY(pane.getHeight()-10-i*text.getBoundsInLocal().getHeight());

                text1=new Text(String.valueOf(settings.getPozostalePaliwoForDisplay(settings.getRodzajePaliw().length-1-i)));
                text1.setFont(Font.font(16));
                text1.setX(maxl*16);
                text1.setY(text.getY());
                text1.setAccessibleText(text.getText().replace(":",""));

                pane.getChildren().add(text);
                pane.getChildren().add(text1);
                textPozostalePaliwoList[settings.getRodzajePaliw().length-1-i]=text1;
            }

            if(flagFormStart){
                mainAlg = new MainAlg(pane);
                mainAlg.start();
                flagFormStart=false;
            }
            flagConfig=true;
        });
        stage.show();
    }

    public void buttonHover(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: #0096C9; -fx-text-fill: white; -fx-background-radius: 0");
    }

    public void buttonUnHover(MouseEvent mouseEvent) {
        ((Button)mouseEvent.getSource()).setStyle("-fx-background-color: transparent; -fx-text-fill: black;");
    }
}
