package com.example.weddingplanner.view.imagePicker;

import android.content.Context;
import android.preference.PreferenceManager;

public class EasyImageConfiguration implements Constants {
    private Context context;

    EasyImageConfiguration(Context context2) {
        this.context = context2;
    }

    public EasyImageConfiguration setImagesFolderName(String str) {
        PreferenceManager.getDefaultSharedPreferences(this.context).edit().putString(BundleKeys.FOLDER_NAME, str).commit();
        return this;
    }

    public EasyImageConfiguration setAllowMultiplePickInGallery(boolean z) {
        PreferenceManager.getDefaultSharedPreferences(this.context).edit().putBoolean(BundleKeys.ALLOW_MULTIPLE, z).commit();
        return this;
    }

    public EasyImageConfiguration setCopyTakenPhotosToPublicGalleryAppFolder(boolean z) {
        PreferenceManager.getDefaultSharedPreferences(this.context).edit().putBoolean(BundleKeys.COPY_TAKEN_PHOTOS, z).commit();
        return this;
    }

    public EasyImageConfiguration setCopyPickedImagesToPublicGalleryAppFolder(boolean z) {
        PreferenceManager.getDefaultSharedPreferences(this.context).edit().putBoolean(BundleKeys.COPY_PICKED_IMAGES, z).commit();
        return this;
    }

    public String getFolderName() {
        return PreferenceManager.getDefaultSharedPreferences(this.context).getString(BundleKeys.FOLDER_NAME, Constants.DEFAULT_FOLDER_NAME);
    }

    public boolean allowsMultiplePickingInGallery() {
        return PreferenceManager.getDefaultSharedPreferences(this.context).getBoolean(BundleKeys.ALLOW_MULTIPLE, true);
    }

    public boolean shouldCopyTakenPhotosToPublicGalleryAppFolder() {
        return PreferenceManager.getDefaultSharedPreferences(this.context).getBoolean(BundleKeys.COPY_TAKEN_PHOTOS, false);
    }

    public boolean shouldCopyPickedImagesToPublicGalleryAppFolder() {
        return PreferenceManager.getDefaultSharedPreferences(this.context).getBoolean(BundleKeys.COPY_PICKED_IMAGES, false);
    }
}
