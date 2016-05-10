package com.mark.assistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016/05/09.
 */
public class GuideFragment extends Fragment {
    public static final String ASSISTANT_GUIDE = "assistant_guide";
    public static final String GUIDE_PACKAGE = "guide_package";
    public static final String DEFAUL_PACKAGE_NAME = "jp.colopl.wcat";
    private static final String GUIDE_SERVER = "guide_server";

    private static final String GUIDE_REMARKS = "guide_remarks";
    private static final String GUIDE_LAST_DATE = "guide_last_date";
    private static final String DEFAUL_REMARKS = "none";
    private static final String DEFAUL_LAST_DATE = new Date().toString();
    private GuideAppManager.AppInfoProvider mAppInfoProvider;
    private ImageView mAppImageView;
    private ImageView mAppInfoImageButton;
    private ImageView mStartAppImageButton;
    private TextView mNameTextView;
    private TextView mPackageTextView;
    private TextView mVersionTextView;
    private TextView mServerTextView;
    private TextView mRemarksTextView;
    private TextView mLastDateTextView;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAppInfoProvider = new GuideAppManager.AppInfoProvider(getActivity());
        mSharedPreferences = getActivity().getSharedPreferences(
                ASSISTANT_GUIDE, Context.MODE_PRIVATE);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_guide, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_app:
                guideApp();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 引导设置app
     */
    private void guideApp() {
        View list = getActivity().getLayoutInflater().inflate(R.layout.app_choice_list, null);
        ListView listView = (ListView) list.findViewById(R.id.listView);
        final AppChoiceAdpater appChoiceAdpater = new AppChoiceAdpater(getActivity(), mAppInfoProvider.getAllApps());
        listView.setAdapter(appChoiceAdpater);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(list);
        builder.setTitle(R.string.guide_app__choice).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        }).show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View edit_panel = getActivity().getLayoutInflater().inflate(R.layout.app_choice_edit, null);
                builder.setView(edit_panel);
                builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText server = (EditText) edit_panel.findViewById(R.id.server);
                        EditText remarks = (EditText) edit_panel.findViewById(R.id.remarks);
                        if (TextUtils.isEmpty(server.getText().toString()) || TextUtils.isEmpty(remarks.getText().toString())) {

                            return;
                        } else {
                            GuideAppManager.AppInfo info = appChoiceAdpater.getItem(i);
                            String date = new Date().toString();
                            mAppImageView.setImageDrawable(info.getIcon());
                            mNameTextView.setText(info.getAppName());
                            mPackageTextView.setText(info.getPackageName());
                            mVersionTextView.setText(info.getVersion());
                            mServerTextView.setText(server.getText().toString());
                            mRemarksTextView.setText(remarks.getText().toString());
                            mLastDateTextView.setText(date);
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString(GUIDE_PACKAGE, info.getPackageName());
                            editor.putString(GUIDE_SERVER, server.getText().toString());
                            editor.putString(GUIDE_REMARKS, remarks.getText().toString());
                            editor.putString(GUIDE_LAST_DATE, date);
                            editor.commit();
                            dialogInterface.dismiss();
                        }

                    }
                });

            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        mAppImageView = (ImageView) view.findViewById(R.id.appImageView);
        mStartAppImageButton = (ImageView) view.findViewById(R.id.startAppImageButton);
        mAppInfoImageButton = (ImageView) view.findViewById(R.id.appInfoImageButton);
        mNameTextView = (TextView) view.findViewById(R.id.nameTextView);
        mPackageTextView = (TextView) view.findViewById(R.id.packageTextView);
        mVersionTextView = (TextView) view.findViewById(R.id.versionTextView);
        mServerTextView = (TextView) view.findViewById(R.id.serverTextView);
        mLastDateTextView = (TextView) view.findViewById(R.id.lastDateTextView);
        mRemarksTextView = (TextView) view.findViewById(R.id.remarksTextView);

        String packageName = mSharedPreferences.getString(GUIDE_PACKAGE, DEFAUL_PACKAGE_NAME);
        if (!TextUtils.isEmpty(packageName)) {
            String server = mSharedPreferences.getString(GUIDE_SERVER, getString(R.string.defaul_server));
            String remarks = mSharedPreferences.getString(GUIDE_REMARKS, DEFAUL_REMARKS);
            String lastDate = mSharedPreferences.getString(GUIDE_LAST_DATE, DEFAUL_LAST_DATE);
            GuideAppManager.AppInfo info = mAppInfoProvider.getAppInfo(packageName);
            mAppImageView.setImageDrawable(info.getIcon());
            mNameTextView.setText(info.getAppName());
            mPackageTextView.setText(info.getPackageName());
            mVersionTextView.setText(info.getVersion());
            mServerTextView.setText(server);
            mRemarksTextView.setText(remarks);
            mLastDateTextView.setText(lastDate);
        } else {
            guideApp();
        }
 return view;
    }


    /**
     * 类名称：AppInfoProvider
     * 类描述：获取应用程序的相关信息
     * 创建人：LXH
     */

    private class AppChoiceAdpater extends BaseAdapter {
        private Context mContext;
        private List<GuideAppManager.AppInfo> mAppInfos;
        private LayoutInflater mLayoutInflater;

        public AppChoiceAdpater(Context context, List<GuideAppManager.AppInfo> infos) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mAppInfos = infos;
        }

        @Override
        public int getCount() {
            return mAppInfos.size();
        }

        @Override
        public GuideAppManager.AppInfo getItem(int i) {
            return mAppInfos.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = mLayoutInflater.inflate(R.layout.app_choice_list_item, viewGroup, false);
                holder.mImageView = (ImageView) view.findViewById(R.id.appImageView);
                holder.mAppNameTextView = (TextView) view.findViewById(R.id.appNameTextView);
                holder.mPackAgeTextView = (TextView) view.findViewById(R.id.packageTextView);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            GuideAppManager.AppInfo info = mAppInfos.get(i);
            holder.mImageView.setImageDrawable(info.getIcon());
            holder.mAppNameTextView.setText(info.getAppName());
            holder.mPackAgeTextView.setText(info.getPackageName());
            return view;

        }

        class ViewHolder {
            ImageView mImageView;
            TextView mAppNameTextView;
            TextView mPackAgeTextView;

        }
    }

}
