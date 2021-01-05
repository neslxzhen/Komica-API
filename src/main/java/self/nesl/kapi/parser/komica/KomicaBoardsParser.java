package self.nesl.kapi.parser.komica;

import org.jsoup.nodes.Element;
import self.nesl.kapi.parser.Parser;
import self.nesl.kapi.po.KThread;
import self.nesl.kapi.utils.UrlUtils;

import java.util.ArrayList;

import static self.nesl.kapi.utils.Utils.print;

public class KomicaBoardsParser extends Parser {
    public KomicaBoardsParser(String url, Element element) {
        super(url, element);
    }

    @Override
    public KThread parse() {
        ArrayList<KThread> boards=new ArrayList<KThread>();
        for (Element ul : element.select("ul")) {
            String ui_title = ul.getElementsByClass("category").text();
            for (Element li : ul.select("li")) {
                String li_title = li.text();
                String li_link = li.select("a").attr("href");
                if (li_link.contains("/index.")) {
                    li_link = li_link.substring(0, li_link.indexOf("/index."));
                }
                try {
                    KThread p=new KThread(li_link,li_link);
                    p.setTitle(li_title);
                    p.addTag(ui_title);
                    p.setParents(new ArrayList<>(){{add(url);}});
                    boards.add(p);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        KThread set=new KThread(null,url);
        set.setTree(boards);
        return set;
    }
}
