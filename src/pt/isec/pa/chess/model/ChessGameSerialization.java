package pt.isec.pa.chess.model;

import java.io.*;

public class ChessGameSerialization {

    private ChessGameSerialization(){}

    public static void serialize(String filename, ChessGame game) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))){
            oos.writeObject(game);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static ChessGame deserialize(String filename) throws ClassNotFoundException, IOException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            ChessGame chess = (ChessGame) ois.readObject();
            return chess;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
