package utd.multicore.actor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utd.multicore.ds.DataStructure;

public class StackActor extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(StackActor.class);
    private final DataStructure<Integer> stack;

    public StackActor(int id, int csCount, DataStructure<Integer> stack) {
        super(id, csCount, 100);
        this.stack = stack;
    }

    @Override
    public void run() {
        for(int i = 0; i < this.csCount; i++) {
            if (random.nextBoolean()) {
                int number = random.nextInt(this.csCount / 100);
                logger.info(this.stack.push(number) + " pushed to stack");
            } else {
                logger.info("popped number: " + this.stack.pop());
            }
        }
    }
}
