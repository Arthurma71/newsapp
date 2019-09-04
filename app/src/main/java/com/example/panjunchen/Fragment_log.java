
package com.example.panjunchen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.ResourceLoader;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.panjunchen.models.NewsAccount;
import com.example.panjunchen.models.TableOperate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

public class Fragment_log extends Fragment {
    private TextView username;
    private SuperTextView changeImage_button;
    private NewsAccount currentAccount;
    private ImageView head;
    private SuperTextView history_button;
    private SuperTextView changePassword_button;
    private SuperTextView clearhistory_button;
    private SuperTextView login_button;

    public Fragment_log(){
        super();
    }

    void updateAccount(){
        if(currentAccount.getUsername().equals("未登录"))login_button.setCenterString("登录账户");
        else login_button.setCenterString("退出登录");
        username.setText(currentAccount.getUsername());
        if(!currentAccount.getImageURL().equals("")){
            try{
                head.setImageBitmap(getBitmapFormUri(getActivity(),new File(currentAccount.getImageURL()).toURL()));}
            catch (Exception e){
                Log.d("updateAccount","imagefail");
                e.printStackTrace();
            }
        }
        else head.setImageDrawable(getResources().getDrawable(R.drawable.defalthead));
    }

    public static Bitmap getBitmapFormUri(Activity ac, URL url) throws FileNotFoundException, IOException {
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(url.openStream(), null, onlyBoundsOptions);
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 120f;//这里设置高度为800f
        float ww = 120f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        Bitmap bitmap = BitmapFactory.decodeStream(url.openStream(), null, bitmapOptions);

        return compressImage(bitmap);//再进行质量压缩
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static void copyFileByStream(File source, File dest) throws
            IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest);){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    private String getRealPath(Uri fileUrl) {
        String fileName = null;
        if( fileUrl != null ) {
            if( fileUrl.getScheme( ).toString( ).compareTo( "content" ) == 0 ) // content://开头的uri
            {
                Cursor cursor = getActivity().getContentResolver( ).query( fileUrl, null, null, null, null );
                if( cursor != null && cursor.moveToFirst( ) ) {
                    try {
                        int column_index = cursor.getColumnIndexOrThrow( MediaStore.Images.Media.DATA );
                        fileName = cursor.getString( column_index ); // 取出文件路径
                    } catch( IllegalArgumentException e ) {
                        e.printStackTrace();
                    }finally{
                        cursor.close( );
                    }
                }
            } else if( fileUrl.getScheme( ).compareTo( "file" ) == 0 ) // file:///开头的uri
            {
                fileName = fileUrl.getPath( );
            }
        }
        return fileName;
    }

    public static String getFileName(String filePath ) {
        if (filePath == null) return null;
        String fileName = null;
        int cut = filePath.lastIndexOf('/');
        if (cut != -1) {
            fileName = filePath.substring(cut + 1);
        }
        return fileName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.login,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        currentAccount = TableOperate.getCurrentNewsAccount();

        changeImage_button = getActivity().findViewById(R.id.changeImage_button);
        clearhistory_button = getActivity().findViewById(R.id.clearHistory_button);
        history_button = getActivity().findViewById(R.id.history_button);
        username = getActivity().findViewById(R.id.user_name);
        head = getActivity().findViewById(R.id.h_head);
        login_button = getActivity().findViewById(R.id.login_button);

        updateAccount();

        changePassword_button = getActivity().findViewById(R.id.changePassword_button);

        changePassword_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentAccount.getUsername().equals("未登录")){
                    Intent intent = new Intent(getContext(),ChangeActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast toast=Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        changeImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!currentAccount.getUsername().equals("未登录"))checkPermission();
                else{
                    Toast toast=Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login_button.getCenterString().equals("登录账户")){
                    Intent intent = new Intent(getContext(),LoginActivity.class);
                    startActivityForResult(intent,1);
                }
                else {
                    TableOperate.getInstance().reNewAccount(currentAccount);
                    currentAccount = new NewsAccount("未登录","","");
                    updateAccount();
                    TableOperate.getInstance().updateLocalAccount(currentAccount);
                    Toast toast=Toast.makeText(getContext(),"退出成功",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        clearhistory_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TableOperate.getInstance().clearHistory();
            }
        });
    }

    //检查权限
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            //发现没有权限，调用requestPermissions方法向用户申请权限，requestPermissions接收三个参数，第一个是context，第二个是一个String数组，我们把要申请的权限
            //名放在数组中即可，第三个是请求码，只要是唯一值就行
            openAlbum();
        } else {
            openAlbum();//有权限就打开相册
        }
    }

    public void openAlbum() {
        //通过intent打开相册，使用startactivityForResult方法启动actvity，会返回到onActivityResult方法，所以我们还得复写onActivityResult方法
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 345);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 345:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    //通过uri的方式返回，部分手机uri可能为空
                    if(uri != null){
                        Log.d("imageCheck",getRealPath(uri));
                        File src = new File(getRealPath(uri));
                        File folder = new File(TableOperate.savePath+File.separator+"Image");
                        if(!folder.exists())folder.mkdirs();
                        File dest = new File(TableOperate.savePath+File.separator+"Image"+File.separator+getFileName(getRealPath(uri)));
                        try{
                            copyFileByStream(src,dest);
                        }catch (Exception e){
                            Log.d("imageCheck","copyFail");
                        }
                        currentAccount.setImageURL(TableOperate.savePath+File.separator+"Image"+File.separator+getFileName(getRealPath(uri)));
                        updateAccount();
                        TableOperate.getInstance().updateLocalAccount(currentAccount);
                    }
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    currentAccount = TableOperate.getCurrentNewsAccount();
                    updateAccount();
                }
                break;
        }
    }
}
