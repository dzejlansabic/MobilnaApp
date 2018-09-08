package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.autori;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.kategorije;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.siriL;

import java.util.ArrayList;

public class ListeFragment extends Fragment {

    ArrayList<String> autoriF = new ArrayList<String>();
    public static Boolean katAut = false;
    onItemClick oic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.liste_fragment, container, false);
    }

    public void onActivityCreated​(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button dAutori = (Button) getView().findViewById(R.id.dAutori);
        Button dKategorije = (Button) getView().findViewById(R.id.dKategorije);
        final Button dDodaj = (Button) getView().findViewById(R.id.dDodajKategoriju);
        final Button dDodajKnjigu = (Button) getView().findViewById(R.id.dDodajKnjigu);
        final Button dPretraga = (Button) getView().findViewById(R.id.dPretraga);
        final Button dDodajOnline = (Button) getView().findViewById(R.id.dDodajOnline);
        final EditText editTextPretraga = (EditText) getView().findViewById(R.id.tekstPretraga);
        final ListView lv = (ListView) getView().findViewById(R.id.listaKategorija);

        dPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filter.FilterListener listener = new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                        if ( i == 0 ) dDodaj.setEnabled(true);
                        else dDodaj.setEnabled(false);
                    }
                };
                final ArrayAdapter<String> adapterKategorije = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
                adapterKategorije.getFilter().filter(editTextPretraga.getText().toString(), listener);

                if ( adapterKategorije.getCount() == 0 ) {
                    dDodaj.setEnabled(true);
                }
                adapterKategorije.notifyDataSetChanged();
                lv.setAdapter(adapterKategorije);
            }
        });

        dDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayAdapter<String> adapterKategorije = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
                kategorije.add(editTextPretraga.getText().toString());
                BazaOpenHelper baza = new BazaOpenHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
                long id = baza.dodajKategoriju(editTextPretraga.getText().toString());
                Toast.makeText(getActivity(), "Kategorija je dodana", Toast.LENGTH_LONG).show();
                adapterKategorije.notifyDataSetChanged();
                dDodaj.setEnabled(false);
                lv.setAdapter(adapterKategorije);
            }
        });

        dAutori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dDodaj.setVisibility(View.GONE);
                dPretraga.setVisibility(View.GONE);
                editTextPretraga.setVisibility(View.GONE);
                katAut = true;
                final ArrayAdapter<String> adapterAutori = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, autoriF);
                for (Autor a : autori) {
                    if ( a.getImeiPrezime() != null ) {
                        if ( !autoriF.contains(a.getImeiPrezime()) && !a.getImeiPrezime().equals("nema autora") )
                            autoriF.add(a.getImeiPrezime());
                    }
                }
                for (Knjiga k : knjige) {
                    if ( k.getAutori() == null ) {
                        if ( !autoriF.contains(k.getImeAutora()) && !k.getImeAutora().equals("nema autora") )
                            autoriF.add(k.getImeAutora());
                    } else {
                        if ( !autoriF.contains(k.getAutori().get(0).getImeiPrezime()) && !k.getAutori().get(0).getImeiPrezime().equals("nema autora") )
                            autoriF.add(k.getImeAutora());
                    }
                }
                Fragment FR = new KnjigeFragment();
                if ( siriL ) {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF2, FR).commit();
                }
                adapterAutori.notifyDataSetChanged();
                lv.setAdapter(adapterAutori);
            }
        });
        dKategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dDodaj.setVisibility(View.VISIBLE);
                dPretraga.setVisibility(View.VISIBLE);
                editTextPretraga.setVisibility(View.VISIBLE);
                katAut = false;
                Fragment FR = new KnjigeFragment();
                if ( siriL ) {
                    getFragmentManager().beginTransaction().replace(R.id.mjestoF2, FR).commit();
                }
                final ArrayAdapter<String> adapterKategorije = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, kategorije);
                adapterKategorije.notifyDataSetChanged();
                lv.setAdapter(adapterKategorije);
            }
        });
        dDodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                DodavanjeKnjigeFragment lf = new DodavanjeKnjigeFragment();
                if ( siriL ) {
                    FrameLayout f2 = (FrameLayout) getView().findViewById(R.id.mjestoF2);
                    getActivity().setContentView(R.layout.siroko_dodavanje);
                    fm.beginTransaction().replace(R.id.frejm, lf).addToBackStack(null).commit();
                } else
                    fm.beginTransaction().replace(R.id.mjestoF1, lf).commit();
            }
        });
        dDodajOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                FragmentOnline fo = new FragmentOnline();
                if ( siriL ) {
                    FrameLayout f2 = (FrameLayout) getView().findViewById(R.id.mjestoF2);
                    getActivity().setContentView(R.layout.siroko_dodavanje);
                    fm.beginTransaction().replace(R.id.frejm, fo).addToBackStack(null).commit();
                } else
                    fm.beginTransaction().replace(R.id.mjestoF1, fo).commit();
            }
        });
        try {
            oic = (onItemClick) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + "implementiraj oic zbog izuzetka");
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Knjiga> odabrane = new ArrayList<>();
                if ( !katAut ) {
                    final String kategorija = parent.getAdapter().getItem(position).toString();
                    for (Knjiga k : knjige) {
                        if ( k.getKategorija().equals(kategorija) ) odabrane.add(k);
                    }
                    oic.onItemClicked(position, odabrane);
                } else {
                    final String autor = parent.getAdapter().getItem(position).toString();
                    for (Knjiga k : knjige) {
                        if ( k.getImeAutora().equals(autor) )
                            odabrane.add(k);
                    }
                    oic.onItemClicked(position, odabrane);
                }
            }
        });
    }

    public interface onItemClick {
        public void onItemClicked(int pos, ArrayList<Knjiga> odabrane);
    }
}