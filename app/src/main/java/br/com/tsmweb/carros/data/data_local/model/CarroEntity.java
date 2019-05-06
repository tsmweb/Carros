package br.com.tsmweb.carros.data.data_local.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "carro")
public class CarroEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    private long id;

    private String tipo;

    private String nome;

    private String desc;

    @ColumnInfo(name = "url_foto")
    private String urlFoto;

    @ColumnInfo(name = "url_info")
    private String urlInfo;

    @ColumnInfo(name = "url_video")
    private String urlVideo;

    private String latitude;

    private String longitude;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarroEntity)) return false;

        CarroEntity that = (CarroEntity) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
