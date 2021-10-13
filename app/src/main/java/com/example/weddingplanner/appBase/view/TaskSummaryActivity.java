package com.example.weddingplanner.appBase.view;

import android.app.Dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.adapter.CategoryAdapter;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.example.weddingplanner.appBase.models.task.TaskSummaryModel;
import com.example.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.example.weddingplanner.appBase.utils.Constants;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.databinding.ActivityTaskSummaryBinding;
import com.example.weddingplanner.databinding.AlertDialogRecyclerListBinding;

import java.util.ArrayList;

public class TaskSummaryActivity extends BaseActivityBinding {
    private ActivityTaskSummaryBinding binding;

    public ArrayList<CategoryRowModel> categoryList;


    private AppDataBase db;

    public Dialog dialogCategoryList;
    private AlertDialogRecyclerListBinding dialogCategoryListBinding;

    public TaskSummaryModel model;

    public int selectedCategoryPos = 0;
    public ToolbarModel toolbarModel;


    public void setBinding() {
        binding = ActivityTaskSummaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = AppDataBase.getAppDatabase(this);
        setModelDetail();
//        binding.setRowModel(model);
    }

    private void setModelDetail() {
        model = new TaskSummaryModel();
        model.setCategoryRowModel(new CategoryRowModel());
    }


    public void setToolbar() {
        toolbarModel = new ToolbarModel();
        toolbarModel.setTitle("Task Summary");
//        binding.includedToolbar.setModel(toolbarModel);

        binding.includedToolbar.textTitle.setText("Task Summary");
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
        binding.includedToolbar.imgBack.setOnClickListener(this);
        binding.linCategory.setOnClickListener(this);
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
        super.onBackPressed();
    }


    public void initMethods() {
        categoryDialogSetup();
        setTotals();
    }

    private void categoryDialogSetup() {
        fillCategoryList();
        setCategoryListDialog();
    }

    private void fillCategoryList() {
        categoryList = new ArrayList<>();
        CategoryRowModel categoryRowModel = new CategoryRowModel(Constants.COST_CAT_TYPE_ALL_CATEGORY, Constants.COST_CAT_TYPE_ALL_CATEGORY, Constants.COST_CAT_TYPE_ALL_CATEGORY);
        model.setCategoryRowModel(categoryRowModel);
        model.setCategoryId(categoryRowModel.getId());
        categoryList.add(categoryRowModel);
        try {
            categoryList.addAll(db.categoryDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        categoryList.get(selectedCategoryPos).setSelected(true);
        if (model.getCategoryRowModel() == null) {
            model.setCategoryId(categoryList.get(selectedCategoryPos).getId());
            model.setCategoryRowModel(categoryList.get(selectedCategoryPos));
        }
    }

    public void setCategoryListDialog() {
        dialogCategoryListBinding = AlertDialogRecyclerListBinding.inflate(LayoutInflater.from(context), (ViewGroup) null, false);
        dialogCategoryList = new Dialog(context);
        dialogCategoryList.setContentView(dialogCategoryListBinding.getRoot());
        dialogCategoryList.setCancelable(false);
        dialogCategoryList.getWindow().setBackgroundDrawableResource(17170445);
        dialogCategoryList.getWindow().setLayout(-1, -2);
        dialogCategoryListBinding.txtTitle.setText(R.string.select_category);
        dialogCategoryListBinding.btnOk.setText(R.string.set);
        dialogCategoryListBinding.recycler.setLayoutManager(new LinearLayoutManager(context));
        dialogCategoryListBinding.recycler.setAdapter(new CategoryAdapter(context, false, categoryList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                try {
                    dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                selectedCategoryPos = i;
                model.setCategoryId(((CategoryRowModel) categoryList.get(selectedCategoryPos)).getId());
                model.setCategoryRowModel((CategoryRowModel) categoryList.get(selectedCategoryPos));
                setTotals();
            }
        }));
        dialogCategoryListBinding.imgAdd.setImageResource(R.drawable.close);
        dialogCategoryListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialogCategoryListBinding.linButton.setVisibility(View.GONE);
        dialogCategoryListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialogCategoryListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDialogCategoryList() {
        try {
            dialogCategoryListBinding.recycler.scrollToPosition(selectedCategoryPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (dialogCategoryList != null && !dialogCategoryList.isShowing()) {
                dialogCategoryList.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public void setTotals() {
        try {
            if (model.getCategoryId().equalsIgnoreCase(Constants.COST_CAT_TYPE_ALL_CATEGORY)) {
                model.setTotalTask(db.taskDao().getAllCount(AppPref.getCurrentEvenId(context)));
                model.setCompletedTask(db.taskDao().getAllCount(AppPref.getCurrentEvenId(context), 0));
                model.setPendingTask(db.taskDao().getAllCount(AppPref.getCurrentEvenId(context), 1));
                model.setTotalSubTask(db.subTaskDao().getAllCount(AppPref.getCurrentEvenId(context)));
                model.setCompletedSubTask(db.subTaskDao().getCompletedCount(AppPref.getCurrentEvenId(context)));
                return;
            }
            model.setTotalTask(db.taskDao().getAllCount(AppPref.getCurrentEvenId(context), model.getCategoryId()));
            model.setCompletedTask(db.taskDao().getAllCount(AppPref.getCurrentEvenId(context), model.getCategoryId(), 0));
            model.setPendingTask(db.taskDao().getAllCount(AppPref.getCurrentEvenId(context), model.getCategoryId(), 1));
            model.setTotalSubTask(db.subTaskDao().getAllCount(AppPref.getCurrentEvenId(context), model.getCategoryId()));
            model.setCompletedSubTask(db.subTaskDao().getCompletedCount(AppPref.getCurrentEvenId(context), model.getCategoryId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.imgIcon.setImageResource(model.getCategoryRowModel().getImgResId());
        binding.tvName.setText(model.getCategoryRowModel().getName());

        // tasks
        binding.tvTotalTask.setText(model.getTotalTask()+"");
        binding.tvTask.setText(model.getPendingTask()+" / "+model.getCompletedTask());
        binding.total.setText(model.getCompletedTask()+"");
        binding.total.setText(model.getTotalTask()+"");
        binding.completed.setText(model.getCompletedTask()+"");
        binding.pending.setText(model.getPendingTask()+"");

        // subtasks
        binding.tvSubTask.setText(model.getCompletedSubTask()+" / "+model.getTotalSubTask());
        binding.subTotal.setText(model.getTotalSubTask()+"");
        binding.subCompleted.setText(model.getCompletedSubTask()+"");
        binding.subPending.setText(model.getPendingSubTask()+"");


    }
}
