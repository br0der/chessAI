import processing.core.PImage;

import java.awt.image.RasterOp;
import java.util.ArrayList;

public class piece {
    public static ArrayList<piece> whitePieces = new ArrayList<>();
    public static ArrayList<piece> blackPieces = new ArrayList<>();
    public boolean isAlive = true;
    public static int[][] kingCoords = new int[2][2]; // Only updated when isChecked() is called
    public int type;
    public boolean isWhite;
    public boolean show = true;
    public int x;
    public int y;
    //0 is pawn, 1 is knight, 2 is bishop, 3 is rook, 4 is queen, 5 is king

    public piece(int x, int y, int type, boolean isWhite) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.isWhite = isWhite;
    }

    public char printNameEnglish() {
        if (isWhite) {
            if (type == 0) {
                return 'P';
            } else if (type == 1) {
                return 'N';
            } else if (type == 2) {
                return 'B';
            } else if (type == 3) {
                return 'R';
            } else if (type == 4) {
                return 'Q';
            } else {
                return 'K';
            }
        } else {
            if (type == 0) {
                return 'p';
            } else if (type == 1) {
                return 'n';
            } else if (type == 2) {
                return 'b';
            } else if (type == 3) {
                return 'r';
            } else if (type == 4) {
                return 'q';
            } else {
                return 'k';
            }
        }
    }

    public PImage printName() {
        String output = "";
        if (isWhite) {
            if (type == 0) {
                return main.wPawn;
            } else if (type == 1) {
                return main.wKnight;
            } else if (type == 2) {
                return main.wBishop;
            } else if (type == 3) {
                return main.wRook;
            } else if (type == 4) {
                return main.wQueen;
            } else {
                return main.wKing;
            }
        } else {
            if (type == 0) {
                return main.bPawn;
            } else if (type == 1) {
                return main.bKnight;
            } else if (type == 2) {
                return main.bBishop;
            } else if (type == 3) {
                return main.bRook;
            } else if (type == 4) {
                return main.bQueen;
            } else {
                return main.bKing;
            }
        }
    }

    public static void move(Move move, piece[][] board){
//        System.out.println("move");

        if (main.enPassant[0] != -1) {
            if (board[move.preX][move.preY].isWhite&&move.postX == main.enPassant[0] && move.postY == (8-main.enPassant[1])) {
                board[main.enPassant[0]][(8-main.enPassant[1]) + 1].isAlive = false;
                move.takenPiece=board[main.enPassant[0]][(8-main.enPassant[1]) + 1];
                board[main.enPassant[0]][(8-main.enPassant[1]) + 1] = null;
                move.enPassant=true;
            }
            if (!board[move.preX][move.preY].isWhite&&move.postX == main.enPassant[0] && move.postY == (8-main.enPassant[1])) {
                board[main.enPassant[0]][(8-main.enPassant[1]) - 1].isAlive = false;
                move.takenPiece=board[main.enPassant[0]][(8-main.enPassant[1]) - 1];
                board[main.enPassant[0]][(8-main.enPassant[1]) - 1] = null;
                move.enPassant=true;
            }
        }
        if(board[move.postX][move.postY]!=null&&!move.enPassant){
            move.takenPiece=board[move.postX][move.postY];
        }

//        System.out.println(move.target.type);
//        if(board[move.postX][move.postY]!=null){
////            System.out.println("hello");
//            System.out.println(board[move.postX][move.postY].type);
//        }
//        System.out.println(move.preX+","+move.preY+", "+move.postX+","+move.postY);
        if (board[move.preX][move.preY].printNameEnglish()=='P'&&move.preY - 2 == move.postY) {
            main.enPassant[0] = move.postX;
            main.enPassant[1] = move.postY+1;
            main.enPassant[1] = 8-main.enPassant[1];
        } else if (board[move.preX][move.preY].printNameEnglish()=='p'&&move.preY + 2 == move.postY) {
            main.enPassant[0] = move.postX;
            main.enPassant[1] = move.postY-1;
            main.enPassant[1] = 8-main.enPassant[1];
        }
        else{
            main.enPassant[0]=-1;
            main.enPassant[1]=-1;
        }
        if(Character.toLowerCase(board[move.preX][move.preY].printNameEnglish())=='p'){
            if(move.preY==0||move.preY==7) {
                move.target.type = 4;
                move.promote=true;
            }
        }
        if(board[move.preX][move.preY].printNameEnglish()=='K'){
            if(main.WCK&&move.preY==move.postY&&move.preX+2==move.postX){
                System.out.println(board[move.preX+1][move.preY]);
                move(new Move(7,7,5,7,board[7][7]),board);
                main.WCK=false;
                main.WCQ=false;
                move.castle = true;
            }
            else if(main.WCQ&&move.preY==move.postY&&move.preX-2==move.postX){
                System.out.println(board[move.preX-1][move.preY]);
                move(new Move(0,7,3,7,board[0][7]),board);
                main.WCQ=false;
                main.WCK=false;
                move.castle = true;
            }
        }
        if(board[move.preX][move.preY].printNameEnglish()=='k'){
            if(main.BCK&&move.preY==move.postY&&move.preX+2==move.postX){
                System.out.println(board[move.preX+1][move.preY]);
                move(new Move(7,0,5,0,board[7][0]),board);
                main.BCK=false;
                main.BCQ=false;
                move.castle = true;
            }
            else if(main.BCQ&&move.preY==move.postY&&move.preX-2==move.postX){
                System.out.println(board[move.preX-1][move.preY]);
                move(new Move(0,0,3,0,board[0][0]),board);
                main.BCK=false;
                main.BCQ=false;
                move.castle = true;
            }
        }
        if(board[move.postX][move.postY]!=null){
            board[move.postX][move.postY].isAlive=false;
            board[move.postX][move.postY]=null;
        }
        board[move.postX][move.postY]= move.target;
        board[move.preX][move.preY]=null;
        move.target.x=move.postX;
        move.target.y=move.postY;
        if(move.preX==4&&move.preY==0){
            main.BCK=false;
            main.BCQ=false;
        }
        else if(move.preX==4&&move.preY==7){
            main.WCK=false;
            main.WCQ=false;
        }
        else if(move.preX==7&&move.preY==7){
            main.WCK=false;
        }
        else if(move.preX==0&&move.preY==7){
            main.WCQ=false;
        }
        else if(move.preX==7&&move.preY==0){
            main.BCK=false;
        }
        else if(move.preX==0&&move.preY==0){
            main.BCQ=false;
        }
        //System.out.println();
    }

    public static void unMakeMove(Move move, piece[][] board){
        //board[move.target.x][move.target.y]=move.target;
//        System.out.print("un");
//        if (move.postX == 0 && move.postY == 3 && move.preX == 2 && move.preY == 2){
//            System.out.println("test");
//        }
        move(new Move(move.postX,move.postY,move.preX,move.preY,move.target), board);
        if(move.enPassant){
            if(move.target.isWhite){
                board[move.postX][move.postY+1] = move.takenPiece;
            }
            else{
                board[move.postX][move.postY-1] = move.takenPiece;
            }
        }
        else {
            board[move.postX][move.postY] = move.takenPiece;
        }
        if(move.castle){
            if(move.target.isWhite){
                if(move.preX==6){
                    move(new Move(5,7,7,7, board[5][7]), board);
                }
                else{
                    move(new Move(3,7,7,7, board[3][7]), board);
                }
            }
            else{
                if(move.preX==6){
                    move(new Move(5,0,0,0, board[5][0]), board);
                }
                else{
                    move(new Move(3,0,7,0, board[3][0]), board);
                }
            }
            main.WCK = move.oldCastleMetaData[0];
            main.WCQ = move.oldCastleMetaData[1];
            main.BCK = move.oldCastleMetaData[2];
            main.BCQ = move.oldCastleMetaData[3];
        }
        if(move.promote){
            move.target.type=1;
        }
        if (move.takenPiece != null) {
            board[move.takenPiece.x][move.takenPiece.y] = move.takenPiece;
            move.takenPiece.isAlive = true;
        }
    }

    public static boolean canMove(int x, int y, int incX, int incY, piece[][] board) {
        boolean pieceIsWhite = board[x][y].isWhite;
        if (x + incX < 8 && x + incX >= 0 && y + incY < 8 && y + incY >= 0) {
            if (board[x + incX][y + incY] == null || board[x + incX][y + incY].isWhite != pieceIsWhite) {
                return moveIsCheckedMoveBack(x, y, incX, incY, board);
            }
        }
        return false;
    }

    public static ArrayList<Move> pawnMove(int x, int y, piece[][] board, int[] enPassant) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        int epX = enPassant[0];
        int epY = enPassant[1];
        if (board[x][y].isWhite) {
            if (board[x][y - 1] == null) {
                if(moveIsCheckedMoveBack(x,y,0,-1,board))
                    possibleMoves.add(new Move(x,y,x,y-1,board[x][y]));

                if (y == 6 && board[x][y - 2] == null) {
                    if(moveIsCheckedMoveBack(x,y,0,-2,board))
                        possibleMoves.add(new Move(x,y,x,y-2,board[x][y]));
                }
            }

            if (y - 1 == 8 - epY) {
                if (x + 1 == epX) {
                    piece temp = board[epX][epY];
                    board[epX][epY] = null;
                    if(moveIsCheckedMoveBack(x,y,1,-1,board))
                        possibleMoves.add(new Move(x,y,x+1,y-1,board[x][y]));
                    board[epX][epY] = temp;
                }
                if (x - 1 == epX) {
                    piece temp = board[epX][epY];
                    board[epX][epY] = null;
                    if(moveIsCheckedMoveBack(x,y,-1,-1,board))
                        possibleMoves.add(new Move(x,y,x-1,y-1,board[x][y]));
                    board[epX][epY] = temp;
                }
            }

            if (x != 7 && board[x + 1][y - 1] != null && !board[x + 1][y - 1].isWhite) {
                if(moveIsCheckedMoveBack(x,y,1,-1,board))
                    possibleMoves.add(new Move(x,y,x+1,y-1,board[x][y]));
            }
            if (x != 0 && board[x - 1][y - 1] != null && !board[x - 1][y - 1].isWhite) {
                if(moveIsCheckedMoveBack(x,y,-1,-1,board))
                    possibleMoves.add(new Move(x,y,x-1,y-1,board[x][y]));
            }
        } else {
            if (board[x][y + 1] == null) {
                if(moveIsCheckedMoveBack(x,y,0,1,board))
                    possibleMoves.add(new Move(x,y,x,y+1,board[x][y]));
                if (y == 1 && board[x][y + 2] == null) {
                    if(moveIsCheckedMoveBack(x,y,0,2,board))
                        possibleMoves.add(new Move(x,y,x,y+2,board[x][y]));
                }
            }

            if (y + 1 == 8 - epY) {
                if (x + 1 == epX) {
                    piece temp = board[epX][epY];
                    board[epX][epY] = null;
                    if(moveIsCheckedMoveBack(x,y,1,1,board))
                        possibleMoves.add(new Move(x,y,x+1,y+1,board[x][y]));
                    board[epX][epY] = temp;
                }
                if (x - 1 == epX) {
                    piece temp = board[epX][epY];
                    board[epX][epY] = null;
                    if(moveIsCheckedMoveBack(x,y,-1,1,board))
                        possibleMoves.add(new Move(x,y,x-1,y+1,board[x][y]));
                    board[epX][epY] = temp;
                }
            }

            if (x != 7 && board[x + 1][y + 1] != null && board[x + 1][y + 1].isWhite) {
                if(moveIsCheckedMoveBack(x,y,1,1,board))
                    possibleMoves.add(new Move(x,y,x+1,y+1,board[x][y]));
            }
            if (x != 0 && board[x - 1][y + 1] != null && board[x - 1][y + 1].isWhite) {
                if(moveIsCheckedMoveBack(x,y,-1,1,board))
                    possibleMoves.add(new Move(x,y,x-1,y+1,board[x][y]));
            }
        }
        return possibleMoves;
    }

    public static ArrayList<Move> knightMove(int x, int y, piece[][] board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        if (canMove(x, y, 2, 1, board)) {
            possibleMoves.add(new Move(x,y,x+2,y+1,board[x][y]));
        }
        if (canMove(x, y, 2, -1, board)) {
            possibleMoves.add(new Move(x,y,x+2,y-1,board[x][y]));
        }
        if (canMove(x, y, -2, 1, board)) {
            possibleMoves.add(new Move(x,y,x-2,y+1,board[x][y]));
        }
        if (canMove(x, y, -2, -1, board)) {
            possibleMoves.add(new Move(x,y,x-2,y-1,board[x][y]));
        }
        if (canMove(x, y, 1, 2, board)) {
            possibleMoves.add(new Move(x,y,x+1,y+2,board[x][y]));
        }
        if (canMove(x, y, 1, -2, board)) {
            possibleMoves.add(new Move(x,y,x+1,y-2,board[x][y]));
        }
        if (canMove(x, y, -1, 2, board)) {
            possibleMoves.add(new Move(x,y,x-1,y+2,board[x][y]));
        }
        if (canMove(x, y, -1, -2, board)) {
            possibleMoves.add(new Move(x,y,x-1,y-2,board[x][y]));
        }
        return possibleMoves;
    }

    public static ArrayList<Move> queenMove(int x, int y, piece[][] board) {
        ArrayList<Move> possibleMoves = rookMove(x, y, board);
        possibleMoves.addAll(bishopMove(x, y, board));
        return possibleMoves;
    }

    public static ArrayList<Move> rookMove(int x, int y, piece[][] board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        //Up
        for (int i = 1; i < 8; i++) {
            if (canMove(x, y, 0, i, board)) {
                possibleMoves.add(new Move(x,y,x,y+i,board[x][y]));
                if (board[x][y + i] != null)
                    break;
            } else {
                break;
            }
        }
        //Down
        for (int i = 1; i < 8; i++) {
            if (canMove(x, y, 0, -i, board)) {
                possibleMoves.add(new Move(x,y,x,y-i,board[x][y]));
                if (board[x][y - i] != null)
                    break;
            } else {
                break;
            }
        }
        //Left
        for (int i = 1; i < 8; i++) {
            if (canMove(x, y, -i, 0, board)) {
                possibleMoves.add(new Move(x,y,x-i,y,board[x][y]));
                if (board[x - i][y] != null)
                    break;
            } else {
                break;
            }
        }
        //Right
        for (int i = 1; i < 8; i++) {
            if (canMove(x, y, i, 0, board)) {
                possibleMoves.add(new Move(x,y,x+i,y,board[x][y]));
                if (board[x + i][y] != null)
                    break;
            } else {
                break;
            }
        }
        return possibleMoves;
    }

    public static ArrayList<Move> bishopMove(int x, int y, piece[][] board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        //Up Right
        for (int i = 1; i < 8; i++) {
            if (canMove(x, y, i, i, board)) {
                possibleMoves.add(new Move(x,y,x+i,y+i,board[x][y]));
                if (board[x + i][y + i] != null)
                    break;
            } else {
                break;
            }
        }
        //Down Right
        for (int i = 1; i < 8; i++) {
            if (canMove(x, y, i, -i, board)) {
                possibleMoves.add(new Move(x,y,x+i,y-i,board[x][y]));
                if (board[x + i][y - i] != null)
                    break;
            } else {
                break;
            }
        }
        //Up Left
        for (int i = 1; i < 8; i++) {
            if (canMove(x, y, -i, i, board)) {
                possibleMoves.add(new Move(x,y,x-i,y+i,board[x][y]));
                if (board[x - i][y + i] != null)
                    break;
            } else {
                break;
            }
        }
        //Down Left
        for (int i = 1; i < 8; i++) {
            if (canMove(x, y, -i, -i, board)) {
                possibleMoves.add(new Move(x,y,x-i,y-i,board[x][y]));
                if (board[x - i][y - i] != null)
                    break;
            } else {
                break;
            }
        }
        return possibleMoves;
    }

    public static ArrayList<Move> kingMove(int x, int y, piece[][] board) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        if (canMove(x, y, -1, 0, board)) {
            possibleMoves.add(new Move(x,y,x-1,y,board[x][y]));
        }
        if (canMove(x, y, -1, -1, board)) {
            possibleMoves.add(new Move(x,y,x-1,y-1,board[x][y]));
        }
        if (canMove(x, y, -1, 1, board)) {
            possibleMoves.add(new Move(x,y,x-1,y+1,board[x][y]));
        }
        if (canMove(x, y, 1, 0, board)) {
            possibleMoves.add(new Move(x,y,x+1,y,board[x][y]));
        }
        if (canMove(x, y, 1, -1, board)) {
            possibleMoves.add(new Move(x,y,x+1,y-1,board[x][y]));
        }
        if (canMove(x, y, 1, 1, board)) {
            possibleMoves.add(new Move(x,y,x+1,y+1,board[x][y]));
        }
        if (canMove(x, y, 0, -1, board)) {
            possibleMoves.add(new Move(x,y,x,y-1,board[x][y]));
        }
        if (canMove(x, y, 0, 1, board)) {
            possibleMoves.add(new Move(x,y,x,y+1,board[x][y]));
        }
        if(board[x][y].isWhite){
            if(main.WCK&&board[x+1][y]==null&&board[x+2][y]==null&&!isChecked(true,board)){
                if(moveIsCheckedMoveBack(x,y,2,0, board)&&moveIsCheckedMoveBack(x,y,1,0, board))
                    possibleMoves.add(new Move(x,y,x+2,y,board[x][y]));
            }
            if(main.WCQ&&board[x-1][y]==null&&board[x-2][y]==null&&board[x-3][y]==null&&!isChecked(true,board)){
                if(moveIsCheckedMoveBack(x,y,-2,0, board)&&moveIsCheckedMoveBack(x,y,-1,0, board))
                    possibleMoves.add(new Move(x,y,x-2,y,board[x][y]));
            }
        }
        else{
            if(main.BCK&&board[x+1][y]==null&&board[x+2][y]==null&&!isChecked(false,board)){
                if(moveIsCheckedMoveBack(x,y,2,0, board)&&moveIsCheckedMoveBack(x,y,1,0, board))
                    possibleMoves.add(new Move(x,y,x+2,y,board[x][y]));
            }
            if(main.BCQ&&board[x-1][y]==null&&board[x-2][y]==null&&board[x-3][y]==null&&!isChecked(false,board)){
                if(moveIsCheckedMoveBack(x,y,-2,0, board)&&moveIsCheckedMoveBack(x,y,-1,0, board))
                    possibleMoves.add(new Move(x,y,x-2,y,board[x][y]));
            }
        }
        return possibleMoves;
    }

    public static boolean isChecked(boolean isWhite, piece[][] board) {
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                if (board[j][i] != null) {
//                    if (board[j][i].printNameEnglish() == 'K') {
//                        kingCoords[0] = new int[]{j, i};
//                    } else if (board[j][i].printNameEnglish() == 'k') {
//                        kingCoords[1] = new int[]{j, i};
//                    }
//                }
//            }
//        }
        for (piece whitePiece : whitePieces) {
            if (whitePiece.printNameEnglish() == 'K') {
                kingCoords[0] = new int[]{whitePiece.x, whitePiece.y};
                break;
            }
        }
        for (piece blackPiece : blackPieces) {
            if (blackPiece.printNameEnglish() == 'k') {
                kingCoords[1] = new int[]{blackPiece.x, blackPiece.y};
                break;
            }
        }
//        System.out.println(kingCoords[0][0]+", "+kingCoords[0][1]);
//        System.out.println(kingCoords[1][0]+", "+kingCoords[1][1]);
        int x = isWhite ? kingCoords[0][0] : kingCoords[1][0];
        int y = isWhite ? kingCoords[0][1] : kingCoords[1][1];
        // Bishop and Queen diagonals
        //Down Right
        for (int i = 1; i < 8; i++) {
            if (x + i < 8 && y + i < 8) {
                if (board[x + i][y + i] != null) {
                    if (board[x + i][y + i].isWhite != isWhite) {
                        if (Character.toLowerCase(board[x + i][y + i].printNameEnglish()) == 'q' || Character.toLowerCase(board[x + i][y + i].printNameEnglish()) == 'b')
                            return true;
                        if (i==1&&!isWhite && board[x + i][y + i].printNameEnglish() == 'P')
                            return true;
                    }
                    break;
                }
            } else {
                break;
            }
        }
//        System.out.println("DownLeft");
        //Down Left
        for (int i = 1; i < 8; i++) {
            if (x - i >= 0 && y + i < 8) {
                if (board[x - i][y + i] != null) {
                    if (board[x - i][y + i].isWhite != isWhite) {
                        if (Character.toLowerCase(board[x - i][y + i].printNameEnglish()) == 'q' || Character.toLowerCase(board[x - i][y + i].printNameEnglish()) == 'b')
                            return true;
                        if (i==1 && !isWhite && board[x - i][y + i].printNameEnglish() == 'P')
                            return true;
                    }
                    break;
                }
            } else {
                break;
            }
        }
//        System.out.println("UpRight");
        //Up Right
        for (int i = 1; i < 8; i++) {
            if (x + i < 8 && y - i >= 0) {
                if (board[x + i][y - i] != null) {
                    if (board[x + i][y - i].isWhite != isWhite) {
                        if (Character.toLowerCase(board[x + i][y - i].printNameEnglish()) == 'q' || Character.toLowerCase(board[x + i][y - i].printNameEnglish()) == 'b')
                            return true;
                        if (i==1 && isWhite && board[x + i][y - i].printNameEnglish() == 'p')
                            return true;
                    }
                    break;
                }
            } else {
                break;
            }
        }
//        System.out.println("UpLeft");
        //Up Left
        for (int i = 1; i < 8; i++) {
            if (x - i >= 0 && y - i >= 0) {
                if (board[x - i][y - i] != null) {
                    if (board[x - i][y - i].isWhite != isWhite) {
                        if (Character.toLowerCase(board[x - i][y - i].printNameEnglish()) == 'q' || Character.toLowerCase(board[x - i][y - i].printNameEnglish()) == 'b')
                            return true;
                        if (i==1&&isWhite && board[x - i][y - i].printNameEnglish() == 'p')
                            return true;
                    }
                    break;
                }
            } else {
                break;
            }
        }
        //Rook and Queen

        //Up
        for (int i = 1; i < 8; i++) {
            if (y - i >= 0) {
                if (board[x][y - i] != null) {
                    if (board[x][y - i].isWhite != isWhite) {
                        if (Character.toLowerCase(board[x][y - i].printNameEnglish()) == 'r' || Character.toLowerCase(board[x][y - i].printNameEnglish()) == 'q')
                            return true;
                    }
                    break;
                }
            } else {
                break;
            }
        }
        //Down
        for (int i = 1; i < 8; i++) {
            if (y + i < 8) {
                if (board[x][y + i] != null) {
                    if (board[x][y + i].isWhite != isWhite) {
                        if (Character.toLowerCase(board[x][y + i].printNameEnglish()) == 'r' || Character.toLowerCase(board[x][y + i].printNameEnglish()) == 'q')
                            return true;
                    }
                    break;
                }
            } else {
                break;
            }
        }
        //Right
        for (int i = 1; i < 8; i++) {
            if (x + i < 8) {
                if (board[x + i][y] != null) {
                    if (board[x + i][y].isWhite != isWhite) {
                        if (Character.toLowerCase(board[x + i][y].printNameEnglish()) == 'r' || Character.toLowerCase(board[x + i][y].printNameEnglish()) == 'q')
                            return true;
                    }
                    break;
                }
            } else {
                break;
            }
        }
        //Left
        for (int i = 1; i < 8; i++) {
            if (x - i >= 0) {
                if (board[x - i][y] != null) {
                    if (board[x - i][y].isWhite != isWhite) {
                        if (Character.toLowerCase(board[x - i][y].printNameEnglish()) == 'r' || Character.toLowerCase(board[x - i][y].printNameEnglish()) == 'q')
                            return true;
                    }
                    break;
                }
            } else {
                break;
            }
        }

        // Knight moves
        if (x + 2 < 8 && y + 1 < 8) {
            if (board[x + 2][y + 1] != null) {
                if (board[x + 2][y + 1].isWhite != isWhite) {
                    if (Character.toLowerCase(board[x + 2][y + 1].printNameEnglish()) == 'n')
                        return true;
                }
            }
        }
        if (x + 2 < 8 && y - 1 >= 0) {
            if (board[x + 2][y - 1] != null) {
                if (board[x + 2][y - 1].isWhite != isWhite) {
                    if (Character.toLowerCase(board[x + 2][y - 1].printNameEnglish()) == 'n')
                        return true;
                }
            }
        }
        if (x - 2 >= 0 && y + 1 < 8) {
            if (board[x - 2][y + 1] != null) {
                if (board[x - 2][y + 1].isWhite != isWhite) {
                    if (Character.toLowerCase(board[x - 2][y + 1].printNameEnglish()) == 'n')
                        return true;
                }
            }
        }
        if (x - 2 >= 0 && y - 1 >= 0) {
            if (board[x - 2][y - 1] != null) {
                if (board[x - 2][y - 1].isWhite != isWhite) {
                    if (Character.toLowerCase(board[x - 2][y - 1].printNameEnglish()) == 'n')
                        return true;
                }
            }
        }
        if (x + 1 < 8 && y + 2 < 8) {
            if (board[x + 1][y + 2] != null) {
                if (board[x + 1][y + 2].isWhite != isWhite) {
                    if (Character.toLowerCase(board[x + 1][y + 2].printNameEnglish()) == 'n')
                        return true;
                }
            }
        }
        if (x + 1 < 8 && y - 2 >= 0) {
            if (board[x + 1][y - 2] != null) {
                if (board[x + 1][y - 2].isWhite != isWhite) {
                    if (Character.toLowerCase(board[x + 1][y - 2].printNameEnglish()) == 'n')
                        return true;
                }
            }
        }
        if (x - 1 >= 0 && y + 2 < 8) {
            if (board[x - 1][y + 2] != null) {
                if (board[x - 1][y + 2].isWhite != isWhite) {
                    if (Character.toLowerCase(board[x - 1][y + 2].printNameEnglish()) == 'n')
                        return true;
                }
            }
        }
        if (x - 1 >= 0 && y - 2 >= 0) {
            if (board[x - 1][y - 2] != null) {
                if (board[x - 1][y - 2].isWhite != isWhite) {
                    if (Character.toLowerCase(board[x - 1][y - 2].printNameEnglish()) == 'n')
                        return true;
                }
            }
        }
        //If the king is not checked at all, return false.
        return false;
    }

    public static boolean moveIsCheckedMoveBack(int x, int y, int incX, int incY, piece[][] board) {
        boolean pieceIsWhite = board[x][y].isWhite;
        Move temp = new Move(x,y,x + incX,y+incY,board[x][y]);
        move(temp,board);
//        board[x + incX][y + incY] = board[x][y];
//        board[x][y] = null;
        if (isChecked(pieceIsWhite, board)) {
            unMakeMove(temp,board);
            return false;
        }
        unMakeMove(temp,board);
//        board[x][y] = board[x + incX][y + incY];

        return true;
    }
}
