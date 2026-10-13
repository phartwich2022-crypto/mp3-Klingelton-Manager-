package com.mp3klingeltonmanager;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_PICK_RINGTONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectButton = findViewById(R.id.button_select_ringtone);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Settings.System.canWrite(MainActivity.this)) {
                    openAudioPicker();
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Bitte erlaube das Ändern der Systemeinstellungen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openAudioPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(Intent.createChooser(intent, "Wähle eine MP3-Datei"), REQUEST_CODE_PICK_RINGTONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_RINGTONE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri audioUri = data.getData();
                try {
                    RingtoneManager.setActualDefaultRingtoneUri(
                            MainActivity.this,
                            RingtoneManager.TYPE_RINGTONE,
                            audioUri
                    );
                    Toast.makeText(MainActivity.this, "Klingelton erfolgreich gesetzt!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Fehler beim Setzen des Klingeltons", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
