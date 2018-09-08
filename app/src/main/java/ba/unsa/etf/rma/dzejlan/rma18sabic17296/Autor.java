package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import java.util.ArrayList;

public class Autor {
    private String imeiPrezime;
    private ArrayList<String> knjige;

    public Autor(String imeiPrezime, ArrayList<String> knjige) {
        this.imeiPrezime = imeiPrezime;
        this.knjige = knjige;
    }

    public Autor(String imeiPrezime,String knjiga) {
        this.imeiPrezime = imeiPrezime;
        knjige=new ArrayList<>();
        this.knjige.add(knjiga);
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public void dodajKnjigu (String id) {
        if(!knjige.contains(id)) {
            knjige.add(id);
        }
    }
}
