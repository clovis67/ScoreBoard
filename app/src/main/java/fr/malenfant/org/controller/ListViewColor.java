package fr.malenfant.org.controller;

import java.util.HashMap;
import java.util.List;
import fr.malenfant.org.R;
import fr.malenfant.org.controller.SharedInformation.type;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListViewColor extends ArrayAdapter<HashMap<String, String>>{

    private String stype;
    HashMap<String, String> item;


	public ListViewColor(Context context, int textViewResourceId, List<HashMap<String, String>> objects, String type) {
        super(context, textViewResourceId, objects);  
        stype = type;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
        {            
            LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(stype.equals(type.TYPE_JOUEUR)){
                view = li.inflate(R.layout.affichagejoueur, null);
            }
        }
        // récupération du résultat
        item = getItem(position);
        if(stype.equals(type.TYPE_JOUEUR)){
            // Joueur
            TextView nom = view.findViewById(R.id.nom);
            TextView id = view.findViewById(R.id.id);
            nom.setText(item.get("nom"));
            id.setText(item.get("id"));

        }
        // Couleur en alternée
        int color1 = 0xaa8FEBFF;
        int color2 = 0xaa0FFFFB;
        if(position % 2 == 0)
  		  view.setBackgroundColor(color1);
  	  	else
  		  view.setBackgroundColor(color2);
       return view;
    }
}

