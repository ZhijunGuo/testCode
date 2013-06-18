package com.example.galaxy08.feed;

import java.io.File;

import com.example.galaxy08.R;
import com.example.galaxy08.app.PreferenceWrapper;
import com.example.galaxy08.dao.FeedSessionColumns;
import com.example.galaxy08.dao.GalaxyProviderInfo;
import com.example.galaxy08.entity.Feed;
import com.example.galaxy08.tool.Tool;
import com.example.galaxy08.util.PicUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 发表新动态
 * 郭智军
 */
public class FeedPublishActivity extends Activity implements OnClickListener,TextWatcher,
android.content.DialogInterface.OnClickListener{
	
	public static final String TARGET_FEED = "target_feed"; 
	public static final int FEED_PUSH_NEW_TEXT_NUM_LIMIT = 500; 
	
	private ImageView backBtn;
	private ImageView pushBtn;
	private ImageView contentImage;
	private ImageView addContentImageBtn;
	private ImageView addressBtn;
	
	private ImageView contentImageDelBtn;
	
	private EditText contentEdit;
	
	private TextView addressText;
	
	//存储用户发布的图片的地址信息
	private String imagePath;
	
	//显示剩余的字数
	private TextView numShowText;
	//是否同步到新浪微博
	private CheckBox syncSnsCheck;
	//暂时存储
	private CharSequence tempString;
	
	private final int IMAGE_CAMERA = 0x1100;
	private final int IMAGE_STORE = 0x1101;
	
	private float lon = 0.0f;
	private float lat = 0.0f;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feed_push_new);
		setupView();
		addListener();
	}
	
	private void setupView(){
		backBtn = (ImageView)findViewById(R.id.feed_push_new_back_btn);
		pushBtn = (ImageView)findViewById(R.id.feed_push_new_push_btn);
		contentImage = (ImageView)findViewById(R.id.feed_push_new_content_image);
		addContentImageBtn = (ImageView)findViewById(R.id.feed_push_new_add_image_btn);
		addressBtn = (ImageView)findViewById(R.id.feed_push_new_add_address_btn);
		
		contentEdit = (EditText)findViewById(R.id.feed_push_new_content_edittext);
		addressText = (TextView)findViewById(R.id.feed_push_new_address_text);
	
		contentImageDelBtn = (ImageView)findViewById(R.id.feed_push_new_content_image_del);
	
		contentEdit.addTextChangedListener(this);
		
		//默认情况下 地址信息 和 图片 不显示
		addressText.setVisibility(View.GONE);
		contentImage.setVisibility(View.GONE);
		contentImageDelBtn.setVisibility(View.GONE);
		
		numShowText = (TextView)findViewById(R.id.feed_push_new_text_num);
		
		numShowText.setText(String.valueOf(FEED_PUSH_NEW_TEXT_NUM_LIMIT));
		//是否同步到新浪微博
		syncSnsCheck = (CheckBox)findViewById(R.id.feed_push_new_sync_sns_check);
	}
	
	
	private void addListener(){
		backBtn.setOnClickListener(this);
		pushBtn.setOnClickListener(this);
		contentImage.setOnClickListener(this);
		addContentImageBtn.setOnClickListener(this);
		contentImageDelBtn.setOnClickListener(this);
		addressBtn.setOnClickListener(this);
		numShowText.setOnClickListener(this);
	}
	
	/**
	 * 发布新feed
	 * 分享类型一个三种：观点、图片、网址。这三种分享共用一个接口，
	 * 并以参数type区分，返回值格式一致
	 */
	private void pushFeed(){
		//1:观点 2:网址 3:图片
		int type = 1;
		int hasPos = 0;
		String address = null;
		//判断有没有图片信息
		if(contentImage.isShown()){
			type = 3;
		}else{
		}
		//判断有没有 位置信息
		if(addressText.isShown()){
			hasPos = 1;
			address = addressText.getText().toString().trim();
		}else{
		}
		String content = contentEdit.getText().toString().trim();
		
		if(type==1&&(content==null||"".equals(content))){
			Toast.makeText(this, getString(R.string.no_content), Toast.LENGTH_SHORT).show();
			return;
		}
		
		/**
		 * 生成一条新的feed信息 插入到数据库中
		 * 
		 */
		
		Feed f = new Feed();
		String userid = PreferenceWrapper.get(PreferenceWrapper.USER_ID, "");
		f.setPublisher_id(userid);
		f.setPublisher_name("智军");
		f.setUserid(userid);
		f.setContent(content);
		f.setTime(Tool.nowTime());
		f.setFeedsession_id("1002");//高级管理
		/**
		 * 新的feed信息插入到数据库中
		 */
		getContentResolver().insert(GalaxyProviderInfo.FEED_URI,
				f.getContentValues());
		/**
		 * 更新 feedsession 数据库中 该feed群组的最后更新时间
		 */
		ContentValues cv = new ContentValues();
		cv.put(FeedSessionColumns.LASTUPDATE, Tool.nowTime());
		getContentResolver().update(GalaxyProviderInfo.FEEDSESSION_URI, 
				cv, FeedSessionColumns.USERID+"=? and "+
						FeedSessionColumns.FEEDSID+"=?", 
						new String[]{
				userid,"1002"
		});
		
		this.finish();
		
//		JwMobileApi.Feeds.addFeed(getUserId(),content, String.valueOf(type), imagePath, 
//				hasPos,lon,lat,address, new BaseJsonHandler<BaseResponse>() {
//
//					@Override
//					public void onStatusOk(BaseResponse response) {
//						Toast.makeText(FeedPublishActivity.this, getString(R.string.push_success), Toast.LENGTH_SHORT).show();
//						FeedPublishActivity.this.finish();
//					}
//					
//					@Override
//					public void onStatusFail(BaseResponse response) {
//						super.onStatusFail(response);
//						Toast.makeText(FeedPublishActivity.this, 
//								getString(R.string.push_fail)+response.getMessage(), Toast.LENGTH_SHORT).show();
//					}
//					
//				});
	}

//	private void startLocatePosition(){
//		
//		RenRenLocationManager   mLocate = RenRenLocationManager.getInstance(getApplicationContext());
//		mLocate.startLocateSingle(this);
//	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.feed_push_new_back_btn:
			this.finish();
			return ;
		case R.id.feed_push_new_push_btn:
			pushFeed();
			break;
		case R.id.feed_push_new_add_address_btn:
			//startLocatePosition();
			Toast.makeText(this, "获取位置信息,请稍等", Toast.LENGTH_SHORT).show();
			break;
		case R.id.feed_push_new_add_image_btn:
			new AlertDialog.Builder(this).setItems(new String[]{
					"相机",
					"相册"
			}, new OnImageSelectBtnClick()).setTitle("来源").show();
			break;
		case R.id.feed_push_new_content_image:
			
			break;
		case R.id.feed_push_new_content_image_del:
			//删除内容图片
			contentImage.setVisibility(View.GONE);
			contentImageDelBtn.setVisibility(View.GONE);
			break;

		case R.id.feed_push_new_text_num:
			if(Integer.valueOf(numShowText.getText().toString().trim()
					)<FEED_PUSH_NEW_TEXT_NUM_LIMIT){
				new AlertDialog.Builder(this).setMessage("删除所有文字?")
				.setPositiveButton("确定", this)
				.setNegativeButton("取消", null).show();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IMAGE_CAMERA://相机
			if(resultCode==RESULT_OK){
				//先保存 后提取 为什么图片总是旋转了90度？？
				Bitmap bitmap = PicUtil.optimizeBitmapByFile(new File(Environment.getExternalStorageDirectory()+"/image.jpg"),60,60);
				contentImage.setImageBitmap(bitmap);
				//bitmap.recycle(); 
				Cursor c = getApplicationContext().getContentResolver().query(
						Media.EXTERNAL_CONTENT_URI,
						new String[] { Images.Media._ID, Images.Media.DATA },
						null, null, Images.Media._ID + " desc limit 1");
				if (c != null) {
					c.moveToFirst();
				}
				imagePath = c.getString(c.getColumnIndex(Images.Media.DATA));
				contentImage.setVisibility(View.VISIBLE);
				contentImageDelBtn.setVisibility(View.VISIBLE);
			}
			break;
		case IMAGE_STORE://相册
			if(resultCode==RESULT_OK){
				
				contentImage.setImageURI(data.getData());
				
				Uri uri = data.getData();
				
				String[] proj = { MediaStore.Images.Media.DATA };
				
				Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null,
						null);

				int actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				actualimagecursor.moveToFirst();

				imagePath = actualimagecursor
						.getString(actual_image_column_index);
				
				contentImage.setVisibility(View.VISIBLE);
				contentImageDelBtn.setVisibility(View.VISIBLE);
			}
			break;

		default:
			break;
		}
		
	}

//	@Override
//	public void onLocatedFailed(RRException arg0) {
//		
//	}
//
//	@Override
//	public void onLocationSucceeded(RenRenLocation location) {
//		if(location != null && TextUtils.isEmpty(location.address)){
//			addressText.setVisibility(View.VISIBLE);
//			addressText.setText(location.address);
//			lon = (float) location.longitude;
//			lat = (float) location.latitude;
//			Toast.makeText(this, "位置:"+location.address+"Name:"+location.poiName, 
//					Toast.LENGTH_SHORT).show();
//		}
//	}
	
	class OnImageSelectBtnClick implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:
				Toast.makeText(FeedPublishActivity.this, "相机", Toast.LENGTH_SHORT).show();
				Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));  
				Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(imageIntent, IMAGE_CAMERA);
				break;
			case 1:
				Toast.makeText(FeedPublishActivity.this, "相册", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, IMAGE_STORE);
				break;

			default:
				break;
			}
		}
		
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		
		int num = FEED_PUSH_NEW_TEXT_NUM_LIMIT;
		if(tempString!=null){
			num-=tempString.length();
		}
		numShowText.setText(String.valueOf(num));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		tempString = s;
	}

	@Override
	public void onClick(DialogInterface arg0, int which) {
		switch (which) {
		case AlertDialog.BUTTON_POSITIVE:
			contentEdit.setText("");
			break;

		default:
			break;
		}
	}
	
}
