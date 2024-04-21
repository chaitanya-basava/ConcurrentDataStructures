package utd.multicore.actor;

import utd.multicore.ds.bst.LazyLockingExternalBST;

public class LazyBSTReadAndWriteActor extends Actor {
    private final LazyLockingExternalBST<Integer> ds;
    private final int bound;

    public LazyBSTReadAndWriteActor(int id, int csCount, double writeDist, LazyLockingExternalBST<Integer> ds, int bound) {
        super(id, csCount, writeDist);
        this.ds = ds;
        this.bound = bound;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.csCount; i++) {
            int operation = random.nextInt(100);
            int number = random.nextInt(this.bound);

            long processRequested = System.currentTimeMillis();
            if (operation < 100 - this.writeDist) {
                this.ds.search(number);
            }
            else {
                if (random.nextBoolean()) {
                    this.ds.add(number);
                }
                else {
                    this.ds.remove(number);
                }
            }
            long processCompleted = System.currentTimeMillis();

            this.setTurnaroundTimeAtI(i, processCompleted - processRequested);
        }
    }
}
