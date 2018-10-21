package qut.parallel_versions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import qut.Gene;
import qut.NucleotideSequence;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 11-10-2018.
 */
@RequiredArgsConstructor
@Getter
public class DataContainer {

    private final Gene gene;
    private final Gene referenceGene;
    private final NucleotideSequence nucleotides;
    private final String name;
}
