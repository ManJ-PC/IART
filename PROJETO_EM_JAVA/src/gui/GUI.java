package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Stack;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import logic.Board;
import logic.Game;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.BorderLayout; 

public class GUI {

	private static JTable tabel = null; 
	
	private static JFrame boardFrame = null;
	
	private static JPanel boardPanel = null;
	
	private static Game game = null;
	
	private static String[][] board = null;
	
	private static int clickCount = 2;
	
	private static Integer[][] playPos = new Integer[2][];
	
	private static JSlider sliderSizeBoard;
	
	private static JSlider sliderDepth;
	
	 private static DefaultTableCellRenderer getRenderer() {
	        return new DefaultTableCellRenderer(){
	            @Override
	            public Component getTableCellRendererComponent(JTable table,
	                    Object value, boolean isSelected, boolean hasFocus,
	                    int row, int column) {
	                Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);
	                if("X".equals(value)){
	                    tableCellRendererComponent.setBackground(Color.WHITE);
	                    tableCellRendererComponent.setForeground(Color.WHITE);
	                } else  if("0".equals(value)){
	                    tableCellRendererComponent.setBackground(Color.BLACK);
	                    tableCellRendererComponent.setForeground(Color.BLACK);
	                } else {
	                    tableCellRendererComponent.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
	                }
	                return tableCellRendererComponent;
	            }
	        };
	    }



	private static void pvp() {
		restartGame();
		
		
		int i = 0;
		byte playPiece;
		Point[] play = null;
		do{

			
			if((i++%2)==0)
				playPiece = Game.blackpiece;
			else
				playPiece = Game.whitepiece;
			
			do{
				System.out.println("Try a valid play!");
				play = getCoords();					
			}while(!game.getBoard().validPlay(play[0],play[1], playPiece));
			game.setPieces(playPiece, play[0], play[1]);
				
        	board[play[0].y][play[0].x] = ""+(char)playPiece; 	
        	board[play[1].y][play[1].x] = ""+(char)playPiece;
        	
			boardPanel.getComponent(0).repaint();
			boardPanel.getComponent(1).repaint();
			boardPanel.repaint();
			boardFrame.repaint();
			
		}while(!game.checkEndGame(play[0], play[1], playPiece));
		
		boardPanel.repaint();
		boardFrame.repaint();
	}


	private static void pvsai() {
		restartGame();

		int i = 0;
		byte playPiece;
		Point[] play = null;
		do{

			
			if((i++%2)==0){
				playPiece = Game.blackpiece;
				do{
					System.out.println("Try a valid play!");
					play = getCoords();					
				}while(!game.getBoard().validPlay(play[0],play[1], playPiece));
				game.setPieces(playPiece, play[0], play[1]);
			}
			else{
				playPiece = Game.whitepiece;
				play = game.getPlay(playPiece);
			}
			
        	board[play[0].y][play[0].x] = ""+(char)playPiece; 	
        	board[play[1].y][play[1].x] = ""+(char)playPiece;
        	
			boardPanel.getComponent(0).repaint();
			boardPanel.getComponent(1).repaint();
			boardPanel.repaint();
			boardFrame.repaint();
			
		}while(!game.checkEndGame(play[0], play[1], playPiece));
		
		boardPanel.repaint();
		boardFrame.repaint();
	}
		
	private static Point[] getCoords() {
		clickCount = 0;
		while(clickCount!=2){
			System.out.flush();
		}
		
		return new Point[]{new Point(playPos[0][0],playPos[0][1]),new Point(playPos[1][0],playPos[1][1])};
	}



	private static void aivsai(){
		restartGame();
		
		
		int i = 0;
		byte playPiece;
		Point[] play = null;
		do{

			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			playPiece = ((i++%2)==0)?Game.blackpiece:Game.whitepiece;
			
			play = game.getPlay(playPiece);
			
        	board[play[0].y][play[0].x] = ""+(char)playPiece; 	
        	board[play[1].y][play[1].x] = ""+(char)playPiece;
        	
			boardPanel.getComponent(0).repaint();
			boardPanel.getComponent(1).repaint();
			boardPanel.repaint();
			boardFrame.repaint();
			
		}while(!game.checkEndGame(play[0], play[1], playPiece));
		
		boardPanel.repaint();
		boardFrame.repaint();
		
	}
	
	private static void showMenu() {
        //Create and set up the window.
        JFrame menuFrame = new JFrame("MenuFrame");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.pack();
        menuFrame.setSize(514, 510);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new MigLayout("", "[250px][]", "[100px][100px][100px][][][]"));
        
        JLabel lblNewLabel = new JLabel("WhirldWind");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
        buttonPanel.add(lblNewLabel, "cell 0 0 2 1,alignx center");
        JButton playervsplayerButton = new JButton("Player vs Player");
        playervsplayerButton.setMinimumSize(new Dimension(250, 100));
        playervsplayerButton.setPreferredSize(new Dimension(250, 100));
        
        buttonPanel.add(playervsplayerButton, "flowx,cell 0 1 2 1,alignx center,growy" );
        
                playervsplayerButton.setSize(new Dimension(540, 540));
                
                playervsplayerButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cleanBoard();
				menuFrame.setVisible(false);
				boardFrame.setVisible(true);
				Thread thread = new Thread(new Runnable() {
		            public void run() {
		            	pvp();
		            }
		        });
				thread.start();
			}        	
                });
        

        boardFrame = new JFrame("BoardFrame");
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setLocationRelativeTo(null);
        boardFrame.pack();
        boardFrame.setSize(540, 600);
        
        boardPanel = new JPanel();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.PAGE_AXIS));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));   
        boardPanel.setBackground(new Color(245,222,179));
        
        
        String[] columns = new String[Game.boardsize];
        
        for(int i = 0; i < Game.boardsize; i++){
        	columns[i] = i + "";
        }
        
        
        tabel = new  JTable(board,columns);
        tabel.setBackground(new Color(222,184,135));
        tabel.setRowHeight(40);
        tabel.setRowSelectionAllowed(false);
        
        tabel.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for(TableColumn cl :Collections.list(tabel.getColumnModel().getColumns())){
        	cl.setPreferredWidth(25);
        	cl.setCellRenderer(getRenderer());
        }
        tabel.updateUI();
        tabel.repaint();
        
        JButton backToMenu = new JButton("Back to Menu");
        backToMenu.setSize(new Dimension(250, 100));


        boardPanel.add(tabel);
        boardPanel.add(Box.createRigidArea(new Dimension(0,25)));
        boardPanel.add(backToMenu);

        for (int c = 0; c < tabel.getColumnCount(); c++)
        {
            Class<?> col_class = tabel.getColumnClass(c);
            tabel.setDefaultEditor(col_class, null);        // remove editor
        }
        
        tabel.addMouseListener(new MouseAdapter() {
        	  public void mouseClicked(MouseEvent e) {
        	    if (e.getClickCount() == 1) {
        	      JTable target = (JTable)e.getSource();
        	      int row = target.getSelectedRow();
        	      int column = target.getSelectedColumn();
        	      
        	      if(clickCount<2){
        	    	  System.out.println(column+":" + row);
        	    	  playPos[clickCount++] = new Integer[]{column, row};
        	    	
        	      }

        	    }
        	  }
        	});
        
        menuFrame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
        JButton playervsaiButton = new JButton("Player vs AI");
        playervsaiButton.setMinimumSize(new Dimension(250, 100));
        playervsaiButton.setPreferredSize(new Dimension(250, 100));
        buttonPanel.add(playervsaiButton, "cell 0 2 2 1,alignx center,growy" );
        
        playervsaiButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cleanBoard();
				menuFrame.setVisible(false);
				boardFrame.setVisible(true);
				Thread thread = new Thread(new Runnable() {
		            public void run() {
		            	pvsai();
		            }
		        });
				thread.start();
			}        	
        });
        
        JLabel lblBoardSize = new JLabel("Board Size");
        lblBoardSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
        buttonPanel.add(lblBoardSize, "cell 0 4,alignx center");
        
        sliderSizeBoard = new JSlider();
        sliderSizeBoard.setMaximum(20);
        sliderSizeBoard.setMinimum(4);
        sliderSizeBoard.setPaintTicks(true);
        sliderSizeBoard.setPaintLabels(true);
        sliderSizeBoard.setMajorTickSpacing(2);
        sliderSizeBoard.setMinorTickSpacing(1);
        buttonPanel.add(sliderSizeBoard, "cell 1 4,grow");
        
        JLabel lblDepth = new JLabel("Depth");
        lblDepth.setFont(new Font("Tahoma", Font.PLAIN, 16));
        buttonPanel.add(lblDepth, "cell 0 5,alignx center");
        
        sliderDepth = new JSlider();
        sliderDepth.setMaximum(6);
        sliderDepth.setMinimum(1);
        sliderDepth.setPaintTicks(true);
        sliderDepth.setPaintLabels(true);
        sliderDepth.setMajorTickSpacing(1);
        sliderDepth.setMinorTickSpacing(1);
        buttonPanel.add(sliderDepth, "cell 1 5,grow");
        
               //Add the ubiquitous "Hello World" label.
               JButton aivsaiButton = new JButton("AI vs AI");
               
               aivsaiButton.setMinimumSize(new Dimension(250, 100));
               
               aivsaiButton.setPreferredSize(new Dimension(250, 100));
               buttonPanel.add(aivsaiButton, "cell 0 3 2 1,alignx center,growy" );
               
               aivsaiButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cleanBoard();
				menuFrame.setVisible(false);
				boardFrame.setVisible(true);
				Thread thread = new Thread(new Runnable() {
		            public void run() {
		            	aivsai();
		            }
		        });
				thread.start();
			}        	
               });
        boardFrame.getContentPane().add(boardPanel);
 
        //Display the window.
        menuFrame.setVisible(true);
        boardFrame.setVisible(false);
        
        
        backToMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				menuFrame.setVisible(true);
				boardFrame.setVisible(false);
			}
        	
        });
    }
	
	private static void cleanBoard(){

		game.resetBoard();
		Board boardOri = game.getBoard();
		
		for(int i = 0; i < Game.boardsize; i++){
			board[i] = new String[Game.boardsize];
			for(int j = 0; j < Game.boardsize; j++){
				char ch = (char)(boardOri.getPiece(j ,i));
				board[i][j] = "" + ch;
			}
		}
	}
 
    public static void main(String[] args) {
		
    	game = new Game();

		board = new String[Game.boardsize][Game.boardsize]; 
		
		cleanBoard();
		
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	showMenu();
            }
        });
        
    }
    
    public static void restartGame(){
    	//setGameBoardSize();
    	setGameDepth();
    	
    	game.resetBoard();
    	

		cleanBoard();
    }
    
    public static void setGameBoardSize(){
    	Game.boardsize = sliderSizeBoard.getValue();
    }
    
    public static void setGameDepth(){
    	Game.depth = sliderDepth.getValue();
    }
}
