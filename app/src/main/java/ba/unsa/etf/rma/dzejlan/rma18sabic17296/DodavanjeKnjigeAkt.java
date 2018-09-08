package ba.unsa.etf.rma.dzejlan.rma18sabic17296;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_NAME;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.DATABASE_VERSION;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.KNJIGA_ID_KATEGORIJE;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.NAZIV_KNJIGE;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.BazaOpenHelper.PREGLEDANA_KNJIGA;
import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;

public class DodavanjeKnjigeAkt extends AppCompatActivity {

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_knjige_akt);
        final Intent intent = getIntent();
        final Button dPonisti= (Button) findViewById(R.id.dPonisti);
        final Button dUpisi=(Button) findViewById(R.id.dUpisiKnjigu);
        final Button dNadjiSliku=(Button) findViewById(R.id.dNadjiSliku);
        final EditText imeAutora=(EditText) findViewById(R.id.imeAutora);
        final EditText nazivKnjige=(EditText) findViewById(R.id.nazivKnjige);
        final Spinner sKategorije = (Spinner) findViewById(R.id.sKategorijaKnjige);
        final ImageView slikaKnjige=(ImageView) findViewById(R.id.naslovnaStr) ;

        ArrayList<String> kateg = new ArrayList<String>();
        kateg=intent.getStringArrayListExtra("kategorije");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kateg);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sKategorije.setAdapter(adapter);

        dPonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), KategorijeAkt.class);
                startActivity(myIntent);
            }
        });
        dUpisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slikaKnjige.setDrawingCacheEnabled(true);
                slikaKnjige.buildDrawingCache();
                Bitmap bm=Bitmap.createBitmap(slikaKnjige.getDrawingCache());
                Knjiga k=new Knjiga(nazivKnjige.getText().toString(),imeAutora.getText().toString(),sKategorije.getSelectedItem().toString(),false,bm);
                knjige.add(k);

                ContentValues nova=new ContentValues();
                nova.put(NAZIV_KNJIGE, nazivKnjige.getText().toString());
                nova.put(KNJIGA_ID_KATEGORIJE, sKategorije.getSelectedItem().toString());
                nova.put(PREGLEDANA_KNJIGA, 0);
                BazaOpenHelper bazaOH=new BazaOpenHelper(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
                SQLiteDatabase db=bazaOH.getWritableDatabase();
                db.insert(BazaOpenHelper.KNJIGA_TABLE,null,nova);
            }
        });

        dNadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Izaberite sliku"),1);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            TextView textLokacijaUri=new TextView(this);
            if (resultCode == DodavanjeKnjigeAkt.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {
                        Uri lokacijaUri=data.getData();
                        textLokacijaUri.setText(lokacijaUri.toString());
                        Bitmap bitmap;
                        ImageView naslovna=(ImageView) findViewById(R.id.naslovnaStr);
                        bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(lokacijaUri));
                        naslovna.setImageBitmap(bitmap);
                        try {
                            bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(lokacijaUri));
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
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
