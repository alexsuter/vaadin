package com.example.application.views.channel;

import java.util.Comparator;

import com.example.application.views.channel.JobsCrawler.Job;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrderBuilder;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("")
public class ChannelView extends AppLayout {

    public ChannelView() {

      DrawerToggle toggle = new DrawerToggle();

      H1 title = new H1("Axon Ivy Product Listing");
      title.getStyle().set("font-size", "var(--lumo-font-size-l)")
              .set("margin", "0");

      SideNav nav = getSideNav();

      Scroller scroller = new Scroller(nav);
      scroller.setClassName(LumoUtility.Padding.SMALL);

      addToDrawer(scroller);
      addToNavbar(toggle, title);


      var builds = new BuildsCrawler("https://jenkins.ivyteam.io/job/core_product/").get();
      var pages =  builds.parallelStream()
              .flatMap(b -> new JobsCrawler(b).get().parallelStream())
              .toList();

      var grid = new Grid<Job>();
      grid.setItems(pages);


      var nameColumn = grid
        .addColumn(Job::name)
        .setHeader("Name")
        .setWidth("10%")
        .setSortable(false)
        .setComparator(new Comparator<Job>() {

          @Override
          public int compare(Job o1, Job o2) {
            if (o2.name().equals("master")) {
              return 1;
            }
            if (o1.name().equals("master")) {
              return -1;
            }
            var v1 = Double.parseDouble(o1.name());
            var v2 = Double.parseDouble(o2.name());
            return (int) v2 - (int) v1;
          }
        });


      var sortOrder = new GridSortOrderBuilder<Job>();
      sortOrder.thenAsc(nameColumn);
      grid.sort(sortOrder.build());

      grid.addColumn(LitRenderer.<Job>of("""
                 <a href="${item.url}">${item.text}</a>
              """)
              .withProperty("url", p -> p.url())
              .withProperty("text", p -> p.text())
      ).setHeader("Link")
      .setWidth("90%");

      grid.setHeightFull();
      setContent(grid);

    }

    private SideNav getSideNav() {
      var sideNav = new SideNav();
      sideNav.addItem(new SideNavItem("Downloads", "/", VaadinIcon.DASHBOARD.create()));
      return sideNav;
  }
}
