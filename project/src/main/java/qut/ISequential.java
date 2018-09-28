package qut;

import java.io.IOException;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 27-9-2018.
 */
public interface ISequential<E extends Sigma70Consensus> {
    void run(String referenceFile, String dir) throws IOException;

    java.util.Map<String, E> getConsensus();
}
