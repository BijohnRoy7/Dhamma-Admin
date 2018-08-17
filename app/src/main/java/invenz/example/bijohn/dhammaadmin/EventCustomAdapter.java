package invenz.example.bijohn.dhammaadmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class EventCustomAdapter extends BaseAdapter {

    private Context context;
    private List<Event> events;

    public EventCustomAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.single_event, parent, false);
        }

        Event event = events.get(position);

        TextView tvDate = convertView.findViewById(R.id.idDate_singleEvent);
        TextView tvEvent = convertView.findViewById(R.id.idEvent_singleEvent);

        tvDate.setText(event.getDate()+"/"+event.getMonth()+"/"+event.getYear());
        Log.d("ROY", "getView1: "+event.getDate()+"/"+event.getMonth());
        tvEvent.setText(event.getEvent());
        return convertView;
    }
}
