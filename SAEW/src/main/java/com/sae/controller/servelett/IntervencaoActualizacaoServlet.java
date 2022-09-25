package com.sae.controller.servelett;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import com.sae.dao.IntervencaoDAO;
import com.sae.model.*;

/**
 * Servlet implementation class IntervencaoActualizacaoServlet
 */
public class IntervencaoActualizacaoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IntervencaoModel inter = null;
	private ArrayList<String> lista = null;
	private ArrayList<ComboBoxModel> listaComboBox = null;
	private ComboBoxModel comboBoxModel = null;
	private AtributoModel atr = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public IntervencaoActualizacaoServlet() {
		super();
		inter = new IntervencaoModel();
		lista = new ArrayList<String>();
		listaComboBox = IntervencaoDAO.listaAcessoVenosoPeriferico();
		comboBoxModel = new ComboBoxModel();
		atr = new AtributoModel();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		atr.setAnyArrayString(request.getParameterValues("intervencao"));
		lista.clear();
		if (atr.getAnyArrayString() != null) {
			for (int i = 0; i < atr.getAnyArrayString().length; i++) {
				lista.add(atr.getAnyArrayString()[i]);
			}
		}

		if (request.getParameter("acesso_venoso_periferico") != null) {
			lista.add(request.getParameter("acesso_venoso_periferico"));
		}

		/**/
		/* realizar_curativo */
		if (request.getParameter("realizar_curativo") != null && !request.getParameter("realizar_curativo").isEmpty()) {
			lista.add(inter.getRealizarCurativo());
		}

		/* verificar_hgt */
		if (request.getParameter("verificar_hgt_veze") != null
				&& (!request.getParameter("verificar_hgt_veze").isEmpty())) {
			lista.add(inter.getVerificarHgt());
		}

		/* verificar_o2 */
		if (request.getParameter("verificar_o2_hacada") != null
				&& (!request.getParameter("verificar_o2_hacada").isEmpty())) {
			lista.add(inter.getVerificarSaturacaoOxigenio());
		}

		/* auxilar_banho_leito */
		if (request.getParameter("auxilar_banho_leito_hacada") != null
				&& (!request.getParameter("auxilar_banho_leito_hacada").isEmpty())) {
			lista.add(inter.getAuxiliarBanhoLeito());
		}

		/* aspiracao_orotraqueal */
		if (request.getParameter("aspiracao_orotraqueal_hacada") != null
				&& (!request.getParameter("aspiracao_orotraqueal_hacada").isEmpty())) {
			lista.add(inter.getAspiracaoOrotraqueal());
		}
		/**/

		if (lista != null) {
			tratamentoDoPossivelErro(this.dado_intervencao(inter, request, response), request, response);
		}
	}

	// DADDOS DA INTERVENÇÃO
	public boolean dado_intervencao(IntervencaoModel inter, HttpServletRequest request, HttpServletResponse response) {
		boolean rsp = false;
		comboBoxModel.setNomeComboBox("Realizar a troca de acesso venoso periférico de 72 em 72horas");
		IntervencaoDAO.deleteIntervencaoById(Integer.parseInt(request.getParameter("cod_interv").trim()));
		for (int i = 0; i < lista.size(); i++) {

			inter.setIdIntervencao(Integer.parseInt(request.getParameter("cod_interv").trim()));

			if (lista.get(i).equals(listaComboBox.get(0).getComboBoxItem())) {
				inter.setIntervencao(comboBoxModel.getNomeComboBox());
				inter.setResposta(listaComboBox.get(0).getComboBoxItem());

			} else if (lista.get(i).equals(listaComboBox.get(1).getComboBoxItem())) {
				inter.setIntervencao(comboBoxModel.getNomeComboBox());
				inter.setResposta(listaComboBox.get(1).getComboBoxItem());

			} else if (lista.get(i).equals(inter.getRealizarCurativo())) {
				inter.setIntervencao(inter.getRealizarCurativo());
				inter.setResposta(request.getParameter("realizar_curativo"));

			} else if (lista.get(i).equals(inter.getVerificarHgt())) {
				inter.setIntervencao(inter.getVerificarHgt());
				inter.setResposta(
						request.getParameter("verificar_hgt_veze") + "X " + request.getParameter("verificar_hgt_data"));

			} else if (lista.get(i).equals(inter.getVerificarSaturacaoOxigenio())) {
				inter.setIntervencao(inter.getVerificarSaturacaoOxigenio());
				inter.setResposta(
						request.getParameter("verificar_o2_hacada") + "h " + request.getParameter("verificar_o2_data"));

			} else if (lista.get(i).equals(inter.getAuxiliarBanhoLeito())) {
				inter.setIntervencao(inter.getAuxiliarBanhoLeito());
				inter.setResposta(request.getParameter("auxilar_banho_leito_hacada") + "h "
						+ request.getParameter("auxilar_banho_leito_data"));

			} else if (lista.get(i).equals(inter.getAspiracaoOrotraqueal())) {
				inter.setIntervencao(inter.getAspiracaoOrotraqueal());
				inter.setResposta(request.getParameter("aspiracao_orotraqueal_hacada") + "h "
						+ request.getParameter("aspiracao_orotraqueal_data"));

			} else {
				inter.setIntervencao(lista.get(i));
				inter.setResposta("Sim");
			}

			inter.setObservacao(request.getParameter("obs"));
			inter.setDataUltimaActualizacao(Date.valueOf(LocalDate.now()));
			inter.setDataRegisto(Date.valueOf(LocalDate.now()));
			inter.setIdPaciente(Integer.parseInt(request.getParameter("cod_pacnt").trim()));
			inter.setPaciente(request.getParameter("paciente"));

			rsp = IntervencaoDAO.cadastroDeIntervencao(inter);
		}
		return rsp;
	}

	// TRATAMENTO DO ERRO AO ACTUALIZAR DIAGNÓSTICO DO PACIENTE
	protected void tratamentoDoPossivelErro(boolean rsp, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (rsp) {
			response.sendRedirect("jsp/intervencoes_consult.jsp");
		} else {
			response.sendRedirect("index.jsp");
		}
	}
}