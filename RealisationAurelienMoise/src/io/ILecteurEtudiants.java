package io;

import exceptions.LectureException;
import modele.Etudiant;

import java.util.List;


public interface ILecteurEtudiants {

    /**
     * Lit la source et reconstruit une liste d'étudiants avec leurs notes.
     */
    List<Etudiant> lire(String source) throws LectureException;

    int getLignesRejetees();
}
