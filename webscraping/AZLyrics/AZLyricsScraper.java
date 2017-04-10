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
        song = song.replaceAll(" ", "+");
        //System.out.println(song);
        String initialurl = "http://search.azlyrics.com/search.php?q="+song;
        /*
        * The general syntax for searching in AZLyrics is http://search.azlyrics.com/search.php?q=(something)
        * So, I just took the part till the '=' and added whatever the user wanted
        * Now, the links come under a bunch of <td> tags
        * When I scraped the <td> tags alone, I got some other "album" search results
        * The number of album search results were exactly 5, so I skipped the first 5 ones
         */
        Document site = Jsoup.connect(initialurl).get();
        Elements lyricsTable = site.select("div.panel");
        ArrayList<String> songNames = new ArrayList<>();
        ArrayList<String> urls = new ArrayList<>();
        for (Element elm : lyricsTable){
            if (elm.text().contains("Album")) {
                continue;
            }
            //System.out.println(elm);
            Elements table = elm.select("table > tbody > tr");
            for (Element elms : table) {
                if (elms.text().contains("More Song Results")) {
                    continue;
                }
                elms.select("small").html("");
                songNames.add(elms.text());
                urls.add(elms.select("a").attr("href"));

            }
        }
        for (int j = 0; j < urls.size(); j++) {
            System.out.println(songNames.get(j));
            System.out.println(urls.get(j));
        }
        System.out.println("Enter your option");
        int choice = in.nextInt();
        choice--;
        Document lyricPage = Jsoup.connect(urls.get(choice)).get();
        Elements lyricTags = lyricPage.select("div[class='col-xs-12 col-lg-8 text-center']>div");
        String lyrics = new String();
        /*
        * Now, that I've gotten the lyrics, I tried to scrape the tag which contains the lyrics
        * To remove a few <div> tags that had unnecessary text, I had to make a very very long if statement consisting of all the classes I didn't want
        * I was forced to make a long if statement because the div tag containing the lyrics did not have a class at all :(
         */
        for (Element elm : lyricTags) {
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
