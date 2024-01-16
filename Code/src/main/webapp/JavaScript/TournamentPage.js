

function TournamentPage(user) {
    //general
    let self=this

    //big component
    let tournamentInfo = document.getElementById("tournamentInfo")
    let battleTableDiv = document.getElementById("battleTableDiv")
    let tournamentPageDiv =document.getElementById("tournamentPageDiv")
    let battleTable = document.getElementById("battleTable")

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
    let tournamentOwner = document.getElementById("tournamentOwner")

    closeTournamentButton.addEventListener("click", (e) => {
        pageOrchestrator.showError("This function is not available for the moment")
    })

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
                            pageOrchestrator.showError(message);
                            break;
                        case 401: // unauthorized
                            pageOrchestrator.showError(message);
                            break;
                        case 500: // server error
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
        makeCall("GET","ShowBattles?TournamentId="+ tournamentId, null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = JSON.parse(x.responseText);
                    switch (x.status) {
                        case 200:
                            self.updateBattleTable(message)
                            break;
                        case 400: // bad request
                            pageOrchestrator.showError(message);
                            break;
                        case 401: // unauthorized
                            pageOrchestrator.showError(message);
                            break;
                        case 500: // server error
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
    };

    //updates all the div regarding the tournament information
    this.updateTournamentInfo= function (tournament){
        tournamentNameLabel.innerHTML="This is the "+ tournament.name + " tournament"
        tournamentOwner.innerHTML="Tournament created by " + tournament.creatorUsername
        if (tournament.description!=null) {
            tournamentDescriptionLabel.innerHTML = tournament.description
        }
        else{
            tournamentDescriptionLabel.innerHTML =""
        }
        tournamentRegistrationDeadlineLabel.innerHTML= "Registration Deadline:" + tournament.regDeadline
    };

    this.updateBattleTable = function (battles){
        battles.forEach(function (battle){
            let tr= document.createElement("tr");
            let td= document.createElement("td");
            let anchor = document.createElement("a");
            td.appendChild(anchor);
            let linkText = document.createTextNode(battle.id);
            anchor.appendChild(linkText);
            anchor.addEventListener("click", (e) => {
                self.hide()
                pageOrchestrator.showBattlePage(battle.id)
            }, false);
            anchor.href = "#";
            tr.appendChild(td);
            battleTable.appendChild(tr);
            td= document.createElement("td");
            td.innerHTML= battle.name;
            tr.appendChild(td);
        })
    }

    //hides everything referred to this page
    this.hide = function () {
        tournamentInfo.style.display="none"
        battleTableDiv.style.display="none"
        tournamentPageDiv.style.display="none"
        battleTable.innerHTML=""

        addCollaboratorButton.style.display="none"
        createBattleButton.style.display="none"
        closeTournamentButton.style.display="none"
        joinTournamentButton.style.display="none"
    };
}