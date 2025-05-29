package pt.isec.pa.chess.ui;

import javafx.application.Application;
import javafx.scene.Scene;

import javafx.stage.Stage;
import pt.isec.pa.chess.model.ChessGameManager;

import pt.isec.pa.chess.ui.elements.ModelLogStage;
import pt.isec.pa.chess.ui.elements.RootPane;

public class ChessUI extends Application {

    private final ChessGameManager gameManager;


    public ChessUI () {gameManager = new ChessGameManager(); }

    public void start(Stage stage) {

        stage.setTitle("Chess Game");
        Scene scene = new Scene(new RootPane(gameManager), 800, 800);
        stage.setScene(scene);
        stage.setMinHeight(600);
        stage.setMinWidth(600);

        stage.show();


        stage.setTitle("Chess Game 2");
        Stage stage2 = new Stage();
        Scene scene2 = new Scene(new RootPane(gameManager), 800, 800);
        stage2.setX(stage.getX() - 50);
        stage2.setScene(scene2);
        stage2.setMinHeight(600);
        stage2.setMinWidth(600);

        stage2.show();

        ModelLogStage modelLogStage=new ModelLogStage();
        modelLogStage.show();


    }

}
