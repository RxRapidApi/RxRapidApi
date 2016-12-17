package com.gatebuzz.rapidapi.rx.example.slack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class SlackFileResponse {
    public boolean ok;
    public SlackFile file;

    public static SlackFileResponse fromJson(String json) {
        return new Gson().fromJson(json, SlackFileResponse.class);
    }

    public static class SlackFile {
        private String id;
        private long created;
        private long timestamp;
        private String name;
        private String title;
        @SerializedName("mimetype")
        private String mimeType;
        @SerializedName("filetype")
        private String fileType;
        @SerializedName("pretty_type")
        private String prettyType;
        private String user;
        private boolean editable;
        private long size;
        private String mode;
        @SerializedName("is_external")
        private boolean isExternal;
        @SerializedName("external_type")
        private String externalType;
        @SerializedName("is_public")
        private boolean isPublic;
        @SerializedName("public_url_shared")
        private boolean isPublicUrlShared;
        @SerializedName("display_as_bot")
        private boolean isDisplayAsBot;
        private String username;
        @SerializedName("url_private")
        private String urlPrivate;
        @SerializedName("url_private_download")
        private String urlPrivateDownload;
        private String permalink;
        @SerializedName("permalink_public")
        private String permalinkPublic;
        @SerializedName("edit_link")
        private String editLink;

        public String getId() {
            return id;
        }

        public long getCreated() {
            return created;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getFileType() {
            return fileType;
        }

        public String getPrettyType() {
            return prettyType;
        }

        public String getUser() {
            return user;
        }

        public boolean isEditable() {
            return editable;
        }

        public long getSize() {
            return size;
        }

        public String getMode() {
            return mode;
        }

        public boolean isExternal() {
            return isExternal;
        }

        public String getExternalType() {
            return externalType;
        }

        public boolean isPublic() {
            return isPublic;
        }

        public boolean isPublicUrlShared() {
            return isPublicUrlShared;
        }

        public boolean isDisplayAsBot() {
            return isDisplayAsBot;
        }

        public String getUsername() {
            return username;
        }

        public String getUrlPrivate() {
            return urlPrivate;
        }

        public String getUrlPrivateDownload() {
            return urlPrivateDownload;
        }

        public String getPermalink() {
            return permalink;
        }

        public String getPermalinkPublic() {
            return permalinkPublic;
        }

        @Override
        public String toString() {
            return new GsonBuilder().setPrettyPrinting().create().toJson(this);
        }
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
