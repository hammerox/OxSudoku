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
}
