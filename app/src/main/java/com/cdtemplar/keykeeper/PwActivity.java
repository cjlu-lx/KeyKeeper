// The MIT License (MIT)
// Copyright (C) 2016 by Lixiong <lx@cdtemplar.com>,http://www.cdtemplar.com
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

package com.cdtemplar.keykeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

public class PwActivity extends AppCompatActivity {

    ContactsFile mContactsFile = null;
    String Key = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pw);

        Key = getIntent().getStringExtra("Key");
        mContactsFile = new ContactsFile(
                getIntent().getIntExtra("ID",-1),
                getIntent().getStringExtra("Name"),
                getIntent().getStringExtra("Note")
        );

        UpdateListView();

        final ListView listView = (ListView) findViewById(R.id.key_listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Key key = (Key) listView.getAdapter().getItem(arg2);

                TextView txtName = (TextView) findViewById(R.id.txtName);
                TextView txtPw = (TextView) findViewById(R.id.txtPw);
                txtName.setText(key.strKeyName);
                txtPw.setText(Common.Decrypt(key.strKeyM,Key));

                //Toast.makeText(getApplicationContext(), "arg0:" + listView.getAdapter().getItem(arg2).toString(),Toast.LENGTH_SHORT).show();
            }
        });
        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBtnClick();
            }
        });
    }// onCreate
    private void OnBtnClick(){
        if(mContactsFile == null)
            return;
        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtPw = (TextView) findViewById(R.id.txtPw);
        if(txtName.getText().toString().equals("") || txtPw.getText().toString().equals(""))
        {
            Common.Message(this,"请输入名称和密码!");
            return;
        }
        KeySaver keySaver = getKeySaver();
        keySaver.NewKey(txtName.getText().toString(), Common.Encrypt(txtPw.getText().toString(),Key));
        mContactsFile.strNote = (new Gson()).toJson(keySaver);
        UpdateListView();
        if( PhoneContacts.Update(mContactsFile.nID,mContactsFile.strNote) > 0 )
            Common.Message(this,"密码已保存!");
    }
    protected  KeySaver getKeySaver()
    {
        if(mContactsFile != null){
            KeySaver keySaver = (new Gson()).fromJson(mContactsFile.strNote,KeySaver.class);
            return keySaver;
        }
        return null;
    }
    protected  void UpdateListView()
    {
        if(mContactsFile != null) {
            ListView listView = (ListView) findViewById(R.id.key_listView);
            KeyAdapter ka = new KeyAdapter(this, R.layout.list_item, getKeySaver().getAllKeys());
            listView.setAdapter(ka);
        }

    }
}
