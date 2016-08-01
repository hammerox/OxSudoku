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

    public static volatile boolean shouldStop = false;

    public PuzzleLoaderService() {
        super("PuzzleLoaderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        shouldStop = false;
        Log.v(LOG_SERVICE, "PuzzleLoader Started");

        int level = intent.getIntExtra(MainActivity.KEY_LEVEL, -1);
        boolean userIsWaiting = intent.getBooleanExtra(MainActivity.KEY_USER_IS_WAITING, true);

        SudokuGenerator sudokuGenerator = new SudokuGenerator(level);
        sudokuGenerator.preparePuzzle();
        int size = SudokuGenerator.GRID_SIZE;
        for (int i = 0; i < size; i++) {
            sudokuGenerator.tryToRemoveCell(i);

            if (shouldStop) {
                Log.v(LOG_SERVICE, "PuzzleLoader KILLED");
                stopSelf();
            }

            if (userIsWaiting) {        // If on loading screen, send update to progressBar
                float completePercentage = 100 * (float) i / (float) size;
                sendUpdate(completePercentage);
            }

            if (sudokuGenerator.getEmptyCellsCounter() == sudokuGenerator.getMaxEmptyCells()) {
                break;                                  // Stop if the number of empty cells is enough...
            }
        }

        // When finished generating the puzzle
        List<Boolean> fillList = sudokuGenerator.getEmptyCellList();
        sudokuGenerator.fillToMask(fillList);

        sendUpdate(100);

        if (userIsWaiting) {
            FileManager.saveCurrentPuzzle(this, sudokuGenerator, level);
            sendResult();
        } else {
            String fileName = Levels.getFileName(level);
            FileManager.savePuzzle(this, sudokuGenerator, fileName);
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
