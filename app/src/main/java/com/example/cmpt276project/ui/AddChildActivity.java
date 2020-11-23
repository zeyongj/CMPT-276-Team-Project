package com.example.cmpt276project.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Children;
import com.example.cmpt276project.model.ChildrenAdapter;

import java.util.Objects;

public class AddChildActivity extends AppCompatActivity {

    private Children children;
    private ChildrenAdapter childrenAdapter;
    private EditText addChildName;




    // Handling profile Image
    private ImageView profileImage;

    private BitmapDrawable drawableProfile;
    private Bitmap bitmapStored;

    private boolean isProfileSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        // enable 'up' button
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);


        children= Children.getInstance();
        childrenAdapter = ChildrenAdapter.getInstance();
        addChildName = findViewById(R.id.txt_enterChildName);

        profileImage = findViewById(R.id.profileImage);

        // set default image
        profileImage.setImageResource(R.drawable.default_user_profile);



        registerClickedSaveChild();
        registerClickedCancel();

        registerClickedChangeProfile();



    }

    private void registerClickedChangeProfile() {
        Button btn = findViewById(R.id.btn_changeProfileImage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProfileSet = true;
                // open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                profileImage.setImageURI(imageUri);
                drawableProfile = (BitmapDrawable) profileImage.getDrawable();
                bitmapStored = drawableProfile.getBitmap();

            }
        }
    }

    // TODO: fix bug when saving child with no names


    private void registerClickedSaveChild() {
        Button btn = findViewById(R.id.btn_saveChild);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChild(children);


            if (isProfileSet) {
                addChildProfile(bitmapStored);

//                Toast.makeText(AddChildActivity.this,
//                        "The ID of image saved is: " + children.getChildProfile(0),
//                        Toast.LENGTH_SHORT).show();
                }

            // default case for not setting profile image
            else {
                profileImage.setImageResource(R.drawable.default_user_profile);
                drawableProfile = (BitmapDrawable) profileImage.getDrawable();
                bitmapStored = drawableProfile.getBitmap();
                addChildProfile(bitmapStored);
            }

            }
        });
    }

    private void registerClickedCancel() {
        Button btn = findViewById(R.id.btn_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void addChild(Children children) {
        children.addChild(addChildName.getText().toString());
        childrenAdapter.notifyItemInserted(children.getSize()-1);
    }

    public void addChildProfile(Bitmap profileID) {
        children.addChildProfile(profileID);
        childrenAdapter.notifyItemInserted(children.getSize()-1);
    }


}