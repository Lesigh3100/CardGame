package com.kevin.android.cardgamev1.creationtools.champions;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kevin.android.cardgamev1.R;
import com.kevin.android.cardgamev1.database.ChampionContract;
import com.kevin.android.cardgamev1.tools.Toasts;

public class ChampionCreator extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    EditText championNameEdit;
    EditText championAbilityDescription;
    EditText championDescriptionEdit;
    Button saveButton;
    Button addImageButton;
    ImageView imageView;
    Context mContext;
    Uri mImageUri;
    Uri mCurrentChamionUri;

    private static final int EDIT_LOADER = 19;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 7;
    private static final int CHOOSE_IMAGE_REQUEST = 4;
    private boolean imageSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_creator);
        setUi();
        getChampionStatsOrNew();
        mContext = this;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton();
            }
        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                mCurrentChamionUri,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_NAME));
            String description = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_TEXT));
            String image = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_IMAGE));
            String ability = cursor.getString(cursor.getColumnIndex(ChampionContract.ChampionEntry.COLUMN_CHAMPION_ABILITY));
            Uri imageUri = Uri.parse(image);

            championDescriptionEdit.setText(description);
            championAbilityDescription.setText(ability);
            imageView.setImageURI(imageUri);
            championNameEdit.setText(name);

            mImageUri = imageUri;
            imageSet = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
    }

    private void getChampionStatsOrNew() {
        Intent intent = getIntent();
        mCurrentChamionUri = intent.getData();
        if (mCurrentChamionUri != null) {
            getLoaderManager().initLoader(EDIT_LOADER, null, this);
        }
    }

    private void setUi(){
        championNameEdit = (EditText)findViewById(R.id.champion_creator_name);
        championAbilityDescription = (EditText)findViewById(R.id.champion_creator_edit_text_ability);
        championDescriptionEdit = (EditText)findViewById(R.id.champion_creator_edit_text_description);
        saveButton = (Button)findViewById(R.id.champion_creator_save_button);
        addImageButton = (Button)findViewById(R.id.champion_creator_image_button);
        imageView = (ImageView)findViewById(R.id.champion_creator_image_view);
    }

    private void saveButton() {
        if (championNameEdit.getText().toString().equals("") || mImageUri == null || championAbilityDescription.getText().toString().equals("") || championDescriptionEdit.getText().toString().equals("")){
            Toasts.toastFailure(this);
        }
        else {
            ContentValues values = new ContentValues();
            String championName = championNameEdit.getText().toString();
            String championImage = mImageUri.toString();
            String championAbility = championAbilityDescription.getText().toString();
            String championDescription = championDescriptionEdit.getText().toString();

            values.put(ChampionContract.ChampionEntry.COLUMN_CHAMPION_NAME, championName);
            values.put(ChampionContract.ChampionEntry.COLUMN_CHAMPION_IMAGE, championImage);
            values.put(ChampionContract.ChampionEntry.COLUMN_CHAMPION_ABILITY, championAbility);
            values.put(ChampionContract.ChampionEntry.COLUMN_CHAMPION_TEXT, championDescription);

            if (mCurrentChamionUri == null) {
                Uri uri = null;
                try{
                    uri = getContentResolver().insert(ChampionContract.ChampionEntry.CONTENT_URI, values);
                } catch (IllegalArgumentException e){
                    Toasts.toastFailure(this);
                }
                if (uri != null){
                    Toasts.toastSuccess(this);
                    finish();
                }
            } else {
                getContentResolver().update(mCurrentChamionUri, values, null, null);
                Toasts.toastSuccess(this);
                finish();
            }
        }
    }


    private void openImage() {
        checkWritePermission();
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getData() != null) {
                mImageUri = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
                    mImageUri = data.getData();

                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    mImageUri = data.getData();
                    int takeFlags = data.getFlags();
                    takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    ContentResolver contentResolver = mContext.getContentResolver();
                    contentResolver.takePersistableUriPermission(mImageUri, takeFlags);
                }
                imageView.setImageURI(mImageUri);
                imageView.invalidate();
                imageSet = true;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                }
        }
    }

    private void checkWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
}
