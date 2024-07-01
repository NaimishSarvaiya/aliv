package com.iotsmartaliv.utils.mail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmailViewModel extends ViewModel {

    private final MutableLiveData<Boolean> emailResult = new MutableLiveData<>();

    public void sendEmail(String recipient, String subject, String content) {
        new Thread(() -> {
            try {
                MailSender.sendMail(recipient, subject, content); // Your existing method
                emailResult.postValue(true); // Success
            } catch (Exception e) {
                emailResult.postValue(false); // Failure
            }
        }).start();
    }

    // Expose LiveData for observing the result
    public LiveData<Boolean> getEmailResult() {
        return emailResult;
    }

}
