package pt.isec.pa.chess.ui.elements;



import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.command.PROP;


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
        setBackground(new Background(new BackgroundFill(Paint.valueOf("#2e4531"), null, null)));

        createViews();

        gameManager.addPropertyChangeListener(PROP.newGame,evt -> updateInit());

        gameManager.addPropertyChangeListener(PROP.initGame, evt -> updateInit());

        gameManager.addPropertyChangeListener(PROP.soundOn, evt -> updateSoundCheckBox());

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
        Label l = new Label("Crie ou abra um jogo!");
        l.setFont(new Font("arial", 20));
        l.setTextFill(Paint.valueOf("#ffffff"));

        setCenter(l);

    }

    public void update() {
        minSize = min(this.getWidth(), this.getHeight());
        if (fullBoard != null) {
            fullBoard.update();
        }
    }

    public void updateSoundCheckBox() {
        soundCheckBox.setSelected(gameManager.getSoundOn());
    }

    public void updateInit() {
        this.playerNames = new PlayerNames(this.gameManager);
        this.soundCheckBox = new SoundCheckBox();
        this.fullBoard = new FullBoard(this.gameManager, this);

        soundCheckBox.setOnAction( event -> {
            gameManager.changeSoundOn(soundCheckBox.isSelected());
        });

        setCenter(fullBoard);

        HBox botInfo= new HBox(playerNames, soundCheckBox);
        botInfo.setBackground(new Background(new BackgroundFill(Paint.valueOf("#baa072"), null, null)));
        botInfo.setAlignment(Pos.CENTER);
        setBottom(botInfo);

    }

    public ChessGameManager newGame() {
        gameManager.createGame();
        return gameManager;

    }

    public double getMinSize() {
        return minSize;
    }


}
