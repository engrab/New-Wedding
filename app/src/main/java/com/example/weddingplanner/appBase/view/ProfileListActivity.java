package com.example.weddingplanner.appBase.view;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.adapter.ProfileAdapter;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.example.weddingplanner.appBase.models.profile.ProfileListModel;
import com.example.weddingplanner.appBase.models.profile.ProfileRowModel;
import com.example.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.ActivityProfileListBinding;
import com.example.weddingplanner.databinding.ActivityTaskSummaryBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfileListActivity extends BaseActivityRecyclerBinding {
    
    public ActivityProfileListBinding binding;
    


    public AppDataBase f554db;
    
    public boolean isResultOK;
    
    public ProfileListModel model;
    private ToolbarModel toolbarModel;


    public void callApi() {
    }


    public void initMethods() {
    }


    public void setBinding() {
        binding = ActivityProfileListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.model = new ProfileListModel();
        this.model.setArrayList(new ArrayList());
        this.model.setNoDataIcon(R.drawable.dummy_empty);
        this.model.setNoDataText(getString(R.string.noDataTitleProfile));
        this.model.setNoDataDetail(getString(R.string.noDataDescProfile));
//        this.binding.setModel(this.model);
        this.f554db = AppDataBase.getAppDatabase(this.context);
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.manage_marriages));
//        this.binding.includedToolbar.setModel(this.toolbarModel);
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.fabAdd.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fabAdd) {
            openItemDetail(-1, new ProfileRowModel(), false);
        } else if (id == R.id.imgBack) {
            onBackPressed();
        }
    }


    public void fillData() {
        try {
            this.model.getArrayList().addAll(this.f554db.profileDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.model.getArrayList().size() > 0) {
            setDefaultTrip();
        }
    }

    private void setDefaultTrip() {
        for (int i = 0; i < this.model.getArrayList().size(); i++) {
            if (this.model.getArrayList().get(i).getId().equals(AppPref.getCurrentEvenId(this.context))) {
                this.model.getArrayList().get(i).setSelected(true);
            } else {
                this.model.getArrayList().get(i).setSelected(false);
            }
        }
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new ProfileAdapter(this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    ProfileListActivity.this.setSelectionAll(false);
                    ProfileListActivity.this.f554db.profileDao().setDeselectAll();
                    ProfileListActivity.this.model.getArrayList().get(i).setSelected(true);
                    AppPref.setCurrentEvenId(ProfileListActivity.this.context, ProfileListActivity.this.model.getArrayList().get(i).getId());
                    ProfileListActivity.this.f554db.profileDao().setSelection(ProfileListActivity.this.model.getArrayList().get(i).getId());
                    boolean unused = ProfileListActivity.this.isResultOK = true;
                    return;
                }
                ProfileListActivity.this.openItemDetail(i, ProfileListActivity.this.model.getArrayList().get(i), true);
            }
        }));
        this.binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (i2 > 0 && ProfileListActivity.this.binding.fabAdd.getVisibility() == View.VISIBLE) {
                    ProfileListActivity.this.binding.fabAdd.hide();
                } else if (i2 < 0 && ProfileListActivity.this.binding.fabAdd.getVisibility() != View.VISIBLE) {
                    ProfileListActivity.this.binding.fabAdd.show();
                }
            }
        });
    }

    
    public void setSelectionAll(boolean z) {
        for (int i = 0; i < this.model.getArrayList().size(); i++) {
            this.model.getArrayList().get(i).setSelected(z);
        }
    }

    
    public void openItemDetail(int i, ProfileRowModel profileRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditProfileActivity.class);
        intent.putExtra(AddEditProfileActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditProfileActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditProfileActivity.EXTRA_MODEL, profileRowModel);
        startActivityForResult(intent, 1002);
    }

    private void notifyAdapter() {
        setViewVisibility();
        if (this.binding.recycler.getAdapter() != null) {
            this.binding.recycler.getAdapter().notifyDataSetChanged();
        }
    }

    private void setViewVisibility() {
        int i = 8;
        this.binding.linData.setVisibility(this.model.isListData() ? View.VISIBLE : View.GONE);
        LinearLayout linearLayout = this.binding.linNoData;
        if (!this.model.isListData()) {
            i = 0;
        }
        linearLayout.setVisibility(i);
    }


    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1002) {
            this.isResultOK = true;
            updateList(intent);
        }
    }

    private void updateList(Intent intent) {
        if (intent != null) {
            try {
                if (intent.hasExtra(AddEditProfileActivity.EXTRA_MODEL)) {
                    ProfileRowModel profileRowModel = (ProfileRowModel) intent.getParcelableExtra(AddEditProfileActivity.EXTRA_MODEL);
                    if (intent.getBooleanExtra(AddEditProfileActivity.EXTRA_IS_DELETED, false)) {
                        boolean isSelected = this.model.getArrayList().get(intent.getIntExtra(AddEditProfileActivity.EXTRA_POSITION, 0)).isSelected();
                        this.model.getArrayList().remove(intent.getIntExtra(AddEditProfileActivity.EXTRA_POSITION, 0));
                        if (isSelected) {
                            setDefaultTripDeleted();
                        }
                    } else if (intent.getBooleanExtra(AddEditProfileActivity.EXTRA_IS_EDIT, false)) {
                        this.model.getArrayList().set(intent.getIntExtra(AddEditProfileActivity.EXTRA_POSITION, 0), profileRowModel);
                    } else {
                        this.model.getArrayList().add(profileRowModel);
                        if (this.model.getArrayList().size() > 0) {
                            setDefaultTrip();
                        }
                    }
                    notifyAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setDefaultTripDeleted() {
        if (this.model.getArrayList().size() > 0) {
            this.f554db.profileDao().setDeselectAll();
            this.f554db.profileDao().setSelection(this.model.getArrayList().get(0).getId());
            AppPref.setCurrentEvenId(this.context, this.model.getArrayList().get(0).getId());
            setDefaultTrip();
            return;
        }
        insertDefaultData();
    }

    private void insertDefaultData() {
        long j;
        ProfileRowModel profileRowModel = new ProfileRowModel();
        Calendar instance = Calendar.getInstance();
        instance.add(2, 1);
        instance.set(13, 0);
        instance.set(14, 0);
        profileRowModel.setId(AppConstants.getUniqueId());
        profileRowModel.setDateTimeInMillis(instance.getTimeInMillis());
        profileRowModel.setSelected(true);
        setWeddingName(profileRowModel);
        try {
            j = this.f554db.profileDao().insert(profileRowModel);
        } catch (Exception e) {
            e.printStackTrace();
            j = 0;
        }
        if (j > 0) {
            this.f554db.profileDao().setDeselectAll();
            AppPref.setCurrentEvenId(this.context, profileRowModel.getId());
            this.model.getArrayList().add(profileRowModel);
            setDefaultTrip();
            notifyAdapter();
            AppConstants.toastShort(this.context, "Default Data added");
        }
    }

    private void setWeddingName(ProfileRowModel profileRowModel) {
        String str = "";
        if (profileRowModel.getName().trim().length() > 0 && profileRowModel.getPartnerName().trim().length() > 0) {
            if (profileRowModel.getGenderType() == 1) {
                str = (str + profileRowModel.getName()) + " & " + profileRowModel.getPartnerName();
            } else {
                str = (str + profileRowModel.getPartnerName()) + " & " + profileRowModel.getName();
            }
        }
        profileRowModel.setWeddingName(str);
    }

    public void onBackPressed() {
        if (this.isResultOK) {
            setResult(-1);
        }
        finish();
    }
}
