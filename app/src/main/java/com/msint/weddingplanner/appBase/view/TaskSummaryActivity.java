package com.msint.weddingplanner.appBase.view;

import android.app.Dialog;

import android.support.p004v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.adapter.CategoryAdapter;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.msint.weddingplanner.appBase.models.task.TaskSummaryModel;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.databinding.ActivityTaskSummaryBinding;
import com.msint.weddingplanner.databinding.AlertDialogRecyclerListBinding;
import java.util.ArrayList;

public class TaskSummaryActivity extends BaseActivityBinding {
    private ActivityTaskSummaryBinding binding;

    public ArrayList<CategoryRowModel> categoryList;

    /* renamed from: db */
    private AppDataBase f557db;

    public Dialog dialogCategoryList;
    private AlertDialogRecyclerListBinding dialogCategoryListBinding;

    public TaskSummaryModel model;

    public int selectedCategoryPos = 0;
    public ToolbarModel toolbarModel;

    /* access modifiers changed from: protected */
    public void setBinding() {
        this.binding = (ActivityTaskSummaryBinding) DataBindingUtil.setContentView(this, R.layout.activity_task_summary);
        this.f557db = AppDataBase.getAppDatabase(this);
        setModelDetail();
        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        this.model = new TaskSummaryModel();
        this.model.setCategoryRowModel(new CategoryRowModel());
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle("Task Summary");
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.linCategory.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgBack) {
            onBackPressed();
        } else if (id == R.id.linCategory) {
            showDialogCategoryList();
        }
    }

    public void onBackPressed() {
        MainActivityDashboard.BackPressedAd(this);
    }

    /* access modifiers changed from: protected */
    public void initMethods() {
        categoryDialogSetup();
        setTotals();
    }

    private void categoryDialogSetup() {
        fillCategoryList();
        setCategoryListDialog();
    }

    private void fillCategoryList() {
        this.categoryList = new ArrayList<>();
        CategoryRowModel categoryRowModel = new CategoryRowModel(Constants.COST_CAT_TYPE_ALL_CATEGORY, Constants.COST_CAT_TYPE_ALL_CATEGORY, Constants.COST_CAT_TYPE_ALL_CATEGORY);
        this.model.setCategoryRowModel(categoryRowModel);
        this.model.setCategoryId(categoryRowModel.getId());
        this.categoryList.add(categoryRowModel);
        try {
            this.categoryList.addAll(this.f557db.categoryDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.categoryList.get(this.selectedCategoryPos).setSelected(true);
        if (this.model.getCategoryRowModel() == null) {
            this.model.setCategoryId(this.categoryList.get(this.selectedCategoryPos).getId());
            this.model.setCategoryRowModel(this.categoryList.get(this.selectedCategoryPos));
        }
    }

    public void setCategoryListDialog() {
        this.dialogCategoryListBinding = (AlertDialogRecyclerListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_recycler_list, (ViewGroup) null, false);
        this.dialogCategoryList = new Dialog(this.context);
        this.dialogCategoryList.setContentView(this.dialogCategoryListBinding.getRoot());
        this.dialogCategoryList.setCancelable(false);
        this.dialogCategoryList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogCategoryList.getWindow().setLayout(-1, -2);
        this.dialogCategoryListBinding.txtTitle.setText(R.string.select_category);
        this.dialogCategoryListBinding.btnOk.setText(R.string.set);
        this.dialogCategoryListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogCategoryListBinding.recycler.setAdapter(new CategoryAdapter(this.context, false, this.categoryList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                try {
                    TaskSummaryActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int unused = TaskSummaryActivity.this.selectedCategoryPos = i;
                TaskSummaryActivity.this.model.setCategoryId(((CategoryRowModel) TaskSummaryActivity.this.categoryList.get(TaskSummaryActivity.this.selectedCategoryPos)).getId());
                TaskSummaryActivity.this.model.setCategoryRowModel((CategoryRowModel) TaskSummaryActivity.this.categoryList.get(TaskSummaryActivity.this.selectedCategoryPos));
                TaskSummaryActivity.this.setTotals();
            }
        }));
        this.dialogCategoryListBinding.imgAdd.setImageResource(R.drawable.close);
        this.dialogCategoryListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    TaskSummaryActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogCategoryListBinding.linButton.setVisibility(View.GONE);
        this.dialogCategoryListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    TaskSummaryActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogCategoryListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    TaskSummaryActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDialogCategoryList() {
        try {
            this.dialogCategoryListBinding.recycler.scrollToPosition(this.selectedCategoryPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.dialogCategoryList != null && !this.dialogCategoryList.isShowing()) {
                this.dialogCategoryList.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public void setTotals() {
        try {
            if (this.model.getCategoryId().equalsIgnoreCase(Constants.COST_CAT_TYPE_ALL_CATEGORY)) {
                this.model.setTotalTask(this.f557db.taskDao().getAllCount(AppPref.getCurrentEvenId(this.context)));
                this.model.setCompletedTask(this.f557db.taskDao().getAllCount(AppPref.getCurrentEvenId(this.context), 0));
                this.model.setPendingTask(this.f557db.taskDao().getAllCount(AppPref.getCurrentEvenId(this.context), 1));
                this.model.setTotalSubTask(this.f557db.subTaskDao().getAllCount(AppPref.getCurrentEvenId(this.context)));
                this.model.setCompletedSubTask(this.f557db.subTaskDao().getCompletedCount(AppPref.getCurrentEvenId(this.context)));
                return;
            }
            this.model.setTotalTask(this.f557db.taskDao().getAllCount(AppPref.getCurrentEvenId(this.context), this.model.getCategoryId()));
            this.model.setCompletedTask(this.f557db.taskDao().getAllCount(AppPref.getCurrentEvenId(this.context), this.model.getCategoryId(), 0));
            this.model.setPendingTask(this.f557db.taskDao().getAllCount(AppPref.getCurrentEvenId(this.context), this.model.getCategoryId(), 1));
            this.model.setTotalSubTask(this.f557db.subTaskDao().getAllCount(AppPref.getCurrentEvenId(this.context), this.model.getCategoryId()));
            this.model.setCompletedSubTask(this.f557db.subTaskDao().getCompletedCount(AppPref.getCurrentEvenId(this.context), this.model.getCategoryId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
