package com.example.hammerox.oxsudoku;

import java.util.List;

public class PuzzleSnapshot {
    final static int GRID_SIZE = 81;
    final static int KEYBOARD_SIZE = 9;

    private int lastInputId = -1;
    private Boolean lastInputIsPencil;
    private int emptyCells;

    private List<Integer> puzzleSolution;
    private List<Boolean> hasSolution;
    private List<Integer> puzzleAnswers;
    private List<Boolean> isAnswerCorrect;
    private List<Boolean> hasUserInput;
    private List<List<Integer>> puzzlePencil;
    private List<Boolean> isNumberComplete;

    public PuzzleSnapshot(SudokuGrid sudokuGrid) {
        lastInputId = sudokuGrid.getLastInputId();
        lastInputIsPencil = sudokuGrid.getLastInputIsPencil();
        emptyCells = sudokuGrid.getEmptyCells();

        puzzleSolution = sudokuGrid.getPuzzleSolution();
        hasSolution = sudokuGrid.getHasSolution();
        puzzleAnswers = sudokuGrid.getPuzzleAnswers();
        isAnswerCorrect = sudokuGrid.getIsAnswerCorrect();
        hasUserInput = sudokuGrid.getHasUserInput();
        puzzlePencil = sudokuGrid.getPuzzlePencil();
        isNumberComplete = sudokuGrid.getIsNumberComplete();
    }

    public PuzzleSnapshot(int emptyCells,
                          List<Boolean> hasSolution,
                          List<Boolean> hasUserInput,
                          List<Boolean> isAnswerCorrect,
                          List<Boolean> isNumberComplete,
                          int lastInputId,
                          Boolean lastInputIsPencil,
                          List<Integer> puzzleAnswers,
                          List<List<Integer>> puzzlePencil,
                          List<Integer> puzzleSolution) {

        this.emptyCells = emptyCells;
        this.hasSolution = hasSolution;
        this.hasUserInput = hasUserInput;
        this.isAnswerCorrect = isAnswerCorrect;
        this.isNumberComplete = isNumberComplete;
        this.lastInputId = lastInputId;
        this.lastInputIsPencil = lastInputIsPencil;
        this.puzzleAnswers = puzzleAnswers;
        this.puzzlePencil = puzzlePencil;
        this.puzzleSolution = puzzleSolution;
    }


    //////////  GETTERS AND SETTERS //////////


    public int getEmptyCells() {
        return emptyCells;
    }

    public void setEmptyCells(int emptyCells) {
        this.emptyCells = emptyCells;
    }

    public static int getGridSize() {
        return GRID_SIZE;
    }

    public List<Boolean> getHasSolution() {
        return hasSolution;
    }

    public void setHasSolution(List<Boolean> hasSolution) {
        this.hasSolution = hasSolution;
    }

    public List<Boolean> getHasUserInput() {
        return hasUserInput;
    }

    public void setHasUserInput(List<Boolean> hasUserInput) {
        this.hasUserInput = hasUserInput;
    }

    public List<Boolean> getIsAnswerCorrect() {
        return isAnswerCorrect;
    }

    public void setIsAnswerCorrect(List<Boolean> isAnswerCorrect) {
        this.isAnswerCorrect = isAnswerCorrect;
    }

    public List<Boolean> getIsNumberComplete() {
        return isNumberComplete;
    }

    public void setIsNumberComplete(List<Boolean> isNumberComplete) {
        this.isNumberComplete = isNumberComplete;
    }

    public static int getKeyboardSize() {
        return KEYBOARD_SIZE;
    }

    public int getLastInputId() {
        return lastInputId;
    }

    public void setLastInputId(int lastInputId) {
        this.lastInputId = lastInputId;
    }

    public Boolean getLastInputIsPencil() {
        return lastInputIsPencil;
    }

    public void setLastInputIsPencil(Boolean lastInputIsPencil) {
        this.lastInputIsPencil = lastInputIsPencil;
    }

    public List<Integer> getPuzzleAnswers() {
        return puzzleAnswers;
    }

    public void setPuzzleAnswers(List<Integer> puzzleAnswers) {
        this.puzzleAnswers = puzzleAnswers;
    }

    public List<List<Integer>> getPuzzlePencil() {
        return puzzlePencil;
    }

    public void setPuzzlePencil(List<List<Integer>> puzzlePencil) {
        this.puzzlePencil = puzzlePencil;
    }

    public List<Integer> getPuzzleSolution() {
        return puzzleSolution;
    }

    public void setPuzzleSolution(List<Integer> puzzleSolution) {
        this.puzzleSolution = puzzleSolution;
    }
}
