package com.msint.weddingplanner.appBase.view;

import android.content.Context;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityBinding;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.taskList.SubTaskRowModel;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.msint.weddingplanner.databinding.ActivitySubTaskAddEditBinding;
import com.msint.weddingplanner.databinding.ActivityTaskSummaryBinding;

public class AddEditSubTaskActivity extends BaseActivityBinding {
    public static String EXTRA_ID = "id";
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    private ActivitySubTaskAddEditBinding binding;


    /* renamed from: db */
    public AppDataBase f545db;
    private boolean isEdit = false;

    public SubTaskRowModel model;
    public ToolbarModel toolbarModel;


    public void initMethods() {
    }


    public void setBinding() {
        binding = ActivitySubTaskAddEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        this.binding = (ActivitySubTaskAddEditBinding) DataBindingUtil.setContentView(this, R.layout.activity_sub_task_add_edit);
        this.f545db = AppDataBase.getAppDatabase(this);
        setModelDetail();
//        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        boolean z = false;
        if (getIntent().hasExtra(EXTRA_IS_EDIT) && getIntent().getBooleanExtra(EXTRA_IS_EDIT, false)) {
            z = true;
        }
        this.isEdit = z;
        try {
            this.model = (SubTaskRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.isEdit ? "Edit Subtask" : "Add Subtask");
        this.toolbarModel.setDelete(this.isEdit);
        this.toolbarModel.setOtherMenu(true);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
//        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        menu.findItem(R.id.delete).setVisible(this.isEdit);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != R.id.delete) {
            return super.onOptionsItemSelected(menuItem);
        }
        deleteItem();
        return true;
    }

    public void deleteItem() {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>" + this.model.getName() + "</b>", true, true, getString(R.string.delete), getString(R.string.cancel), new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                try {
                    AddEditSubTaskActivity.this.f545db.subTaskDao().delete(AddEditSubTaskActivity.this.model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AddEditSubTaskActivity.this.openItemList(true);
            }
        });
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.txtPending.setOnClickListener(this);
        this.binding.txtPaid.setOnClickListener(this);
        this.binding.btnAddEdit.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddEdit:
                addUpdate();
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.imgDelete:
                deleteItem();
                return;
            case R.id.imgOther:
                addUpdate();
                return;
            case R.id.txtPaid:
                this.model.setPending(false);
                return;
            case R.id.txtPending:
                this.model.setPending(true);
                return;
            default:
                return;
        }
    }

    private void addUpdate() {
        if (isValid()) {
            try {
                this.model.getName().trim();
                if (this.isEdit) {
                    this.f545db.subTaskDao().update(this.model);
                } else {
                    this.f545db.subTaskDao().insert(this.model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            openItemList(false);
        }
    }

    private boolean isValid() {
        if (!this.model.getName().trim().isEmpty()) {
            return true;
        }
        Context context = this.context;
        EditText editText = this.binding.etName;
        AppConstants.requestFocusAndError(context, editText, getString(R.string.please_enter) + " " + getString(R.string.name));
        return false;
    }


    public void openItemList(boolean z) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_DELETED, z);
        intent.putExtra(EXTRA_IS_EDIT, getIntent().getBooleanExtra(EXTRA_IS_EDIT, false));
        intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_POSITION, 0));
        intent.putExtra(EXTRA_MODEL, this.model);
        setResult(-1, intent);
        finish();
    }
}
