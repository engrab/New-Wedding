package com.msint.weddingplanner.appBase.view;

import android.app.Dialog;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.adapter.CategoryAdapter;
import com.msint.weddingplanner.appBase.adapter.ImageAdapter;
import com.msint.weddingplanner.appBase.appPref.AppPref;
import com.msint.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.msint.weddingplanner.appBase.models.category.CategoryListModel;
import com.msint.weddingplanner.appBase.models.image.ImageRowModel;
import com.msint.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.msint.weddingplanner.appBase.roomsDB.AppDataBase;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.BackgroundAsync;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.OnAsyncBackground;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import com.msint.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.msint.weddingplanner.databinding.ActivityCategoryListBinding;
import com.msint.weddingplanner.databinding.AlertDialogNewCategoryBinding;
import java.util.ArrayList;

public class CategoryListActivity extends BaseActivityRecyclerBinding {

    public ActivityCategoryListBinding binding;


    /* renamed from: db */
    public AppDataBase f549db;

    public Dialog dialogNewCat;

    public AlertDialogNewCategoryBinding dialogNewCatBinding;

    public ArrayList<ImageRowModel> imageList;

    public CategoryListModel model;

    public int selectedCatPos = 0;

    public int selectedNewCatPos = 0;
    private ToolbarModel toolbarModel;


    public void callApi() {
    }


    public void setBinding() {
        this.binding = (ActivityCategoryListBinding) DataBindingUtil.setContentView(this, R.layout.activity_category_list);
        this.model = new CategoryListModel();
        this.model.setArrayList(new ArrayList());
        this.model.setNoDataIcon(R.drawable.dummy_empty);
        this.model.setNoDataText(getString(R.string.noDataTitleCategory));
        this.model.setNoDataDetail(getString(R.string.noDataDescCategory));
        this.binding.setModel(this.model);
        this.f549db = AppDataBase.getAppDatabase(this.context);
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle("Manage Category");
        this.binding.includedToolbar.setModel(this.toolbarModel);
    }


    public void setOnClicks() {
        this.binding.includedToolbar.imgBack.setOnClickListener(this);
        this.binding.fabAdd.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fabAdd) {
            showNewCatList(-1);
        } else if (id == R.id.imgBack) {
            onBackPressed();
        }
    }


    public void fillData() {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                CategoryListActivity.this.fillFromDB();
            }

            public void onPostExecute() {
                CategoryListActivity.this.notifyAdapter();
            }
        }).execute(new Object[0]);
    }


    public void fillFromDB() {
        try {
            this.model.getArrayList().addAll(this.f549db.categoryDao().getAll());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setRecycler() {
        this.binding.recycler.setLayoutManager(new LinearLayoutManager(this.context));
        this.binding.recycler.setAdapter(new CategoryAdapter(this.context, true, this.model.getArrayList(), new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                if (i2 == 2) {
                    CategoryListActivity.this.deleteItem(i);
                } else {
                    CategoryListActivity.this.showNewCatList(i);
                }
            }
        }));
        this.binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                if (i2 > 0 && CategoryListActivity.this.binding.fabAdd.getVisibility() == 0) {
                    CategoryListActivity.this.binding.fabAdd.hide();
                } else if (i2 < 0 && CategoryListActivity.this.binding.fabAdd.getVisibility() != 0) {
                    CategoryListActivity.this.binding.fabAdd.show();
                }
            }
        });
    }


    public void notifyAdapter() {
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


    public void initMethods() {
        newCatDialogSetup();
    }

    public void deleteItem(final int i) {
        AppConstants.showTwoButtonDialog(this.context, getString(R.string.app_name), getString(R.string.delete_msg) + "<br /> <b>" + this.model.getArrayList().get(i).getName() + "</b> <br />" + getString(R.string.delete_all_related_data) + " category", true, true, getString(R.string.delete), getString(R.string.cancel), new TwoButtonDialogListener() {
            public void onCancel() {
            }

            public void onOk() {
                CategoryListActivity.this.deleteCategory(i);
            }
        });
    }


    public void deleteCategory(final int i) {
        new BackgroundAsync(this.context, true, "", new OnAsyncBackground() {
            public void onPreExecute() {
            }

            public void doInBackground() {
                ArrayList arrayList = new ArrayList();
                try {
                    arrayList.addAll(CategoryListActivity.this.f549db.taskDao().getAll(AppPref.getCurrentEvenId(CategoryListActivity.this.context), CategoryListActivity.this.model.getArrayList().get(i).getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    try {
                        CategoryListActivity.this.f549db.subTaskDao().deleteAll((String) arrayList.get(i));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    try {
                        CategoryListActivity.this.f549db.taskDao().delete(AppPref.getCurrentEvenId(CategoryListActivity.this.context), (String) arrayList.get(i));
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                ArrayList arrayList2 = new ArrayList();
                try {
                    arrayList2.addAll(CategoryListActivity.this.f549db.costDao().getAll(AppPref.getCurrentEvenId(CategoryListActivity.this.context), CategoryListActivity.this.model.getArrayList().get(i).getId()));
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    try {
                        CategoryListActivity.this.f549db.paymentDao().deleteAll((String) arrayList2.get(i2));
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    try {
                        CategoryListActivity.this.f549db.costDao().delete(AppPref.getCurrentEvenId(CategoryListActivity.this.context), (String) arrayList2.get(i2));
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
                ArrayList arrayList3 = new ArrayList();
                try {
                    arrayList3.addAll(CategoryListActivity.this.f549db.vendorDao().getAll(AppPref.getCurrentEvenId(CategoryListActivity.this.context), CategoryListActivity.this.model.getArrayList().get(i).getId()));
                } catch (Exception e7) {
                    e7.printStackTrace();
                }
                for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                    try {
                        CategoryListActivity.this.f549db.paymentDao().deleteAll((String) arrayList3.get(i3));
                    } catch (Exception e8) {
                        e8.printStackTrace();
                    }
                    try {
                        CategoryListActivity.this.f549db.vendorDao().delete(AppPref.getCurrentEvenId(CategoryListActivity.this.context), (String) arrayList3.get(i3));
                    } catch (Exception e9) {
                        e9.printStackTrace();
                    }
                }
            }

            public void onPostExecute() {
                try {
                    CategoryListActivity.this.f549db.categoryDao().delete(CategoryListActivity.this.model.getArrayList().get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CategoryListActivity.this.model.getArrayList().remove(i);
                CategoryListActivity.this.notifyAdapter();
            }
        }).execute(new Object[0]);
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
    }

    public void setNewCatDialog() {
        this.dialogNewCatBinding = (AlertDialogNewCategoryBinding) DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.alert_dialog_new_category, (ViewGroup) null, false);
        this.dialogNewCat = new Dialog(this.context);
        this.dialogNewCat.setContentView(this.dialogNewCatBinding.getRoot());
        this.dialogNewCat.setCancelable(false);
        this.dialogNewCat.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogNewCat.getWindow().setLayout(-1, -2);
        this.dialogNewCatBinding.txtTitle.setText(R.string.add_new_category);
        this.dialogNewCatBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context, 0, false));
        this.dialogNewCatBinding.recycler.setAdapter(new ImageAdapter(true, this.context, this.imageList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                int unused = CategoryListActivity.this.selectedNewCatPos = i;
            }
        }));
        this.dialogNewCatBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    CategoryListActivity.this.dialogNewCat.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.dialogNewCatBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            /* JADX WARNING: Removed duplicated region for block: B:32:0x00ec  */
            /* JADX WARNING: Unknown top exception splitter block from list: {B:24:0x00d3=Splitter:B:24:0x00d3, B:14:0x00a4=Splitter:B:14:0x00a4} */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onClick(View r7) {
                /*
                    r6 = this;
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r7 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r0 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this
                    com.msint.weddingplanner.databinding.AlertDialogNewCategoryBinding r0 = r0.dialogNewCatBinding
                    boolean r7 = r7.isValidNewCat(r0)
                    if (r7 == 0) goto L_0x00f1
                    com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel r7 = new com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel
                    r7.<init>()
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r0 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this
                    com.msint.weddingplanner.databinding.AlertDialogNewCategoryBinding r0 = r0.dialogNewCatBinding
                    android.widget.EditText r0 = r0.etName
                    android.text.Editable r0 = r0.getText()
                    java.lang.String r0 = r0.toString()
                    java.lang.String r0 = r0.trim()
                    r7.setName(r0)
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r0 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this
                    java.util.ArrayList r0 = r0.imageList
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r1 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this
                    int r1 = r1.selectedNewCatPos
                    java.lang.Object r0 = r0.get(r1)
                    com.msint.weddingplanner.appBase.models.image.ImageRowModel r0 = (com.msint.weddingplanner.appBase.models.image.ImageRowModel) r0
                    java.lang.String r0 = r0.getId()
                    r7.setIconType(r0)
                    r0 = 0
                    java.lang.String r2 = r7.getName()     // Catch:{ Exception -> 0x00e3 }
                    r2.trim()     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r2 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00e3 }
                    int r2 = r2.selectedCatPos     // Catch:{ Exception -> 0x00e3 }
                    r3 = -1
                    if (r2 == r3) goto L_0x00b8
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r2 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.models.category.CategoryListModel r2 = r2.model     // Catch:{ Exception -> 0x00e3 }
                    java.util.ArrayList r2 = r2.getArrayList()     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r3 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00e3 }
                    int r3 = r3.selectedCatPos     // Catch:{ Exception -> 0x00e3 }
                    java.lang.Object r2 = r2.get(r3)     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel r2 = (com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel) r2     // Catch:{ Exception -> 0x00e3 }
                    java.lang.String r2 = r2.getId()     // Catch:{ Exception -> 0x00e3 }
                    r7.setId(r2)     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r2 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.models.category.CategoryListModel r2 = r2.model     // Catch:{ Exception -> 0x00e3 }
                    java.util.ArrayList r2 = r2.getArrayList()     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r3 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00e3 }
                    int r3 = r3.selectedCatPos     // Catch:{ Exception -> 0x00e3 }
                    java.lang.Object r2 = r2.get(r3)     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel r2 = (com.msint.weddingplanner.appBase.roomsDB.category.CategoryRowModel) r2     // Catch:{ Exception -> 0x00e3 }
                    boolean r2 = r2.isDefault()     // Catch:{ Exception -> 0x00e3 }
                    r7.setDefault(r2)     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r2 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x009f }
                    com.msint.weddingplanner.appBase.roomsDB.AppDataBase r2 = r2.f549db     // Catch:{ Exception -> 0x009f }
                    com.msint.weddingplanner.appBase.roomsDB.category.CategoryDao r2 = r2.categoryDao()     // Catch:{ Exception -> 0x009f }
                    int r2 = r2.update(r7)     // Catch:{ Exception -> 0x009f }
                    long r2 = (long) r2
                    goto L_0x00a4
                L_0x009f:
                    r2 = move-exception
                    r2.printStackTrace()     // Catch:{ Exception -> 0x00e3 }
                    r2 = r0
                L_0x00a4:
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r4 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00e1 }
                    com.msint.weddingplanner.appBase.models.category.CategoryListModel r4 = r4.model     // Catch:{ Exception -> 0x00e1 }
                    java.util.ArrayList r4 = r4.getArrayList()     // Catch:{ Exception -> 0x00e1 }
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r5 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00e1 }
                    int r5 = r5.selectedCatPos     // Catch:{ Exception -> 0x00e1 }
                    r4.set(r5, r7)     // Catch:{ Exception -> 0x00e1 }
                    goto L_0x00e8
                L_0x00b8:
                    java.lang.String r2 = com.msint.weddingplanner.appBase.utils.AppConstants.getUniqueId()     // Catch:{ Exception -> 0x00e3 }
                    r7.setId(r2)     // Catch:{ Exception -> 0x00e3 }
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r2 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00ce }
                    com.msint.weddingplanner.appBase.roomsDB.AppDataBase r2 = r2.f549db     // Catch:{ Exception -> 0x00ce }
                    com.msint.weddingplanner.appBase.roomsDB.category.CategoryDao r2 = r2.categoryDao()     // Catch:{ Exception -> 0x00ce }
                    long r2 = r2.insert(r7)     // Catch:{ Exception -> 0x00ce }
                    goto L_0x00d3
                L_0x00ce:
                    r2 = move-exception
                    r2.printStackTrace()     // Catch:{ Exception -> 0x00e3 }
                    r2 = r0
                L_0x00d3:
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r4 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00e1 }
                    com.msint.weddingplanner.appBase.models.category.CategoryListModel r4 = r4.model     // Catch:{ Exception -> 0x00e1 }
                    java.util.ArrayList r4 = r4.getArrayList()     // Catch:{ Exception -> 0x00e1 }
                    r4.add(r7)     // Catch:{ Exception -> 0x00e1 }
                    goto L_0x00e8
                L_0x00e1:
                    r7 = move-exception
                    goto L_0x00e5
                L_0x00e3:
                    r7 = move-exception
                    r2 = r0
                L_0x00e5:
                    r7.printStackTrace()
                L_0x00e8:
                    int r7 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
                    if (r7 <= 0) goto L_0x00f1
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r7 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this
                    r7.notifyAdapter()
                L_0x00f1:
                    com.msint.weddingplanner.appBase.view.CategoryListActivity r7 = com.msint.weddingplanner.appBase.view.CategoryListActivity.this     // Catch:{ Exception -> 0x00fb }
                    android.app.Dialog r7 = r7.dialogNewCat     // Catch:{ Exception -> 0x00fb }
                    r7.dismiss()     // Catch:{ Exception -> 0x00fb }
                    goto L_0x00ff
                L_0x00fb:
                    r7 = move-exception
                    r7.printStackTrace()
                L_0x00ff:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.msint.weddingplanner.appBase.view.CategoryListActivity.C09638.onClick(android.view.View):void");
            }
        });
    }


    public boolean isValidNewCat(AlertDialogNewCategoryBinding alertDialogNewCategoryBinding) {
        Context context = this.context;
        EditText editText = alertDialogNewCategoryBinding.etName;
        return AppConstants.isNotEmpty(context, editText, getString(R.string.please_enter) + " " + getString(R.string.name));
    }


    public void showNewCatList(int i) {
        this.selectedCatPos = i;
        if (this.selectedCatPos != -1) {
            this.dialogNewCatBinding.etName.setText(this.model.getArrayList().get(this.selectedCatPos).getName());
            this.selectedNewCatPos = getPositionFromId(this.model.getArrayList().get(this.selectedCatPos).getIconType());
        } else {
            this.selectedNewCatPos = 0;
            this.dialogNewCatBinding.etName.setText("");
        }
        selectionAllCategory(false);
        this.imageList.get(this.selectedNewCatPos).setSelected(true);
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

    private void selectionAllCategory(boolean z) {
        for (int i = 0; i < this.imageList.size(); i++) {
            this.imageList.get(i).setSelected(z);
        }
    }

    private int getPositionFromId(String str) {
        for (int i = 0; i < this.imageList.size(); i++) {
            if (this.imageList.get(i).getId().equalsIgnoreCase(str)) {
                return i;
            }
        }
        return 0;
    }
}
