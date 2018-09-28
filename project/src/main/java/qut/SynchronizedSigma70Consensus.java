package qut;

import edu.au.jacobi.pattern.Match;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 28-9-2018.
 */
public class SynchronizedSigma70Consensus extends Sigma70Consensus  {

    @Override
    public synchronized void addMatch(Match match) {
        super.addMatch(match);
    }
}
