package hw5_final;

import java.util.Random;

public class Road implements Comparable<Road> {
    protected static final int MAX_CAPACITY = 5;
    protected static final int MAX_TIME = 10000;
    protected static final int MAX_COST = 10000;


    private Village startVillage;
    private Village endVillage;

    //cost proportional to road length
    private int cost;
    //how long to take to pass this road
    //equal time GUI draw gnome pass this road
    private int time;
    //random generate capacity in constructor
    private int capacity;

    Random random = new Random();

    public Road(Village start, Village end, int cost, int time, int capacity) {
        this.startVillage = start;
        this.endVillage = end;
        this.cost = cost;
        this.time = time;
        this.cost = cost;
        this.capacity = capacity;
    }

    public Road(Village start, Village end, int cost, int time) {
        this.startVillage = start;
        this.endVillage = end;
        this.cost = cost;
        this.time = time;
        this.cost = cost;
        this.capacity = random.nextInt(MAX_CAPACITY) + 1;
    }


    public Road(Village start, Village end) {
        this.startVillage = start;
        this.endVillage = end;
        this.cost = random.nextInt(MAX_COST) + 1000;
        this.time = random.nextInt(MAX_TIME) + 1000;
        this.capacity = random.nextInt(MAX_CAPACITY) + 1;
    }

    public void setCapacity(int change) {
        this.capacity += change;
    }

    public int getCost() {
        return this.cost;
    }

    public int getTime() {
        return this.time;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public Village getStartVillage() {
        return this.startVillage;
    }

    public Village getEndVillage() {
        return this.endVillage;
    }

    public void deleteRoad() {
        Village start = this.startVillage;
        Village end = this.endVillage;
        start.removeOutRoads(this);
        end.removeInRoads(this);
    }

    public double getLength() {
        return Map.calculateDistance(this.startVillage, this.endVillage);
    }

    @Override
    public int compareTo(Road other) {
        return Integer.compare(this.cost, other.cost);
    }
}
