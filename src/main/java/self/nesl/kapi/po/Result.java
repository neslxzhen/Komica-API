package self.nesl.kapi.po;

import java.util.ArrayList;
import java.util.Date;

public class Result {
    private String datasetId = null;
    private String url = null;
    private Date update = null;
    private ArrayList<KThread> threads=new ArrayList<KThread>();

    public Result(String datasetId, String url) {
        this.datasetId = datasetId;
        this.url = url;
    }

    public ArrayList<KThread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<KThread> threads) {
        this.threads = threads;
    }

    public void addThread( KThread t){
        threads.add(t);
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }
}
