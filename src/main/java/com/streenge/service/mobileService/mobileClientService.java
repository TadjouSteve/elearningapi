package com.streenge.service.mobileService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.streenge.model.admin.Utilisateur;
import com.streenge.model.contact.Client;
import com.streenge.model.produit.Groupe;
import com.streenge.model.produit.Produit;
import com.streenge.model.vente.BonCommande;
import com.streenge.repository.adminRepository.UtilisateurRepository;
import com.streenge.repository.contactRepo.ClientRepository;
import com.streenge.repository.produitRepo.GroupeRepository;
import com.streenge.repository.produitRepo.ProduitRepository;
import com.streenge.repository.venteRepo.BonCommandeRepository;
import com.streenge.service.produitService.GroupeService;
import com.streenge.service.produitService.ProduitService;
import com.streenge.service.utils.erroclass.ErrorAPI;
import com.streenge.service.utils.erroclass.StreengeException;
import com.streenge.service.utils.formData.FormGroupe;
import com.streenge.service.utils.formInt.FormPanier;
import com.streenge.service.utils.formInt.LinePanier;
import com.streenge.service.utils.formInt.LinePanierMobile;
import com.streenge.service.utils.formInt.FormPanierMobile;
import com.streenge.service.utils.formOut.FormBonCommande;
import com.streenge.service.utils.formOut.FormViewProduit;
import com.streenge.service.utils.streengeData.ColorStatut;
import com.streenge.service.utils.streengeData.Data;
import com.streenge.service.utils.streengeData.Etat;
import com.streenge.service.utils.streengeData.Profil;
import com.streenge.service.utils.streengeData.Statut;
import com.streenge.service.venteService.BonCommandeService;

@Service
public class mobileClientService {
	@Autowired
	private ProduitService		produitService;
	@Autowired
	private ProduitRepository	produitRepository;
	@Autowired
	GroupeRepository			groupeRepository;
	@Autowired
	private GroupeService		groupeService;
	
	@Autowired
	private ClientRepository  clientRepository;
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private BonCommandeService bonCommandeService;
	
	@Autowired
	private BonCommandeRepository bonCommandeRepository;

	public List<FormGroupe> getListFormGroupeWithProduit() {
		List<FormGroupe> formGroupes = new ArrayList<>();
		for (Groupe groupe : groupeRepository.findAllByOrderByPrioriteAsc()) {
			if (groupe.getGroupeProduits() != null && !groupe.getGroupeProduits().isEmpty()) {
				formGroupes.add(groupeService.getFormGroupeWithListFormProduitsForMobile(groupe.getId()));
			}
		}

		return formGroupes;
	}

	public List<FormViewProduit> researchProduitsByMolie(String filter) {
		List<Produit> listProduits;
		List<FormViewProduit> formViewProduits = new ArrayList<>();

		if (filter != null && !filter.isBlank()) {
			filter = filter.trim();
			listProduits = produitRepository.findByNomStartingWithOrSkuStartingWithOrderByNomAsc(filter, filter);
		} else {
			listProduits = produitRepository.findAllByOrderByNomAsc();
		}

		for (Produit produit : listProduits) {
			if (!produit.getEtat().equals(Etat.getDelete()) && !Data.typeService.equals(produit.getType())
					&& produit.getMedicament() != null) {
				formViewProduits.add(produitService.generateSimpleFormViewProduitForMobile(produit.getIdProduit()));
			}
		}

		return formViewProduits;
	}
	
	
	public BonCommande createBonCommande(FormPanierMobile formPanierMobile) {
		BonCommande bonCommande=null;
		
		Client client =new Client();
		if (clientRepository.existsById(formPanierMobile.getIdClient())) {
			client=clientRepository.findById(formPanierMobile.getIdClient()).get();
		}else {
			ErrorAPI errorAPI = new ErrorAPI();
			errorAPI.setMessage(
					"Votre compte Client n'est pas enregister..!");
			throw new StreengeException(errorAPI);
		}
		
		Utilisateur utilisateur=new Utilisateur();
		if (utilisateurRepository.findByNom("Mobile_Phone").isEmpty()) {
			utilisateur.setNom("Mobile_Phone");
			utilisateur.setPassword("x-x-x-x");
			utilisateur.setProfil(Profil.vendeur);
			utilisateur=utilisateurRepository.save(utilisateur);
		}else {
			utilisateur=utilisateurRepository.findByNom("Mobile_Phone").get(0);
		}
		
		if (formPanierMobile!=null) {
			FormPanier formPanier= new FormPanier();
			formPanier.setNomClient(client.getNom());
			formPanier.setUtilisateur(utilisateur);
			formPanier.setNote("Commande enregister par le client depuis son mobile..!");
			
			for (LinePanierMobile linePanierMobile : formPanierMobile.getLines()) {
				Produit produit =produitRepository.findById(linePanierMobile.getFormProduit().getId()).get();
				FormViewProduit formViewProduit= produitService.generateSimpleFormViewProduitForMobile(linePanierMobile.getFormProduit().getId());
				LinePanier linePanier=new LinePanier();
				Boolean haveCommercialNAme=  (formViewProduit.getFormMedicament().getNomCommercial()!=null && !formViewProduit.getFormMedicament().getNomCommercial().isBlank());
				
				
				Boolean haveSpecialPack=(produit.getMedicament()!=null && produit.getMedicament().getPack()!=null && !produit.getMedicament().getPack().isBlank());
				String nomPack=haveSpecialPack?produit.getMedicament().getPack():produit.getPacks().get(0).getNom();
				
				String designation=formViewProduit.getNom()+(haveCommercialNAme?("("+formViewProduit.getFormMedicament().getNomCommercial()+")"):"")+" - "+nomPack;
				linePanier.setDesignation(designation);
				linePanier.setIdPack(produit.getPacks().get(0).getId());
				linePanier.setCoutUMP(produit.getCout_UMP());
				linePanier.setQuantite(linePanierMobile.getQuantite());
				linePanier.setPrix(formViewProduit.getPrixVente());
				linePanier.setDiviseur(1);
				linePanier.setTaxe(null);
				formPanier.addLine(linePanier);
			}
			
			bonCommande = bonCommandeService.createOrUpdateBonCommande(formPanier, true);
		}
		
		return bonCommande;
	}
	
	public List<FormPanierMobile> getBonCommandeClientMobile(String IdClient) {
		
		List<FormPanierMobile> formPanierMobiles=new ArrayList<>();
		
		if (clientRepository.existsById(IdClient)) {
			Client client=clientRepository.findById(IdClient).get();
			List<BonCommande> bonCommandes=client.getBonCommandes();
			System.out.println("Nombre boncommmande:"+IdClient+" == "+bonCommandes.size());
			for (int i=0 ;i<bonCommandes.size();i++ ) {
				BonCommande bonCommande=bonCommandes.get(i);
				FormBonCommande formBonCommande=bonCommandeService.getPanierBonCommande(bonCommande.getIdBonCommande());
				FormPanierMobile formPanierMobile=new FormPanierMobile();
				formPanierMobile.setDateAjout(formBonCommande.getDateCreation());
				formPanierMobile.setIdBonCommande(bonCommande.getIdBonCommande());
				formPanierMobile.setIdClient(IdClient);
				formPanierMobile.setNomClient(client.getNom());
				formPanierMobile.setStatut(Statut.getAnnuler().equals(bonCommande.getStatut())?Statut.getAnnuler():formBonCommande.getStatutLivraison());
				formPanierMobile.setColorStatut(ColorStatut.getColor(formPanierMobile.getStatut()));
				formPanierMobile.setStatutPaiement(formBonCommande.getStatutPaiement());
				
				for (LinePanier linePanier : formBonCommande.getLines()) {
					LinePanierMobile linePanierMobile=new LinePanierMobile();
					linePanierMobile.setDesignation(linePanier.getDesignation());
					linePanierMobile.setFormProduit(produitService.generateSimpleFormViewProduitForMobile(linePanier.getIdProduit()));
					linePanierMobile.setPrix(linePanier.getPrix());
					linePanierMobile.setQuantite(linePanier.getQuantite());
					linePanierMobile.setRestant(linePanier.getRestant());
					
					formPanierMobile.addLine(linePanierMobile);
				}
				
				formPanierMobiles.add(formPanierMobile);
			}
		}
		
		return formPanierMobiles;
	}
	
	public void annulationBomCommandeParClientMobile(String idBonCommande,String idClient) {
		System.out.println("IdBonCommande: "+idBonCommande);
		System.out.println("IdClient: "+idClient);
		Utilisateur utilisateur=new Utilisateur();
		if (utilisateurRepository.findByNom("Mobile_Phone").isEmpty()) {
			utilisateur.setNom("Mobile_Phone");
			utilisateur.setPassword("x-x-x-x");
			utilisateur.setProfil(Profil.vendeur);
			utilisateur=utilisateurRepository.save(utilisateur);
		}else {
			utilisateur=utilisateurRepository.findByNom("Mobile_Phone").get(0);
		}
		
		if (clientRepository.existsById(idClient) && bonCommandeRepository.existsById(idBonCommande)) {
			BonCommande bonCommande=bonCommandeRepository.findById(idBonCommande).get();
			Client client=clientRepository.findById(idClient).get();
			if (bonCommande.getClient()!=null && bonCommande.getClient().getIdClient().endsWith(client.getIdClient())) {
				bonCommandeService.cancelBonCommande(idBonCommande, utilisateur.getId());
			}
		}
	}
	
}
