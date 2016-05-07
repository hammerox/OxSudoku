package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

        SudokuGrid.createGrid(getActivity(), rootView, squareDim);

        final TypedArray ta = getActivity().getTheme().obtainStyledAttributes(
                new int[] {android.R.attr.actionBarSize});
        int actionBarHeight = (int) ta.getDimension(0, 0);
        actionBarHeight = Math.round(MetricConverter.convertDpToPixel(actionBarHeight, getActivity()));

        int keyboardDim = screenHeight - actionBarHeight - 9 * squareDim
                - Math.round(MetricConverter.convertDpToPixel(10, getActivity()));

        GridLayout keyboardLayout = (GridLayout) rootView.findViewById(R.id.sudoku_keyboard);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(keyboardDim, keyboardDim);
        params.addRule(RelativeLayout.BELOW, R.id.sudoku_gridlayout);
        keyboardLayout.setLayoutParams(params);

        /*Todo - Fix keyDim to fit automatically (the button's padding are messing the size)*/
        int keyDim = (int)Math.floor((keyboardDim - 15)/ 3);
        for (int key = 1; key <= 9; key++) {
            Button button = new Button(getActivity());
            button.setLayoutParams(new ViewGroup.LayoutParams(keyDim, keyDim));
            String idString = "key_" + key;
            int id = getActivity().getResources()
                    .getIdentifier(idString, "id", getActivity().getPackageName());
            button.setId(id);
            button.setText("" + key);
            button.setGravity(Gravity.CENTER);
            button.setTypeface(Typeface.DEFAULT_BOLD);
            button.setTextSize(30);
            keyboardLayout.addView(button);
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
