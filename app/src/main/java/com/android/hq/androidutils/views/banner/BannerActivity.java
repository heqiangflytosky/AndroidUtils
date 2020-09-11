package com.android.hq.androidutils.views.banner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.android.hq.androidutils.R;

public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        SparseArray list = new SparseArray();
        list.append(0,"https://ae01.alicdn.com/kf/Uec00959acd9c4d0aa900d5fb8ea481931.jpg");
        list.append(1,"https://ae01.alicdn.com/kf/Ue16c54cac6574a06a0c1afdad979b007W.jpg");
        list.append(2,"https://ae01.alicdn.com/kf/U9a21a2f4b83c4030b87c840bc07105e5A.jpg");
        //list.append(3,"https://ae01.alicdn.com/kf/U76a18e0d315e407a8daf3d91de033e31i.jpg");
        //list.append(4,"https://ae01.alicdn.com/kf/U8f29046315a345b488a91f19e0691d7bx.jpg");
        //list.append(5,"https://ae01.alicdn.com/kf/U16c8b999987a4671bf872d061cb63100y.jpg");
        //list.append(6,"https://ae01.alicdn.com/kf/U263dd43ef1254f73b0dddac9562dc9bcB.jpg");

        BannerView bannerView1 = findViewById(R.id.banner1);
        bannerView1.setData(list);
        bannerView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(BannerActivity.this, "position = "+position, Toast.LENGTH_SHORT).show();
            }
        });

        BannerView bannerView2 = findViewById(R.id.banner2);
        bannerView2.setData(list);
        bannerView2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(BannerActivity.this, "position = "+position, Toast.LENGTH_SHORT).show();
            }
        });

        BannerView bannerView3 = findViewById(R.id.banner3);
        bannerView3.setData(list);
        bannerView3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(BannerActivity.this, "position = "+position, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
