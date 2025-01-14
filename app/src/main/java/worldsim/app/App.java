package worldsim.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.InterruptedException;
import java.lang.Runnable;
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


import lombok.AllArgsConstructor;

public class App {

    static final int TILE_SIZE = 20;

    static final MapGen mapGen = new MapGen();
    static final Environment env = new Environment();
    static final MapSaver mapsaver = new JsonMapSaver();
    static final Toolbox toolbox = new Toolbox();
    static final Camera camera = new Camera();
    static final SpriteLoader tilespriteloader = new SpriteLoader("tiles");
    static final SpriteLoader objectspriteloader = new SpriteLoader("objects");

    public static void main(String[] args) throws IOException {
        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setVisible(true);
        jframe.setSize(500, 500);
        BufferedImage screenBufferImg = new BufferedImage(500, 500, 1);
        Graphics graphics = screenBufferImg.getGraphics();
        jframe.addKeyListener(getKeyListener());

        mapGen.setTileNames(tilespriteloader.getSpritenames());
        env.setObjectnames(objectspriteloader.getSpritenames());

        toolbox.initRegister(
            new Toolbox.Tool("PutMapSpace", () -> mapGen.putOpenSpace(camera.getPx()+(int)camera.getCursorx(), camera.getPy()+(int)camera.getCursory(), camera.getPz())),
            new Toolbox.Tool("RemoveMapSpace", () -> mapGen.removeOpenspace(camera.getPx()+(int)camera.getCursorx(), camera.getPy()+(int)camera.getCursory(), camera.getPz())),
            new Toolbox.Tool("Floodfill", () -> mapGen.preFill(camera.getPx()+(int)camera.getCursorx(), camera.getPy()+(int)camera.getCursory())),    
            new Toolbox.Tool("PutObject", () -> env.addEnvObject(camera.getPx()+(float)camera.getCursorx(), camera.getPy()+(float)camera.getCursory(), camera.getPz())),
            new Toolbox.Tool("RemoveObject", () -> env.removeInProximity(camera.getPx()+(float)camera.getCursorx(), camera.getPy()+(float)camera.getCursory()))
        );

        while(true) {
            OpenMapSpace[][] map = mapGen.getMap(camera.getPx(), camera.getPy(), camera.getPz(), screenBufferImg.getWidth()/TILE_SIZE, screenBufferImg.getHeight()/TILE_SIZE);

            for (int i=0; i < map.length; i++ ) {
                for (int j=0; j < map[0].length; j++ ) {
                    if(map[i][j] != null) {
                        BufferedImage tileImg = tilespriteloader.getSpriteFromName(map[i][j].getTerrainType());
                        graphics.drawImage(tileImg, TILE_SIZE*i, TILE_SIZE*j, null);
                        
                        if(map[i][j].getZ() < camera.getPz()) {
                            graphics.setColor(new Color(0,0,0, 125));
                            graphics.fillRect(TILE_SIZE*i, TILE_SIZE*j, TILE_SIZE, TILE_SIZE);
                        }

                    } else {
                        graphics.setColor(Color.black);
                        graphics.fillRect(TILE_SIZE*i, TILE_SIZE*j, TILE_SIZE, TILE_SIZE);
                    }
                }   
            }


            env.getEnvObjectList().forEach(envObj -> graphics.drawImage(objectspriteloader.getSpriteFromName(envObj.envType()), (int)(TILE_SIZE*(envObj.x()-camera.getPx())), (int)(TILE_SIZE*(envObj.y()-camera.getPy())), null));
            
            graphics.drawImage(tilespriteloader.getSpriteFromName(mapGen.getSelectedTerrainType()), TILE_SIZE*(int)camera.getCursorx(), TILE_SIZE*(int)camera.getCursory(), null);
            graphics.drawImage(objectspriteloader.getSpriteFromName(env.getSeletedObjectType()), (int)(TILE_SIZE*camera.getCursorx()), (int)(TILE_SIZE*camera.getCursory()), null);
            graphics.setColor(Color.GREEN);
            graphics.drawString("Toolbox" + toolbox.getCurrentName(), 200, 475);
            jframe.getGraphics().drawImage(screenBufferImg, 0, 10, null);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {

            }
        }
    }

    static KeyListener getKeyListener() {
        return new DynamicKeyListener(
            Map.ofEntries(
                entry(KeyEvent.VK_W, () -> camera.moveCursor(0, -0.1)),
                entry(KeyEvent.VK_S, () -> camera.moveCursor(0, 0.1)),
                entry(KeyEvent.VK_D, () -> camera.moveCursor(0.1, 0)),
                entry(KeyEvent.VK_A, () -> camera.moveCursor(-0.1, 0))
            ),
            Map.ofEntries(
                entry(KeyEvent.VK_RIGHT, () -> camera.move(1, 0)),
                entry(KeyEvent.VK_LEFT, () -> camera.move(-1, 0)),
                entry(KeyEvent.VK_DOWN, () -> camera.move(0, 1)),
                entry(KeyEvent.VK_UP, () -> camera.move(0, -1)),
                entry(KeyEvent.VK_C, () -> mapGen.cycleSelectedTerrain()),
                entry(KeyEvent.VK_X, () -> env.cycleSelected()),
                entry(KeyEvent.VK_PLUS, () -> camera.moveZaxis(1)),
                entry(KeyEvent.VK_MINUS, () -> camera.moveZaxis(-1)),
                entry(KeyEvent.VK_SPACE, toolbox::runCurrentAction),
                entry(KeyEvent.VK_F, toolbox::cycleForward),
                entry(KeyEvent.VK_E, () -> mapsaver.saveToFile(mapGen.getMapSpaceAsList(), env.getEnvObjectList()))
            ));
    }


    @AllArgsConstructor
    static class DynamicKeyListener implements KeyListener {


        Map<Integer, Runnable> keyPressActions;
        Map<Integer, Runnable> keyReleaseActions;
            
        public void keyPressed(KeyEvent e) {
            Runnable action = keyPressActions.get(e.getKeyCode());
            
            if(action != null) {
                action.run();
            }
        }

        public void keyReleased(KeyEvent e) {
            Runnable action = keyReleaseActions.get(e.getKeyCode());
            
            if(action != null) {
                action.run();
            }
        }

        public void keyTyped(KeyEvent e) {
            
        }

    }
}
