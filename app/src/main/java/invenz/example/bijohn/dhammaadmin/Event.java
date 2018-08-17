package invenz.example.bijohn.dhammaadmin;

public class Event {

    private String date, month, year, event;

    public Event(String date, String month, String year, String event) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.event = event;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
