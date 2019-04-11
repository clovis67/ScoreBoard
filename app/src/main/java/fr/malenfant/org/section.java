package fr.malenfant.org;

import java.util.ArrayList;
import java.util.List;

public class section {

    private String title;
	private String icon;
    private List<sectionItem> sectionItems = new ArrayList<>();

    public section(String title, String icon) {
        this.title = title;
		this.icon = icon;
    }

    public void addSectionItem(long id, String title, String icon) {
        this.sectionItems.add( new sectionItem(id, title, icon));
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<sectionItem> getSectionItems() {
        return sectionItems;
    }
    
    public void setSectionItems(List<sectionItem> sectionItems) {
        this.sectionItems = sectionItems;
    }
    
}
