package com.example.weddingplanner.appBase.view;

import android.app.Dialog;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.weddingplanner.R;
import com.example.weddingplanner.appBase.adapter.CategoryAdapter;
import com.example.weddingplanner.appBase.adapter.ImageAdapter;
import com.example.weddingplanner.appBase.appPref.AppPref;
import com.example.weddingplanner.appBase.baseClass.BaseActivityRecyclerBinding;
import com.example.weddingplanner.appBase.models.category.CategoryListModel;
import com.example.weddingplanner.appBase.models.image.ImageRowModel;
import com.example.weddingplanner.appBase.models.toolbar.ToolbarModel;
import com.example.weddingplanner.appBase.roomsDB.AppDataBase;
import com.example.weddingplanner.appBase.roomsDB.category.CategoryRowModel;
import com.example.weddingplanner.appBase.utils.AppConstants;
import com.example.weddingplanner.appBase.utils.BackgroundAsync;
import com.example.weddingplanner.appBase.utils.Constants;
import com.example.weddingplanner.appBase.utils.OnAsyncBackground;
import com.example.weddingplanner.appBase.utils.RecyclerItemClick;
import com.example.weddingplanner.appBase.utils.TwoButtonDialogListener;
import com.example.weddingplanner.databinding.ActivityCategoryListBinding;
import com.example.weddingplanner.databinding.AlertDialogNewCategoryBinding;

import java.util.ArrayList;

public class CategoryListActivity extends BaseActivityRecyclerBinding {

    public ActivityCategoryListBinding binding;


    public AppDataBase db;

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
        binding = ActivityCategoryListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        this.model = new CategoryListModel();
        this.model.setArrayList(new ArrayList());
        this.model.setNoDataIcon(R.drawable.dummy_empty);
        this.model.setNoDataText(getString(R.string.noDataTitleCategory));
        this.model.setNoDataDetail(getString(R.string.noDataDescCategory));
//        this.binding.setModel(this.model);
        this.db = AppDataBase.getAppDatabase(this.context);
    }


    public void setToolbar() {
        this.toolbarModel = new ToolbarModel();
        this.toolbarModel.setTitle("Manage Category");
//        this.binding.includedToolbar.setModel(this.toolbarModel);
        this.binding.includedToolbar.textTitle.setText("Manage Category");
        binding.includedToolbar.imgDelete.setVisibility(View.GONE);
        binding.includedToolbar.imgAdd.setVisibility(View.GONE);
        binding.includedToolbar.imageHome.setVisibility(View.GONE);
        binding.includedToolbar.imgOther.setVisibility(View.GONE);
        binding.includedToolbar.spinner.setVisibility(View.GONE);
        binding.includedToolbar.progressbar.setVisibility(View.GONE);
        binding.includedToolbar.etOther.setVisibility(View.GONE);
        binding.includedToolbar.search.setVisibility(View.GONE);
        binding.includedToolbar.imgShare.setVisibility(View.GONE);
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
            this.model.getArrayList().addAll(this.db.categoryDao().getAll());
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
                if (i2 > 0 && CategoryListActivity.this.binding.fabAdd.getVisibility() == View.VISIBLE) {
                    CategoryListActivity.this.binding.fabAdd.hide();
                } else if (i2 < 0 && CategoryListActivity.this.binding.fabAdd.getVisibility() != View.VISIBLE) {
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
        this.binding.linData.setVisibility(this.model.isListData() ? View.VISIBLE : View.GONE);
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
                    arrayList.addAll(CategoryListActivity.this.db.taskDao().getAll(AppPref.getCurrentEvenId(CategoryListActivity.this.context), CategoryListActivity.this.model.getArrayList().get(i).getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    try {
                        CategoryListActivity.this.db.subTaskDao().deleteAll((String) arrayList.get(i));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    try {
                        CategoryListActivity.this.db.taskDao().delete(AppPref.getCurrentEvenId(CategoryListActivity.this.context), (String) arrayList.get(i));
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                ArrayList arrayList2 = new ArrayList();
                try {
                    arrayList2.addAll(CategoryListActivity.this.db.costDao().getAll(AppPref.getCurrentEvenId(CategoryListActivity.this.context), CategoryListActivity.this.model.getArrayList().get(i).getId()));
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                    try {
                        CategoryListActivity.this.db.paymentDao().deleteAll((String) arrayList2.get(i2));
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    try {
                        CategoryListActivity.this.db.costDao().delete(AppPref.getCurrentEvenId(CategoryListActivity.this.context), (String) arrayList2.get(i2));
                    } catch (Exception e6) {
                        e6.printStackTrace();
                    }
                }
                ArrayList arrayList3 = new ArrayList();
                try {
                    arrayList3.addAll(CategoryListActivity.this.db.vendorDao().getAll(AppPref.getCurrentEvenId(CategoryListActivity.this.context), CategoryListActivity.this.model.getArrayList().get(i).getId()));
                } catch (Exception e7) {
                    e7.printStackTrace();
                }
                for (int i3 = 0; i3 < arrayList3.size(); i3++) {
                    try {
                        CategoryListActivity.this.db.paymentDao().deleteAll((String) arrayList3.get(i3));
                    } catch (Exception e8) {
                        e8.printStackTrace();
                    }
                    try {
                        CategoryListActivity.this.db.vendorDao().delete(AppPref.getCurrentEvenId(CategoryListActivity.this.context), (String) arrayList3.get(i3));
                    } catch (Exception e9) {
                        e9.printStackTrace();
                    }
                }
            }

            public void onPostExecute() {
                try {
                    CategoryListActivity.this.db.categoryDao().delete(CategoryListActivity.this.model.getArrayList().get(i));
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
        this.dialogNewCatBinding = AlertDialogNewCategoryBinding.inflate(LayoutInflater.from(this.context), (ViewGroup) null, false);
        this.dialogNewCat = new Dialog(this.context);
        this.dialogNewCat.setContentView(this.dialogNewCatBinding.getRoot());
        this.dialogNewCat.setCancelable(false);
        this.dialogNewCat.getWindow().setBackgroundDrawableResource(17170445);
        this.dialogNewCat.getWindow().setLayout(-1, -2);
        this.dialogNewCatBinding.txtTitle.setText(R.string.add_new_category);
        this.dialogNewCatBinding.recycler.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false));
        this.dialogNewCatBinding.recycler.setAdapter(new ImageAdapter(true, this.context, imageList, new RecyclerItemClick() {
            public void onClick(int i, int i2) {
                selectedNewCatPos = i;
                showNewCatList(selectedCatPos);

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
            /* class com.msint.weddingplanner.appBase.view.CategoryListActivity.AnonymousClass8 */

            /* JADX WARNING: Removed duplicated region for block: B:26:0x00ec  */
            public void onClick(View view) {
                long j;
                Exception e;
                if (CategoryListActivity.this.isValidNewCat(CategoryListActivity.this.dialogNewCatBinding)) {
                    CategoryRowModel categoryRowModel = new CategoryRowModel();
                    categoryRowModel.setName(CategoryListActivity.this.dialogNewCatBinding.etName.getText().toString().trim());
                    categoryRowModel.setIconType(((ImageRowModel) CategoryListActivity.this.imageList.get(CategoryListActivity.this.selectedNewCatPos)).getId());
                    try {
                        categoryRowModel.getName().trim();
                        if (CategoryListActivity.this.selectedCatPos != -1) {
                            categoryRowModel.setId(CategoryListActivity.this.model.getArrayList().get(CategoryListActivity.this.selectedCatPos).getId());
                            categoryRowModel.setDefault(CategoryListActivity.this.model.getArrayList().get(CategoryListActivity.this.selectedCatPos).isDefault());
                            try {
                                j = (long) CategoryListActivity.this.db.categoryDao().update(categoryRowModel);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                j = 0;
                            }
                            try {
                                CategoryListActivity.this.model.getArrayList().set(CategoryListActivity.this.selectedCatPos, categoryRowModel);
                            } catch (Exception e3) {
                                e = e3;
                            }
                        } else {
                            categoryRowModel.setId(AppConstants.getUniqueId());
                            try {
                                j = CategoryListActivity.this.db.categoryDao().insert(categoryRowModel);
                            } catch (Exception e4) {
                                e4.printStackTrace();
                                j = 0;
                            }
                            CategoryListActivity.this.model.getArrayList().add(categoryRowModel);
                        }
                    } catch (Exception e5) {
                        e = e5;
                        j = 0;
                        e.printStackTrace();
                        if (j > 0) {
                        }
                        CategoryListActivity.this.dialogNewCat.dismiss();
                    }
                    if (j > 0) {
                        CategoryListActivity.this.notifyAdapter();
                    }
                }
                try {
                    CategoryListActivity.this.dialogNewCat.dismiss();
                } catch (Exception e6) {
                    e6.printStackTrace();
                }
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
