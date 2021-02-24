package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.clicks;

import lombok.Data;

//Esta clase solo me srive para recibir datos json
@Data
public class Clicks {
    private int clicks;

    public Clicks(int clicks){
        this.clicks = clicks;
    }
}
