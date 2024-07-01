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
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.ListOfInstructorAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.InstructorListData;
import com.iotsmartaliv.model.InstructorListResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 22/7/19 :July.
 */
public class ListOfInstructorFragment extends Fragment implements RetrofitListener<InstructorListResponse> {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    ApiServiceProvider apiServiceProvider;
    List<InstructorListData> instructorListData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_instructor_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        apiServiceProvider = ApiServiceProvider.getInstance(getContext());
        initiate();
        return view;
    }

    public void initiate() {
        apiServiceProvider.callForInstructorList(LOGIN_DETAIL.getAppuserID(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResponseSuccess(InstructorListResponse instructorListResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.INSTRUCTOR_LIST:
                if (instructorListResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (instructorListResponse.getData().size() > 0) {
                        instructorListData = instructorListResponse.getData();
                    } else {
                        instructorListData = new ArrayList<>();
                        Toast.makeText(getContext(), "List is empty", Toast.LENGTH_SHORT).show();
                    }
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(new ListOfInstructorAdapter(getContext(), instructorListData));
                } else {
                    Toast.makeText(getContext(), instructorListResponse.getMsg(), Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

    }
}
