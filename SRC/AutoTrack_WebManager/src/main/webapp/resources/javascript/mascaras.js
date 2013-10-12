// Mascara CNPJ
function mascaraCNPJ(evento){  
     
   var campo, valor, i, tam, caracter, mascara;  
   var bksp = 8;
   var key_0 = 48;
   var key_9 = 57;
   
   mascara = "99.999.999/9999-99";
	
   if(document.all){  // Internet Explorer
	   tecla = evento.keyCode; 
   }else if(document.getElementById){ // Nestcape
		tecla = evento.which;
   }
   
   if (document.all) // Internet Explorer  
      campo = evento.srcElement;  
   else // Nestcape, Mozzila  
       campo= evento.target;  
   
   if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9)){
	   valor = campo.value;  
	   tam = valor.length;  
	     
	   for(i=0;i<mascara.length;i++){  
	      caracter = mascara.charAt(i);  
	      if(caracter!="9")
	         if(i<tam & caracter!=valor.charAt(i))  
	            campo.value = valor.substring(0,i) + caracter + valor.substring(i,tam);
	   }
   }else{	
		return false;
   }
}

function mascaraCPF(evento){  
    
   var campo, valor, i, tam, caracter, mascara;  
   var bksp = 8;
   var key_0 = 48;
   var key_9 = 57;
   
   mascara = "999.999.999-99";
	
   if(document.all){  // Internet Explorer
		tecla = evento.keyCode; 
   }else if(document.getElementById){ // Nestcape
  		tecla = evento.which;
   }
   
   if (document.all) // Internet Explorer  
	      campo = evento.srcElement;  
	   else // Nestcape, Mozzila  
	       campo= evento.target;
   
   if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9)){
		   valor = campo.value;  
		   tam = valor.length;  
		     
		   for(i=0;i<mascara.length;i++){  
		      caracter = mascara.charAt(i);  
		      if(caracter!="9")
		         if(i<tam & caracter!=valor.charAt(i))  
		            campo.value = valor.substring(0,i) + caracter + valor.substring(i,tam);  
		                 
		}
	}else{	
		return false;
	}
}

function mascaraData(evento){  
    
   var campo, valor, i, tam, caracter, mascara;  
   var bksp = 8;
   var key_0 = 48;
   var key_9 = 57;
   
   mascara = "99/99/9999";
	
   if(document.all){  // Internet Explorer
		tecla = evento.keyCode; 
   }else if(document.getElementById){ // Nestcape
  		tecla = evento.which;
   }
   
   if (document.all) // Internet Explorer  
	      campo = evento.srcElement;  
	   else // Nestcape, Mozzila  
	       campo= evento.target;
   
   if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9)){
		   valor = campo.value;  
		   tam = valor.length;  
		     
		   for(i=0;i<mascara.length;i++){  
		      caracter = mascara.charAt(i);  
		      if(caracter!="9")
		         if(i<tam & caracter!=valor.charAt(i))  
		            campo.value = valor.substring(0,i) + caracter + valor.substring(i,tam);  
		                 
		}
	}else{	
		return false;
	}
}

function mascaraDateTime(evento){  
    
	   var campo, valor, i, tam, caracter, mascara;  
	   var bksp = 8;
	   var key_0 = 48;
	   var key_9 = 57;
	   
	   mascara = "99/99/9999 99:99";
		
	   if(document.all){  // Internet Explorer
			tecla = evento.keyCode; 
	   }else if(document.getElementById){ // Nestcape
	  		tecla = evento.which;
	   }
	   
	   if (document.all) // Internet Explorer  
		      campo = evento.srcElement;  
		   else // Nestcape, Mozzila  
		       campo= evento.target;
	   
	   if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9)){
			   valor = campo.value;  
			   tam = valor.length;  
			     
			   for(i=0;i<mascara.length;i++){  
			      caracter = mascara.charAt(i);  
			      if(caracter!="9")
			         if(i<tam & caracter!=valor.charAt(i))  
			            campo.value = valor.substring(0,i) + caracter + valor.substring(i,tam);  
			                 
			}
		}else{	
			return false;
		}
	}

function mascaraTelefone(evento){  
    
   var campo, valor, i, tam, caracter, mascara;  
   var bksp = 8;
   var key_0 = 48;
   var key_9 = 57;
   
   mascara = "(99) 9999-9999";
	
   if(document.all){  // Internet Explorer
		tecla = evento.keyCode; 
   }else if(document.getElementById){ // Nestcape
  		tecla = evento.which;
   }
   
   if (document.all) // Internet Explorer  
	      campo = evento.srcElement;  
	   else // Nestcape, Mozzila  
	       campo= evento.target;
   
   if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9)){
		   valor = campo.value;  
		   tam = valor.length;  
		     
		   for(i=0;i<mascara.length;i++){  
		      caracter = mascara.charAt(i);  
		      if(caracter!="9")
		         if(i<tam & caracter!=valor.charAt(i))  
		            campo.value = valor.substring(0,i) + caracter + valor.substring(i,tam);  
		                 
		}
	}else{	
		return false;
	}
}

// Permite somente n�meros
function somenteNumeros(numero) {

	if(document.all){ // Internet Explorer
		tecla = numero.keyCode;
	}
	else if(document.getElementById){ // Nestcape/FireFox
		tecla = numero.which;
	}
		
	//tecla==8 � para permitir o backspace funcionar para apagar
	if ( (tecla >= 48 && tecla <= 57) || tecla == 8 || tecla == 0 ) {
		return true;
	}
	else {
		return false;
	}
}

//Readonly(n�o permite a edi��o pelo usu�rio)
function readonly(evento) {

	if(document.all){ // Internet Explorer
		tecla = evento.keyCode;
	}
	else if(document.getElementById){ // Nestcape/FireFox
		tecla = evento.which;
	}
	
	if (false) {
		return true;
	}
	else {
		return false;
	}
}

// Formata valores monet�rios (R$ 00.000.000,00)
function formatarNumero(ObjForm,teclapres,tammax,decimais)
{
  var bksp = 8;
  var key_0 = 48;
  var key_9 = 57;

	if(document.all){  // Internet Explorer
    	tecla = teclapres.keyCode; 
	}
   	else if(document.getElementById){ // Nestcape
    	tecla = teclapres.which;
  	}

	var tamanhoObjeto	= ObjForm.value.length;

	if (tecla == bksp && tamanhoObjeto == tammax){
	  tamanhoObjeto = tamanhoObjeto - 1;
	}

	if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9 && (tamanhoObjeto+1) <= tammax))
	//if (( tecla == bksp || tecla == 88 || tecla >= 48 && tecla <= 57 || tecla >= 96 && tecla <= 105 ) && ((tamanhoObjeto+1) <= tammax))
	{
		vr	= ObjForm.value;
		vr  = vr.replace(eval("/\\./g"),"");
		vr	= vr.replace( ",", "" );
		tam	= vr.length;

		if (tam < tammax && tecla != bksp){
			tam = vr.length + 1 ;
		}

		if ((tecla == bksp) && (tam > 1)){
			tam = tam - 1 ;
			vr = ObjForm.value;

			vr  = vr.replace(eval("/\\./g"),"");
			vr	= vr.replace( ",", "" );
		}

		//C�lculo para casas decimais setadas por parametro
		if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9) )
		{
			if (decimais > 0){

				if ( (tam <= decimais) )
				{
					ObjForm.value = ("0," + vr) ;
				}
				if( (tam == (decimais + 1)) && (tecla == 8))
				{
					ObjForm.value = vr.substr( 0, (tam - decimais)) + ',' + vr.substr( tam - (decimais), tam ) ;
				}
				if ( (tam > (decimais + 1)) && (tam <= (decimais + 3)) &&  ((vr.substr(0,1)) == "0"))
				{
					ObjForm.value = vr.substr( 1, (tam - (decimais+1))) + ',' + vr.substr( tam - (decimais), tam ) ;
				}
				if ( (tam > (decimais + 1)) && (tam <= (decimais + 3)) &&  ((vr.substr(0,1)) != "0"))
				{
				    ObjForm.value = vr.substr( 0, tam - decimais ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
				if ( (tam >= (decimais + 4)) && (tam <= (decimais + 6)) )
				{
			 		ObjForm.value = vr.substr( 0, tam - (decimais + 3) ) + '.' + vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
			 	if ( (tam >= (decimais + 7)) && (tam <= (decimais + 9)) )
				{
			 		ObjForm.value = vr.substr( 0, tam - (decimais + 6) ) + '.' + vr.substr( tam - (decimais + 6), 3 ) + '.' + vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
				if ( (tam >= (decimais + 10)) && (tam <= (decimais + 12)) )
				{
			 		ObjForm.value = vr.substr( 0, tam - (decimais + 9) ) + '.' + vr.substr( tam - (decimais + 9), 3 ) + '.' + vr.substr( tam - (decimais + 6), 3 ) + '.' + vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
				if ( (tam >= (decimais + 13)) && (tam <= (decimais + 15)) )
				{
			 		ObjForm.value = vr.substr( 0, tam - (decimais + 12) ) + '.' + vr.substr( tam - (decimais + 12), 3 ) + '.' + vr.substr( tam - (decimais + 9), 3 ) + '.' + vr.substr( tam - (decimais + 6), 3 ) + '.' + vr.substr( tam - (decimais + 3), 3 ) + ',' + vr.substr( tam - decimais, tam ) ;
				}
			}
			else if(decimais == 0){
				if ( tam <= 3 )
				{
			 		ObjForm.value = vr ;
				}
				if ( (tam >= 4) && (tam <= 6) )
				{
					if(tecla == bksp)
					{
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}

					ObjForm.value = vr.substr(0, tam - 3) + '.' + vr.substr( tam - 3, 3 );
				}
				if ( (tam >= 7) && (tam <= 9) )
				{
					if(tecla == bksp)
					{
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr( 0, tam - 6 ) + '.' + vr.substr( tam - 6, 3 ) + '.' + vr.substr( tam - 3, 3 );
				}
				if ( (tam >= 10) && (tam <= 12) )
				{
			 		if(tecla == bksp)
					{
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr( 0, tam - 9 ) + '.' + vr.substr( tam - 9, 3 ) + '.' + vr.substr( tam - 6, 3 ) + '.' + vr.substr( tam - 3, 3 );
				}
				if ( (tam >= 13) && (tam <= 15) )
				{
					if(tecla == bksp)
					{
						ObjForm.value = vr.substr(0, tam);
						teclapres.cancelBubble = true;
						return false;
					}
					ObjForm.value = vr.substr( 0, tam - 12 ) + '.' + vr.substr( tam - 12, 3 ) + '.' + vr.substr( tam - 9, 3 ) + '.' + vr.substr( tam - 6, 3 ) + '.' + vr.substr( tam - 3, 3 ) ;
				}
			}
		}
	}
	else{	
		if((tecla != 8) && (tecla != 9) && (tecla != 0) && (tecla != 13) && (tecla != 35) && (tecla != 36) && (tecla != 46)){
			teclapres.cancelBubble = true;
			return false;
		}
	}
}