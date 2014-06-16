var ScreenBrightness = function() {
    
}

ScreenBrightness.prototype.SetScreenBrightness = function(phnum) {
   //alert("values: "+phnum);
    var cordovaRef = window.PhoneGap || window.Cordova || window.cordova;
   cordovaRef.exec(null, null, "ScreenBrightness", "SetScreenBrightness",[{"number":phnum}]);
   // console.log("values: "+phnum)
  
};

if(!window.plugins) {
    window.plugins = {};
}
if(!window.plugins.ScreenBrightness) {
    window.plugins.ScreenBrightness = new ScreenBrightness();
}

