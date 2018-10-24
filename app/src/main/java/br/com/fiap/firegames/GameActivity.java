package br.com.fiap.firegames;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import br.com.fiap.firegames.model.Game;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "AddGameActivity";
    private DatePickerDialog mDatePickerDialog;
    TextView edtName;
    TextView edtDeveloper;
    TextView edtReleaseDate;
    Button btAdd;

    private FirebaseFirestore firestoreDB;
    String id = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        edtName = findViewById(R.id.edtName);
        edtDeveloper = findViewById(R.id.edtDeveloper);
        edtReleaseDate = findViewById(R.id.edtReleaseDate);
        btAdd = findViewById(R.id.btAdd);

        firestoreDB = FirebaseFirestore.getInstance();

        // Get Extras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("UpdateGameId");
            edtName.setText(bundle.getString("UpdateGameName"));
            edtDeveloper.setText(bundle.getString("UpdateGameDeveloper"));
            edtReleaseDate.setText(bundle.getString("UpdateGameReleaseDate"));
        }

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String developer = edtDeveloper.getText().toString();
                String releaseDate = edtReleaseDate.getText().toString();

                if (name.length() > 0) {
                    if (id.length() > 0) {
                        updateGame(id, name, developer, releaseDate);
                    } else {
                        addGame(name, developer, releaseDate);
                    }
                }

                finish();
            }
        });

        setDateTimeField();

        edtReleaseDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                edtReleaseDate.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private void updateGame(String id, String name, String developer, String releaseDate) {
        Map<String, Object> game = (new Game(id, name, developer, releaseDate)).toMap();

        firestoreDB.collection("games")
                .document(id)
                .set(game)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding Game document", e);
                        Toast.makeText(getApplicationContext(), "Ops! Alterações não salvas", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addGame(String name, String developer, String releaseDate) {
        Map<String, Object> game = new Game(name, developer, releaseDate).toMap();

        firestoreDB.collection("games")
                .add(game)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Jogo adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding Game document", e);
                        Toast.makeText(getApplicationContext(), "Ops! O jogo não pode ser salvo!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
