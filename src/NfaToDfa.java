import java.util.*;
import java.util.stream.Collectors;

public class NfaToDfa {

    Fa eNfa;
    public NfaToDfa(Fa eNfa) {
        this.eNfa = eNfa;
    }

    public Fa convert() {
        Map<List<String>, List<String>> dfaTransitions = new HashMap<>();
        Set<Set<String>> dfaStates = new HashSet<>();
        Set<String> dfaFinalStates = new HashSet<>();

        Set<String> startState = eClose(eNfa.getStartState(), eNfa.getDelta());
        dfaStates.add(startState);
        if (isFinalState(startState, eNfa.getFinalState())) {
            dfaFinalStates.add(startState.toString());
        }
        eNfa.removeSigma("ε");
        Queue<Set<String>> queue = new LinkedList<>();
        queue.add(startState);

        while (!queue.isEmpty()) {
            Set<String> currentState = queue.poll();

            for (String input : eNfa.getSigma()) {

                Set<String> nextState = new HashSet<>();
                for (String compositeState : currentState) {
                    // Split the state into individual states
                    String[] states = compositeState.split(",");
                    for (String state : states) {
                        List<String> transitionKey = Arrays.asList(state, input);
                        if (eNfa.getDelta().containsKey(transitionKey)) {
                            for (String target : eNfa.getDelta().get(transitionKey)) {
                                nextState.addAll(eClose(target, eNfa.getDelta()));
                            }
                        }
                    }
                }

                if (!nextState.isEmpty()) {
                    if(dfaStates.add(nextState)) // 중복된 상태는 queue 추가 X
                        queue.add(nextState);
                    dfaTransitions.put(new ArrayList<>(Arrays.asList(currentState.toString(), input)), new ArrayList<>(nextState));

                    if (isFinalState(nextState, eNfa.getFinalState())) {
                        dfaFinalStates.add(nextState.toString());
                    }
                }
            }
        }

        // Create new Fa instance for DFA
        Fa dfa = new Fa(new ArrayList<>(dfaStates.stream().map(Object::toString).collect(Collectors.toList())),
                new ArrayList<>(eNfa.getSigma()),
                dfaTransitions,
                startState.toString(),
                new ArrayList<>(dfaFinalStates));


        return dfa;
    }

    private Set<String> eClose(String state, Map<List<String>, List<String>> delta) {
        Set<String> closure = new HashSet<>();
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // Split the state by commas and push each part into the stack
        String[] states = state.split(",");
        for (String s : states) {
            s = s.trim();  // Remove white spaces
            stack.push(s);
            visited.add(s);
        }

        while (!stack.isEmpty()) {
            String s = stack.pop();
            closure.add(s);
            List<String> transitionKey = Arrays.asList(s, "ε");
            if (delta.containsKey(transitionKey)) {
                for (String target : delta.get(transitionKey)) {
                    // Split the target by commas and push each part into the stack
                    states = target.split(",");
                    for (String t : states) {
                        t = t.trim();  // Remove white spaces
                        if (!visited.contains(t)) {
                            stack.push(t);
                            visited.add(t);
                        }
                    }
                }
            }
        }
        return closure;
    }




    private boolean isFinalState(Set<String> states, List<String> finalState) {
        for (String state : states) {
            if (finalState.contains(state)) {
                return true;
            }
        }
        return false;
    }
}
