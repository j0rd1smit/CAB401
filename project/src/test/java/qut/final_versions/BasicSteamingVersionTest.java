package qut.final_versions;

import lombok.Getter;
import qut.AbstractFunctionalTest;
import qut.ISequential;

/**
 * TODO Explanation
 *
 * @author Jordi Smit on 11-10-2018.
 */
public class BasicSteamingVersionTest extends AbstractFunctionalTest {

    @Getter
    private final ISequential iSequential = new BasicSteamingVersion();
}