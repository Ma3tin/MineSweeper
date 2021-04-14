package cz.educanet.minesweeper.logic;

import java.util.Random;

public class Minesweeper {

    private int rowsCount;
    private int columnsCount;
    private Field[][] field;
    private int bombs;
    private boolean bombClicked;
    private int flags;
    private int square;


    public Minesweeper(int rows, int columns) {

        this.rowsCount = rows;
        this.columnsCount = columns;
        field = new Field[columns][rows];
        bombs = 10;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                field[i][j] = new Field();
            }
        }
        createBombsField(rows, columns);
        bombClicked = false;
        flags = bombs;
        square = rows * columns - bombs;
    }


    /**
     * 0 - Hidden
     * 1 - Visible
     * 2 - Flag
     * 3 - Question mark
     *
     * @param x X
     * @param y Y
     * @return field type
     */
    public int getField(int x, int y) {
        return field[x][y].getState();
    }

    /**
     * Toggles the field state, ie.
     * 0 -> 1,
     * 1 -> 2,
     * 2 -> 3 and
     * 3 -> 0
     *
     * @param x X
     * @param y Y
     */
    public void toggleFieldState(int x, int y) {
        if (field[x][y].getState() == 2) field[x][y].setState(0);
        else field[x][y].setState(2);
        flags--;
    }

    /**
     * Reveals the field and all fields adjacent (with 0 adjacent bombs) and all fields adjacent to the adjacent fields... ect.
     *
     * @param x X
     * @param y Y
     */
    public void reveal(int x, int y) {
        if (field[x][y].getBomb()) {
            bombClicked = true;
        }
        recurseReveal(x, y);
        square--;
    }

    private void recurseReveal(int x, int y) {
        field[x][y].setState(1);

        if (getAdjacentBombCount(x, y) == 0) {
            boolean TL = (x != 0) && (y != 0);
            boolean TR = (x != columnsCount - 1) && (y != 0);
            boolean BL = (x != 0) && (y != rowsCount - 1);
            boolean BR = (x != columnsCount - 1) && (y != rowsCount - 1);

            if (TL && !field[x - 1][y - 1].getBomb() && field[x - 1][y - 1].getState() != 1){
                recurseReveal(x - 1, y - 1); //Top left
                square--;
            }

            if ((TL || TR) && !field[x][y - 1].getBomb() && field[x][y - 1].getState() != 1){
                square--;
                recurseReveal(x, y - 1); //Top
            }
            if (TR && !field[x + 1][y - 1].getBomb() && field[x + 1][y - 1].getState() != 1){
                square--;
                recurseReveal(x + 1, y - 1); //Top right
            }
            if ((TR || BR) && !field[x + 1][y].getBomb() && field[x + 1][y].getState() != 1) {
                square--;
                recurseReveal(x + 1, y); //Right
            }
            if (BR && !field[x + 1][y + 1].getBomb() && field[x + 1][y + 1].getState() != 1){
                square--;
                recurseReveal(x + 1, y + 1); //Bottom right
            }
            if ((BR || BL) && !field[x][y + 1].getBomb() && field[x][y + 1].getState() != 1){
                square--;
                recurseReveal(x, y + 1); //Bottom
            }
            if (BL && !field[x - 1][y + 1].getBomb() && field[x - 1][y + 1].getState() != 1){
                square--;
                recurseReveal(x - 1, y + 1); //Bottom left
            }
            if ((BL || TL) && !field[x - 1][y].getBomb() && field[x - 1][y].getState() != 1){
                square--;
                recurseReveal(x - 1, y); //Left
            }
        }
    }

    /**
     * Returns the amount of adjacent bombs
     *
     * @param x X
     * @param y Y
     * @return number of adjacent bombs
     */
    public int getAdjacentBombCount(int x, int y) {
        // TODO
        int bombCounter = 0;

        boolean TL = (x != 0) && (y != 0);
        boolean TR = (x != columnsCount - 1) && (y != 0);
        boolean BL = (x != 0) && (y != rowsCount - 1);
        boolean BR = (x != columnsCount - 1) && (y != rowsCount - 1);

        if (TL && field[x - 1][y - 1].getBomb()) bombCounter++; //Top left
        if ((TL || TR) && field[x][y - 1].getBomb()) bombCounter++; //Top
        if (TR && field[x + 1][y - 1].getBomb()) bombCounter++; //Top right
        if ((TR || BR) && field[x + 1][y].getBomb()) bombCounter++; //Right
        if (BR && field[x + 1][y + 1].getBomb()) bombCounter++; //Bottom right
        if ((BR || BL) && field[x][y + 1].getBomb()) bombCounter++; //Bottom
        if (BL && field[x - 1][y + 1].getBomb()) bombCounter++; //Bottom left
        if ((BL || TL) && field[x - 1][y].getBomb()) bombCounter++; //Left

        return bombCounter;
    }
    /**
     * Checks if there is a bomb on the current position
     */

    /**
     * @param y Y
     * @return true if bomb on position
     */
    public boolean isBombOnPosition(int x, int y) {
        return (field[x][y].getBomb());
    }

    public Field[][] createBombsField(int rows, int columns) {
        // TODO same coords fix
        Random rd = new Random();
        int counter = 0;
        while (counter != bombs) {
            int x = rd.nextInt(columns);
            int y = rd.nextInt(rows);
            while (field[x][y].getBomb()) {
                x = rd.nextInt(columns);
                y = rd.nextInt(rows);
            }
            field[x][y].setBomb(true);
            counter++;
            //System.out.println(x + " " + y);
        }


        /*int bombCount = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (field[i][j].getBomb()) bombCount++;
            }
        }
        System.out.println(bombCount);*/
        return field;
    }

    /**
     * Returns the amount of bombs on the field
     *
     * @return bomb count
     */
    public int getBombCount() {
        return bombs;
    }

    /**
     * total bombs mínus number of flags
     *
     * @return remaining bomb count
     */
    public int getRemainingBombCount() {
        return bombs - flags;
    }

    /**
     * returns true if every flag is on a bomb, else false
     *
     * @return if player won
     */
    public boolean didWin() {
        return (square == 0);
    }

    /**
     * returns true if player revealed a bomb, else false
     *
     * @return if player lost
     */
    public boolean didLoose() {
        if (bombClicked) {
            return true;
        }
        return false;
    }

    public int getRows() {
        return rowsCount;
    }

    public int getColumns() {
        return columnsCount;
    }

}
