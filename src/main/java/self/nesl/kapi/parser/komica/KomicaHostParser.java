package self.nesl.kapi.parser.komica;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import self.nesl.kapi.parser.HostParser;
import self.nesl.kapi.po.Board;
import self.nesl.kapi.po.Host;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static self.nesl.kapi.utils.Utils.print;

public class KomicaHostParser extends HostParser {

    @Override
    public Host parse() {
        Map<String, Board[]> datasets = new HashMap<>();
        for (Map.Entry<String, String> entry : new HashMap<String, String>() {{
            put("top50.komica.org", "https://komica.org/mainmenu2019.html");
            put("top50.komica2.net", "https://komica2.net/mainmenu2018.html");
            put("all.komica.org", "https://komica.org/bbsmenu.html");
            put("all.komica2.net", "https://komica2.net/bbsmenu2018.html");
        }}.entrySet()) {
            Element element = null;
            try {
                element = Jsoup.connect(entry.getValue()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String dataSetId = entry.getKey();
            Board[] set = dataSetId.startsWith("top50.") ? parseTop50(element) : parseMain(element);
            datasets.put(dataSetId, set);
        }
        return new Host("komica.org", datasets);
    }

    public Board[] parseTop50(Element element) {
        ArrayList<Board> boards = new ArrayList<Board>();
        for (Element e : element.select(".divTableRow a")) {
            String url = e.attr("href");
            if (url.contains("/index.")) {
                url = url.substring(0, url.indexOf("/index."));
            }
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String title = e.text();
            Board p = new Board(url, title,url, new String[]{"root"});
            p.addTag("top50");
            boards.add(p);
        }
        return boards.toArray(new Board[0]);
    }

    public Board[] parseMain(Element element) {
        ArrayList<Board> boards = new ArrayList<Board>();
        for (Element ul : element.select("ul")) {
            String ui_title = ul.getElementsByClass("category").text();
            for (Element li : ul.select("li")) {
                String li_title = li.text();
                String li_link = li.select("a").attr("href");
                if (li_link.contains("/index.")) {
                    li_link = li_link.substring(0, li_link.indexOf("/index."));
                }
                try {
                    Board p = new Board(li_link, li_title,li_link, new String[]{"root"});
                    p.addTag(ui_title);
                    boards.add(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return boards.toArray(new Board[0]);
    }
}
