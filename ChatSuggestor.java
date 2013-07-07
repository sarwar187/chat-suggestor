/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package languagemodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TriGramNode{
    String tname="";
    int tcount=0;
}
class BigramNode{
    String bName="";
    int bcount=0;
    ArrayList<TriGramNode> tNode = new ArrayList<TriGramNode>();
}
class Node{
    int ucount = 0;
    ArrayList<BigramNode> bNode = new ArrayList<BigramNode>();
}
public class ChatSuggestor {
    
    public static int check(String str){
        int flag = 0;
        if(str.equals("AM")||str.equals("PM"))return 1;
        
        for(int i=0;i<str.length();i++){
            if(('a'>str.charAt(i)||str.charAt(i)>'z')&&('A'>str.charAt(i)||str.charAt(i)>'Z')){
                flag=1;
                break;
            }
        }
        
        return flag;
    }
    public static void main(String args[]) throws FileNotFoundException{
        HashMap<String,Node> bigram = new HashMap<String,Node>();
        int vocabulary=0;
        int tokens=0;
        Scanner sc = new Scanner(new File("D:\\chat.txt"));
        String str="";
        while(sc.hasNextLine()){
            str+=sc.nextLine();
            str+="#";
        }
        //System.out.println(str);
        Pattern p = Pattern.compile("(me: )([A-Za-z \\n0-9:;\\)#?]+)");
        //Matcher m = p.matcher(str);
        
        String[] splitString = (str.split("Riaz: "));
        //System.out.println(splitString.length);// Should be 14
  for (String string : splitString) {
            Matcher m = p.matcher(string);
            String hashString = "";
            if(m.find()){
                hashString = m.group(2);
                hashString = hashString.replace('?', ' ');
                //System.out.println(hashString);
                //hashString.replace('?',' ');
                StringTokenizer st = new StringTokenizer(hashString, "#");
                while(st.hasMoreTokens()){
                    String spaceString = st.nextToken();
                    //spaceString.trim();
                    spaceString = spaceString.trim();
                    if(spaceString.length()==0)continue;
                    StringTokenizer st1 = new StringTokenizer(spaceString);
                    ArrayList<String> finalString = new ArrayList<String>();
                    while(st1.hasMoreTokens()){
                        String checkString = st1.nextToken();
                        if(check(checkString)==0){
                            finalString.add(checkString);
                        }
                    }
                    for(int i=0;i<finalString.size();i++){           //iterating each current word
                        //UNIGRAM FOUND
                        if(bigram.containsKey(finalString.get(i))){  // if hashmap contains the current word check if the next word is in the next list
                            tokens++;                                // increasing total number of tokens seen so far 
                            Node n = bigram.get(finalString.get(i)); // getting the node w.r.t. the current word
                            n.ucount++;                              // increase unigram count of the current word
                            if(finalString.size()-1 == i)            // if the token in finalString is the last token the just dont look fot the next token
                                continue;
                            int flagBigram=0;
                            for(int j=0;j<n.bNode.size();j++){       // check if the next of the current word is available as the tail of the current word
                                if(finalString.get(i+1).equals(n.bNode.get(j).bName)){
                                    //BIGRAM FOUND
                                    flagBigram=1;                          // if flag is 1 then then next word of the current word is available 
                                    n.bNode.get(j).bcount++;         // we increase the count of the next word
                                    if(finalString.size()-2>i){
                                        int flagTrigram=0;
                                        for(int k=0;k<n.bNode.get(j).tNode.size();k++){
                                            if(finalString.get(i+2).equals(n.bNode.get(j).tNode.get(k).tname)){
                                                n.bNode.get(j).tNode.get(k).tcount++;
                                                flagTrigram=1;
                                            }
                                        }
                                        if(flagTrigram==0){
                                            TriGramNode t = new TriGramNode();
                                            t.tname = finalString.get(i+2);
                                            t.tcount = 1;
                                            n.bNode.get(j).tNode.add(t);
                                        }
                                    }
                                }
                            }
                            // NEWLY ADDED
                            /*for(int j=0;j<n.bNode.get(i).bNode.size();j++){ // check if the next of the current word is available
                                if(finalString.get(i+1).equals(n.bNode.get(j).bName)){
                                    flag=1;
                                    n.bNode.get(j).bcount++;
                                }
                            }*/
                            
                            //NEWLY ADDED
                            //IF BIGRAM NOT FOUND
                            if(flagBigram==0){                              // if not available then create a new node and add into the arraylist of Node class
                                BigramNode bGramNode = new BigramNode();
                                bGramNode.bName = finalString.get(i+1);
                                bGramNode.bcount=1;  // create the node with count 1
                                if(finalString.size()-2>i){
                                    TriGramNode t = new TriGramNode();
                                    t.tname = finalString.get(i+2);
                                    t.tcount=1;
                                    bGramNode.tNode.add(t);
                                }
                                n.bNode.add(bGramNode);
                            }
                        }
                        else{                                          // if hashmap does not contain the current word insert it and inset the next word with count
                            //IF UNIGRAM NOT FOUND
                            vocabulary++;
                            tokens++;
                            Node n = new Node();
                            n.ucount=1;
                            if(finalString.size()-1 == i){            // if the token in finalString is the last token the just dont look fot the next token
                                bigram.put(finalString.get(i),n);
                                continue;
                            }
                            BigramNode bGramNode = new BigramNode();
                            //System.out.println("dhukse " + finalString.get(i) + " " + finalString.get(i+1));
                            bGramNode.bName = finalString.get(i+1);
                            bGramNode.bcount = 1;
                            if(finalString.size()-2>i){            // if the token in finalString is the last token the just dont look fot the next token
                                TriGramNode t = new TriGramNode();
                                t.tname = finalString.get(i+2);
                                t.tcount = 1;
                                bGramNode.tNode.add(t);
                            }
                            n.bNode.add(bGramNode);
                            bigram.put(finalString.get(i),n);
                        }
                    }
                }
            }
            //System.out.println(string);
	}
        long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(mem0/(double)1024/1024/8);
        Scanner scInput = new Scanner(System.in);
        while(scInput.hasNextLine()){
            String inputString = scInput.nextLine();
            if(inputString.equals("end"))break;
            StringTokenizer st = new StringTokenizer(inputString);
            String key1="",key2="",temp="";
            while(st.hasMoreTokens()){
                key1 = st.nextToken();
                if(st.hasMoreTokens())key2=st.nextToken();
                else if(key2.length()!=0){
                    temp = key1;
                    key1 = key2;
                    key2 = temp;
                }
            }
            //System.out.println(key1 + " " + key2);
            Node n = bigram.get(key1);
            if(n==null){
                System.out.println("no suggestion");
                continue;
            }
            String finalTriGram = "no trigram";
            double max=-1;
            int flag=0;
            for(int i=0;i<n.bNode.size();i++){
                //System.out.println("key2 ");
                if(key2.length()==0)break;
                if(key2.equals(n.bNode.get(i).bName)){
                    BigramNode b = n.bNode.get(i);
                    if(b.tNode.size()>0)
                        flag=1;
                    for(int k=0;k<b.tNode.size();k++){
                        TriGramNode t = b.tNode.get(k);
                        if(((double)t.tcount/b.bcount)>max){
                            max = (double)t.tcount/b.bcount;
                            finalTriGram = t.tname;
                        }
                    }
                }
                
            }
            if(!finalTriGram.equals("no trigram")){
                //System.out.println("dhukse");
                System.out.println("Suggested Token : " + finalTriGram);
                continue;
            }
            if(flag==0){
                if(key2.length()==0)
                    n = bigram.get(key1);
                else            
                    n = bigram.get(key2);
                if(n==null){
                    System.out.println("no suggestions");
                    continue;
                }
                
                String finalBigram="";
                for(int i=0;i<n.bNode.size();i++){
                    double val = ((double)n.bNode.get(i).bcount+1)/(n.ucount+vocabulary);
                    if(val>=max){
                        finalBigram = n.bNode.get(i).bName;
                        max=val;
                    }
                }
                System.out.println("Suggested token : " + finalBigram);
            
            }
            
        }
        
        new Report(bigram);
    }
}
