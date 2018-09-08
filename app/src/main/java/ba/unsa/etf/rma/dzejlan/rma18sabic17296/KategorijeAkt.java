package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.app.FragmentManager;
import android.widget.FrameLayout;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import android.os.Bundle;

import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.ID_KATEGORIJE;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.NAZIV_KATEGORIJE;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.onItemClick {

    public static ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    public static ArrayList<String> kategorije = new ArrayList<String>();
    public static ArrayList<Autor> autori = new ArrayList<>();
    public static Boolean siriL;
    public ArrayList<Knjiga> pomocna = knjige;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_kategorije_akt);
        if ( kategorije.isEmpty() ) {
            BazaOpenHelper bazaOH = new BazaOpenHelper(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
            SQLiteDatabase db = bazaOH.getWritableDatabase();
            String[] kolone = new String[]{ID_KATEGORIJE, NAZIV_KATEGORIJE};
            Cursor cursor = db.query(BazaOpenHelper.KATEGORIJA_TABLE, kolone, null, null, null, null, null);
            int INDEX_NAZIV = cursor.getColumnIndexOrThrow(NAZIV_KATEGORIJE);
            while (cursor.moveToNext()) {
                kategorije.add(cursor.getString(INDEX_NAZIV));
            }
            bazaOH.close();
        }
        BazaOpenHelper bazaOH2 = new BazaOpenHelper(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        autori = bazaOH2.dajListuAutora();
        knjige = bazaOH2.dajListuKnjiga();
        bazaOH2.close();

        siriL = false;

        FragmentManager fm = getFragmentManager();
        FrameLayout fknjige = (FrameLayout) findViewById(R.id.mjestoF2);

        if ( fknjige != null ) {
            siriL = true;
            KnjigeFragment kf;
            kf = (KnjigeFragment) fm.findFragmentById(R.id.mjestoF2);
            Bundle arguments = new Bundle();

            if ( kf == null ) {
                kf = new KnjigeFragment();
                arguments.putParcelableArrayList("knjige", pomocna);
                kf.setArguments(arguments);
                fm.beginTransaction().replace(R.id.mjestoF2, kf).commit();
            }
        }
        ListeFragment lf = (ListeFragment) fm.findFragmentByTag("Lista");

        if ( lf == null ) {
            lf = new ListeFragment();
            fm.beginTransaction().replace(R.id.mjestoF1, lf, "Lista").commit();
        } else {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onItemClicked(int pos, ArrayList<Knjiga> odabrane) {
        pomocna = odabrane;
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList("knjige", odabrane);
        KnjigeFragment kf = new KnjigeFragment();
        kf.setArguments(arguments);

        if ( siriL ) {
            getFragmentManager().beginTransaction().replace(R.id.mjestoF2, kf).commit();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.mjestoF1, kf).addToBackStack(null).commit();
        }
    }
}