package com.axonivy.product.listing.crawler;

import java.lang.module.ModuleDescriptor.Version;
import java.util.Comparator;

import com.axonivy.product.listing.crawler.JobsCrawler.Job;

public class JobComparator implements Comparator<Job> {

  @Override
  public int compare(Job o1, Job o2) {
    var v1 = Version.parse(o1.version());
    var v2 = Version.parse(o2.version());
    return v2.compareTo(v1);
  }
}
