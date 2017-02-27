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

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lx on 2017-02-23.
 */

public class Common {
    public static void Message(Context context,String str)
    {

            Toast.makeText(context,str,Toast.LENGTH_SHORT).show();

    }

    static private String getKey(String strKey)
    {
        String str = strKey + "lixiong@2017abcdefg";
        return str.substring(0,16);
    }

    public static String Encrypt(String str,String strKey)
    {
        try {
            str = Aes.Encrypt(str,getKey(strKey));
            return str;
        }
        catch (Exception ee)
        {

        }
        return "";
    }
    public static String Decrypt(String str,String strKey)
    {
        try {
            str = Aes.Decrypt(str,getKey(strKey));
            if(str == null)
                return "";
            return  str;
        }
        catch (Exception ee)
        {

        }
        return "";
    }
}
