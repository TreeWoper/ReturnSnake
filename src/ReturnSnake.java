//� A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date - 
//Class -
//Lab  -

import java.awt.AWTException;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class ReturnSnake extends JFrame {

    private static final int WIDTH = 949;
    private static final int HEIGHT = 873;


    public ReturnSnake() {
        super("Return Snake");
        setSize(WIDTH, HEIGHT);
        setResizable(true);
        startScreen();
    }
    static Thread music = new Thread() {
        @Override
        public void run() {
            Clip clip;
            try {
                AudioInputStream input = AudioSystem.getAudioInputStream(new File("Free Synthwave Loop.wav"));
                clip = AudioSystem.getClip();
                clip.open(input);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-25);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    };

    Thread mouse = new Thread() {
        @Override
        public void run() {
            Image image = null;
            Cursor c = null;
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            while (mouse.isAlive()) {
                image = toolkit.getImage("Cursor.png");
                c = toolkit.createCustomCursor(image, new Point(getContentPane().getX(), getContentPane().getY()), "img");
                getContentPane().setCursor(c);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
                image = toolkit.getImage("Cursor2.png");
                c = toolkit.createCustomCursor(image, new Point(getContentPane().getX(), getContentPane().getY()), "img");
                getContentPane().setCursor(c);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
                image = toolkit.getImage("Cursor3.png");
                c = toolkit.createCustomCursor(image, new Point(getContentPane().getX(), getContentPane().getY()), "img");
                getContentPane().setCursor(c);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
                image = toolkit.getImage("Cursor4.png");
                c = toolkit.createCustomCursor(image, new Point(getContentPane().getX(), getContentPane().getY()), "img");
                getContentPane().setCursor(c);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    };

    public void startGame() {
        Grid theGame = new Grid();
        ((Component) theGame).setFocusable(true);
        getContentPane().add(theGame);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        music.start();
        setVisible(true);
        
    }

    private void startScreen() {
        mouse.start();
        Start sup = null;
        try {
            sup = new Start();
        } catch (IOException ex) {
            Logger.getLogger(ReturnSnake.class.getName()).log(Level.SEVERE, null, ex);
        }
        getContentPane().removeAll();
        getContentPane().add(sup);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Clip clip = null;
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File("Hypnotic-Puzzle4.wav"));
            clip = AudioSystem.getClip();
            clip.open(input);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-25);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        }
        setVisible(true);
        while (sup.getDone() == false) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException ex) {
                Logger.getLogger(ReturnSnake.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            Thread.sleep(400);
        } catch (InterruptedException ex) {
            Logger.getLogger(ReturnSnake.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (sup.getDone() && sup.getSelected() == 1) {
            getContentPane().removeAll();
            clip.stop();
            startGame();
        } else if (sup.getDone() && sup.getSelected() == 3) {
            getContentPane().removeAll();
            clip.stop();
            Start.setTutorial(true);
            startGame();
        }
    }

    public static void main(String args[]) {
        ReturnSnake snekek = new ReturnSnake();
    }
}
