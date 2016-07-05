package rs.ac.uns.ftn.luxa.dotsandboxes;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.List;

import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Player;
import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Status;
import rs.ac.uns.ftn.luxa.dotsandboxes.model.GameNode;
import rs.ac.uns.ftn.luxa.dotsandboxes.util.IntIntTuple;
import rs.ac.uns.ftn.luxa.dotsandboxes.util.Stack;

@EActivity(R.layout.activity_game)
public class GameActivity extends AppCompatActivity {

    private static int MAX_DEPTH = 3;

    private static final int LINE_THICKNESS = 45;

    private static final long ANIMATION_DURATION = 1000;

    private View[][] views;

    private Player currentPlayer;

    @Bean
    Stack<GameNode> stack;

    GameNode node;

    @Extra
    int n;

    @ViewById
    LinearLayout centerLayout;

    @ViewById
    Button restart;

    @AfterExtras
    void setMaxDepth() {
        MAX_DEPTH = 5 - n;
    }

    @Click({R.id.restart})
    @AfterViews
    void afterViews() {
        restart.setEnabled(false);
        centerLayout.removeAllViews();
        currentPlayer = Player.PLAYER;
        node = new GameNode(n);
        node.setPlayer(Player.PLAYER);

        final int nn = 2 * n + 1;
        views = new View[nn][nn];

        for (int i = 0; i < nn; i++) {
            final LinearLayout newLayout = new LinearLayout(this);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            newLayout.setOrientation(LinearLayout.VERTICAL);

            if (i % 2 == 0) {
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                params.width = LINE_THICKNESS;
                for (int j = 0; j < nn; j++) {
                    final View newView = new View(this);
                    final LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(0, 0);
                    if (j % 2 == 0) {
                        viewParams.height = LINE_THICKNESS;
                        viewParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        newView.setBackgroundColor(Color.BLACK);
                    } else {
                        viewParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        viewParams.weight = 1;
                        newView.setBackgroundColor(Color.LTGRAY);
                        final int finalI = i;
                        final int finalJ = j;
                        newView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (currentPlayer == Player.PLAYER) {
                                    playerTurn(finalI, finalJ);
                                }
                                return true;
                            }
                        });
                    }
                    newView.setLayoutParams(viewParams);
                    newLayout.addView(newView);
                    views[i][j] = newView;
                }
            } else {
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                params.weight = 1;
                for (int j = 0; j < nn; j++) {
                    final View newView = new View(this);
                    final LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(0, 0);
                    if (j % 2 == 0) {
                        viewParams.height = LINE_THICKNESS;
                        viewParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        newView.setBackgroundColor(Color.LTGRAY);
                        final int finalI = i;
                        final int finalJ = j;
                        newView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (currentPlayer == Player.PLAYER) {
                                    playerTurn(finalI, finalJ);
                                }
                                return true;
                            }
                        });
                    } else {
                        viewParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        viewParams.weight = 1;
                        newView.setAlpha(.5f);
                    }
                    newView.setLayoutParams(viewParams);
                    newLayout.addView(newView);
                    views[i][j] = newView;
                }
            }
            newLayout.setLayoutParams(params);
            centerLayout.addView(newLayout);
        }
    }

    private void activate(int i, int j, Player player) {
        Log.e("Activate", String.format("%d, %d, %s", i, j, player));

        ObjectAnimator.ofInt(views[i][j], "backgroundColor", Color.LTGRAY, player == Player.PLAYER ? Color.BLUE : Color.RED).setDuration(ANIMATION_DURATION).start();
        views[i][j].setOnTouchListener(null);
    }

    private void playerTurn(int i, int j) {
        activate(i, j, Player.PLAYER);

        final GameNode newNode = new GameNode(node);
        newNode.setPlayer(Player.PLAYER);
        newNode.getFields()[j][i].setStatus(Status.PLAYERS);
        final GameNode oldNode = node;
        node = newNode;

        for (IntIntTuple t : node.findDifferentFields(oldNode)) {
            activate(t.getSecond(), t.getFirst(), Player.PLAYER);
        }

        Log.e("NODE", node.toString());
        if (!checkForEnd()) {
            if (oldNode.completedFields() == newNode.completedFields()) {
                currentPlayer = Player.COMPUTER;
                computerTurn();
            } else {
                Toast.makeText(this, "Good job! Play once more.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkForEnd() {
        Log.e("SCORE", node.completedFields() + " " + GameNode.maxScore);
        final boolean finished = node.completedFields() == GameNode.maxScore;

        if (finished) {
            enableRestart();
            if (node.getScoreDifference() > 0) {
                lost();
            } else if (node.getScoreDifference() == 0) {
                draw();
            } else {
                won();
            }
        }
        return finished;
    }

    @UiThread
    void enableRestart() {
        restart.setEnabled(true);
    }

    @Background
    void computerTurn() {
        final List<GameNode> nodes = node.getNextNodes(Status.COMPUTERS);

        if (nodes.size() > 0) {
            for (GameNode gameNode : nodes) {
                calculateScore(gameNode, Status.COMPUTERS, MAX_DEPTH);
            }
        }

        final GameNode newNode = findMaxNewNode(nodes);
        final GameNode oldNode = node;
        node = newNode;
        setActive(oldNode);

        if (!checkForEnd()) {
            if (oldNode.completedFields() != newNode.completedFields()) {
                computerTurn();
            } else {
                yourTurn();
                currentPlayer = Player.PLAYER;
            }
        }
    }

    @UiThread
    void lost() {
        Toast.makeText(this, "Your LOST.", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void won() {
        Toast.makeText(this, "Your WON! Congratulations!", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void draw() {
        Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void yourTurn() {
        Toast.makeText(this, "Your turn.", Toast.LENGTH_SHORT).show();
    }

    @UiThread(delay = 200)
    void setActive(GameNode newNode) {
        final List<IntIntTuple> free = node.findDifferentEdges(newNode);
        for (IntIntTuple t : free) {
            activate(t.getSecond(), t.getFirst(), Player.COMPUTER);
        }

        for (IntIntTuple t : node.findDifferentFields(newNode)) {
            activate(t.getSecond(), t.getFirst(), Player.COMPUTER);
        }
    }

    void calculateScore(GameNode node, Status player, int depth) {
        final List<GameNode> nodes = node.getNextNodes(player);
        if (depth > 0 && nodes.size() > 0) {
            for (GameNode gameNode : nodes) {
                if (gameNode.completedFields() > node.completedFields()) {
                    calculateScore(gameNode, player, depth - 1);
                } else {
                    calculateScore(gameNode, other(player), depth - 1);
                }
            }
            if (player == Status.COMPUTERS) {
                node.setScore(findMax(nodes).getScore());
            } else {
                node.setScore(findMin(nodes).getScore());
            }
        } else {
            node.setScore(node.getScoreAdjusted());
        }
    }

    private Status other(Status status) {
        return status == Status.PLAYERS ? Status.COMPUTERS : Status.PLAYERS;
    }

    private GameNode findMax(List<GameNode> nodes) {
        GameNode n = null;
        Collections.shuffle(nodes);
        if (nodes.size() > 0) {
            n = nodes.get(0);
            for (GameNode gameNode : nodes) {
                if (n.getScore() < gameNode.getScore()) {
                    n = gameNode;
                }
            }
        }
        return n;
    }

    private GameNode findMaxNewNode(List<GameNode> nodes) {
        GameNode n = null;
        Collections.shuffle(nodes);
        if (nodes.size() > 0) {
            n = nodes.get(0);
            for (GameNode gameNode : nodes) {
                if (n.getScoreDifference() < gameNode.getScoreDifference()) {
                    n = gameNode;
                }
            }
        }
        return n;
    }

    private GameNode findMin(List<GameNode> nodes) {
        GameNode n = null;
        Collections.shuffle(nodes);
        if (nodes.size() > 0) {
            n = nodes.get(0);
            for (GameNode gameNode : nodes) {
                if (n.getScore() > gameNode.getScore()) {
                    n = gameNode;
                }
            }
        }

        return n;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            System.out.println("Multitouch detected!");
            return true;
        } else
            return super.onTouchEvent(event);
    }
}
