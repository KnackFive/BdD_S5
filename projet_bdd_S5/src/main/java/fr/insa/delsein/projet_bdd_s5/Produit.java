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
 * @author ndelsein01
 */
public class Produit {
    
    private int id;
    private String ref;
    private String des;

    private Produit(int id, String ref, String des){
        this.id=id;
        this.des=des;
        this.ref=ref;
    }
    
    public Produit(String ref, String des){
        this(-1,ref,des);
    }
    
    @Override
    public String toString() {
        return "Produit{" + "id=" + getId() + ", ref=" + getRef() + ", des=" + getDes() + '}';
    }
    
    public static Produit demande() {
        String ref = ConsoleFdB.entreeString("ref : ");
        String des = ConsoleFdB.entreeString("des : ");
        return new Produit(ref, des);
    }

    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert into p_produit (ref,des) values (?,?)")) {
            pst.setString(1, this.ref);
            pst.setString(2, this.des);
            pst.executeUpdate();           
        }
    }
    
    public static List<Produit> tousLesProduits(Connection con) throws SQLException {
        List<Produit> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,ref,des from p_produit")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    res.add(new Produit(id, ref, des));
                }
            }
        }
        return res;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the ref
     */
    public String getRef() {
        return ref;
    }

    /**
     * @param ref the ref to set
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * @return the des
     */
    public String getDes() {
        return des;
    }

    /**
     * @param des the des to set
     */
    public void setDes(String des) {
        this.des = des;
    }
    
    
    
}
