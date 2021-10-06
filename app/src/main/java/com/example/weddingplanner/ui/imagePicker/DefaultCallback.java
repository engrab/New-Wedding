package com.example.weddingplanner.ui.imagePicker;

public abstract class DefaultCallback implements EasyImage.Callbacks {
    @Override // com.selfmentor.myweddingplanner.imagePicker.EasyImage.Callbacks
    public void onCanceled(EasyImage.ImageSource imageSource, int i) {
    }

    @Override // com.selfmentor.myweddingplanner.imagePicker.EasyImage.Callbacks
    public void onImagePickerError(Exception exc, EasyImage.ImageSource imageSource, int i) {
    }
}
