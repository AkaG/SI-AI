/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.js_ps_player;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Thread.sleep;

public class JS_PS_Player extends Player {

    private int MAX_EVAL = 1000;

    @Override
    public String getName() {
        return "Jakub Stańczak 122441 Patryk Scheffler 122534";
    }

    @Override
    public Move nextMove(Board b) {
        int depth = b.getSize() < 20 ? 18 - b.getSize()/2 : 15 - b.getSize() / 2;
        depth = max(depth, 3);
        Node ret = alfaBeta(b, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, this.getColor());

        if(ret.getMove() == null){
            Random generator = new Random();
            List<Move> moves = b.getMovesFor(this.getColor());
            ret.setMove(moves.get(generator.nextInt(moves.size())));
        }

        return ret.getMove();
    }

    private int evalState(Board board, Color currPlayer) {
        int result = 0;

        List<Move> maxPlayer = board.getMovesFor(Color.PLAYER1);
        List<Move> minPlayer = board.getMovesFor(Color.PLAYER2);

        result += maxPlayer.size()*2;
        result -= minPlayer.size()*2;

        //max player
        if (currPlayer == Color.PLAYER1) {
            if (maxPlayer.size() == 0)
                return -MAX_EVAL;
            if (minPlayer.size() == 0)
                return MAX_EVAL;

            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if(board.getState(i,j) == Color.PLAYER1) {
//                        result -= sqrt(abs(i - (board.getSize() - (j + 1))));
                        result -= sqrt(abs(board.getSize() - (i + j + 1)));
                    }
                }
            }

        } else { // min player
            if (minPlayer.size() == 0)
                return MAX_EVAL;
            if (maxPlayer.size() == 0)
                return -MAX_EVAL;

            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if(board.getState(i,j) == Color.PLAYER2) {
//                        result += sqrt(abs(board.getSize() - (i + j + 1)));
                        result += sqrt(abs(i - (board.getSize() - (j + 1))));
                    }
                }
            }
        }

        return result;
    }

    /*
        player1 - max
        player2 - min
    */
    private Node alfaBeta(Board board, int depth, int alfa, int beta, Color player) {
        if (isTerminalState(board) || depth == 0)
            return new Node(evalState(board, player), null);

        Board clone = board.clone();
        Node toReturn = new Node();

        if (player == Color.PLAYER1) {
            toReturn.value = Integer.MIN_VALUE;
            for (Move m : board.getMovesFor(player)) {
                clone.doMove(m);

                Node tmp = alfaBeta(clone, depth - 1, alfa, beta, Color.PLAYER2);
                alfa = max(alfa, tmp.value);

                if(tmp.value > toReturn.value || toReturn.getMove() == null){
                    toReturn.value = tmp.value;
                    toReturn.setMove(m);
                }

                if (alfa >= beta)
                    break;
                clone.undoMove(m);
            }
        } else {
            toReturn.value = Integer.MAX_VALUE;
            for (Move m : board.getMovesFor(player)) {
                clone.doMove(m);
                Node tmp = alfaBeta(clone, depth - 1, alfa, beta, Color.PLAYER1);
                beta = min(beta, tmp.value);

                if(tmp.value < toReturn.value || toReturn.getMove() == null){
                    toReturn.value = tmp.value;
                    toReturn.setMove(m);
                }

                if (alfa >= beta)
                   break;
                clone.undoMove(m);
            }
        }

        return toReturn;
    }

    private boolean isTerminalState(Board b) {
        if (b.getMovesFor(Color.PLAYER1).size() == 0 || b.getMovesFor(Color.PLAYER2).size() == 0)
            return true;
        return false;
    }

    public List<Node> createNodeList(List<Move> moves) {
        List<Node> nodes = new ArrayList<>();
        for (Move m : moves)
            nodes.add(new Node(m));
        return nodes;
    }

    class Node {
        public int value = 0;
        private Move move;

        public Node() {
        }

        public Node(Move move) {
            this.move = move;
        }

        public Node(int value, Move move) {
            this.value = value;
            this.move = move;
        }

        public void setMove(Move move) {
            this.move = move;
        }

        public Move getMove() {
            return move;
        }
    }
}
