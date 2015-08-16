package hw5_final;


import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class Gnome extends Thread {
    protected final static int HAVEDESTINATION = 1;
    protected final static int RANDOMWALK = 0;
    protected final static int STAY = 2;

    //the world this gnome is in
    private static GnomeWald gw;
    private int ID;
    private static int num_of_gnomes = 1;
    private volatile boolean isRunning = true;

    //gnome and village don't have affiliation
    private Village currVillage;
    //destination village
    private Village nextVillage;
    //which villages the gnome has been visited
    private ArrayList<Village> stamps = new ArrayList<Village>();
    //urge between (0,1), cost of road = u*time +(1-u)*cost
    private double urge;
    //3 status: STAY, RANDOMWALK, HAVEDESTINATION 
    private int status;
    protected int[] currPosition = new int[2];
    Road currRoad; 
    
    Random random = new Random();
    Dimension preferredSize = new Dimension((int) (UserInterface.FRAMEWIDTH * 0.95), (int) (UserInterface.FRAMEHEIGHT * 0.8));


    /**
     * **********************
     * Constructor and Initialization
     * ***********************
     */


    //Constructor
    public Gnome(GnomeWald gw) {
        this(gw, 1.0);
    }//end of constructor


    //Constructor
    public Gnome(GnomeWald gwald, double urge) {
        this.ID = num_of_gnomes++;
        gw = gwald;
        this.urge = urge;
        ArrayList<Village> optionVillage = gw.getMap().getVillages();
        this.currVillage = optionVillage.get((int) (Math.random() * optionVillage.size()));
        // this.currVillage.addGnome(this);
        this.nextVillage = null;
        this.status = STAY;
        updateCurrentPosition();
    }//end of constructor


    /**
     * **********************
     * Getter and Setter
     * ***********************
     */

    public int getID() {
        return ID;
    }


    public Village getCurrVillage() {
        return this.currVillage;
    }


    public void setStatus(int status) {
        //System.out.println(ID + ": Status " + this.status + " -> " + status);
        this.status = status;
    }


    public int getStatus() {
        return this.status;
    }


    public void setNextVillage(Village targetVillage) {
        this.nextVillage = targetVillage;
    }


    public Village getNextVillage() {
        return this.nextVillage;
    }


    /**
     * **********************
     * Gnome move related methods
     * ***********************
     */

    public Path getPath() {
        ArrayList<Path> paths = gw.getPaths(currVillage, nextVillage);

        double minCost = Double.MAX_VALUE;
        Path bestPath = null;
        for (Path path : paths) {
            Double roadCost = 0.0;
            try {
                roadCost = urge * gw.askRoad(path.getRoadSeq().get(0));
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Curr Village" + currVillage.getID() + " " + nextVillage.getID());
            }
            long time = gw.askRoad(path.getRoadSeq().get(0));
            for (Road road : path.getRoadSeq()) {
                roadCost += urge * road.getTime() + (1 - urge) * road.getCost();
                time += road.getTime();
            }

            path.setTime(time);
            if (roadCost < minCost) {
                minCost = roadCost;
                bestPath = path;
            }
        }
        return bestPath;
    }


    public void showMovement(Road r) {
        System.out.println("Road time" + r.getTime());
        try {
            sleep(r.getTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void leaveVillage() {
        System.out.println("Gnome " + ID + " leaves village " + currVillage.getID());
        if (currVillage.getGnomes().contains(this)) {
            // if gnome was staying here
            currVillage.removeGnome(this);
            currVillage.setCapacity(+1);
        }
        // on the road
        currVillage = null;
    }

    
    public void enterVillage(Village v) {
        System.out.println("Gnome " + ID + " enters village " + v.getID());
        this.currVillage = v;
        stamps.add(v);
        //if arrive at destination
        if (currVillage.equals(nextVillage) && this.status == HAVEDESTINATION) {
            System.out.println("Gnome " + ID + " arrives at " + currVillage.getID());
            nextVillage = null;
            stayAtCurrentVillage();
        }
        updateCurrentPosition();
    }


    public void stayAtCurrentVillage() {
        if (currVillage == null) {
            System.out.println("Gnome " + ID + " try to stay when not at any village.");
        }
        if (currVillage.getGnomes().contains(this)) {
            // already stayed at the village
            return;
        }
        if (currVillage.getCapacity() > 0) {
            // if can stay here
            currVillage.setCapacity(-1);
            currVillage.addGnome(this);
            this.status = STAY;
            updateCurrentPosition();
            System.out.println("Gnome " + this.ID + " stays at village " + currVillage.getID());
        } else {
            // if failed to stay here
            // wait a moment then randomly go to an adjacent village
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.status = RANDOMWALK;
        }
    }

    
    //random walk to adjacent village
    public Village randomAdjacentVillage() {
        ArrayList<Village> options = this.currVillage.getAdjacentVillages();
        int randomIndex = random.nextInt(options.size());
        this.nextVillage = options.get(randomIndex);
        return options.get(randomIndex);
    }


    //when delete a village, kill all gnomes in it
    public void killGnome() {
        this.isRunning = false;
    }


    public void updateCurrentPosition() {
        //if this gnome is staying at its current village
    	if (this.currVillage != null) {
            this.currPosition[0] = this.currVillage.getPostion()[0];
            this.currPosition[1] = this.currVillage.getPostion()[1];
            GnomeWald.repaintGUI();
            //if this gnome is on the road to another village
        } else if (currRoad != null) {
            int roadTime = currRoad.getTime();
            int x1 = currRoad.getStartVillage().getPostion()[0];
            int y1 = currRoad.getStartVillage().getPostion()[1];
            int x2 = currRoad.getEndVillage().getPostion()[0];
            int y2 = currRoad.getEndVillage().getPostion()[1];

            double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
            // roadTime is the time we take in milliseconds
            // stepLength is the length we move each second
            int unit = 100;
            double stepLength = distance / (roadTime / unit);
            double sin = (y2 - y1) / distance;
            double cos = (x2 - x1) / distance;
            for (int i = 0; i < roadTime - unit; i += unit) {
                x1 += (int) (stepLength * cos);
                y1 += (int) (stepLength * sin);
                this.currPosition[0] = x1;
                this.currPosition[1] = y1;
                GnomeWald.repaintGUI();
                try {
                    sleep(unit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("!! Gnome " + ID + " have neither current village or current road!");
        }
    }

    
    /**
     * ************************************
     * Synchronization methods
     * *************************************
     */

	/*
	 When status is STAY, try to stay at current village
	 Whenever failed to stay (current village out of capacity), wait 5 seconds
	 and go to status RANDOMWALK
	 Whenever succeeded to stay, do nothing
	 When status is HAVEDESTINATION, try to go to nextVillage, after that, try to stay
	 When status is RAMDOMWALK, get a random adjacent village and go to status HAVEDESTINATION
	 with that village as nextVillage
	 */
    @Override
    public void run() {
        while (isRunning) {
            while (this.status == HAVEDESTINATION) {
                //arrive destination
            	if (currVillage.equals(nextVillage)) {
                    nextVillage = null;
                    this.status = STAY;
                }
                Path path = getPath();
                if (path == null) {
                    nextVillage = null;
                    this.status = STAY;
                    continue;
                }
                Road road = path.getRoadSeq().get(0);
                this.currRoad = road;
                try {
                    System.out.println("Gnome " + ID + " waiting");
                    gw.requestRoad(road);
                    leaveVillage();
                    updateCurrentPosition();
                    enterVillage(road.getEndVillage());
                    gw.releaseRoad(road);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }//end of status HAVEDESTINATION

            if (this.status == RANDOMWALK) {
                nextVillage = randomAdjacentVillage();
                System.out.println("Gnome " + ID + " random go to Village" + nextVillage.getID());
                this.status = HAVEDESTINATION;
            }//end of status RANDOMWALK

            if (this.status == STAY) {
                stayAtCurrentVillage();
            }//end of status STAY
        
        }//end of main loop
    }
}