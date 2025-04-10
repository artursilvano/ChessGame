package pt.isec.pa.chess.model;

import java.io.IOException;

public class ChessMain {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Cria uma nova partida
        ChessGame game = new ChessGame();

        game.makeAMove("Pe2", 4, 4);
        System.out.println(game.showGame());


        // Serializa a partida (salva o estado em um arquivo)
        ChessGameSerialization.serialize("chess_game.ser", game);

        // Desserializa a partida (lê o arquivo e recria o objeto)
        ChessGame loadedGame = ChessGameSerialization.deserialize("chess_game.ser");

        // Exibe o estado do tabuleiro após a desserialização
        System.out.println(loadedGame.showGame());
    }
}
