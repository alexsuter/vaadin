package com.axonivy.product.listing.crawler;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.axonivy.product.listing.crawler.BuildsCrawler.Build;

class TestBuildsCrawler {

  @Test
  void get() throws IOException {
    try (var url = TestBuildsCrawler.class.getResourceAsStream("build.html")) {
      var crawler = new BuildsCrawler(url, "https://localhost/");
      var builds = crawler.get();
      assertThat(builds)
      .containsOnly(
              new Build("https://localhost/job/master/lastSuccessfulBuild/"),
              new Build("https://localhost/job/release%252F10.0/lastSuccessfulBuild/"),
              new Build("https://localhost/job/release%252F11.2/lastSuccessfulBuild/"),
              new Build("https://localhost/job/release%252F11.3/lastSuccessfulBuild/"),
              new Build("https://localhost/job/release%252F8.0/lastSuccessfulBuild/"),
              new Build("https://localhost/job/release%252F9.2/lastSuccessfulBuild/"),
              new Build("https://localhost/job/release%252F9.4/lastSuccessfulBuild/"),
              new Build("https://localhost/job/update-yaml-schemas_master-640/lastSuccessfulBuild/"));
    }
  }
}
