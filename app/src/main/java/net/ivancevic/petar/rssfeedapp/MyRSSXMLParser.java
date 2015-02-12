package net.ivancevic.petar.rssfeedapp;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petar on 22.12.2014..
 */
public class MyRSSXMLParser {
    private static final String ns = null;


    public static void parse (InputStream in) throws IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readFeed(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private static void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List feedItems = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "rss");
        parser.nextTag();
        while( parser.next() != XmlPullParser.END_TAG ) {
            if( parser.getEventType() != XmlPullParser.START_TAG ) {
                continue;
            }

            String name = parser.getName();
            if(name.equals("item")) {
                MainActivity.feedData.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
    }

    //procitaj svaki item i vrati ga za unos u listu
    private static feedItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        String title = null;
        String description = null;
        String creator = null;
        String pubDate = null;
        int numComments = 0;

        //da krenemo od item tag-a
        parser.require(XmlPullParser.START_TAG, ns, "item");

        while( parser.next() != XmlPullParser.END_TAG ) {
            if( parser.getEventType() != XmlPullParser.START_TAG ) {
                continue;
            }


            String name = parser.getName();
            if( name.equals("title") ) {
                title = readTitle(parser);
            } else if( description == null && (name.equals("content:encoded") || name.equals("description"))) {
                description = readDescription(parser);
            } else if( name.equals("dc:creator")) {
                creator = readCreator(parser);
            } else if( name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            } else if( name.equals("slash:comments")) {
                numComments = readComments(parser);
            } else {
                skip(parser);
            }
        }

        feedItem tempItem = new feedItem(title, description, creator, pubDate, numComments);
        return tempItem;
    }

// =========================================================
// =========================================================
//citanje pojedinacnih tagova
// =========================================================
// =========================================================

    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private static String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        String description;

        if(parser.getName().equals("content:encoded")) {
            parser.require(XmlPullParser.START_TAG, ns, "content:encoded");
            description = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "content:encoded");
        } else {
            parser.require(XmlPullParser.START_TAG, ns, "description");
            description = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "description");
        }
        return description;
    }

    private static String readCreator(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "dc:creator");
        String creator = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "dc:creator");
        return creator;
    }

    private static String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }

    private static int readComments(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "slash:comments");
        int numComments = Integer.parseInt(readText(parser));
        parser.require(XmlPullParser.END_TAG, ns, "slash:comments");
        return numComments;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    //preskoci item
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

