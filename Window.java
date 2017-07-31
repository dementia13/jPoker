/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import static poker.Score.getTie;
import static poker.poker.CARD_INIT;
import poker.poker.Card;
import static poker.poker.DECK_SIZE;
import poker.poker.Deck;
import poker.poker.Hand;
import static poker.poker.NUM_HANDS;
import static poker.poker.NUM_RANKS;
import static poker.poker.createDeck;
import static poker.poker.getWinner;

/**
 *
 * @author Sean Quinn
 */
public class Window extends javax.swing.JFrame {     
    
    boolean clicked = true;
    boolean created = false;
    boolean displayed = false;
    boolean shuffled = false;
    
    Dimension hpDim = new Dimension(500, 165);
    Dimension jfDim = new Dimension(1200, 700);
    Font bFont = new Font("Lucida Grande", Font.BOLD, 14);
    Color bColor = new Color(0, 121, 0);
    Color tColor = new Color(255, 255, 204);
    
    static Deck createdDeck = new Deck();
    static Deck shuffledDeck = new Deck();
    static Hand[] allHands = new Hand[poker.NUM_HANDS + poker.CARD_INIT];
    
    static Image table = new ImageIcon("src/poker/resources/table.png")
        .getImage().getScaledInstance(60, 100, Image.SCALE_SMOOTH);
    
// --- UI COMPONENTS -----------------------------------------------------------    
        
    
/**                     cardPanel
 * A panel for display of a single Card object.
 *
 * @see handPanel 
 */
    class cardPanel extends JLabel{
        cardPanel(){
            JLabel cPanel = new JLabel();
            cPanel.setPreferredSize(new Dimension(75, 125));
            cPanel.setOpaque(true);
            cPanel.setEnabled(true);
            cPanel.setBackground(bColor);
            cPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }
    
    JTextArea dTField1 = new JTextArea();
    JTextArea dTField2 = new JTextArea();
    JTextArea dTField3 = new JTextArea();
    JTextArea dTField4 = new JTextArea();
    
/**                     handPanel
 * Panel for display of a card hand. Includes labels for the hand ID# and type, 
 * and a panel for display of each card icon.
 *
 * @see cardPanel
 */
    class handPanel extends JPanel{
        
        class hPanel extends JPanel{
            hPanel(){
                JPanel hPan = new JPanel();
                hPan.setPreferredSize(new Dimension(500,200));
                hPan.setBackground(bColor);
                hPan.setLayout(new GridBagLayout());
                
            }
        }
        JLabel hLab = new JLabel("");
        JLabel tLab = new JLabel("");
        JPanel cPan = new JPanel();
        JPanel textPanel = new JPanel(new GridBagLayout());

        cardPanel cPanel1 = new cardPanel();
        cardPanel cPanel2 = new cardPanel();
        cardPanel cPanel3 = new cardPanel();
        cardPanel cPanel4 = new cardPanel();
        cardPanel cPanel5 = new cardPanel();
        
        GridBagConstraints hlC = new GridBagConstraints();
        GridBagConstraints tlC = new GridBagConstraints();
        GridBagConstraints cP = new GridBagConstraints();
        GridBagConstraints tpL = new GridBagConstraints();
        GridBagConstraints hpL = new GridBagConstraints();

        public handPanel(){
            // Main panel
            hPanel hPan = new hPanel();
            // Panel where cards sit
            cPan.setPreferredSize(new Dimension(405, 180));
            cPan.setBackground(bColor);
            cPan.setLayout(new FlowLayout(FlowLayout.LEADING, 6, 6));

            // Add card holders to panel
            cPan.add(cPanel1);
            cPan.add(cPanel2);
            cPan.add(cPanel3);
            cPan.add(cPanel4);
            cPan.add(cPanel5);

            hLab.setFont(bFont);
            hLab.setBackground(bColor);
            hLab.setForeground(tColor);
            hLab.setPreferredSize(new Dimension (100, 20));
            tLab.setFont(bFont);
            tLab.setBackground(bColor);
            tLab.setForeground(tColor);
            tLab.setPreferredSize(new Dimension(100, 20));
            hLab.setOpaque(true);
            tLab.setOpaque(true);
            
            hpL.anchor = GridBagConstraints.FIRST_LINE_START;
            hpL.gridx = 0;
            hpL.gridy = 0;
            tpL.anchor = GridBagConstraints.FIRST_LINE_END;
            tpL.gridx = 0;
            tpL.gridy = 11;

            hlC.fill = GridBagConstraints.HORIZONTAL;
            hlC.ipadx = 5;
            hlC.ipady = 5;
            hlC.gridx = 0;
            hlC.gridy = 0;
            hlC.anchor = GridBagConstraints.FIRST_LINE_START;

            tlC.fill = GridBagConstraints.HORIZONTAL;
            tlC.ipadx = 5;
            tlC.ipady = 5;
            tlC.gridx = 0;
            tlC.gridy = 5;
            tlC.anchor = GridBagConstraints.LAST_LINE_START;

            cP.gridx = 1;
            cP.gridy = 0;
            cP.anchor = GridBagConstraints.FIRST_LINE_END;
            cP.ipadx = 5;

            textPanel.add(hLab, hpL);
            textPanel.add(tLab, tpL);
            hPan.add(textPanel, tlC);
            hPan.add(cPan, cP);
            hPan.setOpaque(true);
            hPan.setEnabled(true);
            hPan.revalidate();
            hPan.repaint();
            hPan.setVisible(true);
        }
    }
    
    /**                     mainPanel
     * A panel for display of the card hands.
     *
     * @see handPanel 
     */
    class mainPanel extends JPanel{
        mainPanel(){
            JPanel mPanel = new JPanel();   
            mPanel.setBackground(bColor);
            this.setLayout(new GridLayout(4,2));
            mPanel.setBorder(null);
            mPanel.setPreferredSize(new Dimension (1200, 500));
        }
    }
        
    handPanel hp1 = new handPanel();
    handPanel hp2 = new handPanel();
    handPanel hp3 = new handPanel();
    handPanel hp4 = new handPanel();
    handPanel hp5 = new handPanel();
    handPanel hp6 = new handPanel();
    handPanel hp7 = new handPanel();
    handPanel hp8 = new handPanel();
        
    JButton bDeal = new JButton();    
    JButton bDeck = new JButton();    
    JButton bHand = new JButton(); 
    JButton bNew = new JButton();
    JButton bShuffle = new JButton();               
    JButton bWinner = new JButton();
    
    
    JFrame mFrame = new JFrame();
    
    JPanel dPanel = new JPanel();
    mainPanel mPanel = new mainPanel();
    
/* -------------------------------------------------------------------------
* Creates new form Window
*/
    public Window() {
        
        mFrame.setEnabled(true);
        mFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mFrame.setPreferredSize(jfDim);
        mPanel.setBackground(bColor);
        
        JLayeredPane basePanel = new JLayeredPane();
        basePanel.setEnabled(true);
        basePanel.setOpaque(true);
        basePanel.setBackground(bColor);
        mFrame.setContentPane(basePanel);
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
        
    // --- CREATING MENU -------------------------------------------------------
        
        JMenuBar menuBar = new JMenuBar();
        JMenu mFile, mPlay, mView, mDemo, mHelp;
        JMenuItem iNew, iQuit;
        JMenuItem iShuffle, iDeal;
        JMenuItem iDeck, iHands, iWinner;
        JMenuItem iScores1, iScores2, iScores3, iStraight, iFlush, iHouse, 
                iFour, iStFlush, iTie, iNull;
        JMenuItem iRules, iShort, iDemos, iGameplay;
        
        mFile = new JMenu("File");
        mFile.setMnemonic(KeyEvent.VK_F);
        mPlay = new JMenu("Play");
        mPlay.setMnemonic(KeyEvent.VK_P);
        mView = new JMenu("View");
        mView.setMnemonic(KeyEvent.VK_V);
        mDemo = new JMenu("Demo");
        mDemo.setMnemonic(KeyEvent.VK_M);
        mHelp = new JMenu("Help");
        mHelp.setMnemonic(KeyEvent.VK_H);
        menuBar.add(mFile);
        menuBar.add(mPlay);
        menuBar.add(mView);
        menuBar.add(mDemo);
        menuBar.add(mHelp);
        
    // --- FILE MENU ITEMS -------------------------------    
        
        iNew = new JMenuItem("New deck");
        iNew.setMnemonic(KeyEvent.VK_N);
        iNew.addActionListener(new iNewAction());
        mFile.add(iNew);
        iQuit = new JMenuItem("Quit");
        iQuit.setMnemonic(KeyEvent.VK_Q);
        iQuit.addActionListener(new exit());
        mFile.add(iQuit);
        
    // --- PLAY MENU ITEMS --------------------------------    
        
        iShuffle = new JMenuItem("Shuffle");
        iShuffle.setMnemonic(KeyEvent.VK_S);
        iShuffle.addActionListener(new iShuffleAction());
        mPlay.add(iShuffle);
        iDeal = new JMenuItem("Deal");
        iDeal.setMnemonic(KeyEvent.VK_D);
        iDeal.addActionListener(new iDealAction());
        mPlay.add(iDeal);
        
    // --- VIEW MENU ITEMS ---------------------------------            
        iDeck = new JMenuItem("Deck");
        iDeck.setMnemonic(KeyEvent.VK_C);
        iDeck.addActionListener(new iDeckAction());
        mView.add(iDeck);
        iHands = new JMenuItem("Hand");
        iHands.setMnemonic(KeyEvent.VK_A);
        iHands.addActionListener(new iHandsAction());
        mView.add(iHands);
        iWinner = new JMenuItem("Winner");
        iWinner.setMnemonic(KeyEvent.VK_W);
        iWinner.addActionListener(new iWinnerAction());
        mView.add(iWinner);
        
    // --- DEMO MENU ITEMS ----------------------------------    
        
        iFlush = new JMenuItem("Flush");
        iFlush.addActionListener(new iFlushAction());
        iFour = new JMenuItem("Four of a Kind");
        iFour.addActionListener(new iFourAction());
        iHouse = new JMenuItem("Full house");
        iHouse.addActionListener(new iHouseAction());
        // This was a testing file
        //iNull = new JMenuItem("NullPtr");
        //iNull.addActionListener(new iNullAction());
        iScores1 = new JMenuItem("Scores1");
        iScores1.addActionListener(new iScores1Action());
        iScores2 = new JMenuItem("Scores2");
        iScores2.addActionListener(new iScores2Action());
        iScores3 = new JMenuItem("Scores3");
        iScores3.addActionListener(new iScores3Action());
        iStFlush = new JMenuItem("Straight flush");
        iStFlush.addActionListener(new iStFlushAction());
        iStraight = new JMenuItem("Straight");
        iStraight.addActionListener(new iStraightAction());
        iTie = new JMenuItem("Tie");
        iTie.addActionListener(new iTieAction());
        //mDemo.add(iNull);
        mDemo.add(iScores1);
        mDemo.add(iScores2);
        mDemo.add(iScores3);
        mDemo.add(iStraight);
        mDemo.add(iFlush);
        mDemo.add(iHouse);
        mDemo.add(iFour);
        mDemo.add(iStFlush);
        mDemo.add(iTie);
    
    // --- HELP MENU ITEMS ----------------------------------    
        
        iRules = new JMenuItem("Rules");
        iRules.setMnemonic(KeyEvent.VK_L);
        iRules.addActionListener(new iRulesAction());
        mHelp.add(iRules);
        iShort = new JMenuItem("Key shortcuts");
        iShort.setMnemonic(KeyEvent.VK_K);
        iShort.addActionListener(new iShortAction());
        mHelp.add(iShort);
        iDemos = new JMenuItem("Demo files");
        iDemos.setMnemonic(KeyEvent.VK_O);
        iDemos.addActionListener(new iDemosAction());
        mHelp.add(iDemos);
        iGameplay = new JMenuItem("Gameplay");
        iGameplay.setMnemonic(KeyEvent.VK_R);
        iGameplay.addActionListener(new iGameplayAction());
        mHelp.add(iGameplay);
        
        // --- SETTING THE BUTTON PANEL ITEMS-----------------------------------
            // --- CREATING PANEL ----------------------------------------------
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(1200, 35));
        buttonPanel.setMaximumSize(new Dimension(1200,35));
        buttonPanel.setBackground(bColor);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBorder(null);
        
        JTextField txtPlayers = new JTextField();
        txtPlayers.setBackground(bColor);
        txtPlayers.setForeground(tColor);
        txtPlayers.setFont(bFont);
        txtPlayers.setBorder(BorderFactory.createLineBorder(tColor));
        txtPlayers.setEditable(false);
        txtPlayers.setText("5");
        txtPlayers.setEnabled(true);
        txtPlayers.setVisible(true);
        
        JTextField labPlayers = new JTextField();
        labPlayers.setBackground(bColor);
        labPlayers.setForeground(tColor);
        labPlayers.setFont(bFont);
        labPlayers.setBorder(null);
        labPlayers.setEditable(false);
        labPlayers.setText("Number of players: ");
        labPlayers.setEnabled(true);
        labPlayers.validate();
        labPlayers.setVisible(true);
        
        bNew.setBackground(tColor);
        bNew.setForeground(bColor);
        bNew.setFont(bFont);
        bNew.setText("New deck");
        bNew.addActionListener(new iNewAction());
        bNew.setEnabled(true);
        bNew.validate();
        bNew.setVisible(true);
        
        bShuffle.setBackground(tColor);
        bShuffle.setForeground(bColor);
        bShuffle.setFont(bFont);
        bShuffle.setText("Shuffle deck");
        bShuffle.addActionListener(new iShuffleAction());
        bShuffle.setEnabled(true);
        bShuffle.validate();
        bShuffle.setVisible(true);
        
        bDeal.setBackground(tColor);
        bDeal.setForeground(bColor);
        bDeal.setFont(bFont);
        bDeal.setText("Deal cards");
        bDeal.addActionListener(new iDealAction());
        bDeal.setEnabled(true);
        bDeal.validate();
        bDeal.setVisible(true);
        
        JTextField labSpacer = new JTextField();
        labSpacer.setBackground(bColor);
        labSpacer.setText("               ");
        labSpacer.setBorder(null);
        labSpacer.validate();
        labSpacer.setVisible(true);
        
        JTextField labShow = new JTextField();
        labShow.setBackground(bColor);
        labShow.setForeground(tColor);
        labShow.setFont(bFont);
        labShow.setBorder(null);
        labShow.setHorizontalAlignment(JTextField.RIGHT);
        labShow.setEditable(false);
        labShow.setText("Show: ");
        labShow.setEnabled(true);
        labShow.validate();
        labShow.setVisible(true);
        
        bDeck.setBackground(tColor);
        bDeck.setForeground(bColor);
        bDeck.setFont(bFont);
        bDeck.setText("Show deck");
        bDeck.addActionListener(new iDeckAction());
        bDeck.setEnabled(true);
        bDeck.validate();
        bDeck.setVisible(true);
        
        bHand.setBackground(tColor);
        bHand.setForeground(bColor);
        bHand.setFont(bFont);
        bHand.setText("Show hand");
        bHand.addActionListener(new iHandsAction());
        bHand.setEnabled(true);
        bHand.validate();
        bHand.setVisible(true);
        
        bWinner.setBackground(tColor);
        bWinner.setForeground(bColor);
        bWinner.setFont(bFont);
        bWinner.setText("Show winner");
        bWinner.addActionListener(new iWinnerAction());
        bWinner.setEnabled(true);
        bWinner.validate();
        bWinner.setVisible(true);
                
        buttonPanel.add(labPlayers);
        buttonPanel.add(txtPlayers);
        buttonPanel.add(bNew);
        buttonPanel.add(bShuffle);
        buttonPanel.add(bDeal);
        buttonPanel.add(labSpacer);
        buttonPanel.add(labShow);
        buttonPanel.add(bDeck);
        buttonPanel.add(bHand);
        buttonPanel.add(bWinner);
        
        GridBagConstraints bpC = new GridBagConstraints();
        bpC.gridx = 0;
        bpC.gridy = 0;
        
    // --- DECK DISPLAY AREA ---------------------------------------------------
            
        dPanel.setBackground(bColor);
        dPanel.setPreferredSize(new Dimension(1200, 235));
        dPanel.setMaximumSize(new Dimension(1200, 250));
        dPanel.setOpaque(true);
        dPanel.setEnabled(true);
        dPanel.setLayout(new FlowLayout());
        dPanel.setBorder(null);
        
        dTField1.setBackground(bColor);
        dTField1.setForeground(tColor);
        dTField1.setPreferredSize(new Dimension(280,  235));
        dTField1.setColumns(1);
        dTField1.setBorder(null);
        dTField1.setEditable(false);
        dTField1.setFont(bFont);
        dTField1.setOpaque(true);
        dTField1.setEnabled(true);
        
        dTField2.setBackground(bColor);
        dTField2.setForeground(tColor);
        dTField2.setPreferredSize(new Dimension(280,  235));
        dTField1.setColumns(1);
        dTField2.setBorder(null);
        dTField2.setEditable(false);
        dTField2.setFont(bFont);
        dTField2.setOpaque(true);
        dTField2.setEnabled(true);
        
        dTField3.setBackground(bColor);
        dTField3.setForeground(tColor);
        dTField3.setPreferredSize(new Dimension(280,  235));
        dTField1.setColumns(1);
        dTField3.setBorder(null);
        dTField3.setEditable(false);
        dTField3.setFont(bFont);
        dTField3.setOpaque(true);
        dTField3.setEnabled(true);
        
        dTField4.setBackground(bColor);
        dTField4.setForeground(tColor);
        dTField4.setPreferredSize(new Dimension(280,  235));
        dTField1.setColumns(1);
        dTField4.setBorder(null);
        dTField4.setEditable(false);
        dTField4.setFont(bFont);
        dTField4.setOpaque(true);
        dTField4.setEnabled(true);
        
        GridBagConstraints dpC = new GridBagConstraints();
        dpC.gridx = 0;
        dpC.gridy = 1;
        
    // --- HAND DISPLAY --------------------------------------------------------
        
        hp1.hLab.setText("Hand 1");
        hp1.hLab.setEnabled(true);
        hp1.setBackground(bColor);
        hp1.setOpaque(true);
        hp1.setEnabled(true);
        hp1.revalidate();
        
        hp2.hLab.setText("Hand 2");
        hp2.hLab.setEnabled(true);
        hp2.setBackground(bColor);
        hp2.setOpaque(true);
        hp2.setEnabled(true);
        hp2.revalidate();
        
        hp3.hLab.setText("Hand 3");
        hp3.hLab.setEnabled(true);
        hp3.setBackground(bColor);
        hp3.setOpaque(true);
        hp3.setEnabled(true);
        hp3.revalidate();
        
        if(NUM_HANDS >= 4){  
            hp4.hLab.setText("Hand 4");
            hp4.hLab.setEnabled(true);
            hp4.setBackground(bColor);
            hp4.setOpaque(true);
            hp4.setEnabled(true);
            hp4.revalidate();
        }
        if(NUM_HANDS >= 5){
            hp5.hLab.setText("Hand 5");
            hp5.hLab.setEnabled(true);
            hp5.setBackground(bColor);
            hp5.setOpaque(true);
            hp5.setEnabled(true);
            hp5.revalidate();
        }
        if(NUM_HANDS >= 6){
            hp6.hLab.setText("Hand 6");
            hp6.hLab.setEnabled(true);
            hp6.setOpaque(true);
            hp6.setEnabled(true);
            hp6.revalidate();
        }
        if(NUM_HANDS >= 7){
            hp7.hLab.setText("Hand 7");
            hp7.hLab.setEnabled(true);
            hp7.setOpaque(true);
            hp7.setEnabled(true);
            hp7.revalidate();
        }
        hp6.setBackground(bColor);
        hp7.setBackground(bColor);
        hp8.setBackground(bColor);
        
        mPanel.add(hp1);
        mPanel.add(hp2);
        mPanel.add(hp3);
        mPanel.add(hp4);
        mPanel.add(hp5);
        mPanel.add(hp6);
        mPanel.add(hp7);
        mPanel.add(hp8);
        
    // --- CREATE MAIN FRAME ---------------------------------------------------
        
        mFrame.setJMenuBar(menuBar);
    
        buttonPanel.setEnabled(true);
        buttonPanel.revalidate();
        buttonPanel.repaint();
        buttonPanel.setVisible(true);
        basePanel.add(buttonPanel);
        basePanel.add(Box.createRigidArea(new Dimension(1200,10)));
        
        dPanel.add(dTField1);
        dPanel.add(dTField2);
        dPanel.add(dTField3);
        dPanel.add(dTField4);
        
        dPanel.revalidate();
        dPanel.repaint();
        dPanel.setVisible(true);
        basePanel.add(dPanel);
        
        basePanel.add(Box.createRigidArea(new Dimension(10,10)));
        
        JScrollPane vScroll = new JScrollPane(mPanel);
        vScroll.setPreferredSize(new Dimension(1200, 200));
        vScroll.setVerticalScrollBarPolicy
            (ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        Box mBox = Box.createHorizontalBox();
        mBox.add(vScroll);
        
        mPanel.setOpaque(true);
        mPanel.setEnabled(true);
        mPanel.revalidate();
        mPanel.repaint();   
        mPanel.setVisible(true);
        vScroll.setBorder(null);
        vScroll.setOpaque(true);
        vScroll.setEnabled(true);
        vScroll.revalidate();
        vScroll.setVisible(true);
        
                
        basePanel.add(vScroll);
        basePanel.add(Box.createRigidArea(new Dimension(1200,10)));
        basePanel.revalidate();
        basePanel.repaint();
        basePanel.setVisible(true);
        
        
        mFrame.revalidate();
        mFrame.repaint();
        mFrame.pack();
        mFrame.setVisible(true);
    }
        
/**                     displayCard
 * Assembles a card name from its rankName and suitName fields
 *
 * @param   null
 * @return  Full name of a card (i.e., "Ace of Spades"
 * @see     getDeckText
 */    
    String displayCard(Card theCard){
        String cardName = theCard.rankName + " of " + theCard.suitName + "\n";
        return cardName;
    }
        
/**                     displayDeck
 * Iterates a card deck and generates a list of all the cards by rank and suit 
 * name.
 *
 * @param   theDeck
 * @return  Deck of cards listed by name in order
 * @see     
 */
    String displayDeck(Deck theDeck){
        String deckString = "";
        for(int i = CARD_INIT; i < DECK_SIZE; i++){
            deckString += theDeck.deck[i].rankName + " of "
                    + theDeck.deck[i].suitName + "\n";
        }
        return deckString;
    }
    
/**                     getCardImage
 * Loads the image associated with a particular card using the cardID, an
 * integer in the range 1-52. The rear of the card is represented by 53.
 *
 * @param   cardID  Integer value of the card, its ID number
 * @return  Image representation of the card
 */
    public static Image getCardImage(int cardID){
        Image cardImage = new ImageIcon("src/cardDisplay/resources/cardRear.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
        switch (cardID) {
            case 1: cardImage = new ImageIcon("src/poker/resources/aceClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 2: cardImage =   new ImageIcon("src/poker/resources/twoClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 3: cardImage =  new ImageIcon("src/poker/resources/threeClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 4: cardImage =  new ImageIcon("src/poker/resources/fourClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 5: cardImage =  new ImageIcon("src/poker/resources/fiveClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 6: cardImage =  new ImageIcon("src/poker/resources/sixClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 7: cardImage =  new ImageIcon("src/poker/resources/sevenClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 8: cardImage =   new ImageIcon("src/poker/resources/eightClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 9: cardImage =  new ImageIcon("src/poker/resources/nineClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break; 
            case 10: cardImage =  new ImageIcon("src/poker/resources/tenClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break; 
            case 11: cardImage =  new ImageIcon("src/poker/resources/jackClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break; 
            case 12: cardImage =  new ImageIcon("src/poker/resources/queenClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 13: cardImage =  new ImageIcon("src/poker/resources/kingClubs.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break; 
            case 14: cardImage =  new ImageIcon("src/poker/resources/aceDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;  
            case 15: cardImage =  new ImageIcon("src/poker/resources/twoDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH);
                        break;
            case 16: cardImage =  new ImageIcon("src/poker/resources/threeDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 17: cardImage =  new ImageIcon("src/poker/resources/fourDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 18: cardImage =  new ImageIcon("src/poker/resources/fiveDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 19: cardImage =  new ImageIcon("src/poker/resources/sixDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 20: cardImage =  new ImageIcon("src/poker/resources/sevenDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 21: cardImage =  new ImageIcon("src/poker/resources/eightDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 22: cardImage =  new ImageIcon("src/poker/resources/nineDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 23: cardImage =  new ImageIcon("src/poker/resources/tenDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 24: cardImage =  new ImageIcon("src/poker/resources/jackDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 25: cardImage =  new ImageIcon("src/poker/resources/queenDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 26: cardImage =   new ImageIcon("src/poker/resources/kingDiamonds.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 27: cardImage =  new ImageIcon("src/poker/resources/aceHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 28: cardImage =   new ImageIcon("src/poker/resources/twoHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 29: cardImage =  new ImageIcon("src/poker/resources/threeHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 30: cardImage =  new ImageIcon("src/poker/resources/fourHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 31: cardImage =  new ImageIcon("src/poker/resources/fiveHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 32: cardImage =  new ImageIcon("src/poker/resources/sixHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 33: cardImage =  new ImageIcon("src/poker/resources/sevenHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 34: cardImage =  new ImageIcon("src/poker/resources/eightHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 35: cardImage =  new ImageIcon("src/poker/resources/nineHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 36: cardImage =  new ImageIcon("src/poker/resources/tenHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 37: cardImage =  new ImageIcon("src/poker/resources/jackHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 38: cardImage =  new ImageIcon("src/poker/resources/queenHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 39: cardImage =  new ImageIcon("src/poker/resources/kingHearts.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 40: cardImage =  new ImageIcon("src/poker/resources/aceSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 41: cardImage =  new ImageIcon("src/poker/resources/twoSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;  
            case 42: cardImage =  new ImageIcon("src/poker/resources/threeSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 43: cardImage =  new ImageIcon("src/poker/resources/fourSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 44: cardImage =  new ImageIcon("src/poker/resources/fiveSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 45: cardImage =  new ImageIcon("src/poker/resources/sixSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 46: cardImage =  new ImageIcon("src/poker/resources/sevenSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 47: cardImage =  new ImageIcon("src/poker/resources/eightSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 48: cardImage =  new ImageIcon("src/poker/resources/nineSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break; 
            case 49: cardImage =  new ImageIcon("src/poker/resources/tenSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 50: cardImage =  new ImageIcon("src/poker/resources/jackSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 51: cardImage =  new ImageIcon("src/poker/resources/queenSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 52: cardImage =  new ImageIcon("src/poker/resources/kingSpades.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
            case 53: cardImage = new ImageIcon("src/poker/resources/cardRear.png")
                        .getImage().getScaledInstance(75,125, Image.SCALE_SMOOTH); 
                        break;
        }
        return cardImage;
    }
        
/**                     getDeckText
 * Called by showDeck to retrieve the names of the Card objects for a single
 * column of display. If the Deck has not been shuffled, this will retrieve all
 * the cards of a single suit. Calls displayCard to construct the card name.
 *
 * @param   start   ID# of first Card to be read
 * @param   finish  ID# of last Card to be read
 * @param   theDeck Deck in current state (shuffled or not)
 * @return          rank and suit of the 13 cards in the given range
 * @see             showDeck
 * @see             displayCard
 */
    String getDeckText(int start, int finish, Deck theDeck){
        String deckText = "";
        for(int i = start; i <= finish; i++){
            deckText += displayCard(theDeck.deck[i]);
        }
        return deckText;
    }
        
/**                     readFile
 * Opens a text file for display in a help window
 *
 * @param   path        string value of path to file to be read
 * @param   encoding    charset for String
 * @return              contents of the file as a String
 * @see                 showDemos
 * @see                 showGameplay
 * @see                 showRules
 * @see                 showKeyCommands
 * 
 */
    String readFile(String path, Charset encoding)throws IOException{
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
        
/**                     showDeck
 * Displays a listing of all cards in the deck. Cards are shown in present 
 * order, which makes this useful for seeing the results of a shuffle.
 * <p>
 * The list is displayed in four columns in a JTextArea below the button panel. 
 * This field does not update dynamically, so if the deck is shuffled while 
 * already displayed, it will need to be hidden and displayed again to see the
 * shuffled cards.
 *
 * @param   null
 * @return  null
 * @see     iDeckAction
 */
    void showDeck(){
        if(created && !displayed){
            Deck theDeck = new Deck();
            if(shuffled){
                theDeck = shuffledDeck;
            }
            else
            if(created){
                theDeck = createdDeck;
            }
            dTField1.setEnabled(true);
            dTField2.setEnabled(true);
            dTField3.setEnabled(true);
            dTField4.setEnabled(true);
            for(int i = CARD_INIT; i < DECK_SIZE; i++){
                if(i <= NUM_RANKS){
                    dTField1.setText(getDeckText(1, 13, theDeck));
                }
                else
                    if((i > NUM_RANKS) && (i <= 2 * NUM_RANKS)){
                        dTField2.setText(getDeckText(14, 26, theDeck));
                    }
                else
                    if((i >  2 * NUM_RANKS) && (i <= 3 * NUM_RANKS)){
                        dTField3.setText(getDeckText(27, 39, theDeck));
                    }
                else
                    if((i > 3 * NUM_RANKS) && (i <= 4 * NUM_RANKS)){
                        dTField4.setText(getDeckText(40, 52, theDeck));
                    }
            }
            displayed = true;
            bDeck.setText("Hide deck");
            dTField1.revalidate();
            dTField2.revalidate();
            dTField3.revalidate();
            dTField4.revalidate();
        }
        else
        if(created && displayed){
            displayed = false;
            bDeck.setText("Show deck");
                dTField1.setText("");
                dTField2.setText("");
                dTField3.setText("");
                dTField4.setText("");
        }
    }
        
/**                     showDemos
 * Dialog window that explains contents of the demonstration files. Loads
 * content from file src/poker/resources/demo.txt
 *
 * @param 
 * @return
 */
    void showDemos(){
        String demoPath = "src/poker/resources/demo.txt";
        JDialog dcFrame = new JDialog(mFrame, "Demos", 
            Dialog.ModalityType.APPLICATION_MODAL);
        JPanel dcPanel = new JPanel(new FlowLayout());
        dcPanel.setBackground(bColor);
        dcPanel.setForeground(tColor);
        dcPanel.setPreferredSize(new Dimension(720, 420));
        dcPanel.setEnabled(true);
        dcPanel.setOpaque(true);
        JTextArea dcText = new JTextArea();
        dcText.setBackground(bColor);
        dcText.setForeground(tColor);
        dcText.setFont(bFont);
        dcText.setBorder(null);
        dcText.setMargin(new Insets(10, 10, 10, 10));
        dcText.setEnabled(true);
        dcText.setPreferredSize(new Dimension(700,400));
        dcText.setOpaque(true);
        try{
            dcText.setText(readFile(demoPath, Charset.defaultCharset()));
        }catch (IOException exc){
            dcText.setText("File not found");
        }
        dcPanel.add(dcText);
        
        dcFrame.setBackground(bColor);
        dcFrame.setContentPane(dcPanel);
        dcFrame.setPreferredSize(new Dimension (720, 420));
        dcFrame.setEnabled(true);
        dcFrame.revalidate();
        dcFrame.pack();
        dcFrame.setVisible(true);
    }
        
/**                     showGameplay
 * Dialog window that displays gameplay instructions. Loads content from file at
 * src/poker/resources/gameplay.txt
 *
 * @param   null
 * @return  null
 */
    void showGameplay(){
        String gameplayPath = "src/poker/resources/gameplay.txt";
        JDialog gcFrame = new JDialog(mFrame, "Gameplay", 
            Dialog.ModalityType.APPLICATION_MODAL);
        JPanel gcPanel = new JPanel(new FlowLayout());
        gcPanel.setBackground(bColor);
        gcPanel.setForeground(tColor);
        gcPanel.setPreferredSize(new Dimension(730, 420));
        gcPanel.setEnabled(true);
        gcPanel.setOpaque(true);
        JTextArea gcText = new JTextArea();
        gcText.setBackground(bColor);
        gcText.setForeground(tColor);
        gcText.setFont(bFont);
        gcText.setBorder(null);
        gcText.setMargin(new Insets(10, 10, 10, 10));
        gcText.setEnabled(true);
        gcText.setPreferredSize(new Dimension(720,400));
        gcText.setOpaque(true);
        try{
            gcText.setText(readFile(gameplayPath, Charset.defaultCharset()));
        }catch (IOException exc){
            gcText.setText("File not found");
        }
        gcPanel.add(gcText);
        
        gcFrame.setBackground(bColor);
        gcFrame.setContentPane(gcPanel);
        gcFrame.setPreferredSize(new Dimension (730, 420));
        gcFrame.setEnabled(true);
        gcFrame.revalidate();
        gcFrame.pack();
        gcFrame.setVisible(true);
    } 

/**                             showHands
 * Displays a handPanel object for each card hand in the game. Changes into a
 * "Hide hands" option when clicked, which shows the reverse side of the cards.
 * 
 * @param   null  
 * @return  null
 * 
 * @see     handPanel
 * @see     iHandsAction
 */
    void showHands(){
        if(clicked){
            JScrollBar vScroll = new JScrollBar();
            hp1.cPanel1.setIcon(new ImageIcon(Window.getCardImage(allHands[1].hand[1].iDNum)));
            hp1.cPanel2.setIcon(new ImageIcon(Window.getCardImage(allHands[1].hand[2].iDNum)));
            hp1.cPanel3.setIcon(new ImageIcon(Window.getCardImage(allHands[1].hand[3].iDNum)));
            hp1.cPanel4.setIcon(new ImageIcon(Window.getCardImage(allHands[1].hand[4].iDNum)));
            hp1.cPanel5.setIcon(new ImageIcon(Window.getCardImage(allHands[1].hand[5].iDNum)));
            hp1.cPan.add(hp1.cPanel1);
            hp1.cPan.add(hp1.cPanel2);
            hp1.cPan.add(hp1.cPanel3);
            hp1.cPan.add(hp1.cPanel4);
            hp1.cPan.add(hp1.cPanel5);
            
            hp1.hLab.setText("Hand 1:\n");
            hp1.tLab.setText(allHands[1].type);
            hp1.textPanel.add(hp1.hLab, hp1.hpL);
            hp1.textPanel.add(hp1.tLab, hp1.tpL);
            hp1.add(hp1.textPanel);
            hp1.add(hp1.cPan);
            hp1.setEnabled(true);
            hp1.revalidate();
            
            hp2.cPanel1.setIcon(new ImageIcon(Window.getCardImage(allHands[2].hand[1].iDNum)));
            hp2.cPanel2.setIcon(new ImageIcon(Window.getCardImage(allHands[2].hand[2].iDNum)));
            hp2.cPanel3.setIcon(new ImageIcon(Window.getCardImage(allHands[2].hand[3].iDNum)));
            hp2.cPanel4.setIcon(new ImageIcon(Window.getCardImage(allHands[2].hand[4].iDNum)));
            hp2.cPanel5.setIcon(new ImageIcon(Window.getCardImage(allHands[2].hand[5].iDNum)));
            hp2.cPan.add(hp2.cPanel1);
            hp2.cPan.add(hp2.cPanel2);
            hp2.cPan.add(hp2.cPanel3);
            hp2.cPan.add(hp2.cPanel4);
            hp2.cPan.add(hp2.cPanel5);
            
            hp2.hLab.setText("Hand 2:\n");
            hp2.tLab.setText(allHands[2].type);
            hp2.textPanel.add(hp2.hLab, hp2.hpL);
            hp2.textPanel.add(hp2.tLab, hp2.tpL);
            hp2.add(hp2.textPanel);
            hp2.add(hp2.cPan);
            hp2.setEnabled(true);
            hp2.revalidate();
            
            hp3.cPanel1.setIcon(new ImageIcon(Window.getCardImage(allHands[3].hand[1].iDNum)));
            hp3.cPanel2.setIcon(new ImageIcon(Window.getCardImage(allHands[3].hand[2].iDNum)));
            hp3.cPanel3.setIcon(new ImageIcon(Window.getCardImage(allHands[3].hand[3].iDNum)));
            hp3.cPanel4.setIcon(new ImageIcon(Window.getCardImage(allHands[3].hand[4].iDNum)));
            hp3.cPanel5.setIcon(new ImageIcon(Window.getCardImage(allHands[3].hand[5].iDNum)));
            hp3.cPan.add(hp3.cPanel1);
            hp3.cPan.add(hp3.cPanel2);
            hp3.cPan.add(hp3.cPanel3);
            hp3.cPan.add(hp3.cPanel4);
            hp3.cPan.add(hp3.cPanel5);
            
            hp3.hLab.setText("Hand 3:\n");
            hp3.tLab.setText(allHands[3].type);
            hp3.textPanel.add(hp3.hLab, hp3.hpL);
            hp3.textPanel.add(hp3.tLab, hp3.tpL);
            hp3.add(hp3.textPanel);
            hp3.add(hp3.cPan);
            hp3.setEnabled(true);
            hp3.revalidate();
            
            if(poker.NUM_HANDS >= 4){
                hp4.cPanel1.setIcon(new ImageIcon(Window.getCardImage(allHands[4].hand[1].iDNum)));
                hp4.cPanel2.setIcon(new ImageIcon(Window.getCardImage(allHands[4].hand[2].iDNum)));
                hp4.cPanel3.setIcon(new ImageIcon(Window.getCardImage(allHands[4].hand[3].iDNum)));
                hp4.cPanel4.setIcon(new ImageIcon(Window.getCardImage(allHands[4].hand[4].iDNum)));
                hp4.cPanel5.setIcon(new ImageIcon(Window.getCardImage(allHands[4].hand[5].iDNum)));
                hp4.cPan.add(hp4.cPanel1);
                hp4.cPan.add(hp4.cPanel2);
                hp4.cPan.add(hp4.cPanel3);
                hp4.cPan.add(hp4.cPanel4);
                hp4.cPan.add(hp4.cPanel5);
            
                hp4.hLab.setText("Hand 4:\n");
                hp4.tLab.setText(allHands[4].type);
                hp4.textPanel.add(hp4.hLab, hp4.hpL);
                hp4.textPanel.add(hp4.tLab, hp4.tpL);
                hp4.add(hp4.textPanel);
                hp4.add(hp4.cPan);
                hp4.setEnabled(true);
                hp4.revalidate();
            }
            
            if(poker.NUM_HANDS >= 5){
                hp5.cPanel1.setIcon(new ImageIcon(Window.getCardImage(allHands[5].hand[1].iDNum)));
                hp5.cPanel2.setIcon(new ImageIcon(Window.getCardImage(allHands[5].hand[2].iDNum)));
                hp5.cPanel3.setIcon(new ImageIcon(Window.getCardImage(allHands[5].hand[3].iDNum)));
                hp5.cPanel4.setIcon(new ImageIcon(Window.getCardImage(allHands[5].hand[4].iDNum)));
                hp5.cPanel5.setIcon(new ImageIcon(Window.getCardImage(allHands[5].hand[5].iDNum)));
                hp5.cPan.add(hp5.cPanel1);
                hp5.cPan.add(hp5.cPanel2);
                hp5.cPan.add(hp5.cPanel3);
                hp5.cPan.add(hp5.cPanel4);
                hp5.cPan.add(hp5.cPanel5);
                
                hp5.hLab.setText("Hand 5:\n");
                hp5.tLab.setText(allHands[5].type);
                hp5.textPanel.add(hp5.hLab, hp5.hpL);
                hp5.textPanel.add(hp5.tLab, hp5.tpL);
                hp5.add(hp5.textPanel);
                hp5.add(hp5.cPan);
                hp5.setEnabled(true);
                hp5.revalidate();
            }
            
            if(poker.NUM_HANDS >= 6){
                hp6.cPanel1.setIcon(new ImageIcon(Window.getCardImage(allHands[6].hand[1].iDNum)));
                hp6.cPanel2.setIcon(new ImageIcon(Window.getCardImage(allHands[6].hand[2].iDNum)));
                hp6.cPanel3.setIcon(new ImageIcon(Window.getCardImage(allHands[6].hand[3].iDNum)));
                hp6.cPanel4.setIcon(new ImageIcon(Window.getCardImage(allHands[6].hand[4].iDNum)));
                hp6.cPanel5.setIcon(new ImageIcon(Window.getCardImage(allHands[6].hand[5].iDNum)));
                hp6.cPan.add(hp6.cPanel1);
                hp6.cPan.add(hp6.cPanel2);
                hp6.cPan.add(hp6.cPanel3);
                hp6.cPan.add(hp6.cPanel4);
                hp6.cPan.add(hp6.cPanel5);
                
                hp6.hLab.setText("Hand 6:\n");
                hp6.tLab.setText(allHands[6].type);
                hp6.textPanel.add(hp6.hLab, hp6.hpL);
                hp6.textPanel.add(hp6.tLab, hp6.tpL);
                hp6.add(hp6.textPanel);
                hp6.add(hp6.cPan);
                hp6.setEnabled(true);
                hp6.revalidate();
            }
            
            if(poker.NUM_HANDS >= 7){
                hp7.cPanel1.setIcon(new ImageIcon(Window.getCardImage(allHands[7].hand[1].iDNum)));
                hp7.cPanel2.setIcon(new ImageIcon(Window.getCardImage(allHands[7].hand[2].iDNum)));
                hp7.cPanel3.setIcon(new ImageIcon(Window.getCardImage(allHands[7].hand[3].iDNum)));
                hp7.cPanel4.setIcon(new ImageIcon(Window.getCardImage(allHands[7].hand[4].iDNum)));
                hp7.cPanel5.setIcon(new ImageIcon(Window.getCardImage(allHands[7].hand[5].iDNum)));
                hp7.cPan.add(hp7.cPanel1);
                hp7.cPan.add(hp7.cPanel2);
                hp7.cPan.add(hp7.cPanel3);
                hp7.cPan.add(hp7.cPanel4);
                hp7.cPan.add(hp7.cPanel5);
                
                hp7.hLab.setText("Hand 7:\n");
                hp7.tLab.setText(allHands[7].type);
                hp7.textPanel.add(hp7.hLab, hp7.hpL);
                hp7.textPanel.add(hp7.tLab, hp7.tpL);
                hp7.add(hp7.textPanel);
                hp7.add(hp7.cPan);
                hp7.setEnabled(true);
                hp7.revalidate();
            }
            
            mPanel.add(hp1);
            mPanel.add(hp2);
            mPanel.add(hp3);
            mPanel.add(hp4);
            mPanel.add(hp5);
            mPanel.add(hp6);
            mPanel.add(hp7);
            mPanel.add(hp8);
            
            hp1.setVisible(true);
            hp2.setVisible(true);
            hp3.setVisible(true);
            hp4.setVisible(true);
            hp5.setVisible(true);
            hp6.setVisible(true);
            hp7.setVisible(true);
            
            
            //mPanel.revalidate();
            //mPanel.repaint();
            //mPanel.setVisible(true);
            
            bHand.setText("Hide hands");
            clicked = false;
        }
        else{       
            showRearHands();
            bHand.setText("Show hands");
            clicked = true;
        }   
    } 
        
/**                         showKeyCommands
 * Opens a help window that displays available keyboard shortcuts. Loads content
 * from file at src/poker/resources/commands.txt
 * 
 * @param   null
 * @return  null
 */
    void showKeyCommands(){
        String commandPath = "src/poker/resources/commands.txt";
        JDialog kcFrame = new JDialog(mFrame, "Key Shortcuts", 
            Dialog.ModalityType.APPLICATION_MODAL);
        JPanel kcPanel = new JPanel(new FlowLayout());
        kcPanel.setBackground(bColor);
        kcPanel.setForeground(tColor);
        kcPanel.setPreferredSize(new Dimension(450, 400));
        kcPanel.setEnabled(true);
        kcPanel.setOpaque(true);
        JTextArea kcText = new JTextArea();
        kcText.setBackground(bColor);
        kcText.setForeground(tColor);
        kcText.setFont(bFont);
        kcText.setBorder(null);
        kcText.setMargin(new Insets(10, 10, 10, 10));
        kcText.setEnabled(true);
        kcText.setPreferredSize(new Dimension(400,400));
        kcText.setOpaque(true);
        try{
            kcText.setText(readFile(commandPath, Charset.defaultCharset()));
        }catch (IOException exc){
            kcText.setText("File not found");
        }
        kcPanel.add(kcText);
        
        kcFrame.setBackground(bColor);
        kcFrame.setContentPane(kcPanel);
        kcFrame.setPreferredSize(new Dimension (420, 400));
        kcFrame.setEnabled(true);
        kcFrame.revalidate();
        kcFrame.pack();
        kcFrame.setVisible(true);
    }  
    
        
/**                             showRearHands
 * Displays a handPanel object for each card hand in the game. Becomes a
 * "Hide hands" option when clicked, which shows the reverse side of the cards.
 * 
 * @param   null  
 * @return  null
 * 
 * @see     handPanel
 * @see     showHands
 */
    void showRearHands(){
            hp1.cPanel1.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp1.cPanel2.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp1.cPanel3.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp1.cPanel4.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp1.cPanel5.setIcon(new ImageIcon(Window.getCardImage(53))); 
            hp1.cPan.add(hp1.cPanel1);
            hp1.cPan.add(hp1.cPanel2);
            hp1.cPan.add(hp1.cPanel3);
            hp1.cPan.add(hp1.cPanel4);
            hp1.cPan.add(hp1.cPanel5);
            
            hp1.hLab.setText("Hand 1:\n");
            hp1.tLab.setText("");
            hp1.add(hp1.textPanel);
            hp1.add(hp1.cPan);
            hp1.setEnabled(true);
            hp1.revalidate();

            hp2.cPanel1.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp2.cPanel2.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp2.cPanel3.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp2.cPanel4.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp2.cPanel5.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp2.cPan.add(hp2.cPanel1);
            hp2.cPan.add(hp2.cPanel2);
            hp2.cPan.add(hp2.cPanel3);
            hp2.cPan.add(hp2.cPanel4);
            hp2.cPan.add(hp2.cPanel5);
            
            hp2.hLab.setText("Hand 2:\n");
            hp2.tLab.setText("");
            hp2.add(hp2.textPanel);
            hp2.add(hp2.cPan);
            hp2.setEnabled(true);
            hp2.revalidate();

            hp3.cPanel1.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp3.cPanel2.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp3.cPanel3.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp3.cPanel4.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp3.cPanel5.setIcon(new ImageIcon(Window.getCardImage(53)));
            hp3.cPan.add(hp3.cPanel1);
            hp3.cPan.add(hp3.cPanel2);
            hp3.cPan.add(hp3.cPanel3);
            hp3.cPan.add(hp3.cPanel4);
            hp3.cPan.add(hp3.cPanel5);
            
            hp3.hLab.setText("Hand 3:\n");
            hp3.tLab.setText("");
            hp3.add(hp3.textPanel);
            hp3.add(hp3.cPan);
            hp3.setEnabled(true);
            hp3.revalidate();

            if(poker.NUM_HANDS >= 4){
                hp4.cPanel1.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp4.cPanel2.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp4.cPanel3.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp4.cPanel4.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp4.cPanel5.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp4.cPan.add(hp4.cPanel1);
                hp4.cPan.add(hp4.cPanel2);
                hp4.cPan.add(hp4.cPanel3);
                hp4.cPan.add(hp4.cPanel4);
                hp4.cPan.add(hp4.cPanel5);
            
                hp4.hLab.setText("Hand 4:\n");
                hp4.tLab.setText("");
                hp4.add(hp4.textPanel);
                hp4.add(hp4.cPan);
                hp4.setEnabled(true);
                hp4.revalidate();
            }

            if(poker.NUM_HANDS >= 5){
                hp5.cPanel1.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp5.cPanel2.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp5.cPanel3.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp5.cPanel4.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp5.cPanel5.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp5.cPan.add(hp5.cPanel1);
                hp5.cPan.add(hp5.cPanel2);
                hp5.cPan.add(hp5.cPanel3);
                hp5.cPan.add(hp5.cPanel4);
                hp5.cPan.add(hp5.cPanel5);
            
                hp5.hLab.setText("Hand 5:\n");
                hp5.tLab.setText("");
                hp5.add(hp5.textPanel);
                hp5.add(hp5.cPan);
                hp5.setEnabled(true);
                hp5.revalidate();
            }

            if(poker.NUM_HANDS >= 6){
                hp6.cPanel1.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp6.cPanel2.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp6.cPanel3.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp6.cPanel4.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp6.cPanel5.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp6.cPan.add(hp6.cPanel1);
                hp6.cPan.add(hp6.cPanel2);
                hp6.cPan.add(hp6.cPanel3);
                hp6.cPan.add(hp6.cPanel4);
                hp6.cPan.add(hp6.cPanel5);
            
                hp6.hLab.setText("Hand 6:\n");
                hp6.tLab.setText("");
                hp6.add(hp6.textPanel);
                hp6.add(hp6.cPan);
                hp6.setEnabled(true);
                hp6.revalidate();
            }

            if(poker.NUM_HANDS >= 7){
                hp7.cPanel1.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp7.cPanel2.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp7.cPanel3.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp7.cPanel4.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp7.cPanel5.setIcon(new ImageIcon(Window.getCardImage(53)));
                hp7.cPan.add(hp7.cPanel1);
                hp7.cPan.add(hp7.cPanel2);
                hp7.cPan.add(hp7.cPanel3);
                hp7.cPan.add(hp7.cPanel4);
                hp7.cPan.add(hp7.cPanel5);
            
                hp7.hLab.setText("Hand 7:\n");
                hp7.tLab.setText("");
                hp7.add(hp7.textPanel);
                hp7.add(hp7.cPan);
                hp7.setEnabled(true);
                hp7.revalidate();
            }
            
            mPanel.add(hp1);
            mPanel.add(hp2);
            mPanel.add(hp3);
            mPanel.add(hp4);
            mPanel.add(hp5);
            mPanel.add(hp6);
            mPanel.add(hp7);
            mPanel.add(hp8);
            
            hp1.setVisible(true);
            hp2.setVisible(true);
            hp3.setVisible(true);
            hp4.setVisible(true);
            hp5.setVisible(true);
            hp6.setVisible(true);
            hp7.setVisible(true);   
                
            mPanel.revalidate();
            mPanel.repaint();
            mPanel.setVisible(true);
    }
        
/**                             showRules
 * Opens a help window that displays the game rules. Loads content from file at
 * src/poker/resources/rules.txt
 *
 * @param   null
 * @return  null
 */    
    void showRules(){
        String rulesPath = "src/poker/resources/rules.txt";
        JDialog rcFrame = new JDialog(mFrame, "Rules", 
            Dialog.ModalityType.APPLICATION_MODAL);
        JPanel rcPanel = new JPanel(new FlowLayout());
        rcPanel.setBackground(bColor);
        rcPanel.setForeground(tColor);
        rcPanel.setPreferredSize(new Dimension(720, 420));
        rcPanel.setEnabled(true);
        rcPanel.setOpaque(true);
        JTextArea rcText = new JTextArea();
        rcText.setBackground(bColor);
        rcText.setForeground(tColor);
        rcText.setFont(bFont);
        rcText.setBorder(null);
        rcText.setMargin(new Insets(10, 10, 10, 10));
        rcText.setEnabled(true);
        rcText.setPreferredSize(new Dimension(700,400));
        rcText.setOpaque(true);
        try{
            rcText.setText(readFile(rulesPath, Charset.defaultCharset()));
        }catch (IOException exc){
            rcText.setText("File not found");
        }
        rcPanel.add(rcText);
        
        rcFrame.setBackground(bColor);
        rcFrame.setContentPane(rcPanel);
        rcFrame.setPreferredSize(new Dimension (720, 420));
        rcFrame.setEnabled(true);
        rcFrame.revalidate();
        rcFrame.pack();
        rcFrame.setVisible(true);
    } 
    
/**                             showWinner
 * Opens a window that displays the winning hand. In case of a tie (which 
 * may never occur), a message is displayed instead.
 *
 * @param   null
 * @return  null
 */
    void showWinner(){
        Dimension winDim = new Dimension(600,250);
        int winner = getWinner(allHands);
        JDialog winnerFrame = new JDialog(mFrame, "Winning hand", 
            Dialog.ModalityType.APPLICATION_MODAL);
        winnerFrame.setLayout(new FlowLayout());
        winnerFrame.setPreferredSize(winDim);
        
        
        if(getTie(allHands, winner)){
            Font tFont = new Font(Font.SERIF, Font.BOLD, 36);
            JPanel tiePanel = new JPanel();
            JTextField tieLabel = new JTextField("Tie!");
            tiePanel.setPreferredSize(winDim);
            tiePanel.setBackground(bColor);
            tiePanel.setLayout(new FlowLayout());
            
            tieLabel.setFont(tFont);
            tieLabel.setBackground(bColor);
            tieLabel.setForeground(tColor);
            tieLabel.setEditable(false);
            tieLabel.setBorder(null);
            tieLabel.setEnabled(true);
            tieLabel.setOpaque(true);   
            tieLabel.revalidate();
            tieLabel.repaint();
            tieLabel.setVisible(true);
            
            tiePanel.add(tieLabel);
            tiePanel.setEnabled(true);
            tiePanel.setOpaque(true);
            tiePanel.revalidate();
            tiePanel.repaint();
            tiePanel.setVisible(true);
            
            winnerFrame.add(tiePanel);
            winnerFrame.revalidate();
            winnerFrame.repaint();
            winnerFrame.pack();
            winnerFrame.setVisible(true);
        }
        else {
            Hand wHand = allHands[winner];
            
            handPanel winnerPanel = new handPanel();
            winnerPanel.setPreferredSize(winDim);
            winnerPanel.setBackground(bColor);
            
            winnerPanel.cPanel1.setIcon(new ImageIcon(Window.getCardImage(wHand.hand[1].iDNum)));
            winnerPanel.cPanel2.setIcon(new ImageIcon(Window.getCardImage(wHand.hand[2].iDNum)));
            winnerPanel.cPanel3.setIcon(new ImageIcon(Window.getCardImage(wHand.hand[3].iDNum)));
            winnerPanel.cPanel4.setIcon(new ImageIcon(Window.getCardImage(wHand.hand[4].iDNum)));
            winnerPanel.cPanel5.setIcon(new ImageIcon(Window.getCardImage(wHand.hand[5].iDNum)));
            winnerPanel.hLab.setText("Hand " + wHand.handID + ":\n" );
            winnerPanel.tLab.setText(wHand.type);
            winnerPanel.textPanel.add(winnerPanel.hLab, winnerPanel.hpL);
            winnerPanel.textPanel.add(winnerPanel.tLab, winnerPanel.tpL);
            
            winnerPanel.add(winnerPanel.textPanel, winnerPanel.tlC);
            winnerPanel.add(winnerPanel.cPan, winnerPanel.cP);
            winnerPanel.cPan.add(winnerPanel.cPanel1);
            winnerPanel.cPan.add(winnerPanel.cPanel2);
            winnerPanel.cPan.add(winnerPanel.cPanel3);
            winnerPanel.cPan.add(winnerPanel.cPanel4);
            winnerPanel.cPan.add(winnerPanel.cPanel5);
            winnerPanel.add(winnerPanel.cPan, winnerPanel.cP);
            
            winnerPanel.setEnabled(true);
            winnerPanel.setOpaque(true);
            winnerPanel.revalidate();
            winnerPanel.repaint();
            winnerPanel.setVisible(true);
            
            winnerFrame.add(winnerPanel);
            winnerFrame.revalidate();
            winnerFrame.repaint();
            winnerFrame.pack();
            winnerFrame.setVisible(true);
        }
        
    }

/**                     vCreateDeck
 * Calls function to create a new deck of cards, and sets "created" flag 
 * to "true"
 *
 * @param   null
 * @return  a new default deck of cards
 */  
    Deck vCreateDeck(){
        created = true;
        return poker.createDeck();
    }
        
/**                     vDealHands
 * Responds to user input to deal card hands. Calls the poker.dealHands 
 * function. Shuffles deck first, if necessary. If deck was never created, 
 * creates it and then shuffles it.
 *
 * @param   shuffledDeck
 * @return  null
 * @see     poker.dealHands
 */
    void vDealHands(Deck shuffledDeck){
        if(!created){       // create deck if not done
            allHands = poker.dealHands(vShuffleDeck(createDeck()), allHands);
        }
        else
            if(!shuffled){
                allHands = poker.dealHands(vShuffleDeck(createdDeck), allHands);
            }
        else{
                allHands = poker.dealHands(shuffledDeck, allHands);
        }
        showRearHands();
    }
        
/**                     vShuffleDeck
 * Randomizes deck. If no deck has been created, then calls function to create
 * one and then randomizes it. 
 *
 * @param   sDeck   a previously created deck
 * @return          a deck in randomized order
 * @see     poker.shuffleDeck
 * @see     poker.createDeck
 * @see     iShuffleAction
 * @see     bShuffleAction
 */
    Deck vShuffleDeck(Deck sDeck){
        shuffled = true;
        if(!created){   // create deck if there is none     
            return poker.shuffleDeck(createDeck());
        }
        else
        return poker.shuffleDeck(sDeck);
    }
    
    // private BufferedImage img;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    /*
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jFrame2 = new javax.swing.JFrame();
        jFrame3 = new javax.swing.JFrame();
        jFrame4 = new javax.swing.JFrame();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame2Layout = new javax.swing.GroupLayout(jFrame2.getContentPane());
        jFrame2.getContentPane().setLayout(jFrame2Layout);
        jFrame2Layout.setHorizontalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame2Layout.setVerticalGroup(
            jFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame3Layout = new javax.swing.GroupLayout(jFrame3.getContentPane());
        jFrame3.getContentPane().setLayout(jFrame3Layout);
        jFrame3Layout.setHorizontalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame3Layout.setVerticalGroup(
            jFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jFrame4Layout = new javax.swing.GroupLayout(jFrame4.getContentPane());
        jFrame4.getContentPane().setLayout(jFrame4Layout);
        jFrame4Layout.setHorizontalGroup(
            jFrame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame4Layout.setVerticalGroup(
            jFrame4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jMenu3.setText("jMenu3");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(0, 121, 0));
        setEnabled(false);
        setFocusCycleRoot(false);
        setFocusTraversalKeysEnabled(false);
        setFocusable(false);
        setFocusableWindowState(false);
        setMaximumSize(new java.awt.Dimension(1, 1));
        setPreferredSize(new java.awt.Dimension(1, 1));
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents
*/
// --- BUTTON PANEL ACTIONS ----------------------------------------------------
        
/**
 *
 * @param   null
 * @return  null
 */
    private void bNewAction(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        createdDeck = createDeck();
    }                                    
         
/**
 *
 * @param   null
 * @return  null
 */
    private void bShuffleAction(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        vShuffleDeck(createdDeck);
    }
        
/**                     bDealAction
 * ActionListener for "Deal hands" button.
 *
 * @param   null
 * @return  null
 * @see     vDealHands
 * @see     iDealAction
 */
    private void bDealAction(java.awt.event.ActionEvent evt) {                                         
        // DEAL HANDS
        vDealHands(shuffledDeck);
    } 
        
/**                     bDeckAction
 * ActionListener for button that displays deck.
 *
 * @param   null
 * @return  null
 * @see     showDeck
 * @see     iDeckAction
 */
    private void bDeckAction(java.awt.event.ActionEvent evt) {                                         
        // SHOW DECK
        showDeck();
    }  
        
/**
 *
 * @param   null
 * @return  null
 */
    private void bHandsAction(java.awt.event.ActionEvent evt) {                                         
        // SHOW HANDS
        showHands();
    }   
        
/**                     txtPlayersAction
 * Sets number of hands. Default is 5, currently not editable.
 *
 * @param   null
 * @return  null
 */                                 
    private void txtPlayersAction(java.awt.event.ActionEvent evt) {  
        //poker.NUM_HANDS = Integer.valueOf(txtPlayers.getText());
    }  
    
// --- MENU ITEM ACTIONS -------------------------------------------------------

        
/**                     exit
 * ActionListener to exit program when main window is closed
 *
 * @param   null
 * @return  null
 */
    class exit implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    } 
        
/**                     iDealAction
 * ActionListener for "Deal hands" menu/button items
 *
 * @param   null
 * @return  null
 * @see     vDealHands
 * @see     poker.dealHands
 */
    class iDealAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            vDealHands(shuffledDeck);
        }
    }
        
/**                     iDeckAction
 * ActionListener for "Show deck" menu/button items
 *
 * @param   null
 * @return  null
 * @see     showDeck
 */
    class iDeckAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            showDeck();
        }
    }
        
/**                     iDemosAction
 * ActionListener for "Demo files" Help menu item
 *
 * @param   null
 * @return  null
 * @see     showDemos
 */
    class iDemosAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            showDemos();
        }
    }
            
/**                     iFlushAction
 * ActionListener to launch the "flush" demo, which demonstrates proper scoring
 * of a game in which one of the hands is a flush
 *
 * @param   null
 * @return  null
 * @see     Score.getScore
 * @see     Score.isFlush
 */
    class iFlushAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("flush", allHands);
            showRearHands();
        }
    }
 
/**                     iFourAction
 * ActionListener to launch the "four" demo, which demonstrates proper scoring 
 * of a game that contains a four-of-a-kind hand
 *
 * @param   null
 * @return  null
 * @see     Score.getScore
 * @see     Score.hasFourKind
 */
    class iFourAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("four", allHands);
            showRearHands();
        }
    }
                       
/**                     iGameplayAction
 * ActionListener for "Gameplay" Help menu item. Launches dialog window.
 *
 * @param   null
 * @return  null
 * @see     showGameplay
 */
    class iGameplayAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            showGameplay();
        }
    }
        
/**                     iHandsAction
 * ActionListener to direct "Show hands" menu/button events
 *
 * @param   null
 * @return  null
 */
    class iHandsAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            showHands();
        }
    }
        
/**                     iHouseAction
 * ActionListener to launch the "house" demo, which demonstrates proper scoring
 * of a game in which one of the hands is a full house
 *
 * @param   null    
 * @return  null
 * @see     Score.getScore
 * @see     Score.isFullHouse
 */
    class iHouseAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("house", allHands);
            showRearHands();
        }
    }
        
/**                     iNewAction
 * ActionListener to generate a default (pre-shuffled) deck
 *
 * @param 
 * @return
 */    
    class iNewAction implements ActionListener{
        public void actionPerformed(ActionEvent evt) { 
            createdDeck = createDeck();
            created = true;
        }  
    }
        
/**                     iNullAction
 * ActionListener to launch "NullPtr" demo, a troubleshooting hand that causes
 * a null pointer exception. This issue has been corrected.
 *
 * @param 
 * @return
 */
    /*
    class iNullAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("NullPtr", allHands);
            showRearHands();
        }
    }
    */
            
/**                     iScores1Action
 * ActionListener to launch the "Scores1" demo, which demonstrates proper
 * scoring of a game that includes a hand that contains a single pair
 *
 * @param   null
 * @return  null
 * @see     Score.getScore
 * @see     Score.isPair
 */
    class iScores1Action implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("scores1", allHands);
            showRearHands();
        }
    }
            
/**                     iScores2Action
 * ActionListener to launch the "Scores2" demo, which demonstrates proper 
 * scoring of a game that includes a hand with two pairs
 *
 * @param   null
 * @return  null
 * @see     Score.getScore
 * @see     Score.isTwoPair
 */
    class iScores2Action implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("scores2", allHands);
            showRearHands();
        }
    }
            
/**                     iScores3Action
 * ActionListener to launch the "Scores3" demo, which demonstrates proper 
 * scoring of a game in which a hand has three of a kind
 *
 * @param   null
 * @return  null
 * @see     Score.getScore
 * @see     Score.isThreeKind
 */
    class iScores3Action implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("scores3", allHands);
            showRearHands();
        }
    }
            
/**                     iStFlushAction
 * ActionListener to launch the "stflush" demo, which demonstrates proper 
 * scoring of a game in which one hand is a straight flush
 *
 * @param   null
 * @return  null
 * @see     Score.getScore
 * @see     Score.isFlush
 */
    class iStFlushAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("stflush", allHands);
            showRearHands();
        }
    }
            
/**                     iStraightAction
 * ActionListener to launch the "straight" demo, which demonstrates proper 
 * scoring of a game in which one hand is a straight
 *
 * @param   null
 * @return  null
 * @see     Score.getScore
 * @see     Score.isStraight
 */
    class iStraightAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            allHands = demo.getDemo("straight", allHands);
            showRearHands();
        }
    }
        
/**                     iRulesAction
 * ActionListener for the "Rules" Help menu item, which displays a dialog 
 * explaining game rules and scoring
 *
 * @param   null
 * @return  null
 * @see     showRules
 */
    class iRulesAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            showRules();
        }
    }  
        
/**
 *
 * @param 
 * @return
 */
    class iShortAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            showKeyCommands();
        }
    }
        
/**                     iShuffleAction
 * ActionListener for button/menu items to shuffle deck
 *
 * @param   null
 * @return  null
 * @see     vShuffleDeck
 * @see     poker.shuffleDeck
 */
    class iShuffleAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            shuffledDeck = vShuffleDeck(createdDeck);
        }
    }
        
/**                     iTieAction
 * ActionListener to launch "Tie" demo, which demonstrates proper handling of a
 * game in which the high score is shared by two hands
 *
 * @param   null
 * @return  null
 * @see     poker.getWinner
 */
    class iTieAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            shuffled = true;
            allHands = demo.getDemo("tie", allHands);
            showRearHands();
        }
    }
        
/**                     iWinnerAction
 * ActionListener to capture button/menu action to show winning hand
 *
 * @param   null 
 * @return  null
 * @see     showWinner
 */
    class iWinnerAction implements ActionListener{
        public void actionPerformed(ActionEvent evt){
            showWinner();
        }
    }
    
    
    /*
        This is what we want to display:
            - new game M B
            - a deck (back view) 
            - a deck (front view) M B
            - deal M B
            - shuffle M B
            - a hand (hidden)
            - a hand (shown) M B
            - all hands
            - the winning score & hand
            - demo mode option M 
    
    So, menu items could be:
        - Main:
            - (N)ew game
            - (Q)uit game
        - Play:
            - (S)huffle deck
            - (D)eal hands
        - Display: 
            - Show dec(k)
            - Show (h)ands
            - Show (w)inning hand
        - Demo mode:
            - shortcuts to load premade hands
            - tie
        - Help
            - Short(c)uts
    
    And buttons could be:
        - New game
        - Shuffle
        - Deal
        - Show deck
        - Show hands
        - Show winner
    */
    
    /*
        jDesktopPane1 = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                grphcs.drawImage(img, 0, 0, null);
            }
        }
    */
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Window().setVisible(true);
            }
        });           

    }
  /*  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame jFrame1;
    private javax.swing.JFrame jFrame2;
    private javax.swing.JFrame jFrame3;
    private javax.swing.JFrame jFrame4;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    // End of variables declaration//GEN-END:variables
*/
}
