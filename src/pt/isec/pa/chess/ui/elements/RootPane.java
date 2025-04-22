package pt.isec.pa.chess.ui.elements;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;

public class RootPane extends BorderPane {

    private ChessGameManager gameManager;

    public RootPane(ChessGameManager gameManager) {
        this.gameManager = gameManager;

        createViews();
        // registerHandlers();
        // update();
    }

    private void createViews(){
        setTop(
                new VBox(
                        new MenuTop(this.gameManager)
                )
        );


    }


}
