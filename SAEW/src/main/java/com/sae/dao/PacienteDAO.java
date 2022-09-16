package com.sae.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sae.model.*;

public class PacienteDAO {

    private static Connection con = null;
    private static PreparedStatement pst = null;
    private static ResultSet rs = null;
    
    private static ArrayList<PacienteModel> listaPat = null;
    private static PacienteModel patModel = null;

    public static boolean cadastroDePaciente(PacienteModel pac) {
        try {
            String SQL_INSERT = "INSERT INTO paciente(id_paciente, paciente, data_nascimento, leito, queixa_principal, data_internacao, data_registo)values(?,?,?,?,?,?,?)";
            con = ConexaoSQL.getConnection();
            pst = con.prepareStatement(SQL_INSERT);

            pst.setInt(1, pac.getIdPaciente());
            pst.setString(2, pac.getPaciente());
            pst.setDate(3, pac.getDataNascimento());
            pst.setInt(4, pac.getLeito());
            pst.setString(5, pac.getQueixaPrincipal());
            pst.setDate(6, pac.getDataInternacao());
            pst.setDate(7, pac.getDataRegisto());

            pst.execute();
            return true;
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return false;
    }
    
    public static ArrayList<PacienteModel> listaPaciente() {
    	listaPat = new ArrayList<PacienteModel>();
        try {
            String select = "SELECT * from paciente";
            con = ConexaoSQL.getConnection();
            pst = con.prepareStatement(select);
            rs = pst.executeQuery();

            while (rs.next()) {
            	patModel = new PacienteModel();
            	patModel.setIdPaciente(rs.getInt("id_paciente"));
            	patModel.setPaciente(rs.getString("paciente"));
            	patModel.setDataNascimento(rs.getDate("data_nascimento"));
            	patModel.setLeito(rs.getInt("leito"));
            	patModel.setQueixaPrincipal(rs.getString("queixa_principal"));
            	patModel.setDataInternacao(rs.getDate("data_internacao"));
            	patModel.setDataRegisto(rs.getDate("data_registo"));
            	listaPat.add(patModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPat;
    }


    public static void deletePacienteById(int codigo) {
        try {
            String deleteQuery = "DELETE FROM paciente WHERE Codigo = ?";
            con = ConexaoSQL.getConnection();
            pst = con.prepareStatement(deleteQuery);
            pst.setInt(1, codigo);
            pst.executeUpdate();

        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }

    public static void actualizaPaciente(PacienteModel pat) {
        try {
            String updateQuery = "UPDATE paciente SET paciente = ?, data_nascimento = ?, leito = ?, queixa_principal, data_internacao = ? WHERE codigo = ?";
            con = ConexaoSQL.getConnection();
            pst = con.prepareStatement(updateQuery);

            pst.setInt(1, pat.getIdPaciente());
            pst.setString(2, pat.getPaciente());
            pst.setDate(3, pat.getDataNascimento());
            pst.setInt(4, pat.getLeito());
            pst.setString(5, pat.getQueixaPrincipal());
            pst.setDate(6, pat.getDataInternacao());
            pst.setDate(7, pat.getDataUltimaActualizacao());

            pst.executeUpdate();
            pst.close();
            con.close();

        } catch (SQLException e) {
        	e.printStackTrace();
        }
    }
 
}