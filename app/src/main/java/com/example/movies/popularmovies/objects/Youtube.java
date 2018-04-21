
package com.example.movies.popularmovies.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Youtube implements Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("type")
    @Expose
    private String type;
    public final static Creator<Youtube> CREATOR = new Creator<Youtube>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Youtube createFromParcel(Parcel in) {
            return new Youtube(in);
        }

        public Youtube[] newArray(int size) {
            return (new Youtube[size]);
        }

    }
    ;

    protected Youtube(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.size = ((String) in.readValue((String.class.getClassLoader())));
        this.source = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Youtube() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(size);
        dest.writeValue(source);
        dest.writeValue(type);
    }

    public int describeContents() {
        return  0;
    }

}
