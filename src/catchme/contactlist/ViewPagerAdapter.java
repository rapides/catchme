package catchme.contactlist;

import catchme.mapcontent.ItemDetailFragment;
import catchme.messages.MessagesFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
    	Fragment fragment = null;
    	if(i ==0){
    		fragment = new MessagesFragment();
    	}else if(i==1){
    		fragment = new ItemListFragment();
    	}else if(i==2){
    		fragment = new ItemDetailFragment();
    	}
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ""+position;
    }
   
}
