package Snake;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;


public class Snake extends JFrame implements ActionListener
{
	private JMenuBar menuMB = new JMenuBar();
	
	private JMenu gameM, optionsM, helpM; //pg. 686
	private JMenuItem newGame, pauseI, exitI, diffI, rulesI;
	
	private JLabel scoreL;

	private ButtonGroup difficulty, theme;
	private JRadioButtonMenuItem easyRB, medRB, hardRB, theme1, theme2, theme3;
//	private JCheckBoxMenuItem doubled;
	private String helpStr;
	public static Board Gameboard;
	
	public Snake() throws FileNotFoundException 
	{
    	Container pane = getContentPane();
    	Gameboard = new Board();
        pane.add(Gameboard);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(320, 340);
        setLocationRelativeTo(null);
        setTitle("©2013 Snake");
        
		setJMenuBar(menuMB);
        setGameMenu();
        setOptionsMenu();
        setHelpMenu();
        
        helpStr = "How to ©2013 Snake...\n\nMove: Arrow Keys\nNew Game: F2\nPause: P\n" +
        		"Quit: Esc\nObjective: Collect as many fruits as possible.\nWarning: " +
        		"Not all fruit are good for you!";
        
        setResizable(false);
        setVisible(true);
        
        
    }

	public void setGameMenu()
	{
		gameM = new JMenu("Game");
		menuMB.add(gameM);
		
		newGame = new JMenuItem("New Game     F2");
		gameM.add(newGame);
		newGame.addActionListener(this);
		
		pauseI = new JMenuItem("Pause              P");
		gameM.add(pauseI);
		pauseI.addActionListener(this);
		
		exitI = new JMenuItem("Exit                 Esc");
		gameM.add(exitI);
		exitI.addActionListener(this);
		
		scoreL = new JLabel("             ©2013 Snake 1.0 by Teran Inc.");
		gameM.add(scoreL);
		
	}

	private void setOptionsMenu()
	{
		difficulty = new ButtonGroup();
		theme = new ButtonGroup();
		
		optionsM = new JMenu("Options");
		menuMB.add(optionsM);
        
        easyRB = new JRadioButtonMenuItem("Beginner", true);
        medRB = new JRadioButtonMenuItem("Better");
        hardRB = new JRadioButtonMenuItem("Best");
        theme1 = new JRadioButtonMenuItem("Default");
        theme2 = new JRadioButtonMenuItem("Pac Man");
        theme3 = new JRadioButtonMenuItem("Mario", true);  
        
//        doubled = new JCheckBoxMenuItem("Double Points");
		
		difficulty.add(easyRB);//  cool menus example
		difficulty.add(medRB);
		difficulty.add(hardRB);	
			
        theme.add(theme1);
		theme.add(theme2);
		theme.add(theme3);
		
        easyRB.addActionListener(this);
        medRB.addActionListener(this);
        hardRB.addActionListener(this);
        theme1.addActionListener(this);
        theme2.addActionListener(this);
        theme3.addActionListener(this);
//        doubled.addActionListener(this);
        
		optionsM.add(easyRB);
		optionsM.add(medRB);
		optionsM.add(hardRB);
		
		optionsM.addSeparator();
		optionsM.addSeparator();
		
		optionsM.add(theme1);
		optionsM.add(theme2);
		optionsM.add(theme3);
		
//		optionsM.addSeparator();
//		optionsM.addSeparator();
//		
//		optionsM.add(doubled);
	}
	
	private void setHelpMenu()
	{
		helpM = new JMenu("Help");
		menuMB.add(helpM);
		
		rulesI = new JMenuItem("Rules");
		helpM.add(rulesI);		
		rulesI.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource().equals(rulesI))
		{
			if(!Board.pause)
			{
				Board.pauseSound();
				Board.timer.stop();
				Board.pause = true;
			}
			
			
			JOptionPane.showMessageDialog(null, helpStr, "Help", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(e.getSource().equals(exitI))
		{
//				Board.revFruitSound();
			System.exit(1);
		}
		
		if(e.getSource().equals(newGame))
		{
			try
			{
				Board.restart();
			}
			
			catch (FileNotFoundException e1) 
			{
				e1.printStackTrace();
			}
		}
		
		if(e.getSource().equals(pauseI))
		{
			Board.pauseSound();
//            	System.out.println("Selected pauseI");
        	if(!Board.pause)
        	{
        		Board.timer.stop();
        		Board.pause = true;
        		
//            		System.out.println("Paused");
        	}
        	else if(Board.pause)
        	{
        		Board.pause = false;
        		Board.timer.restart();
//            		System.out.println("Unpaused");
        	}
		}
		
		if(e.getSource().equals(easyRB))
		{
			Board.diffChangeSound();				
			Board.timer.setDelay(100);
			Board.points = 0;
			Board.tails = 3;
		}
		
		if(e.getSource().equals(medRB))
		{
			Board.diffChangeSound();
			Board.timer.setDelay(75);
			Board.points = 0;
			Board.tails = 3;
		}
		
		if(e.getSource().equals(hardRB))
		{			
			Board.diffChangeSound();
			Board.timer.setDelay(25);
			Board.points = 0;
			Board.tails = 3;
		}
		
		if(e.getSource().equals(theme1))
		{
			Board.newGameSound();
	        Board.head = Board.blueHead;
	        Board.eat = Board.apple;
	        Board.tail = Board.redTail;
		}
		
		if(e.getSource().equals(theme2))
		{
			Board.newGameSound();
			Board.head = Board.pacManR;
			Board.tail = Board.deadGhost;
			Board.eat = Board.redGhost;
		}
		
		if(e.getSource().equals(theme3))
		{
			Board.newGameSound();
			Board.head = Board.marioHead;
			Board.tail = Board.marioTail;
			Board.eat = Board.marioFood;				
		}
			
	}
	
	
    public static void main(String[] args) throws FileNotFoundException
    {
        new Snake();
    }
}