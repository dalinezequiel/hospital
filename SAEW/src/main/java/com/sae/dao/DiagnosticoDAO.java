package com.sae.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sae.model.*;

public class DiagnosticoDAO {

	private static Connection con = null;
	private static PreparedStatement pst = null;
	private static ResultSet rs = null;

	private static ArrayList<DiagnosticoModel> listaDiag = null;
	private static DiagnosticoModel diagModel = null;

	// CADASTRO DE DIAGNÓSTICOS
	public static boolean cadastroDeDiagnostico(DiagnosticoModel diag) {
		try {
			String SQL_INSERT_QUERY = "INSERT INTO diagnostico(id_diagnostico, diagnostico, resposta, obs, ultima_actualizacao,  data_registo, id_paciente, paciente)values(?,?,?,?,?,?,?,?)";
			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_INSERT_QUERY);

			pst.setInt(1, diag.getIdDiagnostico());
			pst.setString(2, diag.getDiagnostico());
			pst.setString(3, diag.getResposta());
			pst.setString(4, diag.getObservacao());
			pst.setDate(5, diag.getDataUltimaActualizacao());
			pst.setDate(6, diag.getDataRegisto());
			pst.setInt(7, diag.getIdPaciente());
			pst.setString(8, diag.getPaciente());

			pst.execute();
			return true;
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return false;
	}

	// LISTAGEM DE TODOS DE DIAGNÓSTICOS
	public static ArrayList<DiagnosticoModel> listaDiagnostico() {
		listaDiag = new ArrayList<DiagnosticoModel>();
		try {
			String SQL_SELECT_QUERY = "SELECT * from diagnostico";
			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_SELECT_QUERY);
			rs = pst.executeQuery();

			while (rs.next()) {
				diagModel = new DiagnosticoModel();
				diagModel.setIdDiagnostico(rs.getInt("id_diagnostico"));
				diagModel.setDiagnostico(rs.getString("diagnostico"));
				diagModel.setResposta(rs.getString("resposta"));
				diagModel.setObservacao(rs.getString("obs"));
				diagModel.setDataUltimaActualizacao(rs.getDate("ultima_actualizacao"));
				diagModel.setDataRegisto(rs.getDate("data_registo"));
				diagModel.setIdPaciente(rs.getInt("id_paciente"));
				diagModel.setPaciente(rs.getString("paciente"));
				listaDiag.add(diagModel);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return listaDiag;
	}

	// LISTAGEM DE TODOS DE DIAGNÓSTICOS PELO CÓDIGO
	public static ArrayList<DiagnosticoModel> listaDiagnosticoById(int id_diagnostico) {
		listaDiag = new ArrayList<DiagnosticoModel>();
		try {
			String SQL_SELECT_QUERY = "SELECT * from diagnostico WHERE id_diagnostico = ?";
			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_SELECT_QUERY);
			pst.setInt(1, id_diagnostico);
			rs = pst.executeQuery();

			while (rs.next()) {
				diagModel = new DiagnosticoModel();
				diagModel.setIdDiagnostico(rs.getInt("id_diagnostico"));
				diagModel.setDiagnostico(rs.getString("diagnostico"));
				diagModel.setResposta(rs.getString("resposta"));
				diagModel.setObservacao(rs.getString("obs"));
				diagModel.setDataUltimaActualizacao(rs.getDate("ultima_actualizacao"));
				diagModel.setDataRegisto(rs.getDate("data_registo"));
				diagModel.setIdPaciente(rs.getInt("id_paciente"));
				diagModel.setPaciente(rs.getString("paciente"));
				listaDiag.add(diagModel);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return listaDiag;
	}

	// LISTAGEM DE TODOS DE DIAGNÓSTICOS USANDO VÁRIOS PARÂMETROS
	public static ArrayList<DiagnosticoModel> listaDiagnosticoByMultipleParameters(int id_diagnostico, String paciente,
			String queixa_principal) {
		listaDiag = new ArrayList<DiagnosticoModel>();
		try {
			String SQL_SELECT_QUERY = "SELECT d.id_diagnostico, d.diagnostico, d.resposta, \r\n"
					+ "d.id_paciente, d.paciente, d.ultima_actualizacao, d.data_registo \r\n"
					+ "from diagnostico as d\r\n" + "where  d.id_diagnostico = ? or d.paciente = ?\r\n"
					+ "or d.id_paciente = (\r\n"
					+ "	select paciente.id_paciente from paciente where queixa_principal = ?\r\n" + ");";
			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_SELECT_QUERY);
			pst.setInt(1, id_diagnostico);
			pst.setString(2, paciente);
			pst.setString(3, queixa_principal);
			rs = pst.executeQuery();

			while (rs.next()) {
				diagModel = new DiagnosticoModel();
				diagModel.setIdDiagnostico(rs.getInt("id_diagnostico"));
				diagModel.setDiagnostico(rs.getString("diagnostico"));
				diagModel.setResposta(rs.getString("resposta"));
				diagModel.setIdPaciente(rs.getInt("id_paciente"));
				diagModel.setPaciente(rs.getString("paciente"));
				diagModel.setDataUltimaActualizacao(rs.getDate("ultima_actualizacao"));
				diagModel.setDataRegisto(rs.getDate("data_registo"));
				listaDiag.add(diagModel);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return listaDiag;
	}

	public static boolean getExistenciaDoDiagnosticoById(int id_diagnostico, String paciente, String queixa_principal) {
		listaDiag = new ArrayList<DiagnosticoModel>();
		try {
			String SQL_SELECT_QUERY = "SELECT * from diagnostico \r\n" + "WHERE id_diagnostico = ? or paciente = ?\r\n"
					+ "or id_paciente = (\r\n"
					+ "	select paciente.id_paciente from paciente where queixa_principal = ?\r\n" + ")";
			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_SELECT_QUERY);
			pst.setInt(1, id_diagnostico);
			pst.setString(2, paciente);
			pst.setString(3, queixa_principal);
			rs = pst.executeQuery();

			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return false;
	}

	// RETORNAR O TOTAL DE TODOS DE DIAGNÓSTICOS
	public static ArrayList<DiagnosticoModel> getTotalDiagnosticoWithDistinct() {
		listaDiag = new ArrayList<DiagnosticoModel>();
		try {
			String SQL_SELECT_QUERY = "SELECT count(distinct(id_diagnostico)) as total from diagnostico;";
			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_SELECT_QUERY);
			rs = pst.executeQuery();

			while (rs.next()) {
				diagModel = new DiagnosticoModel();
				diagModel.setTotal(rs.getInt("total"));
				listaDiag.add(diagModel);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return listaDiag;
	}

	// RETORNAR O TOTAL DE TODOS DE DIAGNÓSTICOS USANDO VÁRIOS PARÂMETROS
	public static int getTotalDiagnosticoWithDistinct(int id_diagnostico, String paciente, String queixa_principal) {
		listaDiag = new ArrayList<DiagnosticoModel>();
		try {
			String SQL_SELECT_QUERY = "select count(distinct(id_diagnostico)) as total\r\n" + "from diagnostico\r\n"
					+ "where id_diagnostico = ? or paciente = ?\r\n" + "or  paciente = (\r\n"
					+ "	select paciente from paciente where queixa_principal = ?\r\n" + ");";

			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_SELECT_QUERY);
			pst.setInt(1, id_diagnostico);
			pst.setString(2, paciente);
			pst.setString(3, queixa_principal);
			rs = pst.executeQuery();

			while (rs.next()) {
				diagModel = new DiagnosticoModel();
				diagModel.setTotal(rs.getInt("total"));
				listaDiag.add(diagModel);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return listaDiag.get(0).getTotal();
	}

	// EXCLUÍ O DIAGNÓSTICO PELO ID DO DIAGNÓSTICO
	public static boolean deleteDiagnosticoById(int id_diagnostico) {
		try {
			String SQL_DELETE_QUERY = "DELETE FROM diagnostico WHERE id_diagnostico = ?";
			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_DELETE_QUERY);
			pst.setInt(1, id_diagnostico);
			pst.executeUpdate();
			pst.close();

			return true;
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return false;
	}

	public static boolean actualizaDiagnostico(DiagnosticoModel diag) {
		try {
			String SQL_UPDATE_QUERY = "UPDATE paciente SET diagnostico = ?, resposta = ?, obs = ?, ultima_actualizacao, data_registo = ? WHERE id_diagnostico = ?";
			con = ConexaoSQL.getConnection();
			pst = con.prepareStatement(SQL_UPDATE_QUERY);

			pst.setInt(1, diag.getIdDiagnostico());
			pst.setString(2, diag.getDiagnostico());
			pst.setString(3, diag.getResposta());
			pst.setString(4, diag.getObservacao());
			pst.setDate(5, diag.getDataUltimaActualizacao());
			pst.setDate(6, diag.getDataRegisto());
			pst.setInt(7, diag.getIdPaciente());
			pst.setString(8, diag.getPaciente());

			pst.executeUpdate();
			pst.close();
			con.close();

			return true;

		} catch (SQLException e) {
			System.out.println("Ocorreu um erro!\n" + e.getMessage());
		}
		return false;
	}

	public static void main(String[] args) {
		/*
		 * boolean rs = DiagnosticoDAO.getExistenciaDoDiagnosticoById(0, "",
		 * "Dores na garganta"); if(rs) {
		 * System.out.println(DiagnosticoDAO.listaDiagnosticoByMultipleParameters(0, "",
		 * "Dores na garganta").get(0).getPaciente()); }else { System.out.println(rs); }
		 */
		System.out.println(DiagnosticoDAO.getTotalDiagnosticoWithDistinct(0, "", ""));
	}
}
