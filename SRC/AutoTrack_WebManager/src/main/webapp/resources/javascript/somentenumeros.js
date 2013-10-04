function somenteNumeros(e) {

if(document.all)  // Internet Explorer
	{
    	tecla = e.keyCode; }
   	else if(document.getElementById) // Nestcape/FireFox
    {
    	tecla = e.which;
  	}
//techa==8 é para permitir o backspace funcionar para apagar
if ( (tecla >= 48 && tecla <= 57) || tecla == 8 || tecla == 0 ) {

return true;
}
else {
return false;
}

}