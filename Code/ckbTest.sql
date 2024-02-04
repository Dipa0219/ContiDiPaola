Drop schema if exists `ckbtest`;
CREATE SCHEMA `ckbtest` ;

Drop table if exists `ckbtest`.`user`;

CREATE TABLE `ckbtest`.`user` (
  `idUser` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Surname` varchar(45) NOT NULL,
  `BirthDate` date NOT NULL,
  `Username` varchar(45) NOT NULL,
  `Email` varchar(60) NOT NULL,
  `Password` text NOT NULL,
  `GitHubUser` varchar(250) NOT NULL,
  `Role` int unsigned NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `idUser_UNIQUE` (`idUser`),
  UNIQUE KEY `Username_UNIQUE` (`Username`),
  UNIQUE KEY `Email_UNIQUE` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

Drop table if exists `ckbtest`.`tournament`;

CREATE TABLE `ckbtest`.`tournament` (
  `idTournament` int unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `CreatorId` int NOT NULL,
  `RegDeadline` datetime NOT NULL,
  `Phase` varchar(15) NOT NULL,
  PRIMARY KEY (`idTournament`),
  UNIQUE KEY `idTournament_UNIQUE` (`idTournament`),
  UNIQUE KEY `Name_UNIQUE` (`Name`),
  KEY `idUser_idx` (`CreatorId`),
  CONSTRAINT `idUser` FOREIGN KEY (`CreatorId`) REFERENCES `user` (`idUser`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

Drop table if exists `ckbtest`.`t_subscription`;

CREATE TABLE `ckbtest`.`t_subscription` (
  `TournamentId` int unsigned NOT NULL,
  `UserId` int NOT NULL,
  `Points` int DEFAULT NULL,
  PRIMARY KEY (`TournamentId`,`UserId`),
  KEY `user_idx` (`UserId`),
  CONSTRAINT `tournament` FOREIGN KEY (`TournamentId`) REFERENCES `tournament` (`idTournament`) ON DELETE CASCADE,
  CONSTRAINT `user` FOREIGN KEY (`UserId`) REFERENCES `user` (`idUser`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

Drop table if exists `ckbtest`.`battle`;

CREATE TABLE `ckbtest`.`battle` (
  `idbattle` int unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `RegDeadline` datetime NOT NULL,
  `SubDeadline` datetime NOT NULL,
  `CodeKata` blob,
  `MinNumStudent` int unsigned NOT NULL,
  `MaxNumStudent` int unsigned NOT NULL,
  `TournamentId` int unsigned NOT NULL,
  `phase` varchar(45) NOT NULL,
  PRIMARY KEY (`idbattle`),
  UNIQUE KEY `Name_UNIQUE` (`Name`),
  UNIQUE KEY `idbattle_UNIQUE` (`idbattle`),
  CONSTRAINT `battle_tournament` FOREIGN KEY (`TournamentId`) REFERENCES `tournament` (`idTournament`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

Drop table if exists `ckbtest`.`team`;

CREATE TABLE `ckbtest`.`team` (
  `idteam` int unsigned NOT NULL AUTO_INCREMENT,
  `numberStudent` int DEFAULT NULL,
  `battleId` int unsigned NOT NULL,
  `phase` varchar(45) DEFAULT NULL,
  `teamLeader` int NOT NULL,
  `points` int DEFAULT '0',
  `teamName` varchar(45) NOT NULL,
  PRIMARY KEY (`idteam`),
  KEY `battle_idx` (`battleId`),
  KEY `creatorId_idx` (`teamLeader`),
  CONSTRAINT `battle` FOREIGN KEY (`battleId`) REFERENCES `battle` (`idbattle`) ON DELETE CASCADE,
  CONSTRAINT `creatorId` FOREIGN KEY (`teamLeader`) REFERENCES `user` (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

Drop table if exists `ckbtest`.`team_student`;

CREATE TABLE `ckbtest`.`team_student` (
  `teamId` int unsigned NOT NULL,
  `studentId` int NOT NULL,
  `phase` varchar(45) DEFAULT NULL,
  `points` int DEFAULT '0',
  PRIMARY KEY (`teamId`,`studentId`),
  KEY `student_idx` (`studentId`),
  CONSTRAINT `student` FOREIGN KEY (`studentId`) REFERENCES `user` (`idUser`),
  CONSTRAINT `team` FOREIGN KEY (`teamId`) REFERENCES `team` (`idteam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER $$
USE `ckbtest`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ckbtest`.`tournament_AFTER_UPDATE` AFTER INSERT ON `tournament` FOR EACH ROW
BEGIN
	INSERT INTO  `ckbtest`.`t_subscription` (TournamentId, UserId)
			VALUE(NEW.IdTournament, NEW.CreatorId);
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `ckbtest`.`team_AFTER_INSERT`;

DELIMITER $$
USE `ckbtest`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ckbtest`.`team_AFTER_INSERT` AFTER INSERT ON `team` FOR EACH ROW
BEGIN
INSERT INTO `ckbtest`.`team_student` (`teamId`, `studentId`, `phase`) VALUES (new.idteam, new.teamLeader, 'Accept');
END$$
DELIMITER ;

DELIMITER $$
USE `ckbtest`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ckbtest`.`team_AFTER_UPDATE` AFTER UPDATE ON `team` FOR EACH ROW
BEGIN
	if new.points <> old.points then
		UPDATE `ckbtest`.`team_student` SET `Points` =  new.points WHERE `teamId` = new.idteam;
    end if;
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `ckbtest`.`team_student_BEFORE_UPDATE`;

DELIMITER $$
USE `ckbtest`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ckbtest`.`team_student_AFTER_UPDATE` AFTER UPDATE ON `team_student` FOR EACH ROW
BEGIN
	DECLARE teamCount INT;
    DECLARE teamSize INT;
    DECLARE minNumberStudentPerTeam INT;
    DECLARE maxNumberStudentPerTeam INT;
    
	IF NEW.phase = "Accept" THEN
        #count the number of student in accept phase per team
        SELECT COUNT(*) INTO teamCount
        FROM `ckbtest`.team_student
        WHERE teamId = NEW.teamId and phase = "Accept";
        
        #get number of teammates
        SELECT numberStudent INTO teamSize
        FROM `ckbtest`.team
        WHERE idTeam = NEW.teamId;
        
        #get min and max number of student per team for specific battle
        SELECT MinNumStudent, MaxNumStudent
			INTO minNumberStudentPerTeam, maxNumberStudentPerTeam
		FROM `ckbtest`.battle
        WHERE idBattle = (
			SELECT battleId
            FROM `ckbtest`.team
            WHERE idTeam = NEW.teamId);
		
        #number of accepted student equal to team size
        IF teamCount = teamSize THEN
			UPDATE `ckbtest`.team
            SET phase = "Complete"
            WHERE idTeam = NEW.teamId;
		ELSE
			#number of accepted student is in battle limits
            IF teamCount >= minNumberStudentPerTeam
				and teamCount <= maxNumberStudentPerTeam THEN
					UPDATE `ckbtest`.team
					SET phase = "Complete"
					WHERE idTeam = NEW.teamId;
            END IF;
        END IF;
    END IF;
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS `ckbtest`.`team_student_AFTER_UPDATE`;

DELIMITER $$
USE `ckbtest`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ckbtest`.`team_student_AFTER_UPDATE` AFTER UPDATE ON `team_student` FOR EACH ROW
BEGIN
	declare old_points int;
    select points into old_points
    from `ckbtest`.t_subscription
	where userid = new.studentId 
		and tournamentId= (select tournamentId
							from `ckbtest`.team join battle on battleId = idbattle
                            where idTeam = new.teamid);
	if new.points <> old.points then
		UPDATE `ckbtest`.`t_subscription` SET `Points` =  old_points + new.points - old.points 
        WHERE userid = new.studentId 
			and tournamentId= (select tournamentId
							from `ckbtest`.team join battle on battleId = idbattle
                            where idTeam = new.teamid);
    end if;
END$$
DELIMITER ;


INSERT INTO `ckbtest`.`user` (`Name`, `Surname`, `BirthDate`, `Username`, `Email`, `Password`, `GitHubUser`, `Role`) VALUES ('Bob', 'Ross', '1999-02-03', 'Bob99', 'BobRoss@gmail.com', 'password', 'Bob99', '1');
INSERT INTO `ckbtest`.`user` (`Name`, `Surname`, `BirthDate`, `Username`, `Email`, `Password`, `GitHubUser`, `Role`) VALUES ('David', 'Jones', '1975-08-06', 'David87', 'DavidJones@mail.polimi.it', '123456', 'David87', '0');
INSERT INTO `ckbtest`.`user` (`Name`, `Surname`, `BirthDate`, `Username`, `Email`, `Password`, `GitHubUser`, `Role`) VALUES ('Josh', 'Hart', '1978-06-03', 'Josh78', 'JoshHart@yahoo.it', 'abcdef', 'Josh78', '0');
INSERT INTO `ckbtest`.`user` (`Name`, `Surname`, `BirthDate`, `Username`, `Email`, `Password`, `GitHubUser`, `Role`) VALUES ('Ted', 'Parker', '1969-12-26', 'Ted69', 'TedParker@gmail.com', '261269', 'Ted69', '0');
INSERT INTO `ckbtest`.`user` (`Name`, `Surname`, `BirthDate`, `Username`, `Email`, `Password`, `GitHubUser`, `Role`) VALUES ('Jean', 'White', '2000-05-17', 'Jean00', 'Jean.Grey@gmail.com', 'qwerty', 'Jean00', '1');
INSERT INTO `ckbtest`.`user` (`Name`, `Surname`, `BirthDate`, `Username`, `Email`, `Password`, `GitHubUser`, `Role`) VALUES ('Tim', 'Grey', '1998-07-30', 'Tim98', 'Tim.Duncan@yahoo.it', 'asdfgh', 'Tim98', '1');

INSERT INTO `ckbtest`.`tournament` (`Name`, `Description`, `CreatorId`, `RegDeadline`, `Phase`) VALUES ('C_Start1', 'Basic course of C language', '2', '2024-01-19 19:00:00', 'Ongoing');
INSERT INTO `ckbtest`.`tournament` (`Name`, `Description`, `CreatorId`, `RegDeadline`, `Phase`) VALUES ('Java_1', 'First approach to class programming', '3', '2023-09-03 17:00:00', 'Closed');
INSERT INTO `ckbtest`.`tournament` (`Name`, `Description`, `CreatorId`, `RegDeadline`, `Phase`) VALUES ('Java_2', 'Average course for java. Advised for student that has already practised this langauge or has partecipated in Java_1 tournament', '3', '2024-01-08 10:00:00', 'Ongoing');
INSERT INTO `ckbtest`.`tournament` (`Name`, `Description`, `CreatorId`, `RegDeadline`, `Phase`) VALUES ('Java_3', 'Advanced course for java. Advised for student that has confidence with this langauge or has partecipated in Java_2 tournament', '3', '2024-03-01 10:00:00', 'Not Started');
INSERT INTO `ckbtest`.`tournament` (`Name`, `CreatorId`, `RegDeadline`, `Phase`) VALUES ('Python_1', '4', '2023-09-12 10:00:00', 'Ongoing');
INSERT INTO `ckbtest`.`tournament` (`Name`, `CreatorId`, `RegDeadline`, `Phase`) VALUES ('Acse', '4', '2023-12-01 10:00:00', 'Ongoing');

INSERT INTO `ckbtest`.`t_subscription` (`TournamentId`, `UserId`,`Points`) VALUES ('1', '1', '5');
INSERT INTO `ckbtest`.`t_subscription` (`TournamentId`, `UserId`) VALUES ('1', '4');
INSERT INTO `ckbtest`.`t_subscription` (`TournamentId`, `UserId`,`Points`) VALUES ('6', '1', '0');
INSERT INTO `ckbtest`.`t_subscription` (`TournamentId`, `UserId`,`Points`) VALUES ('6', '5', '0');
INSERT INTO `ckbtest`.`t_subscription` (`TournamentId`, `UserId`,`Points`) VALUES ('6', '6', '0');

INSERT INTO `ckbtest`.`battle` (`Name`, `Description`, `RegDeadline`, `SubDeadline`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `phase`) VALUES ('S_Out', 'First practice of scanf and printf', '2024-01-15 10:00:00', '2024-03-12 10:00:00', '1', '3', '1', 'Ongoing');
INSERT INTO `ckbtest`.`battle` (`Name`, `Description`, `RegDeadline`, `SubDeadline`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `phase`) VALUES ('Iterate', 'Introduce this function in acse', '2024-01-24 12:00:00', '2024-02-24 23:59:59', '1', '2', '6', 'Ongoing');
INSERT INTO `ckbtest`.`battle` (`Name`, `Description`, `RegDeadline`, `SubDeadline`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `phase`) VALUES ('Do_while', 'Introduce this function in acse', '2024-01-17 12:00:00', '2024-01-31 23:59:59', '1', '2', '6', 'Closed');
INSERT INTO `ckbtest`.`battle` (`Name`, `Description`, `RegDeadline`, `SubDeadline`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `phase`) VALUES ('Break', 'Introduce this function in acse', '2024-02-28 12:00:00', '2024-03-30 23:59:59', '1', '2', '6', 'Not Started');
INSERT INTO `ckbtest`.`battle` (`Name`, `Description`, `RegDeadline`, `SubDeadline`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `phase`) VALUES ('Else_if', 'Introduce this function in acse', '2024-03-01 10:00:00', '2024-03-15 23:59:59', '1', '3', '6', 'Not Started');
INSERT INTO `ckbtest`.`battle` (`Name`, `Description`, `RegDeadline`, `SubDeadline`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `phase`) VALUES ('Case', 'Introduce this function in acse', '2024-04-01 10:00:00', '2024-04-25 23:59:59', '1', '2', '6', 'Not Started');
INSERT INTO `ckbtest`.`battle` (`Name`, `Description`, `RegDeadline`, `SubDeadline`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `phase`) VALUES ('Add_Char', 'Introduce this function in acse(Difficult)', '2024-05-01 10:00:00', '2024-06-15 23:59:59', '2', '3', '6', 'Not Started');

INSERT INTO `ckbtest`.`team` (`numberStudent`, `battleId`, `phase`, `teamLeader`, `points`, `teamName`) VALUES ('2', '2', 'Complete', '1', '0', 'Bombers');
INSERT INTO `ckbtest`.`team` (`numberStudent`, `battleId`, `phase`, `teamLeader`, `points`, `teamName`) VALUES ('2', '4', 'Partial', '1', '0', 'Bombers');
INSERT INTO `ckbtest`.`team` (`numberStudent`, `battleId`, `phase`, `teamLeader`, `points`, `teamName`) VALUES ('3', '5', 'Partial', '1', '0', 'Bombers');
INSERT INTO `ckbtest`.`team` (`numberStudent`, `battleId`, `phase`, `teamLeader`, `points`, `teamName`) VALUES ('1', '5', 'Complete', '6', '0', 'Tim98');
INSERT INTO `ckbtest`.`team` (`numberStudent`, `battleId`, `phase`, `teamLeader`, `points`, `teamName`) VALUES ('1', '1', 'Complete', '1', '5', 'Bob99');

INSERT INTO `ckbtest`.`team_student` (`teamId`, `studentId`, `phase`, `points`) VALUES ('1', '5', 'Accept', '0');
INSERT INTO `ckbtest`.`team_student` (`teamId`, `studentId`, `phase`, `points`) VALUES ('2', '5', 'Not Accept', '0');
INSERT INTO `ckbtest`.`team_student` (`teamId`, `studentId`, `phase`, `points`) VALUES ('3', '5', 'Not Accept', '0');
INSERT INTO `ckbtest`.`team_student` (`teamId`, `studentId`, `phase`, `points`) VALUES ('3', '6', 'Not Accept', '0');



