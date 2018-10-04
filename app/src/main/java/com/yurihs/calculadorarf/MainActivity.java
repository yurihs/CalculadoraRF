package com.yurihs.calculadorarf;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnSendListener {

    private EIRPFragment eirpFragment = new EIRPFragment();
    private FSLFragment fslFragment = new FSLFragment();
    private BudgetFragment budgetFragment = new BudgetFragment();
    private TxRateNyquistFragment txRateNyquistFragment = new TxRateNyquistFragment();
    private TxRateShannonFragment txRateShannonFragment = new TxRateShannonFragment();
    private FresnelFragment fresnelFragment = new FresnelFragment();
    private AboutFragment aboutFragment = new AboutFragment();

    private int currentNavigationItem = R.id.navigation_eirp;
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null) {
            currentNavigationItem = savedInstanceState.getInt("currentNavigationItem");
        } else {
            currentNavigationItem = R.id.navigation_eirp;
        }

        navigationView.setCheckedItem(currentNavigationItem);

        MenuItem checkedItem = navigationView.getCheckedItem();
        if (checkedItem != null) {
            onNavigationItemSelected(checkedItem);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentNavigationItem", currentNavigationItem);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setCurrentFragment(Fragment newFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, newFragment);
        currentFragment = newFragment;
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        currentNavigationItem = item.getItemId();
        Fragment nextFragment = null;
        switch (currentNavigationItem) {
            case R.id.navigation_eirp:
                nextFragment = eirpFragment;
                break;
            case R.id.navigation_fsl:
                nextFragment = fslFragment;
                break;
            case R.id.navigation_budget:
                nextFragment = budgetFragment;
                break;
            case R.id.navigation_tx_rate_nyquist:
                nextFragment = txRateNyquistFragment;
                break;
            case R.id.navigation_tx_rate_shannon:
                nextFragment = txRateShannonFragment;
                break;
            case R.id.navigation_fresnel:
                nextFragment = fresnelFragment;
                break;
            case R.id.navigation_about:
                nextFragment = aboutFragment;
        }

        if (nextFragment != null) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

            setCurrentFragment(nextFragment);

            ActionBar supportActionBar = getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setTitle(item.getTitle());
            }

            return true;
        }

        return false;
    }

    @Override
    public void onSendTransmitterEIRPToBudget(Double transmitter_eirp) {
        budgetFragment = BudgetFragment.newInstance(transmitter_eirp, null);
        currentNavigationItem = R.id.navigation_budget;
        NavigationView navigationView = findViewById(R.id.navigation);

        navigationView.setCheckedItem(currentNavigationItem);

        MenuItem checkedItem = navigationView.getCheckedItem();
        if (checkedItem != null) {
            onNavigationItemSelected(checkedItem);
        }
    }

    @Override
    public void onSendFSLToBudget(Double fsl) {
        budgetFragment = BudgetFragment.newInstance(null, fsl);
        currentNavigationItem = R.id.navigation_budget;
        NavigationView navigationView = findViewById(R.id.navigation);

        navigationView.setCheckedItem(currentNavigationItem);

        MenuItem checkedItem = navigationView.getCheckedItem();
        if (checkedItem != null) {
            onNavigationItemSelected(checkedItem);
        }
    }

    // from https://stackoverflow.com/a/17789187
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // from https://stackoverflow.com/a/37564553
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            /*
             * It gets into the above IF-BLOCK if anywhere the screen is touched.
             */

            View v = getCurrentFocus();
            if (v instanceof EditText) {
                /*
                 * Now, it gets into the above IF-BLOCK if an EditText is already in focus, and you tap somewhere else
                 * to take the focus away from that particular EditText. It could have 2 cases after tapping:
                 * 1. No EditText has focus
                 * 2. Focus is just shifted to the other EditText
                 */

                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    hideKeyboard();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
