package org.example;
/*******************************************************************
 * Covers NFL Extraction Tool
 * Copyright 2020 Dan Farris
 * version 230711
 * Reads/cleans input URL and returns all Elements and Document
 *******************************************************************/
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import static org.jsoup.Jsoup.connect;
public class WebSiteReader
{
    private Document dirtyDoc;
    public Elements readCleanWebsite(String urlToRead) throws IOException
    {
        dirtyDoc = Jsoup.parse(String.valueOf(connect(urlToRead).get()));
        return getDirtyDoc().getAllElements();
    }
    public Document getDirtyDoc() {
        return dirtyDoc;
    }
}



