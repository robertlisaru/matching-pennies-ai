package ro.ulbs.ai.homework.ui;

import ro.ulbs.ai.homework.game.ComputerOpponent;
import ro.ulbs.ai.homework.game.NetworkOpponent;
import ro.ulbs.ai.homework.game.Opponent;
import ro.ulbs.ai.homework.network.Client;
import ro.ulbs.ai.homework.network.Server;
import ro.ulbs.ai.homework.strategies.Strategy;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MainFrame extends JFrame {
    private Opponent opponent;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem exitMenuItem = new JMenuItem("Exit");
    private JMenu newGame = new JMenu("New game");
    private JMenuItem hostMenuItem = new JMenuItem("Host");
    private JMenuItem joinMenuItem = new JMenuItem("Join");
    private JMenuItem againstComputer = new JMenuItem("Against computer");
    private JMenu helpMenu = new JMenu("Help");
    private JMenuItem about = new JMenuItem("About");
    private JMenu viewMenu = new JMenu("View");
    private JMenuItem viewStrategyScores = new JCheckBoxMenuItem("Computer strategy scores");
    private HistoryPanel historyPanel = new HistoryPanel();
    private CenterPanel centerPanel = new CenterPanel();
    private WestPanel westPanel = new WestPanel();
    private EastPanel eastPanel = new EastPanel();

    public MainFrame() {
        setTitle("Matching Pennies Game");
        try {
            setIconImage(ImageIO.read(getClass().getResourceAsStream("/penny.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setMinimumSize(new Dimension(1024, 480));
        setLocationRelativeTo(null); //screen center
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //region menu bar
        againstComputer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opponent = new ComputerOpponent();
                westPanel.updateScores();
                historyPanel.clear();
                historyPanel.setVisible(true);
                centerPanel.setVisible(true);
                westPanel.setVisible(true);
                if (eastPanel == null) {
                    eastPanel = new EastPanel();
                    add(eastPanel, BorderLayout.EAST);
                }
                eastPanel.updateStrategyScores();
                viewStrategyScores.setEnabled(true);
            }
        });
        hostMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WaitingClientDialog dialog = new WaitingClientDialog(MainFrame.this);
            }
        });
        joinMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip;
                ip = (String) JOptionPane.showInputDialog(MainFrame.this, "Server IP: ", "Enter server address",
                        JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (ip != null && ip.length() != 0) {
                    ConnectingDialog connectingDialog = new ConnectingDialog(MainFrame.this, ip);
                }

            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "Student: Lisaru Robert-Adrian \n" +
                        "Grupa: 232/2\n" +
                        "Anul universtar: 3\n" +
                        "Anul calendaristic: 2018\n");
            }
        });
        newGame.add(againstComputer);
        newGame.add(hostMenuItem);
        newGame.add(joinMenuItem);
        fileMenu.add(newGame);
        fileMenu.add(exitMenuItem);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        helpMenu.add(about);
        viewStrategyScores.setSelected(false);
        viewStrategyScores.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    eastPanel.setVisible(true);
                } else {
                    eastPanel.setVisible(false);
                }
            }
        });
        viewStrategyScores.setEnabled(false);
        viewMenu.add(viewStrategyScores);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
        //endregion

        add(eastPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.NORTH);
        add(westPanel, BorderLayout.WEST);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        MainFrame mainFrame = new MainFrame();
    }

    private void initializeComponents() {
        if (eastPanel != null) {
            viewStrategyScores.setSelected(false);
            eastPanel.setVisible(false);
            remove(eastPanel);
            eastPanel = null;
        }
        viewStrategyScores.setEnabled(false);
        westPanel.updateScores();
        historyPanel.clear();
        historyPanel.setVisible(true);
        centerPanel.setVisible(true);
        westPanel.setVisible(true);
    }

    private class HistoryPanel extends JPanel {
        private DefaultTableModel historyTableModel;
        private JTable historyTable;
        private JScrollPane historyTableScrollPane;

        public HistoryPanel() {
            setBorder(BorderFactory.createTitledBorder("History"));
            setVisible(false);
            historyTableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            Vector column = new Vector();
            column.add("You");
            column.add("Opponent");
            historyTableModel.addColumn("Round", column);
            historyTable = new JTable(historyTableModel);
            historyTableScrollPane = new JScrollPane(historyTable);
            historyTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            historyTable.setRowSelectionAllowed(false);
            historyTable.setColumnSelectionAllowed(true);
            historyTable.setPreferredScrollableViewportSize(new Dimension(historyTable.getPreferredSize().width,
                    historyTable.getRowHeight() * historyTable.getRowCount()));
            setLayout(new BorderLayout());
            add(historyTableScrollPane, BorderLayout.CENTER);
            pack();
        }

        public void addLastRound() {
            Vector column = new Vector();
            column.add(opponent.getHistory().getLastRound().getPlayerChoice() ? "H" : "T");
            column.add(opponent.getHistory().getLastRound().getPrediction() ? "T" : "H");
            historyTableModel.addColumn(opponent.getHistory().getNumberOfRounds(), column);
            for (int i = 1; i < historyTable.getColumnCount(); i++) {
                historyTable.getColumnModel().getColumn(i).setPreferredWidth(30);
            }
            historyTable.setColumnSelectionInterval(historyTable.getColumnCount() - 1, historyTable.getColumnCount() - 1);
            historyTable.scrollRectToVisible(new Rectangle(historyTable.getCellRect(0,
                    historyTable.getColumnCount() - 1, true)));
        }

        public void clear() {
            historyTableModel.getDataVector().removeAllElements();
            historyTableModel.setColumnCount(0);
            Vector column = new Vector();
            column.add("You");
            column.add("Opponent");
            historyTableModel.addColumn("Round", column);
        }
    }

    private class CenterPanel extends JPanel {
        private JButton heads = new JButton("Heads");
        private JButton tails = new JButton("Tails");

        public CenterPanel() {
            setVisible(false);
            heads.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    opponent.play(true);
                    historyPanel.addLastRound();
                    westPanel.updateScores();
                    if (eastPanel != null) {
                        eastPanel.updateStrategyScores();
                    }
                }
            });
            tails.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    opponent.play(false);
                    historyPanel.addLastRound();
                    westPanel.updateScores();
                    if (eastPanel != null) {
                        eastPanel.updateStrategyScores();
                    }
                }
            });
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHEAST;
            c.gridx = 0;
            c.gridy = 0;
            add(heads, c);

            c.anchor = GridBagConstraints.NORTHWEST;
            c.gridx = 1;
            c.gridy = 0;
            add(tails, c);

            c.gridx = 2;
            c.weightx = 1;
            add(new JPanel(), c);

            c.gridy = 1;
            c.weighty = 1;
            add(new JPanel(), c);
        }
    }

    private class WestPanel extends JPanel {
        JLabel yourPointsLabel = new JLabel("Your points: ");
        JLabel opponentPointsLabel = new JLabel("Opponent points: ");
        JLabel yourPoints = new JLabel("0");
        JLabel opponentPoints = new JLabel("0");

        public WestPanel() {

            setVisible(false);
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Points"),
                    new EmptyBorder(5, 5, 5, 5)));
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.NORTHEAST;
            c.gridx = 0;
            c.gridy = 0;
            add(yourPointsLabel, c);
            c.gridx = 1;
            c.gridy = 0;
            add(yourPoints, c);
            c.gridx = 0;
            c.gridy = 1;
            add(opponentPointsLabel, c);
            c.gridx = 1;
            c.gridy = 1;
            add(opponentPoints, c);
            c.weighty = 1;
            c.gridy = 2;
            c.gridx = 0;
            add(new JPanel(), c);

            pack();
        }

        public void updateScores() {
            yourPoints.setText(String.valueOf(opponent.getPlayerPoints()));
            opponentPoints.setText(String.valueOf(opponent.getOpponentPoints()));
        }
    }

    private class EastPanel extends JPanel {
        private Map<String, JProgressBar> progressBarMap = new HashMap<>();
        private JLabel currentStrategyLabel = new JLabel("Current strategy: ");
        private Map<String, JLabel> correctGuessesLabels = new HashMap<>();
        private JLabel currentStrategy = new JLabel();

        public EastPanel() {
            setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Strategy scores"),
                    new EmptyBorder(5, 5, 5, 5)));
            setVisible(false);
            setLayout(new GridBagLayout());
        }

        private void addProgressBars() {
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            for (int i = 0; i < ((ComputerOpponent) opponent).getStrategyList().size(); i++) {
                Strategy strategy = ((ComputerOpponent) opponent).getStrategyList().get(i);
                c.gridx = 0;
                c.gridy = i;
                add(new JLabel(strategy.getStrategyName()), c);
                c.gridx = 1;
                JProgressBar progressBar = new JProgressBar(0, ((ComputerOpponent) opponent).getMaxStrategyScore());
                progressBar.setValue(strategy.getScore());
                progressBarMap.put(strategy.getStrategyName(), progressBar);
                add(progressBar, c);
                c.gridx = 2;
                JLabel correctGuesses = new JLabel(String.valueOf(strategy.getCorrectGuesses()));
                correctGuessesLabels.put(strategy.getStrategyName(), correctGuesses);
                add(correctGuesses, c);
            }
            currentStrategy.setText(((ComputerOpponent) opponent).getBestStrategy().getStrategyName());
            c.gridx = 0;
            c.gridy = ((ComputerOpponent) opponent).getStrategyList().size();
            add(currentStrategyLabel, c);
            c.gridx = 1;
            c.gridy = ((ComputerOpponent) opponent).getStrategyList().size();
            add(currentStrategy, c);
            c.weighty = 1;
            c.gridx++;
            c.gridy++;
            add(new JPanel(), c);

            pack();
        }

        public void updateStrategyScores() {
            if (progressBarMap.size() == 0) {
                addProgressBars();
            }
            for (Strategy strategy : ((ComputerOpponent) opponent).getStrategyList()) {
                progressBarMap.get(strategy.getStrategyName()).setValue(strategy.getScore());
                correctGuessesLabels.get(strategy.getStrategyName()).setText(String.valueOf(strategy.getCorrectGuesses()));
            }
            currentStrategy.setText(((ComputerOpponent) opponent).getBestStrategy().getStrategyName());
        }
    }

    private class WaitingClientDialog extends Dialog implements Runnable {
        JButton cancel = new JButton("Cancel");
        private Server server;

        public WaitingClientDialog(Frame owner) {
            super(owner);
            setLocationRelativeTo(owner);
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        server.close();
                        dispose();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            setModal(true);
            setTitle("Connection");
            setLayout(new BorderLayout());
            add(new JLabel("Waiting for player..."), BorderLayout.NORTH);
            add(cancel, BorderLayout.CENTER);
            pack();
            setResizable(true);
            new Thread(this).start();
            setVisible(true);
        }

        @Override
        public void run() {
            try {
                server = new Server();
                server.accept();
                opponent = new NetworkOpponent(server);
                initializeComponents();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dispose();
        }
    }

    private class ConnectingDialog extends Dialog implements Runnable {
        private JButton cancel = new JButton("Cancel");
        private String ip;
        private Client client;

        public ConnectingDialog(Frame owner, String ip) {
            super(owner);
            this.ip = ip;
            setLocationRelativeTo(owner);
            cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        client.close();
                        dispose();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            setModal(true);
            setTitle("Connection");
            setLayout(new BorderLayout());
            add(new JLabel("Connecting to server..."), BorderLayout.NORTH);
            add(cancel, BorderLayout.CENTER);
            pack();
            setResizable(true);
            new Thread(this).start();
            setVisible(true);
        }

        @Override
        public void run() {
            try {
                client = new Client();
                client.connect(ip);
                opponent = new NetworkOpponent(client);
                initializeComponents();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dispose();
        }
    }
}

