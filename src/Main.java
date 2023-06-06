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

        System.out.println("--------NFA--------");
        nfa.getFa().print();
        System.out.println("\n--------NFA--------");

        NfaToDfa dfaConverter = new NfaToDfa(nfa.getFa());
        Fa dfa = dfaConverter.convert();

        System.out.println("--------DFA--------");
        dfa.print();
        dfa.printSimple();
        System.out.println("\n--------DFA--------");

        Mfa mfaConverter = new Mfa();
        Fa mfa = mfaConverter.minimizeDfa(dfa);

        System.out.println("--------MFA--------");
        mfa.print();
        mfa.printSimple();
        System.out.println("\n--------MFA--------");

    }
}