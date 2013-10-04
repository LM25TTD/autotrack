package com.autotrack.webmanager.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller("Utils")
@Scope("session")
public class Utils implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1203660690180294605L;
	
	public static String convResposta (Boolean verdadeiro){
		if (verdadeiro)
			return "Sim";
		else
			return "Não";
	}


	public static <T1> List<Linha<T1>> mapearParaLinhas(List<T1> dados) {

		List<Linha<T1>> linhas = new ArrayList<Linha<T1>>();

		if (dados != null)
			for (T1 dado : dados)
				linhas.add(new Linha<T1>(false, dado));

		return linhas;
	}
	
	public static <T1> List<T1> mapearParaElementos(List<Linha<T1>> linhas) {

		List<T1> elementos = new ArrayList<T1>();

		if (linhas != null)
			for (Linha<T1> linha : linhas)
				elementos.add(linha.getElemento());

		return elementos;
	}

	public static <T1> List<T1> mapearSelecionadosParaElementos(List<Linha<T1>> linhas) {

		List<T1> elementos = new ArrayList<T1>();

		if (linhas != null)
			for (Linha<T1> linha : linhas)
				if (linha.isSelecionado())
					elementos.add(linha.getElemento());

		return elementos;
	}
	
	public static <T1> List<T1> mapearNaoSelecionadosParaElementos(List<Linha<T1>> linhas) {

		List<T1> elementos = new ArrayList<T1>();

		if (linhas != null)
			for (Linha<T1> linha : linhas)
				if (!linha.isSelecionado())
					elementos.add(linha.getElemento());

		return elementos;
	}

	/**
	 * Utilizado na funcao Criptografar
	 * 
	 * @param text
	 * @return
	 */
	private static char[] hexCodes(byte[] text) {

		char[] hexOutput = new char[text.length * 2];
		String hexString;

		for (int i = 0; i < text.length; i++) {
			hexString = "00" + Integer.toHexString(text[i]);
			hexString.toUpperCase().getChars(hexString.length() - 2, hexString.length(), hexOutput, i * 2);
		}
		return hexOutput;
	}

	/**
	 * Fun��o de Criptografia
	 * 
	 * @param pwd
	 *            Palavra a criptografar
	 * @return Palavra criptografada
	 * @throws NoSuchAlgorithmException
	 */
	public static String criptografar(String pwd)
			throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		if (md != null) {
			return new String(hexCodes(md.digest(pwd.getBytes())));
		}
		return null;
	}

	public static Boolean isNumber(String aNumber) {

		try {
			Double.parseDouble(aNumber.replace("-", "").replace(".", "").replace("/", ""));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	

	/**
	 * Retorna as mensagens do arquivo de mensagens de acordo com a palavra
	 * chave
	 */
	public static String getMessage(String nomeArquivo, String key) {

		try {
			return ResourceBundle.getBundle(nomeArquivo).getString(key);
		} catch (Exception e) {
			return key;
		}
	}

	public static String removePath(String nome) {

		int ixBarra = nome.lastIndexOf("/");

		if (ixBarra <= 0)
			ixBarra = nome.lastIndexOf("\\");

		return nome.substring(ixBarra + 1);
	}
	
	public static void downloadArquivo( String nome, byte[] conteudo ) throws IOException {

		FacesContext fctx = FacesContext.getCurrentInstance();

		HttpServletResponse resp = (HttpServletResponse) fctx.getExternalContext().getResponse();

		resp.setContentType( "application/octet-stream" );
		resp.setContentLength( conteudo.length );
		resp.setHeader( "Content-Disposition", "atachment;filename=" + nome );

		resp.getOutputStream().write( conteudo );
		resp.getOutputStream().flush();
		resp.getOutputStream().close();

		fctx.responseComplete();
	}
	
	public static void downloadHTML( String nome, byte[] conteudo ) throws IOException {

		FacesContext fctx = FacesContext.getCurrentInstance();

		HttpServletResponse resp = (HttpServletResponse) fctx.getExternalContext().getResponse();

		resp.setContentType( "text/html" );
		resp.setContentLength( conteudo.length );
		resp.setHeader( "Content-Disposition", "atachment;filename=" + nome );

		resp.getOutputStream().write( conteudo );
		resp.getOutputStream().flush();
		resp.getOutputStream().close();

		fctx.responseComplete();
	}

	public static byte[] getBytes(InputStream is) throws IOException {

	    int len;
	    int size = 1024;
	    byte[] buf;

	    if (is instanceof ByteArrayInputStream) {
	      size = is.available();
	      buf = new byte[size];
	      len = is.read(buf, 0, size);
	    } else {
	      ByteArrayOutputStream bos = new ByteArrayOutputStream();
	      buf = new byte[size];
	      while ((len = is.read(buf, 0, size)) != -1)
	        bos.write(buf, 0, len);
	      buf = bos.toByteArray();
	    }
	    return buf;
	  }
	
	public static int dataDiff(java.util.Date dataHigh, java.util.Date dataLow){  
		  
	     GregorianCalendar startTime = new GregorianCalendar();  
	     GregorianCalendar endTime = new GregorianCalendar();  
	       
	     GregorianCalendar curTime = new GregorianCalendar();  
	     GregorianCalendar baseTime = new GregorianCalendar();  
	  
	     startTime.setTime(dataLow);  
	     endTime.setTime(dataHigh);  
	       
	     int dif_multiplier = 1;  
	       
	     // Verifica a ordem de inicio das datas  
	     if( dataLow.compareTo( dataHigh ) < 0 ){  
	         baseTime.setTime(dataHigh);  
	         curTime.setTime(dataLow);  
	         dif_multiplier = 1;  
	     }else{  
	         baseTime.setTime(dataLow);  
	         curTime.setTime(dataHigh);  
	         dif_multiplier = -1;  
	     }  
	       
	     int result_years = 0;  
	     int result_months = 0;  
	     int result_days = 0;  
	  
	     // Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import acumulando  
	     // no total de dias. Ja leva em consideracao ano bissesto  
	     while( curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR) ||  
	            curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH)  )  
	     {  
	           
	         int max_day = curTime.getActualMaximum( GregorianCalendar.DAY_OF_MONTH );  
	         result_months += max_day;  
	         curTime.add(GregorianCalendar.MONTH, 1);  
	           
	     }  
	       
	     // Marca que � um saldo negativo ou positivo  
	     result_months = result_months*dif_multiplier;  
	       
	       
	     // Retirna a diferenca de dias do total dos meses  
	     result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));  
	       
	     return result_years+result_months+result_days;  
	}  
	
	
	
}
