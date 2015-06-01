package sep.software.anicare.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import sep.software.anicare.R;

/**
 * Created by hongkunyoo on 15. 6. 1..
 */
public class AniCareButton extends Button {

    public AniCareButton(Context context) {
        super(context);
        setCustomSettings(context);
    }

    public AniCareButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setCustomSettings(context);
    }

    public AniCareButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setCustomSettings(context);
    }

    private void setCustomSettings(Context context) {
//        Typeface typeFace= Typeface.createFromAsset(context.getAssets(), "NotoSansCJKkr-Bold.otf");
//        this.setTypeface(typeFace);
        this.setTextColor(getResources().getColor(android.R.color.white));
        this.setBackground(getResources().getDrawable(R.drawable.custom_btn_bg));
        this.setPadding(8,2,8,2);
    }

}
