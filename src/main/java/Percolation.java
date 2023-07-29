import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final boolean[][] state;
    private int openedCount;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Non positive size detected");
        }

        this.n = n;
        this.state = new boolean[n][n];
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Out of bounds argument detected");
        }

        if (!isOpen(row, col)) {
            state[row - 1][col - 1] = true;
            openedCount++;

            int openedIndex = getIndex(row, col);

            if (row == 1) {
                weightedQuickUnionUF.union(openedIndex, 0);
            }

            if (row == n) {
                weightedQuickUnionUF.union(openedIndex, n * n + 1);
            }

            int[][] adjacentCells = new int[][]{
                    new int[]{row - 1, col},
                    new int[]{row, col - 1},
                    new int[]{row + 1, col},
                    new int[]{row, col + 1},
            };
            for (int[] cell : adjacentCells) {
                if (cell[0] >= 1 && cell[0] <= n && cell[1] >= 1 && cell[1] <= n && isOpen(cell[0], cell[1])) {
                    int adjacentCellIndex = getIndex(cell[0], cell[1]);
                    weightedQuickUnionUF.union(openedIndex, adjacentCellIndex);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Out of bounds argument detected");
        }

        return state[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException("Out of bounds argument detected");
        }

        return isOpen(row, col) && getRoot(getIndex(row, col)) == getRoot(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return getRoot(n * n + 1) == getRoot(0);
    }

    private int getIndex(int row, int col) {
        return (row - 1) * n + col;
    }

    private int getRoot(int index) {
        return weightedQuickUnionUF.find(index);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 3;
        Percolation percolation = new Percolation(n);
        int[][] cells = new int[][]{
                new int[]{1, 1},
                new int[]{2, 1},
                new int[]{1, 3},
                new int[]{2, 3},
                new int[]{3, 2},
        };
        for (int[] cell : cells) {
            percolation.open(cell[0], cell[1]);
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (percolation.isFull(i, j)) {
                    System.out.println(String.format("[%s,%s]: open=%s, full=%s", i, j, percolation.isOpen(i, j),
                            percolation.isFull(i, j)));
                }
            }
        }
    }
}
