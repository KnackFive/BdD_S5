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
public class Operation {
    
    private int id;
    private int idtype;
    private int idproduit;
    
    private Operation(int id, int idtype, int idproduit){
        this.id=id;
        this.idtype=idtype;
        this.idproduit=idproduit;        
    }
    
    public Operation(int idtype, int idproduit){
        this(-1,idtype,idproduit);
    }
    
    @Override
    public String toString() {
        return "Operation{" + "id=" + getId() + ", idtype=" + getIdtype()+ ", idproduit=" + getIdproduit() + '}';
    }

    public static Operation demande() {
        int idtype = ConsoleFdB.entreeInt("idtype : ");
        int idproduit = ConsoleFdB.entreeInt("idproduit : ");
        return new Operation(idtype,idproduit);
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert into p_operation (idtype,idproduit) values (?,?)")) {
            pst.setInt(1, this.idtype);
            pst.setInt(2, this.idproduit);
            pst.executeUpdate();
        }
    }
    
    public static List<Operation> toutesLesOperations(Connection con) throws SQLException {
        List<Operation> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,idtype,idproduit from p_operation")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idtype = rs.getInt("idtype");
                    int idproduit = rs.getInt("idproduit");
                    res.add(new Operation(id, idtype, idproduit));
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
     * @return the idtype
     */
    public int getIdtype() {
        return idtype;
    }

    /**
     * @param idtype the idtype to set
     */
    public void setIdtype(int idtype) {
        this.idtype = idtype;
    }

    /**
     * @return the idproduit
     */
    public int getIdproduit() {
        return idproduit;
    }

    /**
     * @param idproduit the idproduit to set
     */
    public void setIdproduit(int idproduit) {
        this.idproduit = idproduit;
    }
    
    
}
