package com.example.weddingplanner.allLeading.view;

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
import com.example.weddingplanner.allLeading.adapter.CategoryAdapterLeading;
import com.example.weddingplanner.allLeading.adapter.ImageAdapterLeading;
import com.example.weddingplanner.allLeading.adapter.PaymentAdapterLeading;
import com.example.weddingplanner.allLeading.baseClass.BaseActivityRecyclerBindingLeading;
import com.example.weddingplanner.allLeading.models.image.ImageRowModel;
import com.example.weddingplanner.allLeading.models.toolbar.ToolbarModel;
import com.example.weddingplanner.allLeading.roomDatabase.AppDataBase;
import com.example.weddingplanner.allLeading.roomDatabase.category.CategoryRowModel;
import com.example.weddingplanner.allLeading.roomDatabase.payment.PaymentRowModel;
import com.example.weddingplanner.allLeading.roomDatabase.vendor.VendorRowModel;
import com.example.weddingplanner.allLeading.utils.AppConstants;
import com.example.weddingplanner.allLeading.utils.BackgroundAsync;
import com.example.weddingplanner.allLeading.utils.Constants;
import com.example.weddingplanner.allLeading.utils.OnAsyncBackground;
import com.example.weddingplanner.allLeading.utils.RecyclerItemClick;
import com.example.weddingplanner.allLeading.utils.TwoButtonDialogListener;
import com.example.weddingplanner.databinding.ActivityVendorAddEditBinding;
import com.example.weddingplanner.databinding.AlertDialogNewCategoryBinding;
import com.example.weddingplanner.databinding.AlertDialogRecyclerListBinding;
import com.example.weddingplanner.pdfViewLeading.ReportRowModel;

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

public class AddEditVendorActivityLeading extends BaseActivityRecyclerBindingLeading implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
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
        db = AppDataBase.getAppDatabase(this);
        setModelDetail();
//        binding.setRowModel(model);
    }

    private void setModelDetail() {
        boolean z = false;
        if (getIntent().hasExtra(EXTRA_IS_EDIT) && getIntent().getBooleanExtra(EXTRA_IS_EDIT, false)) {
            z = true;
        }
        isEdit = z;
        if (isEdit) {
            model = (VendorRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
            return;
        }
        model = new VendorRowModel();
        model.setEdit(true);
        model.setExpandedContact(true);
        model.setArrayList(new ArrayList());
    }


    public void setToolbar() {
        toolbarModel = new ToolbarModel();
        toolbarModel.setTitle(isEdit ? "Edit Vendor" : "Add Vendor");
        toolbarModel.setDelete(true);
        toolbarModel.setOtherMenu(true);

        isEdit = getIntent().getBooleanExtra(EXTRA_IS_EDIT,false);


        binding.includedToolbar.imgDelete.setImageResource(isEdit ? R.drawable.delete : R.drawable.phone_book);
        binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
        toolbarModel.setShare(isEdit);
        if (isEdit){
            binding.includedToolbar.imgAdd.setVisibility(View.GONE);
            binding.includedToolbar.imgShare.setVisibility(View.GONE);
            binding.includedToolbar.etOther.setVisibility(View.GONE);
            binding.includedToolbar.search.setVisibility(View.GONE);
            binding.includedToolbar.imageHome.setVisibility(View.GONE);
            binding.includedToolbar.progressbar.setVisibility(View.GONE);
            binding.includedToolbar.imgAdd.setVisibility(View.GONE);
            binding.includedToolbar.spinner.setVisibility(View.GONE);
        }else {
            binding.includedToolbar.imgAdd.setVisibility(View.GONE);
            binding.includedToolbar.imgShare.setVisibility(View.GONE);
            binding.includedToolbar.etOther.setVisibility(View.GONE);
            binding.includedToolbar.search.setVisibility(View.GONE);
            binding.includedToolbar.imageHome.setVisibility(View.GONE);
            binding.includedToolbar.progressbar.setVisibility(View.GONE);
            binding.includedToolbar.imgAdd.setVisibility(View.GONE);
            binding.includedToolbar.spinner.setVisibility(View.GONE);
        }
//        binding.includedToolbar.setModel(toolbarModel);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        menu.findItem(R.id.delete).setVisible(isEdit);
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
        AppConstants.showTwoButtonDialog(context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>" + model.getName() + "</b>", true, true, getString(R.string.delete), getString(R.string.cancel), new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                try {
                    db.paymentDao().deleteAll(model.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    db.vendorDao().delete(model);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                openItemList(true);
            }
        });
    }


    public void setOnClicks() {
        binding.includedToolbar.imgBack.setOnClickListener(this);
        binding.includedToolbar.imgDelete.setOnClickListener(this);
        binding.includedToolbar.imgOther.setOnClickListener(this);
        binding.linCategory.setOnClickListener(this);
        binding.btnAddEdit.setOnClickListener(this);
        binding.imgEdit.setOnClickListener(this);
        binding.imgAddAnother.setOnClickListener(this);
        binding.imgAddNoData.setOnClickListener(this);
        binding.linEdit.setOnClickListener(this);
        binding.linEditContact.setOnClickListener(this);
        binding.imgExpandLin.setOnClickListener(this);
        binding.imgExpandContact.setOnClickListener(this);
        binding.imgPhone.setOnClickListener(this);
        binding.imgEmail.setOnClickListener(this);
        binding.imgWebsite.setOnClickListener(this);
        binding.imgAddress.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddEdit:
            case R.id.imgAddAnother:
            case R.id.imgAddNoData:
                addItem();
                return;
            case R.id.imgAddress:
                openMap(model.getAddress());
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.imgDelete:
                if (isEdit) {
                    deleteItem();
                    return;
                } else {
                    pickContactPerm();
                    return;
                }
            case R.id.imgEdit:
                if (!model.isEdit()) {
                    model.setEdit(true);
                    return;
                } else {
                    isAddUpdate(false);
                    return;
                }
            case R.id.imgEmail:
                sendEmail(model.getEmailId());
                return;
            case R.id.imgExpandContact:
                model.setExpandedContact(true ^ model.isExpandedContact());
                return;
            case R.id.imgExpandLin:
                model.setExpanded(true ^ model.isExpanded());
                return;
            case R.id.imgOther:
                isAddUpdate(true);
                return;
            case R.id.imgPhone:
                dialPhoneNumber(model.getPhoneNo());
                return;

            case R.id.imgWebsite:
                openUrl(model.getWebSite());
                return;
            case R.id.linCategory:
                showDialogCategoryList();
                return;
            case R.id.linEdit:
            case R.id.linEditContact:
                if (!model.isEdit()) {
                    AppConstants.toastShort(context, "Enable editing ...");
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
        if (isEdit) {
            try {
                binding.etName.setText(model.getName());
                binding.etNote.setText(model.getNote());
                binding.etPhoneNumber.setText(model.getPhoneNo());
                binding.etEmailId.setText(model.getEmailId());
                binding.etWebsite.setText(model.getWebSite());
                binding.etAddress.setText(model.getAddress());
                binding.etEstimatedAmount.setText(AppConstants.getFormattedPrice(model.getExpectedAmount()));
            } catch (NumberFormatException e) {
                binding.etEstimatedAmount.setText(0);
                e.printStackTrace();
            }
        }
    }

    private void setEditTextChange() {
        binding.etEstimatedAmount.addTextChangedListener(new TextWatcher() {
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
        categoryList = new ArrayList<>();
        try {
            categoryList.addAll(db.categoryDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectedCategoryPos = getSelectedPosById();
        categoryList.get(selectedCategoryPos).setSelected(true);
        if (model.getCategoryRowModel() == null) {
            model.setCategoryId(categoryList.get(selectedCategoryPos).getId());
            model.setCategoryRowModel(categoryList.get(selectedCategoryPos));
        }
    }


    private int getSelectedPosById() {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId().equalsIgnoreCase(model.getCategoryId())) {
                return i;
            }
        }
        return 0;
    }

    public void setCategoryListDialog() {
        dialogCategoryListBinding = AlertDialogRecyclerListBinding.inflate(LayoutInflater.from(context),  (ViewGroup) null, false);
        dialogCategoryList = new Dialog(context);
        dialogCategoryList.setContentView(dialogCategoryListBinding.getRoot());
        dialogCategoryList.setCancelable(false);
        dialogCategoryList.getWindow().setBackgroundDrawableResource(17170445);
        dialogCategoryList.getWindow().setLayout(-1, -2);
        dialogCategoryListBinding.txtTitle.setText(R.string.select_category);
        dialogCategoryListBinding.btnOk.setText(R.string.set);
        dialogCategoryListBinding.recycler.setLayoutManager(new LinearLayoutManager(context));
        dialogCategoryListBinding.recycler.setAdapter(new CategoryAdapterLeading(context, false, categoryList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                selectedCategoryPos = i;
                initView();
            }
        }));
        dialogCategoryListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogCategoryList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showNewCatList();
            }
        });
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


    public void selectionAllCategory(boolean z) {
        for (int i = 0; i < categoryList.size(); i++) {
            categoryList.get(i).setSelected(z);
        }
    }

    private void newCatDialogSetup() {
        fillImageList();
        setNewCatDialog();
    }

    private void fillImageList() {
        imageList = new ArrayList<>();
        ImageRowModel imageRowModel = new ImageRowModel();
        imageRowModel.setId(Constants.COST_CAT_TYPE_ATTIRE_ACCESSORIES);
        imageList.add(imageRowModel);
        ImageRowModel imageRowModel2 = new ImageRowModel();
        imageRowModel2.setId(Constants.COST_CAT_TYPE_HEALTH_BEAUTY);
        imageList.add(imageRowModel2);
        ImageRowModel imageRowModel3 = new ImageRowModel();
        imageRowModel3.setId(Constants.COST_CAT_TYPE_MUSIC_SHOW);
        imageList.add(imageRowModel3);
        ImageRowModel imageRowModel4 = new ImageRowModel();
        imageRowModel4.setId(Constants.COST_CAT_TYPE_FLOWER_DECOR);
        imageList.add(imageRowModel4);
        ImageRowModel imageRowModel5 = new ImageRowModel();
        imageRowModel5.setId(Constants.COST_CAT_TYPE_ACCESSORIES);
        imageList.add(imageRowModel5);
        ImageRowModel imageRowModel6 = new ImageRowModel();
        imageRowModel6.setId(Constants.COST_CAT_TYPE_JEWELRY);
        imageList.add(imageRowModel6);
        ImageRowModel imageRowModel7 = new ImageRowModel();
        imageRowModel7.setId(Constants.COST_CAT_TYPE_PHOTO_VIDEO);
        imageList.add(imageRowModel7);
        ImageRowModel imageRowModel8 = new ImageRowModel();
        imageRowModel8.setId(Constants.COST_CAT_TYPE_CEREMONY);
        imageList.add(imageRowModel8);
        ImageRowModel imageRowModel9 = new ImageRowModel();
        imageRowModel9.setId(Constants.COST_CAT_TYPE_RECEPTION);
        imageList.add(imageRowModel9);
        ImageRowModel imageRowModel10 = new ImageRowModel();
        imageRowModel10.setId(Constants.COST_CAT_TYPE_TRANSPORTATION);
        imageList.add(imageRowModel10);
        ImageRowModel imageRowModel11 = new ImageRowModel();
        imageRowModel11.setId(Constants.COST_CAT_TYPE_ACCOMMODATION);
        imageList.add(imageRowModel11);
        ImageRowModel imageRowModel12 = new ImageRowModel();
        imageRowModel12.setId(Constants.COST_CAT_TYPE_MISCELLANEOUS);
        imageList.add(imageRowModel12);
        imageList.get(selectedNewCatPos).setSelected(true);
    }

    public void setNewCatDialog() {
        dialogNewCatBinding = AlertDialogNewCategoryBinding.inflate(LayoutInflater.from(context), (ViewGroup) null, false);
        dialogNewCat = new Dialog(context);
        dialogNewCat.setContentView(dialogNewCatBinding.getRoot());
        dialogNewCat.setCancelable(false);
        dialogNewCat.getWindow().setBackgroundDrawableResource(17170445);
        dialogNewCat.getWindow().setLayout(-1, -2);
        dialogNewCatBinding.txtTitle.setText(R.string.add_new_category);
        dialogNewCatBinding.recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        dialogNewCatBinding.recycler.setAdapter(new ImageAdapterLeading(true, context, imageList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                selectedNewCatPos = i;
                initView();
            }
        }));
        dialogNewCatBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogNewCat.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialogNewCatBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                long j;
                model.setName(binding.etName.getText().toString());
                model.setNote(binding.etNote.getText().toString());
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
            dialogNewCatBinding.recycler.scrollToPosition(selectedNewCatPos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (dialogNewCat != null && !dialogNewCat.isShowing()) {
                dialogNewCat.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public void fillData() {
        setViewVisibility();
    }


    public void setRecycler() {
        binding.recycler.setLayoutManager(new LinearLayoutManager(context));
        binding.recycler.setAdapter(new PaymentAdapterLeading(context, model.getArrayList(), new RecyclerItemClick() {
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
            paymentRowModel.setParentId(model.getId());
            paymentRowModel.setType(2);
            paymentRowModel.setId(AppConstants.getUniqueId());
            paymentRowModel.setDateInMillis(Calendar.getInstance().getTimeInMillis());
            openItemDetail(-1, paymentRowModel, false);
        }
    }


    public void openItemDetail(int i, PaymentRowModel paymentRowModel, boolean z) {
        Intent intent = new Intent(context, AddEditPaymentActivityLeading.class);
        intent.putExtra(AddEditPaymentActivityLeading.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditPaymentActivityLeading.EXTRA_POSITION, i);
        intent.putExtra(AddEditPaymentActivityLeading.EXTRA_MODEL, paymentRowModel);
        startActivityForResult(intent, 1002);
    }

    private void notifyAdapter() {
        setViewVisibility();
        if (binding.recycler.getAdapter() != null) {
            binding.recycler.getAdapter().notifyDataSetChanged();
        }
    }

    private void setViewVisibility() {
        int i = 8;
        binding.linData.setVisibility(model.isListData() ? View.VISIBLE : View.GONE);
        LinearLayout linearLayout = binding.linNoData;
        if (!model.isListData()) {
            i = 0;
        }
        linearLayout.setVisibility(i);
    }

    private boolean isAddUpdate(boolean z) {
        model.setName(binding.etName.getText().toString());
        model.setNote(binding.etNote.getText().toString());
        model.setPhoneNo(binding.etPhoneNumber.getText().toString().trim());
        model.setEmailId(binding.etEmailId.getText().toString().trim());
        model.setWebSite(binding.etWebsite.getText().toString().trim());
        model.setAddress(binding.etAddress.getText().toString().trim());
        if (!isValid()) {
            return false;
        }
        try {
            model.getName().trim();
            model.getPhoneNo().trim();
            model.getEmailId().trim();
            model.getWebSite().trim();
            if (model.getId() != null) {
                db.vendorDao().update(model);
            } else {
                model.setId(AppConstants.getUniqueId());
                db.vendorDao().insert(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.setEdit(false);
        if (!z) {
            return true;
        }
        openItemList(false);
        return true;
    }

    private boolean isValid() {

        if (model.getName().trim().isEmpty()) {
            Context context = this.context;
            EditText editText = binding.etName;
            AppConstants.requestFocusAndError(context, editText, getString(R.string.please_enter) + " " + getString(R.string.name));
            return false;
        } else if (model.getExpectedAmount() > 0.0d) {
            return true;
        } else {
            Context context2 = context;
            EditText editText2 = binding.etEstimatedAmount;
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
        intent.putExtra(EXTRA_MODEL, model);
        setResult(-1, intent);

        super.onBackPressed();
    }

    public void pickContactPerm() {
        if (isHasPermissions(context, "android.permission.READ_CONTACTS")) {
            pickContact();
            return;
        }
        requestPermissions(context, getString(R.string.rationale_contact), 1051, "android.permission.READ_CONTACTS");
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
                                    model.setName(str);
                                    model.setPhoneNo(str2);
                                    model.setEmailId(str3);
                                    model.setAddress(str4);
                                    model.setWebSite(str5);
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
                                    model.setName(str);
                                    model.setPhoneNo(str2);
                                    model.setEmailId(str3);
                                    model.setAddress(str4);
                                    model.setWebSite(str5);
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
                                    model.setName(str);
                                    model.setPhoneNo(str2);
                                    model.setEmailId(str3);
                                    model.setAddress(str4);
                                    model.setWebSite(str5);
                                }
                                model.setName(str);
                                model.setPhoneNo(str2);
                                model.setEmailId(str3);
                                model.setAddress(str4);
                                model.setWebSite(str5);
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
                model.setName(str);
                model.setPhoneNo(str2);
                model.setEmailId(str3);
                model.setAddress(str4);
                model.setWebSite(str5);
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
                model.setName(str);
                model.setPhoneNo(str2);
                model.setEmailId(str3);
                model.setAddress(str4);
                model.setWebSite(str5);
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
                model.setName(str);
                model.setPhoneNo(str2);
                model.setEmailId(str3);
                model.setAddress(str4);
                model.setWebSite(str5);
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

                model.setName(str);
                model.setPhoneNo(str2);
                model.setEmailId(str3);
                model.setAddress(str4);
                model.setWebSite(str5);
            }
        } else {
            str = "";
        }
        model.setName(str);
        model.setPhoneNo(str2);
        model.setEmailId(str3);
        model.setAddress(str4);
        model.setWebSite(str5);
    }

    private void updateList(Intent intent) {
        if (intent != null) {
            try {
                if (intent.hasExtra(EXTRA_MODEL)) {
                    PaymentRowModel paymentRowModel = (PaymentRowModel) intent.getParcelableExtra(EXTRA_MODEL);
                    if (intent.getBooleanExtra(EXTRA_IS_DELETED, false)) {
                        model.getArrayList().remove(intent.getIntExtra(EXTRA_POSITION, 0));
                    } else if (intent.getBooleanExtra(EXTRA_IS_EDIT, false)) {
                        model.getArrayList().set(intent.getIntExtra(EXTRA_POSITION, 0), paymentRowModel);
                    } else {
                        model.getArrayList().add(paymentRowModel);
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
            model.setPendingAmount(db.paymentDao().getTotal(model.getId(), 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            model.setPaidAmount(db.paymentDao().getTotal(model.getId(), 0));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        isUpdateList = true;
    }

    public void onBackPressed() {
        if (isUpdateList) {
            openItemList(false);
        } else {
            super.onBackPressed();
        }
    }

    public void dialPhoneNumber(String str) {
        if (str == null || str.trim().length() <= 0) {
            AppConstants.toastShort(context, "Phone number not found");
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
            AppConstants.toastShort(context, "Email id not found");
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
            AppConstants.toastShort(context, "Website not found");
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
            AppConstants.toastShort(context, "Address not found");
            return;
        }
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google.co.in/maps?q=" + str)));
        } catch (Exception e) {
            e.printStackTrace();
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
        new BackgroundAsync(context, true, "", new OnAsyncBackground() {
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
        document = new Document(PageSize.A4, 16.0f, 16.0f, 16.0f, 16.0f);
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.REPORT_DIRECTORY);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            fileName = repoType + "_" + repoTitle + "_" + getCurrentDateTime() + ".pdf";
            try {
                writer = PdfWriter.getInstance(document, new FileOutputStream(new File(dir, fileName)));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
    }


    public void fillDocData() {
        paragraph = new Paragraph((repoType + " " + repoTitle).toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 18.0f, 1));
        paragraph.setAlignment(1);
        paragraph.add((Element) new Paragraph(" "));
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        paragraph = new Paragraph("");
        paragraph.add((Element) new Paragraph(" "));
        try {
            document.add(paragraph);
        } catch (DocumentException e2) {
            e2.printStackTrace();
        }
        VendorRowModel vendorRowModel = model;
        Paragraph paragraph2 = paragraph;
        paragraph2.add((Element) new Paragraph("Name : " + vendorRowModel.getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph3 = paragraph;
        paragraph3.add((Element) new Paragraph("Category : " + vendorRowModel.getCategoryRowModel().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph4 = paragraph;
        paragraph4.add((Element) new Paragraph("Status : " + vendorRowModel.getStatusText(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph5 = paragraph;
        paragraph5.add((Element) new Paragraph("Estimated amount : " + vendorRowModel.getExpectedAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph6 = paragraph;
        paragraph6.add((Element) new Paragraph("Balance : " + vendorRowModel.getBalanceFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph7 = paragraph;
        paragraph7.add((Element) new Paragraph("Paid : " + vendorRowModel.getPaidAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph8 = paragraph;
        paragraph8.add((Element) new Paragraph("Pending : " + vendorRowModel.getPendingAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        String note = vendorRowModel.getNote();
        if (!note.isEmpty()) {
            Paragraph paragraph9 = paragraph;
            paragraph9.add((Element) new Paragraph("Note : " + note, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String phoneNo = vendorRowModel.getPhoneNo();
        if (!phoneNo.isEmpty()) {
            Paragraph paragraph10 = paragraph;
            paragraph10.add((Element) new Paragraph("Phone No : " + phoneNo, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String emailId = vendorRowModel.getEmailId();
        if (!emailId.isEmpty()) {
            Paragraph paragraph11 = paragraph;
            paragraph11.add((Element) new Paragraph("Email Id : " + emailId, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String webSite = vendorRowModel.getWebSite();
        if (!webSite.isEmpty()) {
            Paragraph paragraph12 = paragraph;
            paragraph12.add((Element) new Paragraph("WebSite : " + webSite, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String address = vendorRowModel.getAddress();
        if (!address.isEmpty()) {
            Paragraph paragraph13 = paragraph;
            paragraph13.add((Element) new Paragraph("Address : " + address, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        paragraph.setAlignment(0);
        try {
            document.add(paragraph);
        } catch (DocumentException e3) {
            e3.printStackTrace();
        }
        if (vendorRowModel.getArrayList() != null && vendorRowModel.getArrayList().size() > 0) {
            paragraph = new Paragraph(subTitle.toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, 0));
            paragraph.add((Element) new Paragraph(" "));
            paragraph.setAlignment(1);
            try {
                document.add(paragraph);
            } catch (DocumentException e4) {
                e4.printStackTrace();
            }
            try {
                document.add(getTable(vendorRowModel.getArrayList()));
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
        new FooterPageEvent().onEndPage(writer, document);
        try {
            document.close();

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
}
