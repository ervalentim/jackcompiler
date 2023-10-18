package br.ufma.ecp;

public class VMWriter {
    private StringBuilder vmOutput = new StringBuilder();

    enum Segment {
        CONST("constant"),
        ARG("argument"),
        LOCAL("local"),
        STATIC("static"),
        THIS("this"),
        THAT("that"),
        POINTER("pointer"),
        TEMP("temp");

        private Segment(String value) {
            this.value = value;
        }

        public String value;
    };

    enum Command {
        ADD,
        SUB,
        NEG,
        EQ,
        GT,
        LT,
        AND,
        OR,
        NOT
    };

    public String vmOutput() {
        return vmOutput.toString();
    }

    void writePush(Segment segment, int index) {
        vmOutput.append(String.format("push %s %d\n", segment.value, index));
    }

    void writePop(Segment segment, int index) {

        vmOutput.append(String.format("pop %s %d\n", segment.value, index));
    }

    void writeArithmetic(Command command) {
        vmOutput.append(String.format("%s\n", command.name().toLowerCase()));
    }

    void writeLabel(String label) {
        vmOutput.append(String.format("label %s\n", label));
    }

    void writeGoto(String label) {
        vmOutput.append(String.format("goto %s\n", label));
    }

    void writeIf(String label) {
        vmOutput.append(String.format("if-goto %s\n", label));
    }

    void writeCall(String name, int nArgs) {
        vmOutput.append(String.format("call %s %d\n", name, nArgs));
    }

    void writeFunction(String name, int nLocals) {
        vmOutput.append(String.format("function %s %d\n", name, nLocals));
    }

    void writeReturn() {
        vmOutput.append(String.format("return\n"));
    }

}