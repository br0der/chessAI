public class evaluation {
    public double eval;
    public Move move;

    public evaluation(double eval, Move move){
        this.eval = eval;
        this.move = move;
    }

    public evaluation(double eval){
        this.eval = eval;
        this.move = null;
    }
}
