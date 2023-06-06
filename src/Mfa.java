import java.util.*;
import java.util.stream.Collectors;

public class Mfa {

    public Fa minimizeDfa(Fa dfa) {
        Map<Set<String>, List<Map.Entry<String, Set<String>>>> partitions = partitionStates(dfa);

        Fa minimizedDfa = new Fa();
        Map<Set<String>, String> newStates = new HashMap<>();
        int i = 0;
        for (Set<String> partition : partitions.keySet()) {
            String newState = partition.toString();
            //String.format("q%03d", i++);
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
            for (String transition : dfa.getSigma()) {
                for (String s : partition.getKey()) {
                    List<String> transitionKey = Arrays.asList(s, transition);
                    List<String> toState = dfa.getDelta().get(transitionKey);

                    if (toState != null) {
                        List<String> newState = null;
                        for (Set<String> set : partitions.keySet()) {
                            if (set.toString().contains(toState.toString())) {
                                newState = set.stream().toList();
                            }
                        }

                        minimizedDfa.getDelta().put(Arrays.asList(partition.getKey().toString(), transition), newState);

                    }


                }


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

                if (toStates.size() > 1) {
                    // Partition is not uniform, split it

                    int size = partitions.size();
                    for (Set<String> newPartition : toStates.values()) {
                        partitions.put(newPartition, new ArrayList<>());
                    }
                    if (size != partitions.size()) {
                        partitions.remove(partition.getKey());
                        changed = true;
                        break;
                    }

                }
            }
        } while (changed);

        return partitions;
    }

}
