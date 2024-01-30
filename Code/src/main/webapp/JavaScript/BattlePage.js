/*Function to organize the battle page which is the one shown after you click on a particular battle
* It contains all the information regarding the battle chosen
* and the button to allow the educator to modify the team evaluation.
* It also contains the button to allow a student to join the chosen battle alone or as a team.*/
function BattlePage(user){
    //general initialization
    let self= this
    let battleId

    let battlePageDiv= document.getElementById("battlePageDiv")
    let battleInfo= document.getElementById("battleInfo")

    let joinBattleAloneButton = document.getElementById("joinBattleAloneButton")
    let joinBattleAsTeamButton = document.getElementById("joinBattleAsTeamButton")
    let selectTeamButton = document.getElementById("selectTeamButton")
    let modifyGradeButton = document.getElementById("modifyGradeButton")
    let rankingTableDiv= document.getElementById("rankingTableDiv")

    //Initialization of ranking elements
    let rankingStarted= document.getElementById("rankingStarted")
    let rankingTable = document.getElementById("rankingTable")
    let rankingTableBody = document.getElementById("rankingTableBody")

    let battleNameLabel= document.getElementById("battleNameLabel")
    let battleDescriptionLabel = document.getElementById("battleDescriptionLabel")
    let battleRegistrationDeadlineLabel = document.getElementById("battleRegistrationDeadlineLabel")
    let battleSubmissionDeadlineLabel = document.getElementById("battleSubmissionDeadlineLabel")
    let battleNumberTeamMemberLabel = document.getElementById("battleNumberTeamMemberLabel")
    let joinBattleAloneMessage = document.getElementById("joinBattleAloneMessage")
    let teamMateInput = document.getElementById("teamMateInput")
    let joinBattleAsTeamSubmit = document.getElementById("joinBattleAsTeamSubmit")
    let teamInput = document.getElementById("teamInput")
    let joinTeamSubmit = document.getElementById("joinTeamSubmit")
    let teamGradeInput = document.getElementById("teamGradeInput")
    let modifyGradeSubmit = document.getElementById("modifyGradeSubmit")

    joinBattleAloneButton.addEventListener('click', (e) => {
        makeCall("POST", 'JoinBattleAlone?BattleId=' + battleId, null,
            function (x) {
                //server return message
                var message = x.responseText;
                switch (x.status) {
                    case 200: //OK
                        openModal("joinBattleAlone")
                        joinBattleAloneMessage.innerHTML = "You signed up in the tournament"

                        hideAllButton()
                        break;
                    default: //error occurs
                        openModal("joinBattleAlone")
                        joinBattleAloneMessage.innerHTML = message
                        break;
                }
            })
    })

    joinBattleAsTeamButton.addEventListener('click', (e) => {
        makeCall("GET", 'ShowUserTeam?BattleId=' + battleId, null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE){
                    var message = x.responseText;
                    switch (x.status){
                        case 200:
                            message = JSON.parse(message)
                            if (message.length === 0) {
                                joinBattleAsTeamButton.style.display = "none"
                            }
                            else {
                                teamMateInput.innerHTML=''
                                updateTeammates(message)
                            }
                            break
                        default:
                            pageOrchestrator.showError(message);
                            break
                    }
                }
            })
    })

    joinBattleAsTeamSubmit.addEventListener('click', (e) => {
        //create Join Battle as Team form reference
        var form = e.target.closest("form")
        if (form.checkValidity()){
            makeCall("POST", 'JoinBattleAsTeam?BattleId=' + battleId, form,
                function (x) {
                    //server return message
                    var message = x.responseText;
                    switch (x.status) {
                        case 200: //OK
                            clearForm("joinBattleAsTeamForm")
                            hideAllButton()
                            closeModal()
                            break;
                        default: //error occurs
                            document.getElementById("errormessageJoinBattleAsTeam").textContent = message
                            break;
                    }
                })
        }
        else {
            form.reportValidity()
        }
        clearForm("joinBattleAsTeamForm")
    })

    selectTeamButton.addEventListener('click', (e) => {
        makeCall("GET", 'ShowTeam?BattleId=' + battleId, null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE){
                    var message = x.responseText;
                    switch (x.status){
                        case 200:
                            message = JSON.parse(message)
                            if (message.length === 0) {
                                selectTeamButton.style.display = "none"
                            }
                            else {
                                teamInput.innerHTML=''
                                updateTeams(message)
                            }
                            break
                        default:
                            pageOrchestrator.showError(message);
                            break
                    }
                }
            })
    })

    joinTeamSubmit.addEventListener('click', (e) => {
        //create Select Your Team form reference
        var form = e.target.closest("form")
        if (form.checkValidity()){
            makeCall("POST", 'JoinTeam?BattleId=' + battleId, form,
                function (x) {
                    //server return message
                    var message = x.responseText
                    switch (x.status){
                        case 200: //OK
                            clearForm("joinTeamForm")
                            hideAllButton()
                            closeModal()
                            break
                        default: //error occurs
                            document.getElementById("errormessageJoinTeam").textContent = message
                            break
                    }
                })
        }
        else {
            form.reportValidity()
        }
        clearForm("joinTeamForm")
    })

    modifyGradeButton.addEventListener('click', (e) => {
        makeCall("GET", 'ShowTeamInBattle?BattleId=' + battleId, null,
            function (x) {
                if (x.readyState === XMLHttpRequest.DONE){
                    var message = x.responseText
                    switch (x.status) {
                        case 200:
                            message = JSON.parse(message)
                            if (message.length === 0) {
                                modifyGradeButton.style.display = "none"
                            }
                            else {
                                teamGradeInput.innerHTML=''
                                updateTeamsGrade(message)
                            }
                            break
                        default:
                            pageOrchestrator.showError(message);
                            break
                    }
                }
            })
    })

    modifyGradeSubmit.addEventListener('click', (e) => {
        //create Modify Grade form reference
        var form = e.target.closest("form")
        if (form.checkValidity()){
            makeCall("POST", 'ModifyGrade?BattleId=' + battleId, form,
                function (x){
                    var message = x.responseText
                    switch (x.status){
                        case 200:
                            clearForm("modifyGradeForm")
                            closeModal()
                            break
                        default: //error occurs
                            document.getElementById("errormessageModifyGrade").textContent = message
                            break
                    }
                })
        }
        else {
            form.reportValidity()
        }
        clearForm("modifyGradeForm")
    })

    //show all possible team in an ongoing battle
    function updateTeamsGrade(teams) {
        teams.forEach(function (team) {
            let option = document.createElement("option")
            option.text = team.teamName
            option.value = team.idTeam
            teamGradeInput.add(option)
        })
    }

    //shows all possible teammates
    function updateTeammates(teammates){
        teammates.forEach(function (teammate) {
            let option = document.createElement("option")
            option.text = teammate.username
            option.value = teammate.id
            teamMateInput.add(option)
        })
    }

    //show all possible team
    function updateTeams(teams){
        teams.forEach(function (team) {
            let option = document.createElement("option")
            option.text = team.teamName
            option.value = team.idTeam
            teamInput.add(option)
        })
    }

    //hide all page buttons
    function hideAllButton() {
        joinBattleAloneButton.style.display="none"
        joinBattleAsTeamButton.style.display="none"
        selectTeamButton.style.display="none"
        modifyGradeButton.style.display = "none"
    }

    /*This is the method used to open the battle page
   * First it decides which button must be shown
   * Then with the makeCall function obtains all the tournament information and updates them*/
    this.openPage=function (id){
        battleId=id
        battleInfo.style.display=""
        battlePageDiv.style.display=""
        rankingTableDiv.style.display=""
        if (user.role===1){
            joinBattleAloneButton.style.display=""
            joinBattleAsTeamButton.style.display=""
            selectTeamButton.style.display=""
        }
        else if (user.role===0) {
            modifyGradeButton.style.display = ""
        }
        makeCall("GET","ShowBattleInfo?BattleId="+ battleId, null,
            function (x){
                if (x.readyState === XMLHttpRequest.DONE) {
                    var message = x.responseText;
                    switch (x.status) {
                        case 200:
                            message=JSON.parse(message)
                            self.updateBattleInfo(message)
                            break;
                        default:
                            pageOrchestrator.showError(message);
                            break;
                    }
                }
            }
        )
        makeCall("GET","CheckBattleRanking?BattleId="+ battleId, null,
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
    }

    //This function is used to update the tournament information
    //It obtains in input the battle information and adds them to the corresponding http element
    this.updateBattleInfo=function (battle){
        battleNameLabel.innerHTML="This is the "+battle.name +" battle of the "+battle.tournamentName +" tournament"
        if (battle.description!=null) {
            battleDescriptionLabel.innerHTML = battle.description
        }
        else{
            battleDescriptionLabel.innerHTML =""
        }
        battleRegistrationDeadlineLabel.innerHTML="Registration Deadline: "+battle.regDeadline
        battleSubmissionDeadlineLabel.innerHTML="Submission Deadline: "+battle.subDeadline
        battleNumberTeamMemberLabel.innerHTML="Number of Team Member: between "+battle.minNumStudent +" to "+battle.maxNumStudent

        hideAllButton()
        if (user.role === 1){
            if (battle.phase === "Not Started") {
                if (battle.minNumStudent === 1){
                    joinBattleAloneButton.style.display=""
                }
                else {
                    joinBattleAloneButton.style.display="none"
                }
                joinBattleAsTeamButton.style.display = ""
            }
        }
        else if (user.role === 0){
            if (battle.phase === "Ongoing"){
                modifyGradeButton.style.display = ""
            }
            else {
                modifyGradeButton.style.display = "none"
            }
        }
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
    this.hide=function (){
        battleInfo.style.display="none"
        battlePageDiv.style.display="none"
        joinBattleAloneButton.style.display="none"
        joinBattleAsTeamButton.style.display="none"
        selectTeamButton.style.display="none"
        modifyGradeButton.style.display = "none"
        rankingStarted.style.display="none"
        rankingTable.style.display="none"
        rankingTableDiv.style.display="none"
    }
}