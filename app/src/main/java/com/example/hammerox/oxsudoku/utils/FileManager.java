package com.example.hammerox.oxsudoku.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.hammerox.oxsudoku.services.SudokuGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class FileManager {

    public static final String FILE_NAME = "OxSudoku";
    public static final String CURRENT_PUZZLE = "current_puzzle";
    public static final String LOG_FILE = FileManager.class.getSimpleName();

    private static Gson gson = new Gson();


    public static boolean hasSavedPuzzle(Context context, String fileName) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.contains(fileName);
    }


    public static boolean hasCurrentPuzzle(Context context) {
        return hasSavedPuzzle(context, CURRENT_PUZZLE);
    }


    public static SudokuGenerator loadPuzzle(Context context, String fileName) {
        SharedPreferences prefs = getPreferences(context);
        String jsonObject = prefs.getString(fileName, "");
        Type type = new TypeToken<SudokuGenerator>() {}.getType();

        Log.v(LOG_FILE, "Puzzle loaded: " + fileName);
        return gson.fromJson(jsonObject, type);
    }


    public static void savePuzzle(Context context, SudokuGenerator sudokuGenerator, String fileName) {
        SharedPreferences.Editor editor = getEditor(context);
        String jsonObject = gson.toJson(sudokuGenerator);
        editor.putString(fileName, jsonObject);
        editor.apply();
        Log.v(LOG_FILE, "Puzzle saved: " + fileName);
    }


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }
}
