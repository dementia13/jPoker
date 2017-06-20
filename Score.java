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
                        
/*----------------- getMost ----------------------------------------------------
 * function getMost(int, int, int)
 *
 * Purpose: Tracks when highest or second-highest card count changes 
 *
 * @param int(changed) - the new card
 * 	  int *most - the high count
 * 	  int *n_most - the second high count
 *
 * @return int[] - the three most common ranks in descending order. Given 5
 *      cards, there can be at most two ranks that appear in multiple, along 
 *      with a possible third single card
 *
------------------------------------------------------------------------------*/
    static int[] getMost(int changed, int most, int nextMost){
        if (changed > most){
            nextMost = most;
            most = changed;
        }
        else
        if (changed > nextMost){
            nextMost = changed;
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
    static int[]getMostCount(){
        return mCount;
    }

/*----------------- getScore ---------------------------------------------------
 * function getScore(Hand)
 *
 * Purpose: Assigns a score to each hand. Score is assigned based on 
 * 	information at website
 * 	https://www.pokerstars.com/poker/games/rules/hand-rankings/ 
 *
 *	Rankings in this order;
 *	Straight flush
 *	Four of a kind
 *	Full house
 *	Flush
 *	Straight
 *	Three of a kind
 *	Two pair
 *	One pair
 *	High card
 *
 *	Each type of hand starts with a base score. This base score is higher 
 *      than the highest possible score of the next lower rank. The face
 *	value of the hand is added to this base, and the total is returned.
 *
 *	Cards that aren't part of a winning grouping (for example, the other 
 *      two cards in a three-of-a-kind) are factored in as tiebreakers. 
 *      This is done in such a way that values can't overlap. For example, 
 *      two eights, two twos and a King cannot outweigh two eights, two
 *      threes and a two. Each card or grouping has its own base score that is 
 *      guaranteed to outrank any combination of lower-valued cards.
 *	This causes surprisingly high score values, especially in the
 *	lower-value hands, but it ranks hands accurately.
 *
 *      This function is unavoidably long because it has to sort through the
 *      nine possible ranks and the number of combinations possible within
 *      each. Separate functions analyze the hand to determine what kind it
 *      is, and the result is sent to the appropriate function to calculate the
 *      score.
 *
 *      Each hand is passed through this function in turn. A set of integers
 *      track the highest through lowest face values present, as well as 
 *      whether all the cards are of the same suit.
 *
 * @param - Hand - the Hand being scored 
 *
 * @return long score - the score value of the hand 
 *
-----------------------------------------------------------------------------*/
static long getScore(poker.Hand aHand){
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
			return isFourKind(highCard, lowCard);
		case 3:
			if(lowCount == 2){
				return isFullHouse(highCard, lowCard);
			}
			else
			return isThreeKind(highCard, nextCard, lowCard);
		case 2:
			if(nextCount == 2){
				return isTwoPair(highCard, 
					highCard, lowCard);
			}
			else
			return isPair(highCard, nextCard, thirdCard,
					lowCard);	
		case 1:
			if (hasStraight(sorted) == true){
                            if (suitCount == poker.CPH){
                                return isStraightFlush(lowCard);
                            }
                                return isStraight(lowCard);
                        }
			else
			if (suitCount == poker.CPH){
                            aHand = isFlush(aHand);
                            return aHand.score;
			}
			else
			return isHighCard(sorted);
		default:
			System.out.println("logic error in getScore function");
	}
	return score;
    }       

    
/*----------------- getSortedHand ----------------------------------------------
 * function getSort(int[], Hand) 
 *
 * Purpose: Sorts the cards in ascending order. 
 *
 * @param - Hand uHand - the unordered hand 
 * @param - int[] sorted - the card values, high to low
 *
 * @return - Hand sHand - the sorted hand of cards 
 *
-----------------------------------------------------------------------------*/
    static poker.Hand getSortedHand(int[] sorted, poker.Hand uHand){
        poker.Hand sHand = new poker.Hand();
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
        return sHand;
    }

    
    // -- "HAS" FUNCTIONS - DETECT PRESENCE OF STRAIGHT OR FLUSH
    
/*----------------- hasFlush -------------------------------------------------
 * function hasFlush(int)  
 *
 * Purpose: Marks when flush is detected. Does little: the int suitCount in
 *  the calling function, getScore, is passed in. If suitCount is the same as
 *  the number of cards in a hand, then they are all the same suit & is a flush
 *
 * @param - int suitCount
 *
 * @return - true or false
 *
-----------------------------------------------------------------------------*/
    /*
    static boolean hasFlush(int suitCount){
	if (suitCount == poker.CPH){
		return true;
	}
	else
            return false;
    }
    */
    
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
    static boolean hasStraight(int sorted[]){
	int count;
	int temp;
	if ((sorted[1] == 1) && (sorted[2] == 10)){
		temp = poker.NUM_RANKS + poker.CARD_INIT;  
		sorted[1] = sorted[2];
		sorted[2] = sorted[3];
		sorted[3] = sorted[4];
		sorted[4] = sorted[5];
		sorted[5] = temp;
	}
        // returns false if any card is not one more than the previous
	for (count = poker.CARD_INIT; count < sorted[poker.CPH]; count++){
		if ((sorted[count + poker.CARD_INIT] - sorted[count]) != 1){
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
 * 	high score of 200,616.
 * 	Highest possible 1-pair hand: 2K - Q - J - 10
 * 	low card: 1-10
 * 	third card: 11 cards: 11 + (11 * 13) = 154 
 * 	next card: 12 cards: 154 + (12 * 169) = 2182
 * 	high card: 13 cards: 2182 + (13 * 13 * 13 * 13) = 30743
 * 	Adding all gives two pair base: 33089
 *
 * @param - Hand - the Hand to be tested 
long isPair(int high_card, int next_card, int *third_card, int *low_card
 *
 * @return - long - base value of a pair plus the suit value of the pair and
 *      weighted value of single cards in hand
 *
-----------------------------------------------------------------------------*/
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
    static long isStraightFlush(int lowCard){
	long score = 0;
	score = (lowCard + poker.FOUR_KIND_BASE + poker.FULL_HOUSE_BASE + 
                poker.FLUSH_BASE + poker.STRAIGHT_BASE + poker.THREE_KIND_BASE 
                    + poker.TWO_PAIR_BASE + poker.PAIR_BASE 
                        + poker.STRAIGHT_FLUSH_BASE); 
	return score; 
    }
    
/*----------------- isThreeKind ----------------------------------------------
 * function isThreeKind(int hCard, int nCard, int lCard)  
 *
 * Purpose: returns the score of a hand containing a three-of-a-kind 
 *
 * @param - int - the rank value of the three-of-a-kind
 * 	and two tiebreaker cards 
 * 	See isHighCard for further details
 * 	Highest possible hand: 3K + Q + J
 * 	low card: 1-11
 * 	next card: 12 faces: 12 + 12 * 13 = 168
 *	high card: 13 faces: 169 + 13 * 13 * 13 = 2366 
 *	Adding all gives straight base: 2545
 *
 * @return - long - the score of the hand above the highest possible scores
 * 	of all lower hands 
 * 	200,636 + 22,330 + 1885 = 224851
 *
-----------------------------------------------------------------------------*/
    static long isThreeKind(int hCard, int nCard, int lCard){
	long score = 0;
	/* See function description for derivation of these numbers */
	score = ((hCard * 169) + 169) + ((nCard * 13) + 12) + lCard;
	score = (score + poker.PAIR_BASE + poker.TWO_PAIR_BASE + 
                poker.THREE_KIND_BASE);
	return score; 
    }
    
/*----------------- isTwoPair ----------------------------------------------
 * function isTwoPair(Hand)  
 *
 * Purpose: calculates the score of a hand with two pairs.
 * 	    See isHighCard and isPair for details on the scoring.	 
 * 	    Highest possible hand: 2K + 2Q + J
 * 	    low card: 1-11
 * 	    next card: 12 cards: 12 + 12 * 13 = 168
 * 	    high card: 13 cards: 169 + 13 * 13 * 13 = 2366
 * 	    Adding all gives Three of a kind base: 2545
 *
 * @param - int highCard - higher of the two pairs
 * 	    int nextCard - lower of the two pairs
 * 	    int lowCard - tiebreaker card 
 *
 * @return - long - score for the hand + highest possible score for
 *	    a single-pair hand + highest score for a high-card hand,
 *	    200,636 + 22,330 = 222,966 
 *
-----------------------------------------------------------------------------*/
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
    static void setMostCount(int[] mostCount){
        mCount = mostCount;
    }
    
/*----------------- sortHand ---------------------------------------------------
 * function sortHand(Card[]) 
 *
 * Purpose: Sorts the cards in order. 
    - Iterates the hand to see which has the highest value
    - Keeps a running total of any ranks that appear multiple times
    - Cards are sorted into order of multiple ranks first, and then by 
    -   descending rank value
    - function getMost tracks multiple appearances and stores them in
    -   int array mostCount
 *
 * @param - Hand uHand - the unordered hand 
 *
 * @return - Hand - returns the hand sorted in ascending order
 *
-----------------------------------------------------------------------------*/
    static poker.Hand sortHand(poker.Hand uHand){
	//int rank_count = poker.ZERO;
	int most = poker.ZERO;
	int nextMost = poker.ZERO;
	int temp = poker.ZERO;
        int temp2 = ZERO;
        int tempCount = ZERO;

	int highCard = poker.ZERO;
	int nextCard = poker.ZERO;
	int thirdCard = poker.ZERO;
	int fourthCard = poker.ZERO;
	int lowCard = poker.DECK_SIZE;
        
        int highCount = poker.ZERO; 
        int nextCount = poker.ZERO;
        int thirdCount = poker.ZERO;
        int fourthCount = poker.ZERO; 
        int lowCount = poker.ZERO;
        int suitCount = poker.ZERO;
        
        //System.out.println("sortHand: before");
        //poker.textDisplayHand(uHand);
        
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
                mostCount = getMost(highCount, most, nextMost); 
            }  
            else{
                if (uHand.hand[index].rank > highCard){
        // Case: first card read in is the first high card
                    if(index == 1){
                        highCard = uHand.hand[index].rank;
                        highCount++;
                        mostCount = getMost(highCount, most, nextMost);
                    }
                    else{
    // Case: new card is higher than existing high card 
                        if(lowCard == poker.DECK_SIZE){
                            /* if there's a high card
        * 					but not a low card */ 
                            lowCard = highCard;
                            lowCount = highCount;
                            mostCount = getMost(lowCount, most, nextMost);
                        }
                        else{
                            /* add the new high card and push any
        * 					existing cards down the ranks */
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
                        highCount = poker.CARD_INIT;
                        mostCount = getMost(highCount, most, nextMost); 
                    }
                }
                else 
    // Cases: new card matches one already read
            {                
                if (uHand.hand[index].rank == nextCard){
                    tempCount = nextCount;
                    if(thirdCard != ZERO){
                        fourthCard = thirdCard;
                        fourthCount = thirdCount;
                    }
                    nextCount++; 
                    thirdCard = nextCard;
                    thirdCount = tempCount;
                    nextCard = uHand.hand[index].rank;
                    mostCount = getMost(nextCount, most, nextMost); 
                }
                else
                    if (uHand.hand[index].rank == thirdCard){
                        thirdCount++; 
                        fourthCard = uHand.hand[index].rank;
                        mostCount = getMost(thirdCount, most, nextMost); 
                    }
                else
                    if (uHand.hand[index].rank == lowCard){
                        if(fourthCard != ZERO){
                            if(thirdCard == fourthCard){
                                nextCard = thirdCard;
                            }
                            thirdCard = fourthCard;
                        }
                        lowCount++; 
                        fourthCard = uHand.hand[index].rank;
                        mostCount = getMost(lowCount, most, nextMost); 
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
                        mostCount = getMost(highCount, most, nextMost);
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
                                                && (thirdCount > ZERO)){
                                    fourthCount = lowCount;
                                    fourthCard = lowCard;
                                }
                            }
                            lowCard = uHand.hand[index].rank;
                            lowCount = poker.CARD_INIT;
                            mostCount = getMost(lowCount, most, nextMost);
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
                        nextCount = poker.CARD_INIT;	
                        mostCount = getMost(nextCount, most, nextMost); 
                        }
                else
                    if ((uHand.hand[index].rank > thirdCard)
                            && (index > 2)
                                    && (uHand.hand[index].rank < nextCard)){
    // Case: new card is lower than existing second but higher than third card
                        fourthCard = thirdCard;
                        fourthCount = thirdCount;
                        thirdCard = uHand.hand[index].rank;
                        thirdCount = poker.CARD_INIT;
                        mostCount = getMost(thirdCount, most, nextMost); 
                    } 
                else
    // Should only happen if there is no existing fourth card                    
                    if ((uHand.hand[index].rank > fourthCard)
                            && (uHand.hand[index].rank < thirdCard)
                                && (uHand.hand[index].rank > lowCard)
                                    && (index > 2)){
                        fourthCard = uHand.hand[index].rank;
                        fourthCount = poker.CARD_INIT;
                        mostCount = getMost(fourthCount, most, nextMost); 
                    }
                
                }
                }
            most = mostCount[0];
            nextMost = mostCount[1];
        }
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
                    lowCount = highCount;
                    highCount = most;
                    nextCard = fourthCard;
                    nextCount = nextMost;
                    fourthCard = temp2;
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
                else 
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
                    tempCount = highCount;
                    highCard = lowCard;
                    highCount = most;
                    nextCard = fourthCard;
                    nextCount = nextMost;
                    thirdCount = tempCount;
                    lowCount = thirdCount;
                    lowCard = thirdCard;
                    fourthCard = temp2;
                    thirdCard = temp;
                }
            }
	}
        int[] sorted = new int[]{ZERO, lowCard, fourthCard, thirdCard, nextCard,
            highCard};
        
        setMostCount(mostCount);
        
        poker.Hand sHand = getSortedHand(sorted, uHand);

        return sHand;
    }
}
