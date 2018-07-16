package com.thxy.common.widget.media;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.thxy.common.R;

import net.qiujuer.genius.ui.Ui;


/**
 * 图片选择BottomSheetDialogFragment
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangeListener {

    private GalleryView mGalleryViwe;

    private OnSelectedListener mlistener;
    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_gallery, container, false);
         mGalleryViwe = rootView.findViewById(R.id.galleryView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGalleryViwe.setup(getLoaderManager(),this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        //选中一张
        if (count > 0) {
            dismiss();
            if (mlistener != null) {
                String[] selectedPath = mGalleryViwe.getSelectedPath();
                mlistener.OnSelectedImage(selectedPath[0]);
                mlistener=null;
            }
        }
    }

    public GalleryFragment setOnSelectedListener(OnSelectedListener listener){
        mlistener=listener;
        return this;
    }

    public interface OnSelectedListener{
        void OnSelectedImage(String path);
    }
public static class TransStatusBottomSheetDialog extends BottomSheetDialog{

    public TransStatusBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public TransStatusBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window == null) {
            return;
        }
        //屏幕高度
        int ScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        //状态栏高度
        int statusHeight = (int) Ui.dipToPx(getContext().getResources(), 25);

        int dialogHeight= ScreenHeight-statusHeight;

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,dialogHeight);
    }
}
}

