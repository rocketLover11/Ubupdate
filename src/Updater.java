import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Updater {
    private static final String LOG_FILE = "/home/" + System.getProperty("user.name") + "/ubupdate_log.txt";
    public static void main(String[] args) {
        try {
            runUpdates();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void runUpdates() throws IOException, InterruptedException {
        String log1 = runCommand("apt update");
        String log2 = runCommand("apt -y upgrade");
        log(log1, log2);
    }

    private static String runCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder out = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line).append("\n");
        }

        p.waitFor();
        return out.toString();
    }

    private static void log(String log1, String log2) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringBuilder log = new StringBuilder();
        log.append("===== Update Run: ").append(timestamp).append(" =====\n");
        log.append(log1).append("\n");
        log.append(log2).append("\n");
        log.append("=================================================\n\n");
        writeFile(LOG_FILE, log.toString());
    }

    private static void writeFile(String path, String text) throws IOException {
        FileWriter fw = new FileWriter(path, true);
        fw.write(text);
        fw.close();
    }
}