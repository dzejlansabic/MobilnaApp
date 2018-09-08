package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.siriL;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.ListeFragment.katAut;

class KnjigaAdapter extends ArrayAdapter<Knjiga> {
    int resource = R.layout.element_liste;

    public KnjigaAdapter(Context context,
                         int _resource,
                         List<Knjiga> items) {
        super(context, _resource, items);
        resource = _resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout newView;
        if ( convertView == null ) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li;
            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }
        final Button dPreporuci = (Button) newView.findViewById(R.id.dPreporuci);
        Knjiga knjiga = getItem(position);
        if ( knjiga != null ) {
            int brojKnjiga = 0;
            for (Knjiga k : knjige) {
                if ( k.getImeAutora() != null ) {
                    if ( knjiga.getImeAutora().equals(k.getImeAutora()) ) brojKnjiga++;
                } else {
                    if ( knjiga.getAutori().get(0).toString().equals(k.getAutori().get(0).toString()) )
                        brojKnjiga++;
                }
            }
            TextView imeAutora = (TextView) newView.findViewById(R.id.eAutor);
            if ( katAut ) {
                dPreporuci.setVisibility(View.GONE);
                if ( knjiga.getImeAutora() == null ) {
                    imeAutora.setText(knjiga.getAutori().get(0).toString() + "   " + brojKnjiga);
                } else imeAutora.setText(knjiga.getImeAutora() + "    " + brojKnjiga);
                if(knjiga.getBrojStranica()!=0) {
                    TextView brojStr = (TextView) newView.findViewById(R.id.eBrojStranica);
                    String brs = String.valueOf(knjiga.getBrojStranica());
                    brojStr.setText("Broj str: " + brs);
                }
                else {
                    TextView brojStr = (TextView) newView.findViewById(R.id.eBrojStranica);
                    brojStr.setText("Nepoznat broj stranica");
                }
                if(knjiga.getDatumObjavljivanja()!=null)  {
                    TextView datum = (TextView) newView.findViewById(R.id.eDatumObjavljivanja);
                    datum.setText("Datum izdavanja " + knjiga.getDatumObjavljivanja());
                }
                else {
                    TextView datum = (TextView) newView.findViewById(R.id.eDatumObjavljivanja);
                    datum.setText("nepoznat datum izdavanja");
                }
                if(knjiga.getOpis()!=null) {
                    TextView opis = (TextView) newView.findViewById(R.id.eOpis);
                    if ( knjiga.getOpis().length() > 231 )
                        opis.setText(knjiga.getOpis().substring(0, 230));
                    else opis.setText("Opis: " + knjiga.getOpis());
                }
                else {
                    TextView opis = (TextView) newView.findViewById(R.id.eOpis);
                    opis.setText("Nema opisa.");
                }
            } else
                imeAutora.setText(knjiga.getImeAutora());
            final TextView nazivKnjige = (TextView) newView.findViewById(R.id.eNaziv);
            if ( !katAut ) {
                dPreporuci.setVisibility(View.VISIBLE);
                    if(knjiga.getBrojStranica()!=0) {
                        TextView brojStr = (TextView) newView.findViewById(R.id.eBrojStranica);
                        String brs = String.valueOf(knjiga.getBrojStranica());
                        brojStr.setText("Broj str: " + brs);
                    }
                    else {
                        TextView brojStr = (TextView) newView.findViewById(R.id.eBrojStranica);
                        brojStr.setText("Nepoznat broj stranica");
                    }
                    if(knjiga.getDatumObjavljivanja()!=null)  {
                        TextView datum = (TextView) newView.findViewById(R.id.eDatumObjavljivanja);
                        datum.setText("Datum izdavanja " + knjiga.getDatumObjavljivanja());
                    }
                    else {
                        TextView datum = (TextView) newView.findViewById(R.id.eDatumObjavljivanja);
                        datum.setText("nepoznat datum izdavanja");
                    }
                    if(knjiga.getOpis()!=null) {
                        TextView opis = (TextView) newView.findViewById(R.id.eOpis);
                        if ( knjiga.getOpis().length() > 231 )
                            opis.setText(knjiga.getOpis().substring(0, 230));
                        else opis.setText("Opis: " + knjiga.getOpis());
                    }
                    else {
                        TextView opis = (TextView) newView.findViewById(R.id.eOpis);
                        opis.setText("Nema opisa.");
                    }
                dPreporuci.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        KategorijeAkt MyKategorijeAkt = (KategorijeAkt) getContext();
                        FragmentManager fm = MyKategorijeAkt.getFragmentManager();

                        FragmentPreporuci fo = new FragmentPreporuci();
                        Bundle arguments = new Bundle();
                        arguments.putString("naziv", nazivKnjige.getText().toString());
                        fo.setArguments(arguments);

                        if ( siriL ) {
                            fm.beginTransaction().replace(R.id.mjestoF2, fo).addToBackStack(null).commit();
                        } else
                            fm.beginTransaction().replace(R.id.mjestoF1, fo).commit();
                    }
                });
            }
            nazivKnjige.setText(knjiga.getNazivKnjige());
            try {
                ImageView slika = (ImageView) newView.findViewById(R.id.eNaslovna);
                if ( knjiga.getSlikaURL() != null ) {
                    Picasso.get().load(knjiga.getSlikaURL().toString()).into(slika);
                } else {
                    if ( knjiga.getSlika() != null )
                        slika.setImageBitmap(knjiga.getSlika());
                }
            } catch (Exception e) {
            }
        }
        return newView;
    }
}