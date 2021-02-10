package self.nesl.kapi.parser.komica._2cat;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import self.nesl.kapi.parser.Parser;
import self.nesl.kapi.parser.komica.sora.SoraThreadParser;
import self.nesl.kapi.po.KThread;
import self.nesl.kapi.utils.UrlUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static self.nesl.kapi.utils.Utils.print;

public class _2catThreadParser extends SoraThreadParser {
    public _2catThreadParser(String url, Element element) {
        super(replace(url), element);
        super.subParser=_2catPostParser.class;
        super.CSS_DETAIL="span.push_btn a";
        super.CSS_THREAD="div.reply";
    }

    private static String replace(String url){
        url=url.replaceAll("/~","/");
        if(url.endsWith("/")) url=url.substring(0,url.length()-1);
        return url;
    }

    @Override
    public KThread addTo(KThread thread, KThread post){
        thread.addPost(post);
        return thread;
    }
}
