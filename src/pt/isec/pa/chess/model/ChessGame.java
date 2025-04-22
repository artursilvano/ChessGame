package pt.isec.pa.chess.model;

import pt.isec.pa.chess.model.data.board.Board;
import pt.isec.pa.chess.model.data.pieces.*;

import java.io.*;
import java.util.Scanner;

import static pt.isec.pa.chess.model.data.pieces.PieceType.*;
import static pt.isec.pa.chess.model.data.pieces.Team.*;

public class ChessGame implements Constants, Serializable {
    @Serial private static final long serialVersionUID = 1L;// FACADE

    private Board board;

    private Team roundTeam;
    private int round;

    private String pWhite;
    private String pBlack;

    public Piece selectedPiece = null;

    private Team winner = null;

    public ChessGame() {
        board = new Board();
        round = 0;
        roundTeam = WHITE;
    }

    public ChessGame (String filename) {   // Inicia um jogo de xadrez ja iniciado
        importGame(filename);
    }

    public String getpWhite() {
        return pWhite;
    }

    public void setpWhite(String pWhite) {
        this.pWhite = pWhite;
    }

    public String getpBlack() {
        return pBlack;
    }

    public void setpBlack(String pBlack) {
        this.pBlack = pBlack;
    }

    public String showGame() { return this.roundTeam + this.board.toString(); }

    public boolean selectPiece(int row, int col) {                                    // Seleciona peca
        if (this.selectedPiece != null) return false;                                 // Se selecionou uma peca, nao pode selecionar outra

        this.selectedPiece = this.board.getPiece(row, col);
        if (this.selectedPiece != null && this.selectedPiece.getTeam().equals(this.roundTeam) && !this.selectedPiece.getPossibilities().isEmpty()) {
            return true;
        }

        this.selectedPiece = null;
        return false;
    }

    public boolean makeAMove(int row, int col) {
        if (this.selectedPiece == null) return false;
        if (this.selectedPiece.move(row, col)) {
            this.selectedPiece = null;
            this.nextRound();
            return true;
        }
        return false;
    }

    public int getRound() { return this.round; }

    public Team currentPlayer() { return this.roundTeam; }

    public void nextRound() {
        ++round;
        roundTeam = (roundTeam == BLACK) ? WHITE : BLACK;
        cleanEnPassant(roundTeam);
    }

    public boolean isNotOver() {
        for (int l = 0; l < TAM; l++)        // Verifica se alguma das pecas do time que vai jogar tem movimentos possiveis (Se nenhuma peca do time tiver movimentos possiveis, o jogo acaba)
            for (int c = 0; c < TAM; c++)
                if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getTeam().equals(this.roundTeam)) {
                    if (!this.board.getPiece(l, c).getPossibilities().isEmpty()) return true;
                }

        this.winner = this.roundTeam == WHITE ? BLACK : WHITE;
        return false;
    }

    public void cleanEnPassant(Team t){
        for (int l = 0; l < TAM; l++) {
            for (int c = 0; c < TAM; c++) {
                if (this.board.getPiece(l, c) != null && this.board.getPiece(l, c).getType().equals(ENPASSANT)) {
                    if (this.board.getPiece(l, c).getTeam().equals(t)) {
                        this.board.removePiece(l, c);
                    }
                }
            }
        }
    }

    public boolean exportGame(String filename) {
        try (FileWriter fw = new FileWriter(filename)){
            cleanEnPassant(BLACK);
            cleanEnPassant(WHITE);
            fw.write(this.roundTeam.toString() + ",\n");
            for (int i = 0; i < TAM; i++)
                for (int j = 0; j < TAM; j++)
                    if (this.board.getPiece(i, j) != null) {
                        fw.write(this.board.getPiece(i, j).toString().replaceAll("\\s","") + ",");
                    }

            fw.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean importGame(String filename) {
        try (FileReader fr = new FileReader(filename); Scanner sc = new Scanner(fr)){
            String el = sc.nextLine();

            this.roundTeam = el.equals("WHITE,") ? WHITE : BLACK;

            el = sc.nextLine();
            this.board = new Board(el);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }








}
