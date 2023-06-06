import java.util.*;

public class Fa {
    private List<String> Q = new ArrayList<>();
    private List<String> Sigma =new ArrayList<>();
    private Map<List<String>, List<String>> delta = new HashMap<>();
    private String startState;
    private List<String> finalState =new ArrayList<>();
    private Map<String, String> stateNames = new HashMap<>(); // new mapping


    public Fa() {
    }

    public Fa(List<String> q, List<String> sigma, Map<List<String>, List<String>> delta, String startState, List<String> finalState) {
        Q = q;
        Sigma = sigma;
        this.delta = delta;
        this.startState = startState;
        this.finalState = finalState;
    }

    public void print(){
        System.out.print("StateSet = { ");
        System.out.print(Q.toString());
        System.out.println("}");

        System.out.print("TerminalSet :{ ");
        System.out.print(Sigma.toString());
        System.out.println("}");

        System.out.println("DeltaFunctions = {");
        for (Map.Entry<List<String>, List<String>> entry : delta.entrySet()) {
            List<String> key = entry.getKey();
            List<String> values = entry.getValue();

            System.out.print(key +"= "+ values);

            System.out.println();
        }
        System.out.println("}");

        System.out.println("start state: " + (startState));
        System.out.print("end state: ");
        for (String s:finalState
             ) {
            System.out.print(s);
        }
    }

    public void assignStateNames() {
        int stateCounter = 0;
        for (String state : Q) {
            stateNames.put(state, "State" + stateCounter++);
        }
    }

    public void printSimple() {
        assignStateNames();

        System.out.print("\nStateSet = {");
        for (String q : Q) {
            System.out.print("\t" + stateNames.get(q));
        }
        System.out.println("}");

        System.out.print("TerminalSet :{ ");
        System.out.print(Sigma.toString());
        System.out.println("}");

        System.out.println("DeltaFunctions = {");
        for (Map.Entry<List<String>, List<String>> entry : delta.entrySet()) {
            List<String> key = entry.getKey();
            List<String> values = entry.getValue();
            String newKey = "( "+stateNames.get(key.get(0))+", "+ key.get(1)+" )";
            String newValue = stateNames.get(values.toString());
            System.out.print(newKey +" = "+ newValue);

            System.out.println();
        }
        System.out.println("}");

        // ... print the other components similarly

        System.out.println("start state: " + stateNames.get(startState));

        System.out.print("end state: ");
        for (String s: finalState) {
            System.out.print("\t" + stateNames.get(s));
        }
    }

    public void removeSigma(String s){
        Sigma.remove(s);
    }

    public List<String> getQ() {
        return Q;
    }

    public void setQ(List<String> q) {
        Q = q;
    }

    public List<String> getSigma() {
        return Sigma;
    }

    public void setSigma(List<String> sigma) {
        Sigma = sigma;
    }

    public Map<List<String>, List<String>> getDelta() {
        return delta;
    }

    public void setDelta(Map<List<String>, List<String>> delta) {
        this.delta = delta;
    }

    public String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public List<String> getFinalState() {
        return finalState;
    }

    public void setFinalState(List<String> finalState) {
        this.finalState = finalState;
    }

    public void setFinalState(String finalState) {
        this.finalState.add(finalState);
    }
}
