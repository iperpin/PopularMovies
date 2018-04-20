
package com.example.movies.popularmovies.objects;

import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieTrailer implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<MovieTrailerResult> results = null;
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
        in.readList(this.results, (MovieTrailerResult.class.getClassLoader()));
    }

    public MovieTrailer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieTrailerResult> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailerResult> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeList(results);
    }

    public int describeContents() {
        return  0;
    }

    @Override
    public String toString() {
        return "MovieTrailer{" +
                "id=" + id +
                ", results=" + results +
                '}';
    }
}
