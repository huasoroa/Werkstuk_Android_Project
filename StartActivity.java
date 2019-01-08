package be.ehb.dig_x.ricardo.werkstuk_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import be.ehb.dig_x.ricardo.werkstuk_android.Database.AppDatabase;
import be.ehb.dig_x.ricardo.werkstuk_android.Database.ShoeEntry;
import be.ehb.dig_x.ricardo.werkstuk_android.Model.Shoe;


public class StartActivity extends AppCompatActivity {

    private AppDatabase mDb;

    Button login,signup;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        mDb= AppDatabase.getInstance(getApplicationContext());


        insertShoes();
        if (firebaseUser != null){
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mDb= AppDatabase.getInstance(getApplicationContext());

        insertShoes();
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,SignupActivity.class));
            }
        });

    }

    private void insertShoes(){

        mDb.ShoeDao().deleteAllShoes();
        InputStream inputStream = getResources().openRawResource(R.raw.stockx);
        CSVFile csvFile = new CSVFile(inputStream);
        List<String[]> scoreList = csvFile.read();

       /* String languageToLoad  = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
*/

        for (String[] rec : scoreList){
            ShoeEntry shoeEntry = new ShoeEntry(rec[0].replace("\"",""),"Brand",rec[2].replace("\"",""),"colorway",rec[4],rec[3]);
            mDb.ShoeDao().insertShoe(shoeEntry);
        }

    }
}
