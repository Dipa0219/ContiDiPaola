package it.polimi.SE2.CK.utils;

import java.util.Random;

public class Evaluation {

    public int point (){
        Random random = new Random();

        return random.nextInt(100) + 1;
    }
}
