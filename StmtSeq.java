import java.util.*;

public class StmtSeq {

    int option;

    Stmt stmt;
    StmtSeq stmtseq;

    StmtSeq() {
        option = 0;
        stmt = null;
        stmtseq = null;
    }

    public void parse(Scanner S) {
        // Option 1: <stmt-seq> ::= <stmt>
        option = 1;
        stmt = new Stmt();
        stmt.parse(S);
        // System.out.println("after stmt"+S.tokens);
        // Option 2: <stmt-seq> ::= <stmt><stmt-seq>
        // If the current token != Core.END, continue parsing stmtseq.
        Core[] expectedones = new Core[] { Core.ID, Core.IF, Core.WHILE, Core.INPUT, Core.OUTPUT, Core.INT, Core.REF };
        if (Utility.checkIfTokenIsExpected(expectedones, S.currentToken())) {
            option = 2;
            stmtseq = new StmtSeq();
            stmtseq.parse(S);
        }
    }

    public void semantic(Stack<Map<String, Core>> scopetrack) {
        stmt.semantic(scopetrack);
        if (option == 2) {
            stmtseq.semantic(scopetrack);
        }
    }

    public void execute(Memory memory, Scanner inputScanner) {
        stmt.execute(memory, inputScanner);
        if (option == 2) {
            stmtseq.execute(memory, inputScanner);
        }
    }

}
