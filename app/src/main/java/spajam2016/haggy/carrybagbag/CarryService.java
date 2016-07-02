package spajam2016.haggy.carrybagbag;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CarryService extends Service {
    public CarryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
