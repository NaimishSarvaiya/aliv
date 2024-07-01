package com.iotsmartaliv.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * This class uses as fragment for show the terms of use.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class TermsOfFragment extends Fragment {

    @BindView(R.id.pdfView)

    PDFView pdfView;
    Unbinder unbinder;
     @BindView(R.id.i_agree_btn)
    Button iAgreeBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_of, container, false);

        unbinder = ButterKnife.bind(this, view);
        pdfView.fromAsset("Term_and_Conditions.pdf")
                .defaultPage(0)
                .onPageChange(null)
                .enableAnnotationRendering(true)
                .onLoad(nbPages -> iAgreeBtn.setVisibility(View.VISIBLE))
                .scrollHandle(new DefaultScrollHandle(getContext()))
                .spacing(0)
                .load();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.i_agree_btn)
    public void onViewClicked() {

        if (getActivity() instanceof SignUpActivity)
            ((SignUpActivity) getActivity()).userAceeptTerm();
//        getFragmentManager().beginTransaction().remove(TermsOfFragment.this).commit();
        getFragmentManager().popBackStack();
    }


}
