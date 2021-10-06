package com.example.weddingplanner.ui;


import static com.example.weddingplanner.ui.Key.KEY_ADDRESS;
import static com.example.weddingplanner.ui.Key.KEY_CURRENCY;
import static com.example.weddingplanner.ui.Key.KEY_PARTNER_EMAIL_ID;
import static com.example.weddingplanner.ui.Key.KEY_PARTNER_GENDER;
import static com.example.weddingplanner.ui.Key.KEY_PARTNER_NAME;
import static com.example.weddingplanner.ui.Key.KEY_TOTAL_BUGET;
import static com.example.weddingplanner.ui.Key.KEY_USER_GENDER;
import static com.example.weddingplanner.ui.Key.KEY_USER_NAME;
import static com.example.weddingplanner.ui.Key.KEY_WEDDING_DATETIME;
import static com.example.weddingplanner.ui.Key.KEY_WEDDING_NAME;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.MyApp;
import com.example.weddingplanner.databinding.ActivityCreateUserFormBinding;
import com.example.weddingplanner.databinding.LayoutUpdateappDialogBinding;
import com.example.weddingplanner.ui.CropImage.ImageCompressionDispose;
import com.example.weddingplanner.ui.CropImage.ImageListener;
import com.example.weddingplanner.ui.imagePicker.DefaultCallback;
import com.example.weddingplanner.ui.imagePicker.EasyImage;
import com.example.weddingplanner.ui.model.ConstantData;
import com.example.weddingplanner.ui.model.CurrencyModel;
import com.example.weddingplanner.ui.model.LoginData;
import com.example.weddingplanner.ui.model.WeddingFormData;
import com.example.weddingplanner.ui.param.Params;
import com.example.weddingplanner.ui.pref.MyPref;
import com.theartofdev.edmodo.cropper.CropImage;


import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import pub.devrel.easypermissions.EasyPermissions;

public class CreateUserFormActivity extends AppCompatActivity {
    private static final String TAG = "CreateUserFormActivity";
    private static final String docName = "userInfo";


    private boolean MoveToDashboard = false;
    private String ProfilePath;
    private ActivityCreateUserFormBinding binding;
    private CurrencyModel currency;
    private String currencyName = "USD";
    private String currencySymbole = "$";
    private String currencyText = "USD";
    private long dateMillisecond;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private LoginData loginData;
    //    private SignIn signIn;
    private String str_P_Gender = Params.FEMALE;
    private String str_U_Gender = Params.MALE;
    private String token;
    private WeddingFormData weddingData;
    private String weddingdate;
    private final int padding_btn = MyApp.getInstance().getResources().getDimensionPixelSize(R.dimen._6sdp);


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = ActivityCreateUserFormBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        this.signIn = new SignIn(this);
        ConstantData.DisplayProgressInitialize(this);

        this.token = MyPref.getPreference(Params.TOKEN);

        this.MoveToDashboard = getIntent().getBooleanExtra(Params.MOVE_TO_DASHBOARD, false);
        this.currency = new CurrencyModel("USD", "$", "US Dollar", false);


        initView();
        UserGender();
        PartnerGender();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            new RandomString(8, ThreadLocalRandom.current());
//        }
        this.binding.tvCeremonyDate.setTypeface(ConstantData.getRegulerFontFamily(this));
        this.binding.tvCeremonyTime.setTypeface(ConstantData.getRegulerFontFamily(this));
        this.binding.etYourName.setTypeface(ConstantData.getRegulerFontFamily(this));
        this.binding.etPartnerName.setTypeface(ConstantData.getRegulerFontFamily(this));
        this.binding.etPartnerEmail.setTypeface(ConstantData.getRegulerFontFamily(this));
        this.binding.etWeddingName.setTypeface(ConstantData.getRegulerFontFamily(this));
        this.binding.etAddress.setTypeface(ConstantData.getRegulerFontFamily(this));
        this.binding.etBudget.setTypeface(ConstantData.getRegulerFontFamily(this));
        this.binding.etCurrency.setTypeface(ConstantData.getRegulerFontFamily(this));
    }

    private void initView() {
        this.binding.toolbar.imgBack.setVisibility(View.VISIBLE);
        this.binding.toolbar.cardDone.setVisibility(View.VISIBLE);
        this.binding.toolbar.tvTitle.setText(getResources().getString(R.string.create_wedding));

        this.binding.etPartnerName.setHint(getString(R.string.partner_name));
        this.binding.etPartnerEmail.setHint(getString(R.string.partner_email));

        this.binding.tvCeremonyDate.setSelected(true);
        TextView textView = this.binding.etWeddingName;
        this.binding.btnImgRemove.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
            }
        });
        this.binding.toolbar.imgBack.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                finish();
            }
        });
        this.binding.tvCeremonyDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                OpenCalender();
            }
        });
        this.binding.tvCeremonyTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                OpenTimePicker();
            }
        });
        this.binding.imgUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                addPicture(view);
            }
        });

        this.binding.etYourName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!CreateUserFormActivity.this.binding.etWeddingName.isFocused()) {
                    TextView textView = CreateUserFormActivity.this.binding.etWeddingName;
                    textView.setText(CreateUserFormActivity.this.binding.etYourName.getText().toString() + " & " + CreateUserFormActivity.this.binding.etPartnerName.getText().toString());
                }
            }
        });
        this.binding.etPartnerName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!CreateUserFormActivity.this.binding.etWeddingName.isFocused()) {
                    TextView textView = CreateUserFormActivity.this.binding.etWeddingName;
                    textView.setText(CreateUserFormActivity.this.binding.etYourName.getText().toString() + " & " + CreateUserFormActivity.this.binding.etPartnerName.getText().toString());
                }
            }
        });
        this.binding.etCurrency.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(CreateUserFormActivity.this, CurrencyActivity.class);
                if (!CreateUserFormActivity.this.binding.etCurrency.getText().toString().trim().isEmpty()) {
                    intent.putExtra(Params.CURRENCY_VALUE, CreateUserFormActivity.this.currency);
                }
                CreateUserFormActivity.this.startActivityForResult(intent, 1002);
            }
        });
        this.binding.toolbar.cardDone.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                CreateUserFormActivity.this.saveDetail(view);
            }
        });


    }


    public void saveDetail(View view) {
        String user_name = this.binding.etYourName.getText().toString().trim();
        String partner_name = this.binding.etPartnerName.getText().toString().trim();
        String partner_email = this.binding.etPartnerEmail.getText().toString().trim();
        this.binding.tvCeremonyDate.getText().toString().trim();
        this.binding.tvCeremonyTime.getText().toString().trim();
        String wedding_name = this.binding.etWeddingName.getText().toString().trim();
        String address = this.binding.etAddress.getText().toString().trim();
        String buget = this.binding.etBudget.getText().toString().trim();
        this.binding.etCurrency.getText().toString().trim();
        String user_gender = this.str_U_Gender;
        String partner_gender = this.str_P_Gender;
        if (this.binding.etPartnerEmail.getText().toString().isEmpty() || this.binding.etPartnerEmail.getText().toString().matches(getString(R.string.email_pattern))) {
            this.weddingData = new WeddingFormData(user_name, partner_name.equals("") ? "Unknown" : partner_name,
                    user_gender, partner_gender, partner_email, wedding_name, String.valueOf(this.dateMillisecond),
                    address, buget, this.currencyText);


            MyPref.setWeddingData(weddingData);

//            this.loginData.setUserGender(user_gender);
//            this.loginData.setPartnerName(partner_name);
//            this.loginData.setPartnerGender(partner_gender);
//            this.loginData.setPartnerEmailId(partner_email);
//            MyPref.setLoginData(this.loginData);
            SubmitWeddingform(this.ProfilePath, this.weddingData);
            return;
        }
        this.binding.etPartnerEmail.setError("Please Enter Valid Partner EmailId");
    }

    public void addPicture(View view) {
        checkPermAndFill();
    }


    private void OpenTimePicker() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(this.dateMillisecond);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(CreateUserFormActivity.this.dateMillisecond);
                instance.set(11, i);
                instance.set(12, i2);
                CreateUserFormActivity.this.dateMillisecond = instance.getTimeInMillis();
                CreateUserFormActivity.this.binding.tvCeremonyTime.setText(ConstantData.getLongToStringTimeAMPM(CreateUserFormActivity.this.dateMillisecond));
            }
        }, instance.get(11), instance.get(12), false).show();
    }

    private void OpenTimePicker(long timeMiliSecond) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeMiliSecond);
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(timeMiliSecond);
                instance.set(11, i);
                instance.set(12, i2);
                CreateUserFormActivity.this.binding.tvCeremonyTime.setText(ConstantData.getLongToStringTimeAMPM(instance.getTimeInMillis()));
            }
        }, instance.get(11), instance.get(12), false).show();
    }


    private void OpenCalender() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(this.dateMillisecond);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(CreateUserFormActivity.this.dateMillisecond);
                instance.set(1, i);
                instance.set(2, i2);
                instance.set(5, i3);
                CreateUserFormActivity.this.dateMillisecond = instance.getTimeInMillis();
                CreateUserFormActivity.this.binding.tvCeremonyDate.setText(ConstantData.getLongToStringDayDateTimewithAMPM(CreateUserFormActivity.this.dateMillisecond));
            }
        }, instance.get(1), instance.get(2), instance.get(5)).show();
    }

    private void checkPermAndFill() {
        EasyImage.openGallery(this, 0);
    }

    private boolean isHasPermissions(Context context, String... strArr) {
        return EasyPermissions.hasPermissions(context, strArr);
    }

    private void requestPermissions(Context context, String str, int i, String... strArr) {
        EasyPermissions.requestPermissions((Activity) context, str, i, strArr);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        EasyPermissions.onRequestPermissionsResult(i, strArr, iArr, this);
    }

    private void SubmitWeddingform(String str, WeddingFormData weddingFormData) {
        ConstantData.ShowProgress();
        if (ConstantData.isNetworkAvailable(this)) {

            String user_name = weddingFormData.getUser_name();
            String partner_name = weddingFormData.getPartner_name();
            String user_gender = weddingFormData.getUser_gender();
            String partner_gender = weddingFormData.getPartner_gender();
            String partner_email_id = weddingFormData.getPartner_email_id();
            String wedding_name = weddingFormData.getWedding_name();
            String wedding_datetime = weddingFormData.getWedding_datetime();
            String address = weddingFormData.getAddress();
            String total_budget = weddingFormData.getTotal_budget();
            String currency = weddingFormData.getCurrency();

            saveDataOnFirstore(user_name, partner_name, user_gender, partner_gender, partner_email_id, wedding_name,
                    wedding_datetime, address, total_budget, currency);

        } else {
            ConstantData.toastShort(this, getString(R.string.no_internet_available));
            ConstantData.HideProgress();
        }
    }

    private void saveDataOnFirstore(String user_name, String partner_name, String user_gender, String partner_gender, String partner_email_id, String wedding_name,
                                    String wedding_datetime, String address, String total_budget, String currency) {

        HashMap<String, String> data = new HashMap<>();


        data.put(KEY_USER_NAME, user_name);
        data.put(KEY_PARTNER_NAME, partner_name);
        data.put(KEY_USER_GENDER, user_gender);
        data.put(KEY_PARTNER_GENDER, partner_gender);
        data.put(KEY_PARTNER_EMAIL_ID, partner_email_id);
        data.put(KEY_WEDDING_NAME, wedding_name);
        data.put(KEY_WEDDING_DATETIME, wedding_datetime);
        data.put(KEY_ADDRESS, address);
        data.put(KEY_TOTAL_BUGET, total_budget);
        data.put(KEY_CURRENCY, currency);

    }


    private void AppUpdateDialog(int i) {
        Dialog dialog = new Dialog(this, R.style.DialogCustomTheme);
        LayoutUpdateappDialogBinding inflate = LayoutUpdateappDialogBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(inflate.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(-1, -2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
        TextView textView = inflate.tvTitle;
        textView.setText("New update available : Version - " + i);
        inflate.cardUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                try {
                    CreateUserFormActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + CreateUserFormActivity.this.getPackageName())));
                } catch (ActivityNotFoundException unused) {
                    Toast.makeText(CreateUserFormActivity.this, "Couldn't find PlayStore on this device", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UserGender() {
        this.binding.tvMalePersonal.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                userMaleGender();
            }
        });
        this.binding.tvFemalePersonal.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                userFemaleGender();
            }
        });
        this.binding.tvOtherPersonal.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                userOtherGender();
            }
        });
    }

    public void userMaleGender() {
        this.str_U_Gender = Params.MALE;
        gendorselection(this.binding.tvMalePersonal);
    }

    public void userFemaleGender() {
        this.str_U_Gender = Params.FEMALE;
        gendorselection(this.binding.tvFemalePersonal);
    }

    public void userOtherGender() {
        this.str_U_Gender = Params.OTHER;
        gendorselection(this.binding.tvOtherPersonal);
    }

    private void PartnerGender() {
        this.binding.tvMalePartner.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                partnerMaleGender();
            }
        });
        this.binding.tvFemalePartner.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                partnerFemaleGender();
            }
        });
        this.binding.tvOtherPartner.setOnClickListener(new View.OnClickListener() {

            public final void onClick(View view) {
                partnerOtherGender();
            }
        });
    }

    public void partnerMaleGender() {
        this.str_P_Gender = Params.MALE;
        gendorpartnerselection(this.binding.tvMalePartner);
    }

    public void partnerFemaleGender() {
        this.str_P_Gender = Params.FEMALE;
        gendorpartnerselection(this.binding.tvFemalePartner);
    }

    public void partnerOtherGender() {
        this.str_P_Gender = Params.OTHER;
        gendorpartnerselection(this.binding.tvOtherPartner);
    }

    private void gendorselection(TextView textView) {
        this.binding.tvMalePersonal.setBackground(getResources().getDrawable(R.drawable.square_white_selection_gender));
        this.binding.tvMalePersonal.setTextColor(getResources().getColor(R.color.black));
        this.binding.tvFemalePersonal.setBackground(getResources().getDrawable(R.drawable.square_white_selection_gender));
        this.binding.tvFemalePersonal.setTextColor(getResources().getColor(R.color.black));
        this.binding.tvOtherPersonal.setBackground(getResources().getDrawable(R.drawable.square_white_selection_gender));
        this.binding.tvOtherPersonal.setTextColor(getResources().getColor(R.color.black));
        textView.setBackground(getResources().getDrawable(R.drawable.square_batli));
        textView.setTextColor(getResources().getColor(R.color.white));
        TextView textView2 = this.binding.tvMalePersonal;
        int i = this.padding_btn;
        textView2.setPadding(i, i, i, i);
        TextView textView3 = this.binding.tvFemalePersonal;
        int i2 = this.padding_btn;
        textView3.setPadding(i2, i2, i2, i2);
        TextView textView4 = this.binding.tvOtherPersonal;
        int i3 = this.padding_btn;
        textView4.setPadding(i3, i3, i3, i3);
        int i4 = this.padding_btn;
        textView.setPadding(i4, i4, i4, i4);
    }

    private void gendorpartnerselection(TextView textView) {
        this.binding.tvMalePartner.setBackground(getResources().getDrawable(R.drawable.square_white_selection_gender));
        this.binding.tvMalePartner.setTextColor(getResources().getColor(R.color.black));
        this.binding.tvFemalePartner.setBackground(getResources().getDrawable(R.drawable.square_white_selection_gender));
        this.binding.tvFemalePartner.setTextColor(getResources().getColor(R.color.black));
        this.binding.tvOtherPartner.setBackground(getResources().getDrawable(R.drawable.square_white_selection_gender));
        this.binding.tvOtherPartner.setTextColor(getResources().getColor(R.color.black));
        textView.setBackground(getResources().getDrawable(R.drawable.square_batli));
        textView.setTextColor(getResources().getColor(R.color.white));
        TextView textView2 = this.binding.tvMalePartner;
        int i = this.padding_btn;
        textView2.setPadding(i, i, i, i);
        TextView textView3 = this.binding.tvFemalePartner;
        int i2 = this.padding_btn;
        textView3.setPadding(i2, i2, i2, i2);
        TextView textView4 = this.binding.tvOtherPartner;
        int i3 = this.padding_btn;
        textView4.setPadding(i3, i3, i3, i3);
        int i4 = this.padding_btn;
        textView.setPadding(i4, i4, i4, i4);
    }


    @Override
    public void onDestroy() {
        CompositeDisposable compositeDisposable = this.disposable;
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        EasyImage.handleActivityResult(i, i2, intent, this, new DefaultCallback() {

            @Override
            public void onImagesPicked(List<File> list, EasyImage.ImageSource imageSource, int i) {
                CropImage.activity(Uri.fromFile(list.get(0))).setAspectRatio(1, 1).start(CreateUserFormActivity.this);
            }
        });
        if (i == 203) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
            if (i2 == -1) {
                this.disposable.add(new ImageCompressionDispose().getDisposeInstance(getApplicationContext(), activityResult.getUri().getPath(), new ImageListener() {

                    @Override
                    public void onImageCopy(String str) {
                        CreateUserFormActivity.this.ProfilePath = str;
                        Glide.with(CreateUserFormActivity.this).load(CreateUserFormActivity.this.ProfilePath).into(CreateUserFormActivity.this.binding.imgUser);
                        CreateUserFormActivity.this.binding.btnImgRemove.setVisibility(View.VISIBLE);
                        MyPref.savePreference(CreateUserFormActivity.this.ProfilePath, Params.PROFILE_IMAGE);
                        CreateUserFormActivity.this.loginData.setPhotourl(CreateUserFormActivity.this.ProfilePath);
                        MyPref.setLoginData(CreateUserFormActivity.this.loginData);
                    }
                }));
            } else if (i2 == 204) {
                activityResult.getError();
            }
        }
        if (i == 1002 && i2 == -1) {
            CurrencyModel currencyModel = intent.getParcelableExtra(Params.CURRENCY);
            this.currency = currencyModel;
            if (currencyModel != null) {
                this.currencySymbole = currencyModel.getCurrencySymbol();
                this.currencyName = this.currency.getCurrencyName();
                this.currencyText = this.currency.getCurrency();
                TextView textView = this.binding.etCurrency;
                textView.setText(this.currency.getCurrency() + ", " + this.currency.getCurrencyName());
            }
        }
    }

}
