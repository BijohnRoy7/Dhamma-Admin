package invenz.example.bijohn.dhammaadmin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //private static final String SEND_NOTIFICATION_URL = "http://192.168.43.166/dhamma/SendNotification.php";
    private static final String SEND_NOTIFICATION_URL = "http://invenz-it.com/dhamma/SendNotification.php";
    private static final String TAG = "ROY" ;

    private Spinner spDate, spMonth, spYear, spHour, spMin, spAmPm;
    private EditText etEvent, etNotificationTitle, etNotificationMessage;
    private Button btAddEvent, btSetTimer, btShowAllEvents, btSendNotification;

    private List<String> years ;
    private List<String> dates;
    private List<String> months ,hours, mins, amPms;;
    private String[] day28, day30, day31;

    private String selectedDate= "", selectedMonth="", selectedYear="", eventText;

    private ArrayAdapter<String> yearAdapter, monthAdapter, dateAdapter, hourAdapter, minAdapter, amPmAdapter;

    private FirebaseFirestore firebaseFirestoreDb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spDate = findViewById(R.id.idDay);
        spMonth = findViewById(R.id.idMonth);
        spYear = findViewById(R.id.idYear);
        etEvent = findViewById(R.id.idEventText);
        btAddEvent = findViewById(R.id.idAddEvent);
        btSetTimer = findViewById(R.id.idTimer);
        btShowAllEvents = findViewById(R.id.idShowEvents);

        /*###                ###*/
        etNotificationTitle = findViewById(R.id.idNotificationTitle);
        etNotificationMessage = findViewById(R.id.idNotificationMessage);
        btSendNotification = findViewById(R.id.idNotification_mainAct);

        spHour = findViewById(R.id.idHour);
        spMin = findViewById(R.id.idMinute);
        spAmPm = findViewById(R.id.idAmPm);


        years = new ArrayList<>();
        years.add("Select Year");
        years.add("2018");
        years.add("2019");
        years.add("2020");

        months = new ArrayList<>();
        months.add("Select Month");
        for (int i=1;i<=12;i++){
            months.add(i+"");
        }

        dates = new ArrayList<>();

        yearAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, years);
        spYear.setAdapter(yearAdapter);
        spYear.setSelected(false);

        monthAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, months);
        spMonth.setAdapter(monthAdapter);
        spMonth.setEnabled(false);
        spMonth.setClickable(false);

        day28 = getResources().getStringArray(R.array.day_28);
        day30 = getResources().getStringArray(R.array.day_30);
        day31 = getResources().getStringArray(R.array.day_31);

        for (int i=0; i<day30.length;i++){
            dates.add(day30[i]+"");
        }


        hours = new ArrayList<>();
        for (int i=1; i<=12;i++){
            hours.add(i+"");
        }
        hourAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, hours);
        spHour.setAdapter(hourAdapter);


        mins = new ArrayList<>();
        for (int i=0; i<=60;i++){
            mins.add(i+"");
        }
        minAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, mins);
        spMin.setAdapter(minAdapter);

        amPms = new ArrayList<>();
        amPms.add("Am");
        amPms.add("Pm");
        amPmAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, amPms);
        spAmPm.setAdapter(amPmAdapter);


        spDate.setEnabled(false);
        spDate.setClickable(false);


        /*############################ SPINNER WORKS START HERE #####################################*/
        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*##################### When Year Selected #########################*/
                if (position!=0) {
                    selectedYear = years.get(position).toString();
                    Log.d("ROY", "onItemSelected: year " + selectedYear);

                    //select month
                    spMonth.setEnabled(true);
                    spMonth.setClickable(true);
                    spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position!=0 ){

                                selectedMonth = months.get(position);

                                //select date
                                spDate.setEnabled(true);
                                spDate.setClickable(true);

                                if (selectedMonth.equals("2")){
                                    dateAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, day28);
                                    spDate.setAdapter(dateAdapter);
                                }
                                else if(selectedMonth.equals("1") || selectedMonth.equals("3") || selectedMonth.equals("5") || selectedMonth.equals("7")  || selectedMonth.equals("8") || selectedMonth.equals("10") || selectedMonth.equals("12")){
                                    dateAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, day31);
                                    spDate.setAdapter(dateAdapter);
                                }
                                else {
                                    dateAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, day30);
                                    spDate.setAdapter(dateAdapter);
                                }

                                spDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                        selectedDate = dates.get(position);
                                        Log.d("ROY", "onItemSelected: "+selectedDate+"/"+selectedMonth+"/"+selectedYear);

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*############################ SPINNER ENDS START HERE #####################################*/



        /*############################## FOR ADDING NEW EVENT ####################################*/
        btAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!selectedDate.equals("") || !selectedMonth.equals("") || !selectedYear.equals("") || !eventText.equals("") ){
                    Log.d("ROY", "Button onItemSelected: "+selectedDate+"/"+selectedMonth+"/"+selectedYear);



                    if (selectedYear.equals("Select Year") || selectedMonth.equals("Select Month") || selectedDate.equals("Select Date")){
                        Toast.makeText(MainActivity.this, "Please Select year, month and date", Toast.LENGTH_SHORT).show();
                    }else {
                        Map<Object, String> eventMap = new HashMap<>();
                        eventMap.put("date", selectedDate);
                        eventMap.put("month", selectedMonth);
                        eventMap.put("year", selectedYear);
                        eventMap.put("event", eventText);

                        String doc = selectedDate + "_" + selectedMonth + "_" + selectedYear;

                        firebaseFirestoreDb.collection("AllEvents").document(doc).
                                set(eventMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "successfully added", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ROY", "onFailure: " + e);
                            }
                        });

                    }
                }
            }
        });




        /*#################################### FOR SETTING NEW TIMER ##################################*/
        btSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventText = etEvent.getText().toString();

                eventText = etEvent.getText().toString();
                String sHour, sMin, sAmPm;
                int intHour ;
                sHour = spHour.getSelectedItem().toString();
                sMin = spMin.getSelectedItem().toString();
                sAmPm = spAmPm.getSelectedItem().toString();


                if (!selectedDate.equals("") || !selectedMonth.equals("") || !selectedYear.equals("") || !eventText.equals("") || !sHour.equals("")  || !sMin.equals("") || !sMin.equals("")){

                    intHour = Integer.parseInt(sHour);
                    if (sAmPm == "Pm"){
                        intHour += 12;
                    }
                    sHour = String.valueOf(intHour);

                    Log.d("ROY", "onClick: " +intHour+":"+sMin+" "+sAmPm);


                    Map<Object, String> eventMap  = new HashMap<>();
                    eventMap.put("date", selectedDate);
                    eventMap.put("month",selectedMonth);
                    eventMap.put("year", selectedYear);
                    eventMap.put("event",eventText);
                    eventMap.put("hour",sHour);
                    eventMap.put("min",sMin);

                    firebaseFirestoreDb.collection("SetTimer").document("1").set(eventMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Timer successfully set", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ROY", "onFailure: "+e);
                        }
                    });

                }

            }
        });



        btShowAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowEvents.class));
            }
        });



        btSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String sTitle = etNotificationTitle.getText().toString().trim();
                final String sMessage = etNotificationMessage.getText().toString().trim();

                if (sTitle.isEmpty() || sMessage.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please give title and message for notification", Toast.LENGTH_SHORT).show();
                }else {

                    StringRequest stringRequest = new StringRequest(StringRequest.Method.POST, SEND_NOTIFICATION_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        //String serverMessage = jsonObject.getString("message");
                                        //Toast.makeText(MainActivity.this, ""+serverMessage, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: "+error);
                            error.getStackTrace();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> notificationMap = new HashMap<>();
                            notificationMap.put("title", sTitle);
                            notificationMap.put("message", sMessage);

                            return notificationMap;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                }
            }
        });

    }
}
