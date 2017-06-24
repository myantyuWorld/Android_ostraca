package com.example.yuichi_oba.ostraca.tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by Yuichi-Oba on 2017/06/24.
 */

/***
 * ダイアログ表示のためのクラス
 */
public class MyDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("Message").setMessage("出席記録が完了しました").setPositiveButton("OK", null).show();
    }

}
