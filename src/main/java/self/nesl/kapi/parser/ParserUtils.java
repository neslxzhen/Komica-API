package self.nesl.kapi.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static self.nesl.kapi.utils.Utils.print;

public class ParserUtils {
    public static Date parseTime(String time){
        for(String s : Arrays.asList(
                "yyyy/MM/dd(EEE) HH:mm:ss.SSS",
                "yy/MM/dd(EEE) HH:mm:ss",
                "yy/MM/dd(EEE)HH:mm:ss",
                "yy/MM/dd(EEE)HH:mm",
                "yy/MM/dd HH:mm:ss" // mymoe
        )){
            try {
                return new SimpleDateFormat(s, Locale.ENGLISH).parse(time);
            }catch (ParseException ignored) {}
        }
        return null;
    }

    static String[] engWeek=new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

    public static String parseChiToEngWeek(String s){
        if(s==null)return null;
        String[] chiWeek=new String[]{"一","二","三","四","五","六","日"};
        for(int i=0;i<chiWeek.length;i++){
            s=s.replace(chiWeek[i],engWeek[i]);
        }
        return s;
    }

    public static String parseJpnToEngWeek(String s){
        String[] jpnWeek=new String[]{"月","火","水","木","金","土","日"};
        for(int i=0;i<jpnWeek.length;i++){
            s=s.replace(jpnWeek[i],engWeek[i]);
        }
        return s;
    }
}
