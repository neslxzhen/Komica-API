package self.nesl.kapi.db;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import self.nesl.kapi.gson.KThreadAdapter;
import self.nesl.kapi.parser.HostParser;
import self.nesl.kapi.po.Host;
import self.nesl.kapi.po.KThread;
import java.io.File;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.ArrayList;
import java.util.List;

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

    public static void writeHosts(){
        for (HostParser hostParser:new HostParser[]{

        }){
            writeHost(hostParser.parse());
        }
    }

    private static void writeHost(Host host) {
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
            string.setName(host.getDomain());
            string.setDesc(gson.toJson(host));
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

    public static Host[] getHosts(){
        ArrayList<Host> hosts=new ArrayList<Host>();
        try {
            File file = new File(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(MyMap.class);
            Unmarshaller m = jaxbContext.createUnmarshaller();
            MyMap map = (MyMap) m.unmarshal(file);
            for (MyString myString :map.getStrings())
                hosts.add(gson.fromJson(myString.getDesc(),Host.class));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return hosts.toArray(new Host[0]);
    }
}
