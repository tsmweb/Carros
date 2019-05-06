package br.com.tsmweb.carros.ui.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import br.com.tsmweb.carros.BR;

public class CarroBinding extends BaseObservable implements Parcelable {

    private static final long serialVersionUID = 6601006766832473959L;

    private long id;

    private String tipo;

    private String nome;

    private String desc;

    private String urlFoto;

    private String urlInfo;

    private String urlVideo;

    private String latitude;

    private String longitude;

    public boolean selected; // Flag para indicar que o carro está selecionado

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Bindable
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Bindable
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        notifyPropertyChanged(BR.nome);
    }

    @Bindable
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        notifyPropertyChanged(BR.desc);
    }

    @Bindable
    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
        notifyPropertyChanged(BR.urlFoto);
    }

    @Bindable
    public String getUrlInfo() {
        return urlInfo;
    }

    public void setUrlInfo(String urlInfo) {
        this.urlInfo = urlInfo;
        notifyPropertyChanged(BR.urlInfo);
    }

    @Bindable
    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
        notifyPropertyChanged(BR.urlVideo);
    }

    @Bindable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        notifyPropertyChanged(BR.latitude);
    }

    @Bindable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        notifyPropertyChanged(BR.longitude);
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

    public static final Creator<CarroBinding> CREATOR = new Creator<CarroBinding>() {

        @Override
        public CarroBinding createFromParcel(Parcel parcel) {
            CarroBinding c = new CarroBinding();
            c.readFromParcel(parcel);

            return c;
        }

        @Override
        public CarroBinding[] newArray(int size) {
            return new CarroBinding[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarroBinding)) return false;

        CarroBinding that = (CarroBinding) o;

        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }

}
