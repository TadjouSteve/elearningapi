# elearningAPI

Backend de la Plateforme E-learning
Description
Ce backend est une API REST développée en Java Spring Boot, conçue pour gérer les fonctionnalités de la plateforme e-learning du programme des 1000 jeunes entrepreneurs.
Il s'agit d'une application basée sur une architecture robuste utilisant une base de données MySQL et prenant en charge des fonctionnalités comme :

Gestion des utilisateurs et authentification.
Téléchargement de vidéos et documents.
Envoi d'emails via un serveur SMTP.
Génération de certificats PDF.

# Prérequis

Avant de commencer, assurez-vous d'avoir les éléments suivants installés sur votre machine :

Java 17
Maven 3.x
MySQL 8.x
Eclipse IDE avec l'extension Spring Boot Tool Suite (STS).

# Installation

Étape 1 : Importer le projet dans Eclipse
Ouvrez Eclipse IDE.
Sélectionnez File > Import > Maven > Existing Maven Projects.
Naviguez jusqu'au dossier contenant le projet backend et cliquez sur Finish.
Étape 2 : Configuration de la base de données
Créez une base de données MySQL nommée elearningdata1000 (ou utilisez un autre nom, à configurer dans application.properties) :
sql
Copier le code
CREATE DATABASE elearningdata1000;
Modifiez le fichier application.properties pour y renseigner vos identifiants MySQL :
properties
Copier le code
spring.datasource.url=jdbc:mysql://localhost:3306/elearningdata1000
spring.datasource.username=<VOTRE_NOM_UTILISATEUR>
spring.datasource.password=<VOTRE_MOT_DE_PASSE>

# Étape 3 : Exécuter l'application

Faites un clic droit sur le projet dans Eclipse.
Sélectionnez Run As > Spring Boot App.
L'API sera accessible à l'adresse suivante :
http://localhost:9006/elearningapi

# Endpoints principaux

# Gestion des étudiants

Liste des étudiants : GET /admin/etudiants/{pageNumber}
Détails d’un étudiant : GET /admin/etudiant/{matricule}
Gestion des professeurs
Liste des professeurs : GET /admin/professeurs/{pageNumber}
Créer un professeur : POST /admin/professeur

# Gestion des cours

Liste des modules : GET /admin/modules
Créer un module : POST /admin/module

# Gestion des médias

Créer une rubrique : POST /admin/media/rubrique
Créer un article : POST /admin/media/article/{idRubrique}

# Gestion des fichiers

Les fichiers téléchargés (vidéos et documents) sont stockés dans les répertoires définis dans le fichier application.properties :

Vidéos : F:/workflow/momdoc
Documents : F:/workflow/momdocfichier
Ces chemins peuvent être modifiés selon vos besoins.

# Configuration des emails

L'application envoie des emails via un serveur SMTP. Voici un exemple de configuration dans application.properties :

properties
Copier le code
spring.mail.host=mail.programmeleadership.net
spring.mail.port=587
spring.mail.username=NePasRepondre_noReply@programmeleadership.net
spring.mail.password=4HOgODamm1GEmwEO

# Déploiement

Pour déployer sur un serveur distant :

Générez un fichier .war :
bash
Copier le code
mvn clean package
Déployez le fichier .war sur un serveur Tomcat configuré pour exécuter Spring Boot.
Technologies utilisées
Java 17
Spring Boot 2.7.1
MySQL
Maven
Eclipse IDE avec Spring Boot Tool Suite (STS)
