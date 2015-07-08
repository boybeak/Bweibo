package com.beak.beakkit.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.beak.beakkit.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by gaoyunfei on 15/5/15.
 */
public class DebugFragment extends Fragment {

    private static DebugFragment sFragment = new DebugFragment();

    public static DebugFragment getInstance () {
        return sFragment;
    }

    private Button mClearBtn, mCopyBtn, mSaveBtn, mStartBtn, mLastBtn, mNextBtn, mEndBtn;
    private ScrollView mScrollView = null;
    private TextView mMsgTv = null;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mMsgTv != null) {
                CharSequence cs = intent.getCharSequenceExtra(Debug.KEY_DEBUG_MSG);
                mMsgTv.append(cs);
                mMsgTv.measure(0, 0);
                mScrollView.smoothScrollTo(0, mMsgTv.getMeasuredHeight());
            }
        }
    };

    private IntentFilter mFilter = new IntentFilter(Debug.ACTION_DEBUG);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug, null, true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClearBtn = (Button)view.findViewById(R.id.debug_clear);
        mCopyBtn = (Button)view.findViewById(R.id.debug_copy_all);
        mSaveBtn = (Button)view.findViewById(R.id.debug_save_all);
        mStartBtn = (Button)view.findViewById(R.id.debug_start);
        mEndBtn = (Button)view.findViewById(R.id.debug_end);
        mLastBtn = (Button)view.findViewById(R.id.debug_last_page);
        mNextBtn = (Button)view.findViewById(R.id.debug_next_page);
        mScrollView = (ScrollView)view.findViewById(R.id.debug_scrollview);
        mMsgTv = (TextView)view.findViewById(R.id.debug_msg);

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMsgTv.setText("");
            }
        });
        mCopyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(mMsgTv.getText().toString());
                Toast.makeText(getActivity(), R.string.debug_copy_success, Toast.LENGTH_SHORT).show();
            }
        });
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = Environment.getExternalStorageDirectory() + File.separator
                        + "debug" + File.separator + System.currentTimeMillis() + ".txt";
                File outFile = new File(path);
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }
                try {
                    String content = mMsgTv.getText().toString();
                    FileWriter fileWriter = new FileWriter(path);
                    fileWriter.write(content);
                    fileWriter.flush();
                    fileWriter.close();
                    Toast.makeText(getActivity(), "saved at " + outFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "save failed ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mLastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMsgTv.measure(0, 0);
                mScrollView.smoothScrollBy(0, -mMsgTv.getMeasuredHeight());
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMsgTv.measure(0, 0);
                mScrollView.smoothScrollBy(0, mMsgTv.getMeasuredHeight());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

}
