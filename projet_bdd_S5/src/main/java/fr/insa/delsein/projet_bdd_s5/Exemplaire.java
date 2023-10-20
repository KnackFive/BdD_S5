/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.delsein.projet_bdd_s5;

import fr.insa.delsein.utils.ConsoleFdB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author natha
 */
public class Exemplaire {
    
    private int id;
    private int numserie;
    private int idproduit;

    private Exemplaire(int id, int numserie, int idproduit){
        this.id=id;
        this.numserie=numserie;
        this.idproduit=idproduit;
    }
    
    public Exemplaire(int numserie, int idproduit){
        this(-1, numserie, idproduit);
    }
    
    @Override
    public String toString() {
        return "Exemplaire{" + "id=" + getId() + ", numserie=" + getNumserie()+ ", idproduit=" + getIdproduit() + '}';
    }
    
    public static Exemplaire demande() {
        int numserie = ConsoleFdB.entreeInt("numserie : ");
        int idproduit = ConsoleFdB.entreeInt("idproduit : ");
        return new Exemplaire(numserie, idproduit);
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert into p_exemplaire (numserie,idproduit) values (?,?)")) {
            pst.setInt(1, this.numserie);
            pst.setInt(2, this.idproduit);
            pst.executeUpdate();
        }
    }
    
    public static List<Exemplaire> tousLesExemplaires(Connection con) throws SQLException {
        List<Exemplaire> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,numserie,idproduit from p_exemplaire")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int numserie = rs.getInt("numserie");
                    int idproduit = rs.getInt("idproduit");
                    res.add(new Exemplaire(id, numserie, idproduit));
                }
            }
        }
        return res;
    }
    
    public int getId() {
        return id;
    }

    public int getNumserie() {
        return numserie;
    }

    public void setNumserie(int numserie) {
        this.numserie = numserie;
    }

    public int getIdproduit() {
        return idproduit;
    }

    public void setIdproduit(int idproduit) {
        this.idproduit = idproduit;
    }
    
    
    
}
