package com.mily.stacja;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.awt.*;
import java.io.*;
import java.util.Arrays;

public class SettingsController {
    public TextField tfM, tfK, tfN, tfTimeRefill, tfTimeFueling, tfTimePaying, tfMaxPaliwa, tfRodzajePaliw, tfTimeAnim, tfCars;
    public static Settings settings = new Settings();
    public void initialize(){
        initSet();
    }

    private void initSet(){
        tfCars.setText(String.valueOf(settings.getCars()));
        tfM.setText(String.valueOf(settings.getM()));
        tfK.setText(String.valueOf(settings.getK()));
        tfN.setText(String.valueOf(settings.getN()));
        tfTimeRefill.setText(String.valueOf(settings.getTimeRefill()));
        tfTimeFueling.setText(String.valueOf(settings.getTimeFueling()));
        tfTimePaying.setText(String.valueOf(settings.getTimePaying()));
        tfMaxPaliwa.setText(String.valueOf(settings.getMaxPaliwa()));
        tfTimeAnim.setText(String.valueOf(settings.getTimeAnim()));
        tfRodzajePaliw.setText(Arrays.toString(settings.getRodzajePaliw()).replaceAll("[\\[\\]]",""));
    }

    private void saveSetting(){
        settings.setCars(Integer.parseInt(tfCars.getText()));
        settings.setM(Integer.parseInt(tfM.getText()));
        settings.setK(Integer.parseInt(tfK.getText()));
        settings.setN(Integer.parseInt(tfN.getText()));
        settings.setTimeRefill(Integer.parseInt(tfTimeRefill.getText()));
        settings.setTimeFueling(Integer.parseInt(tfTimeFueling.getText()));
        settings.setTimePaying(Integer.parseInt(tfTimePaying.getText()));
        settings.setMaxPaliwa(Integer.parseInt(tfMaxPaliwa.getText()));
        settings.setTimeAnim(Integer.parseInt(tfTimeAnim.getText()));
        settings.setRodzajePaliw(tfRodzajePaliw.getText().replaceAll(" ","").split(","));
    }

    public void setSettings(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        saveSetting();
        thisStage.close();
    }

    public static void writeXml(Document document, OutputStream outputStream) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(outputStream);
        transformer.transform(source, result);
    }

    public void saveToFileSettings(ActionEvent actionEvent) throws IOException, ParserConfigurationException, TransformerException {
        saveSetting();

        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Zapisz ustawienia");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Ustawienia", "*.xml"));
        File file = fileChooser.showSaveDialog(thisStage);
        if(file==null) return;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        Element rootElement = document.createElement("stacja");
        document.appendChild(rootElement);
        Element Cars = document.createElement("Cars");
        Cars.setTextContent(String.valueOf(settings.getCars()));
        rootElement.appendChild(Cars);
        Element M = document.createElement("M");
        M.setTextContent(String.valueOf(settings.getM()));
        rootElement.appendChild(M);
        Element K = document.createElement("K");
        K.setTextContent(String.valueOf(settings.getK()));
        rootElement.appendChild(K);
        Element N = document.createElement("N");
        N.setTextContent(String.valueOf(settings.getN()));
        rootElement.appendChild(N);
        Element TimeRefill = document.createElement("TimeRefill");
        TimeRefill.setTextContent(String.valueOf(settings.getTimeRefill()));
        rootElement.appendChild(TimeRefill);
        Element TimeFueling = document.createElement("TimeFueling");
        TimeFueling.setTextContent(String.valueOf(settings.getTimeFueling()));
        rootElement.appendChild(TimeFueling);
        Element TimePaying = document.createElement("TimePaying");
        TimePaying.setTextContent(String.valueOf(settings.getTimePaying()));
        rootElement.appendChild(TimePaying);
        Element MaxPaliwa = document.createElement("MaxPaliwa");
        MaxPaliwa.setTextContent(String.valueOf(settings.getMaxPaliwa()));
        rootElement.appendChild(MaxPaliwa);
        Element TimeAnim = document.createElement("TimeAnimation");
        TimeAnim.setTextContent(String.valueOf(settings.getTimeAnim()));
        rootElement.appendChild(TimeAnim);
        Element RodzajePaliw = document.createElement("RodzajePaliw");
        RodzajePaliw.setTextContent(Arrays.toString(settings.getRodzajePaliw()));
        rootElement.appendChild(RodzajePaliw);

        FileOutputStream outputStream = new FileOutputStream(file);
        writeXml(document, outputStream);

        thisStage.close();
    }

    public void loadFromFileSettings(ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException {
        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wczytaj ustawienia");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Ustawienia", "*.xml"));
        File file = fileChooser.showOpenDialog(thisStage);

        if(file==null) return;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        try {
            settings.setCars(Integer.parseInt(document.getElementsByTagName("Cars").item(0).getTextContent()));
            settings.setM(Integer.parseInt(document.getElementsByTagName("M").item(0).getTextContent()));
            settings.setK(Integer.parseInt(document.getElementsByTagName("K").item(0).getTextContent()));
            settings.setN(Integer.parseInt(document.getElementsByTagName("N").item(0).getTextContent()));
            settings.setTimeRefill(Integer.parseInt(document.getElementsByTagName("TimeRefill").item(0).getTextContent()));
            settings.setTimeFueling(Integer.parseInt(document.getElementsByTagName("TimeFueling").item(0).getTextContent()));
            settings.setTimePaying(Integer.parseInt(document.getElementsByTagName("TimePaying").item(0).getTextContent()));
            settings.setMaxPaliwa(Integer.parseInt(document.getElementsByTagName("MaxPaliwa").item(0).getTextContent()));
            settings.setTimeAnim(Integer.parseInt(document.getElementsByTagName("TimeAnimation").item(0).getTextContent()));
            settings.setRodzajePaliw(document.getElementsByTagName("RodzajePaliw").item(0).getTextContent().replaceAll("[\\[\\]]","").replaceAll(" ","").split(","));
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Błąd ładowania pliku");
            alert.setHeaderText(null);
            alert.setContentText("Niepoprawna składnia pliku \""+file.getName()+"\"");

            alert.show();
            final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.default");
            runnable.run();

            settings.loadDefault();
            return;
        }
        initSet();

        thisStage.close();
    }

    public void validate(KeyEvent keyEvent) {
        char input=keyEvent.getCharacter().charAt(0);
        Object source = keyEvent.getSource();
        if(Character.isLetter(input)){
            String Oldtext = ((TextField)source).getText();
            String NewText = Oldtext.replaceAll("[^\\d]", "");
            int caret=((TextField)source).getCaretPosition();
            ((TextField)source).setText(NewText);
            ((TextField)source).positionCaret(caret-1);
        }
    }

    public void onPressEnter(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ENTER){
            Node node = (Node) keyEvent.getSource();
            Stage thisStage = (Stage) node.getScene().getWindow();
            saveSetting();
            thisStage.close();
        }
    }
}
