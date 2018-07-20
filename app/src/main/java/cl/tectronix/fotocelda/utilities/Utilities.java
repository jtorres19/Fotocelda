package cl.tectronix.fotocelda.utilities;

import android.content.Context;
import android.widget.Toast;

public final class Utilities {
    public static void showToastText(Context context, String texto){
        Toast toast = Toast.makeText(context, texto, Toast.LENGTH_LONG);
        toast.show();
    }

}
