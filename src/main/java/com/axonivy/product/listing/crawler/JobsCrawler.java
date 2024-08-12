package com.axonivy.product.listing.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

public class JobsCrawler {

  private final InputStream in;
  private final String baseUrl;

  public JobsCrawler(InputStream in, String baseUrl) {
    this.in = in;
    this.baseUrl = baseUrl;
  }

  public List<Job> get() {
    if (in == null) {
      return List.of();
    }
    var jobs = new ArrayList<Job>();
    try {
      var doc = Jsoup.parse(in, StandardCharsets.UTF_8.name(), baseUrl);
      var aTags = doc.getElementsByTag("a");
      for (var aTag : aTags) {
        var href = aTag.absUrl("href");
        if (href.contains("AxonIvy") && href.endsWith(".zip") && !href.contains("Repository")) {
          var job = new LinkToJobConverter(href).toJob();
          jobs.add(job);
        }
      }
      return jobs;
    } catch (HttpStatusException ex) {
      if (ex.getStatusCode() == 404) {
        return List.of();
      }
      throw new RuntimeException(ex);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public record Job(String artifact, String version, String os, String platform, String text, String url) {}
}
