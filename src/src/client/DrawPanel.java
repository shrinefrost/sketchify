package src.client;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel {
    private BufferedImage canvas;
    private Graphics2D g2;
    private int prevX, prevY;
    private boolean drawing;
    private Color brushColor = Color.BLACK;
    private int brushSize = 5;
    private GameWindow gameWindow;

    public DrawPanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(600, 500));
        initCanvas();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameWindow.isDrawer) { // ✅ Only the drawer can draw
                    prevX = e.getX();
                    prevY = e.getY();
                    drawing = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                drawing = false;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (drawing && gameWindow.isDrawer) { // ✅ Prevent guessers from drawing
                    int x = e.getX();
                    int y = e.getY();
                    g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2.setColor(brushColor);
                    g2.drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                    repaint();

                    // Send drawing data to server
                    gameWindow.sendDrawing(prevX + "," + prevY + "," + x + "," + y + "," + brushColor.getRGB() + "," + brushSize);
                }
            }
        });
    }

    private void initCanvas() {
        canvas = new BufferedImage(600, 500, BufferedImage.TYPE_INT_ARGB);
        g2 = canvas.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 600, 500);
        g2.setColor(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, null);
    }

    public void setBrushColor(Color color) {
        this.brushColor = color;
    }

    public void setBrushSize(int size) {
        this.brushSize = size;
    }

    public void setBrushMode() {
        brushColor = Color.BLACK;
    }

    public void setEraserMode() {
        brushColor = Color.WHITE;
    }

    public void clearCanvas() {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2.setColor(Color.BLACK);
        repaint();
    }

    public void receiveDrawing(String data) {
        String[] parts = data.split(",");
        if (parts.length == 6) {
            int x1 = Integer.parseInt(parts[0]);
            int y1 = Integer.parseInt(parts[1]);
            int x2 = Integer.parseInt(parts[2]);
            int y2 = Integer.parseInt(parts[3]);
            Color color = new Color(Integer.parseInt(parts[4]));
            int size = Integer.parseInt(parts[5]);

            g2.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(color);
            g2.drawLine(x1, y1, x2, y2);
            repaint();
        }
    }
}
