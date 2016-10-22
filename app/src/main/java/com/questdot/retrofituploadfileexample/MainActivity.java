package com.questdot.retrofituploadfileexample;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
                import android.provider.MediaStore;
                import android.support.design.widget.Snackbar;
                import android.support.v7.app.AppCompatActivity;
                import android.support.v7.widget.Toolbar;
                import android.view.View;
                import android.widget.Button;
                import android.widget.ImageView;
                import android.widget.Toast;

                import com.squareup.picasso.Picasso;

                import java.io.File;

                import okhttp3.MediaType;
                import okhttp3.MultipartBody;
                import okhttp3.RequestBody;
                import retrofit2.Call;
                import retrofit2.Callback;
                import retrofit2.Response;

                public class MainActivity extends AppCompatActivity {


                    ImageView imageView;
                    String imagePath;
                    Toolbar toolbar;

                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.activity_main);


                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);


                        imageView = (ImageView) findViewById(R.id.imageView);

                        Button button = (Button) findViewById(R.id.fab);


                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(imagePath!=null)
                                    uploadImage();
                                else
                                    Toast.makeText(getApplicationContext(),"Please select image", Toast.LENGTH_LONG).show();

                            }
                        });
                    }


                    private void uploadImage() {

                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage("loading...");
                        progressDialog.show();

                        FileApi service = RetroClient.getApiService();

                        File file = new File(imagePath);

                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

                        Call<Respond> resultCall = service.uploadImage(body);

                        resultCall.enqueue(new Callback<Respond>() {
                            @Override
                            public void onResponse(Call<Respond> call, Response<Respond> response) {

                                progressDialog.dismiss();

                                // Response Success or Fail
                                if (response.isSuccessful()) {
                                    if (response.body().getError()==true)

                                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                                    else
                                        Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                                }

                                imageView.setImageDrawable(null);
                                imagePath = null;

            }

            @Override
            public void onFailure(Call<Respond> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    public void showImagePopup(View view) {
            final Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_PICK);

            final Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose image");
            startActivityForResult(chooserIntent, 100);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

                Picasso.with(getApplicationContext()).load(new File(imagePath))
                        .into(imageView);

                Toast.makeText(getApplicationContext(),"Please reselect your image",Toast.LENGTH_LONG).show();
                cursor.close();

            } else {

                Toast.makeText(getApplicationContext(),"Unable to load image",Toast.LENGTH_LONG).show();
            }
        }
    }




}
