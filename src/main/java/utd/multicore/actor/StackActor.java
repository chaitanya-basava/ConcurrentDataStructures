package utd.multicore.actor;

import utd.multicore.ds.DataStructure;

public class StackActor extends Actor {
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
                this.stack.push(number);
            } else {
                this.stack.pop();
            }
        }
    }
}
