/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetest;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Jolan
 */
public class ImageTest extends Application {

    private static Logger log;
    private ListView<File> list;
    private ColorPicker cpTrans;
    private Slider slider;
    private CTabPane tabPane;
    private ProgressBar pgbProgress;
    private TextField txtMaxWidth;

    @Override
    public void start(Stage stage) {
        list = new ListView<>();

        Button btnFile = new Button();
        Button btnFolder = new Button();
        Button btnClear = new Button();
        Button btnSave = new Button();
        Button btnCompress = new Button();
        Button btnRemoveAll = new Button();
        Button btnRemoveSelected = new Button();

        Label lblCompQ = new Label();
        Label lblTrans = new Label();
        Label lblMaxWidth = new Label();

        slider = new CSlider(1, 10, 5);

        log = new Logger();

        pgbProgress = new ProgressBar(0);

        tabPane = new CTabPane();

        cpTrans = new ColorPicker();

        txtMaxWidth = new TextField();

        btnFile.setText("Select Files");
        btnFile.setLayoutX(5);
        btnFile.setLayoutY(10);
        btnFile.setPrefWidth(120);

        btnFolder.setText("Select Folder");
        btnFolder.setLayoutX(150);
        btnFolder.setLayoutY(10);
        btnFolder.setPrefWidth(150);

        btnClear.setText("Clear Log");
        btnClear.setLayoutX(80);
        btnClear.setLayoutY(420);

        btnSave.setText("Save Log");
        btnSave.setLayoutX(5);
        btnSave.setLayoutY(420);

        btnCompress.setText("Compress");
        btnCompress.setLayoutX(5);
        btnCompress.setLayoutY(375);

        btnRemoveAll.setText("Remove All");
        btnRemoveAll.setLayoutX(5);
        btnRemoveAll.setLayoutY(135);
        btnRemoveAll.setPrefWidth(120);

        btnRemoveSelected.setText("Remove Selected");
        btnRemoveSelected.setLayoutX(150);
        btnRemoveSelected.setLayoutY(135);
        btnRemoveSelected.setPrefWidth(150);

        lblCompQ.setText("Compression Quality");
        lblCompQ.setLayoutX(5);
        lblCompQ.setLayoutY(50);

        lblTrans.setText("Replace Transparency");
        lblTrans.setLayoutX(5);
        lblTrans.setLayoutY(95);

        lblMaxWidth.setText("px Max Width");
        lblMaxWidth.setLayoutX(230);
        lblMaxWidth.setLayoutY(375);

        txtMaxWidth.setLayoutX(150);
        txtMaxWidth.setLayoutY(375);
        txtMaxWidth.setPrefWidth(75);

        log.setLayoutX(5);
        log.setLayoutY(450);

        list.setPrefWidth(300);
        list.setPrefHeight(200);
        list.setLayoutX(5);
        list.setLayoutY(170);

        slider.setPrefWidth(150);
        slider.setLayoutX(150);
        slider.setLayoutY(50);

        pgbProgress.setPrefSize(790, 5);
        pgbProgress.setLayoutX(5);
        pgbProgress.setLayoutY(595);

        tabPane.setLayoutX(400);
        tabPane.setLayoutY(5);

        cpTrans.setPrefWidth(150);
        cpTrans.setLayoutX(150);
        cpTrans.setLayoutY(95);

        btnFile.setOnAction((ActionEvent event) -> {
            btnCompress.setDisable(true);
            log.log("Selecting images...");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            List<File> flist = fileChooser.showOpenMultipleDialog(stage);
            if (flist != null) {
                new Thread() {
                    public void run() {
                        int amount = flist.size();
                        int progress = 0;
                        setProgress(0);

                        for (File file : flist) {
                            if (file != null && isImage(file)) {
                                addToList(file);
                                progress += 1;
                                setProgress((1 / (double) amount) * progress);
                            }
                        }
                        setProgress(1);
                        writeLog("All images added");
                        disable(btnCompress, false);
                    }
                }.start();

            } else {
                disable(btnCompress, false);
            }

        });

        btnFolder.setOnAction((ActionEvent event) -> {
            btnCompress.setDisable(true);
            log.log("Selecting Folder...");
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Open Resource Folder");
            File folder = dc.showDialog(stage);

            if (folder != null) {
                new Thread() {
                    public void run() {
                        int amount = folder.listFiles().length;
                        int progress = 0;
                        setProgress(0);

                        for (File file : folder.listFiles()) {
                            if (file != null && isImage(file)) {
                                addToList(file);
                                progress += 1;
                                setProgress((1 / (double) amount) * (double) progress);
                            }
                        }
                        setProgress(1);
                        writeLog("All images in folder added");
                        disable(btnCompress, false);
                    }
                }.start();

            } else {
                disable(btnCompress, false);
            }

        });

        btnCompress.setOnAction((ActionEvent event) -> {
            btnCompress.setDisable(true);
            if (list.getItems() != null) {
                log.log("Processing...");

                new Thread() {
                    public void run() {
                        try {
                            ImageCompressor ic = new ImageCompressor();
                            ic.setTime(0);
                            int amount = list.getItems().size();
                            int progress = 0;
                            setProgress(0);
                            for (File file : list.getItems()) {
                                if (file != null && isImage(file)) {

                                    ic.compressImage(file, slider.valueProperty().floatValue(), awtColor(cpTrans.getValue()), txtMaxWidth.getText());
                                    progress += 1;
                                    setProgress((1 / (double) amount) * (double) progress);

                                }
                            }
                            writeLog("\nDONE");
                            writeLog("Total Time: " + String.valueOf(Double.valueOf(ic.getTime()) / 1000) + " sec\n");
                        } catch (Exception ex) {
                            writeLog("Exception caught: " + ex.toString());
                        }
                        disable(btnCompress, false);
                    }
                }.start();

            }

        });

        list.setOnMouseClicked((MouseEvent event) -> {
            File newValue = list.getSelectionModel().getSelectedItem();
            if (newValue != null && tabPane.getTabPreview().renderPreview()) {
                ImageCompressor ic = new ImageCompressor();
                tabPane.getTabPreview().setOldImage(null);
                tabPane.getTabPreview().setNewImage(null);
                System.gc();
                tabPane.getTabPreview().setOldImage(ic.getImage(newValue));
                tabPane.getTabPreview().setNewImage(ic.getCompressedImage(newValue, awtColor(cpTrans.getValue()), slider.valueProperty().floatValue()));
                String t = String.valueOf(ic.getTime());
                String oldS = String.valueOf(Math.round(ic.getOldSize() / 1000));
                String newS = String.valueOf(Math.round(ic.getNewSize() / 1000));
                tabPane.getTabPreview().setInfo("Compression Time: " + t + " ms\nOriginal Size: " + oldS + " kb\nNew Size: " + newS + " kb");
            }
        });

        btnClear.setOnAction((ActionEvent event) -> {
            log.clear();
        });

        btnRemoveAll.setOnAction((ActionEvent event) -> {
            list.getItems().clear();
            System.gc();
        });

        btnRemoveSelected.setOnAction((ActionEvent event) -> {
            list.getItems().remove(list.getSelectionModel().getSelectedItem());
            System.gc();
        });

        btnSave.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                log.saveFile(file);
            }
        });

        txtMaxWidth.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.matches("\\d*")) {
                try {
                    Integer.parseInt(newValue);
                } catch (java.lang.NumberFormatException ex) {
                }
            } else {
                txtMaxWidth.setText(oldValue);
            }
        });

        Pane root = new Pane();
        root.getChildren().addAll(btnFile, lblCompQ, slider, btnFolder, log, btnClear, btnSave, btnCompress, tabPane, list, cpTrans, lblTrans, btnRemoveAll, btnRemoveSelected, pgbProgress, txtMaxWidth, lblMaxWidth);
        Scene scene = new Scene(root, 800, 615);

        stage.setTitle("Image Conversion");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public boolean isImage(File f) {
        try {
            Image image = ImageIO.read(f);
            return image != null;
        } catch (IOException ex) {
            return false;
        }
    }

    public static Logger log() {
        return log;
    }

    public Color awtColor(javafx.scene.paint.Color fx) {
        return new Color((float) fx.getRed(),
                (float) fx.getGreen(),
                (float) fx.getBlue(),
                1); //opacity op 1 zetten, gelijk welk kleur geselecteerd is
    }

    public File getSelectedFile() {
        try {
            return list.getSelectionModel().getSelectedItem();
        } catch (Exception ex) {
            return null;
        }
    }

    public Color getPickColor() {
        return awtColor(cpTrans.getValue());
    }

    public float getCompRate() {
        return slider.valueProperty().floatValue();
    }

    public void refreshImages() {
        if (getSelectedFile() != null && tabPane.getTabPreview().renderPreview()) {
            ImageCompressor ic = new ImageCompressor();

            tabPane.getTabPreview().setOldImage(ic.getImage(getSelectedFile()));
            tabPane.getTabPreview().setNewImage(ic.getCompressedImage(getSelectedFile(), awtColor(cpTrans.getValue()), slider.valueProperty().floatValue()));
        }
    }

    public void writeLog(String txt) {
        Platform.runLater(() -> {
            log.log(txt);
        });
    }

    public void setProgress(double value) {
        Platform.runLater(() -> {
            pgbProgress.setProgress(value);
        });
    }

    public void addToList(File f) {
        Platform.runLater(() -> {
            list.getItems().add(f);
        });
    }

    public void disable(Button btn, boolean bln) {
        Platform.runLater(() -> {
            btn.setDisable(bln);
        });
    }

}
