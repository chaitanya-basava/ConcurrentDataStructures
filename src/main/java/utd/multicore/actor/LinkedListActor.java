package utd.multicore.actor;

import utd.multicore.ds.DataStructure;

public class LinkedListActor extends Actor {
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

            if (operation < 100 - this.writeDist) {
                this.linkedList.search(number);
            }
            else {
                if (random.nextBoolean()) {
                    this.linkedList.add(number);
                }
                else {
                    this.linkedList.remove(number);
                }
            }
        }
    }
}
