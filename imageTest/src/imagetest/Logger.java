/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javafx.scene.control.TextArea;

/**
 *
 * @author Jolan
 */
public class Logger extends TextArea {

    public Logger() {
        this.setEditable(false);
        this.setPrefWidth(790);
        this.setPrefHeight(140);

    }

    public void log(String value) {
        if (value != null) {
            this.setText(this.getText() + value + "\n");
            this.appendText("");
        }
    }

    public void log(Float value) {
        if (value != null) {
            this.setText(this.getText() + String.valueOf(value) + "\n");
            this.appendText("");
        }
    }

    public void saveFile(File file) {
        try {
            try (PrintStream out = new PrintStream(new FileOutputStream(file.getAbsolutePath()))) {
                String content = this.getText().replaceAll("(\n)", "\r\n");
                out.print(content);
                System.out.println(content);
                out.flush();
                out.close();
            }
        } catch (Exception ex) {

        }

    }

}
