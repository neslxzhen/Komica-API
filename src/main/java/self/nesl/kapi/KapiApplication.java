package self.nesl.kapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import self.nesl.kapi.db.XmlDB;
import self.nesl.kapi.gson.KThreadAdapter;
import self.nesl.kapi.parser.ParserFactory;
import self.nesl.kapi.po.KThread;
import self.nesl.kapi.po.Post;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import static self.nesl.kapi.utils.Utils.print;

class Form {
	private String url;
	private int page;
	private String element;

	public Form(String url,int page, String element) {
		this.url = url;
		this.page = page;
		this.element = element;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public void setPage(String page) {
		if(page==null){
			this.page=0;
			return;
		}
		this.page = Integer.getInteger(page);
	}
}

// https://spring.io/quickstart

@SpringBootApplication
@RestController
public class KapiApplication {
	Gson gson = new GsonBuilder().registerTypeAdapter(KThread.class, new KThreadAdapter()).create();

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(KapiApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "65533"));
		app.run(args);
	}

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	private ResponseEntity getEntity(String body){
		return getEntity(body,null,0);
	}

	private ResponseEntity getEntity(String body,String url,int page){
		HttpStatus status=(body==null||body.equals("null"))?HttpStatus.NOT_FOUND:HttpStatus.OK;
		String resUrl;
		if(url!=null && page!=0){
			resUrl=new ParserFactory(url).getPageUrl(page);
		}else {
			resUrl=url;
		}

		return ResponseEntity.status(status)
				.header("url",resUrl)
				.body(body);
	}

	/*
	* thread
	* return KThread
	* */

	@PostMapping("/thread")
	public ResponseEntity createThread(Form request) {
		KThread thread = new ParserFactory(request.getUrl()).getParser(Jsoup.parse(request.getElement())).parse();
		PostDB.add(thread);
		return getEntity(gson.toJson(thread),request.getUrl(),request.getPage());
	}

	@GetMapping("/thread")
	public ResponseEntity getThread(@RequestHeader(value="url") String url,@RequestHeader(value="page") int page) {
		KThread thread = null;
		return getEntity(gson.toJson(thread),url,page);
	}

	/*
	* hosts, boards
	* return ArrayList<KThread>
	* */

	@GetMapping("/hosts")
	public ResponseEntity getHosts() {
		return getEntity(new Gson().toJson(XmlDB.getHosts()));
	}
}

