package com.example.application.views.channel;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

public class BuildsCrawler {

  private final String url;

  public BuildsCrawler(String url) {
    this.url = url;
  }

  public List<Build> get() {
    var builds = new ArrayList<Build>();
    try {
      var doc = Jsoup.parse(URI.create(url).toURL(), 2000);
      var aTags = doc.getElementsByTag("a");
      for (var aTag : aTags) {
        var title = aTag.attr("title");
        if (title.contains("master") || title.contains("release")) {
          var link = url + aTag.attr("href") + "lastSuccessfulBuild/";
          builds.add(new Build(link));
        }
      }
      return builds;
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public record Build(String url) {}
}
