package jp.ac.it_college.std.s14007.tetris.tetris;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity implements Board.Callback {
    private Board board;
    private Handler handler;
    private int current;
    private int pastRecord = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_main);

        loadBestScore();
        Bitmap srcImage = BitmapFactory.decodeResource(getResources(),
                android.R.drawable.ic_media_play);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap fallImage = Bitmap.createBitmap(srcImage, 0, 0,
                srcImage.getWidth(), srcImage.getHeight(), matrix, true);
        ((ImageButton)findViewById(R.id.fall)).setImageBitmap(fallImage);

        matrix.postRotate(90);
        Bitmap leftImage = Bitmap.createBitmap(srcImage, 0, 0,
                srcImage.getWidth(), srcImage.getHeight(), matrix, true);
        ((ImageButton)findViewById(R.id.left)).setImageBitmap(leftImage);

        board = (Board)findViewById(R.id.board);
        board.setCallback(this);

    }

    public void gameButtonClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                board.send(Input.Left);
                break;
            case R.id.right:
                board.send(Input.Right);
                break;
            case R.id.fall:
                board.send(Input.Down);
                break;
            case R.id.rotate:;
                board.send(Input.Rotate);
                break;
        }
    }

    @Override
    public void scoreAdd(final int score) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView scoreView = (TextView)findViewById(R.id.score);
//                current = Integer.parseInt(scoreView.getText().toString());
                current += score;
                scoreView.setText(String.valueOf(current));
            }
        });
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void onGameOver() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveBestScore() {


        if (pastRecord < current) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    TextView bestScoreView = (TextView)findViewById(R.id.best_score);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("bestScore", current);
                    editor.commit();
                    bestScoreView.setText(String.valueOf(preferences.getInt("bestScore", 0)));
                }
            });

        }
    }

    private void loadBestScore() {
        TextView bestScoreView = (TextView)findViewById(R.id.best_score);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        pastRecord = preferences.getInt("bestScore", 0);
        if (pastRecord != 0) {
            String s = String.valueOf(pastRecord);
            bestScoreView.setText(s);
        }
    }
}
