import java.security.SecureRandom;

public class Zobrist {
    private static final long[][][] piecesArray = new long[8][2][64];
    private static final long[] castlingRights = new long[16];
    /// ep file (0 = no ep).
    private static final long[] enPassantFile = new long[9];
    private static long sideToMove;

    public Zobrist(){
        fillZobrist();
    }

    public long random(){
        SecureRandom random = new SecureRandom();
        return random.nextLong();
    }

    public void fillZobrist(){
        for (int pieceType = 0; pieceType < 6; pieceType++)
        {
            for (int color = 0; color < 2; color++)
            {
                for (int square = 0; square < 64; square++)
                {
                    piecesArray[pieceType][color][square] = random();
                }
            }
        }
        for (int column = 0; column < 8; column++)
        {
            enPassantFile[column] = random();
        }
        for (int i = 0; i < 4; i++)
        {
            castlingRights[i] = random();
        }
        sideToMove = random();
    }

    public static long saveZobristKey(piece[][] board){
        long zobristKey = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    int pieceType = board[i][j].type;
                    int pieceColor = board[i][j].isWhite? 1 : 0;

                    zobristKey ^= Zobrist.piecesArray[pieceType] [pieceColor] [i*8+j];
                }
            }
        }

        zobristKey ^= enPassantFile[main.enPassant[0]+1];

        if (main.isWhitesMove) {
            zobristKey ^= sideToMove;
        }

        zobristKey ^= castlingRights[(main.WCK? 1 : 0)*8+(main.WCQ? 1 : 0)*4+(main.BCK? 1 : 0)*2+(main.BCQ? 1 : 0)];

        return zobristKey;
    }
}
