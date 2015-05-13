package com.tempapp.marcbeltran.tempapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class Game extends ActionBarActivity {
    ArrayList<Integer> numbers = new ArrayList<Integer>();
    ArrayList<Button> cmdNums = new ArrayList<Button>();
    ArrayList<Button> cmdOpps = new ArrayList<Button>();

    TextView txtCalc;
    Button cmdNum1, cmdNum2, cmdNum3, cmdNum4;
    Button cmdAdd, cmdSub, cmdMult, cmdDiv;
    Button cmdAux1, cmdAux2;
    int pos,count, num1, num2, res;
    ArrayList<Integer> used = new ArrayList<Integer>();
    Boolean active, auxUsed = false,aux2Used=false;
    Random rand = new Random();
    String opperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        txtCalc = (TextView) findViewById(R.id.calc);
        cmdNum1 = (Button)findViewById(R.id.num1);
        cmdNum2 = (Button)findViewById(R.id.num2);
        cmdNum3 = (Button)findViewById(R.id.num3);
        cmdNum4 = (Button)findViewById(R.id.num4);
        cmdAdd = (Button)findViewById(R.id.add);
        cmdSub = (Button)findViewById(R.id.sub);
        cmdMult = (Button)findViewById(R.id.mult);
        cmdDiv = (Button)findViewById(R.id.div);
        cmdAux1 = (Button)findViewById(R.id.aux1);
        cmdAux2 = (Button)findViewById(R.id.aux2);


        active = true;

        cmdNums.add(cmdNum1);
        cmdNums.add(cmdNum2);
        cmdNums.add(cmdNum3);
        cmdNums.add(cmdNum4);
        cmdOpps.add(cmdAdd);
        cmdOpps.add(cmdSub);
        cmdOpps.add(cmdMult);
        cmdOpps.add(cmdDiv);
        createNumbers();

        count = 0;

        cmdAux1.setEnabled(false);
        cmdAux2.setEnabled(false);
        cmdNums.get(0).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + numbers.get(0).toString());
                check("0", pos);
                cmdNums.get(0).setEnabled(false);
            }
        });
        cmdNums.get(1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + numbers.get(1).toString());
                check("1", pos);
                cmdNums.get(1).setEnabled(false);
            }
        });
        cmdNums.get(2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + numbers.get(2).toString());
                check("2", pos);
                cmdNums.get(2).setEnabled(false);
            }
        });
        cmdNums.get(3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + numbers.get(3).toString());
                check("3", pos);
                cmdNums.get(3).setEnabled(false);
            }
        });
        cmdAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + "+");
                check("add", pos);
            }
        });
        cmdSub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + "-");
                check("sub",pos);
            }
        });
        cmdMult.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + "X");
                check("mult",pos);
            }
        });
        cmdDiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + "/");
                check("div",pos);
            }
        });
        cmdAux1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtCalc.setText(txtCalc.getText().toString() + cmdAux1.getText());
                check("aux1",pos);
            }
        });
    }

    public void check(String opp, int position){
        pos++;
        if (opp == "0" || opp == "1" || opp == "2" || opp == "3"){
            if (pos==1){
                num1 = numbers.get(Integer.parseInt(opp));
                used.add(Integer.parseInt(opp));
            } else {
                num2 = numbers.get(Integer.parseInt(opp));
                used.add(Integer.parseInt(opp));
            }
            for(int i=0; i<4; i++) {
                cmdNums.get(i).setEnabled(false);
                cmdAux1.setEnabled(false);
                cmdOpps.get(i).setEnabled(true);
            }
        }
        if(opp=="aux1"){
            if (pos==1){
                num1 = Integer.parseInt(cmdAux1.getText().toString());
                used.add(4);
            } else {
                num2 = Integer.parseInt(cmdAux1.getText().toString());
                used.add(4);
            }
            for(int i=0; i<4; i++) {
                cmdNums.get(i).setEnabled(false);
                cmdAux1.setEnabled(false);
                cmdOpps.get(i).setEnabled(true);
            }
            auxUsed = true;
        }
        if (opp == "add" || opp == "sub" || opp == "mult" || opp == "div"){
            for(int i=0; i<4; i++) {
                if(!used.contains(i)) {
                    cmdNums.get(i).setEnabled(true);
                }
                if (!auxUsed){
                    cmdAux1.setEnabled(true);
                }
                cmdOpps.get(i).setEnabled(false);
            }
            opperation = opp;
        }
        if (pos==3){
            count++;
            if(opperation=="add"){
                res = num1 + num2;
            }
            if(opperation=="sub"){
                res = num1 - num2;
            }
            if(opperation=="mult"){
                res = num1 * num2;
            }
            if(opperation=="div"){
                res = num1 / num2;
            }
            if (count==1) {
                cmdAux1.setText(Integer.toString(res));
            } else{
                cmdAux2.setText(Integer.toString(res));
                aux2Used=true;
            }
            pos = 0;
            cmdAux1.setEnabled(true);
            if (auxUsed){
                int notUsed = -1000000;
                for(int i=0;i<4;i++){
                    if(!used.contains(i)){
                        notUsed = numbers.get(i);
                    }
                }
                if (notUsed==Integer.parseInt(cmdAux1.getText().toString())){
                    txtCalc.setText("DONE");
                    for(int i=0; i<4; i++) {
                        cmdNums.get(i).setEnabled(false);
                        cmdOpps.get(i).setEnabled(false);
                    }
                    cmdAux1.setEnabled(false);
                    cmdAux2.setEnabled(false);
                }
            } else{
                if (aux2Used){
                    if(Integer.parseInt(cmdAux2.getText().toString())==Integer.parseInt(cmdAux1.getText().toString())) {
                        txtCalc.setText("DONE");
                        for(int i=0; i<4; i++) {
                            cmdNums.get(i).setEnabled(false);
                            cmdOpps.get(i).setEnabled(false);
                        }
                        cmdAux1.setEnabled(false);
                        cmdAux2.setEnabled(false);
                    }
                }
            }
            for(int i=0; i<4; i++) {
                cmdNums.get(i).setEnabled(false);
                cmdOpps.get(i).setEnabled(false);
            }
            cmdAux1.setEnabled(false);
            for(int i=0; i<4; i++) {
                if (!used.contains(i)) {
                    cmdNums.get(i).setEnabled(true);
                }
                cmdOpps.get(i).setEnabled(false);
            }
            auxUsed = false;
        }
    }

    public void createNumbers(){
        for(int i=0; i<4; i++) {
            numbers.add(rand.nextInt(9));
            cmdNums.get(i).setText(numbers.get(i).toString());
        }
        for(int i=0; i<4; i++) {
            cmdOpps.get(i).setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
