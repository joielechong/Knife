package io.github.mthli.knifedemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.github.mthli.knife.Knife;
import io.github.mthli.knife.KnifeText;

public class MainActivity extends Activity {
    private static final String EXAMPLE = ""
            + "<b>Bold</b><p>"
            + "<i>Italic</i><br><br>"
            + "<u>Underline</u><br><br>"
            + "<s>Strikethrough</s><br><br>"
            + "<ul><li>Bullet</li></ul>"
            + "<blockquote>Quote</blockquote>"
            + "<a href=\"https://google.com/\">Link</a><br><br>";

    private KnifeText textView;
    private Knife knife;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.knife);

        knife = textView.getKnife();
        knife.setHtml(EXAMPLE);

        textView.setSelection(textView.getEditableText().length());

        setFormatButton(R.id.bold, R.string.toast_bold, Knife.BOLD);
        setFormatButton(R.id.italic, R.string.toast_italic, Knife.ITALIC);
        setFormatButton(R.id.underline, R.string.toast_underline, Knife.UNDERLINE);
        setFormatButton(R.id.strikethrough, R.string.toast_strikethrough, Knife.STRIKE);
        setFormatButton(R.id.bullet, R.string.toast_bullet, Knife.BULLET);
        setFormatButton(R.id.quote, R.string.toast_quote, Knife.QUOTE);

        setButton(R.id.link, R.string.toast_insert_link, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });

        setButton(R.id.clear, R.string.toast_format_clear, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.clearFormat();
            }
        });

        setButton(R.id.code, R.string.toast_code, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(knife.getHtml());
                findViewById(R.id.tools).setVisibility(View.GONE);
            }
        });
    }

    private void setButton(int id, final int textId, View.OnClickListener listener) {
        final View view = findViewById(id);
        view.setOnClickListener(listener);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, textId, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setFormatButton(int id, final int textId, final Class format) {
        setButton(id, textId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.toggle(format);
            }
        });
    }

    private void showLinkDialog() {
        final int start = textView.getSelectionStart();
        final int end = textView.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }

                // When KnifeText lose focus, use this saved start / end positions
                knife.setLink(link, start, end);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // DO NOTHING HERE
                    }
                });

        builder.create().show();
    }

}
