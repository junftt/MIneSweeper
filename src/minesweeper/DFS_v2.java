package minesweeper;

public class DFS_v2
{   
    public static void howToDisplayWhenZero(int row, int column, char[][] mime_map, int[][] visited)//DFS
    {
        if((row<0||row>9) || (column<0||column>9) || (visited[row][column]!=0))
        {
            return;
        }
        char temp = mime_map[row][column];
        if (temp == 'm'){
            visited[row][column]=3;
            return;
        } else if (temp!='m' && temp!='0'){
            visited[row][column]=2;
            return;
        } else if (temp=='0') {
            visited[row][column]=1;
            howToDisplayWhenZero(row-1, column, mime_map, visited);
            howToDisplayWhenZero(row, column+1, mime_map, visited);
            howToDisplayWhenZero(row+1, column, mime_map, visited);
            howToDisplayWhenZero(row, column-1, mime_map, visited);
        }
    }
}