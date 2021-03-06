package com.fsh.zhaolong.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.fsh.zhaolong.R;
import com.fsh.zhaolong.bean.AddProjrctResponse;
import java.util.ArrayList;
import java.util.List;

public class ProjectDialog {
  private final Display display;
  CallPayType callPayType;
  private Context context;
  private Dialog dialog;
  private LinearLayout lLayout_bg;
  private TextView txt_title;
  private TextView txt_msg;
  private ImageView iv_barcode;
  private ListView listView;
  private List<AddProjrctResponse.DataBean> list = new ArrayList<>();
  private MyAdapter adapter;
  private boolean showTitle = false;
  private boolean showMsg = false;
  private boolean showBarcode = false;
  private boolean showPosBtn = false;
  private boolean showNegBtn = false;

  public ProjectDialog(Context context) {
    this.context = context;
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    display = windowManager.getDefaultDisplay();
  }

  public ProjectDialog builder() {
    // 获取Dialog布局
    View view = LayoutInflater.from(context).inflate(R.layout.view_alertdialog, null);

    // 获取自定义Dialog布局中的控件
    lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
    txt_title = (TextView) view.findViewById(R.id.txt_title);
    txt_title.setVisibility(View.GONE);
    txt_msg = (TextView) view.findViewById(R.id.txt_msg);
    txt_msg.setVisibility(View.GONE);
    iv_barcode = (ImageView) view.findViewById(R.id.barcode_iv);
    iv_barcode.setVisibility(View.GONE);
    listView = (ListView) view.findViewById(R.id.listview);

    // 定义Dialog布局和参数
    dialog = new Dialog(context, R.style.AlertDialogStyle);
    dialog.setContentView(view);
    adapter = new MyAdapter();
    listView.setAdapter(adapter);
    // 调整dialog背景大小
    lLayout_bg.setLayoutParams(
        new FrameLayout.LayoutParams((int) (display.getWidth() * 0.5), LayoutParams.WRAP_CONTENT));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (callPayType != null) {
          callPayType.callback(list.get(i), i);
        }
        dismiss();
      }
    });
    return this;
  }

  public ProjectDialog setTitle(String title) {
    showTitle = true;
    txt_title.setText(title);
    return this;
  }

  public ProjectDialog setMsg(String msg) {
    showMsg = true;
    txt_msg.setGravity(Gravity.CENTER);
    txt_msg.setText(msg);
    return this;
  }

  public ProjectDialog setMsgAlignLeft(String msg) {
    showMsg = true;
    txt_msg.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    txt_msg.setText(msg);
    return this;
  }

  public ProjectDialog setCancelable(boolean cancel) {
    dialog.setCancelable(cancel);
    return this;
  }

  public ProjectDialog setCanceledOnTouchOutside(boolean cancel) {
    dialog.setCanceledOnTouchOutside(cancel);
    return this;
  }

  public ProjectDialog setPositiveButton(String text) {
    showPosBtn = true;
    //if (TextUtils.isEmpty(text)) {
    //  btn_pos.setText("确定");
    //} else {
    //  btn_pos.setText(text);
    //}
    //btn_pos.setOnClickListener(new OnClickListener() {
    //  @Override public void onClick(View v) {
    //    dialog.dismiss();
    //  }
    //});
    return this;
  }

  public ProjectDialog setList(List<AddProjrctResponse.DataBean> list) {
    this.list = list;
    adapter.notifyDataSetChanged();
    return this;
  }

  public ProjectDialog setPositiveButton(String text, final OnClickListener listener) {
    showPosBtn = true;

    return this;
  }

  private void setLayout() {
    if (!showTitle && !showMsg) {
      txt_title.setText("提示");
      txt_title.setVisibility(View.VISIBLE);
    }

    if (showTitle) {
      txt_title.setVisibility(View.VISIBLE);
    }

    if (showMsg) {
      txt_msg.setVisibility(View.VISIBLE);
    }

    if (showBarcode) {
      iv_barcode.setVisibility(View.VISIBLE);
    }

    if (!showPosBtn && !showNegBtn) {
      //btn_pos.setText("确定");
      //btn_pos.setVisibility(View.VISIBLE);
      //btn_pos.setBackgroundResource(R.drawable.alert_dialog_single_selector);
      //btn_pos.setOnClickListener(new OnClickListener() {
      //  @Override public void onClick(View v) {
      //    dialog.dismiss();
      //  }
      //});

    }
  }

  public void show() {
    setLayout();
    dialog.show();
  }

  public void dismiss() {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  public void setProjectCallback(CallPayType callPayType) {
    this.callPayType = callPayType;
  }

  public interface CallPayType {
    void callback(AddProjrctResponse.DataBean dataBean, int i);
  }

  private class MyAdapter extends BaseAdapter {

    @Override public int getCount() {
      return list.size();
    }

    @Override public Object getItem(int i) {
      return list.get(i);
    }

    @Override public long getItemId(int i) {
      return i;
    }

    @Override public View getView(int i, View view, ViewGroup viewGroup) {
      ViewHold viewHold;
      if (view == null) {
        viewHold = new ViewHold();
        view = LayoutInflater.from(context).inflate(R.layout.adapter_alert, null);
        viewHold.tv = (TextView) view.findViewById(R.id.tv);
        view.setTag(viewHold);
      } else {
        viewHold = (ViewHold) view.getTag();
      }
      viewHold.tv.setText(list.get(i).getProjectname());
      return view;
    }

    class ViewHold {
      TextView tv;
    }
  }
}