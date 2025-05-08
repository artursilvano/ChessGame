package pt.isec.pa.chess.model;

public interface Constants {
    int TAM = 8;

    Character [] xAxis = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};


    default int columnToNum(Character c) {
        for (int i = 0; i < TAM; i++) {
            if (xAxis[i] == c) return i;
        }
        return -1;
    }

}