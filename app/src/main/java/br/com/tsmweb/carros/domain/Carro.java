package br.com.tsmweb.carros.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "carro")
public class Carro implements Parcelable {

    private static final long serialVersionUID = 6601006766832473959L;

    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private long id;

    private String tipo;

    private String nome;

    private String desc;

    @SerializedName("url_foto")
    @ColumnInfo(name = "url_foto")
    private String urlFoto;

    @SerializedName("url_info")
    @ColumnInfo(name = "url_info")
    private String urlInfo;

    @SerializedName("url_video")
    @ColumnInfo(name = "url_video")
    private String urlVideo;

    private String latitude;

    private String longitude;

    @Expose
    @Ignore
    public boolean selected; // Flag para indicar que o carro está selecionado

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(String urlInfo) {
        this.urlInfo = urlInfo;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Carro{" +
                "nome='" + nome + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Escreve os dados para serem serializados
        dest.writeLong(id);
        dest.writeString(tipo);
        dest.writeString(nome);
        dest.writeString(desc);
        dest.writeString(urlFoto);
        dest.writeString(urlInfo);
        dest.writeString(urlVideo);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }

    public void readFromParcel(Parcel parcel) {
        // Lê os dados na mesma ordem em que foram escritos
        id = parcel.readLong();
        tipo = parcel.readString();
        nome = parcel.readString();
        desc = parcel.readString();
        urlFoto = parcel.readString();
        urlInfo = parcel.readString();
        urlVideo = parcel.readString();
        latitude = parcel.readString();
        longitude = parcel.readString();
    }

    public static final Parcelable.Creator<Carro> CREATOR = new Parcelable.Creator<Carro>() {

        @Override
        public Carro createFromParcel(Parcel parcel) {
            Carro c = new Carro();
            c.readFromParcel(parcel);

            return c;
        }

        @Override
        public Carro[] newArray(int size) {
            return new Carro[size];
        }
    };
}
