package ba.unsa.etf.rma.dzejlan.rma18sabic17296;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class MojResultReceiver extends ResultReceiver {
    private Receiver mojReceiver;

    public MojResultReceiver(Handler handler) {
        super(handler);
    }

    public void setMojReceiver (Receiver receiver) {
        mojReceiver=receiver;
    }
    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mojReceiver!=null) mojReceiver.onReceiveResult(resultCode, resultData);
    }
}