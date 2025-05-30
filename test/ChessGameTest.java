import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.chess.model.ChessGame;
import pt.isec.pa.chess.model.data.pieces.PieceType;
import pt.isec.pa.chess.model.data.pieces.Team;

import static org.junit.jupiter.api.Assertions.*;

public class ChessGameTest {

    ChessGame game;

    @BeforeEach
    public void setup() {
        game = new ChessGame();
    }

    @Test
    public void testInitialRoundAndTeam() {
        assertEquals(0, game.getRound(), "Round inicial deve ser 0");
        assertEquals(Team.WHITE, game.currentPlayer(), "Jogador inicial deve ser WHITE");
    }

    @Test
    public void testSelectValidPiece() {
        // Seleciona um peão branco na posição inicial (linha 6, coluna 0)
        boolean selected = game.selectPiece(6, 0);
        assertTrue(selected, "Deve selecionar um peão branco válido");
        assertNotNull(game.getSelectedPiece());
    }

    @Test
    public void testSelectInvalidPiece() {
        // Tentativa de selecionar uma casa vazia
        boolean selected = game.selectPiece(4, 4);
        assertFalse(selected, "Não deve selecionar uma peça em casa vazia");
        assertNull(game.getSelectedPiece());
    }

    @Test
    public void testMakeValidMove() {
        game.selectPiece(6, 0); // Seleciona peão branco
        boolean moved = game.makeAMove(5, 0); // Move peão uma casa para frente
        assertTrue(moved, "Movimento válido deve retornar true");
        assertNull(game.getSelectedPiece());
        assertEquals(1, game.getRound(), "Round deve avançar após movimento");
        assertEquals(Team.BLACK, game.currentPlayer(), "Deve alternar jogador para BLACK");
    }


    @Test
    public void testIsOverAtStart() {
        // No começo do jogo, não deve estar terminado
        assertFalse(game.isOver(), "Jogo não deve estar terminado no início");
    }

    @Test
    public void testIsCheck() {
        // Criar situação simples de xeque no rei branco
        // Para simplificar, posicionar uma torre preta perto do rei branco
        game.getBoard().addPiece(
                PieceType.ROOK.createPiece(game.getBoard(), 6, 4, Team.BLACK),
                6, 4
        );
        assertTrue(game.isCheck(), "Deve detectar xeque no rei branco");
    }
}
