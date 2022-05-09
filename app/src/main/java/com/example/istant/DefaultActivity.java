package com.example.istant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DefaultActivity extends AppCompatActivity {

    private ListView lv;
    private Button btn_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        lv = (ListView) findViewById(R.id.list_default);
        btn_default = (Button) findViewById(R.id.btn_default);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> default_array = new ArrayList<String>();
        default_array.add("PiediBus");
        default_array.add("Allenamento Nuoto");
        default_array.add("Allenamento Calcio");
        default_array.add("Partita Tennis");
        default_array.add("Corso di canto");


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                default_array );

        lv.setAdapter(arrayAdapter);


        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = "Personalizzata";
                Intent intent = new Intent(DefaultActivity.this, activity_createNewActivities.class);
                intent.putExtra("key",info);
                startActivity(intent);
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if(position == 0){
                    String info = "PiediBus";
                    Intent intent = new Intent(DefaultActivity.this, activity_createNewActivities.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
                else if(position == 1){
                    String info = "AllenamentoNuoto";
                    Intent intent = new Intent(DefaultActivity.this, activity_createNewActivities.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
                else if(position == 2){
                    String info = "AllenamentoCalcio";
                    Intent intent = new Intent(DefaultActivity.this, activity_createNewActivities.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
                else if(position == 3){
                    String info = "PartitaTennis";
                    Intent intent = new Intent(DefaultActivity.this, activity_createNewActivities.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
                else{
                    String info = "CorsoCanto";
                    Intent intent = new Intent(DefaultActivity.this, activity_createNewActivities.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
            }
        });
    }
}
