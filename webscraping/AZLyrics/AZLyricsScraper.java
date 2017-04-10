import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;


/**
 * Created by Aakash on 4/9/2017.
 */
public class AZLyricsScraper {
    public static void main(String[] args) throws IOException {
        System.out.println("What song do you want?");
        Scanner in = new Scanner(System.in);
        String song = in.nextLine();
        Document site = Jsoup.connect("http://search.azlyrics.com/search.php?q="+song).get();
        Elements lyricsTable = site.select("td");
        ArrayList<String> songNames = new ArrayList<>();
        ArrayList<String> urls = new ArrayList<>();
        int i = 0;
        for(Element elm : lyricsTable) {
            if(i<6) {
                i++;
                continue;
            }
            Elements newElm = elm.select("small");
            for(Element elms : newElm) {
                newElm.html("");
            }
            String name = elm.text();
            if(name.contains("More Song Results")) {
                continue;
            }
            String url = elm.select("a").first().attr("href");
            songNames.add(name);
            urls.add(url);
        }
        for (int j = 0; j < urls.size(); j++) {
            System.out.println(songNames.get(j));
        }
        System.out.println("Enter your option");
        int choice = in.nextInt();
        choice--;
        Document lyricPage = Jsoup.connect(urls.get(choice)).get();
        Elements lyricTags = lyricPage.select("div[class='col-xs-12 col-lg-8 text-center']>div");
        String lyrics = new String();
        for(Element elm : lyricTags){
            if(elm.attr("class").equals("div-share noprint")||elm.attr("class").equals("collapse noprint")||elm.attr("class").equals("panel album-panel noprint")||elm.attr("class").equals("noprint")||elm.attr("class").equals("smt")||elm.attr("class").equals("hidden")||elm.attr("class").equals("smt noprint")||elm.attr("class").equals("div-share")||elm.attr("class").equals("lyricsh")||elm.attr("class").equals("ringtone")) {
                continue;
            }
            lyrics = elm.text();
            break;
            //System.out.println(elm.text());
        }
        System.out.println(lyrics);
    }
}
