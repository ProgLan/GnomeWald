package hw5_final;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI extends JPanel implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 6596867623741923914L;
    GnomeWald gw;
    Map map;

    Dimension preferredSize = new Dimension((int) (UserInterface.FRAMEWIDTH * 0.8), (int) (UserInterface.FRAMEHEIGHT * 0.8));

    /**
     * **********************
     * Main panel elements
     * ***********************
     */

    JButton SearchButton = new JButton("Search a gnome");
    JButton AddButton = new JButton("Add a Village");
    JButton AddRoadButton = new JButton("Add a road between Villages");
    JButton DeleteButton = new JButton("Delete a Village");
    JButton MoveButton = new JButton("Move 3 Gnomes");
    JButton ShowTopoSortButton = new JButton("Show TopoSort");
    //JTextField topoSortList = new JTextField(gw.tryTopoSort());


    /**
     * **********************
     * Delete button elements
     * ***********************
     */

    JFrame deleteOptionInput = new JFrame("Delete Option");
    JTextField deleteVillageID = new JTextField("Delete Village ID");
    JButton deleteAll = new JButton("DeleteAllConnectWay");
    JButton deleteReconnect = new JButton("DeleteAndReconnectVillage");
    JPanel panel = new JPanel();

    //delete village option
    int op = 0;
    //delete village ID
    int vid = 0;


    /**
     * **********************
     * Search button elements
     * ***********************
     */
    JFrame searchOptionInput = new JFrame("Search a gnome");
    JTextField searchGnomeID = new JTextField("Put a Gnome ID");
    JTextField searchRes = new JTextField("Result shows here              ");
    JButton search = new JButton("Search!!!");
    JPanel panel2 = new JPanel();

    //delete village option
    int searchGID = 0;


    /**
     * **********************
     * Move gnome button elements
     * ***********************
     */
    JFrame moveGnomeOptionInput = new JFrame("Move the Gnome");
    JTextField gnomeID = new JTextField("Input the gnome's ID");
    JTextField gnomeID2 = new JTextField("Input the gnome's ID");
    JTextField gnomeID3 = new JTextField("Input the gnome's ID");
    JButton randomMoveOption = new JButton("Ramdom Move");
    JButton speicifiedMoveOption = new JButton("To your assigned Village");
    JPanel panel4 = new JPanel();
    //if choose to a speicified village
    JFrame moveGnomeToAssignedVillage = new JFrame("Move the Gnome to your favorite village");
    JTextField moveVillageID = new JTextField("Input your favorite village ID");
    JButton moveGnome = new JButton("Move!!!");
    JPanel panel5 = new JPanel();

    int gID = 0;
    int gID2 = 0;
    int gID3 = 0;
    int moveVID = 0;


    /**
     * **********************
     * Add road button elements
     * ***********************
     */

    JFrame addRoadOptionInput = new JFrame("Add Road Between Two Villages");
    JTextField firstVillageInput = new JTextField("Input first Village ID");
    JTextField secondVillageInput = new JTextField("Input second Village ID");
    JTextField doubleOneWayOption = new JTextField("Add double or one-way road");
    JButton addOneRoad = new JButton("AddOneWayRoad");
    JButton addDoubleRoad = new JButton("AddTwoWayRoad");
    JPanel panel3 = new JPanel();

    int firstVID = 0;
    int secondVID = 0;
    

    /**
     * **********************
     * Constructor and Initialization
     * ***********************
     */

    public GUI(GnomeWald gw1) {
        this.setBackground(Color.GRAY);
        this.setOpaque(true);
        //gw has gnome info
        this.gw = gw1;
        //map has road and village info
        this.map = gw.getMap();


        this.add(SearchButton, BorderLayout.SOUTH);
        this.add(AddButton, BorderLayout.SOUTH);
        this.add(DeleteButton, BorderLayout.SOUTH);
        this.add(AddRoadButton, BorderLayout.SOUTH);
        this.add(MoveButton, BorderLayout.SOUTH);
        this.add(ShowTopoSortButton, BorderLayout.SOUTH);
        //this.add(topoSortList);
        //topoSortList.setEditable(false);
        
        /*******
         * ShowTopoSortButton
         */
        ShowTopoSortButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(panel, gw.tryTopoSort());
			}
        	
        });

        /*******
         * SearchButton
         */
        SearchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                searchRes.setEditable(false);
                panel2.add(searchGnomeID);
                panel2.add(searchRes);
                panel2.add(search);
                searchOptionInput.add(panel2);
                //searchOptionInput.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                searchOptionInput.pack();
                searchOptionInput.setVisible(true);

                search.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String option = searchGnomeID.getText();
                            searchGID = Integer.parseInt(option);
                            String res = "Current Pos is in Village " + GnomeWald.getGnomeById(searchGID).getCurrVillage().getID();
                            searchRes.setText(res);
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(panel3, "Put a number please!");
                        } catch (NullPointerException npe) {
                            JOptionPane.showMessageDialog(panel3, "Don't put a gnome ID that doesn't exist!");
                        }

                    }

                });

            }

        });

        /*******
         * DeleteButton
         */
        DeleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    panel.add(deleteVillageID);
                    panel.add(deleteAll);
                    panel.add(deleteReconnect);
                    deleteOptionInput.add(panel);
                    //deleteOptionInput.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    deleteOptionInput.pack();
                    deleteOptionInput.setVisible(true);

                    deleteAll.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String villageID = deleteVillageID.getText();
                            vid = Integer.parseInt(villageID);
                            gw.deleteVillage(vid, 1);
                        }
                    });

                    deleteReconnect.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String villageID = deleteVillageID.getText();
                            vid = Integer.parseInt(villageID);
                            gw.deleteVillage(vid, 2);
                        }
                    });
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(panel3, "Put a number please!");
                } catch (NullPointerException npe) {
                    JOptionPane.showMessageDialog(panel3, "Don't put a village ID that doesn't exist!");
                }

            }

        });

        /*******
         * AddButton
         */
        AddButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            	GnomeWald.addVillage();
            	GnomeWald.connectLastVillage();
                gw.addGnome();
            }

        });

        /*******
         * AddRoadButton
         */
        AddRoadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel3.add(firstVillageInput);
                panel3.add(secondVillageInput);
                panel3.add(addOneRoad);
                panel3.add(addDoubleRoad);
                addRoadOptionInput.add(panel3);
                addRoadOptionInput.pack();
                addRoadOptionInput.setVisible(true);
                addOneRoad.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            String firstVid = firstVillageInput.getText();
                            String secndVid = secondVillageInput.getText();

                            firstVID = Integer.parseInt(firstVid);
                            secondVID = Integer.parseInt(secndVid);

                            int cost = (int) (Math.random() * 10);
                            int time = (int) (Math.random() * 1000 + 1000);
                            GnomeWald.addRoad(firstVID, secondVID, cost, time);
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(panel3, "Put a number please!");
                        } catch (NullPointerException npe) {
                            JOptionPane.showMessageDialog(panel3, "Don't put a village ID that doesn't exist!");
                        }

                    }
                });

                addDoubleRoad.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        try {
                            String firstVid = firstVillageInput.getText();
                            String secndVid = secondVillageInput.getText();

                            firstVID = Integer.parseInt(firstVid);
                            secondVID = Integer.parseInt(secndVid);
                            //add one way road between villages

                            int cost = (int) (Math.random() * 10);
                            int time = (int) (Math.random() * 1000 + 1000);
                            GnomeWald.addDoubleRoad(firstVID, secondVID, cost, time);
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(panel3, "Put a number please!");
                        } catch (NullPointerException npe) {
                            JOptionPane.showMessageDialog(panel3, "Don't put a village ID that doesn't exist!");
                        }

                    }
                });
            }
        });

        /*******
         * MoveButton
         */
        MoveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel4.add(gnomeID);
                panel4.add(gnomeID2);
                panel4.add(gnomeID3);
                panel4.add(randomMoveOption);
                panel4.add(speicifiedMoveOption);
                moveGnomeOptionInput.add(panel4);
                moveGnomeOptionInput.pack();
                moveGnomeOptionInput.setVisible(true);

                //option1: move randomly
                randomMoveOption.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            // random move
                            String strGID = gnomeID.getText();
                            int gID = Integer.parseInt(strGID);
                            String strGID2 = gnomeID2.getText();
                            int gID2 = Integer.parseInt(strGID2);
                            String strGID3 = gnomeID3.getText();
                            int gID3 = Integer.parseInt(strGID3);
                            System.out.println("GUI: Gnome " + gID + " random go");
                            gw.gnomeGo(gID);
                            gw.gnomeGo(gID2);
                            gw.gnomeGo(gID3);
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(panel3, "Put a number please!");
                        } catch (NullPointerException npe) {
                            JOptionPane.showMessageDialog(panel3, "Don't put a village ID that doesn't exist!");
                        }

                    }

                });

                //option2: to a specified destination
                speicifiedMoveOption.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        panel5.add(moveVillageID);
                        panel5.add(moveGnome);
                        moveGnomeToAssignedVillage.add(panel5);
                        moveGnomeToAssignedVillage.pack();
                        moveGnomeToAssignedVillage.setVisible(true);

                        moveGnome.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    //move to a dest village
                                    String strGID = gnomeID.getText();
                                    int gID = Integer.parseInt(strGID);
                                    String strGID2 = gnomeID2.getText();
                                    int gID2 = Integer.parseInt(strGID2);
                                    String strGID3 = gnomeID3.getText();
                                    int gID3 = Integer.parseInt(strGID3);

                                    moveVID = Integer.parseInt(moveVillageID.getText());

                                    //GnomeWald.printPath(GnomeWald.getGnomeById(gID));
                                    System.out.println("GUI: Gnome " + gID + " go to Village " + moveVID);
                                    System.out.println("GUI: Gnome " + gID2 + " go to Village " + moveVID);
                                    System.out.println("GUI: Gnome " + gID3 + " go to Village " + moveVID);
                                    gw.gnomeGoTo(gID, moveVID);
                                    gw.gnomeGoTo(gID2, moveVID);
                                    gw.gnomeGoTo(gID3, moveVID);
                                } catch (NumberFormatException nfe) {
                                    JOptionPane.showMessageDialog(panel3, "Put a number please!");
                                } catch (NullPointerException npe) {
                                    JOptionPane.showMessageDialog(panel3, "Don't put a village ID that doesn't exist!");
                                }
                            }

                        });
                    }

                });
            }

        });

    }


    /**
     * **********************
     * Paint related methodS
     * ***********************
     */

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw Road
        for (Road r : map.getRoads()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.lightGray);
            if (r != null) {
                g2.drawLine(r.getStartVillage().getPostion()[0], r.getStartVillage().getPostion()[1], r.getEndVillage().getPostion()[0], r.getEndVillage().getPostion()[1]);
            }
        }

        //draw Village
        for (Village v : map.getVillages()) {
            g.setColor(Color.GREEN);
            g.fillRect(v.getPostion()[0], v.getPostion()[1], (int) (preferredSize.getWidth() / 30), (int) (preferredSize.getHeight() / 30));
            //g.draw3DRect(v.getPostion()[0], v.getPostion()[1], (int)(preferredSize.getWidth()/30), (int)(preferredSize.getHeight()/30), true);
            g.setColor(Color.black);
            g.drawString("V" + v.getID(), v.getPostion()[0], v.getPostion()[1]);
            g.drawString("CurNum" + v.getUpdateCurrGnome(), v.getPostion()[0] + (int) (preferredSize.getWidth() / 50), v.getPostion()[1] + (int) (preferredSize.getHeight() / 50));

        }

        // draw Gnomes
        for (Gnome gnome : gw.getGnomes()) {
            g.setColor(Color.RED);
            try {
                g.drawOval(gnome.currPosition[0], gnome.currPosition[1], 20, 20);
                g.drawString("G" + gnome.getID(), gnome.currPosition[0] + 18, gnome.currPosition[1] + 30);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }


}