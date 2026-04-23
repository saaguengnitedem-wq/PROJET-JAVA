=====================================================
Dossier personnel : MOISE
Role : Modele de donnees, hierarchie d'exceptions,
       interfaces I/O et leurs implementations CSV,
       jeu de donnees de test
=====================================================

Fichiers dont je suis responsable :

  Modele :
    - src/modele/Personne.java                 (classe ABSTRAITE)
    - src/modele/Etudiant.java                 (extends Personne,
                                                implements Comparable)
    - src/modele/Note.java                     (classe IMMUABLE, final)

  Hierarchie d'exceptions :
    - src/exceptions/DonneeInvalideException.java  (exception mere)
    - src/exceptions/NoteInvalideException.java    (extends DonneeInvalideException)
    - src/exceptions/MatriculeInvalideException.java
    - src/exceptions/LectureException.java
    - src/exceptions/EcritureException.java

  Entrees / Sorties :
    - src/io/ILecteurEtudiants.java            (INTERFACE)
    - src/io/IEcrivainResultats.java           (INTERFACE)
    - src/io/LecteurCSV.java                   (implements ILecteurEtudiants)
    - src/io/EcrivainResultats.java            (implements IEcrivainResultats)

  Donnees :
    - data/etudiants.csv                       (jeu de donnees de test)

Ce que j'ai fait :

1. Classe abstraite Personne (src/modele/Personne.java)
   - Mot-cle "abstract" : empeche l'instanciation directe
     (on ne peut PAS ecrire "new Personne(...)").
   - Attributs "protected final" (matricule, prenom, nom) :
     accessibles aux sous-classes, immuables apres construction.
   - Constructeur PROTEGE qui valide ses arguments et leve
     une MatriculeInvalideException si le matricule est null
     ou vide (principe fail-fast).
   - Methode ABSTRAITE getCategorie() : chaque sous-classe
     DOIT la redefinir (polymorphisme).
   - Redefinition d'equals() et hashCode() bases sur le
     matricule (deux personnes avec le meme matricule sont
     considerees egales).

2. Classe Etudiant (src/modele/Etudiant.java)
   - HERITE de Personne : "class Etudiant extends Personne".
   - Le constructeur appelle super(...) pour reutiliser
     la validation de la classe mere.
   - Redefinit getCategorie() en retournant "Etudiant"
     (demonstration de polymorphisme).
   - IMPLEMENTE Comparable<Etudiant> : compareTo() trie
     par moyenne DECROISSANTE. Cela permet a Aurelien
     d'ecrire simplement Collections.sort(liste) dans le
     Gestionnaire, sans passer de Comparator externe.
   - getNotes() retourne Collections.unmodifiableList(notes) :
     l'appelant ne peut pas modifier la liste interne
     (encapsulation defensive).
   - Methodes metier : calculerMoyenne(), meilleureNote(),
     pireNote(), getNotesParCours().

3. Classe Note (src/modele/Note.java)
   - Classe declaree "final" : on ne peut pas en heriter.
   - Attributs "private final" : une fois initialises dans
     le constructeur, ils ne peuvent plus etre modifies.
   - Le constructeur valide la valeur (doit etre dans [0 ; 20])
     ET le nom du cours (non vide), et leve
     NoteInvalideException en cas de probleme.
   - Aucun setter : la classe est completement IMMUABLE.

4. Hierarchie d'exceptions personnalisees (src/exceptions/)
   - DonneeInvalideException (extends Exception) : classe
     mere permettant a l'appelant d'ecrire un seul catch
     pour rattraper toute erreur de donnees.
   - NoteInvalideException extends DonneeInvalideException
     - message automatique quand la valeur est hors bornes.
   - MatriculeInvalideException extends DonneeInvalideException
   - LectureException : erreur globale de lecture
     (fichier introuvable, E/S impossible). Conserve la
     cause d'origine via super(message, cause).
   - EcritureException : equivalent pour l'ecriture.

5. Interfaces I/O et implementations CSV
   (src/io/ILecteurEtudiants.java, IEcrivainResultats.java,
   LecteurCSV.java, EcrivainResultats.java)

   Interfaces :
   - ILecteurEtudiants : contrat "on me donne une source,
     je rends une liste d'etudiants". Le format (CSV, JSON,
     base, service web) n'est pas specifie ici. Principe D
     de SOLID (Dependency Inversion).
   - IEcrivainResultats : symetrique pour l'ecriture.

   Implementation LecteurCSV :
   - TRY-WITH-RESOURCES sur le Scanner : le fichier se
     ferme automatiquement, meme en cas d'exception.
   - Gestion des erreurs en DEUX NIVEAUX :
       * Erreur globale (fichier introuvable) -> LectureException
         qui conserve la cause FileNotFoundException. Le
         programme s'arrete.
       * Erreur locale (ligne incomplete, matricule vide,
         note hors bornes, valeur non numerique) -> la ligne
         est rejetee, un message d'avertissement sur
         System.err, le compteur s'incremente, et la lecture
         CONTINUE avec la ligne suivante.
   - Transformation des exceptions du JDK en exceptions
     metier :
       * NumberFormatException  -> NoteInvalideException
       * FileNotFoundException  -> LectureException
   - Regroupement des notes d'un meme matricule via
     LinkedHashMap (preserve l'ordre d'apparition).
   - Support du point ET de la virgule comme separateur
     decimal : "15.5" et "15,5" sont tous deux acceptes.

   Implementation EcrivainResultats :
   - Recoit une IStrategieMention par INJECTION DE DEPENDANCE
     (constructeur avec Objects.requireNonNull). Le bareme
     de mention (defini par Aurelien) est donc un parametre
     externe.
   - try-with-resources sur le FileWriter.
   - Cree automatiquement le dossier parent si necessaire
     (mkdirs).
   - Locale.ROOT dans String.format pour forcer le point
     comme separateur decimal (compatible Excel/LibreOffice
     partout dans le monde).
   - En cas d'IOException, leve une EcritureException
     enrichie d'un message clair et de la cause d'origine.

6. Jeu de donnees (data/etudiants.csv)
   - 8 etudiants, 4 cours chacun = 32 lignes de notes.
   - Cours : Mathematiques, Informatique, Physique, Francais.
   - Notes volontairement variees pour tester toutes les
     mentions du bareme (de Passable a Tres Bien).

Points techniques importants :
  - Principes SOLID respectes :
    * S (Single Responsibility) : chaque classe a un role unique.
    * O (Open/Closed) : pour ajouter un LecteurJSON, on cree
        une nouvelle classe qui implements ILecteurEtudiants,
        sans toucher au code existant.
    * L (Liskov) : Etudiant peut remplacer Personne partout.
    * I (Interface Segregation) : 2 interfaces I/O ciblees,
        plutot qu'une grosse interface fourre-tout.
  - Encapsulation stricte : attributs prives, pas d'exposition
    de l'etat interne, collections retournees en lecture seule.
  - Validation fail-fast : impossible de creer un objet
    incoherent. Les erreurs sont detectees a la source.
  - Toutes les exceptions bloquantes font remonter une
    exception metier avec un message comprehensible, tout
    en conservant la cause originale pour le debogage.

Pour la presentation orale, je presenterai :
  - Le role de la classe abstraite Personne et pourquoi on
    ne peut pas l'instancier directement.
  - La hierarchie d'heritage Etudiant extends Personne et
    la demonstration du polymorphisme via getCategorie().
  - L'immutabilite de Note et ses benefices.
  - La hierarchie d'exceptions personnalisees et le
    mecanisme de capture "specifique OU generique"
    qu'elle permet.
  - Le mecanisme de gestion d'erreurs en deux niveaux
    (local non bloquant vs global bloquant) sur un exemple
    concret : fichier avec lignes corrompues (matricule
    vide, note a 25, valeur "abc", ligne trop courte).
