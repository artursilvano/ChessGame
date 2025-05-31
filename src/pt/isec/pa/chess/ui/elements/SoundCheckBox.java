package pt.isec.pa.chess.ui.elements;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import pt.isec.pa.chess.ui.res.SoundManager;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SoundCheckBox extends CheckBox {

    private static SoundCheckBox instance;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public SoundCheckBox() {

        super("Sound");
        this.setBackground(new Background(new BackgroundFill(Paint.valueOf("#baa072"), null, null)));
        this.setPadding(new Insets(5, 0, 50, 250));
        this.setAlignment(Pos.CENTER);

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

