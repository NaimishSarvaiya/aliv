package com.iotsmartaliv.fragments.community;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotsmartaliv.adapter.CommunityListAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.ResArrayObjectData;
import com.iotsmartaliv.apiAndSocket.models.SuccessArrayResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentCommunityListBinding;


//import butterknife.OnClick;
//import butterknife.Unbinder;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommunityListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityListFragment extends Fragment implements RetrofitListener<SuccessArrayResponse> {
    ApiServiceProvider apiServiceProvider;
    CommunityListAdapter communityListAdapter;
    private OnFragmentInteractionListener mListener;

    FragmentCommunityListBinding binding;

    public CommunityListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CommunityListFragment.
     */
    public static CommunityListFragment newInstance() {
        return new CommunityListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCommunityListBinding.inflate(inflater,container,false);
//        View view = inflater.inflate(R.layout.fragment_community_list, container, false);

        apiServiceProvider = ApiServiceProvider.getInstance(getContext(),false);
        communityListAdapter = new CommunityListAdapter(getContext(), mListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewCommunity.setLayoutManager(mLayoutManager);
        binding.recyclerViewCommunity.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCommunity.setAdapter(communityListAdapter);
        apiServiceProvider.callForListOfCommunity(LOGIN_DETAIL.getAppuserID(), this);
        binding.floatingActionButton.setOnClickListener(v-> onViewClicked() );
        return binding.getRoot();
    }

    public void setOnFragmentInteractionListener(OnFragmentInteractionListener OnFragmentInteractionListener) {
        mListener = OnFragmentInteractionListener;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public void onResponseSuccess(SuccessArrayResponse successArrayResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_LIST_API:
                if (successArrayResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (successArrayResponse.getData().size() > 0) {
                        communityListAdapter.addItem(successArrayResponse.getData());
                    } else {
                        if (mListener != null) {
                            mListener.onListEmptyCallback(false);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), successArrayResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_LIST_API:
                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void onViewClicked() {
        if (mListener != null) {
            mListener.onListEmptyCallback(true);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteractionSelectCategory(ResArrayObjectData uri);

        void onListEmptyCallback(boolean b);
    }

}
