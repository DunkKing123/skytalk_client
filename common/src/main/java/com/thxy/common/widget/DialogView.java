package com.thxy.common.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Administrator on 2017/10/8.
 */

@SuppressLint("ValidFragment")
public class DialogView extends DialogFragment {

    private String message;
    private String title;
    private OnClickListener listener;
    private boolean useNegativeButton;
    private boolean allowCancel = true;

    public DialogView(String title, String message,OnClickListener listener) {
        this.title = title;
        this.message = message;
        this.listener = listener;
        this.useNegativeButton = true;
    }

    public DialogView(String title, String message,OnClickListener listener,boolean useNegativeButton) {
        this.title = title;
        this.message = message;
        this.listener = listener;
        this.useNegativeButton = useNegativeButton;
    }

    public DialogView(String title, String message,OnClickListener listener,boolean useNegativeButton,boolean allowCancel) {
        this.title = title;
        this.message = message;
        this.listener = listener;
        this.useNegativeButton = useNegativeButton;
        this.allowCancel = allowCancel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                        listener.onPositiveClick();
                    }
                });
        if (useNegativeButton) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dismiss();
                    listener.onNegativeClick();
                }
            });
        }
        AlertDialog dialog = builder.create();
        //dialog.requestWindowFeature(STYLE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(allowCancel);
        dialog.setCancelable(allowCancel);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    return true;
                }else {
                    return false;
                }
            }
        });
        dialog.show();
        return dialog;
    }

    public interface OnClickListener {
        void onPositiveClick();

        void onNegativeClick();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try{
            super.show(manager,tag);
        }catch (IllegalStateException ignore){
        }
    }
}
