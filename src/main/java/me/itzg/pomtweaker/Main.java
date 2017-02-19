package me.itzg.pomtweaker;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Geoff Bourne
 * @since Feb 2017
 */
public class Main {
    public static final Action[] actions = new Action[]{
            new SetProjectVersionAction()
    };

    public static void main(String[] args) throws IOException {

        final ArgumentParser topParser = ArgumentParsers.newArgumentParser("pomtweaker");

        final Subparsers subparsers = topParser.addSubparsers();

        for (Action action : actions) {
            final Subparser installed = action.install(subparsers);
            installed.setDefault("action", action);
        }

        try {
            final Namespace ns = topParser.parseArgs(args);

            final Action action = ns.get("action");

            final Path currentPath = Paths.get(".");

            Files.walk(currentPath)
                    .filter(path -> path.getFileName().toString().equals("pom.xml"))
                    .forEach(path -> {
                        try {
                            action.execute(ns, path);
                        } catch (IOException e) {
                            System.err.printf("Action %s failed : %s%n", action, e);
                            System.exit(2);
                        }
                    });

        } catch (ArgumentParserException e) {
            topParser.handleError(e);
        }
    }
}
