package pt.isec.pa.chess.ui.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.chess.model.ChessGameManager;

public class BoardRowCoords extends VBox {

    BoardRowCoords(ChessGameManager gameManager, int wh) {
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(20);
        this.setPrefHeight(wh);

        Label l;
        for (int i = 0; i < gameManager.getBoardSize(); i++) {
            l = new Label(Integer.toString(gameManager.getBoardSize() - i));
            l.setPrefHeight((double) wh / gameManager.getBoardSize());
            l.setAlignment(Pos.CENTER);
            l.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            this.getChildren().add(l);
        }
    }
}
