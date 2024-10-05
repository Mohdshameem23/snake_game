import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {

    private SnakePanel snakePanel;

    public SnakeGame() {
        snakePanel = new SnakePanel();
        this.add(snakePanel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.setResizable(false);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}

class SnakePanel extends JPanel implements ActionListener, KeyListener {
    private final int TILE_SIZE = 25;
    private final int GRID_SIZE = 24;
    private final int PANEL_SIZE = TILE_SIZE * GRID_SIZE;
    private final int GAME_DELAY = 100;

    private ArrayList<Point> snake;
    private Point food;
    private char direction = 'R'; // Initial direction: 'R' (right)
    private boolean gameRunning = true;
    private Timer timer;

    public SnakePanel() {
        this.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        initializeGame();
    }

    public void initializeGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));
        spawnFood();
        timer = new Timer(GAME_DELAY, this);
        timer.start();
    }

    private void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(GRID_SIZE);
        int y = rand.nextInt(GRID_SIZE);
        food = new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSnake(g);
        drawFood(g);
        if (!gameRunning) {
            drawGameOver(g);
        }
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void drawGameOver(Graphics g) {
        String message = "Game Over";
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString(message, PANEL_SIZE / 4, PANEL_SIZE / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameRunning) {
            moveSnake();
            checkCollision();
            checkFood();
        }
        repaint();
    }

    private void moveSnake() {
        Point head = snake.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case 'U': newHead.y--; break;
            case 'D': newHead.y++; break;
            case 'L': newHead.x--; break;
            case 'R': newHead.x++; break;
        }

        // Add the new head position at the start of the snake
        snake.add(0, newHead);

        // Remove the last segment if the snake hasn't eaten food
        if (!newHead.equals(food)) {
            snake.remove(snake.size() - 1);
        }
    }

    private void checkCollision() {
        Point head = snake.get(0);

        // Check if the snake hits the wall
        if (head.x < 0 || head.y < 0 || head.x >= GRID_SIZE || head.y >= GRID_SIZE) {
            gameRunning = false;
            timer.stop();
        }

        // Check if the snake collides with itself
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameRunning = false;
                timer.stop();
                break;
            }
        }
    }

    private void checkFood() {
        if (snake.get(0).equals(food)) {
            spawnFood();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Prevent the snake from reversing direction
        if (keyCode == KeyEvent.VK_LEFT && direction != 'R') {
            direction = 'L';
        }
        if (keyCode == KeyEvent.VK_RIGHT && direction != 'L') {
            direction = 'R';
        }
        if (keyCode == KeyEvent.VK_UP && direction != 'D') {
            direction = 'U';
        }
        if (keyCode == KeyEvent.VK_DOWN && direction != 'U') {
            direction = 'D';
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
