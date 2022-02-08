package com.nurdtechie98.watchman;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import androidx.annotation.RequiresApi;

import java.util.List;

import static com.nurdtechie98.watchman.NodeParser.logNodeHeirarchy;
import static com.nurdtechie98.watchman.NodeParser.writeToFile;

public class Watcher extends AccessibilityService {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String packageName = (String) accessibilityEvent.getPackageName();
        String className = (String) accessibilityEvent.getClassName();
        AccessibilityNodeInfo node = accessibilityEvent.getSource();
        String output = packageName + "/" + className;
        Log.i("WATCHMAN", "package_class_name " + output);
        switch(output) {
            case "android/com.android.server.am.AppNotRespondingDialog":
                anrHandler(node);
                break;
            default:
                genericHandler(node, output);
                break;
        }
    }

    @Override
    public void onServiceConnected() {
        Log.i("WATCHMAN", "Started accessibility");
    }

    @Override
    public void onInterrupt() {
    }

    private void anrHandler(AccessibilityNodeInfo nodeInfo) {
        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/aerr_close");
        if (findAccessibilityNodeInfosByViewId.size() > 0) {
            AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
            Log.i("WATCHMAN", "Dismissing anr popup");
            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void genericHandler(AccessibilityNodeInfo node, String packageName) {
        List<AccessibilityWindowInfo> windows = super.getWindows();
        Log.i("WATCHMAN", "all windows: " + windows.toString());
        AccessibilityWindowInfo topLayer = null;
        for(AccessibilityWindowInfo window: windows) {
            if(window.getLayer() == 0) {
                topLayer = window;
            }
        }

        if(topLayer.getType() != AccessibilityWindowInfo.TYPE_APPLICATION) {
            if(topLayer == null) Log.i("NURDTECHIE98:WIN1", node.toString());
            AccessibilityNodeInfo derivedNode = topLayer.getRoot();
            if(derivedNode == null) return;
            new Thread( new Runnable() { @Override public void run() {
                String tree = logNodeHeirarchy(derivedNode, 0, 10);
                Log.i("WATCHMAN:TREE", tree);
                if(tree != "") writeToFile(tree, packageName);
            } } ).start();
        } else {
            Log.i("WATCHMAN", "things looks normal");
        }
    }
}
