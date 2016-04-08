package Snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Board extends JPanel implements ActionListener
{

	private final int WIDTH = 600;
	private final int HEIGHT = 560;
	private final static int TAIL_SIZE = 20;
	private final static int MAX_TAILS = 999;
	private final static int RAND_POS = 20;
	private final int REV_DELAY = 1500;

	public static int delay = 100;
	public static int points = -1;
	public static int once = 0;

	private static int x[] = new int[MAX_TAILS];
	private static int y[] = new int[MAX_TAILS];

	public static int multiplier;
	public static int fHighScore;
	public static int revTime = 0;
	public static int tails;
	public static int food_x;
	public static int food_y;
	public static int revFood_x;
	public static int revFood_y;
	public static int badFood_x;
	public static int badFood_y;
	public static int superFood_x;
	public static int superFood_y;
	public static int portal_x = 75;
	public static int portal_y = 250;
	public static int rand_x;
	public static int rand_y;

	public static boolean left = false;
	public static boolean right = true;
	public static boolean up = false;
	public static boolean down = false;  
	public static boolean pause = false;
	public static boolean reversed = false;
	public static boolean playing = true;

	public static Timer timer, revTimer;
	public static Image head, tail, eat;
	public static Image blueHead, redTail, apple, marioHead, marioTail, marioFood;
	public static Image pacManR, pacManL, pacManU, pacManD, redGhost, deadGhost;
	public static Image revImage, badFood, superFood, portal;


	public Board() throws FileNotFoundException
	{
		addKeyListener(new TAdapter());

		setBackground(Color.orange);

		Scanner file = new Scanner(new FileReader("highScore.txt"));
		fHighScore = file.nextInt();
		file.close();

		System.out.println("High Score: " + fHighScore);

		ImageIcon dotImage = new ImageIcon("redTail.png");
		redTail = dotImage.getImage();

		ImageIcon appleImage = new ImageIcon("apple.png");
		apple = appleImage.getImage();

		ImageIcon headImage = new ImageIcon("blueDot.png");
		blueHead = headImage.getImage();

		ImageIcon pacImageR = new ImageIcon("pacManR.png");
		pacManR = pacImageR.getImage();

		ImageIcon pacImageL = new ImageIcon("pacManL.png");
		pacManL = pacImageL.getImage();

		ImageIcon pacImageU = new ImageIcon("pacManU.png");
		pacManU = pacImageU.getImage();

		ImageIcon pacImageD = new ImageIcon("pacManD.png");
		pacManD = pacImageD.getImage();

		ImageIcon redGhostI = new ImageIcon("redGhost.png");
		redGhost = redGhostI.getImage();

		ImageIcon deadGhostI = new ImageIcon("deadGhost.png");
		deadGhost = deadGhostI.getImage();

		ImageIcon marioHeadI = new ImageIcon("marioHead.png");
		marioHead = marioHeadI.getImage();

		ImageIcon mariotailI = new ImageIcon("marioTail.png");
		marioTail = mariotailI.getImage();

		ImageIcon marioFoodI = new ImageIcon("marioFood.png");
		marioFood = marioFoodI.getImage();

		ImageIcon reverseFoodI = new ImageIcon("revFood.png");
		revImage = reverseFoodI.getImage();

		ImageIcon badFoodI = new ImageIcon("badFood.png");
		badFood = badFoodI.getImage();

		ImageIcon superFoodI = new ImageIcon("superFood.png");
		superFood = superFoodI.getImage();

		head = marioHead;
		eat = marioFood;
		tail = marioTail;
		//      portal = head;

		
		
		setFocusable(true);
		inBounds(); 

		playSound(50);       
	}   
	
//	public static void addPoints()
//	{
//		if(Snake.easyRB.isSelected())
//			multiplier = 1;
//		
//		if(Snake.medRB.isSelected())
//			multiplier = 3;
//		
//		if(Snake.hardRB.isSelected())
//			multiplier = 5;
//	}

	public static void restart() throws FileNotFoundException
	{
		tails = 3;

		for (int i = 0; i < tails; i++) 
		{
			x[i] = 50 - i*10;
			y[i] = 50;
		}

		locateFood();
		locateBadFood();
		locateSuperFood();
		locateRand();
		locateRevFood();
		
		reversed = false;
		
		playing = true;
		points = 0;
		
		up = false;
		down = false;
		right = true;

		if(head == pacManU || head == pacManL || head == pacManD)
			head = pacManR;
	}

	public static void playSound(int c)
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("marioMusic.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();    		
			clip.loop(c);
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void newGameSound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("newGameSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();    		
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void diffChangeSound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("diffChangeSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();    		
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void eatSound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("eatSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void badFruitSound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("badFruitSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace(); 
		}
	}

	public static void superFruitSound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("goodFruitSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void revFruitSound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("revFruitSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void gameOverSound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("gameOverSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public static void pauseSound()
	{
		try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("pauseSound.wav").getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}
		catch(Exception ex)
		{
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public void inBounds()
	{

		tails = 3;

		for (int i = 0; i < tails; i++) 
		{
			x[i] = 50 - i*10;
			y[i] = 50;
		}

		locateFood();
		locateBadFood();
		locateSuperFood();
		locateRand();
		locateRevFood();

		timer = new Timer(delay, this);
		timer.start();
	}

	public void paint(Graphics g)
	{
		super.paint(g);

		if(playing)
		{
			Font small = new Font("Helvetica", Font.BOLD, 14);
			g.setColor(Color.white);
			g.setFont(small);
			g.drawString(Integer.toString(points), 585, 12);
			g.drawImage(eat, food_x, food_y, this);
			g.drawImage(revImage, revFood_x, revFood_y, this);
			g.drawImage(badFood, badFood_x, badFood_y, this);
			g.drawImage(superFood, superFood_x, superFood_y, this);
			//            g.drawImage(portal, portal_x, portal_y, this);

			for (int i = 0; i < tails; i++)
			{
				if (i == 0)
					g.drawImage(head, x[i], y[i], this);
				else g.drawImage(tail, x[i], y[i], this);
			}

			Toolkit.getDefaultToolkit().sync();
			g.dispose();
			//            paused(g);
		} 

		//        else if(playing && pause)
		//        	paused(g);

		else if(!playing) 
		{
			try
			{
				gameOverSound();
				gameOver(g);
//				System.out.println("Game Over Done!");
				return;
			} 
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void paused(Graphics g)
	{
		if(pause)
		{
			String msg = "PAUSED";
			Font small = new Font("Helvetica", Font.BOLD, 24);
			FontMetrics a = this.getFontMetrics(small);

			g.setColor(Color.white);
			g.setFont(small);

			g.drawString(msg, (WIDTH - a.stringWidth(msg)) / 2, HEIGHT / 2);

			Toolkit.getDefaultToolkit().sync();

			validate();
			repaint();

		}
		else if(!pause)
		{
			g.dispose();
		}
	}

	public void gameOver(Graphics g) throws FileNotFoundException 
	{     
		String highScore;
		String msg = "Game Over";
		String pts = "Score:  " + Integer.toString(points);
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics a = this.getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);

		g.drawString(msg, (WIDTH - a.stringWidth(msg)) / 2, (HEIGHT / 2) - 60);
		g.drawString(pts, (WIDTH - a.stringWidth(pts)) / 2, (HEIGHT / 2) - 30);

		if(points > fHighScore)
		{
			fHighScore = points;
			highScore = "NEW High Score:  " + Integer.toString(fHighScore);

			PrintWriter outFile = new PrintWriter("highScore.txt");
			outFile.print(fHighScore);
			outFile.close();
		}

		else
			highScore = "High Score:  " + Integer.toString(fHighScore);

		g.drawString(highScore, (WIDTH - a.stringWidth(highScore)) / 2, (HEIGHT / 2));        	
		return;
	}


	public void checkFood()
	{
		for(int i = 0; i < 15; i++)
		{
			for(int k = 0; k < 15; k++)
			{
				if ((x[0] + i == food_x + k) && (y[0] + i == food_y + k))
				{
					eatSound();
					tails++;
					locateFood();
				}
			}
		}

		for(int i = 0; i < 15; i++)
		{
			for(int k = 0; k < 15; k++)
			{
				if ((x[0] + i == superFood_x + k) && (y[0] + i == superFood_y + k))
				{
					superFruitSound();
					tails++;
					tails++;
					locateSuperFood();
				}
			}
		}

		for(int i = 0; i < 15; i++)
		{
			for(int k = 0; k < 15; k++)
			{
				if((x[0] + i == revFood_x + k) && (y[0] + i == revFood_y + k))
				{           
					if(!reversed)
					{
						revFruitSound();
						revTime = 100;
						reversed = true;      		
					}
					else if(reversed)
						reversed = false;
					locateRevFood();
				}
			}
		}

		for(int i = 0; i < 15; i++)
		{
			for(int k = 0; k < 15; k++)
			{
				if ((x[0] + i == badFood_x + k) && (y[0] + i == badFood_y + k))
				{
					badFruitSound();
					if(tails-2 > 1)
					{
						tails--;
						tails--;
					}
					locateBadFood();
				}
			}
		}

		for(int i = 0; i < 15; i++)
		{
			for(int k = 0; k < 15; k++)
			{
				if ((x[0] + i == portal_x + k) && (y[0] + i == portal_y + k))
				{
					x[0] = rand_x;
					y[0] = rand_y;
					locatePortal();
					locateRand();
				}
			}
		}
	}

	public void move()
	{
		for (int i = tails; i > 0; i--)
		{
			x[i] = x[(i - 1)];
			y[i] = y[(i - 1)];
		}

		if (left) 
		{
			x[0] -= TAIL_SIZE;
		}

		if (right)
		{
			x[0] += TAIL_SIZE;
		}

		if (up) 
		{
			y[0] -= TAIL_SIZE;
		}

		if (down) 
		{
			y[0] += TAIL_SIZE;
		}
	}

	public void checkCollision()
	{

		for(int i = tails; i > 0; i--)
		{

			if((i > 4) && (x[0] == x[i]) && (y[0] == y[i]))
			{
				playing = false;
			}
		}

		if(y[0] > HEIGHT)
		{
			playing = false;
		}

		if(y[0] < 0)
		{
			playing = false;
		}

		if(x[0] > WIDTH)
		{
			playing = false;
		}

		if(x[0] < 0)
		{
			playing = false;
		}
	}
	
	public static void addPoints()
	{
		if(Snake.pointsMult == 1)
			points++;
		
		else if(Snake.pointsMult == 2)
		{
			points++;
			points++;
		}
		
		else if(Snake.pointsMult == 3)
		{
			points++;
			points++;
			points++;
			points++;
		}
	}

	public static void locateFood() 
	{
		int r = (int) (Math.random() * RAND_POS);
		food_x = ((r * TAIL_SIZE));
		r = (int) (Math.random() * RAND_POS);
		food_y = ((r * TAIL_SIZE));
		addPoints();
	}

	public static void locateRand() 
	{
		int r = (int) (Math.random() * RAND_POS);
		rand_x = ((r * TAIL_SIZE));
		r = (int) (Math.random() * RAND_POS);
		rand_y = ((r * TAIL_SIZE));
	}

	public void locatePortal() 
	{
		int r = (int) (Math.random() * RAND_POS);
		portal_x = ((r * TAIL_SIZE));
		r = (int) (Math.random() * RAND_POS);
		portal_y = ((r * TAIL_SIZE));
	}

	public static void locateSuperFood() 
	{
		int r = (int) (Math.random() * RAND_POS);
		superFood_x = ((r * TAIL_SIZE));
		r = (int) (Math.random() * RAND_POS);
		superFood_y = ((r * TAIL_SIZE));
		addPoints();
		addPoints();
	}

	public static void locateBadFood() 
	{
		int r = (int) (Math.random() * RAND_POS);
		badFood_x = ((r * TAIL_SIZE));
		r = (int) (Math.random() * RAND_POS);
		badFood_y = ((r * TAIL_SIZE));
		points--;
		points--;
	}

	public static void locateRevFood() 
	{
		int rand = (int) (Math.random() * RAND_POS);
		revFood_x = ((rand * TAIL_SIZE));
		rand = (int) (Math.random() * RAND_POS);
		revFood_y = ((rand * TAIL_SIZE));
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(playing) 
		{
			checkFood();
			checkCollision();
			move();

			if(revTime-- < 0)
				reversed = false;

			repaint();
		}

		if(!playing && once < 1)
		{
			repaint();
			once++;
		}
	}

	private class TAdapter extends KeyAdapter
	{
		public void keyPressed(KeyEvent e) 
		{

			int key = e.getKeyCode();

			if(!reversed)
			{

				if ((key == KeyEvent.VK_LEFT) && (!right))
				{
					up = false;
					down = false;
					left = true;

					if(head == pacManU || head == pacManR || head == pacManD)
						head = pacManL;
				}

				if ((key == KeyEvent.VK_RIGHT) && (!left)) 
				{
					up = false;
					down = false;
					right = true;

					if(head == pacManU || head == pacManL || head == pacManD)
						head = pacManR;
				}

				if ((key == KeyEvent.VK_UP) && (!down)) 
				{
					right = false;
					left = false;
					up = true;

					if(head == pacManL || head == pacManR || head == pacManU)
						head = pacManU;
				}

				if ((key == KeyEvent.VK_DOWN) && (!up)) 
				{
					right = false;
					left = false;
					down = true;

					if(head == pacManL || head == pacManR || head == pacManD)
						head = pacManD;
				}
			}

			if(reversed)
			{

				if ((key == KeyEvent.VK_LEFT) && (!left))
				{
					up = false;
					down = false;
					right = true;

					if(head == pacManU || head == pacManL || head == pacManD)
						head = pacManR;
				}

				if ((key == KeyEvent.VK_RIGHT) && (!right)) 
				{
					up = false;
					down = false;
					left = true;

					if(head == pacManU || head == pacManR || head == pacManD)
						head = pacManL;
				}

				if ((key == KeyEvent.VK_UP) && (!up)) 
				{
					right = false;
					left = false;
					down = true;

					if(head == pacManL || head == pacManR || head == pacManD)
						head = pacManD;
				}

				if ((key == KeyEvent.VK_DOWN) && (!down)) 
				{
					right = false;
					left = false;
					up = true;

					if(head == pacManL || head == pacManR || head == pacManU)
						head = pacManU;
				}
			}

			if(key == KeyEvent.VK_ESCAPE)
			{
//				revFruitSound();
				System.exit(1);
			}

			if(key == KeyEvent.VK_F2)
			{
				newGameSound();
				try
				{
					restart();
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				}
			}

			if(key == KeyEvent.VK_P)
			{
				pauseSound();
				//            	System.out.println("Pressed P");
				if(!pause)
				{
					timer.stop();      		
					pause = true;

					//            		System.out.println("Paused");
				}
				else if(pause)
				{
					pause = false;
					timer.restart();
					//            		System.out.println("Unpaused");
				}            		
			}
		}
	}
}

