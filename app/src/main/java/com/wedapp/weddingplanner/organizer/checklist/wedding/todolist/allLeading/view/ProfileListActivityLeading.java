package com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.view;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.R;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.adapter.ProfileAdapterLeading;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.appPref.AppPrefLeading;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.baseClass.BaseActivityRecyclerBindingLeading;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.models.profile.ProfileListModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.models.profile.ProfileRowModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.models.toolbar.ToolbarModel;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.roomDatabase.AppDataBase;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.AppConstants;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.allLeading.utils.RecyclerItemClick;
import com.wedapp.weddingplanner.organizer.checklist.wedding.todolist.databinding.ActivityProfileListBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfileListActivityLeading extends BaseActivityRecyclerBindingLeading {
    
    public ActivityProfileListBinding binding;
    


    public AppDataBase db;
    
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
        this.db = AppDataBase.getAppDatabase(this.context);
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.manage_marriages));
//        this.binding.includedToolbar.setModel(this.toolbarModel);

        this.binding.includedToolbar.textTitle.setText(getString(R.string.manage_marriages));
        binding.includedToolbar.imgDelete.setVisibility(View.GONE);
        binding.includedToolbar.imgAdd.setVisibility(View.GONE);
        binding.includedToolbar.imageHome.setVisibility(View.GONE);
        binding.includedToolbar.imgOther.setVisibility(View.GONE);
        binding.includedToolbar.spinner.setVisibility(View.GONE);
        binding.includedToolbar.progressbar.setVisibility(View.GONE);
        binding.includedToolbar.etOther.setVisibility(View.GONE);
        binding.includedToolbar.search.setVisibility(View.GONE);
        binding.includedToolbar.imgShare.setVisibility(View.GONE);
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
            this.model.getArrayList().addAll(this.db.profileDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.model.getArrayList().size() > 0) {
            setDefaultTrip();
        }
    }

    private void setDefaultTrip() {
        for (int i = 0; i < this.model.getArrayList().size(); i++) {
            if (this.model.getArrayList().get(i).getId().equals(AppPrefLeading.getCurrentEvenId(this.context))) {
                this.model.getArrayList().get(i).setSelected(true);
            } else {
                this.model.getArrayList().get(i).setSelected(false);
            }
        }
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new ProfileAdapterLeading(this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    ProfileListActivityLeading.this.setSelectionAll(false);
                    ProfileListActivityLeading.this.db.profileDao().setDeselectAll();
                    ProfileListActivityLeading.this.model.getArrayList().get(i).setSelected(true);
                    AppPrefLeading.setCurrentEvenId(ProfileListActivityLeading.this.context, ProfileListActivityLeading.this.model.getArrayList().get(i).getId());
                    ProfileListActivityLeading.this.db.profileDao().setSelection(ProfileListActivityLeading.this.model.getArrayList().get(i).getId());
                    boolean unused = ProfileListActivityLeading.this.isResultOK = true;
                    return;
                }
                ProfileListActivityLeading.this.openItemDetail(i, ProfileListActivityLeading.this.model.getArrayList().get(i), true);
            }
        }));
        this.binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (i2 > 0 && ProfileListActivityLeading.this.binding.fabAdd.getVisibility() == View.VISIBLE) {
                    ProfileListActivityLeading.this.binding.fabAdd.hide();
                } else if (i2 < 0 && ProfileListActivityLeading.this.binding.fabAdd.getVisibility() != View.VISIBLE) {
                    ProfileListActivityLeading.this.binding.fabAdd.show();
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
        Intent intent = new Intent(this.context, AddEditProfileActivityLeading.class);
        intent.putExtra(AddEditProfileActivityLeading.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditProfileActivityLeading.EXTRA_POSITION, i);
        intent.putExtra(AddEditProfileActivityLeading.EXTRA_MODEL, profileRowModel);
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
                if (intent.hasExtra(AddEditProfileActivityLeading.EXTRA_MODEL)) {
                    ProfileRowModel profileRowModel = (ProfileRowModel) intent.getParcelableExtra(AddEditProfileActivityLeading.EXTRA_MODEL);
                    if (intent.getBooleanExtra(AddEditProfileActivityLeading.EXTRA_IS_DELETED, false)) {
                        boolean isSelected = this.model.getArrayList().get(intent.getIntExtra(AddEditProfileActivityLeading.EXTRA_POSITION, 0)).isSelected();
                        this.model.getArrayList().remove(intent.getIntExtra(AddEditProfileActivityLeading.EXTRA_POSITION, 0));
                        if (isSelected) {
                            setDefaultTripDeleted();
                        }
                    } else if (intent.getBooleanExtra(AddEditProfileActivityLeading.EXTRA_IS_EDIT, false)) {
                        this.model.getArrayList().set(intent.getIntExtra(AddEditProfileActivityLeading.EXTRA_POSITION, 0), profileRowModel);
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
            this.db.profileDao().setDeselectAll();
            this.db.profileDao().setSelection(this.model.getArrayList().get(0).getId());
            AppPrefLeading.setCurrentEvenId(this.context, this.model.getArrayList().get(0).getId());
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
            j = this.db.profileDao().insert(profileRowModel);
        } catch (Exception e) {
            e.printStackTrace();
            j = 0;
        }
        if (j > 0) {
            this.db.profileDao().setDeselectAll();
            AppPrefLeading.setCurrentEvenId(this.context, profileRowModel.getId());
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
