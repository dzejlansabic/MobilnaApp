package ba.unsa.etf.rma.dzejlan.rma18sabic17296;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.autori;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;

public class BazaOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "mojaBazaPodataka.db";
    public static final String KATEGORIJA_TABLE = "Kategorija";
    public static final int DATABASE_VERSION = 1;
    public static final String ID_KATEGORIJE ="_id";
    public static final String NAZIV_KATEGORIJE ="naziv";

    public static final String KNJIGA_TABLE = "Knjiga";
    public static final String ID_KNJIGE ="_id";
    public static final String NAZIV_KNJIGE ="naziv";
    public static final String OPIS_KNJIGE ="opis";
    public static final String DATUM_OBJAVLJIVANJA_KNJIGE = "datumObjavljivanja";
    public static final String BR_STRANICA_KNJIGE = "brojStranica";
    public static final String WEB_SERVIS_KNJIGE = "idWebServis";
    public static final String KNJIGA_ID_KATEGORIJE = "idkategorije";
    public static final String SLIKA_KNJIGE="slika";
    public static final String PREGLEDANA_KNJIGA="pregledana";

    public static final String AUTOR_TABLE = "Autor";
    public static final String ID_AUTORA ="_id";
    public static final String IME_AUTORA ="ime";

    public static final String AUTORSTVO_TABLE = "Autorstvo";
    public static final String ID_AUTORSTVO ="_id";
    public static final String AUTORSTVO_ID_AUTORA ="idautora";
    public static final String AUTORSTVO_ID_KNJIGE ="idknjige";

    private static final String DATABASE_CREATE = "create table " +
            KATEGORIJA_TABLE + " (" + ID_KATEGORIJE +
            " integer primary key autoincrement, " +
                NAZIV_KATEGORIJE + " text not null " +");";

    private  static  final String DATEBASE_CREATE2 =
            "create table "+ KNJIGA_TABLE + " (" + ID_KNJIGE +
                    " integer primary key autoincrement, " +
                    NAZIV_KNJIGE + " text not null, " + OPIS_KNJIGE + " text, " +
                    DATUM_OBJAVLJIVANJA_KNJIGE + " text, "+
                    BR_STRANICA_KNJIGE+" integer, " +
                    WEB_SERVIS_KNJIGE + " text, "+
                    KNJIGA_ID_KATEGORIJE + " integer not null, " +
                    SLIKA_KNJIGE + " text, " +
                    PREGLEDANA_KNJIGA + " integer " + ");";

    private static final String DATEBASE_CREATE3 = "create table "+ AUTOR_TABLE + " (" + ID_AUTORA +
            " integer primary key autoincrement, " +
            IME_AUTORA + " text not null " + ");";

    private static final String DATEBASE_CREATE4="create table "+ AUTORSTVO_TABLE + " (" + ID_AUTORSTVO +
            " integer primary key autoincrement, " +
            AUTORSTVO_ID_AUTORA + " integer not null, " +AUTORSTVO_ID_KNJIGE + " integer not null " +");";

    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(DATEBASE_CREATE2);
        db.execSQL(DATEBASE_CREATE3);
        db.execSQL(DATEBASE_CREATE4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + KATEGORIJA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + KNJIGA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AUTOR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AUTORSTVO_TABLE);
        onCreate(db);
    }

    public long dodajKategoriju(String naziv) {

        ContentValues nova=new ContentValues();
        nova.put(NAZIV_KATEGORIJE, naziv);
        SQLiteDatabase db=this.getWritableDatabase();
        long maxID=db.insert(BazaOpenHelper.KATEGORIJA_TABLE,null,nova);

        db.close();
        return maxID;
    }

    public long dajIdKategorije (String nazivKategorije) {

        long idKategorije=-1;
        String[] koloneRezultati=new String[] {ID_KATEGORIJE, NAZIV_KATEGORIJE};
        String selection=(NAZIV_KATEGORIJE + "=?");
        String[] selectionArgs={nazivKategorije};
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query(BazaOpenHelper.KATEGORIJA_TABLE, koloneRezultati,selection,selectionArgs,null,null,null);
        int INDEX_ID_KATEGORIJE=cursor.getColumnIndex(ID_KATEGORIJE);
        if(cursor.moveToFirst()) {
            idKategorije = cursor.getInt(INDEX_ID_KATEGORIJE);
        }
        db.close();
        cursor.close();
        return idKategorije;
    }

    public long dodajKnjigu(Knjiga knjiga){

            ContentValues nova = new ContentValues();
            nova.put(NAZIV_KNJIGE, knjiga.getNazivKnjige());
            nova.put(DATUM_OBJAVLJIVANJA_KNJIGE, knjiga.getDatumObjavljivanja());
            nova.put(OPIS_KNJIGE,knjiga.getOpis());
            nova.put(BR_STRANICA_KNJIGE, knjiga.getBrojStranica());
            nova.put(WEB_SERVIS_KNJIGE, knjiga.getId());
            nova.put(KNJIGA_ID_KATEGORIJE, dajIdKategorije(knjiga.getKategorija()));
            nova.put(SLIKA_KNJIGE, knjiga.getSlikaURL().toString());
            nova.put(PREGLEDANA_KNJIGA, 0);
            SQLiteDatabase db = this.getWritableDatabase();
            long maxID = db.insert(BazaOpenHelper.KNJIGA_TABLE, null, nova);

        db.close();
        return maxID;
    }

    public long dodajAutora(Autor a) {
        long maxID=-1;
        if(!a.getImeiPrezime().contains("nema autora")) {
            String[] koloneRezultati = new String[]{ID_AUTORA, IME_AUTORA};
            String selection = (IME_AUTORA + "=?");
            String[] selectionArgs = {a.getImeiPrezime()};
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.query(BazaOpenHelper.AUTOR_TABLE, koloneRezultati, selection, selectionArgs, null, null, null);
            int INDEX_ID_AUTORA = cursor.getColumnIndex(ID_AUTORA);
            if ( !cursor.moveToFirst() ) {
                Autor a1 = new Autor(a.getImeiPrezime(), a.getKnjige());
                autori.add(a1);
                ContentValues novi = new ContentValues();
                novi.put(IME_AUTORA, a.getImeiPrezime());
                SQLiteDatabase db2 = this.getWritableDatabase();
                maxID=db2.insert(BazaOpenHelper.AUTOR_TABLE, null, novi);
            } else {
                maxID = cursor.getInt(INDEX_ID_AUTORA);
                for (Autor aut : autori) {
                    if ( aut.getImeiPrezime().equals(a.getImeiPrezime()) ) {
                        aut.dodajKnjigu(a.getKnjige().get(0));
                    }
                }
            }
            cursor.close();
            db.close();
        }
        return maxID;
    }
    public String dajNazivKnjige(int idKnjige) {

        String naziv="nema naziva";
        String[] kolone_rezultat = new String[]{ID_KNJIGE, NAZIV_KNJIGE};
        SQLiteDatabase db = this.getWritableDatabase();
        String where=ID_KNJIGE+"="+idKnjige;
        Cursor cursor = db.query(BazaOpenHelper.KNJIGA_TABLE, kolone_rezultat, where, null, null, null, null);
        int NAZIV =  cursor.getColumnIndexOrThrow(NAZIV_KNJIGE);
        if(cursor.moveToFirst())
            naziv=cursor.getString(NAZIV);
        db.close();
        cursor.close();
        return naziv;
    }

    public ArrayList<String> knjigeAutora(int id) {

        ArrayList<String> knjige=new ArrayList<>();
        String[] kolone_rezultat = new String[]{AUTORSTVO_ID_KNJIGE, AUTORSTVO_ID_AUTORA};
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(BazaOpenHelper.AUTORSTVO_TABLE, kolone_rezultat, null, null, null, null, null);
        int AUTOR =  cursor.getColumnIndexOrThrow(AUTORSTVO_ID_AUTORA);
        int KNJIGA=cursor.getColumnIndexOrThrow(AUTORSTVO_ID_KNJIGE);
        while(cursor.moveToNext()) {
            if(cursor.getInt(AUTOR)==id)
                knjige.add(dajNazivKnjige(cursor.getInt(KNJIGA)));
        }
        db.close();
        cursor.close();
        return knjige;
    }

    public ArrayList<Autor> dajListuAutora(){

        String[] kolone_rezultat = new String[]{ID_AUTORA, IME_AUTORA};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Autor>  aut= new ArrayList<>();
        Cursor cursor = db.query(BazaOpenHelper.AUTOR_TABLE, kolone_rezultat, null, null, null, null, null);
        int IME =  cursor.getColumnIndexOrThrow(IME_AUTORA);
        int ID=cursor.getColumnIndexOrThrow(ID_AUTORA);
        while(cursor.moveToNext()){
            String ime=cursor.getString(IME);
            ArrayList<String> knjige=knjigeAutora(cursor.getInt(ID));
            Autor a=new Autor(ime,knjige);
            aut.add(a);
        }
        db.close();
        cursor.close();
        return aut;
    }

    public ArrayList<Knjiga> dajListuKnjiga(){

        String[] kolone_rezultat = new String[]{ID_KNJIGE, NAZIV_KNJIGE, OPIS_KNJIGE, DATUM_OBJAVLJIVANJA_KNJIGE,
                                                BR_STRANICA_KNJIGE, WEB_SERVIS_KNJIGE, KNJIGA_ID_KATEGORIJE, SLIKA_KNJIGE,
                                                 PREGLEDANA_KNJIGA};
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Knjiga> knjige= new ArrayList<>();
        Cursor cursor = db.query(BazaOpenHelper.KNJIGA_TABLE, kolone_rezultat, null, null, null, null, null);
        int ID=cursor.getColumnIndexOrThrow(ID_KNJIGE);
        int NAZIV =  cursor.getColumnIndexOrThrow(NAZIV_KNJIGE);
        int OPIS =  cursor.getColumnIndexOrThrow(OPIS_KNJIGE);
        int DATUM =  cursor.getColumnIndexOrThrow(DATUM_OBJAVLJIVANJA_KNJIGE);
        int STRANICE =  cursor.getColumnIndexOrThrow(BR_STRANICA_KNJIGE);
        int KATEGORIJA =  cursor.getColumnIndexOrThrow(KNJIGA_ID_KATEGORIJE);
        int SLIKA =  cursor.getColumnIndexOrThrow(SLIKA_KNJIGE);
        int PREGLEDANA =  cursor.getColumnIndexOrThrow(PREGLEDANA_KNJIGA);
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            String id=cursor.getString(ID);
            String naziv=cursor.getString(NAZIV);
            String opis=cursor.getString(OPIS);
            String datum=cursor.getString(DATUM);
            int stranice=cursor.getInt(STRANICE);
            int kategorija=cursor.getInt(KATEGORIJA);
            String slika=cursor.getString(SLIKA);
            int pregledana=cursor.getInt(PREGLEDANA);
            ArrayList<Autor> autori=dajListuAutora();
            ArrayList<Autor> konacniAutori=new ArrayList<>();
            for(Autor a:autori) {
                if(a.getKnjige().contains(naziv))
                    konacniAutori.add(a);
            }
            URL b= null;
            try {
                b = new URL(slika);
            } catch (MalformedURLException e) {e.printStackTrace();}

            Knjiga k= new Knjiga(id,naziv,konacniAutori,opis,datum,b,stranice);
            if(konacniAutori.get(0).getImeiPrezime()!=null)
            k.setImeAutora(konacniAutori.get(0).getImeiPrezime());
            else k.setImeAutora("nema imena");
            k.setKategorija(dajImeKategorije(kategorija));
            boolean pt=false;
            if(pregledana==0)
                pt=false;
            else  pt=true;

            k.setObojena(pt);
            knjige.add(k);
        }
        db.close();
        cursor.close();
        return knjige;
    }

    public String dajImeKategorije (long idKat) {

        String imeKat="";
        String[] koloneRezultati=new String[] {ID_KATEGORIJE, NAZIV_KATEGORIJE};
        String where=ID_KATEGORIJE+"="+idKat;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.query(BazaOpenHelper.KATEGORIJA_TABLE, koloneRezultati,where,null,null,null,null);
        int NAZIV=cursor.getColumnIndex(NAZIV_KATEGORIJE);
        if(cursor.moveToFirst()) {
            imeKat = cursor.getString(NAZIV);
        }
        db.close();
        cursor.close();
        return imeKat;
    }
}