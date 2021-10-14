package com.example.weddingplanner.appBase.view;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.weddingplanner.appBase.adapter.CompanionAdapter;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.example.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.roomsDB.guest.GuestRowModel;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.appBase.utils.BackgroundAsync;
import com.example.weddingplanner.appBase.utils.Constants;
import com.example.weddingplanner.appBase.utils.OnAsyncBackground;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.example.weddingplanner.databinding.ActivityGuestAddEditBinding;
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

public class AddEditGuestActivity extends BaseActivityRecyclerBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_ID = "id";
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    public static String EXTRA_POSITION_MAIN = "positionMain";
    private ActivityGuestAddEditBinding binding;



    public AppDataBase db;
    private File dir;
    private Document document;
    private String fileName = null;
    private boolean isEdit = false;
    private boolean isUpdateList;

    public GuestRowModel model;
    private Paragraph paragraph;
    private String repoTitle = "Detail";
    private String repoType = "Guest";
    private String subTitle = "Companions";
    public ToolbarModel toolbarModel;
    private PdfWriter writer = null;


    public void callApi() {
    }


    public void initMethods() {
    }

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }


    public void setBinding() {
        binding = ActivityGuestAddEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.db = AppDataBase.getAppDatabase(this);
        setModelDetail();
//        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        this.model = new GuestRowModel();
        this.model.setEdit(true);
        isEdit = getIntent().getBooleanExtra(EXTRA_IS_EDIT,false);

        if (this.isEdit) {
            this.model = (GuestRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
            binding.etName.setText(model.getName());
            return;
        }

        this.model.setExpandedContact(true);
        this.model.setArrayList(new ArrayList());
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.isEdit ? "Edit Guest" : "Add Guest");
        this.toolbarModel.setDelete(true);
        this.binding.includedToolbar.imgDelete.setImageResource(this.isEdit ? R.drawable.delete : R.drawable.phone_book);
        this.toolbarModel.setOtherMenu(true);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
        binding.includedToolbar.textTitle.setText(this.isEdit ? "Edit Guest" : "Add Guest");
        this.toolbarModel.setShare(this.isEdit);
//        this.binding.includedToolbar.setModel(this.toolbarModel);

        binding.includedToolbar.imgAdd.setVisibility(View.GONE);
        binding.includedToolbar.imgDrawer.setVisibility(View.GONE);
        binding.includedToolbar.search.setVisibility(View.GONE);
        binding.includedToolbar.progressbar.setVisibility(View.GONE);
        binding.includedToolbar.etOther.setVisibility(View.GONE);
        binding.includedToolbar.spinner.setVisibility(View.GONE);
        binding.includedToolbar.imageHome.setVisibility(View.GONE);
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
                    AddEditGuestActivity.this.db.guestDao().deleteAllComp(AppPref.getCurrentEvenId(AddEditGuestActivity.this.context), AddEditGuestActivity.this.model.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    AddEditGuestActivity.this.db.guestDao().delete(AddEditGuestActivity.this.model);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                AddEditGuestActivity.this.openItemList(true);
            }
        });
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.includedToolbar.imgShare.setOnClickListener(this);
        this.binding.btnAddEdit.setOnClickListener(this);
        this.binding.imgEdit.setOnClickListener(this);
        this.binding.imgAdd.setOnClickListener(this);
        this.binding.imgAddNoData.setOnClickListener(this);
        this.binding.linEdit.setOnClickListener(this);
        this.binding.linEditContact.setOnClickListener(this);
        this.binding.imgExpandContact.setOnClickListener(this);
        this.binding.imgPhone.setOnClickListener(this);
        this.binding.imgEmail.setOnClickListener(this);
        this.binding.imgAddress.setOnClickListener(this);
        this.binding.txtMale.setOnClickListener(this);
        this.binding.txtFemale.setOnClickListener(this);
        this.binding.txtAdult.setOnClickListener(this);
        this.binding.txtBaby.setOnClickListener(this);
        this.binding.txtChild.setOnClickListener(this);
        this.binding.txtInvitationSent.setOnClickListener(this);
        this.binding.txtInvitationNotSent.setOnClickListener(this);
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
                this.model.setExpandedContact(!this.model.isExpandedContact());
                return;
            case R.id.imgExpandLin:
                this.model.setExpanded(!this.model.isExpanded());
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
            case R.id.linEdit:
            case R.id.linEditContact:
                if (!this.model.isEdit()) {
                    AppConstants.toastShort(this.context, "Enable editing ...");
                    return;
                }
                return;
            case R.id.txtAdult:
                this.model.setStageType(1);
                return;
            case R.id.txtBaby:
                this.model.setStageType(2);
                return;
            case R.id.txtChild:
                this.model.setStageType(3);
                return;
            case R.id.txtFemale:
                this.model.setGenderType(2);
                return;
            case R.id.txtInvitationNotSent:
                this.model.setInvitationSent(false);
                return;
            case R.id.txtInvitationSent:
                this.model.setInvitationSent(true);
                return;
            case R.id.txtMale:
                this.model.setGenderType(1);
                return;
            default:
                return;
        }
    }

    private void pickContact() {
        startActivityForResult(new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI), Constants.REQUEST_PICK_CONTACT);
    }


    public void fillData() {
        setViewVisibility();
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new CompanionAdapter(this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    AddEditGuestActivity.this.updateTotal();
                } else {
                    AddEditGuestActivity.this.openItemDetail(i, AddEditGuestActivity.this.model.getArrayList().get(i), true);
                }
            }
        }));
    }

    private void addItem() {
        if (isAddUpdate(false)) {
            GuestRowModel guestRowModel = new GuestRowModel();
            guestRowModel.setCompanion(true);
            guestRowModel.setGuestId(this.model.getId());
            guestRowModel.setId(AppConstants.getUniqueId());
            guestRowModel.setEdit(true);
            guestRowModel.setExpandedContact(true);
            openItemDetail(-1, guestRowModel, false);
        }
    }


    public void openItemDetail(int i, GuestRowModel guestRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditCompanionsActivity.class);
        intent.putExtra(AddEditPaymentActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditPaymentActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditPaymentActivity.EXTRA_MODEL, guestRowModel);
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
        if (!isValid()) {
            return false;
        }
        try {
            this.model.getName().trim();
            this.model.getPhoneNo().trim();
            this.model.getEmailId().trim();
            if (this.model.getId() != null) {
                this.db.guestDao().update(this.model);
            } else {
                this.model.setId(AppConstants.getUniqueId());
                this.db.guestDao().insert(this.model);
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
        intent.putExtra(EXTRA_POSITION_MAIN, getIntent().getIntExtra(EXTRA_POSITION_MAIN, 0));
        intent.putExtra(EXTRA_MODEL, this.model);
        setResult(-1, intent);
        onBackPressed();
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
        Cursor query;
        Cursor query2;
        String string;
        Cursor query3;
        String string2;
        String str = "";
        String str2 = "";
        String str3 = "";
        String str4 = "";
        Cursor managedQuery = managedQuery(intent.getData(), (String[]) null, (String) null, (String[]) null, (String) null);
        if (managedQuery.moveToFirst()) {
            String string3 = managedQuery.getString(managedQuery.getColumnIndexOrThrow("_id"));
            String string4 = managedQuery.getString(managedQuery.getColumnIndex("display_name"));
            try {
                if (Integer.valueOf(managedQuery.getInt(managedQuery.getColumnIndex("has_phone_number"))).intValue() > 0) {
                    ContentResolver contentResolver = getContentResolver();
                    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    Cursor query4 = contentResolver.query(uri, (String[]) null, "contact_id = " + string3, (String[]) null, (String) null);
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
                                query3 = contentResolver2.query(uri2, (String[]) null, "contact_id = " + string3, (String[]) null, (String) null);
                                string2 = query3.getString(query3.getColumnIndex("data1"));
                                try {
                                    query3.close();
                                    str3 = string2;
                                } catch (Exception e2) {
                                    e = e2;
                                    str3 = string2;
                                    e.printStackTrace();
                                    ContentResolver contentResolver3 = getContentResolver();
                                    Uri uri3 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                                    query2 = contentResolver3.query(uri3, (String[]) null, "contact_id=" + string3, (String[]) null, (String) null);
                                    string = query2.getString(query2.getColumnIndex("data1"));
                                    query2.close();
                                    str4 = string;
                                    ContentResolver contentResolver4 = getContentResolver();
                                    Uri uri4 = ContactsContract.Data.CONTENT_URI;
                                    query = contentResolver4.query(uri4, new String[]{"data1", "data2"}, "contact_id = " + string3 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                                    query.getString(query.getColumnIndex("data1"));
                                    query.close();
                                    str = string4;
                                    this.model.setName(str);
                                    this.model.setPhoneNo(str2);
                                    this.model.setEmailId(str3);
                                    this.model.setAddress(str4);
                                }
                                ContentResolver contentResolver32 = getContentResolver();
                                Uri uri32 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                                query2 = contentResolver32.query(uri32, (String[]) null, "contact_id=" + string3, (String[]) null, (String) null);
                                string = query2.getString(query2.getColumnIndex("data1"));
                                try {
                                    query2.close();
                                    str4 = string;
                                } catch (Exception e3) {
                                    e = e3;
                                    str4 = string;
                                    e.printStackTrace();
                                    ContentResolver contentResolver42 = getContentResolver();
                                    Uri uri42 = ContactsContract.Data.CONTENT_URI;
                                    query = contentResolver42.query(uri42, new String[]{"data1", "data2"}, "contact_id = " + string3 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                                    query.getString(query.getColumnIndex("data1"));
                                    query.close();
                                    str = string4;
                                    this.model.setName(str);
                                    this.model.setPhoneNo(str2);
                                    this.model.setEmailId(str3);
                                    this.model.setAddress(str4);
                                }
                                ContentResolver contentResolver422 = getContentResolver();
                                Uri uri422 = ContactsContract.Data.CONTENT_URI;
                                query = contentResolver422.query(uri422, new String[]{"data1", "data2"}, "contact_id = " + string3 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                                query.getString(query.getColumnIndex("data1"));
                                query.close();
                                str = string4;
                                this.model.setName(str);
                                this.model.setPhoneNo(str2);
                                this.model.setEmailId(str3);
                                this.model.setAddress(str4);
                            } catch (Exception e4) {
                                e4.printStackTrace();
                                return;
                            }
                        }
                    }
                }
            } catch (Exception e5) {
                ContentResolver contentResolver22 = getContentResolver();
                Uri uri22 = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                query3 = contentResolver22.query(uri22, (String[]) null, "contact_id = " + string3, (String[]) null, (String) null);
                string2 = query3.getString(query3.getColumnIndex("data1"));
                query3.close();
                str3 = string2;
                ContentResolver contentResolver322 = getContentResolver();
                Uri uri322 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                query2 = contentResolver322.query(uri322, (String[]) null, "contact_id=" + string3, (String[]) null, (String) null);
                string = query2.getString(query2.getColumnIndex("data1"));
                query2.close();
                str4 = string;
                ContentResolver contentResolver4222 = getContentResolver();
                Uri uri4222 = ContactsContract.Data.CONTENT_URI;
                query = contentResolver4222.query(uri4222, new String[]{"data1", "data2"}, "contact_id = " + string3 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                query.getString(query.getColumnIndex("data1"));
                query.close();
                str = string4;
                this.model.setName(str);
                this.model.setPhoneNo(str2);
                this.model.setEmailId(str3);
                this.model.setAddress(str4);
            }
            try {
                ContentResolver contentResolver222 = getContentResolver();
                Uri uri222 = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                query3 = contentResolver222.query(uri222, (String[]) null, "contact_id = " + string3, (String[]) null, (String) null);
                if (query3 != null && query3.moveToFirst()) {
                    string2 = query3.getString(query3.getColumnIndex("data1"));
                    query3.close();
                    str3 = string2;
                }
            } catch (Exception e6) {

                ContentResolver contentResolver3222 = getContentResolver();
                Uri uri3222 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                query2 = contentResolver3222.query(uri3222, (String[]) null, "contact_id=" + string3, (String[]) null, (String) null);
                string = query2.getString(query2.getColumnIndex("data1"));
                query2.close();
                str4 = string;
                ContentResolver contentResolver42222 = getContentResolver();
                Uri uri42222 = ContactsContract.Data.CONTENT_URI;
                query = contentResolver42222.query(uri42222, new String[]{"data1", "data2"}, "contact_id = " + string3 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                query.getString(query.getColumnIndex("data1"));
                query.close();
                str = string4;
                this.model.setName(str);
                this.model.setPhoneNo(str2);
                this.model.setEmailId(str3);
                this.model.setAddress(str4);
            }
            try {
                ContentResolver contentResolver32222 = getContentResolver();
                Uri uri32222 = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                query2 = contentResolver32222.query(uri32222, (String[]) null, "contact_id=" + string3, (String[]) null, (String) null);
                if (query2 != null && query2.moveToFirst()) {
                    string = query2.getString(query2.getColumnIndex("data1"));
                    query2.close();
                    str4 = string;
                }
            } catch (Exception e7) {

                ContentResolver contentResolver422222 = getContentResolver();
                Uri uri422222 = ContactsContract.Data.CONTENT_URI;
                query = contentResolver422222.query(uri422222, new String[]{"data1", "data2"}, "contact_id = " + string3 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                query.getString(query.getColumnIndex("data1"));
                query.close();
                str = string4;
                this.model.setName(str);
                this.model.setPhoneNo(str2);
                this.model.setEmailId(str3);
                this.model.setAddress(str4);
            }
            try {
                ContentResolver contentResolver4222222 = getContentResolver();
                Uri uri4222222 = ContactsContract.Data.CONTENT_URI;
                query = contentResolver4222222.query(uri4222222, new String[]{"data1", "data2"}, "contact_id = " + string3 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                if (query != null && query.moveToFirst()) {
                    query.getString(query.getColumnIndex("data1"));
                    query.close();
                }
            } catch (Exception e8) {
                e8.printStackTrace();
            }
            str = string4;
        }
        this.model.setName(str);
        this.model.setPhoneNo(str2);
        this.model.setEmailId(str3);
        this.model.setAddress(str4);
    }

    private void updateList(Intent intent) {
        if (intent != null) {
            try {
                if (intent.hasExtra(EXTRA_MODEL)) {
                    GuestRowModel guestRowModel = (GuestRowModel) intent.getParcelableExtra(EXTRA_MODEL);
                    if (intent.getBooleanExtra(EXTRA_IS_DELETED, false)) {
                        this.model.getArrayList().remove(intent.getIntExtra(EXTRA_POSITION, 0));
                    } else if (intent.getBooleanExtra(EXTRA_IS_EDIT, false)) {
                        this.model.getArrayList().set(intent.getIntExtra(EXTRA_POSITION, 0), guestRowModel);
                    } else {
                        this.model.getArrayList().add(guestRowModel);
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
        this.model.setCompanions((long) this.model.getArrayList().size());
        this.isUpdateList = true;
    }

    public void onBackPressed() {
        if (this.isUpdateList) {
            openItemList(false);
        } else if (this.isEdit) {
            super.onBackPressed();
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
                AddEditGuestActivity.this.savePdf();
            }

            public void onCancel() {
                AddEditGuestActivity.this.openReportList();
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
                AddEditGuestActivity.this.initDoc();
            }

            public void doInBackground() {
                AddEditGuestActivity.this.fillDocData();
            }

            public void onPostExecute() {
                AddEditGuestActivity.this.addingDocFooter();
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
        GuestRowModel guestRowModel = this.model;
        this.paragraph = new Paragraph("Name : " + guestRowModel.getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1));
        Paragraph paragraph2 = this.paragraph;
        StringBuilder sb = new StringBuilder();
        sb.append("Gender : ");
        sb.append(guestRowModel.getGenderType() == 1 ? "Male" : "Female");
        paragraph2.add((Element) new Paragraph(sb.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph3 = this.paragraph;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Age stage : ");
        sb2.append(guestRowModel.getStageType() == 1 ? "Adult" : guestRowModel.getStageType() == 2 ? "Baby" : "Child");
        paragraph3.add((Element) new Paragraph(sb2.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        Paragraph paragraph4 = this.paragraph;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Invitation : ");
        sb3.append(guestRowModel.isInvitationSent() ? "Sent" : "Not send");
        paragraph4.add((Element) new Paragraph(sb3.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        String note = guestRowModel.getNote();
        if (!note.isEmpty()) {
            Paragraph paragraph5 = this.paragraph;
            paragraph5.add((Element) new Paragraph("Note : " + note, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String phoneNo = guestRowModel.getPhoneNo();
        if (!phoneNo.isEmpty()) {
            Paragraph paragraph6 = this.paragraph;
            paragraph6.add((Element) new Paragraph("Phone No : " + phoneNo, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String emailId = guestRowModel.getEmailId();
        if (!emailId.isEmpty()) {
            Paragraph paragraph7 = this.paragraph;
            paragraph7.add((Element) new Paragraph("Email Id : " + emailId, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        String address = guestRowModel.getAddress();
        if (!address.isEmpty()) {
            Paragraph paragraph8 = this.paragraph;
            paragraph8.add((Element) new Paragraph("Address : " + address, new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
        }
        this.paragraph.setAlignment(0);
        try {
            this.document.add(this.paragraph);
        } catch (DocumentException e3) {
            e3.printStackTrace();
        }
        if (guestRowModel.getArrayList() != null && guestRowModel.getArrayList().size() > 0) {
            this.paragraph = new Paragraph(this.subTitle.toUpperCase(), new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, 0));
            this.paragraph.add((Element) new Paragraph(" "));
            this.paragraph.setAlignment(1);
            try {
                this.document.add(this.paragraph);
            } catch (DocumentException e4) {
                e4.printStackTrace();
            }
            try {
                this.document.add(getTable(guestRowModel.getArrayList()));
            } catch (DocumentException e5) {
                e5.printStackTrace();
            }
        }
    }

    private PdfPTable getTable(ArrayList<GuestRowModel> arrayList) {
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

    private ArrayList<ReportRowModel> fillSubList(ArrayList<GuestRowModel> arrayList) {
        ArrayList<ReportRowModel> arrayList2 = new ArrayList<>();
        if (arrayList.size() <= 0) {
            return arrayList2;
        }
        arrayList2.clear();
        ReportRowModel reportRowModel = new ReportRowModel();
        reportRowModel.setValueList(new ArrayList());
        reportRowModel.getValueList().add("Name");
        reportRowModel.getValueList().add("Gender");
        reportRowModel.getValueList().add("Age stage");
        reportRowModel.getValueList().add("Notes");
        reportRowModel.getValueList().add("Phone No");
        reportRowModel.getValueList().add("Email Id");
        reportRowModel.getValueList().add("Address");
        arrayList2.add(reportRowModel);
        for (int i = 0; i < arrayList.size(); i++) {
            ReportRowModel reportRowModel2 = new ReportRowModel();
            reportRowModel2.setValueList(new ArrayList());
            reportRowModel2.getValueList().add(arrayList.get(i).getName());
            reportRowModel2.getValueList().add(arrayList.get(i).getGenderType() == 1 ? "Male" : "Female");
            reportRowModel2.getValueList().add(arrayList.get(i).getStageType() == 1 ? "Adult" : arrayList.get(i).getStageType() == 2 ? "Baby" : "Child");
            reportRowModel2.getValueList().add(arrayList.get(i).getNote());
            reportRowModel2.getValueList().add(arrayList.get(i).getPhoneNo());
            reportRowModel2.getValueList().add(arrayList.get(i).getEmailId());
            reportRowModel2.getValueList().add(arrayList.get(i).getAddress());
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
                ColumnText.showTextAligned(directContent, 1, new Phrase("Created by : " + AddEditGuestActivity.this.getString(R.string.app_name), new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, 1)), document.leftMargin() + ((document.right() - document.left()) / 2.0f), document.bottom() + 10.0f, 0.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getCurrentDateTime() {
        return new SimpleDateFormat("dd_MM_yyyy_hh:mm:ss a").format(new Date(Calendar.getInstance().getTimeInMillis()));
    }
}
