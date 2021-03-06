package vip.frendy.ytplayer.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by frendy on 2017/11/14.
 */

public class Playlist implements Serializable {

    /**
     * kind : youtube#playlistItemListResponse
     * etag : "ld9biNPKjAjgjV7EZ4EKeEGrhao/Bvp2Ez9WcNNcoB4ShrZRGDqKDjM"
     * nextPageToken : CDIQAA
     * pageInfo : {"totalResults":200,"resultsPerPage":50}
     * items : [PlaylistItems]
     */

    private String kind;
    private String etag;
    private String nextPageToken = "";
    private PageInfoBean pageInfo;
    private ArrayList<PlaylistItems> items;
    private int type = 0;

    /**
     * error : {"errors":[{"domain":"youtube.playlistItem","reason":"playlistNotFound","message":"The playlist identified with the requests <code>playlistId<\/code> parameter cannot be found.","locationType":"parameter","location":"playlistId"}],"code":404,"message":"The playlist identified with the requests <code>playlistId<\/code> parameter cannot be found."}
     */
    private ErrorBean error;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ArrayList<PlaylistItems> getItems() {
        return items;
    }

    public void setItems(ArrayList<PlaylistItems> items) {
        this.items = items;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public static class PageInfoBean implements Serializable {
        /**
         * totalResults : 200
         * resultsPerPage : 50
         */

        private int totalResults;
        private int resultsPerPage;

        public int getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }

        public int getResultsPerPage() {
            return resultsPerPage;
        }

        public void setResultsPerPage(int resultsPerPage) {
            this.resultsPerPage = resultsPerPage;
        }
    }


    public static class ErrorBean {
        /**
         * errors : [{"domain":"youtube.playlistItem","reason":"playlistNotFound","message":"The playlist identified with the requests <code>playlistId<\/code> parameter cannot be found.","locationType":"parameter","location":"playlistId"}]
         * code : 404
         * message : The playlist identified with the requests <code>playlistId</code> parameter cannot be found.
         */
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
