package com.msint.weddingplanner.appBase.baseClass;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.msint.weddingplanner.appBase.utils.OnFragmentInteractionListener;

public abstract class BaseFragmentBinding extends Fragment implements View.OnClickListener {
    public Context context;
    private OnFragmentInteractionListener mListener;

    /* access modifiers changed from: protected */
    public abstract View getViewBinding();

    /* access modifiers changed from: protected */
    public abstract void initMethods();

    /* access modifiers changed from: protected */
    public abstract void setBinding(LayoutInflater layoutInflater, ViewGroup viewGroup);

    /* access modifiers changed from: protected */
    public abstract void setOnClicks();

    /* access modifiers changed from: protected */
    public abstract void setToolbar();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.context = getActivity();
        setBinding(layoutInflater, viewGroup);
        setToolbar();
        setOnClicks();
        initMethods();
        return getViewBinding();
    }

    public void onButtonPressed(Uri uri) {
        if (this.mListener != null) {
            this.mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context2) {
        super.onAttach(context2);
        if (context2 instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context2;
            return;
        }
        throw new RuntimeException(context2.toString() + " must implement OnFragmentInteractionListener");
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }
}
