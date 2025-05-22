package pt.isec.pa.chess.ui.elements;

import javafx.scene.control.CheckBox;
import pt.isec.pa.chess.ui.res.SoundManager;


public class SoundCheckBox extends CheckBox {

    private static SoundCheckBox instance;

    public SoundCheckBox() {
        super("Sound");
        instance = this;
    }


    public static boolean isChecked() {
        return instance != null && instance.isSelected();
    }
}

