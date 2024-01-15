function PersonalHomePage(user) {
    let homePageButton = document.getElementById("homePageDiv");
    let tournamentTableDiv = document.getElementById("tournamentTableDiv");
    var self = this;

    this.openPage = function () {
        console.log("Ciao")
        if (user.role !== 1) {
            homePageButton.style.display = "";
        }
        tournamentTableDiv.style.display = ""
        /*makeCall("GET","ShowTournament",null,function (){

        })*/
    };
}