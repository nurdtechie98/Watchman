package com.nurdtechie98.watchman;

import android.os.Build;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class NodeParser {
    public static String logNodeHeirarchy(AccessibilityNodeInfo nodeInfo, int depth, int maxDepth) {
        if(depth > maxDepth) return "";
        if (nodeInfo == null) return "";
        String logString = "";
        String indent = "";
        for (int i = 0; i < depth; ++i) {
            indent += " ";
        }
        CharSequence nodeText = nodeInfo.getText();
        CharSequence contentDesc = nodeInfo.getContentDescription();
        if(nodeText != null || contentDesc != null) logString = indent + "Text: " + nodeText + " " + " Content-Description: " + contentDesc;
        for (int i = 0; i < nodeInfo.getChildCount(); ++i) {
            String childText = logNodeHeirarchy(nodeInfo.getChild(i), depth + 1, maxDepth);
            if(childText != "") {
                if(logString != "") {
                    logString += "\n" + childText;
                } else {
                    logString = childText;
                }
            }
        }
        return logString;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void writeToFile(String data, String className) {
        File logFileName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "watchman_dump.txt");
        String logfilePath = String.valueOf(logFileName.getAbsolutePath());
        Time now = new Time();
        String final_data = "\n===========" + now + "===========\n";
        final_data += "\n===========" + className + "===========\n";
        final_data += data;
        try {
            FileOutputStream outputStreamLog;
            outputStreamLog = new FileOutputStream(logfilePath, true); // true for append
            outputStreamLog.write(final_data.getBytes());
            Log.i("WATCHMAN","wrote to file:" + logfilePath);
            outputStreamLog.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
