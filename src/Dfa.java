import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;


public class Dfa {
    private Pair pair;
    private String[] letter;
    private Map<Set<Integer>, Integer> map;
    private Set<Integer> tempset;
    private Queue<Integer> queue = new LinkedList<>();
    private List<Character[]> dfa = new ArrayList<>();
    private List<Character> endState = new ArrayList<>();

    private int state = 'A';

    public Dfa(Pair pair, String[] letter) {
        this.pair = pair;
        this.letter = letter;

        map = new HashMap<>();
    }

    public List<Character[]> getDFA() {
        List<Character[]> redfa = new ArrayList<>();
        for (Character[] ch : dfa) {
            if(getSet(ch[0])==null||getSet(ch[0]).isEmpty()) {
                continue;
            }
            else {
                Character[] newch = new Character[ch.length];
                for(int i=0;i<ch.length;i++) {
                    if(ch[i]==null)
                        continue;
                    Set<Integer> set = getSet(ch[i]);
                    if(set == null||set.isEmpty())
                        newch[i] = null;
                    else
                        newch[i] = ch[i];
                }
                redfa.add(newch);
            }
        }
        return redfa;
    }

    public List<Character> getEndState() {
        return endState;
    }

    public String[] getLetter() {
        return letter;
    }

    public void printDFA() {
        System.out.println();
        System.out.println("--------DFA--------");

        for (Entry<Set<Integer>, Integer> entry : map.entrySet()) {
            if(entry.getValue()==-1)
                continue;
            System.out.println((char)entry.getValue().intValue() +" = " + entry.getKey() + (isStart(entry.getKey())?" START ":"") + (isEnd(entry.getKey())?" END ":"") );
        }
        System.out.println("--------DFA--------");
    }

    private boolean isStart(Set<Integer> set) {
        for (Integer integer : set) {
            if(integer == pair.startNode.getState())
                return true;
        }
        return false;
    }

    private boolean isEnd(Set<Integer> set) {
        for (Integer integer : set) {
            if(integer == pair.endNode.getState()) {
                endState.add(new Character((char)getCharacter(set).intValue()));
                return true;
            }
        }
        return false;
    }

    public void createDFA() {
        tempset = new HashSet<>();
        Set<Integer> start= move(pair.startNode,-1);
        map.put(start, state);
        queue.add(state++);
        while(!queue.isEmpty()) {
            Character[] dfaline = new Character[letter.length-1];
            int character = queue.poll();
            System.out.println();
            System.out.println((char)character);
            dfaline[0] = (char)character;
            Set<Integer> set = getSet(character);
            for(int i=1;i<letter.length-1;i++) {
                tempset = new HashSet<>();
                Set<Integer> midset = new HashSet<>();
                for (Integer integer : set) {
                    Node node = getNode(pair.startNode, integer);
                    revisit();
                    if(node==null) {
                        continue;
                    }
                    else if((char)node.getEdge() == letter[i].charAt(0)) {
                        midset.add(node.next.getState());
                    }
                }
                for (Integer integer : midset) {
                    Node node = getNode(pair.startNode, integer);
                    revisit();
                    move(node, -1);
                }
                Integer c = getCharacter(tempset);
                if(c==null) {
                    if(tempset.isEmpty()) {
                        map.put(tempset, -1);
                        System.out.println("null");
                        dfaline[i] = null;
                    }
                    else {
                        queue.add(state);
                        System.out.println(dfaline[i]);
                        dfaline[i] = (char)state;
                        map.put(tempset, state++);
                    }
                }
                else {
                    if(c==-1) {
                        System.out.println("null");
                        dfaline[i] = null;
                    }
                    else {
                        dfaline[i] = (char)c.intValue();
                        System.out.println(dfaline[i]);
                    }
                }
            }
            dfa.add(dfaline);
        }
    }

    private Set<Integer> move(Node startNode, int i) {
        connect(startNode, i);
        revisit();
        return tempset;
    }

    private void connect(Node node, int i) {
        if(node==null||node.isVisited())
            return;
        node.setVisited();
        tempset.add(node.getState());
        if(node.getEdge()==-1||node.getEdge()==i) {
            connect(node.next, i);
            connect(node.next2, i);
        }
        else
            return;
    }

    private Node getNode(Node node, int startstate) {
        if (node == null || node.isVisited())
            return null;
        node.setVisited();
        if (node.getState() == startstate)
            return node;
        if (node.getState() > startstate)
            return null;
        Node temp1 = getNode(node.next, startstate);
        Node temp2 = getNode(node.next2, startstate);
        if (temp1 != null)
            return temp1;
        if (temp2 != null)
            return temp2;
        return null;
    }

    private Integer getCharacter(Set<Integer> set) {
        return map.get(set);
    }

    private Set<Integer> getSet(int character) {
        for (Entry<Set<Integer>, Integer> m :map.entrySet())  {
            if(m.getValue()==character)
                return m.getKey();
        }
        return null;
    }

    private void revisit(Node node) {
        if (node == null || !node.isVisited()) {
            return;
        }
        node.setUnVisited();
        revisit(node.next);
        revisit(node.next2);
    }
    private void revisit() {
        pair.startNode.setUnVisited();
        revisit(pair.startNode.next);
        revisit(pair.startNode.next2);
    }

    @Override
    public String toString() {
        return tempset.toString();
    }
}
