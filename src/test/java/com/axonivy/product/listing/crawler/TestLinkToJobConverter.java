package com.axonivy.product.listing.crawler;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.axonivy.product.listing.crawler.JobsCrawler.Job;

class TestLinkToJobConverter {

  @Test
  void toJob() {
    var link = "https://localhost/lastSuccessfulBuild/artifact/workspace/ch.ivyteam.ivy.designer.product/target/products/AxonIvyDesigner11.4.0.2408091727_Linux_x64.zip";
    var job = new LinkToJobConverter(link).toJob();
    assertThat(job).isEqualTo(new Job("Designer", "11.4.0", "Linux", "x64", "AxonIvyDesigner11.4.0.2408091727_Linux_x64.zip", link));
  }
}
