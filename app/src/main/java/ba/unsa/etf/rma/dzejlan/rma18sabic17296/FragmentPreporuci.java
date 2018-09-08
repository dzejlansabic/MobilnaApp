package ba.unsa.etf.rma.dzejlan.rma18sabic17296;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.provider.ContactsContract;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.siriL;

public class FragmentPreporuci extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preporuci_fragment , container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Spinner sKontakti=(Spinner) getView().findViewById(R.id.sKontakti);
        final ImageView iSlika=(ImageView) getView().findViewById(R.id.iSlika);
        final TextView tNaziv=(TextView) getView().findViewById(R.id.tNaziv);
        final TextView tIme=(TextView) getView().findViewById(R.id.tIme);
        final TextView tDatum=(TextView) getView().findViewById(R.id.tDatum);
        final Button dPosalji=(Button) getView().findViewById(R.id.dPosalji);
        final Button dPovratak=(Button) getView().findViewById(R.id.dPovratakSaPreporuke);

        final  ArrayList<String> imena=new ArrayList<>();
        final ArrayList<String>emailovi=new ArrayList<>();
        Cursor phones = getActivity().getContentResolver().query
                (ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String ime=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String email=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            if(email!=null)
            emailovi.add(email);
            imena.add(ime);
        }
        phones.close();

          final  ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, emailovi);
            sKontakti.setAdapter(adapter);

        String naziv=getArguments().getString("naziv");
        for(Knjiga k : knjige) {
            if(naziv.equals(k.getNazivKnjige())) {
                tNaziv.setText(k.getNazivKnjige());
                tIme.setText(k.getImeAutora());
                tDatum.setText(k.getDatumObjavljivanja());
                if(k.getSlikaURL()!=null)
                Picasso.get().load(k.getSlikaURL().toString()).into(iSlika);
            }
        }
        dPosalji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ime="";
                Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,null,null, null);
                while (phones.moveToNext())
                {
                    if(sKontakti.getSelectedItem().toString().equals(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))))
                        ime=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                }
                phones.close();

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",sKontakti.getSelectedItem().toString(),null));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Zdravo " + ime.split(" ")[0] +", \nprocitaj knjigu "+ tNaziv.getText().toString() + " od " + tIme.getText().toString()+"!");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
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