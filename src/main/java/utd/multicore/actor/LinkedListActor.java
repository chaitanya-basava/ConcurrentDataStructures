package utd.multicore.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.ds.DataStructure;

public class LinkedListActor extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(LinkedListActor.class);
    private final DataStructure<Integer> linkedList;
    private final int bound;

    public LinkedListActor(int id, int csCount, double writeDist, DataStructure<Integer> linkedList, int bound) {
        super(id, csCount, writeDist);
        this.linkedList = linkedList;
        this.bound = bound;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.csCount; i++) {
            int operation = random.nextInt(100);
            int number = random.nextInt(this.bound);

            boolean result;
            if (operation < 100 - this.writeDist) {
                result = this.linkedList.search(number);
                logger.info(String.format("%s found status: %s", number, result));
            }
            else {
                if (random.nextBoolean()) {
                    result = this.linkedList.add(number);
                    logger.info(String.format("%s add status: %s", number, result));
                }
                else {
                    result = this.linkedList.remove(number);
                    logger.info(String.format("%s remove status: %s", number, result));
                }
            }
        }
    }
}
