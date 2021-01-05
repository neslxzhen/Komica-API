package self.nesl.kapi;

import self.nesl.kapi.po.KThread;
import self.nesl.kapi.po.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PostDB {
    public static Stream<Post> stream(){
        return new ArrayList<Post>().stream();
    }

    public static void add(KThread thread){

    }

    public static void add(Post post){

    }

    public static void replace(Post post){

    }

    public static void addAll(ArrayList<KThread> list){

    }
}
