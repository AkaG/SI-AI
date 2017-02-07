/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.js_ps_player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Thread.sleep;

public class JS_PS_Player extends Player {

    private Random random=new Random(0xdeadbeef);

    @Override
    public String getName() {
        return "Jakub Stańczak 122441 Patryk Scheffler 122534";
    }

    @Override
    public Move nextMove(Board b) {
        List<Node> nodes = createNodeList(b.getMovesFor(getColor()));
        return chooseBest(nodes, b);
    }

    private Move chooseBest(List<Node> nodes, Board board){
        for(Node n : nodes)
            n.value = alfaBeta(board, 1, 0,0,this.getColor());
        return maxValueNode(nodes).getMove();
    }

    private Node maxValueNode(List<Node> nodes){
        Node toReturn = nodes.get(0);
        for (Node n : nodes)
            if(toReturn.value < n.value)
                toReturn = n;
        return toReturn;
    }

    private int alfaBeta(Board board, int depth, int alfa, int beta, Color player){
        if(isTerminalState(board) || depth == 0)
            return evalState(board, player);

        Board clone = board.clone();
        if(player == this.getColor()){
            for (Move m : board.getMovesFor(player)){
                clone.doMove(m);
                alfa = max(alfa, alfaBeta(clone, depth-1, alfa, beta, player == Color.PLAYER1 ? Color.PLAYER2 : Color.PLAYER1));
                if(alfa >= beta)
                    return beta;
                clone.undoMove(m);
            }
            return alfa;
        }else {
            for (Move m : board.getMovesFor(player)){
                clone.doMove(m);
                beta = min(alfa, alfaBeta(clone, depth-1, alfa, beta, player == Color.PLAYER1 ? Color.PLAYER2 : Color.PLAYER1));
                if(alfa >= beta)
                    return alfa;
                clone.undoMove(m);
            }
            return beta;
        }
    }

    private int evalState(Board b, Color player){
        int result = 0;

        List<Move> p1 = b.getMovesFor(this.getColor());
        List<Move> p2 = this.getColor() == Color.PLAYER1 ? b.getMovesFor(Color.PLAYER2) :  b.getMovesFor(Color.PLAYER1);

        return result;
    }

    private boolean isTerminalState(Board b){
        if(b.getMovesFor(Color.PLAYER1).size() == 0 || b.getMovesFor(Color.PLAYER2).size() == 0)
            return true;
        return false;
    }

    public List<Node> createNodeList(List<Move> moves){
        List<Node> nodes = new ArrayList<>();
        for(Move m : moves)
            nodes.add(new Node(m));
        return nodes;
    }

    class Node {
        private Move move;
        public int value = 0;

        public Node(Move move) {
            this.move = move;
        }

        public Move getMove() {
            return move;
        }
    }
}
