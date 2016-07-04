package rs.ac.uns.ftn.luxa.dotsandboxes;

import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final Integer[] ints = {2, 3};

    @ViewById
    Spinner spinner;

    @AfterViews
    void initSpinner() {
        spinner.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, ints));
    }

    @Click
    void startGame() {
        GameActivity_.intent(this).n((Integer) spinner.getSelectedItem()).start();
    }
}
