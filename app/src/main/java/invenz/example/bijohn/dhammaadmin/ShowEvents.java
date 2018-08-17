package invenz.example.bijohn.dhammaadmin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowEvents extends AppCompatActivity {

    private ListView eventListView;
    private List<Event> eventList;
    private EventCustomAdapter eventCustomAdapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events);

        eventListView = findViewById(R.id.idEventsList);
        eventList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();



        firebaseFirestore.collection("AllEvents").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (DocumentSnapshot doc: task.getResult()){
                        Map<String, Object> eventMap = doc.getData();

                        String date, month, year, myEvent;
                        date = eventMap.get("date").toString();
                        month = eventMap.get("month").toString();
                        year = eventMap.get("year").toString();
                        myEvent = eventMap.get("event").toString();
                        Log.d("ROY", "onComplete1: "+date);

                        Event event = new Event(date, month, year, myEvent);
                        eventList.add(event);

                    }

                    eventCustomAdapter = new EventCustomAdapter(getApplicationContext(), eventList);
                    eventListView.setAdapter(eventCustomAdapter);

                }else {
                    Log.d("ROY", "onComplete2 (show all eventList): "+task.getException());
                }
            }
        });




        eventListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Event event = eventList.get(position);
                String documentName = event.getDate()+"_"+event.getMonth()+"_"+event.getYear();
                Log.d("ROY", "onItemLongClick: "+documentName);

                firebaseFirestore.collection("AllEvents").document(documentName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d("ROY", "DocumentSnapshot successfully deleted!");
                        Toast.makeText(ShowEvents.this, "Event successfully deleted", Toast.LENGTH_SHORT).show();
                        eventCustomAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ROY", "Error deleting document", e);
                    }
                });;

                return false;
            }
        });

    }
}
