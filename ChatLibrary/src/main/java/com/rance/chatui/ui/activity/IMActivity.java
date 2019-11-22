package com.rance.chatui.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.labo.kaji.relativepopupwindow.RelativePopupWindow;
import com.rance.chatui.R;
import com.rance.chatui.adapter.ChatAdapter;
import com.rance.chatui.adapter.CommonFragmentPagerAdapter;
import com.rance.chatui.base.MyApplication;
import com.rance.chatui.enity.FullImageInfo;
import com.rance.chatui.enity.Link;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.ui.fragment.ChatEmotionFragment;
import com.rance.chatui.ui.fragment.ChatFunctionFragment;
import com.rance.chatui.util.Constants;
import com.rance.chatui.util.GlobalOnItemClickManagerUtils;
import com.rance.chatui.util.MediaManager;
import com.rance.chatui.util.MessageCenter;
import com.rance.chatui.widget.ChatContextMenu;
import com.rance.chatui.widget.EmotionInputDetector;
import com.rance.chatui.widget.NoScrollViewPager;
import com.rance.chatui.widget.StateButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class IMActivity extends AppCompatActivity {
    private static final String TAG = "IMActivity";
    RecyclerView chatList;
    ImageView emotionVoice;
    EditText editText;
    TextView voiceText;
    ImageView emotionButton;
    ImageView emotionAdd;
    StateButton emotionSend;
    NoScrollViewPager viewpager;
    RelativeLayout emotionLayout;
    private TextView tv_title;
    private ImageView img_back;

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<MessageInfo> messageInfos;
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;
    private int headImg;
    private String title="";
    private Context mContext;
    private int type;//0 单聊  1 群聊  2 公众号 3...
    private String name=null;
    private int head_image=0;
    private int myHeadImg=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);
        mContext=this;
        Intent intent=getIntent();
        headImg=intent.getIntExtra("headImg",0);
        myHeadImg=intent.getIntExtra("myHeadImg",0);
        title=intent.getStringExtra("title");
        type=intent.getIntExtra("type",0);
        findViewByIds();
        EventBus.getDefault().register(this);
        initWidget();
        handleIncomeAction();
    }

    private void findViewByIds() {
        chatList = (RecyclerView) findViewById(R.id.chat_list);
        emotionVoice = (ImageView) findViewById(R.id.emotion_voice);
        editText = (EditText) findViewById(R.id.edit_text);
        voiceText = (TextView) findViewById(R.id.voice_text);
        emotionButton = (ImageView) findViewById(R.id.emotion_button);
        emotionAdd = (ImageView) findViewById(R.id.emotion_add);
        emotionSend = (StateButton) findViewById(R.id.emotion_send);
        emotionLayout = (RelativeLayout) findViewById(R.id.emotion_layout);
        viewpager = (NoScrollViewPager) findViewById(R.id.viewpager);
        img_back= (ImageView) findViewById(R.id.img_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(type==1){
            int count = new Random().nextInt(13);
            if(count<5){
                count=5;
            }
            tv_title.setText(title+"("+count+")");

            final int finalCount = count;
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d(TAG, "beforeTextChanged: "+charSequence);
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Log.d(TAG, "onTextChanged: "+charSequence+"i=="+i+"   i1==="+i1+"    i2==="+i2);

                    try {
                        if(charSequence.length()<1){
                            return;
                        }
                        String content =charSequence.toString();
                        String s=  content.substring(content.length()-1);
                        Log.d(TAG, "onTextChanged: s=="+s);
                        if(s.equals("@")){
                            Class c = Class.forName("com.yts.wechatdemo.GroupMemberListActivity");
                            Intent intent =new Intent(mContext,c);
                            intent.putExtra("count", finalCount);
                            startActivityForResult(intent,1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        }else {
            tv_title.setText(title);
        }



    }

    private void handleIncomeAction() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }

        MessageCenter.handleIncoming(bundle, getIntent().getType(), this);
    }

    private void initWidget() {
        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();

        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);

        chatAdapter = new ChatAdapter(messageInfos);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        chatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        mDetector.hideEmotionLayout(false);
                        mDetector.hideSoftInput();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
        LoadData();
    }

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
            Toast.makeText(IMActivity.this, "onHeaderClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageClick(View view, int position) {
            Log.d(TAG, "onImageClick: ");
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            FullImageInfo fullImageInfo = new FullImageInfo();
            fullImageInfo.setLocationX(location[0]);
            fullImageInfo.setLocationY(location[1]);
            fullImageInfo.setWidth(view.getWidth());
            fullImageInfo.setHeight(view.getHeight());
            fullImageInfo.setImageUrl(messageInfos.get(position).getFilepath());
            EventBus.getDefault().postSticky(fullImageInfo);
            startActivity(new Intent(IMActivity.this, FullImageActivity.class));
            overridePendingTransition(0, 0);
        }

        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            Log.d(TAG, "onVoiceClick: ");

            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (animView != null) {
                        animView.setImageResource(res);
                        animView = null;
                    }
                    switch (messageInfos.get(position).getType()) {
                        case 1:
                            animationRes = R.drawable.voice_left;
                            res = R.mipmap.icon_voice_left3;
                            break;
                        case 2:
                            animationRes = R.drawable.voice_right;
                            res = R.mipmap.icon_voice_right3;
                            break;
                    }
                    animView = imageView;
                    animView.setImageResource(animationRes);
                    animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    animationDrawable.start();
                    MediaManager.playSound(messageInfos.get(position).getFilepath(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Log.d(TAG, "onCompletion: 播放完成");
                            animView.setImageResource(res);
                        }
                    });
                }
            });

        }

        @Override
        public void onFileClick(View view, int position) {
            Log.d(TAG, "onFileClick: ");
            MessageInfo messageInfo = messageInfos.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            File file = new File(messageInfo.getFilepath());
            Uri fileUri = FileProvider.getUriForFile(IMActivity.this, Constants.AUTHORITY, file);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setDataAndType(fileUri, messageInfo.getMimeType());
            startActivity(intent);
        }

        @Override
        public void onLinkClick(View view, int position) {
            Log.d(TAG, "onLinkClick: ");
            MessageInfo messageInfo = messageInfos.get(position);
            Link link = (Link) messageInfo.getObject();
            Uri uri = Uri.parse(link.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        @Override
        public void onLongClickImage(View view, int position) {
            Log.d(TAG, "onLongClickImage: ");
            ChatContextMenu chatContextMenu = new ChatContextMenu(view.getContext(),messageInfos.get(position));
//            chatContextMenu.setAnimationStyle();
            chatContextMenu.showOnAnchor(view, RelativePopupWindow.VerticalPosition.ABOVE,
                    RelativePopupWindow.HorizontalPosition.CENTER);

        }

        @Override
        public void onLongClickText(View view, int position) {
            Log.d(TAG, "onLongClickText: ");
            ChatContextMenu chatContextMenu = new ChatContextMenu(view.getContext(),messageInfos.get(position));
            chatContextMenu.showOnAnchor(view, RelativePopupWindow.VerticalPosition.ABOVE,
                    RelativePopupWindow.HorizontalPosition.CENTER);
        }

        @Override
        public void onLongClickItem(View view, int position) {
            Log.d(TAG, "onLongClickItem: ");
            ChatContextMenu chatContextMenu = new ChatContextMenu(view.getContext(),messageInfos.get(position));
            chatContextMenu.showOnAnchor(view, RelativePopupWindow.VerticalPosition.ABOVE,
                    RelativePopupWindow.HorizontalPosition.CENTER);
        }

        @Override
        public void onLongClickFile(View view, int position) {
            Log.d(TAG, "onLongClickFile: ");
            ChatContextMenu chatContextMenu = new ChatContextMenu(view.getContext(),messageInfos.get(position));
            chatContextMenu.showOnAnchor(view, RelativePopupWindow.VerticalPosition.ABOVE,
                    RelativePopupWindow.HorizontalPosition.CENTER);
        }

        @Override
        public void onLongClickLink(View view, int position) {
            Log.d(TAG, "onLongClickLink: ");
            ChatContextMenu chatContextMenu = new ChatContextMenu(view.getContext(),messageInfos.get(position));
            chatContextMenu.showOnAnchor(view, RelativePopupWindow.VerticalPosition.ABOVE,
                    RelativePopupWindow.HorizontalPosition.CENTER);
        }
    };

    /**
     * 构造聊天数据
     */
    private void LoadData() {
        int headUrl=0;
        if(headImg==0){
            headUrl=0;
        }else {
            headUrl=headImg;
        }

        messageInfos = new ArrayList<>();

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent("你好，欢迎使用Rance的聊天界面框架");
        messageInfo.setFileType(Constants.CHAT_FILE_TYPE_TEXT);
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_LEFT);
        messageInfo.setHeader(headUrl+"");
        messageInfos.add(messageInfo);

        MessageInfo messageInfo1 = new MessageInfo();
        // messageInfo1.setFilepath("http://www.trueme.net/bb_midi/welcome.wav");
        //Uri uri=Uri.parse("android:resource://"+getPackageName()+"/R.raw.test");
        //  String uri = "android.resource://" + getPackageName() + "/" + R.raw.test;
        //  messageInfo1.setFilepath(uri);
        messageInfo1.setVoiceTime(3000);
        messageInfo1.setFileType(Constants.CHAT_FILE_TYPE_VOICE);
        messageInfo1.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo1.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);

        messageInfo1.setHeader(myHeadImg+"");
        messageInfos.add(messageInfo1);

        MessageInfo messageInfo2 = new MessageInfo();
        messageInfo2.setFilepath("http://img4.imgtn.bdimg.com/it/u=1800788429,176707229&fm=21&gp=0.jpg");
        messageInfo2.setFileType(Constants.CHAT_FILE_TYPE_IMAGE);
        messageInfo2.setType(Constants.CHAT_ITEM_TYPE_LEFT);
        messageInfo2.setHeader(headUrl+"");
        messageInfos.add(messageInfo2);

        MessageInfo messageInfo3 = new MessageInfo();
        messageInfo3.setContent("[微笑][色][色][色]");
        messageInfo3.setFileType(Constants.CHAT_FILE_TYPE_TEXT);
        messageInfo3.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo3.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
        messageInfo3.setHeader(myHeadImg+"");
        messageInfos.add(messageInfo3);
        chatAdapter.addAll(messageInfos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final MessageInfo messageInfo) {
        messageInfo.setHeader(myHeadImg+"");
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        messageInfos.add(messageInfo);
        chatAdapter.notifyItemInserted(messageInfos.size() - 1);
//        chatAdapter.add(messageInfo);
        chatList.scrollToPosition(chatAdapter.getItemCount() - 1);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                chatAdapter.notifyDataSetChanged();
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(head_image!=0&&name!=null){
                    MessageInfo message = new MessageInfo();
                    message.setContent("这是模拟@消息回复   @我干嘛");
                    message.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                    message.setFileType(Constants.CHAT_FILE_TYPE_TEXT);
                    message.setHeader(head_image+"");
                    messageInfos.add(message);
                    chatAdapter.notifyItemInserted(messageInfos.size() - 1);
                    chatList.scrollToPosition(chatAdapter.getItemCount() - 1);
                    head_image=0;
                    name=null;
                }else {
                    MessageInfo message = new MessageInfo();
                    message.setContent("这是模拟消息回复");
                    message.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                    message.setFileType(Constants.CHAT_FILE_TYPE_TEXT);
                    message.setHeader(headImg+"");
                    messageInfos.add(message);
                    chatAdapter.notifyItemInserted(messageInfos.size() - 1);
                    chatList.scrollToPosition(chatAdapter.getItemCount() - 1);
                }

            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if(resultCode==2&&data!=null){
            String s=editText.getText().toString();
             name = data.getStringExtra("name");
            head_image=data.getIntExtra("image",0);
            Log.d(TAG, "onActivityResult: image="+head_image);
            editText.setText(s+name);
        }
    }
}
