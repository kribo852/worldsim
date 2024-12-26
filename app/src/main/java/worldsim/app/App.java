package worldsim.app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.lang.Math;
import java.lang.InterruptedException;
import java.lang.Runnable;
import java.util.function.BiConsumer;
import static java.util.Map.entry;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import lombok.AllArgsConstructor;

public class App {

    static MapGen mapGen = new MapGen();
    static final ColorHandler colorHandler = new ColorHandler();
    static final int TILE_SIZE = 20;

    public static void main(String[] args) {

        Camera camera = new Camera();
        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.setSize(500, 500);
        BufferedImage bimg = new BufferedImage(500, 500, 1);
        Graphics graphics = bimg.getGraphics();
        jframe.addKeyListener(new DynamicKeyListener(Map.ofEntries(
            entry(KeyEvent.VK_RIGHT, () -> camera.move(1, 0)),
            entry(KeyEvent.VK_LEFT, () -> camera.move(-1, 0)),
            entry(KeyEvent.VK_DOWN, () -> camera.move(0, 1)),
            entry(KeyEvent.VK_UP, () -> camera.move(0, -1)),
            entry(KeyEvent.VK_C, () -> colorHandler.cycleTerrain()),
            entry(KeyEvent.VK_PLUS, () -> camera.moveZaxis(1)),
            entry(KeyEvent.VK_MINUS, () -> camera.moveZaxis(-1)),
            entry(KeyEvent.VK_W, () -> camera.moveCursor(0, -1)),
            entry(KeyEvent.VK_S, () -> camera.moveCursor(0, 1)),
            entry(KeyEvent.VK_D, () -> camera.moveCursor(1, 0)),
            entry(KeyEvent.VK_A, () -> camera.moveCursor(-1, 0)),
            entry(KeyEvent.VK_SPACE, () -> mapGen.putOrRemoveOpenSpace(camera.getPx()+camera.getCursorx(), camera.getPy()+camera.getCursory(), camera.getPz(), colorHandler.getCurrent())),
            entry(KeyEvent.VK_F, () -> mapGen.preFill(camera.getPx()+camera.getCursorx(), camera.getPy()+camera.getCursory(), colorHandler.getCurrent())),
            entry(KeyEvent.VK_E, () -> mapGen.saveToFile())
            )));

        while(true) {

            MapGen.OpenMapSpace[][] map = mapGen.getMap(camera.getPx(), camera.getPy(), camera.getPz(), bimg.getWidth()/TILE_SIZE, bimg.getHeight()/TILE_SIZE);

            for (int i=0; i < map.length; i++ ) {
                for (int j=0; j < map[0].length; j++ ) {
                    if(map[i][j] != null) {
                        Color currentSquareColor = colorHandler.getTerrainColor(map[i][j].getTerrainType());
                        
                        if(map[i][j].getZ() < camera.getPz()) {
                            currentSquareColor = currentSquareColor.darker();
                        }

                        graphics.setColor(currentSquareColor);

                        graphics.fillRect(TILE_SIZE*i, TILE_SIZE*j, TILE_SIZE, TILE_SIZE);
                    } else {
                        graphics.setColor(Color.black);
                        graphics.fillRect(TILE_SIZE*i, TILE_SIZE*j, TILE_SIZE, TILE_SIZE);
                    }
                }   
            }

            graphics.setColor(Color.GREEN);
            graphics.drawRect(TILE_SIZE*camera.getCursorx(), TILE_SIZE*camera.getCursory(), TILE_SIZE, TILE_SIZE);
            graphics.drawString(colorHandler.getCurrent().toString(), 100, 475);
            jframe.getGraphics().drawImage(bimg, 0, 10, null);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

            }
        }

    }

    @AllArgsConstructor
    static class DynamicKeyListener implements KeyListener {

        Map<Integer, Runnable> actions;
            
        public void keyPressed(KeyEvent e) {
            
        }

        public void keyReleased(KeyEvent e) {
            Runnable action = actions.get(e.getKeyCode());
            
            if(action != null) {
                action.run();
            }
        }

        public void keyTyped(KeyEvent e) {
            
        }

    }

    public static BufferedImage readTree() throws IOException{
        return ImageIO.read(new File("Tree.png"));
    }
}
