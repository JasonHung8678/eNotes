package jh.enotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    //UI Initialization
    private FloatingActionButton addCourseBtn;
    private RelativeLayout main;
    private TextView textView;
    private Spinner spinner;
    private RelativeLayout courseListContainer;

    //Output/Input Initialization
    private Scanner input;
    //private FileOutputStream fileOutputStream;
    private OutputStreamWriter outputStreamWriter;
    private File dataDir;
    private String dataFileName = "APPDATA";
    private File dataFile;

    //Useful variables
    private int numberOfCourses;
    private ArrayList<String> courses = new ArrayList<String>();

    //Scale
    private float scale;

    //Data
    private String data;

    //Failsafe
    boolean isPaused = false;

    //Activity request codes
    private final int ADD_COURSE_ACTIVITY = 1;
    private final int OPEN_COURSE_ACTIVITY = 2;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI
        addCourseBtn = (FloatingActionButton) findViewById(R.id.addCourseBtn);
        main = (RelativeLayout) findViewById(R.id.main_layout);
        //spinner = (Spinner) findViewById(R.id.spinner);

        //SPINNER SELECTION
        /*AdapterView.OnItemSelectedListener spinnerItemListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(spinnerItemListener);*/

        //Scale
        scale = getResources().getDisplayMetrics().density;

        //GET DATA FILE
        dataDir = getFilesDir();
        dataFile = new File (dataDir.getAbsolutePath() + "/" + dataFileName);

        //INJECT DEBUG
        //try {injectCourses();}
        //catch (IOException e) {}

        try {
            //Get data
            input = new Scanner(dataFile);
            input.useDelimiter(";");
            if (input.hasNext()) {
                numberOfCourses = Integer.parseInt(input.next());
                data = numberOfCourses + "";

                for (int i = 1; i <= numberOfCourses; i++) {
                    courses.add(input.next());
                }
            }
        }
        catch (FileNotFoundException e) {
        }


        //Set writer or create file
        try {
            outputStreamWriter = new OutputStreamWriter(openFileOutput(dataFileName,Context.MODE_PRIVATE));
        }
        catch (FileNotFoundException e) {

        }

        //Scrollview for course buttons
        courseListContainer = (RelativeLayout) findViewById(R.id.courseList);

        loadCoursesButtons();

    }

    public void loadCoursesButtons() {
        for (int i = 1; i<=numberOfCourses; i++) {
            final Button btn = new Button(this);
            btn.setText(courses.get(i-1));
            courseListContainer.addView(btn);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCourse(btn.getText().toString());
                }
            };
            btn.setOnClickListener(onClickListener);


            int buffer = 0;
            if (i != 1) {
                buffer = dpToPx(36)*(i-1) + dpToPx(10)*(i-1);
            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)btn.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(0,dpToPx(i*10)+buffer,0,0);
            btn.setLayoutParams(params);
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            btn.setWidth(width-20);
        }

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, courses);
        spinner.setAdapter(adapter);*/
    }

    public void openCourse(String courseName) {
        Intent intent = new Intent(this, CourseActivity.class);

        intent.putExtra("courseName",courseName);

        startActivityForResult(intent,1);
    }

    public void addCourseOnClick(View view) throws IOException {
        Intent intent = new Intent(this, AddCourseActivity.class);
        intent.putExtra("courses",courses);
        startActivityForResult(intent,ADD_COURSE_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_COURSE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            String courseName = data.getStringExtra("courseName");
            courses.add(courseName);
            numberOfCourses++;
            loadCoursesButtons();
        }
    }

    public void injectCourses() throws IOException {
        numberOfCourses = 3;
        courses.add("Calc");
        courses.add("Chem");
        courses.add("Phys");
    }

    public void deleteFile() {
        File file = new File(getFilesDir() + "/" + dataFileName);
        file.delete();
    }

    private int dpToPx(int dp) {
        int px = (int) (dp * scale + 0.5f);
        return px;
    }

    public void saveData() {
        data = numberOfCourses + ";";
        for (int i = 1; i<=numberOfCourses; i++) {
            data += courses.get(i-1) + ";";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        isPaused = true;
        //SAVE DATA
        try {
            saveData();
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //LOAD DATA
        if(isPaused == true) {
            try {
                outputStreamWriter = new OutputStreamWriter(openFileOutput(dataFileName,Context.MODE_PRIVATE));
            }
            catch (IOException e) {

            }
        }
        isPaused = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //SAVE DATA
        if (isPaused = false) {

            try {
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
            catch (IOException e) {

            }
        }
    }
}
