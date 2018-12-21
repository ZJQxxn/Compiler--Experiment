import java.util.*;

/**
 * Class for syntax parser
 */
public class SyntaxParser {
    private Node root=new Node();
    private static Scanner scanner=new Scanner(System.in);

    /**
     * Main menu of syntax parser.
     */
    public void mainMenu(){
        System.out.println("Input your code:");
        String str=scanner.nextLine();
        if(!str.endsWith("#")){
            System.out.println("A statement must end with # .");
            return;
        }
        try {
            parse(str);
            printResult();
        }
        catch (ParserException ex){
            System.err.println(ex.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Syntax parsing function.
     * @param str A string of statement
     * @throws ParserException Exception
     */
    private void parse(String str)throws ParserException{
        root.setContent("S");
        Stack<String> chStack=new Stack<>();
        chStack.push("#");
        Stack<String> genStack=new Stack<>();
        genStack.push("#");
        genStack.push("S");
        LinkedList<Node> nodeQueue=new LinkedList<>();
        nodeQueue.addLast(root);
        String[] tokens=str.substring(0,str.length()-1).
                split(" ");
        chStack.push(tokens[0]);
        Node curNode=nodeQueue.getFirst();
        for(int index=0;;){
            if (nodeQueue.isEmpty()){
                break;
            }
            curNode=nodeQueue.getFirst();
            //Match correctly
            if (chStack.peek().equals(genStack.peek())){
                if(chStack.peek().equals("#")){
                    break;
                }
                chStack.pop();
                genStack.pop();
                index++;
                if (index>=tokens.length){
                    break;
                }
                chStack.push(tokens[index]);
            }
            String each=chStack.peek();
            //Is number
            if (each.matches("[0-9]+")){
                if (genStack.peek().equals("S")){
                    genStack.pop();
                    genStack.push("SP");
                    genStack.push("T");
                    genStack.push("SP");
                    Node left=new Node("SP");
                    Node middle=new Node("T");
                    Node right=new Node("SP");
                    curNode.add(left);
                    curNode.add(middle);
                    curNode.add(right);
                    nodeQueue.remove(0);
                    nodeQueue.addFirst(right);
                    nodeQueue.addFirst(middle);
                    nodeQueue.addFirst(left);
                }
                else if(genStack.peek().equals("SP")){
                    genStack.pop();
                    genStack.push(each);
                    curNode.add(each);
                    nodeQueue.remove(0);
                }
                else{
                    throw new ParserException("Unexpected character.");
                }
            }
            else if(each.matches("[a-z]+")){
                if (genStack.peek().equals("S")){
                    genStack.pop();
                    genStack.push("SP");
                    genStack.push("T");
                    genStack.push("SP");

                    Node left=new Node("SP");
                    Node middle=new Node("T");
                    Node right=new Node("SP");
                    curNode.add(left);
                    curNode.add(middle);
                    curNode.add(right);
                    nodeQueue.remove(0);
                    nodeQueue.addFirst(right);
                    nodeQueue.addFirst(middle);
                    nodeQueue.addFirst(left);
                }
                else if(genStack.peek().equals("SP")){
                    genStack.pop();
                    genStack.push(each);
                    curNode.add(each);
                    nodeQueue.remove(0);
                }
                else{
                    throw new ParserException("Unexpected character.");
                }
            }
            else if (isRelop(each)){
                if(genStack.peek().equals("T")) {
                    genStack.pop();
                    genStack.push(each);
                    curNode.add(each);
                    nodeQueue.remove(0);
                }
                else{
                    throw new ParserException("Unexpected character.");
                }
            }
            else if(isOp(each)){
                if(genStack.peek().equals("T")) {
                    genStack.pop();
                    genStack.push(each);
                    curNode.add(each);
                    nodeQueue.remove(0);
                }
                else{
                    throw new ParserException("Unexpected character.");
                }
            }
            else if(each.equals("(")){
                if (genStack.peek().equals("S")){
                    genStack.pop();
                    genStack.push("SP");
                    genStack.push("T");
                    genStack.push("SP");

                    Node left=new Node("SP");
                    Node middle=new Node("T");
                    Node right=new Node("SP");
                    curNode.add(left);
                    curNode.add(middle);
                    curNode.add(right);
                    nodeQueue.remove(0);
                    nodeQueue.addFirst(right);
                    nodeQueue.addFirst(middle);
                    nodeQueue.addFirst(left);
                }
                else if(genStack.peek().equals("SP")){
                    genStack.pop();
                    genStack.push(")");
                    genStack.push("SP");
                    genStack.push("T");
                    genStack.push("SP");
                    genStack.push("(");

                    Node left=new Node("SP");
                    Node middle=new Node("T");
                    Node right=new Node("SP");
                    curNode.add("(");
                    curNode.add(left);
                    curNode.add(middle);
                    curNode.add(right);
                    curNode.add(")");
                    nodeQueue.remove(0);
                    nodeQueue.addFirst(right);
                    nodeQueue.addFirst(middle);
                    nodeQueue.addFirst(left);
                }
                else{
                    throw new ParserException("Unexpected character.");
                }
            }
            else if(each.equals(")")){
                continue;
            }
            else{
                System.out.println(each);
                throw new ParserException("Unexpected character.");
            }
        }
     }

    /**
     * Print out parsing tree
     * @throws ParserException
     */
    private void printResult() throws ParserException{
        LinkedList<Node> nodeQueue=new LinkedList<>();
        nodeQueue.addLast(root);
        Integer nodeID=1;
        String fmt="================ Node %d==============";
        while(!nodeQueue.isEmpty()){
            System.out.println(String.format(fmt,nodeID));
            Node curNode=nodeQueue.get(0);
            nodeQueue.remove(0);
            System.out.println("Current Node "+curNode.getContent());
            if(isGen(curNode.getContent())) {
                ArrayList<Node> children = curNode.getChild();
                System.out.println("---------- children nodes ----------");
                for (Node each : children) {
                    nodeQueue.addLast(each);
                    System.out.println(each.getContent());
                }
            }
            nodeID += 1;
        }
    }

    /**
     * Determin whether character is a relation operator
     * @param str Character
     * @return Whether it is relation operator.
     */
    private static boolean isRelop(String str){
        return str.equals("<")||str.equals(">");
    }

    /**
     * Determin whether character is a operator
     * @param str Character
     * @return Whether it is operator.
     */
    private static boolean isOp(String str){
        return str.equals("+")||str.equals("-")||str.equals("*")||str.equals("/");
    }

    /**
     * Determin whether character is a generator
     * @param str Character
     * @return Whether it is relation generator.
     */
    private static boolean isGen(String str){
        return str.equals("S")||str.equals("SP")||str.equals("T");
    }
}
