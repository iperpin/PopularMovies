
package com.example.movies.popularmovies.objects;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieTrailer implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("quicktime")
    @Expose
    private List<Object> quicktime = null;
    @SerializedName("youtube")
    @Expose
    private List<Youtube> youtube = null;
    public final static Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        public MovieTrailer[] newArray(int size) {
            return (new MovieTrailer[size]);
        }

    }
    ;

    protected MovieTrailer(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.quicktime, (Object.class.getClassLoader()));
        in.readList(this.youtube, (Youtube.class.getClassLoader()));
    }

    public MovieTrailer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Object> getQuicktime() {
        return quicktime;
    }

    public void setQuicktime(List<Object> quicktime) {
        this.quicktime = quicktime;
    }

    public List<Youtube> getYoutube() {
        return youtube;
    }

    public void setYoutube(List<Youtube> youtube) {
        this.youtube = youtube;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(quicktime);
        dest.writeList(youtube);
    }

    public int describeContents() {
        return  0;
    }

}
