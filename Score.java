/*
/*
 * Copyright 2017 Sean R Quinn

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR S
ERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package poker;

import java.util.ArrayList;
import static poker.poker.CARD_INIT;
import static poker.poker.CPH;
import static poker.poker.DECK_SIZE;
import poker.poker.Hand;
import static poker.poker.NUM_HANDS;
import static poker.poker.NUM_RANKS;
import static poker.poker.ZERO;

/**
 *
 * @author Sean Quinn
 * sean@dementia13.net
 * 
 * This class contains the various functions used by Poker for scoring and 
 *  ranking the hands.
 */
public class Score {
    
    static final int HIGH_ACE = 14; // value of high ace in a straight
    
    static int[] mCount = {ZERO, ZERO};

    /*
    static int getLargest(int *h_count, int *n_count, int *t_count, int *f_count,
		int *l_count, int r_count, int* h_card, int* n_card,
			int* t_card, int* f_card, int* l_card){
	int temp = ZERO;
	if (*h_count > r_count){
		r_count = *h_count;
	}
	if (*n_count > r_count){
		r_count = *n_count;
		temp = *h_count;
		*h_count = *n_count;
		*n_count = temp;
	}
	if (*t_count > r_count){
		r_count = *t_count;
		temp = *h_card;
		*h_card = *t_card;
		*t_card = *n_card;
		*n_card = temp;
	}
	/* No need to test for fourth card- there's no way
 		it can be counted more than once. And if
		the low card counts twice, there is no fourth card 
	if (*l_count > r_count){
		r_count = *l_count;
		temp = *h_card;
		*h_card = *l_card;
		*l_card = *t_card;
		*t_card = *n_card;
		*n_card = temp;
	}	
	return r_count;
    }
    */
    
/**                        getMost
* Track cards that appear in a Hand multiple times. A 5-card hand can have at
* most 2 cards that appear multiple times, so this is a 2-int array that
* contains the most- and second-most-frequently occurring Cards.
* This is used for sorting and scoring the Hands.
* 
* @param counts     the value that changed
* @return           int array with the number of times the most-frequently
*                   occurring cards appear. The actual card values are not 
*                   specified.
*
*/
    static int[] getMost(int[] counts){
        int removed = 0;
        int most = 0, nextMost = 0;
        for(int index = 0; index < CPH; index++ ){
            if(counts[index] > most){
                most = counts[index];
                removed = index;
            }
        }
        for(int nindex = 0; nindex < CPH;
                nindex++){
            if((counts[nindex] > nextMost) && (nindex != removed)){
                nextMost = counts[nindex];
            }
        }
        return new int[]{most, nextMost};
    }

 /*----------------- getMostCount ----------------------------------------------
 * function getMostCount()
 *
 * Purpose: used by getScore to find which cards appear in the hand the most.
 *      Two-integer array is calculated & stored during the sortHand function
 *
 * @return int[] - the two most common ranks in descending order. Given 5
 *      cards, there can be at most two ranks that appear in multiple
 *
------------------------------------------------------------------------------*/
 /**
 *
 */
    static int[]getMostCount(){
        return mCount;
    }
    
/**                     getScore
 * Assigns a score to each hand. Scoring is based on five-card draw rules as
 * referenced from website https://www.pokerstars.com/poker/games/rules/hand-rankings/ 
 * <p>
 * Order of rankings:
 * <li>Straight flush</li>
 * <li>Four of a kind</li>
 * <li>Full house</li>
 * <li>Flush</li>
 * <li>Straight</li>
 * <li>Three of a kind</li>
 * <li>Two pair</li>
 * <li>One pair</li>
 * <li>High card</li>
 * <p>
 * The low cards, which are not part of a ranking or are not the high card in a 
 * hand with no groupings, are used as tiebreakers. Hands are sorted in order, and
 * a tie at one position will result in the next lower card being used as a tiebreaker.
 * Each position is weighted so that the higher card has more influence on the 
 * hand score. For example, a 4 and a 3 cannot outrank a 5 and an ace. Each position
 * in the hand carries a base score that is higher than what the next lower position
 * can achieve, and each ranking carries a base score that is higher than the 
 * highest possible score of the next lower ranking.
 * <p>
 * This function analyzes each hand for its type and depends on helper functions
 * to calculate the score.
 * 
 * @param   aHand   Each hand is passed to this function at its creation
 * @return          a long integer score value
 */
    static long getScore(Hand aHand){
        long score = ZERO;
        int straight = ZERO;
        int flush = ZERO;
        int hIndex = ZERO;
        int rankCount = ZERO;

        // -- Values used in calculating scores --------------------------------

        int highCard = aHand.hand[5].rank;
        int lowCard = aHand.hand[1].rank;
        int nextCard = aHand.hand[4].rank;
        int thirdCard = aHand.hand[3].rank;
        int fourthCard = aHand.hand[2].rank;
        int highCount = ZERO;
        int nextCount = ZERO;
        int thirdCount = ZERO;
        int fourthCount = ZERO;
        int lowCount = ZERO;
        int suitCount = poker.CARD_INIT;

        for(int i = poker.CARD_INIT; i < poker.CPH; i++){
            if(aHand.hand[i].suit == aHand.hand[i + poker.CARD_INIT].suit){
                suitCount++;
            }
        }

        /* Iterate through hand
                Break down hand for:
                - what is the high card in the hand
                - how many times does each rank appear */


        // make an array of the five face values in ascending order
        int sorted[] = {ZERO, lowCard, fourthCard, thirdCard, 
                nextCard, highCard}; 
        int[] mostCount = getMostCount();
        highCount = mostCount[ZERO];
        nextCount = mostCount[1];

        switch (highCount){
                case 4: 
                        poker.setType("Four of a kind");
                        return isFourKind(highCard, lowCard);
                case 3:
                        if(nextCount == 2){
                            poker.setType("Full house");
                            return isFullHouse(highCard, lowCard);
                        }
                        else  
                            poker.setType("Three of a kind");
                            return isThreeKind(highCard, nextCard, lowCard);
                case 2:
                        if(nextCount == 2){
                            poker.setType("Two pairs");
                            return isTwoPair(highCard, highCard, lowCard);
                        }
                        else
                            poker.setType("One pair");
                            return isPair(highCard, nextCard, thirdCard,
                                        lowCard);	
                case 1:
                        if (hasStraight(sorted) == true){
                            if (suitCount == poker.CPH){
                                poker.setType("Straight flush");  
                                return isStraightFlush(lowCard);
                            }
                                poker.setType("Straight");
                                return isStraight(lowCard);
                        }
                        else
                        if (suitCount == poker.CPH){
                            poker.setType("Flush");
                            aHand = isFlush(aHand);
                            return aHand.score;
                        }
                        else
                            poker.setType("High card");
                            return isHighCard(sorted);
                default:
                        System.out.println("logic error in getScore function");
        }
        return score;
    }       
    
/**                        getSortedHand
 * Matches information from the Hand object back to the int array of sorted
 * Card ID#s, resulting in a Hand sorted by ascending Card value.
 * <p>Maybe there's an easier way to do this.
 * 
 * @param   sorted  array of Card ID#s sorted by ascending value
 * @param   uHand   Hand object to be sorted
 * @return          Hand object with Cards sorted by ascending value
*
*/
    static Hand getSortedHand(int[] sorted, Hand uHand){
        Hand sHand = new Hand();
            boolean removed[] = new boolean[poker.CPH + poker.CARD_INIT];
        for(int cSortIndex = poker.CARD_INIT; cSortIndex <= poker.CPH; 
                    cSortIndex++){
            
            for(int hSortIndex = poker.CARD_INIT; hSortIndex <= poker.CPH;
                        hSortIndex++){
                // Taking face value from the sorted int array, matching it 
                // back to face values from the unsorted hand, reading those
                // values into new sorted hand
                // removed[] prevents matching to already-matched cards: 
                // otherwise a pair might count the same card twice instead of
                // getting both of the same-value cards
                if(removed[hSortIndex]){
                    continue;
                }
                if(sorted[cSortIndex] == uHand.hand[hSortIndex].rank){
                    sHand.hand[cSortIndex] = uHand.hand[hSortIndex];
                    //System.out.println(uHand.hand[hSortIndex].iDNum);
                    //System.out.println(sHand.hand[cSortIndex].iDNum);
                    removed[hSortIndex] = true;
                    break;
                }
            }
        }
        sHand.handID = uHand.handID;
        sHand.type = uHand.type;
        return sHand;
    }
    
/**                     getTie
 * Detects whether a game has two hands with the same winning score. Doesn't
 * care if there is a tie for a lower place.
 * 
 * @param   allHands    array of all Hand objects
 * @param   winner      ID# of hand with high score
 * 
 * return               true if tied, false otherwise
 *
 */
    static boolean getTie(Hand[] allHands, int winner){
        //boolean tie = false;
        long target = allHands[winner].score;
        for(int handex = 1; handex <= NUM_HANDS; handex++){
            if((handex != winner) && (allHands[handex].score == target)){
                poker.isTie = true;
                return true;
            }
        }
        return false;
    }

    
    // DETECT PRESENCE OF STRAIGHT
    
/*----------------- hasStraight ------------------------------------------------
 * function hasStraight(int[])  
 *
 * Purpose: Tests the hand for presence of a straight.
 * 	Function is passed an array of cards sorted low to high. Their
 * 	values are compared one by one against the counter of a for loop
 * 	that increments by one. If the value of the counter = the value of 
 * 	the card on every pass, it is a straight.
 *
 * @param - int[] sorted - the sorted cards in an integer array. 
 *
 * @return - true or false 
 *
-----------------------------------------------------------------------------*/
 /**
 *
 */
    static boolean hasStraight(int sorted[]){
        // if 2 low cards are 1 & 10, it is a royal straight, set ace high
	if ((sorted[1] == 1) && (sorted[2] == 10)){
            sorted[1] = sorted[2];
            sorted[2] = sorted[3];
            sorted[3] = sorted[4];
            sorted[4] = sorted[5];
            sorted[5] = HIGH_ACE;
	}
        // returns false if any card is not one more than the previous
	for(int count = CARD_INIT + 1; count <= CPH; count++){
            int strtCount = (sorted[count] - sorted[count - 1]);
            if (strtCount != 1){
                    return false;
            } 
	}
	return true;
    }
    
    // -- "IS" FUNCTIONS - RETURN SCORE FOR A GIVEN HAND
    
/*----------------- isFlush ----------------------------------------------------
 * function isFlush(Hand)  
 *
 * Purpose: Calculates the score of a hand that is a flush. 
 *
 * @param - int pointer to the rank value of each card, in sorted order 
 *
 * @return - int - flush base value + weighted face card value 
 *
------------------------------------------------------------------------------*/
 /**
 *
 */
    static poker.Hand isFlush(poker.Hand sHand){
	long score = 0;
	/* Numbers are as calculated for isHighCard */
	score = (sHand.hand[1].rank) + (9 + (sHand.hand[2].rank * 13)) + 
                ((169 * sHand.hand[3].rank) + 140) + 
                    ((13 * 13 * 13 * sHand.hand[4].rank) + 2000)
                        + ((13 * 13 * 13 * 13 * sHand.hand[5].rank) + 28365); 
	score = (score + poker.PAIR_BASE + poker.TWO_PAIR_BASE + 
                poker.THREE_KIND_BASE + poker.STRAIGHT_BASE + poker.FLUSH_BASE); 
        sHand.score = score;
	return sHand;
    }
    
    
/*----------------- isFourKind ------------------------------------------------
 * function isFourKind(int, int)  
 *
 * Purpose: Calculates the score of a four-of-a-kind hand. Follows scoring
 * 	pattern of lower-scoring hands.
 * 	Highest possible hand: 4K + Q
 * 	low card: 1-13
 * 	high card: 12 cards, 14-170
 * 	score is high card + low card + combined high scores of all lower-
 * 		ranked hands 
 * 	Adding all gives straight flush base = 183
 *
 * @param - int high - face value of the four of a kind
 * 	    int low - face value of tiebreaker card 
 *
 * @return - long - score + scores of all lower-ranked combinations
 *
-----------------------------------------------------------------------------*/
 /**
 *
 */
    static long isFourKind(int high, int low){
	long score = 0;
	score = (13 * high + 14) + low;
	score = (score + poker.PAIR_BASE + poker.TWO_PAIR_BASE + 
                poker.THREE_KIND_BASE + poker.STRAIGHT_BASE + poker.FLUSH_BASE 
                    + poker.FULL_HOUSE_BASE + poker.FOUR_KIND_BASE); 
	return score;
    }
    
/*----------------- isFullHouse ----------------------------------------------
 * function isFullHouse(int, int)  
 *
 * Purpose: Calculates score of a hand containing a full house. Pattern similar
 * 	to those of lower-valued hands.
 *	Highest possible hand: 3K + 2Q
 *	low card: 1-13
 *	high card: 12 cards, 14 + 12 * high_card = 169 
 *	score is high card + low card + combined high scores of all 
 *		lower-ranked hands
 *	Adding all gives 183 = four of a kind base
 *
 * @param - int highCard - value of the three-card grouping of the hand 
 * 	    int lowCard - value of the two-card grouping of the hand
 *
 * @return - long score - base score for a full house + the suit value
 *
-----------------------------------------------------------------------------*/
 /**
 *
 */
    static long isFullHouse(int highCard, int lowCard){
	int score;
	/* numbers derived in function description */
	score = (12 * highCard + 14) + lowCard;
	score = (score + poker.PAIR_BASE + poker.TWO_PAIR_BASE + 
                poker.THREE_KIND_BASE + poker.STRAIGHT_BASE + poker.FLUSH_BASE 
                    + poker.FULL_HOUSE_BASE);
	return score;
    }
    
/*----------------- isHighCard ----------------------------------------------
 * function isHighCard(int[])  
 *
 * Purpose:  
 *	Scoring: Every card in the hand is a potential tiebreaker, so they 
 *		all count. The highest possible hand of this type goes
 *		like this: K-Q-J-10-8 (as 9 would give a straight). The low
 *		card counts as face value, but each next higher card has to 
 *		multiply instead of adding, to guarantee that each combination
 *		gives a unique score. This is like exponential math in a 
 *		base number system where the base decreasese by 1 each time
 *		a card has been played
 *		low card: 8 cards
 *			- Not 9 cards, or would be a straight
 *		next card: 10 cards: 9 + 10 * 13 = 139 
 *		third: 11 cards: 140 + (11 * 13 * 13) = 1999
 *		fourth: 12 cards: 2000 + (12 * 13 * 13 * 13) = 28364
 *		fifth: 13 cards: 28365 + (13 + 13 * 13 * 13 * 13) = 399658
 *		Adding all together gives 430168 - will be base score for
 *			one pair
 *
 * @param - Hand - the Hand to be tested 
 *
 * @return - long score - weighted value of all cards in hand
 *
-----------------------------------------------------------------------------*/
 /**
 *
 */
    static long isHighCard(int[] sorted){
	long score = 0;
	/* numbers are as calculated in function documentation */
	score = (sorted[1]) + (9 + (sorted[2] * 13)) + ((169 * sorted[3])
		+ 140) + ((13 * 13 * 13 * sorted[4]) + 2000)
                    + ((13 * 13 * 13 * 13 * sorted[5]) + 28365); 
	return score; 
    }
    
/*----------------- isPair ----------------------------------------------
 * function isPair(Hand)  
 *
 * Purpose: Calculates the score of a hand identified to contain one pair.
 * 	Lowest possible score is higher than highest possible score of a 
 * 	High Card hand - ensured by adding hand score on top of High Card
 * 	high score of 430,168.
 * 	Highest possible 1-pair hand: 2K - Q - J - 10
 * 	low card: 1-10
 * 	third card: 11 cards: 11 + (11 * 13) = 154 
 * 	next card: 12 cards: 155 + (12 * 169) = 2183
 * 	high card: 13 cards: 2183 + 2(13 * 13 * 13 * 13) = 59305
 * 	Adding all gives two pair base: 33089
 *
 * @param - Hand - the Hand to be tested 
long isPair(int high_card, int next_card, int *third_card, int *low_card
 *
 * @return - long - base value of a pair plus the suit value of the pair and
 *      weighted value of single cards in hand
 *
-----------------------------------------------------------------------------*/
 /**
 *
 */
    static long isPair(int highCard, int nextCard, int thirdCard,
					int lowCard){
	long score = 0;
	/* see function description for origin of these numbers */
	score = ((highCard * 13 * 13 * 13 + 2182) 
		+ (nextCard * 13 * 13 + 154) + (thirdCard * 13 + 11)
		+ lowCard);
	score = (poker.PAIR_BASE + score);
	return score; 
    }
    
/*----------------- isStraight -------------------------------------------------
 * function isStraight(int lowCard)  
 *
 * Purpose: Calculates the score of a hand that contains a straight 
 * 	Highest possible hand: A + K + Q + J + 10. All 5 cards count, so
 * 	there are no tiebreakers.
 * 	Score: 1-10 + combined scores of lower hands
 *
 * @param - int - lowCard - low card is selected as the score value. No sense in
 *      calculating the other cards when the final value is only different 
 *      by 1 anyway
 *
 * @return - long - low card + base score 
 *
-----------------------------------------------------------------------------*/
 /**
 *
 */
    static long isStraight(int lowCard){
	long score = 0;
	score = (lowCard + poker.PAIR_BASE + poker.TWO_PAIR_BASE + 
                poker.THREE_KIND_BASE + poker.STRAIGHT_BASE); 
	return score; 
    }
    
/*----------------- isStraightFlush ------------------------------------------
 * function isStraightFlush(int lowCard)  
 *
 * Purpose: Calculates the score of a straight flush
 * 	Highest possible hand: A + K + Q + J + 10
 * 	returns low card: 1-10
 * 	score = low card + combination of all lower-value hands 
 *	  
 * @param - int - lowCard - the low card of the straight 
 * 
 * @return - long - returns the base value of a straight flush
 * 			plus the low suit of the hand 
 *
-----------------------------------------------------------------------------*/
 /**
 *
 */
    static long isStraightFlush(int lowCard){
	long score = 0;
	score = (lowCard + poker.FOUR_KIND_BASE + poker.FULL_HOUSE_BASE + 
                poker.FLUSH_BASE + poker.STRAIGHT_BASE + poker.THREE_KIND_BASE 
                    + poker.TWO_PAIR_BASE + poker.PAIR_BASE 
                        + poker.STRAIGHT_FLUSH_BASE); 
	return score; 
    }
    
/**                     isThreeKind
 * Calculates the score of a three-of-a-kind hand. Starts with the highest
 * possible score of a 2-pair hand.
 * Highest hand: 3K + Q + J
 * 
 * @param   hCard   Three of a kind card value
 * @param   nCard   high single card
 * @param   lCard   low single card
 * @return 
 * @see     isHighCard
*
*/
    static long isThreeKind(int hCard, int nCard, int lCard){
	long score = 0;
	/* See function description for derivation of these numbers */
	score = ((hCard * 169) + 169) + ((nCard * 13) + 12) + lCard;
	score = (score + poker.PAIR_BASE + poker.TWO_PAIR_BASE + 
                poker.THREE_KIND_BASE);
	return score; 
    }
    
/**                        isTwoPair
 * Calculates the score of a hand with two pairs.
 * High hand: 2K + 2Q + J
 * low card: 1-11
 * next card: 12 cards: 11 + 2(12 * 13) = 323
 * high card: 12 cards: 323 + 2(13 * 13 * 13) = 4406
 * base: 463,439
 * 
 * @param highCard  the high pair
 * @param nextCard  the low pair
 * @param lowCard   the single card
*
* @see  isHighCard
* @see  isPair
*/
    static long isTwoPair(int highCard, int nextCard, int lowCard){
	long score = 0;
	/* numbers as derived in function description */
	score = (((highCard * 169) + 169) + ((nextCard * 12) + 12)
		+ lowCard);
	score = (score + poker.PAIR_BASE + poker.TWO_PAIR_BASE);
	return score; 
    }
        
/*----------------- setMostCount -----------------------------------------------
 * function setMostCount(Int[]) 
 *
 * Purpose: Stores the values for the most commonly occurring rank or ranks
 *      that were obtained in the sortHand function.
 *
 * @param - Int[] mostCount - the two most-occurring ranks in the hand.
 *      anything past that will be a 1, and both of these values may also be 1
 *
 * @return - void - stores the values for the getMostCount function, called
 *      from getScore
 *
------------------------------------------------------------------------------*/
 /**
 *
 */
    static void setMostCount(int[] mostCount){
        mCount = mostCount;
    }
    
/**                        sortHand
 * Arranges a Hand of Card objects in order from lowest to highest value. 
 * Groupings, such as pairs, are set at the high position, even if their face
 * value is lower than those of any of the other cards. Multiple instances of
 * groupings, as in two pairs or a full house, are themselves arranged in
 * ascending order. 
 * <p>
 * The function iterates through the hand to determine the order of values and
 * whether any cards appear multiple times. The array mostCount tracks 
 * multiple appearances of up to 2 cards (more than 2 is impossible in a 5-card
 * hand). 
 * 
 * @param uHand    the Hand object to be processed
 * @return         the Hand in sorted order
 *
 */
    static poker.Hand sortHand(poker.Hand uHand){
	int most = ZERO;
	int nextMost = ZERO;
	int temp = ZERO;
        int temp2 = ZERO;
        int tempCount = ZERO;

	int highCard = ZERO;
	int nextCard = ZERO;
	int thirdCard = ZERO;
	int fourthCard = ZERO;
	int lowCard = DECK_SIZE;
        
        int highCount = ZERO; 
        int nextCount = ZERO;
        int thirdCount = ZERO;
        int fourthCount = ZERO; 
        int lowCount = ZERO;
        int suitCount = ZERO;
        
        int[] mostCount = new int[]{most, nextMost};
    // Case: new card is same rank as existing high card
	for (int index = poker.CARD_INIT; index <= poker.CPH; index++){
            if (uHand.hand[index].rank == highCard){
                highCount++;                    
                if(thirdCard > ZERO){
                    fourthCard = thirdCard;
                    fourthCount = thirdCount;
                }
                if(nextCard > ZERO){
                    thirdCard = nextCard;
                    thirdCount = nextCount;
                }
                nextCard = uHand.hand[index].rank;
                nextCount = 0;
            }  
            else{
                if (uHand.hand[index].rank > highCard){
        // Case: first card read in is the first high card
                    if(index == 1){
                        highCard = uHand.hand[index].rank;
                        highCount++;
                    }
                    else{
        // Case: new card is higher than existing high card 
                       // if(highCard == lowCard){
            //Case: Two or more very low cards have been read in
                            
                            
                        //}
                        if(lowCard == poker.DECK_SIZE){
                            /* if there's a high card
        * 					but not a low card */ 
                            lowCard = highCard;
                            lowCount = highCount;
                        }
                        else{
                            /* add the new high card and push any
        * 					existing cards down the ranks */
                            // Safe: If there were already a fourth card, we
                            // wouldn't still be reading in new cards
                            if(thirdCard > ZERO){
                                fourthCard = thirdCard;
                                fourthCount = thirdCount;
                            }
                            if(nextCard > ZERO){
                                thirdCard = nextCard;
                                thirdCount = nextCount;
                            }
                            nextCard = highCard;
                            nextCount = highCount;
                        }
                        highCard = uHand.hand[index].rank;
                        highCount = CARD_INIT;
                    }
                }
                else 
    // Cases: new card matches one already read
            {      
                if (uHand.hand[index].rank == lowCard){
                    if(fourthCard != ZERO){
                        if(thirdCard == fourthCard){
                            nextCard = thirdCard;
                        }
                        thirdCard = fourthCard;
                    }
                    lowCount++; 
                    fourthCard = uHand.hand[index].rank;
                    //mostCount = getMost(uHand.hand[index].rank, lowCount, 
                    //        most, nextMost); 
                }    
                else
                    if (uHand.hand[index].rank == thirdCard){
                        thirdCount++; 
                        fourthCard = uHand.hand[index].rank;
                        //mostCount = getMost(uHand.hand[index].rank, thirdCount, 
                        //        most, nextMost); 
                    }
                else          
                    if (uHand.hand[index].rank == nextCard){
                        nextCount++; 
                        if(thirdCard != ZERO){
                            fourthCard = thirdCard;
                            //fourthCount = thirdCount;
                        }
                        //tempCount = thirdCount;
                        //thirdCard = nextCard;
                        //thirdCount = tempCount;
                        thirdCard = uHand.hand[index].rank;
                    }
                else
                if (uHand.hand[index].rank < lowCard){
    // Case: second card read in has to be either high card or low card
    // don't tie it to index- could get 4-of-a-kind as 1st 4 cards
                    if(uHand.hand[index].rank == highCard){
                        if(nextCard == ZERO){
                            nextCard = uHand.hand[index].rank;
                        }
                        else
                            if(thirdCard == ZERO){
                                thirdCard = uHand.hand[index].rank;
                            }
                        else
                            if(fourthCard == ZERO){
                                fourthCard = uHand.hand[index].rank;
                            }
                        highCount++;
                        //mostCount = getMost(uHand.hand[index].rank, highCount, 
                        //        most, nextMost);
                    }
                    else{
    // Case: new card read in is lower than the existing low card
                            if ((lowCount > 0) || (lowCard != poker.DECK_SIZE)){
                                /* if there's already a low card:
                                            move it up the line */
                                if (nextCard == ZERO){  
                                    nextCount = lowCount;
                                    nextCard = lowCard;	
                                }
                                else
                                if ((thirdCard == ZERO)
                                            && (nextCard > ZERO)){
                                    thirdCount = lowCount;
                                    thirdCard = lowCard;
                                }
                                else
                                if ((fourthCard == ZERO)
                                                && (thirdCard > ZERO)){
                                    fourthCount = lowCount;
                                    fourthCard = lowCard;
                                }
                            }
                            lowCard = uHand.hand[index].rank;
                            lowCount = CARD_INIT;
                            //mostCount = getMost(uHand.hand[index].rank, 
                             ////       lowCount, most, nextMost);
                        }
                    }
                    else
                        if ((uHand.hand[index].rank > nextCard)
                            && (index > 2)
                                && (uHand.hand[index].rank < highCard)){
    // Case: new card is higher than the existing second-highest card
                            if(thirdCard > ZERO){
                                fourthCard = thirdCard;
                                fourthCount = thirdCount;
                            }
                        thirdCard = nextCard;
                        thirdCount = nextCount;
                        nextCard = uHand.hand[index].rank;
                        nextCount = CARD_INIT;	
                        //mostCount = getMost(uHand.hand[index].rank, nextCount, 
                        //        most, nextMost); 
                        }
                else
                    if ((uHand.hand[index].rank > thirdCard)
                            && (index > 2)
                                    && (uHand.hand[index].rank < nextCard)){
    // Case: new card is lower than existing second but higher than third card
                        if(fourthCard == ZERO){
                            fourthCard = thirdCard;
                            fourthCount = thirdCount;
                        }
                        thirdCard = uHand.hand[index].rank;
                        thirdCount = CARD_INIT;
                        //mostCount = getMost(uHand.hand[index].rank, thirdCount, 
                        //        most, nextMost); 
                    } 
                else
    // Should only happen if there is no existing fourth card                    
                    if ((uHand.hand[index].rank > fourthCard)
                            && (uHand.hand[index].rank < thirdCard)
                                && (uHand.hand[index].rank > lowCard)
                                    && (index > 2)){
                        fourthCard = uHand.hand[index].rank;
                        fourthCount = CARD_INIT;
                        //mostCount = getMost(uHand.hand[index].rank, 
                        //        fourthCount, most, nextMost); 
                    }
                
                }
                }
        }
        int[] counts = new int[]{highCount, nextCount, thirdCount, fourthCount, 
            lowCount};
        mostCount = getMost(counts);
        most = mostCount[0];
        nextMost = mostCount[1];
        /*
        System.out.print("most: " + most + " next most: " + nextMost);
        System.out.println("High: " + highCard + " next: " + nextCard
            + " low: " + lowCard);
        */
	/* Reorder the cards by groupings */
	/* First find which occurs most & 2nd most */
	/* A five-card hand allows at most two groupings of more than one card*/
	
	/* Now reorder their positions */
	if((most == 2) && (nextMost == 2)){
            /* hand has two pairs */
            if(highCount != most){
                /* other two values are the pairs */
                if(nextCount == most){
                        temp = highCard;
                        tempCount = highCount;
                        highCard = nextCard;
                        highCount = most;
                        nextCard = thirdCard;
                        nextCount = nextMost;
                        thirdCard = fourthCard;
                        fourthCard = lowCard;
                        lowCount = tempCount;
                        lowCard = temp;
                }
            }
            // If highCount = most and nextCount = nextMost, it's already sorted
            else
            if((highCount == most) && (thirdCount != nextMost)){
                    temp = thirdCard;
                    tempCount = thirdCount;
                    thirdCard = fourthCard;
                    nextCount = nextMost;
                    fourthCard = lowCard;
                    lowCount = tempCount;
                    lowCard = temp;		
            }
	}
	else 
	if(most == 4){
            if(highCount != most){
                temp = highCard;
                highCard = lowCard;
                lowCount = highCount;
                highCount = most;
                lowCard = temp;
            }
	}
	else
	if(most == 3){
            if(nextMost == 2){
            /* full house */
                if(highCount != most){
                    /* low card is the three of a kind,
* 					switch high and low */
                    temp = highCard;
                    temp2 = nextCard;
                    highCard = lowCard;
                    //lowCount = highCount;
                    highCount = most;
                    nextCard = fourthCard;
                    nextCount = nextMost;
                    fourthCard = temp;
                    lowCard = temp2;
                }
            }
            else
            if(highCount != most){
                /* if high card is most
* 				then hand is already in order */
                if(nextCount != most){
                    /* low card is most */
                    /* reorder low-high-next */
                    temp = highCard;
                    temp2 = nextCard;
                    tempCount = highCount;
                    highCard = lowCard;
                    highCount = most;
                    lowCard = temp2;
                    lowCount = tempCount;
                    nextCard = fourthCard;
                    nextCount = nextMost;
                    fourthCard = temp;	
                }
                else {
                    /* next card is most */
                    /* reorder next-high-low */
                    temp = highCard;
                    tempCount = highCount;
                    highCount = most;
                    highCard = nextCard;
                    nextCard = thirdCard;
                    thirdCard = fourthCard;
                    thirdCount = most;
                    fourthCard = temp;
                    fourthCount = nextMost;
                }
            }
	}
	else
	if((most == 2) && (nextMost == 1)){
            /* One pair */
            /* Make pair the high card, others follow in order */
            if(highCount != most){
                /* Nothing to do if high card is the pair */
                if(nextCount == most){
                    temp = highCard;
                    tempCount = highCount;
                    highCard = nextCard;
                    highCount = most;
                    nextCount = tempCount;
                    nextCard = thirdCard;
                    thirdCard = temp;
                }
                else
                if(thirdCount == most){
                    temp = highCard;
                    temp2 = nextCard;
                    tempCount = highCount;
                    highCard = thirdCard;
                    highCount = most;
                    nextCard = fourthCard;
                    nextCount = nextMost;
                    thirdCount = tempCount;
                    thirdCard = temp;
                    fourthCard = temp2;
                }
                else
                if(lowCount == most){
                    temp = highCard;
                    temp2 = nextCard;
                    int temp3 = thirdCard;
                    tempCount = highCount;
                    highCard = lowCard;
                    highCount = most;
                    nextCard = fourthCard;
                    nextCount = nextMost;
                    lowCount = thirdCount;
                    thirdCount = tempCount;
                    lowCard = temp3;
                    fourthCard = temp2;
                    thirdCard = temp;
                }
            }
	}
        int[] sorted = new int[]{ZERO, lowCard, fourthCard, thirdCard, nextCard,
            highCard};
        
        setMostCount(mostCount);
        
        return getSortedHand(sorted, uHand);

        //return sHand;
    }
}
