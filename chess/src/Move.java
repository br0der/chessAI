public class Move {
    public int preX;
    public int preY;
    public int postX;
    public int postY;
    public boolean enPassant = false;
    public boolean promote = false;
    public boolean castle = false;
    public boolean[] oldCastleMetaData = {main.WCK, main.WCQ, main.BCK, main.BCQ};
    public piece takenPiece;
    public piece target;
    public Move(int preX, int preY, int postX, int postY, piece target){
        this.preX = preX;
        this.preY = preY;
        this.postX = postX;
        this.postY = postY;
        this.target = target;
    }
}
