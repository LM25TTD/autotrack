
function checkUnAll(form, chkTodos, str) {
  var sub = "";
  var i = 0;
  for (i = 0; i < form.length; i++){
  	if (form.elements[i].type=="checkbox"){
		sub= form.elements[i].id;
		if (sub.indexOf(str)>-1) //encontrou
		  	form.elements[i].checked = chkTodos.checked? true:false;
	} 
  }
}

function desmarcaCheckTodos(form,chk,strTodos) {
  var sub = "";
  var i = 0;
  if ((chk.type != "checkbox") || (chk.checked == false)){
	  for (i = 0; i < form.length; i++){
  		if (form.elements[i].type=="checkbox"){
			sub= form.elements[i].id;
			if (sub.indexOf(strTodos)>-1){ //encontrou
			  	form.elements[i].checked = false;
			  	break;
			}
		} 
	  }
  }
}