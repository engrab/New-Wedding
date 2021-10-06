package com.example.weddingplanner.backupRestore;

import java.util.ArrayList;

public interface GetCompleteResponse {
    void getList(ArrayList<RestoreRowModel> arrayList);

    void getResponse(boolean z);
}
