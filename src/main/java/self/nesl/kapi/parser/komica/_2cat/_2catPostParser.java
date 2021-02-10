package self.nesl.kapi.parser.komica._2cat;

import org.jsoup.nodes.Element;
import self.nesl.kapi.parser.Parser;
import self.nesl.kapi.parser.komica.sora.SoraPostParser;
import self.nesl.kapi.po.KThread;
import self.nesl.kapi.utils.UrlUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static self.nesl.kapi.utils.Utils.print;


public class _2catPostParser extends SoraPostParser {
    public _2catPostParser(Element element,String threadId,String threadUrl,String postNo,String postUrl) {
        super(element,threadId,threadUrl,postNo,postUrl);
    }

    @Override
    public void setDetail(){
        super.installAnimeDetail();
    }

    @Override
    public void setParent() {
//        String r="#r";
//        if(!url.contains(r)) return;
//        String first=url.substring(url.indexOf(r)+r.length());
//        super.post.setParents(new ArrayList<String>(){{ add(getId(first)); }});
    }

    private static String getBoardId(String url){
        String boardCode= new UrlUtils(url).getPath();
        return boardCode.replace("/","");
    }

    @Override
    public void setPicture(){
        String boardUrl=threadUrl==null?postUrl:threadUrl;
        String boardCode= getBoardId(boardUrl);
        try {
            String fileName= element.selectFirst("a.imglink[href=#]").attr("title");
            String newLink= MessageFormat.format("//cat.2nyan.org/{0}/src/{1}",boardCode,fileName);
            post.setPictureUrl(new UrlUtils(newLink, boardUrl).getUrl());
        } catch (NullPointerException ignored) {}

    }
}
