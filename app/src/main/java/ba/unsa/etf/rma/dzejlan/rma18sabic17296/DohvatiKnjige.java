package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DohvatiKnjige extends AsyncTask<String, Integer, Void> {

    public interface IDohvatiKnjigeDone {

        public void onDohvatiDone(ArrayList<Knjiga> knjige);
    }

    private ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    private IDohvatiKnjigeDone pozivatelj;

    public DohvatiKnjige(IDohvatiKnjigeDone p) {
        pozivatelj = p;
    }

    @Override
    protected Void doInBackground(String... params) {
        String query = null;
        try {
            query = URLEncoder.encode(params[0], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url1 = "https://www.googleapis.com/books/v1/volumes?q=intitle:" + query + "&maxResults=5";
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject jo = new JSONObject(rezultat);
            JSONArray items = jo.getJSONArray("items");

            for (int i = 0; i < 5; i++) {
                JSONObject knjiga = items.getJSONObject(i);
                String naziv = "nema naziva";
                String id = "nema id";
                String opis = "nema opisa";
                String datumObjavljivanja = "nema datuma";
                URL slika = new URL("http://books.google.com/books/content?id=PLtlf3DdFrkC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
                int brojStranica = 0;
                ArrayList<Autor> autori = new ArrayList<>();
                id = knjiga.getString("id");
                JSONObject volumeInfo = knjiga.getJSONObject("volumeInfo");
                naziv = volumeInfo.getString("title");
                try {
                    opis = volumeInfo.getString("description");
                } catch (Exception e) {
                    opis = "nema opisa";
                }
                try {
                    datumObjavljivanja = volumeInfo.getString("publishedDate");
                } catch (Exception e2) {
                    datumObjavljivanja = "nema datuma";
                }
                try {
                    brojStranica = volumeInfo.getInt("pageCount");
                } catch (Exception e3) {
                    brojStranica = 0;
                    e3.printStackTrace();
                }
                try {
                    JSONArray aut = volumeInfo.getJSONArray("authors");
                    for (int k = 0; k < aut.length(); k++) {
                        Autor a = new Autor(aut.get(k).toString(), id);
                        autori.add(a);
                    }
                } catch (Exception e4) {
                    Autor a1 = new Autor("nema autora", "");
                    autori.add(a1);
                }
                try {
                    JSONObject jslika = volumeInfo.getJSONObject("imageLinks");
                    slika = new URL(jslika.getString("thumbnail"));
                } catch (Exception e) {
                    slika = new URL("http://books.google.com/books/content?id=PLtlf3DdFrkC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");
                }
                Knjiga k = new Knjiga(id, naziv, autori, opis, datumObjavljivanja, slika, brojStranica);
                knjige.add(k);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pozivatelj.onDohvatiDone(knjige);
    }
}