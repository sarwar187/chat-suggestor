/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package languagemodel;

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author sarwar
 */
public class Report {
    Report(HashMap<String,Node> bigram){
        Iterator a = bigram.keySet().iterator();
        int unigrams = 0;
        while(a.hasNext()){
            String str1 = (String) a.next();
            Node n = bigram.get(str1);
            if(n.ucount>=5)
                System.out.println("unigram " + str1 + " " + n.ucount);
            unigrams++;
        }
        //System.out.println("Total number of unigrams " + count);
        int bigrams =0;
        a = bigram.keySet().iterator();
        while(a.hasNext()){
            String str1 = (String) a.next();
            Node n = bigram.get(str1);
            for(int i=0;i<n.bNode.size();i++){
                BigramNode b = n.bNode.get(i);
                if(b.bcount>=3)
                    System.out.println("bigram " + str1 + " " + b.bName + " " + b.bcount);
                bigrams++;
            }
        }
        //System.out.println("Total number of bigrams " + count);
        int trigrams = 0;
        a = bigram.keySet().iterator();
        while(a.hasNext()){    
            String str1 = (String) a.next();
            Node n = bigram.get(str1);
            for(int i=0;i<n.bNode.size();i++){
                BigramNode b = n.bNode.get(i);
                
                for(int j=0;j<b.tNode.size();j++){
                    TriGramNode t = b.tNode.get(j);
                    if(t.tcount>=2)
                        System.out.println("trigram " + str1 + " " + b.bName + " "+ t.tname + " " + t.tcount);
                    trigrams++;
                }
                //System.out.println("check " + n.bNode.get(i).bName + n.bNode.get(i).bcount);
            }
        }
        System.out.println("Total number of unigrams " + unigrams);
        System.out.println("Total number of bigrams " + bigrams);
        System.out.println("Total number of trigrams " + trigrams);
        
    }
    
}
