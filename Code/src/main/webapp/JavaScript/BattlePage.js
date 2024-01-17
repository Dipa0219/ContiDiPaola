function BattlePage(user){
    //general initialization
    let self= this
    let battleId

    let battleInfo= document.getElementById("battleInfo")

    let battleNameLabel= document.getElementById("battleNameLabel")
    let battleDescriptionLabel = document.getElementById("battleDescriptionLabel")
    let battleRegistrationDeadlineLabel = document.getElementById("battleRegistrationDeadlineLabel")
    let battleSubmissionDeadlineLabel = document.getElementById("battleSubmissionDeadlineLabel")
    let battleNumberTeamMemberLabel = document.getElementById("battleNumberTeamMemberLabel")

    this.openPage=function (id){
        battleId=id
        battleInfo.style.display=""
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
    }

    this.hide=function (){
        battleInfo.style.display="none"
    }
}