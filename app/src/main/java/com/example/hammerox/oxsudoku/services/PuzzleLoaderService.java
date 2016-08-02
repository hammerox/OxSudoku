package com.example.hammerox.oxsudoku.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.hammerox.oxsudoku.ui.MainActivity;
import com.example.hammerox.oxsudoku.utils.FileManager;
import com.example.hammerox.oxsudoku.utils.Levels;

import java.util.List;


public class PuzzleLoaderService extends IntentService {

    public final static String LOG_SERVICE = PuzzleLoaderService.class.getSimpleName();
    public final static String BROADCAST_SERVICE = "broadcast_puzzle_loader";
    public final static String KEY_IS_COMPLETE = "broadcast_status";
    public final static String KEY_UPDATE = "broadcast_update";

    public static volatile boolean userIsWaiting;
    public static volatile String mLevelName;

    public PuzzleLoaderService() {
        super("PuzzleLoaderService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        int level = intent.getIntExtra(MainActivity.KEY_LEVEL, -1);
        mLevelName = Levels.FILENAMES[level];
        userIsWaiting = intent.getBooleanExtra(MainActivity.KEY_USER_IS_WAITING, true);

        // Runtime check if level already has a backup puzzle
        if (FileManager.hasSavedPuzzle(this, level)) {
            Log.v(LOG_SERVICE, mLevelName + " already has a backup");
        } else {

            Log.v(LOG_SERVICE, "PuzzleLoader " + mLevelName + " Started");

            // Prepare generator
            SudokuGenerator sudokuGenerator = new SudokuGenerator(level);
            int size = SudokuGenerator.GRID_SIZE;

            // Start loop
            for (int i = 0; i < size; i++) {
                sudokuGenerator.tryToRemoveCell(i);     // Main method for puzzle generation

                if (userIsWaiting) {        // If loading screen is on, send update to progressBar
                    float completePercentage = 100 * (float) i / (float) size;
                    sendUpdate(completePercentage);
                }

                if (sudokuGenerator.getEmptyCellsCounter() == sudokuGenerator.getMaxEmptyCells()) {
                    break;      // Stop if the number of empty cells is enough...
                }
            }

            // When finished, set last variables
            List<Boolean> fillList = sudokuGenerator.getEmptyCellList();
            sudokuGenerator.fillToMask(fillList);

            // Save generated puzzle
            // Check if puzzle is to use now or to save as backup
            if (userIsWaiting) {
                sendUpdate(100);
                FileManager.saveCurrentPuzzle(this, sudokuGenerator, level);
                sendResult();
            } else {
                String fileName = Levels.FILENAMES[level];
                FileManager.savePuzzle(this, sudokuGenerator, fileName);
            }
        }

    }


    private void sendUpdate(float percentage) {
        Intent resultIntent = new Intent(BROADCAST_SERVICE);
        resultIntent.putExtra(KEY_IS_COMPLETE, false);
        resultIntent.putExtra(KEY_UPDATE, percentage);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }


    private void sendResult() {
        Log.v(LOG_SERVICE, "Loading finished!");
        Intent resultIntent = new Intent(BROADCAST_SERVICE);
        resultIntent.putExtra(KEY_IS_COMPLETE, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }
}
