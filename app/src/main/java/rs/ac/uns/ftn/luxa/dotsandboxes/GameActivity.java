package rs.ac.uns.ftn.luxa.dotsandboxes;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_game)
public class GameActivity extends AppCompatActivity {

    private static final int LINE_THICKNESS = 45;

    private static final long ANIMATION_DURATION = 1000;

    @Extra
    int n;

    @ViewById
    LinearLayout centerLayout;

    @AfterViews
    void afterViews() {
        Toast.makeText(this, n + "", Toast.LENGTH_SHORT).show();
        final int layerCount = 2 * n + 1;
        for (int i = 0; i < layerCount; i++) {
            final LinearLayout newLayout = new LinearLayout(this);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
            newLayout.setOrientation(LinearLayout.VERTICAL);
            if (i % 2 == 0) {
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                params.width = LINE_THICKNESS;
                for (int j = 0; j < layerCount; j++) {
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
                        newView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ObjectAnimator.ofInt(v, "backgroundColor", Color.LTGRAY, Color.BLUE).setDuration(ANIMATION_DURATION).start();
                                v.setClickable(false);
                            }
                        });
                    }
                    newView.setLayoutParams(viewParams);
                    newLayout.addView(newView);
                }
            } else {
                params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                params.weight = 1;
                for (int j = 0; j < layerCount; j++) {
                    final View newView = new View(this);
                    final LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(0, 0);
                    if (j % 2 == 0) {
                        viewParams.height = LINE_THICKNESS;
                        viewParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        newView.setBackgroundColor(Color.LTGRAY);
                        newView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ObjectAnimator.ofInt(v, "backgroundColor", Color.LTGRAY, Color.RED).setDuration(ANIMATION_DURATION).start();
                                v.setClickable(false);
                            }
                        });
                    } else {
                        viewParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                        viewParams.weight = 1;
                    }
                    newView.setLayoutParams(viewParams);
                    newLayout.addView(newView);
                }
            }
            newLayout.setLayoutParams(params);
            centerLayout.addView(newLayout);

        }
    }

}
