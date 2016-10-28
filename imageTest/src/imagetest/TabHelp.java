/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetest;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jolan
 */
public class TabHelp extends Tab {

    public TabHelp() {
        this.setText("Help");
        VBox boxMain = new VBox();

        Label lblHelp = new Label();
        
        lblHelp.setText("\nSTEP 1:\n- Add files to the list by selecting files or selecting an entire folder."
                + "\n\nSTEP 2:\n- Choose your compression quality.\n(1 low quality - 10 high quality)"
                + "\n\nSTEP 3:\n- Choose a color that will replace transparent pixels.\nNote: adding transparency to your color will not have any effect."
                + "\n\nSTEP 4:\n- Add or remove items from the list.\nYou can preview the outcome of the setting you chose."
                + "\n\nSTEP 5:\n- Choose a maximum width in pixels for your exported images.\nWhen you don't choose a maximum, the original size will be used."
                + "\n\nSTEP 6:\n- Compress!");
        
        boxMain.getChildren().addAll(lblHelp);        
        this.setContent(boxMain);
    }

}
