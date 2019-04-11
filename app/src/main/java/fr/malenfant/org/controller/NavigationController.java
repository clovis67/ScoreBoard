package fr.malenfant.org.controller;

import fr.malenfant.org.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

public class NavigationController {
	
	public static NavigationController instance = new NavigationController();

	public static NavigationController getInstance() {
		return instance;
	}

	private NavigationController() {

	}
	
	public void startAppRating(Context context) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, 
				Uri.parse("market://details?id=" + context.getPackageName())));
	}
	
	public void showExitDialog( final FragmentActivity activity) {
		ConfirmDialog newFragment = ConfirmDialog.newInstance(
				R.string.confirm_quit, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						activity.finish();
					}
				}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		newFragment.show(activity.getSupportFragmentManager(), "dialog");
	}
}
