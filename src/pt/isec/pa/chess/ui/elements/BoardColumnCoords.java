package pt.isec.pa.chess.ui.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import pt.isec.pa.chess.model.ChessGameManager;

public class BoardColumnCoords extends HBox {

    BoardColumnCoords(ChessGameManager gameManager, int wh) {
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(wh);
        this.setPrefHeight(20);

        Label l = null;
        for (int i = 0; i < gameManager.getBoardSize(); i++) {
            l = new Label(Character.toString(gameManager.getXAxis(i)));
            l.setPrefWidth((double) wh / gameManager.getBoardSize());
            l.setAlignment(Pos.CENTER);
            l.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
            this.getChildren().add(l);
        }
    }

}
