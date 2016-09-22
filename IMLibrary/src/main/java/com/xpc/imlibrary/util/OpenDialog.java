package com.xpc.imlibrary.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xpc.imlibrary.R;
import com.xpc.imlibrary.widget.CustomDialog;
import com.xpc.imlibrary.widget.MaxLengthEditText;

/**
 * 调用对话框
 * @author qiaocbao
 * @time 2014-7-25 下午5:21:36
 */
public class OpenDialog {
    /**输入框成员对象*/
    private Dialog myDialog;
	private OpenDialog() {
	}

	private static class DialogHolder {
		static final OpenDialog INSTANCE = new OpenDialog();
	}

	public static OpenDialog getInstance() {
		return DialogHolder.INSTANCE;
	}

	private Object readResolve() {
		return getInstance();
	}

	/**
	 * 弹出一个确定对话框，无操作
	 * 
	 * @param context
	 * @param content
	 */
	public void showDialog(Context context, String content) {
		try {
			if (context != null) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setMessage(content);
				builder.setTitle(context.getResources().getString(R.string.warm_prompt));
				builder.setPositiveButton(context.getResources().getString(R.string.confirm),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 设置你的操作事项
							}
						});
				builder.create().show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回一个对话框Builder
	 * 
	 * @param context
	 * @param content
	 * @return
	 */
	// public CustomDialog.Builder showListenerDialog(Context context,
	// String content) {
	//
	// CustomDialog.Builder builder = new CustomDialog.Builder(context);
	// builder.setMessage(content);
	// builder.setTitle("温馨提示");
	// return builder;
	// }

	/**
	 * 弹出一个确定对话框，有监听事件
	 * @param context	当前context
	 * @param content	对话框内容
	 * @param ok	按钮
	 * @param okListener	按钮监听事件
	 */
	public void showOneBtnListenerDialog(Context context, String content,
										 String ok, DialogInterface.OnClickListener okListener) {
		try {
			if (context != null) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setMessage(content);
				builder.setTitle(context.getResources().getString(R.string.warm_prompt));
				builder.setPositiveButton(ok, okListener);
				builder.create().show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 弹出两个按钮对话框，有监听事件
	 * @param context	当前context
	 * @param content	对话框内容
	 * @param ok	第一个按钮
	 * @param okListener	第一个按钮监听事件
	 * @param cancel	第二个按钮
	 * @param cancelListener	第二个按钮监听事件
	 */
	public void showTwoBtnListenerDialog(Context context, String content,
										 String ok, DialogInterface.OnClickListener okListener,
										 String cancel, DialogInterface.OnClickListener cancelListener) {
		try {
			if (context != null) {
				CustomDialog.Builder builder = new CustomDialog.Builder(context);
				builder.setMessage(content);
				builder.setTitle(context.getResources().getString(R.string.warm_prompt));
				builder.setPositiveButton(ok, okListener);
				builder.setNegativeButton(cancel, cancelListener);
				builder.create().show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 内容输入对话框
	 * @param context 当前context
	 * @param title  标题
	 * @param defaultContent  EditText默认显示内容
	 * @param listener 确定按钮事件处理
	 */
	public void showEditContentDialog(Context context, String title, String defaultContent, final OnEditContentDialogClickListener listener, int maxLeng) {
		
		myDialog = new Dialog(context, R.style.Dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.edit_content_dialog, null);
		TextView dialogTitleText = (TextView) view.findViewById(R.id.dialogTitleText);
		
		dialogTitleText.setText(title);
		final MaxLengthEditText contentEdit = (MaxLengthEditText) view.findViewById(R.id.contentEdit);
		if(maxLeng != 0){
			contentEdit.setFilters(new InputFilter[]{new MyLengthFilter(maxLeng,context)});
		}
		if(defaultContent!=null){
			contentEdit.setText(defaultContent);
		}
		TextView sureText= (TextView) view.findViewById(R.id.sureText);
		TextView cancelText= (TextView) view.findViewById(R.id.cancelText);
		sureText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onClick(myDialog, contentEdit.getText().toString().trim());		
				}
			}
		});
		cancelText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
		myDialog.setContentView(view, new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		LayoutParams lp = myDialog.getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
	    myDialog.show();
	}
	/**重载编辑框方法,默认长度15*/
	public void showEditContentDialog(Context context, String title, String defaultContent, final OnEditContentDialogClickListener listener) {
		  showEditContentDialog(context, title, defaultContent, listener, 0);
	}
	/**
	 * 内容输入对话框
	 * @param context
	 * @param title
	 * @param defaultContent
	 * @param listener
	 */
	public void showInputContentDialog(Context context, String title, String defaultContent, final OnEditContentDialogClickListener listener) {
		myDialog = new Dialog(context, R.style.Dialog);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.edittext_dialog, null);
		TextView dialogTitleText = (TextView) view.findViewById(R.id.dialogTitleText);
		dialogTitleText.setText(title);
		final EditText contentEdit = (EditText) view.findViewById(R.id.contentEdit);
		if(defaultContent!=null){
			contentEdit.setText(defaultContent);
		}
		TextView sureText= (TextView) view.findViewById(R.id.sureText);
		TextView cancelText= (TextView) view.findViewById(R.id.cancelText);
		sureText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener!=null){
					listener.onClick(myDialog, contentEdit.getText().toString().trim());		
				}
			}
		});
		cancelText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
		myDialog.setContentView(view, new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		LayoutParams lp = myDialog.getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
	    myDialog.show();
	}
	/**
	 * 两个按钮的输入对话框
	 * @param context 当前context
	 * @param title  标题
	 * @param listener 确定按钮事件处理
	 */
	public void showEditDialog(Context context, String title, final OnEditDialogClickListener listener) {
			myDialog = new Dialog(context, R.style.Dialog);
			View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_layout,null);
			TextView tv_title = (TextView) view.findViewById(R.id.title);
			tv_title.setText(title);
			final EditText inputEditText = (EditText) view.findViewById(R.id.inputEditText);
			Button bt_cancel = (Button) view.findViewById(R.id.negativeButton);
			Button bt_confirm = (Button) view.findViewById(R.id.positiveButton);
			bt_cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					myDialog.dismiss();
				}
			});
			bt_confirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(v,inputEditText.getText().toString().trim());
					myDialog.dismiss();
				}
			});
			myDialog.setContentView(view, new LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			LayoutParams lp = myDialog.getWindow().getAttributes();
			lp.gravity = Gravity.CENTER;
		    myDialog.show();
	}

   public interface OnEditDialogClickListener{ 
	    public void onClick(View v, String content);
   }
   public interface OnEditContentDialogClickListener{ 
	    public void onClick(Dialog dialog, String content);
  }
   
//	/**
//	 * 含listView的选择操作框
//	 * @param context  当前context
//	 * @param items  每个item的字符串
//	 * @param listener 监听
//	 */
//
//	public void showListDialog(Context context, String[] items, final OnItemClickListener listener) {
//			myDialog = new Dialog(context, R.style.Dialog);
//			View view = LayoutInflater.from(context).inflate(R.layout.dialog_list_layout,null);
//			ListView selectedListView = (ListView) view.findViewById(R.id.selectedListView);
//			ListAdapter adapter = new ArrayAdapter<String>(context, R.layout.item_selected_operation_list, items);
//			selectedListView.setAdapter(adapter);
//			selectedListView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//										int position, long id) {
//					myDialog.dismiss();
//					listener.onItemClick(parent, view, position, id);
//				}
//			});
//		myDialog.setContentView(view, new LayoutParams(
//				ViewGroup.LayoutParams.WRAP_CONTENT,
//				ViewGroup.LayoutParams.WRAP_CONTENT));
//		LayoutParams lp = myDialog.getWindow().getAttributes();
//		lp.gravity = Gravity.CENTER;
//		myDialog.show();
//	}
 
}
