package com.autotrack.webmanager.constants;

public interface URL {
	
	String ROLE_USER = "ROLE_USER";
	String ROLE_ADMIN = "ROLE_ADMIN";
	String ADMINSITRADOR = "Administrador";
	String USER_COM = "Usuário Comum";
	String PERF_INDEF = "Perfil Indefinido";
	
	
	/*URL da Rest API*/
	String VERSION = "/api";
	String EMBEDDED = VERSION + "/embedded";
	
	String SEM_NAVEGACAO = null;
	String LOGIN_PAGE = "/index.xhtml";
	
	String SECURED_PATH = "/secured";
	String USER_BASE = SECURED_PATH + "/user";
	String ADMIN_BASE = SECURED_PATH + "/admin";
	
	
	String USER_PAGINA_RASTREAMENTO = USER_BASE + "/veiculos/rastrearVeiculo.xhtml";
	String USER_PAGINA_PRINCIPAL = USER_PAGINA_RASTREAMENTO;
	String USER_FILTRO_VEICULOS = USER_BASE + "/veiculos/filtro.xhtml";
	String USER_CADASTRO_VEICULOS = USER_BASE + "/veiculos/cadastro.xhtml";
	
	String ADMIN_FILTRO_USUARIOS = ADMIN_BASE + "/usuarios/filtro.xhtml";
	String ADMIN_CADASTRO_USUARIOS = ADMIN_BASE + "/usuarios/cadastro.xhtml";
	String ADMIN_FILTRO_MODULOS = ADMIN_BASE + "/modulos/filtro.xhtml";
	String ADMIN_CADASTRO_MODULOS = ADMIN_BASE + "/modulos/cadastro.xhtml";
	String ADMIN_PAGINA_PRINCIPAL = ADMIN_FILTRO_USUARIOS;
	String ADMIN_FILTRO_DESIGNACOES = ADMIN_BASE + "/designacoes/filtro.xhtml";

	
	
			
}
