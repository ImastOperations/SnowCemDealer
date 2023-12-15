package in.imast.snowcemdealer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import in.imast.snowcemdealer.R;
import in.imast.snowcemdealer.helper.DialogClass;
import in.imast.snowcemdealer.helper.LocaleHelper;
import in.imast.snowcemdealer.helper.StaticSharedpreference;
import in.imast.snowcemdealer.helper.Utilities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChooseLanguageActivity extends AppCompatActivity {

    LinearLayout linearSubmit;
    Utilities utilities;
    DialogClass dialogClass;
    Dialog dialog;

    RelativeLayout relativeEnglish, relativeHindi, relativeGujrati;
    TextView tvEnglish, tvHindi, tvHindi2, tvGujrati, tvGujrati2;
    String selectedLanguage = "english";

    ImageView imgEnglish, imgHindi, imgGujrat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_language);
        utilities = new Utilities(ChooseLanguageActivity.this);
        dialogClass = new DialogClass();

        imgEnglish = findViewById(R.id.imgEnglish);
        imgHindi = findViewById(R.id.imgHindi);
        imgGujrat = findViewById(R.id.imgGujrat);

        relativeEnglish = findViewById(R.id.relativeEnglish);
        relativeHindi = findViewById(R.id.relativeHindi);
        relativeGujrati = findViewById(R.id.relativeGujrati);
        linearSubmit = findViewById(R.id.linearSubmit);

        tvHindi2 = findViewById(R.id.tvHindi2);
        tvGujrati2 = findViewById(R.id.tvGujrati2);

        tvEnglish = findViewById(R.id.tvEnglish);
        tvHindi = findViewById(R.id.tvHindi);
        tvGujrati = findViewById(R.id.tvGujrati);

        if (StaticSharedpreference.getInfo("language", this).equals("english") ||
                StaticSharedpreference.getInfo("language", this).equals("")) {
            unselectedViews();
            selectedViews(relativeEnglish, tvEnglish, null, imgEnglish);

        } else if (StaticSharedpreference.getInfo("language", this).equals("hindi")) {
            selectedLanguage = "hindi";
            unselectedViews();
            selectedViews(relativeHindi, tvHindi, tvHindi2, imgHindi);

        } else if (StaticSharedpreference.getInfo("language", this).equals("gujrati")) {
            selectedLanguage = "gujrati";
            unselectedViews();
            selectedViews(relativeGujrati, tvGujrati, tvGujrati2, imgGujrat);

        }
        linearSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StaticSharedpreference.saveInfo("language", selectedLanguage, ChooseLanguageActivity.this);

                if (selectedLanguage.equals("english")) {
                    LocaleHelper.setLocale(ChooseLanguageActivity.this, "en");
                } else if (selectedLanguage.equals("hindi")) {
                    LocaleHelper.setLocale(ChooseLanguageActivity.this, "hi");
                } else if (selectedLanguage.equals("gujrati")) {
                    LocaleHelper.setLocale(ChooseLanguageActivity.this, "gu");
                }

                startActivity(new Intent(ChooseLanguageActivity.this, MainActivity.class)
                        .putExtra("status",""));
                finishAffinity();

            }
        });


        relativeEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectedLanguage = "english";
                unselectedViews();
                selectedViews(relativeEnglish, tvEnglish, null, imgEnglish);


                //  ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });

        relativeHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedLanguage = "hindi";
                unselectedViews();
                selectedViews(relativeHindi, tvHindi, tvHindi2, imgHindi);


                Log.v("akram", "hini");

                // ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });

        relativeGujrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectedViews();
                selectedViews(relativeGujrati, tvGujrati, tvGujrati2, imgGujrat);

                selectedLanguage = "gujrati";

                // ActivityRecreationHelper.recreate(ChooseLanguageActivity.this, false);
            }
        });
    }


    private void unselectedViews() {

        relativeEnglish.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));
        relativeGujrati.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));
        relativeHindi.setBackground(getResources().getDrawable(R.drawable.rectangle_unselect));

        tvHindi.setTextColor(Color.parseColor("#000000"));
        tvHindi2.setTextColor(Color.parseColor("#000000"));
        tvEnglish.setTextColor(Color.parseColor("#000000"));
        tvGujrati.setTextColor(Color.parseColor("#000000"));
        tvGujrati2.setTextColor(Color.parseColor("#000000"));

        imgGujrat.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
        imgHindi.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
        imgEnglish.setBackground(getResources().getDrawable(R.drawable.iuc_radio_button_unselect));
    }

    private void selectedViews(RelativeLayout relative, TextView tv1, TextView tv2, ImageView img) {

        relative.setBackground(getResources().getDrawable(R.drawable.rectangle_select));

        tv1.setTextColor(Color.parseColor("#813D3C"));
        if (tv2 != null)
            tv2.setTextColor(Color.parseColor("#813D3C"));

        img.setBackground(getResources().getDrawable(R.drawable.ic_radio_button));

    }

}

