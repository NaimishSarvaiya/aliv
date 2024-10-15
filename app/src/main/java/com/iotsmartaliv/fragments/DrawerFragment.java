package com.iotsmartaliv.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.doormaster.vphone.inter.DMVPhoneModel;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.InstructorActivity;
import com.iotsmartaliv.activity.LoginActivity;
import com.iotsmartaliv.activity.MainActivity;
import com.iotsmartaliv.activity.PrivacyPolicyActivity;
import com.iotsmartaliv.activity.SettingActivity;
import com.iotsmartaliv.activity.ViewPager.BroadcastCommunityActivity;
import com.iotsmartaliv.activity.automation.HomeAutomationActivity;
import com.iotsmartaliv.activity.feedback.FeedBackActivity;
import com.iotsmartaliv.apiAndSocket.models.ResArrayObjectData;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.fragments.community.CommunityJoinFragment;
import com.iotsmartaliv.fragments.community.CommunityListFragment;
import com.iotsmartaliv.fragments.community.CommunitySubListFragment;
import com.iotsmartaliv.services.ShakeOpenService;
import com.iotsmartaliv.utils.SharePreference;

import java.util.ArrayList;
import java.util.Objects;

//import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static com.iotsmartaliv.activity.MainActivity.drawerLayout;
import static com.iotsmartaliv.constants.Constant.API_AUTH;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This fragment class is used for left fragment drawer.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class DrawerFragment extends Fragment implements View.OnClickListener, CommunityJoinFragment.OnJoinCommunityFragmentInListener, CommunityListFragment.OnFragmentInteractionListener, HomeFragment.OnJoinCommunityFragmentInListener {
    TextView tv_profile_name;
    private RelativeLayout relHome, rlSetting, relMyAccount, relNotifications, relPayment, relHelp, relAboutUs,
            relMessage, rel_community, rel_logout, rel_instructor, rel_communityBroadcast/*,rl_tutorial*/, rel_feedback, rel_privacyPolicy;
    private ImageView imgCross;
    ArrayList <String> appFeture;

    public DrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setOnonJoinCommunityFragmentInListener(this);
        loadFragments(homeFragment, false);
        initViews(layout);
        initListener();
        try {
            tv_profile_name.setText(LOGIN_DETAIL.getUsername());
        } catch (Exception e) {
            e.getStackTrace();
            getActivity().finish();
        }
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LOGIN_DETAIL != null)
            tv_profile_name.setText(LOGIN_DETAIL.getUsername());
    }

    /**
     * Initialize listener
     */
    private void initListener() {
        rel_instructor.setOnClickListener(this);
        relHome.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        rel_community.setOnClickListener(this);
        relMyAccount.setOnClickListener(this);
        relNotifications.setOnClickListener(this);
        relPayment.setOnClickListener(this);
        relHelp.setOnClickListener(this);
        relAboutUs.setOnClickListener(this);
        imgCross.setOnClickListener(this);
        rel_logout.setOnClickListener(this);
        relMessage.setOnClickListener(this);
        rel_communityBroadcast.setOnClickListener(this);
        rel_feedback.setOnClickListener(this);
        rel_privacyPolicy.setOnClickListener(this);
        /*rl_tutorial.setOnClickListener(this);*/
    }

    /**
     * Initialize views
     */
    private void initViews(View layout) {
        appFeture = (ArrayList<String>) SharePreference.getInstance(getActivity()).getFeatureForApp();
        rel_instructor = layout.findViewById(R.id.rel_instructor);
        relHome = layout.findViewById(R.id.rel_home);
        rlSetting = layout.findViewById(R.id.rl_setting);
        rel_community = layout.findViewById(R.id.rel_community);
        rel_logout = layout.findViewById(R.id.rel_logout);
        rel_communityBroadcast = layout.findViewById(R.id.rl_communitybroadcast);
        /*rl_tutorial = layout.findViewById(R.id.rl_tutorial);*/
        relMyAccount = layout.findViewById(R.id.rel_my_account);
        relNotifications = layout.findViewById(R.id.rel_notification);
        relPayment = layout.findViewById(R.id.rel_payment);
        relHelp = layout.findViewById(R.id.rel_help);
        relAboutUs = layout.findViewById(R.id.rel_about_us);
        imgCross = layout.findViewById(R.id.img_cross);
        relMessage = layout.findViewById(R.id.rel_message);
        tv_profile_name = layout.findViewById(R.id.tv_profile_name);
        rel_feedback = layout.findViewById(R.id.rel_feedback);
        rel_privacyPolicy = layout.findViewById(R.id.rel_privacyPolicy);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rel_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setOnonJoinCommunityFragmentInListener(this);
                loadFragments(homeFragment, true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ((MainActivity) Objects.requireNonNull(getActivity())).tvHeader.setText("");
                }
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.VISIBLE);
                break;
            case R.id.rel_community:
                drawerLayout.closeDrawer(GravityCompat.START);
                CommunityListFragment communityListFragment = CommunityListFragment.newInstance();
                communityListFragment.setOnFragmentInteractionListener(this);
                ((MainActivity) getActivity()).tvHeader.setText("Communities");
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
                loadFragments(communityListFragment, true);
                break;
            case R.id.rel_my_account:
                drawerLayout.closeDrawer(GravityCompat.START);
                ((MainActivity) getActivity()).tvHeader.setText(getResources().getString(R.string.my_account));
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
                Fragment fragmentMyAccount = new MyAccountFragment();
                loadFragments(fragmentMyAccount, true);
                break;
            case R.id.rel_notification:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment fragmentNotification = new NotificationFragment();
                ((MainActivity) getActivity()).tvHeader.setText(getResources().getString(R.string.notification));
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
                loadFragments(fragmentNotification, true);
                break;
            case R.id.rel_payment:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment fragmentPayment = new PaymentFragment();
                ((MainActivity) getActivity()).tvHeader.setText(getResources().getString(R.string.payment));
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
                loadFragments(fragmentPayment, true);
                break;
            case R.id.rel_help:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment fragmentHelp = new HelpFragment();
                ((MainActivity) getActivity()).tvHeader.setText(getResources().getString(R.string.help));
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
                loadFragments(fragmentHelp, true);
                break;
            case R.id.rel_about_us:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment fragmentAboutUs = new AboutUsFragment();
                ((MainActivity) getActivity()).tvHeader.setText(getResources().getString(R.string.about_us));
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
                loadFragments(fragmentAboutUs, true);
                break;
            case R.id.rel_message:
                drawerLayout.closeDrawer(GravityCompat.START);
                Fragment messageFragment = new MessageFragment();
                ((MainActivity) getActivity()).tvHeader.setText(getResources().getString(R.string.message));
                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
                loadFragments(messageFragment, true);
                break;

            case R.id.rl_communitybroadcast:
                drawerLayout.closeDrawer(GravityCompat.START);
                if (appFeture.contains(Constant.BROADCAST_MANAGMENT)) {
                    Intent intent1 = new Intent(getContext(), BroadcastCommunityActivity.class);
                    startActivity(intent1);
                }else {
                    Toast.makeText(requireActivity(),"Notice Board is not enabled for your community. Please contact your admin. Thanks!",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rel_feedback:
                drawerLayout.closeDrawer(GravityCompat.START);
                        if (appFeture.contains(Constant.FEEDBACK_MANAGMENT)) {
                            Intent feddbackintent = new Intent(getContext(), FeedBackActivity.class);
                            startActivity(feddbackintent);
                        }else {
                            Toast.makeText(requireActivity(),"Feedback Management is not enabled for your community. Please contact your admin. Thanks!",Toast.LENGTH_LONG).show();
                        }
//                Intent feddbackintent = new Intent(getContext(), FeedBackActivity.class);
//                startActivity(feddbackintent);
                break;
            case R.id.rel_privacyPolicy:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent privacyPolicyIntent = new Intent(getContext(), PrivacyPolicyActivity.class);
                startActivity(privacyPolicyIntent);
                break;
            case R.id.rel_logout:
                drawerLayout.closeDrawer(GravityCompat.START);
                new AwesomeInfoDialog(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage("Are you sure?\n Do you want to logout?")
                        .setColoredCircle(R.color.colorPrimary)
                        .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                        .setCancelable(true)
                        .setPositiveButtonText(getString(R.string.dialog_yes_button))
                        .setPositiveButtonbackgroundColor(R.color.Orange)
                        .setPositiveButtonTextColor(R.color.white)
                        .setNegativeButtonText(getString(R.string.dialog_no_button))
                        .setNegativeButtonbackgroundColor(R.color.colorPrimary)
                        .setNegativeButtonTextColor(R.color.Orange)
                        .setPositiveButtonClick(() -> {
                            //click
                            LOGIN_DETAIL = null;
                            DMVPhoneModel.exit();
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    try {
                                        FirebaseMessaging.getInstance().deleteToken();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.d("FCMTOKEN", "doInBackground: " + e.getLocalizedMessage());
                                    }
                                    return null;
                                }
                                @Override
                                protected void onPostExecute(Void result) {
                                    Log.d("FCMTOKEN", "doInBackground: Done");
                                }
                            }.execute();
                            getActivity().stopService(new Intent(getContext(), ShakeOpenService.class));
                            SharePreference.getInstance(getContext()).delete(API_AUTH);
                            SharePreference.getInstance(getContext()).clearPref();
                            SharePreference.getInstance(getContext()).putBoolean(Constant.HAS_ON_BOARDING_SHOWN, true);
                            // startActivity(new Intent(getContext(), SplashActivity.class));

                            // login in issue-----------
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            getActivity().finish();
                            //--------------------
                        })
                        .setNegativeButtonClick(new Closure() {
                            @Override
                            public void exec() {

                            }
                        })
                        .show();

                break;
            case R.id.img_cross:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.rl_setting:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;

            case R.id.rel_instructor:
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(getContext(), InstructorActivity.class));
                break;
           /* case R.id.rl_tutorial:
                drawerLayout.closeDrawer(Gravity.START);
                Intent intent = new Intent(getContext(), OnBoardingActivity.class);
                intent.putExtra(Constant.FROM_DRAWER, true);
                startActivity(intent);
                break;*/

        }
    }

    /**
     * This method is used for load the fragments.
     *
     * @param fragment   is object of fragment
     * @param isBackTrue is enable back.
     */
    private void loadFragments(Fragment fragment, boolean isBackTrue) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isBackTrue) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * this method is use for change fragment for community
     */
    @Override
    public void onChangeJoinCommunityFaragment() {
        drawerLayout.closeDrawer(GravityCompat.START);
        ((MainActivity) getActivity()).tvHeader.setText("Communities");
        ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
        CommunityListFragment communityListFragment = CommunityListFragment.newInstance();
        communityListFragment.setOnFragmentInteractionListener(this);
        loadFragments(communityListFragment, false);
    }

    @Override
    public void onFragmentInteractionSelectCategory(ResArrayObjectData category) {
        drawerLayout.closeDrawer(GravityCompat.START);
        ((MainActivity) getActivity()).tvHeader.setText(category.getCommunityName());
        CommunitySubListFragment communitySubListFragment = CommunitySubListFragment.newInstance(category.getCommunityID());
        ((MainActivity) getActivity()).setFragment(communitySubListFragment);
        ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
        loadFragments(communitySubListFragment, true);
    }

    @Override
    public void onListEmptyCallback(boolean b) {
        addJoinCommunity(b);
    }

    @Override
    public void addChangeJoinCommunityFaragment() {
        onChangeJoinCommunityFaragment();
    }

    public void addJoinCommunity(Boolean b) {
        CommunityJoinFragment communityJoinFragment = CommunityJoinFragment.newInstance();
        communityJoinFragment.OnFragmentInteractionListener(this);
        ((MainActivity) getActivity()).setFragment(communityJoinFragment);
        ((MainActivity) getActivity()).tvHeader.setText("Join Community");
        ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
        loadFragments(communityJoinFragment, b);
    }
}