package iri.elearningapi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.text.AbstractDocument.LeafElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iri.elearningapi.model.courModel.Chapitre;
import iri.elearningapi.model.courModel.ChapitreEn;
import iri.elearningapi.model.courModel.GammeEtudiantModule;
import iri.elearningapi.model.courModel.Module;
import iri.elearningapi.model.userModel.GammeEtudiant;
import iri.elearningapi.repository.courRepository.ChapitreRepository;
import iri.elearningapi.repository.courRepository.EtudiantChapitreRepository;
import iri.elearningapi.repository.courRepository.GammeEtudiantModuleRepository;
import iri.elearningapi.repository.courRepository.ModuleRepository;
import iri.elearningapi.repository.courRepository.QuestionCourRepository;
import iri.elearningapi.repository.userRepository.EtudiantRepository;
import iri.elearningapi.repository.userRepository.GammeEtudiantRepository;
import iri.elearningapi.repository.userRepository.ProfesseurRepository;
import iri.elearningapi.utils.elearningData.Etat;
import iri.elearningapi.utils.errorClass.ElearningException;
import iri.elearningapi.utils.errorClass.ErrorAPI;
import iri.elearningapi.utils.form.formInt.FormFilter;
import iri.elearningapi.utils.form.formInt.FormLink;
import iri.elearningapi.utils.form.formOut.FormChapitre;
import iri.elearningapi.utils.form.formOut.FormViewChapitre;
import iri.elearningapi.utils.form.formOut.FormViewModule;

@Service
public class ContenuFormationService {

	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	private ProfesseurRepository professeurRepository;

	@Autowired
	private ModuleRepository moduleRepository;

	@Autowired
	private QuestionCourRepository questionCourRepository;

	// @Autowired
	// private EtudiantModuleRepository etudiantModuleRepository;

	@Autowired
	private EtudiantChapitreRepository etudiantChapitreRepository;

	@Autowired
	private ChapitreRepository chapitreRepository;

	@Autowired
	private GammeEtudiantRepository gammeEtudiantRepository;

	@Autowired
	private GammeEtudiantModuleRepository gammeEtudiantModuleRepository;

	public List<Module> getListModuule(FormFilter filter) {
		List<Module> modules = new ArrayList<Module>();
		if (filter != null && filter.getTitre() != null && !filter.getTitre().isBlank()) {
			modules = moduleRepository.findAllByTitreContainingOrderByTitreAsc(filter.getTitre());
		} else {
			modules = moduleRepository.findAllByOrderByTitreAsc();
		}
		return modules;
	}

	public Module getModule(int idModule) {
		if (moduleRepository.existsById(idModule)) {
			return moduleRepository.findById(idModule).get();
		} else {
			throw new ElearningException(new ErrorAPI("Le module rehercher n'existe pas..!"));
		}
	}

	public FormViewModule getFormViewModule(int idModule) {
		FormViewModule formViewModule = new FormViewModule();
		if (moduleRepository.existsById(idModule)) {
			Module module = moduleRepository.findById(idModule).get();
			formViewModule.setModule(module);
			formViewModule.setNombreCour(module.getChapitres().size());
			for (GammeEtudiantModule gammeEtudiantModule : module.getGammeEtudiantModules()) {
				gammeEtudiantModule.getGammeEtudiant()
						.setNombreEtudiant(gammeEtudiantModule.getGammeEtudiant().getEtudiants().size());

				formViewModule.addGammeEtudiant(gammeEtudiantModule.getGammeEtudiant());
			}
			for (Chapitre chapitre : module.getChapitres()) {
				FormChapitre formChapitre = new FormChapitre();
				formChapitre.setIdChapitre(chapitre.getIdChapitre());
				formChapitre.setIdModule(idModule);
				formChapitre.setImageURL(chapitre.getImage());
				formChapitre.setTitre(chapitre.getTitre());
				formChapitre.setTotalQcm(chapitre.getQcms().size());
				formChapitre.setOrdre(chapitre.getOrdre());
				
				formViewModule.setNombreQCM(formViewModule.getNombreQCM() + formChapitre.getTotalQcm());
				formViewModule.addFormChapitre(formChapitre);
			}

			formViewModule.setAllGammeEtudiants(gammeEtudiantRepository.findAll());// cette ligne est la pour une
																					// facilitation , pas optimal
			return formViewModule;
		} else {
			throw new ElearningException(new ErrorAPI("Le module rehercher n'existe pas..!"));
		}

	}

	public Module createOrUpdateModule(Module module, boolean isNew) {
		controlModule(module, isNew);
		if (isNew) {
			module.setEtat(Etat.ACTIF.name());
		}
		module = moduleRepository.save(module);
		return module;
	}

	private void controlModule(Module module, boolean isNew) {
		if (module == null) {
			throw new ElearningException(new ErrorAPI("Impossible de creer ce module, il semble etre null"));
		}

		if (module.getTitre() == null || module.getTitre().isBlank() || module.getTitre().length() < 5) {
			throw new ElearningException(
					new ErrorAPI("Le titre du module doit ne peut pas etre vide ou contenier moins de 5 caractères"));
		}

		if (module.getTitreEn() == null || module.getTitreEn().isBlank() || module.getTitreEn().length() < 5) {
			throw new ElearningException(new ErrorAPI(
					"La version anglaise du titre du module doit ne peut pas etre vide ou contenier moins de 5 caractères"));
		}

		if (moduleRepository.existsByTitre(module.getTitre())) {
			if (isNew) {
				throw new ElearningException(new ErrorAPI("Un autre module existe deja avec ce titre"));
			} else if (moduleRepository.findByTitre(module.getTitre()).getIdModule() != module.getIdModule()) {
				throw new ElearningException(new ErrorAPI("Un autre module existe deja avec ce titre"));
			}
		}

	}

	public void linkGammeEtudiantToModule(int idModule, List<FormLink> formLinks) {
		if (moduleRepository.existsById(idModule)) {
			Module module = moduleRepository.findById(idModule).get();
			for (FormLink formLink : formLinks) {
				if (gammeEtudiantRepository.existsById(formLink.getIdElement())) {
					GammeEtudiant gammeEtudiant = gammeEtudiantRepository.findById(formLink.getIdElement()).get();
					boolean isAlReadyLink = module.getGammeEtudiantModules().stream()
							.anyMatch(item -> (item.getGammeEtudiant() != null
									&& item.getGammeEtudiant().getId() == gammeEtudiant.getId()));
					if (formLink.isLinked()) {
						if (!isAlReadyLink) {

							GammeEtudiantModule gammeEtudiantModule = new GammeEtudiantModule();
							gammeEtudiantModule.setGammeEtudiant(gammeEtudiant);
							gammeEtudiantModule.setModule(module);
							gammeEtudiantModuleRepository.save(gammeEtudiantModule);
							// System.out.println("enregistrement entre du lien entre la gamme d'etudiant et
							// le module");
						}
					} else {
						if (isAlReadyLink) {
							Optional<GammeEtudiantModule> gammeEtudiantModuleOptional = module.getGammeEtudiantModules()
									.stream().filter(item -> (item.getGammeEtudiant() != null
											&& item.getGammeEtudiant().getId() == module.getIdModule()))
									.findAny();
							if (gammeEtudiantModuleOptional.isPresent()) {
								gammeEtudiantModuleRepository.deleteEasy(gammeEtudiantModuleOptional.get().getId());
								// System.out.println("supression du lien");
							}
						}
					}
				}
			}

		}
	}

	public List<FormChapitre> getListChapitre(FormFilter filter) {
		List<FormChapitre> formChapitres = new ArrayList<FormChapitre>();
		List<Chapitre> listChapitres = new ArrayList<Chapitre>();

		if (filter != null && filter.getTitre() != null && !filter.getTitre().isBlank()) {
			listChapitres = chapitreRepository.findAllByTitreContainingOrderByTitreAsc(filter.getTitre());
		} else {
			listChapitres = chapitreRepository.findAllByOrderByTitreAsc();

		}
		for (Chapitre chapitre : listChapitres) {
			FormChapitre formChapitre = new FormChapitre();
			formChapitre.setIdChapitre(chapitre.getIdChapitre());
			formChapitre.setIdModule(chapitre.getModule() != null ? chapitre.getModule().getIdModule() : 0);
			formChapitre.setTitre(chapitre.getTitre());
			formChapitre.setOrdre(chapitre.getOrdre());
			formChapitre.setTitreEn((chapitre.getChapitreEns() != null && !chapitre.getChapitreEns().isEmpty())
					? chapitre.getChapitreEns().get(0).getTitre()
					: "");
			formChapitre.setTitreModule(chapitre.getModule() != null ? chapitre.getModule().getTitre() : null);
			formChapitre.setTotalQcm(chapitre.getQcms().size());
			formChapitre.setEtat(chapitre.getEtat());

			formChapitres.add(formChapitre);
		}
		return formChapitres;
	}

	public FormViewChapitre getFormViewChapitre(int idChapitre) {
		// TODO Auto-generated method stub
		if (chapitreRepository.existsById(idChapitre)) {
			Chapitre chapitre = chapitreRepository.findById(idChapitre).get();
			FormViewChapitre formViewChapitre = new FormViewChapitre();
			formViewChapitre.setChapitre(chapitre);
			formViewChapitre.setChapitreEn((chapitre.getChapitreEns() != null && !chapitre.getChapitreEns().isEmpty())
					? chapitre.getChapitreEns().get(0)
					: null);

			
			return formViewChapitre;
		} else {
			throw new ElearningException(new ErrorAPI("Le chapitre rehercher n'existe pas..!"));
		}
	}
	
	public Chapitre createOrUpdateChapitre(Chapitre chapitre,boolean isNew) {
		controlChapitre(chapitre);
		if (isNew) {
			chapitre.setDateAjout(new Date());
			chapitre.setEtat(Etat.ACTIF.name());
		}
		
		if (chapitre.getModule()!=null && moduleRepository.existsById(chapitre.getModule().getIdModule())) {
			Module module= moduleRepository.findById(chapitre.getModule().getIdModule()).get();
			chapitre.setModule(module);
		}
		
		chapitre= chapitreRepository.save(chapitre);
		return chapitre;
	}
	
	private void controlChapitre(Chapitre chapitre) {
		if(chapitre==null) {
			throw new ElearningException(new ErrorAPI("impossible de creer un chapitre avec ce formulaire, il est null..!"));
		}
		
		if (chapitre.getModule()==null || !moduleRepository.existsById(chapitre.getModule().getIdModule())) {
			throw new ElearningException(new ErrorAPI("Le module de formation selectionner pour ce chapitre n'existe pas, choissisez un module conforme..!"));
		}
		
		if (chapitre.getTitre()==null || chapitre.getTitre().isBlank() || chapitre.getTitre().length()<10) {
			throw new ElearningException(new ErrorAPI("Le titre du chapitre ne peut pas etre vide ou contenier moins de 5 carracteres..!"));
		}
		
		if (chapitre.getVideo()==null && chapitre.getImage()==null) {
			throw new ElearningException(new ErrorAPI("Au moins une ou lien de video foit etre utiliser pour ce chapitre"));
		}
		
		if (chapitre.getOrdre()<0 || chapitre.getOrdre()>100) {
			throw new ElearningException(new ErrorAPI("Le numero d'orde du cour / chapitre doit etre compris entre 0 et 100"));

		}
	}

}
