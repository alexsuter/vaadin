package com.example.application.views.channel;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class ChannelView extends VerticalLayout {

    public ChannelView() throws IOException, InterruptedException {
      setSizeFull();

      var builds = new BuildCrawler().go();

      var pages =  builds.parallelStream()
              .flatMap(b -> new Cralwer().go(b.url).parallelStream())
              .toList();

      var grid = new Grid<Page>();
      //grid.setHeightFull();
      grid.setItems(pages);
      grid.addColumn(Page::text).setHeader("Text");
      grid.addColumn(Page::url).setHeader("Url");
      add(grid);
    }

    public record Person(String name) {

    }

    public record Build(String url) {}

    public record Page(String text, String url) {}


    public static class BuildCrawler {

      public List<Build> go() throws IOException, InterruptedException {
        var url = "https://jenkins.ivyteam.io/job/core_product/";

        var pages = new ArrayList<Build>();

        try (var client = HttpClient.newHttpClient()) {
          var request = HttpRequest.newBuilder(URI.create(url)).build();
          var content = client.send(request, BodyHandlers.ofString()).body();

          var jsoup = Jsoup.parse(content);
          var a = jsoup.getElementsByTag("a");

          for (var b : a) {
            var name = b.attr("title");
            if (name.contains("master") || name.contains("release")) {
              var u = url + b.attr("href") + "lastSuccessfulBuild/";
              pages.add(new Build(u));
            }
          }


          return pages;
        }
      }
    }

    public static class Cralwer {

      public List<Page> go(String url) {

        var pages = new ArrayList<Page>();

        try (var client = HttpClient.newHttpClient()) {
          var request = HttpRequest.newBuilder(URI.create(url)).build();
          var content = client.send(request, BodyHandlers.ofString()).body();

          var jsoup = Jsoup.parse(content);
          var a = jsoup.getElementsByTag("a");

          for (var b : a) {

            var href = b.attr("href");
            if (href.contains("AxonIvy") && href.endsWith(".zip") && !href.contains("Repository")) {
              var u = url + href;
              var text = StringUtils.substringAfterLast(href, "/");
              var page = new Page(text, u);
              pages.add(page);
            }

            //$href = $child->getAttribute('href');
            //if (str_contains($href, "AxonIvy") && str_ends_with($href, '.zip') && !str_contains($href, "Repository")) {
            //  $text = basename($href);
            //  $url = $baseUrl . $href;
            //  $link = new ProductLink($text, $url);
            //  $productLinks[] = $link;
            //}

//            var name = b.attr("title");
//            if (name.contains("master") || name.contains("release")) {
//              var u = url + b.attr("href") + "lastSuccessfulBuild/";
//              pages.add(new Page(u));
//            }
          }

          return pages;
        } catch (IOException | InterruptedException ex) {
          throw new RuntimeException(ex);
        }
      }
    }
}
