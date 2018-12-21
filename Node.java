import java.util.ArrayList;

/**
 * Class for node of parsing tree
 */
public class Node {
    private String content;
    private ArrayList<Node> children=new ArrayList<>();

    public Node(String s){
        this.content=s;
    }

    public Node(){

    }

    public void add(String c){
        Node temp=new Node();
        temp.setContent(c);
        this.children.add(temp);
    }

    public void add(Node n){
        this.children.add(n);
    }

    public String getContent(){
        return this.content;
    }

    public void setContent(String str){
        this.content=str;
    }

    public ArrayList<Node> getChild(){
        return children;
    }
}
