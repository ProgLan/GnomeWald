package hw5_final;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class GnomeWald {
    protected final static int NUM_OF_INITIAL_GNOME = 5;
    protected final static int NUM_OF_INITIAL_Village = 5;
    protected final static int PROBABILITY_OF_VILLAGE_INTERCONNECTED = 5;

    protected static Map map;
    protected static ArrayList<Gnome> gnomes = new ArrayList<Gnome>();
    protected static HashMap<Road, RoadManager> roadManagers = new HashMap<Road, RoadManager>();

    static Random random = new Random();
    // world and gui interacts with each other,
    // so world should also have the ability to notify gui.
    static GUI gui;

    
    /**
     * ************************************
     * Constructor and initialization
     * *************************************
     */

    //constructor
    public GnomeWald() {
        map = new Map();
    }//end of constructor

    
    // have to init gui first,
    // separate these initialization methods from constructor
    // called by UserInterface
    public void initWald() {
        initVillages();
        initStampsRequired();
        tryTopoSort();
        initRoadsMST();
        initGnomes();
    }

    
    public void setGui(GUI g) {
        gui = g;
        repaintGUI();
    }

    
    public static void repaintGUI() {
        gui.repaint();
    }

    
    public Map getMap() {
        return map;
    }

    
    //set up initial map villages
    public static void initVillages() {
        for (int i = 0; i < NUM_OF_INITIAL_Village; i++) {
            Map.addVillage();
        }
    }


    // for each village,
    // it requires another village's stamp with 1/5 probability
    public static void initStampsRequired() {
        for (Village v1 : Map.villages)
            for (Village v2 : Map.villages) {
                if (v1 == v2) continue;
                int dice = random.nextInt(5);
                if (dice == 1) {
                    // v1 requires v2
                    v1.stampsRequired.add(v2);
                    v2.villagesRequireMe.add(v1);
                }
            }

        for (Village v : Map.villages) {
            System.out.print("Village " + v.getID() + " requires: ");
            for (Village v2 : v.stampsRequired)
                System.out.print(v2.getID() + " ");
            System.out.println();
        }
    }

    
    public static void initRoadsMST() {
        ArrayList<Road> mstRoads = Map.MST(Map.getAllRoads(Map.villages));
        for (Road road : mstRoads) {
            addRoad(road);
        }
    }

    //set up initial map gnomes
    public void initGnomes() {
        for (int i = 0; i < NUM_OF_INITIAL_GNOME; i++) {
            addGnome();
        }
    }

    
    public String tryTopoSort() {
    	String res = "TopoSort result: ";
        System.out.print("TopoSort result: ");
        for (Village v : Map.topologicalSort()) {
        	res += Integer.toString(v.getID()) + " ";
            System.out.print(v.getID() + " ");
        }
        System.out.println("(villages not listed are not reachable with the stamp requirement above)");
        res += "(villages not listed are not reachable with the stamp requirement above)";
        return res;
    }

    
    /**
     * ************************************
     * Village related methods
     * *************************************
     */

    public static void addVillage() {
        Map.addVillage();
        repaintGUI();
    }

    
    // connect the newest village to a closest village
    public static void connectLastVillage() {
        Village lastVillage = Map.villages.get(Map.villages.size() - 1);
        Village closestVillage = Map.getClosestVillage(lastVillage);
        addDoubleRoad(lastVillage.getID(), closestVillage.getID());
    }

    
    public static Village getVillageById(int VillageID) {
        return Map.getVillage(VillageID);
    }

    
    public void deleteVillage(int VillageID, int option) {
        Village v = Map.getVillage(VillageID);
        if (v == null)
            return;
        ArrayList<Gnome> villageGnome = v.getGnomes();
        while (villageGnome.size() > 0) {
            Gnome g = villageGnome.remove(0);
            gnomes.remove(g);
            g.killGnome();
        }
        map.deleteVillage(VillageID, option);
        repaintGUI();
    }

    
    /**
     * ************************************
     * Road related methods
     * *************************************
     */

    public static void addRoad(Road road) {
        Road newadd = Map.addRoad(road);
        if (newadd != null) {
            roadManagers.put(newadd, new RoadManager(newadd.getCapacity()));
        }
        repaintGUI();
    }

    
    public static void addRoad(int v1, int v2, int cost, int time) {
        Road newadd = Map.addRoad(v1, v2, cost, time);
        if (newadd != null) {
            roadManagers.put(newadd, new RoadManager(newadd.getCapacity()));
        }
        repaintGUI();
    }

    
    public static void addDoubleRoad(int v1, int v2) {
        addRoad(v1, v2);
        addRoad(v2, v1);
    }

    
    public static void addDoubleRoad(int v1, int v2, int cost, int time) {
        addRoad(v1, v2, cost, time);
        addRoad(v2, v1, cost, time);
    }

    
    public static void addRoad(int v1, int v2) {
        Road newadd = Map.addRoad(v1, v2);
        if (newadd != null) {
            roadManagers.put(newadd, new RoadManager(newadd.getCapacity()));
        }
        repaintGUI();
    }

    
    public static void addRoad(Village v1, Village v2, int cost, int time) {
        Road newadd = Map.addRoad(v1, v2, cost, time);
        if (newadd != null) {
            roadManagers.put(newadd, new RoadManager(newadd.getCapacity()));
        }
        repaintGUI();
    }

    
    /**
     * ************************************
     * Gnome related methods
     * *************************************
     */

    public void addGnome() {
        Gnome g = new Gnome(this, 1.0);
        gnomes.add(g);
        g.start();
        repaintGUI();
    }

    
    public ArrayList<Gnome> getGnomes() {
        return gnomes;
    }

    
    public static Gnome getGnomeById(int GnomeID) {
        for (Gnome g : gnomes) {
            if (g.getID() == GnomeID) {
                return g;
            }
        }
        return null;
    }


    public void gnomeGoTo(int gnomeID, int targetVillageID) {
        System.out.println("Wald: Gnome" + gnomeID + " go to " + targetVillageID);
        Gnome g = getGnomeById(gnomeID);
        Village v = getVillageById(targetVillageID);
        if (g == null || v == null) {
            return;
        }
        gnomeGoTo(g, v);
    }


    public void gnomeGoTo(Gnome gnome, Village targetVillage) {
        System.out.println("GO!");
        Village nextVillage = gnome.getNextVillage();
        if (gnome.getNextVillage() == null) {
            gnome.setNextVillage(targetVillage);
            gnome.setStatus(1);
        } else {
            System.out.println("Gnome already has next village " + nextVillage.getID());
        }
    }

    
    public void gnomeGo(int ID) {
        Gnome g = getGnomeById(ID);
        gnomeGo(g);
    }

    public void gnomeGo(Gnome g) {
        System.out.println("Wald: Gnome" + g.getID() + " random go");
        g.setStatus(Gnome.RANDOMWALK);
    }



    /**
     * ************************************
     * Gnome sync related methods
     * *************************************
     */

    public ArrayList<Path> getPaths(int V1ID, int V2ID) {
        return map.dfs(V1ID, V2ID);
    }

    public ArrayList<Path> getPaths(Village V1ID, Village V2ID) {
        return map.dfs(V1ID, V2ID);
    }

    public void requestRoad(Road road) throws InterruptedException {
        roadManagers.get(road).take();
    }

    public void releaseRoad(Road road) throws InterruptedException {
        roadManagers.get(road).give();
    }


    // ask about queueing time for road
    public int askRoad(Road road) {
        RoadManager roadManager = roadManagers.get(road);
        if (roadManager.size() > 0)
            return 0;
        return road.getTime() * (1 + roadManager.numberBlocked);
    }

}
