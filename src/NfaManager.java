import java.util.Stack;

public class NfaManager {
    private final int NFA_MAX = 256;
    private Node[] nfaStatesArr = null;
    private Stack<Node> nfaStack = null;
    private int nextAlloc = 0;
    private int nfaStates = 0;

    public NfaManager()  {
        nfaStatesArr = new Node[NFA_MAX];
        for (int i = 0; i < NFA_MAX; i++) {
            nfaStatesArr[i] = new Node();
        }

        nfaStack = new Stack<Node>();

    }

    public Node newNfa()  {
        Node nfa = null;
        if (nfaStack.size() > 0) {
            nfa = nfaStack.pop();
        }
        else {
            nfa = nfaStatesArr[nextAlloc];
            nextAlloc++;
        }

        //초기화 한뒤 E 연결
        nfa.clearState();
        nfa.setState(nfaStates++);
        nfa.setEdge(Node.EPSILON);

        return nfa;
    }

    public int getNfaStates() {
        return nfaStates;
    }
}