package com.iotsmartaliv.fragments.instructor;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.InstructorInductionAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.InstructorInductionFragmentBinding;
import com.iotsmartaliv.model.InstructorInductionData;
import com.iotsmartaliv.model.InstructorInductionDataResponse;

import java.util.ArrayList;
import java.util.List;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 22/7/19 :July.
 */

public class InstructorInductionFragment extends Fragment implements RetrofitListener<InstructorInductionDataResponse> {

    public List<InstructorInductionData> instructorInductionList = new ArrayList<>();
    InstructorInductionFragmentBinding binding;
    ApiServiceProvider apiServiceProvider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = InstructorInductionFragmentBinding.inflate(inflater,container,false);
        View view = inflater.inflate(R.layout.instructor_induction_fragment, container, false);
        apiServiceProvider = ApiServiceProvider.getInstance(getContext());
        initiate();
        return binding.getRoot();
    }

    public void initiate() {
        apiServiceProvider.callForInstructorInductionList(LOGIN_DETAIL.getAppuserID(), this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onResponseSuccess(InstructorInductionDataResponse instructorInductionDataResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.INSTRUCTOR_INDUCTION:
                if (instructorInductionDataResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (instructorInductionDataResponse.getData().size() > 0) {
                        instructorInductionList = instructorInductionDataResponse.getData();
                        binding.itemNotFoundTv.setVisibility(View.GONE);
                    } else {
                        instructorInductionList = new ArrayList<>();
                        binding.itemNotFoundTv.setVisibility(View.VISIBLE);

                        //  Toast.makeText(getContext(), "List is Empty", Toast.LENGTH_LONG).show();
                    }
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    binding.recyclerView.setLayoutManager(mLayoutManager);
                    binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                    binding.recyclerView.setAdapter(new InstructorInductionAdapter(getActivity(), instructorInductionList));
                    //recyclerView.getAdapter().notifyDataSetChanged();

                } else {
                    binding.itemNotFoundTv.setVisibility(View.VISIBLE);
                    binding.itemNotFoundTv.setText(instructorInductionDataResponse.getMsg());
                    Toast.makeText(getContext(), instructorInductionDataResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

    }

}
