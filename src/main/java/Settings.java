import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    public final static String URL = "url";
    public final static String LOGIN = "login";
    public final static String PASSWORD = "password";
    private static Settings settings;

    private Settings() {
    }

    public static Settings getSettings(){
        if (settings==null) settings = new Settings();
        return settings;
    }

    public String[] getConnectionData() {
        String[] data = null;
        File file = new File("DBDataConnection.prop");
        try {
            if (!file.exists()) file.createNewFile();
            else {
                Properties properties = new Properties();
                properties.load(new FileInputStream(file));
                data = new String[]{
                        properties.getProperty(Settings.URL),
                        properties.getProperty(Settings.LOGIN),
                        properties.getProperty(Settings.PASSWORD),
                };
            }
        } catch (IOException e) {
            System.out.println("Не удалось получить данные с файла для соединения c БД");
        }
        return data;
    }
}
