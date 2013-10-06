package jp.shuri.android.railsclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class MyExceptionDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder Builder = new AlertDialog.Builder(getActivity());
		Builder.setTitle("プログラム異常終了");
		Builder.setMessage("アプリケーションが異常終了しました。");
		Builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent i = new Intent(getActivity(), MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				getActivity().startActivity(i);
				dialog.dismiss();
            }
		});
		Builder.setCancelable(false);

		return Builder.create();
	}
}

