package utd.multicore.actor;

import java.util.Random;

public abstract class Actor implements Runnable {
    protected final int id;
    protected final int csCount;
    protected final double writeDist;
    protected final Random random = new Random();
    private final long[] latency;
    private final long[] turnaroundTime;

    protected Actor(int id, int csCount, double writeDist) {
        this.id = id;
        this.csCount = csCount;
        this.writeDist = writeDist;
        this.latency = new long[this.csCount];
        this.turnaroundTime = new long[this.csCount];
    }

    public void setLatencyAtI(int i, long latency) {
        this.latency[i] = latency;
    }

    public void setTurnaroundTimeAtI(int i, long tat) {
        this.turnaroundTime[i] = tat;
    }

    public long getLatencyAtI(int i) {
        return this.latency[i];
    }

    public long getTurnaroundTimeAtI(int i) {
        return this.turnaroundTime[i];
    }
}
