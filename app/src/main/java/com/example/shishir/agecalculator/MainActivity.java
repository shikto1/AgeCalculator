package com.example.shishir.agecalculator;

import android.app.DatePickerDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView todayDateTv, dateOfBirthTv, yearAtAgeTv, monthsAtAgeTv, daysAtAgeTv, monthsAtNextBirth, daysAtNextBirth;
    private Button calculateBtn, clearBtn;
    private Calendar calendar;
    private static int dateFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById();

    }

    private void findViewById() {
        todayDateTv = (TextView) findViewById(R.id.todaysDate);
        dateOfBirthTv = (TextView) findViewById(R.id.dateOfBirth);
        yearAtAgeTv = (TextView) findViewById(R.id.yearAtAge);
        monthsAtAgeTv = (TextView) findViewById(R.id.monthsAtAge);
        daysAtAgeTv = (TextView) findViewById(R.id.daysAtAge);
        monthsAtNextBirth = (TextView) findViewById(R.id.montsAtNextBirthDay);
        daysAtNextBirth = (TextView) findViewById(R.id.daysAtNextBirthDay);
        calculateBtn = (Button) findViewById(R.id.calculateBtn);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        calendar = Calendar.getInstance();

        todayDateTv.setOnClickListener(this);
        dateOfBirthTv.setOnClickListener(this);
        calculateBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

        setTodayDate();
    }

    private void setTodayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd - MM - yyyy"); // the format of your date
        String formattedDate = formatter.format(new Date());
        todayDateTv.setText(formattedDate);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.todaysDate: {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(true);
                datePickerDialog.show();
                dateFlag = 1;
                break;
            }
            case R.id.dateOfBirth: {
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setCancelable(true);
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
                dateFlag = 2;
                break;
            }
            case R.id.calculateBtn: {
                String today = todayDateTv.getText().toString();
                String birthDate = dateOfBirthTv.getText().toString();

                calculateAge(today, birthDate);
                break;
            }
            case R.id.clearBtn: {
                dateOfBirthTv.setText("dd - mm - yyyy");
                break;
            }
        }
    }

    private void calculateAge(String today, String birthDate) {

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd - MM - yyyy");
        LocalDate dateOfBirth = LocalDate.parse(birthDate, formatter);
        LocalDate currentDate = LocalDate.parse(today, formatter);
        Period age = new Period(dateOfBirth, currentDate, PeriodType.yearMonthDay());
        int day = age.getDays() + 1;
        int month = age.getMonths();
        int year = age.getYears();

        if (year < 100) {
            yearAtAgeTv.setText(String.format("%02", year));
        } else {
            yearAtAgeTv.setText(year + "");
        }
        monthsAtAgeTv.setText(String.format("%02d", month));
        daysAtAgeTv.setText(String.format("%02d", day));


        //Getting Birth Month and Date...................................................

        SimpleDateFormat sdf = new SimpleDateFormat("dd - MM - yyyy");
        Calendar birth = Calendar.getInstance();// the format of your date
        try {
            Date birthDateFormat = sdf.parse(birthDate);
            birth.setTime(birthDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int birthMonth = birth.get(Calendar.MONTH);
        int dayOfBirthMonth = birth.get(Calendar.DAY_OF_MONTH);


        //Setting this month to current year.................................................

        Calendar currentYearBirthDate = Calendar.getInstance();
        currentYearBirthDate.set(Calendar.MONTH, birthMonth);
        currentYearBirthDate.set(Calendar.DAY_OF_MONTH, dayOfBirthMonth);
        LocalDate currentYearBirth = LocalDate.fromCalendarFields(currentYearBirthDate);

        Period period = new Period(currentDate, currentYearBirth, PeriodType.yearMonthDay());
        int monthsRemaining = period.getMonths();
        int daysRemaining = period.getDays();

        monthsAtNextBirth.setText(String.format("%02d", monthsRemaining));
        daysAtNextBirth.setText(String.format("%02d", daysRemaining));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dateFlag == 1) {
            todayDateTv.setText(dayOfMonth + " - " + (month + 1) + " - " + year);
        } else {
            dateOfBirthTv.setText(dayOfMonth + " - " + (month + 1) + " - " + year);
        }
    }
}
