package iri.elearningapi.utils.form.formInt;

public class FormLink {
	private int idModule = 0;
	private int idElement = 0;// pour tout les beans qui on un identifient de type Integer
	private boolean isLinked = false;
	private String texte;
	private int note;
	
	private String matricule=null;
	private boolean sendToAll=true;
	private int number=0;;

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

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public boolean isSendToAll() {
		return sendToAll;
	}
	
	public boolean getSendToAll() {
		return sendToAll;
	}

	public void setSendToAll(boolean sendToAll) {
		this.sendToAll = sendToAll;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
