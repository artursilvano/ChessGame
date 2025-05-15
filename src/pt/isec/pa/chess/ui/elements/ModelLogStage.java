package pt.isec.pa.chess.ui.elements;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ModelLog;

import java.util.List;

public class ModelLogStage extends Stage {

    private ListView<String> ListView;
    private Button btnClear;
    public ModelLogStage(){

        createviews();
        update();
        registerHandlers();
    }
    private void createviews() {
        ListView = new ListView<>();
        btnClear = new Button("Clear");
        VBox layout = new VBox();
        layout.getChildren().addAll( ListView,btnClear);


        setTitle("ModelLog");

        Scene scene2 = new Scene(layout, 800, 800);
        setX(getX() - 100);
        setScene(scene2);
        setMinHeight(600);
        setMinWidth(600);

    }

    private void registerHandlers() {
        btnClear.setOnAction(_ -> {
            ModelLog.getInstance().clearLogs();

        });
        ModelLog.getInstance().addPropertyChangeListener(_ -> update());
    }

    private void update() {
        List<String> lista = ModelLog.getInstance().getLogs();
        ListView.getItems().setAll(lista);
    }
}
