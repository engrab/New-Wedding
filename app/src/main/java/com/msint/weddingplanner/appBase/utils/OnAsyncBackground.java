package com.msint.weddingplanner.appBase.utils;

public interface OnAsyncBackground {
    void doInBackground();

    void onPostExecute();

    void onPreExecute();
}
