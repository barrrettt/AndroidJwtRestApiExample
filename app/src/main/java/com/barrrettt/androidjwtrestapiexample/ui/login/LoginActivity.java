package com.barrrettt.androidjwtrestapiexample.ui.login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.barrrettt.androidjwtrestapiexample.R;
import com.barrrettt.androidjwtrestapiexample.data.user.User;
import com.barrrettt.androidjwtrestapiexample.helpers.DataBase;
import com.barrrettt.androidjwtrestapiexample.helpers.HttpConnection;
import com.barrrettt.androidjwtrestapiexample.ui.main.MainActivity;

import java.lang.ref.WeakReference;


public class LoginActivity extends AppCompatActivity {

    Context context;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    ProgressBar loadingProgressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get controls
        context = this;
        usernameEditText = findViewById(R.id.usernameLabel);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String username = intent.getStringExtra("USER_NAME");
        if (username != null) usernameEditText.setText(username);

        //onclick Button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lanza un task para coger el JWT del server
                new AuthUserPassTask((Activity) context).execute();
            }
        });
    }

    //activar / desactivar los controles para esperas
    public void enableViews(Boolean isActive){
        usernameEditText.setEnabled(isActive);
        passwordEditText.setEnabled(isActive);
        loginButton.setEnabled(isActive);
        if (isActive){
            loadingProgressBar.setVisibility(View.INVISIBLE);
        }else{
            loadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void saveJWT(User user){
        DataBase.saveJWT(getApplicationContext(),user);
    }

    //lanza la activity principal
    public void showMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}

    /*ASYNC TASK PARA AUTENTICAR USER + OPTENER EL JWT*/
class AuthUserPassTask extends AsyncTask<Void, Void, String> {

    private WeakReference<Activity> wrActivity;

    public AuthUserPassTask(Activity activity) {
        this.wrActivity = new WeakReference<Activity>(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //get reference
        LoginActivity activity = null;
        if (wrActivity!=null) activity = (LoginActivity) wrActivity.get();

        //disable views
        activity.enableViews(false);

        //oculta teclado
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(activity.context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.passwordEditText.getWindowToken(), 0);

        //toast
        Toast.makeText(activity.getApplicationContext(), "Esperando respuesta...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(Void... params) {
        //get reference
        LoginActivity activity = null;
        if (wrActivity!=null) activity = (LoginActivity) wrActivity.get();

        //get datas de los controles
        String username = activity.usernameEditText.getText().toString();
        String password = activity.passwordEditText.getText().toString();

        //Petición HTTP para autenticar y optener el jwt
        HttpConnection conn = new HttpConnection();
        String jwt = conn.autenticarUsuario(username,password);

        //Guardar user y Token a la database si nos devuelven algo:
        if (jwt != null) {
            activity.saveJWT(new User(username,jwt));
        }
        return jwt;
    }

    @Override
    protected void onPostExecute(String jwt) {
        //get reference
        LoginActivity activity = null;
        if (wrActivity!=null) activity = (LoginActivity) wrActivity.get();

        //hay JWT?
        if (jwt != null) {
            Toast.makeText(activity.getApplicationContext(), "JWT " + jwt, Toast.LENGTH_LONG).show();
            activity.showMainActivity();

        } else {
            Toast.makeText(activity.getApplicationContext(), "SIN JWT", Toast.LENGTH_LONG).show();
            activity.enableViews(true);
        }

        this.wrActivity = null;
    }
}
