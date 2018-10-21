package qut;

import java.io.IOException;

/**
 * A interface create for testing purposes.
 *
 * @author Jordi Smit on 27-9-2018.
 */
public interface ISequential<E extends Sigma70Consensus> {
    void run(String referenceFile, String dir) throws IOException;

    java.util.Map<String, E> getConsensus();
}
