package iri.elearningapi.utils.form.formOut;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.EtudiantChapitre;
import iri.elearningapi.model.courModel.EtudiantModule;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.userModel.Etudiant;
import iri.elearningapi.repository.courRepository.ChapitreRepository;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;

public class FormViewEtudiant {

	protected Etudiant	etudiant		= new Etudiant();
	protected int		moduleTotal		= 0;
	protected int		chapitreTotal	= 0;
	protected int		qcmTotal		= 0;

	HashMap<Integer, String>	listModule			= new HashMap<>();
	List<ChapitreSimpleform>	chapitreSimpleforms	= new ArrayList<>();
	List<ModuleSimpleForm>		moduleSimpleForms	= new ArrayList<>();

	protected List<EtudiantModule>		etudiantModules		= new ArrayList<>();
	protected List<EtudiantChapitre>	etudiantChapitres	= new ArrayList<>();

	public FormViewEtudiant() {
	};

	public FormViewEtudiant(int idEtudiant, EtudiantRepository etudiantRepository,
			ChapitreRepository chapitreRepository, ModuleRepository moduleRepository) {
		super();
		// TODO Auto-generated constructor stub
		for (Module module : moduleRepository.findAllByOrderByTitreAsc()) {
			listModule.put(module.getIdModule(), module.getTitre());
			ModuleSimpleForm moduleSimpleForm = new ModuleSimpleForm();
			moduleSimpleForm.setIdModule(module.getIdModule());
			moduleSimpleForm.setTitre(module.getTitre());
			moduleSimpleForm.setIsAcessible(
					(module.getDateDeblocage() != null && module.getDateDeblocage().before(new Date())));
			moduleSimpleForm.setdateDeblocage(module.getDateDeblocage());
			moduleSimpleForms.add(moduleSimpleForm);
		}

		for (Chapitre chapitre : chapitreRepository.findAll()) {
			ChapitreSimpleform chapitreSimpleform = new ChapitreSimpleform();
			chapitreSimpleform.setIdChapitre(chapitre.getIdChapitre());
			chapitreSimpleform.setIdModule((chapitre.getModule() != null) ? chapitre.getModule().getIdModule() : 0);
			chapitreSimpleform.setTitre(chapitre.getTitre());
			chapitreSimpleform.setQcm(chapitre.getQcms() != null ? chapitre.getQcms().size() : 0);
			chapitreSimpleforms.add(chapitreSimpleform);
		}

		if (etudiantRepository.existsById(idEtudiant)) {
			etudiant = etudiantRepository.findById(idEtudiant).get();
			etudiantChapitres = etudiant.getEtudiantChapitres();

			for (int i = 0; i < etudiantChapitres.size(); i++) {
				Chapitre chapitre1 = new Chapitre();
				chapitre1.setIdChapitre(etudiantChapitres.get(i).getChapitre().getIdChapitre());
				chapitre1.setTitre(etudiantChapitres.get(i).getChapitre().getTitre());
				etudiantChapitres.get(i).setChapitre(chapitre1);
			}

			etudiantModules = etudiant.getEtudiantModules();

			for (int i = 0; i < etudiantModules.size(); i++) {
				Module module1 = new Module();
				module1.setIdModule(etudiantModules.get(i).getModule().getIdModule());
				module1.setTitre(etudiantModules.get(i).getModule().getTitre());
				etudiantModules.get(i).setModule(module1);
			}
		}

	}

	public Etudiant getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(Etudiant etudiant) {
		this.etudiant = etudiant;
	}

	public int getModuleTotal() {
		return moduleTotal;
	}

	public void setModuleTotal(int moduleTotal) {
		this.moduleTotal = moduleTotal;
	}

	public int getChapitreTotal() {
		return chapitreTotal;
	}

	public void setChapitreTotal(int chapitreTotal) {
		this.chapitreTotal = chapitreTotal;
	}

	public int getQcmTotal() {
		return qcmTotal;
	}

	public void setQcmTotal(int qcmTotal) {
		this.qcmTotal = qcmTotal;
	}

	public HashMap<Integer, String> getListModule() {
		return listModule;
	}

	public void setListModule(HashMap<Integer, String> listModule) {
		this.listModule = listModule;
	}

	public List<EtudiantModule> getEtudiantModules() {
		return etudiantModules;
	}

	public void setEtudiantModules(List<EtudiantModule> etudiantModules) {
		this.etudiantModules = etudiantModules;
	}

	public List<EtudiantChapitre> getEtudiantChapitres() {
		return etudiantChapitres;
	}

	public void setEtudiantChapitres(List<EtudiantChapitre> etudiantChapitres) {
		this.etudiantChapitres = etudiantChapitres;
	}

	public List<ChapitreSimpleform> getChapitreSimpleforms() {
		return chapitreSimpleforms;
	}

	public void setChapitreSimpleforms(List<ChapitreSimpleform> chapitreSimpleforms) {
		this.chapitreSimpleforms = chapitreSimpleforms;
	}

	public List<ModuleSimpleForm> getModuleSimpleForms() {
		return moduleSimpleForms;
	}

	public void setModuleSimpleForms(List<ModuleSimpleForm> moduleSimpleForms) {
		this.moduleSimpleForms = moduleSimpleForms;
	}

	protected class ChapitreSimpleform {
		protected int		idChapitre;
		protected int		idModule;
		protected String	titre;
		protected int		qcm;

		public int getIdChapitre() {
			return idChapitre;
		}

		public void setIdChapitre(int idChapitre) {
			this.idChapitre = idChapitre;
		}

		public int getIdModule() {
			return idModule;
		}

		public void setIdModule(int idModule) {
			this.idModule = idModule;
		}

		public String getTitre() {
			return titre;
		}

		public void setTitre(String titre) {
			this.titre = titre;
		}

		public int getQcm() {
			return qcm;
		}

		public void setQcm(int qcm) {
			this.qcm = qcm;
		}

	}

	protected class ModuleSimpleForm {
		protected int		idModule;
		protected String	titre;
		protected Boolean	isAcessible		= true;
		protected Date		dateDeblocage	= new Date();

		public int getIdModule() {
			return idModule;
		}

		public void setIdModule(int idModule) {
			this.idModule = idModule;
		}

		public String getTitre() {
			return titre;
		}

		public void setTitre(String titre) {
			this.titre = titre;
		}

		public Boolean getIsAcessible() {
			return isAcessible;
		}

		public void setIsAcessible(Boolean isAcessible) {
			this.isAcessible = isAcessible;
		}

		public Date getdateDeblocage() {
			return dateDeblocage;
		}

		public void setdateDeblocage(Date dateDeblocage) {
			this.dateDeblocage = dateDeblocage;
		}

	}
}
