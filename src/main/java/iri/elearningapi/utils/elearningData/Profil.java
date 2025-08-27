package iri.elearningapi.utils.elearningData;

public enum Profil {
	ETUDIANT_USER("ETUDIANT"),
    ADMIN_USER("ADMIN"),
    PROFESSEUR_USER("PROFESSEUR");

	private final String value;

    Profil(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
	
	
	

}
