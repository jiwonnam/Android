package com.example.test1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;


public class Frag_mood extends Fragment{
    public Frag_mood(){

    }
    private View view;
    UserInfo user;
    TableLayout tableLayout;
    int[][] mood;
    TableRow[] tr;
    TextView[][] tv;
    int row, col;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_mood, container, false);
        tableLayout = view.findViewById(R.id.moodTableLayout);

        Bundle get_bundle = getArguments();
        user = get_bundle.getParcelable("userInfo");

        mood = new int[32][13];

        for(int day = 0; day < 32; day++){
            for(int month = 0; month < 13; month++){
                mood[day][month] = 0;
            }
        }

        // Create TableRow, TextView
        tr = new TableRow[32];
        tv = new TextView[32][13];

        for(int i = 0; i < tr.length; i++){
            tr[i] = new TableRow(getContext());
            tr[i].setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
        }

        // Set TextView of first row
        for(int i = 0; i < tv[0].length; i++){
            tv[0][i] = new TextView(getContext());
            tv[0][i].setBackgroundColor(Color.parseColor("#91BFE1"));
            tv[0][i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f
            ));
            tv[0][i].setGravity(Gravity.CENTER);
            tv[0][i].setPadding(1, 1, 1, 1);
        }
        tv[0][0].setText("");
        tv[0][1].setText("J");
        tv[0][1].setText("F");
        tv[0][2].setText("M");
        tv[0][3].setText("A");
        tv[0][4].setText("M");
        tv[0][5].setText("J");
        tv[0][6].setText("J");
        tv[0][7].setText("A");
        tv[0][8].setText("S");
        tv[0][9].setText("O");
        tv[0][10].setText("N");
        tv[0][11].setText("D");

        // set TextView of remains
        for(int i = 1; i < tv.length; i++){
            tv[i][0] = new TextView(getContext());
            tv[i][0].setBackgroundColor(Color.parseColor("#9FB7BA"));
            tv[i][0].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT, 1f
            ));
            tv[i][0].setGravity(Gravity.CENTER);
            tv[i][0].setPadding(1, 1, 1, 1);
            tv[i][0].setText(Integer.toString(i));

            for(int j = 1; j < tv[i].length; j++){
                tv[i][j] = new TextView(getContext());
                tv[i][j].setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1f
                ));
                tv[i][j].setGravity(Gravity.CENTER);
                tv[i][j].setPadding(1, 1, 1, 1);
                int color = mood[i][j];
                    switch (color){
                        case 0:
                            tv[i][j].setBackgroundColor(Color.parseColor("#FFFFFF"));
                            break;
                        case 1:
                            tv[i][j].setBackgroundColor(Color.parseColor("#673AB7"));
                            break;
                        case 2:
                            tv[i][j].setBackgroundColor(Color.parseColor("#3F51B5"));
                            break;
                        case 3:
                            tv[i][j].setBackgroundColor(Color.parseColor("#2196F3"));
                            break;
                        case 4:
                            tv[i][j].setBackgroundColor(Color.parseColor("#E91E63"));
                            break;
                        case 5:
                            tv[i][j].setBackgroundColor(Color.parseColor("#009688"));
                            break;
                }
            }
        }

        for(int i = 0; i < tr.length; i++){
            for(int j = 0; j < tv[i].length; j++){
                tr[i].addView(tv[i][j]);
            }
        }
        for(int i = 0; i< tr.length; i++) {
            tableLayout.addView(tr[i]);

        }
        /*tr.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));*/

        for(row = 0; row < tv.length; row++){
            for(col = 0; col < tv[row].length; col++){
                final int r = row;
                final int c = col;
                tv[r][c].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mood[r][c] = 1;
                        tv[r][c].setBackgroundColor(Color.parseColor("#673AB7"));
                    }
                });

            }
        }
        return view;
    }


}
