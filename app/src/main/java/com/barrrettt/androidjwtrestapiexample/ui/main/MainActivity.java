package com.barrrettt.androidjwtrestapiexample.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.barrrettt.androidjwtrestapiexample.R;
import com.barrrettt.androidjwtrestapiexample.data.user.User;
import com.barrrettt.androidjwtrestapiexample.helpers.DataBase;
import com.barrrettt.androidjwtrestapiexample.helpers.HttpConnection;
import com.barrrettt.androidjwtrestapiexample.ui.login.LoginActivity;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    User actualUser;

    Context context;
    TextView usernameTextView;
    TextView jwtTextView;
    TextView responseTextView;
    EditText paramText;
    ProgressBar progressBar;
    Button helloButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get views
        context = this;
        usernameTextView = findViewById(R.id.usernameLabel);
        jwtTextView = findViewById(R.id.jwtLabel);
        responseTextView = findViewById(R.id.responseLabel);
        paramText = findViewById(R.id.paramText);
        progressBar = findViewById(R.id.progressBar);
        helloButton = findViewById(R.id.saludarButton);


        //reset database
        //DataBase.resetDB(getApplicationContext());

        //cogemos el usuario actual y su JWT.
        actualUser = DataBase.getUserJWT(getApplicationContext());
        if (actualUser.getName().equalsIgnoreCase("null")){
            startLogin("");
            finish();
            return;
        }

        //mostramos datos de user
        usernameTextView.setText("👤 "+actualUser.getName());
        jwtTextView.setText(actualUser.getJwt());

        //onclick Button
        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = paramText.getText().toString();
                //lanza un task para coger la peticion del server
                new HelloTask((Activity) context).execute(param);
            }
        });

        //onclick label user
        usernameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin(actualUser.getName());
            }
        });
    }

    //lanza la activity de login
    public void startLogin(String username){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("USER_NAME", username);
        startActivity(intent);
        finish();
    }

}

/*ASYNC TASK PARA HACER PETICION HTTP GET */
class HelloTask extends AsyncTask<String, Void, String> {

    private WeakReference<Activity> wrActivity;

    public HelloTask(Activity activity) {
        this.wrActivity = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //get reference
        MainActivity activity = null;
        if (wrActivity!=null) activity = (MainActivity) wrActivity.get();

        //loading + button disable
        activity.progressBar.setVisibility(View.VISIBLE);
        activity.helloButton.setEnabled(false);
    }

    @Override
    protected String doInBackground(String... params) {
        //get reference
        MainActivity activity = null;
        if (wrActivity!=null) activity = (MainActivity) wrActivity.get();

        //Petición HTTP para obtener datos del server
        String parm = params[0];
        HttpConnection conn = new HttpConnection();
        String response = conn.getHello(activity.actualUser,parm);
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        //get reference
        MainActivity activity = null;
        if (wrActivity!=null) activity = (MainActivity) wrActivity.get();

        if (response != null){
            activity.responseTextView.setText(response);
        }else{
            activity.responseTextView.setText("Sin respuesta");
        }

        //ocutar progress + botton enable
        activity.progressBar.setVisibility(View.GONE);
        activity.helloButton.setEnabled(true);

        this.wrActivity = null;
    }
}