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

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.CommunityListAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessArrayResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentCommunitySubListBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CommunitySubListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunitySubListFragment extends Fragment implements RetrofitListener<SuccessArrayResponse> {
    private static final String ARG_PARAM_CATEGORY = "category";
    CommunityListAdapter communityListAdapter;
    ApiServiceProvider apiServiceProvider;
    private String categoryID;
    FragmentCommunitySubListBinding binding;

    // private OnFragmentInteractionListener mListener;

    public CommunitySubListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CommunitySubListFragment.
     */
    public static CommunitySubListFragment newInstance(String param1) {
        CommunitySubListFragment fragment = new CommunitySubListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_CATEGORY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryID = getArguments().getString(ARG_PARAM_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCommunitySubListBinding.inflate(inflater,container,false);
//        View view = inflater.inflate(R.layout.fragment_community_sub_list, container, false);

        apiServiceProvider = ApiServiceProvider.getInstance(getContext());
        communityListAdapter = new CommunityListAdapter(getContext(), null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerViewCommunity.setLayoutManager(mLayoutManager);
        binding.recyclerViewCommunity.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewCommunity.setAdapter(communityListAdapter);
        apiServiceProvider.callForListOfSubCommunity(categoryID, this);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }


    @Override
    public void onResponseSuccess(SuccessArrayResponse successArrayResponse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.SUB_COMMUNITY_LIST_API:
                if (successArrayResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (successArrayResponse.getData().size() > 0) {
                        communityListAdapter.addItem(successArrayResponse.getData());
                    } else {
                        Toast.makeText(getContext(), "Sub Community Not found", Toast.LENGTH_LONG).show();
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
            case Constant.UrlPath.SUB_COMMUNITY_LIST_API:
                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

   /*

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

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
