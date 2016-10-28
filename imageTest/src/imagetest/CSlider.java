/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetest;

import javafx.scene.control.Slider;

/**
 *
 * @author Jolan
 */
public class CSlider extends Slider {

    public CSlider(int min, int max, int v) {

        this.setMin(min);
        this.setMax(max);
        this.setValue(v);
        this.setShowTickLabels(true);
        this.setShowTickMarks(true);
        this.setMajorTickUnit(1);
        this.setMinorTickCount(1);
        this.setBlockIncrement(1);

        this.valueProperty().addListener((obs, oldval, newVal)
                -> this.setValue(Math.round(newVal.doubleValue())));

    }

}
