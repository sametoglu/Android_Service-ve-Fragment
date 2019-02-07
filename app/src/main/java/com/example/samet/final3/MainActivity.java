package com.example.samet.final3;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Fragment fragmentbir,fragmentiki;
    private EditText sayi1,sayi2;
    private TextView sonuc;
    private Button btn_topla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        registerEventHandlers();
    }

    private void initComponents() {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentbir = fragmentManager.findFragmentById(R.id.fragmentbir);
        fragmentiki = fragmentManager.findFragmentById(R.id.fragmentiki);

        sayi1 = findViewById(R.id.sayi1);
        sayi2 = findViewById(R.id.sayi2);

        sonuc = findViewById(R.id.sonuc);

        btn_topla = findViewById(R.id.btn_topla);

    }

    private void registerEventHandlers() {

        btn_toplaon_Click();
    }

    private void btn_toplaon_Click() {
        btn_topla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int valsayi1 = Integer.valueOf(sayi1.getText().toString());
                int valsayi2 = Integer.valueOf(sayi2.getText().toString());

                Integer toplam  = valsayi1+valsayi2;

                sonuc.setText(toplam.toString());

                Intent i = new Intent(MainActivity.this,ServiceActivity.class);
                startActivity(i);
            }


        });
    }
}
