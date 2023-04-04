package ng.org.gradle.software;

import java.io.OutputStream;
import java.io.PrintStream;

public class IndentedPrintStream extends PrintStream {
    private int indent = 0;

    public IndentedPrintStream(OutputStream out) {
        super(out);
    }

    @Override
    public void print(String s) {
        for (int i = 0; i < indent; i++) {
            super.print('\t');
        }
        super.print(s);
    }

    public void indent() {
        indent++;
    }

    public void dedent() {
        indent--;
        if (indent < 0) {
            throw new IllegalStateException("too many dedents");
        }
    }
}
