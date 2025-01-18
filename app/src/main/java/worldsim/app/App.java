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
            new Toolbox.Tool("PutMapSpace", () -> mapGen.putOpenSpace(camera.getPx()+(int)camera.getCursorx(), camera.getPy()+(int)camera.getCursory(), camera.getPz()), getTileCursor()),
            new Toolbox.Tool("RemoveMapSpace", () -> mapGen.removeOpenspace(camera.getPx()+(int)camera.getCursorx(), camera.getPy()+(int)camera.getCursory(), camera.getPz()), getRemoveTileCursor()),
            new Toolbox.Tool("Floodfill", () -> mapGen.preFill(camera.getPx()+(int)camera.getCursorx(), camera.getPy()+(int)camera.getCursory()), getTileCursor()),    
            new Toolbox.Tool("PutObject", () -> env.addEnvObject(camera.getPx()+(float)camera.getCursorx(), camera.getPy()+(float)camera.getCursory(), camera.getPz()), getObjectCursor()),
            new Toolbox.Tool("RemoveObject", () -> env.removeInProximity(camera.getPx()+(float)camera.getCursorx(), camera.getPy()+(float)camera.getCursory()), getRemoveObjectCursor()),
            new Toolbox.Tool("MiniMap", () -> {}, showMiniMap())
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
            
            //TOOLS
            toolbox.getCurrentCursorDepiction().depictConsumer(camera.getCursorx(), camera.getCursory(), graphics);   
            graphics.setColor(Color.GREEN);
            graphics.drawString("Toolbox" + toolbox.getCurrentName(), 200, 475);
            jframe.getGraphics().drawImage(screenBufferImg, 0, 0, null);

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


    static CursorDepiction getTileCursor() {
        return (cursorx, cursory, g) -> {
            g.drawImage(tilespriteloader.getSpriteFromName(mapGen.getSelectedTerrainType()), TILE_SIZE*(int)cursorx, TILE_SIZE*(int)cursory, null); 
            g.setColor(new Color(0, 255, 0, 50));
            g.drawRect(TILE_SIZE*(int)cursorx, TILE_SIZE*(int)cursory, TILE_SIZE, TILE_SIZE);  
        };
    }

    static CursorDepiction getRemoveTileCursor() {
        return (cursorx, cursory, g) -> {
            g.setColor(Color.black);    
            g.fillRect(TILE_SIZE*(int)cursorx, TILE_SIZE*(int)cursory, TILE_SIZE, TILE_SIZE);  
        };
    }

    static CursorDepiction getObjectCursor() {
        return (cursorx, cursory, g) -> g.drawImage(objectspriteloader.getSpriteFromName(env.getSeletedObjectType()), (int)(TILE_SIZE*cursorx), (int)(TILE_SIZE*cursory), null);
    }

    static CursorDepiction getRemoveObjectCursor() {
        return (cursorx, cursory, g) -> {
            g.setColor(Color.red);
            g.drawLine((int)(TILE_SIZE*cursorx)-3, (int)(TILE_SIZE*cursory)-3, (int)(TILE_SIZE*cursorx)+3, (int)(TILE_SIZE*cursory)+3);
            g.drawLine((int)(TILE_SIZE*cursorx)+3, (int)(TILE_SIZE*cursory)-3, (int)(TILE_SIZE*cursorx)-3, (int)(TILE_SIZE*cursory)+3);
        };
    }

    static CursorDepiction showMiniMap() {
        int screensize = 500;//should maybe be static global
        return (cursorx, cursory, g) -> {
            g.setColor(Color.green);
            OpenMapSpace[][] map = mapGen.getMap(-250, -250, camera.getPz(), screensize, screensize);
            for(int i=0; i<screensize; i++) {
                for(int j=0; j<screensize; j++) {
                    if(map[i][j] != null) {
                        g.fillRect(i, j, 1, 1);
                    }
                }   
            }
            g.setColor(new Color(255, 0, 0, 255));
            g.drawRect(camera.getPx()+screensize/2, camera.getPy()+screensize/2, screensize/TILE_SIZE, screensize/TILE_SIZE);
        };
    }

}
