package me.itzg.pomtweaker;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Geoff Bourne
 * @since Feb 2017
 */
public interface Action {
    Subparser install(Subparsers subparsers);

    void execute(Namespace ns, Path pomFile) throws IOException;
}
