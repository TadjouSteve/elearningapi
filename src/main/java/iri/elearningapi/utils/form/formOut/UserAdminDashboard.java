package iri.elearningapi.utils.form.formOut;

public class UserAdminDashboard extends UserDashboard {
	private int		etudiantInscrit	= 0;
	private int		profActif		= 0;
	private int		profTotal		= 0;
	

	public int getEtudiantInscrit() {
		return etudiantInscrit;
	}

	public void setEtudiantInscrit(int etudiantInscrit) {
		this.etudiantInscrit = etudiantInscrit;
	}

	public int getProfActif() {
		return profActif;
	}

	public void setProfActif(int profActif) {
		this.profActif = profActif;
	}

	public int getProfTotal() {
		return profTotal;
	}

	public void setProfTotal(int profTotal) {
		this.profTotal = profTotal;
	}

	

}
