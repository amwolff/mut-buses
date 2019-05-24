package daemon;

public interface Fetcher {
    int callEveryMs = 11000;

    void run();
}
