package utd.multicore;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.actor.Actor;
import utd.multicore.actor.LinkedListActor;
import utd.multicore.actor.StackActor;
import utd.multicore.ds.ConcurrentStack;
import utd.multicore.ds.DataStructure;
import utd.multicore.ds.linkedlist.ConcurrentLinkedList;
import utd.multicore.ds.linkedlist.FineGrainedConcurrentLinkedList;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static CommandLine parseArgs(String[] args) {
        Options options = new Options();

        Option datastructureOption = new Option("d", "datastructure", true, "DS to use (int)");
        datastructureOption.setRequired(true);
        options.addOption(datastructureOption);

        Option writeDistOption = new Option("w", "writeDistribution", true, "distribution of write operations (%)");
        options.addOption(writeDistOption);

        Option numThreadsOption = new Option("n", "numThreads", true, "num of threads");
        numThreadsOption.setRequired(true);
        options.addOption(numThreadsOption);

        Option keySpace = new Option("k", "keySpace", true, "input key space size (int)");
        options.addOption(keySpace);

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

    public static void main(String[] args) throws IllegalArgumentException, InterruptedException {
        CommandLine cmd = Main.parseArgs(args);
        int algoId = Integer.parseInt(cmd.getOptionValue("datastructure"));
        int numThreads = Integer.parseInt(cmd.getOptionValue("numThreads"));
        int keySpace = Integer.parseInt(cmd.getOptionValue("keySpace", String.valueOf(100)));
        int writeDist = Integer.parseInt(cmd.getOptionValue("writeDistribution"));
        int csCount = Integer.parseInt(cmd.getOptionValue("csCount", String.valueOf(1000000)));

        Actor[] actors = new Actor[numThreads];
        Thread[] actorThreads = new Thread[numThreads];

        DataStructure<Integer> ds = switch(algoId) {
            case 0 -> new ConcurrentLinkedList<>();
            case 1 -> new FineGrainedConcurrentLinkedList<>();
            case 2 -> new ConcurrentStack<>();
            default -> throw new IllegalStateException("Unexpected value: " + algoId);
        };

        long actorStartMillis = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            Actor actor = switch(algoId) {
                case 0, 1 -> new LinkedListActor(i, csCount / numThreads, writeDist, ds, keySpace);
                case 2 -> new StackActor(i, csCount / numThreads, ds);
                default -> throw new IllegalStateException("Unexpected value: " + algoId);
            };
            actors[i] = actor;
            actorThreads[i] = new Thread(actors[i]);
            actorThreads[i].start();
        }
        for (int i = 0; i < numThreads; i++) actorThreads[i].join();
        long actorEndMillis = System.currentTimeMillis();

        logger.info(ds.toString());

        double throughput = (double) csCount / (actorEndMillis - actorStartMillis);
        logger.info(String.format("System Throughput: %.2f ops/ms", throughput));
    }
}
