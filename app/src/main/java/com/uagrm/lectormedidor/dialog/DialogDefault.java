package com.uagrm.lectormedidor.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uagrm.lectormedidor.R;


public class DialogDefault {
    private String title, message, positiveBtnText, negativeBtnText;
    private Activity activity;
    private int icon;
    private Icon visibility;
    private Animacion animation;
    private DialogDefaultInterface pListener, nListener;
    private int pBtnColor, nBtnColor, bgColor;
    private boolean cancel;
    private DialogDefault(Builder builder) {
        this.title = builder.title;
        this.message = builder.message;
        this.activity = builder.activity;
        this.icon = builder.icon;
        this.animation = builder.animation;
        this.visibility = builder.visibility;
        this.pListener = builder.pListener;
        this.nListener = builder.nListener;
        this.positiveBtnText = builder.positiveBtnText;
        this.negativeBtnText = builder.negativeBtnText;
        this.pBtnColor = builder.pBtnColor;
        this.nBtnColor = builder.nBtnColor;
        this.bgColor = builder.bgColor;
        this.cancel = builder.cancel;
    }


    public static class Builder {
        private Typeface typeFaceRegular,typeFaceBold;
        private String title, message, positiveBtnText, negativeBtnText;
        private Activity activity;
        private int icon;
        private Icon visibility;
        private Animacion animation;
        private DialogDefaultInterface pListener, nListener;
        private int pBtnColor, nBtnColor, bgColor;
        private boolean cancel;
        private Spanned messageHtml = null;

        public Builder(Activity activity) {
            this.activity = activity;
            typeFaceRegular = Typeface.createFromAsset(activity.getAssets(), "fonts/MulishRegular.ttf");
            typeFaceBold = Typeface.createFromAsset(activity.getAssets(), "fonts/MulishBold.ttf");
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setBackgroundColor(int bgColor) {
            this.bgColor = bgColor;
            return this;
        }


        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessageHtml(Spanned message) {
            this.messageHtml = message;
            return this;
        }


        public Builder setPositiveBtnText(String positiveBtnText) {
            this.positiveBtnText = positiveBtnText;
            return this;
        }

        public Builder setTextColorPositive(int bgColor) {
            this.bgColor = bgColor;
            return this;
        }


        public Builder setNegativeBtnText(String negativeBtnText) {
            this.negativeBtnText = negativeBtnText;
            return this;
        }

        public Builder setNegativeBtnBackground(int nBtnColor) {
            this.nBtnColor = nBtnColor;
            return this;
        }


        //setIcon
        public Builder setIcon(int icon, Icon visibility) {
            this.icon = icon;
            this.visibility = visibility;
            return this;
        }

        public Builder setAnimation(Animacion animation) {
            this.animation = animation;
            return this;
        }

        //set Positive listener
        public Builder OnPositiveClicked(DialogDefaultInterface pListener) {
            this.pListener = pListener;
            return this;
        }

        //set Negative listener
        public Builder OnNegativeClicked(DialogDefaultInterface nListener) {
            this.nListener = nListener;
            return this;
        }

        public Builder isCancellable(boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        public DialogDefault build() {
            TextView message1, title1;
            ImageView iconImg;
            Button nBtn, pBtn;
            final Dialog dialog;
            if (animation == Animacion.POP)
                dialog = new Dialog(activity, R.style.PopTheme);
            else if (animation == Animacion.SIDE)
                dialog = new Dialog(activity, R.style.SideTheme);
            else if (animation == Animacion.SLIDE)
                dialog = new Dialog(activity, R.style.SlideTheme);
            else
                dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(cancel);
            dialog.setContentView(R.layout.dialog_default);

            //getting resources
            title1 = dialog.findViewById(R.id.title);
            title1.setTypeface(typeFaceBold);
            message1 = dialog.findViewById(R.id.message);
            message1.setTypeface(typeFaceRegular);
            iconImg = dialog.findViewById(R.id.icon);
            RelativeLayout relativelayout1 = dialog.findViewById(R.id.relativelayout1);
            nBtn =  dialog.findViewById(R.id.negativeBtn);
            nBtn.setTypeface(typeFaceBold);
            pBtn =  dialog.findViewById(R.id.positiveBtn);
            pBtn.setTypeface(typeFaceBold);
            title1.setText(title);
            message1.setText(message);
            if (messageHtml != null) {
                message1.setText((CharSequence) messageHtml);
                message1.setGravity(Gravity.LEFT);
            }
            if (positiveBtnText != null)
                pBtn.setText(positiveBtnText);
            if (pBtnColor != 0) {
                GradientDrawable bgShape = (GradientDrawable) pBtn.getBackground();
                bgShape.setColor(pBtnColor);
            }
            if (nBtnColor != 0) {
                GradientDrawable bgShape = (GradientDrawable) nBtn.getBackground();
                bgShape.setColor(nBtnColor);
            }
            if (negativeBtnText != null)
                nBtn.setText(negativeBtnText);
            iconImg.setImageResource(icon);
            if (visibility == Icon.Visible){
                iconImg.setVisibility(View.VISIBLE);
            }else{
                iconImg.setVisibility(View.GONE);
            }


            if (bgColor != 0)
                pBtn.setTextColor(bgColor);
            if (pListener != null) {
                pBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pListener.OnClick();
                        dialog.dismiss();
                    }
                });
            } else {
                pBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }

                });
            }

            if (nListener != null) {
                nBtn.setVisibility(View.VISIBLE);
                nBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nListener.OnClick();

                        dialog.dismiss();
                    }
                });
            }

            if (nBtn.getText().toString().equals("")) {
                nBtn.setVisibility(View.GONE);
                LinearLayout.LayoutParams lay = (LinearLayout.LayoutParams) pBtn.getLayoutParams();
                lay.weight = 100;
                pBtn.setLayoutParams(lay);
            }

            dialog.show();

            return new DialogDefault(this);

        }
    }

}
