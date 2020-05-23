package edu.sapi.mestint;

import java.util.Scanner;

public class Amoba {
    public static int X;
    public static int O;
    public static int EMPTY;

    private int n;
    private int line;
    private int[][] table = new int[][]
            {
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0},
                    new int[]{0, 0, 0, 0, 0, 0, 0, 0},
            };


    public Amoba(int n, int line) {
        this.n = n;
        this.line = line;

        X = 2;
        EMPTY = 0;
        O = line * X + X;

    }

    public static void main(String[] args) {
        int depth = 3;
        Amoba amoba = new Amoba(8, 5);
        AmobaSolver solver = new AmobaSolver(amoba);
        Scanner scanner = new Scanner(System.in);
        int humanFigure;
        int computerFigure;
        int computerPlayerType = AmobaSolver.MAX;
        System.out.println("Choose figure X or O");
        String resp = scanner.next();
        while (!"x".equals(resp.toLowerCase()) && !"o".equals(resp.toLowerCase())) {
            System.out.println("Invalid figure choose X or O");
            resp = scanner.next();
        }
        if ("x".equals(resp.toLowerCase())) {
            humanFigure = AmobaSolver.X;
            computerFigure = AmobaSolver.O;
        } else {
            humanFigure = AmobaSolver.O;
            computerFigure = AmobaSolver.X;
        }
        System.out.println("Figure choosed: " + resp.toUpperCase());

        System.out.println("Do you want to start? y/n");
        resp = scanner.next();
        while (!"y".equals(resp.toLowerCase()) && !"n".equals(resp.toLowerCase())) {
            System.out.println("Invalid response choose y or n");
            resp = scanner.next();
        }
        int player = "y".equals(resp.toLowerCase()) ? humanFigure : computerFigure;

        amoba.printOut();

        boolean full = false;
        while (!full) {
            if (player == humanFigure) {
                humanStep(amoba, humanFigure, scanner);
                if (amoba.isSolved()) {
                    System.out.println("You won!");
                    break;
                }
                player = computerFigure;
            } else {
                solver.putOnTable(player, computerPlayerType, 0, false, 4, true);
                if (amoba.isSolved()) {
                    System.out.println("Computer wins!");
                    break;
                }
                player = humanFigure;
            }
            amoba.printOut();
            full = amoba.isDrawn();
        }
        if (full) {
            System.out.println("No winner!");
        }
        amoba.printOut();
        scanner.close();
    }

    public static void humanStep(Amoba amoba, int figure, Scanner scanner) {
        System.out.println("Your step, enter coordinates");
        int row = 0, column = 0;
        boolean ok = true;
        do {
            if (!ok) {
                System.out.println("That spot is already taken. Choose another one.");
            }
            System.out.println("Row");
            row = scanner.nextInt();
            while ((row < 1) || (row > 8)) {
                System.out.println("Invalid coordinate for row, value must be [1-8]");
                row = scanner.nextInt();
            }
            System.out.println("Column");
            column = scanner.nextInt();
            while ((column < 1) || (column > 8)) {
                System.out.println("Invalid coordinate for column, value must be [1-8]");
                column = scanner.nextInt();
            }
            ok = amoba.isEmpty(row - 1, column - 1);
        } while (!ok);
        amoba.placeElement(row - 1, column - 1, figure == 0 ? X : O);
    }

    public boolean isSolved() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (table[i][j] != EMPTY) {
                    if (isEastVestSolved(i, j)) {
                        return true;
                    } else if (isNordSouthSolved(i, j)) {
                        return true;
                    } else if (isEastSouthSolved(i, j)) {
                        return true;
                    } else if (isNorthEastSolved(i, j)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isDrawn() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (table[i][j] == EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isEastVestSolved(int i, int j) {
        if (j + line > n) {
            return false;
        } else {
            int sum = 0;

            for (int k = j; k < j + line; ++k) {
                sum += table[i][k];
            }

            return sum == line * X || sum == line * O;
        }
    }

    private boolean isNordSouthSolved(int i, int j) {
        if (i - line < -1) {
            return false;
        } else {
            int sum = 0;

            for (int k = i; k > i - line; --k) {
                sum += table[k][j];
            }

            return sum == line * X || sum == line * O;
        }
    }

    private boolean isEastSouthSolved(int i, int j) {
        if (i + line > n || j + line > n) {
            return false;
        } else {
            int sum = 0;

            for (int k = 0; k < line; ++k) {
                sum += table[i + k][j + k];
            }

            return sum == line * X || sum == line * O;
        }
    }

    private boolean isNorthEastSolved(int i, int j) {
        if (i - line < -1 || j + line > n) {
            return false;
        } else {
            int sum = 0;

            for (int k = 0; k < line; ++k) {
                sum += table[i - k][j + k];
            }

            return sum == line * X || sum == line * O;
        }
    }

    public void printOut() {
        System.out.print("   ");
        for (int i = 1; i <= n; i++) {
            System.out.print(i + "  ");
        }
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.print((i + 1) + "  ");
            for (int j = 0; j < n; j++) {
                if (table[i][j] == EMPTY) {
                    System.out.print("_  ");
                } else if (table[i][j] == X) {
                    System.out.print("X  ");
                } else {
                    System.out.print("O  ");
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    public int getDimension() {
        return n;
    }

    private int getOpposite(int type) {
        if (type == X) {
            return O;
        }

        return X;
    }

    public boolean isEmpty(int i, int j) {
        return table[i][j] == EMPTY;
    }

    public void placeElement(int i, int j, int element) {
        table[i][j] = element;
    }

    public double getEstimatedValue(int i, int j) {
        if (isSolved()) {
            return 1;
        }

        double value = 0;

        value += getEastValue(i, j, false);
        value += getVestValue(i, j, false);
        value += getNorthValue(i, j, false);
        value += getSouthValue(i, j, false);
        value += getNorthEastValue(i, j, false);
        value += getNorthVestValue(i, j, false);
        value += getSouthVestValue(i, j, false);
        value += getSouthEastValue(i, j, false);

        return value;
    }

    private double getEastValue(int i, int j, boolean opposite) {
        final int currentVal = table[i][j];
        final int oppositeVal = getOpposite(table[i][j]);

        double numOfCurrent = 0;
        double numOfOpposite = 0;

        for (int k = 1; k < line; k++) {
            if (j + k < n) {
                if (table[i][j + k] == currentVal) {
                    numOfCurrent += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (int k = 1; k < line; k++) {
            if (j + k < n) {
                if (table[i][j + k] == oppositeVal) {
                    numOfOpposite += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        double currVal = 1 / (16 * Math.pow(2, (line - numOfCurrent)));
        double oppsVal = 1 / (16 * Math.pow(2, (line - numOfOpposite)));

        return currVal + oppsVal;
    }

    private double getVestValue(int i, int j, boolean opposite) {
        final int currentVal = table[i][j];
        final int oppositeVal = getOpposite(table[i][j]);

        double numOfCurrent = 0;
        double numOfOpposite = 0;

        for (int k = 1; k < line; k++) {
            if (j - k >= 0) {
                if (table[i][j - k] == currentVal) {
                    numOfCurrent += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (int k = 1; k < line; k++) {
            if (j - k >= 0) {
                if (table[i][j - k] == oppositeVal) {
                    numOfOpposite += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        double currVal = 1 / (16 * Math.pow(2, (line - numOfCurrent)));
        double oppsVal = 1 / (16 * Math.pow(2, (line - numOfOpposite)));

        return currVal + oppsVal;
    }

    private double getNorthValue(int i, int j, boolean opposite) {
        final int currentVal = table[i][j];
        final int oppositeVal = getOpposite(table[i][j]);

        double numOfCurrent = 0;
        double numOfOpposite = 0;

        for (int k = 1; k < line; k++) {
            if (i - k >= 0) {
                if (table[i - k][j] == currentVal) {
                    numOfCurrent += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (int k = 1; k < line; k++) {
            if (i - k >= 0) {
                if (table[i - k][j] == oppositeVal) {
                    numOfOpposite += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        double currVal = 1 / (16 * Math.pow(2, (line - numOfCurrent)));
        double oppsVal = 1 / (16 * Math.pow(2, (line - numOfOpposite)));

        return currVal + oppsVal;
    }

    private double getSouthValue(int i, int j, boolean opposite) {
        final int currentVal = table[i][j];
        final int oppositeVal = getOpposite(table[i][j]);

        double numOfCurrent = 0;
        double numOfOpposite = 0;

        for (int k = 1; k < line; k++) {
            if (i + k < n) {
                if (table[i + k][j] == currentVal) {
                    numOfCurrent += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (int k = 1; k < line; k++) {
            if (i + k < n) {
                if (table[i + k][j] == oppositeVal) {
                    numOfOpposite += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        double currVal = 1 / (16 * Math.pow(2, (line - numOfCurrent)));
        double oppsVal = 1 / (16 * Math.pow(2, (line - numOfOpposite)));

        return currVal + oppsVal;
    }

    private double getNorthEastValue(int i, int j, boolean opposite) {
        final int currentVal = table[i][j];
        final int oppositeVal = getOpposite(table[i][j]);

        double numOfCurrent = 0;
        double numOfOpposite = 0;

        for (int k = 1; k < line; k++) {
            if (i - k >= 0 && j + k < n) {
                if (table[i - k][j + k] == currentVal) {
                    numOfCurrent += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (int k = 1; k < line; k++) {
            if (i - k >= 0 && j + k < n) {
                if (table[i - k][j + k] == oppositeVal) {
                    numOfOpposite += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        double currVal = 1 / (16 * Math.pow(2, (line - numOfCurrent)));
        double oppsVal = 1 / (16 * Math.pow(2, (line - numOfOpposite)));

        return currVal + oppsVal;
    }

    private double getNorthVestValue(int i, int j, boolean opposite) {
        final int currentVal = table[i][j];
        final int oppositeVal = getOpposite(table[i][j]);

        double numOfCurrent = 0;
        double numOfOpposite = 0;

        for (int k = 1; k < line; k++) {
            if (i - k >= 0 && j - k >= 0) {
                if (table[i - k][j - k] == currentVal) {
                    numOfCurrent += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (int k = 1; k < line; k++) {
            if (i - k >= 0 && j - k >= 0) {
                if (table[i - k][j - k] == oppositeVal) {
                    numOfOpposite += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        double currVal = 1 / (16 * Math.pow(2, (line - numOfCurrent)));
        double oppsVal = 1 / (16 * Math.pow(2, (line - numOfOpposite)));

        return currVal + oppsVal;
    }

    private double getSouthVestValue(int i, int j, boolean opposite) {
        final int currentVal = table[i][j];
        final int oppositeVal = getOpposite(table[i][j]);

        double numOfCurrent = 0;
        double numOfOpposite = 0;

        for (int k = 1; k < line; k++) {
            if (i + k < n && j - k >= 0) {
                if (table[i + k][j - k] == currentVal) {
                    numOfCurrent += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (int k = 1; k < line; k++) {
            if (i + k < n && j - k >= 0) {
                if (table[i + k][j - k] == oppositeVal) {
                    numOfOpposite += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        double currVal = 1 / (16 * Math.pow(2, (line - numOfCurrent)));
        double oppsVal = 1 / (16 * Math.pow(2, (line - numOfOpposite)));

        return currVal + oppsVal;
    }

    private double getSouthEastValue(int i, int j, boolean opposite) {
        final int currentVal = table[i][j];
        final int oppositeVal = getOpposite(table[i][j]);

        double numOfCurrent = 0;
        double numOfOpposite = 0;

        for (int k = 1; k < line; k++) {
            if (i + k < n && j + k < n) {
                if (table[i + k][j + k] == currentVal) {
                    numOfCurrent += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        for (int k = 1; k < line; k++) {
            if (i + k < n && j + k < n) {
                if (table[i + k][j + k] == oppositeVal) {
                    numOfOpposite += 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }

        double currVal = 1 / (16 * Math.pow(2, (line - numOfCurrent)));
        double oppsVal = 1 / (16 * Math.pow(2, (line - numOfOpposite)));

        return currVal + oppsVal;
    }

    public int[][] getTable() {
        return table;
    }
}
