import java.util.ArrayList;

/**
 * Created by mortontc on 10/29/2015.
 */
public class Board {
    public String[][] grid;

    public Board(String[][] layout){
        grid = layout.clone();
    }

    public String get(int x,int y){
        return grid[x][y];
    }

    public Board(Board b){
        String[][] old = b.getGrid();
        String[][] newOne = new String[old.length][old[0].length];
        for(int i = 0; i < old.length; i++){
            for(int j = 0; j < old[i].length; j++){
                newOne[i][j] = old[i][j];
            }
        }
        grid = newOne;
    }

    public String[][] getGrid(){
        return grid;
    }

    public void move(int x1,int y1,int x2,int y2){
        String temp = grid[y1][x1];
        grid[y1][x1] = grid[y2][x2];
        grid[y2][x2] = temp;
    }

    public void move(int x1, int y1, int dir){//0,0 is top left
        if(dir == 1){
            move(x1, y1, x1,y1 - 1);
        }
        else if(dir == 2){
            move(x1, y1, x1 + 1, y1);
        }
        else if(dir == 3){
            move(x1, y1, x1, y1 + 1);
        }
        else if(dir == 4){
            move(x1, y1, x1 - 1, y1);
        }
        else if(dir == 5){
            move(x1, y1, x1 + 1,y1 - 1);
        }
        else if(dir == 6){
            move(x1, y1, x1 + 1, y1 + 1);
        }
        else if(dir == 7){
            move(x1, y1, x1 - 1, y1 + 1);
        }
        else if(dir == 8){
            move(x1, y1, x1 - 1, y1 - 1);
        }

    }

    public Board getFinal(Path move){
        Board end = this;
        ArrayList<Integer> order = move.getOrder();
        int x1 = move.getInitx();
        int y1 = move.getInity();
        for(int i = 0; i < order.size(); i++){
            int dir = order.get(i);
            end.move(x1, y1, dir);
            if(dir == 1){
                y1--;
            }
            else if(dir == 2){
                x1++;
            }
            else if(dir == 3){
                y1++;
            }
            else if(dir == 4){
                x1--;
            }
            else if(dir == 5){
                y1--;
                x1++;
            }
            else if(dir == 6){
                x1++;
                y1++;
            }
            else if(dir == 7){
                y1++;
                x1--;
            }
            else if(dir == 8){
                x1--;
                y1--;
            }
        }
        return end;
    }
}
