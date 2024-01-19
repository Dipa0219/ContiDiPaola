
/*Function to organize the tournament page which is the one shown after you click on a particular tournament
* It contains all the information regarding the tournament chosen
* and the button to allow the educator to create a new battle, to add a new collaborator and also to close a tournament
* It also contains the button to allow a student to join the chosen tournament*/
function TournamentPage(user) {
    //General initialization
    let self=this
    let tournamentId

    //Initialization of all big element in the page
    let tournamentInfo = document.getElementById("tournamentInfo")
    let battleTableDiv = document.getElementById("battleTableDiv")
    let battleTableType = document.getElementById("battleTableType")
    let tournamentPageDiv =document.getElementById("tournamentPageDiv")
    let battleTable = document.getElementById("battleTable")

    //Initialization of student button
    let joinTournamentButton = document.getElementById("joinTournamentButton")

    //Initialization of educator buttons
    let addCollaboratorButton =document.getElementById("addCollaboratorButton")
    let createBattleButton = document.getElementById("createBattleButton")
    let closeTournamentButton = document.getElementById("closeTournamentButton")

    //Initialization of tournament information element
    let tournamentNameLabel = document.getElementById("tournamentNameLabel")
    let tournamentDescriptionLabel=document.getElementById("tournamentDescriptionLabel")
    let tournamentRegistrationDeadlineLabel = document.getElementById("tournamentRegistrationDeadlineLabel")
    let tournamentOwner = document.getElementById("tournamentOwner")


    //Function that perform the closure of a tournament TODO
    function closingTournament(){
        var requestData = {tournamentID: tournamentId}
        console.log(tournamentId)
        //Post in CloseTournament servlet
        makeCall("POST", 'CloseTournament?TournamentID='+tournamentId, null,
            function (x) {
                console.log("sono qui")
                if (x.readyState === XMLHttpRequest.DONE) {
                    //server return message
                    var message = x.responseText;
                    let p= document.createElement("p")
                    let closeModalMessage= document.getElementById("closeTournamentMessage")
                    switch (x.status){
                        case 200: //OK
                            openModal("closeTournament")
                            p.textContent = "The tournament is closed"
                            closeModalMessage.append(p);

                            // createBattleButton.style.display = "none"
                            // addCollaboratorButton.style.display = "none"
                            // closeTournamentButton.style.display = "none"
                            break;
                        case 400: //BAD REQUEST
                            openModal("closeTournament")
                            p.textContent = message
                            break;
                        case 401: //UNAUTHORIZED
                            openModal("closeTournament")
                            p.textContent = message
                            break;
                        case 406: //NOT ACCEPTABLE
                            openModal("closeTournament")
                            p.textContent = message
                            break;
                        case 409: //CONFLICT
                            openModal("closeTournament")
                            p.textContent = message
                            break;
                        case 500: //INTERNAL SERVER ERROR
                            openModal("closeTournament")
                            p.textContent = message
                            break;
                    }
                }
            })
    }

    //At the moment is only used to test the error page
    closeTournamentButton.addEventListener("click", (e) => {
        closingTournament()
    })


    /*This is the method used to open the tournament page
    * First it decides which button must be shown
    * Then with the makeCall function obtains all the tournament information and updates them
    * In the end he obtains all the tournament in the battle with the second makeCall and adds them to the table*/
    this.openPage = function (id) {
        if (id!=null) {
            tournamentId = id
        }
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
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            message=JSON.parse(message)
                            self.updateTournamentInfo(message)
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
        makeCall("GET","ShowBattles?TournamentId="+ tournamentId, null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            message=JSON.parse(message)
                            self.updateBattleTable(message)
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
    };

    //This function is used to update the tournament information
    //It obtains in input the tournament information and adds them to the corresponding http element
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

    /*This function is used to update the battle table
    * It obtains in input the list of battle and add a new row in the
    * table for each tournament contained in the list*/
    this.updateBattleTable = function (battles){
        battleTable.innerHTML=""
        let flag=0
        battles.forEach(function (battle){
            flag =1
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

        if(flag===0){
            battleTableDiv.style.display="none"
            battleTableType.style.display=""
            battleTableType.innerHTML="You haven't subscribed to any tournament"
        }
    }

    /*This function is used to hide all the element contained in this page*/
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