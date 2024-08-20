//� A+ Computer Science  -  www.apluscompsci.com
//Name -
//Date -
//Class - 
//Lab  -

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Canvas;
import java.awt.Font;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Grid extends Canvas implements KeyListener, Runnable, MouseListener {

    private final boolean[] keys;
    private BufferedImage back;
    private final Square[][] matt;
    private final ArrayList<Snake> s;
    private int x = 0, y = 0;
    private int curr;
    private int startR1 = (int) (Math.random() * 12);
    private int startR2 = (int) (Math.random() * 15);
    private int limit;
    private int score;
    private int comment;
    private boolean pause;

    public Grid() {
        setBackground(Color.black);

        comment = 0;
        keys = new boolean[4];
        matt = new Square[12][15];
        for (Square[] matt1 : matt) {
            for (int j = 0; j < matt1.length; j++) {
                matt1[j] = new Square();
            }
        }
        score = 0;
        limit = 5;

        s = new ArrayList<>();

        s.add(new Snake(startR1, startR2));

        this.addKeyListener(this);
        new Thread(this).start();

        setVisible(true);
    }

    @Override
    public void update(Graphics window) {
        paint(window);
    }

    @Override
    public void paint(Graphics window) {

        Graphics2D twoDGraph = (Graphics2D) window;

        if (back == null) {
            back = (BufferedImage) (createImage(getWidth(), getHeight()));
        }

        Graphics graphToBack = back.createGraphics();

        graphToBack.setColor(Color.BLUE);
        graphToBack.drawString("Return Snake!", 25, 50);
        graphToBack.setColor(Color.WHITE);
        graphToBack.fillRect(0, 0, 954, 863);

        for (int i = 0; i < matt.length; i++) {
            for (int j = 0; j < matt[i].length; j++) {
                if (matt[i][j].getType() != 2) {
                    matt[i][j].setType(0);
                }
                x = i;
                y = j;
            }
        }

        for (int i = 0; i < s.size(); i++) {
            int r = s.get(i).getRow();
            int c = s.get(i).getColumn();
            if (i == 0) {
                matt[r][c].setType(4);
            } else {
                matt[r][c].setType(1);
            }
        }
        int count = 0;
        for (int i = 0; i < matt.length; i++) {
            for (int j = 0; j < matt[i].length; j++) {
                if (matt[i][j].getType() == 2) {
                    count++;
                    x = i;
                    y = j;
                }
            }
        }
        if (count == 0) {
            int r1 = ((int) (Math.random() * 10)) + 1;
            int r2 = ((int) (Math.random() * 13)) + 1;
            matt[r1][r2] = new Square(2);
            x = r1;
            y = r2;
        }
        matt[startR1][startR2].setType(3);

        int width = 2;
        int hight = 62;
        for (Square[] matt1 : matt) {
            for (Square item : matt1) {
                switch (item.getType()) {
                    case 0:
                        graphToBack.setColor(Color.BLACK);
                        break;
                    case 2:
                        graphToBack.setColor(Color.RED);
                        break;
                    case 4:
                        graphToBack.setColor(Color.GREEN);
                        break;
                    case 3:
                        graphToBack.setColor(Color.WHITE);
                        break;
                    default:
                        Color[] boi = {Color.CYAN, Color.BLUE, Color.GRAY, Color.PINK, Color.ORANGE, Color.MAGENTA, Color.YELLOW};
                        graphToBack.setColor(boi[(int) (Math.random() * 7)]);
                        break;
                }
                graphToBack.fillRect(width, hight, item.getSize(), item.getSize());
                width += (2 + item.getSize());
                if (width >= 890) {
                    width = 2;
                }
                if (width == 2) {
                    hight += (2 + item.getSize());
                }
            }
        }

        if (score == 1) {
            comment = 1;
        } else if (limit == 0) {
            comment = 2;
        } else if (limit == 1 && score == 4) {
            comment = 3;
        } else if (limit == 5 && score > 1) {
            comment = 4;
        }

        for (int i = 1; i < s.size(); i++) {
            if (s.get(0).getRow() == s.get(i).getRow() && s.get(0).getColumn() == s.get(i).getColumn() && Start.getTutorial() == true) {
                comment = 10;
            } else if (s.get(0).getRow() == s.get(i).getRow() && s.get(0).getColumn() == s.get(i).getColumn() && Start.getTutorial() == false) {
                try {
                    back = ImageIO.read(new File("gameOver.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
                pause = true;
                twoDGraph.drawImage(back, 0, 0, 954, 863, null, null);
                reSet();
            }
        }

        Color boi = graphToBack.getColor();
        graphToBack.setColor(Color.CYAN);
        graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 30));
        graphToBack.drawString("Score: " + score, 650, 40);
        graphToBack.drawString("Limit: " + limit, 40, 40);
        if (Start.getTutorial() == true || comment == 5 || comment == 100) {
            switch (comment) {
                case 0:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("Move around with the arrow buttons!", 155, 25);
                    graphToBack.drawString("Grab red fruit boxes to increase score!", 155, 50);
                    break;
                case 1:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("Great job your lenght and score increased,", 155, 25);
                    graphToBack.drawString("get more fruit to keep going!", 155, 50);
                    break;
                case 2:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("Uh-Oh, looks like your limit is 0, now there", 155, 25);
                    graphToBack.drawString("isn't enough room in your for more fruit", 155, 50);
                    break;
                case 3:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("If you try to eat more than you can, your", 155, 25);
                    graphToBack.drawString("score will go down, instead return to the ORIGIN", 155, 50);
                    break;
                case 4:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("Great job, once you return home, you get a", 155, 25);
                    graphToBack.drawString("pause and can keep going like the begining", 155, 50);
                    break;
                case 5:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("At this point you are good to go, now the", 155, 25);
                    graphToBack.drawString("reall game starts, Good luck!", 155, 50);
                    break;
                case 7:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("Keep Going!", 155, 40);
                    break;
                case 10:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("If this was reall you would have lost,", 155, 25);
                    graphToBack.drawString("can't tutch yourslef this is basic", 155, 50);
                    break;
                case 11:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("If this was reall you would have lost, you", 155, 25);
                    graphToBack.drawString("can't go off the grid, Im kinda disapointed", 155, 50);
                    break;
                case 100:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("When you are ready click on the window to start", 155, 25);
                    graphToBack.drawString("this super awsome game that is totaly original!", 155, 50);
                    break;
                default:
                    graphToBack.setFont(new Font("TimesRoman", Font.ROMAN_BASELINE, 25));
                    graphToBack.drawString("This is basically the game snake but with a", 155, 25);
                    graphToBack.drawString("return feature if you didn't already guess it", 155, 50);
                    break;
            }
        }

        if (score == 3) {
            comment = 7;
        }
        if (comment == 4 && score >= 7) {
            comment = 5;
        }

        if (comment == 5 && score >= 8 && !Start.getTutorial()) {
            pause = true;
            reSet();
        }

        graphToBack.setColor(boi);

        twoDGraph.drawImage(back, null, null);
        try {
            Thread.sleep(150);
        } catch (InterruptedException ex) {
            Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
        }
        doTheMove(twoDGraph);
        addTheTails();
    }

    public void reSet() {
        if (Start.getTutorial() == false) {
            Clip clip;
            try {
                AudioInputStream input = AudioSystem.getAudioInputStream(new File("gameIsOver.wav"));
                clip = AudioSystem.getClip();
                clip.open(input);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-20);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            }
        }
        try {
            Thread.sleep(1400);
        } catch (InterruptedException ex) {
            Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
        }
        back = (BufferedImage) (createImage(getWidth(), getHeight()));
        Start.setTutorial(false);
        startR1 = (int) (Math.random() * 12);
        startR2 = (int) (Math.random() * 15);
        limit = 5;
        score = 0;
        s.clear();
        s.add(new Snake(startR1, startR2));
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
        pause = false;
    }

    public void doTheMove(Graphics2D t) {
        if (keys[0] == true) {
            if (s.get(0).getColumn() - 1 >= 0) {
                s.add(0, new Snake(s.get(0).getRow(), s.get(0).getColumn() - 1));
                s.remove(s.size() - 1);
            } else if (Start.getTutorial() == true) {
                try {
                    back = ImageIO.read(new File("gameOver.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
                pause = true;
                t.drawImage(back, 0, 0, 954, 863, null, null);
                reSet();
            } else {
                comment = 11;
            }
        }
        if (keys[1] == true) {
            if (s.get(0).getColumn() + 1 <= 14) {
                s.add(0, new Snake(s.get(0).getRow(), s.get(0).getColumn() + 1));
                s.remove(s.size() - 1);
            } else if (Start.getTutorial() == true) {
                try {
                    back = ImageIO.read(new File("gameOver.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
                pause = true;
                t.drawImage(back, 0, 0, 954, 863, null, null);
                reSet();
            } else {
                comment = 11;
            }
        }
        if (keys[2] == true) {
            if (s.get(0).getRow() - 1 >= 0) {
                s.add(0, new Snake(s.get(0).getRow() - 1, s.get(0).getColumn()));
                s.remove(s.size() - 1);
            } else if (Start.getTutorial() == true) {
                try {
                    back = ImageIO.read(new File("gameOver.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
                pause = true;
                t.drawImage(back, 0, 0, 954, 863, null, null);
                reSet();
            } else {
                comment = 11;
            }
        }
        if (keys[3] == true) {
            if (s.get(0).getRow() + 1 <= 11) {
                s.add(0, new Snake(s.get(0).getRow() + 1, s.get(0).getColumn()));
                s.remove(s.size() - 1);
            } else if (Start.getTutorial() == true) {
                try {
                    back = ImageIO.read(new File("gameOver.png"));
                } catch (IOException ex) {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
                pause = true;
                t.drawImage(back, 0, 0, 954, 863, null, null);
                reSet();
            } else {
                comment = 11;
            }
        }
    }

    public void addTheTails() {
        if (s.get(0).getRow() == x && s.get(0).getColumn() == y && limit > 0) {
            new Thread(musicScore).start();
            score++;
            limit--;
            if (s.size() == 1) {
                if (keys[0] == true && s.get(s.size() - 1).getColumn() + 1 < 15) {
                    s.add(new Snake(s.get(s.size() - 1).getRow(), s.get(s.size() - 1).getColumn() + 1));
                }
                if (keys[1] == true && s.get(s.size() - 1).getColumn() - 1 >= 0) {
                    s.add(new Snake(s.get(s.size() - 1).getRow(), s.get(s.size() - 1).getColumn() - 1));
                }
                if (keys[2] == true && s.get(s.size() - 1).getRow() - 1 >= 0) {
                    s.add(new Snake(s.get(s.size() - 1).getRow() - 1, s.get(s.size() - 1).getColumn()));
                }
                if (keys[3] == true && s.get(s.size() - 1).getRow() + 1 < 12) {
                    s.add(new Snake(s.get(s.size() - 1).getRow() + 1, s.get(s.size() - 1).getColumn() + 1));
                }
            } else {
                int r = s.get(s.size() - 1).getRow();
                int c = s.get(s.size() - 1).getColumn();
                int rr = s.get(s.size() - 2).getRow();
                int cc = s.get(s.size() - 2).getColumn();
                if (r == rr && c > cc && c + 1 < 15) {
                    s.add(new Snake(r, c + 1));
                } else if (r == rr && cc > c && c - 1 >= 0) {
                    s.add(new Snake(r, c - 1));
                } else if (cc == c && r > rr && r + 1 < 12) {
                    s.add(new Snake(r + 1, c));
                } else {
                    if (r - 1 >= 0) {
                        s.add(new Snake(r - 1, c));
                    }
                }

            }
        } else if (s.get(0).getRow() == x && s.get(0).getColumn() == y && limit - 1 < 0) {
            if (s.size() > 2) {
                s.remove(s.size() - 1);
            }
            limit++;
            score -= 2;
            new Thread(minusP).start();
        } else if (s.get(0).getRow() == startR1 && s.get(0).getColumn() == startR2 && limit == 0) {
            new Thread(musicHome).start();
            keys[0] = false;
            keys[1] = false;
            keys[2] = false;
            keys[3] = false;
            limit += 5;
        }
        pause = false;
    }

    Thread musicScore = new Thread() {
        @Override
        public void run() {
            Clip clip;
            try {
                AudioInputStream input = AudioSystem.getAudioInputStream(new File("Point.wav"));
                clip = AudioSystem.getClip();
                clip.open(input);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-20);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            }
        }
    };
    Thread musicGameOver = new Thread() {
        @Override
        public void run() {
            Clip clip;
            try {
                AudioInputStream input = AudioSystem.getAudioInputStream(new File("gameIsOver.wav"));
                clip = AudioSystem.getClip();
                clip.open(input);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-20);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            }
        }
    };
    Thread musicHome = new Thread() {
        @Override
        public void run() {
            Clip clip;
            try {
                AudioInputStream input = AudioSystem.getAudioInputStream(new File("Home.wav"));
                clip = AudioSystem.getClip();
                clip.open(input);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-10);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            }
        }
    };
    Thread minusP = new Thread() {
        @Override
        public void run() {
            Clip clip;
            try {
                AudioInputStream input = AudioSystem.getAudioInputStream(new File("minusPoint.wav"));
                clip = AudioSystem.getClip();
                clip.open(input);
                FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-17);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            }
        }
    };

    @Override
    public void keyPressed(KeyEvent e) {
        if (pause == false) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && curr != KeyEvent.VK_RIGHT) {
                curr = KeyEvent.VK_LEFT;
                keys[0] = true;
                keys[1] = false;
                keys[2] = false;
                keys[3] = false;
                pause = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && curr != KeyEvent.VK_LEFT) {
                curr = KeyEvent.VK_RIGHT;
                keys[1] = true;
                keys[0] = false;
                keys[2] = false;
                keys[3] = false;
                pause = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP && curr != KeyEvent.VK_DOWN) {
                curr = KeyEvent.VK_UP;
                keys[2] = true;
                keys[0] = false;
                keys[1] = false;
                keys[3] = false;
                pause = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN && curr != KeyEvent.VK_UP) {
                curr = KeyEvent.VK_DOWN;
                keys[3] = true;
                keys[0] = false;
                keys[1] = false;
                keys[2] = false;
                pause = true;
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(5);
                repaint();
            }
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        comment = -1;
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
}
