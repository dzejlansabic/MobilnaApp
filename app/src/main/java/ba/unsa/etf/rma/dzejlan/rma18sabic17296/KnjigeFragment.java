package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.zip.Inflater;

import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.siriL;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.ListeFragment.katAut;

public class KnjigeFragment extends Fragment {

    ListeFragment.onItemClick oic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.knjige_fragment , container, false);
    }

    public void onActivityCreated​(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<Knjiga> odabraneKnjige=new ArrayList<Knjiga>();
        if(getArguments()!= null && getArguments().containsKey("knjige"))
            odabraneKnjige=getArguments().getParcelableArrayList("knjige");


            ListView lv = (ListView)   getView().findViewById(R.id.listaKnjiga);
            Button dPovratak=(Button) getView().findViewById(R.id.dPovratak);
            KnjigaAdapter knjigaAdapter = new KnjigaAdapter(getActivity(), R.layout.element_liste, odabraneKnjige);
            lv.setAdapter(knjigaAdapter);

        dPovratak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!siriL) {
                    FragmentManager fm=getFragmentManager();
                    ListeFragment lf=new ListeFragment();
                    fm.beginTransaction().replace(R.id.mjestoF1, lf).commit();
                }
            }
        });
    }
}