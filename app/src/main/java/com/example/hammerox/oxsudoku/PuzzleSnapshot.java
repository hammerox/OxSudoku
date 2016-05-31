package com.example.hammerox.oxsudoku;

import java.util.ArrayList;
import java.util.List;

public class PuzzleSnapshot {
    final static int GRID_SIZE = 81;
    final static int KEYBOARD_SIZE = 9;

    private Boolean lastInputWasFill = false;

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
        this.hasSolution = new ArrayList<>(hasSolution);
        this.hasUserInput = new ArrayList<>(hasUserInput);
        this.isAnswerCorrect = new ArrayList<>(isAnswerCorrect);
        this.isNumberComplete = new ArrayList<>(isNumberComplete);
        this.lastInputId = lastInputId;
        this.lastInputIsPencil = lastInputIsPencil;
        this.puzzleAnswers = new ArrayList<>(puzzleAnswers);
        this.puzzlePencil = copyPuzzlePencil(puzzlePencil);
        this.puzzleSolution = new ArrayList<>(puzzleSolution);
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
                          List<Integer> puzzleSolution,
                          Boolean lastInputWasFill) {

        this.emptyCells = emptyCells;
        this.hasSolution = new ArrayList<>(hasSolution);
        this.hasUserInput = new ArrayList<>(hasUserInput);
        this.isAnswerCorrect = new ArrayList<>(isAnswerCorrect);
        this.isNumberComplete = new ArrayList<>(isNumberComplete);
        this.lastInputId = lastInputId;
        this.lastInputIsPencil = lastInputIsPencil;
        this.puzzleAnswers = new ArrayList<>(puzzleAnswers);
        this.puzzlePencil = copyPuzzlePencil(puzzlePencil);
        this.puzzleSolution = new ArrayList<>(puzzleSolution);
        this.lastInputWasFill = lastInputWasFill;
    }

    public List<List<Integer>> copyPuzzlePencil(List<List<Integer>> original) {
        List<List<Integer>> copy = new ArrayList<>();
        int size = original.size();
        for (int i = 0; i < size; i++) {
            List<Integer> cellCopy = new ArrayList<>(original.get(i));
            copy.add(cellCopy);
        }
        return copy;
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

    public Boolean getLastInputWasFill() {
        return lastInputWasFill;
    }

    public void setLastInputWasFill(Boolean lastInputWasFill) {
        this.lastInputWasFill = lastInputWasFill;
    }
}
