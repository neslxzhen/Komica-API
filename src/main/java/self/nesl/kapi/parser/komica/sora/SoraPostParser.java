package self.nesl.kapi.parser.komica.sora;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import self.nesl.kapi.parser.ParserFactory;
import self.nesl.kapi.utils.UrlUtils;
import self.nesl.kapi.parser.Parser;
import self.nesl.kapi.po.KThread;

import java.util.ArrayList;
import java.util.Calendar;

import static self.nesl.kapi.parser.ParserUtils.*;
import static self.nesl.kapi.utils.Utils.print;

public class SoraPostParser extends Parser {
//    komica.org (
//            [綜合,男性角色,短片2,寫真],
//            [新番捏他,新番實況,漫畫,動畫,萌,車],
//            [四格],
//            [女性角色,歡樂惡搞,GIF,Vtuber],
//            [蘿蔔,鋼普拉,影視,特攝,軍武,中性角色,遊戲速報,飲食,小說,遊戲王,奇幻/科幻,電腦/消費電子,塗鴉王國,新聞,布袋戲,紙牌,網路遊戲]
//            )

    public String threadId;
    public String threadUrl;
    public String postNo;
    public String postUrl;
    public KThread post;

    public SoraPostParser(Element element,String threadId,String threadUrl,String postNo,String postUrl) {
        super(postUrl,element);
        this.threadId=threadId;
        this.threadUrl=threadUrl;
        this.postNo=postNo;
        this.postUrl=postUrl;
    }

    public String getId(String postUrl){
        String last=postUrl;
        String path= ParserFactory.getPath(getClass());
        if(postUrl.contains(path)){
            last=postUrl.substring(postUrl.indexOf(path)+path.length());
        }
        String r="#r";
        if(postUrl.contains(r)){
            last=last.substring(last.indexOf(r)+r.length());
        }
        return last;
    }

    public KThread parse() {
        post =new KThread(url,getId(url));
        post.setOrigin(element);
        setPicture();
        setDetail();
        setQuote();
        setParent();
        setDescription();
        post.setUpdate(Calendar.getInstance().getTime());
        return post;
    }

    public void setDetail() {
        try {
            installDefaultDetail();
        } catch (NullPointerException e) {
//            e.printStackTrace();
            try {
                install2catDetail();
            } catch (NullPointerException | StringIndexOutOfBoundsException e2) {
//                e.printStackTrace();
                installAnimeDetail();
            }
        }
    }

    public void setPicture() {
        try {
            Element thumbImg = element.selectFirst("img");
            String originalUrl = thumbImg.parent().attr("href");

            String url=new UrlUtils(originalUrl,null).getUrl();
            post.setPictureUrl(url);
        } catch (NullPointerException ignored) {}
    }

    public void installDefaultDetail() { // 綜合: https://sora.komica.org
        post.setTitle(whenNotNull(element.selectFirst("span.title")));
        String[] splited=element.selectFirst("div.post-head span.now").text().split("ID:");
        post.setTime(parseTime(parseChiToEngWeek(splited[0])));
        String poster=splited[1];
        poster=poster.replaceAll("\\(\\d\\/\\d\\)","");
        if(poster.toLowerCase().startsWith("id:")){
            poster=poster.substring(3);
        }
        post.setPoster(poster);
    }

    private String whenNotNull(Element element){
        return element!=null?element.text():null;
    }

    public void install2catDetail() { // 新番捏他: https://2cat.komica.org/~tedc21thc/new
        Element detailEle = element.selectFirst(String.format("label[for='%s']", post.getPostId()));
        Element titleEle = detailEle.selectFirst("span.title");
        if (titleEle != null) {
            post.setTitle(titleEle.text().trim());
            titleEle.remove();
        }

        String s = detailEle.text().trim();
        String[] post_detail = s.substring(1, s.length() - 1).split(" ID:");
        post.setTime(parseTime(parseJpnToEngWeek(post_detail[0].trim())));
        post.setPoster(post_detail[1]);
    }

    public void installAnimeDetail() { // 動畫: https://2cat.komica.org/~tedc21thc/anime/ 比起 2cat.komica 沒有label[for="3273507"]
        String detailStr = element.ownText();
        detailStr = detailStr.length() == 0 ? element.text() : detailStr;
        String[] post_detail = detailStr.split(" ID:");
        post.setTime(parseTime(parseChiToEngWeek(post_detail[0].substring(post_detail[0].indexOf("[") + 1).trim())));
        post.setPoster(post_detail[1].substring(0, post_detail[1].indexOf("]")));
    }

    public void setQuote() {
        post.setQuote(element.selectFirst("div.quote"));
    }

    public void setParent(){
        Elements eles = element.select("span.resquote a.qlink");
        ArrayList<String> parents=new ArrayList<>();
        if (eles.size() <= 0 && threadId!=null) {
            // is main
            parents.add(threadId);
        } else {
            for (Element resquote : eles) {
                String resquote_id = resquote.attr("href").replace("#r", "");
                parents.add(resquote_id);
            }
        }
        post.setParents(parents);
    }

    public void setDescription() {
        String ind = post.getQuoteText().replaceAll(">>(No\\.)*[0-9]{6,} *(\\(.*\\))*", "");
        ind = ind.replaceAll(">+.+\n", "");
        post.setDesc(ind.trim());
    }
}
