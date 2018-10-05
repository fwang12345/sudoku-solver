public class Square {
    public int value;
    public boolean[] unavail;
    public boolean[] tested;
    
    /**
     * Description: Creates an instance of a square on the sudoku grid
     * Input: int value
     */
    public Square(int value) {
        if (value < 0 || value > 9) {
            throw new RuntimeException("ERROR: Value is not between 0 and 9");
        }
        this.value = value;
        if (value == 0) {
            unavail = new boolean[9];
            tested = new boolean[9];
        }
    }
}