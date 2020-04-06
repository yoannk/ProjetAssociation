package com.example.projetassociation.Utilities;

import android.content.Context;
import android.widget.Toast;

public class Functions {

    public static void getToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


}
