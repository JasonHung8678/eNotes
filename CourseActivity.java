package jh.enotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CourseActivity extends AppCompatActivity {

    String courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");

        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(courseName);
    }
}
