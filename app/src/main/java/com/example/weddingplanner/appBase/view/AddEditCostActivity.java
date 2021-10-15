package com.example.weddingplanner.appBase.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.adapter.CategoryAdapter;
import com.example.weddingplanner.appBase.adapter.ImageAdapter;
import com.example.weddingplanner.appBase.adapter.PaymentAdapter;
import com.example.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.example.weddingplanner.appBase.models.image.ImageRowModel;
import com.example.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.example.weddingplanner.appBase.roomsDB.cost.CostRowModel;
import com.example.weddingplanner.appBase.roomsDB.payment.PaymentRowModel;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.appBase.utils.BackgroundAsync;
import com.example.weddingplanner.appBase.utils.Constants;
import com.example.weddingplanner.appBase.utils.OnAsyncBackground;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.example.weddingplanner.databinding.ActivityCostAddEditBinding;
import com.example.weddingplanner.databinding.AlertDialogNewCategoryBinding;
import com.example.weddingplanner.databinding.AlertDialogRecyclerListBinding;
import com.example.weddingplanner.pdfRepo.ReportRowModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddEditCostActivity extends BaseActivityRecyclerBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_ID = "id";
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    public static String EXTRA_POSITION_MAIN = "positionMain";
    private ActivityCostAddEditBinding binding;

    public ArrayList<CategoryRowModel> categoryList;



    public AppDataBase db;

    public Dialog dialogCategoryList;
    private AlertDialogRecyclerListBinding dialogCategoryListBinding;

    public Dialog dialogNewCat;

    public AlertDialogNewCategoryBinding dialogNewCatBinding;
    private File dir;
    private Document document;
    private String fileName = null;

    public ArrayList<ImageRowModel> imageList;
    private boolean isEdit = false;
    private boolean isUpdateList;

    public CostRowModel model;
    private Paragraph paragraph;
    private String repoTitle = "Detail";
    private String repoType = "Budget";

    public int selectedCategoryPos = 0;

    public int selectedNewCatPos = 0;
    private String subTitle = "Payments";
    public ToolbarModel toolbarModel;
    private PdfWriter writer = null;


    public void callApi() {
    }

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }


    public void setBinding() {
        binding = ActivityCostAddEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.db = AppDataBase.getAppDatabase(this);
        setModelDetail();
//        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        boolean z = false;

        this.model = new CostRowModel();
        this.model.setEdit(true);
        if (getIntent().hasExtra(EXTRA_IS_EDIT) && getIntent().getBooleanExtra(EXTRA_IS_EDIT, false)) {
            z = true;
        }
        this.isEdit = z;
        if (this.isEdit) {
            this.model = (CostRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
            return;
        }

        this.model.setArrayList(new ArrayList());
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.isEdit ? "Edit Budget" : "Add Budget");
        this.toolbarModel.setDelete(this.isEdit);
        this.toolbarModel.setOtherMenu(true);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
        this.toolbarModel.setShare(this.isEdit);
//        this.binding.includedToolbar.setModel(this.toolbarModel);

        isEdit = getIntent().getBooleanExtra(EXTRA_IS_EDIT, false);
        binding.includedToolbar.textTitle.setText(this.isEdit ? "Edit Budget" : "Add Budget");
        if (isEdit){
            binding.includedToolbar.imgDelete.setVisibility(View.VISIBLE);
            binding.includedToolbar.imgShare.setVisibility(View.GONE);
            binding.includedToolbar.etOther.setVisibility(View.GONE);
            binding.includedToolbar.search.setVisibility(View.GONE);
            binding.includedToolbar.imageHome.setVisibility(View.GONE);
            binding.includedToolbar.progressbar.setVisibility(View.GONE);
            binding.includedToolbar.imgAdd.setVisibility(View.GONE);
            binding.includedToolbar.spinner.setVisibility(View.GONE);
        }else {

            binding.includedToolbar.imgShare.setVisibility(View.GONE);
            binding.includedToolbar.etOther.setVisibility(View.GONE);
            binding.includedToolbar.imgDelete.setVisibility(View.GONE);
            binding.includedToolbar.search.setVisibility(View.GONE);
            binding.includedToolbar.imageHome.setVisibility(View.GONE);
            binding.includedToolbar.progressbar.setVisibility(View.GONE);
            binding.includedToolbar.imgAdd.setVisibility(View.GONE);
            binding.includedToolbar.spinner.setVisibility(View.GONE);
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
                    db.paymentDao().deleteAll(model.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    db.costDao().delete(model);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                openItemList(true);
            }
        });
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.linCategory.setOnClickListener(this);
        this.binding.btnAddEdit.setOnClickListener(this);
        this.binding.imgEdit.setOnClickListener(this);
        this.binding.imgAddAnother.setOnClickListener(this);
        this.binding.imgAddNoData.setOnClickListener(this);
        this.binding.linEdit.setOnClickListener(this);
        this.binding.imgExpandLin.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddEdit:
            case R.id.imgAddAnother:
            case R.id.imgAddNoData:
                addItem();
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.imgDelete:
                deleteItem();
                return;
            case R.id.imgEdit:
                if (!this.model.isEdit()) {
                    this.model.setEdit(true);
                    return;
                } else {
                    isAddUpdate(false);
                    return;
                }
            case R.id.imgExpandLin:
                this.model.setExpanded(true ^ this.model.isExpanded());
                return;
            case R.id.imgOther:
                isAddUpdate(true);
                return;

            case R.id.linCategory:
                showDialogCategoryList();
                return;
            case R.id.linEdit:
                if (!this.model.isEdit()) {
                    AppConstants.toastShort(this.context, "Enable editing ...");
                    return;
                }
                return;
            default:
                return;
        }
    }


    public void initMethods() {
        setEditTextValue();
        setEditTextChange();
        categoryDialogSetup();
        newCatDialogSetup();
    }

    private void setEditTextValue() {
        if (this.isEdit) {
            try {
                this.binding.etEstimatedAmount.setText(AppConstants.getFormattedPrice(this.model.getExpectedAmount()));
                binding.etName.setText(model.getName());
                binding.etNote.setText(model.getNote());
            } catch (NumberFormatException e) {
                this.binding.etEstimatedAmount.setText(0);
                e.printStackTrace();
            }
        }
    }

    private void setEditTextChange() {
        this.binding.etEstimatedAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    model.setExpectedAmount(Double.valueOf(charSequence.toString().trim()).doubleValue());
                } catch (NumberFormatException e) {
                    model.setExpectedAmount(0.0d);
                    e.printStackTrace();
                }
            }
        });
    }

    private void categoryDialogSetup() {
        fillCategoryList();
        setCategoryListDialog();
    }

    private void fillCategoryList() {
        this.categoryList = new ArrayList<>();
        try {
            this.categoryList.addAll(this.db.categoryDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.selectedCategoryPos = getSelectedPosById();
        this.categoryList.get(this.selectedCategoryPos).setSelected(true);
        if (this.model.getCategoryRowModel() == null) {
            this.model.setCategoryId(this.categoryList.get(this.selectedCategoryPos).getId());
            this.model.setCategoryRowModel(this.categoryList.get(this.selectedCategoryPos));
        }
    }

    private int getSelectedPosById() {
        for (int i = 0; i < this.categoryList.size(); i++) {
            if (this.categoryList.get(i).getId().equalsIgnoreCase(this.model.getCategoryId())) {
                return i;
            }
        }
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        binding.imgIcon.setImageResource(categoryList.get(selectedCategoryPos).getImgResId());
        binding.title.setText(categoryList.get(selectedCategoryPos).getName());
    }
    public void setCategoryListDialog() {
        this.dialogCategoryListBinding = AlertDialogRecyclerListBinding.inflate(LayoutInflater.from(this.context),  (ViewGroup) null, false);
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
                selectedCategoryPos = i;
                initView();
            }
        }));
        this.dialogCategoryListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showNewCatList();
            }
        });
        this.dialogCategoryListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogCategoryListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                model.setCategoryId(((CategoryRowModel) categoryList.get(selectedCategoryPos)).getId());
                model.setCategoryRowModel((CategoryRowModel) categoryList.get(selectedCategoryPos));
                try {
                    dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showDialogCategoryList() {
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


    public void selectionAllCategory(boolean z) {
        for (int i = 0; i < this.categoryList.size(); i++) {
            this.categoryList.get(i).setSelected(z);
        }
    }

    private void newCatDialogSetup() {
        fillImageList();
        setNewCatDialog();
    }

    private void fillImageList() {
        this.imageList = new ArrayList<>();
        ImageRowModel imageRowModel = new ImageRowModel();
        imageRowModel.setId(Constants.COST_CAT_TYPE_ATTIRE_ACCESSORIES);
        this.imageList.add(imageRowModel);
        ImageRowModel imageRowModel2 = new ImageRowModel();
        imageRowModel2.setId(Constants.COST_CAT_TYPE_HEALTH_BEAUTY);
        this.imageList.add(imageRowModel2);
        ImageRowModel imageRowModel3 = new ImageRowModel();
        imageRowModel3.setId(Constants.COST_CAT_TYPE_MUSIC_SHOW);
        this.imageList.add(imageRowModel3);
        ImageRowModel imageRowModel4 = new ImageRowModel();
        imageRowModel4.setId(Constants.COST_CAT_TYPE_FLOWER_DECOR);
        this.imageList.add(imageRowModel4);
        ImageRowModel imageRowModel5 = new ImageRowModel();
        imageRowModel5.setId(Constants.COST_CAT_TYPE_ACCESSORIES);
        this.imageList.add(imageRowModel5);
        ImageRowModel imageRowModel6 = new ImageRowModel();
        imageRowModel6.setId(Constants.COST_CAT_TYPE_JEWELRY);
        this.imageList.add(imageRowModel6);
        ImageRowModel imageRowModel7 = new ImageRowModel();
        imageRowModel7.setId(Constants.COST_CAT_TYPE_PHOTO_VIDEO);
        this.imageList.add(imageRowModel7);
        ImageRowModel imageRowModel8 = new ImageRowModel();
        imageRowModel8.setId(Constants.COST_CAT_TYPE_CEREMONY);
        this.imageList.add(imageRowModel8);
        ImageRowModel imageRowModel9 = new ImageRowModel();
        imageRowModel9.setId(Constants.COST_CAT_TYPE_RECEPTION);
        this.imageList.add(imageRowModel9);
        ImageRowModel imageRowModel10 = new ImageRowModel();
        imageRowModel10.setId(Constants.COST_CAT_TYPE_TRANSPORTATION);
        this.imageList.add(imageRowModel10);
        ImageRowModel imageRowModel11 = new ImageRowModel();
        imageRowModel11.setId(Constants.COST_CAT_TYPE_ACCOMMODATION);
        this.imageList.add(imageRowModel11);
        ImageRowModel imageRowModel12 = new ImageRowModel();
        imageRowModel12.setId(Constants.COST_CAT_TYPE_MISCELLANEOUS);
        this.imageList.add(imageRowModel12);
        this.imageList.get(this.selectedNewCatPos).setSelected(true);
    }

    public void setNewCatDialog() {
        this.dialogNewCatBinding = AlertDialogNewCategoryBinding.inflate(LayoutInflater.from(this.context), (ViewGroup) null, false);
        this.dialogNewCat = new Dialog(this.context);
        this.dialogNewCat.setContentView(this.dialogNewCatBinding.getRoot());
        this.dialogNewCat.setCancelable(false);
        this.dialogNewCat.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogNewCat.getWindow().setLayout(-1, -2);
        this.dialogNewCatBinding.txtTitle.setText(R.string.add_new_category);
        this.dialogNewCatBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false));
        this.dialogNewCatBinding.recycler.setAdapter(new ImageAdapter(true, this.context, this.imageList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                int unused = selectedNewCatPos = i;
            }
        }));
        this.dialogNewCatBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogNewCat.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogNewCatBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                long j;
                model.setName(binding.etName.getText().toString().trim());
                if (isValidNewCat(dialogNewCatBinding)) {
                    CategoryRowModel categoryRowModel = new CategoryRowModel(AppConstants.getUniqueId(), dialogNewCatBinding.etName.getText().toString().trim(), ((ImageRowModel) imageList.get(selectedNewCatPos)).getId());
                    try {
                        categoryRowModel.getName().trim();
                        j = db.categoryDao().insert(categoryRowModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                        j = 0;
                    }
                    if (j > 0) {
                        selectionAllCategory(false);
                        categoryRowModel.setSelected(true);
                        categoryList.add(categoryRowModel);
                        int unused = selectedCategoryPos = categoryList.size() - 1;
                        showDialogCategoryList();
                    }
                    dialogNewCatBinding.etName.setText("");
                }
                try {
                    dialogNewCat.dismiss();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }


    public boolean isValidNewCat(AlertDialogNewCategoryBinding alertDialogNewCategoryBinding) {
        Context context = this.context;
        EditText editText = alertDialogNewCategoryBinding.etName;
        return AppConstants.isNotEmpty(context, editText, getString(R.string.please_enter) + " " + getString(R.string.name));
    }


    public void showNewCatList() {
        try {
            this.dialogNewCatBinding.recycler.scrollToPosition(this.selectedNewCatPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.dialogNewCat != null && !this.dialogNewCat.isShowing()) {
                this.dialogNewCat.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public void fillData() {
        setViewVisibility();
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new PaymentAdapter(this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    updateTotal();
                } else {
                    openItemDetail(i, model.getArrayList().get(i), true);
                }
            }
        }));
    }

    private void addItem() {
        if (isAddUpdate(false)) {
            PaymentRowModel paymentRowModel = new PaymentRowModel();
            paymentRowModel.setParentId(this.model.getId());
            paymentRowModel.setType(1);
            paymentRowModel.setId(AppConstants.getUniqueId());
            paymentRowModel.setDateInMillis(Calendar.getInstance().getTimeInMillis());
            openItemDetail(-1, paymentRowModel, false);
        }
    }


    public void openItemDetail(int i, PaymentRowModel paymentRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditPaymentActivity.class);
        intent.putExtra(AddEditPaymentActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditPaymentActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditPaymentActivity.EXTRA_MODEL, paymentRowModel);
       ;
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

    private boolean isAddUpdate(boolean z) {
        model.setName(binding.etName.getText().toString());
        model.setNote(binding.etNote.getText().toString());
        model.setExpectedAmount(Double.parseDouble(binding.etEstimatedAmount.getText().toString().trim()));
        if (!isValid()) {
            return false;
        }
        try {
            this.model.getName().trim();
            if (this.model.getId() != null) {
                this.db.costDao().update(this.model);
            } else {
                this.model.setId(AppConstants.getUniqueId());
                this.db.costDao().insert(this.model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.model.setEdit(false);
        if (!z) {
            return true;
        }
        openItemList(false);
        return true;
    }

    private boolean isValid() {
        if (this.model.getName().trim().isEmpty()) {
            Context context = this.context;
            EditText editText = this.binding.etName;
            AppConstants.requestFocusAndError(context, editText, getString(R.string.please_enter) + " " + getString(R.string.name));
            return false;
        } else if (this.model.getExpectedAmount() > 0.0d) {
            return true;
        } else {
            Context context2 = this.context;
            EditText editText2 = this.binding.etEstimatedAmount;
            AppConstants.requestFocusAndError(context2, editText2, getString(R.string.please_enter) + " " + getString(R.string.estimated_amount));
            return false;
        }
    }


    public void openItemList(boolean z) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_DELETED, z);
        intent.putExtra(EXTRA_IS_EDIT, getIntent().getBooleanExtra(EXTRA_IS_EDIT, false));
        intent.putExtra(EXTRA_POSITION, getIntent().getIntExtra(EXTRA_POSITION, 0));
        intent.putExtra(EXTRA_POSITION_MAIN, getIntent().getIntExtra(EXTRA_POSITION_MAIN, 0));
        intent.putExtra(EXTRA_MODEL, this.model);
        setResult(-1, intent);
        super.onBackPressed();
    }


    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1002) {
            updateList(intent);
        }
    }

    private void updateList(Intent intent) {
        if (intent != null) {
            try {
                if (intent.hasExtra(EXTRA_MODEL)) {
                    PaymentRowModel paymentRowModel = (PaymentRowModel) intent.getParcelableExtra(EXTRA_MODEL);
                    if (intent.getBooleanExtra(EXTRA_IS_DELETED, false)) {
                        this.model.getArrayList().remove(intent.getIntExtra(EXTRA_POSITION, 0));
                    } else if (intent.getBooleanExtra(EXTRA_IS_EDIT, false)) {
                        this.model.getArrayList().set(intent.getIntExtra(EXTRA_POSITION, 0), paymentRowModel);
                    } else {
                        this.model.getArrayList().add(paymentRowModel);
                    }
                    updateTotal();
                    notifyAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void updateTotal() {
        try {
            this.model.setPendingAmount(this.db.paymentDao().getTotal(this.model.getId(), 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.model.setPaidAmount(this.db.paymentDao().getTotal(this.model.getId(), 0));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.isUpdateList = true;
    }

    public void onBackPressed() {
        if (this.isUpdateList) {
            openItemList(false);
        } else {
            super.onBackPressed();
        }
    }



    public void savePdf() {
        if (isHasPermissions(this, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
            saveDoc();
            return;
        }
        requestPermissions(this, getString(R.string.rationale_save), Constants.REQUEST_PERM_FILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
    }

    private void saveDoc() {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
                initDoc();
            }

            public void doInBackground() {
                fillDocData();
            }

            public void onPostExecute() {
                addingDocFooter();
            }
        }).execute(new Object[0]);
    }


    public void initDoc() {
        this.document = new Document(PageSize.A4, 16.0f, 16.0f, 16.0f, 16.0f);
        this.dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.REPORT_DIRECTORY);
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
        try {
            this.fileName = this.repoType + "_" + this.repoTitle + "_" + getCurrentDateTime() + ".pdf";
            try {
                this.writer = PdfWriter.getInstance(this.document, new FileOutputStream(new File(this.dir, this.fileName)));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.document.open();
    }


    public void fillDocData() {
        this.paragraph = new Paragraph((this.repoType + " " + this.repoTitle).toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 18.0f, 1));
        this.paragraph.setAlignment(1);
        this.paragraph.add((Element) new Paragraph(" "));
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        this.paragraph = new Paragraph("");
        this.paragraph.add((Element) new Paragraph(" "));
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e2) {
            e2.printStackTrace();
        }
        CostRowModel costRowModel = this.model;
        Paragraph paragraph2 = this.paragraph;
        paragraph2.add((Element) new Paragraph("Name : " + costRowModel.getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph3 = this.paragraph;
        paragraph3.add((Element) new Paragraph("Category : " + costRowModel.getCategoryRowModel().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph4 = this.paragraph;
        paragraph4.add((Element) new Paragraph("Status : " + costRowModel.getStatusText(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph5 = this.paragraph;
        paragraph5.add((Element) new Paragraph("Estimated amount : " + costRowModel.getExpectedAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph6 = this.paragraph;
        paragraph6.add((Element) new Paragraph("Balance : " + costRowModel.getBalanceFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph7 = this.paragraph;
        paragraph7.add((Element) new Paragraph("Paid : " + costRowModel.getPaidAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph8 = this.paragraph;
        paragraph8.add((Element) new Paragraph("Pending : " + costRowModel.getPendingAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        String note = costRowModel.getNote();
        if (!note.isEmpty()) {
            Paragraph paragraph9 = this.paragraph;
            paragraph9.add((Element) new Paragraph("Note : " + note, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        this.paragraph.setAlignment(0);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e3) {
            e3.printStackTrace();
        }
        if (costRowModel.getArrayList() != null && costRowModel.getArrayList().size() > 0) {
            this.paragraph = new Paragraph(this.subTitle.toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, 0));
            this.paragraph.add((Element) new Paragraph(" "));
            this.paragraph.setAlignment(1);
            try {
                this.document.add(this.paragraph);
            } catch (DocumentException e4) {
                e4.printStackTrace();
            }
            try {
                this.document.add(getTable(costRowModel.getArrayList()));
            } catch (DocumentException e5) {
                e5.printStackTrace();
            }
        }
    }

    private PdfPTable getTable(ArrayList<PaymentRowModel> arrayList) {
        ArrayList<ReportRowModel> fillSubList = fillSubList(arrayList);
        PdfPTable pdfPTable = new PdfPTable(fillSubList.get(0).getValueList().size());
        pdfPTable.getDefaultCell().setVerticalAlignment(1);
        pdfPTable.getDefaultCell().setHorizontalAlignment(1);
        pdfPTable.setWidthPercentage(100.0f);
        pdfPTable.setSpacingBefore(0.0f);
        pdfPTable.setSpacingAfter(0.0f);
        for (int i = 0; i < fillSubList.size(); i++) {
            for (int i2 = 0; i2 < fillSubList.get(i).getValueList().size(); i2++) {
                pdfPTable.addCell(fillSubList.get(i).getValueList().get(i2));
            }
            if (i == 0) {
                pdfPTable.setHeaderRows(1);
                PdfPCell[] cells = pdfPTable.getRow(0).getCells();
                for (PdfPCell backgroundColor : cells) {
                    backgroundColor.setBackgroundColor(BaseColor.GRAY);
                }
            }
        }
        return pdfPTable;
    }


    public void addingDocFooter() {
        new FooterPageEvent().onEndPage(this.writer, this.document);
        try {
            this.document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ReportRowModel> fillSubList(ArrayList<PaymentRowModel> arrayList) {
        ArrayList<ReportRowModel> arrayList2 = new ArrayList<>();
        if (arrayList.size() <= 0) {
            return arrayList2;
        }
        arrayList2.clear();
        ReportRowModel reportRowModel = new ReportRowModel();
        reportRowModel.setValueList(new ArrayList());
        reportRowModel.getValueList().add("Name");
        reportRowModel.getValueList().add("Amount");
        reportRowModel.getValueList().add("Status");
        reportRowModel.getValueList().add("Date");
        arrayList2.add(reportRowModel);
        for (int i = 0; i < arrayList.size(); i++) {
            ReportRowModel reportRowModel2 = new ReportRowModel();
            reportRowModel2.setValueList(new ArrayList());
            reportRowModel2.getValueList().add(arrayList.get(i).getName());
            reportRowModel2.getValueList().add(arrayList.get(i).getAmountFormatted());
            reportRowModel2.getValueList().add(arrayList.get(i).isPending() ? "Pending" : "Paid");
            ArrayList<String> valueList = reportRowModel2.getValueList();
            StringBuilder sb = new StringBuilder();
            sb.append(arrayList.get(i).isPending() ? "Expire on " : "Paid on ");
            sb.append(arrayList.get(i).getDateFormatted());
            valueList.add(sb.toString());
            arrayList2.add(reportRowModel2);
        }
        return arrayList2;
    }


//    public void openReportList() {
//        startActivity(new Intent(this, ReportsListActivity.class));
//    }

    public class FooterPageEvent extends PdfPageEventHelper {
        public FooterPageEvent() {
        }

        public void onEndPage(PdfWriter pdfWriter, Document document) {
            try {
                PdfContentByte directContent = pdfWriter.getDirectContent();
                ColumnText.showTextAligned(directContent, 1, new Phrase("Created by : " + getString(R.string.app_name), new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, 1)), document.leftMargin() + ((document.right() - document.left()) / 2.0f), document.bottom() + 10.0f, 0.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getCurrentDateTime() {
        return new SimpleDateFormat("dd_MM_yyyy_hh:mm:ss a").format(new Date(Calendar.getInstance().getTimeInMillis()));
    }

    private boolean isHasPermissions(Context context, String... strArr) {
        return EasyPermissions.hasPermissions(context, strArr);
    }

    private void requestPermissions(Context context, String str, int i, String... strArr) {
        EasyPermissions.requestPermissions((Activity) context, str, i, strArr);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    public void onPermissionsGranted(int i, @NonNull List<String> list) {
        if (i == 1053) {
            saveDoc();
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }
}
