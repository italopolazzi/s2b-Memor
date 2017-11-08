package com.dev.mendes.android_mytasks.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dev.mendes.android_mytasks.R;
import com.dev.mendes.android_mytasks.activity.MapsActivity;
import com.dev.mendes.android_mytasks.dataBase.DataBaseControl;
import com.dev.mendes.android_mytasks.dataBase.Task;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ModTaskFragment extends Fragment {

    public static String taskId, taskName, taskDate, taskPlace, taskCoordinates, taskNote, task;
    public static EditText etTaskName, etTaskPlace, etTaskDate, etTaskNote;
    public static Button btnPersistir;
    public static final int REQUEST_LATLNG = 0;
    public static final int RESULT_OK = 0;
    public static final int RESULT_CANCELED = 1;
    private Task mTask;
    private static final String ARG_TASK = "task";
    private static boolean MODE_EDIT;

    public ModTaskFragment(boolean mode_edit) {
        MODE_EDIT = mode_edit;
    }

    public static ModTaskFragment newInstance(Task task) {
        ModTaskFragment fragment = new ModTaskFragment(true);
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        if (getArguments() != null) {
            mTask = getArguments().getParcelable(ARG_TASK);
            MODE_EDIT = true;
            taskId = mTask.getId();

            Toast.makeText(getContext(), taskId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_task, container, false);

        // Encontra os elementos da tela
        etTaskName = (EditText) view.findViewById(R.id.et_name);
        etTaskPlace = (EditText) view.findViewById(R.id.et_place);
        etTaskDate = (EditText) view.findViewById(R.id.et_date);
        etTaskNote = (EditText) view.findViewById(R.id.et_note);
        btnPersistir = (Button) view.findViewById(R.id.btn_persistit);

        if(MODE_EDIT){
            etTaskName.setText(mTask.getTaskName());
            etTaskDate.setText(mTask.getTaskDate());
            etTaskPlace.setText(mTask.getTaskPlace());
            etTaskNote.setText(mTask.getTaskNote());
            btnPersistir.setText(R.string.btn_edit);
        }

        MyEditTextDatePicker myEditTextDatePicker = new MyEditTextDatePicker(etTaskDate);

        btnPersistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Encontra o banco de dados
                DataBaseControl crud = new DataBaseControl(getContext());

                taskName = etTaskName.getText().toString();
                taskDate = etTaskDate.getText().toString();
                taskPlace = etTaskPlace.getText().toString();
                taskNote = etTaskNote.getText().toString();

                if(taskName.isEmpty()){
                    Toast.makeText(getContext(), R.string.inputTaskNameEmpty, Toast.LENGTH_SHORT).show();
                } else {
                    if(MODE_EDIT) {
                        task = crud.editTask(taskId, taskName, taskDate, taskPlace, taskCoordinates, taskNote);
                    } else {
                        task = crud.addTask(taskName, taskDate, taskPlace, taskCoordinates, taskNote);
                    }
                    Toast.makeText(getContext(), task, Toast.LENGTH_SHORT).show();

                    getActivity()
                            .finish();
                }
            }
        });

        ImageView btnLocal = (ImageView) view.findViewById(R.id.btn_selLocal);
        btnLocal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(getContext(), MapsActivity.class), REQUEST_LATLNG);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LATLNG) {
            //startActivity(new Intent(new Intent(getContext(), MapsActivity.class)));
            if(resultCode == RESULT_OK){
                String lat, lng;

                Bundle bundle = data.getBundleExtra("bundle");
                LatLng coordinates = bundle.getParcelable("maps_location");
                lat = String.valueOf(coordinates.latitude);
                lng = String.valueOf(coordinates.longitude);

                taskCoordinates = String.format("%s|%s", lat, lng);
                Double latDou = Double.parseDouble(lat);
                Double lngDou = Double.parseDouble(lng);
                buscarDescricao(latDou,lngDou);

                Toast.makeText(getContext(), taskCoordinates, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                taskCoordinates = "null";
                Toast.makeText(getContext(), "RESULT_CANCELED", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void buscarDescricao(Double lat, Double lng){
        //new Connection().execute(taskCoordinates.split("|"));
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses != null && addresses.size() > 0 ){
                Address address = addresses.get(0);
                // Thoroughfare seems to be the street name without numbers
                String street, city, state, zip, country, result;

                street = address.getThoroughfare();
                zip = address.getPostalCode();
                city = address.getLocality();
                state = address.getAdminArea();
                country = address.getCountryName();

                result = String.format("%s (%s) - %s - %s - %s", street, zip, city, state, country);

                etTaskPlace.setText(result);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyEditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
        EditText _editText;
        private int _day;
        private int _month;
        private int _birthYear;

        public MyEditTextDatePicker(EditText editText)
        {
            this._editText = (EditText) editText;
            this._editText.setOnClickListener(this);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _birthYear = year;
            _month = monthOfYear;
            _day = dayOfMonth;
            updateDisplay();
        }
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            DatePickerDialog dialog = new DatePickerDialog(getContext(), this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();

        }

        // updates the date in the birth date EditText
        private void updateDisplay() {

            _editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
        }
    }
}
