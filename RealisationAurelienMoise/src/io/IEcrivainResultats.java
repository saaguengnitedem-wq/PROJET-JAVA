package io;

import exceptions.EcritureException;
import modele.Etudiant;

import java.util.List;

public interface IEcrivainResultats {

    /**
     Sauvegarde la liste (supposée déjà triée) des étudiants dans la destination spécifiée.
     */
    void ecrire(List<Etudiant> classement, String destination)
            throws EcritureException;
}
