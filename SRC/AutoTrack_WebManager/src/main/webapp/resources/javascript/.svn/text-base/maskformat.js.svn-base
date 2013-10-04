/** Mascara os componentes
Exemplo de Utilização: 
1) CPF - formatar(this, '###.###.###-##')
2) CNPJ - formatar(this, '##.###.###/####-##')
3) Data - formatar(this, '##/##/####')
4) CEP - formatar(this, '#####-###')
5) Telefone * - formatar(this, '####-####')
* Conforme a necessidade. Para o exemplo, considera-se 
que o telefone possui 8 dígitos. Se quiser adicionar o DDD, poderia
ser utilizado formatar(this, '(##) ####-####')
(92) 3673-8206
*/

function formatar(src, mask) 
{	
	var i = src.value.length;
	var saida = '#';
	var texto = mask.substring(i);
	
	if (texto.substring(0,1) != saida) 
	{
		src.value += texto.substring(0,1);
	}	
}

/*

function formatar(src, mask)
{
	var srcLastIndex = src.value.length - 1;
	
	if (src.value.charAt(srcLastIndex) == mask.charAt(srcLastIndex)) 
	{
		src.value += mask.charAt(srcLastIndex);
	}
}
*/

// JavaScript Document

/* Descrição.: formata um campo do formulário de
 * acordo com a máscara informada...
 * Parâmetros: - objForm (o Objeto Form)
 * - strField (string contendo o nome
 * do textbox)
 * - sMask (mascara que define o
 * formato que o dado será apresentado,
 * usando o algarismo "9" para
 * definir números e o símbolo "!" para
 * qualquer caracter...
 * - evtKeyPress (evento)
 * Uso.......: <input type="textbox"
 * name="xxx".....
 * onkeypress="return txtBoxFormat(document.rcfDownload, 'str_cep', '99999-999', event);">
 * Observação: As máscaras podem ser representadas como os exemplos abaixo:
 * CEP -> 99.999-999
 * CPF -> 999.999.999-99
 * CNPJ -> 99.999.999/9999-99
 * Data -> 99/99/9999
 * Tel Resid -> (99) 9999-9999
 * Tel Cel -> (99) 9999-9999
 * Processo -> 99.999999999/999-99
 * C/C -> 999999-!
 */
 
function formatarMascarado(objForm, strField, sMask, evtKeyPress) {

	var i, nCount, sValue, fldLen, mskLen,bolMask, sCod, nTecla;

	if(document.all) { // Internet Explorer
	    nTecla = evtKeyPress.keyCode;
	} else if(document.layers) { // Nestcape
	    nTecla = evtKeyPress.which;
	} else {
	    nTecla = evtKeyPress.which;
	    if (nTecla == 8) {
	        return true;
	    }
	}
	
	sValue = objForm[strField].value;
	// Limpa todos os caracteres de formatação que
	// já estiverem no campo.
	// toString().replace [transforma em sring e troca elementos por ""]
	sValue = sValue.toString().replace( ":", "" );
	sValue = sValue.toString().replace( "-", "" );
	sValue = sValue.toString().replace( "-", "" );
	sValue = sValue.toString().replace( ".", "" );
	sValue = sValue.toString().replace( ".", "" );
	sValue = sValue.toString().replace( "/", "" );
	sValue = sValue.toString().replace( "/", "" );
	sValue = sValue.toString().replace( "/", "" );
	sValue = sValue.toString().replace( "(", "" );
	sValue = sValue.toString().replace( "(", "" );
	sValue = sValue.toString().replace( ")", "" );
	sValue = sValue.toString().replace( ")", "" );
	sValue = sValue.toString().replace( " ", "" );
	sValue = sValue.toString().replace( " ", "" );
	fldLen = sValue.length;
	mskLen = sMask.length;
	
	i = 0;
	nCount = 0;
	sCod = "";
	mskLen = fldLen;
	
	while (i <= mskLen) {
		bolMask = ((sMask.charAt(i) == "-") || (sMask.charAt(i) == ":") || (sMask.charAt(i) == ".") || (sMask.charAt(i) == "/"));
		bolMask = bolMask || ((sMask.charAt(i) == "(") || (sMask.charAt(i) == ")") || (sMask.charAt(i) == " ") || (sMask.charAt(i) == "."));
		
		//Se for true utiliza elementos especiais aumenta a máscara
		if (bolMask) {
		    sCod += sMask.charAt(i);
		    mskLen++;
		//Caso false mostra o sValue(o q foi digitado)
		} else {
		    sCod += sValue.charAt(nCount);
		    nCount++;
		}
		i++;
	}
	
	objForm[strField].value = sCod;
	if (nTecla != 8) { // backspace
	    if (sMask.charAt(i-1) == "9") { // apenas números...
	    return ((nTecla > 47) && (nTecla < 58)); } // números de 0 a 9
	else { // qualquer caracter...
	    return true;
	}
	} else {
	    return true;
	}
}
