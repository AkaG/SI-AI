/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.games.js_ps_player;

import java.util.List;
import java.util.Random;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class JS_PS_Player extends Player {

    private Random random=new Random(0xdeadbeef);

    @Override
    public String getName() {
        return "Jakub Stańczak 122441 Patryk Scheffler 122534";
    }

    @Override
    public Move nextMove(Board b) {
        List<Move> moves = b.getMovesFor(getColor());
        return moves.get(random.nextInt(moves.size()));
    }
}
