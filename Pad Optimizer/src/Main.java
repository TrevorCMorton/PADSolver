import java.util.ArrayList;
/*
written by trevor morton
algorithm takes in 2d array representing board and recursively determines best pad move
 */
public class Main {
    public static int boardl = 6;
    public static int boardh = 5;
    public static Board original;
    public static int maxSteps = 22;
    public static int startFilter = 1;
    public static int steps;//for stats
    public static int endStep;//for code
    public static int finals;
    public static boolean diagonals = false;
    public static int bottleneck = maxSteps/2;//if chunk == maxSteps, gets rid of chunking

    public static void main(String[] args) {//get rid of trees whose lower branch results dont match early on
                                            //can also leave out all from 0 to 10 generally with startFilter
                                            //find best half of 10 step, extrapolate those to 15, and so on by passing
                                            //path into tree with the step ended on passed in as well
        long sTime = System.currentTimeMillis();
        steps = 0;
        finals = 0;
        endStep = 0;
        ArrayList<Path> paths = new ArrayList<Path>();
        //String[][] layout = new String[][]{{"a","b","c"},{"d","e","f"},{"g","h","i"}};
        //String[][] layout = new String[][]{{"1","0","0"},{"1","0","1"},{"1","1","1"}};
        //String[][] layout = new String[][]{{"b","b","c"},{"a","c","c"},{"a","a","b"}};
        String[][] layout = new String[][]{ {"d","h","l","b","r","h"},
                                            {"d","r","h","r","r","l"},
                                            {"g","d","h","b","d","r"},
                                            {"h","b","l","l","r","g"},
                                            {"g","l","h","g","r","l"}};
        original = new Board(layout);

        for(int y = 0; y < boardh; y++){
            for(int x = 0; x < boardl; x++){
                for(int p = startFilter; p < bottleneck; p++) {//loop for best path of any length for a specific starting point
                    if(!diagonals) {//if only using cardinal directions
                        Path attempt = tree(0, p, new Path(x, y));//tree returns the optimal path for the given scenario
                        paths.add(attempt);
                    }
                    else{//if including diagonals
                        Path attempt = diagTree(0, p, new Path(x, y));//tree returns the optimal path for the given scenario
                        paths.add(attempt);
                    }

                }
            }
        }
        ArrayList<Path> newPaths = new ArrayList<>();
        for(int x = 0; x < paths.size(); x++){
            for(int p = bottleneck; p < maxSteps; p++) {//loop for best path of any length for a specific starting point
                if(!diagonals) {//if only using cardinal directions
                    Path attempt = tree(bottleneck, p, paths.get(x));//tree returns the optimal path for the given scenario
                    newPaths.add(attempt);
                }
                else{//if including diagonals
                    Path attempt = diagTree(bottleneck, p, paths.get(x));//tree returns the optimal path for the given scenario
                    newPaths.add(attempt);
                }

            }
        }
        Path best = newPaths.get(0);//set best as first path in list so we always choose best path with shortest length
        for(int o = 0; o < newPaths.size(); o++) {//loop evaluating paths to find best
            if(eval(best) < eval(newPaths.get(o))){
                best = newPaths.get(o);
            }
        }
/*
        Path best = start;
        best.add(2);
        best.add(2);
        best.add(3);
*/

        System.out.println("finished in " + (System.currentTimeMillis() - sTime) + "ms with " + steps + " steps and "
                            + finals + " paths and " + eval(best) + " orbs");

        ArrayList<Integer> order = best.getOrder();
        for(int i = 0; i < order.size(); i++){
            System.out.print(order.get(i));
        }
        System.out.println();
        System.out.println(best.getInitx() + " " + best.getInity() + " " + best.getCurrx() + " " + best.getCurry());

        Board end = new Board(original);
        end = end.getFinal(best);

        for(int i = 0; i < boardh; i++){
            for(int j = 0; j < boardl; j++){
                System.out.print(layout[i][j]);
            }
            System.out.println();
        }
        System.out.println();



        String[][] arr = end.getGrid();
        for(int i = 0; i < boardh; i++){
            for(int j = 0; j < boardl; j++){
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
        /*
        for(int i = 0; i < 30; i++){//step
            for(int j = 0; j < 6; j++){//
                for(int k = 0; k < 5; k++) {
                    Path start = new Path(j, k);
                    paths.add(tree(0, i, start));
                }
            }
        }
        */
    }
    public static Path tree(int step, int max, Path pathy){//merge sort-style with set length, go through all lengths and
                                                        //spots if quick enough
                                                        //note this method is blind to the board, exept for eval
                                                        //store path and board as one for eval?  means modifying instance of board in ifs
                                                        //0,0 is top left
        //System.out.println(step);
        steps++;
        ArrayList<Path> options = new ArrayList<Path>();
        if(step == max){
            finals++;
            //System.out.println(step);
            return pathy;
        }
        if(step != 0){
            if (pathy.getCurry() != 0 && pathy.getLast() != 3) {//if not wall up and last wasn't down
                Path next = pathy.add(1);
                //next.add(1);
                options.add(tree(step + 1, max, next));
            }
            if (pathy.getCurrx() != boardl - 1 && pathy.getLast() != 4) {//not wall right and last not left
                Path next = pathy.add(2);
                options.add(tree(step + 1, max, next));
            }
            if (pathy.getCurry() != boardh - 1 && pathy.getLast() != 1) {//not wall down and last not up
                Path next = pathy.add(3);
                options.add(tree(step + 1, max, next));
            }
            if (pathy.getCurrx() != 0 && pathy.getLast() != 2) {//not wall left and last not right
                Path next = pathy.add(4);
                options.add(tree(step + 1, max, next));
            }
        }
        else{
            if (pathy.getCurry() != 0) {//if not wall up and last wasn't down
                Path next = pathy.add(1);
                options.add(tree(step + 1, max, next));
            }
            if (pathy.getCurrx() != boardl - 1) {//not wall right and last not left
                Path next = pathy.add(2);
                options.add(tree(step + 1, max, next));
            }
            if (pathy.getCurry() != boardh - 1) {//not wall down and last not up
                Path next = pathy.add(3);
                options.add(tree(step + 1, max, next));
            }
            if (pathy.getCurrx() != 0) {//not wall left and last not right
                Path next = pathy.add(4);
                options.add(tree(step + 1, max, next));
            }
        }
        Path best = options.get(0);
        for(int i = 0; i < options.size(); i++){
            if(eval(best) < eval(options.get(i))){
                best = options.get(i);
            }
        }
        return best;
    }

    public static Path diagTree(int step, int max, Path pathy){//merge sort-style with set length, go through all lengths and
        //spots if quick enough
        //note this method is blind to the board, exept for eval
        //store path and board as one for eval?  means modifying instance of board in ifs
        //0,0 is top left
        //System.out.println(step);
        steps++;
        ArrayList<Path> options = new ArrayList<Path>();
        if(step == max ){
            finals++;
            return pathy;
        }
        if(step != 0){
            if (pathy.getCurry() != 0 && pathy.getLast() != 3) {//if not wall up and last wasn't down
                Path next = pathy.add(1);
                //next.add(1);
                options.add(diagTree(step + 1, max, next));
            }
            if (pathy.getCurrx() != boardl - 1 && pathy.getLast() != 4) {//not wall right and last not left
                Path next = pathy.add(2);
                options.add(diagTree(step + 1, max, next));
            }
            if (pathy.getCurry() != boardh - 1 && pathy.getLast() != 1) {//not wall down and last not up
                Path next = pathy.add(3);
                options.add(diagTree(step + 1, max, next));
            }
            if (pathy.getCurrx() != 0 && pathy.getLast() != 2) {//not wall left and last not right
                Path next = pathy.add(4);
                options.add(diagTree(step + 1, max, next));
            }
            if((pathy.getCurry() != 0 && pathy.getCurrx() != boardl - 1) && pathy.getLast() != 7){//not wall up or right and last not down left
                Path next = pathy.add(5);
                options.add(diagTree(step + 1, max, next));
            }
            if((pathy.getCurry() != boardh - 1 && pathy.getCurrx() != boardl - 1) && pathy.getLast() != 8){//not wall down or right and last not up left
                Path next = pathy.add(6);
                options.add(diagTree(step + 1, max, next));
            }
            if((pathy.getCurry() != boardh - 1 && pathy.getCurrx() != 0) && pathy.getLast() != 5){//not wall down or left and last not down left
                Path next = pathy.add(7);
                options.add(diagTree(step + 1, max, next));
            }
            if((pathy.getCurry() != 0 && pathy.getCurrx() != 0) && pathy.getLast() != 6){//not wall up or left and last not down left
                Path next = pathy.add(8);
                options.add(diagTree(step + 1, max, next));
            }
        }
        else{
            if (pathy.getCurry() != 0) {//if not wall up
                Path next = pathy.add(1);
                options.add(diagTree(step + 1, max, next));
            }
            if (pathy.getCurrx() != boardl - 1) {//not wall right
                Path next = pathy.add(2);
                options.add(diagTree(step + 1, max, next));
            }
            if (pathy.getCurry() != boardh - 1) {//not wall down
                Path next = pathy.add(3);
                options.add(diagTree(step + 1, max, next));
            }
            if (pathy.getCurrx() != 0) {//not wall left
                Path next = pathy.add(4);
                options.add(diagTree(step + 1, max, next));
            }
            if(pathy.getCurry() != 0 && pathy.getCurrx() != boardl - 1){//not wall up or right
                Path next = pathy.add(5);
                options.add(diagTree(step + 1, max, next));
            }
            if(pathy.getCurry() != boardh - 1 && pathy.getCurrx() != boardl - 1){//not wall down or right
                Path next = pathy.add(6);
                options.add(diagTree(step + 1, max, next));
            }
            if(pathy.getCurry() != boardh - 1 && pathy.getCurrx() != 0){//not wall down or left
                Path next = pathy.add(7);
                options.add(diagTree(step + 1, max, next));
            }
            if(pathy.getCurry() != 0 && pathy.getCurrx() != 0){//not wall up or left
                Path next = pathy.add(8);
                options.add(diagTree(step + 1, max, next));
            }
        }
        Path best = options.get(0);
        for(int i = 0; i < options.size(); i++){
            if(eval(best) < eval(options.get(i))){
                best = options.get(i);
            }
        }
        return best;
    }

    public static int eval(Path attempt){//returns orbs matched with leaning towards kirin outcomes
        Board end = new Board(original);
        end = end.getFinal(attempt);//returns board thats been changed by path
        int orbs = 0;
        ArrayList<String> types = new ArrayList<String>();
        for(int i = 0; i < boardl; i++){
            for(int j = 0; j < boardh; j++) {
                String type = end.get(j, i);
                if (i > 0 && i < boardl - 1 && end.get(j, i - 1).equals(type) && end.get(j, i +1).equals(type)) {//both sides
                    types.add(type);
                    orbs++;
                }
                else if (j > 0 && j < boardh - 1 && end.get(j - 1, i).equals(type) && end.get(j + 1, i).equals(type)) {//top bottom
                    types.add(type);
                    orbs++;
                }
                else if (i > 1 && end.get(j, i - 1).equals(type) && end.get(j, i - 2).equals(type)) {//two left
                    types.add(type);
                    orbs++;
                }
                else if (i < boardl - 2 && end.get(j, i + 1).equals(type) && end.get(j, i + 2).equals(type)) {//two right
                    types.add(type);
                    orbs++;
                }
                else if (j > 1 && end.get(j - 1, i).equals(type) && end.get(j - 2, i).equals(type)) {//two up
                    types.add(type);
                    orbs++;
                }
                else if (j < boardh - 2 && end.get(j + 1, i).equals(type) && end.get(j + 2, i).equals(type)) {//two down
                    types.add(type);
                    orbs++;
                }

            }
        }
        if(types.contains("l") && types.contains("b") && types.contains("r") && types.contains("g")){
            return 1000 + orbs;
        }
        else {
            return orbs;
        }
    }
}
