package storage.sample.sample_storage;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    //firebaseStorage 인스턴스 생성
    //하나의 Storage와 연동되어 있는 경우, getInstance()의 파라미터는 공백으로 두어도 됨
    //하나의 앱이 두개 이상의 Storage와 연동이 되어있 경우, 원하는 저장소의 스킴을 입력
    //getInstance()의 파라미터는 firebase console에서 확인 가능('gs:// ... ')
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://test-image-769b5.appspot.com");

    //생성된 FirebaseStorage를 참조하는 storage 생성
    StorageReference storageRef = storage.getReference();

    //Storage 내부의 images 폴더 안의 image.jpg 파일명을 가리키는 참조 생성
    StorageReference pathReference = storageRef.child("stock/thumb/5.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView iv = (ImageView)findViewById(R.id.iv);
        Button btn = (Button)findViewById(R.id.btn);
        final TextView tv = (TextView)findViewById(R.id.tv);


        btn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        try{
                            File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
                            final File file = new File(dir, "image.jpg");
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            final FileDownloadTask fileDownloadTask = pathReference.getFile(file);
                            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    tv.setText(file.getAbsolutePath());
                                    iv.setImageURI(Uri.fromFile(file));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e("zzz","fail");
                                }
                            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                }
                            });
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
