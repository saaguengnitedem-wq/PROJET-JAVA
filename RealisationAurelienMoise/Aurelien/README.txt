=====================================================
Dossier personnel : AURELIEN
Role :  affichage, orchestration Main,
       patterns de conception
=====================================================

Fichiers dont je suis responsable :
  - src/service/GestionnaireEtudiants.java
  - src/service/IStrategieMention.java         (INTERFACE - Strategy pattern)
  - src/service/MentionFrancaise.java          (implementation)
  - src/service/MentionNordAmericaine.java     (implementation)
  - src/util/IAfficheur.java                   (INTERFACE)
  - src/util/AfficheurConsole.java             (implementation)
  - src/Main.java                              (injection de dependances)
  - README.md global (a la racine)
  - contributions.txt (a la racine)

Ce que j'ai fait :

1. Classe GestionnaireEtudiants (src/service/GestionnaireEtudiants.java)
   - Centralise toutes les operations sur la collection
     d'etudiants. Principe S de SOLID : Etudiant gere UN
     etudiant, GestionnaireEtudiants gere la COLLECTION.
   - classerParMoyenne() : utilise Collections.sort() qui
     tire parti de l'ordre naturel defini par
     Etudiant.compareTo() (cf. partie de Moise). Aucun
     Comparator externe n'est necessaire : la logique de
     tri "moyenne decroissante" appartient a Etudiant.
   - Copie defensive dans le constructeur : la liste interne
     est INDEPENDANTE de la liste recue en parametre.
   - getEtudiants() retourne Collections.unmodifiableList
     (protection contre les modifications externes).
   - calculerMoyenneGenerale(), compterAdmis(),
     rechercherParMatricule() pour les statistiques.

2. Interface IStrategieMention + 2 implementations
   (STRATEGY PATTERN)
   - Interface IStrategieMention : contrat abstrait
     "donnez-moi une moyenne, je vous rends une mention".
   - MentionFrancaise : bareme classique /20
     (Tres Bien, Bien, Assez Bien, Passable, Echec).
   - MentionNordAmericaine : conversion /20 -> /100 puis
     attribution d'une lettre (A, B, C, D, F).
   - Pour ajouter un troisieme bareme (suisse, belge, sur 10,
     etc.), il suffit de creer une nouvelle classe qui
     implements IStrategieMention, SANS modifier le code
     existant. C'est le principe O de SOLID (Open/Closed).

3. Interface IAfficheur + AfficheurConsole
   - IAfficheur : contrat pour tout afficheur (titre,
     classement, statistiques, detail d'un etudiant).
   - AfficheurConsole : implementation texte pour la console.
   - Permet d'imaginer facilement un AfficheurHTML,
     AfficheurGraphique, AfficheurSilencieux (pour les
     tests automatiques), sans toucher au reste du projet.
   - Dans afficherDetailEtudiant(), l'appel
     etudiant.getCategorie() est une demonstration de
     POLYMORPHISME : la methode est definie abstraite
     dans Personne (realisee par Moise) et implementee
     dans Etudiant.

4. Classe Main (src/Main.java) - INJECTION DE DEPENDANCES
   - Main ne connait que des INTERFACES :
       IAfficheur          afficheur  = new AfficheurConsole();
       ILecteurEtudiants   lecteur    = new LecteurCSV();
       IStrategieMention   strategie  = new MentionFrancaise();
       IEcrivainResultats  ecrivain   = new EcrivainResultats(strategie);
   - Les classes concretes ne sont instanciees qu'a un
     SEUL endroit. Pour changer de format ou de bareme, il
     suffit de modifier la ligne correspondante.
   - Gestion globale des exceptions avec try/catch :
       * catch (LectureException)  -> System.exit(1)
       * catch (EcritureException) -> System.exit(2)
       * catch (Exception)         -> System.exit(99) (filet
         de securite pour tout imprevu)
   - Les codes de sortie permettent d'utiliser le programme
     dans un script shell qui sait distinguer "echec de
     lecture" vs "echec d'ecriture" vs "bug inattendu".
   - Support des arguments en ligne de commande :
       args[0] = fichier d'entree
       args[1] = fichier de sortie
     avec des valeurs par defaut raisonnables.
   - Affiche le nombre de lignes rejetees quand il est
     non nul (feedback explicite a l'utilisateur).

5. Documentation globale
   - README.md : architecture complete, concepts Java
     utilises, instructions de compilation/execution,
     exemple de sortie, tableau de correspondance
     SOLID -> classes, codes de sortie. CETTE FOIS-CI
     la documentation decrit fidelement le code livre.
   - contributions.txt : repartition detaillee du travail
     entre Moise et moi.

Points techniques importants :
  - Principes SOLID activement mis en pratique :
    * S : responsabilites bien separees entre packages.
    * O : Strategy Pattern sur les mentions.
    * L : AfficheurConsole substituable partout ou
        un IAfficheur est attendu.
    * I : 4 interfaces ciblees (ILecteurEtudiants,
        IEcrivainResultats, IStrategieMention, IAfficheur)
        plutot qu'une grosse interface fourre-tout.
    * D : Main depend uniquement d'abstractions.
  - Gestion d'erreurs production-ready : codes de sortie
    differencies, messages sur System.err, cause d'origine
    conservee.

Pour la presentation orale, je presenterai :
  - L'architecture generale et les 5 packages.
  - L'injection de dependances dans Main comme point
    central du respect des principes SOLID.
  - La demonstration "changer de bareme en 1 ligne" :
    on remplace new MentionFrancaise() par
    new MentionNordAmericaine() et toute la sortie passe
    aux lettres A/B/C/D/F sans autre modification.
  - La demonstration des differents codes de sortie
    selon le type d'erreur rencontre.
