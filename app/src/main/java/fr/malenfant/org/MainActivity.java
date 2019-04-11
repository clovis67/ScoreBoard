package fr.malenfant.org;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import fr.malenfant.org.controller.DbGame;
import fr.malenfant.org.controller.NavigationController;
import fr.malenfant.org.fragment.JoueurListFragment;
import fr.malenfant.org.fragment.MainFragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends FragmentActivity
		implements SlidingMenuFragment.OnMenuSelectedListener,
                   JoueurListFragment.OnMenuSelectedListener{

	private SlidingMenu slidingMenu ;
	private MainFragment main_fragment;
    private JoueurListFragment joueur_list_fragment;
    ContentValues newjoueur;
    DbGame dbgame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_main);

		main_fragment = new MainFragment();
		main_fragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction()
        	.add(R.id.main_fragment, main_fragment).commit();
		
		// Chargement du sliding menu
		slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.slidingmenu);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	
    public void showFragment(final Fragment fragment) {
        if (fragment == null)
            return;
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        // We can also animate the changing of fragment
        ft.setCustomAnimations(R.anim.slide_in_right,
                R.anim.slide_out_left);
        ft.replace(R.id.main_fragment, fragment);
        ft.commit();
    }
    
    @Override
    public void onArticleSelected(int id) {
    	this.slidingMenu.toggle();
    	dbgame = new DbGame(this);
        switch (id) {
        // Général
        case 101:
        	// Page d'accueil
        	main_fragment = new MainFragment();
        	showFragment(main_fragment);
            break;
        case 102:
            // J'aime
            NavigationController.getInstance().startAppRating(this);
            break;
        case 103:
            // Aide
            break;
        case 104:
            // A propos
            break;
        case 105:
            // Quitter
            NavigationController.getInstance().showExitDialog(this);
            break;
        // Jouer
        case 201:
            // New party TTR
            break;
        case 202:
            // New party Belote
            break;
        case 203:
            // New party Allumettes
            break;
        case 204:
            // New party Rami
            break;
        case 205:
            // New party Scrabble
            break;
        // Historique
        case 301:
            // TTR
            break;
        case 302:
            // Belote
            break;
        case 303:
            // Allumettes
            break;
        case 304:
            // Rami
            break;
        case 305:
            // Scrabble
            break;
        // Joueurs
        case 401:
            // Ajout joueur
            // Ouverture pour ajouter un nouveau joueur
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
            builder.setTitle(R.string.titre_joueur);
            // Set up the input
            final EditText input = new EditText(MainActivity.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            builder.setView(input);
            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newjoueur = new ContentValues();
                    newjoueur.put("NOM", input.getText().toString());
                    long ins = dbgame.insertJoueur(newjoueur);
                    // int present = dbgame.existJoueur(input.getText().toString());
                    //if ( present == 0 ){
                    if ( ins == 9 ){
                        Toast.makeText( MainActivity.this, R.string.creation_ko, Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText( MainActivity.this, R.string.creation_ok, Toast.LENGTH_LONG).show();
                        joueur_list_fragment = new JoueurListFragment();
                        showFragment(joueur_list_fragment);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            break;
        case 402:
            // Liste
            joueur_list_fragment = new JoueurListFragment();
            showFragment(joueur_list_fragment);
            break;
        case 403:
            // Historique
            break;
        }
    }
	
	@Override
    public void onBackPressed() {
        if ( slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
        else {
            super.onBackPressed();
        }
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            this.slidingMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.slidingMenu.toggle();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
