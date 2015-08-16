package hw5_final;

import java.util.ArrayList;

public class Path {
    private ArrayList<Road> roadSeq;

    private long time;

    public Path() {
        this.roadSeq = new ArrayList<Road>();
    }

    public Path(ArrayList<Road> roadSeq) {
        this.roadSeq = roadSeq;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ArrayList<Road> getRoadSeq() {
        return this.roadSeq;
    }

    public long getTime() {
        return this.time;
    }

}