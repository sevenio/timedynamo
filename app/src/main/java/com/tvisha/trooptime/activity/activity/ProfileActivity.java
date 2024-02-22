package com.tvisha.trooptime.activity.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tvisha.trooptime.activity.activity.apiPostModels.ChangePasswordResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.UpdateProfileResponse;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 5;
    public static Context context;
    public static Activity activity;
    Intent CamIntent, GalIntent, CropIntent;
    Uri selectedFileuri;
    File file;

    String AWS_FILE__PATH = "", FILE_NAME = "";
    ImageView backImage, logoutImage, profileImage, cameraImage;
    EditText name_et, mobile_et, email_et, location_et, old_password_et, password_et, confirm_password_et, company_name_et;
    Button save;
    SharedPreferences sharedPreferences;
    String userId, email, name, user_avatar, mobileNumber, token, location, companyName;
    int notification_num, servicecount;
    CustomProgressBar customProgressBar;
    ApiInterface apiService;
    String password = "", oldPassword = "";
    LinearLayout oldPasswordLayout, passwordLayout, confirmPasswordLayout, locationLayout, backLayout, logoutLayout;

    ProgressBar imageLoadingProgressBar;
    private String selectedImagePath = "";

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(ProfileActivity.this).isFinishing()) {
                            if (customProgressBar != null && !customProgressBar.isShowing()) {
                                customProgressBar.show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeProgress() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(ProfileActivity.this).isFinishing()) {
                            if (customProgressBar != null && customProgressBar.isShowing()) {
                                customProgressBar.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        initListeners();



    }

    private void initViews() {

        try {

            context = ProfileActivity.this;
            activity = ProfileActivity.this;
            customProgressBar = new CustomProgressBar(ProfileActivity.this);
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            shareprefernceData();
            apiService = new ApiClient().getInstance();
            setupUI(findViewById(R.id.ll_layout));
            backImage = findViewById(R.id.backImage);
            logoutImage = findViewById(R.id.logoutImage);
            imageLoadingProgressBar = findViewById(R.id.imageLoadingProgressBar);
            profileImage = findViewById(R.id.profileImage);
            cameraImage = findViewById(R.id.cameraImage);
            name_et = findViewById(R.id.name_et);
            mobile_et = findViewById(R.id.mobile_et);
            email_et = findViewById(R.id.email_et);
            location_et = findViewById(R.id.location_et);
            company_name_et = findViewById(R.id.company_name_et);
            old_password_et = findViewById(R.id.old_password_et);
            password_et = findViewById(R.id.password_et);
            confirm_password_et = findViewById(R.id.confirm_password_et);
            save = findViewById(R.id.save);
            oldPasswordLayout = findViewById(R.id.oldPasswordLayout);
            passwordLayout = findViewById(R.id.passwordLayout);
            confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
            locationLayout = findViewById(R.id.locationLayout);
            backLayout = findViewById(R.id.backLayout);
            logoutLayout = findViewById(R.id.logoutLayout);
            updateViews();
            updateProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateProfile() {
        try {
            if (user_avatar != null && !user_avatar.isEmpty()) {
                imageLoadingProgressBar.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions()
                        .error(R.drawable.default_profile)
                        .priority(Priority.HIGH);
                Glide.with(ProfileActivity.this)
                        .load(MyApplication.AWS_BASE_URL + user_avatar)
                        //.transform(new BlurTransformation(this))
                        .apply(options)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                imageLoadingProgressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                imageLoadingProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(profileImage);
            } else {
                Glide.with(ProfileActivity.this).load(R.drawable.default_profile)
                        .into(profileImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateViews() {
        try {
            if(name!=null && !name.trim().isEmpty())
            {
                name_et.setText(name);
            }
            else
            {
                name_et.setText("-");
            }

            if(email!=null && !email.trim().isEmpty())
            {
                email_et.setText(email);
            }
            else
            {
                email_et.setText("-");
            }

            if(mobileNumber!=null && !mobileNumber.trim().isEmpty())
            {
                mobile_et.setText(mobileNumber);
            }
            else
            {
                mobile_et.setText("-");
            }

            if (location != null && !location.trim().isEmpty()) {
                locationLayout.setVisibility(View.VISIBLE);
                location_et.setText(location);
            } else {
                location_et.setText("-");
            }

            if (companyName != null && !companyName.trim().isEmpty()) {
                company_name_et.setText(companyName);
            }
            else
            {
                company_name_et.setText("-");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        try {
            // backImage.setOnClickListener(this);
            backLayout.setOnClickListener(this);
            cameraImage.setOnClickListener(this);
            // logoutImage.setOnClickListener(this);
            logoutLayout.setOnClickListener(this);
            save.setOnClickListener(this);
            oldPasswordLayout.setOnClickListener(this);
            passwordLayout.setOnClickListener(this);
            confirmPasswordLayout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.backImage:
                case R.id.backLayout:
                    onBackPressed();
                    break;
                case R.id.cameraImage:
                    if (checkAndRequestPermissions()) {
                        picImage();
                    }
                    break;
                case R.id.oldPasswordLayout:
                    Utilities.openKeyboard(context, old_password_et);
                    break;
                case R.id.passwordLayout:
                    Utilities.openKeyboard(context, password_et);
                    break;
                case R.id.confirmPasswordLayout:
                    Utilities.openKeyboard(context, confirm_password_et);
                    break;
                case R.id.save:
                    validateForm();
                    break;
                case R.id.logoutImage:
                    logout();
                    break;
                case R.id.logoutLayout:
                    logout();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateForm() {

        try {

            if ((old_password_et.getText().toString() == null || old_password_et.getText().toString().trim().isEmpty())) {
                Toast.makeText(activity, "Please enter old password", Toast.LENGTH_LONG).show();
                Utilities.openKeyboard(activity, old_password_et);
                save.setClickable(true);
            } else if (password_et.getText().toString() == null || password_et.getText().toString().trim().isEmpty()) {
                Toast.makeText(activity, "Please enter password", Toast.LENGTH_LONG).show();
                Utilities.openKeyboard(activity, password_et);
                save.setClickable(true);
            } else if ((password_et.getText().toString() != null || !password_et.getText().toString().trim().isEmpty()) && password_et.getText().toString().length() < 6) {
                Toast.makeText(activity, "Password must contain atleast 6 characters", Toast.LENGTH_LONG).show();
                Utilities.openKeyboard(activity, password_et);
                save.setClickable(true);

            } else if (confirm_password_et.getText().toString() == null || confirm_password_et.getText().toString().trim().isEmpty()) {
                Toast.makeText(activity, "Please enter confirm password", Toast.LENGTH_LONG).show();
                Utilities.openKeyboard(activity, confirm_password_et);
                save.setClickable(true);
            } else if (!confirm_password_et.getText().toString().trim().equals(password_et.getText().toString().trim())) {
                Toast.makeText(activity, "Password and Confirm password must be same", Toast.LENGTH_LONG).show();
                Utilities.openKeyboard(activity, confirm_password_et);
                save.setClickable(true);
            } else {
                Utilities.closeKeyboard(context, activity);
                password = password_et.getText().toString().trim();
                oldPassword = old_password_et.getText().toString().trim();

                if (Utilities.isNetworkAvailable(activity)) {
                    callChangePasswordApi();
                } else {
                    Toast.makeText(activity, "Please check internet connection", Toast.LENGTH_LONG).show();

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareprefernceData() {
        try {
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
            user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
            notification_num = sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT, 0);
            mobileNumber = sharedPreferences.getString(SharePreferenceKeys.USER_MOBILE, "");
            servicecount = sharedPreferences.getInt("countforservice", 0);
            token = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
            location = sharedPreferences.getString(SharePreferenceKeys.COMPANY_LOCATION, "");
            companyName = sharedPreferences.getString(SharePreferenceKeys.COMPANY_NAME, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void callChangePasswordApi() {

        try {

            openProgress();

            Call<ChangePasswordResponse> call = apiService.changePassword(token, userId, mobileNumber, oldPassword, password);
            call.enqueue(new Callback<ChangePasswordResponse>() {
                @Override
                public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                    ChangePasswordResponse apiResponse = response.body();
                    closeProgress();
                    if (apiResponse != null) {

                        save.setClickable(true);
                        if (apiResponse.isSuccess()) {

                            old_password_et.setText("");
                            password_et.setText("");
                            confirm_password_et.setText("");
                            password = "";
                            oldPassword = "";
                            Toast.makeText(activity, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(activity, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                }

                @Override
                public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {

                    save.setClickable(true);
                    closeProgress();

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callUpdateProfileApi() {

        try {

            openProgress();

            Call<UpdateProfileResponse> call = apiService.updateProfile(token, userId, AWS_FILE__PATH, mobileNumber);
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    UpdateProfileResponse apiResponse = response.body();
                    closeProgress();
                    if (apiResponse != null) {

                        save.setClickable(true);
                        if (apiResponse.isSuccess()) {



                            sharedPreferences.edit().putString(SharePreferenceKeys.USER_AVATAR, AWS_FILE__PATH).apply();
                            user_avatar = AWS_FILE__PATH;
                            updateProfile();
                            Toast.makeText(activity, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                        } else {


                            Toast.makeText(activity, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }

                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {

                    save.setClickable(true);
                    closeProgress();

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setupUI(View view) {

        try {


            if (!(view instanceof EditText && view instanceof AutoCompleteTextView)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {


                        if (!(event.getAction() == MotionEvent.ACTION_UP)) {
                            Utilities.closeKeyboard(ProfileActivity.this, ProfileActivity.this);
                        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Utilities.closeKeyboard(ProfileActivity.this, ProfileActivity.this);
                        }


                        return false;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkAndRequestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Helper.checkStoragePermissions(this)) {
                    ActivityCompat.requestPermissions(this,(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ? Constants.STORAGE_PERMISSION_LIST : Constants.STORAGE_PERMISSION_LIST_BLOW_T, Constants.STORAGE_PERMISSION);
                    return false;
                }else /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)*/{
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                }
            }
            /*int permissionCamera = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

            int writeStorage = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);


            List<String> listPermissionsNeeded = new ArrayList<>();

            if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }

            if (writeStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[0]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void picImage() {
        try {

            if (true){
                imagePickerDialog();
                return;
            }

            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});


            //startActivityForResult(chooserIntent, PICK_IMAGE);



            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Apps");
            selectedFileuri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


            PackageManager packageManager = activity.getPackageManager();
            List<Intent> yourIntentsList = new ArrayList<Intent>();
            CamIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            CamIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedFileuri);


            GalIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            GalIntent.setType("image/*");

            // GalIntent.setAction(Intent.ACTION_GET_CONTENT);
            // GalIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedFileuri);

       /* List<ResolveInfo> listCam = packageManager.queryIntentActivities(CamIntent, 0);
        for (ResolveInfo res : listCam) {
            final Intent finalIntent = new Intent(CamIntent);
            finalIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            yourIntentsList.add(finalIntent);
        }*/

       /* List<ResolveInfo> listGall = packageManager.queryIntentActivities(GalIntent, 0);
        for (ResolveInfo res : listGall) {
            final Intent finalIntent = new Intent(GalIntent);
            finalIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            yourIntentsList.add(finalIntent);
        }*/


            yourIntentsList.add(GalIntent);
            Intent chooser = Intent.createChooser(CamIntent, "Choose App");
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, yourIntentsList.toArray(new Parcelable[]{}));
            startActivityForResult(chooser, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void imagePickerDialog() {
        Dialog dialog = new BottomSheetDialog(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView =  layoutInflater.inflate(R.layout.profile_pic_dilaog,null);
        dialog.setContentView(contentView);
        contentView.setBackground(ContextCompat.getDrawable(this,R.drawable.shadow_curved_popup));
        /*dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetBehavior.from(contentView).setPeekHeight(contentView.getHeight());
            }
        });*/
        TextView picFromCamera = dialog.findViewById(R.id.pic_camera);
        TextView picFromGallery = dialog.findViewById(R.id.pic_gallary);
        picFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.TITLE,"Time dynamo picture");
                contentValues.put(MediaStore.Images.Media.DESCRIPTION,"From your camera");
                selectedFileuri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,selectedFileuri);
                startActivityForResult(intent,Constants.CAMERA_ACTIVITY_RESULTS);
                dialog.dismiss();
            }
        });

        picFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),Constants.GALLERY_ACTIVITY_RESULTS);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
            return contentUri.getPath();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switch (requestCode) {
                    case Constants.STORAGE_PERMISSION:
                        if (Helper.checkStoragePermissions(this)) {
                            picImage();
                        }
                        break;

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode){
                    case Constants.CAMERA_ACTIVITY_RESULTS:
                        selectedImagePath = getRealPathFromURI(selectedFileuri);

                        if (selectedImagePath != null) {

                            uploadImageAWS(selectedImagePath);
                        }
                        break;
                    case Constants.GALLERY_ACTIVITY_RESULTS:
                        if (data.getData() != null) {

                            Uri selectedImageUri = data.getData();
                            if (selectedImageUri != null) {
                                selectedImagePath = getRealPathFromURI(data.getData());

                            }

                            profileImage.setImageURI(selectedImageUri);
                            selectedFileuri = selectedImageUri;
                            if (selectedImagePath != null) {
                                uploadImageAWS(selectedImagePath);
                            }

                        }
                        break;
                }
                if (true){
                    return;
                }
                if (requestCode == 0) {


                    if (selectedFileuri != null) {


                        boolean isCamera = false;
                        if (data == null) {
                            isCamera = true;

                        } else {

                            final String action = data.getAction();
                            if (action == null) {

                                isCamera = false;
                            } else {
                                isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                            }
                        }


                        if (isCamera) {


                            selectedImagePath = getRealPathFromURI(selectedFileuri);

                            if (selectedImagePath != null) {

                                uploadImageAWS(selectedImagePath);
                            }


                        } else {


                            if (data.getData() != null) {

                                Uri selectedImageUri = data.getData();
                                if (selectedImageUri != null) {
                                    selectedImagePath = getRealPathFromURI(data.getData());

                                }

                                profileImage.setImageURI(selectedImageUri);
                                selectedFileuri = selectedImageUri;
                                if (selectedImagePath != null) {
                                    uploadImageAWS(selectedImagePath);
                                }


                            }
                        }


                    }

                } else if (requestCode == 1) {


                    if (data != null) {
                        Bitmap photo = (Bitmap) data.getExtras().get("data");
                        profileImage.setImageBitmap(photo);


                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI1(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void uploadImageAWS(String path) {

        try {

            File file = new File(path);
            BasicAWSCredentials credentials = new BasicAWSCredentials(MyApplication.AWS_KEY, MyApplication.AWS_SECRET_KEY);
            AmazonS3Client s3Client = new AmazonS3Client(credentials);

            TransferUtility transferUtility =
                    TransferUtility.builder()
                            .context(context)
                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                            .s3Client(s3Client)
                            .build();

            if (file != null && file.isFile()) {
                String fileName = file.getName();
                String AWS_FILE_KEY = "user_avatar/" + fileName.trim().replace(" ", "");
                FILE_NAME = AWS_FILE_KEY;


                TransferObserver uploadObserver = transferUtility.upload(MyApplication.AWS_BUCKET, AWS_FILE_KEY, file, new ObjectMetadata(),
                        CannedAccessControlList.PublicRead);

                openProgress();

                uploadObserver.setTransferListener(new TransferListener() {

                    @Override
                    public void onStateChanged(int id, TransferState transferState) {
                        try {
                            if (transferState == TransferState.COMPLETED) {

                                AWS_FILE__PATH = FILE_NAME;


                                if (Utilities.isNetworkAvailable(activity)) {


                                    callUpdateProfileApi();
                                } else {
                                    Toast.makeText(activity, "Please check internet connection", Toast.LENGTH_LONG).show();
                                    closeProgress();
                                }

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            closeProgress();

                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                        int percentDone = (int) percentDonef;
                    }

                    @Override
                    public void onError(int id, Exception ex) {

                        closeProgress();
                    }

                });
            } else {
                Toast.makeText(ProfileActivity.this, "no file found", Toast.LENGTH_LONG).show();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {

        try {

            if (MyApplication.homePageResponse != null) {
                MyApplication.homePageResponse = null;
            }
            if (MyApplication.UserRequestListResponse != null) {
                MyApplication.UserRequestListResponse = null;
            }
            if (MyApplication.selfAttendenceApiResponce != null) {
                MyApplication.selfAttendenceApiResponce = null;
            }


            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false).apply();
            sharedPreferences.edit().clear().apply();
            Navigation.getInstance().openLoginPage(ProfileActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Network : upload image on AWS
  /*  public void uploadImageToAws() {
        try {
            if (!Utilities.isNetworkAvailable(context))
                return;



            Random random = new Random();
            int code = (100000 + random.nextInt(900000));
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString() + "" + code;

            final String filename = profileFilePath.substring(profileFilePath.lastIndexOf("/") + 1);
            final String responsePath = "profile" + "/" + ts + filename.trim().replace(" ", "");

            AwsPreferences awsPreferences = AwsPreferences.getInstance(getBaseActivity());

            String sendAwsPath = awsPreferences.getProjectPath() + responsePath;

            TransferUtility transferUtility = awsUtils.getTransferUtility(getBaseActivity(), "");

            List<TransferObserver> observers = transferUtility.getTransfersWithType(TransferType.UPLOAD);

            observers.clear();

            TransferObserver observer = transferUtility.upload(
                    awsPreferences.getBucket(),
                    sendAwsPath,
                    new File(profileFilePath),
                    new ObjectMetadata(),
                    CannedAccessControlList.PublicRead);

            observers.add(observer);

            displayDialog();

            observer.setTransferListener(new TransferListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onStateChanged(int id, TransferState state) {

                    try {
                        if (state == TransferState.COMPLETED) {

                            profilePic = responsePath;
                            GlideApp.with(getBaseActivity())
                                    .applyDefaultRequestOptions(new RequestOptions())
                                    .load(AwsPreferences.getInstance(getBaseActivity()).getImageUrl() + responsePath)
                                    .into(ivProfilePick);

                            uploadedImages++;
                            isNewImage = false;
                            updateList(true);

                        } else if (state != TransferState.IN_PROGRESS) {
                            if (state == TransferState.FAILED
                                    || state == TransferState.CANCELED) {
                                updateList(false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        updateList(false);
                    }

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    updateList(true);
                }

                @Override
                public void onError(int id, Exception ex) {
                    ex.printStackTrace();
                    updateList(false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


}
