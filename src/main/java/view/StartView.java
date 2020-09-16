package main.java.view;

import main.java.model.*;
import main.java.model.rating.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StartView extends JPanel implements ActionListener {

    final private Image background;
    final private JFrame frame;
    final private JButton continueButton;
    final private JButton playButton;
    final private JButton ratingButton;
    final private JButton quitButton;

    final private JPanel ratingPanel;
    final private JLabel[] ratingLines;
    final private JButton ratingBackButton;
    final private JTextField textField;
    final private JButton addNameButton;


    private LevelView levelView;

    private boolean isPrevGameLoaded = false;
    private Game prevGame = null;

    private String playerName = "Test";
    private RatingTable table;

    public StartView(JFrame frame){

        this.setLayout(null);

        textField = new JTextField(140);
        textField.setBounds(400, 60, 140, 30);
        this.add(textField);
        textField.setVisible(true);

        addNameButton = new JButton("YOUR NAME");
        addNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if(action.equals("YOUR NAME")) {
                    playerName = textField.getText();
                    levelView.setPlayerName(playerName);
                    ShowMenuButtons();
                }
            }
        });


        ratingBackButton = new JButton("BACK");
        ratingBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if(action.equals("BACK")) {
                    ratingPanel.setVisible(false);
                    ratingBackButton.setVisible(false);
                    ShowMenuButtons();
                }
            }
        });

        ratingBackButton.setVisible(false);
        this.add(ratingBackButton);
        ratingBackButton.setBounds(50, 500, 100, 50);

        ratingPanel = new JPanel(null);
        ratingLines = new JLabel[3];

        ratingLines[0] = new JLabel("");
        ratingLines[1] = new JLabel("");
        ratingLines[2] = new JLabel("");

        ratingPanel.add(ratingLines[0]);
        ratingPanel.add(ratingLines[1]);
        ratingPanel.add(ratingLines[2]);

        ratingPanel.setVisible(false);

        this.add(ratingPanel);

        ratingPanel.setBounds(380, 180, 140, 250);

        ratingLines[0].setBounds(20, 20, 100, 50);
        ratingLines[1].setBounds(20, 90, 100, 50);
        ratingLines[2].setBounds(20, 160, 100, 50);

        try {
            table = RatingTable.loadFromFile("ratings.txt");
            prevGame = Game.loadGameFromFile("lastGame.bin");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        isPrevGameLoaded = (prevGame != null);


        this.frame = frame;
        this.background = new ImageIcon("src/main/java/view/images/background_start.jpg").getImage();

        this.continueButton = new JButton("CONTINUE");
        this.continueButton.addActionListener(this);

        this.playButton = new JButton("NEW GAME");
        this.playButton.addActionListener(this);

        this.ratingButton = new JButton("RATING");
        this.ratingButton.addActionListener(this);

        this.quitButton = new JButton("QUIT");
        this.quitButton.addActionListener(this);


        ButtonPaint(continueButton, 400, 130);
        ButtonPaint(playButton, 400, 200);
        ButtonPaint(ratingButton, 400, 270);
        ButtonPaint(quitButton, 400, 340);
        ButtonPaint(addNameButton, 550, 50);


        continueButton.setVisible(isPrevGameLoaded);

        this.levelView = new LevelView(table, playerName, callback);
        levelView.resetLevels(new Game[] {new Game(0,0, Constant.FIRST_LVL_ROW_MAP, Constant.FIRST_LVL_COLUMN_MAP, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y)}, 0);
        levelView.setVisible(false);
        this.frame.add(levelView);
        levelView.setBounds(0, 0, 900, 600);
        HideMenuButtons();
    }

    private void loadRatingTable() {
        for(int i = 0; i < table.getSize(); i++) {
            ratingLines[i].setText(table.getRatingTable().get(i).name + "   " + table.getRatingTable().get(i).suns);
        }
    }

    private void HideMenuButtons() {
        continueButton.setVisible(false);
        playButton.setVisible(false);
        ratingButton.setVisible(false);
        quitButton.setVisible(false);
    }

    private void ShowMenuButtons() {
        if(isPrevGameLoaded) {
            continueButton.setVisible(true);
        }
        playButton.setVisible(true);
        ratingButton.setVisible(true);
        quitButton.setVisible(true);
    }

    private void ButtonPaint(JButton button, int x, int y) {
        this.add(button);
        button.setBounds(x, y, 140, 50);
    }

    public void onRepaint(Graphics g) {
        g.drawImage(background, 0, 0, this.frame.getWidth(), this.frame.getHeight(), null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        onRepaint(g);
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("CONTINUE")) {
            this.setVisible(false);
            int startLevel = 0;
            Game[] games = new Game[2];
            games[0] = new Game(0,1, Constant.FIRST_LVL_ROW_MAP, Constant.FIRST_LVL_COLUMN_MAP, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y + 100);
            games[1] = new Game(1, 5, Constant.SECOND_LVL_ROW_MAP, Constant.SECOND_LVL_COLUMN_MAP, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);
            if(isPrevGameLoaded) {
                startLevel = prevGame.getLevelNumber();
                games[prevGame.getLevelNumber()] = prevGame;
            }
            levelView.resetLevels(games, startLevel);
            levelView.setVisible(true);
            levelView.repaint();
        }
        if (s.equals("NEW GAME")) {
            this.setVisible(false);

            Game[] games = new Game[2];
            games[0] = new Game(0,1, Constant.FIRST_LVL_ROW_MAP, Constant.FIRST_LVL_COLUMN_MAP, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y+100);
            games[1] = new Game(1, 5, Constant.SECOND_LVL_ROW_MAP, Constant.SECOND_LVL_COLUMN_MAP, Constant.COORDINATE_SHIFT_POINT_X, Constant.COORDINATE_SHIFT_POINT_Y);

            levelView.resetLevels(games, 0);
            levelView.setVisible(true);
            levelView.repaint();
        }
        if (s.equals("RATING")) {
            loadRatingTable();
            HideMenuButtons();
            ratingPanel.setVisible(true);
            ratingBackButton.setVisible(true);
        }
        if (s.equals("QUIT")) {
            try {
                table.saveToFile("ratings.txt");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            this.frame.setVisible(false);
            this.frame.dispose();
            System.exit(0);
        }
    }

    LevelView.LevelExitCallback callback = new LevelView.LevelExitCallback() {
        @Override
        public void onExitInMainMenu(Game game) {
            levelView.setVisible(false);
            setVisible(true);
            isPrevGameLoaded = true;
            prevGame = game;
            continueButton.setVisible(isPrevGameLoaded);
        }
    };
}
