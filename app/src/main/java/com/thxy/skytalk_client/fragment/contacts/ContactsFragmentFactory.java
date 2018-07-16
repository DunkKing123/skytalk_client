package com.thxy.skytalk_client.fragment.contacts;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

/**
 * Created by Administrator on 2017/8/3.
 */

public class ContactsFragmentFactory {

    private static SparseArray<Fragment> mContactsFragmentArray = new SparseArray<>();
    public static Fragment createInteractionFragment(int position) {
        // 先从集合中取, 如果没有,才创建对象, 提高性能
        Fragment fragment = mContactsFragmentArray.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new FollowsFragment();
                    break;
                case 1:
                    fragment = new FollowingFragment();
                    break;
                case 2:
                    fragment = new FriendsFragment();
                    break;
                default:
                    break;
            }
            mContactsFragmentArray.put(position, fragment);// 将fragment保存在集合中
        }
        return fragment;
    }

}
