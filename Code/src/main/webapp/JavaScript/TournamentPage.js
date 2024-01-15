

function TournamentPage(user) {
    //general
    let self=this

    //big component
    let tournamentInfo = document.getElementById("tournamentInfo")
    let battleTableDiv = document.getElementById("battleTableDiv")
    let tournamentPageDiv =document.getElementById("tournamentPageDiv")

    //student buttons
    let joinTournamentButton = document.getElementById("joinTournamentButton")
    //educator buttons
    let addCollaboratorButton =document.getElementById("addCollaboratorButton")
    let createBattleButton = document.getElementById("createBattleButton")
    let closeTournamentButton =document.getElementById("closeTournamentButton")

    //tournamentInfo
    let tournamentNameLabel = document.getElementById("tournamentNameLabel")
    let tournamentDescriptionLabel=document.getElementById("tournamentDescriptionLabel")
    let tournamentRegistrationDeadlineLabel = document.getElementById("tournamentRegistrationDeadlineLabel")

    //show the page for the tournament with the given tournament id
    this.openPage = function (tournamentId) {
        tournamentInfo.style.display=""
        battleTableDiv.style.display=""
        tournamentPageDiv.style.display=""
        if (user.role===0){
            addCollaboratorButton.style.display=""
            createBattleButton.style.display=""
            closeTournamentButton.style.display=""
        }
        else if (user.role===1) {
            joinTournamentButton.style.display = ""
        }
        makeCall("GET","ShowTournamentInfo?TournamentId="+ tournamentId, null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = JSON.parse(x.responseText);
                    switch (x.status) {
                        case 200:
                            self.updateTournamentInfo(message)
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

    //updates all the div regarding the tournament information
    this.updateTournamentInfo= function (tournament){
        tournamentNameLabel.innerHTML="This is the "+ tournament.name + " tournament"
        tournamentDescriptionLabel.innerHTML=tournament.description
        tournamentRegistrationDeadlineLabel.innerHTML= "Registration Deadline:" + tournament.regDeadline
    };

    //hides everything referred to this page
    this.hide = function () {
        tournamentInfo.style.display="none"
        battleTableDiv.style.display="none"
        tournamentPageDiv.style.display="none"

        addCollaboratorButton.style.display="none"
        createBattleButton.style.display="none"
        closeTournamentButton.style.display="none"
        joinTournamentButton.style.display="none"
    };
}