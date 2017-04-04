package fr.ua.iutlens.rdv.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hemery on 02/04/2017.
 */
@Entity
public class ChoixCandCren {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne()
    private Candidat candidat;
    @ManyToOne()
    private Creneau creneau;
    private int numCreneau;
    private Date dateReservation;
    private int ordrePassage;
}
