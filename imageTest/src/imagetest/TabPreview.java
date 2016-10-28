/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetest;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
/**
 *
 * @author Jolan
 */
public class TabPreview extends Tab {

    private final CheckBox chkPreview;
    private final ScrollPane spOld;
    private final ScrollPane spNew;
    private final Label lblInfo;
    final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
    private ImageView ivOld, ivNew;

    public TabPreview() {
        this.setText("Preview");

        VBox box = new VBox();

        chkPreview = new CheckBox();

        Button btnRefresh = new Button();

        spOld = new ScrollPane();
        spNew = new ScrollPane();

        ivOld = new ImageView();
        ivNew = new ImageView();

        ivOld.setPreserveRatio(true);
        ivNew.setPreserveRatio(true);
        ivOld.setOnScroll(e -> {
            double delta = e.getDeltaY();
            if (delta < 0) {
                ivOld.setFitWidth(ivOld.getLayoutBounds().getWidth() * 0.8);
            } else if (delta > 0) {
                ivOld.setFitWidth(ivOld.getLayoutBounds().getWidth() * 1.2);
            }
        });
        ivNew.setOnScroll(e -> {
            double delta = e.getDeltaY();
            if (delta < 0) {
                ivNew.setFitWidth(ivNew.getLayoutBounds().getWidth() * 0.8);
            } else if (delta > 0) {
                ivNew.setFitWidth(ivNew.getLayoutBounds().getWidth() * 1.2);
            }
        });
        

        spOld.setContent(ivOld);
        spNew.setContent(ivNew);

        lblInfo = new Label("");

        spOld.setPrefSize(150, 150);
        spNew.setPrefSize(150, 150);

        chkPreview.setText("Render Preview");
        chkPreview.setLayoutX(5);
        chkPreview.setLayoutY(5);

        btnRefresh.setText("Refresh");

        btnRefresh.setOnAction((ActionEvent event) -> {
            new ImageTest().refreshImages();
        });

        box.setSpacing(10);
        box.setPadding(new Insets(10, 20, 10, 20));

        box.getChildren().addAll(chkPreview, spOld, spNew, lblInfo);

        this.setContent(box);

    }

    public boolean renderPreview() {
        return chkPreview.isSelected();
    }

    public void setInfo(String txt) {
        lblInfo.setText(txt);
    }

    public void setOldImage(Image im) {
        ivOld.setImage(im);
    }

    public void setNewImage(Image im) {
        ivNew.setImage(im);
    }

}
