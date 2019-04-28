package daemon;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public interface Fetcher {
    static List<String> queriedLines = new Vector<>();

    void Run() throws IOException;
}
