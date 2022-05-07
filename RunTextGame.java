import java.util.Scanner;

public class RunTextGame {
    private static final String DISPLAY_SPACER = " ";
    private static Scanner keyboard = new Scanner(System.in);

    static GameBoard game;

    public static void main(String[] args) {
        game = new GameBoard(5, 5);

        while ( game.isGameOver() == false ) {

            displayBoard( game.getGrid(), true );

            String userMove = getUserMove();

            if (userMove.equals("quit") || userMove.equals("exit")) {
                System.out.println("Quitting =(");
                System.exit(0);
            }

            Location loc = convertMoveToLocation(userMove);
            if (loc != null) {
                boolean valid = game.move(loc.getRow(), loc.getCol());
            }

        }

        displayBoard( game.getGrid(), true );

        System.out.println("Game is over!");
    }

    private static Location convertMoveToLocation(String userMove) {
        userMove = userMove.trim();

        String[] vals = null;
        if (userMove.contains(",")) {
            vals = userMove.split(",");
        } else if (userMove.contains(" ")) {
            vals = userMove.split(" ");
        }

        if (vals == null || vals.length < 2) {
            displayErrorFor(userMove);
            return null;
        }

        try {
            int row = Integer.parseInt(vals[0].trim());
            int col = Integer.parseInt(vals[1].trim());

            return new Location(row, col);
        } catch (Exception e){
            displayErrorFor(userMove);
        }

        return null;
    }

    private static void displayErrorFor(String userMove) {
        System.out.println("uh oh!  I don't know what '" + userMove + "' means!");
        System.out.println("Can you re-type your move using the format:  row, col");
        System.out.println("For example:  3, 2    to move in row 3, column 2");
    }

    private static String getUserMove() {
        System.out.println("Type the row and column for your move: ");
        String userResponse = keyboard.nextLine();
        return userResponse;
    }

    private static void displayBoard(int[][] grid, boolean displayRowsCols) {
        if (displayRowsCols) {
            System.out.print("   ");
            for (int c = 0; c < grid[0].length; c++) {
                System.out.print(c + DISPLAY_SPACER);
            }
            System.out.println();
        }

        for (int r = 0; r < grid.length; r++) {

            if (displayRowsCols) {
                System.out.print(" " + r + " ");
            }

            for (int c = 0; c < grid[r].length; c++) {
                int gridVal = grid[r][c];
                String displayVal = getDisplayStringFor( gridVal );
                System.out.print(displayVal + DISPLAY_SPACER);
            }
            System.out.println();
        }
    }

    private static String getDisplayStringFor(int gridVal) {
        if (gridVal == 0) {
            return ".";
        } else if (gridVal == 1) {
            return "X";
        } else if (gridVal == 0) {
            return "0";
        }

        return " ";
    }
}
