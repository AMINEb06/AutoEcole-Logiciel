# Gestion d’une Auto-École – Mini Projet Java

**Université Abderrahmane Mira – Béjaïa**  
**Département d’Informatique – 3ème année Licence**  
**Module : Java – Année universitaire 2024/2025**  

## Objectif
Créer une application Java permettant de gérer les activités d’une auto-école : élèves, moniteurs, véhicules, séances, examens et paiements.  
L’application utilise l’architecture **trois tiers** : Présentation / Logique métier / Base de données (MySQL).  

## Contenu du projet
Mini-Projet-AutoEcole/
├─ src/
│ └─ autoecole/
│ ├─ dao/ # Gestion des données (DAO)
│ ├─ model/ # Entités (Eleve, Moniteur, Vehicule, etc.)
│ └─ presentation/ # Interface utilisateur (Swing)
├─ lib/ 
├─ dist/ # .jar compilé 
├─ A2_AutoEcole.jar # Exécutable du projet
├─ Rapport_auto_école.pdf # Rapport complet du mini-projet
└─ README.md 


## Fonctionnalités principales
- Gestion des **élèves, moniteurs et véhicules**  
- Organisation des **séances de conduite et de théorie**  
- Suivi des **examens et des paiements**  
- Interface graphique simple et ergonomique avec Swing  

## Base de données
- MySQL est utilisé pour stocker toutes les informations.  
- Tables principales : `eleve`, `moniteur`, `vehicule`, `seance`, `examen`, `paiement`, `user`.  

