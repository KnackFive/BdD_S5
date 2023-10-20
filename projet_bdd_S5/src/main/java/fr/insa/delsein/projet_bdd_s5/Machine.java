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
public class Machine {
    
    private int id;
    private String ref;
    private String des;
    private double puissance;
    
    private Machine(int id, String ref, String des, double puissance){
        this.id=id;
        this.des=des;
        this.ref=ref;
        this.puissance=puissance;
    }
    
    public Machine(String ref, String des, double puissance){
        this(-1,ref,des,puissance);
    }
    
    @Override
    public String toString() {
        return "Machine{" + "id=" + getId() + ", ref=" + getRef() + ", des=" + getDes() + ", puissance=" + getPuissance() + '}';
    }
    
    public static Machine demande() {
        String ref = ConsoleFdB.entreeString("ref : ");
        String des = ConsoleFdB.entreeString("des : ");
        double puissance = ConsoleFdB.entreeDouble("puissance : ");
        return new Machine(ref, des, puissance);
    }

    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert into p_machine (ref,des,puissance) values (?,?,?)")) {
            pst.setString(1, this.ref);
            pst.setString(2, this.des);
            pst.setDouble(3, this.puissance);
            pst.executeUpdate();
        }
    }
    
    public static List<Machine> toutesLesMachines(Connection con) throws SQLException {
        List<Machine> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,ref,des, puissance from p_machine")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ref = rs.getString("ref");
                    String des = rs.getString("des");
                    double puissance = rs.getDouble("puissance");
                    res.add(new Machine(id, ref, des, puissance));
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

    /**
     * @return the puissance
     */
    public double getPuissance() {
        return puissance;
    }

    /**
     * @param puissance the puissance to set
     */
    public void setPuissance(double puissance) {
        this.puissance = puissance;
    }
    
}
