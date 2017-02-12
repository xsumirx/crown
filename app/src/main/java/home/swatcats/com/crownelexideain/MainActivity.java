package home.swatcats.com.crownelexideain;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView img;
    private static final int SELECT_PICTURE = 1;

    private SharedPreferences mPrefs;
    private int mCurViewMode;
    private Uri selectedUri;
    private String path;

    private boolean btnAllState,btnEntryState,btnClubhouseState,btnLandScapeState,btnTerraceState,btnB1State,btnB2State,btnB3State,btnB4State;

    Button btnAll,btnEntry,btnClubHouse,btnLandscape,btnTerrace,btnB1,btnB2,btnB3,btnB4,btnPick;

    public Socket socket;
    private static final int SERVER_PORT = 8000;
    private static final String SERVER_IP = "192.168.4.1";
    private boolean connected = false;


    private ImageButton btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // new Thread(new ClientThread()).start();
        new Thread(new ClientThread()).start();
        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask, 3000, 5000);

        img = (ImageView)findViewById(R.id.imageViewAppBackground);
        btnAll = (Button)findViewById(R.id.btnAllOn);
        btnEntry = (Button)findViewById(R.id.btnEntry);
        btnClubHouse = (Button)findViewById(R.id.btnClubHouse);
        btnLandscape = (Button)findViewById(R.id.btnLandscape);
        btnTerrace = (Button)findViewById(R.id.btnTerrace);
        btnB1 = (Button)findViewById(R.id.btnB1);
        btnB2 = (Button)findViewById(R.id.btnB2);
        btnB3 = (Button)findViewById(R.id.btnB3);
        btnB4 = (Button)findViewById(R.id.btnB4);
        btnPick = (Button)findViewById(R.id.btnPick);

        btnSetting = (ImageButton)findViewById(R.id.btnSetting);


        if(savedInstanceState != null)
        {
            selectedUri = savedInstanceState.getParcelable("PATH");

            SharedPreferences sharedPreferences = getSharedPreferences("APP",MODE_PRIVATE);
            btnAllState = sharedPreferences.getBoolean("B1",false);
            btnEntryState = sharedPreferences.getBoolean("B2",false);
            btnClubhouseState = sharedPreferences.getBoolean("B3",false);
            btnLandScapeState = sharedPreferences.getBoolean("B4",false);
            btnTerraceState = sharedPreferences.getBoolean("B5",false);
            btnB1State = sharedPreferences.getBoolean("B6",false);
            btnB2State = sharedPreferences.getBoolean("B7",false);
            btnB3State = sharedPreferences.getBoolean("B8",false);
            btnB4State = sharedPreferences.getBoolean("B9",false);

        }else
        {
            btnAllState = false;
            btnEntryState = false;
            btnClubhouseState = false;
            btnLandScapeState = false;
            btnTerraceState = false;
            btnB1State = false;
            btnB2State = false;
            btnB3State = false;
            btnB4State = false;


        }




        //Load Button Properties
        if(btnAllState)
            btnAll.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnAll.setBackgroundColor(getResources().getColor(R.color.buttonOff));

        //Load Button Properties
        if(btnEntryState)
            btnEntry.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnEntry.setBackgroundColor(getResources().getColor(R.color.buttonOff));

        //Load Button Properties
        if(btnClubhouseState)
            btnClubHouse.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnClubHouse.setBackgroundColor(getResources().getColor(R.color.buttonOff));

        //Load Button Properties
        if(btnLandScapeState)
            btnLandscape.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnLandscape.setBackgroundColor(getResources().getColor(R.color.buttonOff));

        //Load Button Properties
        if(btnTerraceState)
            btnTerrace.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnTerrace.setBackgroundColor(getResources().getColor(R.color.buttonOff));

        //Load Button Properties
        if(btnB1State)
            btnB1.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnB1.setBackgroundColor(getResources().getColor(R.color.buttonOff));



        //Load Button Properties
        if(btnB2State)
            btnB2.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnB2.setBackgroundColor(getResources().getColor(R.color.buttonOff));


        //Load Button Properties
        if(btnB3State)
            btnB3.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnB3.setBackgroundColor(getResources().getColor(R.color.buttonOff));


        //Load Button Properties
        if(btnB4State)
            btnB4.setBackgroundColor(getResources().getColor(R.color.buttonOn));
        else
            btnB4.setBackgroundColor(getResources().getColor(R.color.buttonOff));




        FullScreen();

        img = (ImageView)findViewById(R.id.imageViewAppBackground);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View decorView = getWindow().getDecorView();
                int uiOpt = decorView.getSystemUiVisibility();
                uiOpt |= (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN);
                decorView.setSystemUiVisibility(uiOpt);
            }
        });

        if(selectedUri != null )
        {
            img.setImageURI(selectedUri);
        }


        btnAll.setOnClickListener(onClickListener);
        btnEntry.setOnClickListener(onClickListener);
        btnClubHouse.setOnClickListener(onClickListener);
        btnLandscape.setOnClickListener(onClickListener);
        btnTerrace.setOnClickListener(onClickListener);
        btnB1.setOnClickListener(onClickListener);
        btnB2.setOnClickListener(onClickListener);
        btnB3.setOnClickListener(onClickListener);
        btnB4.setOnClickListener(onClickListener);
        btnPick.setOnClickListener(onClickListener);


        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Setting.class);
                startActivity(i);
            }
        });



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }





    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button)v;
            switch (btn.getText().toString())
            {

                case "ALL ON/OFF":


                    if(btnAllState) {
                        btnAllState = false;
                        btnAll.setBackgroundColor(getResources().getColor(R.color.buttonOff));
                    }
                    else {
                        btnAllState = true;
                        btnAll.setBackgroundColor(getResources().getColor(R.color.buttonOn));
                    }
                    break;


                case "ENTRY":
                    if(btnEntryState) {
                        btnEntryState = false;
                        btnEntry.setBackgroundColor(getResources().getColor(R.color.buttonOff));
                    }
                    else {
                        btnEntryState = true;
                        btnEntry.setBackgroundColor(getResources().getColor(R.color.buttonOn));
                    }
                    break;


                case "CLUB HOUSE":
                    if(btnClubhouseState) {
                        btnClubhouseState = false;
                        btnClubHouse.setBackgroundColor(getResources().getColor(R.color.buttonOff));
                    }
                    else {
                        btnClubhouseState = true;
                        btnClubHouse.setBackgroundColor(getResources().getColor(R.color.buttonOn));
                    }
                    break;

                case "PICK":

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

                    break;


                default:break;
            }

        }
    };

    public void FullScreen() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SharedPreferences sharedPreferences = getSharedPreferences("APP",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("B1",btnAllState);
        editor.putBoolean("B2",btnEntryState);
        editor.putBoolean("B3",btnClubhouseState);
        editor.putBoolean("B4",btnLandScapeState);
        editor.putBoolean("B5",btnTerraceState);
        editor.putBoolean("B6",btnB1State);
        editor.putBoolean("B7",btnB2State);
        editor.putBoolean("B8",btnB4State);
        editor.putBoolean("B9",btnB4State);
        editor.commit();
        outState.putParcelable("PATH",selectedUri);
        super.onSaveInstanceState(outState);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedUri = data.getData();
                img.setImageURI(selectedUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else
                {
                    finish();
                }

                break;
            }

            default:
                break;
        }
    }

    public void sendMessage(String str)  {

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(str);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                Log.d("ClientActivity", "C: Connecting...");
                socket = new Socket(serverAddr, SERVER_PORT);
                connected = true;
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    class MyTimerTask extends TimerTask {
        // Important : keep TCP connection alive
        public void run() {
            sendMessage("OK");
        }
    }

}
