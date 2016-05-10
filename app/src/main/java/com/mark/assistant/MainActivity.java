package com.mark.assistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {


    private  int lastSelectedPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_white_24dp, R.string.bottom_nav_title_guide))
                .addItem(new BottomNavigationItem(R.drawable.ic_book_white_24dp, R.string.bottom_nav_title_backup))
                .addItem(new BottomNavigationItem(R.drawable.ic_tv_white_24dp, R.string.bottom_nav_title_start))
                .addItem(new BottomNavigationItem(R.drawable.ic_videogame_asset_white_24dp,R.string.bottom_nav_title_personal))
                .setMode(BottomNavigationBar.MODE_SHIFTING).initialise();

        //setFirstSelectedPosition
 bottomNavigationBar.setActiveColor(R.color.colorPrimary);
        bottomNavigationBar.setTabSelectedListener(this);
//
//        case R.id.menu_github:
//        String url = "https://github.com/Ashok-Varma/BottomNavigation";
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
// startActivity(i);   bottomNavigationBar.clearAll();/**/

    }

    @Override
    public void onTabSelected(int position) {
        lastSelectedPosition = position;

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}

