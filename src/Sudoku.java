public class Sudoku {
    /**
     * Description: Reads the given sudoku values from a file
     * Input: String filename
     * Output: Sqaure9[][]
     */
    public static Square[][] readFile(String filename) {
        In inStream = new In(filename);
        Square[][] grid = new Square[9][9];
        for (int row = 0; row < 9; row++) {
            String nums = inStream.readLine();
            for (int column = 0; column < 9; column++) {
                int num = nums.charAt(column) - 48;
                try {
                    grid[row][column] = new Square(num);
                }
                catch (RuntimeException e) {
                    grid[row][column] = new Square(0);
                }
            }
        }
        return grid;
    }
    
    /**
     * Description: Updates the unavailible values in a box of 9 squares and
     * determines whether there are any repeat in values
     * Input: Square[][] grid, int x, int y
     * Output: boolean
     */
    public static boolean updateBox(Square[][] grid, int x, int y) {
        StringBuffer nums = new StringBuffer();
        StringBuffer xInds = new StringBuffer();
        StringBuffer yInds = new StringBuffer();
        boolean repeat = false;
        for (int row = x * 3; row < (x + 1) * 3 && !repeat; row++) {
            for (int col = y * 3; col < (y + 1) * 3 && !repeat; col++) {
                if (grid[row][col].value == 0) {
                    xInds.append(row);
                    yInds.append(col);
                }
                else {
                    int value = grid[row][col].value;
                    for (int i = 0; i < nums.length() && !repeat; i++) {
                        if (value == nums.charAt(i) - 48) {
                            repeat = true;
                        }
                    }
                    nums.append(value);
                }
            }
        }
        for (int i = 0; i < xInds.length() && !repeat; i++) {
            for (int j = 0; j < nums.length(); j++) {
                int num1 = xInds.charAt(i) - 48;
                int num2 = yInds.charAt(i) - 48;
                int num3 = nums.charAt(j) - 48;
                grid[num1][num2].unavail[num3 - 1] = true;
            }
        }
        return repeat;
    }
    
    /**
     * Description: Updates the unavailable values in a row and determines 
     * whether there are any repeat values
     * Input: Square[][] grid, int row
     * Output: boolean
     */
    public static boolean updateRow(Square[][] grid, int row) {
        StringBuffer rowNums = new StringBuffer();
        StringBuffer rowInds = new StringBuffer();
        boolean repeat = false;
        for (int col = 0; col < 9 && !repeat; col++) {
            if (grid[row][col].value == 0) {
                rowInds.append(col);
            }
            else {
                int value = grid[row][col].value;
                    for (int i = 0; i < rowNums.length() && !repeat; i++) {
                        if (value == rowNums.charAt(i) - 48) {
                            repeat = true;
                        }
                    }
                    rowNums.append(value);
            }
        }
        for (int x = 0; x < rowInds.length() && !repeat; x++) {
            for (int y = 0; y < rowNums.length(); y++) {
                int num1 = rowNums.charAt(y) - 48;
                int num2 = rowInds.charAt(x) - 48;
                grid[row][num2].unavail[num1 - 1] = true;
            }
        }
        return repeat;
    }
    
    /**
     * Description: Updates the unavailable values in a column and determines 
     * whether there are any repeat values
     * Input: Square[][] grid, int col
     * Output: boolean
     */
    public static boolean updateCol(Square[][] grid, int col) {
        StringBuffer colNums = new StringBuffer();
        StringBuffer colInds = new StringBuffer();
        boolean repeat = false;
        for (int row = 0; row < 9 && !repeat; row++) {
            if (grid[row][col].value == 0) {
                colInds.append(row);
            }
            else {
                int value = grid[row][col].value;
                for (int i = 0; i < colNums.length() && !repeat; i++) {
                    if (value == colNums.charAt(i) - 48) {
                        repeat = true;
                    }
                }
                colNums.append(value);
            }
        }
        for (int x = 0; x < colInds.length() && !repeat; x++) {
            for (int y = 0; y < colNums.length(); y++) {
                int num1 = colNums.charAt(y) - 48;
                int num2 = colInds.charAt(x) - 48;
                grid[num2][col].unavail[num1 - 1] = true;
            }
        }
        return repeat;
    }
    
    /**
     * Description: Updates the unavailable values of all squares in the grid
     * Input: Square[][] grid
     */
    public static void updateGrid(Square[][] grid) {
        for (int i = 0; i < 9; i++) {
            updateRow(grid, i);
            updateCol(grid, i);
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                updateBox(grid, i, j);
            }
        }
    }
    
    /**
     * Description: Prints the values of all squares in the grid
     * Input: Square[][] grid
     */
    public static void print2DArray(Square[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j].value + " ");
                if (j == grid[0].length - 1) {
                    System.out.println();
                }
            }
        }
        System.out.println();
    }
    
    /**
     * Determines whether a square has any more unavailable values
     * In order for a value to be available, it has to be one of the possible
     * values and it has to be untested 
     * Input: Square[][] grid, int row, int col
     * Output: boolean
     */
    public static boolean avail(Square[][] grid, int row, int col) {
        boolean avail = false;
        for (int i = 0; i < 9 && !avail; i++) {
            if (!grid[row][col].unavail[i] && !grid[row][col].tested[i]) {
                avail = true;
            }
        }
        return avail;
    }
    
    /**
     * Description: For all squares with value 0, reset the unavailible values so that 
     * all squares can now be any value
     * Input: Square[][] grid, StringBuffer row, StringBuffer col
     */
    public static void reset(Square[][] grid, StringBuffer row, 
                             StringBuffer col) {
        for (int i = 0; i < row.length(); i++) {
            int x = row.charAt(i) - 48;
            int y = col.charAt(i) - 48;
            grid[x][y].unavail = new boolean[9];
        }
    }
    
    /**
     * Description: Determines if a puzzle is possible to solve by looking 
     * for any repeats in value and any squares that have 0 possible values
     * Input: Square[][] grid
     */
    public static boolean poss(Square[][] grid) {
        for (int i = 0; i < 9; i++) {
            if (updateRow(grid, i) || updateCol(grid, i)){
                return false;
            }
        }
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (updateBox(grid, i, j)){
                    return false;
                }
            }
        }
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j].value == 0) {
                    String inds = "";
                    for (int k = 0; k < 9; k++) {
                        if (grid[i][j].unavail[k] == false) {
                            inds += k + 1;
                        }
                    }
                    if (inds.length() == 0) {
                        return false;
                    }
                }
            }
        } 
        return true;
    }
    
    /**
     * Description: Solves the puzzle after all squares with only 1 possible
     * value were eliminated
     * Input: Square[][] grid, StringBuffer row, StringBuffer col, int index
     */
    public static void solve(Square[][] grid, StringBuffer row, 
                             StringBuffer col, int index) {
        int x = row.charAt(index) - 48;
        int y = col.charAt(index) - 48;
        if (!avail(grid, x, y)) {
            if (index == 0) {
                System.out.println("This Sudoku puzzle is impossible to " + 
                                   "solve for [" + x + "][" + y + "]");
                PennDraw.clear();
                draw(grid);
                PennDraw.advance();
                print2DArray(grid);
                return;
            }
            else {
                boolean finish = true;
                int x1;
                int y1;
                int index1 = index;
                grid[x][y].tested = new boolean[9];
                for (int i = 1; i <= index && finish; i++) {
                    x1 = row.charAt(index - i) - 48;
                    y1 = col.charAt(index - i) - 48;
                    grid[x1][y1].value = 0;
                    if (avail(grid, x1, y1)) {
                        index1 = index - i;
                        finish = false;
                    }
                    else {
                        grid[x1][y1].tested = new boolean[9];
                    }
                }
                reset(grid, row, col);
                updateGrid(grid);
                
                PennDraw.clear();
                draw(grid);
                PennDraw.advance();
            
                if (finish) {
                    System.out.println("This Sudoku puzzle is impossible " + 
                                       "to solve for [" + x + "][" + y + "]");
                    print2DArray(grid);
                    return;
                }
                else {
                    solve(grid, row, col, index1);
                }
            }
        }
        else {
            boolean found = false;
            for (int i = 0; i < 9 && !found; i++) {
                if (!grid[x][y].unavail[i] && !grid[x][y].tested[i]) {
                    grid[x][y].value = i + 1;
                    grid[x][y].unavail[i] = true;
                    grid[x][y].tested[i] = true;
                    found = true;
                }
            }
            updateRow(grid, x);
            updateCol(grid, y);
            updateBox(grid, x / 3, y / 3);
            
            PennDraw.clear();
            draw(grid);
            PennDraw.advance();
            
            if (index == row.length() - 1) {
                print2DArray(grid);
                return;
            }
            else {
                solve(grid, row, col, index + 1);
            }
        }
    }
    
    /**
     * Description: Continuously assigns values to square with only one 
     * possible value no more squares have only 1 possible value
     * Input: Square[][] grid
     */
    public static void eliminate(Square[][] grid) {
        boolean unsolved = false;
        boolean progress = false;
        if (!poss(grid)) {
            PennDraw.clear();
            draw(grid);
            PennDraw.advance();
            return;
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j].value == 0) {
                    unsolved = true;
                    String inds = "";
                    for (int k = 0; k < 9; k++) {
                        if (grid[i][j].unavail[k] == false) {
                            inds += k + 1;
                        }
                    }
                    if (inds.length() == 1) {
                        grid[i][j].value = inds.charAt(0) - 48;
                        grid[i][j].unavail = null;
                        progress = true;
                    }
                }
            }
        }
        if (!unsolved) {
            PennDraw.clear();
            draw(grid);
            PennDraw.advance();
            return;
        }
        else {
            if (!progress) {
                return;
            }
            else {
                PennDraw.clear();
                draw(grid);
                PennDraw.advance();
                eliminate(grid);
            }
        }
    }
    
    /**
     * Description: Draws the grid
     * Input: Square[][] grid
     */
    public static void draw(Square[][] grid) {
        PennDraw.setFontSize(50);
        PennDraw.setPenRadius();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                PennDraw.square(j, 8 - i, 0.5);
                int value = grid[i][j].value;
                if (value != 0) {
                    PennDraw.text(j, 8 - i, "" + grid[i][j].value);
                }
            }
        }
        PennDraw.setPenRadius(0.01);
        PennDraw.line(-0.5, 2.5, 8.5, 2.5);
        PennDraw.line(-0.5, 5.5, 8.5, 5.5);
        PennDraw.line(2.5, -0.5, 2.5, 8.5);
        PennDraw.line(5.5, -0.5, 5.5, 8.5);
    }
  
    public static void main(String[] args) {
        PennDraw.setCanvasSize(900, 900);
        PennDraw.setXscale(-0.5, 8.5);
        PennDraw.setYscale(-0.5, 8.5);
        PennDraw.enableAnimation(1000);
        
        String filename = args[0];
        Square[][] grid = readFile(filename);
        
        PennDraw.clear();
        draw(grid);
        PennDraw.advance();
        print2DArray(grid);
        
        eliminate(grid);
        if (!poss(grid)) {
            System.out.println("This Sudoku puzzle is impossible to solve");
            print2DArray(grid);
        }
        else {
            StringBuffer row = new StringBuffer();
            StringBuffer col = new StringBuffer();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (grid[i][j].value == 0) {
                        row.append(i);
                        col.append(j);
                    }
                }
            }

            if (row.length() == 0) {
                print2DArray(grid);
            }
            else {
                solve(grid, row, col, 0);
            }
        }
        PennDraw.disableAnimation();
    }
}