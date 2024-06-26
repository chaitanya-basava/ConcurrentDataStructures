package utd.multicore;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.actor.Actor;
import utd.multicore.actor.ReadAndWriteActor;
import utd.multicore.actor.StackActor;
import utd.multicore.ds.bst.ConcurrentBST;
import utd.multicore.ds.ConcurrentStack;
import utd.multicore.ds.DataStructure;
import utd.multicore.ds.bst.LazyLockingExternalBST;
import utd.multicore.ds.linkedlist.ConcurrentLinkedList;
import utd.multicore.ds.linkedlist.FineGrainedConcurrentLinkedList;
import utd.multicore.ds.utils.LazyTreeNode;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


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

    public static void buildExternalBSTWithSentinels(LazyLockingExternalBST<Integer> ds) {
        ds.current = new LazyTreeNode<>(Integer.MAX_VALUE - 2);

        int parentItem = Integer.MAX_VALUE - 1;
        ds.parent = new LazyTreeNode<>(parentItem, ds.current, new LazyTreeNode<>(parentItem));

        int grandParentItem = Integer.MAX_VALUE;
        ds.grandParent = new LazyTreeNode<>(grandParentItem, ds.parent, new LazyTreeNode<>(grandParentItem));
    }

    public static void main(String[] args) throws IllegalArgumentException, InterruptedException {
        CommandLine cmd = Main.parseArgs(args);
        int algoId = Integer.parseInt(cmd.getOptionValue("datastructure"));
        int numThreads = Integer.parseInt(cmd.getOptionValue("numThreads"));
        int keySpace = Integer.parseInt(cmd.getOptionValue("keySpace", String.valueOf(100))) + 1;
        int writeDist = Integer.parseInt(cmd.getOptionValue("writeDistribution", String.valueOf(100)));
        int csCount = Integer.parseInt(cmd.getOptionValue("opCount", String.valueOf(1000000)));

        Actor[] actors = new Actor[numThreads];
        Thread[] actorThreads = new Thread[numThreads];

        DataStructure<Integer> ds = switch(algoId) {
            case 0 -> new ConcurrentLinkedList<>();
            case 1 -> new FineGrainedConcurrentLinkedList<>();
            case 2 -> new ConcurrentStack<>();
            case 3 -> new ConcurrentBST<>();
            case 4 -> new LazyLockingExternalBST<>();
            default -> throw new IllegalStateException("Unexpected value: " + algoId);
        };
        if (algoId == 4) buildExternalBSTWithSentinels((LazyLockingExternalBST<Integer>) ds);
        ds.warmup(Integer.class, keySpace);
        logger.info("Warmup complete: " + ds.getSize());

        long actorStartMillis = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            Actor actor = switch(algoId) {
                case 0, 1, 3, 4 -> new ReadAndWriteActor(i, csCount / numThreads, writeDist, ds, keySpace);
                case 2 -> new StackActor(i, csCount / numThreads, ds);
                default -> throw new IllegalStateException("Unexpected value: " + algoId);
            };
            actors[i] = actor;
            actorThreads[i] = new Thread(actors[i]);
            actorThreads[i].start();
        }
        for (int i = 0; i < numThreads; i++) actorThreads[i].join();
        long actorEndMillis = System.currentTimeMillis();

        logger.info(ds.getSize() + " " + ds);
        logger.info("Num of Adds: " + ds.getNumAdds());
        logger.info("Num of Deletes: " + ds.getNumDeletes());
        logger.info("Num of Searches: " + ds.getNumSearches());

        double throughput = (double) csCount / (actorEndMillis - actorStartMillis);
        logger.info(String.format("System Throughput: %.2f ops/ms", throughput));

        double avgTAT = 0;
        for(Actor actor: actors) {
            for(int i = 0; i < actor.getCsCount(); i++) {
                avgTAT += actor.getTurnaroundTimeAtI(i);
            }
        }
        avgTAT /= csCount;

        String folderPath = "./tests/" + algoId + "-" + writeDist + "-" + (keySpace-1);
        String fileName = folderPath + "/" + numThreads + ".csv";

        Path path = Paths.get(folderPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + folderPath, e);
            }
        }

        try(FileWriter writer = new FileWriter(fileName, true)) {
            writer.append(String.valueOf(throughput)).append(",")
                    .append(String.valueOf(avgTAT)).append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
