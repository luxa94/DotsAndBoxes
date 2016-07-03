package rs.ac.uns.ftn.luxa.dotsandboxes;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Player;
import rs.ac.uns.ftn.luxa.dotsandboxes.enums.Status;
import rs.ac.uns.ftn.luxa.dotsandboxes.model.GameNode;
import rs.ac.uns.ftn.luxa.dotsandboxes.util.Stack;

@EActivity(R.layout.activity_game)
public class GameActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;

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

    @AfterViews
    void afterViews() {
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
        newNode.getFields()[i][j].setStatus(Status.PLAYERS);
        if (node.score() == newNode.score()) {
            currentPlayer = Player.COMPUTER;
            node = newNode;
            stack.clear();
            stack.push(node);
            computerTurn();
        } else {
            Toast.makeText(this, "Good job! Play once more.", Toast.LENGTH_SHORT).show();
        }
    }

    @Background
    void computerTurn() {
        while (!stack.isEmpty()) {
            final GameNode nextNode = stack.pop();
            final List<GameNode> nodes = nextNode.getNextNodes();
            
        }
    }

    private Player other(Player player) {
        return player == Player.COMPUTER ? Player.PLAYER : Player.COMPUTER;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if(event.getPointerCount() > 1) {
            System.out.println("Multitouch detected!");
            return true;
        }
        else
            return  super.onTouchEvent(event);
    }
}
