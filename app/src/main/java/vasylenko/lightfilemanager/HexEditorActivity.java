package vasylenko.lightfilemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import static vasylenko.lightfilemanager.worker.FileWorker.convertFileToHex;

public class HexEditorActivity extends AppCompatActivity {
    private EditText hexEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_editor);

        hexEditText = (EditText)findViewById(R.id.hex_editor);
        hexEditText.setKeyListener(null);

        Intent intent = getIntent();
        String pathTofile = intent.getStringExtra(MainActivity.HEX_EDITOR);
        try {
            hexEditText.setText(convertFileToHex(pathTofile));
        } catch (IOException e) {
            Toast.makeText(this, "Built Hexdecimal ERROR! ", Toast.LENGTH_SHORT).show();
        }
    }
}
