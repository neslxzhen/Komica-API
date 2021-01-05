package self.nesl.kapi.db;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import self.nesl.kapi.gson.KThreadAdapter;
import self.nesl.kapi.parser.Parser;
import self.nesl.kapi.parser.ParserFactory;
import self.nesl.kapi.parser.komica.Komica50BoardsParser;
import self.nesl.kapi.parser.komica.KomicaBoardsParser;
import self.nesl.kapi.parser.komica.sora.SoraPostParser;
import self.nesl.kapi.po.KThread;
import self.nesl.kapi.po.Post;
import self.nesl.kapi.utils.UrlUtils;

import java.io.File;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name="map")
class MyMap {
    private List<MyString> strings;
    public List<MyString> getStrings() {
        return strings;
    }

    public void setStrings(List<MyString> string) {
        this.strings = string;
    }
}

@XmlRootElement
class MyString {
    String name;
    String desc;

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    @XmlValue
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}

public class XmlDB {
    private final static String xml="src\\main\\resources\\xml\\hosts.xml";
    private static Gson gson = new GsonBuilder().registerTypeAdapter(KThread.class, new KThreadAdapter()).create();
    private static String MAP_NAME_COLUMN="name";
    private static String MAP_URL_COLUMN="url";
    private static String MAP_BOARDS_PARSER_COLUMN="board_parser";


    public static void writeHosts(){
        for (Map map:new Map[]{
                new HashMap<String, Object>() {{
                    put(MAP_NAME_COLUMN,"top50.komica.org");
                    put(MAP_URL_COLUMN, "https://komica.org/mainmenu2019.html");
                    put(MAP_BOARDS_PARSER_COLUMN, Komica50BoardsParser.class);
                }},
                new HashMap<String, Object>() {{
                    put(MAP_NAME_COLUMN,"komica.org");
                    put(MAP_URL_COLUMN, "https://komica.org/bbsmenu.html");
                    put(MAP_BOARDS_PARSER_COLUMN, KomicaBoardsParser.class);
                }},
                new HashMap<String, Object>() {{
                    put(MAP_NAME_COLUMN,"top50.komica2.net");
                    put(MAP_URL_COLUMN, "https://komica2.net/mainmenu2018.html");
                    put(MAP_BOARDS_PARSER_COLUMN, Komica50BoardsParser.class);
                }},
                new HashMap<String, Object>() {{
                    put(MAP_NAME_COLUMN,"komica2.net");
                    put(MAP_URL_COLUMN, "https://komica2.net/bbsmenu2018.html");
                    put(MAP_BOARDS_PARSER_COLUMN, KomicaBoardsParser.class);
                }},
        }){
            String url=(String) map.get(MAP_URL_COLUMN);
            String datasetId=(String) map.get(MAP_NAME_COLUMN);
            Element element = null;
            try {
                element=Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Class<? extends Parser> parser=(Class<? extends Parser>) map.get(MAP_BOARDS_PARSER_COLUMN);
            KThread thread = null;
            try {
                thread=parser.getDeclaredConstructor(new Class[]{
                        String.class,String.class,Element.class
                }).newInstance(
                        datasetId, url,element
                ).parse();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

            writeHost(url,thread);
        }
    }

    private static void writeHost(String menuUrl, KThread boards) {
        File file = null;
        JAXBContext jaxbContext = null;
        MyMap map = null;
        try {
            // read
            file = new File(xml);
            jaxbContext = JAXBContext.newInstance(MyMap.class);
            Unmarshaller m = jaxbContext.createUnmarshaller();
            map = (MyMap) m.unmarshal(file);
        }catch (UnmarshalException e){
            map=new MyMap();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        try{
            // do
            MyString string=new MyString();
            string.setName(menuUrl);
            string.setDesc(gson.toJson(boards));
            if(map.getStrings()==null){
                map.setStrings(new ArrayList(){{ add(string); }});
            }else{
                map.getStrings().add(string);
            }

            // write
            Marshaller m2 = jaxbContext.createMarshaller();
            m2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m2.marshal(map, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<KThread> getHosts(){
        ArrayList<KThread> kThreads=new ArrayList<KThread>();
        try {
            File file = new File(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(MyMap.class);
            Unmarshaller m = jaxbContext.createUnmarshaller();
            MyMap map = (MyMap) m.unmarshal(file);
            for (MyString myString :map.getStrings())
                kThreads.add(gson.fromJson(myString.getDesc(), KThread.class));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return kThreads;
    }
}
