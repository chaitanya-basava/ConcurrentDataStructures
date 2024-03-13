package utd.multicore;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static CommandLine parseArgs(String[] args) {
        Options options = new Options();

        Option datastructureOption = new Option("d", "datastructure", true, "DS to use (int)");
        datastructureOption.setRequired(true);
        options.addOption(datastructureOption);

        Option writeDistOption = new Option("w", "writeDistribution", true, "distribution of write operations");
        options.addOption(writeDistOption);

        Option numThreadsOption = new Option("n", "numThreads", true, "num of threads");
        numThreadsOption.setRequired(true);
        options.addOption(numThreadsOption);

        Option opCountOption = new Option("c", "opCount", true, "total num of ops to perform");
        options.addOption(opCountOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            return parser.parse(options, args);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] args) {
        CommandLine cmd = Main.parseArgs(args);
    }
}