package self.nesl.kapi.parser;
import org.jsoup.nodes.Element;
import self.nesl.kapi.utils.UrlUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
public class ParserFactory {
    public final static String MAP_HOST_COLUMN = "host";
    public final static String MAP_PARSER_COLUMN = "parser";
    public final static String MAP_PATH_COLUMN = "path";
    public final static String MAP_PAGE_URL_COLUMN = "pageUrl";

    private String url;
    private String host;
    private Class<? extends Parser> parser;

    public ParserFactory(String url) {
        this.url = url;
        this.host = new UrlUtils(url).getHost();
        this.parser=(Class<? extends Parser>) getParser(host,MAP_PARSER_COLUMN);
    }

    public Parser getParser(Element element) {
        try {
            return parser.getDeclaredConstructor(new Class[]{
                    String.class,Element.class,
            }).newInstance(
                    url,element
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPageUrl(int page){
        if(page==0)return url;
        return ((String) getParser(host,MAP_PAGE_URL_COLUMN))
                .replaceAll("\\{\\{url}}",url)
                .replaceAll("\\{\\{page}}",page+"");
    }

    public static String getPath(Class<? extends Parser> parser){
        return ((String) getParserOther(parser,MAP_PATH_COLUMN));
    }

    private static Object getParserOther(Class<? extends Parser> parser, String column){
        for (Map map:new Map[]{
        }){
            if(map.get(MAP_PARSER_COLUMN).equals(parser)){
                return map.get(column);
            }
        }
        return null;
    }

    private static Object getParser(String host,String column) {
        for (Map map:new Map[]{
                new HashMap<String, Object>(){{
                    put(MAP_PARSER_COLUMN, null);
                    put(MAP_PAGE_URL_COLUMN, null);
                    put(MAP_HOST_COLUMN, new String[]{
                            "vi.anacel.com",  // Figure/GK
                            "acgspace.wsfun.com",  // 艦隊收藏
                            "p.komica.acg.club.tw",
                            "mymoe.moe", // 綜合2
                            "secilia.zawarudo.org",
                    });
                }},
                // add komica model item in there
        }) {
            for (String s: (String[]) map.get(MAP_HOST_COLUMN)) {
                if(host.contains(s))
                    return map.get(column);
        }

    } return null;
}}
