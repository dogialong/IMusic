package com.skay.imusic.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.skay.imusic.R;

/**
 * Created by longdg on 2017-08-27.
 */

public abstract class AbsActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST = 100;
    private boolean hadPermissions;
    private String[] permissions;
    private String permissionDeniedMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        permissions = getPermissionsToRequest();
        hadPermissions = hasPermissions();

        setPermissionDeniedMessage(null);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        if (!hasPermissions()) {
            requestPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final boolean hasPermissions = hasPermissions();
        if (hasPermissions != hadPermissions) {
            hadPermissions = hasPermissions;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                onHasPermissionsChanged(hasPermissions);
            }
        }
    }

    protected View getSnackBarContainer() {
        return getWindow().getDecorView();
    }

    protected void onHasPermissionsChanged(boolean hasPermission) {

    }

    @Nullable
    protected String[] getPermissionsToRequest() {
        return null;
    }

    protected void setPermissionDeniedMessage(String message) {
        permissionDeniedMessage = message;
    }

    private String getPermissionDeniedMessage() {
        return permissionDeniedMessage == null ? getString(R.string.permissions_denied) : permissionDeniedMessage;
    }

    protected void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            requestPermissions(permissions, PERMISSION_REQUEST);
        }
    }

    protected boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(AbsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Snackbar.make(getSnackBarContainer(), getPermissionDeniedMessage(),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.action_grant, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        requestPermission();
                                    }
                                })
                                .show();
                    } else {
                        Snackbar.make(getSnackBarContainer(), getPermissionDeniedMessage(),
                                Snackbar.LENGTH_INDEFINITE)
                                .setAction(R.string.action_settings, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", AbsActivity.this.getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                    return;
                }
                hadPermissions = true;
                onHasPermissionsChanged(true);
            }
        }
    }
}
