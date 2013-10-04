function formatarNumero(ObjForm,teclapres,tammax,decimais)
{


  var bksp = 8;
  var key_0 = 48;
  var key_9 = 57;

	if(document.all)  // Internet Explorer
	{
    	tecla = teclapres.keyCode; }
   	else if(document.getElementById) // Nestcape
    {
    	tecla = teclapres.which;
  	}

	var tamanhoObjeto	= ObjForm.value.length;

	if (tecla == bksp && tamanhoObjeto == tammax)
	{
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

		//Cálculo para casas decimais setadas por parametro
		if ( tecla == bksp || (tecla >= key_0 && tecla <= key_9) )
		{
			if (decimais > 0)
			{

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
			else if(decimais == 0)
			{
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
	else
	{
		if((tecla != 8) && (tecla != 9) && (tecla != 0) && (tecla != 13) && (tecla != 35) && (tecla != 36) && (tecla != 46))
		{
			teclapres.cancelBubble = true;
			return false;
		}
	}
}