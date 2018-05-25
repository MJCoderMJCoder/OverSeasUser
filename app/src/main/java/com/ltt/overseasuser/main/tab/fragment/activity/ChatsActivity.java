package com.ltt.overseasuser.main.tab.fragment.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.base.Constants;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.adapter.ChatRecycleViewAdapter;
import com.ltt.overseasuser.main.tab.fragment.adapter.ChatRequestsAdapter;
import com.ltt.overseasuser.model.ChatMessageBean;
import com.ltt.overseasuser.model.ViewRequestBean;
import com.ltt.overseasuser.utils.AscKeyComparator;
import com.ltt.overseasuser.utils.FileUtils;
import com.ltt.overseasuser.utils.L;
import com.ltt.overseasuser.utils.ToastUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by yunwen on 2018/5/8.
 */

public class ChatsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.action_bar)
    LinearLayout actionBar;
    @BindView(R.id.recyclerview_chat)
    RecyclerView mRecyclerviewChat;
    @BindView(R.id.btn_up)
    Button mBtnUp;
    @BindView(R.id.et_sendmessage)
    EditText mEtSendmessage;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    @BindView(R.id.bt_photo)
    Button mBtPhoto;
    @BindView(R.id.bt_file)
    Button mBtFile;
    @BindView(R.id.ll_up)
    LinearLayout mLlUp;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.bt_requestdetails)
    Button btRequestdetails;
    private ActionBar bar;
    private DatabaseReference mDatabaseReference;
    //登录成功码
    private static final int SIGN_IN_REQUEST_CODE = 1001;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1002;
    //文件请求码
    private static final int FILE_REQUEST_CODE = 1003;

    private final int PERMISSION_INTGER = 1004;

    private Animation mAnimation_bottom;
    private Animation mAnimation_top;
    //是否第一次点击
    private boolean isFirst = true;
    private ChatRecycleViewAdapter mAdapter;
    //用于存储信息
    List<ChatMessageBean.MessageBean> listmessage = new ArrayList<>();
    //用于展示信息
    List<ChatMessageBean.MessageBean> showlistmessages = new ArrayList<>();
    //用于存储用户信息
    ChatMessageBean.MembersBean membersBean = new ChatMessageBean.MembersBean();
    private int index = 1;

    private Uri fileUri = null;
    private DataSnapshot mNext2;

    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private View view;
    private PopupWindow popupWindow;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_chats;
    }

    @Override
    protected void prepareActivity() {
        FirebaseApp.initializeApp(this);
        VerifyLogin();
        setChatActionBar();
        setRefresh();
        initView();
        initMessageData();

    }

    private void writeNewUser(String channel_type, String requester, String service_provider) {
        List<ChatMessageBean.MessageBean> messageLists = new ArrayList<>();
        ChatMessageBean.MembersBean membersBean = new ChatMessageBean.MembersBean(requester, service_provider);
        ChatMessageBean user = new ChatMessageBean(channel_type, messageLists, membersBean);
        mDatabaseReference.child("conversations").push().setValue(user.toMap());
    }

    /**
     * 刷新界面信息
     */
    private void setRefresh() {
        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index += 1;
                        showlistmessages.clear();
                        if (listmessage.size() < index * 10) {
                            showlistmessages.addAll(listmessage.subList(0, listmessage.size()));
                        } else {
                            showlistmessages.addAll(listmessage.subList(listmessage.size() - index * 10, listmessage.size()));
                        }
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(ChatsActivity.this, "刷新了数据", Toast.LENGTH_SHORT).show();
                        // 加载完数据设置为不刷新状态，将下拉进度收起来
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
    }

    private void initView() {
        mAnimation_bottom = AnimationUtils.loadAnimation(this, R.anim.rotate_button_bottom);
        mAnimation_top = AnimationUtils.loadAnimation(this, R.anim.rotate_button_top);
        mBtnUp.startAnimation(mAnimation_bottom);
        mBtnUp.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtPhoto.setOnClickListener(this);
        mBtFile.setOnClickListener(this);
        btRequestdetails.setOnClickListener(this);
        mAdapter = new ChatRecycleViewAdapter(ChatsActivity.this, showlistmessages, membersBean);

        mRecyclerviewChat.setAdapter(mAdapter);
//        mAdapter.set_interface_LikeQuesstion(this);
//        mAdapter.setOnRecycleViewisSpeakingClickListenter(this);
//        mAdapter.set_interface_LikeOtherQuesstion(this);
        mRecyclerviewChat.setLayoutManager(new LinearLayoutManager(ChatsActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerviewChat.setItemAnimator(new DefaultItemAnimator());
        mRecyclerviewChat.setHasFixedSize(true);

    }

    /**
     * 验证登录
     */
    private void VerifyLogin() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ToastUtils.showToast("Not logged in Google account.");
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
            displayChatMessages();
        }
    }

    private void displayChatMessages() {
        //Gets the current login google user information.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        L.e(TAG, currentUser.toString() + "--" + currentUser.getUid() + "---" + currentUser.getEmail() + "---" + currentUser.getDisplayName() + "---" +
                currentUser.getPhoneNumber() + "---" + currentUser.getProviderId() + "---");
        List<? extends UserInfo> providerData = currentUser.getProviderData();
        for (int i = 0; i < providerData.size(); i++) {
            L.e(TAG, providerData.get(i).getUid() + "---" + providerData.get(i).getEmail());
        }
        /**The new session*/
        //writeNewUser("service", "requester_uid", "service_provider_uid");
    }

    /**
     * 系统回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successfully signed in. Welcome!", Toast.LENGTH_LONG).show();
                initMessageData();
                displayChatMessages();
            } else {
                Toast.makeText(this, "We couldn't sign you in. Please try again later.", Toast.LENGTH_LONG).show();
                // Close the app
                finish();
            }
        } else if (requestCode == ALBUM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String path = null;
                try {
                    path = FileUtils.getPath(getApplicationContext(), uri);

                    //上传图片资源到firebase
                    upFiletoFirebase("images", path);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Log.e("Picture address", "---" + uri + "---" + path);
            }
        } else if (requestCode == FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String path = FileUtils.getFileAbsolutePath(ChatsActivity.this, uri);
                //上传文件到firebase
                if (path == null) {
                    ToastUtils.showToast("The file format is not correct.");
                } else {
                    upFiletoFirebase("files", path);
                }
                Log.e("Address of the file", "---" + uri + "---" + path + "---");
            }
        }
    }

    /**
     * 获取聊天记录信息
     */
    private void initMessageData() {
//        String conversation_id = getIntent().getStringExtra("conversation_id");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                L.e(TAG, "onDataChange");
                //ChatMsgListBean value = dataSnapshot.getValue(ChatMsgListBean.class);
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                DataSnapshot next = children.iterator().next();
                mNext2 = next.getChildren().iterator().next();
                Object listmessages = dataSnapshot.child("conversations").child(mNext2.getKey()).child("list_message").getValue();
                Object members = dataSnapshot.child("conversations").child(mNext2.getKey()).child("members").getValue();
                Map<String, Object> listmessagesMap = (HashMap<String, Object>) listmessages;
                HashMap<String, Object> membersMap = (HashMap<String, Object>) members;
                listmessage.addAll(AscKeyComparator.AscMap(listmessagesMap));
                membersBean.setRequester((String) membersMap.get(Constants.REQUESTER));
                membersBean.setService_provider((String) membersMap.get(Constants.SERVICE_PROVIDER));
                showlistmessages.addAll(listmessage.subList(listmessage.size() - index * 10, listmessage.size()));
                L.e(TAG + "---" + "重复几次了~~~~" + members + "---" + membersBean.getRequester() + "---" + membersBean.getService_provider());
                mAdapter.notifyDataSetChanged();
                mRecyclerviewChat.smoothScrollToPosition(mAdapter.getTotalCount());
                L.e(TAG, "---" + listmessage.size() + "---" + listmessage + "---\n" + mNext2.getKey() + "---\n" + next.getValue());
                for (int i = 0; i < listmessage.size(); i++) {
                    L.e(TAG, listmessage.get(i).getSenderName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                L.e(TAG, "onCancelled" + databaseError.getMessage());
            }
        });
    }


    /**
     * 设置标题栏内容
     */
    private void setChatActionBar() {
        String username = getIntent().getStringExtra("username");
        String request_category = getIntent().getStringExtra("request_category");
        bar = ActionBar.init(actionBar);
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bar.setLeft2(R.mipmap.user);
        bar.showNotify();
        bar.showRlTitle();
        if (username == null) {
            bar.setTop("test");
        } else {
            bar.setTop(username);
        }
        if (request_category == null) {
            bar.setBottom("tests");
        } else {
            bar.setBottom(request_category);
        }

    }


    @OnClick({R.id.iv_notify, R.id.btn_up, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_notify:
                startActivity(new Intent(ChatsActivity.this, NotificationActivity.class));
                finish();
                break;
            case R.id.btn_up:
                if (isFirst) {
                    mBtnUp.startAnimation(mAnimation_top);
                    showInputMenu();
                    isFirst = false;
                } else {
                    mBtnUp.startAnimation(mAnimation_bottom);
                    mLlUp.setVisibility(View.GONE);
                    isFirst = true;
                }
                break;
            case R.id.btn_send:
                String message = mEtSendmessage.getText().toString();
                updateMessage(message, "text");
                mEtSendmessage.setText("");
                break;
            case R.id.bt_photo:
                setPermission("image");
                break;
            case R.id.bt_file:
                setPermission("file");
                break;
            case R.id.bt_requestdetails:
                getViewRequest();
                break;
        }
    }

    private void getViewRequest() {
        Call<ViewRequestBean> call = RetrofitUtil.getAPIService().getQuestions("2");
        call.enqueue(new CustomerCallBack<ViewRequestBean>() {
            @Override
            public void onResponseResult(ViewRequestBean response) {
                dismissLoadingView();
                ViewRequestBean.DataBean data = response.getData();
                showPoupView(data);

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                dismissLoadingView();
            }

        });
    }

    private void showPoupView(ViewRequestBean.DataBean data) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.chat_detail_popupview, null);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(rlChatall);
            TextView tv_requests = (TextView) view.findViewById(R.id.tv_requests);
            TextView tv_date_created = (TextView)  view.findViewById(R.id.tv_date_created);
            TextView tv_user = (TextView)  view.findViewById(R.id.tv_user);
            RecyclerView chat_request_recyclerviews = (RecyclerView)  view.findViewById(R.id.chat_request_recyclerview);
            List<ViewRequestBean.DataBean.QuestionsBean> questions = data.getQuestions();
            tv_requests.setText(data.getRequest());
            tv_date_created.setText("2018-5-25 09:01:25");
            tv_user.setText(data.getUser());
            L.e(TAG, "---------" + questions.size() + "---" + questions);

            ChatRequestsAdapter groupAdapter = new ChatRequestsAdapter(this, questions);
            chat_request_recyclerviews.setAdapter(groupAdapter);
            chat_request_recyclerviews.setLayoutManager(new LinearLayoutManager(ChatsActivity.this, LinearLayoutManager.VERTICAL, false));
            chat_request_recyclerviews.setItemAnimator(new DefaultItemAnimator());
            chat_request_recyclerviews.setHasFixedSize(true);

            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 800, 1200);
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth()/2
                - popupWindow.getWidth()/2;
        Log.i("coder", "xPos:" + xPos);

        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0,0);

    }

    /**
     * 请求相关权限
     *
     * @param type
     */
    private void setPermission(String type) {
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()));
        if (permission) {
            if ("image".equals(type)) {
                openImageMessage();
            } else {
                openFileMessage();
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_INTGER);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_INTGER) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults.length > 0) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                        Toast.makeText(this, "权限授权失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void updateMessage(String message, String type) {
        //ChatMessageBean chatMessageBean = new ChatMessageBean();
        ChatMessageBean.MessageBean messageBean = new ChatMessageBean.MessageBean();
        //ChatMessageBean.MembersBean membersBeans = new ChatMessageBean.MembersBean();
        messageBean.setCreatedAt(new Date().getTime());
        messageBean.setMessage(message);
        messageBean.setSenderId(membersBean.getRequester());
        messageBean.setSenderName("Popmach Asia");
        messageBean.setType(type);
        showlistmessages.add(messageBean);
        mAdapter.notifyDataSetChanged();
        mRecyclerviewChat.smoothScrollToPosition(mAdapter.getTotalCount());
        //chatMessageBean.setList_message(showlistmessages);

        //添加发送信息到firebase后台数据库
        // TODO: 2018/5/10
//        if (mNext2 != null) {
//            mDatabaseReference.child("conversations").child(mNext2.getKey()).child("list_message").push().setValue(messageBean.toMap());
//        }

    }

    /**
     * 打开选择本地文件
     */
    private void openFileMessage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    /**
     * 打开选择本地图片
     */
    private void openImageMessage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    /**
     * 上传图片或文件到firebase存储
     */
    private void upFiletoFirebase(final String classes, String uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        // File or Blob
        //String s = Environment.getExternalStorageDirectory() + "/aimagetest/hand.jpg";
        Uri file = Uri.fromFile(new File(uri));
        // Create the file metadata
        StorageMetadata metadata;
        if ("file".equals(classes)) {
            metadata = new StorageMetadata.Builder()
                    .setContentType("file/*")
                    .build();
        } else {
            metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build();
        }
        // Upload file and metadata to the path 'images/mountains.jpg'
        UploadTask uploadTask = storageRef.child(classes + "/" + mNext2.getKey() + "/" + file.getLastPathSegment()).putFile(file, metadata);
        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                fileUri = downloadUrl;
                L.e("文件上传成功地址", "--" + downloadUrl);
                if ("files".equals(classes)) {
                    updateMessage(fileUri + "", "file");
                } else {
                    updateMessage(fileUri + "", "image");
                }

            }
        });

//        .addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
//                System.out.println("Upload is paused");
//            }
//        })
    }

    private void showInputMenu() {
        mLlUp.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
