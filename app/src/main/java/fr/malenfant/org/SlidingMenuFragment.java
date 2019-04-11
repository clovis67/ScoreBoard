package fr.malenfant.org;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class SlidingMenuFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    
    private ExpandableListView sectionListView;
    OnMenuSelectedListener mCallback;
    
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnMenuSelectedListener {
        /** Called by MainActivity when a list item is selected */
        void onArticleSelected(int id);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        List<section> sectionList = createMenu();
                
        View view = inflater.inflate(R.layout.slidingmenu_fragment, container, false);
        this.sectionListView = (ExpandableListView) view.findViewById(R.id.slidingmenu_view);
        this.sectionListView.setGroupIndicator(null);
        
        SectionListAdapter sectionListAdapter = new SectionListAdapter(this.getActivity(), sectionList);
        this.sectionListView.setAdapter(sectionListAdapter); 
        
        this.sectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
              @Override
              public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
              }
            });
        
        this.sectionListView.setOnChildClickListener(this);
        
        int count = sectionListAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            this.sectionListView.expandGroup(position);
        }
        
        return view;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnMenuSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMenuSelectedListener");
        }
    }

    private List<section> createMenu() {
        List<section> sectionList = new ArrayList<>();
		
        section oGeneralSection = new section("Général", "ic_menu_home");
        oGeneralSection.addSectionItem(101, "Accueil", "ic_menu_home");
		oGeneralSection.addSectionItem(102, "J'aime", "ic_menu_rating");
        oGeneralSection.addSectionItem(103, "Aide", "ic_menu_aide");
        oGeneralSection.addSectionItem(104, "A propos", "ic_menu_apropos");
        oGeneralSection.addSectionItem(105, "Quitter", "ic_menu_quit");
	

        section oGameSection = new section("Jouer", "ic_menu_play");
        oGameSection.addSectionItem(201, "Les Aventuriers du Rail", "ic_menu_rail");
        oGameSection.addSectionItem(202, "Belote", "ic_menu_belote");
		oGameSection.addSectionItem(203, "Allumettes", "ic_menu_allu");
		oGameSection.addSectionItem(204, "Rami", "ic_menu_rami");
        oGameSection.addSectionItem(205, "Scrabble", "ic_menu_scrabble");
        
        section oHistoSection = new section("Historique", "ic_menu_hist");
        oHistoSection.addSectionItem(301, "Les Aventuriers du Rail", "ic_menu_rail");
        oHistoSection.addSectionItem(302, "Belote", "ic_menu_belote");
        oHistoSection.addSectionItem(303, "Allumettes", "ic_menu_allu");
        oHistoSection.addSectionItem(304, "Rami", "ic_menu_rami");
        oHistoSection.addSectionItem(305, "Scrabble", "ic_menu_scrabble");
		
        section oJoueurSection = new section("Joueurs", "ic_menu_player");
        oJoueurSection.addSectionItem(401, "Ajout", "ic_menu_add");
        oJoueurSection.addSectionItem(402, "Liste", "ic_menu_player");
        oJoueurSection.addSectionItem(403, "Historique", "ic_menu_hist");
				
        sectionList.add(oGeneralSection);
        sectionList.add(oGameSection);
        sectionList.add(oHistoSection);
		sectionList.add(oJoueurSection);
        return sectionList;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {
    	mCallback.onArticleSelected((int)id);  	
        return false;
    }
}