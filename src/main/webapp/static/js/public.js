//Oauth2.0
function isMicroMessenger(){
	var result = false;
	var ua = window.navigator.userAgent;
	if(ua.indexOf("MicroMessenger") > -1)
		result = true;
	return result;
}