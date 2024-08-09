package com.example.application.views.channel;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import com.example.application.views.channel.BuildsCrawler.Build;

public class JobsCrawler {

  private final Build build;

  public JobsCrawler(Build build) {
    this.build = build;
  }

  public List<Job> get() {
    var jobs = new ArrayList<Job>();
    try {
      var doc = Jsoup.parse(URI.create(build.url()).toURL(), 2000);
      var aTags = doc.getElementsByTag("a");
      for (var aTag : aTags) {
        var href = aTag.attr("href");
        if (href.contains("AxonIvy") && href.endsWith(".zip") && !href.contains("Repository")) {
          var link = build.url() + href;
          var text = StringUtils.substringAfterLast(href, "/");
          var name = name(link);
          var page = new Job(name, text, link);
          jobs.add(page);
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

  private String name(String url) {
    url = StringUtils.substringAfterLast(url, "job/");
    url = StringUtils.substringBefore(url, "/");
    url = url.replace("%252F", "/");
    if (!url.equals("master")) {
      url = StringUtils.substringAfter(url, "release/");
    }
    return url;
  }

  public record Job(String name, String text, String url) {}
}
