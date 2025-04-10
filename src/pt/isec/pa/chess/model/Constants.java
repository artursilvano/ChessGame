package pt.isec.pa.chess.model;

public interface Constants {
    int TAM = 8;
    Character [] xAxis = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

    default int columnToNum(Character c) {
        for (int i = 0; i < TAM; i++) {
            if (xAxis[i] == c) return i;
        }
        return -1;
    }

}