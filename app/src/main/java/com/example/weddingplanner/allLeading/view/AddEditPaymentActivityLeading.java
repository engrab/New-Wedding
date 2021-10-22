package com.example.weddingplanner.allLeading.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import com.example.weddingplanner.R;
import com.example.weddingplanner.allLeading.baseClass.BaseActivityBindingLeading;
import com.example.weddingplanner.allLeading.models.toolbar.ToolbarModel;
import com.example.weddingplanner.allLeading.roomDatabase.AppDataBase;
import com.example.weddingplanner.allLeading.roomDatabase.payment.PaymentRowModel;
import com.example.weddingplanner.allLeading.utils.AppConstants;
import com.example.weddingplanner.allLeading.utils.TwoButtonDialogListener;
import com.example.weddingplanner.databinding.ActivityPaymentAddEditBinding;

import java.util.Calendar;

public class AddEditPaymentActivityLeading extends BaseActivityBindingLeading {
    public static String EXTRA_ID = "id";
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    private ActivityPaymentAddEditBinding binding;
    


    public AppDataBase db;
    private boolean isEdit = false;
    
    public PaymentRowModel model;
    public ToolbarModel toolbarModel;


    public void setBinding() {
        binding = ActivityPaymentAddEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.db = AppDataBase.getAppDatabase(this);
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
            this.model = (PaymentRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.isEdit ? "Edit Payment" : "Add Payment");
        this.toolbarModel.setDelete(this.isEdit);
        this.toolbarModel.setOtherMenu(true);

        isEdit = getIntent().getBooleanExtra(EXTRA_IS_EDIT, false);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
        binding.includedToolbar.textTitle.setText(this.isEdit ? "Edit Payment" : "Add Payment");
//        this.binding.includedToolbar.setModel(this.toolbarModel);

        if (isEdit){
            binding.includedToolbar.imgShare.setVisibility(View.GONE);
            binding.includedToolbar.imgAdd.setVisibility(View.GONE);
            binding.includedToolbar.imgDrawer.setVisibility(View.GONE);
            binding.includedToolbar.search.setVisibility(View.GONE);
            binding.includedToolbar.progressbar.setVisibility(View.GONE);
            binding.includedToolbar.etOther.setVisibility(View.GONE);
            binding.includedToolbar.spinner.setVisibility(View.GONE);
            binding.includedToolbar.imageHome.setVisibility(View.GONE);
            binding.includedToolbar.imgDelete.setVisibility(View.VISIBLE);
        }else {

            binding.includedToolbar.imgShare.setVisibility(View.GONE);
            binding.includedToolbar.imgAdd.setVisibility(View.GONE);
            binding.includedToolbar.imgDrawer.setVisibility(View.GONE);
            binding.includedToolbar.search.setVisibility(View.GONE);
            binding.includedToolbar.progressbar.setVisibility(View.GONE);
            binding.includedToolbar.etOther.setVisibility(View.GONE);
            binding.includedToolbar.spinner.setVisibility(View.GONE);
            binding.includedToolbar.imageHome.setVisibility(View.GONE);
            binding.includedToolbar.imgDelete.setVisibility(View.GONE);
        }


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
                    db.paymentDao().delete(model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                openItemList(true);
            }
        });
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.txtPending.setOnClickListener(this);
        this.binding.txtPaid.setOnClickListener(this);
        this.binding.txtDate.setOnClickListener(this);
        this.binding.btnAddEdit.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddEdit:
                addUpdate();
                return;
            case R.id.imgBack:
                super.onBackPressed();
                return;
            case R.id.imgDelete:
                deleteItem();
                return;
            case R.id.imgOther:
                addUpdate();
                return;
            case R.id.txtDate:
                showDatePickerDialog();
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

    private void showDatePickerDialog() {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(this.model.getDateInMillis());
        try {
            new DatePickerDialog(this.context, R.style.AppThemeDialogActionBar, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    instance.set(1, i);
                    instance.set(2, i2);
                    instance.set(5, i3);
                    model.setDateInMillis(instance.getTimeInMillis());
                }
            }, instance.get(1), instance.get(2), instance.get(5)).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initMethods() {
        setEditTextValue();
        setEditTextChange();
    }

    private void setEditTextValue() {
        if (this.isEdit) {
            try {
                this.binding.etAmount.setText(AppConstants.getFormattedPrice(this.model.getAmount()));
                binding.etName.setText(model.getName());
                binding.txtDate.setText(model.getDateFormatted());

            } catch (NumberFormatException e) {
                this.binding.etAmount.setText(0);
                e.printStackTrace();
            }
        }
    }

    private void setEditTextChange() {
        this.binding.etAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    binding.txtDate.setText(model.getDateFormatted());
                    model.setAmount(Double.valueOf(charSequence.toString().trim()).doubleValue());
                } catch (NumberFormatException e) {
                    model.setAmount(0.0d);
                    e.printStackTrace();
                }
            }
        });
    }

    private void addUpdate() {
        model.setName(binding.etName.getText().toString().trim());
        model.setAmount(Double.parseDouble(binding.etAmount.getText().toString().trim()));
        if (isValid()) {
            try {
                this.model.getName().trim();
                if (this.isEdit) {
                    this.db.paymentDao().update(this.model);
                } else {
                    this.db.paymentDao().insert(this.model);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            openItemList(false);
        }
    }

    private boolean isValid() {

        if (this.model.getName().trim().isEmpty()) {
            Context context = this.context;
            EditText editText = this.binding.etName;
            AppConstants.requestFocusAndError(context, editText, getString(R.string.please_enter) + " " + getString(R.string.name));
            return false;
        } else if (this.model.getAmount() > 0.0d) {
            return true;
        } else {
            Context context2 = this.context;
            EditText editText2 = this.binding.etAmount;
            AppConstants.requestFocusAndError(context2, editText2, getString(R.string.please_enter) + " " + getString(R.string.amount));
            return false;
        }
    }

    
    public void openItemList(boolean z) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_DELETED, z);
        intent.putExtra(EXTRA_IS_EDIT, getIntent().getBooleanExtra(EXTRA_IS_EDIT, false));
        intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_POSITION, 0));
        intent.putExtra(EXTRA_MODEL, this.model);
        setResult(-1, intent);
        super.onBackPressed();
    }
}
