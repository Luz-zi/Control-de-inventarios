var ClickDerecho = {
    'sensitivity': 350,
    'count': 0,
    'timer':false,
    'active':function () {
        this.count++;
        this.timer = setTimeout(
            this.endCountdown.bind(this),
            this.sensitivity
        );
    },
    'endCountdown': function () {
        this.count = 0;
        this.timer = false;
    }
};


window.addEventListener("contextmenu", function(event){

    ClickDerecho.active();
    if(ClickDerecho.count == 1){
        event.preventDefault();
        var contextElement = document.getElementById("menu-derecho");
        contextElement.style.top = event.clientY + "px";
        contextElement.style.left = event.clientX + "px";
        contextElement.classList.add("active");
    }
});
window.addEventListener("click", function(){
    document.getElementById("menu-derecho").classList.remove("active");
});