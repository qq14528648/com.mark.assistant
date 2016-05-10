package com.mark.assistant;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2016/5/10.
 */
public class GuideAppManager

{
    public static class AppInfoProvider {
        private PackageManager packageManager;

        public AppInfoProvider(Context context) {
            packageManager = context.getPackageManager();
        }

        /**
         * 获取系统中所有应用信息，
         * 并将应用软件信息保存到list列表中。
         **/
        public List<AppInfo> getAllApps() {
            List<AppInfo> list = new ArrayList<AppInfo>();

            //获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (PackageInfo info : packageInfos) {
                AppInfo mAppInfo = new AppInfo();
                //拿到包名
                String packageName = info.packageName;
                //拿到应用程序的信息
                ApplicationInfo appInfo = info.applicationInfo;
                //拿到应用程序的图标
                Drawable icon = appInfo.loadIcon(packageManager);
                String appName = appInfo.loadLabel(packageManager).toString();
                mAppInfo.setPackageName(packageName);
                mAppInfo.setAppName(appName);
                mAppInfo.setIcon(icon);
                mAppInfo.setVersion(info.versionName);
                if (filterApp(appInfo)) {
                    //非系统应用添加 进来
                    list.add(mAppInfo);
                }

            }
            return list;
        }

        public AppInfo getAppInfo(String pack) {
            //获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (PackageInfo info : packageInfos) {
                //拿到包名
                String packageName = info.packageName;
                if (pack.equals(packageName)) {
                    AppInfo mAppInfo = new AppInfo();
                    //拿到应用程序的信息
                    ApplicationInfo appInfo = info.applicationInfo;
                    //拿到应用程序的图标
                    Drawable icon = appInfo.loadIcon(packageManager);

                    String appName = appInfo.loadLabel(packageManager).toString();
                    mAppInfo.setPackageName(packageName);
                    mAppInfo.setAppName(appName);
                    mAppInfo.setIcon(icon);
                    return mAppInfo;
                }
            }
            return null;
        }

        /**
         * /**
         * 判断某一个应用程序是不是系统的应用程序，
         * 如果是返回true，否则返回false。
         */
        private boolean filterApp(ApplicationInfo info) {
            //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
            if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                return true;
            } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//判断是不是系统应用
                return true;
            }
            return false;
        }
    }

    public  static class AppInfo {

        private Drawable icon;
        private String appName;
        private String packageName;
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

    }

}
