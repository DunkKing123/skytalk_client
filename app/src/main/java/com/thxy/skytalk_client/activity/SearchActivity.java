package com.thxy.skytalk_client.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.thxy.common.app.BackActivity;
import com.thxy.skytalk_client.R;
import com.thxy.skytalk_client.fragment.search.ISearchFragment;
import com.thxy.skytalk_client.fragment.search.SearchUserFragment;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 搜索用户Activity
 */
public class SearchActivity extends BackActivity {

    private Fragment mSearchUserFragment;
    private static final String TAG = SearchActivity.class.getSimpleName();
    private ISearchFragment iSearchFragment;
    private String mSearchText;

    public static void show(Context context) {
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        Log.i(TAG, "onAttachFragment: "+fragment.getClass().getSimpleName());
        if (fragment == mSearchUserFragment){
            iSearchFragment = (ISearchFragment) mSearchUserFragment;
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        Log.i(TAG, "initWidget: EditUserInfoActivity");
        mSearchUserFragment = new SearchUserFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_search_content,mSearchUserFragment)
                .commit();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    /**
     * 初始化Toolbar菜单与SearchView
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //初始化菜单
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_view,menu);

        //找到定义好的菜单
        MenuItem menuItem = menu.findItem(R.id.action_search_view);
        SearchView searchView = (SearchView) menuItem.getActionView();
        if (searchView!=null){
            searchView.setQueryHint("你在找谁呢");
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            if (searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (!TextUtils.isEmpty(query)) {
                        search(query);
                    }else{
                        return false;
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mSearchText =newText;
                   return true;
                }
            });
        }
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    /**
     *  搜索
     * @param searchContent 搜索内容
     */
    private void search(String searchContent) {
        iSearchFragment.search(searchContent);
    }

    @OnClick(R.id.fab_search)
    void onSearchClick(){
        search(mSearchText);
    }
}
