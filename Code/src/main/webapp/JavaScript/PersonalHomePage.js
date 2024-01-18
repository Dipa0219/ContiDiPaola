/*Function to organize the personal home page which is the one shown once you made the login
* It contains all the tournament created by the educator/interesting for the student
* and the possibility for the educator of creating a new tournament*/
function PersonalHomePage(user) {
    //Element initialization
    //General usage
    var self = this;

    //Button div initialization
    let homePageButton = document.getElementById("homePageDiv");

    //Tournament table initialization
    let tournamentTableType =document.getElementById("tournamentTableType")
    let tournamentTableDiv = document.getElementById("tournamentTableDiv");
    let tournamentTable= document.getElementById("tournamentTable")

    /*This is the method used to open the personal home page
    * First it decides if the create tournament button must be shown
    * Then with the makeCall function obtains all the necessary tournament
    * and then updates the tournament table*/
    this.openPage = function () {
        console.log("Ciao")
        if (user.role !== 1) {
            homePageButton.style.display = "";
        }
        tournamentTableDiv.style.display = ""
        makeCall("GET","ShowTournament",null, function(x) {
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            message=JSON.parse(message)
                            self.updateTournamentTable(message)
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
    };

    /*This function is used to update the tournament table
    * It obtains in input the list of tournaments and add a new row in the
    * table for each tournament contained in the list*/
    this.updateTournamentTable = function (tournaments){
        let flag=0
        tournamentTable.innerHTML=""
        tournaments.forEach(function (tournament) {
            flag=1
            let tr = document.createElement("tr");
            let td = document.createElement("td");
            let anchor = document.createElement("a");
            td.appendChild(anchor);
            let linkText = document.createTextNode(tournament.id);
            anchor.appendChild(linkText);
            anchor.addEventListener("click", (e) => {
                self.hide()
                pageOrchestrator.showTournamentPage(tournament.id)
            }, false);
            anchor.href = "#";
            tr.appendChild(td);
            tournamentTable.appendChild(tr);
            td = document.createElement("td");
            td.innerHTML = tournament.name;
            tr.appendChild(td);
            td = document.createElement("td");
            if (tournament.creatorUsername !== user.username) {
                td.innerHTML = tournament.creatorUsername;
            } else
                td.innerHTML = "ME"
            tr.appendChild(td);
        })
        if(flag===0){
            tournamentTableDiv.style.display="none"
            tournamentTableType.style.display=""
            tournamentTableType.innerHTML="You haven't subscribed to any tournament"
        }
    }

    /*This function is used to hide all the element contained in this page*/
    this.hide = function (){
        homePageButton.style.display="none"
        tournamentTableDiv.style.display="none"
        tournamentTableType.innerHTML=""
    }
}