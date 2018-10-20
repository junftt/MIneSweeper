package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MineSweeper {

    //abstract
    public static final int mine_num_row = 10;
    public static final int mine_num_cloumn = 10;

    public static char[][] mine_ground = new char[mine_num_row][mine_num_cloumn];//声明数列//char与Character不同

    public static void initiateMineground() {
        for (int i = 0; i < mine_num_row; ++i) {
            for (int j = 0; j < mine_num_cloumn; ++j) {
                mine_ground[i][j] = '0';
            }
        }
    }

    public static final int howmany_mine = 10;
    public static int[][] ground_coordinate = new int[howmany_mine][2];

//雷重复的问题
    public static void pushMines() {
        //在数组中生成抽象炸弹
        for (int i = 0; i < howmany_mine; ++i) {
            ground_coordinate[i][0] = (int) (10 * Math.random());
            ground_coordinate[i][1] = (int) (10 * Math.random());
            for (int j = 0; j < i; ++j) {
                while ((ground_coordinate[j][0] == ground_coordinate[i][0]) || (ground_coordinate[j][1] == ground_coordinate[i][1]) || (ground_coordinate[i][0] == 10) || (ground_coordinate[i][1] == 10)) {
                    ground_coordinate[i][0] = (int) (10 * Math.random());
                    ground_coordinate[i][1] = (int) (10 * Math.random());
                }
            }
        }
        //检测坐标重复问题
        for (int i = 0; i < howmany_mine; ++i) {
            mine_ground[ground_coordinate[i][1]][ground_coordinate[i][0]] = 'm';
        }
    }

    public static void pushNumber() {
        for (int i = 0; i < howmany_mine; ++i) {   //以雷为中心填数
            if (ground_coordinate[i][1] - 1 >= 0) {
                if (mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0]] != 'm') {
                    mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0]] = (char) ((int) (mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0]]) + 1);
                }
            }
            if (ground_coordinate[i][1] - 1 >= 0 && ground_coordinate[i][0] - 1 >= 0) {
                if (mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0] - 1] != 'm') {
                    mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0] - 1] = (char) ((int) (mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0] - 1]) + 1);
                }
            }
            if (ground_coordinate[i][1] - 1 >= 0 && ground_coordinate[i][0] + 1 <= 9) {
                if (mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0] + 1] != 'm') {
                    mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0] + 1] = (char) ((int) (mine_ground[ground_coordinate[i][1] - 1][ground_coordinate[i][0] + 1]) + 1);
                }
            }

            if (ground_coordinate[i][1] + 1 <= 9) {
                if (mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0]] != 'm') {
                    mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0]] = (char) ((int) (mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0]]) + 1);
                }
            }
            if (ground_coordinate[i][1] + 1 <= 9 && ground_coordinate[i][0] - 1 >= 0) {
                if (mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0] - 1] != 'm') {
                    mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0] - 1] = (char) ((int) (mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0] - 1]) + 1);
                }
            }
            if (ground_coordinate[i][1] + 1 <= 9 && ground_coordinate[i][0] + 1 <= 9) {
                if (mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0] + 1] != 'm') {
                    mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0] + 1] = (char) ((int) (mine_ground[ground_coordinate[i][1] + 1][ground_coordinate[i][0] + 1]) + 1);
                }
            }

            if (ground_coordinate[i][0] - 1 >= 0) {
                if (mine_ground[ground_coordinate[i][1]][ground_coordinate[i][0] - 1] != 'm') {
                    mine_ground[ground_coordinate[i][1]][ground_coordinate[i][0] - 1] = (char) ((int) (mine_ground[ground_coordinate[i][1]][ground_coordinate[i][0] - 1]) + 1);
                }
            }
            if (ground_coordinate[i][0] + 1 <= 9) {
                if (mine_ground[ground_coordinate[i][1]][ground_coordinate[i][0] + 1] != 'm') {
                    mine_ground[ground_coordinate[i][1]][ground_coordinate[i][0] + 1] = (char) ((int) (mine_ground[ground_coordinate[i][1]][ground_coordinate[i][0] + 1]) + 1);
                }
            }
        }
    }

    public static void howToOpen(int row, int column, char[][] mine_ground, int[][] visited)//DFS
    {
        if ((row < 0 || row > mine_num_row - 1) || (column < 0 || column > mine_num_cloumn - 1) || (visited[row][column] != 0)) {
            return;
        }
        char temp = mine_ground[row][column];
        if (temp == 'm') {
            visited[row][column] = 3;
            return;
        } else if (temp != 'm' && temp != '0') {
            visited[row][column] = 2;
            return;
        } else if (temp == '0') {
            visited[row][column] = 1;
            howToOpen(row - 1, column, mine_ground, visited);
            howToOpen(row, column + 1, mine_ground, visited);
            howToOpen(row + 1, column, mine_ground, visited);
            howToOpen(row, column - 1, mine_ground, visited);
        }
    }

    public static int[][] whether_opened = new int[mine_num_row][mine_num_cloumn];
    public static int howmany_F = 0;

    //UI
    static class MyButton extends JButton//构造函数不继承
    {

        public MyButton(int row, int column, char what_is)//只区分参数类型和个数
        {
            this.row = row;
            this.column = column;
            this.what_is = what_is;
        }//new MyButton(1);
        //new MyButton(1,2);
        int row;
        int column;
        char what_is;
    }

    static MyButton[][] button_array = new MyButton[mine_num_row][mine_num_cloumn];

    public static void main(String[] args) {
        initiateMineground();
        pushMines();
        pushNumber();

        final int frame_width = 500;
        final int frame_length = 500;
        final int mine_num_row = 10;
        final int mine_num_cloumn = 10;

        JFrame frame = new JFrame("Mineframe");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frame_width, frame_length);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(mine_num_row, mine_num_cloumn, 0, 0));
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        //OnClickAction temp = new OnClickAction();
        for (int i = 0; i < mine_num_row; ++i) {
            for (int j = 0; j < mine_num_cloumn; ++j) {
                button_array[i][j] = new MyButton(i, j, mine_ground[i][j]);
                button_array[i][j].addMouseListener(new OnRightClick());
                //button_array[i][j].addActionListener(new OnClickAction());
            }
        }

        for (int i = 0; i < mine_num_row; ++i) {
            for (int j = 0; j < mine_num_cloumn; ++j) {
                panel.add(button_array[i][j]);
            }
        }
        frame.add(panel);
    }

    public static class OnRightClick implements MouseListener {

        public void mouseClicked(MouseEvent e) {
            MyButton temp = (MyButton) (e.getSource());
            if (e.getButton() == MouseEvent.BUTTON3) { //right click
                if ((temp.getText()).equals("F")) {
                    --howmany_F;
                    temp.setText("");
                    temp.setBackground(null);
                } else if (temp.getBackground() != Color.gray) {
                    ++howmany_F;
                    temp.setText("F");
                    temp.setBackground(Color.blue);
                }
            } else {
                if (temp.what_is == 'm' && !((temp.getText()).equals("F"))) {
                    for (int i = 0; i < mine_num_row; ++i) {
                        for (int j = 0; j < mine_num_cloumn; ++j) {
                            if (button_array[i][j].what_is == 'm') {
                                button_array[i][j].setText("m");
                                button_array[i][j].setBackground(Color.red);
                            } else {
                                button_array[i][j].setText("" + button_array[i][j].what_is);
                                button_array[i][j].setBackground(Color.gray);
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "You Lose!");
                } else if (Character.isDigit(temp.what_is) && temp.what_is != '0' && !((temp.getText()).equals("F")) && (whether_opened[temp.row][temp.column] == 0)) {
                    whether_opened[temp.row][temp.column] = 1;
                    temp.setText("" + temp.what_is);
                    temp.setBackground(Color.gray);
                } else if (temp.what_is == '0' && (whether_opened[temp.row][temp.column] == 0)) {
                    int row = temp.row;
                    int column = temp.column;
                    int[][] visited = new int[mine_num_row][mine_num_cloumn];
                    howToOpen(row, column, mine_ground, visited);
                    for (int i = 0; i < mine_num_row; ++i) {
                        for (int j = 0; j < mine_num_cloumn; ++j) {
                            if (visited[i][j] == 1 && !((button_array[i][j].getText()).equals("F"))) {
                                whether_opened[i][j] = 1;
                                button_array[i][j].setBackground(Color.gray);
                            } else if (visited[i][j] == 2 && !((button_array[i][j].getText()).equals("F"))) {
                                whether_opened[i][j] = 1;
                                button_array[i][j].setBackground(Color.gray);
                                button_array[i][j].setText("" + button_array[i][j].what_is);
                            }
                        }
                    }
                }
            }
            if (howmany_F == 10) {
                int sum = 0;
                for (int i = 0; i < mine_num_row; ++i) {
                    for (int j = 0; j < mine_num_cloumn; ++j) {
                        sum += whether_opened[i][j];
                    }
                }
                if (sum == 90) {
                    JOptionPane.showMessageDialog(null, "You Win!");
                }
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
