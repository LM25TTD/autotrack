package com.autotrack.webmanager.util;





public abstract class ValidacaoUtil {  
	
	
	
	public static boolean validaEmail(String email){
			 
		String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	 
		return email.matches(EMAIL_PATTERN);
	}
	
	public static boolean validaTelefone(String telefone){
		String TELEPHONE_PATTERN = "\\([0-9]{2}?\\)[0-9]{4}?\\-[0-9]{4}?";
		return telefone.matches(TELEPHONE_PATTERN);
	}
	
	
	public static boolean rgValido(String rg){
		String aux=rg; 
		if (aux !=rg.replaceAll("[^0123456789-]", ""))
			return false;
		else
			return true;		
	}
	
	public static boolean verificarIPValido(String ip) {  
        if (ip == null) { return false; }  
        if (ip.trim().equals("")) { return false; }  
        if (ip.indexOf("-") >= 0) { return false; }  
        String[] strPartes = ip.replace('.', '-').split("-");  
        if (strPartes.length != 4) { return false; }  
        for (int i = 0; i < strPartes.length; i++) {  
            String strPedaco = strPartes[i];  
            if (strPedaco == null) { return false; }  
            if (strPedaco.trim().equals("")) { return false; }  
            try {  
                int intPedaco = Integer.parseInt(strPedaco);  
                if ((intPedaco == 0 && (i==0||i==4))) { return false; }
                if((intPedaco >= 254||intPedaco<0)){return false;}
            } catch (NumberFormatException e) {  
                return false;  
            }  
        }  
        return true;  
    }  
	
	
	
	
    private static String calcDigVerif(String num) {  
        Integer primDig, segDig;  
        int soma = 0, peso = 10;  
        for (int i = 0; i < num.length(); i++)  
                soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;  
  
        if (soma % 11 == 0 | soma % 11 == 1)  
            primDig = new Integer(0);  
        else  
            primDig = new Integer(11 - (soma % 11));  
  
        soma = 0;  
        peso = 11;  
        for (int i = 0; i < num.length(); i++)  
                soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;  
          
        soma += primDig.intValue() * 2;  
        if (soma % 11 == 0 | soma % 11 == 1)  
            segDig = new Integer(0);  
        else  
            segDig = new Integer(11 - (soma % 11));  
  
        return primDig.toString() + segDig.toString();  
    }  
  
    public static int calcSegDig(String cpf, int primDig) {  
        int soma = 0, peso = 11;  
        for (int i = 0; i < cpf.length(); i++)  
                soma += Integer.parseInt(cpf.substring(i, i + 1)) * peso--;  
          
        soma += primDig * 2;  
        if (soma % 11 == 0 | soma % 11 == 1)  
            return 0;  
        else  
            return 11 - (soma % 11);  
    }  
  
    public static String geraCPF() {  
        String iniciais = "";  
        Integer numero;  
        for (int i = 0; i < 9; i++) {  
            numero = new Integer((int) (Math.random() * 10));  
            iniciais += numero.toString();  
        }  
        return iniciais + calcDigVerif(iniciais);  
    }  
  
    public static boolean validaCPF(String cpf) {
    	cpf = retornaNumeros(cpf);
        if (cpf.length() != 11)  
            return false;  
  
        String numDig = cpf.substring(0, 9);  
        return calcDigVerif(numDig).equals(cpf.substring(9, 11));  
    }
    
    public static String retornaNumeros(String numero) {
		
		return numero.replaceAll("[^0123456789]", "");
		

	}
    
    public static boolean validaCNPJ(String cnpj) {  
       int soma = 0, dig;  
       String cnpj_calc = cnpj.substring(0,12);  
  
       if ( cnpj.length() != 14 )  
         return false;  
  
       char[] chr_cnpj = cnpj.toCharArray();  
  
       /* Primeira parte */  
       for( int i = 0; i < 4; i++ )  
         if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 )  
           soma += (chr_cnpj[i] - 48 ) * (6 - (i + 1)) ;  
       for( int i = 0; i < 8; i++ )  
         if ( chr_cnpj[i+4]-48 >=0 && chr_cnpj[i+4]-48 <=9 )  
           soma += (chr_cnpj[i+4] - 48 ) * (10 - (i + 1)) ;  
       dig = 11 - (soma % 11);  
  
       cnpj_calc += ( dig == 10 || dig == 11 ) ?  
                      "0" : Integer.toString(dig);  
  
       /* Segunda parte */  
       soma = 0;  
       for ( int i = 0; i < 5; i++ )  
         if ( chr_cnpj[i]-48 >=0 && chr_cnpj[i]-48 <=9 )  
           soma += (chr_cnpj[i] - 48 ) * (7 - (i + 1)) ;  
       for ( int i = 0; i < 8; i++ )  
         if ( chr_cnpj[i+5]-48 >=0 && chr_cnpj[i+5]-48 <=9 )  
           soma += (chr_cnpj[i+5] - 48 ) * (10 - (i + 1)) ;  
       dig = 11 - (soma % 11);  
       cnpj_calc += ( dig == 10 || dig == 11 ) ?  
                      "0" : Integer.toString(dig);  
  
       return cnpj.equals(cnpj_calc);  
    }
}
