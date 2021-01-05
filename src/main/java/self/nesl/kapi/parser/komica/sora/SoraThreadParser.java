package self.nesl.kapi.parser.komica.sora;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import self.nesl.kapi.parser.ParserFactory;
import self.nesl.kapi.utils.UrlUtils;
import self.nesl.kapi.parser.Parser;
import self.nesl.kapi.po.KThread;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import static self.nesl.kapi.utils.Utils.print;

public class SoraThreadParser extends Parser {
    public Class<? extends Parser> subParser;

    public String CSS_DETAIL;
    public String CSS_THREAD;

    public SoraThreadParser(String url, Element element) {
        super(url, element);
        subParser=SoraPostParser.class;
        this.CSS_DETAIL="span.warn_txt2";
        this.CSS_THREAD="div.post";
    }

    @Override
    public KThread parse() {
        KThread dataset =new KThread(url,new UrlUtils(url).unProtocol());
        ArrayList<KThread> threads=new ArrayList<KThread>();

        String path= ParserFactory.getPath(subParser);
        String head=url.contains(path)?url+"#r":url+path;

        for (Element thread_ele : getThreads()) {
            int replyCount = 0;
            String threadNo = null;
            String threadUrl = null;
            KThread thread = null;
            for (Element post_ele:  thread_ele.select(CSS_THREAD)) {

                String postNo = post_ele.attr("id").substring(1);
                String postUrl=head+postNo;

                if(post_ele.hasClass("threadpost")){
                    try {
                        String s = post_ele.selectFirst(CSS_DETAIL).text();
                        s = s.replaceAll("\\D", "");
                        replyCount = Integer.parseInt(s);
                    } catch (Exception ignored) {}

                    threadNo = postNo;
                    threadUrl=postUrl;
                    try {
                        thread=subParser.getDeclaredConstructor(new Class[]{
                                Element.class,String.class,String.class,String.class,String.class
                        }).newInstance(
                                post_ele,null,null,threadNo,threadUrl
                        ).parse();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }else{
                    replyCount +=1;
                    KThread post=null;
                    try {
                        post= subParser.getDeclaredConstructor(new Class[]{
                                Element.class,String.class,String.class,String.class,String.class
                        }).newInstance(
                                post_ele,threadNo,threadUrl,postNo,postUrl
                        ).parse();
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    if(post.getParents()==null || post.getParents().size()==0){
                        String finalThreadNo = threadNo;
                        post.setParents(new ArrayList<String>(){{ add(finalThreadNo); }});
                    }

                    thread= addTo(thread,post);
                }
            }
            thread.setReplyCount(replyCount);
            threads.add(thread);
        }
        dataset.addAll(threads);
        return dataset;
    }

    public KThread addTo(KThread thread, KThread post){
        thread.addPost(post.getParents(),post);
        return thread;
    }

    public Elements getThreads() {
        return installThreadTag(element.selectFirst("#threads")).select("div.thread");
    }

    public static Element installThreadTag(Element threads){
        //如果找不到thread標籤，就是2cat.komica.org，要用 installThreadTag() 改成標準綜合版樣式
        if (threads.selectFirst("div.thread") != null) return threads;

        //將thread加入threads中，變成標準綜合版樣式
        Element thread = threads.appendElement("div").addClass("thread");
        for (Element div : threads.children()) {
            thread.appendChild(div);
            if (div.tagName().equals("hr")) {
                threads.appendChild(thread);
                thread = threads.appendElement("div").addClass("thread");
            }
        }

        return threads;
    }
}
