package org.example;
/**********************************************************************************
 * Must be run before
 * cd /usr/bin/
 * sudo safaridriver --enable
 * version 230711
 **********************************************************************************/
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class Main extends JComponent
{
    private static String version = "230711";
    CityNameMapBuilder cityNameMapBuilder = new CityNameMapBuilder();
    private XSSFWorkbook sportDataWorkbook;
    private static HashMap<String, String> weekDateMap = new WeekDateMapBuilder().WeekDateMapBuilder();
    private HashMap<String, String> xRefMap = new HashMap<>();
    public WebSiteReader webSiteReader = new WebSiteReader();
    public ExcelBuilder excelBuilder = new ExcelBuilder();
    public static DataCollector dataCollector = new DataCollector();
    private static String season = "2022";
    private static String weekNumber = "7";
    public static WebDriver driver;
    public static JavascriptExecutor js;
    public static void main(String[] args) throws IOException, InterruptedException
    {
        System.out.println("SharpMarkets, version " + version + ", Copyright 2021 Dan Farris");
        new CityNameMapBuilder();//Builds full city name map to correct for Covers variations in team city names
        new WeekDateMapBuilder();//Builds Game dates for this week
        driver = new SafariDriver();
        driver.manage().window().maximize();
        js = (JavascriptExecutor)driver;
        dataCollector.setCityNameMap(CityNameMapBuilder.getCityNameMap());
        new Main().scrape();//Get out of static context
    }
    private void scrape() throws IOException, InterruptedException
    {
        String weekDate = weekDateMap.get(weekNumber);
        dataCollector.setThisSeason(season);
        Elements nflElements = webSiteReader.readCleanWebsite("https://www.covers.com/sports/nfl/matchups?selectedDate=" + weekDate);//Main Covers scores & matchups page
        Elements weekElements = nflElements.select(".cmg_game_data, .cmg_matchup_game_box");
        xRefMap = buildXref(weekElements);
        System.out.println(xRefMap);
        Main.driver.get("https://www.covers.com/sports/nfl/matchups");//Main Covers Scores & Matchups page
        for (Map.Entry<String, String> entry : xRefMap.entrySet())
        {
            String dataEventId = entry.getKey();
            String dataGame = xRefMap.get(dataEventId);
            System.out.println("Main65 START MAIN LOOP-----------------------------------------------------START MAIN LOOP FOR dataEventId/dataGame " + dataEventId + "/" + dataGame + "\t" + dataCollector.getAwayFullNameMap().get(dataEventId) + " @ " + dataCollector.getHomeFullNameMap().get(dataEventId) + "-------------------------------------------------------------------------------------------START MAIN LOOP");

            System.out.println("END MAIN LOOP----------------------------------------------------------------END MAIN LOOP FOR dataEventId/dataGame " + dataEventId + "/" + dataGame + "    " + dataCollector.getAwayFullNameMap().get(dataEventId) + " @ " + dataCollector.getHomeFullNameMap().get(dataEventId) + "-------------------------------------------------------------------------------------------END MAIN LOOP");
        }
        driver.quit();
        System.out.println("Proper Finish...HOORAY!");
    }
    public HashMap<String, String> buildXref(Elements weekElements)
    {
        for (Element e : weekElements)
        {
            String dataLinkString = e.attr("data-link");
            String[] dlsa = dataLinkString.split("/");
            String dataLink = dlsa[5];
            String dataEvent = e.attr("data-event-id");
            xRefMap.put(dataEvent, dataLink);
            System.out.println("Main.buildXref() dataEvent => " + dataEvent + ", dataLink => " + dataLink);
        }
        return xRefMap;
    }
}