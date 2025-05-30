package pt.isec.pa.chess.ui.elements;

import javafx.scene.control.CheckBox;
import pt.isec.pa.chess.ui.res.SoundManager;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SoundCheckBox extends CheckBox {

    private static SoundCheckBox instance;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public SoundCheckBox() {

        super("Sound");
        instance = this;
        registerHandlers();
    }


    public static boolean isChecked() {
        return instance != null && instance.isSelected();
    }

    public void registerHandlers() {
        this.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                SoundManager.stop();
            }
        });
    }


}

