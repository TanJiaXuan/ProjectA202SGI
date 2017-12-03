package com.example.jooxuan.shoplist;
/**
 * Created by JooXuan on 12/1/2017.
 */

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int DELETE = 1;

    EditText nameTxt, quantityTxt, remarkTxt;
    ImageView shopImgView;
    List<Shop> Shops = new ArrayList<Shop>();
    ListView shopListView;
    Uri imageUri = Uri.parse("android.resource://com.example.jooxuan.shoplist/drawable/insert.jpg");
    DatabaseHandler dbHandler;
    int longClickedIndex;
    ArrayAdapter<Shop> shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt = (EditText) findViewById(R.id.txtName);
        quantityTxt =(EditText) findViewById(R.id.txtQuantity);
        remarkTxt = (EditText) findViewById(R.id.txtRemark);
        shopListView =(ListView) findViewById(R.id.listView);
        shopImgView = (ImageView)findViewById(R.id.imgViewShop);
        dbHandler = new DatabaseHandler(getApplicationContext());

        registerForContextMenu(shopListView);
        shopListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedIndex = position;
                return false;
            }
        });

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec= tabHost.newTabSpec("create");
        tabSpec.setContent(R.id.tabCreate);
        tabSpec.setIndicator("Create");
        tabHost.addTab(tabSpec);

        tabSpec= tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabShopList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);

        final Button addBtn = (Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shop shop = new Shop(dbHandler.getShopsCount(), String.valueOf(nameTxt.getText()), String.valueOf(quantityTxt.getText()), String.valueOf(remarkTxt.getText()), imageUri);
                if(!shopExists(shop)) {
                    dbHandler.createShop(shop);
                    Shops.add(shop);
                    shopAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText())+ " has been added into list!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), String.valueOf(nameTxt.getText()) + " already exists. Please use another name", Toast.LENGTH_SHORT).show();
            }
        });

        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBtn.setEnabled(String.valueOf(nameTxt.getText()).trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        shopImgView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
            }
        });

        if(dbHandler.getShopsCount() != 0)
             Shops.addAll(dbHandler.getAllShops());

        populateList();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle("Choose Options");
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete List");
    }

    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId() == DELETE){
            dbHandler.deleteShop(Shops.get(longClickedIndex));
            Shops.remove(longClickedIndex);
            shopAdapter.notifyDataSetChanged();
        }

        return super.onContextItemSelected(item);
    }

    private boolean shopExists(Shop shop){
        String name = shop.getName();
        int shopCount = Shops.size();

        for(int i = 0; i < shopCount; i++){
            if(name.compareToIgnoreCase(Shops.get(i).getName()) == 0)
                return true;
        }
        return false;
    }

    public void onActivityResult(int reqCode, int resCode, Intent data){
        if(resCode == RESULT_OK){
            if(reqCode == 1) {
                imageUri = data.getData();
                shopImgView.setImageURI(data.getData());
            }
        }
    }

    private void populateList(){
        shopAdapter = new ShopListAdapter();
        shopListView.setAdapter(shopAdapter);
    }

    private class ShopListAdapter extends ArrayAdapter<Shop> {
        public ShopListAdapter(){
            super (MainActivity.this,R.layout.listview_item, Shops);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent){
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent,false);

            Shop currentShop = Shops.get(position);

            TextView name = (TextView) view.findViewById(R.id.ItemName);
            name.setText(currentShop.getName());
            TextView quantity = (TextView) view.findViewById(R.id.ItemQuantity);
            quantity.setText(currentShop.getQuantity());
            TextView remark = (TextView) view.findViewById(R.id.ItemRemark);
            remark.setText(currentShop.getRemark());
            ImageView imgListShop = (ImageView) view.findViewById(R.id.imgListContact);
            imgListShop.setImageURI(currentShop.getImg());

            return view;
        }
    }
}
