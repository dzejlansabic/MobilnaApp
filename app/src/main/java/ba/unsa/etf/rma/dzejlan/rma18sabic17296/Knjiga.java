package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.ArrayList;

public class Knjiga implements Parcelable {
    private String id;
    private ArrayList<Autor> autori;
    private String opis;
    private String datumObjavljivanja;
    private URL slikaURL;
    private int brojStranica;
    private String nazivKnjige;
    private String imeAutora;
    private String kategorija;
    private boolean obojena;
    private Bitmap slika;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlikaURL() {
        return slikaURL;
    }

    public void setSlikaURL(URL slikaURL) {
        this.slikaURL = slikaURL;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public Knjiga(String nazivKnjige, String imeAutora, String kategorija, boolean obojena, Bitmap slika) {
        this.nazivKnjige = nazivKnjige;
        this.imeAutora = imeAutora;
        this.kategorija = kategorija;
        this.obojena = obojena;
        this.slika = slika;
    }

    public Knjiga(String id, String nazivKnjige, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slikaURL, int brojStranica) {
        this.id = id;
        this.nazivKnjige = nazivKnjige;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slikaURL = slikaURL;
        this.brojStranica = brojStranica;
    }

    public Bitmap getSlika() {
        return slika;
    }

    public void setSlika(Bitmap slika1) {
        this.slika = slika1;
    }

    public String getNazivKnjige() {
        return nazivKnjige;
    }

    public void setNazivKnjige(String nazivKnjige) {
        this.nazivKnjige = nazivKnjige;
    }

    public String getImeAutora() {
        return imeAutora;
    }

    public void setImeAutora(String imeAutora) {
        this.imeAutora = imeAutora;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public boolean isObojena() {
        return obojena;
    }

    public void setObojena(boolean obojena) {
        this.obojena = obojena;
    }

    protected Knjiga(Parcel in) {
        id = in.readString();
        imeAutora = in.readString();
        nazivKnjige = in.readString();
        kategorija = in.readString();
        autori = in.readArrayList(Autor.class.getClassLoader());
        opis = in.readString();
        datumObjavljivanja = in.readString();
        brojStranica = in.readInt();
    }

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel parcel) {
            return new Knjiga(parcel);
        }

        @Override
        public Knjiga[] newArray(int i) {
            return new Knjiga[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nazivKnjige);
        parcel.writeString(imeAutora);
        parcel.writeString(kategorija);
        parcel.writeString(id);
        parcel.writeString(opis);
        parcel.writeString(datumObjavljivanja);
        parcel.writeInt(brojStranica);
    }
}