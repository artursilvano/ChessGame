package pt.isec.pa.chess.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.ui.elements.PlayerNames;

import pt.isec.pa.chess.ui.elements.RootPane;

public class ChessUI extends Application {

    private ChessGameManager gameManager;

    PlayerNames pn;


    public ChessUI () {gameManager = new ChessGameManager(); }

    public void start(Stage stage) {


        pn = new PlayerNames(gameManager);


        stage.setTitle("Chess Game");

        RootPane root = new RootPane(gameManager);


        Scene scene = new Scene(root, 800, 800);


        stage.setScene(scene);


        stage.show();
    }

}
