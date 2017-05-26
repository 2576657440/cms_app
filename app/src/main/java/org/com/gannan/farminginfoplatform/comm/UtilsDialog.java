package org.com.gannan.farminginfoplatform.comm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import org.com.gannan.farminginfoplatform.R;
import org.com.gannan.farminginfoplatform.activity.LoginActivity;
import org.com.gannan.farminginfoplatform.utils.AnimUtil;
import org.com.gannan.farminginfoplatform.utils.SharedUtil;


/**
 * Created by Administrator on 2016/6/6.
 */
public class UtilsDialog {
    private View.OnClickListener onClickListenerOk;
    public AlertDialog dialog;
    public static volatile UtilsDialog instance;

    public static UtilsDialog getInstance() {
        if (instance == null) {
            synchronized (UtilsDialog.class) {
                if (instance == null) {
                    instance = new UtilsDialog();
                }
            }
        }
        return instance;
    }
    /*
     * 有更新时显示提示对话框
     */

    public void showNoticeDialog(final Activity activity, String title, String msg) {
        if(dialog==null) {
            dialog = new AlertDialog.Builder(activity).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();

            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (screenWidth * 0.8f);
            dialog.getWindow().setAttributes(params);

            dialog.setContentView(R.layout.util_dialog);
            TextView titleTv = (TextView) dialog.findViewById(R.id.dialog_title);
            TextView msgTv = (TextView) dialog.findViewById(R.id.message);
            TextView okBtn = (TextView) dialog.findViewById(R.id.cadbtnOk);
            TextView cancelBtn = (TextView) dialog.findViewById(R.id.cadbtnCancel);
            titleTv.setText(title);
            msgTv.setText(msg);
            okBtn.setOnClickListener(onClickListenerOk);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }

    /*
         * 仅信息显示提示对话框
         */
    public void showSingleNoticeDialog(final Activity activity, String title, String msg) {
        if(dialog==null) {
            dialog = new AlertDialog.Builder(activity).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (screenWidth * 0.9f);
            dialog.getWindow().setAttributes(params);

            dialog.setContentView(R.layout.util_dialog);
            TextView titleTv = (TextView) dialog.findViewById(R.id.dialog_title);
            TextView msgTv = (TextView) dialog.findViewById(R.id.message);
            TextView okBtn = (TextView) dialog.findViewById(R.id.cadbtnOk);
            TextView cancelBtn = (TextView) dialog.findViewById(R.id.cadbtnCancel);
            cancelBtn.setVisibility(View.GONE);
            titleTv.setText(title);
            msgTv.setText(msg);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dismiss();
                }
            });

        }
    }

    /*
        * 仅信息显示提示对话框
        */
    public void showExitNoticeDialog(final Activity activity, String title, String msg) {
        if(dialog==null) {
            dialog = new AlertDialog.Builder(activity).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();

            int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = (int) (screenWidth * 0.9f);
            dialog.getWindow().setAttributes(params);

            dialog.setContentView(R.layout.util_dialog);
            TextView titleTv = (TextView) dialog.findViewById(R.id.dialog_title);
            TextView msgTv = (TextView) dialog.findViewById(R.id.message);
            TextView okBtn = (TextView) dialog.findViewById(R.id.cadbtnOk);
            TextView cancelBtn = (TextView) dialog.findViewById(R.id.cadbtnCancel);
            cancelBtn.setVisibility(View.GONE);
            titleTv.setText(title);
            msgTv.setText(msg);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedUtil.saveLoginFlag(activity, false);
                    SharedUtil.savePassword(activity, "");

                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                    AnimUtil.changePageIn(activity);
                    dismiss();
                }
            });
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            dialog=null;
        }
    }

    public View.OnClickListener getOnClickListenerOk() {
        return onClickListenerOk;
    }

    public void setOnClickListenerOk(View.OnClickListener onClickListenerOk) {
        this.onClickListenerOk = onClickListenerOk;
    }

}
