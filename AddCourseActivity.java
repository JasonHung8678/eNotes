package jh.enotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Button submit;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        editText = (EditText)findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.errorText);
        submit = (Button) findViewById(R.id.submitButton);
        cancel = (Button) findViewById(R.id.cancelButton);
        submit.setTextColor(Color.WHITE);
        submit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        cancel.setTextColor(Color.WHITE);
        cancel.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void submitOnClick(View view) {
        Intent resultIntent = new Intent();
        String courseName = editText.getText().toString().trim().toUpperCase();

        ArrayList<String> courses = new ArrayList<String>();
        Intent intent = getIntent();
        courses = intent.getStringArrayListExtra("courses");

        if (courses.contains(courseName)) {
            textView.setText("Error: Duplicate names. (Please pick a unique name!)");
            editText.getText().clear();
        }
        else if (courseName.isEmpty()) {
            textView.setText("Error: Please enter a name!");
            editText.getText().clear();
        }
        else {
            resultIntent.putExtra("courseName", courseName);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    public void cancelOnClick(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
