package com.autotrack.webmanager.constants;

public interface URL {
	
	String ROLE_USER = "ROLE_USER";
	String ROLE_ADMIN = "ROLE_ADMIN";
	
	
	/*URL da Rest API*/
	String VERSION = "/api";
	String EMBEDDED = VERSION + "/embedded";
	
	String SEM_NAVEGACAO = null;
	String LOGIN_PAGE = "/index.xhtml";
	
	String SECURED_PATH = "/secured";
	String USER_BASE = SECURED_PATH + "/user";
	String ADMIN_BASE = SECURED_PATH + "/admin";
	
	String USER_PAGINA_PRINCIPAL = USER_BASE + "/ativarModulos/filtro.xhtml";
		
	String ADMIN_PAGINA_PRINCIPAL = ADMIN_BASE + "/principal.xhtml";
	
			
}
