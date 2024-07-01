package com.iotsmartaliv.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.iotsmartaliv.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.iotsmartaliv.utils.CommanUtils.bitmapToBase64;

/**
 * This activity class is used for adding the product.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int ACCESS_CAMERA_PERMISSION = 1001, ACCESS_EXTERNAL_STORAGE_PERMISSION = 1003;
    private final int GALLERY_REQUEST_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;
    boolean one, two, three;
    private Spinner spinner;
    private ImageView imgOne, imgTwo, imgThree, imgBackAddProduct;
    private RelativeLayout btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        btn = findViewById(R.id.rel_sign_up);
        imgBackAddProduct = findViewById(R.id.imgback);
        imgBackAddProduct.setOnClickListener(view -> finish());
        btn.setOnClickListener(view -> startActivity(new Intent(AddProductActivity.this, ProductListActivity.class)));
        imgOne = findViewById(R.id.uploadimgone);
        imgTwo = findViewById(R.id.uploadimgtwo);
        imgThree = findViewById(R.id.uploadimgthree);
//        unregisterForContextMenu(imgOne);
        imgOne.setOnClickListener(view -> {
            one = true;
            two = false;
            three = false;
//                openImage();
            registerForContextMenu(imgOne);
            if (checkAndRequestPermissions())
                openContextMenu(view);
            else
                checkAndRequestPermissions();
        });
        imgTwo.setOnClickListener(view -> {
            two = true;
            one = false;
            three = false;
//    openImage();
            registerForContextMenu(imgTwo);
            if (checkAndRequestPermissions())
                openContextMenu(view);
            else
                checkAndRequestPermissions();
        });
        imgThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                three = true;
                two = false;
                one = false;
                registerForContextMenu(imgThree);
                if (checkAndRequestPermissions())
                    openContextMenu(view);
                else
                    checkAndRequestPermissions();
            }
        });
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Select Category");
        categories.add("Category 1");
        categories.add("Category 2");
        categories.add("Category 3");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    /**
     * This method is used for open Image gallery.
     */
    private void openImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * This method is used for checking runtime permission.
     */
    private boolean checkAndRequestPermissions() {
        try {
            if (!isAccessCameraAllowed()) {
                //Requesting camera permission
                requestAccessCameraPermission();
            } else if (!isWriteExternalAllowed()) {
                //Requesting external storage permission.
                requestWriteExternalPermission();
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method is used for checking whether camera permission allow or not.
     */
    private boolean isAccessCameraAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.CAMERA);
        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    /**
     * This method is used for request camera permission.
     */
    private void requestAccessCameraPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.CAMERA}, ACCESS_CAMERA_PERMISSION);
    }

    /**
     * This method is used for checking the write external storage permission whether allow or not.
     */
    private boolean isWriteExternalAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;
        //If permission is not granted returning false
    }

    /**
     * This method is used for request write external permission.
     */
    private void requestWriteExternalPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ACCESS_EXTERNAL_STORAGE_PERMISSION);
    }

    /**
     * Handling result of the permission requested
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ACCESS_CAMERA_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkAndRequestPermissions();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this, Manifest.permission.CAMERA)) {
                        checkAndRequestPermissions();
                    } else {
                        //Never ask again and handle your app without permission.
//                        if (!isSetting) {
////                            openPopUpForPermission(getResources().getString(R.string.allow_camera_permission));
//                        }
                    }
                }
                break;
            case ACCESS_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkAndRequestPermissions();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AddProductActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        checkAndRequestPermissions();
                    } else {
                        //Never ask again and handle your app without permission.
//                        if (!isSetting) {
//                            openPopUpForPermission(getResources().getString(R.string.allow_storage_permission));
//                        }
                    }
                }
                break;
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.context_menu_title);
        menu.add(0, v.getId(), 0, R.string.txt_gallary);
        menu.add(0, v.getId(), 0, R.string.txt_camera);
        menu.add(0, v.getId(), 0, R.string.txt_cancel);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == getString(R.string.txt_gallary)) {
            Intent dataBack = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(dataBack, GALLERY_REQUEST_CODE);
            closeContextMenu();
            return true;
        } else if (item.getTitle() == getString(R.string.txt_camera)) {
            Intent dataBack = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(dataBack, CAMERA_REQUEST_CODE);
            closeContextMenu();
            return true;
        } else if (item.getTitle() == getString(R.string.txt_cancel)) {
            closeContextMenu();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Callback methods once user returns back from camera, gallery and other screens.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && one) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        imgOne.setImageURI(selectedImageUri);
                        final InputStream imageStream;
                        try {
                            imageStream = getContentResolver().openInputStream(selectedImageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            String imgProfileBase64 = bitmapToBase64(selectedImage);
//                            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
//                                editUserProfileApi();
//                            } else {
//                                Toast.makeText(this, R.string.msg_noInternet, Toast.LENGTH_SHORT).show();
//                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (resultCode == Activity.RESULT_OK && two) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        imgTwo.setImageURI(selectedImageUri);
                        final InputStream imageStream;
                        try {
                            imageStream = getContentResolver().openInputStream(selectedImageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            String imgProfileBase64 = bitmapToBase64(selectedImage);
//                            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
//                                editUserProfileApi();
//                            } else {
//                                Toast.makeText(this, R.string.msg_noInternet, Toast.LENGTH_SHORT).show();
//                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (resultCode == Activity.RESULT_OK && three) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        imgThree.setImageURI(selectedImageUri);
                        final InputStream imageStream;
                        try {
                            imageStream = getContentResolver().openInputStream(selectedImageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            String imgProfileBase64 = bitmapToBase64(selectedImage);
//                            if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
//                                editUserProfileApi();
//                            } else {
//                                Toast.makeText(this, R.string.msg_noInternet, Toast.LENGTH_SHORT).show();
//                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case CAMERA_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        onCaptureImageResult(data);
                    }
                }
                break;
        }
    }

    /**
     * Retrieve image from captured
     *
     * @param data
     */
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, bytes);
        String imgProfileBase64 = bitmapToBase64(thumbnail);
//        if (NetworkAvailablity.getInstance(this).checkNetworkStatus()) {
//            editUserProfileApi();
//        } else {
//            Toast.makeText(this, R.string.msg_noInternet, Toast.LENGTH_SHORT).show();
//        }
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (one) {
            imgOne.setImageBitmap(thumbnail);
        } else if (two) {
            imgTwo.setImageBitmap(thumbnail);
        } else if (three) {
            imgThree.setImageBitmap(thumbnail);
        }
    }

}
