package com.inotechsol.amirhafiz.locateease.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inotechsol.amirhafiz.locateease.MainActivity;
import com.inotechsol.amirhafiz.locateease.R;
import com.inotechsol.amirhafiz.locateease.ViewPagerSlidingTab.PagetSlidingTab_Strip;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Amir on 5/5/2017.
 */

public class NavigateContainer_Fragment extends Fragment implements ViewPager.OnPageChangeListener {

    static PagetSlidingTab_Strip view_comm_frag_tabs;
    ViewPager pager;

    View viewpager_divider;
    public ViewComm_Adapter mAdapter;



    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View frgView = inflater.inflate(R.layout.navigate_container,container,false);

        ((MainActivity)getActivity()).hideShowActionBar(true);


        pager = (ViewPager)frgView.findViewById(R.id.pager);
        mAdapter = new ViewComm_Adapter(getChildFragmentManager());
        pager.setAdapter(mAdapter);
        pager.setOffscreenPageLimit(1);//1

        view_comm_frag_tabs = (PagetSlidingTab_Strip) frgView.findViewById(R.id.view_comm_frag_tabs);
        view_comm_frag_tabs.setViewPager(pager);
        Typeface mRobotoRegular = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Regular.ttf");
        view_comm_frag_tabs.setTypeface(mRobotoRegular);
        view_comm_frag_tabs.setOnPageChangeListener(this);



        return frgView;

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SearchFragment frag = (SearchFragment) getChildFragmentManager().getFragments().get(pager.getCurrentItem());
        frag.yourMethod(data);  // Method for callback in YourFragment
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class ViewComm_Adapter extends FragmentPagerAdapter {

        private FragmentManager mFragmentManager;
        private Map<Integer, String> mFragmentTags;

        public ViewComm_Adapter(FragmentManager fm) {
            super(fm);
            this.mFragmentManager = fm;
            this.mFragmentTags = new HashMap<Integer, String>();
        }


        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new SearchFragment();
                    break;
                case 1:
                    fragment = new PickUpFriend();//
                    break;

                case 2:
                    fragment = new Favourites();//
                    break;

            }
            return fragment;//(position == 0)? new Enquries_Fragment() : new Suggestions_Fragment();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            CharSequence TabName = "";
            switch (position) {
                case 0:         //About
                    TabName = "Search";//"About";
                    break;
                case 1:       //Directory
                    TabName = "Pick Up";
                    break;

                case 2:
                    TabName = "Favorites";
                    break;
            }
            return TabName;
        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//            mFragmentTags.remove(position);
//        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment currFrg = (Fragment) obj;


                mFragmentTags.put(position, currFrg.getTag());
                Log.d("TAG", position + ": " + currFrg.getTag());


            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);

            return tag == null ? null : mFragmentManager.findFragmentByTag(tag);
        }
    }

}
