package com.axonivy.product.listing.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

public class BuildsCrawler {

  private final InputStream in;
  private final String baseUrl;

  public BuildsCrawler(InputStream in, String baseUrl) {
    this.in = in;
    this.baseUrl = baseUrl;
  }

  public List<Build> get() {
    if (in == null) {
      return List.of();
    }
    var builds = new ArrayList<Build>();
    try {
      var doc = Jsoup.parse(in, StandardCharsets.UTF_8.name(), baseUrl);
      var aTags = doc.getElementsByTag("a");
      for (var aTag : aTags) {
        var title = aTag.attr("title");
        if (title.contains("master") || title.contains("release")) {
          var link = aTag.absUrl("href") + "lastSuccessfulBuild/";
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
