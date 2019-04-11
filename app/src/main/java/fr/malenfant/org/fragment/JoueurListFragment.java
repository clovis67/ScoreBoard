package fr.malenfant.org.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import fr.malenfant.org.R;
import fr.malenfant.org.controller.DbGame;
import fr.malenfant.org.controller.ListViewColor;
import fr.malenfant.org.controller.SharedInformation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class JoueurListFragment extends Fragment{

	View mainView;
	DbGame dbgame;
	ListView maListViewPerso;
    ListViewColor mSchedule;
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> listItem;
    ContentValues newjoueur;
    AlertDialog.Builder builder;
    Activity activity;

    String nom;
    String seqnr;

	OnMenuSelectedListener mCallback;

	// JSON Node names
    private static final String TAG_NOM = "nom";
    private static final String TAG_SEQNR = "id";
 	
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnMenuSelectedListener {
        /** Called by MainActivity when a list item is selected */
        void onArticleSelected(int id);
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnMenuSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMenuSelectedListener");
        }
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mainView = inflater
				.inflate(R.layout.list_joueur, container, false);
		
		onLoading();
		
		return mainView;
	}

	private void onLoading() {
    	// Ouverture de la base
    	dbgame = new DbGame(getActivity());
    	// Vérifier si la table est vide ou non
        int nb = dbgame.nbjoueur();
        if (nb == 0){
            // On demande si création nouveau joueur
            builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
            builder.setTitle(R.string.ajout_joueur);
            // Set up the input
            final EditText input = new EditText(getActivity());
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            builder.setView(input);
            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newjoueur = new ContentValues();
                    newjoueur.put("NOM", input.getText().toString());
                    dbgame.insertJoueur(newjoueur);
                    int present = dbgame.existJoueur(input.getText().toString());
                   if ( present == 0 ){
                       Toast.makeText(getActivity(), "Erreur lors de la création du joueur", Toast.LENGTH_LONG).show();
                   }else{
                       //Création de la listview
                       maListViewPerso = mainView.findViewById(R.id.listviewperso);
                       listcreation();
                       dbgame.close();
                       //Enfin on met un écouteur d'évènement sur notre listView
                       maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
                           @SuppressWarnings("unchecked")
                           public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                               //on récupère la HashMap contenant les infos de notre item (titre, description, img)
                               //mCallback.onArticleSelected(401);
                           }
                       });
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
        }else{
            //Création de la listview
            maListViewPerso = mainView.findViewById(R.id.listviewperso);
            listcreation();
            dbgame.close();
            //Enfin on met un écouteur d'évènement sur notre listView
            maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
                @Override
                @SuppressWarnings("unchecked")
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    //on récupère la HashMap contenant les infos de notre item (id, nom)
                    map = new HashMap<String, String>();
                    map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
                    nom = map.get("nom");
                    seqnr = map.get("id");
                    //Log.d("Nom", nom);
                    builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                    builder.setTitle(R.string.modif_joueur);
                    // Set up the input
                    final EditText input = new EditText(getActivity());
                    input.setText(nom);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    builder.setView(input);
                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbgame = new DbGame(getActivity());
                            newjoueur = new ContentValues();
                            newjoueur.put("NOM", input.getText().toString());
                            newjoueur.put("SEQNR", seqnr);
                            dbgame.modifJoueur(newjoueur);
                            int present = dbgame.existJoueur(input.getText().toString());
                            dbgame.close();
                            if ( present == 0 ){
                                Toast.makeText(getActivity(), "Erreur lors de la modification du joueur", Toast.LENGTH_LONG).show();
                            }else{
                                //Création de la listview
                                maListViewPerso = (ListView) mainView.findViewById(R.id.listviewperso);
                                listcreation();
                                dbgame.close();
                                //Enfin on met un écouteur d'évènement sur notre listView
                                maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
                                    @SuppressWarnings("unchecked")
                                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                        //on récupère la HashMap contenant les infos de notre item
                                        //mCallback.onArticleSelected(401);
                                    }
                                });
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
                }
            });
            maListViewPerso.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
                    //on récupère la HashMap contenant les infos de notre item (id, nom)
                    map = new HashMap<String, String>();
                    map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
                    nom = map.get("nom");
                    seqnr = map.get("id");
                    // Demande de validation de la suppression du joueur
                    builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
                    builder.setTitle(R.string.sup_joueur);
                    builder.setMessage(R.string.val_sup_joueur)
                    // Set up the buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbgame = new DbGame(getActivity());
                            newjoueur = new ContentValues();
                            newjoueur.put("NOM", nom);
                            newjoueur.put("SEQNR", seqnr);
                            dbgame.deleteJoueur(newjoueur);
                            int present = dbgame.existJoueur(nom);
                            dbgame.close();
                            if ( present == 0 ){
                                Toast.makeText(getActivity(), "Erreur lors de la suppression du joueur", Toast.LENGTH_LONG).show();
                            }else{
                                //Création de la listview
                                maListViewPerso = (ListView) mainView.findViewById(R.id.listviewperso);
                                listcreation();
                                dbgame.close();
                                //Enfin on met un écouteur d'évènement sur notre listView
                                maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
                                    @SuppressWarnings("unchecked")
                                    public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                        //on récupère la HashMap contenant les infos de notre item
                                        //mCallback.onArticleSelected(401);
                                    }
                                });
                            }
                        }
                    })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }
	}
	
    private void listcreation() {
	    dbgame = new DbGame(getActivity());
  	    //Création de la ArrayList qui nous permettra de remplir la listView
        listItem = new ArrayList<HashMap<String, String>>();
   	    listItem = dbgame.fillListview();
        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        mSchedule = new ListViewColor(getActivity(), R.layout.affichagejoueur, listItem, SharedInformation.type.TYPE_JOUEUR);
        //Création d'un SimpleAdapter qui se chargera de mettre les items présents dans notre list (listItem) dans la vue affichageitem
        //SimpleAdapter mSchedule = new SimpleAdapter(getActivity(), listItem, R.layout.affichagejoueur,
          //      new String[] {"nom", "id"}, new int[] {R.id.nom, R.id.id});
        //On attribut à notre listView l'adapter que l'on vient de créer
        maListViewPerso.setAdapter(mSchedule);
        mSchedule.notifyDataSetChanged();
  	}

}
