package com.streenge.service.utils.streengeData;

public class Periode {
	public final static int	aujourdhui		= 1;
	public final static int	cetteSemaine	= 2;
	public final static int	ceMois			= 3;
	public final static int	moisPasse		= 4;
	public final static int	cetteAnnee		= 5;
	public final static int	anneePasse		= 6;
	
	public static String getPeriodeName(int periode) {
		String name="";
		if (periode==Periode.aujourdhui){
			name="Aujourd'hui";
		}else if (periode==Periode.cetteSemaine) {
			name="Cette semaine";
		}else if(periode==Periode.ceMois){
			name="Ce mois";
		}else if(periode==Periode.moisPasse){
			name="Le mois passé";
		}else if(periode==Periode.cetteAnnee){
			name="Cette année";
		}else if (periode==Periode.anneePasse) {
			name="L'année passé";
		}else {
			name="Ce mois";
		}
		
		return name;
	}
}
