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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import static android.provider.ContactsContract.Contacts.Entity.RAW_CONTACT_ID;

/**
 * Created by lx on 2017-02-22.
 */

public class PhoneContacts {

    public static Context mContext;
    public static void initialize(Context c){
        mContext = c;
    }
    public static void  AddContacts(String strName,String strNote){
        //插入raw_contacts表，并获取_id属性
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));
        //插入data表
        uri = Uri.parse("content://com.android.contacts/data");
        //add Name
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        //values.put("data2", "zdong");
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strName);
        resolver.insert(uri, values);
        values.clear();

        //add Phone
        /*
        values.put(ContactsContract.Contacts.Entity.RAW_CONTACT_ID, contact_id);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);//"vnd.android.cursor.item/phone_v2");
        //values.put(ContactsContract.CommonDataKinds.Phone.TYPE, "2");   //手机
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        values.put(ContactsContract.CommonDataKinds.Phone.DATA, "88888888-88888888");
        resolver.insert(uri, values);
        values.clear();
        */


        //add Phone
        values.put(ContactsContract.Contacts.Entity.RAW_CONTACT_ID, contact_id);
        values.put(ContactsContract.Contacts.Data.MIMETYPE,ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Note.NOTE, strNote);
        resolver.insert(uri, values);
        values.clear();
    }

    public static int Update(int id,String strNote){
        Uri uri = Uri.parse("content://com.android.contacts/data");//对data表的所有数据操作
        ContentResolver resolver = mContext.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ContactsContract.CommonDataKinds.Note.NOTE, strNote);
        int nn = resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,id+""});
        return  nn;
    }
    public static boolean insert(Context mContext,String given_name, String mobile_number,
                          String work_email, String im_qq) {
        try {
            ContentValues values = new ContentValues();
            ContentResolver resolver = mContext.getContentResolver();

            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = resolver.insert(
                    ContactsContract.RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            if (given_name != "") {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, given_name);
                resolver.insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入电话数据
            if (mobile_number != "") {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, mobile_number);
                values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                resolver.insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入Email数据
            if (work_email != "") {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Email.DATA, work_email);
                values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
                resolver.insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入QQ数据
            if (im_qq != "") {
                values.clear();
                values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                values.put(ContactsContract.CommonDataKinds.Im.DATA, im_qq);
                values.put(ContactsContract.CommonDataKinds.Im.PROTOCOL, ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ);
                resolver.insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }
         }

        catch (Exception e) {
            return false;
        }
        return true;
    }

}
