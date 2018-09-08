package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.AUTORSTVO_ID_AUTORA;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.AUTORSTVO_ID_KNJIGE;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.kategorije;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.siriL;

public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, MojResultReceiver.Receiver {

    ArrayList<Knjiga> knjigice = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_online, container, false);
    }

    @Override
    public void onDohvatiDone(ArrayList<Knjiga> knjige) {
        knjigice.addAll(knjige);
        final Spinner sRezultati = (Spinner) getView().findViewById(R.id.sRezultati);
        ArrayList<String> naziviKnjiga = new ArrayList<>();
        for (Knjiga k : knjigice) {
            naziviKnjiga.add(k.getNazivKnjige());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, naziviKnjiga);
        sRezultati.setAdapter(adapter2);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        final Spinner sRezultati = (Spinner) getView().findViewById(R.id.sRezultati);
        switch (resultCode) {
            case 0:
                break;
            case 1:
                ArrayList<Knjiga> m = new ArrayList<Knjiga>();
                m = resultData.getParcelableArrayList("listaKnjiga");
                ArrayList<String> naziviKnjiga = new ArrayList<>();
                for (Knjiga k : m) {
                    naziviKnjiga.add(k.getNazivKnjige());
                    Toast.makeText(getActivity(), k.getNazivKnjige(), Toast.LENGTH_LONG).show();
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, naziviKnjiga);
                sRezultati.setAdapter(adapter2);
                break;
            case 2:
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> knjige) {

        knjigice.addAll(knjige);
        final Spinner sRezultati = (Spinner) getView().findViewById(R.id.sRezultati);
        ArrayList<String> naziviKnjiga = new ArrayList<>();
        for (Knjiga k : knjige) {
            naziviKnjiga.add(k.getNazivKnjige());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, naziviKnjiga);
        sRezultati.setAdapter(adapter2);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Button dPovratak = (Button) getView().findViewById(R.id.dPovratak);
        final Button dDodaj = (Button) getView().findViewById(R.id.dAdd);
        final Button dRun = (Button) getView().findViewById(R.id.dRun);
        final EditText tekstUpit = (EditText) getView().findViewById(R.id.tekstUpit);
        final Spinner sKategorije = (Spinner) getView().findViewById(R.id.sKategorije);
        final Spinner sRezultati = (Spinner) getView().findViewById(R.id.sRezultati);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sKategorije.setAdapter(adapter);

        dDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (Knjiga k : knjigice) {
                    if ( sRezultati.getSelectedItem().toString().equals(k.getNazivKnjige()) ) {
                        Knjiga k1 = new Knjiga(sRezultati.getSelectedItem().toString(), k.getAutori().get(0).getImeiPrezime(), sKategorije.getSelectedItem().toString(), false, null);
                        k1.setBrojStranica(k.getBrojStranica());
                        k1.setDatumObjavljivanja(k.getDatumObjavljivanja());
                        k1.setId(k.getId());
                        k1.setOpis(k.getOpis());
                        k1.setSlikaURL(k.getSlikaURL());
                        k1.setAutori(k.getAutori());
                        Toast.makeText(getActivity(), "Dodana je knjiga", Toast.LENGTH_SHORT).show();
                        knjige.add(k1);

                        BazaOpenHelper baza = new BazaOpenHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
                        long id_k = baza.dodajKnjigu(k1);

                        Autor a = new Autor(k1.getImeAutora(), k1.getNazivKnjige());
                        long id_a = baza.dodajAutora(a);

                        if(id_a==-1 || id_k==-1)
                            Toast.makeText(getActivity(), "Knjiga ili autor nisu dodani", Toast.LENGTH_SHORT).show();


                        ContentValues novo = new ContentValues();
                        novo.put(AUTORSTVO_ID_AUTORA, id_a);
                        novo.put(AUTORSTVO_ID_KNJIGE, id_k);
                        BazaOpenHelper baza2 = new BazaOpenHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
                        SQLiteDatabase db2 = baza2.getWritableDatabase();
                        db2.insert(BazaOpenHelper.AUTORSTVO_TABLE, null, novo);
                    }
                }
            }
        });

        dRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                knjigice.clear();
                String unos = tekstUpit.getText().toString();
                if ( unos.contains("autor:") ) {
                    new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone) FragmentOnline.this).execute(unos.substring(6, unos.length()));
                } else if ( unos.contains(";") ) {
                    String[] unosi;
                    unosi = unos.split(";");
                    for (String i : unosi) {
                        new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(i);
                    }
                } else if ( unos.contains("korisnik:") ) {
                    Intent mojIntent = new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                    MojResultReceiver mojReceiver = new MojResultReceiver(new Handler());
                    mojReceiver.setMojReceiver(FragmentOnline.this);
                    mojIntent.putExtra("id", unos.split(":")[1]);
                    mojIntent.putExtra("receiver", mojReceiver);

                    getActivity().startService(mojIntent);
                } else
                    new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) FragmentOnline.this).execute(tekstUpit.getText().toString());
            }
        });

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( !siriL ) {
                    FragmentManager fm = getFragmentManager();
                    ListeFragment lf = new ListeFragment();
                    fm.beginTransaction().replace(R.id.mjestoF1, lf).commit();
                }
            }
        });

    }

}
