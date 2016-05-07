package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.hammerox.oxsudoku.Tools.DpScreenSize;


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

        int screenWidth = DpScreenSize.getScreenWeight(getActivity());
        int screenHeight = DpScreenSize.getScreenHeight(getActivity());
        int gridSize;
        if (screenWidth < screenHeight) gridSize = screenWidth; else gridSize = screenHeight;
        /*Todo - Fix squareSize*/
        /* squareSize = Math.round(gridSize / 9)
         * squareSize está dando 40, era pra dar 60 */
        int squareSize = 60;

        GridLayout gridLayout = (GridLayout)rootView.findViewById(R.id.sudoku_gridlayout);
        for(int row = 1; row <= 9; row++) {
            for (int col = 1; col <= 9; col++) {
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(new ViewGroup.LayoutParams(squareSize, squareSize));
                if (col == 1) {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            textView.setBackgroundResource(R.drawable.grid_border_d);
                            break;
                        default:
                            textView.setBackgroundResource(R.drawable.grid_border_a);
                            break;
                    }
                } else if (col == 3 || col == 6) {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            textView.setBackgroundResource(R.drawable.grid_border_f);
                            break;
                        default:
                            textView.setBackgroundResource(R.drawable.grid_border_c);
                            break;
                    }
                } else {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            textView.setBackgroundResource(R.drawable.grid_border_e);
                            break;
                        default:
                            textView.setBackgroundResource(R.drawable.grid_border_b);
                            break;
                    }
                }
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
