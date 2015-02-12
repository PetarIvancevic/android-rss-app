package net.ivancevic.petar.rssfeedapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by Petar on 20.12.2014..
 */
public class feedItem implements Parcelable {
    private String title;
    private String description;
    private String creator; //dc:creator
    private String pubDate;
    private int numComments; //slash:comments

    public feedItem(String title, String description, String creator, String pubDate, int numComments) {
        this.title = title;
        this.description = Html.fromHtml(description.replaceAll("<img.+/(img)*>", "")).toString();
        this.creator = creator;
        //napravit cu si svoj string date bez nastavka
        String[] taDA = pubDate.split(" ");
        StringBuilder newDate = new StringBuilder();
        newDate.append(taDA[0]+" ");
        newDate.append(taDA[2]+" ");
        newDate.append(taDA[1]+" ");
        newDate.append(taDA[3]+" ");
        newDate.append(taDA[4]);
        this.pubDate = newDate.toString();
        this.numComments = numComments;
    }

    public feedItem(feedItem copy) {
        this.title = copy.title;
        this.description = copy.description;
        this.creator = copy.creator;
        this.pubDate = copy.pubDate;
        this.numComments = copy.numComments;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreator() {
        return creator;
    }

    public String getpubDate() {
        return pubDate;
    }

    public int getNumComments() {
        return numComments;
    }

    public String getObjData() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(this.title+"\n");
        strBuilder.append(this.description+"\n");
        strBuilder.append(this.creator+"\n");
        strBuilder.append(this.pubDate+"\n");
        strBuilder.append(this.numComments+"\n");
        return strBuilder.toString();
    }

    //Parcel constructor
    public feedItem(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.creator = in.readString();
        this.pubDate = in.readString();
        this.numComments = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.creator);
        dest.writeString(this.pubDate);
        dest.writeInt(this.numComments);
    }

    public static final Parcelable.Creator<feedItem> CREATOR = new Parcelable.Creator<feedItem>() {

        @Override
        public feedItem createFromParcel(Parcel source) {
            return new feedItem(source);  //using parcelable constructor
        }

        @Override
        public feedItem[] newArray(int size) {
            return new feedItem[size];
        }
    };

}
