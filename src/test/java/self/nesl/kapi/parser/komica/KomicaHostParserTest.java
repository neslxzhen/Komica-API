package self.nesl.kapi.parser.komica;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import self.nesl.kapi.po.Host;

import static org.junit.jupiter.api.Assertions.*;
import static self.nesl.kapi.utils.Utils.print;

class KomicaHostParserTest {

    KomicaHostParser komicaHostParser=new KomicaHostParser();
    @Test
    void parse() {
        print(new Gson().toJson(komicaHostParser.parse()));
    }
}