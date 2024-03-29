package qut;

import jaligner.BLOSUM62;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.matrix.Matrix;


public class PeptideSequence {
    private static final Matrix BLOSUM_62 = BLOSUM62.Load();
    public byte[] bytes;

    public PeptideSequence() {
    }

    public PeptideSequence(String string) {
        bytes = string.getBytes();
    }

    public static double Similarity(PeptideSequence A, PeptideSequence B) {
        return SmithWatermanGotoh.align(new Sequence(A.toString()), new Sequence(B.toString()), BLOSUM_62, 10f, 0.5f).calculateScore();
    }

    @Override
    public String toString() {
        return new String(bytes);
    }
}
