package com.msint.weddingplanner.appBase.view;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.guest.GuestRowModel;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.msint.weddingplanner.databinding.ActivityGuestAddEditBinding;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddEditCompanionsActivity extends BaseActivityRecyclerBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_ID = "id";
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    private ActivityGuestAddEditBinding binding;


    /* renamed from: db */
    public AppDataBase f540db;
    private boolean isEdit = false;
    private boolean isUpdateList;

    public GuestRowModel model;
    public ToolbarModel toolbarModel;

    /* access modifiers changed from: protected */
    public void callApi() {
    }

    /* access modifiers changed from: protected */
    public void fillData() {
    }

    /* access modifiers changed from: protected */
    public void initMethods() {
    }

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }

    /* access modifiers changed from: protected */
    public void setRecycler() {
    }

    /* access modifiers changed from: protected */
    public void setBinding() {
        this.binding = (ActivityGuestAddEditBinding) DataBindingUtil.setContentView(this, R.layout.activity_guest_add_edit);
        this.f540db = AppDataBase.getAppDatabase(this);
        setModelDetail();
        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        boolean z = false;
        if (getIntent().hasExtra(EXTRA_IS_EDIT) && getIntent().getBooleanExtra(EXTRA_IS_EDIT, false)) {
            z = true;
        }
        this.isEdit = z;
        try {
            this.model = (GuestRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(this.isEdit ? "Edit Companion" : "Add Companion");
        this.toolbarModel.setDelete(true);
        this.binding.includedToolbar.imgDelete.setImageResource(this.isEdit ? R.drawable.delete : R.drawable.phone_book);
        this.toolbarModel.setOtherMenu(true);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
        this.binding.includedToolbar.setModel(this.toolbarModel);
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
                    AddEditCompanionsActivity.this.f540db.paymentDao().deleteAll(AddEditCompanionsActivity.this.model.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    AddEditCompanionsActivity.this.f540db.guestDao().delete(AddEditCompanionsActivity.this.model);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                AddEditCompanionsActivity.this.openItemList(true);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
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

    private boolean isAddUpdate(boolean z) {
        if (!isValid()) {
            return false;
        }
        try {
            this.model.getName().trim();
            this.model.getPhoneNo().trim();
            this.model.getEmailId().trim();
            if (this.isEdit) {
                this.f540db.guestDao().update(this.model);
            } else {
                this.f540db.guestDao().insert(this.model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        intent.putExtra(EXTRA_MODEL, this.model);
        setResult(-1, intent);
        finish();
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
        }
    }

    public void onPermissionsDenied(int i, @NonNull List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied((Activity) this, list)) {
            new AppSettingsDialog.Builder((Activity) this).build().show();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1101) {
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
                e = e5;
                e.printStackTrace();
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
                e = e6;
                e.printStackTrace();
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
                e = e7;
                e.printStackTrace();
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
}
