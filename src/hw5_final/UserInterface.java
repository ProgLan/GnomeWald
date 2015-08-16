package hw5_final;

import javax.swing.*;

/*
 Main() starts here 
 */

public class UserInterface {
    public static final int FRAMEWIDTH = 800;
    public static final int FRAMEHEIGHT = 800;

    public static void main(String[] args) {
        GnomeWald gw = new GnomeWald();

        //set the basic GUI feature
        JFrame frame = new JFrame("GnomeWald");
        frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GUI gui = new GUI(gw);
        gw.setGui(gui);
        gw.initWald();

        frame.add(gui);
        frame.setVisible(true);
    }

}