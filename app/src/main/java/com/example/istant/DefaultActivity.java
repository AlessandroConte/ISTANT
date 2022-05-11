package com.example.istant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        setContentView(R.layout.default_activity);

        lv = (ListView) findViewById(R.id.list_default);
        btn_default = (Button) findViewById(R.id.btn_default);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> default_array = new ArrayList<String>();
        default_array.add(getResources().getString(R.string.createNewActivity_piedibus));
        default_array.add(getResources().getString(R.string.createNewActivity_allenamentonuoto));
        default_array.add(getResources().getString(R.string.createNewActivity_allenamentocalcio));
        default_array.add(getResources().getString(R.string.createNewActivity_partitatennis));
        default_array.add(getResources().getString(R.string.createNewActivity_corsocanto));


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
                String info = getResources().getString(R.string.createNewActivity_personalizzata);
                Intent intent = new Intent(DefaultActivity.this, CreateNewActivitiesActivity.class);
                intent.putExtra("key",info);
                startActivity(intent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    String info = getString(R.string.createNewActivity_piedibus);
                    Intent intent = new Intent(DefaultActivity.this, CreateNewActivitiesActivity.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
                else if(position == 1){
                    String info = getString(R.string.createNewActivity_allenamentonuoto);
                    Intent intent = new Intent(DefaultActivity.this, CreateNewActivitiesActivity.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
                else if(position == 2){
                    String info = getString(R.string.createNewActivity_allenamentocalcio);
                    Intent intent = new Intent(DefaultActivity.this, CreateNewActivitiesActivity.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
                else if(position == 3){
                    String info = getString(R.string.createNewActivity_partitatennis);
                    Intent intent = new Intent(DefaultActivity.this, CreateNewActivitiesActivity.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
                else{
                    String info = getString(R.string.createNewActivity_corsocanto);
                    Intent intent = new Intent(DefaultActivity.this, CreateNewActivitiesActivity.class);
                    intent.putExtra("key",info);
                    startActivity(intent);
                }
            }
        });
    }
}
