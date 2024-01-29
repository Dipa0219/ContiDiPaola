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

    let battleNameLabel= document.getElementById("battleNameLabel")
    let battleDescriptionLabel = document.getElementById("battleDescriptionLabel")
    let battleRegistrationDeadlineLabel = document.getElementById("battleRegistrationDeadlineLabel")
    let battleSubmissionDeadlineLabel = document.getElementById("battleSubmissionDeadlineLabel")
    let battleNumberTeamMemberLabel = document.getElementById("battleNumberTeamMemberLabel")
    let joinBattleAloneMessage = document.getElementById("joinBattleAloneMessage")

    joinBattleAloneButton.addEventListener('click', (e) => {
        console.log("battle id" + battleId)
        makeCall("POST", 'JoinBattleAlone?BattleId=' + battleId, null,
            function (x) {
                //server return message
                var message = x.responseText;
                switch (x.status) {
                    case 200: //OK
                        openModal("joinBattleAlone")
                        joinBattleAloneMessage.innerHTML = "You signed up in the tournament"

                        joinBattleAloneButton.style.display = "none"
                        joinBattleAsTeamButton.style.display = "none"
                        selectTeamButton.style.display = "none"
                        break;
                    default: //error occurs
                        openModal("joinBattleAlone")
                        joinBattleAloneMessage.innerHTML = message
                        break;
                }
            })
    })

    /*This is the method used to open the battle page
   * First it decides which button must be shown
   * Then with the makeCall function obtains all the tournament information and updates them*/
    this.openPage=function (id){
        battleId=id
        battleInfo.style.display=""
        battlePageDiv.style.display=""
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

        if (user.role === 1){
            if (battle.minNumStudent === 1){
                joinBattleAloneButton.style.display=""
            }
            else {
                joinBattleAloneButton.style.display="none"
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


    /*This function is used to hide all the element contained in this page*/
    this.hide=function (){
        battleInfo.style.display="none"
        battlePageDiv.style.display="none"
        joinBattleAloneButton.style.display="none"
        joinBattleAsTeamButton.style.display="none"
        selectTeamButton.style.display="none"
        modifyGradeButton.style.display = "none"
    }
}