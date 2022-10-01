import java.util.ArrayList;
import java.util.HashMap;

public class ai {
    public static Move bestMove;
    public static double bestEval;
    public static piece[][] board;
    public static HashMap<Long,Double> map = new HashMap<Long,Double>();

    public static double countMaterial(boolean isWhite){
        double total = 0;
        ArrayList<piece> target;
        if(isWhite){
            target = piece.whitePieces;
        }
        else{
            target = piece.blackPieces;
        }
        for (piece piece : target) {
            char type = Character.toLowerCase(piece.printNameEnglish());
            if (type == 'q') {
                total += 8;
            } else if (type == 'r') {
                total += 5;
            } else if (type == 'b' || type == 'n') {
                total += 3;
            } else if (type == 'p') {
                total++;
            } else {
                total += 200;
            }
        }
        return total;
    }

    public static double evaluate(){
        double whiteMaterial = countMaterial(true);
        double blackMaterial = countMaterial(false);
        double eval = whiteMaterial-blackMaterial;
        int persp = main.isWhitesMove? 1 : -1;
        return eval*persp;
    }

    public static double search(int depth, boolean isWhite, double alpha, double beta, boolean isFirstLoop){
        if(depth==0)
            return evaluate();
        ArrayList<Move> Moves = main.generateMoves(isWhite);
        if(Moves.size() == 0){
            if(piece.isChecked(true,board)||piece.isChecked(false,board))
                return -100000;
            return 0;
        }

        //evaluation bestEval = new evaluation(-100000);
//        Move bestMoveThisIteration;
        for(Move move : Moves) {
            //move

            //System.out.println(move.target.x + ", " + move.target.y + " " + move.preX + ", " + move.preY + " a");

            piece.move(move, board);
            //check if eval is good
            double eval;
            if(map.containsKey(Zobrist.saveZobristKey(board))){
                eval = map.get(Zobrist.saveZobristKey(board));
            }
            else {
                eval = -search(depth - 1, !isWhite, -beta, -alpha, false);

            }
            //undo move
            piece.unMakeMove(move,board);
            //System.out.println(move.target.x + ", " + move.target.y + " " + move.preX + ", " + move.preY + " b");

            if(eval>=beta) {
                map.put(Zobrist.saveZobristKey(board), eval);
                return beta;
            }
            if (eval > alpha) {
                if(isFirstLoop){
                    bestMove = move;
                }
//                else{
//                    bestMoveThisIteration = move;
//                }
                alpha = eval;
            }
            alpha = Math.max(alpha, eval);
        }
//        map.put(Zobrist.saveZobristKey(board),eval);
        return alpha;
    }
}
