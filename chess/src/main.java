import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

public class main extends PApplet {
    static String position = "";
    static PImage piecePNG;
    static PImage wKing;
    static PImage wQueen;
    static PImage wBishop;
    static PImage wKnight;
    static PImage wRook;
    static PImage wPawn;
    static PImage bKing;
    static PImage bQueen;
    static PImage bBishop;
    static PImage bKnight;
    static PImage bRook;
    static PImage bPawn;
    static HashMap<Long,evaluation> allEvals = new HashMap<Long, evaluation>();
    boolean mouseDown = false;
    public static int[] enPassant = {-1,-1};
    int locX = -1;
    int locY = -1;
    ArrayList<Move> possibleMoves = new ArrayList<>();
    static boolean WCK = false, WCQ = false, BCK = false, BCQ = false;
    private static piece[][] board = new piece[8][8];
    public static boolean isWhitesMove;
    private static int halfmove, fullmove;
    private static int w = 700;
    private static int h = 700;
    private static Zobrist zobrist;

    public static void main(String[] args) {
        zobrist  = new Zobrist();
        ai.board=board;
        PApplet.main("main"); //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        position=loadFENPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        position=saveFENPosition();
        System.out.println(position);
        System.out.println("It is " + (isWhitesMove? "White's " : "Black's ") + "move");
    }

    public void settings() {
        size(w, h);
    }


    public void setup(){
        // 1280, 427
        noStroke();
        piecePNG = loadImage("chess/sprites.png");
        wKing = piecePNG.get(0,0,213,213);
        wQueen = piecePNG.get(214,0,213,213);
        wBishop = piecePNG.get(427,0,213,213);
        wKnight = piecePNG.get(640,0,213,213);
        wRook = piecePNG.get(853,0,213,213);
        wPawn = piecePNG.get(1066,0,213,213);
        bKing = piecePNG.get(0,213,213,213);
        bQueen = piecePNG.get(214,213,213,213);
        bBishop = piecePNG.get(427,213,213,213);
        bKnight = piecePNG.get(640,213,213,213);
        bRook = piecePNG.get(853,213,213,213);
        bPawn = piecePNG.get(1066,213,213,213);
    }

    public void draw(){
        background(255);

        drawBoard();
        fill(0,255,0,75);
        if(mouseDown){
            if(board[locX][locY]!=null) {
                image(board[locX][locY].printName(), mouseX - (w / 16), mouseY - (h / 16), w / 8, h / 8);
                drawMoves(possibleMoves);
            }
        }
    }

    public void mousePressed(){
        mouseDown=true;
        locX = findMouseX(0,w);
        locY = findMouseY(0,h);
        if(board[locX][locY]!=null) {
            fill(0, 150, 0, 100);
            board[locX][locY].show=false;
            possibleMoves = getPossibleMoves(locX,locY,isWhitesMove);
        }
    }

    public void mouseReleased(){
        mouseDown=false;

        int locXtemp = findMouseX(0,w);
        int locYtemp = findMouseY(0,h);
        if(board[locX][locY]!=null){
            board[locX][locY].show=true;
            for (int i = 0; i < possibleMoves.size(); i++) {
                //Make sure it works with black too
                if (possibleMoves.get(i).postX == locXtemp && possibleMoves.get(i).postY == locYtemp) {
                    piece.move(new Move(locX,locY,locXtemp,locYtemp,board[locX][locY]),board);
                    isWhitesMove=!isWhitesMove;
                    System.out.println(saveFENPosition());
                    System.out.println("It is " + (isWhitesMove? "White's " : "Black's ") + "move");
//                    System.out.println(generateMoves(false).size());
//                    ai.search(8, isWhitesMove, -10000000, 10000000, true);
//                    if(ai.bestMove!=null) {
//                        piece.move(ai.bestMove, board);
//                        System.out.println(ai.bestMove.preX + ", " + ai.bestMove.preY + ", " + ai.bestMove.postX + ", " + ai.bestMove.postY);
//                    }
//                    else{
//                        System.out.println("I resign");
//                        System.exit(0);
//                    }
//                    isWhitesMove=!isWhitesMove;
//                    System.out.println("It is " + (isWhitesMove? "White's " : "Black's ") + "move");
                    break;
                }
            }
        }
//        System.out.println(piece.isChecked(false, board));
        locX = -1;
        locY = -1;
        possibleMoves=new ArrayList<>();
    }

    public static String saveFENPosition(){
        String FEN = "";
        for (int i = 0; i < 8; i++) {
            int count = 0;
            for (int j = 0; j < 8; j++) {
                if(board[j][i]==null){
                    count++;
                    if(j==4&&i==0){
                        BCK=false;
                        BCQ=false;
                    }
                    else if(j==4&&i==7){
                        WCK=false;
                        WCQ=false;
                    }
                    else if(j==7&&i==7){
                        WCK=false;
                    }
                    else if(j==0&&i==7){
                        WCQ=false;
                    }
                    else if(j==7&&i==0){
                        BCK=false;
                    }
                    else if(j==0&&i==0){
                        BCQ=false;
                    }
                }
                else{
                    if(count>0){
                        FEN+=count;
                        count=0;
                    }
                    FEN+=board[j][i].printNameEnglish();
                    if(WCK&&j==7&&i==7&&board[j][i].printNameEnglish()!='R'){
                        WCK=false;
                    }
                    else if(WCQ&&j==0&&i==7&&board[j][i].printNameEnglish()!='R'){
                        WCQ=false;
                    }
                    else if(BCK&&j==7&&i==0&&(board[j][i]==null||board[j][i].printNameEnglish()!='r')){
                        BCK=false;
                    }
                    else if(BCQ&&j==0&&i==0&&(board[j][i]==null||board[j][i].printNameEnglish()!='r')){
                        BCQ=false;
                    }
                }
            }
            if(count>0){
                FEN+=count;
            }
            if(i!=7){
                FEN+="/";
            }

        }
        FEN+=" ";
        FEN+=isWhitesMove? 'w' : 'b';
        FEN+=" ";
        if(WCK){
            FEN+="K";
        }
        if(WCQ){
            FEN+="Q";
        }
        if(BCK){
            FEN+="k";
        }
        if(BCQ){
            FEN+="q";
        }
        if(FEN.charAt(FEN.length()-1)==' '){
            FEN+="-";
        }
        FEN += " ";
        if(enPassant[0]!=-1) {
            FEN += (char) (enPassant[0] + 97);
            FEN += (char) (enPassant[1] + 48);
        }
        else{
            FEN+="-";
        }
        FEN += " " + halfmove + " " + fullmove;
        return FEN;
    }

    public static String loadFENPosition(String FEN){
        //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        String[] metaData = FEN.split(" ");
        String[] position = metaData[0].split("/");
        for (int i = 0; i < position.length; i++) {
            //New Line
            int positionInLine = -1;
            for (int j = 0; j < position[i].length(); j++) {
                //Next char in the line
                positionInLine++;
                if(Character.isDigit(position[i].charAt(j))){
                    //If it is a number
                    int num = Character.getNumericValue(position[i].charAt(j));
                    positionInLine+=num-1;
                }
                else{
                    //If it isn't a number
                    char pieceName = Character.toLowerCase(position[i].charAt(j));
                    int pieceVal;
                    if(pieceName=='p')
                        pieceVal=0;
                    else if(pieceName=='n')
                        pieceVal=1;
                    else if(pieceName=='b')
                        pieceVal=2;
                    else if(pieceName=='r')
                        pieceVal=3;
                    else if(pieceName=='q')
                        pieceVal=4;
                    else {
//                        if(Character.isUpperCase(position[i].charAt(j)))
//                            piece.kingCoords[0]= new int[]{positionInLine,i};
//                        else
//                            piece.kingCoords[1]= new int[]{positionInLine,i};
                        pieceVal = 5;
                    }
                    if(Character.isUpperCase(position[i].charAt(j))){
                        piece.whitePieces.add(new piece(positionInLine, i, pieceVal, Character.isUpperCase(position[i].charAt(j))));
                        board[positionInLine][i]=piece.whitePieces.get(piece.whitePieces.size()-1);
                    }
                    else{
                        piece.blackPieces.add(new piece(positionInLine, i, pieceVal, Character.isUpperCase(position[i].charAt(j))));
                        board[positionInLine][i]=piece.blackPieces.get(piece.blackPieces.size()-1);
                    }

                }
            }
        }
        if(metaData.length>1) {
            //w for whites move b for blacks move
            if (metaData[1].equals("w")) {
                isWhitesMove = true;
            } else {
                isWhitesMove = false;
            }
            //K Q white can castle king/queen side, k q black can castle king/queen side, - for nothing
            if (!metaData[2].equals("-")) {
                for (int i = 0; i < metaData[2].length(); i++) {
                    if(metaData[2].charAt(i)=='K'){
                        WCK=true;
                    } else if(metaData[2].charAt(i)=='Q'){
                        WCQ = true;
                    } else if(metaData[2].charAt(i)=='k'){
                        BCK = true;
                    } else if(metaData[2].charAt(i)=='q'){
                        BCQ = true;
                    }
                }
            }
            //En Passant possibility, x6 or x2 if there is an en passant possibility, - for none
            if(!metaData[3].equals("-")){
                enPassant[0] = (int) metaData[3].charAt(0) - 97;
                enPassant[1] = (int) metaData[3].charAt(1) - 48;
            }
            //Halfmove clock: This is the number of halfmoves since the last capture or pawn advance. The reason for this field is that the value is used in the fifty-move rule.
            halfmove = Integer.parseInt(metaData[4]);
            //Fullmove number: The number of the full move. It starts at 1, and is incremented after Black's move.
            fullmove = Integer.parseInt(metaData[5]);
        }
        else{
            isWhitesMove = true;
            halfmove = -1;
            fullmove = -1;
        }
        return FEN;
    }



    public int findMouseX(int leftBound, int rightBound){
        if(mouseX>rightBound||mouseX<leftBound){
            return -1;
        }
        int avg = (rightBound+leftBound)/2;
        if(mouseX>avg){
            avg = (avg+rightBound)/2;
            if(mouseX>avg){
                avg = (avg+rightBound)/2;
                if(mouseX>avg){
                    return 7;
                }
                else{
                    return 6;
                }
            }
            else{
                avg = (avg+((rightBound+leftBound)/2))/2;
                if(mouseX>avg){
                    return 5;
                }
                else{
                    return 4;
                }
            }
        }
        else{
            avg = (avg+leftBound)/2;
            if(mouseX<avg){
                avg = (avg+leftBound)/2;
                if(mouseX<avg){
                    return 0;
                }
                else{
                    return 1;
                }
            }
            else{
                avg = (avg+((rightBound+leftBound)/2))/2;
                if(mouseX<avg){
                    return 2;
                }
                else{
                    return 3;
                }
            }
        }
    }

    public int findMouseY(int lowBound, int highBound){
        if(mouseY>highBound||mouseY<lowBound){
            return -1;
        }
        int avg = (highBound+lowBound)/2;
        if(mouseY>avg){
            avg = (avg+highBound)/2;
            if(mouseY>avg){
                avg = (avg+highBound)/2;
                if(mouseY>avg){
                    return 7;
                }
                else{
                    return 6;
                }
            }
            else{
                avg = (avg+((highBound+lowBound)/2))/2;
                if(mouseY>avg){
                    return 5;
                }
                else{
                    return 4;
                }
            }
        }
        else{
            avg = (avg+lowBound)/2;
            if(mouseY<avg){
                avg = (avg+lowBound)/2;
                if(mouseY<avg){
                    return 0;
                }
                else{
                    return 1;
                }
            }
            else{
                avg = (avg+((highBound+lowBound)/2))/2;
                if(mouseY<avg){
                    return 2;
                }
                else{
                    return 3;
                }
            }
        }
    }

    public static ArrayList<Move> generateMoves(boolean isWhite){
        ArrayList<Move> totalMoves = new ArrayList<>();
        ArrayList<piece> targetList = isWhite? piece.whitePieces : piece.blackPieces;

        for (int i = 0; i < targetList.size(); i++) {
            if(targetList.get(i).isAlive){
                totalMoves.addAll(getPossibleMoves(targetList.get(i).x,targetList.get(i).y, isWhite));
            }
        }
        return totalMoves;
    }

    public void drawBoard(){
        double length = (double)w/8;
        double height = (double)h/8;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if((i+j)%2==0){
                    fill(250, 223, 155);
                }
                else{
                    fill(158, 117, 16);
                }
                rect((int) length*j,(int) height*i, (int) (length*j+w/8),(int) (height*i+h/8));
            }
        }
        for (int i = 0; i < piece.whitePieces.size(); i++) {
            int x = piece.whitePieces.get(i).x;
            int y = piece.whitePieces.get(i).y;
            if(board[x][y]!=null&&board[x][y].show&&board[x][y].isAlive)
                image(board[x][y].printName(),(int) length*x,(int) height*y, (int) (w/8),(int) (h/8));
            //System.out.print(board[j][i]!=null? board[j][i].printName() : " ");
        }
        for (int i = 0; i < piece.blackPieces.size(); i++) {
            int x = piece.blackPieces.get(i).x;
            int y = piece.blackPieces.get(i).y;
            if(board[x][y]!=null&&board[x][y].show&&board[x][y].isAlive)
                image(board[x][y].printName(),(int) length*x,(int) height*y, (int) (w/8),(int) (h/8));
            //System.out.print(board[j][i]!=null? board[j][i].printName() : " ");
        }
    }

    public static ArrayList<Move> getPossibleMoves(int x, int y, boolean isWhite){
        if (board[x][y].printName() == (isWhite? wKing : bKing)) {
            return piece.kingMove(x, y, board);
        } else if (board[x][y].printName() == (isWhite? wQueen : bQueen)) {
            return piece.queenMove(x, y, board);
        } else if (board[x][y].printName() == (isWhite? wRook : bRook)) {
            return piece.rookMove(x, y, board);
        } else if (board[x][y].printName() == (isWhite? wBishop : bBishop)) {
            return piece.bishopMove(x, y, board);
        } else if (board[x][y].printName() == (isWhite? wKnight : bKnight)) {
            return piece.knightMove(x, y, board);
        } else if (board[x][y].printName() == (isWhite? wPawn : bPawn)) {
            return piece.pawnMove(x, y, board, enPassant);
        }
        return new ArrayList<Move>();
    }

    public boolean isCheckMated(boolean isWhite){
        return generateMoves(isWhite).size()!=0;
    }

//    public ArrayList<piece> getAllPossibleMoves(boolean isWhite){
//        ArrayList<piece> pieces = isWhite? piece.whitePieces : piece.blackPieces;
//        ArrayList<piece> possibleMoves = new ArrayList<>();
//        for (int i = 0; i < pieces.size(); i++) {
//            if(pieces.get(i).isAlive){
//                //possibleMoves.addAll(getPossibleMoves(pieces.get(i).x,pieces.get(i).y));
//            }
//        }
//    }

    public void drawMoves(ArrayList<Move> possibleMoves){
        for (int i = 0; i < possibleMoves.size(); i++) {
            Move move = possibleMoves.get(i);
            if(board[move.postX][move.postY]!=null)
                rect(move.postX * (w / 8), move.postY * (h / 8), w / 8, h / 8);
            else
                ellipse(move.postX * (w / 8) + w/16,move.postY * (h / 8) + h/16,w/24,h/24);
        }
    }
}
