package com.example.weddingplanner.appBase.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
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
import com.example.weddingplanner.appBase.roomsDB.payment.PaymentRowModel;
import com.example.weddingplanner.appBase.roomsDB.vendor.VendorRowModel;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.appBase.utils.BackgroundAsync;
import com.example.weddingplanner.appBase.utils.Constants;
import com.example.weddingplanner.appBase.utils.OnAsyncBackground;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.example.weddingplanner.databinding.ActivityVendorAddEditBinding;
import com.example.weddingplanner.databinding.AlertDialogNewCategoryBinding;
import com.example.weddingplanner.databinding.AlertDialogRecyclerListBinding;
import com.example.weddingplanner.pdfRepo.ReportRowModel;
import com.example.weddingplanner.pdfRepo.ReportsListActivity;
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

public class AddEditVendorActivity extends BaseActivityRecyclerBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_ID = "id";
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    public static String EXTRA_POSITION_MAIN = "positionMain";
    private ActivityVendorAddEditBinding binding;

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

    public VendorRowModel model;
    private Paragraph paragraph;
    private String repoTitle = "Detail";
    private String repoType = "Vendor";

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
        binding = ActivityVendorAddEditBinding.inflate(getLayoutInflater());
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
        if (this.isEdit) {
            this.model = (VendorRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
            return;
        }
        this.model = new VendorRowModel();
        this.model.setEdit(true);
        this.model.setExpandedContact(true);
        this.model.setArrayList(new ArrayList());
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.isEdit ? "Edit Vendor" : "Add Vendor");
        this.toolbarModel.setDelete(true);
        this.binding.includedToolbar.imgDelete.setImageResource(this.isEdit ? R.drawable.delete : R.drawable.phone_book);
        this.toolbarModel.setOtherMenu(true);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
        this.toolbarModel.setShare(this.isEdit);
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
                    AddEditVendorActivity.this.db.paymentDao().deleteAll(AddEditVendorActivity.this.model.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    AddEditVendorActivity.this.db.vendorDao().delete(AddEditVendorActivity.this.model);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                AddEditVendorActivity.this.openItemList(true);
            }
        });
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.includedToolbar.imgShare.setOnClickListener(this);
        this.binding.linCategory.setOnClickListener(this);
        this.binding.btnAddEdit.setOnClickListener(this);
        this.binding.imgEdit.setOnClickListener(this);
        this.binding.imgAdd.setOnClickListener(this);
        this.binding.imgAddNoData.setOnClickListener(this);
        this.binding.linEdit.setOnClickListener(this);
        this.binding.linEditContact.setOnClickListener(this);
        this.binding.imgExpandLin.setOnClickListener(this);
        this.binding.imgExpandContact.setOnClickListener(this);
        this.binding.imgPhone.setOnClickListener(this);
        this.binding.imgEmail.setOnClickListener(this);
        this.binding.imgWebsite.setOnClickListener(this);
        this.binding.imgAddress.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddEdit:
            case R.id.imgAdd:
            case R.id.imgAddNoData:
                addItem();
                return;
            case R.id.imgAddress:
                openMap(this.model.getAddress());
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.imgDelete:
                if (this.isEdit) {
                    deleteItem();
                    return;
                } else {
                    pickContactPerm();
                    return;
                }
            case R.id.imgEdit:
                if (!this.model.isEdit()) {
                    this.model.setEdit(true);
                    return;
                } else {
                    isAddUpdate(false);
                    return;
                }
            case R.id.imgEmail:
                sendEmail(this.model.getEmailId());
                return;
            case R.id.imgExpandContact:
                this.model.setExpandedContact(true ^ this.model.isExpandedContact());
                return;
            case R.id.imgExpandLin:
                this.model.setExpanded(true ^ this.model.isExpanded());
                return;
            case R.id.imgOther:
                isAddUpdate(true);
                return;
            case R.id.imgPhone:
                dialPhoneNumber(this.model.getPhoneNo());
                return;
            case R.id.imgShare:
                showPdfDialog();
                return;
            case R.id.imgWebsite:
                openUrl(this.model.getWebSite());
                return;
            case R.id.linCategory:
                showDialogCategoryList();
                return;
            case R.id.linEdit:
            case R.id.linEditContact:
                if (!this.model.isEdit()) {
                    AppConstants.toastShort(this.context, "Enable editing ...");
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void pickContact() {
        startActivityForResult(new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI), Constants.REQUEST_PICK_CONTACT);
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
                    AddEditVendorActivity.this.model.setExpectedAmount(Double.valueOf(charSequence.toString().trim()).doubleValue());
                } catch (NumberFormatException e) {
                    AddEditVendorActivity.this.model.setExpectedAmount(0.0d);
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
                    AddEditVendorActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AddEditVendorActivity.this.showNewCatList();
            }
        });
        this.dialogCategoryListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    AddEditVendorActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogCategoryListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddEditVendorActivity.this.model.setCategoryId(((CategoryRowModel) AddEditVendorActivity.this.categoryList.get(AddEditVendorActivity.this.selectedCategoryPos)).getId());
                AddEditVendorActivity.this.model.setCategoryRowModel((CategoryRowModel) AddEditVendorActivity.this.categoryList.get(AddEditVendorActivity.this.selectedCategoryPos));
                try {
                    AddEditVendorActivity.this.dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        binding.imgIcon.setImageResource(categoryList.get(selectedCategoryPos).getImgResId());
        binding.tvName.setText(categoryList.get(selectedCategoryPos).getName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
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
                selectedNewCatPos = i;
                initView();
            }
        }));
        this.dialogNewCatBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    AddEditVendorActivity.this.dialogNewCat.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogNewCatBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                long j;
                model.setName(binding.etName.getText().toString());
                if (AddEditVendorActivity.this.isValidNewCat(AddEditVendorActivity.this.dialogNewCatBinding)) {
                    CategoryRowModel categoryRowModel = new CategoryRowModel(AppConstants.getUniqueId(), AddEditVendorActivity.this.dialogNewCatBinding.etName.getText().toString().trim(), ((ImageRowModel) AddEditVendorActivity.this.imageList.get(AddEditVendorActivity.this.selectedNewCatPos)).getId());
                    try {
                        categoryRowModel.getName().trim();
                        j = AddEditVendorActivity.this.db.categoryDao().insert(categoryRowModel);
                    } catch (Exception e) {
                        e.printStackTrace();
                        j = 0;
                    }
                    if (j > 0) {
                        AddEditVendorActivity.this.selectionAllCategory(false);
                        categoryRowModel.setSelected(true);
                        AddEditVendorActivity.this.categoryList.add(categoryRowModel);
                        int unused = AddEditVendorActivity.this.selectedCategoryPos = AddEditVendorActivity.this.categoryList.size() - 1;
                        AddEditVendorActivity.this.showDialogCategoryList();
                    }
                    AddEditVendorActivity.this.dialogNewCatBinding.etName.setText("");
                }
                try {
                    AddEditVendorActivity.this.dialogNewCat.dismiss();
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
                    AddEditVendorActivity.this.updateTotal();
                } else {
                    AddEditVendorActivity.this.openItemDetail(i, AddEditVendorActivity.this.model.getArrayList().get(i), true);
                }
            }
        }));
    }

    private void addItem() {
        if (isAddUpdate(false)) {
            PaymentRowModel paymentRowModel = new PaymentRowModel();
            paymentRowModel.setParentId(this.model.getId());
            paymentRowModel.setType(2);
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
        if (!isValid()) {
            return false;
        }
        try {
            this.model.getName().trim();
            this.model.getPhoneNo().trim();
            this.model.getEmailId().trim();
            this.model.getWebSite().trim();
            if (this.model.getId() != null) {
                this.db.vendorDao().update(this.model);
            } else {
                this.model.setId(AppConstants.getUniqueId());
                this.db.vendorDao().insert(this.model);
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
        model.setName(binding.etName.getText().toString());
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
    }

    public void pickContactPerm() {
        if (isHasPermissions(this.context, "android.permission.READ_CONTACTS")) {
            pickContact();
            return;
        }
        requestPermissions(this.context, getString(R.string.rationale_contact), 1051, "android.permission.READ_CONTACTS");
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
        if (i == 1051) {
            pickContact();
        } else if (i == 1053) {
            saveDoc();
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }


    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i == 1002) {
            updateList(intent);
        } else if (i == 1101) {
            addDetailFromContact(intent);
        }
    }

    private void addDetailFromContact(Intent intent) {
        String str;
        Cursor query;
        String string;
        Cursor query2;
        String string2;
        Cursor query3;
        String string3;
        String str2 = "";
        String str3 = "";
        String str4 = "";
        String str5 = "";
        Cursor managedQuery = managedQuery(intent.getData(), (String[]) null, (String) null, (String[]) null, (String) null);
        if (managedQuery.moveToFirst()) {
            String string4 = managedQuery.getString(managedQuery.getColumnIndexOrThrow("_id"));
            str = managedQuery.getString(managedQuery.getColumnIndex("display_name"));
            try {
                if (Integer.valueOf(managedQuery.getInt(managedQuery.getColumnIndex("has_phone_number"))).intValue() > 0) {
                    ContentResolver contentResolver = getContentResolver();
                    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    Cursor query4 = contentResolver.query(uri, (String[]) null, "contact_id = " + string4, (String[]) null, (String) null);
                    if (query4 != null && query4.moveToFirst()) {
                        String string5 = query4.getString(query4.getColumnIndex("data1"));
                        try {
                            query4.close();
                            str2 = string5;
                        } catch (Exception e) {
                            e = e;
                            str2 = string5;
                            try {
                                e.printStackTrace();
                                ContentResolver contentResolver2 = getContentResolver();
                                Uri uri2 = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                                query3 = contentResolver2.query(uri2, (String[]) null, "contact_id = " + string4, (String[]) null, (String) null);
                                string3 = query3.getString(query3.getColumnIndex("data1"));
                                try {
                                    query3.close();
                                    str3 = string3;
                                } catch (Exception e2) {
                                    e = e2;
                                    str3 = string3;
                                    e.printStackTrace();
                                    ContentResolver contentResolver3 = getContentResolver();
                                    Uri uri3 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                                    query2 = contentResolver3.query(uri3, (String[]) null, "contact_id=" + string4, (String[]) null, (String) null);
                                    string2 = query2.getString(query2.getColumnIndex("data1"));
                                    query2.close();
                                    str4 = string2;
                                    ContentResolver contentResolver4 = getContentResolver();
                                    Uri uri4 = ContactsContract.Data.CONTENT_URI;
                                    query = contentResolver4.query(uri4, new String[]{"data1", "data2"}, "contact_id = " + string4 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                                    string = query.getString(query.getColumnIndex("data1"));
                                    query.close();
                                    str5 = string;
                                    this.model.setName(str);
                                    this.model.setPhoneNo(str2);
                                    this.model.setEmailId(str3);
                                    this.model.setAddress(str4);
                                    this.model.setWebSite(str5);
                                }
                                ContentResolver contentResolver32 = getContentResolver();
                                Uri uri32 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                                query2 = contentResolver32.query(uri32, (String[]) null, "contact_id=" + string4, (String[]) null, (String) null);
                                string2 = query2.getString(query2.getColumnIndex("data1"));
                                try {
                                    query2.close();
                                    str4 = string2;
                                } catch (Exception e3) {
                                    e = e3;
                                    str4 = string2;
                                    e.printStackTrace();
                                    ContentResolver contentResolver42 = getContentResolver();
                                    Uri uri42 = ContactsContract.Data.CONTENT_URI;
                                    query = contentResolver42.query(uri42, new String[]{"data1", "data2"}, "contact_id = " + string4 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                                    string = query.getString(query.getColumnIndex("data1"));
                                    query.close();
                                    str5 = string;
                                    this.model.setName(str);
                                    this.model.setPhoneNo(str2);
                                    this.model.setEmailId(str3);
                                    this.model.setAddress(str4);
                                    this.model.setWebSite(str5);
                                }
                                ContentResolver contentResolver422 = getContentResolver();
                                Uri uri422 = ContactsContract.Data.CONTENT_URI;
                                query = contentResolver422.query(uri422, new String[]{"data1", "data2"}, "contact_id = " + string4 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                                string = query.getString(query.getColumnIndex("data1"));
                                try {
                                    query.close();
                                    str5 = string;
                                } catch (Exception e4) {
                                    e = e4;
                                    str5 = string;
                                    e.printStackTrace();
                                    this.model.setName(str);
                                    this.model.setPhoneNo(str2);
                                    this.model.setEmailId(str3);
                                    this.model.setAddress(str4);
                                    this.model.setWebSite(str5);
                                }
                                this.model.setName(str);
                                this.model.setPhoneNo(str2);
                                this.model.setEmailId(str3);
                                this.model.setAddress(str4);
                                this.model.setWebSite(str5);
                            } catch (Exception e5) {
                                e5.printStackTrace();
                                return;
                            }
                        }
                    }
                }
            } catch (Exception e6) {

                ContentResolver contentResolver22 = getContentResolver();
                Uri uri22 = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                query3 = contentResolver22.query(uri22, (String[]) null, "contact_id = " + string4, (String[]) null, (String) null);
                string3 = query3.getString(query3.getColumnIndex("data1"));
                query3.close();
                str3 = string3;
                ContentResolver contentResolver322 = getContentResolver();
                Uri uri322 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                query2 = contentResolver322.query(uri322, (String[]) null, "contact_id=" + string4, (String[]) null, (String) null);
                string2 = query2.getString(query2.getColumnIndex("data1"));
                query2.close();
                str4 = string2;
                ContentResolver contentResolver4222 = getContentResolver();
                Uri uri4222 = ContactsContract.Data.CONTENT_URI;
                query = contentResolver4222.query(uri4222, new String[]{"data1", "data2"}, "contact_id = " + string4 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                string = query.getString(query.getColumnIndex("data1"));
                query.close();
                str5 = string;
                this.model.setName(str);
                this.model.setPhoneNo(str2);
                this.model.setEmailId(str3);
                this.model.setAddress(str4);
                this.model.setWebSite(str5);
            }
            try {
                ContentResolver contentResolver222 = getContentResolver();
                Uri uri222 = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                query3 = contentResolver222.query(uri222, (String[]) null, "contact_id = " + string4, (String[]) null, (String) null);
                if (query3 != null && query3.moveToFirst()) {
                    string3 = query3.getString(query3.getColumnIndex("data1"));
                    query3.close();
                    str3 = string3;
                }
            } catch (Exception e7) {

                ContentResolver contentResolver3222 = getContentResolver();
                Uri uri3222 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                query2 = contentResolver3222.query(uri3222, (String[]) null, "contact_id=" + string4, (String[]) null, (String) null);
                string2 = query2.getString(query2.getColumnIndex("data1"));
                query2.close();
                str4 = string2;
                ContentResolver contentResolver42222 = getContentResolver();
                Uri uri42222 = ContactsContract.Data.CONTENT_URI;
                query = contentResolver42222.query(uri42222, new String[]{"data1", "data2"}, "contact_id = " + string4 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                string = query.getString(query.getColumnIndex("data1"));
                query.close();
                str5 = string;
                this.model.setName(str);
                this.model.setPhoneNo(str2);
                this.model.setEmailId(str3);
                this.model.setAddress(str4);
                this.model.setWebSite(str5);
            }
            try {
                ContentResolver contentResolver32222 = getContentResolver();
                Uri uri32222 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                query2 = contentResolver32222.query(uri32222, (String[]) null, "contact_id=" + string4, (String[]) null, (String) null);
                if (query2 != null && query2.moveToFirst()) {
                    string2 = query2.getString(query2.getColumnIndex("data1"));
                    query2.close();
                    str4 = string2;
                }
            } catch (Exception e8) {

                ContentResolver contentResolver422222 = getContentResolver();
                Uri uri422222 = ContactsContract.Data.CONTENT_URI;
                query = contentResolver422222.query(uri422222, new String[]{"data1", "data2"}, "contact_id = " + string4 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                string = query.getString(query.getColumnIndex("data1"));
                query.close();
                str5 = string;
                this.model.setName(str);
                this.model.setPhoneNo(str2);
                this.model.setEmailId(str3);
                this.model.setAddress(str4);
                this.model.setWebSite(str5);
            }
            try {
                ContentResolver contentResolver4222222 = getContentResolver();
                Uri uri4222222 = ContactsContract.Data.CONTENT_URI;
                query = contentResolver4222222.query(uri4222222, new String[]{"data1", "data2"}, "contact_id = " + string4 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                if (query != null && query.moveToFirst()) {
                    string = query.getString(query.getColumnIndex("data1"));
                    query.close();
                    str5 = string;
                }
            } catch (Exception e9) {

                this.model.setName(str);
                this.model.setPhoneNo(str2);
                this.model.setEmailId(str3);
                this.model.setAddress(str4);
                this.model.setWebSite(str5);
            }
        } else {
            str = "";
        }
        this.model.setName(str);
        this.model.setPhoneNo(str2);
        this.model.setEmailId(str3);
        this.model.setAddress(str4);
        this.model.setWebSite(str5);
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
        } else if (this.isEdit) {
        } else {
            super.onBackPressed();
        }
    }

    public void dialPhoneNumber(String str) {
        if (str == null || str.trim().length() <= 0) {
            AppConstants.toastShort(this.context, "Phone number not found");
            return;
        }
        try {
            Intent intent = new Intent("android.intent.action.DIAL");
            startActivity(intent.setData(Uri.parse("tel:" + str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String str) {
        if (str == null || str.trim().length() <= 0) {
            AppConstants.toastShort(this.context, "Email id not found");
            return;
        }
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("mailto:" + str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openUrl(String str) {
        if (str == null || str.trim().length() <= 0) {
            AppConstants.toastShort(this.context, "Website not found");
            return;
        }
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            str = "http://" + str;
        }
        Intent intent = null;
        try {
            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str));
            try {
                intent2.addFlags(1208483840);
                intent = intent2;
            } catch (Exception e) {
                Intent intent3 = intent2;
                e = e;
                intent = intent3;
                e.printStackTrace();
                startActivity(intent);
            }
        } catch (Exception e2) {

            startActivity(intent);
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        }
    }

    public void openMap(String str) {
        if (str == null || str.trim().length() <= 0) {
            AppConstants.toastShort(this.context, "Address not found");
            return;
        }
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google.co.in/maps?q=" + str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPdfDialog() {
        AppConstants.pdfReportDialog(this.context, new TwoButtonDialogListener() {
            public void onOk() {
                AddEditVendorActivity.this.savePdf();
            }

            public void onCancel() {
                AddEditVendorActivity.this.openReportList();
            }
        });
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
                AddEditVendorActivity.this.initDoc();
            }

            public void doInBackground() {
                AddEditVendorActivity.this.fillDocData();
            }

            public void onPostExecute() {
                AddEditVendorActivity.this.addingDocFooter();
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
        VendorRowModel vendorRowModel = this.model;
        Paragraph paragraph2 = this.paragraph;
        paragraph2.add((Element) new Paragraph("Name : " + vendorRowModel.getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph3 = this.paragraph;
        paragraph3.add((Element) new Paragraph("Category : " + vendorRowModel.getCategoryRowModel().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph4 = this.paragraph;
        paragraph4.add((Element) new Paragraph("Status : " + vendorRowModel.getStatusText(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph5 = this.paragraph;
        paragraph5.add((Element) new Paragraph("Estimated amount : " + vendorRowModel.getExpectedAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph6 = this.paragraph;
        paragraph6.add((Element) new Paragraph("Balance : " + vendorRowModel.getBalanceFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph7 = this.paragraph;
        paragraph7.add((Element) new Paragraph("Paid : " + vendorRowModel.getPaidAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph8 = this.paragraph;
        paragraph8.add((Element) new Paragraph("Pending : " + vendorRowModel.getPendingAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        String note = vendorRowModel.getNote();
        if (!note.isEmpty()) {
            Paragraph paragraph9 = this.paragraph;
            paragraph9.add((Element) new Paragraph("Note : " + note, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String phoneNo = vendorRowModel.getPhoneNo();
        if (!phoneNo.isEmpty()) {
            Paragraph paragraph10 = this.paragraph;
            paragraph10.add((Element) new Paragraph("Phone No : " + phoneNo, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String emailId = vendorRowModel.getEmailId();
        if (!emailId.isEmpty()) {
            Paragraph paragraph11 = this.paragraph;
            paragraph11.add((Element) new Paragraph("Email Id : " + emailId, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String webSite = vendorRowModel.getWebSite();
        if (!webSite.isEmpty()) {
            Paragraph paragraph12 = this.paragraph;
            paragraph12.add((Element) new Paragraph("WebSite : " + webSite, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String address = vendorRowModel.getAddress();
        if (!address.isEmpty()) {
            Paragraph paragraph13 = this.paragraph;
            paragraph13.add((Element) new Paragraph("Address : " + address, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        this.paragraph.setAlignment(0);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e3) {
            e3.printStackTrace();
        }
        if (vendorRowModel.getArrayList() != null && vendorRowModel.getArrayList().size() > 0) {
            this.paragraph = new Paragraph(this.subTitle.toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, 0));
            this.paragraph.add((Element) new Paragraph(" "));
            this.paragraph.setAlignment(1);
            try {
                this.document.add(this.paragraph);
            } catch (DocumentException e4) {
                e4.printStackTrace();
            }
            try {
                this.document.add(getTable(vendorRowModel.getArrayList()));
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
            openReportList();
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


    public void openReportList() {
        startActivity(new Intent(this, ReportsListActivity.class));
    }

    public class FooterPageEvent extends PdfPageEventHelper {
        public FooterPageEvent() {
        }

        public void onEndPage(PdfWriter pdfWriter, Document document) {
            try {
                PdfContentByte directContent = pdfWriter.getDirectContent();
                ColumnText.showTextAligned(directContent, 1, new Phrase("Created by : " + AddEditVendorActivity.this.getString(R.string.app_name), new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, 1)), document.leftMargin() + ((document.right() - document.left()) / 2.0f), document.bottom() + 10.0f, 0.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getCurrentDateTime() {
        return new SimpleDateFormat("dd_MM_yyyy_hh:mm:ss a").format(new Date(Calendar.getInstance().getTimeInMillis()));
    }
}
