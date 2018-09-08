package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class KnjigePoznanika extends IntentService {
    public int STATUS_START = 0;
    public int STATUS_FINISH = 1;
    public int STATUS_ERROR = 2;
    ArrayList<Knjiga> knjige = new ArrayList<>();

    public KnjigePoznanika() {
        super(null);
    }

    public KnjigePoznanika(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        Bundle bundle = new Bundle();
        receiver.send(STATUS_START, Bundle.EMPTY);

        String id = intent.getStringExtra("id");
        try {
            id = URLEncoder.encode(id, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url1 = "https://www.googleapis.com/books/v1/users/" + id + "/bookshelves";
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String rezultat = convertStreamToString(in);

            JSONObject rez = new JSONObject(rezultat);
            JSONArray shelves = rez.getJSONArray("items");
            for (int i = 0; i < shelves.length(); i++) {
                JSONObject bookshelf = shelves.getJSONObject(i);
                String idShelf = bookshelf.getString("id");

                try {
                    idShelf = URLEncoder.encode(idShelf, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String link = "https://www.googleapis.com/books/v1/users/" + id + "/bookshelves/" + idShelf + "/volumes";

                URL novi = new URL(link);
                HttpURLConnection urlconn = (HttpURLConnection) novi.openConnection();
                InputStream in1 = new BufferedInputStream((urlconn.getInputStream()));

                String rezultat1 = convertStreamToString(in1);

                JSONObject jo = new JSONObject(rezultat1);

                JSONArray items = jo.getJSONArray("items");
                for (int j = 0; j < items.length(); j++) {
                    JSONObject book = items.getJSONObject(j);

                    String idd = null;

                    try {
                        idd = book.getString("id");
                    } catch (JSONException e) {
                        idd = "No id";
                    }

                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    String nazivKnjige = null;
                    try {
                        nazivKnjige = volumeInfo.getString("title");
                    } catch (JSONException e) {
                        nazivKnjige = "No  title";
                    }
                    ArrayList<Autor> autori = new ArrayList<>();

                    JSONArray authors = null;
                    try {
                        authors = volumeInfo.getJSONArray("authors");
                        for (int k = 0; k < authors.length(); k++) {
                            autori.add(new Autor(authors.getString(k), idd));
                        }
                    } catch (JSONException e) {
                        autori.add(new Autor("No authors", idd));
                    }
                    String opis = "Nema opisa";
                    try {
                        opis = volumeInfo.getString("description");
                    } catch (JSONException e) {
                        opis = "No     description";
                    }
                    String datumObjavljivanja = "Bez datuma";
                    try {
                        datumObjavljivanja = volumeInfo.getString("publishedDate");
                    } catch (JSONException e) {
                        datumObjavljivanja = "No value publishedDate";
                    }
                    JSONObject image = null;
                    String urlSlika = null;

                    try {
                        image = volumeInfo.getJSONObject("imageLinks");
                        urlSlika = image.getString("smallThumbnail");
                    } catch (JSONException e) {
                        urlSlika = "http://www.ipwatchdog.com/wp-content/uploads/2017/09/no-value.jpg";
                    }
                    URL slika = new URL(urlSlika);
                    int brojStranica = 0;
                    try {
                        brojStranica = volumeInfo.getInt("pageCount");
                    } catch (JSONException e) {
                        brojStranica = -1;
                    }
                    knjige.add(new Knjiga(idd, nazivKnjige, autori, opis, datumObjavljivanja, slika, brojStranica));
                }
            }
        } catch (IOException | JSONException e) {
            receiver.send(STATUS_ERROR, Bundle.EMPTY);
        }
        bundle.putParcelableArrayList("listaKnjiga", knjige);
        receiver.send(STATUS_FINISH, bundle);
    }

    public String convertStreamToString(InputStream is) {
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
}