package ba.unsa.etf.rma.dzejlan.rma18sabic17296;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.AUTORSTVO_ID_AUTORA;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.AUTORSTVO_ID_KNJIGE;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.kategorije;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.siriL;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DodavanjeKnjigeFragment extends Fragment {
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(uri,"r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate (R.layout.dodavanje_knjige_fragment ,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Button dPonisti=(Button) getView().findViewById(R.id.dPonisti);
        final Button dUpisi=(Button) getView().findViewById(R.id.dUpisiKnjigu);
        final Button dNadji=(Button) getView().findViewById(R.id.dNadjiSliku);
        final EditText imeAutora=(EditText) getView().findViewById(R.id.imeAutora);
        final EditText nazivKnjige=(EditText) getView().findViewById(R.id.nazivKnjige);
        final Spinner sKategorije = (Spinner) getView().findViewById(R.id.sKategorijaKnjige);
        final ImageView slikaKnjige=(ImageView) getView().findViewById(R.id.naslovnaStr) ;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sKategorije.setAdapter(adapter);

        super.onActivityCreated(savedInstanceState) ;
        dUpisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slikaKnjige.setDrawingCacheEnabled(true);
                slikaKnjige.buildDrawingCache();
                Bitmap bm=Bitmap.createBitmap(slikaKnjige.getDrawingCache());
                Knjiga k=new Knjiga(nazivKnjige.getText().toString(),imeAutora.getText().toString(),sKategorije.getSelectedItem().toString(),false,bm);
                k.setBrojStranica(0);
                k.setDatumObjavljivanja("nepoznat datum izdavanja");
                k.setId("nema id");
                k.setOpis("nema opisa");
                try {
                    k.setSlikaURL(new URL("http://books.google.com/books/content?id=PLtlf3DdFrkC&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                Autor a=new Autor(imeAutora.getText().toString(),nazivKnjige.getText().toString());
                ArrayList<Autor> atr=new ArrayList<>();
                atr.add(a);
                k.setAutori(atr);
                knjige.add(k);
                Toast.makeText(getActivity(), "Dodana je knjiga", Toast.LENGTH_SHORT).show();

                BazaOpenHelper bazaOH=new BazaOpenHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
                SQLiteDatabase db=bazaOH.getWritableDatabase();
                long id_k= bazaOH.dodajKnjigu(k);

                Autor a1 = new Autor(imeAutora.getText().toString(),nazivKnjige.getText().toString());
                long id_a=bazaOH.dodajAutora(a1);
                if(id_a==-1 || id_k==-1)
                    Toast.makeText(getActivity(), "Knjiga ili autor nisu dodani", Toast.LENGTH_SHORT).show();
                ContentValues novo=new ContentValues();
                novo.put(AUTORSTVO_ID_AUTORA, id_a);
                novo.put(AUTORSTVO_ID_KNJIGE, id_k);
                BazaOpenHelper baza=new BazaOpenHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
                SQLiteDatabase db2=baza.getWritableDatabase();
                db2.insert(BazaOpenHelper.AUTORSTVO_TABLE,null,novo);
            }
        });

        dNadji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Izaberite sliku"),1);
            }
        });
        dPonisti.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            TextView textLokacijaUri=new TextView(getActivity());
            if (resultCode == DodavanjeKnjigeAkt.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {
                        Uri lokacijaUri=data.getData();
                        textLokacijaUri.setText(lokacijaUri.toString());
                        Bitmap bitmap;
                        ImageView naslovna=(ImageView) getView().findViewById(R.id.naslovnaStr);
                        bitmap=BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(lokacijaUri));
                        naslovna.setImageBitmap(bitmap);

                        try {
                            bitmap=BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(lokacijaUri));
                            naslovna.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == DodavanjeKnjigeAkt.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
