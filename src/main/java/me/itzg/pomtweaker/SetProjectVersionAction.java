package me.itzg.pomtweaker;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Geoff Bourne
 * @since Feb 2017
 */
public class SetProjectVersionAction implements Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(SetProjectVersionAction.class);

    public static final String ARG_VERSION = "version";

    public Subparser install(Subparsers subparsers) {
        final Subparser subparser = subparsers.addParser("set-project-version");
        subparser.addArgument(ARG_VERSION).required(true);
        return subparser;
    }

    public void execute(Namespace ns, Path pomFile) throws IOException {
        final String version = ns.getString(ARG_VERSION);
        LOGGER.info("Setting version to {} of {}", version, pomFile);

        final Path tempOutFile = Files.createTempFile("pom", "xml");

        try (BufferedReader reader = Files.newBufferedReader(pomFile)) {

            StreamSource xsltSource;
            try (InputStream xsltStream = this.getClass().getResourceAsStream("/SetProjectVersionAction.xslt")) {
                LOGGER.info("Using XSLT {}", xsltStream);
                xsltSource = new StreamSource(xsltStream);
                final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);

                transformer.setParameter("version", version);

                try (BufferedWriter writer = Files.newBufferedWriter(tempOutFile)) {
                    transformer.transform(new StreamSource(reader), new StreamResult(writer));
                }

            } catch (TransformerException e) {
                throw new IOException(e);
            }
        }

        Files.move(tempOutFile, pomFile, StandardCopyOption.REPLACE_EXISTING);
    }

}
