package main.java.view;

import main.java.model.*;
import main.java.model.rating.RatingTable;
import main.java.model.util.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LevelView extends JPanel implements ActionListener {

    final private Image fallSun;
    final private Image block;
    final private Image zombie;
    final private Image board;
    final private Image sunframe;
    final private Image paper;
    final private Image table;
    final private Image gunflower;
    final private Image sunflower;
    final private Image background;
    final private Image bomb;

    final private ImageIcon shovelIcon;
    final private ImageIcon gunflowerIcon;
    final private ImageIcon sunflowerIcon;

    final private JLabel sunScoreLabel;
    final private JLabel moveSunflowerLabel;
    final private JLabel moveGunflowerLabel;
    final private JLabel moveShovelLabel;
    final private JButton pauseButton;

    private JLabel menuLabel = new JLabel("");
    private JButton[] menuButtons = new JButton[3];
    private JPanel menuPanel = new JPanel();

    private Point offset;

    private int nextLevel = 0;
    private Game[] games;
    private Game game;
    public Vector2D mapLeftBottom;
    private LevelExitCallback callback;

    private RatingTable ratingTable;
    String playerName;


    public LevelView(RatingTable ratingTable, String playerName, LevelExitCallback callback) {
        this.ratingTable = ratingTable;
        this.playerName = playerName;

        Timer timer = new Timer(50, this);

        this.background = new ImageIcon("src/main/java/view/images/background.jpg").getImage();
        this.fallSun = new ImageIcon("src/main/java/view/images/sun.png").getImage();
        this.block = new ImageIcon("src/main/java/view/images/block.jpg").getImage();
        this.zombie = new ImageIcon("src/main/java/view/images/zombiePic.png").getImage();
        this.board = new ImageIcon("src/main/java/view/images/board.png").getImage();
        this.sunframe = new ImageIcon("src/main/java/view/images/SunFrame.png").getImage();
        this.paper = new ImageIcon("src/main/java/view/images/paper.png").getImage();
        this.table = new ImageIcon("src/main/java/view/images/table.png").getImage();
        this.sunflower = new ImageIcon("src/main/java/view/images/sunny.png").getImage();
        this.gunflower = new ImageIcon("src/main/java/view/images/peas.png").getImage();
        this.bomb = new ImageIcon("src/main/java/view/images/apple.png").getImage();
        this.sunflowerIcon = new ImageIcon("src/main/java/view/images/sunny.png");
        this.gunflowerIcon = new ImageIcon("src/main/java/view/images/peas.png");
        this.shovelIcon = new ImageIcon("src/main/java/view/images/shovel.png");
        this.sunScoreLabel = new JLabel();
        this.callback = callback;

        this.moveSunflowerLabel = new JLabel(sunflowerIcon);
        this.moveGunflowerLabel = new JLabel(gunflowerIcon);
        this.moveShovelLabel = new JLabel(shovelIcon);

        menuButtons[0] = new JButton("");
        menuButtons[1] = new JButton("");
        menuButtons[2] = new JButton("");

        this.add(menuPanel);
        menuPanel.add(menuLabel);
        menuPanel.add(menuButtons[0]);
        menuPanel.add(menuButtons[1]);
        menuPanel.add(menuButtons[2]);

        menuPanel.setVisible(false);
        menuPanel.setLayout(null);

        menuPanel.setBounds(380, 130, 140, 230);

        menuPanel.setBackground(Color.WHITE);

        menuLabel.setBounds(20, 20, 100, 40);

        menuButtons[0].setBounds(20, 70, 100, 40);
        menuButtons[1].setBounds(20, 120, 100, 40);
        menuButtons[2].setBounds(20, 170, 100, 40);

        menuButtons[0].addActionListener(menuListener);
        menuButtons[1].addActionListener(menuListener);
        menuButtons[2].addActionListener(menuListener);

        this.pauseButton = new JButton("PAUSE");
        this.pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = e.getActionCommand();
                if (s.equals("PAUSE")) {
                    game.isPaused = true;

                    menuLabel.setText("Pause");
                    menuLabel.setVisible(true);

                    menuButtons[0].setText("Continue");
                    menuButtons[1].setText("Restart");
                    menuButtons[2].setText("Main menu");

                    menuPanel.setVisible(true);

                    menuButtons[0].setVisible(true);
                    menuButtons[1].setVisible(true);
                    menuButtons[2].setVisible(true);

                    pauseButton.setVisible(false);
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Vector2D pos = SetWorld.SwapBetweenWindowAndWorld(new Vector2D(e.getX(), e.getY()));
                Sun sunObj = game.searchSunsArray(pos.x, pos.y);
                if (sunObj != null) {
                    game.catchSun(sunObj);
                }
            }

        });

        timer.start();
        setViewMoveItems();


    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void resetLevels(Game[] games, int startLevel) {
        this.games = games;

        this.game = games[startLevel];
        this.mapLeftBottom = new Vector2D(game.getXMapShift(), game.getYMapShift());
        this.nextLevel = startLevel + 1;

        this.game.isPaused = false;

        menuPanel.setVisible(false);
        pauseButton.setVisible(true);
    }


    private void setViewMoveItems() {
        this.setLayout(null);

        moveSunflowerLabel.addMouseListener(sunFlowerMouseListener);
        moveSunflowerLabel.addMouseMotionListener(flowerMotionListener);
        moveSunflowerLabel.setBounds(Constant.SUN_FLOWER_SPAWN_X_BY_SET_WORLD, Constant.SUN_FLOWER_SPAWN_Y_BY_SET_WORLD, Constant.SHOP_OBJECTS_SIZE, Constant.SHOP_OBJECTS_SIZE);
        moveSunflowerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(moveSunflowerLabel);


        moveGunflowerLabel.addMouseListener(gunFlowerMouseListener);
        moveGunflowerLabel.addMouseMotionListener(flowerMotionListener);
        moveGunflowerLabel.setBounds(Constant.GUN_FLOWER_SPAWN_X_BY_SET_WORLD, Constant.GUN_FLOWER_SPAWN_Y_BY_SET_WORLD, Constant.SHOP_OBJECTS_SIZE, Constant.SHOP_OBJECTS_SIZE);
        moveGunflowerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(moveGunflowerLabel);


        moveShovelLabel.addMouseListener(shovelMouseListener);
        moveShovelLabel.addMouseMotionListener(flowerMotionListener);
        moveShovelLabel.setBounds(Constant.SHOVEL_SPAWN_X_BY_SET_WORLD, Constant.SHOVEL_SPAWN_Y_BY_SET_WORLD, Constant.SHOP_OBJECTS_SIZE, Constant.SHOP_OBJECTS_SIZE);
        moveShovelLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(moveShovelLabel);
    }

    private void sunScorePaint(String score) {
        Font font = new Font("Verdana", Font.ITALIC, 40);
        this.setLayout(null);
        sunScoreLabel.setText(score);
        sunScoreLabel.setFont(font);
        sunScoreLabel.setForeground(Color.white);
        this.add(sunScoreLabel);
        Dimension size = sunScoreLabel.getPreferredSize();
        int x = 80;
        int y = 80;
        if (game.getSunScore() == 0) {
            x = x + 27;
        } else if (game.getSunScore() > 0 && game.getSunScore() < 100) {
            x = x + 17;
        }
        sunScoreLabel.setBounds(x, y, size.width, size.height);
    }


    public void onRepaint(Graphics g) {
        g.drawImage(background, 0, 0, Constant.WIDTH_FRAME, Constant.HEIGHT_FRAME, null);
        g.drawImage(board, 50, 2, 550, 150, null);
        g.drawImage(sunframe, 70, 2, 100, 150, null);
        g.drawImage(fallSun, 95, 30, 50, 50, null);
        g.drawImage(paper, 65, 80, 110, 65, null);

        g.drawImage(table, 175, 0, 130, 150, null);
        g.drawImage(table, 310, 0, 130, 150, null);
        g.drawImage(table, 445, 0, 130, 150, null);

        buttonPaint(pauseButton, 700, 10);

        sunScorePaint("");
        sunScorePaint(game.getSunScore() + "");


        Vector2D blockPos = new Vector2D(game.getXMapShift(), game.getYMapShift());

        for (int i = 0; i < game.getRowMap(); i++) {
            for (int j = 0; j < game.getColumnMap(); j++) {
                Vector2D pos = SetWorld.SwapBetweenWindowAndWorld(blockPos);
                g.drawImage(block, pos.x, pos.y, Constant.SIZE_POINT, -Constant.SIZE_POINT, null);
                blockPos.x += Constant.SIZE_POINT;
            }
            blockPos.y += Constant.SIZE_POINT;
            blockPos.x = game.getXMapShift();
        }

        int i = 0;
        while (game.getZombiesArray(i) != null) {
            Vector2D pos = SetWorld.SwapBetweenWindowAndWorld(game.getZombiesArray(i).getPosition());
            g.drawImage(zombie, pos.x, pos.y, Constant.ZOMBIE_SIZE, Constant.ZOMBIE_SIZE, null);
            i++;
        }

        i = 0;
        while (game.getFlowersArray(i) != null) {
            Vector2D pos = SetWorld.SwapBetweenWindowAndWorld(game.getFlowersArray(i).getPosition());
            g.drawImage(game.getFlowersArray(i).getClass() == Sunflower.class ? sunflower : gunflower, pos.x, pos.y - Constant.SHOP_OBJECTS_SIZE, Constant.SHOP_OBJECTS_SIZE, Constant.SHOP_OBJECTS_SIZE, null);
            i++;
        }

        moveSunflowerLabel.setEnabled(game.getIsSunflowerReady());
        moveGunflowerLabel.setEnabled(game.getIsGunflowerReady());

        i = 0;
        while (game.getBombsArray(i) != null) {
            Vector2D pos = SetWorld.SwapBetweenWindowAndWorld(game.getBombsArray(i).getPosition());
            g.drawImage(bomb, pos.x, pos.y - Constant.BOMB_SIZE, Constant.BOMB_SIZE, Constant.BOMB_SIZE, null);
            i++;
        }

        i = 0;
        while (game.getSunsArray(i) != null) {
            Vector2D pos = SetWorld.SwapBetweenWindowAndWorld(game.getSunsArray(i).getPosition());
            g.drawImage(fallSun, pos.x, pos.y - Constant.SUN_SIZE, Constant.SUN_SIZE, Constant.SUN_SIZE, null);
            i++;
        }
    }

    private void buttonPaint(JButton button, int x, int y) {
        this.add(button);
        button.setBounds(x, y, 100, 40);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        onRepaint(g);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!this.isVisible() || game.isStop()) return;

        game.gameUpdates();

        if (game.isPaused) {
            if (game.isWin()) {
                game.stop();
                ratingTable.add(playerName, game.getSunScore());
                menuLabel.setText("You win");

                menuLabel.setVisible(true);
                if (nextLevel < games.length) {
                    menuButtons[0].setText("Next level");
                    menuButtons[1].setText("Restart");
                    menuButtons[2].setText("Main menu");

                    menuPanel.setVisible(true);

                    menuButtons[0].setVisible(true);
                    menuButtons[1].setVisible(true);
                    menuButtons[2].setVisible(true);
                }
                else {
                    menuButtons[0].setText("Restart");
                    menuButtons[1].setText("Main menu");

                    menuPanel.setVisible(true);

                    menuButtons[0].setVisible(true);
                    menuButtons[1].setVisible(true);
                    menuButtons[2].setVisible(false);
                }

                pauseButton.setVisible(false);
            }
            if (game.isLoser()) {
                game.stop();

                menuLabel.setText("You lose");
                menuLabel.setVisible(true);

                menuButtons[0].setText("Restart");
                menuButtons[1].setText("Main menu");

                menuPanel.setVisible(true);

                menuButtons[0].setVisible(true);
                menuButtons[1].setVisible(true);
                menuButtons[2].setVisible(false);

                pauseButton.setVisible(false);
            }
        }
        repaint();
    }


    final public static class SetWorld {

        public static Vector2D SwapBetweenWindowAndWorld(Vector2D pos) {
            return new Vector2D(pos.x, Constant.HEIGHT_FRAME - 30 - pos.y);
        }

    }


    private final MouseAdapter sunFlowerMouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            offset = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (game.isPaused) return;

            if (!moveSunflowerLabel.isEnabled()) return;

            Vector2D pos = new Vector2D(moveSunflowerLabel.getLocation().x + e.getX(),
                    moveSunflowerLabel.getLocation().y + e.getY());
            pos = SetWorld.SwapBetweenWindowAndWorld(pos);

            Vector2D mapRightUp = mapLeftBottom.Add(new Vector2D(game.getRoadMap().getColumns() * Constant.SIZE_POINT, game.getRoadMap().getRows() * Constant.SIZE_POINT));

            if (pos.x < mapLeftBottom.x || pos.y < mapLeftBottom.y || pos.x > mapRightUp.x || pos.y > mapRightUp.y) {
                moveSunflowerLabel.setLocation(Constant.SUN_FLOWER_SPAWN_X_BY_SET_WORLD, Constant.SUN_FLOWER_SPAWN_Y_BY_SET_WORLD);
                return;
            }

            boolean state = game.plantSunflower(pos.Sub(mapLeftBottom));

            if (state) {
                moveSunflowerLabel.setEnabled(false);
            }
            moveSunflowerLabel.setLocation(Constant.SUN_FLOWER_SPAWN_X_BY_SET_WORLD, Constant.SUN_FLOWER_SPAWN_Y_BY_SET_WORLD);
        }
    };


    private final MouseAdapter gunFlowerMouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            offset = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (game.isPaused) return;

            if (!moveGunflowerLabel.isEnabled()) return;

            Vector2D pos = new Vector2D(moveGunflowerLabel.getLocation().x + e.getX(),
                    moveGunflowerLabel.getLocation().y + e.getY());
            pos = SetWorld.SwapBetweenWindowAndWorld(pos);

            Vector2D mapRightUp = mapLeftBottom.Add(new Vector2D(game.getRoadMap().getColumns() * Constant.SIZE_POINT,
                    game.getRoadMap().getRows() * Constant.SIZE_POINT));

            if (pos.x < mapLeftBottom.x || pos.y < mapLeftBottom.y || pos.x > mapRightUp.x || pos.y > mapRightUp.y) {
                moveGunflowerLabel.setLocation(Constant.GUN_FLOWER_SPAWN_X_BY_SET_WORLD,
                        Constant.GUN_FLOWER_SPAWN_Y_BY_SET_WORLD);
                return;
            }

            boolean state = game.plantGunflower(pos.Sub(mapLeftBottom));

            if (state) {
                moveGunflowerLabel.setEnabled(false);
            }
            moveGunflowerLabel.setLocation(Constant.GUN_FLOWER_SPAWN_X_BY_SET_WORLD,
                    Constant.GUN_FLOWER_SPAWN_Y_BY_SET_WORLD);
        }
    };

    private final MouseAdapter shovelMouseListener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            offset = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (game.isPaused) return;

            Vector2D pos = new Vector2D(moveShovelLabel.getLocation().x + e.getX(),
                    moveShovelLabel.getLocation().y + e.getY());
            pos = SetWorld.SwapBetweenWindowAndWorld(pos);

            Vector2D mapRightUp = mapLeftBottom.Add(new Vector2D(game.getRoadMap().getColumns() * Constant.SIZE_POINT,
                    game.getRoadMap().getRows() * Constant.SIZE_POINT));

            if (pos.x < mapLeftBottom.x || pos.y < mapLeftBottom.y || pos.x > mapRightUp.x || pos.y > mapRightUp.y) {
                moveShovelLabel.setLocation(Constant.SHOVEL_SPAWN_X_BY_SET_WORLD, Constant.SHOVEL_SPAWN_Y_BY_SET_WORLD);
                return;
            }

            game.destroyFlower(pos.Sub(mapLeftBottom));

            moveShovelLabel.setLocation(Constant.SHOVEL_SPAWN_X_BY_SET_WORLD, Constant.SHOVEL_SPAWN_Y_BY_SET_WORLD);
        }
    };

    private final MouseAdapter flowerMotionListener = new MouseAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            if (!e.getComponent().isEnabled() || game.isPaused) return;

            int x = e.getPoint().x - offset.x;
            int y = e.getPoint().y - offset.y;
            Component component = e.getComponent();
            Point currPoint = component.getLocation();
            currPoint.x += x;
            currPoint.y += y;
            component.setLocation(currPoint);

        }
    };

    private final ActionListener menuListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();
            if (s.equals("Next level")) {
                game = games[nextLevel++];
                mapLeftBottom = new Vector2D(game.getXMapShift(), game.getYMapShift());

                pauseButton.setVisible(true);
                menuPanel.setVisible(false);
            } else if (s.equals("Restart")) {
                game.restart();

                pauseButton.setVisible(true);
                menuPanel.setVisible(false);
            } else if (s.equals("Continue")) {
                game.isPaused = false;

                pauseButton.setVisible(true);
                menuPanel.setVisible(false);
            } else if (s.equals("Main menu")) {
                System.out.println("Main menu");

                try {
                    if(game.isWin() && nextLevel < games.length) {
                        games[nextLevel].saveGameToFile("lastGame.bin");
                        callback.onExitInMainMenu(games[nextLevel]);
                    } else {
                        if(game.isLoser()) {
                            game.restart();
                        }
                        game.saveGameToFile("lastGame.bin");
                        callback.onExitInMainMenu(game);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }


            }
        }
    };

    public interface LevelExitCallback {
        void onExitInMainMenu(Game game);
    }
}