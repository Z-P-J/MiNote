package com.zpj.minote.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文字变色工具类
 */
public class KeywordUtil {

    /**
     * 关键字高亮变色
     *
     * @param color   变化的色值
     * @param text    文字
     * @param keyword 文字中的关键字
     * @return 结果SpannableString
     */
    public static SpannableString hightlight(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        keyword = escapeExprSpecialWord(keyword);
        text = escapeExprSpecialWord(text);
        if (text.contains(keyword) && !TextUtils.isEmpty(keyword)) {
            try {
                Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(text);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /**
     * 使用indexOf来获取高亮关键字的位置
     * 使用正则表达式的方法有bug
     * @param color 高亮颜色
     * @param text 文本
     * @param keyword 高亮关键字
     * @return SpannableString
     */
    public static SpannableString hightlightKeyword(int color, String text, String keyword) {
        SpannableString str = new SpannableString(text);
        text = text.toLowerCase();
        keyword = keyword.toLowerCase();
        int len = keyword.length();
        int index = -1;
        do {
            index = text.indexOf(keyword, index < 0 ? 0 : index + len);
            if (index >= 0) {
                str.setSpan(new ForegroundColorSpan(Color.RED), index, index + len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } while (index >= 0);
        return str;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return keyword
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
