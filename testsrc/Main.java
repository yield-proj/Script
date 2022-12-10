import com.xebisco.yieldscript.Script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        StringBuilder file = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("test.ys"))))) {
            String line;
            while ((line = reader.readLine()) != null)
                file.append(line).append('\n');
            if (file.length() > 0)
                file.setLength(file.length() - 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Script script = new Script(file.toString());
        script.run();
    }
}
