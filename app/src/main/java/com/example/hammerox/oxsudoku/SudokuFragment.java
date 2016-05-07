package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;


public class SudokuFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SudokuFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SudokuFragment newInstance(String param1, String param2) {
        SudokuFragment fragment = new SudokuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.sudoku_fragment, container, false);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;

        int gridSize;
        if (screenWidth < screenHeight) gridSize = screenWidth; else gridSize = screenHeight;
        int squareSize = gridSize / 9;

        GridLayout gridLayout = (GridLayout)rootView.findViewById(R.id.sudoku_gridlayout);
        for(int row = 1; row <= 9; row++) {
            for (int col = 1; col <= 9; col++) {
                Drawable mDrawable;
                if (col == 1) {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            mDrawable = ResourcesCompat
                                    .getDrawable(getResources(), R.drawable.grid_border_d, null);
                            break;
                        default:
                            mDrawable = ResourcesCompat
                                    .getDrawable(getResources(), R.drawable.grid_border_a, null);
                            break;
                    }
                } else if (col == 3 || col == 6) {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            mDrawable = ResourcesCompat
                                    .getDrawable(getResources(), R.drawable.grid_border_f, null);
                            break;
                        default:
                            mDrawable = ResourcesCompat
                                    .getDrawable(getResources(), R.drawable.grid_border_c, null);
                            break;
                    }
                } else {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            mDrawable = ResourcesCompat
                                    .getDrawable(getResources(), R.drawable.grid_border_e, null);
                            break;
                        default:
                            mDrawable = ResourcesCompat
                                    .getDrawable(getResources(), R.drawable.grid_border_b, null);
                            break;
                    }
                }

                if (col == 4 || col == 5 || col == 6) {
                    switch (row) {
                        case 4:
                        case 5:
                        case 6:
                            int mColor = ContextCompat.getColor(getActivity(), R.color.colorLightGray);
                            mDrawable.setColorFilter(
                                    new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                            break;
                    }
                } else {
                    switch (row) {
                        case 1:
                        case 2:
                        case 3:
                        case 7:
                        case 8:
                        case 9:
                            int mColor = ContextCompat.getColor(getActivity(), R.color.colorLightGray);
                            mDrawable.setColorFilter(
                                    new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                            break;
                    }
                }
                TextView textView = new TextView(getActivity());
                String idString = "major_" + row + col;
                int id = getActivity().getResources()
                        .getIdentifier(idString, "id", getActivity().getPackageName());
                textView.setId(id);
                textView.setLayoutParams(new ViewGroup.LayoutParams(squareSize, squareSize));
                textView.setBackground(mDrawable);
                textView.setText(col + "");
                textView.setGravity(Gravity.CENTER);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextSize(30);
                gridLayout.addView(textView);
            }
        }


        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
