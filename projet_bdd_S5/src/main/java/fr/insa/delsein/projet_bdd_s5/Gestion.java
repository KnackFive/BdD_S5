/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.delsein.projet_bdd_s5;

import fr.insa.delsein.utils.ConsoleFdB;
import fr.insa.delsein.utils.exceptions.ExceptionsUtils;
import fr.insa.delsein.utils.list.ListUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author ndelsein01
 */
public class Gestion {
    
    private Connection conn;

    public Gestion(Connection conn) {
        this.conn = conn;
    }
    
    public static Connection connectGeneralMySQL(String host,int port,String database,String user,String pass) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database,user,pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }
    
    public static Connection connectSurServeurM3() throws SQLException {
        return connectGeneralMySQL("92.222.25.165",3306,"m3_ndelsein01","m3_ndelsein01","8b2fa6a4");
    }
    
    public void creeSchema() throws SQLException {
        this.conn.setAutoCommit(false);
        try (Statement st = this.conn.createStatement()) {
            st.executeUpdate(
                    "create table p_machine (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    ref varchar(30) not null,\n"
                    + "    des varchar(30) not null,\n"
                    + "    puissance double not null \n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table p_typeoperation (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    des varchar(30) not null \n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table p_operation (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    idtype integer not null, \n"
                    + "    idproduit integer not null \n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table p_realise (\n"                //Classe realise à créer : comment gérer les fk en java ?
                    + "    idmachine integer not null,\n"       //Réponse : on ne céer que les classes "simples", d'objet matériel
                    + "    idtype integer not null,\n"          //Pour les liens fk : mettre uniquement des attributs "int" au lieu de l'objet en question 
                    + "    duree integer not null \n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table p_produit (\n"                
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    ref varchar(30) not null,\n"
                    + "    des varchar(30) not null \n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table p_precede (\n"
                    + "    opavant integer not null,\n"
                    + "    opapres integer not null \n"
                    + ")\n"
            );
            st.executeUpdate(
                    "create table p_exemplaire (\n"
                    + "    id integer not null primary key AUTO_INCREMENT,\n"
                    + "    numserie integer not null,\n"
                    + "    idproduit integer not null \n"
                    + ")\n"
            );
            st.executeUpdate(
                    "alter table p_realise \n"
                    + "    add constraint fk_p_realise_idmachine \n"
                    + "    foreign key (idmachine) references p_machine(id) \n"
            );
            st.executeUpdate(
                    "alter table p_realise \n"
                    + "    add constraint fk_p_realise_idtype \n"
                    + "    foreign key (idtype) references p_typeoperation(id) \n"
            );
            st.executeUpdate(
                    "alter table p_operation \n"
                    + "    add constraint fk_p_operation_idtype \n"
                    + "    foreign key (idtype) references p_typeoperation(id) \n"
            );
            st.executeUpdate(
                    "alter table p_operation \n"
                    + "    add constraint fk_p_operation_idproduit \n"
                    + "    foreign key (idproduit) references p_produit(id) \n"
            );
            st.executeUpdate(
                    "alter table p_exemplaire \n"
                    + "    add constraint fk_p_exemplaire_idproduit \n"
                    + "    foreign key (idproduit) references p_produit(id) \n"
            );
            this.conn.commit();
        } catch (SQLException ex) {
            this.conn.rollback();
            throw ex;
        } finally {
            this.conn.setAutoCommit(true);
        }
    }
    
    public void deleteSchema() throws SQLException {
        try (Statement st = this.conn.createStatement()) {
            try {
                st.executeUpdate("alter table p_realise drop constraint fk_p_realise_idmachine");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table p_realise drop constraint fk_p_realise_idtype");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table p_operation drop constraint fk_p_operation_idtype");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table p_operation drop constraint fk_p_operation_idproduit");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("alter table p_exemplaire drop constraint fk_p_exemplaire_idproduit");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate("drop table p_machine");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table p_typeoperation");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table p_operation");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table p_realise");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table p_produit");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table p_precede");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table p_exemplaire");
            } catch (SQLException ex) {
            }
        }
    }
    
    public void initTest() throws SQLException {
        Machine F01 = new Machine("F01", "rapide", 20);
        F01.saveInDBV1(this.conn);
        Machine F02 = new Machine("F02", "lente", 10);
        F02.saveInDBV1(this.conn);
        Typeoperation frais = new Typeoperation("fraisage");
        frais.saveInDBV1(conn);
        Typeoperation tour = new Typeoperation("tournage");
        tour.saveInDBV1(conn);
        Produit T23 = new Produit("T23","tel");
        T23.saveInDBV1(conn);
        Produit P12 = new Produit("P12","ordi");
        P12.saveInDBV1(conn);
        Produit T03 = new Produit("T03","table");
        T03.saveInDBV1(conn);
        Operation op1 = new Operation(1,1);
        op1.saveInDBV1(conn);
        Operation op2 = new Operation(2,1);
        op2.saveInDBV1(conn);
        Operation op3 = new Operation(2,3);
        op3.saveInDBV1(conn);
        Exemplaire ex1 = new Exemplaire(19102023,1);
        ex1.saveInDBV1(conn);
        Exemplaire ex2 = new Exemplaire(20231019,3);
        ex2.saveInDBV1(conn);
        try (Statement st = this.conn.createStatement()) {
            st.executeUpdate("insert into p_realise\n" +
                             "(idmachine,idtype,duree) values ('1','1','20')");
        } catch (SQLException ex) {
            System.out.println("Impossible d'ajouter test_realise");
            }
        try (Statement st = this.conn.createStatement()) {
            st.executeUpdate("insert into p_precede\n" +
                             "(opavant,opapres) values ('1','3')");
        } catch (SQLException ex) {
            System.out.println("Impossible d'ajouter test_precede");
            }
    }
    
    public void razBDD() throws SQLException {
        this.deleteSchema();
        this.creeSchema();
        this.initTest();
    }
    
    public void menuMachine() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu machine");
            System.out.println("================");
            System.out.println((i++) + ") lister les machines");
            System.out.println((i++) + ") ajouter une machine");
            System.out.println((i++) + ") chercher par pattern");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Machine> users = Machine.toutesLesMachines(this.conn);
                    System.out.println("Il y a " + users.size() + " machines : ");
                    System.out.println(ListUtils.enumerateList(users));
                } else if (rep == j++) {
                    System.out.println("entrez une nouvelle machine : ");
                    Machine nouveau = Machine.demande();
                    nouveau.saveInDBV1(this.conn);
                } 
                else if (rep == j++) {
                    this.afficheMachineAvecPattern();
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void menuTypeoperation() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu typeoperation");
            System.out.println("================");
            System.out.println((i++) + ") lister les types d'operation");
            System.out.println((i++) + ") ajouter un type d'operation");
            System.out.println((i++) + ") chercher par pattern");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Typeoperation> users = Typeoperation.tousLesTypeoperations(conn);
                    System.out.println("Il y a " + users.size() + " types d'operation : ");
                    System.out.println(ListUtils.enumerateList(users));
                } else if (rep == j++) {
                    System.out.println("entrez un nouveau type d'operation : ");
                    Typeoperation nouveau = Typeoperation.demande();
                    nouveau.saveInDBV1(this.conn);
                } 
                else if (rep == j++) {
                    this.afficheTypeoperationAvecPattern();
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void menuOperation() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu operation");
            System.out.println("================");
            System.out.println((i++) + ") lister les operations");
            System.out.println((i++) + ") ajouter une operation");
            System.out.println((i++) + ") chercher par pattern");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Operation> users = Operation.toutesLesOperations(this.conn);
                    System.out.println("Il y a " + users.size() + " operations : ");
                    System.out.println(ListUtils.enumerateList(users));
                } else if (rep == j++) {
                    System.out.println("entrez une nouvelle operation : ");
                    Operation nouveau = Operation.demande();
                    nouveau.saveInDBV1(this.conn);
                } 
                else if (rep == j++) {
                    this.afficheOperationAvecPattern();
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void menuProduit() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu produit");
            System.out.println("================");
            System.out.println((i++) + ") lister les produits");
            System.out.println((i++) + ") ajouter un produit");
            System.out.println((i++) + ") chercher par pattern");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Produit> users = Produit.tousLesProduits(this.conn);
                    System.out.println("Il y a " + users.size() + " produits : ");
                    System.out.println(ListUtils.enumerateList(users));
                } else if (rep == j++) {
                    System.out.println("entrez un nouveau produit : ");
                    Produit nouveau = Produit.demande();
                    nouveau.saveInDBV1(this.conn);
                } 
                else if (rep == j++) {
                    this.afficheProduitAvecPattern();
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void menuExemplaire() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu exemplaire");
            System.out.println("================");
            System.out.println((i++) + ") lister les exemplaires");
            System.out.println((i++) + ") ajouter un exemplaire");
            System.out.println((i++) + ") chercher par pattern");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Exemplaire> users = Exemplaire.tousLesExemplaires(this.conn);
                    System.out.println("Il y a " + users.size() + " exemplaires : ");
                    System.out.println(ListUtils.enumerateList(users));
                } else if (rep == j++) {
                    System.out.println("entrez un nouvel exemplaire : ");
                    Exemplaire nouveau = Exemplaire.demande();
                    nouveau.saveInDBV1(this.conn);
                } 
                else if (rep == j++) {
                    this.afficheExemplaireAvecPattern();
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void menuGestion(){
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println((i++) + ") gestion des machines");
            System.out.println((i++) + ") gestion des types d'operation");
            System.out.println((i++) + ") gestion des produits");
            System.out.println((i++) + ") gestion des operations");
            System.out.println((i++) + ") gestion des exemplaires");
            System.out.println((i++) + ") creer un lien entre une machine et un type d'operation");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            int j = 1;
            if (rep == j++) {
                this.menuMachine();
            } else if (rep == j++) {
                this.menuTypeoperation();
            } else if (rep == j++) {
                this.menuProduit();
            } else if (rep == j++) {
                this.menuOperation();
            } else if (rep == j++) {
                this.menuExemplaire();
            } else if (rep == j++) {
                this.lienMachineTypeoperation();
            }
        }
    }
    
    public void menuPrincipal() {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu principal");
            System.out.println("==============");
            System.out.println((i++) + ") supprimer schéma");
            System.out.println((i++) + ") créer schéma");
            System.out.println((i++) + ") RAZ BDD = supp + crée + init");
            System.out.println((i++) + ") menu gestion");
            System.out.println("0) Fin");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    this.deleteSchema();
                } else if (rep == j++) {
                    this.creeSchema();
                } else if (rep == j++) {
                    this.razBDD();
                } else if (rep == j++) {
                    this.menuGestion();
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }

    public void afficheMachineAvecPattern() throws SQLException {
        int rep = -1;
        while (rep!=0){
            int i = 1;
            System.out.println((i++) + ") recherche par id");
            System.out.println((i++) + ") recherche par reference");
            System.out.println((i++) + ") recherche par description");
            System.out.println((i++) + ") recherche par puissance");          
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de l'identificateur : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_machine where id like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String ref = r.getString("ref");
                            String des = r.getString("des");
                            double puissance = r.getDouble("puissance");
                            System.out.println(id + " : " + ref + ", " + des + ", " + puissance);
                        }   
                    }
                } else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de la reference : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_machine where ref like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String ref = r.getString("ref");
                            String des = r.getString("des");
                            double puissance = r.getDouble("puissance");
                            System.out.println(id + " : " + ref + ", " + des + ", " + puissance);
                        }  
                    }
                } 
                else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de la description : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_machine where des like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String ref = r.getString("ref");
                            String des = r.getString("des");
                            double puissance = r.getDouble("puissance");
                            System.out.println(id + " : " + ref + ", " + des + ", " + puissance);
                        }  
                    }
                }
                else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de la puissance : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_machine where puissance like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String ref = r.getString("ref");
                            String des = r.getString("des");
                            double puissance = r.getDouble("puissance");
                            System.out.println(id + " : " + ref + ", " + des + ", " + puissance);
                        }  
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void afficheTypeoperationAvecPattern() throws SQLException {
        int rep = -1;
        while (rep!=0){
            int i = 1;
            System.out.println((i++) + ") recherche par id");
            System.out.println((i++) + ") recherche par description");     
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de l'identificateur : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_typeoperation where id like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String des = r.getString("des");
                            System.out.println(id + " : " + des);
                        }   
                    }
                } else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de la description : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_typeoperation where des like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String des = r.getString("des");
                            System.out.println(id + " : " + des);
                        }  
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void afficheOperationAvecPattern() throws SQLException {
        int rep = -1;
        while (rep!=0){
            int i = 1;
            System.out.println((i++) + ") recherche par id");
            System.out.println((i++) + ") recherche par idtype");
            System.out.println((i++) + ") recherche par idproduit");         
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de l'identificateur : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_operation where id like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            int idtype = r.getInt("idtype");
                            int idproduit = r.getInt("idproduit");
                            System.out.println(id + " : type " + idtype + ", produit " + idproduit);
                        }   
                    }
                } else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de l'identificateur typeoperation : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_operation where idtype like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            int idtype = r.getInt("idtype");
                            int idproduit = r.getInt("idproduit");
                            System.out.println(id + " : type " + idtype + ", produit " + idproduit);
                        }  
                    }
                } 
                else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de l'identificateur produit : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_operation where idproduit like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            int idtype = r.getInt("idtype");
                            int idproduit = r.getInt("idproduit");
                            System.out.println(id + " : type " + idtype + ", produit " + idproduit);
                        }  
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void afficheProduitAvecPattern() throws SQLException {
        int rep = -1;
        while (rep!=0){
            int i = 1;
            System.out.println((i++) + ") recherche par id");
            System.out.println((i++) + ") recherche par reference");
            System.out.println((i++) + ") recherche par description");         
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de l'identificateur : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_produit where id like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String ref = r.getString("ref");
                            String des = r.getString("des");
                            System.out.println(id + " : " + ref + ", " + des);
                        }   
                    }
                } else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de la reference : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_produit where ref like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String ref = r.getString("ref");
                            String des = r.getString("des");
                            System.out.println(id + " : " + ref + ", " + des);
                        }  
                    }
                } 
                else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de la description : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_produit where des like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            String ref = r.getString("ref");
                            String des = r.getString("des");
                            System.out.println(id + " : " + ref + ", " + des);
                        }  
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void afficheExemplaireAvecPattern() throws SQLException {
        int rep = -1;
        while (rep!=0){
            int i = 1;
            System.out.println((i++) + ") recherche par id");
            System.out.println((i++) + ") recherche par numserie");
            System.out.println((i++) + ") recherche par idproduit");         
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de l'identificateur : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_exemplaire where id like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            int numserie = r.getInt("numserie");
                            int idproduit = r.getInt("idproduit");
                            System.out.println(id + " : " + numserie + ", " + idproduit);
                        }   
                    }
                } else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern du numero de serie : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_exemplaire where numserie like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            int numserie = r.getInt("numserie");
                            int idproduit = r.getInt("idproduit");
                            System.out.println(id + " : " + numserie + ", " + idproduit);
                        }  
                    }
                } 
                else if (rep == j++) {
                    String patNom = ConsoleFdB.entreeString("entrez le pattern de l'identificateur produit : ");
                    try (PreparedStatement st = this.conn.prepareStatement(
                        "select * from p_exemplaire where idproduit like ?"
                        )) {
                        st.setString(1, patNom);
                        ResultSet r = st.executeQuery();
                        while (r.next()) {
                            int id = r.getInt("id");
                            int numserie = r.getInt("numserie");
                            int idproduit = r.getInt("idproduit");
                            System.out.println(id + " : " + numserie + ", " + idproduit);
                        }  
                    }
                }
            } catch (SQLException ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.delsein", 5));
            }
        }
    }
    
    public void lienMachineTypeoperation(){
        int idmachine = ConsoleFdB.entreeInt("idmachine : ");
        int idtype = ConsoleFdB.entreeInt("idtype : ");
        int duree = ConsoleFdB.entreeInt("duree : ");
        try (PreparedStatement pst = conn.prepareStatement("insert into p_realise (idmachine,idtype,duree) values (?,?,?)")) {
            pst.setInt(1, idmachine);
            pst.setInt(2, idtype);
            pst.setInt(3, duree);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Impossible d'ajouter le lien, verifiez que idmachine et idtype existent déjà !");
            }
    }
    
    public static void debut() {
        try {
            Connection con = connectSurServeurM3();
            System.err.println("connecté");
            Gestion g = new Gestion(con);
            g.menuPrincipal();
        } catch (SQLException ex) {
            throw new Error("Connection impossible",ex);
        }
    }
    
    public static void main(String[] args) {
        debut();
        
    }
}
