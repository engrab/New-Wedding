package com.example.weddingplanner.view.imagePicker;

public interface Constants {
    public static final String DEFAULT_FOLDER_NAME = "EasyImage";

    public interface BundleKeys {
        public static final String ALLOW_MULTIPLE = "pl.aprilapps.easyimage.allow_multiple";
        public static final String COPY_PICKED_IMAGES = "pl.aprilapps.easyimage.copy_picked_images";
        public static final String COPY_TAKEN_PHOTOS = "pl.aprilapps.easyimage.copy_taken_photos";
        public static final String FOLDER_NAME = "pl.aprilapps.folder_name";
    }

    public interface RequestCodes {
        public static final int CAPTURE_VIDEO = 17260;
        public static final int EASYIMAGE_IDENTIFICATOR = 876;
        public static final int PICK_PICTURE_FROM_DOCUMENTS = 2924;
        public static final int PICK_PICTURE_FROM_GALLERY = 4972;
        public static final int SOURCE_CHOOSER = 32768;
        public static final int TAKE_PICTURE = 9068;
    }
}
