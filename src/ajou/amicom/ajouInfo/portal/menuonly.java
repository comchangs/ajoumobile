package ajou.amicom.ajouInfo.portal;



import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class menuonly extends  TabActivity {
    public static parsing parsing;
    private LinearLayout mLinearLayout;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menuonly);
        TabHost tabHost = getTabHost();
        final parsing parsing = new parsing();
        
		tabHost.addTab(tabHost.newTabSpec("breakfast").setIndicator("아침",getResources().getDrawable(R.drawable.breakfast))
				.setContent(new Intent(this, breakfast.class)));
		tabHost.addTab(tabHost.newTabSpec("lunch").setIndicator("점심",getResources().getDrawable(R.drawable.lunch))
				.setContent(new Intent(this, lunch.class)));
		tabHost.addTab(tabHost.newTabSpec("dinner").setIndicator("저녁",getResources().getDrawable(R.drawable.dinner))
				.setContent(new Intent(this, dinner.class)));
		tabHost.addTab(tabHost.newTabSpec("snack").setIndicator("분식",getResources().getDrawable(R.drawable.snack))
				.setContent(new Intent(this, snack.class)));
		tabHost.setCurrentTab(1);
	}
}
