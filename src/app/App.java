package app;

import javax.swing.JFrame;

public class App extends JFrame {

    public static void main(String[] args) {
        int boardWidth = 550;
        int boardHeight = 840;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        flappyBird.requestFocus();
    }
}
