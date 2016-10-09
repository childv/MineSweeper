package com.example.veronica.minesweeper.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Veronica on 9/27/16.
 * Used to store the MineSweeper board matrix and game state.
 */

public class MineSweeperModel {
    //Prevents program from resetting when the screen is rotated
    private static MineSweeperModel instance = null;

    //Initiates model with mines and numbers
    private MineSweeperModel() {
        placeMines();
        placeNumbers();
    }

    public static MineSweeperModel getInstance() {
        if (instance == null) {
            instance = new MineSweeperModel();
        }
        return instance;
    }

    private short gameState = 0;
    public static final short WIN = 1;
    public static final short mineLoss = 2;
    public static final short flagLoss = 3;

    private static final int numberOfMines = 4;
    private static final int boardHeight = 5;
    private static final int boardWidth = 5;

    //Assign integers for MineSweeper values
    public static final short MINE = 9;
    public static final short COVERED = 10;
    public static final short UNCOVERED = 11;
    public static final short FLAGGED = 12;
    private static final short EMPTY = 13;

    //Generates 5x5 board matrix that has mines and number positions
    private short[][] fieldModel = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,}
    };

    //Generates 5x5 board matrix that has flags, covered or empty
    private short[][] coverModel = {
            {COVERED, COVERED, COVERED, COVERED, COVERED},
            {COVERED, COVERED, COVERED, COVERED, COVERED},
            {COVERED, COVERED, COVERED, COVERED, COVERED},
            {COVERED, COVERED, COVERED, COVERED, COVERED},
            {COVERED, COVERED, COVERED, COVERED, COVERED}
    };

    /**
     * Places mines on the model matrix.
     */
    private void placeMines() {
        Random random = new Random();
        int mineCounter = numberOfMines;
        int boardSize = boardWidth * boardHeight;
        while (mineCounter != 0) {
            int randX = Math.round(random.nextInt(boardSize) / boardWidth);
            int randY = Math.round(random.nextInt(boardSize) / boardHeight);

            if (getFieldContent(randX, randY) == EMPTY) {
                Log.d("TAG_DRAW", "Drawing mine at: " + randX + ", " + randY);
                setFieldContent(randX, randY, MINE);
                mineCounter--;
            }
        }
    }

    private void placeNumbers() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                //avoid overwriting mines
                if (getFieldContent(i, j) != MINE) {
                    ArrayList<Short> adjSquares = checkAdjacentSquares(i, j);
                    short mineCounter = 0;
                    for (Short square : adjSquares) {
                        if (square == MINE) {
                            mineCounter++;
                        }
                    }
                    Log.d("TAG_DRAW", "Drawing " + mineCounter + " at: " + i + ", " + j);
                    setFieldContent(i, j, mineCounter);
                }
            }
        }
    }

    /**
     * Collects the adjacent squares of a given square
     *
     * @param i given square's x coordinate
     * @param j given square's y coordinate
     * @return an ArrayList containing the short values of all the adjacent squares
     */
    private ArrayList<Short> checkAdjacentSquares(int i, int j) {
        ArrayList<Short> adjSquares = new ArrayList<>();
        if (i == 0) {
            if (j == 0) {
                //top left corner
                adjSquares.add(getFieldContent(i, j + 1));
                adjSquares.add(getFieldContent(i + 1, j + 1));
                adjSquares.add(getFieldContent(i + 1, j));
            } else if (j == 4) {
                //bottom left corner
                adjSquares.add(getFieldContent(i, j - 1));
                adjSquares.add(getFieldContent(i + 1, j - 1));
                adjSquares.add(getFieldContent(i + 1, j));
            } else {
                //middle left side
                adjSquares.add(getFieldContent(i, j - 1));
                adjSquares.add(getFieldContent(i + 1, j - 1));
                adjSquares.add(getFieldContent(i + 1, j));
                adjSquares.add(getFieldContent(i + 1, j + 1));
                adjSquares.add(getFieldContent(i, j + 1));
            }
        } else if (i == 4) {
            if (j == 0) {
                //top right corner
                adjSquares.add(getFieldContent(i - 1, j));
                adjSquares.add(getFieldContent(i - 1, j + 1));
                adjSquares.add(getFieldContent(i, j + 1));
            } else if (j == 4) {
                //bottom right corner
                adjSquares.add(getFieldContent(i, j - 1));
                adjSquares.add(getFieldContent(i - 1, j - 1));
                adjSquares.add(getFieldContent(i - 1, j));
            } else {
                //middle right side
                adjSquares.add(getFieldContent(i, j - 1));
                adjSquares.add(getFieldContent(i - 1, j - 1));
                adjSquares.add(getFieldContent(i - 1, j));
                adjSquares.add(getFieldContent(i - 1, j + 1));
                adjSquares.add(getFieldContent(i, j + 1));
            }
        } else if (j == 0) {
            //middle top side
            adjSquares.add(getFieldContent(i - 1, j));
            adjSquares.add(getFieldContent(i - 1, j + 1));
            adjSquares.add(getFieldContent(i, j + 1));
            adjSquares.add(getFieldContent(i + 1, j + 1));
            adjSquares.add(getFieldContent(i + 1, j));

        } else if (j == 4) {
            //middle bottom side
            adjSquares.add(getFieldContent(i - 1, j));
            adjSquares.add(getFieldContent(i - 1, j - 1));
            adjSquares.add(getFieldContent(i, j - 1));
            adjSquares.add(getFieldContent(i + 1, j - 1));
            adjSquares.add(getFieldContent(i + 1, j));

        } else {
            //rest of board
            adjSquares.add(getFieldContent(i - 1, j));
            adjSquares.add(getFieldContent(i - 1, j + 1));
            adjSquares.add(getFieldContent(i, j + 1));
            adjSquares.add(getFieldContent(i + 1, j + 1));
            adjSquares.add(getFieldContent(i + 1, j));
            adjSquares.add(getFieldContent(i + 1, j - 1));
            adjSquares.add(getFieldContent(i, j - 1));
            adjSquares.add(getFieldContent(i - 1, j - 1));
        }
        return adjSquares;
    }

    /**
     * Returns what's on the board at the given coordintes
     *
     * @param x specifies row
     * @param y specifies column
     * @return the symbol at the coordinate
     */
    public short getFieldContent(int x, int y) {
        return fieldModel[x][y];
    }

    public void setFieldContent(int x, int y, short state) {
        fieldModel[x][y] = state;
    }

    public short getCoverContent(int x, int y) {
        return coverModel[x][y];
    }

    public void setCoverContent(int x, int y, short state) {
        coverModel[x][y] = state;
    }

    /**
     * Resets the board model
     * Covers all squares and replaces mines and numbers
     * Resets win or loss
     */
    public void resetModel() {
        gameState = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                fieldModel[i][j] = EMPTY;
                coverModel[i][j] = COVERED;
            }
        }
        placeMines();
        placeNumbers();
    }

    /**
     * Checks to see if a mine was uncovered
     */
    private void checkUncoveredMines() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (getFieldContent(i, j) == MINE && getCoverContent(i, j) == UNCOVERED) {
                    gameState = mineLoss;
                }
            }
        }
    }

    /**
     * Checks to see if a flag has been placed to match each mine
     */
    private void checkFlags() {
        int mineCounter = numberOfMines;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (getCoverContent(i, j) == FLAGGED && getFieldContent(i, j) != MINE) {
                    gameState = flagLoss;
                } else if (getCoverContent(i, j) == FLAGGED && getFieldContent(i, j) == MINE) {
                    mineCounter--;
                    if (mineCounter == 0) {
                        gameState = WIN;
                    }
                }
            }
        }
    }

    public void checkGameState() {
        checkFlags();
        checkUncoveredMines();
    }

    public short getGameState() {
        return gameState;
    }
}
