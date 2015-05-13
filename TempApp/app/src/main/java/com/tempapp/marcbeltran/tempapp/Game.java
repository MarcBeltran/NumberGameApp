package com.tempapp.marcbeltran.tempapp;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
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

    //*********SERVER CODE************

    private TextView serverStatus;

    public static String SERVERIP = "10.0.2.15";
    public static final int SERVERPORT = 8080;
    private Handler handler = new Handler();
    private ServerSocket serverSocket;

    //*********CLIENT CODE**********
    private EditText serverIp;
    private Button connectPhones;
    private String serverIpAddress = "";
    private boolean connected = false;

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

        //************ SERVER CODE ***********

        serverStatus = (TextView) findViewById(R.id.server_status);
        SERVERIP = getLocalIpAddress();
        //SERVERIP = getIpAddress();
        Log.d("IP: ",SERVERIP);
        Thread fst = new Thread(new ServerThread());
        fst.start();

        //************ CLIENT CODE ***********

        serverIp = (EditText) findViewById(R.id.server_ip);
        connectPhones = (Button) findViewById(R.id.connect_phones);
        connectPhones.setOnClickListener(connectListener);

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

    //*********** SERVER CODE ***********

    public class ServerThread implements Runnable {

        public void run() {
            try {
                if (SERVERIP != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            serverStatus.setText("Listening on IP: " + SERVERIP);
                        }
                    });
                    serverSocket = new ServerSocket(SERVERPORT);
                    while (true) {
                        // LISTEN FOR INCOMING CLIENTS
                        Socket client = serverSocket.accept();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                serverStatus.setText("Connected.");
                            }
                        });

                        try {
                            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            String line = null;
                            while ((line = in.readLine()) != null) {
                                Log.d("ServerActivity", line);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // DO WHATEVER YOU WANT TO THE FRONT END
                                        // THIS IS WHERE YOU CAN BE CREATIVE
                                    }
                                });
                            }
                            break;
                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    serverStatus.setText("Oops. Connection interrupted. Please reconnect your phones.");
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            serverStatus.setText("Couldn't detect internet connection.");
                        }
                    });
                }
            } catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        serverStatus.setText("Error");
                    }
                });
                e.printStackTrace();
            }
        }
    }

    // GETS THE IP ADDRESS OF YOUR PHONE'S NETWORK
    private String getLocalIpAddress() {
        String ipv4;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = inetAddress.getHostAddress())) { return ipv4; }
                }
            }
        } catch (SocketException ex) {
            Log.e("ServerActivity", ex.toString());
        }
        return null;
    }

    public String getIpAddress() {
        String ip = "0.0.0.0";
        String str;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet("http://ip2country.sourceforge.net/ip2c.php?format=JSON");
            // HttpGet httpget = new HttpGet("http://whatismyip.com.au/");
            // HttpGet httpget = new HttpGet("http://www.whatismyip.org/");
            HttpResponse response;

            response = httpclient.execute(httpget);
            Log.i("externalip",response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            entity.getContentLength();
            str = EntityUtils.toString(entity);
            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
            JSONObject json_data = new JSONObject(str);
            ip = json_data.getString("ip");
            Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_LONG).show();
        }
        catch (Exception e){

        }
        return ip;
    }

    //*********** CLIENT CODE **************

    private View.OnClickListener connectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!connected) {
                serverIpAddress = serverIp.getText().toString();
                if (!serverIpAddress.equals("")) {
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }
            }
        }
    };

    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, 8080);
                connected = true;
                while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                        // WHERE YOU ISSUE THE COMMANDS
                        out.println("Hey Server!");
                        Log.d("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            // MAKE SURE YOU CLOSE THE SOCKET UPON EXITING
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
