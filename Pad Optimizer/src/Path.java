import java.util.ArrayList;

/**
 * Created by mortontc on 10/29/2015.
 */
public class Path {
    public ArrayList<Integer> order;
    public int initx;
    public int inity;
    public int currx;
    public int curry;
    //1 up, 2 right, 3 down, 4 left, 5 up right, 6 down right, 7 down left, 8 up left
    public Path(int x,int y){//starts path at coordinates given, 0,0 is top left
        order = new ArrayList<Integer>();
        initx = x;
        inity = y;
        currx = x;
        curry = y;
    }
    public Path(int x1, int y1, int x2, int y2){
        order = new ArrayList<Integer>();
        initx = x1;
        inity = y1;
        currx = x2;
        curry = y2;
    }

    public Path add(int dir){//1 up, 2 right, 3 down 4 left
        Path next = new Path(this.getInitx(), this.getInity(), this.getCurrx(), this.getCurry());
        ArrayList<Integer> list = this.getOrder();
        for(int i = 0; i < list.size(); i++){
            next.order.add(list.get(i));
        }
        next.order.add(dir);
        if(dir == 1){
            next.curry--;
        }
        else if(dir == 2){
            next.currx++;
        }
        else if(dir == 3){
            next.curry++;
        }
        else if(dir == 4){
            next.currx--;
        }
        else if(dir == 5){
            next.currx++;
            next.curry--;
        }
        else if(dir == 6){
            next.currx++;
            next.curry++;
        }
        else if(dir == 7){
            next.currx--;
            next.curry++;
        }
        else if(dir == 8){
            next.currx--;
            next.curry--;
        }
        return next;
    }

    public int getLast(){
        return order.get(order.size()-1);
    }

    public int getCurrx(){
        return currx;
    }

    public int getCurry(){
        return curry;
    }

    public int getInitx(){
        return initx;
    }

    public int getInity(){
        return inity;
    }

    public ArrayList<Integer> getOrder(){
        return order;
    }
}
