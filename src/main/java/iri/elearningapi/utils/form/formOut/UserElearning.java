package iri.elearningapi.utils.form.formOut;

import iri.elearningapi.utils.elearningData.Profil;

public class UserElearning {

	private int		id;
	private String	nom;
	private String	login			= "";
	private String	prenom;
	private String	email;
	private String	matricule;
	private String	telephone;
	private String	profession		= "";
	private String	nomEntreprise	= "";
	private String	matiere			= "";
	private String	password;
	private int		level;
	private Profil	profil;

	public UserElearning() {
		this.nom = "";
		this.prenom = "";
		this.email = "";
		this.matricule = "";
		this.telephone = "";
		this.password = "";

		this.profil = Profil.ETUDIANT_USER;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Profil getProfil() {
		return profil;
	}

	public void setProfil(Profil profil) {
		this.profil = profil;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getNomEntreprise() {
		return nomEntreprise;
	}

	public void setNomEntreprise(String entreprise) {
		this.nomEntreprise = entreprise;
	}

	public String getMatiere() {
		return matiere;
	}

	public void setMatiere(String matiere) {
		this.matiere = matiere;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
