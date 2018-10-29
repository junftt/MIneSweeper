package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MineSweeper {
    public MineSweeper(int frame_width, int frame_length, int ground_row, int ground_column, int how_many_mines) {
        this.frame_width = frame_width;
        this.frame_length = frame_length;
        this.ground_row = ground_row;
        this.ground_column = ground_column;
        this.how_many_mines = how_many_mines;
        mines_coordinate = new int[how_many_mines][2];
        ground = new char[ground_row][ground_column];
        button_array = new MyButton[ground_row][ground_column];
        frame = new JFrame("Mineframe");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frame_width, frame_length);
    }

    private final int ground_row;
    private final int ground_column;
    private final char[][] ground;//数组里面可以改，final只限定变量不可改

    public void initiateMineground() {
        for (int i = 0; i < ground_row; ++i) {
            for (int j = 0; j < ground_column; ++j) {
                ground[i][j] = '0';
            }
        }
    }

    public final int how_many_mines;
    public int[][] mines_coordinate;

    public void pushMines() {
        for (int i = 0; i < how_many_mines; ++i) {
            mines_coordinate[i][0] = (int) (ground_column * Math.random());
            mines_coordinate[i][1] = (int) (ground_row * Math.random());
            for (int j = 0; j < i; ++j) {
                while ((mines_coordinate[j][0] == mines_coordinate[i][0]) || (mines_coordinate[j][1] == mines_coordinate[i][1])) {
                    mines_coordinate[i][0] = (int) (ground_column * Math.random());
                    mines_coordinate[i][1] = (int) (ground_row * Math.random());
                    j = 0;
                }
            }
            ground[mines_coordinate[i][1]][mines_coordinate[i][0]] = 'm';
        }
    }

    public void pushNumber() {
        for (int num = 0; num < how_many_mines; ++num) {
            for (int i = -1; i < 2; ++i) {
                for (int j = -1; j < 2; ++j) {
                    if (((mines_coordinate[num][1] + i) >= 0 && (mines_coordinate[num][1] + i) <= ground_row - 1) && ((mines_coordinate[num][0] + j) >= 0 && (mines_coordinate[num][0] + j) <= ground_column - 1)) {
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
    final int frame_width;
    final int frame_length;

    public void initiateUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(ground_row, ground_column, 0, 0));
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        OnClick click_action = new OnClick();//OnClick click_action = minesweeper.new OnClick()//OnClick click_action = new MineSweeper.OnClick()
        for (int i = 0; i < ground_row; ++i) {
            for (int j = 0; j < ground_column; ++j) {
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

        public void updateState() {
            if (is_flaged) {
                setText("F");
                setBackground(Color.blue);
            } else if (is_opened) {
                if (is_what == '0') {
                    setText("");
                } else {
                    setText("" + is_what);
                }
                if (is_what == 'm') {
                    setBackground(Color.red);
                } else {
                    setBackground(Color.gray);
                }
            } else {
                setText("");
                setBackground(null);
            }
        }

        public void openZero(int row, int column, char[][] ground, int[][] visited)//DFS
        {
            if ((row < 0 || row > ground_row - 1) || (column < 0 || column > ground_column - 1) || (visited[row][column] != 0)) {
                return;
            }
            char temp = ground[row][column];
            if (temp != 'm' && temp != '0') {
                visited[row][column] = 2;
                button_array[row][column].is_opened = true;
                button_array[row][column].updateState();
                return;
            } else {
                visited[row][column] = 1;
                button_array[row][column].is_opened = true;
                button_array[row][column].updateState();
                openZero(row - 1, column, ground, visited);
                openZero(row, column + 1, ground, visited);
                openZero(row + 1, column, ground, visited);
                openZero(row, column - 1, ground, visited);
            }
        }

        public void Open_rightClick() {
            if (is_flaged) {
                --howmany_F;
                is_flaged = false;
                updateState();
            } else if (!is_opened && howmany_F < how_many_mines) {
                ++howmany_F;
                is_flaged = true;
                updateState();
            }
        }

        public void Open_leftClick(char[][] ground, int[][] visited) {
            if (is_what == 'm' && !is_flaged) {
                for (int i = 0; i < ground_row; ++i) {
                    for (int j = 0; j < ground_column; ++j) {
                        button_array[i][j].is_opened = true;
                        button_array[i][j].updateState();
                    }
                }
                JOptionPane.showMessageDialog(null, "You Lose!");
            } else if (Character.isDigit(is_what) && is_what != '0' && !is_flaged && !is_opened) {
                is_opened = true;
                updateState();
            } else if (is_what == '0' && !is_opened) {
                openZero(row, column, ground, visited);
            }
        }
    }

    MyButton[][] button_array;
    public static void main(String[] args) {
        MineSweeper minesweeper = new MineSweeper(500, 500, 10, 10, 10);//frame_width, frame_length, ground_row, ground_column, how_many_mines
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
                int[][] visited = new int[ground_row][ground_column];
                temp.Open_leftClick(ground, visited);
            }
            if (howmany_F == how_many_mines) {
                for (int i = 0; i < how_many_mines; ++i) {
                    if (!(button_array[mines_coordinate[i][1]][mines_coordinate[i][0]].is_flaged)) {
                        return;
                    }
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
