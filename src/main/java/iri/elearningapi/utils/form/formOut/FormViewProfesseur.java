package iri.elearningapi.utils.form.formOut;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import iri.elearningapi.model.Message;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.courModel.ProfesseurModule;
import iri.elearningapi.model.courModel.Reponse;
import iri.elearningapi.model.userModel.Professeur;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;

public class FormViewProfesseur {
	private Professeur	professeur		= new Professeur();
	private int			moduleTotal		= 0;
	private int			reponseTotal	= 0;

	List<Message>	messages	= new ArrayList<>();
	List<Reponse>	reponses	= new ArrayList<>();

//@JsonIgnore
	List<Module> modules = new ArrayList<>();

	List<ModuleSimpleForm> moduleSimpleForms = new ArrayList<>();

	public FormViewProfesseur() {
	};

	public FormViewProfesseur(int idProfesseur, ProfesseurRepository professeurRepository,
			ModuleRepository moduleRepository) {
		super();
		if (professeurRepository.existsById(idProfesseur)) {
			this.professeur = professeurRepository.findById(idProfesseur).get();
			this.messages = this.professeur.getMessages();
			this.reponses = this.professeur.getReponses();
			if (this.professeur.getProfesseurModules() != null) {
				for (ProfesseurModule professeurModule : this.professeur.getProfesseurModules()) {
					ModuleSimpleForm newModuleSimpleForm = new ModuleSimpleForm();
					newModuleSimpleForm.setIdModule(professeurModule.getModule().getIdModule());
					newModuleSimpleForm.setTitre(professeurModule.getModule().getTitre());

					newModuleSimpleForm.setIsAcessible(professeurModule.getModule().getDateDeblocage() != null
							&& professeurModule.getModule().getDateDeblocage().before(new Date()));
					newModuleSimpleForm.setDateDeblocage(professeurModule.getModule().getDateDeblocage());
					moduleSimpleForms.add(newModuleSimpleForm);

				}
			}

			if (moduleRepository != null) {
				modules = moduleRepository.findAllByOrderByTitreAsc();
			}
		}
	}

	public FormViewProfesseur(int idProfesseur, ProfesseurRepository professeurRepository) {
		this(idProfesseur, professeurRepository, null);
	}

	public Professeur getProfesseur() {
		return professeur;
	}

	public void setProfesseur(Professeur professeur) {
		this.professeur = professeur;
	}

	public int getModuleTotal() {
		return moduleTotal;
	}

	public void setModuleTotal(int moduleTotal) {
		this.moduleTotal = moduleTotal;
	}

	public int getReponseTotal() {
		return reponseTotal;
	}

	public void setReponseTotal(int reponseTotal) {
		this.reponseTotal = reponseTotal;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Reponse> getReponses() {
		return reponses;
	}

	public void setReponses(List<Reponse> reponses) {
		this.reponses = reponses;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public List<ModuleSimpleForm> getModuleSimpleForms() {
		return moduleSimpleForms;
	}

	public void setModuleSimpleForms(List<ModuleSimpleForm> moduleSimpleForms) {
		this.moduleSimpleForms = moduleSimpleForms;
	}

	protected class ModuleSimpleForm {
		private int			idModule		= 0;
		protected String	titre;
		protected int		nbrChapitre		= 0;
		protected Boolean	isAcessible		= true;
		protected Date		dateDeblocage	= new Date();
		// protected

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


		public int getNbrChapitre() {
			return nbrChapitre;
		}

		public void setNbrChapitre(int nbrChapitre) {
			this.nbrChapitre = nbrChapitre;
		}

		public Date getDateDeblocage() {
			return dateDeblocage;
		}

		public void setDateDeblocage(Date dateDeblocage) {
			this.dateDeblocage = dateDeblocage;
		}
	}

}
