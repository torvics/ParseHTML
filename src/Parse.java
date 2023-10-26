import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parse {
    public static void main(String[] args) {
        try {
            String url = "https://people.sc.fsu.edu/~jburkardt/data/csv/csv.html";
            Document document = Jsoup.connect(url).get();

            List<String> csvFileNames = new ArrayList<>();
            Elements links = document.select("a[href$=.csv]");

            for (Element link : links) {
                String href = link.attr("href");
                csvFileNames.add(href);
            }

            List<Thread> threads = new ArrayList<>();

            for (String fileName : csvFileNames) {
                Thread thread = new Thread(() -> {
                    try {
                        URL csvURL = new URL("https://people.sc.fsu.edu/~jburkardt/data/csv/" + fileName);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(csvURL.openStream()));
                        int lineCount = 0;
                        while (reader.readLine() != null) {
                            lineCount++;
                        }
                        System.out.println("El archivo CSV " + fileName + " tiene " + lineCount + " lineas");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                threads.add(thread);
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}