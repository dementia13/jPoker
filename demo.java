/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Scanner;
import static poker.Score.getScore;
import static poker.Window.allHands;
import static poker.poker.CPH;
import poker.poker.Card;
import poker.poker.Hand;
import static poker.poker.NUM_HANDS;
import static poker.poker.assignCard;
import static poker.poker.assignHand;
import static poker.poker.getRank;
import static poker.poker.getRankName;
import static poker.poker.getSuit;
import static poker.poker.getSuitName;
import static poker.poker.getType;
import static poker.poker.textDisplayHand;

/**
 *
 * @author Sean Quinn
 */
public class demo {
    
    static String filePath = "src/poker/resources/";
        
    
    demo(){
        
    }
    
    static poker.Hand[] getDemo(String demoFile, Hand[] allHands){
        filePath += demoFile;
        File dmFile = new File(filePath);
        int newHandID = 0;
        try{
            Scanner scanner = new Scanner(dmFile);
            for(int handCount = 1; scanner.hasNext(); handCount++){
                Hand newHand = new Hand();          
                /*
                    System.out.println(handCount);
                    if (handCount > 1){
                    System.out.println(allHands[(handCount - 1)].toString());
                    }
                */                
                newHand.handID = scanner.nextInt();
                for(int cardCount = 1; cardCount <= CPH; cardCount++){
                    if(scanner.hasNextInt()){
                        Card newCard = new Card();
                        newCard = assignCard(scanner.nextInt());
                        newHand.hand[cardCount] = newCard;
                    }
                }
                newHand = assignHand(newHand.handID, newHand.hand);
            //System.out.println(newHand.toString());
                newHand = Score.sortHand(newHand);
            //System.out.println(newHand.toString());
                newHand.score = getScore(newHand);
            //System.out.println(newHand.toString());
                newHand.type = getType();
                allHands[handCount] = newHand;
            //System.out.println(newHand.toString());
            }
            scanner.close();
        }catch(FileNotFoundException fnfe ){
            System.err.println("File not found: " + fnfe.getMessage());
        }
        return allHands;
    }
    
}
