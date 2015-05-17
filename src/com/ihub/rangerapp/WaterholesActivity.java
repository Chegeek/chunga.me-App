package com.ihub.rangerapp;

import java.util.Map;
import com.ihub.rangerapp.data.service.WaterholeService;
import com.ihub.rangerapp.data.service.WaterholeServiceImpl;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class WaterholesActivity extends CameraGPSActionBarActivity {
	
	AutoCompleteTextView waterholeName;
	Spinner levelOfWaterSpinner;
	EditText noOfAnimalsSeenView;
	EditText extraNotes;
	Button saveBtn;
	
	String waterholes[] = {"Tank5D","Tank7D","Sagana","Marembo","Panda2","KamAma","Washu1","Maung1","Salama","Matopeni","Simon","KCB","Pika pika","Tank8","TaiKam","Amaka1","Washu2","Camp Tsavo","Mwakaramba","Marungu","Garawa","Punda","Rukin1","Ngumu","Porini","Chui","Kongoni","Rukin2","Catherine","Pombe","Twiga","Simba","Mbuyuni","Nyoka","Kivuko","Taita2","Taita3","Tank6","Tank7","Kamba1","Sagal1","Mgeno1","Maung2","Choke1","Sagal2","Choke2","Choke3","Choke4","Maung3","Patricia","Mswahili","Maung5","SimbaMGE","Katana","SagallaTank","Sagal3","Rukin3","Rukin4","Tank1","Tank2","Bunduki","Tank5","Kisima","Mbugani juu","MwakarambaD","Jojoba","Mpya","Bendera","SagNo9","Kifaru","British","Nyekundu","Mairimba","Mlamba","Fisi","Somali","Panda1","Mangale","Jiwe","Sagatisa","Pua na Mdomo","Ndovu","Gae Rock","Henry ","Jeruman","Jiwe la simba","Mali ya Mungu","Matopeni ndogo ","Nyati","Ian","Kona","Mnago","Hunters","Roadside","Shifta","Mwamba","Mfamayo","Alice","Bahati","Juliana","Kambanga","Choke","Impala","Makwasinyi","Nyaga","Kasigau","Mikuluni","Catherine2","Lokidori","Jongolo"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_waterholes);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaterholesActivity.this.onBackPressed();
            }
        });
        
        Intent data = getIntent();
        
        initViews();
        
        waterholeName = (AutoCompleteTextView) findViewById(R.id.waterholeName);
        
        ArrayAdapter<String> waterholeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, waterholes);
        waterholeName.setAdapter(waterholeAdapter);
        waterholeName.setThreshold(2);
        
        levelOfWaterSpinner = (Spinner) findViewById(R.id.levelOfWaterSpinner);
        noOfAnimalsSeenView = (EditText) findViewById(R.id.noOfAnimalsSeenView);
        extraNotes = (EditText) findViewById(R.id.extraNotes);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        
        levelOfWaterSpinner = (Spinner) findViewById(R.id.levelOfWaterSpinner);
        ArrayAdapter<CharSequence> levelOfWaterAdapter = ArrayAdapter.createFromResource(this,
                R.array.levels_of_water_array, android.R.layout.simple_spinner_item);
        levelOfWaterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        levelOfWaterSpinner.setAdapter(levelOfWaterAdapter);
        
        saveBtn.setOnClickListener(new View.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				if(mode == 3)
					finish();
				else
					if(isValid())
						save();
			}
		});
        
        if(mode != 1) {
        	
        	//TODO prefill values
        	
        	if(!TextUtils.isEmpty(getIntent().getStringExtra("name")))
        		waterholeName.setText(getIntent().getStringExtra("name"));
        	
        	if(!TextUtils.isEmpty(getIntent().getStringExtra("levelOfWater")))
            	for(int i = 0; i < levelOfWaterAdapter.getCount(); i++) {
            		if(levelOfWaterSpinner.getItemAtPosition(i).toString().equals(getIntent().getStringExtra("levelOfWater")))
            			levelOfWaterSpinner.setSelection(i);
            	}
        	
        	if(data.hasExtra("numberOfAnimals"))
            	noOfAnimalsSeenView.setText(data.getIntExtra("numberOfAnimals", 0) + "");
        	
        	if(!TextUtils.isEmpty(getIntent().getStringExtra("extraNotes")))
        		extraNotes.setText(getIntent().getStringExtra("extraNotes"));
            
            if(mode == 2) {
            	saveBtn.setText(getString(R.string.edit));
            } else {
            	saveBtn.setText(getString(R.string.close));
            }
        }
	}
	
	protected void save() {
		
		Integer noOfAnimals = 0;
		
		try {
			noOfAnimals = Integer.valueOf(noOfAnimalsSeenView.getText().toString());
		} catch (Exception e) {}
		
		Integer id = -1;
		if(mode == 2)
			id = getIntent().getIntExtra("id", -1);
		
		String level = levelOfWaterSpinner.getSelectedItemPosition() == 0 ? "" : levelOfWaterSpinner.getSelectedItem().toString();
		
		WaterholeService service = new WaterholeServiceImpl();
		Map<String, Object> result = service.save(
			id,
			waterholeName.getText().toString(), 
			level, 
			noOfAnimals, 
			extraNotes.getText().toString(), 
			imagePath, 
			getWP());
		
		showSaveResult(result);
	}
}