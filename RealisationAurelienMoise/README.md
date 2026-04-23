# Projet 1 — Gestion de notes des étudiants (V3)

Programme Java permettant de gérer les étudiants, leurs cours et leurs notes.
Le programme lit un fichier CSV d'entrée, calcule la moyenne de chaque étudiant,
les classe par ordre de mérite, sauvegarde le résultat dans un fichier CSV de
sortie et affiche des statistiques globales.

Cette version (V3) a été restructurée pour mettre en œuvre explicitement :
**principes SOLID, gestion des exceptions, classes abstraites, interfaces,
héritage et collections d'objets**.

## Membres de l'équipe

| Prénom    | Rôle principal                                                 |
|-----------|----------------------------------------------------------------|
| Moïse     | Modèle (`Personne`, `Etudiant`, `Note`), exceptions, I/O CSV   |
| Aurélien  | Logique métier, affichage, orchestration `Main`, Strategy      |

Voir `contributions.txt` pour le détail des tâches de chaque membre.

## Fonctionnalités développées

- Lecture des données d'étudiants et de notes depuis un fichier CSV
  (séparateur `;`, accepte `.` ou `,` comme séparateur décimal).
- Regroupement automatique des notes par matricule d'étudiant.
- Calcul de la moyenne, de la meilleure et de la pire note de chaque étudiant.
- Classement des étudiants par moyenne décroissante (tri naturel via
  `Comparable`).
- Calcul de statistiques globales (moyenne générale, nombre d'admis,
  taux de réussite).
- Attribution d'une mention selon un **barème interchangeable** (français ou
  nord-américain, voir `IStrategieMention`).
- Sauvegarde des résultats dans un fichier CSV.
- **Gestion des erreurs en deux niveaux** :
  - erreurs locales (ligne mal formée, note hors bornes, matricule vide,
    valeur non numérique) → la ligne est rejetée, un avertissement est
    affiché sur `System.err`, la lecture continue ;
  - erreurs globales (fichier introuvable, E/S impossible) → lancement
    d'une exception personnalisée qui arrête le programme proprement avec
    un code de sortie identifiable.

## Concepts Java mis en œuvre

### Classe abstraite et héritage

- `Personne` est une **classe abstraite** (mot-clé `abstract`). Elle définit
  les attributs communs (`matricule`, `prenom`, `nom`) et une méthode
  abstraite `getCategorie()`.
- `Etudiant extends Personne` : redéfinit `getCategorie()` et ajoute la
  liste de notes. Le constructeur appelle `super(...)` pour la validation.
- La hiérarchie est prête à accueillir d'autres types (`Enseignant`,
  `Employe`, etc.) sans toucher aux classes existantes.

### Interfaces et polymorphisme

Quatre interfaces définies par l'équipe :

| Interface              | Paquet    | Implémentations                                   |
|------------------------|-----------|---------------------------------------------------|
| `ILecteurEtudiants`    | `io`      | `LecteurCSV` (d'autres formats possibles)         |
| `IEcrivainResultats`   | `io`      | `EcrivainResultats`                               |
| `IStrategieMention`    | `service` | `MentionFrancaise`, `MentionNordAmericaine`       |
| `IAfficheur`           | `util`    | `AfficheurConsole`                                |

`Etudiant implements Comparable<Etudiant>` permet d'utiliser directement
`Collections.sort(liste)` grâce à l'ordre naturel (moyenne décroissante).

### Exceptions personnalisées (avec héritage)

```
Exception  (JDK)
  ├── DonneeInvalideException
  │     ├── NoteInvalideException
  │     └── MatriculeInvalideException
  ├── LectureException
  └── EcritureException
```

Tous les constructeurs des classes du modèle (`Personne`, `Note`) valident
leurs arguments et **lèvent des exceptions claires** en cas de problème
(`throw new`). Impossible de créer un objet incohérent.

### Principes SOLID

| Lettre | Principe                  | Mise en œuvre                                         |
|--------|---------------------------|-------------------------------------------------------|
| S      | Single Responsibility     | Un rôle par classe : `Etudiant` = un étudiant, `GestionnaireEtudiants` = la collection, `LecteurCSV` = lecture seulement, etc. |
| O      | Open/Closed               | `IStrategieMention` : ajouter un barème = ajouter une classe (pas modifier). |
| L      | Liskov Substitution       | `Etudiant` substituable à `Personne` partout ; respect du contrat de la classe mère. |
| I      | Interface Segregation     | Quatre interfaces ciblées plutôt qu'une grosse. |
| D      | Dependency Inversion      | `Main` ne dépend que d'interfaces ; les classes concrètes ne sont instanciées qu'à un seul endroit. |

## Structure du projet

```
GestionNotesEtudiantsV3/
├── src/
│   ├── Main.java                       # Point d'entrée + injection dépendances
│   ├── exceptions/
│   │   ├── DonneeInvalideException.java   # Exception mère (hiérarchie)
│   │   ├── NoteInvalideException.java     # extends DonneeInvalideException
│   │   ├── MatriculeInvalideException.java
│   │   ├── LectureException.java
│   │   └── EcritureException.java
│   ├── modele/
│   │   ├── Personne.java               # abstract class
│   │   ├── Etudiant.java               # extends Personne, implements Comparable
│   │   └── Note.java                   # final class (immuable)
│   ├── io/
│   │   ├── ILecteurEtudiants.java      # interface
│   │   ├── IEcrivainResultats.java     # interface
│   │   ├── LecteurCSV.java             # implements ILecteurEtudiants
│   │   └── EcrivainResultats.java      # implements IEcrivainResultats
│   ├── service/
│   │   ├── IStrategieMention.java      # interface (Strategy pattern)
│   │   ├── MentionFrancaise.java       # implements IStrategieMention
│   │   ├── MentionNordAmericaine.java  # implements IStrategieMention
│   │   └── GestionnaireEtudiants.java
│   └── util/
│       ├── IAfficheur.java             # interface
│       └── AfficheurConsole.java       # implements IAfficheur
├── data/
│   ├── etudiants.csv                   # Jeu de données d'entrée
│   └── resultats.csv                   # Fichier de sortie (régénéré)
├── Aurelien/  Moise/                   # Dossiers personnels (README individuels)
├── README.md                           # Ce fichier
├── contributions.txt                   # Répartition détaillée du travail
└── .gitignore
```

## Format du fichier d'entrée

Fichier CSV à 5 colonnes séparées par des points-virgules.
La première ligne doit être l'en-tête (elle est ignorée).

```
matricule;prenom;nom;cours;note
E001;Alice;Tremblay;Mathematiques;15.5
E001;Alice;Tremblay;Informatique;17.0
```

Un même étudiant apparaît sur autant de lignes que de cours ; les notes sont
regroupées automatiquement par matricule.

## Instructions d'exécution

### Compilation

Depuis la racine du projet :

```bash
javac -d bin \
  src/exceptions/*.java \
  src/modele/*.java \
  src/service/*.java \
  src/io/*.java \
  src/util/*.java \
  src/Main.java
```

### Exécution

Avec les chemins par défaut (`data/etudiants.csv` → `data/resultats.csv`) :

```bash
java -cp bin Main
```

Avec des fichiers personnalisés :

```bash
java -cp bin Main chemin/vers/entree.csv chemin/vers/sortie.csv
```

### Codes de sortie

| Code | Signification                                    |
|-----:|--------------------------------------------------|
| 0    | Exécution réussie                                |
| 1    | Erreur de lecture (fichier introuvable, etc.)    |
| 2    | Erreur d'écriture du fichier de sortie           |
| 99   | Erreur inattendue (bug, problème non prévu)      |

## Exemple de sortie console

```
==================================
  Gestion de notes des etudiants
==================================
Fichier d'entree   : data/etudiants.csv
Fichier de sortie  : data/resultats.csv
Bareme de mention  : Bareme francais (mentions)

8 etudiant(s) charge(s) avec succes.

============================
  Classement des etudiants
============================
Rang   Matricule  Nom complet               Moyenne
-------------------------------------------------------
1      E003       Charlotte Roy             17.63
2      E007       Gabrielle Cote            16.75
3      E001       Alice Tremblay            15.75
...

=========================
  Statistiques globales
=========================
Nombre total d'etudiants : 8
Nombre d'admis (>=10)    : 8
Taux de reussite         : 100.0 %
Moyenne generale         : 13.94 / 20

==========================
  Detail - Charlotte Roy
==========================
Matricule : E003
Categorie : Etudiant
Notes :
   - Mathematiques: 18.00
   - Informatique: 19.00
   - Physique: 17.50
   - Francais: 16.00
Moyenne : 17.63 / 20
```

## Pour changer de barème de mention

Modifier **une seule ligne** dans `Main.java` :

```java
IStrategieMention strategie = new MentionFrancaise();
// ou
IStrategieMention strategie = new MentionNordAmericaine();
```

## Version

Version 3.0 — Cours de programmation, session 2026.
Évolution majeure par rapport à la V2 : introduction d'une classe abstraite,
hiérarchie d'héritage, interfaces définies par l'équipe, exceptions
personnalisées, respect des cinq principes SOLID.
