package com.example.hammerox.oxsudoku.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hammerox.oxsudoku.services.SudokuGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class FileManager {

    private static Gson gson = new Gson();
    public static final String FILE_NAME = "OxSudoku";
    public static final String CURRENT_PUZZLE = "current_puzzle";

    public static SudokuGenerator loadPuzzle(Context context, String fileName) {
        SharedPreferences prefs = getPreferences(context);
        String jsonObject = prefs.getString(fileName, "");
        Type type = new TypeToken<SudokuGenerator>() {}.getType();

        return gson.fromJson(jsonObject, type);
    }


    public static void savePuzzle(Context context, SudokuGenerator sudokuGenerator, String fileName) {
        SharedPreferences.Editor editor = getEditor(context);
        String jsonObject = gson.toJson(sudokuGenerator);
        editor.putString(fileName, jsonObject);
        editor.apply();
    }


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }


    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }
}
