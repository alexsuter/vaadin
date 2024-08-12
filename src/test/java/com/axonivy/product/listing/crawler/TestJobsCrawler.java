package com.axonivy.product.listing.crawler;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.axonivy.product.listing.crawler.JobsCrawler.Job;

class TestJobsCrawler {

  @Test
  void get() throws IOException {
    try (var url = TestJobsCrawler.class.getResourceAsStream("job.html")) {
      var crawler = new JobsCrawler(url, "https://localhost/");
      var builds = crawler.get();
      var job1 = new Job("Designer", "11.4.0", "Linux", "x64", "AxonIvyDesigner11.4.0.2408091727_Linux_x64.zip", "https://localhost/lastSuccessfulBuild/artifact/workspace/ch.ivyteam.ivy.designer.product/target/products/AxonIvyDesigner11.4.0.2408091727_Linux_x64.zip");
      var job2 = new Job("Engine", "11.4.0", "All", "x64", "AxonIvyEngine11.4.0.2408091727_All_x64.zip", "https://localhost/lastSuccessfulBuild/artifact/workspace/ch.ivyteam.ivy.server.product/target/products/AxonIvyEngine11.4.0.2408091727_All_x64.zip");
      assertThat(builds).contains(job1, job2);
    }
  }
}
