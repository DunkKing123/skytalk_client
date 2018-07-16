package com.thxy.skytalk_client.fragment.main;

import android.support.v4.app.Fragment;
import android.view.View;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.BaseViewPagerFragment;
import com.thxy.skytalk_client.fragment.contacts.ContactsFragmentFactory;
import com.thxy.common.utils.UIUtils;

/**
 *  通讯录Fragment
 */
public class ContactsFragment extends BaseViewPagerFragment {

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
    }

    @Override
    protected int setTabIndicatorPadding() {
        return 16;
    }

    @Override
    protected CharSequence[] createTabTitle() {
        return UIUtils.getStringArray(R.array.interaction_title);
    }

    @Override
    protected Fragment createChildFragment(int position) {
        return ContactsFragmentFactory.createInteractionFragment(position);
    }
}
