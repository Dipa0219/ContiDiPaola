
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
    let rankingTableDiv= document.getElementById("rankingTableDiv")


    //Initialization of ranking elements
    let rankingStarted= document.getElementById("rankingStarted")
    let rankingTable = document.getElementById("rankingTable")
    let rankingTableBody = document.getElementById("rankingTableBody")

    //Initialization of student button
    let joinTournamentButton = document.getElementById("joinTournamentButton")

    //Initialization of educator buttons
    let addCollaboratorButton =document.getElementById("addCollaboratorButton")
    let createBattleButton = document.getElementById("createBattleButton")
    let closeTournamentButton = document.getElementById("closeTournamentButton")
    let addCollaboratorSubmit = document.getElementById("addCollaboratorSubmit")
    let collaboratorInput = document.getElementById("collaboratorInput")
    let createBattleSubmit = document.getElementById("createBattleSubmit")

    //Initialization of tournament information element
    let tournamentNameLabel = document.getElementById("tournamentNameLabel")
    let tournamentDescriptionLabel=document.getElementById("tournamentDescriptionLabel")
    let tournamentRegistrationDeadlineLabel = document.getElementById("tournamentRegistrationDeadlineLabel")
    let tournamentOwner = document.getElementById("tournamentOwner")

    //Adding the click listener
    closeTournamentButton.addEventListener("click", (e) => {
        closingTournament()
    })

    addCollaboratorSubmit.addEventListener("click", (e) => {
        //Create Tournament Form reference
        var form = e.target.closest("form")
        if (form.checkValidity()){
            makeCall("POST", 'AddCollaborator?TournamentId=' + tournamentId , form,
                function (x){
                    if (x.readyState === XMLHttpRequest.DONE){
                        //server return message
                        var message = x.responseText
                        switch (x.status){
                            case 200: //OK
                                clearForm("addCollaboratorForm");
                                self.showAddCollaborator();
                                closeModal();
                                break;
                            case 400: //BAD REQUEST
                                document.getElementById("errormessageAddCollaborator").textContent = message;
                                break;
                            case 401: //UNAUTHORIZED
                                document.getElementById("errormessageAddCollaborator").textContent = message;
                                break;
                            case 409: //CONFLICT
                                document.getElementById("errormessageAddCollaborator").textContent = message;
                                break;
                            case 500: //INTERNAL SERVER ERROR
                                document.getElementById("errormessageAddCollaborator").textContent = message;
                                break;
                        }
                    }
                })
        }
        else {
            form.reportValidity()
        }
        clearForm("addCollaboratorForm")
    })

    joinTournamentButton.addEventListener("click", (e)=>{
        makeCall("POST","JoinTournament?TournamentId="+ tournamentId, null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            joinTournamentButton.style.display="none"
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
    })

    createBattleSubmit.addEventListener("click", (e) => {
        //create Battle form reference
        var form = e.target.closest("form")

        if (form.checkValidity()){
            makeCall("POST", "CreateBattle?TournamentId=" + tournamentId, form,
                function (x){
                    if (x.readyState === XMLHttpRequest.DONE) {
                        var message = x.responseText;
                        switch (x.status) {
                            case 200:
                                clearForm("createBattleForm")
                                closeModal()
                                battleTableType.style.display="none";
                                self.openPage(tournamentId)
                                break;
                            case 400: //BAD REQUEST
                                document.getElementById("errormessageCreateBattle").textContent = message;
                                break;
                            case 401: //UNAUTHORIZED
                                document.getElementById("errormessageCreateBattle").textContent = message;
                                break;
                            case 406: //NOT ACCEPTABLE
                                document.getElementById("errormessageCreateBattle").textContent = message;
                                break;
                            case 409: //CONFLICT
                                document.getElementById("errormessageCreateBattle").textContent = message;
                                break;
                            case 500: //INTERNAL SERVER ERROR
                                document.getElementById("errormessageCreateBattle").textContent = message;
                                break;
                        }
                    }
                })
        }
        else {
            form.reportValidity()
        }
        clearForm("createBattleSubmit")
    })

    //Function that perform the closure of a tournament
    function closingTournament(){
        //Post in CloseTournament servlet
        makeCall("POST", 'CloseTournament?TournamentID='+tournamentId, null,
            function (x) {
                if (x.readyState === XMLHttpRequest.DONE) {
                    //server return message
                    var message = x.responseText;
                    let closeModalMessage= document.getElementById("closeTournamentMessage")
                    switch (x.status){
                        case 200: //OK
                            openModal("closeTournament")
                            closeModalMessage.innerHTML="The tournament is closed";

                            createBattleButton.style.display = "none"
                            addCollaboratorButton.style.display = "none"
                            closeTournamentButton.style.display = "none"
                            break;
                        case 400: //BAD REQUEST
                            openModal("closeTournament")
                            closeModalMessage.innerHTML = message
                            break;
                        case 401: //UNAUTHORIZED
                            openModal("closeTournament")
                            closeModalMessage.innerHTML = message
                            break;
                        case 406: //NOT ACCEPTABLE
                            openModal("closeTournament")
                            closeModalMessage.innerHTML = message
                            break;
                        case 409: //CONFLICT
                            openModal("closeTournament")
                            closeModalMessage.innerHTML = message
                            break;
                        case 500: //INTERNAL SERVER ERROR
                            openModal("closeTournament")
                            closeModalMessage.innerHTML = message
                            break;
                    }
                }
            })
    }

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
        rankingTableDiv.style.display=""
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
                        case 401: //UNAUTHORIZED
                            closeTournamentButton.style.display = "none"
                            createBattleButton.style.display = "none"
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
                            if (message.length===0){
                                battleTableDiv.style.display="none"
                                battleTableType.style.display=""
                                battleTableType.innerHTML="It hasn't been created any battle for this tournament"
                            }
                            else {
                                self.updateBattleTable(message)
                            }
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
        makeCall("GET","CheckTournamentRanking?TournamentId="+ tournamentId, null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            message=JSON.parse(message)
                            if (message.length===0){
                                rankingTableDiv.style.display="none"
                            }
                            self.updateRankingTable(message)
                            break;
                        case 404:
                            message=JSON.parse(message)
                            if (message.NotStarted==="1"){
                                rankingStarted.style.display=""
                            }
                            else{
                                pageOrchestrator.showError(message.NotStarted);
                            }
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
        if (user.role===0) {
            //add collaborator option or hide the add collaborator button
            self.showAddCollaborator();
        }
        else if (user.role===1){
            //add collaborator option or hide the add collaborator button
            makeCall("GET", 'ShowJoinTournament?TournamentId=' + tournamentId, null,
                function (x) {
                    if (x.readyState === XMLHttpRequest.DONE) {
                        var message = x.responseText;
                        switch (x.status) {
                            case 200:
                                message= JSON.parse(message)
                                if (message){
                                    joinTournamentButton.style.display="none"
                                }
                                break;
                            default:
                                pageOrchestrator.showError(message);
                                break;
                        }
                    }
                })
        }
    };

    this.showAddCollaborator = function (){
        makeCall("GET", 'ShowAddCollaborator?TournamentId=' + tournamentId, null,
            function (x) {
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            message = JSON.parse(message)
                            if (message.length === 0) {
                                addCollaboratorButton.style.display = "none"
                            }
                            else {
                                collaboratorInput.innerHTML='';
                                self.updateCollaboratorList(message)
                            }
                            break;
                        case 401: //UNAUTHORIZED
                            addCollaboratorButton.style.display = "none"
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            })
    }

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

        self.hideTournamentButton(tournament)
    };

    //This function is used to hide the tournament button if requests
    this.hideTournamentButton= function (tournament) {
        if (tournament.phase === "Ongoing"){
            joinTournamentButton.style.display="none"
        }
        else if (tournament.phase === "Ended"){
            addCollaboratorButton.style.display = "none"
            joinTournamentButton.style.display="none"
            createBattleButton.style.display = "none"
            closeTournamentButton.style.display = "none"
        }
        else if (tournament.phase === "Not Started"){
            createBattleButton.style.display = "none"
            closeTournamentButton.style.display = "none"
        }
    }

    /*This function is used to update the battle table
    * It obtains in input the list of battle and add a new row in the
    * table for each tournament contained in the list*/
    this.updateBattleTable = function (battles){
        battleTableType.style.display="none"
        battleTableType.innerHTML=""
        battleTable.innerHTML=""
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

    this.updateCollaboratorList = function (collaborators){
        collaborators.forEach(function (collaborator){
            let option = document.createElement("option")
            option.text = collaborator.username
            option.value = collaborator.id
            collaboratorInput.add(option)
        })
    }

    this.updateRankingTable = function (rankings){
        rankingTable.style.display=""
        rankingTableBody.innerHTML=""
        rankingTable.innerHTML=""
        rankings.forEach(function (ranking){
            let tr= document.createElement("tr");
            let td= document.createElement("td");
            td.innerHTML=ranking.position;
            tr.appendChild(td);
            rankingTable.appendChild(tr);
            td= document.createElement("td");
            td.innerHTML= ranking.name;
            tr.appendChild(td);
            td= document.createElement("td");
            td.innerHTML= ranking.points;
            tr.appendChild(td);
            }
        )
    }

    /*This function is used to hide all the element contained in this page*/
    this.hide = function () {
        tournamentInfo.style.display="none"
        battleTableDiv.style.display="none"
        tournamentPageDiv.style.display="none"
        battleTableType.style.display="none"

        addCollaboratorButton.style.display="none"
        createBattleButton.style.display="none"
        closeTournamentButton.style.display="none"
        joinTournamentButton.style.display="none"

        rankingStarted.style.display="none"
        rankingTable.style.display="none"
        rankingTableDiv.style.display="none"
    };
}