package com.steps.networking;

import android.database.Cursor;

public interface OnFinishListener {
	void OnFinish(Cursor result, ErrorCode err);
}
