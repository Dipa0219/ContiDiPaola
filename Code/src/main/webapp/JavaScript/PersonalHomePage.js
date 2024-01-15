function PersonalHomePage(user) {
    let homePageButton = document.getElementById("homePageDiv");

    let tournamentTableDiv = document.getElementById("tournamentTableDiv");
    let tournamentTable= document.getElementById("tournamentTable")
    var self = this;

    this.openPage = function () {
        console.log("Ciao")
        if (user.role !== 1) {
            homePageButton.style.display = "";
        }
        tournamentTableDiv.style.display = ""
        makeCall("GET","ShowTournament",null, function(x) {
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = JSON.parse(x.responseText);
                    switch (x.status) {
                        case 200:
                            self.updateTournamentTable(message)
                            break;
                        case 400: // bad request
                            document.getElementById("errormessage").textContent = message;
                            break;
                        case 401: // unauthorized
                            document.getElementById("errormessage").textContent = message;
                            break;
                        case 500: // server error
                            document.getElementById("errormessage").textContent = message;
                            break;
                    }
                }
            }
        )
    };

    this.updateTournamentTable = function (tournaments){
        tournaments.forEach(function (tournament){
            let tr= document.createElement("tr");
            let td= document.createElement("td");
            let anchor = document.createElement("a");
            td.appendChild(anchor);
            let linkText = document.createTextNode(tournament.id);
            anchor.appendChild(linkText);
            anchor.addEventListener("click", (e) => {
                self.hide()
                pageOrchestrator.showBattlePage(tournament.id)
            }, false);
            anchor.href = "#";
            tr.appendChild(td);
            tournamentTable.appendChild(tr);
            td= document.createElement("td");
            td.innerHTML= tournament.name;
            tr.appendChild(td);
            td= document.createElement("td");
            if(tournament.creatorUsername !== user.username){
                td.innerHTML= tournament.creatorUsername;
            }
            else
                td.innerHTML= "ME"
            tr.appendChild(td);
        })
    }

    this.hide = function (){
        homePageButton.style.display="none"
        tournamentTableDiv.style.display="none"
        tournamentTable.innerHTML=""
    }
}