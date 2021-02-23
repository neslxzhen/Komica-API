package self.nesl.kapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import self.nesl.kapi.gson.KThreadAdapter;
import self.nesl.kapi.parser.ParserFactory;
import self.nesl.kapi.po.KThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static self.nesl.kapi.utils.Utils.print;

@SpringBootTest
public
class KapiApplicationTests {

	@Test
	void contextLoads() {
	}

	public static KThread parse(){
		return parseThread();
	}

	public static KThread parseThreads(){
		String ele=null;
		try {
			File f = new File("src\\test\\html\\SoraThreads.html");
			InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
			StringBuilder builder = new StringBuilder();
			int ch;
			while((ch = read.read()) != -1){
				builder.append((char)ch);
			}
			ele=builder.toString();
		}catch (Exception ignored){}
		return new ParserFactory("https://sora.komica.org/00").getParser(Jsoup.parse(ele)).parse();
	}

	public static KThread parseThread(){
		String ele=null;
		try {
			File f = new File("src\\test\\html\\SoraThread.html");

			InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
			StringBuilder builder = new StringBuilder();
			int ch;
			while((ch = read.read()) != -1){
				builder.append((char)ch);
			}
			ele=builder.toString();
		}catch (Exception ignored){}
		return new ParserFactory("https://sora.komica.org/00/pixmicat.php?res=21647582").getParser(Jsoup.parse(ele)).parse();
	}
}
