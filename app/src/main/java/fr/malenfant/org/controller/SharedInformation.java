package fr.malenfant.org.controller;

import android.provider.BaseColumns;

public class SharedInformation {

	public SharedInformation() {
	}
	
	public static final class type implements BaseColumns {
		private type() {}
		public static final String TYPE_JOUEUR = "joueur";
		public static final String TYPE_PARAM = "parametre";
		public static final String TYPE_FISH = "poisson";
	}
}
