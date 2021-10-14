package com.example.weddingplanner.appBase.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.weddingplanner.appBase.adapter.GuestAdapter;
import com.example.weddingplanner.appBase.adapter.SelectionAdapter;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.example.weddingplanner.appBase.models.guest.GuestListModel;
import com.example.weddingplanner.appBase.models.selection.SelectionRowModel;
import com.example.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.roomsDB.guest.GuestRowModel;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.appBase.utils.BackgroundAsync;
import com.example.weddingplanner.appBase.utils.Constants;
import com.example.weddingplanner.appBase.utils.OnAsyncBackground;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.example.weddingplanner.databinding.ActivityGuestListBinding;
import com.example.weddingplanner.databinding.AlertDialogRecyclerListBinding;
import com.example.weddingplanner.pdfRepo.ReportRowModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GuestListActivity extends BaseActivityRecyclerBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_IS_FILTER = "EXTRA_IS_FILTER";
    public static String EXTRA_IS_PENDING = "EXTRA_IS_PENDING";

    public ActivityGuestListBinding binding;


    private AppDataBase db;

    public Dialog dialogFilterTypeList;
    private AlertDialogRecyclerListBinding dialogFilterTypeListBinding;

    public Dialog dialogOrderTypeList;
    private AlertDialogRecyclerListBinding dialogOrderTypeListBinding;
    private File dir;
    private Document document;
    private String fileName = null;

    public ArrayList<SelectionRowModel> filterTypeList;

    public boolean isFilter = false;
    private boolean isPending = true;
    private boolean isUpdateDashboard = false;

    public ArrayList<GuestRowModel> listMain;

    public GuestListModel model;

    public ArrayList<SelectionRowModel> orderTypeList;
    private Paragraph paragraph;
    private String repoTitle = "List";
    private String repoType = "Guest";
    GuestRowModel rowModel = null;
    private String searchText = "";

    public int selectedFilterTypePos = 0;

    public int selectedOrderTypePos = 0;
    private String subTitle = "Companions";
    private ToolbarModel toolbarModel;
    private PdfWriter writer = null;

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }


    public void setBinding() {
        binding = ActivityGuestListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.model = new GuestListModel();
        this.model.setArrayList(new ArrayList());
        this.listMain = new ArrayList<>();
        this.model.setNoDataIcon(R.drawable.drawer_guests);
        this.model.setNoDataText(getString(R.string.noDataTitleGuests));
        this.model.setNoDataDetail(getString(R.string.noDataDescGuests));
//        this.binding.setModel(this.model);
        this.db = AppDataBase.getAppDatabase(this.context);
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.drawerTitleGuests));
        this.toolbarModel.setOtherMenu(true);
        this.toolbarModel.setSearchMenu(true);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.order_list);
        this.toolbarModel.setShare(true);
//        this.binding.includedToolbar.setModel(this.toolbarModel);
        setSupportActionBar(this.binding.includedToolbar.toolbar);
        binding.includedToolbar.textTitle.setText("Guest");
        this.binding.includedToolbar.imgAdd.setVisibility(View.GONE);
        binding.includedToolbar.imgShare.setImageResource(R.drawable.filter_empty);

        binding.includedToolbar.imgBack.setVisibility(View.VISIBLE);
        binding.includedToolbar.imgOther.setVisibility(View.GONE);
        binding.includedToolbar.imageHome.setVisibility(View.GONE);
        binding.includedToolbar.progressbar.setVisibility(View.GONE);
        binding.includedToolbar.imgDelete.setVisibility(View.GONE);
        binding.includedToolbar.etOther.setVisibility(View.GONE);
        binding.includedToolbar.imgDrawer.setVisibility(View.GONE);
    }

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_list, menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        int itemId = menuItem.getItemId();
//        if (itemId == R.id.shareList) {
//            showPdfDialog();
//            return true;
//        } else if (itemId != R.id.summary) {
//            return super.onOptionsItemSelected(menuItem);
//        } else {
//            openSummary();
//            return true;
//        }
//    }

//    private void openSummary() {
//        startActivity(new Intent(this.context, GuestSummaryActivity.class));
//    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
//        this.binding.includedToolbar.imgAdd.setOnClickListener(this);
        this.binding.includedToolbar.imgShare.setOnClickListener(this);
        this.binding.fabAdd.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAdd:
                openItemDetail(-1, -1, new GuestRowModel(), false);
                return;
//            case R.id.imgAdd:
//                openSummary();
//                return;
            case R.id.imgBack:
                onBackPressed();
                return;
            case R.id.imgOther:
                showDialogOrderTypeList();
                return;
            case R.id.imgShare:
                showDialogFilterTypeList();
                return;
            default:
                return;
        }
    }


    public void callApi() {
        setupFilter();
    }


    public void fillData() {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                fillFromDB();
            }

            public void onPostExecute() {
                notifyAdapter();
            }
        }).execute(new Object[0]);
    }


    public void fillFromDB() {
        List arrayList = new ArrayList();
        try {
            arrayList = this.db.guestDao().getAllGuest(AppPref.getCurrentEvenId(this.context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                GuestRowModel guestRowModel = (GuestRowModel) arrayList.get(i);
                try {
                    guestRowModel.setArrayList(new ArrayList());
                    guestRowModel.getArrayList().addAll(this.db.guestDao().getAllComp(AppPref.getCurrentEvenId(this.context), guestRowModel.getId()));
                    guestRowModel.setCompanions((long) guestRowModel.getArrayList().size());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                this.listMain.add(guestRowModel);
            }
            checkFilterAndFillList();
        }
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new GuestAdapter(this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 1) {
                    openItemDetail(i, listMain.indexOf(model.getArrayList().get(i)), model.getArrayList().get(i), true);
                }
            }
        }));
        this.binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (i2 > 0 && binding.fabAdd.getVisibility() == View.VISIBLE) {
                    binding.fabAdd.hide();
                } else if (i2 < 0 && binding.fabAdd.getVisibility() != View.VISIBLE) {
                    binding.fabAdd.show();
                }
            }
        });
    }


    public void openItemDetail(int i, int i2, GuestRowModel guestRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditGuestActivity.class);
        intent.putExtra(AddEditGuestActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditGuestActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditGuestActivity.EXTRA_POSITION_MAIN, i2);
        intent.putExtra(AddEditGuestActivity.EXTRA_MODEL, guestRowModel);
        startActivityForResult(intent, 1002);
    }


    public void notifyAdapter() {
        if (this.model.getArrayList() != null && this.model.getArrayList().size() > 0) {
            sortBy();
        }
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


    public void initMethods() {
        orderTyDialogSetup();
        setSearch();
    }

    private void orderTyDialogSetup() {
        fillOrderTypeList();
        setOrderTypeListDialog();
    }

    private void fillOrderTypeList() {
        this.orderTypeList = new ArrayList<>();
        this.orderTypeList.add(new SelectionRowModel(Constants.ORDER_TYPE_NAME_ASC));
        this.orderTypeList.add(new SelectionRowModel(Constants.ORDER_TYPE_NAME_DESC));
        this.orderTypeList.add(new SelectionRowModel(Constants.ORDER_TYPE_COMPANION_ASC));
        this.orderTypeList.add(new SelectionRowModel(Constants.ORDER_TYPE_COMPANION_DESC));
        this.selectedOrderTypePos = getSelectedPosById();
        this.orderTypeList.get(this.selectedOrderTypePos).setSelected(true);
    }

    private int getSelectedPosById() {
        for (int i = 0; i < this.orderTypeList.size(); i++) {
            if (this.orderTypeList.get(i).getLabel().equalsIgnoreCase(AppPref.getSortTypeGuest(this.context))) {
                return i;
            }
        }
        return 0;
    }

    public void setOrderTypeListDialog() {
        this.dialogOrderTypeListBinding = AlertDialogRecyclerListBinding.inflate(LayoutInflater.from(this.context), (ViewGroup) null, false);
        this.dialogOrderTypeList = new Dialog(this.context);
        this.dialogOrderTypeList.setContentView(this.dialogOrderTypeListBinding.getRoot());
        this.dialogOrderTypeList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogOrderTypeList.getWindow().setLayout(-1, -2);
        this.dialogOrderTypeListBinding.txtTitle.setText(R.string.select_list_order_type);
        this.dialogOrderTypeListBinding.btnOk.setText(R.string.set);
        this.dialogOrderTypeListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogOrderTypeListBinding.recycler.setAdapter(new SelectionAdapter(this.context, true, this.orderTypeList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                int unused = selectedOrderTypePos = i;
                AppPref.setSortTypeGuest(context, ((SelectionRowModel) orderTypeList.get(selectedOrderTypePos)).getLabel());
                notifyAdapter();
                try {
                    dialogOrderTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        this.dialogOrderTypeListBinding.imgAdd.setImageResource(R.drawable.close);
        this.dialogOrderTypeListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogOrderTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogOrderTypeListBinding.linButton.setVisibility(View.GONE);
        this.dialogOrderTypeListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogOrderTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogOrderTypeListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppPref.setSortTypeGuest(context, ((SelectionRowModel) orderTypeList.get(selectedOrderTypePos)).getLabel());
                notifyAdapter();
                try {
                    dialogOrderTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDialogOrderTypeList() {
        try {
            this.dialogOrderTypeListBinding.recycler.scrollToPosition(this.selectedOrderTypePos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.dialogOrderTypeList != null && !this.dialogOrderTypeList.isShowing()) {
                this.dialogOrderTypeList.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void sortBy() {
        char c;
        String sortTypeGuest = AppPref.getSortTypeGuest(this.context);
        int hashCode = sortTypeGuest.hashCode();
        if (hashCode != -1805634388) {
            if (hashCode != -1090930781) {
                if (hashCode != -581357916) {
                    if (hashCode == -422524019 && sortTypeGuest.equals(Constants.ORDER_TYPE_NAME_DESC)) {
                        c = 1;
                        switch (c) {
                            case 0:
                                Collections.sort(this.model.getArrayList(), new Comparator<GuestRowModel>() {
                                    /* class com.msint.weddingplanner.appBase.view.GuestListActivity.AnonymousClass8 */

                                    public int compare(GuestRowModel guestRowModel, GuestRowModel guestRowModel2) {
                                        return guestRowModel.getName().toLowerCase().compareTo(guestRowModel2.getName().toLowerCase());
                                    }
                                });
                                return;
                            case 1:
                                Collections.sort(this.model.getArrayList(), new Comparator<GuestRowModel>() {
                                    /* class com.msint.weddingplanner.appBase.view.GuestListActivity.AnonymousClass9 */

                                    public int compare(GuestRowModel guestRowModel, GuestRowModel guestRowModel2) {
                                        return guestRowModel2.getName().toLowerCase().compareTo(guestRowModel.getName().toLowerCase());
                                    }
                                });
                                return;
                            case 2:
                                Collections.sort(this.model.getArrayList(), new Comparator<GuestRowModel>() {
                                    /* class com.msint.weddingplanner.appBase.view.GuestListActivity.AnonymousClass10 */

                                    @SuppressLint({"NewApi"})
                                    public int compare(GuestRowModel guestRowModel, GuestRowModel guestRowModel2) {
                                        return (guestRowModel.getCompanions() > guestRowModel2.getCompanions() ? 1 : (guestRowModel.getCompanions() == guestRowModel2.getCompanions() ? 0 : -1));
                                    }
                                });
                                return;
                            case 3:
                                Collections.sort(this.model.getArrayList(), new Comparator<GuestRowModel>() {
                                    /* class com.msint.weddingplanner.appBase.view.GuestListActivity.AnonymousClass11 */

                                    @SuppressLint({"NewApi"})
                                    public int compare(GuestRowModel guestRowModel, GuestRowModel guestRowModel2) {
                                        return (guestRowModel2.getCompanions() > guestRowModel.getCompanions() ? 1 : (guestRowModel2.getCompanions() == guestRowModel.getCompanions() ? 0 : -1));
                                    }
                                });
                                return;
                            default:
                                return;
                        }
                    }
                } else if (sortTypeGuest.equals(Constants.ORDER_TYPE_COMPANION_ASC)) {
                    c = 2;
                    switch (c) {
                    }
                }
            } else if (sortTypeGuest.equals(Constants.ORDER_TYPE_NAME_ASC)) {
                c = 0;
                switch (c) {
                }
            }
        } else if (sortTypeGuest.equals(Constants.ORDER_TYPE_COMPANION_DESC)) {
            c = 3;
            switch (c) {
            }
        }
        c = 65535;
        switch (c) {
        }
    }

    private void selectionAllOrderType(boolean z) {
        for (int i = 0; i < this.orderTypeList.size(); i++) {
            this.orderTypeList.get(i).setSelected(z);
        }
    }

    private void setSearch() {
        this.binding.includedToolbar.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                updateList(str);
                return false;
            }
        });
    }

    public void updateList(String str) {
        if (str == null || str.length() <= 0) {
            this.searchText = "";
            this.model.getArrayList().clear();
            checkFilterAndFillList();
        } else {
            this.searchText = str.trim().toLowerCase();
            this.model.getArrayList().clear();
            for (int i = 0; i < this.listMain.size(); i++) {
                GuestRowModel guestRowModel = this.listMain.get(i);
                if (guestRowModel.getName().toLowerCase().contains(str.trim().toLowerCase())) {
                    if (!this.isFilter) {
                        this.model.getArrayList().add(guestRowModel);
                    } else if (this.isPending) {
                        if (!this.listMain.get(i).isInvitationSent()) {
                            this.model.getArrayList().add(guestRowModel);
                        }
                    } else if (this.listMain.get(i).isInvitationSent()) {
                        this.model.getArrayList().add(guestRowModel);
                    }
                }
            }
        }
        notifyAdapter();
    }

    private void filterTyDialogSetup() {
        fillFilterTypeList();
        setFilterTypeListDialog();
    }

    private void fillFilterTypeList() {
        this.filterTypeList = new ArrayList<>();
        this.filterTypeList.add(new SelectionRowModel(Constants.FILTER_TYPE_ALL_LIST, "Show all list"));
        this.filterTypeList.add(new SelectionRowModel(Constants.FILTER_TYPE_PENDING, "Show invitation not sent only"));
        this.filterTypeList.add(new SelectionRowModel(Constants.FILTER_TYPE_COMPLETED, "Show invitation sent only"));
        this.selectedFilterTypePos = getSelectedPosByIdFilter();
        this.filterTypeList.get(this.selectedFilterTypePos).setSelected(true);
    }

    private int getSelectedPosByIdFilter() {
        for (int i = 0; i < this.filterTypeList.size(); i++) {
            if (this.filterTypeList.get(i).getId().equalsIgnoreCase(getFilterTypeId())) {
                return i;
            }
        }
        return 0;
    }

    private String getFilterTypeId() {
        if (!this.isFilter) {
            return Constants.FILTER_TYPE_ALL_LIST;
        }
        return this.isPending ? Constants.FILTER_TYPE_PENDING : Constants.FILTER_TYPE_COMPLETED;
    }

    public void setFilterTypeListDialog() {
        this.dialogFilterTypeListBinding = AlertDialogRecyclerListBinding.inflate(LayoutInflater.from(this.context), (ViewGroup) null, false);
        this.dialogFilterTypeList = new Dialog(this.context);
        this.dialogFilterTypeList.setContentView(this.dialogFilterTypeListBinding.getRoot());
        this.dialogFilterTypeList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogFilterTypeList.getWindow().setLayout(-1, -2);
        this.dialogFilterTypeListBinding.txtTitle.setText(R.string.select_list_filter_type);
        this.dialogFilterTypeListBinding.btnOk.setText(R.string.set);
        this.dialogFilterTypeListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogFilterTypeListBinding.recycler.setAdapter(new SelectionAdapter(this.context, true, this.filterTypeList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                int unused = selectedFilterTypePos = i;
                AppPref.setSortTypeTask(context, ((SelectionRowModel) filterTypeList.get(selectedFilterTypePos)).getLabel());
                filterList(((SelectionRowModel) filterTypeList.get(selectedFilterTypePos)).getId());
                try {
                    dialogFilterTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        this.dialogFilterTypeListBinding.imgAdd.setImageResource(R.drawable.close);
        this.dialogFilterTypeListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogFilterTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogFilterTypeListBinding.linButton.setVisibility(View.GONE);
        this.dialogFilterTypeListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    dialogFilterTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogFilterTypeListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppPref.setSortTypeTask(context, ((SelectionRowModel) filterTypeList.get(selectedFilterTypePos)).getLabel());
                notifyAdapter();
                try {
                    dialogFilterTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void filterList(String str) {
        if (str.equalsIgnoreCase(Constants.FILTER_TYPE_ALL_LIST)) {
            this.isFilter = false;
        } else {
            this.isFilter = true;
        }
        if (str.equalsIgnoreCase(Constants.FILTER_TYPE_PENDING)) {
            this.isPending = false;
        } else {
            this.isPending = true;
        }
        filterList();
    }

    private void filterList() {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
                setupFilterIcon(isFilter);
            }

            public void doInBackground() {
                checkFilterAndFillList();
            }

            public void onPostExecute() {
                notifyAdapter();
            }
        }).execute(new Object[0]);
    }

    private void showDialogFilterTypeList() {
        try {
            this.dialogFilterTypeListBinding.recycler.scrollToPosition(this.selectedFilterTypePos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.dialogFilterTypeList != null && !this.dialogFilterTypeList.isShowing()) {
                this.dialogFilterTypeList.show();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void setupFilter() {
        if (getIntent() != null && getIntent().hasExtra(EXTRA_IS_FILTER)) {
            this.isFilter = getIntent().getBooleanExtra(EXTRA_IS_FILTER, false);
            this.isPending = getIntent().getBooleanExtra(EXTRA_IS_PENDING, false);
        }
        setupFilterIcon(this.isFilter);
        filterTyDialogSetup();
    }


    public void setupFilterIcon(boolean z) {
        this.binding.includedToolbar.imgShare.setImageResource(z ? R.drawable.filter_filled : R.drawable.filter_empty);
    }


    public void checkFilterAndFillList() {
        this.model.getArrayList().clear();
        if (!this.isFilter) {
            this.model.getArrayList().addAll(this.listMain);
        } else {
            fillFilterList(this.isPending);
        }
    }

    private void fillFilterList(boolean z) {
        for (int i = 0; i < this.listMain.size(); i++) {
            if (z) {
                if (this.listMain.get(i).isInvitationSent()) {
                    this.model.getArrayList().add(this.listMain.get(i));
                }
            } else if (!this.listMain.get(i).isInvitationSent()) {
                this.model.getArrayList().add(this.listMain.get(i));
            }
        }
    }


    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1002) {
            this.isUpdateDashboard = true;
            updateList(intent);
        }
    }

    private void updateList(Intent intent) {
        if (intent != null) {
            try {
                if (intent.hasExtra(AddEditTaskActivity.EXTRA_MODEL)) {
                    this.rowModel = (GuestRowModel) intent.getParcelableExtra(AddEditTaskActivity.EXTRA_MODEL);
                    if (intent.getBooleanExtra(AddEditTaskActivity.EXTRA_IS_DELETED, false)) {
                        this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                        this.listMain.remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION_MAIN, 0));
                    } else if (intent.getBooleanExtra(AddEditTaskActivity.EXTRA_IS_EDIT, false)) {
                        if (this.searchText == null || this.searchText.length() <= 0) {
                            if (!this.isFilter) {
                                this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                            } else if (this.isPending) {
                                if (this.rowModel.isInvitationSent()) {
                                    this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                                } else {
                                    this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                                }
                            } else if (!this.rowModel.isInvitationSent()) {
                                this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                            } else {
                                this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                            }
                        } else if (!this.rowModel.getName().toLowerCase().contains(this.searchText.toLowerCase())) {
                            this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                        } else if (!this.isFilter) {
                            this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                        } else if (this.isPending) {
                            if (this.rowModel.isInvitationSent()) {
                                this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                            } else {
                                this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                            }
                        } else if (!this.rowModel.isInvitationSent()) {
                            this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                        } else {
                            this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                        }
                        this.listMain.set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION_MAIN, 0), this.rowModel);
                    } else {
                        if (this.searchText == null || this.searchText.length() <= 0) {
                            if (!this.isFilter) {
                                this.model.getArrayList().add(this.rowModel);
                            } else if (this.isPending) {
                                if (this.rowModel.isInvitationSent()) {
                                    this.model.getArrayList().add(this.rowModel);
                                }
                            } else if (!this.rowModel.isInvitationSent()) {
                                this.model.getArrayList().add(this.rowModel);
                            }
                        } else if (this.rowModel.getName().toLowerCase().contains(this.searchText.toLowerCase())) {
                            if (!this.isFilter) {
                                this.model.getArrayList().add(this.rowModel);
                            } else if (this.isPending) {
                                if (this.rowModel.isInvitationSent()) {
                                    this.model.getArrayList().add(this.rowModel);
                                }
                            } else if (!this.rowModel.isInvitationSent()) {
                                this.model.getArrayList().add(this.rowModel);
                            }
                        }
                        this.listMain.add(this.rowModel);
                    }
                    notifyAdapter();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public void savePdf() {
        if (this.model.getArrayList().size() > 0) {
            if (isHasPermissions(this, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE")) {
                saveDoc();
                return;
            }
            requestPermissions(this, getString(R.string.rationale_save), Constants.REQUEST_PERM_FILE, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE");
            return;
        }
        AppConstants.toastShort(this.context, this.model.getNoDataText());
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
        try {
            this.document.add(getTable(this.model));
        } catch (DocumentException e2) {
            e2.printStackTrace();
        }
    }

    private PdfPTable getTable(GuestListModel guestListModel) {
        ArrayList<ReportRowModel> fillSubList = fillSubList(guestListModel);
        PdfPTable pdfPTable = new PdfPTable(fillSubList.get(0).getValueList().size());
        float[] fArr = {5.0f, 16.0f, 7.0f, 18.0f, 18.0f, 15.0f, 15.0f, 7.0f};
        pdfPTable.setTotalWidth(100.0f);
        try {
            pdfPTable.setWidths(fArr);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        pdfPTable.getDefaultCell().setVerticalAlignment(1);
        pdfPTable.getDefaultCell().setHorizontalAlignment(1);
        pdfPTable.setWidthPercentage(100.0f);
        pdfPTable.setSpacingBefore(0.0f);
        pdfPTable.setSpacingAfter(0.0f);
        for (int i = 0; i < fillSubList.size(); i++) {
            for (int i2 = 0; i2 < fillSubList.get(i).getValueList().size(); i2++) {
                if (fillSubList.get(i).getValueList().get(i2).equalsIgnoreCase("Not send")) {
                    pdfPTable.addCell((Phrase) new Paragraph(fillSubList.get(i).getValueList().get(i2), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 0, new BaseColor(255, 0, 0))));
                } else if (fillSubList.get(i).getValueList().get(i2).equalsIgnoreCase("Sent")) {
                    pdfPTable.addCell((Phrase) new Paragraph(fillSubList.get(i).getValueList().get(i2), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 0, new BaseColor(0, 128, 0))));
                } else {
                    pdfPTable.addCell(fillSubList.get(i).getValueList().get(i2));
                }
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

    private ArrayList<ReportRowModel> fillSubList(GuestListModel guestListModel) {
        String str;
        String str2;
        ArrayList<ReportRowModel> arrayList = new ArrayList<>();
        arrayList.clear();
        ReportRowModel reportRowModel = new ReportRowModel();
        reportRowModel.setValueList(new ArrayList());
        reportRowModel.getValueList().add("Sr No.");
        reportRowModel.getValueList().add("Name");
        reportRowModel.getValueList().add("Age stage");
        reportRowModel.getValueList().add("Notes");
        reportRowModel.getValueList().add("Phone No");
        reportRowModel.getValueList().add("Email Id");
        reportRowModel.getValueList().add("Address");
        reportRowModel.getValueList().add("Invitation");
        arrayList.add(reportRowModel);
        int i = 0;
        while (i < guestListModel.getArrayList().size()) {
            ReportRowModel reportRowModel2 = new ReportRowModel();
            reportRowModel2.setValueList(new ArrayList());
            ArrayList<String> valueList = reportRowModel2.getValueList();
            StringBuilder sb = new StringBuilder();
            sb.append("");
            int i2 = i + 1;
            sb.append(i2);
            valueList.add(sb.toString());
            reportRowModel2.getValueList().add(guestListModel.getArrayList().get(i).getName());
            ArrayList<String> valueList2 = reportRowModel2.getValueList();
            StringBuilder sb2 = new StringBuilder();
            if (guestListModel.getArrayList().get(i).getStageType() == 1) {
                str = "Adult";
            } else {
                str = guestListModel.getArrayList().get(i).getStageType() == 2 ? "Baby" : "Child";
            }
            sb2.append(str);
            sb2.append(" (");
            sb2.append(guestListModel.getArrayList().get(i).getGenderType() == 1 ? "M" : "F");
            sb2.append(")");
            valueList2.add(sb2.toString());
            reportRowModel2.getValueList().add(guestListModel.getArrayList().get(i).getNote());
            reportRowModel2.getValueList().add(guestListModel.getArrayList().get(i).getPhoneNo());
            reportRowModel2.getValueList().add(guestListModel.getArrayList().get(i).getEmailId());
            reportRowModel2.getValueList().add(guestListModel.getArrayList().get(i).getAddress());
            reportRowModel2.getValueList().add(guestListModel.getArrayList().get(i).isInvitationSent() ? "Sent" : "Not send");
            arrayList.add(reportRowModel2);
            new ArrayList();
            ArrayList<GuestRowModel> arrayList2 = guestListModel.getArrayList().get(i).getArrayList();
            for (int i3 = 0; i3 < arrayList2.size(); i3++) {
                ReportRowModel reportRowModel3 = new ReportRowModel();
                reportRowModel3.setValueList(new ArrayList());
                reportRowModel3.getValueList().add("");
                reportRowModel3.getValueList().add(arrayList2.get(i3).getName());
                ArrayList<String> valueList3 = reportRowModel3.getValueList();
                StringBuilder sb3 = new StringBuilder();
                if (arrayList2.get(i3).getStageType() == 1) {
                    str2 = "Adult";
                } else {
                    str2 = arrayList2.get(i3).getStageType() == 2 ? "Baby" : "Child";
                }
                sb3.append(str2);
                sb3.append(" (");
                sb3.append(arrayList2.get(i3).getGenderType() == 1 ? "M" : "F");
                sb3.append(")");
                valueList3.add(sb3.toString());
                reportRowModel3.getValueList().add(arrayList2.get(i3).getNote());
                reportRowModel3.getValueList().add(arrayList2.get(i3).getPhoneNo());
                reportRowModel3.getValueList().add(arrayList2.get(i3).getEmailId());
                reportRowModel3.getValueList().add(arrayList2.get(i3).getAddress());
                reportRowModel3.getValueList().add("");
                arrayList.add(reportRowModel3);
            }
            i = i2;
        }
        return arrayList;
    }


    public void addingDocFooter() {
        new FooterPageEvent().onEndPage(this.writer, this.document);
        try {
            this.document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void onBackPressed() {
        if (this.isUpdateDashboard) {
            setResult(-1);
        }
        super.onBackPressed();
    }
}
