package vasylenko.lightfilemanager.local;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleManager {

    public static void changeLocale(Resources res, String localeName) {
        //TODO: Must be improve in the latest version!
        Locale locale;
        Configuration config;
        config = new Configuration(res.getConfiguration());

        switch (localeName) {
            case "ru":
                locale = new Locale(localeName);
                Locale.setDefault(locale);
                config.locale = locale;
                break;

            default:
                locale = new Locale(localeName);
                Locale.setDefault(locale);
                config.locale = locale;
                break;
        }
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}