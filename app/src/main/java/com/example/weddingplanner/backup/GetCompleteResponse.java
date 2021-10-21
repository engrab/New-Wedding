package com.example.weddingplanner.backup;

import java.util.ArrayList;

public interface GetCompleteResponse {
    void getList(ArrayList<RestoreRowModel> arrayList);

    void getResponse(boolean z);
}
