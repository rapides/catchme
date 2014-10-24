package catchme.contactlist;

import catchme.mapcontent.ItemDetailFragment;
import catchme.messages.MessagesFragment;
import cycki.catchme.R;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

public class PositonButtonListener implements OnClickListener {
	private long id;

	public PositonButtonListener(long contactId) {
		this.id = contactId;
	}

	@Override
	public void onClick(View v) {
		ViewPager viewPager = ((ViewPager) v.getRootView().findViewById(
				R.id.main_pager));
		viewPager.setCurrentItem(2);
		ItemListFragment.lastChoosedContactId = id;
		ItemDetailFragment mapFrag = (ItemDetailFragment) viewPager.getAdapter()
				.instantiateItem(viewPager, 2);
		mapFrag.updateView(id);
		MessagesFragment msgFrag = (MessagesFragment) viewPager
				.getAdapter().instantiateItem(viewPager, 0);
		msgFrag.updateView(id);
	}

}
