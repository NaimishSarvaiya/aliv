package com.iotsmartaliv.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.ResArrayObjectData;
import com.iotsmartaliv.apiAndSocket.models.SuccessArrayResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;

import java.util.List;

public class ApplyFormViewModel extends ViewModel {

    private MutableLiveData<List<ResArrayObjectData>> communityList = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void fetchCommunity(String userId, ApiServiceProvider apiServiceProvider) {
        apiServiceProvider.callForListOfCommunity(userId, new RetrofitListener<SuccessArrayResponse>() {
            @Override
            public void onResponseSuccess(SuccessArrayResponse response, String apiFlag) {
                if (response.getStatus().equalsIgnoreCase("OK")) {
                    communityList.postValue(response.getData());
                } else {
                    errorMessage.postValue(response.getMsg());
                }
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                errorMessage.postValue(throwable.getMessage());
            }
        });
    }

    public LiveData<List<ResArrayObjectData>> getDataset() {
        return communityList;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

}
