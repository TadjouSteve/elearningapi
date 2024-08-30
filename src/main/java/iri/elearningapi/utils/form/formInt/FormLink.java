package iri.elearningapi.utils.form.formInt;

public class FormLink {
	private int idModule = 0;
	private int idElement = 0;// pour tout les beans qui on un identifient de type Integer
	private boolean isLinked = false;
	private String texte;

	public int getIdModule() {
		return idModule;
	}

	public void setIdModule(int idModule) {
		this.idModule = idModule;
	}

	public boolean isLinked() {
		return isLinked;
	}

	public void setLinked(boolean isLinked) {
		this.isLinked = isLinked;
	}

	public boolean getLinked() {
		return isLinked;
	}

	public void setIsLinked(boolean isLinked) {
		this.isLinked = isLinked;
	}

	public boolean getIsLinked() {
		return isLinked;
	}

	public int getIdElement() {
		return idElement;
	}

	public void setIdElement(int idElement) {
		this.idElement = idElement;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

}
