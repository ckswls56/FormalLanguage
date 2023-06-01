import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("input a regular expression");
        String re = in.nextLine();
        System.out.println("re:" + re);
        Nfa nfa = new Nfa(re);
        nfa.add_join_symbol();
        nfa.postfix();
        nfa.re2nfa();
        nfa.print();

        Dfa dfa = new Dfa(nfa.getPair(),nfa.getLetter());
        dfa.createDFA();
        dfa.printDFA();


    }
}