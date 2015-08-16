package hw5_final;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class Village {
    private ArrayList<Road> inRoads = new ArrayList<Road>();
    private ArrayList<Road> outRoads = new ArrayList<Road>();

    private int ID;
    protected int indegree;
    private static int num_of_village = 1;
    private int capacity;
    //position{row index, column index}
    private int[] position = new int[2];
    protected ArrayList<Village> stampsRequired = new ArrayList<Village>();
    protected ArrayList<Village> villagesRequireMe = new ArrayList<Village>();
    private ArrayList<Gnome> gnomes = new ArrayList<Gnome>();

    Dimension preferredSize = new Dimension((int) (UserInterface.FRAMEWIDTH * 0.95), (int) (UserInterface.FRAMEHEIGHT * 0.8));

    Random random = new Random();

    //constructor
    public Village(int positionX, int positionY, int Capacity) {
        this.position[0] = positionX;
        this.position[1] = positionY;
        this.ID = num_of_village++;
        this.capacity = Capacity;
        // random initialization of stamps required should be
        // handled by Map or GnomeWald, not a single Village.
        // this.stampsRequired = initStamp(Map.villages);
        this.indegree = this.inRoads.size();
    }//end of constructor

    //constructor
    public Village(int positionX, int positionY) {
        //default capacity is from 1 to 5
        this(positionX, positionY, (int) (Math.random() * 5 + 1));
    }//end of constructor


    public void addGnome(Gnome g) {
        this.gnomes.add(g);
    }

    public void removeGnome(Gnome g) {
        this.gnomes.remove(g);
    }

    public ArrayList<Gnome> getGnomes() {
        return gnomes;
    }

    public String getUpdateCurrGnome() {
        return Integer.toString(this.gnomes.size());
    }

    public void removeInRoads(Road r) {
        this.inRoads.remove(r);
        this.indegree--;
    }

    public void removeOutRoads(Road r) {
        this.outRoads.remove(r);
    }

    public void addInRoads(Road r) {
        this.inRoads.add(r);
        this.indegree++;
    }

    public void addOutRoads(Road r) {
        this.outRoads.add(r);
    }

    public void setCapacity(int diff) {
        this.capacity += diff;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getID() {
        return this.ID;
    }

    public int[] getPostion() {
        return this.position;
    }

    public ArrayList<Road> getInRoads() {
        return this.inRoads;
    }

    public ArrayList<Road> getOutRoads() {
        return this.outRoads;
    }

    public ArrayList<Village> getAdjacentVillages() {
        ArrayList<Village> adjacentVillages = new ArrayList<Village>();
        for (Road r : outRoads) {
            adjacentVillages.add(r.getEndVillage());
        }
        return adjacentVillages;
    }

    public void deleteVillage() {

        while (inRoads.size() > 0) {
            Road r = inRoads.remove(0);
            r.deleteRoad();
            this.indegree--;
        }

        while (outRoads.size() > 0) {
            Road r = outRoads.remove(0);
            r.deleteRoad();
        }
    }
}
