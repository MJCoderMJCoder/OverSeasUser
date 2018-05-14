package com.ltt.overseasuser.main.tab.fragment.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.ltt.overseasuser.base.Constants;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.main.tab.fragment.adapter.ChatRecycleViewAdapter;
import com.ltt.overseasuser.model.ChatMessageBean;

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

import static android.app.Activity.RESULT_OK;

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
    private ActionBar bar;
    private DatabaseReference mDatabaseReference;
    //登录成功码
    private static final int SIGN_IN_REQUEST_CODE = 1001;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1002;
    //文件请求码
    private static final int FILE_REQUEST_CODE = 1003;
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

    /**刷新界面信息*/
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
            //startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
            displayChatMessages();
        }
    }

    private void displayChatMessages() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        L.e(TAG,currentUser.toString() + "--" + currentUser.getUid() + "---" + currentUser.getEmail() + "---" + currentUser.getDisplayName() + "---" +
                currentUser.getPhoneNumber() + "---" + currentUser.getProviderId() + "---" );
        List<? extends UserInfo> providerData = currentUser.getProviderData();
        for (int i = 0; i < providerData.size(); i++) {
            L.e(TAG,providerData.get(i).getUid() + "---" + providerData.get(i).getEmail());
        }
    }

    /**系统回调*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Successfully signed in. Welcome!", Toast.LENGTH_LONG).show();
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
                Log.e("图片地址", "---" + uri + "---" +  path);
            }
        } else if (requestCode == FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String path = null;
                try {
                    path = FileUtils.getPath(getApplicationContext(), uri);
                    //上传文件到firebase
                    if (path == null) {
                        ToastUtils.showToast("The file format is not correct.");
                    } else {
                        upFiletoFirebase("files", path);
                    }

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Log.e("文件地址", "---" + uri + "---" + path);
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
                openImageMessage();
                break;
            case R.id.bt_file:
                openFileMessage();
                break;
        }
    }

    private void updateMessage(String message, String type) {
        //ChatMessageBean chatMessageBean = new ChatMessageBean();
        ChatMessageBean.MessageBean messageBean = new ChatMessageBean.MessageBean();
        //ChatMessageBean.MembersBean membersBeans = new ChatMessageBean.MembersBean();
        messageBean.setCreatedAt(new Date().getTime());
        messageBean.setMessage(message);
        messageBean.setSenderId(membersBean.getService_provider());
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

    /**打开选择本地文件*/
    private void openFileMessage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,FILE_REQUEST_CODE);
    }

    /**打开选择本地图片*/
    private void openImageMessage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    /**上传图片或文件到firebase存储*/
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