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
public class Typeoperation {
    
    private int id;
    private String des;

    private Typeoperation(int id, String des){
        this.id=id;
        this.des=des;
    }
    
    public Typeoperation(String des){
        this(-1,des);
    }
    
    @Override
    public String toString() {
        return "Typeoperation{" + "id=" + getId() + ", des=" + getDes() + '}';
    }
    
    public static Typeoperation demande() {
        String des = ConsoleFdB.entreeString("des : ");
        return new Typeoperation(des);
    }
    
    public void saveInDBV1(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "insert into p_typeoperation (des) values (?)")) {
            pst.setString(1, this.des);
            pst.executeUpdate();
        }
    }
    
    public static List<Typeoperation> tousLesTypeoperations(Connection con) throws SQLException {
        List<Typeoperation> res = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement(
                "select id,des from p_typeoperation")) {
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String des = rs.getString("des");
                    res.add(new Typeoperation(id, des));
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
