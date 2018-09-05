package microYoga.model.wx;

public class Activity {
    private String id;
    private String name;
    private String publishPage;
    private String registerPage;
    private String participatePage;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishPage() {
        return publishPage;
    }

    public void setPublishPage(String publishPage) {
        this.publishPage = publishPage;
    }

    public String getRegisterPage() {
        return registerPage;
    }

    public void setRegisterPage(String registerPage) {
        this.registerPage = registerPage;
    }

    public String getParticipatePage() {
        return participatePage;
    }

    public void setParticipatePage(String participatePage) {
        this.participatePage = participatePage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
