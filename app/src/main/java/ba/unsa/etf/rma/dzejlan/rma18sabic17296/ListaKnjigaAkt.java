package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import static ba.unsa.etf.rma.dzejlan.rma18sabic17296.KategorijeAkt.knjige;

public class ListaKnjigaAkt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_knjiga_akt);
        final Button dugmePonisti = (Button) findViewById(R.id.dPovratak);
        final ListView listaKnjiga = (ListView) findViewById(R.id.listaKnjiga);
        final ArrayList<Knjiga> odgovarajuceKnjige = new ArrayList<Knjiga>();
        final KnjigaAdapter knjigaAdapter = new KnjigaAdapter(this, R.layout.element_liste, odgovarajuceKnjige);
        listaKnjiga.setAdapter(knjigaAdapter);
        Intent myIntent = getIntent();
        for (Knjiga k : knjige) {
            if ( k.getKategorija().equals(myIntent.getStringExtra("kategorija")) ) {
                odgovarajuceKnjige.add(k);
                knjigaAdapter.notifyDataSetChanged();
            }
        }
        dugmePonisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odgovarajuceKnjige.clear();
                Intent myIntent = new Intent(getApplicationContext(), KategorijeAkt.class);
                startActivity(myIntent);
            }
        });
    }
}
