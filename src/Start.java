//� A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class - 
//Lab  -

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Start extends Canvas implements KeyListener, Runnable, MouseListener, MouseMotionListener {

    private final boolean[] keys;
    private BufferedImage back;
    private int selected = 0;
    private int prevSelect = 0;
    private boolean done = false;
    private static boolean tutorial = false;

    public Start() throws IOException {
        setBackground(Color.black);

        keys = new boolean[3];
        back = ImageIO.read(new File("ReturnSnakeB.jpg"));
        addd();
        setVisible(true);
    }

    private void addd(){
        this.addKeyListener(this);
        this.addMouseListener((MouseListener) this);
        this.addMouseMotionListener((MouseMotionListener) this);
        new Thread(this).start();
    }
    @Override
    public void update(Graphics window) {
        paint(window);
    }

    @Override
    public void paint(Graphics window) {
        Graphics2D twoDGraph = (Graphics2D) window;
        switch (selected) {
            case 1:
                try {
                    back = ImageIO.read(new File("ReturnSnakeBO1.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 2:
                try {
                    back = ImageIO.read(new File("ReturnSnakeBO2.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 3:
                try {
                    back = ImageIO.read(new File("ReturnSnakeBO3.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            default:
                try {
                    back = ImageIO.read(new File("ReturnSnakeB.jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
        if (keys[0] == true) {
            new Thread(choice).start();
            if (selected == 0) {
                selected = 2;
            } else if (selected > 1) {
                selected--;
            } else {
                selected = 3;
            }
        } else if (keys[1] == true) {
            new Thread(choice).start();
            if (selected == 0) {
                selected = 2;
            } else if (selected < 3) {
                selected++;
            } else {
                selected = 1;
            }
        } else if (keys[2] == true && selected != 2) {
            done = true;
        }
        twoDGraph.drawImage(back, 0, 0, 954, 863, null, null);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int s) {
        selected = s;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean d) {
        done = d;
    }
    
    public static boolean getTutorial(){
        return tutorial;
    }

    public static void setTutorial(boolean t){
        tutorial = t;
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[0] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[1] = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            new Thread(choice).start();
            keys[2] = true;
        }
        repaint();
    }

    Thread choice = new Thread() {
        public void run() {
            Clip clip;
            try {
                AudioInputStream input = AudioSystem.getAudioInputStream(new File("SelectMenu.wav"));
                clip = AudioSystem.getClip();
                clip.open(input);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-25);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    };

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            keys[0] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            keys[1] = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            keys[2] = false;
        }
        repaint();
    }

    public void keyTyped(KeyEvent e) {
        //no code needed here
    }

    public void run() {
        try {
            while (true) {
                Thread.currentThread().sleep(5);
                repaint();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getX() >= 200 && me.getY() <= 720 && me.getY() >= 330 && me.getY() <= 450) {
            selected = 1;
            done = true;
        } else if (me.getX() >= 200 && me.getY() <= 730 && me.getY() >= 460 && me.getY() <= 620) {
            selected = 2;
            done = true;
        } else if (me.getX() >= 180 && me.getY() <= 720 && me.getY() >= 630 && me.getY() <= 830) {
            selected = 3;
            done = true;
        } else {
            selected = 0;
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mouseDragged(MouseEvent me) {
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if (me.getX() >= 200 && me.getY() <= 700 && me.getY() >= 330 && me.getY() <= 450) {
            selected = 1;
            if (prevSelect != selected) {
                new Thread(choice).start();
            }
        } else if (me.getX() >= 200 && me.getY() <= 730 && me.getY() >= 460 && me.getY() <= 610) {
            selected = 2;
            if (prevSelect != selected) {
                new Thread(choice).start();
            }
        } else if (me.getX() >= 180 && me.getY() <= 720 && me.getY() >= 650 && me.getY() <= 810) {
            selected = 3;
            if (prevSelect != selected) {
                new Thread(choice).start();
            }
        } else {
            selected = 0;
        }
        prevSelect = selected;
        repaint();
    }
}
