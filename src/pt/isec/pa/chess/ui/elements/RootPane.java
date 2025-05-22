package pt.isec.pa.chess.ui.elements;



import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.isec.pa.chess.model.ChessGameManager;



import static java.lang.Math.min;

public class RootPane extends BorderPane {


    private final ChessGameManager gameManager;
    private PlayerNames playerNames;
    private FullBoard fullBoard;
    private double minSize;
    private SoundCheckBox soundCheckBox;


    public RootPane(ChessGameManager gameManager) {
        this.gameManager = gameManager;
        this.setMinWidth(500);
        this.setMinHeight(500);
        minSize = min(this.getWidth(), this.getHeight());

        createViews();

        gameManager.addPropertyChangeListener("newGame", evt -> updateInit());

        gameManager.addPropertyChangeListener("initGame", evt -> updateInit());

        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            update();
        } );
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            update();
        } );

    }

    private void createViews(){
        setTop(
                new VBox( new MenuTop(this.gameManager, this) )
        );
        setCenter( new Label("Crie ou abra um jogo!"));

    }

    public void update() {
        minSize = min(this.getWidth(), this.getHeight());
        if (fullBoard != null) {
            fullBoard.update();
        }
    }

    public void updateInit() {
        this.playerNames = new PlayerNames(gameManager);
        fullBoard = new FullBoard(this.gameManager, this);
        soundCheckBox = new SoundCheckBox();

        setBottom(playerNames);
        setCenter(fullBoard);
        setRight(soundCheckBox);

    }

    public ChessGameManager newGame() {
        gameManager.createGame();
        return gameManager;

    }

    public double getMinSize() {
        return minSize;
    }


}
