package utd.multicore.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.ds.DataStructure;

public class LinkedListActor extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(LinkedListActor.class);
    private final DataStructure<Integer> linkedList;

    public LinkedListActor(int id, int csCount, double writeDist, DataStructure<Integer> linkedList) {
        super(id, csCount, writeDist);
        this.linkedList = linkedList;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.csCount; i++) {
            int operation = random.nextInt(100);
            int number = random.nextInt(this.csCount / 100);

            if (operation < 100 - this.writeDist)
                logger.info(number + " found status: " + this.linkedList.search(number));
            else {
                if (random.nextBoolean()) logger.info(number + " add status: " + this.linkedList.add(number));
                else logger.info(number + " remove status: " + this.linkedList.remove(number));
            }
        }
    }
}
