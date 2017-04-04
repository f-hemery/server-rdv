package fr.ua.iutlens.rdv.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hemery on 02/04/2017.
 */
@Entity
public class Creneau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String lieu;
    @ManyToOne
    @JoinColumn(nullable = false, name = "formation_id")
    private Formation Formation;
    private Date dateCreneau;
    private int nbCreneaux;
    private Date heureCreneau;
    private int intervalle;
    private boolean visible;
}
