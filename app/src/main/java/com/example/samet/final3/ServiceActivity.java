package com.example.samet.final3;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

        private EditText message;
        private TextView txt_goster;
        private Button btnGonder,btn_izin;
        private ToggleButton btn_tarih;
        private Boolean isBound = false;
        private DateService dateService;
        int REQ_CODE_CAMERA = 0 ;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_service);
            initComponents();
            registerEventHandlers();
        }

        private ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DateService.Datebinder datebinder = (DateService.Datebinder) service;

                dateService = datebinder.getService();
                isBound=true;
                Toast.makeText(dateService, "tarih servise baglandı", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

                dateService=null;
                isBound=false;
                Toast.makeText(dateService, "tarih servis baglantısı koptu", Toast.LENGTH_SHORT).show();

            }
        };

        private void registerEventHandlers() {
            btnGonder_onClick();
            btn_tarih_onClick();
            btn_izin_onClick();
        }

    private void btn_izin_onClick() {

            btn_izin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    final String permission = Manifest.permission.CAMERA;
                    boolean isGranted = ContextCompat.checkSelfPermission(ServiceActivity.this,
                            permission) == PackageManager.PERMISSION_GRANTED;

                    if (isGranted)
                    {
                        Toast.makeText(ServiceActivity.this, "izin verilmiş", Toast.LENGTH_SHORT).show();
                    }

                    else {

                    boolean rationale = ActivityCompat.shouldShowRequestPermissionRationale(ServiceActivity.this,permission);

                    if (!rationale){

                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceActivity.this);

                        builder.setTitle("Kamera izni");
                        builder.setMessage("Bu izin Onemli Kamera için");
                        builder.setIcon(android.R.drawable.ic_dialog_info);

                        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(ServiceActivity.this,new String[]{permission},REQ_CODE_CAMERA);

                                List<String> izinler = new ArrayList<String>();
                                izinler.add(Manifest.permission.CAMERA);
                                ActivityCompat.requestPermissions(ServiceActivity.this,izinler.toArray(new String[izinler.size()]), REQ_CODE_CAMERA);

                            }
                        });
                        builder.setNegativeButton("iptal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(ServiceActivity.this, "iptal edildi", Toast.LENGTH_SHORT).show();

                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                    else {
                        ActivityCompat.requestPermissions(ServiceActivity.this,new String[]{permission},REQ_CODE_CAMERA);

                        List<String> izinler = new ArrayList<String>();
                        izinler.add(Manifest.permission.CAMERA);
                        ActivityCompat.requestPermissions(ServiceActivity.this,izinler.toArray(new String[izinler.size()]), REQ_CODE_CAMERA);
                        }
                    }
                }
            });

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if(requestCode == REQ_CODE_CAMERA) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "İzin Verildi: " );
            } else
                Log.d("Permissions", "İzin Reddedildi: " );
        }

    }


    private void btn_tarih_onClick() {

            btn_tarih.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btn_tarih.isChecked()){

                        Intent intent = new Intent(ServiceActivity.this,DateService.class);
                        bindService(intent,serviceConnection, BIND_AUTO_CREATE);

                    }
                    else  {
                        unbindService(serviceConnection);
                        isBound = false;
                    }
                }
            });

        }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private void btnGonder_onClick() {
            btnGonder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ServiceActivity.this, MessageService.class);
                    intent.putExtra("message", message.getText().toString());
                    startService(intent);


                    if(isBound) {
                        String readableDate = dateService.getReadableDate();
                        txt_goster.setText(readableDate);
                    }
                    else {

                        Toast.makeText(ServiceActivity.this, "Service Baslamadı Baslatıp Tekrar Deneyin ", Toast.LENGTH_SHORT).show();

                    }


                }
            });
        }
        private void initComponents() {
            message = findViewById(R.id.message);
            btnGonder = findViewById(R.id.btn_gonder);
            btn_tarih=findViewById(R.id.btn_tarih);
            txt_goster=findViewById(R.id.txt_goster);
            btn_izin = findViewById(R.id.btn_izin);
        }
    }

