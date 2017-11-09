package com.example.shishir.agecalculator;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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
                if (!TextUtils.isEmpty(birthDate)) {
                    if (inputIsValid(today, birthDate)) {
                        calculateAge(today, birthDate);
                    } else {
                        showAlert("Date of Birth cannot be less than Today's Date !");
                    }
                } else {
                    showAlert("Date of Birth must be filled up !");
                }

                break;
            }
            case R.id.clearBtn: {
                dateOfBirthTv.setText("");
                yearAtAgeTv.setText("00");
                monthsAtAgeTv.setText("00");
                daysAtAgeTv.setText("00");
                monthsAtNextBirth.setText("00");
                daysAtNextBirth.setText("00");
                break;
            }
        }
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage(message).setCancelable(true).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.show();
    }

    private boolean inputIsValid(String today, String birthDate) {
        boolean result = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd - MM - yyyy");
        Calendar calToday = Calendar.getInstance();
        Calendar calBirth = Calendar.getInstance();
        try {
            calToday.setTime(sdf.parse(today));
            calBirth.setTime(sdf.parse(birthDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (calToday.getTimeInMillis() >= calBirth.getTimeInMillis()) {
            result = true;
        }
        return result;
    }

    private void calculateAge(String today, String birthDate) {

        int monthRemaining = 0, dayRemaining = 0;

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd - MM - yyyy");
        LocalDate dateOfBirth = LocalDate.parse(birthDate, formatter);
        LocalDate currentDate = LocalDate.parse(today, formatter);
        Period age = new Period(dateOfBirth, currentDate, PeriodType.yearMonthDay());
        int day = age.getDays() + 1;
        int month = age.getMonths();
        int year = age.getYears();

        if (year < 100) {
            yearAtAgeTv.setText(String.format("%02d", year));
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


        //Setting current months and days of months.................................................

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int currentMonth = cal.get(Calendar.MONTH);
        int dayOfCurrentMonth = cal.get(Calendar.DAY_OF_MONTH);
        Toast.makeText(this, dayOfBirthMonth + "  " + birthMonth + "\n" + dayOfCurrentMonth + "  " + currentMonth, Toast.LENGTH_SHORT).show();


        // Getting Differences....................................................

        if (birthMonth == currentMonth) {
            if (dayOfBirthMonth >= dayOfCurrentMonth) {
                dayRemaining = dayOfBirthMonth - dayOfCurrentMonth;
                monthRemaining = birthMonth - currentMonth;
            } else {
                dayRemaining = (dayOfBirthMonth + 30) - dayOfCurrentMonth;
                monthRemaining = ((12 - currentMonth) + birthMonth) - 1;
            }
        } else if (birthMonth > currentMonth) {
            if (dayOfBirthMonth >= dayOfCurrentMonth) {
                dayRemaining = dayOfBirthMonth - dayOfCurrentMonth;
                monthRemaining = birthMonth - currentMonth;
            } else {
                dayOfBirthMonth = dayOfBirthMonth + 30;
                currentMonth = currentMonth + 1;
                dayRemaining = dayOfBirthMonth - dayOfCurrentMonth;
                monthRemaining = birthMonth - currentMonth;
            }
        } else if (birthMonth < currentMonth) {
            if (dayOfBirthMonth >= dayOfCurrentMonth) {
                dayRemaining = dayOfBirthMonth - dayOfCurrentMonth;
                monthRemaining = (12 - currentMonth) + birthMonth;
            } else {
                dayRemaining = (dayOfBirthMonth + 30) - dayOfCurrentMonth;
                monthRemaining = ((12 - currentMonth) + birthMonth) - 1;
            }
        }

        monthsAtNextBirth.setText(String.format("%02d", monthRemaining));
        daysAtNextBirth.setText(String.format("%02d", dayRemaining));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dateFlag == 1) {
            todayDateTv.setText(String.format("%02d", dayOfMonth) + " - " + String.format("%02d", month + 1) + " - " + year);
        } else {
            dateOfBirthTv.setText(String.format("%02d", dayOfMonth) + " - " + String.format("%02d", month + 1) + " - " + year);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Do you Really want to exit ?").setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }
}
