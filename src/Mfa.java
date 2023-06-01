import java.util.*;
public class Mfa {

    public Fa minimizeDfa(Fa dfa) {
        Map<Set<String>, List<Map.Entry<String, Set<String>>>> partitions = partitionStates(dfa);

        Fa minimizedDfa = new Fa();
        Map<Set<String>, String> newStates = new HashMap<>();
        int i = 0;
        for (Set<String> partition : partitions.keySet()) {
            String newState = "q" + i++;
            newStates.put(partition, newState);
            minimizedDfa.getQ().add(newState);

            if (partition.contains(dfa.getStartState())) {
                minimizedDfa.setStartState(newState);
            }
            if (!Collections.disjoint(partition, dfa.getFinalState())) {
                minimizedDfa.getFinalState().add(newState);
            }
        }

        for (Map.Entry<Set<String>, List<Map.Entry<String, Set<String>>>> partition : partitions.entrySet()) {
            String fromState = newStates.get(partition.getKey());
            for (Map.Entry<String, Set<String>> transition : partition.getValue()) {
                String toState = newStates.get(transition.getValue());
                List<String> transitionKey = Arrays.asList(fromState, transition.getKey());
                minimizedDfa.getDelta().put(transitionKey, Arrays.asList(toState));
            }
        }

        minimizedDfa.setSigma(new ArrayList<>(dfa.getSigma()));
        minimizedDfa.getSigma().remove("ε");

        return minimizedDfa;
    }


    public Map<Set<String>, List<Map.Entry<String, Set<String>>>> partitionStates(Fa dfa) {
        Map<Set<String>, List<Map.Entry<String, Set<String>>>> partitions = new HashMap<>();

        // Initialize partitions
        Set<String> nonFinalStates = new HashSet<>(dfa.getQ());
        nonFinalStates.removeAll(dfa.getFinalState());
        partitions.put(new HashSet<>(dfa.getFinalState()), new ArrayList<>());
        partitions.put(nonFinalStates, new ArrayList<>());
        int size = nonFinalStates.size();

        boolean changed;
        do {
            changed = false;
            for (Map.Entry<Set<String>, List<Map.Entry<String, Set<String>>>> partition : new ArrayList<>(partitions.entrySet())) {
                Map<String, Set<String>> toStates = new HashMap<>();
                for (String state : partition.getKey()) {
                    for (String input : dfa.getSigma()) {
                        if (!input.equals("ε")) {
                            List<String> transitionKey = Arrays.asList(state, input);
                            if (dfa.getDelta().containsKey(transitionKey)) {
                                String toState = dfa.getDelta().get(transitionKey).toString();  // DFA has only one transition for each state-input pair
                                toStates.computeIfAbsent(toState, k -> new HashSet<>()).add(state);
                            }
                        }
                    }
                }

                if (toStates.size() > 1 && size == toStates.size()) {
                    // Partition is not uniform, split it
                    partitions.remove(partition.getKey());
                    for (Set<String> newPartition : toStates.values()) {
                        partitions.put(newPartition, new ArrayList<>());
                    }
                    changed = true;
                    size = toStates.size();
                    break;
                }
            }
        } while (changed);

        return partitions;
    }

}
