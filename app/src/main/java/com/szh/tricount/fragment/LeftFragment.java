package com.szh.tricount.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.szh.tricount.MainActivity;
import com.szh.tricount.R;
import com.szh.tricount.customview.MyView;
import com.szh.tricount.datas.DataList;

/**
 * Created by szh on 2017/2/17.
 */
public class LeftFragment extends Fragment implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;

    private Button equilateralButton;
    private Button isocelesButton;
    private Button rectangularButton;
    private Button squareButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.left_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvents();
    }

    private void initView(View view) {
        equilateralButton = (Button) view.findViewById(R.id.button_equilateral);
        isocelesButton = (Button) view.findViewById(R.id.button_isosceles);
        rectangularButton = (Button) view.findViewById(R.id.button_rectangular);
        squareButton = (Button) view.findViewById(R.id.square);
    }

    private void initEvents() {
        equilateralButton.setOnClickListener(this);
        isocelesButton.setOnClickListener(this);
        rectangularButton.setOnClickListener(this);
        squareButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        MyView myView = MainActivity.getMyView();
        switch (v.getId()) {
            case R.id.button_equilateral:
                if (DataList.getLinesX().size() != 0 || DataList.getLinesY().size() != 0) {
                    toastAndClose();
                    return;
                }
                ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
                myView.initDrawEquilateral(myView.getLeft(), myView.getTop(), myView.getRight(), myView.getBottom());
                break;
            case R.id.button_isosceles:
                if (DataList.getLinesX().size() != 0 || DataList.getLinesY().size() != 0) {
                    toastAndClose();
                    return;
                }
                ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
                myView.initDrawIsosceles(myView.getLeft(), myView.getTop(), myView.getRight(), myView.getBottom());
                break;
            case R.id.button_rectangular:
                if (DataList.getLinesX().size() != 0 || DataList.getLinesY().size() != 0) {
                    toastAndClose();
                    return;
                }
                ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
                myView.initDrawRectangular(myView.getLeft(), myView.getTop(), myView.getRight(), myView.getBottom());
                break;
            case R.id.square:
                if (DataList.getLinesX().size() != 0 || DataList.getLinesY().size() != 0) {
                    toastAndClose();
                    return;
                }
                ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
                myView.initDrawSquare(myView.getLeft(), myView.getTop(), myView.getRight(), myView.getBottom());
                break;
        }
    }

    private void toastAndClose() {
        Toast.makeText(this.getContext(), R.string.requestClear, Toast.LENGTH_SHORT).show();
        ((MainActivity) getActivity()).getDrawerLayout().closeDrawer(Gravity.LEFT);
    }
}
