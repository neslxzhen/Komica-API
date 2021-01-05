package self.nesl.kapi.parser;

import org.jsoup.nodes.Element;
import self.nesl.kapi.po.KThread;
import self.nesl.kapi.po.Post;

import java.util.ArrayList;

public abstract class Parser {
    public String url;
    public Element element;

    public Parser(String url, Element element){
        this.url=url;
        this.element=element;
    }
    public abstract KThread parse();
}
