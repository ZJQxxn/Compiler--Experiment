public class Word {
    public String tokenId;
    public Object content;

    public Word(){
        this.tokenId="";
        this.content="";
    }

    public Word(String tokenId,String content){
        this.tokenId=tokenId;
        this.content=content;
    }
}
