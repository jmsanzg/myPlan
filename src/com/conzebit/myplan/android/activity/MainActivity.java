/*
 * This file is part of myPlan.
 *
 * Plan is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * Plan is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.conzebit.myplan.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.conzebit.myplan.R;
import com.conzebit.myplan.android.util.Settings;

public class MainActivity extends Activity {

	private final static int MENU_ABOUT = 1;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem = menu.add(0, MENU_ABOUT, 0, R.string.menu_about);
		menuItem.setIcon(R.drawable.menu_globe);
		return true;
    }
	
	@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
			case MENU_ABOUT:
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse("http://www.conzebit.com/"));
				this.startActivity(i);
		}
        return true;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String termsShown = Settings.getTermsShown(this);
        
        if ("no".equals(termsShown)) {
        	this.setContentView(R.layout.terms_and_conditions);
        	Settings.setTermsShown(this, "yes");
        	Button button = (Button) this.findViewById(R.id.accept_button);
        	button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					loadMainView();
				}
			});
        } else {
        	loadMainView();
        }
    }

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
		if (!Settings.isMandatoryConfigSet(this)) {
			Toast toast = Toast.makeText(this, R.string.main_toast_settings, 5);
			toast.show();			
			startActivityForResult(new Intent(this, SettingsActivity.class), 1);
		}
	}
    
    private void loadMainView() {
    	this.setContentView(R.layout.main);
    	
    	final Activity activity = this;
    	
    	this.findViewById(R.id.viewplans_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(activity, PlanSummaryActivity.class));
			}
		});

    	this.findViewById(R.id.viewstats_button).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(activity, StatisticsSummaryActivity.class));
			}
		});
    	
    	this.findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivityForResult(new Intent(activity, SettingsActivity.class), 3);
			}
		});
    	
        if (!Settings.isMandatoryConfigSet(this)) {
        	startActivityForResult(new Intent(this, SettingsActivity.class), 1);
        }
    }
}