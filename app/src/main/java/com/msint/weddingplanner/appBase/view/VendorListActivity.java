package com.msint.weddingplanner.appBase.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p004v7.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.support.p004v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.adapter.SelectionAdapter;
import com.msint.weddingplanner.appBase.adapter.VendorAdapter;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.msint.weddingplanner.appBase.models.selection.SelectionRowModel;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.models.vendor.VendorListModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.msint.weddingplanner.appBase.roomsDB.payment.PaymentRowModel;
import com.msint.weddingplanner.appBase.roomsDB.vendor.VendorRowModel;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.BackgroundAsync;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.OnAsyncBackground;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.msint.weddingplanner.databinding.ActivityVendorListBinding;
import com.msint.weddingplanner.databinding.AlertDialogRecyclerListBinding;
import com.msint.weddingplanner.pdfRepo.ReportRowModel;
import com.msint.weddingplanner.pdfRepo.ReportsListActivity;
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

public class VendorListActivity extends BaseActivityRecyclerBinding implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static String EXTRA_IS_FILTER = "EXTRA_IS_FILTER";
    public static String EXTRA_IS_PENDING = "EXTRA_IS_PENDING";

    public ActivityVendorListBinding binding;

    /* renamed from: db */
    private AppDataBase f558db;

    public Dialog dialogFilterTypeList;
    private AlertDialogRecyclerListBinding dialogFilterTypeListBinding;

    public Dialog dialogOrderTypeList;
    private AlertDialogRecyclerListBinding dialogOrderTypeListBinding;
    private File dir;
    private Document document;
    private String fileName = null;

    public ArrayList<SelectionRowModel> filterTypeList;

    public boolean isFilter = false;
    private boolean isPending = false;
    private boolean isUpdateDashboard = false;

    public ArrayList<VendorRowModel> listMain;

    public VendorListModel model;

    public ArrayList<SelectionRowModel> orderTypeList;
    private Paragraph paragraph;
    private String repoTitle = "List";
    private String repoType = "Vendor";
    VendorRowModel rowModel = null;
    private String searchText = "";

    public int selectedFilterTypePos = 0;

    public int selectedOrderTypePos = 0;
    private String subTitle = "Payments";
    private ToolbarModel toolbarModel;
    private PdfWriter writer = null;

    public void onRationaleAccepted(int i) {
    }

    public void onRationaleDenied(int i) {
    }

    /* access modifiers changed from: protected */
    public void setBinding() {
        this.binding = (ActivityVendorListBinding) DataBindingUtil.setContentView(this, R.layout.activity_vendor_list);
        this.model = new VendorListModel();
        this.model.setArrayList(new ArrayList());
        this.listMain = new ArrayList<>();
        this.model.setNoDataIcon(R.drawable.drawer_vendors);
        this.model.setNoDataText(getString(R.string.noDataTitleVendors));
        this.model.setNoDataDetail(getString(R.string.noDataDescVendors));
        this.binding.setModel(this.model);
        this.f558db = AppDataBase.getAppDatabase(this.context);
    }

    /* access modifiers changed from: protected */
    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle(getString(R.string.drawerTitleVendors));
        this.toolbarModel.setOtherMenu(true);
        this.toolbarModel.setSearchMenu(true);
        this.binding.includedToolbar.imgOther.setImageResource(R.drawable.order_list);
        this.binding.includedToolbar.imgAdd.setImageResource(R.drawable.summary);
        this.toolbarModel.setShare(true);
        this.binding.includedToolbar.setModel(this.toolbarModel);
        setSupportActionBar(this.binding.includedToolbar.toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.shareList) {
            showPdfDialog();
            return true;
        } else if (itemId != R.id.summary) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            openSummary();
            return true;
        }
    }

    private void openSummary() {
        startActivity(new Intent(this.context, VendorSummaryActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
    }

    /* access modifiers changed from: protected */
    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.includedToolbar.imgOther.setOnClickListener(this);
        this.binding.includedToolbar.imgAdd.setOnClickListener(this);
        this.binding.includedToolbar.imgShare.setOnClickListener(this);
        this.binding.fabAdd.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAdd:
                openItemDetail(-1, -1, new VendorRowModel(), false);
                return;
            case R.id.imgAdd:
                openSummary();
                return;
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

    /* access modifiers changed from: protected */
    public void callApi() {
        setupFilter();
    }

    /* access modifiers changed from: protected */
    public void fillData() {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                VendorListActivity.this.fillFromDB();
            }

            public void onPostExecute() {
                VendorListActivity.this.notifyAdapter();
            }
        }).execute(new Object[0]);
    }


    public void fillFromDB() {
        List arrayList = new ArrayList();
        try {
            arrayList = this.f558db.vendorDao().getAll(AppPref.getCurrentEvenId(this.context));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (arrayList.size() > 0) {
            if (arrayList.size() > 0) {
                CategoryRowModel categoryRowModel = null;
                for (int i = 0; i < arrayList.size(); i++) {
                    VendorRowModel vendorRowModel = (VendorRowModel) arrayList.get(i);
                    try {
                        categoryRowModel = this.f558db.categoryDao().getDetail(vendorRowModel.getCategoryId());
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    vendorRowModel.setCategoryRowModel(categoryRowModel);
                    try {
                        vendorRowModel.setPendingAmount(this.f558db.paymentDao().getTotal(vendorRowModel.getId(), 1));
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    try {
                        vendorRowModel.setPaidAmount(this.f558db.paymentDao().getTotal(vendorRowModel.getId(), 0));
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    vendorRowModel.setArrayList(new ArrayList());
                    try {
                        vendorRowModel.getArrayList().addAll(this.f558db.paymentDao().getAll(vendorRowModel.getId()));
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    this.listMain.add(vendorRowModel);
                }
            }
            checkFilterAndFillList();
        }
    }

    /* access modifiers changed from: protected */
    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new VendorAdapter(this.context, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                VendorListActivity.this.openItemDetail(i, VendorListActivity.this.listMain.indexOf(VendorListActivity.this.model.getArrayList().get(i)), VendorListActivity.this.model.getArrayList().get(i), true);
            }
        }));
        this.binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (i2 > 0 && VendorListActivity.this.binding.fabAdd.getVisibility() == 0) {
                    VendorListActivity.this.binding.fabAdd.hide();
                } else if (i2 < 0 && VendorListActivity.this.binding.fabAdd.getVisibility() != 0) {
                    VendorListActivity.this.binding.fabAdd.show();
                }
            }
        });
    }


    public void openItemDetail(int i, int i2, VendorRowModel vendorRowModel, boolean z) {
        Intent intent = new Intent(this.context, AddEditVendorActivity.class);
        intent.putExtra(AddEditVendorActivity.EXTRA_IS_EDIT, z);
        intent.putExtra(AddEditVendorActivity.EXTRA_POSITION, i);
        intent.putExtra(AddEditVendorActivity.EXTRA_POSITION_MAIN, i2);
        intent.putExtra(AddEditVendorActivity.EXTRA_MODEL, vendorRowModel);
        intent.setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE);
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
        this.binding.linData.setVisibility(this.model.isListData() ? 0 : 8);
        LinearLayout linearLayout = this.binding.linNoData;
        if (!this.model.isListData()) {
            i = 0;
        }
        linearLayout.setVisibility(i);
    }

    /* access modifiers changed from: protected */
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
        this.orderTypeList.add(new SelectionRowModel(Constants.ORDER_TYPE_AMOUNT_ASC));
        this.orderTypeList.add(new SelectionRowModel(Constants.ORDER_TYPE_AMOUNT_DESC));
        this.selectedOrderTypePos = getSelectedPosById();
        this.orderTypeList.get(this.selectedOrderTypePos).setSelected(true);
    }

    private int getSelectedPosById() {
        for (int i = 0; i < this.orderTypeList.size(); i++) {
            if (this.orderTypeList.get(i).getLabel().equalsIgnoreCase(AppPref.getSortTypeVendor(this.context))) {
                return i;
            }
        }
        return 0;
    }

    public void setOrderTypeListDialog() {
        this.dialogOrderTypeListBinding = (AlertDialogRecyclerListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_recycler_list, (ViewGroup) null, false);
        this.dialogOrderTypeList = new Dialog(this.context);
        this.dialogOrderTypeList.setContentView(this.dialogOrderTypeListBinding.getRoot());
        this.dialogOrderTypeList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogOrderTypeList.getWindow().setLayout(-1, -2);
        this.dialogOrderTypeListBinding.txtTitle.setText(R.string.select_list_order_type);
        this.dialogOrderTypeListBinding.btnOk.setText(R.string.set);
        this.dialogOrderTypeListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogOrderTypeListBinding.recycler.setAdapter(new SelectionAdapter(this.context, true, this.orderTypeList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                int unused = VendorListActivity.this.selectedOrderTypePos = i;
                AppPref.setSortTypeVendor(VendorListActivity.this.context, ((SelectionRowModel) VendorListActivity.this.orderTypeList.get(VendorListActivity.this.selectedOrderTypePos)).getLabel());
                VendorListActivity.this.notifyAdapter();
                try {
                    VendorListActivity.this.dialogOrderTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        this.dialogOrderTypeListBinding.imgAdd.setImageResource(R.drawable.close);
        this.dialogOrderTypeListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    VendorListActivity.this.dialogOrderTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogOrderTypeListBinding.linButton.setVisibility(View.GONE);
        this.dialogOrderTypeListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    VendorListActivity.this.dialogOrderTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogOrderTypeListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppPref.setSortTypeVendor(VendorListActivity.this.context, ((SelectionRowModel) VendorListActivity.this.orderTypeList.get(VendorListActivity.this.selectedOrderTypePos)).getLabel());
                VendorListActivity.this.notifyAdapter();
                try {
                    VendorListActivity.this.dialogOrderTypeList.dismiss();
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

    /* JADX WARNING: Removed duplicated region for block: B:22:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sortBy() {
        /*
            r3 = this;
            android.content.Context r0 = r3.context
            java.lang.String r0 = com.msint.weddingplanner.appBase.appPref.AppPref.getSortTypeVendor(r0)
            int r1 = r0.hashCode()
            r2 = -1090930781(0xffffffffbef9b7a3, float:-0.48772916)
            if (r1 == r2) goto L_0x003d
            r2 = -933566160(0xffffffffc85ae930, float:-224164.75)
            if (r1 == r2) goto L_0x0033
            r2 = -422524019(0xffffffffe6d0cb8d, float:-4.9300354E23)
            if (r1 == r2) goto L_0x0029
            r2 = 160811936(0x995cba0, float:3.6061933E-33)
            if (r1 == r2) goto L_0x001f
            goto L_0x0047
        L_0x001f:
            java.lang.String r1 = "Amount descending"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0047
            r0 = 3
            goto L_0x0048
        L_0x0029:
            java.lang.String r1 = "Name descending"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0047
            r0 = 1
            goto L_0x0048
        L_0x0033:
            java.lang.String r1 = "Amount ascending"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0047
            r0 = 2
            goto L_0x0048
        L_0x003d:
            java.lang.String r1 = "Name ascending"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0047
            r0 = 0
            goto L_0x0048
        L_0x0047:
            r0 = -1
        L_0x0048:
            switch(r0) {
                case 0: goto L_0x0079;
                case 1: goto L_0x006a;
                case 2: goto L_0x005b;
                case 3: goto L_0x004c;
                default: goto L_0x004b;
            }
        L_0x004b:
            goto L_0x0087
        L_0x004c:
            com.msint.weddingplanner.appBase.models.vendor.VendorListModel r0 = r3.model
            java.util.ArrayList r0 = r0.getArrayList()
            com.msint.weddingplanner.appBase.view.VendorListActivity$11 r1 = new com.msint.weddingplanner.appBase.view.VendorListActivity$11
            r1.<init>()
            java.util.Collections.sort(r0, r1)
            goto L_0x0087
        L_0x005b:
            com.msint.weddingplanner.appBase.models.vendor.VendorListModel r0 = r3.model
            java.util.ArrayList r0 = r0.getArrayList()
            com.msint.weddingplanner.appBase.view.VendorListActivity$10 r1 = new com.msint.weddingplanner.appBase.view.VendorListActivity$10
            r1.<init>()
            java.util.Collections.sort(r0, r1)
            goto L_0x0087
        L_0x006a:
            com.msint.weddingplanner.appBase.models.vendor.VendorListModel r0 = r3.model
            java.util.ArrayList r0 = r0.getArrayList()
            com.msint.weddingplanner.appBase.view.VendorListActivity$9 r1 = new com.msint.weddingplanner.appBase.view.VendorListActivity$9
            r1.<init>()
            java.util.Collections.sort(r0, r1)
            goto L_0x0087
        L_0x0079:
            com.msint.weddingplanner.appBase.models.vendor.VendorListModel r0 = r3.model
            java.util.ArrayList r0 = r0.getArrayList()
            com.msint.weddingplanner.appBase.view.VendorListActivity$8 r1 = new com.msint.weddingplanner.appBase.view.VendorListActivity$8
            r1.<init>()
            java.util.Collections.sort(r0, r1)
        L_0x0087:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.msint.weddingplanner.appBase.view.VendorListActivity.sortBy():void");
    }

    private void setSearch() {
        this.binding.includedToolbar.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String str) {
                return false;
            }

            public boolean onQueryTextChange(String str) {
                VendorListActivity.this.updateList(str);
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
                VendorRowModel vendorRowModel = this.listMain.get(i);
                if (vendorRowModel.getName().toLowerCase().contains(str.trim().toLowerCase())) {
                    if (!this.isFilter) {
                        this.model.getArrayList().add(vendorRowModel);
                    } else if (this.isPending) {
                        if (this.listMain.get(i).isPending()) {
                            this.model.getArrayList().add(vendorRowModel);
                        }
                    } else if (!this.listMain.get(i).isPending()) {
                        this.model.getArrayList().add(vendorRowModel);
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
        this.filterTypeList.add(new SelectionRowModel(Constants.FILTER_TYPE_PENDING, "Show pending only"));
        this.filterTypeList.add(new SelectionRowModel(Constants.FILTER_TYPE_COMPLETED, "Show paid only"));
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
        this.dialogFilterTypeListBinding = (AlertDialogRecyclerListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_recycler_list, (ViewGroup) null, false);
        this.dialogFilterTypeList = new Dialog(this.context);
        this.dialogFilterTypeList.setContentView(this.dialogFilterTypeListBinding.getRoot());
        this.dialogFilterTypeList.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogFilterTypeList.getWindow().setLayout(-1, -2);
        this.dialogFilterTypeListBinding.txtTitle.setText(R.string.select_list_filter_type);
        this.dialogFilterTypeListBinding.btnOk.setText(R.string.set);
        this.dialogFilterTypeListBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.dialogFilterTypeListBinding.recycler.setAdapter(new SelectionAdapter(this.context, true, this.filterTypeList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                int unused = VendorListActivity.this.selectedFilterTypePos = i;
                AppPref.setSortTypeTask(VendorListActivity.this.context, ((SelectionRowModel) VendorListActivity.this.filterTypeList.get(VendorListActivity.this.selectedFilterTypePos)).getLabel());
                VendorListActivity.this.filterList(((SelectionRowModel) VendorListActivity.this.filterTypeList.get(VendorListActivity.this.selectedFilterTypePos)).getId());
                try {
                    VendorListActivity.this.dialogFilterTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        this.dialogFilterTypeListBinding.imgAdd.setImageResource(R.drawable.close);
        this.dialogFilterTypeListBinding.imgAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    VendorListActivity.this.dialogFilterTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogFilterTypeListBinding.linButton.setVisibility(View.GONE);
        this.dialogFilterTypeListBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    VendorListActivity.this.dialogFilterTypeList.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogFilterTypeListBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AppPref.setSortTypeTask(VendorListActivity.this.context, ((SelectionRowModel) VendorListActivity.this.filterTypeList.get(VendorListActivity.this.selectedFilterTypePos)).getLabel());
                VendorListActivity.this.notifyAdapter();
                try {
                    VendorListActivity.this.dialogFilterTypeList.dismiss();
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
            this.isPending = true;
        } else {
            this.isPending = false;
        }
        filterList();
    }

    private void filterList() {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
                VendorListActivity.this.setupFilterIcon(VendorListActivity.this.isFilter);
            }

            public void doInBackground() {
                VendorListActivity.this.checkFilterAndFillList();
            }

            public void onPostExecute() {
                VendorListActivity.this.notifyAdapter();
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
                if (this.listMain.get(i).isPending()) {
                    this.model.getArrayList().add(this.listMain.get(i));
                }
            } else if (!this.listMain.get(i).isPending()) {
                this.model.getArrayList().add(this.listMain.get(i));
            }
        }
    }

    /* access modifiers changed from: protected */
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
                    this.rowModel = (VendorRowModel) intent.getParcelableExtra(AddEditTaskActivity.EXTRA_MODEL);
                    if (intent.getBooleanExtra(AddEditTaskActivity.EXTRA_IS_DELETED, false)) {
                        this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                        this.listMain.remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION_MAIN, 0));
                    } else if (intent.getBooleanExtra(AddEditTaskActivity.EXTRA_IS_EDIT, false)) {
                        if (this.searchText == null || this.searchText.length() <= 0) {
                            if (!this.isFilter) {
                                this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                            } else if (this.isPending) {
                                if (this.rowModel.isPending()) {
                                    this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                                } else {
                                    this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                                }
                            } else if (!this.rowModel.isPending()) {
                                this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                            } else {
                                this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                            }
                        } else if (!this.rowModel.getName().toLowerCase().contains(this.searchText.toLowerCase())) {
                            this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                        } else if (!this.isFilter) {
                            this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                        } else if (this.isPending) {
                            if (this.rowModel.isPending()) {
                                this.model.getArrayList().set(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0), this.rowModel);
                            } else {
                                this.model.getArrayList().remove(intent.getIntExtra(AddEditTaskActivity.EXTRA_POSITION, 0));
                            }
                        } else if (!this.rowModel.isPending()) {
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
                                if (this.rowModel.isPending()) {
                                    this.model.getArrayList().add(this.rowModel);
                                }
                            } else if (!this.rowModel.isPending()) {
                                this.model.getArrayList().add(this.rowModel);
                            }
                        } else if (this.rowModel.getName().toLowerCase().contains(this.searchText.toLowerCase())) {
                            if (!this.isFilter) {
                                this.model.getArrayList().add(this.rowModel);
                            } else if (this.isPending) {
                                if (this.rowModel.isPending()) {
                                    this.model.getArrayList().add(this.rowModel);
                                }
                            } else if (!this.rowModel.isPending()) {
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

    private void showPdfDialog() {
        AppConstants.pdfReportDialog(this.context, new TwoButtonDialogListener() {
            public void onOk() {
                VendorListActivity.this.savePdf();
            }

            public void onCancel() {
                VendorListActivity.this.openReportList();
            }
        });
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
                VendorListActivity.this.initDoc();
            }

            public void doInBackground() {
                VendorListActivity.this.fillDocData();
            }

            public void onPostExecute() {
                VendorListActivity.this.addingDocFooter();
            }
        }).execute(new Object[0]);
    }


    public void initDoc() {
        this.document = new Document(PageSize.f191A4, 16.0f, 16.0f, 16.0f, 16.0f);
        this.dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.REPORT_DIRECTORY);
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
        try {
            this.fileName = this.repoType + "_" + this.repoTitle + "_" + getCurrentDateTime() + ".pdf";
            this.writer = PdfWriter.getInstance(this.document, new FileOutputStream(new File(this.dir, this.fileName)));
        } catch (FileNotFoundException e) {
            try {
                e.printStackTrace();
            } catch (DocumentException e2) {
                e2.printStackTrace();
            }
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
        int i = 0;
        while (i < this.model.getArrayList().size()) {
            this.paragraph = new Paragraph("");
            this.paragraph.add((Element) new Paragraph(" "));
            try {
                this.document.add(this.paragraph);
            } catch (DocumentException e2) {
                e2.printStackTrace();
            }
            VendorRowModel vendorRowModel = this.model.getArrayList().get(i);
            Paragraph paragraph2 = this.paragraph;
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            i++;
            sb.append(i);
            sb.append(") Name : ");
            sb.append(vendorRowModel.getName());
            paragraph2.add((Element) new Paragraph(sb.toString(), new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, 1, BaseColor.BLUE)));
            Paragraph paragraph3 = this.paragraph;
            paragraph3.add((Element) new Paragraph("Category : " + vendorRowModel.getCategoryRowModel().getName(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
            Paragraph paragraph4 = this.paragraph;
            paragraph4.add((Element) new Paragraph("Estimated amount : " + vendorRowModel.getExpectedAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
            Paragraph paragraph5 = this.paragraph;
            paragraph5.add((Element) new Paragraph("Balance : " + vendorRowModel.getBalanceFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
            Paragraph paragraph6 = this.paragraph;
            paragraph6.add((Element) new Paragraph("Paid : " + vendorRowModel.getPaidAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
            Paragraph paragraph7 = this.paragraph;
            paragraph7.add((Element) new Paragraph("Pending : " + vendorRowModel.getPendingAmountFormatted(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
            Paragraph paragraph8 = new Paragraph();
            paragraph8.add((Element) new Chunk("Status : ", new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1)));
            paragraph8.add((Element) new Chunk(vendorRowModel.getStatusText(), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 1, vendorRowModel.getStatusText().equalsIgnoreCase("Paid") ? new BaseColor(0, 128, 0) : new BaseColor(255, 0, 0))));
            this.paragraph.add((Element) paragraph8);
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
                if (fillSubList.get(i).getValueList().get(i2).equalsIgnoreCase("Pending")) {
                    pdfPTable.addCell((Phrase) new Paragraph(fillSubList.get(i).getValueList().get(i2), new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, 0, new BaseColor(255, 0, 0))));
                } else if (fillSubList.get(i).getValueList().get(i2).equalsIgnoreCase("Paid")) {
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


    public void addingDocFooter() {
        new FooterPageEvent().onEndPage(this.writer, this.document);
        try {
            this.document.close();
            openReportList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openReportList() {
        startActivity(new Intent(this, ReportsListActivity.class).setFlags(PagedChannelRandomAccessSource.DEFAULT_TOTAL_BUFSIZE));
    }

    public class FooterPageEvent extends PdfPageEventHelper {
        public FooterPageEvent() {
        }

        public void onEndPage(PdfWriter pdfWriter, Document document) {
            try {
                PdfContentByte directContent = pdfWriter.getDirectContent();
                ColumnText.showTextAligned(directContent, 1, new Phrase("Created by : " + VendorListActivity.this.getString(R.string.app_name), new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, 1)), document.leftMargin() + ((document.right() - document.left()) / 2.0f), document.bottom() + 10.0f, 0.0f);
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
