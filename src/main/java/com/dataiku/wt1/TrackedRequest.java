package com.dataiku.wt1;

import java.util.HashMap;
import java.util.Map;

import com.dataiku.wt1.controllers.PixelServlet;


/**
 * Model object for one tracking event.
 */
public class TrackedRequest {
    /** IP address of the visitor */
    public String origAddress;
    /** Value of the visitor id */
    public String visitorCookieValue;
    /** Value of the visitor params, encoded as an HTTP query string */
    public String visitorParamsCookieValue;
    /** Value of the sesion params, encoded as an HTTP query string */
    public String sessionParamsCookieValue;

    /** URL of the page from which the tracking event originated */
    public String page;
    /** HTTP referer of the page from which the tracking event originated */
    public String referer;

    /** Timezone offset of the visitor, in minutes from UTC */ 
    public String tzOffset;

    /** Width in pixels of the visitor's browser window */
    public int browserWidth;
    /** Heightin pixels of the visitor's browser window */
    public int browserHeight;
    /** Width in pixels of the visitor's screen */
    public int screenWidth;
    /** Height in pixels of the visitor's screen */
    public int screenHeight;
    /** User-Agent of the visitor's browser */
    public String ua;
    /** Language of the visitor's browser */
    public String browserLanguage;
    
    /** Type of the event (generally "page" or "event") */
    public String type;

    /** Timestamp at which the event was received on the backend */
    public long now = System.currentTimeMillis();

    /** Custom Parameters of the event, encoded as an HTTP query string */
    public String eventParams;

    /**
     * Parse an integer leniently from a multi-valued HTTP parameter
     */
    private int lenientInt(String[] v) {
        if (v == null || v.length < 1) return 0;
        try {
            return Integer.parseInt(v[0]);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * This is called by the TrackerServlet and must not be used in processors
     */
    public void fillEventParams(Map<String, String[]> orig) {
        Map<String, String[]> out = new HashMap<String, String[]>();
        for (Map.Entry<String, String[]> e : orig.entrySet()) {
            /* Ignore params which are handled by the tracker instead of being user params */
            if (e.getKey().equals(PixelServlet.CACHEKILL_PARAM)) {
                continue;
            } else if (e.getKey().equals(PixelServlet.REFERRER_PARAM)) {
                referer = e.getValue().length > 0 ? e.getValue()[0] : null;
            } else if (e.getKey().equals(PixelServlet.TRACKTYPE_PARAM)) {
                type = e.getValue().length > 0 ? e.getValue()[0] : null;
                continue;
            } else if (e.getKey().equals(PixelServlet.BROWSER_WIDTH_PARAM)) {
                browserWidth = lenientInt(e.getValue());
            } else if (e.getKey().equals(PixelServlet.BROWSER_HEIGHT_PARAM)) {
                browserHeight = lenientInt(e.getValue());
            } else if (e.getKey().equals(PixelServlet.SCREEN_WIDTH_PARAM)) {
                screenWidth = lenientInt(e.getValue());
            } else if (e.getKey().equals(PixelServlet.SCREEN_HEIGHT_PARAM)) {
                screenHeight = lenientInt(e.getValue());
            } else if (e.getKey().equals(PixelServlet.TZOFFSET_PARAM)) {
                tzOffset  = e.getValue().length > 0 ? e.getValue()[0] : null;
            } else if (e.getKey().equals(PixelServlet.BROWSER_LANG_PARAM)) {
                browserLanguage = e.getValue().length > 0 ? e.getValue()[0] : null;
            } else if (
                    e.getKey().equals(PixelServlet.SESSION_PARAMS_SET_ARG) ||
                    e.getKey().equals(PixelServlet.SESSION_PARAMS_DEL_ARG) || 
                    e.getKey().equals(PixelServlet.SESSION_PARAMS_CLEAR_ARG) || 
                    e.getKey().equals(PixelServlet.VISITOR_PARAMS_SET_ARG) ||
                    e.getKey().equals(PixelServlet.VISITOR_PARAMS_DEL_ARG) || 
                    e.getKey().equals(PixelServlet.VISITOR_PARAMS_CLEAR_ARG)) { 
                continue; 
            } else {
                out.put(e.getKey(), e.getValue());
            }
        }
        eventParams = Utils.encodeQueryString(out);
    }
}