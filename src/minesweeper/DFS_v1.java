package minesweeper;

public class DFS_v1 {
    public static void main(String[] args)
    {   int row = 1;
        int column = 8;
        char[][] mine_ground = {{'0','0','0','0','0','0','0','0','0','0'},
                                {'0','0','0','0','0','0','0','0','0','0'},
                                {'0','0','0','0','0','0','0','0','0','0'},
                                {'1','1','0','1','1','1','0','1','1','1'},
                                {'0','1','0','1','0','1','0','1','0','0'},
                                {'0','1','0','1','0','1','0','1','0','0'},
                                {'0','1','0','1','0','1','0','1','0','0'},
                                {'0','1','1','1','0','1','1','1','0','0'},
                                {'0','0','0','0','0','0','0','0','0','0'},
                                {'0','0','0','0','0','0','0','0','0','0'},};
        int[][] zero_panel = new int[10][10];
        int[] point_row_backup = new int[100];
        int[] point_column_backup = new int[100];  
        int which_point = 0;
        point_row_backup[0] = row;
        point_column_backup[0] = column;
        findx(row, column, mine_ground, zero_panel,point_row_backup,point_column_backup,which_point);
        for(int i = 0; i<10; ++i)
        {
            for(int j = 0; j<10; ++j)
            {
                System.out.print(zero_panel[i][j]);
            }
            System.out.println();
        }
    }
    
    public static void findx(int row, int column, char[][] mine_ground, int[][] zero_panel,int[] point_row_backup,int[] point_column_backup,int which_point)
    {
        while(which_point>=0)
        {
            if(row-1>=0 && mine_ground[row-1][column]=='0' && zero_panel[row-1][column]==0)//安全第一
            {
                --row;
                if(row-1>=0 && mine_ground[row-1][column] !='0')
                    zero_panel[row-1][column] = 2;
                if(column+1<=9 && mine_ground[row][column+1] != '0')
                    zero_panel[row][column+1] = 2;
                if(row+1<=9 && mine_ground[row+1][column]!='0')
                    zero_panel[row+1][column] = 2;
                if(column-1>=0 && mine_ground[row][column-1] != '0')
                    zero_panel[row][column-1] = 2;
                ++which_point;
                point_row_backup[which_point]=row;
                point_column_backup[which_point]=column; 
                zero_panel[row][column] = 1;
                findx(row, column, mine_ground, zero_panel,point_row_backup,point_column_backup,which_point);
            }
            if(column+1<=9 && mine_ground[row][column+1] == '0' && zero_panel[row][column+1]==0)
            {
                ++column;
                if(row-1>=0 && mine_ground[row-1][column] !='0')
                    zero_panel[row-1][column] = 2;
                if(column+1<=9 && mine_ground[row][column+1] != '0')
                    zero_panel[row][column+1] = 2;
                if(row+1<=9 && mine_ground[row+1][column]!='0')
                    zero_panel[row+1][column] = 2;
                if(column-1>=0 && mine_ground[row][column-1] != '0')
                    zero_panel[row][column-1] = 2;                
                ++which_point;
                point_row_backup[which_point]=row;
                point_column_backup[which_point]=column;             
                zero_panel[row][column] = 1;
                findx(row, column, mine_ground, zero_panel,point_row_backup,point_column_backup,which_point);
            }
            if(row+1<=9 && mine_ground[row+1][column]=='0' && zero_panel[row+1][column]==0)
            {
                ++row;
                if(row-1>=0 && mine_ground[row-1][column] !='0')
                    zero_panel[row-1][column] = 2;
                if(column+1<=9 && mine_ground[row][column+1] != '0')
                    zero_panel[row][column+1] = 2;
                if(row+1<=9 && mine_ground[row+1][column]!='0')
                    zero_panel[row+1][column] = 2;
                if(column-1>=0 && mine_ground[row][column-1] != '0')
                    zero_panel[row][column-1] = 2;                
                ++which_point;
                point_row_backup[which_point]=row;
                point_column_backup[which_point]=column;                
                zero_panel[row][column] = 1;
                findx(row, column, mine_ground, zero_panel,point_row_backup,point_column_backup,which_point);
            }
            if(column-1>=0 && mine_ground[row][column-1] == '0' && zero_panel[row][column-1]==0)
            {
                --column;
                if(row-1>=0 && mine_ground[row-1][column] !='0')
                    zero_panel[row-1][column] = 2;
                if(column+1<=9 && mine_ground[row][column+1] != '0')
                    zero_panel[row][column+1] = 2;
                if(row+1<=9 && mine_ground[row+1][column]!='0')
                    zero_panel[row+1][column] = 2;
                if(column-1>=0 && mine_ground[row][column-1] != '0')
                    zero_panel[row][column-1] = 2;                
                ++which_point;
                point_row_backup[which_point]=row;
                point_column_backup[which_point]=column;          
                zero_panel[row][column] = 1;
                findx(row, column, mine_ground, zero_panel,point_row_backup,point_column_backup,which_point);
            }
            --which_point;
            if(which_point>=0)
            {
            row = point_row_backup[which_point];
            column = point_column_backup[which_point];
            }
        }
    }
}
