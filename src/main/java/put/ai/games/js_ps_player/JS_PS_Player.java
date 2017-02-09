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

public class JS_PS_Player extends Player {

    private int MAX_EVAL = 10000;
    private int MOVE_COST_POW = 2;
    private int DIAGONAL_DIST_POW = 2;

    @Override
    public String getName() {
        return "Jakub Stańczak 122441 Patryk Scheffler 122534";
    }

    @Override
    public Move nextMove(Board b) {
        int depth = b.getSize() < 12 ? 18 - b.getSize() : 12 - b.getSize() / 2;
        depth = max(depth, 3);
        Node ret = alfaBeta(b, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, this.getColor());

        if (ret.getMove() == null) {
            Random generator = new Random();
            List<Move> moves = b.getMovesFor(this.getColor());
            ret.setMove(moves.get(generator.nextInt(moves.size())));
        }

        return ret.getMove();
    }

    private long evalState(Board board, Color currPlayer) {
        long result = 0;

        List<Move> maxPlayer = board.getMovesFor(Color.PLAYER1);
        List<Move> minPlayer = board.getMovesFor(Color.PLAYER2);

        //max player
        if (currPlayer == Color.PLAYER1) {
            if (maxPlayer.size() == 0)
                result -= MAX_EVAL;

            result -= pow(minPlayer.size(), MOVE_COST_POW);

            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if (board.getState(i, j) == Color.PLAYER1) {
                        result -= pow(abs(board.getSize() - (i + j + 1)), DIAGONAL_DIST_POW);
                    }
                }
            }

        } else { // min player
            if (minPlayer.size() == 0)
                return MAX_EVAL;

            result += pow(maxPlayer.size(), MOVE_COST_POW);

            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if (board.getState(i, j) == Color.PLAYER2) {
                        result += pow(abs((board.getSize() - (i+1)) - j), DIAGONAL_DIST_POW);
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
    private Node alfaBeta(Board board, int depth, long alfa, long beta, Color player) {
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

                if (tmp.value > toReturn.value || toReturn.getMove() == null) {
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

                if (tmp.value < toReturn.value || toReturn.getMove() == null) {
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
        return b.getMovesFor(Color.PLAYER1).size() == 0 || b.getMovesFor(Color.PLAYER2).size() == 0;
    }

    class Node {
        long value = 0;
        private Move move;

        Node() {
        }

        public Node(Move move) {
            this.move = move;
        }

        Node(long value, Move move) {
            this.value = value;
            this.move = move;
        }

        Move getMove() {
            return move;
        }

        void setMove(Move move) {
            this.move = move;
        }
    }
}
