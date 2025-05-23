package com.msint.weddingplanner.appBase.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.msint.weddingplanner.appBase.models.profile.ProfileRowModel;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.BackgroundAsync;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.OnAsyncBackground;
import com.msint.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.msint.weddingplanner.databinding.ActivityProfileAddEditBinding;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class AddEditProfileActivity extends BaseActivityRecyclerBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_ID = "id";
    public static String EXTRA_IS_DELETED = "isDeleted";
    public static String EXTRA_IS_EDIT = "isEdit";
    public static String EXTRA_MODEL = "model";
    public static String EXTRA_POSITION = "position";
    private ActivityProfileAddEditBinding binding;
    
    public Calendar calendar;
    

    /* renamed from: db */
    public AppDataBase f544db;
    private boolean isEdit = false;
    
    public ProfileRowModel model;
    public ToolbarModel toolbarModel;

    /* access modifiers changed from: protected */
    public void callApi() {
    }

    /* access modifiers changed from: protected */
    public void fillData() {
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
        this.binding = (ActivityProfileAddEditBinding) DataBindingUtil.setContentView(this, R.layout.activity_profile_add_edit);
        this.f544db = AppDataBase.getAppDatabase(this.context);
        setModelDetail();
        this.binding.setRowModel(this.model);
    }

    private void setModelDetail() {
        this.isEdit = getIntent().hasExtra(EXTRA_IS_EDIT) && getIntent().getBooleanExtra(EXTRA_IS_EDIT, false);
        if (this.isEdit) {
            this.model = (ProfileRowModel) getIntent().getParcelableExtra(EXTRA_MODEL);
        } else {
            this.model = new ProfileRowModel();
            this.model.setId(AppConstants.getUniqueId());
            this.model.setSelected(true);
        }
        this.calendar = Calendar.getInstance();
        if (this.model.getDateTimeInMillis() > 0) {
            this.calendar.setTimeInMillis(this.model.getDateTimeInMillis());
        } else {
            this.calendar.add(2, 1);
            this.model.setDateTimeInMillis(this.calendar.getTimeInMillis());
        }
        this.calendar.set(13, 0);
        this.calendar.set(14, 0);
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle("Profile Details");
        this.toolbarModel.setOtherMenu(true);
        this.toolbarModel.setDelete(this.isEdit);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.save);
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        menu.findItem(R.id.delete).setVisible(this.isEdit);
        return true;
    }

    public void deleteItem() {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>" + this.model.getWeddingName() + "</b> <br />" + getString(R.string.delete_all_related_data) + " marriage", true, true, getString(R.string.delete), getString(R.string.cancel), new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                AddEditProfileActivity.this.deleteProfile();
            }
        });
    }

    
    public void deleteProfile() {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                ArrayList arrayList = new ArrayList();
                try {
                    arrayList.addAll(AddEditProfileActivity.this.f544db.taskDao().getAllMarriage(AddEditProfileActivity.this.model.getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    try {
                        AddEditProfileActivity.this.f544db.subTaskDao().deleteAll((String) arrayList.get(i));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    try {
                        AddEditProfileActivity.this.f544db.taskDao().delete(AddEditProfileActivity.this.model.getId(), (String) arrayList.get(i));
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                ArrayList arrayList2 = new ArrayList();
                try {
                    arrayList2.addAll(AddEditProfileActivity.this.f544db.guestDao().getAllMarriage(AddEditProfileActivity.this.model.getId()));
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    try {
                        AddEditProfileActivity.this.f544db.guestDao().deleteAllComp(AddEditProfileActivity.this.model.getId(), (String) arrayList2.get(i2));
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    try {
                        AddEditProfileActivity.this.f544db.guestDao().delete(AddEditProfileActivity.this.model.getId(), (String) arrayList2.get(i2));
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
                ArrayList arrayList3 = new ArrayList();
                try {
                    arrayList3.addAll(AddEditProfileActivity.this.f544db.costDao().getAllMarriage(AddEditProfileActivity.this.model.getId()));
                } catch (Exception e7) {
                    e7.printStackTrace();
                }
                for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                    try {
                        AddEditProfileActivity.this.f544db.paymentDao().deleteAll((String) arrayList3.get(i3));
                    } catch (Exception e8) {
                        e8.printStackTrace();
                    }
                    try {
                        AddEditProfileActivity.this.f544db.costDao().delete(AddEditProfileActivity.this.model.getId(), (String) arrayList3.get(i3));
                    } catch (Exception e9) {
                        e9.printStackTrace();
                    }
                }
                ArrayList arrayList4 = new ArrayList();
                try {
                    arrayList4.addAll(AddEditProfileActivity.this.f544db.vendorDao().getAllMarriage(AddEditProfileActivity.this.model.getId()));
                } catch (Exception e10) {
                    e10.printStackTrace();
                }
                for (int i4 = 0; i4 < arrayList4.size(); i4++) {
                    try {
                        AddEditProfileActivity.this.f544db.paymentDao().deleteAll((String) arrayList4.get(i4));
                    } catch (Exception e11) {
                        e11.printStackTrace();
                    }
                    try {
                        AddEditProfileActivity.this.f544db.vendorDao().delete(AddEditProfileActivity.this.model.getId(), (String) arrayList4.get(i4));
                    } catch (Exception e12) {
                        e12.printStackTrace();
                    }
                }
            }

            public void onPostExecute() {
                try {
                    AddEditProfileActivity.this.f544db.profileDao().delete(AddEditProfileActivity.this.model);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AddEditProfileActivity.this.openItemList(true);
            }
        }).execute(new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.includedToolbar.imgDelete.setOnClickListener(this);
        this.binding.btnAddEdit.setOnClickListener(this);
        this.binding.imgPickOwn.setOnClickListener(this);
        this.binding.txtMale.setOnClickListener(this);
        this.binding.txtFemale.setOnClickListener(this);
        this.binding.imgPhone.setOnClickListener(this);
        this.binding.imgEmail.setOnClickListener(this);
        this.binding.imgAddress.setOnClickListener(this);
        this.binding.imgPickPartner.setOnClickListener(this);
        this.binding.txtMalePartner.setOnClickListener(this);
        this.binding.txtFemalePartner.setOnClickListener(this);
        this.binding.imgPhonePartner.setOnClickListener(this);
        this.binding.imgEmailPartner.setOnClickListener(this);
        this.binding.imgAddressPartner.setOnClickListener(this);
        this.binding.txtDate.setOnClickListener(this);
        this.binding.txtTime.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAddress:
                openMap(this.model.getAddress());
                return;
            case R.id.imgAddressPartner:
                openMap(this.model.getPartnerAddress());
                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.imgDelete:
                deleteItem();
                return;
            case R.id.imgEmail:
                sendEmail(this.model.getEmailId());
                return;
            case R.id.imgEmailPartner:
                sendEmail(this.model.getPartnerEmailId());
                return;
            case R.id.imgOther:
                addUpdate();
                return;
            case R.id.imgPhone:
                dialPhoneNumber(this.model.getPhoneNo());
                return;
            case R.id.imgPhonePartner:
                dialPhoneNumber(this.model.getPartnerPhoneNo());
                return;
            case R.id.imgPickOwn:
                pickContactPerm(false);
                return;
            case R.id.imgPickPartner:
                pickContactPerm(true);
                return;
            case R.id.txtDate:
                showDatePickerDialog();
                return;
            case R.id.txtFemale:
                this.model.setGenderType(2);
                setWeddingName();
                return;
            case R.id.txtFemalePartner:
                this.model.setPartnerGenderType(2);
                setWeddingName();
                return;
            case R.id.txtMale:
                this.model.setGenderType(1);
                setWeddingName();
                return;
            case R.id.txtMalePartner:
                this.model.setPartnerGenderType(1);
                setWeddingName();
                return;
            case R.id.txtTime:
                showTimePickerDialog();
                return;
            default:
                return;
        }
    }

    
    public void setWeddingName() {
        String str = "";
        if (this.model.getName().trim().length() > 0 && this.model.getPartnerName().trim().length() > 0) {
            if (this.model.getGenderType() == 1) {
                str = (str + this.model.getName()) + " & " + this.model.getPartnerName();
            } else {
                str = (str + this.model.getPartnerName()) + " & " + this.model.getName();
            }
        }
        this.model.setWeddingName(str);
    }

    private void pickContact(boolean z) {
        startActivityForResult(new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI), z ? Constants.REQUEST_PICK_CONTACT_PARTNER : Constants.REQUEST_PICK_CONTACT);
    }

    /* access modifiers changed from: protected */
    public void initMethods() {
        setWeddingName();
        setEditTextValue();
        setEditTextChange();
    }

    private void setEditTextValue() {
        try {
            this.binding.etBudget.setText(AppConstants.getFormattedPrice(this.model.getBudget()));
        } catch (NumberFormatException e) {
            this.binding.etBudget.setText(0);
            e.printStackTrace();
        }
    }

    private void setEditTextChange() {
        this.binding.etName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                AddEditProfileActivity.this.setWeddingName();
            }
        });
        this.binding.etNamePartner.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                AddEditProfileActivity.this.setWeddingName();
            }
        });
        this.binding.etBudget.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    AddEditProfileActivity.this.model.setBudget(Double.valueOf(charSequence.toString().trim()).doubleValue());
                } catch (NumberFormatException e) {
                    AddEditProfileActivity.this.model.setBudget(0.0d);
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.context, R.style.AppThemeDialogActionBar, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                AddEditProfileActivity.this.calendar.set(1, i);
                AddEditProfileActivity.this.calendar.set(2, i2);
                AddEditProfileActivity.this.calendar.set(5, i3);
                AddEditProfileActivity.this.model.setDateTimeInMillis(AddEditProfileActivity.this.calendar.getTimeInMillis());
            }
        }, this.calendar.get(1), this.calendar.get(2), this.calendar.get(5));
        Calendar instance = Calendar.getInstance();
        instance.add(5, 1);
        datePickerDialog.getDatePicker().setMinDate(instance.getTimeInMillis());
        try {
            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTimePickerDialog() {
        new TimePickerDialog(this.context, R.style.AppThemeDialogActionBar, new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint({"NewApi"})
            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                AddEditProfileActivity.this.calendar.set(11, i);
                AddEditProfileActivity.this.calendar.set(12, i2);
                AddEditProfileActivity.this.model.setDateTimeInMillis(AddEditProfileActivity.this.calendar.getTimeInMillis());
            }
        }, this.calendar.get(11), this.calendar.get(12), false).show();
    }

    private boolean isValid() {
        if (this.model.getName().trim().isEmpty()) {
            Context context = this.context;
            EditText editText = this.binding.etName;
            AppConstants.requestFocusAndError(context, editText, getString(R.string.please_enter) + " " + getString(R.string.name));
            return false;
        } else if (this.model.getPartnerName().trim().isEmpty()) {
            Context context2 = this.context;
            EditText editText2 = this.binding.etNamePartner;
            AppConstants.requestFocusAndError(context2, editText2, getString(R.string.please_enter) + " Partner " + getString(R.string.name));
            return false;
        } else if (!this.model.getWeddingName().trim().isEmpty()) {
            return true;
        } else {
            Context context3 = this.context;
            EditText editText3 = this.binding.etNameWedding;
            AppConstants.requestFocusAndError(context3, editText3, getString(R.string.please_enter) + " Wedding " + getString(R.string.name));
            return false;
        }
    }

    private void addUpdate() {
        int i;
        long j;
        if (isValid()) {
            try {
                this.model.getName().trim();
                this.model.getPhoneNo().trim();
                this.model.getEmailId().trim();
                this.model.getPartnerName().trim();
                this.model.getPartnerPhoneNo().trim();
                this.model.getPartnerEmailId().trim();
                this.model.getWeddingName().trim();
                if (this.isEdit) {
                    try {
                        this.f544db.profileDao().update(this.model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    openItemList(false);
                    return;
                }
                try {
                    i = this.f544db.profileDao().getNameExistCount(this.model.getName());
                } catch (Exception e2) {
                    e2.printStackTrace();
                    i = 0;
                }
                AppConstants.hideKeyboard(this.context, this.binding.getRoot());
                AppConstants.logDebug(this.context, "addItem", " getNameExistCount count => " + String.valueOf(i));
                if (i > 0) {
                    AppConstants.hideKeyboard(this.context, this.binding.getRoot());
                    AppConstants.toastShort(this.context, "Name is all ready exist");
                    return;
                }
                try {
                    this.f544db.profileDao().setDeselectAll();
                    AppPref.setCurrentEvenId(this.context, this.model.getId());
                    j = this.f544db.profileDao().insert(this.model);
                } catch (Exception e3) {
                    e3.printStackTrace();
                    j = 0;
                }
                if (j <= 0) {
                    return;
                }
                if (AppPref.isFirstLaunch(this.context)) {
                    AppPref.setFirstLaunch(this.context, false);
                    openMainActivity();
                    return;
                }
                openItemList(false);
            } catch (Exception e4) {
                AppConstants.logDebug(this.context, "addEdit", "Exception => " + e4.getMessage());
                e4.printStackTrace();
            }
        }
    }

    private void openMainActivity() {
        try {
            startActivity(new Intent(this, MainActivityDashboard.class));
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pickContactPerm(boolean z) {
        if (isHasPermissions(this.context, "android.permission.READ_CONTACTS")) {
            pickContact(z);
        } else {
            requestPermissions(this.context, getString(R.string.rationale_contact), z ? Constants.REQUEST_PERM_CONTACT_PARTNER : 1051, "android.permission.READ_CONTACTS");
        }
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
        switch (i) {
            case 1051:
                pickContact(false);
                return;
            case Constants.REQUEST_PERM_CONTACT_PARTNER:
                pickContact(true);
                return;
            default:
                return;
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
        if (i2 == -1) {
            switch (i) {
                case Constants.REQUEST_PICK_CONTACT:
                    addDetailFromContact(intent);
                    return;
                case Constants.REQUEST_PICK_CONTACT_PARTNER:
                    addDetailFromContactPartner(intent);
                    return;
                default:
                    return;
            }
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
                                    setWeddingName();
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
                                    setWeddingName();
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
                            }
                            setWeddingName();
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
                setWeddingName();
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
                setWeddingName();
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
                setWeddingName();
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
        setWeddingName();
    }

    private void addDetailFromContactPartner(Intent intent) {
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
                                    this.model.setPartnerName(str);
                                    this.model.setPartnerPhoneNo(str2);
                                    this.model.setPartnerEmailId(str3);
                                    this.model.setPartnerAddress(str4);
                                    setWeddingName();
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
                                    this.model.setPartnerName(str);
                                    this.model.setPartnerPhoneNo(str2);
                                    this.model.setPartnerEmailId(str3);
                                    this.model.setPartnerAddress(str4);
                                    setWeddingName();
                                }
                                ContentResolver contentResolver422 = getContentResolver();
                                Uri uri422 = ContactsContract.Data.CONTENT_URI;
                                query = contentResolver422.query(uri422, new String[]{"data1", "data2"}, "contact_id = " + string3 + " AND " + "mimetype" + " = '" + "vnd.android.cursor.item/website" + "'", (String[]) null, (String) null);
                                query.getString(query.getColumnIndex("data1"));
                                query.close();
                                str = string4;
                                this.model.setPartnerName(str);
                                this.model.setPartnerPhoneNo(str2);
                                this.model.setPartnerEmailId(str3);
                                this.model.setPartnerAddress(str4);
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                            setWeddingName();
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
                this.model.setPartnerName(str);
                this.model.setPartnerPhoneNo(str2);
                this.model.setPartnerEmailId(str3);
                this.model.setPartnerAddress(str4);
                setWeddingName();
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
                this.model.setPartnerName(str);
                this.model.setPartnerPhoneNo(str2);
                this.model.setPartnerEmailId(str3);
                this.model.setPartnerAddress(str4);
                setWeddingName();
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
                this.model.setPartnerName(str);
                this.model.setPartnerPhoneNo(str2);
                this.model.setPartnerEmailId(str3);
                this.model.setPartnerAddress(str4);
                setWeddingName();
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
        this.model.setPartnerName(str);
        this.model.setPartnerPhoneNo(str2);
        this.model.setPartnerEmailId(str3);
        this.model.setPartnerAddress(str4);
        setWeddingName();
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
