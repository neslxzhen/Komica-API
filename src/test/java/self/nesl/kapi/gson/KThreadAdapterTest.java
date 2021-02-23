package self.nesl.kapi.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import self.nesl.kapi.KapiApplication;
import self.nesl.kapi.KapiApplicationTests;
import self.nesl.kapi.po.KThread;

import static org.junit.jupiter.api.Assertions.*;
import static self.nesl.kapi.utils.Utils.print;

class KThreadAdapterTest {
    Gson gson = new GsonBuilder().registerTypeAdapter(KThread.class, new KThreadAdapter()).create();

    @Test
    void write(){
        String s2=gson.toJson(KapiApplicationTests.parse());
        print(this,"write",s2);
    }

    @Test
    void read(){
        String json=gson.toJson(KapiApplicationTests.parse());
        KThread kThread=gson.fromJson(json, KThread.class);
        print(this,"read",kThread.toString());
    }
}