package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hammerox.oxsudoku.Tools.MetricConverter;


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

        int squareDim = screenWidth / 9;

        new SudokuGrid().createGrid(getActivity(), rootView, squareDim);

        final TypedArray ta = getActivity().getTheme().obtainStyledAttributes(
                new int[] {android.R.attr.actionBarSize});
        int actionBarHeight = (int) ta.getDimension(0, 0);
        actionBarHeight = Math.round(MetricConverter.convertDpToPixel(actionBarHeight, getActivity()));

        int keyboardDim = screenHeight - actionBarHeight - 9 * squareDim
                - Math.round(MetricConverter.convertDpToPixel(10, getActivity()));

        /*Todo - Fix keyDim to fit automatically (the button's padding are messing the size)*/
        int keyDim = (int)Math.floor((keyboardDim - 15)/ 3);

        SudokuKeyboard keyboard = new SudokuKeyboard();
        keyboard.createKeyboard(getActivity(), rootView, keyDim);

        new SudokuGrid();

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
