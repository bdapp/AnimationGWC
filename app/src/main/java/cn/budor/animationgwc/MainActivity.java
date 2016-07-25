package cn.budor.animationgwc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private ListView mList;
    private TextView numText;
    private ImageView bottomImg;

    private int[] imgList = new int[]{R.mipmap.ic_p1, R.mipmap.ic_p2,
            R.mipmap.ic_p3, R.mipmap.ic_p4, R.mipmap.ic_p5, R.mipmap.ic_p6 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        numText = (TextView) findViewById(R.id.main_num);
        bottomImg = (ImageView) findViewById(R.id.main_img);

        mList = (ListView) findViewById(R.id.main_list);

        ItemAdapter adapter = new ItemAdapter(mContext, imgList, bottomImg, numText);
        mList.setAdapter(adapter);

    }


}
