/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetest;

import javafx.scene.control.TabPane;

/**
 *
 * @author Jolan
 */
public class CTabPane extends TabPane {

    private final TabPreview tabPreview;
    private final TabHelp tabHelp;

    public CTabPane() {
        this.setPrefSize(395, 590);
        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        tabPreview = new TabPreview();

        tabHelp = new TabHelp();

        this.getTabs().addAll(tabPreview, tabHelp);
    }

    public TabPreview getTabPreview() {
        return tabPreview;
    }

    public TabHelp getTabHelp() {
        return tabHelp;
    }

}
