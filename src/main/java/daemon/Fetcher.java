package daemon;

public interface Fetcher {
    int callEveryMs = 10000;

    void run();
}
