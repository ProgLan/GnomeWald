package hw5_final;


import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Map {
    protected static ArrayList<Village> villages = new ArrayList<Village>();
    protected static ArrayList<Road> roads = new ArrayList<Road>();

    static Dimension preferredSize = new Dimension((int) (UserInterface.FRAMEWIDTH * 0.8), (int) (UserInterface.FRAMEHEIGHT * 0.8));


    //constructor
    public Map() { }

    /**
     * **********************
     * Village related methods
     * ***********************
     */

    public static void addVillage() {
        int positionX = (int) (Math.random() * (preferredSize.getWidth() - 50) + 50);
        int positionY = (int) (Math.random() * (preferredSize.getWidth() - 100) + 100);
        addVillage(positionX, positionY);
    }


    public static void addVillage(int positionX, int positionY) {
        villages.add(new Village(positionX, positionY));
    }


    //return village by ID
    public static Village getVillage(int ID) {
        for (int i = 0; i < villages.size(); i++) {
            if (ID == villages.get(i).getID()) {
                return villages.get(i);
            }
        }
        return null;
    }


    public ArrayList<Village> getVillages() {
        return villages;
    }


   
    
    public void deleteVillage(int VillageID, int option) {

        System.out.println("null edge in roads");

        Village delete = getVillage(VillageID);
        
        //option2: connect the roads
        //connect outroads'destvillage to inroads start village
        if (option == 2) {
            ArrayList<Road> inRoads = delete.getInRoads();
            ArrayList<Road> outRoads = delete.getOutRoads();

            for (Road r : inRoads) {
                for (Road outr : outRoads) {
                    addRoad(r.getStartVillage(), outr.getEndVillage(), r.getCost() + outr.getCost(),
                            r.getTime() + outr.getTime());
                    // System.out.println("roads size: " + roads.size());
                }
            }
        }
        
        //option1: delete the roads connect to village;
        for (Road r : delete.getInRoads())
            roads.remove(r);
        for (Road r : delete.getOutRoads())
            roads.remove(r);

        delete.deleteVillage();
        villages.remove(delete);
    }


    //add a new add village to current graph
    public static Village getClosestVillage(Village newadd) {
        int mindist = Integer.MAX_VALUE;
        Village closeVillage = null;

        if (newadd.getInRoads().size() == 0 && newadd.getOutRoads().size() == 0) {
            for (int i = 0; i < villages.size() - 1; i++) {
                if (calculateDistance(newadd, villages.get(i)) < mindist) {
                    mindist = (int) (calculateDistance(newadd, villages.get(i)));
                    closeVillage = villages.get(i);
                }
            }
        }
        return closeVillage;
    }


    /**
     * **********************
     * Road related methods
     * ***********************
     */

    public ArrayList<Road> getRoads() {
        return roads;
    }

    //return road by start and end village
    public static Road getRoad(Village start, Village end) {
        for (Road r : roads) {
            if (r.getStartVillage().equals(start) && r.getEndVillage().equals(end)) {
                return r;
            }
        }
        return null;
    }

    
    public static double calculateDistance(Village v1, Village v2) {
        int[] pos1 = new int[2];
        int[] pos2 = new int[2];

        pos1[0] = v1.getPostion()[0];
        pos1[1] = v1.getPostion()[1];

        pos2[0] = v2.getPostion()[0];
        pos2[1] = v2.getPostion()[1];

        double horidis = Math.abs(pos2[0] - pos1[0]);
        double vertdis = Math.abs(pos2[1] - pos1[1]);

        double dist = Math.sqrt(Math.pow(horidis, 2) + Math.pow(vertdis, 2));

        return dist;

    }

    public static Road addRoad(int v1, int v2, int cost, int time) {
        return addRoad(getVillage(v1), getVillage(v2), cost, time);
    }

    public static Road addRoad(int v1, int v2) {
        return addRoad(getVillage(v1), getVillage(v2));
    }

    public static Road addRoad(Village start, Village end) {
        if (start == null || end == null || start.equals(end) || getRoad(start, end) != null) {
            return null;
        }

        Road newadd = new Road(start, end);
        start.addOutRoads(newadd);
        end.addInRoads(newadd);
        roads.add(newadd);
        return newadd;
    }

    public static Road addRoad(Road road) {
        road.getStartVillage().addOutRoads(road);
        road.getEndVillage().addInRoads(road);
        roads.add(road);
        return road;
    }

    public static Road addRoad(Village start, Village end, int cost, int time) {
        if (start == null || end == null || start.equals(end) || getRoad(start, end) != null) {
            return null;
        }

        Road newadd = new Road(start, end, cost, time);
        start.addOutRoads(newadd);
        end.addInRoads(newadd);
        roads.add(newadd);
        return newadd;
    }

    public static Road addRoad(Village start, Village end, int cost, int time, int capacity) {
        if (start == null || end == null || start.equals(end) || getRoad(start, end) != null) {
            return null;
        }

        Road newadd = new Road(start, end, cost, time, capacity);
        start.addOutRoads(newadd);
        end.addInRoads(newadd);
        roads.add(newadd);
        return newadd;
    }


    public static void addDoubleRoads(Village start, Village end, int cost, int time) {
        Road newRoad = addRoad(start, end, cost, time);
        addRoad(end, start, newRoad.getCost(), newRoad.getTime(), newRoad.getCapacity());
    }


    public void deleteRoad(Road road) {
        road.deleteRoad();
        roads.remove(road);
    }

    // ask the world to propose roads
    // don't add the roads to village.inroad / outroad
    // all the roads recorded in worlds
    // for v1 and v2, only return 1 single way road
    public static ArrayList<Road> getAllRoads(ArrayList<Village> vs) {
        ArrayList<Road> allRoads = new ArrayList<Road>();
        for (int i = 0; i < vs.size(); i++)
            for (int j = i + 1; j < vs.size(); j++)
                allRoads.add(new Road(vs.get(i), vs.get(j)));
        return allRoads;
    }

    
    /**
     * **********************
     * Algorithms
     * ***********************
     */


    public ArrayList<Path> dfs(int ID1, int ID2) {
        Village v1 = getVillage(ID1);
        Village v2 = getVillage(ID2);
        if (v1 == null || v2 == null || v1 == v2) {
            return null;
        } else return dfs(v1, v2);
    }

    //DFS
    public ArrayList<Path> dfs(Village start, Village end) {
        HashMap<Village, Boolean> visited = new HashMap<Village, Boolean>();
        for (Village v : villages) {
            visited.put(v, new Boolean(false));
        }

        ArrayList<ArrayList<Village>> res = new ArrayList<ArrayList<Village>>();

        helperdfs(start, end, visited, new ArrayList<Village>(), res);

        ArrayList<Path> finalres = new ArrayList<Path>();

        // from village sequence to road sequence
        for (ArrayList<Village> av : res) {
            Village prev = av.get(0);
            ArrayList<Road> temp = new ArrayList<Road>();
            for (int i = 1; i < av.size(); i++) {
                Village curr = av.get(i);
                Road rr = getRoad(prev, curr);
                temp.add(rr);
                prev = curr;
            }
            finalres.add(new Path(temp));
        }

        return finalres;
    }


    //DFS helper
    public void helperdfs(Village currVillage, Village end, HashMap<Village, Boolean> visited,
                          ArrayList<Village> past, ArrayList<ArrayList<Village>> res) {
        visited.put(currVillage, new Boolean(true));
        past.add(currVillage);

        if (currVillage.equals(end)) {
            //System.out.println(past.size());
            ArrayList<Village> p = new ArrayList<Village>(past);
            res.add(p);
        }

        for (Village v : currVillage.getAdjacentVillages()) {
            if (!visited.get(v)) {
                helperdfs(v, end, visited, past, res);
            }
        }

        visited.put(currVillage, new Boolean(false));
        past.remove(past.size() - 1);
    }



    public static ArrayList<Village> topologicalSort() {
        ArrayList<Village> zeroDegreeNodes = new ArrayList<Village>();
        ArrayList<Village> sortedNodes = new ArrayList<Village>();
        int[] inDegree = new int[villages.size() + 1];

        for (Village village : villages) {
            inDegree[village.getID()] = village.villagesRequireMe.size();
            if (inDegree[village.getID()] == 0)
                zeroDegreeNodes.add(village);
        }

        while (zeroDegreeNodes.size() > 0) {
            Village village = zeroDegreeNodes.iterator().next();
            zeroDegreeNodes.remove(village);
            sortedNodes.add(village);
            for (Village dst : village.stampsRequired) {
                inDegree[dst.getID()] --;
                if (inDegree[dst.getID()] == 0)
                    zeroDegreeNodes.add(dst);
            }
        }

        return sortedNodes;
    }

    // helper function for MST
    public static Village getFather(HashMap<Village, Village> fatherMap, Village village) {
        Village father = village;
        while (!fatherMap.get(father).equals(father))
            father = fatherMap.get(father);
        return father;
    }

    // Minimal spanning tree at the initial stage
    // Consider single way roads as undirected
    // return double roads: for n villages return (n - 1) * 2 roads
    public static ArrayList<Road> MST(ArrayList<Road> roadList) {
        List<Road> sortedRoads = new ArrayList<Road>(roadList);
        HashMap<Village, Village> fatherMap = new HashMap<Village, Village>();
        ArrayList<Road> finalRoads = new ArrayList<Road>();
        Collections.sort(sortedRoads);

        for (Village v : villages) {
            fatherMap.put(v, v);
        }

        for (Road road : sortedRoads) {
            Village v1 = road.getStartVillage();
            Village v2 = road.getEndVillage();
            if (!getFather(fatherMap, v1).equals(getFather(fatherMap, v2))) {
                finalRoads.add(road);
                finalRoads.add(new Road(road.getEndVillage(), road.getStartVillage(),
                        road.getCost(), road.getTime(), road.getCapacity()));
                fatherMap.put(v1, getFather(fatherMap, v2));
            }
        }

        return finalRoads;
    }


    //TODO
    public void Floyd() {
        int i, j, k, x, y;

        boolean[] reached = new boolean[villages.size()];
        int[] preNode = new int[villages.size()];

        reached[0] = true;
        for (i = 1; i < villages.size(); i++) {
            reached[i] = false;
        }

        preNode[0] = 0;

        for (k = 1; k < villages.size(); k++) {
            x = y = 0;
            for (i = 0; i < villages.size(); i++) {
                for (j = 0; j < villages.size(); j++) {
                    if (reached[i] && reached[j] &&
                            calculateDistance(villages.get(i), villages.get(j))
                                    < calculateDistance(villages.get(i), villages.get(j))) {
                        x = i;
                        y = j;
                    }
                }
                preNode[y] = x;
                reached[y] = true;
            }
        }
    }


}

