package com.iotsmartaliv.modules.cardManager;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.ScanCallback;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentCardListBinding;
import com.iotsmartaliv.model.Chat;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//import butterknife.OnClick;
//import butterknife.Unbinder;

import static com.iotsmartaliv.adapter.DevicelistAdapter.selectDevice;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardListFragment extends Fragment implements RetrofitListener<CardUserListModel> {
    ProgressDialog progress;
    String communityId = "", deviceId = "";
    ApiServiceProvider apiServiceProvider;
    List<CardUserList> cardUserLists = new ArrayList<>();
    FragmentCardListBinding binding;
    List<String> addDataList = new ArrayList<String>();
    List<String> removeDataList = new ArrayList<String>();
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCardListBinding.inflate(inflater, container, false);
//        View view = inflater.inflate(R.layout.fragment_card_list, container, false);

        progress = new ProgressDialog(getContext());
        progress.setMessage("Synchronization on Process.....");
        progress.setCancelable(false);
        if (getArguments() != null) {
            deviceId = getArguments().getString(Constant.DEVICE_ID);
            communityId = getArguments().getString(Constant.COMMUNITY_ID);
        }

        apiServiceProvider = ApiServiceProvider.getInstance(getContext(),false);
        apiServiceProvider.callForCardList(communityId, deviceId, LOGIN_DETAIL.getAppuserID(), this);
        binding.syncBtn.setOnClickListener(v -> onViewClicked());
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    public void onViewClicked() {
        progress.show();
//        List<String> addDataList = new ArrayList<String>();
//        List<String> removeDataList = new ArrayList<String>();
        for (CardUserList data : cardUserLists) {
            if (data.getOperation().equalsIgnoreCase("0")) {
                addDataList.add(data.getUidNumber());
            } else {
                removeDataList.add(data.getUidNumber());
            }
        }
        checkAndRequestBluetoothPermissions();

//        removeCardPatch(removeDataList, addDataList);


    }
    private void checkAndRequestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            List<String> permissionsToRequest = new ArrayList<>();

            // Check BLUETOOTH_CONNECT and BLUETOOTH_SCAN permissions
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN);
            }

            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(requireActivity(), permissionsToRequest.toArray(new String[0]), REQUEST_BLUETOOTH_PERMISSIONS);
                return;
            }
        }

        // If permissions are already granted, proceed with the door opening
        updateCardPatch();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean allPermissionsGranted = true;

            // Check if all requested permissions were granted
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // Permissions granted, proceed with the door unlocking process
                updateCardPatch();
            } else {
                // Permissions denied, show a message to the user
                Toast.makeText(requireActivity(), "Bluetooth permissions are required to unlock the door.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void updateCardPatch() {
        int ret1 = LibDevModel.scanDevice(getContext(), false, 1300, oneKeyScanCallback);         // A key to open the door
        //Naimish
        if (ret1 != 0) {
            Toast.makeText(getContext(), ErrorMsgDoorMasterSDK.getErrorMsg(ret1), Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }

    public void updateCardDataOnServer() {
        HashMap<String, String> stringHashMap = new HashMap<>();
        stringHashMap.put("community_ID", selectDevice.getCommunityID());
        stringHashMap.put("device_ID", selectDevice.getDeviceID());
        stringHashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());

        for (int i = 0; i < cardUserLists.size(); i++) {
            CardUserList data = cardUserLists.get(i);
            stringHashMap.put("user_IDs[" + i + "]", data.getAppuserID());
        }
        apiServiceProvider.callForUpdateSyncUser(stringHashMap, new RetrofitListener<CardUserListModel>() {
            @Override
            public void onResponseSuccess(CardUserListModel sucessRespnse, String apiFlag) {
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    apiServiceProvider.callForCardList(communityId, deviceId, LOGIN_DETAIL.getAppuserID(), CardListFragment.this);
                } else {
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                try {
                    Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void addCardPatch(List<String> addDataList) {
        int ret3 = LibDevModel.writeCard(getContext(), DeviceObject.getLibDev(selectDevice), addDataList, (result, bundle) -> {
            if (result == 0x00) {
                progress.dismiss();
                Toast.makeText(getContext(), "Card Add Success", Toast.LENGTH_SHORT).show();
                updateCardDataOnServer();
            } else {
                progress.dismiss();
                if (result == 48) {
                    Toast.makeText(getContext(), "Result Error Timer Out add card", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error On Add Card ::" + result,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, true);       //Write Card Test
        if (ret3 != 0) {
            Toast.makeText(getContext(), "WriteCardFailure " + ErrorMsgDoorMasterSDK.getErrorMsg(ret3), Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }



    public void addCardPatch(List<String> addDataList, Runnable onSuccess) {
        int ret3 = LibDevModel.writeCard(getContext(), DeviceObject.getLibDev(selectDevice), addDataList, (result, bundle) -> {
            if (result == 0x00) {
                Toast.makeText(getContext(), "Card Add Success", Toast.LENGTH_SHORT).show();
                if (onSuccess != null) {
                    onSuccess.run();
                } else {
                    progress.dismiss();
                    updateCardDataOnServer();
                }
            } else {
                progress.dismiss();
                if (result == 48) {
                    Toast.makeText(getContext(), "Result Error: Timeout on adding card", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error On Add Card: " + ErrorMsgDoorMasterSDK.getErrorMsg(result), Toast.LENGTH_SHORT).show();
                }
            }
        }, true); // Write Card Test
        if (ret3 != 0) {
            Toast.makeText(getContext(), "Write Card Failure: " + ErrorMsgDoorMasterSDK.getErrorMsg(ret3), Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }

    public void deleteCardPatch(List<String> removeDataList) {
        int ret3 = LibDevModel.deleteCard(getActivity(), DeviceObject.getLibDev(selectDevice), removeDataList, (result, bundle) -> {
            if (result == 0x00) {
                progress.dismiss();
                updateCardDataOnServer();
                Toast.makeText(getContext(), "Card Delete Success", Toast.LENGTH_SHORT).show();
            } else {
                progress.dismiss();
                if (result == 48) {
                    Toast.makeText(getContext(), "Result Error: Timeout on deleting card", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error On Delete Card: " + ErrorMsgDoorMasterSDK.getErrorMsg(result), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (ret3 != 0) {
            Toast.makeText(getContext(), "Error On Delete Card: " + ErrorMsgDoorMasterSDK.getErrorMsg(ret3), Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
    }

    public void removeCardPatch(List<String> removeDataList, List<String> dataList) {
        if (removeDataList.size() > 0) {
            int ret3 = LibDevModel.deleteCard(getActivity(), DeviceObject.getLibDev(selectDevice), removeDataList, (result, bundle) -> {
                if (result == 0x00) {
                    if (dataList.size() > 0) {
                        addCardPatch(dataList);
                    } else {
                        progress.dismiss();
                        updateCardDataOnServer();
                    }
                    Toast.makeText(getContext(), "Card Delete Success", Toast.LENGTH_SHORT).show();
                } else {
                    progress.dismiss();
                    if (result == 48) {
                        Toast.makeText(getContext(), "Result Error Timer Out On delete card", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error On Delete Card :" + ErrorMsgDoorMasterSDK.getErrorMsg(result), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (ret3 != 0) {
                Toast.makeText(getContext(), "Error On Delete Card : " + ErrorMsgDoorMasterSDK.getErrorMsg(ret3), Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        } else {
            if (dataList.size() > 0) {
                addCardPatch(dataList);
            } else {
                Toast.makeText(getContext(), "No card Data In the list.", Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }
    }

    @Override
    public void onResponseSuccess(CardUserListModel sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.GET_CARD_USERLIST:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {

                    cardUserLists.clear();
                    if (sucessRespnse.getData().size() > 0) {
                        Log.d("onResponseSuccess", "onResponseSuccess: " + sucessRespnse.getData());
                        cardUserLists = sucessRespnse.getData();
                        Log.d("onResponseSuccess", "onResponseSuccess: " + cardUserLists);
                    } else {
//                        Toast.makeText(getContext(), "List is empty", Toast.LENGTH_SHORT).show();
                    }
                    CardListAdapter adapter = new CardListAdapter(cardUserLists);
                    binding.recyclerView.setHasFixedSize(true);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(adapter);
                } else
                    Log.d("onResponseSuccess", "onResponseSuccess: " + sucessRespnse.getMsg());
                Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();

                break;
        }

    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        try {
            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    ScanCallback oneKeyScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(ArrayList<String> deviceList,
                                 ArrayList<Integer> rssi) {
            if (deviceList.size() != 0) {
                for (int i = 0; deviceList.size() > i; i++) {
                    if (deviceList.get(0).equals(selectDevice.getDeviceSnoWithoutAlphabet())) {
                        if (addDataList.size() > 0) {
                            addCardPatch(addDataList, () -> {
                                if (removeDataList.size() > 0) {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            deleteCardPatch(removeDataList);
                                        }
                                    }, 2000);

                                } else {
                                    progress.dismiss();
                                    updateCardDataOnServer();
                                }
                            });
                        } else if (removeDataList.size() > 0) {
                            deleteCardPatch(removeDataList);
                        } else {
                            Toast.makeText(getContext(), "No card data in the list.", Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    } else {
                        progress.dismiss();
                        Toast.makeText(getContext(), "No Nearby device found.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                progress.dismiss();
                Toast.makeText(getContext(), "No Nearby device found.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onScanResultAtOnce(String s, int i) {

        }
    };
}
