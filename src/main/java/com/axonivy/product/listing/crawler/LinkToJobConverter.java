package com.axonivy.product.listing.crawler;

import org.apache.commons.lang3.StringUtils;

import com.axonivy.product.listing.crawler.JobsCrawler.Job;

class LinkToJobConverter {

  private final String link;

  LinkToJobConverter(String link) {
    this.link = link;
  }

  public Job toJob() {
    var artifact = "";

    var text = StringUtils.substringAfterLast(link, "/");

    var temp = text;
    temp = StringUtils.substringBeforeLast(temp, ".");
    if (text.startsWith("AxonIvyDesigner")) {
      artifact = "Designer";
      temp = StringUtils.removeStart(temp, "AxonIvyDesigner");
    } else {
      artifact = "Engine";
      temp = StringUtils.removeStart(temp, "AxonIvyEngine");
    }

    var platform = StringUtils.substringAfterLast(temp, "_");

    var version = StringUtils.substringBefore(temp, "_");
    version = StringUtils.substringBeforeLast(version, ".");

    var os = StringUtils.substringAfter(temp, "_");
    os = StringUtils.substringBeforeLast(os, "_");

    return new Job(artifact, version, os, platform, text, link);
  }
}
