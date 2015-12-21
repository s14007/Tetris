package jp.ac.it_college.std.s14007.tetris.tetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity
        implements Board.Callback {
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
        ((ImageButton) findViewById(R.id.fall)).setImageBitmap(fallImage);

        matrix.postRotate(90);
        Bitmap leftImage = Bitmap.createBitmap(srcImage, 0, 0,
                srcImage.getWidth(), srcImage.getHeight(), matrix, true);
        ((ImageButton)findViewById(R.id.left)).setImageBitmap(leftImage);

        board = (Board)findViewById(R.id.board);
        board.setCallback(this);

    }

    public void gameButtonClick(View v) {
        final BooleanWrapper isContinue = new BooleanWrapper(false);
        switch (v.getId()) {
            case R.id.left:
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        isContinue.value = true;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!isContinue.value) {
                                    return;
                                }
                                board.send(Input.Left);
                                handler.postDelayed(this, 100);
                            }
                        });
                        return true;
                    }
                });

                v.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            isContinue.value = false;
                        }
                        return false;
                    }
                });
                board.send(Input.Left);
                break;

            case R.id.right:
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        isContinue.value = true;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!isContinue.value) {
                                    return;
                                }
                                board.send(Input.Right);
                                handler.postDelayed(this, 100);
                            }
                        });
                        return false;
                    }
                });

                v.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            isContinue.value = false;
                        }
                        return false;
                    }
                });

                board.send(Input.Right);
                break;
            case R.id.fall:
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        isContinue.value = true;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!isContinue.value) {
                                    return;
                                }
                                board.send(Input.Down);
                                handler.postDelayed(this, 100);
                            }
                        });
                        return false;
                    }
                });

                v.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            isContinue.value = false;
                        }
                        return false;
                    }
                });

                board.send(Input.Down);
                break;
            case R.id.rotate:;
                board.send(Input.Rotate);
                break;
        }
    }

    private static class BooleanWrapper {
        public boolean value;

        public BooleanWrapper(boolean value) {
           this.value = value;
        }
    }

    @Override
    public void scoreAdd(final int score) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                TextView scoreView = (TextView) findViewById(R.id.score);
//                current = Integer.parseInt(scoreView.getText().toString());
                current += score;
                scoreView.setText(String.valueOf(current));
            }
        });
    }

    public void onGameOver() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                resetScore();
                Toast.makeText(MainActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveBestScore() {
        if (pastRecord < current) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences preferences = getSharedPreferences("dataSave", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("bestScore", current);
                    Toast.makeText(MainActivity.this, "New record!", Toast.LENGTH_SHORT).show();
                    editor.apply();
                    loadBestScore();
                }
            });
        }
    }

    public void resetScore() {
        current = 0;
        TextView scoreView = (TextView)findViewById(R.id.score);
        scoreView.setText(String.valueOf(current));
    }

    private void loadBestScore() {
        TextView bestScoreView = (TextView)findViewById(R.id.best_score);
        SharedPreferences preferences = getSharedPreferences("dataSave", Context.MODE_PRIVATE);
        pastRecord = preferences.getInt("bestScore", 0);
        bestScoreView.setText(String.valueOf(pastRecord));
    }
}
