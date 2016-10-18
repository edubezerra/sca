/**
 * @author sio-iago
 * @description Some utility functions.
 */
var Utility = {
	confirm: function(redirectUrl) {
		if(window.confirm("Deseja realmente fazer isso?"))
			window.location = redirectUrl;
		else
			console.log("Ação cancelada.");
	}
};