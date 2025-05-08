package pt.isec.pa.chess.ui.elements;


import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;

public class RootPane extends BorderPane {


    private final ChessGameManager gameManager;
    private PlayerNames playerNames;
    private FullBoard fullBoard;


    public RootPane(ChessGameManager gameManager) {
        this.gameManager = gameManager;

        createViews();

    }

    private void createViews(){
        setTop(
                new VBox(

                        new MenuTop(this.gameManager, this)
                )
        );


    }

    public void update() {
        playerNames.update();
        fullBoard.update();
    }

    public void updateInit() {
        this.playerNames = new PlayerNames(gameManager);
        fullBoard = new FullBoard(this.gameManager, this);
        setLeft(playerNames);
        setCenter(fullBoard);
        update();
    }

    public ChessGameManager newGame() {
        gameManager.createGame();
        return gameManager;

    }


}
