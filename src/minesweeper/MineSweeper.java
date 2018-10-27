package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MineSweeper {
    public MineSweeper(int frame_width, int frame_length, int ground_row, int ground_column, int how_many_mines){
        this.FRAME_WIDTH = frame_width;
        this.FRAME_LENGTH = frame_length;
        this.GROUND_ROW = ground_row;
        this.GROUND_COLUMN = ground_column;
        this.HOW_MANY_MINES = how_many_mines;
        mines_coordinate = new int[HOW_MANY_MINES][2];
        ground = new char[GROUND_ROW][GROUND_COLUMN];
        button_array = new MyButton[GROUND_ROW][GROUND_COLUMN];
        frame = new JFrame("Mineframe");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_LENGTH);
    }
    
    private final int GROUND_ROW;
    private final int GROUND_COLUMN;
    private final char[][] ground;//数组里面可以改，final只限定变量不可改

    public void initiateMineground() {
        for (int i = 0; i < GROUND_ROW; ++i) {
            for (int j = 0; j < GROUND_COLUMN; ++j) {
                ground[i][j] = '0';
            }
        }
    }

    public final int HOW_MANY_MINES;
    public int[][] mines_coordinate;

    public void pushMines() {
        for (int i = 0; i < HOW_MANY_MINES; ++i) {
            mines_coordinate[i][0] = (int) (GROUND_COLUMN * Math.random());
            mines_coordinate[i][1] = (int) (GROUND_ROW * Math.random());
            for (int j = 0; j < i; ++j) {
                while ((mines_coordinate[j][0] == mines_coordinate[i][0]) || (mines_coordinate[j][1] == mines_coordinate[i][1])) {
                    mines_coordinate[i][0] = (int) (GROUND_COLUMN * Math.random());
                    mines_coordinate[i][1] = (int) (GROUND_ROW * Math.random());
                    j = 0;
                }
            }
            ground[mines_coordinate[i][1]][mines_coordinate[i][0]] = 'm';
        }
    }

    public void pushNumber() {
        for (int num = 0; num < HOW_MANY_MINES; ++num) {
            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {
                    if (((mines_coordinate[num][1] + i) >= 0 && (mines_coordinate[num][1] + i) <= GROUND_ROW-1) && ((mines_coordinate[num][0] + j) >= 0 && (mines_coordinate[num][0] + j) <= GROUND_COLUMN-1)) {
                        if (ground[mines_coordinate[num][1] + i][mines_coordinate[num][0] + j] != 'm') {
                            ground[mines_coordinate[num][1] + i][mines_coordinate[num][0] + j] = (char) ((int) (ground[mines_coordinate[num][1] + i][mines_coordinate[num][0] + j]) + 1);
                        }
                    }
                }
            }
        }
    }

    //UI
    JFrame frame;
    final int FRAME_WIDTH;
    final int FRAME_LENGTH;    
    public void initiateUI(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(GROUND_ROW, GROUND_COLUMN, 0, 0));
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        OnClick click_action = new OnClick();//OnClick click_action = minesweeper.new OnClick()//OnClick click_action = new MineSweeper.OnClick()
        for (int i = 0; i < GROUND_ROW; ++i) {
            for (int j = 0; j < GROUND_COLUMN; ++j) {
                button_array[i][j] = new MyButton(i, j, ground[i][j]);
                button_array[i][j].addMouseListener(click_action);
                panel.add(button_array[i][j]);
            }
        }
        frame.add(panel);        
    }
 
    public int howmany_F = 0;   
    class MyButton extends JButton//构造函数不继承
    {
        int row;
        int column;
        char is_what;
        boolean is_opened = false;
        boolean is_flaged = false;

        public MyButton(int row, int column, char is_what)//只区分参数类型和个数
        {
            this.row = row;
            this.column = column;
            this.is_what = is_what;
        }

        public void openZero(int row, int column, char[][] ground, int[][] visited)//DFS
        {
            if ((row < 0 || row > GROUND_ROW - 1) || (column < 0 || column > GROUND_COLUMN - 1) || (visited[row][column] != 0)) {
                return;
            }
            char temp = ground[row][column];
            if (temp == 'm') {
                visited[row][column] = 3;
                return;
            } else if (temp != 'm' && temp != '0') {
                visited[row][column] = 2;
                button_array[row][column].is_opened = true;
                button_array[row][column].setBackground(Color.gray);
                button_array[row][column].setText("" + button_array[row][column].is_what);
                return;
            } else if (temp == '0') {
                visited[row][column] = 1;
                button_array[row][column].is_opened = true;
                button_array[row][column].setBackground(Color.gray);
                openZero(row - 1, column, ground, visited);
                openZero(row, column + 1, ground, visited);
                openZero(row + 1, column, ground, visited);
                openZero(row, column - 1, ground, visited);
            }
        }

        public void Open_rightClick() {
            if (is_flaged && howmany_F !=HOW_MANY_MINES) {
                --howmany_F;
                setText("");
                setBackground(null);
            } else if (!is_opened && howmany_F !=HOW_MANY_MINES) {
                ++howmany_F;
                is_flaged = true;
                setText("F");
                setBackground(Color.blue);
            }
        }

        public void Open_leftClick(char[][] ground, int[][] visited) {
            if (is_what == 'm' && !is_flaged) {
                for (int i = 0; i < GROUND_ROW; ++i) {
                    for (int j = 0; j < GROUND_COLUMN; ++j) {
                        if (button_array[i][j].is_what == 'm') {
                            button_array[i][j].setText("m");
                            button_array[i][j].setBackground(Color.red);
                        } else {
                            button_array[i][j].setText("" + button_array[i][j].is_what);
                            button_array[i][j].setBackground(Color.gray);
                        }
                    }
                }
                JOptionPane.showMessageDialog(null, "You Lose!");
            } else if (Character.isDigit(is_what) && is_what != '0' && !is_flaged && !is_opened) {
                is_opened = true;
                setText("" + is_what);
                setBackground(Color.gray);
            } else if (is_what == '0' && !is_opened) {
                openZero(row, column, ground, visited);
            }
        }
    }

    MyButton[][] button_array;
    public static void main(String[] args) {
        MineSweeper minesweeper = new MineSweeper(500, 500, 10, 10, 10);//FRAME_WIDTH, FRAME_LENGTH, GROUND_ROW, GROUND_COLUMN, HOW_MANY_MINES
        minesweeper.initiateMineground();
        minesweeper.pushMines();
        minesweeper.pushNumber();
        minesweeper.initiateUI();
    }

    public class OnClick implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            MyButton temp = (MyButton) (e.getSource());
            if (e.getButton() == MouseEvent.BUTTON3) { //right click
                temp.Open_rightClick();
            } else { //left click
                int[][] visited = new int[GROUND_ROW][GROUND_COLUMN];
                temp.Open_leftClick(ground, visited);
            }
            if (howmany_F == HOW_MANY_MINES) {
                for(int i = 0; i < HOW_MANY_MINES; ++i){
                    if(!(button_array[mines_coordinate[i][1]][mines_coordinate[i][0]].is_flaged))
                        return;
                }
                JOptionPane.showMessageDialog(null, "You Win!");    
            }
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
        }
        public void mouseExited(MouseEvent e) {
        }
    }
}