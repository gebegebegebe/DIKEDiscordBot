package events;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class scraperEvents {
    private boolean imageFlag;
    private boolean emailFlag;

    public scraperEvents(){
        this.imageFlag = false;
        this.emailFlag = false;
    }

    static{
        System.setProperty("jdk.tls.maxHandshakeMessageSize", "55000");
    }

    public boolean getImageFlag(){
        return this.imageFlag;
    }

    public boolean getEmailFlag(){
        return this.emailFlag;
    }

    public String getLink(int n) throws IOException{
        String url = "http://dcse.fmipa.ugm.ac.id/site/en/profil-pengajar/";
        boolean flag1 = false;
        boolean flag2 = false;
        String constructLink = "";
        int cutoff;
        String str = " " + n;
        if(n>9) {
            cutoff = 9;
        } else {
            cutoff = 8;
        }
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();
        for (Element a : doc.select("div.post-content div")) {
            if(!flag1) {
                if (str.equals(a.toString().substring(6, cutoff))) {
                    flag1 = true;
                    continue;
                }
            } else {
                String[] pieces = a.toString().split("\"");
                for(String s : pieces){
                    if(s.contains("http")){
                        constructLink = s;
                        break;
                    }
                }
                break;
            }
        }
        if(!flag1){
            for (Element a : doc.select("div.post-content td")){
                String temp = "" + n;
                if(a.toString().contains(temp)) {
                    flag2 = true;
                    continue;
                }
                if(flag2){
                    String[] pieces = a.toString().split("\"");
                    for(String s : pieces){
                        if (s.contains("http")){
                            constructLink = s;
                            return constructLink;
                        }
                    }
                }
            }
        }
        //System.out.println(constructLink);
        return constructLink;
    }

    public String getDosenName(String url) throws IOException{
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();
        Elements a = doc.select("h1.post-title");
        String str = a.toString();
        String[] pieces = str.split("[<>]");
        //System.out.println(pieces[2]);
        return pieces[2];
    }

    public String getEmail(String url) throws IOException{
        try {
            String email = "";
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();
            Element a = doc.select("article.single-post tr").first();
            String str = a.toString();
            //String str2 = str.substring(66,str.length()-12);
            String[] list = str.split("[<>]");
            for (String i : list) {
                if(i.contains("@")){
                    email = i;
                    this.emailFlag = true;
                    break;
                }
            }
            if (email.contains(" ")) {
                email = email.replace(" ", "");
            }
            if (email.contains("&nbsp;")) {
                email = email.replace("&nbsp;", "");
            }
            if (email.contains("|")) {
                email = email.replace("|", "");
            }
            if (email.contains("Email:")) {
                email = email.replace("Email:", "");
            }
            if (this.emailFlag) {
                //System.out.println(email);
                return email;
            }
            this.emailFlag = false;
            return "";
        } catch (NullPointerException n){
            this.emailFlag = false;
            return "";
        }
    }

    public String getImageURL(String url) throws IOException{
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .get();
        try {
            Element a = doc.select("div.post-content img").first();
            String[] pieces = a.toString().split("\"");
            if (pieces[3].contains("http")) {
                this.imageFlag = true;
                //System.out.println(pieces[3]);
                return pieces[3];
            }
            this.imageFlag = false;
            return "";
        } catch (NullPointerException n){
            this.imageFlag = false;
            return "";
        }
    }
}
