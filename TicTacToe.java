import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[][] buttons;
    private char currentPlayer;
    private JTextField player1NameField, player2NameField;
    private JLabel statusLabel, scoreLabel;
    private int player1Wins, player2Wins, totalGames;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Front Screen Panel
        JPanel frontScreenPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Welcome to Tic Tac Toe");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        frontScreenPanel.add(titleLabel, gbc);

        gbc.gridy++;
        JLabel player1Label = new JLabel("Enter Player 1 Name:");
        frontScreenPanel.add(player1Label, gbc);

        gbc.gridy++;
        player1NameField = new JTextField(15);
        frontScreenPanel.add(player1NameField, gbc);

        gbc.gridy++;
        JLabel player2Label = new JLabel("Enter Player 2 Name:");
        frontScreenPanel.add(player2Label, gbc);

        gbc.gridy++;
        player2NameField = new JTextField(15);
        frontScreenPanel.add(player2NameField, gbc);

        gbc.gridy++;
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        frontScreenPanel.add(startButton, gbc);

        add(frontScreenPanel, BorderLayout.CENTER);

        // Status and Score Labels
        JPanel bottomPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Enter player names and start the game.");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.NORTH);

        scoreLabel = new JLabel("Score: ");
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(scoreLabel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void startGame() {
        String player1Name = player1NameField.getText();
        String player2Name = player2NameField.getText();

        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter names for both players.");
            return;
        }

        setTitle("Tic Tac Toe - " + player1Name + " vs " + player2Name);

        getContentPane().removeAll();
        initializeGameUI();
        revalidate();
        repaint();
    }

    private void initializeGameUI() {
        setLayout(new GridLayout(3, 3));

        buttons = new JButton[3][3];
        currentPlayer = 'X';

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 70));
                buttons[i][j].addActionListener(this);
                add(buttons[i][j]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();

        if (clickedButton.getText().equals(""))
            clickedButton.setText(String.valueOf(currentPlayer));
        if (checkWin()) {
            String winnerName = (currentPlayer == 'X') ? player1NameField.getText() : player2NameField.getText();
            JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " (" + winnerName + ") wins!");
            updateScore(currentPlayer);
            promptPlayAgain();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "It's a draw!");
            promptPlayAgain();
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(String.valueOf(currentPlayer)) &&
                    buttons[i][1].getText().equals(String.valueOf(currentPlayer)) &&
                    buttons[i][2].getText().equals(String.valueOf(currentPlayer))) {
                return true; // Check rows
            }

            if (buttons[0][i].getText().equals(String.valueOf(currentPlayer)) &&
                    buttons[1][i].getText().equals(String.valueOf(currentPlayer)) &&
                    buttons[2][i].getText().equals(String.valueOf(currentPlayer))) {
                return true; // Check columns
            }
        }

        if (buttons[0][0].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[1][1].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[2][2].getText().equals(String.valueOf(currentPlayer))) {
            return true; // Check diagonal
        }

        if (buttons[0][2].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[1][1].getText().equals(String.valueOf(currentPlayer)) &&
                buttons[2][0].getText().equals(String.valueOf(currentPlayer))) {
            return true; // Check reverse diagonal
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("")) {
                    return false; // There is an empty space, game is not a draw yet
                }
            }
        }
        return true; // Board is full, it's a draw
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        currentPlayer = 'X';
    }

    private void updateScore(char player) {
        if (player == 'X') {
            player1Wins++;
        } else {
            player2Wins++;
        }
        totalGames++;
        updateScoreLabel();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + player1NameField.getText() + " - " + player1Wins + ", " +
                player2NameField.getText() + " - " + player2Wins + ", Total Games: " + totalGames);
    }

    private void promptPlayAgain() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetBoard();
        } else {
            determineWinnerAndExit();
        }
    }

    private void determineWinnerAndExit() {
        String winner;
        if (player1Wins > player2Wins) {
            winner = player1NameField.getText();
        } else if (player2Wins > player1Wins) {
            winner = player2NameField.getText();
        } else {
            winner = "No one";
        }
        JOptionPane.showMessageDialog(this, "Game Over!\nWinner: " + winner + "\nScore: " +
                player1NameField.getText() + " - " + player1Wins + ", " +
                player2NameField.getText() + " - " + player2Wins);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TicTacToe().setVisible(true);
        });
    }
}
