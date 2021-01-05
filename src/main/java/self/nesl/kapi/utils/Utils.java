package self.nesl.kapi.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class Utils {
    public static void print(String... strings){
        System.out.println(String.join(",",strings));
    }

    public static void print(Collection objects){
        System.out.println(String.join(",",objects.toString()));
    }

    public static void print(Object... objects){
        System.out.println(String.join(",",Arrays.asList(objects).stream().map(Objects::toString).collect(Collectors.toList())));
    }

    public static String escape(String s){
        for(String c : new String[]{"\\?","\\."}){
            s=s.replaceAll(c,"\\"+c);
        }
        return s;
    }
}