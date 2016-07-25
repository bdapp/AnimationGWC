package cn.budor.animationgwc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ItemAdapter extends BaseAdapter implements ICartView {
    private Context mContext;
    private ICartView cartView;
    private int[] list;

    private ImageView toImg;
    private TextView numText;

    //购物车数量
    private int count = 0;

    public ItemAdapter(Context mContext, int[] list, ImageView bottomImg, TextView numText) {
        this.mContext = mContext;
        this.list = list;
        this.toImg = bottomImg;
        this.numText = numText;

        cartView = this;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder ;
        if (convertView == null){
            holder = new ViewHolder();
             convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pic, null);
            holder.img = (ImageView) convertView.findViewById(R.id.item_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setImageResource(list[position]);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //起始位置图片和坐标
                ImageView fromImg = new ImageView(mContext);
                fromImg.setImageResource(list[position]);
                int[] v_location = new int[2];
                v.getLocationInWindow(v_location);

                // 执行动画
                ICartAnim cartInterface = new CartAnimImpl(mContext, cartView);
                cartInterface.setTranslateAnim(fromImg, toImg, v_location);
            }
        });

        return convertView;
    }

    @Override
    public void setChangeNum() {
        //更新数量显示
        numText.setVisibility(View.VISIBLE);
        numText.setText("" + ++count);
    }


    class ViewHolder {
        private ImageView img;
    }




}
