import java.util.*;

public class LexicalAnalyzer {
    private static List<String> keyWords=new ArrayList<String>(){
        {
            add("if");
            add("then");
            add("else");
            add("while");
            add("do");
        }
    };

    private static Scanner scanner=new Scanner(System.in);

    public static void mainMenu(){
        System.out.println("Input your code:");
        String str=scanner.nextLine();
        if(!str.endsWith("#")){
            System.out.println("A statement must end with # .");
            return;
        }
        try {
            ArrayList<Word> words = analyze(str);
            printResult(words);
        }
        catch (AnalyzerException ex){
            System.err.println(ex.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<Word> analyze(String str) throws AnalyzerException{
        ArrayList<Word> words=new ArrayList<>();
        char[] chars=str.toCharArray();
        char tempCh;
        for(int i=0;i<chars.length;){
            tempCh=chars[i];
            //End of statement
            if (tempCh=='#'){
                break;
            }
            if(tempCh=='_'){
                throw new AnalyzerException("Wrong format of identifier.");
            }
            while(tempCh==' '){
                tempCh=chars[++i];
                continue;
            }
            //Check identifiers and keywords.
            if (Character.isLetter(tempCh)) {
                String tempStr = "";
                tempCh = chars[i++];
                while (Character.isLetter(tempCh) || Character.isDigit(tempCh) || tempCh == '_') {
                    tempStr += tempCh;
                    tempCh = chars[i++];
                }
                if (tempCh != ' ' && tempCh != '#') {
                    throw new AnalyzerException("Wrong format of identifier.");
                } else {
                    if (keyWords.contains(tempStr)) {
                        words.add(new Word("KEYWORD", tempStr));
                        continue;
                    } else {
                        words.add(new Word("VAR", tempStr));
                        continue;
                    }
                }
            }
            //Check digits.
            if (Character.isDigit(tempCh)) {
                String tempStr="";
                tempCh=chars[i];
                while(Character.isDigit(tempCh) || tempCh=='.'){
                    if(tempCh=='.' && tempStr.contains(".")){
                        throw new AnalyzerException("Wrong format of digit.");
                    }
                    tempStr+=tempCh;
                    tempCh=chars[++i];
                }
                if (tempCh!=' ' && tempCh!='#'){
                    throw new AnalyzerException("Wrong format of identifier.");
                }
                else {
                    words.add(new Word("NUM",tempStr));
                    continue;
                }
            }
            //Check operator
            if(isOperator(tempCh)){
                String tempStr=""+tempCh;
                if (chars[i+1]!=' ' && chars[i+1]!='#'){
                    throw new AnalyzerException("Unexpected character.");
                }
                else{
                    words.add(new Word("OP",tempStr));
                    i++;
                }
            }

             //Check relationship operators and = .
            if(isRelop(tempCh)){
                String tempStr="";
                switch(tempCh){
                    case '<':
                        tempStr="<";
                        tempCh=chars[i+1];
                        if(tempCh=='='){
                            tempStr="<=";
                        }
                        else if(tempCh==' '){
                            break;
                        }
                        else{
                            throw new AnalyzerException("Unexpected character.");
                        }
                        break;
                    case '=':
                        tempStr="=";
                        tempCh=chars[i+1];
                        if (tempCh=='='){
                            tempStr="==";
                        }
                        else if(tempCh==' '){
                            break;
                        }
                        else{
                            throw new AnalyzerException("Unexpected character.");
                        }
                        break;
                    case '>':
                        tempStr=">";
                        tempCh=chars[i+1];
                        if (tempCh=='='){
                            tempStr=">=";
                        }
                        else if(tempCh==' '){
                            break;
                        }
                        else{
                            throw new AnalyzerException("Unexpected character.");
                        }
                        break;
                }
                if (tempStr=="="){
                    words.add(new Word("ASSIGN","="));
                }
                else{
                    words.add(new Word("RELOP",tempStr));
                }
                i+=tempStr.length();
            }
        }

        return words;
    }

    private static void printResult(ArrayList<Word> words){
        System.out.println("===========================");
        System.out.println("Result of lexical analyzer:");
        for (Word each:words){
            System.out.println(String.format("< %s , %s >",each.tokenId,each.content.toString()));
        }
        System.out.println("===========================");
    }

    private static Boolean isRelop(char ch){
        return (ch=='=' || ch=='<' || ch=='>');
    }

    private static Boolean isOperator(char ch){
        return (ch=='+' || ch=='-' || ch=='*' || ch=='/');
    }

}
