package app;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int boardWidth = 550;
    int boardHeight = 840;

    // bird attributes
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;
    int jumpVelocity = -10;
    int gravity = 1;
    int birdVelocityY = 0;

    BufferedImage background;
    BufferedImage birdImage;

    // Pipes
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeGap = 200;
    int pipeSpeed = -4;
    ArrayList<Pipe> pipes = new ArrayList<>();

    Timer gameTimer;
    Timer pipeSpawner;
    boolean gameover = false;
    double score = 0;

    public FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        try {
            background = ImageIO.read(new File("C:\\Users\\Omar\\Documents\\NetBeansProjects\\App\\src\\app\\Screenshot 2024-08-12 172607 (2).png"));
            birdImage = ImageIO.read(new File("C:\\Users\\Omar\\Documents\\NetBeansProjects\\App\\src\\app\\Screenshot_2024-08-12_220510__1_-removebg-preview.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Timer for game loop
        gameTimer = new Timer(1000 / 60, this);
        gameTimer.start();

        // Timer for spawning pipes
        pipeSpawner = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnPipes();
            }
        });
        pipeSpawner.start();
    }

    public void spawnPipes() {
        int yOffset = (int) (Math.random() * (pipeHeight /2+3));
        pipes.add(new Pipe(boardWidth, -pipeHeight + yOffset, pipeWidth, pipeHeight, true));
        pipes.add(new Pipe(boardWidth, yOffset + pipeGap, pipeWidth, pipeHeight, false));
    }

    public void move() {
        birdVelocityY += gravity;
        birdY += birdVelocityY;

        for (Pipe pipe : pipes) {
            pipe.x += pipeSpeed;
            if (checkCollision(pipe)) {
                gameover = true;
            }
        }

        if (birdY > boardHeight || birdY < 0) {
            gameover = true;
        }

        pipes.removeIf(pipe -> pipe.x + pipeWidth < 0);

        if (gameover) {
            gameTimer.stop();
            pipeSpawner.stop();
            JOptionPane.showMessageDialog(this, "Game Over. Score: " + (int) score, "Game Over", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean checkCollision(Pipe pipe) {
        Rectangle birdRect = new Rectangle(birdX, birdY, birdWidth, birdHeight);
        Rectangle pipeRect = new Rectangle(pipe.x, pipe.y, pipe.width, pipe.height);
        return birdRect.intersects(pipeRect);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this);

        g.drawImage(birdImage, birdX, birdY, this);

        for (Pipe pipe : pipes) {
             g.drawImage(birdImage, birdX, birdY, this);
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString("Score: " + (int) score, 10, 30);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            birdVelocityY = jumpVelocity;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    class Pipe {
        int x, y, width, height;
        boolean isTopPipe;

        Pipe(int x, int y, int width, int height, boolean isTopPipe) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.isTopPipe = isTopPipe;
        }
    }
}
