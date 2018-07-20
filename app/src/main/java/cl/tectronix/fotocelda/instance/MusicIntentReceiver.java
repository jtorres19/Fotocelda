package cl.tectronix.fotocelda.instance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class MusicIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_HEADSET_PLUG)){
            int state = intent.getIntExtra("state",-1);
            switch (state){
                case 0:
                    Utilities.showToastText(context,"Auriculares desconectados");
                    break;
                case 1:
                    Utilities.showToastText(context,"Auriculares conectados");
                    break;
                default:
                    Utilities.showToastText(context,"Estado desconocido");
                    break;
            }
        }
    }
}
