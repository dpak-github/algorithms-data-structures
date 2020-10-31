import java.lang.reflect.Array;
import java.util.*;

public class AutocompleteSystem {
    /**
     * Your AutocompleteSystem object will be instantiated and called as such:
     * AutocompleteSystem obj = new AutocompleteSystem(sentences, times);
     * List<String> param_1 = obj.input(c);
     */

    Map<String, Integer>  countMap = new HashMap<>();
    Trie trie = new Trie();
    StringBuilder  prefix = new StringBuilder(); // to store current prefix
    public AutocompleteSystem(String[] sentences, int [] times){
        for(int i=0; i<sentences.length; i++){
            countMap.put(sentences[i], times[i]);
            trie.insert(sentences[i]);
        }
    }

    List<String> input(char c){
        List<String> result = new ArrayList<>();
        if(c == '#'){
            trie.insert(prefix.toString());
            countMap.put(prefix.toString(), countMap.getOrDefault(prefix.toString(),0)+1);
            prefix = new StringBuilder();
        }
        else{
            prefix.append(c);
            result = getTopSuggestions();
        }
        return result;
    }

    private List<String> getTopSuggestions(){
        List<String> result = new ArrayList<>();
        TrieNode prefixNode = trie.search(prefix.toString());
        if(prefixNode != null){
            PriorityQueue<String> queue = new PriorityQueue<>(new Comparator<String>() {
                @Override
                public int compare(String a, String b) {
                    return countMap.get(a)!= countMap.get(b) ? countMap.get(a)-countMap.get(b) : b.compareTo(a);
                }
            });
            prefixNode.wordsWithPrefix.forEach(p->{
                queue.offer(p);
                if(queue.size()>3) queue.poll();
            });
            while(!queue.isEmpty()){
                result.add(0, queue.poll());
            }
        }
        return result;
    }

    class Trie{

        TrieNode root;

        public Trie(){
            root = new TrieNode();
        }

        public void insert(String sentence){
            TrieNode curr = root;
            for(int i=0; i<sentence.length(); i++){
                char c = sentence.charAt(i);
                if(curr.next(c)==null){
                    curr.children.put(c, new TrieNode());
                }
                curr.next(c).wordsWithPrefix.add(sentence);
                curr = curr.next(c);
            }
            curr.isEnd = true;
        }

        public TrieNode search(String prefix){
            TrieNode curr = root;
            for(int i=0; i<prefix.length(); i++){
                if(curr.next(prefix.charAt(i))==null){
                    return null;
                }
                curr = curr.next(prefix.charAt(i));
            }
            return curr;
        }

    }
    class TrieNode{
        boolean isEnd;
        Map<Character, TrieNode> children;
        List<String> wordsWithPrefix;

        public TrieNode (){
            children = new HashMap<>();
            isEnd = false;
            wordsWithPrefix = new ArrayList<>();
        }

        public TrieNode next(Character c){
            return this.children.get(c);
        }
    }

    public static void main(String[] args) {
        String [] strings = {"hey", "hey the", "hey stranger", "hey tan", "hey man"};
        int [] counts = {5,2,1,2, 2};
        AutocompleteSystem atc = new AutocompleteSystem(strings, counts);

        String searchString = "hey there#";

        for(int i = 0; i<searchString.length(); i++){
            List<String> results = atc.input(searchString.charAt(i));
            System.out.println("Current character " + searchString.charAt(i));
            for (String result : results) {
                System.out.println(result);
            }
            System.out.println("====================================");
        }

        searchString = "hey there";

        for(int i = 0; i<searchString.length(); i++){
            List<String> results = atc.input(searchString.charAt(i));
            System.out.println("Current character " + searchString.charAt(i));
            for (String result : results) {
                System.out.println(result);
            }
            System.out.println("====================================");
        }
    }
}

/*
example question : https://leetcode.com/problems/design-search-autocomplete-system/
 */
