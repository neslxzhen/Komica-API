package self.nesl.kapi.parser.komica;

import org.jsoup.nodes.Element;
import self.nesl.kapi.parser.Parser;
import self.nesl.kapi.po.KThread;

import java.util.ArrayList;

import static self.nesl.kapi.utils.Utils.print;

public class Komica50BoardsParser extends Parser {
    public Komica50BoardsParser(String url, Element element) {
        super(url, element);
    }

    @Override
    public KThread parse() {
        ArrayList<KThread> boards = new ArrayList<KThread>();
        for (Element e : element.select(".divTableRow a")) {
            String url = e.attr("href");
            if (url.contains("/index.")) {
                url = url.substring(0, url.indexOf("/index."));
            }
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String title = e.text();
            KThread p=new KThread(url,url);
            p.setTitle(title);
            p.setParents(new ArrayList<>(){{add("komica50");}});
            boards.add(p);
        }
        KThread set=new KThread(null,"komica50");
        set.setTree(boards);
        return set;
    }
}
