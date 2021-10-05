package com.msint.weddingplanner.pdfRepo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.xmp.options.PropertyOptions;
import com.msint.weddingplanner.R;
import com.msint.weddingplanner.appBase.utils.AppConstants;
import com.msint.weddingplanner.appBase.utils.Constants;
import com.msint.weddingplanner.appBase.utils.RecyclerItemClick;
import java.io.File;
import java.util.ArrayList;

public class ReportPdfAdapter extends RecyclerView.Adapter {
    private Context context;

    public RecyclerItemClick itemClick;
    private ArrayList<File> list;

    public ReportPdfAdapter(Context context2, ArrayList<File> arrayList, RecyclerItemClick recyclerItemClick) {
        this.context = context2;
        this.list = arrayList;
        this.itemClick = recyclerItemClick;
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_pdf_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.txtName.setText(this.list.get(i).getName());
            TextView textView = rowHolder.txtDate;
            long lastModified = this.list.get(i).lastModified();
            textView.setText(AppConstants.getFormattedDate(lastModified, "dd/MM/yyyy " + Constants.showTimePattern));
            rowHolder.txtSize.setText(AppConstants.getFileSize(this.list.get(i).length()));
        }
    }

    public int getItemCount() {
        return this.list.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgOptions;
        TextView txtDate;
        TextView txtName;
        TextView txtSize;

        public RowHolder(View view) {
            super(view);
            this.txtName = (TextView) view.findViewById(R.id.txtName);
            this.txtDate = (TextView) view.findViewById(R.id.txtDate);
            this.txtSize = (TextView) view.findViewById(R.id.txtSize);
            this.imgOptions = (ImageView) view.findViewById(R.id.imgOptions);
            view.setOnClickListener(this);
            this.imgOptions.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.imgOptions) {
                ReportPdfAdapter.this.showMenu(this.imgOptions, getAdapterPosition());
            } else {
                ReportPdfAdapter.this.openFile(getAdapterPosition());
            }
        }
    }


    public void showMenu(ImageView imageView, final int i) {
        PopupMenu popupMenu = new PopupMenu(this.context, imageView);
        popupMenu.inflate(R.menu.report_options_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuDelete:
                        ReportPdfAdapter.this.deleteFile(i);
                        ReportPdfAdapter.this.itemClick.onClick(i, 0);
                        return true;
                    case R.id.menuOpen:
                        ReportPdfAdapter.this.openFile(i);
                        return true;
                    case R.id.menuShare:
                        ReportPdfAdapter.this.shareFile(i);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }


    public void openFile(int i) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(PropertyOptions.SEPARATE_NODE);
        intent.addFlags(1);
        if (Build.VERSION.SDK_INT > 23) {
            intent.setDataAndType(FileProvider.getUriForFile(this.context, "com.msint.weddingplanner.provider", this.list.get(i)), "application/pdf");
        } else {
            intent.setDataAndType(Uri.fromFile(this.list.get(i)), "application/pdf");
        }
        try {
            this.context.startActivity(Intent.createChooser(intent, "Open File"));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this.context, "No app to read PDF File", 1).show();
        }
    }


    public void shareFile(int i) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("*/*");
        intent.addFlags(PropertyOptions.SEPARATE_NODE);
        intent.addFlags(1);
        if (Build.VERSION.SDK_INT > 23) {
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this.context, "com.msint.weddingplanner.provider", this.list.get(i)));
        } else {
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(this.list.get(i)));
        }
        try {
            this.context.startActivity(Intent.createChooser(intent, "Share File "));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this.context, "No app to read PDF File", Toast.LENGTH_LONG).show();
        }
    }


    public void deleteFile(int i) {
        File file = this.list.get(i);
        try {
            if (!file.exists()) {
                return;
            }
            if (file.delete()) {
                this.list.remove(i);
                notifyItemRemoved(i);
                Toast.makeText(this.context, "File deleted.", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this.context, "File can't be deleted.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
