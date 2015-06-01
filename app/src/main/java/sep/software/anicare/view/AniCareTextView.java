package sep.software.anicare.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import sep.software.anicare.R;

/**
 * Created by hongkunyoo on 15. 6. 1..
 */
public class AniCareTextView extends TextView {
    public AniCareTextView(Context context) {
        super(context);

        setCustomSettings(context);
    }

    public AniCareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setCustomSettings(context);
    }

    public AniCareTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setCustomSettings(context);
    }

    private void setCustomSettings(Context context) {
//        this.setTextColor(getResources().getColor(android.R.color.white));
        this.setBackground(getResources().getDrawable(R.drawable.box_text_view));
        this.setPadding(12,4,12,4);
    }
}
