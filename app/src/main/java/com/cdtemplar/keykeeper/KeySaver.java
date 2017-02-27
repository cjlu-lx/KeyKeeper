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

import android.widget.TextView;

import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lx on 2017-02-21.
 */

public class KeySaver {
    public String KeyName;
    private List<Key> KeyList;
    public KeySaver(String strName)
    {
        KeyList = new LinkedList<Key>();
        KeyName = strName;
        //KeyList.add(new Key("Name","1234"));
    }
    public void NewKey(String strName, String strPwEn)
    {
        for (Key k : KeyList) {
            if(k.strKeyName.equals(strName))
            {
                k.strKeyM = strPwEn;
                return;
            }
        }
        Key k = new Key(strName,strPwEn);
        KeyList.add(k);
    }
    public  List<Key> getAllKeys() {
        return  KeyList;
    }
    public String getGsonString(){
        Gson gson = new Gson();
        String str = gson.toJson(this);
        return str;
    }

}
