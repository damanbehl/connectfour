package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ConnectFourDriver {
    private static char[][] grid;
    private static Scanner in = new Scanner(System.in);
    private static int ROW_COUNT = 6;
    private static int COLUMN_COUNT = 7;
    private static int WINDOW_LENGTH = 4;
    private static char PLAYER = 'R';
    private static char AI = 'B';

    public static void main(String args[]) {

        initializeGame();
    }

    public static void initializeGame() {
        grid = new char[6][7];
        grid = initializeGrid(grid);
        display(grid);
        playTurn();

    }

    public static char[][] initializeGrid(char[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                grid[row][col] = ' ';
            }
        }
        return grid;
    }

    public static void display(char[][] grid) {
        System.out.println("    1    2   3   4   5   6   7   ");
        System.out.println("  -----------------------------  ");
        for (int row = 0; row < grid.length; row++) {
            System.out.print(String.valueOf(6 - row) + " | ");
            for (int col = 0; col < grid[0].length; col++) {
                System.out.print(grid[row][col]);
                System.out.print(" | ");
            }
            System.out.println();
            System.out.println("  -----------------------------  ");
        }
        // System.out.println(" 1 2 3 4 5 6 7");
        System.out.println();
    }

    public static void playTurn() {
        int turn = 1;
        char player = 'R';
        boolean winner = false;

        // play a turn
        while (winner == false && turn <= 42) {
            int play = -1;
            // display(grid);
            switch (player) {
                case 'R':
                    boolean validPlay = false;
                    // int play = -1;
                    do {
                        System.out.print("Player " + player + ", Which column do you want to put a disc into? ");
                        try {
                            String input = in.nextLine();
                            input = input.strip();
                            play = Integer.parseInt(input) - 1;
                        } catch (NumberFormatException e) {
                            System.out.println("You have entered something other than an "
                                    + "integer such as a string or a float please enter again");
                            continue;
                        }
                        // validate play
                        validPlay = validate(play, grid);
                        if (!validPlay) {
                            if (play < 0) {
                                System.out.println("Please enter postive values for column greater than 0");
                            } else if (play >= grid[0].length) {
                                System.out.println(
                                        "You have entered a number that exceeds the number of columns of the game, please keep the value below 8");
                            } else {
                                System.out.println("This column is already stacked please choose another column");
                            }
                        }

                    } while (validPlay == false);
                    break;

                case 'B':
                    int depth = 5;
                    play = (int) (long) minimax(grid, depth, (long) Double.NEGATIVE_INFINITY,
                            (long) Double.POSITIVE_INFINITY, true)[0];
                    // System.out.println("AI chose column "+(play+1));
                    break;
                default:
                    System.out.println("This shouldn't be happening!!");
                    break;
            }

            grid = dropPiece(grid, play, player);
            display(grid);
            winner = isWinner(player, grid);
            int stopper = whereDoesThePuckStop(grid, play);
            // switch players
            if (player == 'R') {
                System.out.println("Player Red(YOU) have placed the disc in row>>" + (ROW_COUNT - stopper + 1)
                        + " and column>>" + (play + 1));
                player = 'B';
            } else {
                System.out.println("Player Blue(AI) has placed the disc in row>>" + (ROW_COUNT - stopper + 1)
                        + " and column>>" + (play + 1));
                player = 'R';
            }

            turn++;
        }
        System.out.println("Game over! The final gird is as below");
        display(grid);

        if (winner) {
            if (player == 'R') {
                System.out.println("AI won");
            } else {
                System.out.println("you won");
            }
        } else {
            System.out.println("Tie game");
        }
        System.out.println("Would you like to play another game(yes/no)(YES/NO)(case insensitive input)");
        String replay = in.nextLine().strip();
        if (replay.equalsIgnoreCase("YES")) {
            initializeGame();
        } else {
            System.out.println("Thanks for playing!");
        }
    }

    public static boolean validate(int column, char[][] grid) {
        // valid column?
        if (column < 0 || column >= grid[0].length) {
            return false;
        }

        // full column?
        if (grid[0][column] != ' ') {
            return false;
        }

        return true;
    }

    public static boolean isWinner(char player, char[][] grid) {
        // check for 4 across
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == player && grid[row][col + 1] == player && grid[row][col + 2] == player
                        && grid[row][col + 3] == player) {
                    return true;
                }
            }
        }
        // check for 4 up and down
        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == player && grid[row + 1][col] == player && grid[row + 2][col] == player
                        && grid[row + 3][col] == player) {
                    return true;
                }
            }
        }
        // check upward diagonal
        for (int row = 3; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == player && grid[row - 1][col + 1] == player && grid[row - 2][col + 2] == player
                        && grid[row - 3][col + 3] == player) {
                    return true;
                }
            }
        }
        // check downward diagonal
        for (int row = 0; row < grid.length - 3; row++) {
            for (int col = 0; col < grid[0].length - 3; col++) {
                if (grid[row][col] == player && grid[row + 1][col + 1] == player && grid[row + 2][col + 2] == player
                        && grid[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Long[] minimax(char board[][], int depth, long alpha, long beta, boolean maximizingPlayer) {
        int valid_locations[] = getValidLocation(board);
        boolean isTerminal = isTerminalNode(board);
        if (depth == 0 || isTerminal) {
            if (isTerminal) {
                if (isWinner(PLAYER, board)) {
                    return new Long[] { null, -100000000000000L };
                } else if (isWinner(AI, board)) {
                    return new Long[] { null, 100000000000000L };
                }
                // when there are no more valid moves
                else {
                    return new Long[] { null, 0L };
                }
            } else {
                // when depth = 0
                return new Long[] { null, positionScore(board, AI) };
            }
        }
        if (maximizingPlayer) {
            long value = (long) Double.NEGATIVE_INFINITY;
            int column = valid_locations[new Random().nextInt(valid_locations.length)];
            for (int colIter : valid_locations) {
                char[][] board_copy = deepCopy(board);
                board_copy = dropPiece(board_copy, colIter, AI);
                long new_score = minimax(board_copy, depth - 1, alpha, beta, false)[1];
                if (new_score > value) {
                    value = new_score;
                    column = colIter;
                }
                alpha = Math.max(alpha, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return new Long[] { (long) column, value };
        } else {
            long value = (long) Double.POSITIVE_INFINITY;
            int column = valid_locations[new Random().nextInt(valid_locations.length)];
            for (int colIter : valid_locations) {
                char[][] board_copy = deepCopy(board);
                board_copy = dropPiece(board_copy, colIter, PLAYER);
                long new_score = minimax(board_copy, depth - 1, alpha, beta, true)[1];
                if (new_score < value) {
                    value = new_score;
                    column = colIter;
                }
                beta = Math.min(beta, value);
                if (alpha >= beta) {
                    break;
                }
            }
            return new Long[] { (long) column, value };
        }

    }

    public static int[] getValidLocation(char board[][]) {
        ArrayList<Integer> valid_locations = new ArrayList<Integer>();
        for (int column = 0; column < COLUMN_COUNT; column++) {
            boolean flag = validate(column, board);
            if (flag == true) {
                valid_locations.add(column);
            }
        }
        int returnArray[] = new int[valid_locations.size()];
        for (int i = 0; i < valid_locations.size(); i++) {
            returnArray[i] = valid_locations.get(i).intValue();
        }
        return returnArray;

    }

    // public static boolean isValidLocation(char board[][], int column){
    // return board[ROW_COUNT-1][column] == ' ';
    // }

    public static char[][] dropPiece(char board[][], int column, char piece) {
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][column] == ' ') {
                board[row][column] = piece;
                break;
            }
        }
        return board;
    }

    public static boolean isTerminalNode(char[][] board) {
        return isWinner(PLAYER, board) || isWinner(AI, grid) || getValidLocation(board).length == 0;
    }

    public static char[][] deepCopy(char[][] arr) {
        char[][] arrayCopy = new char[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            arrayCopy[i] = Arrays.copyOf(arr[i], arr[i].length);
        }
        return arrayCopy;
    }

    public static long positionScore(char board[][], char piece) {
        int score = 0;
        int centerCount = 0;
        // score for center column
        for (int i = 0; i < ROW_COUNT; i++) {
            if (board[i][COLUMN_COUNT / 2] == piece) {
                centerCount++;
            }
        }
        score = score + centerCount * 3;
        // score horizontal
        for (int i = 0; i < ROW_COUNT; i++) {
            // Pick up individual array
            char window[] = board[i];
            for (int j = 0; j < COLUMN_COUNT - 3; j++) {
                score += windowScore(Arrays.copyOfRange(window, j, j + WINDOW_LENGTH), piece);
            }
        }

        // score vertical
        for (int i = 0; i < COLUMN_COUNT; i++) {
            // Pick up vertical column sequentially
            char columnArray[] = getColumn(board, i);
            for (int j = 0; j < ROW_COUNT - 3; j++) {
                score += windowScore(Arrays.copyOfRange(columnArray, j, j + WINDOW_LENGTH), piece);
            }
        }
        // score for diagonal positive
        for (int row = 0; row < ROW_COUNT - 3; row++) {
            for (int col = 0; col < COLUMN_COUNT - 3; col++) {
                char diagonal[] = getDiagonal(board, row, col);
                score = score + windowScore(diagonal, piece);
            }
        }
        // score for diagonal negative
        for (int row = 0; row < ROW_COUNT - 3; row++) {
            for (int col = 0; col < COLUMN_COUNT - 3; col++) {
                char diagonal[] = getNegativeDiagonal(board, row, col);
                score = score + windowScore(diagonal, piece);
            }
        }

        return score;
    }

    public static int windowScore(char window[], char piece) {
        int score = 0;
        char oppPiece = PLAYER;
        if (piece == PLAYER) {
            oppPiece = AI;
        }
        int count = countOccurences(window, piece);
        int oppCount = countOccurences(window, oppPiece);
        int emptyCount = countOccurences(window, ' ');
        if (count == 4) {
            score += 100;
        } else if (count == 3 && emptyCount == 1) {
            score += 5;
        } else if (count == 2 && emptyCount == 2) {
            score += 2;
        }
        if (oppCount == 3 && emptyCount == 1) {
            score -= 4;
        }
        return score;
    }

    public static int countOccurences(char arr[], char find) {
        int count = 0;
        for (char x : arr) {
            if (x == find) {
                count++;
            }
        }
        return count;
    }

    public static char[] getColumn(char[][] matrix, int column) {
        char[] resultant = new char[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            resultant[i] = matrix[i][column];
        }
        return resultant;
    }

    public static char[] getDiagonal(char[][] matrix, int row, int col) {
        char resultant[] = new char[4];
        for (int i = 0; i < WINDOW_LENGTH; i++) {
            resultant[i] = matrix[row + i][col + i];
        }
        return resultant;
    }

    public static char[] getNegativeDiagonal(char[][] matrix, int row, int col) {
        char resultant[] = new char[4];
        for (int i = 0; i < WINDOW_LENGTH; i++) {
            resultant[i] = matrix[row + 3 - i][col + i];
        }
        return resultant;
    }

    public static int whereDoesThePuckStop(char board[][], int column) {
        int stoppedAt = -1;
        for (int row = board.length - 1; row >= 0; row--) {
            if (board[row][column] == ' ') {
                stoppedAt = row;
                break;
            }
        }
        return stoppedAt + 2;
    }
}