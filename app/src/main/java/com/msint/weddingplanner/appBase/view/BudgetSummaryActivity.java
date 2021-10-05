package com.msint.weddingplanner.appBase.view;

import android.app.Dialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.adapter.CategoryAdapter;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.msint.weddingplanner.appBase.models.cost.CostSummaryModel;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.databinding.ActivityBudgetSummaryBinding;
import com.msint.weddingplanner.databinding.AlertDialogRecyclerListBinding;
import java.util.ArrayList;

public class BudgetSummaryActivity extends BaseActivityBinding {
    private ActivityBudgetSummaryBinding binding;
    
    public ArrayList<CategoryRowModel> categoryList;

    /* renamed from: db */
    private AppDataBase f548db;
    
    public Dialog dialogCategoryList;
    private AlertDialogRecyclerListBinding dialogCategoryListBinding;
    
    public CostSummaryModel model;
    
    public int selectedCategoryPos = 0;
    public ToolbarModel toolbarModel;


    public void setBinding() {
        this.binding = (ActivityBudgetSummaryBinding) DataBindingUtil.setContentView(this, R.layout.activity_budget_summary);
        this.f548db = AppDataBase.getAppDatabase(this);
        setModelDetail();
        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        this.model = new CostSummaryModel();
        this.model.setCategoryRowModel(new CategoryRowModel());
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle("Budget Summary");
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }


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
            this.categoryList.addAll(this.f548db.categoryDao().getAll());
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
                    BudgetSummaryActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int unused = BudgetSummaryActivity.this.selectedCategoryPos = i;
                BudgetSummaryActivity.this.model.setCategoryId(((CategoryRowModel) BudgetSummaryActivity.this.categoryList.get(BudgetSummaryActivity.this.selectedCategoryPos)).getId());
                BudgetSummaryActivity.this.model.setCategoryRowModel((CategoryRowModel) BudgetSummaryActivity.this.categoryList.get(BudgetSummaryActivity.this.selectedCategoryPos));
                BudgetSummaryActivity.this.setTotals();
            }
        }));
        this.dialogCategoryListBinding.imgAdd.setImageResource(R.drawable.close);
        this.dialogCategoryListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    BudgetSummaryActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogCategoryListBinding.linButton.setVisibility(View.GONE);
        this.dialogCategoryListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    BudgetSummaryActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogCategoryListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    BudgetSummaryActivity.this.dialogCategoryList.dismiss();
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
            this.model.setTotalBudget(this.f548db.profileDao().getDetail(AppPref.getCurrentEvenId(this.context)).getBudget());
        } catch (Exception e) {
            try {
                e.printStackTrace();
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        if (this.model.getCategoryId().equalsIgnoreCase(Constants.COST_CAT_TYPE_ALL_CATEGORY)) {
            this.model.setTotalCost(this.f548db.costDao().getAllTotal(AppPref.getCurrentEvenId(this.context)));
            this.model.setCompletedCost(this.f548db.costDao().getTotal(AppPref.getCurrentEvenId(this.context), 0));
            this.model.setPendingCost(this.f548db.costDao().getTotal(AppPref.getCurrentEvenId(this.context), 1));
            this.model.setTotalPayment(this.f548db.paymentDao().getAllCountCostType(AppPref.getCurrentEvenId(this.context), 1));
            this.model.setCompletedPayment(this.f548db.paymentDao().getAllCountPaidCostType(AppPref.getCurrentEvenId(this.context), 1));
            return;
        }
        this.model.setTotalCost(this.f548db.costDao().getAllTotal(AppPref.getCurrentEvenId(this.context), this.model.getCategoryId()));
        this.model.setCompletedCost(this.f548db.costDao().getTotal(AppPref.getCurrentEvenId(this.context), 0, this.model.getCategoryId()));
        this.model.setPendingCost(this.f548db.costDao().getTotal(AppPref.getCurrentEvenId(this.context), 1, this.model.getCategoryId()));
        this.model.setTotalPayment(this.f548db.paymentDao().getAllCountCost(AppPref.getCurrentEvenId(this.context), this.model.getCategoryId(), 1));
        this.model.setCompletedPayment(this.f548db.paymentDao().getCompletedCountCost(AppPref.getCurrentEvenId(this.context), this.model.getCategoryId(), 1));
    }
}
