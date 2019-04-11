package fr.malenfant.org.controller;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbGame extends SQLiteOpenHelper{

	// Nom de la DB (nom du fichier SQLite).  
	public static String DATABASE_NAME = "scoreboard.db";
	private String DATABASE_PATH; // chemin défini dans le constructeur
    /**  
     * Numéro de version de la DB.  
     * Si ce numéro change, la fonction onUpgrade est exécutée  
     * pour effacer le contenu de la DB et recréer la nouvelle  
     * version du schéma.  
     */ 
	public static final int DATABASE_VERSION = 1;  
	
    //Lien vers la session DB.  
	public SQLiteDatabase db; 
	
    // Nom des tables de la DB
	public static String TABLE_JEU = "TABLE_JEU";  
	public static String TABLE_CHEMIN = "TABLE_CHEMIN";
	public static String TABLE_COLOR = "TABLE_COLOR";
	public static String TABLE_JOUEUR = "TABLE_JOUEUR";
	public static String TABLE_PARAM = "TABLE_PARAM";
	public static String TABLE_POINT = "TABLE_POINT";
	public static String TABLE_SCORE = "TABLE_SCORE";
	public static String TABLE_VERSION = "TABLE_VERSION";

	// Script SQL de création des tables
	public static String CREATE_JOUEUR = " CREATE TABLE " + TABLE_JOUEUR + " (" +
			"SEQNR	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
			"NOM	TEXT NOT NULL, " +
			"DELE	TEXT );";
	
	public DbGame(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase(); 
		// check si la table existe ou  non
       	Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE  name = '" + TABLE_JOUEUR + "'", null);
       	int nb = c.getCount();
       	if ( nb == 0){
       		onCreate(db);
       	}  
    }

    // Exécuté si la DB n'existe pas.  
    public void onCreate(SQLiteDatabase db) {  
		Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE  name = '" + TABLE_JOUEUR + "'", null);
       	int nb = c.getCount();
       	if ( nb == 0){
       		db.execSQL(CREATE_JOUEUR);
       	} 
    } 

    // Exécuté chaque fois que le numéro de version de DB change.  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
    } 
    
    // On efface toutes les données de la table  
    public void onReset(String TABLE) {  
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);    
    }
    
	/********************************************************************************************/
	// Gestion de la table joueur
	// Compter le nombre de joueur
	public int nbjoueur(){
	Cursor c = db.rawQuery("SELECT * FROM " + TABLE_JOUEUR + " WHERE DELE != 'X'", null);
        return c.getCount();
	}
	// Check si le joueur existe ou  non
	public int existJoueur(String name) {  	
	Cursor c = db.rawQuery("SELECT * FROM " + TABLE_JOUEUR + " WHERE NOM = '" + name + "' AND DELE != 'X'", null);
        return c.getCount();
    }
	// Suppression du joueur
	public long deleteJoueur(ContentValues newjoueur) {
	int flag = 0;	
	Cursor c;
	c = db.rawQuery("SELECT * FROM " + TABLE_JOUEUR + " WHERE SEQNR = '" + newjoueur.getAsString("SEQNR") + "'", null);
	int nb = c.getCount();
	if( nb == 0 ){
		flag = 9;
	}else{
		c = db.rawQuery("UPDATE " + TABLE_JOUEUR + "SET DELE = 'X' WHERE SEQNR = '" + newjoueur.getAsString("SEQNR") + "'", null);
	}
	return flag;
    }
	// Insertion d'un joueur
    public long insertJoueur(ContentValues newjoueur) {
	    Cursor c;
        c = db.rawQuery("SELECT * FROM " + TABLE_JOUEUR + " WHERE NOM = '" + newjoueur.getAsString("NOM") + "'", null);
        int nb = c.getCount();
        if ( nb == 0){
            db.insert(TABLE_JOUEUR, null, newjoueur);
			return 1;
		}else{
            return 9;
        }
    }
	// Insertion d'un joueur
	public long modifJoueur(ContentValues newjoueur) {
		Cursor c;
		c = db.rawQuery("SELECT * FROM " + TABLE_JOUEUR + " WHERE SEQNR = '" + newjoueur.getAsString("SEQNR") + "'", null);
		int nb = c.getCount();
		if ( nb != 0){
		    db.update(TABLE_JOUEUR, newjoueur, null, null);
			return 1;
		}else{
			return 9;
		}
	}
	// Liste des joueurs
	public ArrayList<String> getAllJoueurs() {  
	ArrayList<String> output = new ArrayList<>();
	String[] colonnesARecup = new String[] { "SEQNR", "NOM" };
	Cursor cursorResults = db.rawQuery("SELECT * FROM " + TABLE_JOUEUR + " WHERE DELE != 'X'", null);
	if (null != cursorResults) {  
		if (cursorResults.moveToFirst()) {  
			do {  
				int column1 = cursorResults.getColumnIndex("SEQNR");  
				int column2 = cursorResults.getColumnIndex("NOM");
				String joueur = cursorResults.getString(column1)  
						+ "/" + cursorResults.getString(column2);  
				output.add(joueur);  
			} while (cursorResults.moveToNext());  
		} // end§if  
	}    
	return output;  
    } 
	// Remplissage de la listview des joueurs
	public ArrayList<HashMap<String, String>> fillListview(){
	Cursor c;
	c = db.rawQuery("SELECT * FROM " + TABLE_JOUEUR + " WHERE DELE != 'X'", null);
	ArrayList<HashMap<String, String>> output = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map = new HashMap<String, String>();
	int nb = c.getCount();
	String[] result;
	if (nb == 0){
		// pas de joueur
		map = new HashMap<String, String>();
		map.put("nom", "Invité");
		map.put("id", "00");
		output.add(map);
	}
	if (nb != 0){
		// il existe un ou des joueurs    	
		ArrayList<String> listej = getAllJoueurs();
		if(!listej.isEmpty()){
			int len = listej.size();
			for(int i = 0; i<len; i++){
				result = listej.get(i).split("/",2);
				map = new HashMap<String, String>();
				map.put("nom", result[1]);
				map.put("id", result[0]);
				output.add(map);
			}
		}
	}    	
	return output;
    }


}
