package self.nesl.kapi.po;

import java.util.Map;

public class Host {
    private String domain;
    private Map<String,Board[]> datasets;
    public Host(String domain,Map<String,Board[]> datasets) {
        this.domain=domain;
        this.datasets=datasets;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Map<String,Board[]> getDatasets() {
        return datasets;
    }

    public void setDatasets(Map<String,Board[]> datasets) {
        this.datasets = datasets;
    }
}
