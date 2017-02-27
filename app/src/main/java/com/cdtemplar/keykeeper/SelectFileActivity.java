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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.ListView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.Query;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

public class SelectFileActivity extends AppCompatActivity {
    private ContactsFile mContactsFile = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);

        Contacts.initialize(this);
        PhoneContacts.initialize(this);

        //NewContactsFile("Test1","1");

        //PhoneContacts.Update(16,"NOTE_UPDATE_OK");

        UpdateFileList();

        final EditText txtKeyInput = (EditText) findViewById(R.id.txtKeyInput);
        txtKeyInput.addTextChangedListener( new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if( mContactsFile != null ) {
                    Button button = (Button) findViewById(R.id.btnOK);
                    EditText txtKeyInput = (EditText) findViewById(R.id.txtKeyInput);
                    KeySaver ks = (new Gson()).fromJson(mContactsFile.strNote,KeySaver.class);
                    if (Common.Decrypt(ks.KeyName,txtKeyInput.getText().toString()).equals("KeyKeeper")){

                        button.setText("OK");
                        button.setEnabled(true);
                    }
                    else {
                        button.setText("请输入密钥");
                        button.setEnabled(false);
                    }
                }
             }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        Button button = (Button) findViewById(R.id.btnOK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) findViewById(R.id.btnOK);
                Intent intent = new Intent(button.getContext(),PwActivity.class);
                // 准备跳转
                EditText txtKeyInput = (EditText) findViewById(R.id.txtKeyInput);
                intent.putExtra("ID",mContactsFile.nID);
                intent.putExtra("Key",txtKeyInput.getText().toString());
                intent.putExtra("Note",mContactsFile.strNote);
                intent.putExtra("Name",mContactsFile.strName);
                txtKeyInput.setText("");
                button.getContext().startActivity(intent);
            }
        });

        Button btnNew = (Button) findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btnOK = (Button) findViewById(R.id.btnOK);
                btnOK.setEnabled(false);
                EditText txtKeyInput = (EditText) findViewById(R.id.txtKeyInput);
                EditText txtFile = (EditText) findViewById(R.id.txtFileName);

                Button btnNew = (Button) findViewById(R.id.btnNew);
                if(btnNew.getText().toString().equals("确定新增！"))
                {

                    if(txtKeyInput.getText().toString().equals("") || txtFile.getText().toString().equals("")){
                        Common.Message(txtKeyInput.getContext(),"请输入名称和密钥！");
                    }
                    else
                    {
                        NewContactsFile(txtFile.getText().toString(),txtKeyInput.getText().toString());
                        btnNew.setText("新文件");
                        btnOK.setEnabled(true);
                        txtFile.setEnabled(false);
                    }
                }
                else{
                    btnNew.setText("确定新增！");
                    txtFile.setEnabled(true);
                }
            }
        });
    }   //onCreate
    private void NewContactsFile(String strName,String strKey){
        KeySaver keySaver = new KeySaver(Common.Encrypt("KeyKeeper",strKey));
        PhoneContacts.AddContacts("KeyKeeper-" + strName,keySaver.getGsonString());
    }
    private void UpdateFileList()
    {
        Query q = Contacts.getQuery();
        q.whereStartsWith(Contact.Field.DisplayName, "KeyKeeper-");
        List<Contact> contacts = q.find();

        LinkedList<ContactsFile> listCF = new LinkedList<ContactsFile>();
        for(Contact c : contacts){
            String str = c.getDisplayName();
            str = str.substring(10);

            listCF.add(new ContactsFile(c.getID(),str,c.getNote()));
        }
        ListView listView = (ListView) findViewById(R.id.list_file);
        ContactsFileAdapter ka = new ContactsFileAdapter(this, R.layout.list_item, listCF );
        listView.setAdapter(ka);

        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                final ListView listView = (ListView) findViewById(R.id.list_file);
                ContactsFile contactsFile = (ContactsFile) listView.getAdapter().getItem(arg2);

                EditText txtName = (EditText) findViewById(R.id.txtFileName);
                EditText txtKey = (EditText) findViewById(R.id.txtKeyInput);
                txtName.setText(contactsFile.strName);
                txtKey.setText("");

                mContactsFile = contactsFile;
                //Toast.makeText(getApplicationContext(), "arg0:" + listView.getAdapter().getItem(arg2).toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
