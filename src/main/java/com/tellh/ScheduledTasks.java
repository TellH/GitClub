package com.tellh;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private int page = 2;
    private int repeat = 0;

//    //    @Scheduled(cron = "* * 18/3 * * ? ")
//    public void crawelNewPage() {
//        log.info("The time is now {}", dateFormat.format(new Date()));
////        List<Repository> repositoryList = new ArrayList<>();
//        try {
//            Document doc = Jsoup.connect("https://android-arsenal.com/").get();
//            Element content = doc.body().getElementsByClass("container").get(0);
//            Element projects = content.getElementById("projects");
//            Elements pcs = projects.getElementsByClass("pc");
//            for (Element pc : pcs) {
//                Element header = pc.getElementsByClass("header").get(0);
//                if (header.childNodeSize() <= 1)
//                    continue;
//                String name = header.child(0).child(0).text().replaceAll(" ", "");
//                Element ftr = pc.getElementsByClass("r").get(0);
//                String owner = ftr.getElementsByTag("a").text().replaceAll(" ", "");
//                try {
//                    jdbcTemplate.update("INSERT INTO arsenal_craweler.repository (`full_name`, `name`, `owner`) VALUES (?,?,?)",
//                            owner + "/" + name, name, owner);
//                    log.info("Success insert {}", owner + "," + name);
//                } catch (DataAccessException e) {
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public boolean crawelPage(int page) {
        log.info("The time is now {}, page: {}", dateFormat.format(new Date()), page);
        try {
            Document doc = Jsoup.connect("https://android-arsenal.com?sort=created&page=" + page).get();
            Element content = doc.body().getElementsByClass("container").get(0);
            Element projects = content.getElementById("projects");
            Elements pcs = projects.getElementsByClass("pc");
            for (Element pc : pcs) {
                try {
                    Element header = pc.getElementsByClass("header").get(0);
                    if (header.childNodeSize() <= 1)
                        continue;
                    String name = header.child(0).child(0).text().replaceAll(" ", "");
                    Element ftr = pc.getElementsByClass("r").get(0);
                    String owner = ftr.getElementsByTag("a").text().replaceAll(" ", "");
                    jdbcTemplate.update("INSERT INTO arsenal_craweler.repository (`full_name`, `name`, `owner`) VALUES (?,?,?)",
                            owner + "/" + name, name, owner);
                    log.info("Success insert {}", owner + "," + name);
                } catch (IndexOutOfBoundsException | NullPointerException e) {
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Exception: {} was throwed! messge: {}", e.getClass().getName(), e.getMessage());
//            e.printStackTrace();
            return false;
        }
    }

    //    @Scheduled(fixedDelay = 10000)
    @Scheduled(cron = "* * 10/10 * * ? ")
    public void doCrawel() {
//        while (true) {
//            if (page == 0) {
//                page = 2;
//                return;
//            }
        while (!crawelPage(1)) {
            if (repeat > 3) {
                break;
            }
            repeat++;
        }
//            page--;
        repeat = 0;
//        }
    }
}