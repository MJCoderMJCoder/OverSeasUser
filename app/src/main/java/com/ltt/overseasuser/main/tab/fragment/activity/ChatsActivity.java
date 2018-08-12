package com.ltt.overseasuser.main.tab.fragment.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ltt.overseasuser.R;
import com.ltt.overseasuser.base.BaseActivity;
import com.ltt.overseasuser.base.BaseBean;
import com.ltt.overseasuser.base.Constants;
import com.ltt.overseasuser.core.ActionBar;
import com.ltt.overseasuser.http.CustomerCallBack;
import com.ltt.overseasuser.http.RetrofitUtil;
import com.ltt.overseasuser.main.tab.fragment.adapter.ChatRecycleViewAdapter;
import com.ltt.overseasuser.main.tab.fragment.adapter.ChatRequestsAdapter;
import com.ltt.overseasuser.model.AttachmentFileBean;
import com.ltt.overseasuser.model.ChatMessageBean;
import com.ltt.overseasuser.model.ChatMessagesBean;
import com.ltt.overseasuser.model.ExploreQuestionListBean;
import com.ltt.overseasuser.model.ViewRequestBean;
import com.ltt.overseasuser.utils.AscKeyComparator;
import com.ltt.overseasuser.utils.FileUtils;
import com.ltt.overseasuser.utils.L;
import com.ltt.overseasuser.utils.SPUtils;
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

    LinearLayout lyRequestList;


    private ActionBar bar;
    private DatabaseReference mDatabaseReference;
    //Login success code
    private static final int SIGN_IN_REQUEST_CODE = 1001;
    //Album request code
    private static final int ALBUM_REQUEST_CODE = 1002;
    //File request code
    private static final int FILE_REQUEST_CODE = 1003;

    private final int PERMISSION_INTGER = 1004;
    private Animation mAnimation_bottom;
    private Animation mAnimation_top;
    //Whether the first click
    private boolean isFirst = true;
    private ChatRecycleViewAdapter mAdapter;
    //Used to store information
    List<ChatMessageBean.MessageBean> listmessage = new ArrayList<>();
    //Used to display information
    List<ChatMessageBean.MessageBean> showlistmessages = new ArrayList<>();
    //Used to store user information
    ChatMessageBean.MembersBean membersBean = new ChatMessageBean.MembersBean();
    private int index = 1;

    private Uri fileUri = null;
    private DataSnapshot mNext2;

    // Permission to apply
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private PopupWindow popupWindow;
    private View view;
    private String date_created;
    private String conversation_id;
    private String username;
    private LayoutInflater mlflater;
    private FirebaseAuth mAuth;
    private String opposite_user;

    @Override
    protected int bindLayoutID() {
        return R.layout.activity_chats;
    }

    @Override
    protected void prepareActivity() {
        FirebaseApp.initializeApp(this);
        setChatActionBar();
        VerifyLogin();
        mlflater = getLayoutInflater().from(ChatsActivity.this);
        setRefresh();
        initView();
//        initMessageData();


    }

    private void writeNewUser(String channel_type, String requester, String service_provider) {
        List<ChatMessageBean.MessageBean> messageLists = new ArrayList<>();
        ChatMessageBean.MembersBean membersBean = new ChatMessageBean.MembersBean(requester, service_provider);
        ChatMessageBean user = new ChatMessageBean(channel_type, messageLists, membersBean);
        mDatabaseReference.child("conversations").push().setValue(user.toMap());
    }

    /**
     * Refresh interface information
     */
    private void setRefresh() {
        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // Set the theme color of the drop-down progress
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        // Trigger SwipeRefreshLayout pulldown animation when pulled down, this method will be called back after the animation is finished
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
                        Toast.makeText(ChatsActivity.this, "Refreshed data", Toast.LENGTH_SHORT).show();
                        // The loaded data is set to not refresh state, and the pull-down progress is collected.
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
     * Verify the login
     */
    private void VerifyLogin() {
        mAuth = FirebaseAuth.getInstance();
        String sign_in_custom_token = SPUtils.getString("SIGN_IN_CUSTOM_TOKEN", "");

        mAuth.signInWithCustomToken(sign_in_custom_token)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.e("usermsg", user.toString() + "--" + user.getEmail() + "---" + user.getUid());
                            initMessageData();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(ChatsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
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
     * System callback
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

                    //Upload image resources to firebase
                    upFiletoFirebase("images", path);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                Log.e("picture address", "---" + uri + "---" + path);
            }
        } else if (requestCode == FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = intent.getData();
                String path = FileUtils.getFileAbsolutePath(ChatsActivity.this, uri);
                //Upload file to firebase
                if (path == null) {
                    ToastUtils.showToast("The file format is not correct.");
                } else {
                    upFiletoFirebase("files", path);
                }
                Log.e("file address", "---" + uri + "---" + path);
            }
        }
    }

    /**
     * Get the chat log information
     */
    private void initMessageData() {
        conversation_id = getIntent().getStringExtra("conversation_id");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().getRef().child("conversations");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showlistmessages.clear();
                listmessage.clear();
                Object listmessages = dataSnapshot.child(conversation_id).child("list_message").getValue();
                Object members = dataSnapshot.child(conversation_id).child("members").getValue();
                Map<String, Object> listmessagesMap = (HashMap<String, Object>) listmessages;
                HashMap<String, Object> membersMap = (HashMap<String, Object>) members;
                if (listmessages != null) {
                    listmessage.addAll(AscKeyComparator.AscMap(listmessagesMap));
                }
                if (membersMap != null) {
                    membersBean.setRequester((String) membersMap.get(Constants.REQUESTER));
                    membersBean.setService_provider((String) membersMap.get(Constants.SERVICE_PROVIDER));
                }
                if (listmessage.size() > 10) {
                    if (listmessage.size() - index * 10 > 0) {
                        showlistmessages.addAll(listmessage.subList(listmessage.size() - index * 10, listmessage.size()));
                    }
                } else {
                    showlistmessages.addAll(listmessage);
                }
                if (showlistmessages.size() != 0) {
                    mAdapter.notifyDataSetChanged();
                    mRecyclerviewChat.smoothScrollToPosition(mAdapter.getTotalCount());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                L.e(TAG, "onCancelled" + databaseError.getMessage());
            }
        });
    }


    /**
     * Set the content of the title bar
     */
    private void setChatActionBar() {
        username = getIntent().getStringExtra("username");
        opposite_user = getIntent().getStringExtra("opposite_user");

        String request_category = getIntent().getStringExtra("request_category");
        String request_id = getIntent().getStringExtra("request_id");
        bar = ActionBar.init(actionBar);
        bar.setLeft(R.mipmap.back, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        bar.setLeft2(R.mipmap.user);
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
        if (request_category.contains("#")) {
            tvCategory.setText( request_category);
        } else {
            tvCategory.setText("#" + request_id + " "+ request_category);
        }

    }


    @OnClick({R.id.iv_notify, R.id.btn_up, R.id.btn_send, R.id.bt_requestdetails})
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

    //get list data
    private void getViewRequest() {
        String request_id = getIntent().getStringExtra("request_id");
        date_created = getIntent().getStringExtra("date_created");

        Call<ViewRequestBean> call = RetrofitUtil.getAPIService().getQuestions(request_id);
        call.enqueue(new CustomerCallBack<ViewRequestBean>() {
            @Override
            public void onResponseResult(ViewRequestBean response) {
                dismissLoadingView();
                List<ExploreQuestionListBean> data = response.getData();
                showPoupView(data);

            }

            @Override
            public void onResponseError(BaseBean errorMessage, boolean isNetError) {
                dismissLoadingView();
            }

        });
    }

    private void showPoupView(List<ExploreQuestionListBean> data) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.chat_detail_popupview, null);
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(rlChatall);
            TextView tv_requests = (TextView) view.findViewById(R.id.tv_requests);
            TextView tv_date_created = (TextView)  view.findViewById(R.id.tv_date_created);
            TextView tv_user = (TextView)  view.findViewById(R.id.tv_user);
            LinearLayout ly_listquest = (LinearLayout) view.findViewById(R.id.ly_listquest);
//            RecyclerView chat_request_recyclerviews = (RecyclerView)  view.findViewById(R.id.chat_request_recyclerview);
            String opposite_user = getIntent().getStringExtra("opposite_user");
            String request_category = getIntent().getStringExtra("request_category");
//            List<ViewRequestBean.DataBean.QuestionsBean> questions = data.getQuestions();
//            tv_requests.setText(data.getRequest());
            tv_date_created.setText(date_created);
            if (opposite_user != null) {
                tv_user.setText(opposite_user);
            } else {
                tv_user.setText("Test");
            }
            if (request_category != null) {
                tv_requests.setText(request_category);
            } else {
                tv_requests.setText("Test");
            }

//            L.e(TAG, "---------" + questions.size() + "---" + questions);

//            ChatRequestsAdapter groupAdapter = new ChatRequestsAdapter(this, questions);
//            chat_request_recyclerviews.setAdapter(groupAdapter);
//            chat_request_recyclerviews.setLayoutManager(new LinearLayoutManager(ChatsActivity.this, LinearLayoutManager.VERTICAL, false));
//            chat_request_recyclerviews.setItemAnimator(new DefaultItemAnimator());
//            chat_request_recyclerviews.setHasFixedSize(true);
            for (ExploreQuestionListBean reqeustData : data) {
                if (reqeustData.getForm_type().equals("file")) {
                    String value = reqeustData.getValue();
                    if (value.equals("false"))
                        continue;
                    Gson gson = new Gson();
                    List<AttachmentFileBean> attachmentFileList = gson.fromJson(value, new TypeToken<List<AttachmentFileBean>>() {
                    }.getType());
                    for (final AttachmentFileBean attachmentfile : attachmentFileList) {
                        if (attachmentfile.getFile_type().equals("image/png") || attachmentfile.getFile_type().equals("image/jpeg")) {
                            View pdfView = mlflater.inflate(R.layout.detailimagelayout, null);
                            LinearLayout ly_iamge= (LinearLayout)  pdfView.findViewById(R.id.ly_image);
                            ImageView imageView =new ImageView(getContext());
                            ly_iamge.addView(imageView);
                            ly_listquest.addView(pdfView);
                            Glide.with(getContext()).load(attachmentfile.getFile_path())
                                    .placeholder(R.mipmap.loading)
                                    .error(R.mipmap.icon_close)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .override(300,100)
                                    .into(imageView);

                        } else if (attachmentfile.getFile_type().equals("application/pdf")) {
                            View pdfView = mlflater.inflate(R.layout.detailpdflayout, null);
                            TextView tv_fileName = pdfView.findViewById(R.id.tv_title);
                            tv_fileName.setText(attachmentfile.getFile_name());
                            ly_listquest.addView(pdfView);
                            ImageView downPfd = pdfView.findViewById(R.id.download);
                            downPfd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(attachmentfile.getFile_path())));
                                }
                            });

                        } else if (attachmentfile.getFile_type().equals("audio/mp3")||attachmentfile.getFile_type().equals("audio/wav")
                                ||attachmentfile.getFile_type().equals("application/octet-stream")) {
//                            View voiceView = mlflater.inflate(R.layout.chat_detailvoicelayout, null);
////                            AudioImageActivity audioObject =new AudioImageActivity(voiceView,attachmentfile.getFile_path(),ChatsActivity.this);
//                            TextView tv_tittle = voiceView.findViewById(R.id.tv_title);
//                            tv_tittle.setText(attachmentfile.getFile_name());
//                            ly_listquest.addView(voiceView);

                            View voiceView = mlflater.inflate(R.layout.chat_detailvoicelayout, null);
                            voiceView.setPadding(10, 0, 10, 0);
                            NewAudioImageActivity audioObject = new NewAudioImageActivity(voiceView, attachmentfile.getFile_path(), ChatsActivity.this);
                            TextView tv_tittle = voiceView.findViewById(R.id.tv_title);
                            tv_tittle.setText(attachmentfile.getFile_name());
                            ly_listquest.addView(voiceView);
                        }

                    }


                } else {
                    View requestView = mlflater.inflate(R.layout.detailrequestlayout, null);
                    TextView requestTittle = requestView.findViewById(R.id.tv_requestittle);
                    requestTittle.setText(reqeustData.getQuestion_title());
                    TextView requestAnswer = requestView.findViewById(R.id.tv_requestanswer);
                    requestAnswer.setText(reqeustData.getValue());
                    Log.e("sss--", ly_listquest + "---" + requestView);
                    ly_listquest.addView(requestView);
                }
            }
            // Create an object PopuWidow
            popupWindow = new PopupWindow(view, 800, 1200);
        }

        // Make it together
        popupWindow.setFocusable(true);
        // Settings allow external clicks to disappear
        popupWindow.setOutsideTouchable(true);

        // This is to click "return" to make it disappear, and it will not affect your background.
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // The displayed position is: half the width of the screen - half the height of the PopupWindow
        int xPos = windowManager.getDefaultDisplay().getWidth()/2
                - popupWindow.getWidth()/2;
        Log.i("coder", "xPos:" + xPos);

        popupWindow.showAtLocation(this.getWindow().getDecorView(),Gravity.CENTER, 0,0);

    }


    /**
     * Request related permissions
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
                        // Determine if the user has clicked and no longer reminds. (Check if the permission is still available)
                        Toast.makeText(this, "authority authorization failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "permission to succeed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void updateMessage(String message, String type) {
        //ChatMessageBean chatMessageBean = new ChatMessageBean();
        ChatMessagesBean.MessageBean messageBean = new ChatMessagesBean.MessageBean();
        //ChatMessageBean.MembersBean membersBeans = new ChatMessageBean.MembersBean();
        Map<String, String> timestamps = ServerValue.TIMESTAMP;
        String timestamp = timestamps.get(".sv");
        messageBean.setCreatedAt(timestamps);
        messageBean.setMessage(message);
        messageBean.setSenderId(membersBean.getRequester());
        messageBean.setSenderName(username);
        messageBean.setType(type);
//        showlistmessages.add(messageBean);
//        mAdapter.notifyDataSetChanged();
        //mRecyclerviewChat.smoothScrollToPosition(mAdapter.getTotalCount());
        //chatMessageBean.setList_message(showlistmessages);

        //Add send information to firebase backend database
        // TODO: 2018/5/10
        Log.e("sss", messageBean.toMap() + "--" + timestamps + "--" + new Date().getTime());
        mDatabaseReference.child(conversation_id).child("list_message").push().setValue(messageBean);

//        var user = firebase.auth().currentUser;
//
//        firebase.database().ref(‘conversations/’ + conversation_id + ‘/list_message’).set({
//                createdAt: firebase.database.ServerValue.TIMESTAMP,
//                senderId: user.uid,
//                message: “HELLO”
//        type : “text”
//        senderName: “LUQMAN PAISAL”
//        });
    }

    /**
     * Open select local file
     */
    private void openFileMessage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//Set the type, I am here any type, any suffix can be written like this.
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    /**
     * Open select local image
     */
    private void openImageMessage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }

    /**
     * Upload images or files to firebase storage
     */
    private void upFiletoFirebase(final String classes, String uri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
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
        final StorageReference riversRef = storageRef.child(classes + "/" + conversation_id + "/" +file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("---", exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                StorageMetadata metadata1 = taskSnapshot.getMetadata();
            }
        });
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    fileUri = downloadUri;
                    L.e("File upload success address", "--" + downloadUri);
                    if ("files".equals(classes)) {
                        updateMessage(fileUri + "", "file");
                    } else {
                        updateMessage(fileUri + "", "image");
                    }
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
//        // Upload file and metadata to the path 'images/mountains.jpg'
//        UploadTask uploadTask = storageRef.child(classes + "/" + conversation_id + "/" + file.getLastPathSegment()).putFile(file, metadata);
//        // Listen for state changes, errors, and completion of the upload.
//
//        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//                System.out.println("Upload is " + progress + "% done");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // Handle successful uploads on complete
////                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
//                StorageMetadata metadata1 = taskSnapshot.getMetadata();
////                fileUri = downloadUrl;
////                L.e("文件上传成功地址", "--" + downloadUrl);
////                if ("files".equals(classes)) {
////                    updateMessage(fileUri + "", "file");
////                } else {
////                    updateMessage(fileUri + "", "image");
////                }
//
//            }
//        });

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
