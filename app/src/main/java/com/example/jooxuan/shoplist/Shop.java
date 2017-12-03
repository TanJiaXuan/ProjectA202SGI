package com.example.jooxuan.shoplist;
/**
 * Created by JooXuan on 12/1/2017.
 */
import android.net.Uri;


public class Shop {

    private String _name, _quantity, _remark;
    private Uri _img;
    private int _id;

    public Shop(int id, String name, String quantity, String remark, Uri img) {

        _id = id;
        _name = name;
        _quantity = quantity;
        _remark = remark;
        _img = img;
    }

    public String getName() {
        return _name;
    }

    public String getQuantity() {
        return _quantity;
    }

    public String getRemark() {
        return _remark;
    }

    public Uri getImg() {return _img;}

    public int getId() {
        return _id;
    }
}
